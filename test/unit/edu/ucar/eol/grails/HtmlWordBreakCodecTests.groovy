package edu.ucar.eol.grails

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import grails.test.*

class HtmlWordBreakCodecTests extends GrailsUnitTestCase {

    void testHtmlEncode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        // these are encoded by HTMLCodec and WordBreak has nothing to do
        assertEquals ( "foobar".encodeAsHtmlWordBreak() , 'foobar' )
    }

    void testSimpleEncode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foo/bar".encodeAsHtmlWordBreak() , "foo/<wbr>bar" )
        assertEquals ( "foo;bar".encodeAsHtmlWordBreak() , "foo;<wbr>bar" )
    }


    void testDoubleEncode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        // the < > get encoded and the resulting ; is punctuation that gets wbr
        assertEquals ( "foo<bahz>bar".encodeAsHtmlWordBreak() , 'foo&lt;<wbr>bahz&gt;<wbr>bar' )
    }

    void testEncodeExceptions() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foo, bar".encodeAsHtmlWordBreak() , "foo, bar" )
    }

    void testHtmlDecode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foobar" , 'foobar'.decodeHtmlWordBreak() )
        assertEquals ( "foo<bahz>bar" , 'foo&lt;bahz&gt;bar'.decodeHtmlWordBreak() )
        assertEquals ( "foo<wbr>bar" , 'foo&lt;wbr&gt;bar'.decodeHtmlWordBreak() )
    }

    void testSimpleDecode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foo/bar" , "foo/<wbr>bar".decodeHtmlWordBreak() )
        assertEquals ( "foo;bar" , "foo;<wbr>bar".decodeHtmlWordBreak() )
    }

    void testDecodeExceptions() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foo, bar" , "foo, bar".decodeHtmlWordBreak() )
    }

    void testDoubleDecode() {
        loadCodec HTMLCodec     // used by HtmlWordBreakCodec
        loadCodec HtmlWordBreakCodec

        assertEquals ( "foo<bahz>bar", 'foo&lt;<wbr>bahz&gt;<wbr>bar'.decodeHtmlWordBreak() )
    }

}
