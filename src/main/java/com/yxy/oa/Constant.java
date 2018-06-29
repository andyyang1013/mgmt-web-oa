package com.yxy.oa;

/**
 * 工程常量类
 **/
public class Constant {

    /**
     * 项目名称，用于在redis中进行区分
     */
    public static final String PROJ_NAME = "oa-manager";


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
     * 系统管理权限父Id
     */
    public static final int SYS_PERMISSION_ID = 20;

}
