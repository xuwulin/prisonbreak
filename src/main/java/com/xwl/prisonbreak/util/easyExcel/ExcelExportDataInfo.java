package com.xwl.prisonbreak.util.easyExcel;

import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/17 9:34
 * @Description: 导出excel的数据信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportDataInfo {

    /**
     * sheet名
     */
    private String sheetName;

    /**
     * 导出数据的对象类型，如：ExportUserInfo
     */
    private Object type;

    /**
     * 导出数据
     */
    private List<? extends BaseRowModel> data;
}
