package com.yxy.oa.service.impl;

import com.alibaba.fastjson.JSON;
import com.yxy.oa.service.INoticeService;
import com.yxy.oa.util.Okhttp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件或者短信服务实现类
 **/
@Service
public class NoticeServiceImpl implements INoticeService {

    public static final String SUCCESS = "ok";

    @Value("${notice.server.url}")
    private String noticeServiceUrl;

    @Override
    public boolean sendMsg(String receiver, String contentParam) {

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("receiver", receiver);
        paramsMap.put("content", contentParam);
        paramsMap.put("mode", "1");
        paramsMap.put("type", "5");
        paramsMap.put("token", "PartnerMessageToken");

        Map<String, String> headersMap = new HashMap<>();
        String result = Okhttp.builder().post(noticeServiceUrl, headersMap, paramsMap);
        Map<String, String> resultMap = JSON.parseObject(result, HashMap.class);

        return SUCCESS.equals(resultMap.get("result"));

    }

    @Override
    public boolean sendEmail(String receiver, String contentParam) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("receiver", receiver);
        paramsMap.put("content", contentParam);
        paramsMap.put("mode", "2");
        paramsMap.put("type", "5");
        paramsMap.put("token", "PartnerMessageToken");

        Map<String, String> headersMap = new HashMap<>();
        String result = Okhttp.builder().post(noticeServiceUrl, headersMap, paramsMap);
        Map<String, String> resultMap = JSON.parseObject(result, HashMap.class);

        return SUCCESS.equals(resultMap.get("result"));
    }
}
