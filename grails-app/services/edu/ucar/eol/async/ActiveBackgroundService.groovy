package edu.ucar.eol.async

import grails.transaction.Transactional
import groovyx.gpars.activeobject.ActiveObject
import groovyx.gpars.activeobject.ActiveMethod
import groovyx.gpars.dataflow.DataflowQueue
import java.util.concurrent.atomic.AtomicBoolean
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsHttpSession
import org.codehaus.groovy.grails.web.servlet.GrailsFlashScope

@ActiveObject
@Transactional
class ActiveBackgroundService {

    def grailsApplication

    private static final AtomicBoolean lock = new AtomicBoolean()

    /**
     * Start a specificied method of a service in the background,
     * via startProcess(), but also sets up the session and flash
     * objects for the controller.
     *
     * Add "promise" and "promiseMessage" to the session,
     * and an initial status (or error) message to flash.message.
     *
     * @param session GrailsHttpSession from a Grails controller
     * @param flash GrailsFlashScope from a Grails controller
     * @param serviceName a Spring bean in the current application context
     * @param methodName the method to start in the background, must take 2 arguments:
     *                   "data" (passed from this method), and an "out" object
     *                   which can receive String messages via the "<<" operator
     * @param data optional object to be passed as the first argument to the
     *             actual service method (parameter Map is suggested)
     * @param sb optional StringBuffer that can be set with periodic status
     *           update messages, will be passed as second argument to the
     *           actual service method
     * @param useLock boolean whether to use a global (multi-process) lock,
     *                which means only one background process at a time for the app
     * @return a Promise for the result;
     *         the final result value will be the final status message String
     * @throws Throwable if some asynchronous/Gpars exception occurs,
     *                   or a propogated RuntimeException from the service method
     */
    void startProcessWithSession(
            GrailsHttpSession session, GrailsFlashScope flash,
            String serviceName, String methodName,
            data=null,
            boolean useLock=true
            )
    {
        // lock will be consulted by startProcess()
        if (session.promise) {
            flash.message = 'A background task is already running (or not resolved); not starting another.'
            flash.messageType = 'warning'
            return
        }
        StringBuffer sb = new StringBuffer(1024)
        def p = startProcess(serviceName, methodName, data, sb, useLock)
        if (p) {
          session.promiseMessage = sb
          session.promise = p
          flash.message = "Started background task ${serviceName}.$methodName"
          flash.messageType = 'info'
        } else {
          log.debug "got null promise, sb=${sb}"
          flash.message = sb.toString()
          if (flash.message.blank) flash.message = 'Unknown error when starting update.'
          flash.messageType = 'error'
        }
    }

