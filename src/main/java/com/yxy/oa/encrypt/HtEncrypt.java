package com.yxy.oa.encrypt;

import com.yxy.oa.encrypt.util.HtEncryptUtil;
import com.yxy.oa.util.Md5Util;
import org.springframework.util.StringUtils;

/**
 * 华体的加密算法
 **/
public class HtEncrypt implements IEncrypt {
    @Override
    public String getBackPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return HtEncryptUtil.sha512(password.toLowerCase(), salt);
    }

    @Override
    public String getFrontPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return Md5Util.md5Hex(password).toLowerCase();
    }
}
