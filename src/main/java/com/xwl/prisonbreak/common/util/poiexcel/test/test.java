package com.xwl.prisonbreak.common.util.poiexcel.test;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.common.util.poiexcel.ExcelUtils;
import com.xwl.prisonbreak.common.util.poiexcel.ExcelUtils.Column;
import com.xwl.prisonbreak.common.util.poiexcel.ExcelUtils.ExportRules;
import com.xwl.prisonbreak.common.util.poiexcel.ImportRspInfo;
import com.xwl.prisonbreak.common.util.poiexcel.POIException;
import com.xwl.prisonbreak.common.util.poiexcel.ProjectVerifyBuilder;
import com.xwl.prisonbreak.common.util.poiexcel.vo.ClassRoom;
import com.xwl.prisonbreak.common.util.poiexcel.vo.Parent;
import com.xwl.prisonbreak.common.util.poiexcel.vo.ProjectEvaluate;
import com.xwl.prisonbreak.common.util.poiexcel.vo.Student;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

;


/**
 * @Author: Bj
 * @CreateDate: 2019/5/7 21:57
 * @Description: 参考  https://gitee.com/stupid1t/small_tools
 * @Version: 1.0
 */
public class test {
    public static void main(String[] args) {
        //////////////////////简单导出(只支持xsl)//////////////////////
//        test1();
        //////////////////复杂表格导出(只支持xsl)///////////////////////
//        test2();
        //////////////////复杂的对象级联导出///////////////////////////
//        test3();
        /////////////////map对象的简单导出////////////////////////////
//        test4();
        ////////////////模板导出(支持xsl和xsls)////////////////////////
//        test5();
        ////////////////////简单的导入(读取)/////////////////
//        test6();
        //////////////////复杂导入，带图片导入，带回调处理//////////////
//        test7();
        //////////自定义校验器，导入需要校验字段,必须继承AbstractVerifyBuidler////////////
    }


