package com.xwl.prisonbreak.michael.controller;

import com.alibaba.fastjson.JSONObject;
import com.xwl.prisonbreak.michael.entity.SysUser;
import com.xwl.prisonbreak.michael.pojo.vo.ExportUserInfo;
import com.xwl.prisonbreak.michael.pojo.vo.ExportUserInfoMultiLineHead;
import com.xwl.prisonbreak.michael.pojo.vo.ImportUserInfo;
import com.xwl.prisonbreak.michael.service.SysUserService;
import com.xwl.prisonbreak.common.util.easyExcel.ExcelExportDataInfo;
import com.xwl.prisonbreak.common.util.easyExcel.ExcelUtil;
import com.xwl.prisonbreak.common.util.poiExcel.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 19:59
 * @Description: excel导入导出
 * 更多POI导入导出操作见com.xwl.prisonbreak.util.poiExcel.test.test
 */
@RestController
@RequestMapping("/api/excel")
@Api(tags = "excel导入导出（EasyExcel/POI）")
public class ExcelController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/exportUserInfoByEasyExcel")
    @ApiOperation(value = "导出用户数据,单个sheet(easyExcel)")
    public boolean exportUserInfoByEasyExcel() throws IOException {
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
    public boolean exportUserInfoWithSheetsByEasyExcel() throws IOException {
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
    public boolean importUserInfoByEasyExcel(@RequestParam("file") MultipartFile file) throws IOException {
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
    public boolean importUserInfoByPOI(@RequestParam("file") MultipartFile file) throws IOException {
        // 将文件解析成字符串
        String json = ExcelUtils.getJsonStrFromExcel(file);
        if (StringUtils.isBlank(json)) {
            return false;
        }
        List<ExportUserInfo> importUserInfos = JSONObject.parseArray(json, ExportUserInfo.class);

        return true;
    }
}
