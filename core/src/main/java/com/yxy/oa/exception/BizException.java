package com.yxy.oa.exception;


import lombok.Getter;

/**
 * 系统业务异常类
 **/
public class BizException extends RuntimeException {

    @Getter
    private CodeMsg codeMsg;

    public BizException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.codeMsg = codeMsg;
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable e) {
        super(message, e);
    }

    public BizException(CodeMsg codeMsg, Throwable e) {
        super(codeMsg.getMsg(), e);
        this.codeMsg = codeMsg;
    }

    public String getExceptionCode() {
        return codeMsg == null ? "biz_exception" : codeMsg.getMsg();
    }

    @Override
    public String getMessage() {
        return codeMsg == null ? super.getMessage() : codeMsg.getMsg();
    }
}