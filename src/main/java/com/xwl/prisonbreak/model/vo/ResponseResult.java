package com.xwl.prisonbreak.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/19 21:17
 * @Description: 返回值实体类
 */
@Data
@AllArgsConstructor
public class ResponseResult {
    @ApiModelProperty(value = "状态码", name = "code")
    private Integer code;

    @ApiModelProperty(value = "消息", name = "message")
    private String message;

    @ApiModelProperty(value = "数据", name = "data")
    private Object data;
}
