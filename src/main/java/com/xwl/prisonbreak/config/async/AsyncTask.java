package com.xwl.prisonbreak.config.async;

import com.xwl.prisonbreak.common.util.lfasr.LfasrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * @author xwl
 * @date 2020-04-21 14:57
 * @description 异步任务
 */
@Component
@Slf4j
public class AsyncTask {

    /**
     * 异步转换
     */
    @Async("asyncTaskExecutor")
    public void asyncConvert(String filePath) throws Exception {
        log.info("线程id：" + Thread.currentThread().getId() + "，线程名称：" + Thread.currentThread().getName());
        File file = new File(filePath);
        // 转换开始时间
        long currentTimeMillis = System.currentTimeMillis();
        LfasrUtil.lfasrResult(file);
        // 转换结束时间
        long currentTimeMillis1 = System.currentTimeMillis();
        log.info("线程id：" + Thread.currentThread().getId() +
                "，线程名称：" + Thread.currentThread().getName() +
                "，文件名称：" + file.getName() +
                "，转换耗时：" + (currentTimeMillis1 - currentTimeMillis) / 1000 + "s");
    }
}
