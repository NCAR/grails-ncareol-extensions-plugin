package edu.ucar.eol.grails

/**
 * like HTMLCodec but then use break tags for single newlines and paragraph tags for double newlines
 */
class HtmlParagraphCodec {

    /**
     * encode quad-newlines to <p>, double-newlines to <BR>, with HTML encoding and DOS-conversion
     */
    static encode = { str ->
        // DOS & double-newlines to P:
        str.encodeAsHTML().replaceAll("\r\n","\n").replaceAll("\r","\n").replaceAll("\n",'<BR>').replaceAll("<BR><BR>",'\n<P class="para-break"></P>\n')

        // double-newlines to P:
        //str.encodeAsHTML().normalize().replace("\n\n",'<P>')
    }

    /**
     * decode empty <P> to double-newline, with HTML decoding
     */
    static decode = { str ->
        str.replaceAll('(?i)[\\s\\r\\n]*<p[\\s\\r\\n]*(class="para-break")?[\\s\\r\\n]*>[\\s\\r\\n]*</p>[\\r\\n]*',"\n\n").decodeHTML()
    }

}
