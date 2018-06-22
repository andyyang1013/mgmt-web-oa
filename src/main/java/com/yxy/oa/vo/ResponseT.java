package com.yxy.oa.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * http响应数据包装类【支持泛型】
 */
@Data
public class ResponseT<T> implements Serializable {
    /**
     * 请求结果编码,200代表成功
     */
    private String code = "200";


    /**
     * 前端提示信息：【当请求失败时，该属性有值；否则，为空】  默认值【中文】，当国际化配置文件中没有定义时显示
     */
    private String msg;

    /**
     * 业务数据体 【当请求失败时，该属性值为空】
     */
    private T data;


}
