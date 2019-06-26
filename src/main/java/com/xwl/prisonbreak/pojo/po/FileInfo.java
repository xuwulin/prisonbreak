package com.xwl.prisonbreak.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.InputStream;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 20:25
 * @Description: 文件信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "file_info")
// 继承父类的元素，生成的equals和hashCode方法应该包含父类在内
@EqualsAndHashCode(callSuper = true)
public class FileInfo extends BaseEntity {

    /**
     * 主键，32位UUID
     */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键id", name = "id")
    private String id;

    /**
     * 资源id
     */
    @TableField("resource_id")
    @ApiModelProperty(value = "资源id", name = "resourceId")
    private String resourceId;

    /**
     * 文件名
     */
    @TableField("file_name")
    @ApiModelProperty(value = "文件名", name = "fileName")
    private String fileName;

    /**
     * 原始文件名称
     */
    @TableField("file_origin_name")
    @ApiModelProperty(value = "原始文件名称", name = "fileOriginName")
    private String fileOriginName;

    /**
     * 上传后的文件路径
     */
    @TableField("file_path")
    @ApiModelProperty(value = "上传后的文件路径", name = "filePath")
    private String filePath;

    /**
     * 文件类型
     */
    @TableField("file_type")
    @ApiModelProperty(value = "文件类型", name = "fileType")
    private String fileType;

    /**
     * 文件路径md5加密
     */
    @TableField("md5")
    @ApiModelProperty(value = "文件路径md5加密", name = "md5")
    private String md5;

    /**
     * 文件是否有效(true/1: 有效 ;false/0: 无效)
     */
    @TableField("valid")
    @ApiModelProperty(value = "文件是否有效(true/1: 有效 ;false/0: 无效)", name = "valid")
    private Boolean valid = true;

    /**
     * 文件大小，单位B（Byte）
     */
    @TableField("size")
    @ApiModelProperty(value = "文件大小，单位B（Byte）", name = "size")
    private Long size;

    /**
     * 删除时间
     * 逻辑删除时，此值不能被赋值
     */
//    @TableField("delete_time")
//    private String deleteTime;

    /**
     * 文件内容
     * 数据库中不存在此字段
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "文件内容", name = "content")
    private InputStream content;

}
