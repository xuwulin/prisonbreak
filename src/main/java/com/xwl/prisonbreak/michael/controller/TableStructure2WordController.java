package com.xwl.prisonbreak.michael.controller;

import com.xwl.prisonbreak.common.util.tableStructure2word.AllTables;
import com.xwl.prisonbreak.common.util.tableStructure2word.TableStructure;
import com.xwl.prisonbreak.common.util.tableStructure2word.Template2WordGeneratorUtil;
import com.xwl.prisonbreak.michael.mapper.TableStructureMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


@RestController
@RequestMapping("/api/tableStrToWord")
@Api(tags = "表结构导出为word")
public class TableStructure2WordController {

    @Autowired
    TableStructureMapper tableStructureMapper;

    @GetMapping("/export")
    @ApiOperation("导出为word")
    public void download(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        Map<String, List<AllTables>> resMap = new HashMap<>();
        List<AllTables> allTablesList = new ArrayList<>();
        // 从数据库查询出的数据
//        List<TableStructure> tableStrList = tableStructureMapper.listTableStructure();

        // 模拟数据
        List<TableStructure> tableStrList = new ArrayList<>();
        tableStrList.add(new TableStructure("tbl_user", "用户表", "id", "id", "int", "Y", "Y", ""));
        tableStrList.add(new TableStructure("tbl_user", "用户表", "user_name", "姓名", "varchar", "N", "Y", ""));
        tableStrList.add(new TableStructure("tbl_user", "用户表", "age", "年龄", "int", "N", "N", ""));
        tableStrList.add(new TableStructure("tbl_dept", "用户表", "id", "id", "int", "Y", "Y", ""));
        tableStrList.add(new TableStructure("tbl_dept", "用户表", "dept_no", "部门编号", "int", "N", "N", ""));
        tableStrList.add(new TableStructure("tbl_dept", "用户表", "dept_name", "部门名称", "varchar", "N", "N", ""));

        Map<String, List<TableStructure>> collect = tableStrList.stream()
                .collect(groupingBy(TableStructure::getTableName));

        for (Map.Entry<String, List<TableStructure>> map : collect.entrySet()) {
            String tableName = map.getKey();
            List<TableStructure> tableStrs = map.getValue();

            // 筛选出主键
            List<String> primaryKeys = tableStrs.stream().filter(item -> StringUtils.equals(item.getPrimary(), "Y"))
                    .map(TableStructure::getColumn)
                    .collect(Collectors.toList());

            // 复合主键之间适用、连接
            StringBuilder sb = new StringBuilder();
            for (String primaryKey : primaryKeys) {
                sb.append(primaryKey).append("、");
            }
            String primaryKeyStr = sb.toString();
            if (primaryKeyStr.length() > 1) {
                primaryKeyStr = primaryKeyStr.substring(0, sb.toString().length() - 1);
            }

            // 各个字段不能为null，否则Freemarker的模板引擎在处理时会因为找不到值而报错
            tableStrs.forEach(item -> {
                if (StringUtils.isBlank(item.getDefaultVal())) {
                    item.setDefaultVal("");
                }
                if (StringUtils.isBlank(item.getPrimary())) {
                    item.setPrimary("");
                }
                if (StringUtils.isBlank(item.getTableNameDesc())) {
                    item.setTableNameDesc("");
                }
                if (StringUtils.isBlank(item.getColumnName())) {
                    item.setColumnName("");
                }
                if (StringUtils.isBlank(item.getNotnull())) {
                    item.setNotnull("");
                }
            });


            AllTables allTables = new AllTables();
            allTables.setTableNameDesc(tableStrs.get(0).getTableNameDesc());
            allTables.setTableName(tableName);
            allTables.setPrimaryKey(primaryKeyStr);
            allTables.setTables(tableStrs);

            allTablesList.add(allTables);
        }
        resMap.put("listmap", allTablesList);

        // 提示：在调用工具类生成Word文档之前应当检查所有字段是否完整
        // 否则Freemarker的模板引擎在处理时会因为找不到值而报错
        File file = null;
        InputStream inputStream = null;
        ServletOutputStream out = null;
        try {
            // 调用工具类WordGenerator的createDoc方法生成Word文档
            file = Template2WordGeneratorUtil.createDoc(resMap);
            inputStream = new FileInputStream(file);

            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件默认名为resume.doc
            resp.addHeader("Content-Disposition", "attachment;filename=tableStructure.doc");

            out = resp.getOutputStream();
            // 缓冲区
            byte[] buffer = new byte[1024];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (out != null) {
                out.close();
            }
            if (file != null) {
                // 删除临时文件
                file.delete();
            }
        }
    }
}
