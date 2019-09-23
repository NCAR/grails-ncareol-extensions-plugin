package grailsx.test

import grails.test.GrailsUnitTestCase
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass

class GrailsxUnitTestCaseTests extends GroovyTestCase {

    void testEmpties() {
        def testCase = new TestUnitTestCase()

        testCase.setUp()
        testCase.testConfigClean()
        testCase.tearDown()

        testCase.setUp()
        testCase.testEmptyGetConfig()
        testCase.tearDown()

        testCase.setUp()
        testCase.testEmptyGetGrailsApplication()
        testCase.tearDown()

        testCase.setUp()
        testCase.testEmptyRefConfig()
        testCase.tearDown()

        testCase.setUp()
        testCase.testEmptyRefGrailsApplication()
        testCase.tearDown()
    }

    void testConfigs() {
        def testCase = new TestUnitTestCase()

        testCase.setUp()
        testCase.testConfigEquiv()
        testCase.tearDown()

        testCase.setUp()
        testCase.testConfigAssign()
        testCase.tearDown()

        testCase.setUp()
        testCase.testSetConfigFoo()
        testCase.tearDown()

        testCase.setUp()
        testCase.testSetGAConfigBar()
        testCase.tearDown()

        testCase.setUp()
        testCase.testConfigClean()
        testCase.tearDown()
    }

}


class TestUnitTestCase extends GrailsxUnitTestCase {

    void setUp() {
        super.setUp()
    }

    void tearDown() {
        super.tearDown()
    }

    void testEmptyGetConfig() {
        def cf = this.getConfig()
        assertNotNull cf
        assertNotNull cf.config
    }

    void testEmptyGetGrailsApplication() {
        def ga = this.getGrailsApplication()
        assertNotNull ga
        assertNotNull ga.config
    }

    void testEmptyRefConfig() {
        def cf = config
        assertNotNull cf
        assertNotNull cf.config
    }

    void testEmptyRefGrailsApplication() {
        def ga = grailsApplication
        assertNotNull ga
        assertNotNull ga.config
    }

    void testConfigEquiv() {
        assertEquals grailsApplication.config.hashCode, config.hashCode
    }

    void testConfigAssign() {
        def key = 'my.key.is'
        def val = 'assigned a value'

        config."$key" = val
        assertEquals val, grailsApplication.config."$key"
        assertEquals grailsApplication.config."$key", config."$key"

        val += ' again'
        grailsApplication.config."$key" = val
        assertEquals val, config."$key"
        assertEquals grailsApplication.config."$key", config."$key"
    }

    void testSetConfigFoo() {
        config.foo = 42
        assertEquals grailsApplication.config.foo, config.foo
        assertEquals 42, grailsApplication.config.foo
    }

    void testSetGAConfigBar() {
        grailsApplication.config.bar = 54
        assertEquals grailsApplication.config.bar, config.bar
        assertEquals 54, config.bar
    }

    void testConfigClean() {
        assert !config.foo  // [:] and not 42 leftover from another test
        assert !config.bar  // [:] and not 54 leftover from another test
    }

}
