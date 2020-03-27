package com.xwl.prisonbreak.michael.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2020-03-27 14:29
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInputDto implements Serializable {
    private String username;
    private String password;
}
