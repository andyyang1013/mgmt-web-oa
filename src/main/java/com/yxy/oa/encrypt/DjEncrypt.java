package com.yxy.oa.encrypt;/**
 * Created by dell on 2018/2/27.
 */

import com.yxy.oa.encrypt.util.SHAEncryptUtil;
import com.yxy.oa.util.Md5Util;
import org.springframework.util.StringUtils;

/**
 * 电竞的加密算法
 **/
public class DjEncrypt implements IEncrypt {
    @Override
    public String getBackPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return SHAEncryptUtil.sha1(password.toUpperCase() + salt);
    }

    @Override
    public String getFrontPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return Md5Util.md5Hex(password).toUpperCase();
    }
}
