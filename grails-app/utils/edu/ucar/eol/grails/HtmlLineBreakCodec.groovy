package edu.ucar.eol.grails

/**
 * like HTMLCodec but then add a BR tag for line breaks (CR, CR LF, or LF)
 */
class HtmlLineBreakCodec {

    /**
     * encode sequences of returns and newlines as a single BR tag
     */
    static encode = { str ->
        // sequences of returns/newlines to single br:
        str.encodeAsHTML().replaceAll('[\r\n]+','<BR>')

        // DOS & single-newlines to br:
        //str.encodeAsHTML().replaceAll('\r?\n|\r','<BR>')
        //str.encodeAsHTML().replace("\r\n",'<BR>').replace("\r",'<BR>').replace("\n",'<BR>')

        // DOS & double-newlines to br:
        //str.encodeAsHTML().replace("\r\n","\n").replace("\r","\n").replace("\n\n",'<BR>')

        // MOVED to HtmlParagraphCodec
        // double-newlines to br:
        //str.encodeAsHTML().normalize().replace("\n\n",'<BR>')
    }

    /**
     * decode BR tags to single newlines
     */
    static decode = { str ->
        // br to single-newline:
        //str.replace('<BR>',"\n").replace('<br>',"\n").decodeHTML()
        str.replaceAll('(?i)<br\\s*/?>',"\n").replaceAll('\\n+',"\n").decodeHTML()

        // MOVED to HtmlParagraphCodec
        // br to double-newline:
        //str.replaceAll('(?i)<br\\s*/?>',"\n\n").decodeHTML()
    }

}
