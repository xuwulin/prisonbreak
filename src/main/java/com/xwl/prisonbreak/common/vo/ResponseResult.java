package com.xwl.prisonbreak.common.vo;

import com.xwl.prisonbreak.common.enums.ResponseTypes;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/19 21:17
 * @Description: 响应结果，方式一：ResponseResult + ResponseTypes（枚举）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {
    @ApiModelProperty(value = "状态码", name = "code")
    private String code;

    @ApiModelProperty(value = "是否成功", name = "success")
    private boolean success;

    @ApiModelProperty(value = "消息", name = "message")
    private String message;

    @ApiModelProperty(value = "数据", name = "data")
    private T data;

    public ResponseResult(T data) {
        if (!(data instanceof ResponseTypes)) {
            this.success = true;
            this.code = ResponseTypes.SUCCESS.getCode();
            this.message = ResponseTypes.SUCCESS.getDesc();
            this.data = data;
        } else {
            boolean success = data == ResponseTypes.SUCCESS;
            this.success = success;
            this.code = success ? ResponseTypes.SUCCESS.getCode() : ((ResponseTypes) data).getCode();
            this.message = success ? ResponseTypes.SUCCESS.getDesc() : ((ResponseTypes) data).getDesc();
        }
    }

    public ResponseResult(String message, T data) {
        this.success = true;
        this.code = ResponseTypes.SUCCESS.getCode();
        this.message = message;
        this.data = data;
    }

    public ResponseResult(ResponseTypes type, String message) {
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = null;
    }

    public ResponseResult(ResponseTypes type, String message, T data) {
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
