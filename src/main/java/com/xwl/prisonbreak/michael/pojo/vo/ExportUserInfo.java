package com.xwl.prisonbreak.michael.pojo.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 16:33
 * @Description: 导出用户信息
 * @ExcelProperty(value = "姓名",index = 0) value是表头数据，默认会写在excel的表头位置，index代表第几列
 */
@Data
public class ExportUserInfo extends BaseRowModel {
    @ExcelProperty(value = "id", index = 0)
    private Integer id;

    @ExcelProperty(value = "用户名", index = 1)
    private String username;

    @ExcelProperty(value = "昵称", index = 2)
    private String nickname;

    @ExcelProperty(value = "密码", index = 3)
    private String password;

    @ExcelProperty(value = "权限id", index = 4)
    private Integer roleId;

    @ExcelProperty(value = "部门id", index = 5)
    private Integer deptId;

    @ExcelProperty(value = "创建时间", index = 6)
    private Date createTime;

    @ExcelProperty(value = "修改时间", index = 7)
    private Date updateTime;

    @ExcelProperty(value = "备注", index = 8)
    private String remark;

    @ExcelProperty(value = "是否删除(0:未删除 1:已删除)", index = 9)
    private Integer deleted;

    @ExcelProperty(value = "乐观锁", index = 10)
    private Integer updateVersion;

    @ExcelProperty(value = "邮箱", index = 11)
    private String email;

    @ExcelProperty(value = "电话", index = 12)
    private String phone;

}
