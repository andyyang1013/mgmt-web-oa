package com.yxy.oa.util;/**
 * Created by dell on 2018/3/27.
 */

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * base64的加解密
 **/
public class Base64Util {
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * base64编码
     *
     * @param str 待编码的字符串
     * @return
     */
    public static String encode(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(str.getBytes(CHARSET_UTF8));
    }

    /**
     * base64编码
     *
     * @param bytes 待编码的字节流
     * @return
     */
    public static String encode(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * base64解码
     *
     * @param str 待解码的字符串
     * @return
     */
    public static String decode(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(str), CHARSET_UTF8);
    }

    /**
     * base64解码
     *
     * @param bytes 待解码的字节流
     * @return
     */
    public static String decode(byte[] bytes) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(bytes), CHARSET_UTF8);
    }
}
