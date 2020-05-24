package com.xwl.prisonbreak.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2020-02-28 13:35
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SysLog implements Serializable {

    private static final long serialVersionUID = 3620752365876776411L;
    /**
     * 操作人
     */
    private String username;

    /**
     * 操作
     */
    private String operation;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求IP地址
     */
    private String ip;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求方法，GET/POST/PUT/DELETE
     */
    private String httpMethod;

    /**
     * 方法名，全类名 + 方法名
     */
    private String method;

    /**
     * 参数，json格式
     */
    private String params;

    /**
     * 响应，json格式
     */
    private String response;

    /**
     * 异常信息，json格式
     */
    private String exception;

    /**
     * 操作时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String operateDate;

    /**
     * 耗时，单位：毫秒
     */
    private String timeConsuming;
}
