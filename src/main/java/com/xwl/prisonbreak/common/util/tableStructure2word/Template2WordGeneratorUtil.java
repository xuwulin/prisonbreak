package com.xwl.prisonbreak.common.util.tableStructure2word;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xwl
 * @Date: 2019/9/19 9:29
 * @Description: 根据模板生成word
 */
public class Template2WordGeneratorUtil {

    private Template2WordGeneratorUtil() {
        throw new AssertionError();
    }

    public static File createDoc(Map<?, ?> dataMap) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(Template2WordGeneratorUtil.class, "/templates");
        Map<String, Template> allTemplates = new HashMap<>();
        try {
            allTemplates.put("tableStructureTemplate", configuration.getTemplate("tableStructureTemplate.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        String name = "temp" + (int) (Math.random() * 100000) + ".doc";
        File file = new File(name);
        Template template = allTemplates.get("tableStructureTemplate");
        try {
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            template.process(dataMap, writer);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return file;
    }
}
