package com.yxy.oa.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 利用okhttp进行get和post的访问
 **/
public class Okhttp {

    private Logger logger = LoggerFactory.getLogger(Okhttp.class);

    private OkHttpClient okClient = null;

    private static class Builder {
        private static final Okhttp INSTANCE = new Okhttp();
    }

    public static final Okhttp builder() {
        return Builder.INSTANCE;
    }

    /**
     * 构造方法
     *
     * @return
     */
    private Okhttp() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        //连接超时
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        //读取超时
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        //写入超时
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okClient = clientBuilder.build();
    }


    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public String get(String url) {
        String result = null;
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            logger.error("get方法请求失败：", e);
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @param url
     * @param data 提交的参数为key=value&key1=value1的形式
     * @return
     */
    public String post(String url, String data) {
        String result = null;
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/html;charset=utf-8"), data);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = okClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            logger.error("post方法请求失败：", e);
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @param url
     * @param headersParams 请求头参数
     * @param bodyParams    body体
     * @return
     */
    public String post(String url, Map<String, String> headersParams, Map<String, String> bodyParams) {
        String result = null;
        Request request = new Request.Builder().url(url).headers(setHeaders(headersParams)).post(setBody(bodyParams)).build();
        try {
            Response response = okClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            logger.error("post方法请求失败：", e);
        }
        return result;
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            for (Map.Entry<String, String> entry : headersParams.entrySet()) {
                headersbuilder.add(entry.getKey(), entry.getValue());
            }
        }
        headers = headersbuilder.build();
        return headers;
    }

    /**
     * post请求参数
     *
     * @param bodyParams
     * @return
     */
    private RequestBody setBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                formEncodingBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }
}  