/*
 * Copyright 2014 University Corporation for Atmospheric Research
 * See LICENSE.txt
 */

/**
 * Grails plugin to include common Groovy and Grails extension module
 *
 * @author jja@ucar.edu
 * @see String
 */
class NcareolExtensionsGrailsPlugin {
    def groupId = 'edu.ucar.eol'

    // the plugin version
    def version = '1.10.0'

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = '2.3 > *'

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        'grails-app/views/error.gsp',
        'web-app/**',
    ]

    // TODO Fill in these fields
    def title = 'ncareol extensions plugin' // Headline display name of the plugin
    def author = 'John Allison'
    def authorEmail = 'jja@ucar.edu'
    def description = '''\
Include and load some Groovy extension modules and common Grails taglibs.
'''

    // URL to the plugin's documentation
    //def documentation = 'http://github.com/ncareol/grails-ncareol-extensions-plugin/blob/master/README.md'

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = 'BSD Simplified'

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    //def scm = [ url: 'http://github.com/ncareol/grails-ncareol-extensions-plugin' ]
    def scm = [ url: 'file:///net/cds/git/grails-ncareol-extensions-plugin.git' ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)

      // kludge to get Groovy extension modules loaded in Grails
      // http://grails.1312388.n4.nabble.com/groovy-extension-module-and-grails-td4642249.html
      // http://jira.grails.org/browse/GRAILS-10652
      // http://stackoverflow.com/questions/19564902/applying-groovy-extensions-in-grails-produces-missingmethodexception-for-string
      Map<org.codehaus.groovy.reflection.CachedClass, List<MetaMethod>> map = [:]
      ClassLoader classLoader = Thread.currentThread().contextClassLoader
      try {
        Enumeration<URL> resources = classLoader.getResources(org.codehaus.groovy.runtime.m12n.ExtensionModuleScanner.MODULE_META_INF_FILE)
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
            GroovySystem.metaClassRegistry.registerExtensionModuleFromProperties(properties,
                              classLoader, map)
          }
          catch (IOException e) {
            throw new GroovyRuntimeException('Unable to load module META-INF descriptor', e)
          } finally {
            inStream?.close()
          }
        }
      }  catch (IOException ignored) {}
      map.each { org.codehaus.groovy.reflection.CachedClass cls, List<MetaMethod> methods ->
        cls.addNewMopMethods(methods)
      }

      /**
       * helper method to obtain a BigDecimal from parameters
       *
       * optional second argument is default value and can be a BigDecimal, String,
       * int, long, float, or double
       */
      org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap.metaClass.bigDecimal << {
        String name, Object defaultValue ->
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

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
