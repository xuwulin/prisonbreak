package com.xwl.prisonbreak.michael.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2020-05-19 22:58
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Test2 implements Serializable {
    private static final long serialVersionUID = -2346201297764752763L;

    private String id;
    private String name;
}
