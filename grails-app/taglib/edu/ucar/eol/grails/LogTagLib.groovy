package edu.ucar.eol.grails

/**
 * Tag to write log messages from views.
 *
 * adapted and updated from http://www.dilino.org/blog/?p=65
 *
 * @author John Allison
 */
class LogTagLib {

  /**
   * Write a log message.
   *
   * Example:
   * <code>&lt;g:logMsg level="info" view=&quot;${this.getGroovyPageFileName()}&quot;&gt;Hello from main!&lt;/g:logMsg&gt;</code>
   * results in something like:
   * <code>2014-10-17 17:21:54,208 [http-bio-8443-exec-7] INFO taglib.LogTagLib  - layouts/main.gsp : Hello from main!</code>
   *
   * Example:
   * <code>&lt;g:logMsg&gt;Hello from main!&lt;/g:logMsg&gt;</code>
   * results in something like:
   * <code>2014-10-17 17:21:54,208 [http-bio-8443-exec-7] DEBUG taglib.LogTagLib  - Hello from main!</code>
   *
   * @attr level Log level. Defaults to debug.
   * @attr view The view name. Used as a prefix to the log message.
   *  Send <code>${this.getGroovyPageFileName()}</code> since taglibs can't get the view name.
   */
  def logMsg = { attrs, body ->
    def levelStr = attrs['level']?:'debug'
    levelStr = levelStr.toString().toLowerCase()

    if (log."is${levelStr.capitalize()}Enabled"()) {
      def viewStr = ''
      if (attrs['view']) {
        viewStr = attrs['view'].toString().replaceFirst('^.*grails-app/views/','') + ' : '
      }

      log."$levelStr"(viewStr + body())
    }
  }

}
