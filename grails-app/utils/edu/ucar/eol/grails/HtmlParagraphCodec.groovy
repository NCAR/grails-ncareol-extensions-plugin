package edu.ucar.eol.grails

/**
 * like HTMLCodec but then use paragraph tags for double newlines
 */
class HtmlParagraphCodec {

    /**
     * encode DOS & double-newlines to <BR>, with HTML encoding
     */
    static encode = { str ->
        // DOS & double-newlines to br:
        str.encodeAsHTML().replace("\r\n","\n").replace("\r","\n").replace("\n\n",'\n<P class="para-break"></P>\n')

        // double-newlines to br:
        //str.encodeAsHTML().normalize().replace("\n\n",'<P>')
    }

    /**
     * decode empty <P> to double-newline, with HTML decoding
     */
    static decode = { str ->
        str.replaceAll('(?i)[\\s\\r\\n]*<p[\\s\\r\\n]*(class="para-break")?[\\s\\r\\n]*>[\\s\\r\\n]*</p>[\\r\\n]*',"\n\n").decodeHTML()
    }

}
