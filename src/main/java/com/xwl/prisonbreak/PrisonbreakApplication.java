package com.xwl.prisonbreak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author xwl
 * @date 2020-04-21 14:55
 * @description 主启动类
 * @EnableAsync 开启异步调用
 */
@SpringBootApplication
@EnableAsync
public class PrisonbreakApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrisonbreakApplication.class, args);
    }
}
