package com.xwl.prisonbreak.michael.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2019-11-08 11:28
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InsertWithNameInputDTO implements Serializable {
    @ApiModelProperty(name = "username", value = "姓名")
    private String username;

    @ApiModelProperty(name = "nickName", value = "昵称")
    private String nickName;
}
