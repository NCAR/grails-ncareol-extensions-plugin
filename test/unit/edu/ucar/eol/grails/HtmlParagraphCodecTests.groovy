package edu.ucar.eol.grails

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import grails.test.*

class HtmlParagraphCodecTests extends GrailsUnitTestCase {

    void testHtmlEncode() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec

        assertEquals 'foobar', "foobar".encodeAsHtmlParagraph()
        assertEquals 'foo&lt;P&gt;bar', "foo<P>bar".encodeAsHtmlParagraph()
    }

    void testBrEncode() {
        loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec

        assertEquals 'foo<BR>bar', "foo\nbar".encodeAsHtmlParagraph()
        assertEquals 'foo<BR>bar', "foo\r\nbar".encodeAsHtmlParagraph()
        //assertEquals 'foo<BR>\nbar', "foo\r\n\n\rbar".encodeAsHtmlParagraph()
    }

    void testParagraphEncode() {
        //loadCodec HTMLCodec     // used by HtmlParagraphCodec
        loadCodec HtmlParagraphCodec

        def expected = 'foo\n<P class="para-break"></P>\nbar'
        assertEquals expected, "foo\r\n\nbar".encodeAsHtmlParagraph()
        assertEquals expected, "foo\r\rbar".encodeAsHtmlParagraph()
        assertEquals expected, "foo\n\rbar".encodeAsHtmlParagraph()
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
        def expected =  "foo\n\nbar"
        assertEquals expected, 'foo<p></p>bar'.decodeHtmlParagraph()
        assertEquals expected, 'foo<P></P>bar'.decodeHtmlParagraph()
        assertEquals expected, 'foo<P></p>bar'.decodeHtmlParagraph()
        assertEquals expected, 'foo<p></P>bar'.decodeHtmlParagraph()
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
