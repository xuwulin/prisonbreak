package com.xwl.prisonbreak.exception;

import com.xwl.prisonbreak.pojo.vo.ResponseInfo;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:51
 * @Description:
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = -778142600038732285L;
    private String message;
    private String code;

    public BusinessException(String message) {
        this.message = message;
        this.code = ResponseInfo.CODE_ERROR;
    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public ResponseInfo<Object> getResponse() {
        return new ResponseInfo<Object>(code, message, null);
    }
}
