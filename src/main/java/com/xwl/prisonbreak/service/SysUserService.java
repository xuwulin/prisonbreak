package com.xwl.prisonbreak.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xwl.prisonbreak.model.po.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 10:54
 * @Description: 用户服务类
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据id查找，通过mapper.xml的方式
     * @param id
     * @return
     */
    SysUser findByIdUseXml(String id);

    /**
     * 根据id查找，通过注解的方式
     * @param id
     * @return
     */
    SysUser findByIdUseAnnotation(String id);

    /**
     * 新增一条数据，返回新增后的id
     * @param sysUser
     * @return
     */
    Integer insert(SysUser sysUser);

    List<Map<String, Object>> listContentByDynamicTableName(String tableName);

    IPage<SysUser> getPageByXml(Integer page, Integer pageSize, String name);
}
