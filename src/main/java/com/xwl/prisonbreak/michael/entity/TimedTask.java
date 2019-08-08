package com.xwl.prisonbreak.michael.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xwl.prisonbreak.common.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Auther: xwl
 * @Date: 2019/7/25 17:16
 * @Description: 定时任务实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "timed_task")
// 继承父类的元素，生成的equals和hashCode方法应该包含父类在内
@EqualsAndHashCode(callSuper = true)
public class TimedTask extends BaseEntity {
    @TableId(value = "id")
    @ApiModelProperty(value = "主键id", name = "id")
    private String id;

    @TableField("task_name")
    @ApiModelProperty(value = "任务名称", name = "taskName")
    private String taskName;

    @TableField("cron")
    @ApiModelProperty(value = "执行时间表达式", name = "cron")
    private String cron;

    @TableField("cron_desc")
    @ApiModelProperty(value = "执行时间表达式说明", name = "cronDesc")
    private String cronDesc;
}
