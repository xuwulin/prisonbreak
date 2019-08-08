package com.xwl.prisonbreak.common.util.easyExcel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 16:03
 * @Description: 工具类
 */
public class ExcelUtil {

    // ==========================================读取/导入Excel==========================================================
    /**
     * 读取 Excel(多个 sheet,注意每个sheet对应的实体类必须一致！！！)
     * 不一致会报异常：java.lang.NumberFormatException: For input string: "id"
     *
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @return Excel 数据 list
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        for (Sheet sheet : reader.getSheets()) {
            if (rowModel != null) {
                sheet.setClazz(rowModel.getClass());
            }
            reader.read(sheet);
        }
        return excelListener.getDatas();
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel    文件
     * @param rowModel 实体类映射，继承 BaseRowModel 类
     * @param sheetNo  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, Integer sheetNo) {
        return readExcel(excel, rowModel, sheetNo, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param rowModel    实体类映射，继承 BaseRowModel 类
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel,
                                         Integer sheetNo, Integer headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        Sheet sheet = new Sheet(sheetNo, headLineNum, rowModel.getClass());
        reader.read(sheet);
        return excelListener.getDatas();
    }

    /**
     * 返回 ExcelReader
     *
     * @param excel         需要解析的 Excel 文件
     * @param excelListener new ExcelListener()
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();
        if (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx"))) {
            throw new ExcelException("文件格式错误！");
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(excel.getInputStream());
            ExcelReader excelReader = new ExcelReader(inputStream, null, excelListener, false);
            return excelReader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================导出Excel==========================================================
    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param object    映射实体类，Excel 模型
     */
    public static void writeExcel(String storageAdress, String fileName, String sheetName, String suffixName,
                                  List<? extends BaseRowModel> list, BaseRowModel object) {

        OutputStream out = null;
        try {
            String filePath = "";
            suffixName = suffixName.toLowerCase();
            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.XLSX;
            if (StringUtils.isBlank(fileName)) {
                fileName = "默认导出文件名";
            }
            if (StringUtils.isBlank(sheetName)) {
                sheetName = "第一个sheet";
            }
            if (suffixName.startsWith(".")) {
                suffixName = suffixName.substring(1);
            }
            if (StringUtils.equals(suffixName, "xls")) {
                excelTypeEnum = ExcelTypeEnum.XLS;
            }
            // 如果存储位置为空（storageAdress为空）则文件路劲为当前项目的根目录
            if (StringUtils.isBlank(storageAdress)) {
                filePath = fileName;
            } else {
                filePath = storageAdress + fileName;
            }

            File file = new File(storageAdress);
            // 如果文件路径不存在，则创建
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
            out = new FileOutputStream(filePath + "." + suffixName);
            ExcelWriter writer = new ExcelWriter(out, excelTypeEnum);
            Sheet sheet = new Sheet(1, 0, object.getClass());
            sheet.setSheetName(sheetName);
            writer.write(list, sheet);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 推荐使用此方式！！！
     *
     * 导出 Excel ：多个 sheet或单个sheet，带表头
     * @param storageAdress 存储位置，如果存储位置为空，则默认导出路劲为当前项目的根路径
     * @param fileName 导出的文件名
     * @param suffixName 后缀名，默认xlsx，带点或不带点均可（.xlsx、xlsx、.xls、xls）
     * @param list 要导出的数据集合，包含sheet名，数据集合，对象类型
     *
     */
    public static void writeExcelWithSheets(String storageAdress, String fileName,
                                            String suffixName, List<ExcelExportDataInfo> list) {

        OutputStream out = null;
        try {
            String filePath = "";
            suffixName = suffixName.toLowerCase();
            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.XLSX;
            if (StringUtils.isBlank(fileName)) {
                fileName = "默认导出文件名";
            }
            if (suffixName.startsWith(".")) {
                suffixName = suffixName.substring(1);
            }
            if (StringUtils.equals(suffixName, "xls")) {
                excelTypeEnum = ExcelTypeEnum.XLS;
            }
            // 如果存储位置为空（storageAdress为空）则文件路劲为当前项目的根目录
            if (StringUtils.isBlank(storageAdress)) {
                filePath = fileName;
            } else {
                filePath = storageAdress + fileName;
            }

            File file = new File(storageAdress);
            // 如果文件路径不存在，则创建
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }

            out = new FileOutputStream(filePath + "." + suffixName);
            ExcelWriter writer = new ExcelWriter(out, excelTypeEnum);

            for (int i = 0; i < list.size(); i++) {
                Class<? extends BaseRowModel> clazz = (Class<? extends BaseRowModel>) list.get(i).getType().getClass();
                Sheet sheet = new Sheet(i + 1, 0, clazz);
                sheet.setSheetName(list.get(i).getSheetName());
                writer.write(list.get(i).getData(), sheet);
            }

            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        OutputStream out = null;
        try {
            out = new FileOutputStream("E:\\" + fileName+ ".xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }


}
