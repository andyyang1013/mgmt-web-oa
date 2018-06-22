package com.yxy.oa.config.filter;


import com.yxy.oa.entity.SysUser;
import lombok.Data;

/**
 * 使用ThreadLocal封装每次请求的一些参数
 **/
public class UserReqContextUtil {

    /**
     * 用户请求信息上下文
     **/
    private static ThreadLocal<UserReqContext> reqContextLocal = new ThreadLocal<UserReqContext>();

    /**
     * 设置请求信息
     *
     * @param reqContext
     */
    public static void set(UserReqContext reqContext) {
        reqContextLocal.set(reqContext);
    }


    /**
     * 设置请求token
     */
    public static void setToken(String token) {
        UserReqContext userReqContext = null;
        if (reqContextLocal.get() == null) {
            userReqContext = new UserReqContext();
            reqContextLocal.set(userReqContext);
        } else {
            userReqContext = reqContextLocal.get();
        }
        userReqContext.setToken(token);
    }

    /**
     * 设置请求路径uri
     */
    public static void setRequestUri(String requestUri) {
        UserReqContext userReqContext = null;
        if (reqContextLocal.get() == null) {
            userReqContext = new UserReqContext();
            reqContextLocal.set(userReqContext);
        } else {
            userReqContext = reqContextLocal.get();
        }
        userReqContext.setRequestUri(requestUri);
        ;
    }

    /**
     * 设置请求客户端Ip
     */
    public static void setRequestClientIp(String clientIp) {
        UserReqContext userReqContext = null;
        if (reqContextLocal.get() == null) {
            userReqContext = new UserReqContext();
            reqContextLocal.set(userReqContext);
        } else {
            userReqContext = reqContextLocal.get();
        }
        userReqContext.setClientIp(clientIp);
        ;
    }

    /**
     * 设置请求客户端Ip
     */
    public static void setRequestUser(SysUser user) {
        UserReqContext userReqContext = null;
        if (reqContextLocal.get() == null) {
            userReqContext = new UserReqContext();
            reqContextLocal.set(userReqContext);
        } else {
            userReqContext = reqContextLocal.get();
        }
        userReqContext.setUser(user);
        ;
    }

    /**
     * 获取请求信息
     *
     * @return
     */
    public static UserReqContext get() {
        return reqContextLocal.get();
    }


    /**
     * 获取请求token
     */
    public static String getToken() {
        return reqContextLocal.get().getToken();
    }

    /**
     * 获取请求路径uri
     */
    public static String getRequestUri() {
        return reqContextLocal.get().getRequestUri();
    }

    /**
     * 获取请求路径uri
     */
    public static String getRequestClientIp() {
        return reqContextLocal.get().getClientIp();
    }

    /**
     * 获取请求路径uri
     */
    public static SysUser getRequestUser() {
        return reqContextLocal.get().getUser();
    }

    /**
     * 获取当前用户id
     */
    public static Long getRequestUserId() {
        if (reqContextLocal.get() == null || reqContextLocal.get().getUser() == null) {
            return -1L;
        }
        return reqContextLocal.get().getUser().getId();
    }

    /**
     * 删除上下文
     */
    public static void removeContext() {
        reqContextLocal.remove();
    }

    /**
     * 用户请求对象
     */
    @Data
    public static class UserReqContext {
        /**
         * 请求路径uri
         **/
        private String requestUri;

        /**
         * 请求客户端ip
         **/
        private String clientIp;

        /**
         * 请求token
         **/
        private String token;

        /**
         * 用户信息
         **/
        private SysUser user;

    }
}
