package com.xwl.prisonbreak.config.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Auther: xwl
 * @Date: 2019/6/5 21:21
 * @Description: 文件上传、下载配置
 */
@Data
@Component
// @ConfigurationProperties(prefix ="upload")：作用是告诉springBoot，此类中的属性将与默认的全局配置文件中对应属性一一绑定。
// 属性名必须是application.yml或application.properties。
// 【prefix = "upload"】表示与配置文件中哪个层级的属性进行绑定。
@ConfigurationProperties(prefix ="upload")
public class UploadConfigure {

    /**
     * 获取存放位置
     */
    private Map<String, String> localtion;

    /**
     * 单个文件大小
     */
    private String maxFileSize;

    /**
     * 单次上传总文件大小
     */
    private String maxRequestSize;

    /**
     * 获取基础路径
     * @return
     */
    public String getBasePath() {
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            // 从配置文件application.yml中获取windows的文件保存路径
            location = this.getLocaltion().get("windows");
        } else {
            // 从配置文件application.yml中获取linux的文件保存路径
            location = this.getLocaltion().get("linux");
        }
        return location;
    }
}
