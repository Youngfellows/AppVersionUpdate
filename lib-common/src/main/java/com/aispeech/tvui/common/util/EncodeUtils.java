package com.aispeech.tvui.common.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;



public class EncodeUtils {
    private static final String TAG = EncodeUtils.class.getSimpleName();

    private EncodeUtils() {
        throw new UnsupportedOperationException("can't instantiate class"+TAG);
    }

    /**
     * Return the urlencoded string.
     *
     * @param input The input.
     * @return the urlencoded string
     */

    public static String urlEncode(final String input) {
        return urlEncode(input, "UTF-8");
    }



    /**
     * Return the urlencoded string.
     *
     * @param input       The input.
     * @param charsetName The name of charset.
     * @return the urlencoded string
     */

    public static String urlEncode(final String input, final String charsetName) {
        if (input == null || input.length() == 0){
            return "";
        }
        try {
            String str = URLEncoder.encode(input, charsetName);
            str = str.replace("+", "%20");
            str = str.replace("%2b", "+");
            return str;
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Return the string of decode urlencoded string.
     *
     * @param input The input.
     * @return the string of decode urlencoded string
     */

    public static String urlDecode(final String input) {
        return urlDecode(input, "UTF-8");
    }

    /**
     * Return the string of decode urlencoded string.
     *
     * @param input       The input.
     * @param charsetName The name of charset.
     * @return the string of decode urlencoded string
     */

    public static String urlDecode(final String input, final String charsetName) {
        if (input == null || input.length() == 0){
            return "";
        }
        try {

            return URLDecoder.decode(input, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Return Base64-encode string.
     *
     * @param input The input.
     * @return Base64-encode string
     */
    public static String base64Encode2String(final byte[] input) {
        if (input == null || input.length == 0){
            return "";
        }
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * Return the bytes of decode Base64-encode string.
     *
     * @param input The input.
     * @return the string of decode Base64-encode string
     */
    public static String base64Decode(final String input) {
        if (input == null || input.length() == 0){
            return null;
        }
        return new String(Base64.decode(input, Base64.NO_WRAP));
    }

    /**
     * Return the bytes of decode Base64-encode bytes.
     * @param input The input.
     * @return the bytes of decode Base64-encode bytes
     */

    public static byte[] base64Decode(final byte[] input) {
        if (input == null || input.length == 0){
            return new byte[0];
        }
        return Base64.decode(input, Base64.NO_WRAP);
    }

}
