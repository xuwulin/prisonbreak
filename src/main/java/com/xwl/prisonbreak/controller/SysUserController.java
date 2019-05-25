package com.xwl.prisonbreak.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xwl.prisonbreak.model.po.SysUser;
import com.xwl.prisonbreak.model.vo.ExportUserInfo;
import com.xwl.prisonbreak.model.vo.ExportUserInfoMultiLineHead;
import com.xwl.prisonbreak.model.vo.ImportUserInfo;
import com.xwl.prisonbreak.service.SysUserService;
import com.xwl.prisonbreak.util.easyExcel.ExcelExportDataInfo;
import com.xwl.prisonbreak.util.easyExcel.ExcelUtil;
import com.xwl.prisonbreak.util.poiExcel.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/15 19:54
 * @Description:
 */
@RestController
@RequestMapping("/user")
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
    public Object findResultListMap() {
        // 执行sql:SELECT username,nickname FROM sys_user WHERE deleted=0
        // select(String... columns) 列明最好写数据库中的列名，写实体类中的属性名也可以（下划线和驼峰命名会自动转）
        List<Map<String, Object>> maps = sysUserService.listMaps(new QueryWrapper<SysUser>().select("username", "nickname"));

        return maps;
    }

    @GetMapping("/getPageByWrapper")
    @ApiOperation("Wrapper条件构造分页查询")
    public IPage<SysUser> getPageByWrapper(Integer page, Integer pageSize) {
        // 真分页
        // 执行sql:FROM sys_user WHERE deleted=0 AND username LIKE ? LIMIT ?,?
        IPage<SysUser> userIPage = sysUserService.page(new Page<>(1, 10), new QueryWrapper<SysUser>().likeRight("username", "柳"));
        return userIPage;
    }

    @GetMapping("/getPage")
    @ApiOperation("分页查询")
    public IPage<SysUser> getPage(Integer page, Integer pageSize) {
        IPage<SysUser> userIPage = sysUserService.page(new Page<>(page, pageSize));
        return userIPage;
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
    public Boolean saveBatch(){
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

    @GetMapping("/exportUserInfoByEasyExcel")
    @ApiOperation(value = "导出用户数据,单个sheet(easyExcel)")
    public Boolean exportUserInfoByEasyExcel() throws IOException {
        // 先查询数据
        List<SysUser> sysUserList = sysUserService.list();
        ModelMapper modelMapper = new ModelMapper();
        // 对象转换
        List<ExportUserInfo> exportUserInfoList =
                modelMapper.map(sysUserList, new TypeToken<List<ExportUserInfo>>(){}.getType());

        // 要导出的文件名
        String fileName = "用户信息";
        // sheet名
        String sheetName = "第一个sheet";
        // 存储位置
        String storageAdress = "E:\\excel导出位置\\";
        // 后缀名
        String suffixName = "xlsx";

        ExcelUtil.writeExcel(storageAdress, fileName, sheetName, suffixName, exportUserInfoList, new ExportUserInfo());

        return true;
    }

    @GetMapping("/exportUserInfoWithSheetsByEasyExcel")
    @ApiOperation(value = "导出用户数据,多个sheet(easyExcel)")
    public Boolean exportUserInfoWithSheetsByEasyExcel() throws IOException {
        // 先查询数据
        List<SysUser> sysUserList = sysUserService.list();
        ModelMapper modelMapper = new ModelMapper();
        // 对象转换
        List<ExportUserInfo> exportUserInfoList =
                modelMapper.map(sysUserList, new TypeToken<List<ExportUserInfo>>(){}.getType());

        // 要导出的文件名
        String fileName = "用户信息多sheet";
        // 存储位置
        String storageAdress = "E:\\excel导出位置\\";
        // 后缀名
        String suffixName = "xlsx";

        List<ExcelExportDataInfo> list = new ArrayList<>();
        list.add(new ExcelExportDataInfo("第一个sheet", new ExportUserInfo(), exportUserInfoList));
        list.add(new ExcelExportDataInfo("第二个sheet", new ExportUserInfoMultiLineHead(), exportUserInfoList));
        list.add(new ExcelExportDataInfo("第三个sheet", new ExportUserInfo(), exportUserInfoList));

        ExcelUtil.writeExcelWithSheets(storageAdress, fileName, suffixName, list);

        return true;
    }

    @PostMapping("/importUserInfoByEasyExcel")
    @ApiOperation("导入用户数据(easyExcel)")
    public Boolean importUserInfoByEasyExcel(@RequestParam("file") MultipartFile file) throws IOException {
        List<Object> objects = ExcelUtil.readExcel(file, new ImportUserInfo());
        // 要将List<Object>转为List<ImportUserInfo>
        // 需要先将List<Object>转为Object
        // 再将object转为List<ImportUserInfo>
        Object obj = objects;
        List<ImportUserInfo> importUserInfos = (List<ImportUserInfo>) obj;
        return true;

    }

    @PostMapping("/importUserInfoByPOI")
    @ApiOperation("导入用户数据(POI)")
    public Boolean importUserInfoByPOI(@RequestParam("file") MultipartFile file) throws IOException {
        // 将文件解析成字符串
        String json = ExcelUtils.getJsonStrFromExcel(file);
        if (StringUtils.isBlank(json)) {
            return false;
        }
        List<ExportUserInfo> importUserInfos = JSONObject.parseArray(json, ExportUserInfo.class);

        return true;
    }





}
