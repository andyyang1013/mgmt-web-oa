package com.yxy.oa.util;/**
 * Created by dell on 2017/11/13.
 */

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各种工具汇总
 **/
public class Toolkit {


    /**
     * 密码加密格式  [password+salt]
     */
    private static final String ENCRYPT_FORMAT = "%s%s";

    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("sha1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加密算法    【算法：sha1(md5(password)+salt)】   每个用户一个salt【salt=loginid】
     *
     * @param passwordByMd5 MD5加密结果
     * @param salt          盐
     * @return
     */
    public static String encrypt(String passwordByMd5, String salt) {
        if (StringUtils.isEmpty(passwordByMd5) || StringUtils.isEmpty(salt)) {
            return null;
        }
        return getSha1(String.format(ENCRYPT_FORMAT, passwordByMd5, salt));
    }


    /**
     * 生成随机盐
     **/
    public static String generateSalt() {
        Random random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new BASE64Encoder().encode(salt);
    }


    /**
     * 获取当前时间
     **/
    public static Date getCurDate() {
        Calendar curCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return curCalendar.getTime();
    }

    /**
     * 验证为手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 验证为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String regex = "[0-9]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 去除- UUID
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        String salt = generateSalt();
        String password = encrypt("123456", salt);
        System.out.println("salt==" + salt + "password==" + password);
    }
}
