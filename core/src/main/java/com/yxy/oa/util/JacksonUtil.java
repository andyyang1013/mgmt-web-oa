/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.yxy.oa.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public final class JacksonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();

        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

//		MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;

        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        MAPPER.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, true);

        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        /**数字转成字符串*/
        MAPPER.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);

        MAPPER.setTimeZone(TimeZone.getDefault());

        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    private JacksonUtil() {
    }

    /**
     * 将json通过类型转换成对象
     *
     * @param json  json字符串
     * @param clazz 泛型类型
     * @return 返回对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return clazz.equals(String.class) ? (T) json : MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("字符串" + json + "解析成对象" + clazz.getName() + "时异常", e);
        }
        return null;
    }

    /**
     * 将json通过类型转换成对象
     *
     * @param json          json字符串
     * @param typeReference 引用类型
     * @return 返回对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, TypeReference<?> typeReference) {
        try {
            return (T) (typeReference.getType().equals(String.class) ? json : MAPPER.readValue(json, typeReference));
        } catch (IOException e) {
            LOGGER.error("字符串" + json + "解析成对象" + typeReference.getType() + "时异常", e);
        }
        return null;
    }

    /**
     * Json转换为集合
     *
     * @param data
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> jsonToList(String data, Class<T> clazz) {
        List lstObjs = (List<?>) jsonToObject(data, List.class);
        List lstResult = new ArrayList(lstObjs.size());

        for (Iterator objIter = lstObjs.iterator(); objIter.hasNext(); ) {
            Object objT = MAPPER.convertValue(objIter.next(), clazz);
            lstResult.add(objT);
        }
        return lstResult;
    }

    /**
     * Json转换为集合
     *
     * @param jsonFile
     * @param clazz
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> jsonToList(File jsonFile, Class<T> clazz) {
        List lstObjs = (List<?>) jsonToObject(jsonFile, List.class);
        if (lstObjs != null) {
            List lstResult = new ArrayList(lstObjs.size());

            for (Iterator objIter = lstObjs.iterator(); objIter.hasNext(); ) {
                Object objT = MAPPER.convertValue(objIter.next(), clazz);
                lstResult.add(objT);
            }
            return lstResult;
        } else {
            return new ArrayList();
        }
    }

    /**
     * json转化为对象
     *
     * @param json
     * @param t
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> t) {
        T rt = null;
        try {
            rt = MAPPER.readValue(json, t);
        } catch (JsonParseException e) {
            LOGGER.error("字符串：" + json + " 转化为对象" + t.getName() + " 时解析异常", e);
        } catch (JsonMappingException e) {
            LOGGER.error("字符串：" + json + " 转化为对象" + t.getName() + " 时映射异常", e);
        } catch (IOException e) {
            LOGGER.error("字符串：" + json + " 转化为对象" + t.getName() + " 时IO异常", e);
        }
        return rt;
    }

    /**
     * json转化为对象
     *
     * @param jsonFile
     * @param t
     * @return
     */
    public static <T> T jsonToObject(File jsonFile, Class<T> t) {
        T rt = null;
        try {
            rt = MAPPER.readValue(jsonFile, t);
        } catch (JsonParseException e) {
            LOGGER.error("文件：" + jsonFile + " 转化为对象" + t.getName() + " 时解析异常", e);
        } catch (JsonMappingException e) {
            LOGGER.error("文件：" + jsonFile + " 转化为对象" + t.getName() + " 时映射异常", e);
        } catch (IOException e) {
            LOGGER.error("文件：" + jsonFile + " 转化为对象" + t.getName() + " 时IO异常", e);
        }
        return rt;
    }

    /**
     * 将对象转换成json
     *
     * @param src 对象
     * @return 返回json字符串
     * @throws IOException
     */
    public static <T> String toJson(T src) {
        try {
            return src instanceof String ? (String) src : MAPPER.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            LOGGER.error("对象" + src.getClass().getName() + "转化为字符串时异常", e);
        }
        return null;
    }

    /**
     * 将对象转换成json, 传入配置对象
     *
     * @param src    对象
     * @param mapper 配置对象
     * @return 返回json字符串
     * @throws IOException
     * @see ObjectMapper
     */
    public static <T> String toJson(T src, ObjectMapper mapper) {
        if (null != mapper) {
            if (src instanceof String) {
                return (String) src;
            } else {
                try {
                    return mapper.writeValueAsString(src);
                } catch (JsonProcessingException e) {
                    LOGGER.error("对象" + src.getClass().getName() + "转化为字符串时异常", e);
                }
            }
        }
        return null;
    }

    /**
     * 返回{@link ObjectMapper ObjectMapper}对象, 用于定制性的操作
     *
     * @return {@link ObjectMapper ObjectMapper}对象
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }

}
