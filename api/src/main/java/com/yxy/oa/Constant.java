package com.yxy.oa;

/**
 * 工程常量类
 **/
public class Constant {

    /**
     * 项目名称，用于在redis中进行区分
     */
    public static final String PROJ_NAME = "os-api";


    /**
     * http前缀
     */
    public static final String HTTP_PREFIX = "http://";

    /**
     * https前缀
     */
    public static final String HTTPS_PREFIX = "https://";

    /**
     * 用户登录token key
     */
    public static final String USER_TOKEN = "token";


    /**
     * 用户登录token【keyType=string,value=user对象】
     */
    public static final String USER_TOKEN_REDIS_KEY = PROJ_NAME + ":user_token:token=%s";


    /**
     * 2小时
     */
    public static final int USER_TOKEN_EXPIRE = 60 * 60 * 2;


    /**
     * API_KEY 参数名
     */
    public static final String API_KEY = "apiKey";

    /**
     * 签名 参数名
     */
    public static final String SIGN = "sign";

    /**
     * 最大超时时间，单位秒
     */
    public static final int MAX_EXPIRE_TIME = 946080000;

    /**
     * 密码最大长度
     */
    public static final int MAX_PASSWORD_LENGTH = 128;


}
