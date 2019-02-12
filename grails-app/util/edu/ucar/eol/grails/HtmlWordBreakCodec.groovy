package edu.ucar.eol.grails

/**
 * like HTMLCodec but then add word break oppurtunities after some common punctuation
 * --not all punc, e.g. \\p{Punct}\\p{IsPunctuation} since that
 * will get too much already-encoded HTML (&<wbr>amp;)
 */
class HtmlWordBreakCodec {

    static encode = { str ->
        str.encodeAsHTML().normalize().replaceAll("([),-./:;@_|]++)([\\S])",'$1<wbr>$2')
    }

    static decode = { str ->
        str.replaceAll('(?i)<wbr/?>','').decodeHTML()
    }

}
