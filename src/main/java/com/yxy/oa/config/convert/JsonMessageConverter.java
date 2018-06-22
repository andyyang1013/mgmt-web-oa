package com.yxy.oa.config.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.vo.ResponseT;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 消息转换器
 **/
public class JsonMessageConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void init(ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //日期统一格式化
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        /** 处理返回数字转字符串 */
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        super.init(objectMapper);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        //针对swagger的接口不做处理
        String uiConfiguration = "springfox.documentation.swagger.web.UiConfiguration";
        if (uiConfiguration.equals(type.getTypeName())
                || "java.util.List<springfox.documentation.swagger.web.SwaggerResource>".equals(type.getTypeName())
                || "springfox.documentation.spring.web.json.Json".equals(type.getTypeName())
                || "springfox.documentation.swagger.web.SecurityConfiguration".equals(type.getTypeName())
                ) {
            super.writeInternal(object, type, outputMessage);
            return;
        }
        ResponseT resEntity = new ResponseT();
        if (object instanceof ResponseT) {
            resEntity = (ResponseT) object;
        } else {
            resEntity.setCode(CodeMsg.success.getCode());
            resEntity.setData(object);
        }
        super.writeInternal(resEntity, type, outputMessage);
    }
}
