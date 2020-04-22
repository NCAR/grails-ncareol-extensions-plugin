package edu.ucar.eol.grails.plugins

import org.codehaus.groovy.runtime.m12n.ExtensionModuleScanner
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * Utility methods for the NcareolExtensionsGrailsPlugin.
 *
 * @author jja@ucar.edu
 */
class NcareolExtensionsUtil {

    /**
     * kludge to get Groovy extension modules loaded in Grails
     *
     * Call this method in the setUp() method of your Grails unit test
     * so your tested classes have the extensions.
     *
     * http://grails.1312388.n4.nabble.com/groovy-extension-module-and-grails-td4642249.html
     * http://jira.grails.org/browse/GRAILS-10652
     * http://stackoverflow.com/questions/19564902/applying-groovy-extensions-in-grails-produces-missingmethodexception-for-string
     */
    static void setupGroovyExtensions() {
        Map<org.codehaus.groovy.reflection.CachedClass, List<MetaMethod>> map = [:]
        ClassLoader classLoader = Thread.currentThread().contextClassLoader
        try {
            Enumeration<URL> resources = classLoader.getResources(
                ExtensionModuleScanner.MODULE_META_INF_FILE
                )
            for (URL url in resources) {
                if (url.path.contains('groovy-all')) {
                    // already registered
                    continue
                }
                Properties properties = new Properties()
                InputStream inStream
                try {
                    inStream = url.openStream()
                    properties.load(inStream)
                    GroovySystem.metaClassRegistry.registerExtensionModuleFromProperties(
                        properties, classLoader, map
                        )
                } catch (IOException e) {
                    throw new GroovyRuntimeException(
                        'Unable to load module META-INF descriptor',
                        e)
                } finally {
                    inStream?.close()
                }
            }
        }  catch (IOException ignored) {}

        map.each { org.codehaus.groovy.reflection.CachedClass cls, List<MetaMethod> methods ->
            cls.addNewMopMethods(methods)
        }
    }

    /**
     * Add a helper method (GrailsParameterMap.bigDecimal) to obtain a BigDecimal from parameters.
     *
     * The optional second argument is the default value and can be any possible single argument
     * to a BigDecimal constructor: BigDecimal, String, int, long, float, or double.
     *
     * Usage: BigDecimal pi = params.bigDecimal('pi','3.42159')
     */
    static void fixParameterMap() {
        GrailsParameterMap.metaClass.bigDecimal << { String name, Object defaultValue=null ->
            BigDecimal bd = null

            def val = delegate.get(name)
            if (val) {
                if (val instanceof BigDecimal)
                    bd = (BigDecimal)val
                else try {
                    bd = new BigDecimal(val)
                } catch (e) { }
            }

            if (null == bd && null != defaultValue) {
                if (defaultValue instanceof BigDecimal)
                    bd = (BigDecimal)defaultValue
                else try {
                    bd = new BigDecimal(defaultValue)
                } catch (e) { }
            }

            return bd
        }
    }

}