    public static void test1() {
        // 1.获取导出的数据体
        List<ProjectEvaluate> data = new ArrayList<ProjectEvaluate>();
        for (int i = 0; i < 10; i++) {
            ProjectEvaluate obj = new ProjectEvaluate();
            obj.setProjectName("中青旅" + i);
            obj.setAreaName("华东长三角");
            obj.setProvince("河北省");
            obj.setCity("保定市");
            obj.setPeople("张三" + i);
            obj.setLeader("李四" + i);
            obj.setScount(50);
            obj.setAvg(60.0);
            obj.setCreateTime(new Date());
            obj.setImg(ExcelUtils.ImageParseBytes(new File("D:\\workspace_idea\\prisonbreak\\src\\main\\java\\com\\xwl\\prisonbreak\\util\\poiExcel\\file\\1.jpg")));
            data.add(obj);
        }
        // 2.导出标题设置，可为空
        String title = "项目资源统计";
        // 3.导出的hearder设置
        String[] hearder = {"序号", "项目名称", "所属区域", "省份", "市", "项目所属人", "项目领导人", "得分", "平均分", "创建时间", "项目图片"};
        // 4.导出hearder对应的字段设置
        ExcelUtils.Column[] column = {
                Column.field("projectName"),
                Column.field("areaName"),
                Column.field("province"),
                Column.field("city"),
                Column.field("people"),
                Column.field("leader"),
                Column.field("scount"),
                Column.field("avg"),
                Column.field("createTime"),
                // 项目图片
                Column.field("img")

        };
        // 5.执行导出到工作簿
        // ExportRules:1.是否序号;2.列设置;3.标题设置可为空;4.表头设置;5.表尾设置可为空
        Workbook bean = ExcelUtils.createWorkbook(data, new ExportRules(title, hearder, column, true, null));
        // 6.写出文件
        try {
            bean.write(new FileOutputStream("D:\\workspace_idea\\prisonbreak\\src\\main\\java\\com\\xwl\\prisonbreak\\util\\poiExcel\\file\\write\\export01.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        // 1.获取导出的数据体
        List<ProjectEvaluate> data = new ArrayList<ProjectEvaluate>();
        for (int i = 0; i < 50; i++) {
            ProjectEvaluate obj = new ProjectEvaluate();
            obj.setProjectName("中青旅" + i);
            obj.setAreaName("华东长三角");
            obj.setProvince("河北省");
            obj.setCity("保定市");
            obj.setPeople("张三" + i);
            obj.setLeader("李四" + i);
            obj.setScount(50);
            obj.setAvg(60.0);
            obj.setCreateTime(new Date());
            obj.setImg(ExcelUtils.ImageParseBytes(new File("D:\\workspace_idea\\poi4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\1.jpg")));
            data.add(obj);
        }
        // 2.表头设置,可以对应excel设计表头，一看就懂
        HashMap<String, String> headerRules = new HashMap<>();
        // "1,1,A,K" 1,1表示跨行，A,K表示跨列
        headerRules.put("1,1,A,K", "项目资源统计");
        headerRules.put("2,3,A,A", "序号");
        headerRules.put("2,2,B,E", "基本信息");
        headerRules.put("3,3,B,B", "项目名称");
        headerRules.put("3,3,C,C", "所属区域");
        headerRules.put("3,3,D,D", "省份");
        headerRules.put("3,3,E,E", "市");
        headerRules.put("2,3,F,F", "项目所属人");
        headerRules.put("2,3,G,G", "市项目领导人");
        headerRules.put("2,2,H,I", "分值");
        headerRules.put("3,3,H,H", "得分");
        headerRules.put("3,3,I,I", "平均分");
        headerRules.put("2,3,J,J", "创建时间");
        headerRules.put("2,3,K,K", "项目图片");
        // 3.尾部设置，一般可以用来设计合计栏
        HashMap<String, String> footerRules = new HashMap<>();
        footerRules.put("1,2,A,C", "注释:");
        footerRules.put("1,2,D,K", "导出参考代码！");
        // 4.导出hearder对应的字段设置
        Column[] column = {

                Column.field("projectName"),
                // 4.1设置此列宽度为10
                Column.field("areaName")
                        .width(10),
                // 4.2设置此列下拉框数据
                Column.field("province")
                        .width(5)
                        .dorpDown(new String[]{"陕西省", "山西省", "辽宁省"}),
                // 4.3设置此列水平居右
                Column.field("city")
                        .align(HorizontalAlignment.RIGHT),
                // 4.4 设置此列垂直居上
                Column.field("people")
                        .valign(VerticalAlignment.TOP),
                // 4.5 设置此列单元格 自定义校验 只能输入文本为数字文本，具体参考excel函数，不同的地方在于参数的下标从当前单元格A和1开始
                Column.field("leader")
                        .verifyCustom("VALUE(F3:F500)", "请输入数字！"),
                // 4.6设置此列单元格 整数 数据校验 ，同时设置背景色为棕色
                Column.field("scount")
                        .verifyIntNum("10~20")
                        .backColor(IndexedColors.BROWN),
                // 4.7设置此列单元格 浮点数 数据校验， 同时设置字体颜色红色
                Column.field("avg")
                        .verifyFloatNum("10.0~20.0")
                        .color(IndexedColors.RED),
                // 4.8设置此列单元格 日期 数据校验 ，同时宽度为20、限制用户表格输入、水平居中、垂直居中、背景色、字体颜色
                Column.field("createTime")
                        .width(20)
                        .verifyDate("2000-01-03 12:35~3000-05-06 23:23")
                        .align(HorizontalAlignment.LEFT)
                        .valign(VerticalAlignment.CENTER)
                        .backColor(IndexedColors.YELLOW)
                        .color(IndexedColors.GOLD),
                // 4.9项目图片
                Column.field("img")

        };
        // 5.执行导出到工作簿
        // ExportRules:1.是否序号;2.列设置;3.标题设置可为空;4.表头设置;5.表尾设置可为空
        Workbook bean = ExcelUtils.createWorkbook(data, new ExportRules(headerRules, column, true, footerRules), (fieldName, value, row, col) -> {
            if ("projectName".equals(fieldName) && row.getProjectName().equals("中青旅23")) {
                col.align(HorizontalAlignment.LEFT);
                col.valign(VerticalAlignment.CENTER);
                col.height(2);
                col.backColor(IndexedColors.RED);
                col.color(IndexedColors.YELLOW);
            }
            return value;
        });
        // 6.写出文件
        try {
            bean.write(new FileOutputStream("D:\\workspace_idea\\poi4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export12.xls"));
            System.out.println("成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test3() {
        // 1.获取导出的数据体
        List<Student> data = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            // 學生
            Student stu = new Student();
            // 學生所在的班級，用對象
            stu.setClassRoom(new ClassRoom("六班"));
            // 學生的更多信息，用map
            Map<String, Object> moreInfo = new HashMap<>();
            moreInfo.put("parent", new Parent("張無忌" + i));
            stu.setMoreInfo(moreInfo);
            stu.setName("张三" + i);
            data.add(stu);
        }
        // 2.导出标题设置，可为空
        String title = "學生基本信息";
        // 3.导出的hearder设置
        String[] hearder = {"學生姓名", "所在班級", "所在學校", "更多父母姓名"};
        // 4.导出hearder对应的字段设置，列宽设置
        Column[] column = {
                Column.field("name"),
                Column.field("classRoom.name"),
                Column.field("classRoom.school.name"),
                Column.field("moreInfo.parent.name"),
        };
        // 5.执行导出到工作簿
        // ExportRules:1.是否序号;2.字段信息;3.标题设置可为空;4.表头设置;5.表尾设置可为空
        Workbook bean = ExcelUtils.createWorkbook(data, new ExportRules(title, hearder, column, false, null));
        // 6.写出文件
        try {
            bean.write(new FileOutputStream("D:\\workspace_idea\\poi4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export13.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test4() {
        // 1.获取导出的数据体
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 10; i++) {
            // 學生
            Map<String, String> map = new HashMap<>();
            map.put("name", "张三" + i);
            map.put("classRoomName", "三班");
            map.put("school", "世纪中心");
            map.put("parent", "张无忌");
            data.add(map);
        }
        // 2.导出标题设置，可为空
        String title = "學生基本信息Map";
        // 3.导出的hearder设置
        String[] hearder = {"學生姓名", "所在班級", "所在學校", "更多父母姓名"};
        // 4.导出hearder对应的字段设置，列宽设置
        Column[] column = {Column.field("name"), Column.field("classRoomName"), Column.field("school"), Column.field("parent"),};
        // 5.执行导出到工作簿
        // ExportRules:1.是否序号;2.字段信息;3.标题设置可为空;4.表头设置;5.表尾设置可为空
        Workbook bean = ExcelUtils.createWorkbook(data, new ExportRules(title, hearder, column, false, null));
        // 6.写出文件
        try {
            bean.write(new FileOutputStream("D:\\codeidea\\poi_4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export4.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test5() {
        // 1.导出标题设置，可为空
        String title = "客户导入";
        // 2.导出的hearder设置
        String[] hearder = {"宝宝姓名", "宝宝昵称", "家长姓名", "手机号码", "宝宝生日", "月龄", "宝宝性别", "来源渠道", "市场人员", "咨询顾问", "客服顾问", "分配校区", "备注"};
        // 3.导出hearder对应的字段设置，列宽设置
        Column[] column = {
                Column.field("宝宝姓名"),
                Column.field("宝宝昵称"),
                Column.field("家长姓名"),
                Column.field("手机号码").verifyText("11~11", "请输入11位的手机号码！"),
                Column.field("宝宝生日").verifyDate("2000-01-01~3000-12-31"),
                Column.field("月龄").width(4).verifyCustom("VALUE(F3:F6000)", "月齡格式：如1年2个月则输入14"),
                Column.field("宝宝性别").dorpDown(new String[]{"男", "女"}),
                Column.field("来源渠道").width(12).dorpDown(new String[]{"品推", "市场"}), Column.field("市场人员").width(6).dorpDown(new String[]{"张三", "李四"}),
                Column.field("咨询顾问").width(6).dorpDown(new String[]{"张三", "李四"}), Column.field("客服顾问").width(6).dorpDown(new String[]{"大唐", "银泰"}),
                Column.field("分配校区").width(6).dorpDown(new String[]{"大唐", "银泰"}),
                Column.field("备注")
        };
        // 5.执行导出到工作簿
        Workbook bean = ExcelUtils.createWorkbook(Collections.emptyList(), new ExportRules(title, hearder, column, false, null).setXlsx(true));
        // 6.写出文件
        try {
            bean.write(new FileOutputStream("D:\\codeidea\\poi_4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export6.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test6() {
        // 1.获取源文件
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new FileInputStream("D:\\workspace_idea\\poi4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export3.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2.获取sheet0导入
        Sheet sheet = wb.getSheetAt(0);
        // 3.生成VO数据
        //参数：1.生成VO的class类型;2.校验规则;3.导入的sheet;3.从第几行导入(从0开始计数);4.尾部非数据行数量(放弃最后多少行)
        ImportRspInfo<ProjectEvaluate> list = ExcelUtils.parseSheet(ProjectEvaluate.class, EvaluateVerifyBuilder.getInstance(), sheet, 3, 3);
        if (list.isSuccess()) {
            // 导入没有错误，打印数据
            System.out.println(JSON.toJSONString(list.getData()));
        } else {
            // 导入有错误，打印输出错误
            System.out.println(list.getMessage());
        }
    }

    public static void test7() {
        // 1.获取源文件
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new FileInputStream("D:\\codeidea\\poi_4_0_1\\src\\main\\java\\com\\swx\\poi_4_0_1\\excel\\file\\write\\export2.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2.获取sheet0导入
        Sheet sheet = wb.getSheetAt(0);
        // 3.生成VO数据
        //参数：1.生成VO的class类型;2.校验规则;3.导入的sheet;3.从第几行导入;4.尾部非数据行数量;5.导入每条数据的回调
        ImportRspInfo<ProjectEvaluate> list = ExcelUtils.parseSheet(ProjectEvaluate.class, ProjectVerifyBuilder.getInstance(), sheet, 3, 2, (row, rowNum) -> {
            //1.此处可以完成更多的校验
            if (row.getAreaName() == "中青旅") {
                throw new POIException("第" + rowNum + "行，区域名字不能为中青旅！");
            }
            //2.图片导入，再ProjectEvaluate定义类型为byte[]的属性就可以，ProjectVerifyBuilder定义ImgVerfiy校验列.就OK了
        });
        if (list.isSuccess()) {
            // 导入没有错误，打印数据
            System.out.println(JSON.toJSONString(list.getData()));
            //打印图片byte数组长度
            byte[] img = list.getData().get(0).getImg();
            System.out.println(img);
        } else {
            // 导入有错误，打印输出错误
            System.out.println(list.getMessage());
        }
    }

}
