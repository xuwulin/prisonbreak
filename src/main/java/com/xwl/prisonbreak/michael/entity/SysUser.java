package com.xwl.prisonbreak.michael.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 10:13
 * @Description: 用户实体
 * <p>
 * AR模式：Active Record(活动记录)，是一种领域模型模式，特点是一个模型类对应关系型数据库中的一个表，
 * 而模型类的一个实例对应表中的一行记录。ActiveRecord 一直广受动态语言（ PHP 、 Ruby 等）的喜爱，
 * 而 Java 作为准静态语言，对于 ActiveRecord 往往只能感叹其优雅，所以 MP 也在 AR 道路上进行了一定的探索，
 * 仅仅需要让实体类继承 Model 类且实现主键指定方法，即可开启 AR 之旅。接下来看具体代码：
 * <p>
 * 如果不使用AR模式，则可以不用继承Model类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
// mybatis-plus默认开启下划线转驼峰命名，即数据库表为sys_user，实体类为SysUser,会自动映射，因此此处可以不适用@TableName(value = "sys_user")
//@TableName(value = "sys_user")
// oracle数据库主键使用序列，则需要使用@KeySequence注解，value为序列的名字，clazz为主键对应实体属性的类型
//@KeySequence(value = "seq_user", clazz = Integer.class)
public class SysUser extends Model<SysUser> {

    /**
     * 主键
     */
    @TableId("id")
//    @TableId(type = IdType.INPUT) oracle使用
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    /**
     * 用户名
     */
    @TableField("username")
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    /**
     * 别名
     */
    @TableField("nickname")
    @ApiModelProperty(value = "别名", name = "nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField("password")
    @ApiModelProperty(value = "密码", name = "password")
    private String password;

    /**
     * 权限id
     */
    @TableField("role_id")
    @ApiModelProperty(value = "权限id", name = "roleId")
    private Integer roleId;

    /**
     * 权限名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "权限名称", name = "roleName")
    private String roleName;

    /**
     * 部门id
     */
    @TableField("dept_id")
    @ApiModelProperty(value = "部门id", name = "deptId")
    private Integer deptId;

    /**
     * 部门名称
     * exist = false 表示在数据库中不存在此列
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称", name = "deptName")
    private String deptName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间", name = "updateTime")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    /**
     * 是否删除(0:未删除 1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    @ApiModelProperty(value = "是否删除(0:未删除 1:已删除)", name = "deleted")
    private Integer deleted;

    /**
     * 乐观锁
     */
    @TableField("update_version")
    @ApiModelProperty(value = "乐观锁", name = "updateVersion")
    @Version
    private Integer updateVersion;

    /**
     * 邮件
     */
    @TableField("email")
    @ApiModelProperty(value = "邮件", name = "email")
    private String email;

    /**
     * 电话
     */
    @TableField("phone")
    @ApiModelProperty(value = "电话", name = "phone")
    private String phone;

    /**
     * 范回此类的主键
     *
     * @return
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
