package com.xwl.prisonbreak.michael.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2020-05-19 23:11
 * @description
 */
@Data
public class Test3 implements Serializable {
    private static final long serialVersionUID = -867551940894343377L;

    private Test1 test1;
    private Test2 test2;
    private Integer userId;
}
