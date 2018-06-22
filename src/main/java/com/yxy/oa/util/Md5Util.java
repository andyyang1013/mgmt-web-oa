package com.yxy.oa.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 对消息内容做摘要处理
 **/
public class Md5Util {
    /**
     * 生成含有密钥的md5的密码
     *
     * @param content
     * @param keySecret
     * @return
     */
    public static String generateMd5(String content, String keySecret) {
        content = md5Hex(content + keySecret);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = content.charAt(i / 3 * 2);
            char c = keySecret.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = content.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     *
     * @param content
     * @param md5
     * @param keySecret
     * @return
     */
    public static boolean verify(String content, String md5, String keySecret) {
        char[] cs1 = new char[32];  //md5摘要
        char[] cs2 = new char[16];  //密钥
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String keySecretCal = new String(cs2);
        return keySecretCal.equals(keySecret) && md5Hex(content + keySecret).equals(new String(cs1));
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     *
     * @param src
     * @return
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 生成md5密钥
     *
     * @return
     */
    public static String generateKeySecret() {
        Random secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < salt.length; i++) {
            hexString.append(Integer.toHexString(0xf & salt[i]));
        }
        return hexString.toString();
    }

}