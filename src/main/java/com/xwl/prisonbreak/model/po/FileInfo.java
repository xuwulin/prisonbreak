package com.xwl.prisonbreak.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String id;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;

    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 原始文件名称
     */
    @TableField("file_origin_name")
    private String fileOriginName;

    /**
     * 上传后的文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * md5
     */
    @TableField("md5")
    private String md5;

    /**
     * 文件是否有效(true/1: 有效 ;false/0: 无效)
     */
    @TableField("valid")
    private Boolean valid = true;

    /**
     * 文件大小
     */
    @TableField("size")
    private Long size;

    /**
     * 删除时间
     */
    @TableField("delete_time")
    private String deleteTime;

    /**
     * 文件内容
     */
    @TableField(exist = false)
    private InputStream content;

}
