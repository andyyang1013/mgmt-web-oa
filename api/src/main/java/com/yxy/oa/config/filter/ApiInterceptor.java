package com.yxy.oa.config.filter;

import com.yxy.oa.Constant;
import com.yxy.oa.entity.SubsidiaryInfo;
import com.yxy.oa.entity.enums.SubsidiaryStateEnum;
import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.service.ISubsidiaryInfoService;
import com.yxy.oa.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 拦截器，每次请求之前需要做的事情
 * 针对内网的请求（通过白名单ip去判定），不做任何拦截
 * 针对外网的请求，需要验证签名的正确性。
 **/
public class ApiInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    @Autowired
    private ISubsidiaryInfoService subsidiaryInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String apiKey = request.getParameter(Constant.API_KEY);
        if (StringUtils.isEmpty(apiKey)) {
            throw new BizException(CodeMsg.apiKey_not_empty);
        }
        SubsidiaryInfo curSubsidiaryInfo = subsidiaryInfoService.selectByApiKey(apiKey);
        if (curSubsidiaryInfo == null) {
            throw new BizException(CodeMsg.apiKey_is_invalid);
        }
        if (curSubsidiaryInfo.getState() == SubsidiaryStateEnum.NOT_ACCESS.getState()) {
            throw new BizException(CodeMsg.subsidiary_not_access);
        }

        UserReqContextUtil.setSubsidiaryInfo(curSubsidiaryInfo);

        //小幺鸡测试，暂时去掉签名验证，方便测试，线上放开
        String clientSign = request.getHeader(Constant.SIGN);
        Map<String, String[]> parameterMap = request.getParameterMap();
        String sign = SignUtil.sign(parameterMap, curSubsidiaryInfo.getApiSecret());
        if (!sign.equals(clientSign)) {
            throw new BizException(CodeMsg.sign_is_invalid);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //清除上下文
        UserReqContextUtil.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}