    /**
     * Start a specificied method of a service in the background,
     * returning a Gpars Promise for it's results (an informative message String).
     * This is a lightweight wrapper that makes any Grails bean into a
     * background process.
     *
     * An optional given StringBuffer is passed through to the called method
     * (wrapped by a DataflowQueue), which allows for ongoing status updates
     * to the user. The StringBuffer can be stored in the Grails session
     * and reported via the main layout as the user continues to interact with the app.
     *
     * By default, a lock is used to allow only one background process to run
     * for the whole application. If the target method throws an exception,
     * the lock will NOT be unlocked. This allows for an admin to investigate,
     * and then use the unlock() method.
     *
     * If you pass a GORM domain object to the method, and use any of its lazily-initialized
     * child objects, you will eventually start getting Hibernate exceptions like
     * "LazyInitializationException: could not initialize proxy - no Session" or
     * "org.hibernate.SessionException: Session is closed". Wrap all your accesses
     * in a new session: "myObject.withNewSession { session -> ... }"
     *
     * @param serviceName a Spring bean in the current application context
     * @param methodName the method to start in the background, must take 2 arguments:
     *                   "data" (passed from this method), and an "out" object
     *                   which can receive String messages via the "<<" operator
     * @param data optional object to be passed as the first argument to the
     *             actual service method (parameter Map is suggested)
     * @param sb optional StringBuffer that can be set with periodic status
     *           update messages, will be passed as second argument to the
     *           actual service method
     * @param useLock boolean whether to use a global (multi-process) lock,
     *                which means only one background process at a time for the app
     * @return a Promise for the result;
     *         the final result value will be the final status message String
     * @throws Throwable if some asynchronous/Gpars exception occurs,
     *                   or a propogated RuntimeException from the service method
     */
    def startProcess(String serviceName, String methodName,
        data=null,
        StringBuffer sb=null,
        boolean useLock=true
        ) {
        log.info "startProcess($serviceName,$methodName,...) on ${Thread.currentThread()}"

        if (useLock) {
            if (!lock.compareAndSet(false,true)) {
                log.debug "Could not acquire lock(${lock.hashCode()})=${lock} on ${Thread.currentThread()}"
                if (null!=sb) sb.replace(0,sb.length(),'Another background task is already in progress')
                return null // or throw new ...my.AlreadyRunningException OR RuntimeException
            }
            log.debug "got lock(${lock.hashCode()})=${lock} on ${Thread.currentThread()}"
        } else log.debug "NOT locking"

        DataflowQueue dfq = new DataflowQueue()

        try {
            // using the real sb.length() makes 2 method calls which must be synchronized;
            //   a longer length gets reset internally
            // could also synchronize on a StringBuilder and then: sb.setLength(0);sb.append(it.toString())
            if (dfq) dfq.wheneverBound { sb.replace(0,Integer.MAX_VALUE,it.toString()) }

            return startActiveProcess(dfq, serviceName, methodName, data).then {
                if (useLock) {
                    log.debug "unlocking ${lock.hashCode()}"
                    lock.set(false)
                }
                return it
            }
        } catch (e) {
            if (dfq) dfq << 'ERROR'
            log.error "startProcess got exception ${e.class} on thread ${Thread.currentThread()}:\n  ${e.message}"
            log.error e.stackTrace.join('\n')
            throw e
        } finally {
            if (useLock) log.debug "finally NOT unlocking ${lock.hashCode()}"
        }
    }

    /*
     * the actual active method to create a Promise
     */
    @ActiveMethod
    private startActiveProcess(DataflowQueue dfq, String serviceName, String methodName, data) {
        log.info "startActiveProcess on ${Thread.currentThread()}"

        def service = grailsApplication.mainContext.getBean(serviceName)
        if (!service) throw new RuntimeException("Could not get service bean $serviceName")

        if (dfq) dfq << "Starting background process ${serviceName}.${methodName}(...)".toString()

        def args
        if (data || dfq) {
            args = []
            if (data) args << data
            if (dfq) args << dfq
            }

        // let this bomb
        //return service."$methodName"(data,dfq)
        return service.invokeMethod(methodName, args as Object[])
    }

    /**
     * Unlock the shared lock that prevents multiple background processes from running concurrently.
     * Usually not needed, unless an odd exception occurred.
     */
    boolean unlock() {
        lock.set(false)
        lock.get()
    }

    /**
     * Resolve the background task via the saved promise (from the provided session).
     * Session attributes must be setup by startProcessWithSession().
     * Resolution messages will be added to the provide flash.
     *
     * @param session GrailsHttpSession from a Grails controller
     * @param flash GrailsFlashScope from a Grails controller
     */
    void resolve(GrailsHttpSession session, GrailsFlashScope flash) {
      if (session.promise) {
        if (session.promise.bound) {
          try {
            def val = session.promise.get()
            flash.message = "Background task resolved: ${val}\n"
            flash.messageType='info'
          } catch (e) {
            log.error "Async exception ${e.class}: ${e.message}"
            log.error(/stacktrace: ${e.stackTrace.join("\n")}/)
            flash.message = "Background task ERROR:\n\n${e.message}"
            flash.messageType = 'error'
          } finally {
            session.removeAttribute('promise')
            session.removeAttribute('promiseMessage')
          }
        } else {
          flash.message = 'Background task is not ready to resolve.'
          flash.messageType='warning'
        }
      } else {
          flash.message = 'No background task is running.'
          flash.messageType='warning'
      }
    }

}
