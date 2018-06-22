package com.yxy.oa.encrypt;

/**
 * 默认的加密算法
 **/
public class DefaultEncrypt implements IEncrypt {
    @Override
    public String getBackPassword(String password, String salt) {
        return null;
    }

    @Override
    public String getFrontPassword(String password, String salt) {
        return null;
    }
}
