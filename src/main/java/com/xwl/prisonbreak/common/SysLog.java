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
    private static final long serialVersionUID = 1L;

    /**
     * 操作人
     */
    private String username;

    /**
     * 操作
     */
    private String operation;

    /**
     * 方法
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 响应
     */
    private String response;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 操作时间
     */
    private String operateDate;
}
