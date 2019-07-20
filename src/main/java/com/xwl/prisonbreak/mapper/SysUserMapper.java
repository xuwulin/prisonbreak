package com.xwl.prisonbreak.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.prisonbreak.pojo.po.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 10:52
 * @Description: 用户 Mapper 接口
 */
// 这里可以使用@Mapper注解，但是每个mapper都加注解比较麻烦，所以统一配置@MapperScan在扫描路径，在MybatisPlusConfig类中
//@Mapper //声明是一个Mapper,与MybatisPlusConfig中的@MapperScan（也可直接写在启动类中）二选一写上即可
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据id查询
     * 使用mapper.xml的方式写查询语句
     * @param id
     * @return
     */
    SysUser findByIdUseXml(@Param("id") String id);

    /**
     * 根据id查询
     * 使用注解的方式
     * @param id
     * @return
     */
    @Select("SELECT * FROM sys_user t WHERE t.id = #{id}")
    SysUser findByIdUseAnnotation(@Param("id") String id);

    /**
     * 根据动态表名查询内容。
     * 注意：必须使用 ${}，使用 #{}会查询报错
     * @param tableName 表名
     * @return
     */
    @Select("SELECT * FROM ${tableName}")
    List<Map<String, Object>> listContentByDynamicTableName(@Param("tableName") String tableName);

    List<SysUser> findByNameUseXml(@Param("name") String name, Page page);

    /**
     * 根据id修改，xml方式
     * @param id
     * @param nickname
     * @return
     */
    int updateByIdXml(@Param("id") String id, @Param("nickname") String nickname);

    /**
     * 使用<foreach></foreach>标签
     * @param ids
     * @return
     */
    List<SysUser> getByIds(@Param("ids") List<String> ids);
}
