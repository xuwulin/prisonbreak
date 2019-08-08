package com.xwl.prisonbreak.common.util.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xuwulin
 * @Date: 2019/5/16 16:01
 * @Description: 监听类，可以自定义
 */
public class ExcelListener extends AnalysisEventListener {

    // 自定义用于暂时存储data。
    // 可以通过实例获取该值
    private List<Object> datas = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        System.out.println("当前行：" + context.getCurrentRowNum());
        System.out.println(object);
        // 数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(object);
        // 根据业务自行 do something
        doSomething();

        // 如数据过大，可以进行定量分批处理
        /*if (datas.size() <= 100000) {
            datas.add(object);
        } else {
            doSomething();
            datas = new ArrayList<Object>();
        }*/

    }

    /**
     * 根据业务自行实现该方法
     */
    private void doSomething() {
        // 1、入库调用接口
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析结束销毁不用的资源
//        datas.clear();
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
