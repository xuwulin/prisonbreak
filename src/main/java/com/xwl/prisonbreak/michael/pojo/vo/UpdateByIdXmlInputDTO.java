package com.xwl.prisonbreak.michael.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2019-11-06 9:30
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateByIdXmlInputDTO implements Serializable {
    @ApiModelProperty(name = "id", value = "id")
    private String id;

    @ApiModelProperty(name = "nickName", value = "昵称")
    private String nickName;
}
