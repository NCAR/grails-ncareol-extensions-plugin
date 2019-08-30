package edu.ucar.eol.grails

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import grails.test.*

class HtmlLineBreakCodecTests extends GrailsUnitTestCase {

    void testEncodeSimple() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foobar".encodeAsHtmlLineBreak() , 'foobar' )
    }

    void testEncodeBR() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo<BR>bar".encodeAsHtmlLineBreak() , 'foo&lt;BR&gt;bar' )
    }

    void testEncodeSingle() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\nbar".encodeAsHtmlLineBreak() , "foo<BR>bar" )
    }

    void testEncodeRepeated() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\n\nbar".encodeAsHtmlLineBreak() , "foo<BR>bar" )
    }

    void testEncodeMultiple() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\nbar\n\nbahz\n".encodeAsHtmlLineBreak() , "foo<BR>bar<BR>bahz<BR>" )
    }

    void testEncodeDOS() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\r\nbar".encodeAsHtmlLineBreak() , "foo<BR>bar" )
    }


    void testEncodeMultipleDOS() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\r\n\nbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )
        assertEquals ( "foo\r\n\n\rbar".encodeAsHtmlLineBreak() , "foo<BR>bar" )
        assertEquals ( "foo\r\rbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )
        assertEquals ( "foo\n\rbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )
    }


    void testDecodeSimple() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foobar" , 'foobar'.decodeHtmlLineBreak() )
    }

    void testDecodeHTML() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo<BR>bar" , 'foo&lt;BR&gt;bar'.decodeHtmlLineBreak() )
    }

    void testDecodeNL() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\nbar" , "foo\nbar".decodeHtmlLineBreak() )
    }

    void testDecodeBR() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        assertEquals ( "foo\nbar" , 'foo<BR>bar'.decodeHtmlLineBreak() )
    }

    void testDecodeBRNL() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        // (encoder won't generate this)
        assertEquals ( "foo\nbar" , "foo\n<BR>\nbar".decodeHtmlLineBreak() )
    }

    void testDecodeBRBR() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec
        // (encoder won't generate this)
        // tough luck if we actually want "foo\n\nbar":
        // that's too hard to deal with at the same time as BRNL
        assertEquals ( "foo\nbar" , "foo<BR><BR>bar".decodeHtmlLineBreak() )
    }


    void testDecodeBrStyles() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec

        assertEquals ( 'foo<Br>bar'.decodeHtmlLineBreak() , "foo\nbar" )
        assertEquals ( 'foo<bR>bar'.decodeHtmlLineBreak() , "foo\nbar" )
        assertEquals ( 'foo<br>bar'.decodeHtmlLineBreak() , "foo\nbar" )

        assertEquals ( 'foo<Br/>bar'.decodeHtmlLineBreak() , "foo\nbar" )
        assertEquals ( 'foo<bR />bar'.decodeHtmlLineBreak() , "foo\nbar" )
        assertEquals ( "foo<br  \t />bar".decodeHtmlLineBreak() , "foo\nbar" )

    }

}
