package com.yxy.oa.encrypt.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncryptUtil {

    /**
     * 传入文本内容，返回 sha-1 串
     *
     * @param strText
     * @return
     */
    public static String sha1(final String strText) {
        return sha(strText, "sha-1");
    }

    /**
     * 传入文本内容，返回 sha-256 串
     *
     * @param strText
     * @return
     */
    public static String sha256(final String strText) {
        return sha(strText, "sha-256");
    }

    /**
     * 传入文本内容，返回 sha-512 串
     *
     * @param strText
     * @return
     */
    public static String sha512(final String strText) {
        return sha(strText, "sha-512");
    }

    /**
     * 字符串 sha 加密
     *
     * @param strText
     * @return
     */
    private static String sha(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (StringUtils.isEmpty(strText)) {
            return strResult;
        }
        try {
            // sha 加密开始
            // 创建加密对象 并傳入加密類型
            MessageDigest messageDigest = MessageDigest.getInstance(strType);
            // 传入要加密的字符串
            messageDigest.update(strText.getBytes());
            // 得到 byte 類型结果
            byte[] messageDigestBuffer = messageDigest.digest();

            // 將 byte 转换为 string
            StringBuffer strHexString = new StringBuffer();
            // 遍历 byte buffer
            for (int i = 0; i < messageDigestBuffer.length; i++) {
                String hex = Integer.toHexString(0xff & messageDigestBuffer[i]);
                if (hex.length() < 2) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            // 得到返回結果
            strResult = strHexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return strResult;
    }
}