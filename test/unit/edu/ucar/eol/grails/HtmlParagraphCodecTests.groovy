package edu.ucar.eol.grails

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import grails.test.*

class HtmlParagraphCodecTests extends GrailsUnitTestCase {

    void testEncode() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec

        assertEquals ( "foobar".encodeAsHtmlParagraph() , 'foobar' )
        assertEquals ( "foo<P>bar".encodeAsHtmlParagraph() , 'foo&lt;P&gt;bar' )

        assertEquals ( "foo\nbar".encodeAsHtmlParagraph() , "foo\nbar" )
        assertEquals ( "foo\r\nbar".encodeAsHtmlParagraph() , "foo\nbar" )

        assertEquals ( "foo\r\n\nbar".encodeAsHtmlParagraph() , 'foo\n<P class="para-break"></P>\nbar' )
        assertEquals ( "foo\r\rbar".encodeAsHtmlParagraph() , 'foo\n<P class="para-break"></P>\nbar' )
        assertEquals ( "foo\n\rbar".encodeAsHtmlParagraph() , 'foo\n<P class="para-break"></P>\nbar' )

        //assertEquals ( "foo\r\n\n\rbar".encodeAsHtmlParagraph() , "foo<BR>\nbar" )

    }

    void testDecodeSimple() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foobar" , 'foobar'.decodeHtmlParagraph() )
    }

    void testDecodeHtml() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foo<P>bar" , 'foo&lt;P&gt;bar'.decodeHtmlParagraph() )
    }

    void testDecodeNL() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foo\nbar" , "foo\nbar".decodeHtmlParagraph() )
    }

    void testDecodeCRNL() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foo\r\nbar" , "foo\r\nbar".decodeHtmlParagraph() )
    }

    void testDecodeBareP() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( 'foo<p></p>bar'.decodeHtmlParagraph() , "foo\n\nbar" )
        assertEquals ( 'foo<P></P>bar'.decodeHtmlParagraph() , "foo\n\nbar" )
        assertEquals ( 'foo<P></p>bar'.decodeHtmlParagraph() , "foo\n\nbar" )
        assertEquals ( 'foo<p></P>bar'.decodeHtmlParagraph() , "foo\n\nbar" )
    }

    void testDecodeExpectedP() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foo\n\nbar" , 'foo\n<P class="para-break"></P>\nbar'.decodeHtmlParagraph() )
    }

    void testDecodeWeirdP() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        assertEquals ( "foo\n\nbar" , 'foo\n\n <P  > \t\n</P>\nbar'.decodeHtmlParagraph() )
    }

    void testDecodeUnclosedP() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        def str = "foo<P>\nbar"
        assertEquals ( str, str.decodeHtmlParagraph() )
    }

    void testDecodeFullP() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec
        def str = "foo<P>\nbar bahz\nquux\n</p>\nqizzle"
        assertEquals ( str, str.decodeHtmlParagraph() )
    }

}
