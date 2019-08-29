package edu.ucar.eol.grails

/**
 * like HTMLCodec but then add <BR> for line breaks (CR, CR LF, or LF)
 */
class HtmlLineBreakCodec {

    static encode = { str ->
        // DOS & single-newlines to br:
        //str.encodeAsHTML().replaceAll('\r?\n|\r','<BR>')
        //str.encodeAsHTML().replace("\r\n",'<BR>').replace("\r",'<BR>').replace("\n",'<BR>')

        // DOS & double-newlines to br:
        //str.encodeAsHTML().replace("\r\n","\n").replace("\r","\n").replace("\n\n",'<BR>')

        // double-newlines to br:
        str.encodeAsHTML().normalize().replace("\n\n",'<BR>')
    }

    static decode = { str ->
        // br to single-newline:
        //str.replace('<BR>',"\n").replace('<br>',"\n").decodeHTML()
        //str.replaceAll('(?i)<br\s*/?>',"\n").decodeHTML()

        // br to double-newline:
        str.replaceAll('(?i)<br\\s*/?>',"\n\n").decodeHTML()
    }

}
