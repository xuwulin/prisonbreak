package com.xwl.prisonbreak.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 16:33
 * @Description: 导入用户信息
 * 作为映射实体类，通过 @ExcelProperty 注解与 index 变量可以标注成员变量所映射的列，同时不可缺少 setter 方法
 */
@Data
public class ImportUserInfo extends BaseRowModel {
    /**
     * 主键
     */
    @ExcelProperty(index = 0)
    private Integer id;

    /**
     * 用户名
     */
    @ExcelProperty(index = 1)
    private String username;

    /**
     * 别名
     */
    @ExcelProperty(index = 2)
    private String nickname;

    /**
     * 密码
     */
    @ExcelProperty(index = 3)
    private String password;

    /**
     * 权限id
     */
    @ExcelProperty(index = 4)
    private Integer roleId;

    /**
     * 部门id
     */
    @ExcelProperty(index = 5)
    private Integer deptId;

    /**
     * 创建时间
     */
    @ExcelProperty(index = 6)
    private Date createTime;

    /**
     * 修改时间
     */
    @ExcelProperty(index = 7)
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelProperty(index = 8)
    private String remark;

    /**
     * 是否删除(0:未删除 1:已删除)
     */
    @ExcelProperty(index = 9)
    private Integer deleted;

    /**
     * 乐观锁
     */
    @ExcelProperty(index = 10)
    private Integer updateVersion;

    /**
     * 邮件
     */
    @ExcelProperty(index = 11)
    private String email;

    /**
     * 电话
     */
    @ExcelProperty(index = 12)
    private String phone;

}
