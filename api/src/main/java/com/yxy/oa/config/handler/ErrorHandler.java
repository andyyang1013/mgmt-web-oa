package com.yxy.oa.config.handler;/**
 * Created by dell on 2017/11/22.
 */

import com.yxy.oa.exception.BizException;
import com.yxy.oa.exception.CodeMsg;
import com.yxy.oa.util.JacksonUtil;
import com.yxy.oa.vo.ResponseT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 异常处理
 **/
@ControllerAdvice
public class ErrorHandler {
    private Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * 统一异常处理
     *
     * @param exception exception
     * @return
     */
    @ExceptionHandler({Exception.class})
    public Object jsonErrorHandler(HttpServletResponse response, Exception exception) {
        ResponseT<String> resEntity = new ResponseT<>();
        CodeMsg exceptionCode = null;
        if (exception instanceof BizException) {
            BizException bizExcepton = (BizException) exception;
            resEntity.setCode(bizExcepton.getExceptionCode());
            resEntity.setMsg(bizExcepton.getMessage());
            resEntity.setData(null);
        } else if (exception instanceof TypeMismatchException) {
            exceptionCode = CodeMsg.param_type_not_match;
            logger.error(CodeMsg.param_type_not_match.getMsg() + "：", exception);
            resEntity.setCode(exceptionCode.getCode());
            resEntity.setMsg(exceptionCode.getMsg());
            resEntity.setData(null);
        } else if (exception instanceof BindException) {
            exceptionCode = CodeMsg.param_type_not_match;
            List<FieldError> fieldErrors = ((BindException) exception).getFieldErrors();
            StringBuffer msgBuffer = new StringBuffer();
            for (FieldError fieldError : fieldErrors) {
                msgBuffer.append(fieldError.getDefaultMessage()).append(";");
            }
            if (msgBuffer.length() > 0) {
                msgBuffer.deleteCharAt(msgBuffer.length() - 1);
            }
            resEntity.setCode(exceptionCode.getCode());
            resEntity.setMsg(msgBuffer.toString());
            resEntity.setData(null);
            logger.error(CodeMsg.param_type_not_match.getMsg() + "：", exception);
        } else if (exception instanceof NoHandlerFoundException) {
            exceptionCode = CodeMsg.resource_not_found;
            resEntity.setCode(exceptionCode.getCode());
            resEntity.setMsg(exceptionCode.getMsg());
            resEntity.setData(null);
        } else {
            exceptionCode = CodeMsg.system_error;
            resEntity.setCode(exceptionCode.getCode());
            resEntity.setMsg(exceptionCode.getMsg());
            resEntity.setData(null);
            logger.error("系统异常：", exception);
        }

        try {
            response.setContentType("application/json;charset=UTF-8");
//            response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            response.setStatus(HttpStatus.OK.value());
            PrintWriter writer = response.getWriter();
            writer.write(JacksonUtil.toJson(resEntity));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            return new BizException(CodeMsg.system_error);
        }
        return null;
    }
}
