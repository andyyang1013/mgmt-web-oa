package com.yxy.oa.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Java 加密解密之消息摘要算法
 **/
public class SignUtil {

    /**
     * 根据正文内容和密钥，生成内容摘要
     *
     * @param secret  密钥
     * @param content 正文内容
     * @return
     */
    public static String digest(String secret, String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = secret.getBytes("utf-8");
            byte[] dataBytes = content.getBytes("utf-8");

            SecretKey secretKey = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secretKey);

            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            return new String(hexB, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据参数和密钥，生成内容摘要
     * 把数组所有元素排序，并按照“key1=value1&key2=value2…”的格式拼接
     *
     * @param paraMap 参数
     * @param secret  密钥
     * @return String
     */
    public static String sign(Map<String, ?> paraMap, String secret) {
        List<String> keys = new ArrayList<>(paraMap.keySet());
        Collections.sort(keys);
        StringBuilder s = new StringBuilder();
        for (String key : keys) {
            Object values = paraMap.get(key);
            if (values instanceof String[]) {
                for (String value : (String[]) values) {
                    if (StringUtils.isNotEmpty(value)) {
                        s.append(key).append("=").append(urlEncode(value)).append("&");
                    }
                }
            } else if (values instanceof List) {
                for (String value : (List<String>) values) {
                    if (StringUtils.isNotEmpty(value)) {
                        s.append(key).append("=").append(urlEncode(value)).append("&");
                    }
                }
            } else {
                if (values != null && StringUtils.isNotEmpty(String.valueOf(values))) {
                    s.append(key).append("=").append(urlEncode(String.valueOf(values))).append("&");
                }
            }
        }
        return digest(secret, s.toString());
    }

    /**
     * 对中文做转码
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }

}