package com.xwl.prisonbreak.pojo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 15:09
 * @Description: 实体基类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间", name = "updateTime")
    private Date updateTime;

    /**
     * 是否删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    @ApiModelProperty(value = "是否删除", name = "deleted")
    private Integer deleted;

    /**
     * 乐观锁(默认值0)
     */
    @Version
    @TableField("update_version")
    @ApiModelProperty(value = "乐观锁", name = "updateVersion")
    private Integer updateVersion;
}
