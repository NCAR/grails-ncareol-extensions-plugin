package edu.ucar.eol.grails.plugins

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

// some stoopid tests to see if the plugin loaded correctly
class NcareolExtensionsTests extends GroovyTestCase {

    void testStringAppendSimple() {
        String given = "abc"
        String addtl = "def"
        String expected = "abcdef"

        assertEquals expected, given.append(addtl,1)
    }

    void testParamsBigDecimalNull() {
        def params = new GrailsParameterMap([:],null)
        assertNull params.bigDecimal('foo')
    }

    void testParamsBigDecimalSimple() {
        def params = new GrailsParameterMap([:],null)

        def piStr = '3.14'
        def piBD = new BigDecimal(piStr)
        params.pi = piStr

        assertEquals piBD, params.bigDecimal('pi')
        assertEquals piBD, params.bigDecimal('pi','42') // unused default value
    }

    void testParamsBigDecimalWithMissingValue() {
        def params = new GrailsParameterMap([:],null)
        def piStr = '3.14'
        def piBD = new BigDecimal(piStr)

        assertEquals piBD, params.bigDecimal('pi',piStr)
        assertEquals piBD, params.bigDecimal('pi',piBD)
    }

}
