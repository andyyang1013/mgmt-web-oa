package com.yxy.oa.service;

/**
 * 邮件或者短信服务
 **/
public interface INoticeService {

    /**
     * 发送短信
     *
     * @param receiver     接收人手机号，可支持多个,使用;分隔，如18682208618;18520127259
     * @param contentParam 正文参数，json对象字符串
     * @return
     */
    boolean sendMsg(String receiver, String contentParam);

    /**
     * 发送email
     *
     * @param receiver     接收人邮箱，可支持多个,使用;分隔，如18682208618@163.com;18520127259@126.com
     * @param contentParam 正文参数，json对象字符串
     * @return
     */
    boolean sendEmail(String receiver, String contentParam);
}
