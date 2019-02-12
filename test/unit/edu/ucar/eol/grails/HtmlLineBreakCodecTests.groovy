package edu.ucar.eol.grails

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import grails.test.*

class HtmlLineBreakCodecTests extends GrailsUnitTestCase {

    void testEncode() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec

        assertEquals ( "foobar".encodeAsHtmlLineBreak() , 'foobar' )
        assertEquals ( "foo<BR>bar".encodeAsHtmlLineBreak() , 'foo&lt;BR&gt;bar' )

        assertEquals ( "foo\nbar".encodeAsHtmlLineBreak() , "foo\nbar" )
        assertEquals ( "foo\r\nbar".encodeAsHtmlLineBreak() , "foo\nbar" )

        assertEquals ( "foo\r\n\nbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )
        assertEquals ( "foo\r\n\n\rbar".encodeAsHtmlLineBreak() , "foo<BR>\nbar" )
        assertEquals ( "foo\r\rbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )
        assertEquals ( "foo\n\rbar".encodeAsHtmlLineBreak() , 'foo<BR>bar' )

    }


    void testDecodeReverse() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec

        assertEquals ( "foobar" , 'foobar'.decodeHtmlLineBreak() )
        assertEquals ( "foo<BR>bar" , 'foo&lt;BR&gt;bar'.decodeHtmlLineBreak() )

        assertEquals ( "foo\nbar" , "foo\nbar".decodeHtmlLineBreak() )

        assertEquals ( "foo\n\nbar" , 'foo<BR>bar'.decodeHtmlLineBreak() )
        assertEquals ( "foo\n\n\nbar" , "foo<BR>\nbar".decodeHtmlLineBreak() )

    }


    void testDecodeBrStyles() {
        loadCodec HTMLCodec     // used by HtmlLineBreakCodec
        loadCodec HtmlLineBreakCodec

        assertEquals ( 'foo<Br>bar'.decodeHtmlLineBreak() , "foo\n\nbar" )
        assertEquals ( 'foo<bR>bar'.decodeHtmlLineBreak() , "foo\n\nbar" )
        assertEquals ( 'foo<br>bar'.decodeHtmlLineBreak() , "foo\n\nbar" )

        assertEquals ( 'foo<Br/>bar'.decodeHtmlLineBreak() , "foo\n\nbar" )
        assertEquals ( 'foo<bR />bar'.decodeHtmlLineBreak() , "foo\n\nbar" )
        assertEquals ( "foo<br  \t />bar".decodeHtmlLineBreak() , "foo\n\nbar" )

    }

}
