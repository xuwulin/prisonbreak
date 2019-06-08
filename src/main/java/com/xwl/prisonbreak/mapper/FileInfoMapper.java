package com.xwl.prisonbreak.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.prisonbreak.model.po.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:07
 * @Description: 文件 Mapper 接口
 */
public interface FileInfoMapper extends BaseMapper<FileInfo> {

    @Select("SELECT * FROM file_info t WHERE t.file_name = #{fileName}")
    FileInfo findByFileName(@Param("fileName") String fileName);

    @Select("SELECT * FROM file_info t WHERE t.file_name = #{fileName} AND t.valid = #{valid}")
    FileInfo findByFileNameAndValid(@Param("fileName") String fileName, @Param("valid") Boolean valid);

    @Select("SELECT * FROM file_info t WHERE t.valid = #{valid}")
    List<FileInfo> findByValid(@Param("valid") Boolean valid);

    @Select("SELECT * FROM file_info t WHERE t.resource_id = #{resourceId}")
    List<FileInfo> findByResourceId(String resourceId);

}
