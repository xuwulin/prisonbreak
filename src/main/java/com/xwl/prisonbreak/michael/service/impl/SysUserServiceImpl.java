package com.xwl.prisonbreak.michael.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwl.prisonbreak.michael.mapper.SysUserMapper;
import com.xwl.prisonbreak.michael.entity.SysUser;
import com.xwl.prisonbreak.michael.pojo.vo.DelByIdAndNickNameInputDTO;
import com.xwl.prisonbreak.michael.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 10:55
 * @Description: 用户 服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findByIdUseXml(String id) {
        SysUser res = sysUserMapper.findByIdUseXml(id);
        return res;
    }

    @Override
    public SysUser findByIdUseAnnotation(String id) {
        SysUser res = sysUserMapper.findByIdUseAnnotation(id);
        return res;
    }

    @Override
    public Integer insert(SysUser sysUser) {
        // 插入，返回新增的条数
        // 注意：insert方法会忽略sysUser中为null的字段，但是设置了自动填充的字段，即使为null也会被插入，如create_time, update_time
        int insert = sysUserMapper.insert(sysUser);

        // 获取id，数据库主键自增，新增后会自动将新的id注入到sysUser中，可以通过getId()获取新增记录id值
        Integer id = sysUser.getId();

        return id;
    }

    @Override
    public List<Map<String, Object>> listContentByDynamicTableName(String tableName) {
        List<Map<String, Object>> res = sysUserMapper.listContentByDynamicTableName(tableName);
        return res;
    }

    @Override
    public Page<SysUser> getPageByXml(Integer page, Integer pageSize, String name) {
        Page<SysUser> pageable = new Page<>(page, pageSize);
        List<SysUser> byNameUseXml = sysUserMapper.findByNameUseXml(name, pageable);
        Page<SysUser> res = pageable.setRecords(byNameUseXml);
        return res;
    }

    @Override
    public int updateByIdXml(String id, String nickname) {
        int res = sysUserMapper.updateByIdXml(id, nickname);
        return res;
    }

    @Override
    public List<SysUser> getByIds(List<String> ids) {
        List<SysUser> byIds = sysUserMapper.getByIds(ids);
        return byIds;
    }

    @Override
    public int delByIdAndNickName(DelByIdAndNickNameInputDTO inputDTO) {
        int i = sysUserMapper.delByIdAndNickName(inputDTO);
        return i;
    }

    @Override
    public SysUser findByUsernameAndPassword(String username, String password) {
        return sysUserMapper.findByUsernameAndPassword(username, password);
    }


}
