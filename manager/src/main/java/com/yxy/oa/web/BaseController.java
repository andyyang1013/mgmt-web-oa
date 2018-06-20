package com.yxy.oa.web;

import com.yxy.oa.config.convert.DateEditor;
import com.yxy.oa.config.filter.UserReqContextUtil;
import com.yxy.oa.repository.IRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 基础控制器类
 **/
public class BaseController {

    public static final String SUCCESS = "success";

    protected Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected IRedisRepository redisRepository;


    /**
     * 表单日期提交，支持多种格式
     *
     * @param webDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        webDataBinder.registerCustomEditor(Date.class, new DateEditor(true));
    }


    /**
     * 获取当前用户id
     *
     * @return
     */
    protected Long getCurUserId() {
        return UserReqContextUtil.getRequestUserId();
    }

    /**
     * 用户登出
     */
    protected void removeContext() {
        UserReqContextUtil.removeContext();
    }

}
