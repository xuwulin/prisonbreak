package com.xwl.prisonbreak.michael.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.prisonbreak.aop.CustomAopAnnotation;
import com.xwl.prisonbreak.michael.entity.SysUser;
import com.xwl.prisonbreak.common.vo.ResponseResult;
import com.xwl.prisonbreak.michael.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 19:54
 * @Description:
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户接口")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/userId")
    @ApiOperation("根据id查询，通过IService中的方法")
    public SysUser selectById(String id) {
        // 调用 baseMapper.selectById(id)
        // 其中selectById(Serializable id)中的id需要序列化，
        // 实体类中一定要重写！！！
        // protected Serializable pkVal() {
        //        return this.id;
        // }
        SysUser sysUser = sysUserService.getById(id);
        return sysUser;
    }

    @CustomAopAnnotation(param = "id", detail = "参数id")
    @GetMapping("/findByIdUseXml")
    @ApiOperation("根据id查询，通过写mapper.xml的方式实现")
    public SysUser findByIdUseXml(String id) {
        SysUser sysUser = sysUserService.findByIdUseXml(id);
        return sysUser;
    }

    @GetMapping("/findByIdUseAnnotation")
    @ApiOperation("根据id查询，通过注解的方式实现")
    public SysUser findByIdUseAnnotation(String id) {
        SysUser sysUser = sysUserService.findByIdUseAnnotation(id);
        return sysUser;
    }

    @GetMapping("/findOneByColumns")
    @ApiOperation("通过多个字段（多列）查询一条记录，构造LambdaQueryWrapper条件")
    public SysUser findOneByColumns() {
        // 构造查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getId, 1)
                .eq(SysUser::getUsername, "贾宝玉");
        // 执行的sql:SELECT id,username,nickname,password,role_id,dept_id,create_time,update_time,remark,deleted,email,phone,update_version FROM sys_user WHERE deleted=0 AND id = ? AND username = ?
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        return sysUser;
    }

    @GetMapping("/findOneByColumnsThrowEX")
    @ApiOperation("通过多个字段（多列）查询一条记录，构造LambdaQueryWrapper条件，如果匹配到多条是否需要抛出异常")
    public SysUser findOneByColumnsThrowEX() {
        // 构造查询条件
        LambdaQueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>()
                .lambda()
                .eq(SysUser::getUsername, "贾宝玉");
        // 执行的sql:SELECT id,username,nickname,password,role_id,dept_id,create_time,update_time,remark,deleted,email,phone,update_version FROM sys_user WHERE deleted=0 AND username = ?
        // 根据构造条件，如果匹配到多条是否需要抛出异常：true 是; false 否 ，如果为false,则只会返回查找到的第一条记录
        // Expected one result (or null) to be returned by selectOne(), but found: 2
        SysUser sysUser = sysUserService.getOne(queryWrapper, true);
        return sysUser;
    }

    @GetMapping("/findByIds")
    @ApiOperation("通过id集合查询")
    public Collection<SysUser> findByIds() {
        List<Integer> ids = Arrays.asList(1, 2);
        // 执行sql: WHERE id IN ( ? , ? ) AND deleted=0
        Collection<SysUser> sysUsers = sysUserService.listByIds(ids);

        return sysUsers;
    }

    @GetMapping("/findResultMap")
    @ApiOperation("查询返回map")
    public Map<String, Object> findResultMap() {
//        new QueryWrapper<SysUser>().select("username");
        // 如果查询到有多条，只会返回第一条数据
        Map<String, Object> map = sysUserService.getMap(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, "贾宝玉"));

        return map;
    }

    @GetMapping("/findResultListMap")
    @ApiOperation("查询返回List<Map<String, Object>>")
    public ResponseResult<List<Map<String, Object>>> findResultListMap() {
        // 执行sql:SELECT username,nickname FROM sys_user WHERE deleted=0
        // select(String... columns) 列明最好写数据库中的列名，写实体类中的属性名也可以（下划线和驼峰命名会自动转）
        List<Map<String, Object>> maps = sysUserService.listMaps(new QueryWrapper<SysUser>().select("username", "nickname"));

        return new ResponseResult<>(maps);
    }

    @GetMapping("/getPageByWrapper")
    @ApiOperation("Wrapper条件构造分页查询")
    public IPage<SysUser> getPageByWrapper(Integer page, Integer pageSize) {
        // 真分页
        // 执行sql:FROM sys_user WHERE deleted=0 AND username LIKE ? LIMIT ?,?
        IPage<SysUser> userIPage = sysUserService.page(new Page<>(page, pageSize), new QueryWrapper<SysUser>().likeRight("username", "柳"));
        return userIPage;
    }

    @GetMapping("/getPage")
    @ApiOperation("分页查询")
    public ResponseResult<IPage<SysUser>> getPage(Integer page, Integer pageSize) {
        IPage<SysUser> userIPage = sysUserService.page(new Page<>(page, pageSize));
        return new ResponseResult<>(userIPage);
    }

    @GetMapping("/getPageByXml")
    @ApiOperation("xml方式分页查询")
    public ResponseResult<IPage<SysUser>> getPageByXml(Integer page, Integer pageSize, String name) {
        IPage<SysUser> userIPage = sysUserService.getPageByXml(page, pageSize, name);
        return new ResponseResult<>(userIPage);
    }

    @GetMapping("/listContentByDynamicTableName")
    @ApiOperation("根据表名获取该表内容（动态传入表名）")
    public ResponseResult<List<Map<String, Object>>> listContentByDynamicTableName(String tableName) {
        List<Map<String, Object>> res = sysUserService.listContentByDynamicTableName(tableName);
        return new ResponseResult<>(res);
    }

    @PostMapping("insert")
    @ApiOperation("新增一条数据，返回新增的id")
    public Integer insert(@RequestBody SysUser sysUser) {
        Integer res = sysUserService.insert(sysUser);

        return res;
    }

    @GetMapping("updateById")
    @ApiOperation("根据id更新一条数据")
    public Boolean updateById() {
        SysUser user = new SysUser();
        user.setId(102);
        user.setUsername("李四");
        // 调用的是 baseMapper.updateById(entity)
        // 也会忽略null值
        // 执行sql: UPDATE sys_user SET username=?, update_time=? WHERE id=? AND deleted=0
        boolean res = sysUserService.updateById(user);
        return res;
    }

    @DeleteMapping("delById")
    @ApiOperation("根据id删除一条数据")
    public Boolean delById() {
        // 执行sql: UPDATE sys_user SET deleted=1 WHERE id=? AND deleted=0
        boolean res = sysUserService.removeById(105);
        return res;
    }

    @DeleteMapping("delByMap")
    @ApiOperation("根据Map删除一条数据，")
    public Boolean delByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "张三");
        map.put("nickname", "张三疯");
        // 执行sql: 如果实体类中使用注解：@TableLogic 标注了是否删除字段
        // 则sql为（软删除）：UPDATE sys_user SET deleted=1 WHERE nickname = ? AND username = ? AND deleted=0
        // 否则为（真删除）：DELETE FROM sys_user WHERE nickname = ? AND username = ?
        boolean res = sysUserService.removeByMap(map);
        return res;
    }

    @GetMapping("/saveBatch")
    @ApiOperation("批量添加")
    public Boolean saveBatch() {
        List<SysUser> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SysUser user = new SysUser()
                    .setUsername("柳湘莲" + i)
                    .setNickname("冷二郎" + i)
                    .setPassword("123456")
                    .setDeptId(i)
                    .setDeptName("部门" + 1)
                    .setEmail("lengerlang@gmail.com")
                    .setPhone("18888888888")
                    .setRoleId(i)
                    .setRoleName("酷哥");
            userList.add(user);
        }
        boolean saveRes = sysUserService.saveBatch(userList);

        if (!saveRes) {
            return false;
        }
        return true;
    }

    @GetMapping("/testVersion")
    @ApiOperation(value = "测试乐观锁")
    public Boolean testVersion() {
        SysUser sysSysUser = new SysUser();
        sysSysUser.setId(2)
                .setUsername("林黛玉")
                .setNickname("颦儿")
                .setPassword("123456")
                .setUpdateVersion(1);
        boolean res = sysUserService.updateById(sysSysUser);

        if (!res) {
            return false;
        }
        return true;
    }

    @GetMapping("/updateByIdXml")
    @ApiOperation(value = "根据id修改,这种方式要想修改系统字段update_time，只有在xml中显示声明")
    public ResponseResult updateByIdXml(String id, String nickName) {
        int res = sysUserService.updateByIdXml(id, nickName);
        return new ResponseResult(res);
    }

    @PostMapping("/getByIds")
    @ApiOperation(value = "根据ids查询")
    public ResponseResult getByIds(@RequestBody List<String> ids) {
        List<SysUser> byIds = sysUserService.getByIds(ids);
        return new ResponseResult(byIds);
    }

}
