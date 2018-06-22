package com.yxy.oa.encrypt;

import com.yxy.oa.util.Md5Util;
import org.springframework.util.StringUtils;

/**
 * 一比分的加密算法
 **/
public class YbfEncrypt implements IEncrypt {
    @Override
    public String getBackPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return password.toUpperCase();
    }

    @Override
    public String getFrontPassword(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            return "";
        }
        return Md5Util.md5Hex(password).toUpperCase();
    }
}
