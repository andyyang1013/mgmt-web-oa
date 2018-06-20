package com.yxy.oa.config.filter;

import com.yxy.oa.Constant;
import com.yxy.oa.entity.User;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.repository.IRedisRepository;
import com.yxy.oa.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，每次请求之前需要做的事情
 * 针对内网的请求（通过白名单ip去判定），不做任何拦截
 * 针对外网的请求，需要验证签名的正确性。
 **/
public class LoginInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private IRedisRepository redisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //是否合法用户
        boolean isLegalUser = false;

        //经过nginx代理，获取真实的客户端请求地址.如果为空直接取客户端地址
        String clientIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(clientIp)) {
            clientIp = request.getRemoteAddr();
        } else {
            clientIp = clientIp.split(",")[0];
        }
        String token = CookieUtil.getCookieValue(request, Constant.USER_TOKEN);
        UserReqContextUtil.setRequestClientIp(clientIp);
        UserReqContextUtil.setToken(token);
        if (StringUtils.isEmpty(token)) {
            throw new BizException(CodeMsg.user_token_blank);
        }
        Object object = redisRepository.get(String.format(Constant.USER_TOKEN_REDIS_KEY, token));
        if (object != null && object instanceof User) {
            User user = (User) object;
            UserReqContextUtil.setRequestUser(user);
            isLegalUser = true;
        }
        if (!isLegalUser) {
            logger.error("用户不合法");
            throw new BizException(CodeMsg.user_token_invalid);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}