package com.xwl.prisonbreak.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.prisonbreak.model.po.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
