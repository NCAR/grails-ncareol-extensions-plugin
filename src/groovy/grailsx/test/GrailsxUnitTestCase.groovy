package grailsx.test

import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass

/**
 * Adds grailsApplication with a config for unit-testing services, domains,
 * or even src/groovy classes in Grails 2.x (and probably 1.x).
 * In unit tests, we are without the full integration and dependency injection,
 * so GrailsxUnitTestCase provides workable mocked grailsApplication and config objects,
 * for classes that use grailsApplication instead of ConfigurationHolder (as we
 * are never sure which one we're "supposed" to use, and what works varies in
 * different areas of a Grails app).
 *
 * Your "Tests" class should simply extend this class instead of GrailsUnitTestCase.
 * (note the "x" in or not in the classnames)
 *
 * If you implement setUp(), then call "super.setUp()".
 * There, or wherever you instantiate your target, assign its grailsApplication and/or config:
 *     myService = new myService()
 *     myService.grailsApplication = grailsApplication
 *
 * In your test methods, set any "config" items your targets need, e.g.
 *     config.foo.bar = 'bahz'
 * and your target's call to grailsApplication.config will pick up your test config.
 *
 */
class GrailsxUnitTestCase extends GrailsUnitTestCase {

    ConfigObject config
    GrailsApplication grailsApplication

    void setUp() {
        super.setUp()
    }

    /**
     * @return ConfigObject which is tied to the mocked GrailsApplication object
     */
    ConfigObject getConfig() {
        if (config == null) config = new ConfigObject()
        config
    }

    /**
     * @return GrailsApplication mock object, with a ConfigObject,
     *         suitable for unit testing classes in Grails without DI
     */
    GrailsApplication getGrailsApplication() {
        if (null == grailsApplication) grailsApplication = createMockGrailsApplication()
        grailsApplication
    }

    // adapted from org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngineTests
    protected GrailsApplication createMockGrailsApplication() {
        [
         getMainContext: { ->  null},
         getConfig: { ->  config},
         getFlatConfig: { -> config.flatten() },
         getArtefacts: { String artefactType -> [] as GrailsClass[] },
         getArtefactByLogicalPropertyName: { String type, String logicalName ->  null}
        ] as GrailsApplication
    }

}
