
package com.yxy.oa.encrypt.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 安全相关工具类
 */
public class HtEncryptUtil {

    private static Logger logger = LoggerFactory.getLogger(HtEncryptUtil.class);
    private static final String AES_KEY_16 = "f69ae5a048d1yfZX";
    private static final String UTF8 = "utf-8";

    private HtEncryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * MD5 加密方法
     *
     * @param plainText 输入需要加密的字符串
     * @return 加密后得到的字符串 2016年7月20日
     */
    public static String md5(String plainText) {
        // 首先判断是否为空
        if (StringUtils.isEmpty(plainText)) {
            return null;
        }
        return DigestUtils.md5Hex(plainText);
    }

    /**
     * 本系统用户密码加密方法
     *
     * @param md5Str 对用户密码进行md5
     * @param userId
     * @return
     */
    public static String sha512(String md5Str, String userId) {
        if (StringUtils.isEmpty(md5Str) || StringUtils.isEmpty(userId)) {
            return "";
        }
        // 对userId处理后用做密码盐
        String salt = Hex.encodeHexString(userId.getBytes(StandardCharsets.UTF_8));
        return DigestUtils.sha512Hex(StringUtils.join(md5Str, salt));
    }

    public static String sha512(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return DigestUtils.sha512Hex(str);
    }

    /**
     * sha1 加密方法
     *
     * @param inStr
     * @return 2016年7月20日
     */
    public static String sha1(String inStr) {
        return DigestUtils.sha1Hex(inStr);
    }

    /**
     * @param content
     * @return
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws Exception
     * @Description AES 加密数据
     */
    public static String encodeAES(String content) throws InvalidKeyException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return encryptAES(content, AES_KEY_16);
    }

    /**
     * @param content
     * @return
     * @throws Exception
     * @Description AES 解密数据
     */
    public static String decodeAES(String content) {
        return decryptAES(content, AES_KEY_16);
    }

    /**
     * @param content
     * @param sKey    : 密匙，密匙16位
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws Exception
     * @Description AEC 加密
     */
    public static String encryptAES(String content, String sKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        if (sKey == null) {
            logger.error("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            logger.error("Key[{}]长度不是16位", sKey);
            return null;
        }
        byte[] raw = sKey.getBytes(UTF8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(UTF8));
        return byte2Hex(encrypted);
    }

    /**
     * @param content
     * @param sKey
     * @return
     * @Description AES 解密算法
     */
    public static String decryptAES(String content, String sKey) {
        try {
            if (sKey == null) {
                logger.error("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.error("Key[{}]长度不是16位", sKey);
                return null;
            }
            byte[] raw = sKey.getBytes(UTF8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2Byte(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, UTF8);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    // byte[] 数据转16进制
    public static String byte2Hex(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 16进制转 byte[]
    public static byte[] hex2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
