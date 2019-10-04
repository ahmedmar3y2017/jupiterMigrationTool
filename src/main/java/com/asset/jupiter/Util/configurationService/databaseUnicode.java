package com.asset.jupiter.Util.configurationService;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static com.asset.jupiter.Util.defines.Defines.CODE_PAGE_ARABIC;
import static com.asset.jupiter.Util.defines.Defines.CODE_PAGE_ENGLISH;

// class to get unicode
public class databaseUnicode {


    static String unicode = "";

    public static String getUnicode() {
        return unicode;
    }

    public static void setUnicode(String unicode) {
        databaseUnicode.unicode = unicode;
    }

    public static String encodeToDatabse(String sentence) throws SQLException {
        String dbEncoding = getUnicode();
        if (!dbEncoding.equals("UTF8") && !dbEncoding.equals(CODE_PAGE_ARABIC)) {
            sentence = encodeString(sentence, CODE_PAGE_ARABIC, CODE_PAGE_ENGLISH);
        } else if (dbEncoding.equals(CODE_PAGE_ARABIC)) {
            sentence = encodeString(sentence, CODE_PAGE_ENGLISH, CODE_PAGE_ARABIC);
        }
        return sentence;
    }

    public String decodeFromDatabse(String sentence) throws SQLException {
        String dbEncoding = getUnicode();
        if (!dbEncoding.equals("UTF8") && !dbEncoding.equals(CODE_PAGE_ARABIC)) {
            sentence = encodeString(sentence, CODE_PAGE_ENGLISH, CODE_PAGE_ARABIC);
        }
        return sentence;
    }

    public static String encodeString
            (String sentence, String encodingCharset, String decodingCharset) {
        try {
            String encodedSentence = new String(sentence.getBytes(encodingCharset), decodingCharset);
            return detectDoubleEncoding(sentence, encodedSentence);
        } catch (UnsupportedEncodingException e) {
            return sentence;
        }
    }

    private String encodeMessage(String value) throws Exception {

        try {
            String encodedSentence = new String(value.getBytes("Cp1252"), "Cp1256");
            return detectDoubleEncoding(value, encodedSentence);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static String detectDoubleEncoding(String sentence, String encodedSentence) {
        //detect double encoding
        char[] encodedChars = encodedSentence.toCharArray();
        char[] sentenceChars = sentence.toCharArray();
        for (int i = 0; i < sentenceChars.length; i++) {
            if (i < encodedChars.length) { //in case the two strings don't have the same length - by Areeg
                if (sentenceChars[i] != '?' && encodedChars[i] == '?')
                    return sentence;
            }
        }
        return encodedSentence;
    }
}
