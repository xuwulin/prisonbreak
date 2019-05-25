package com.xwl.prisonbreak.util.easyExcel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 16:06
 * @Description:
 */
public class ExcelWriterFactroy extends ExcelWriter {
    private OutputStream outputStream;
    private int sheetNo = 1;

    public ExcelWriterFactroy(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactroy write(List<? extends BaseRowModel> list, String sheetName,
                                    BaseRowModel object) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, object.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
