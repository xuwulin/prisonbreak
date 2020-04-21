package com.xwl.prisonbreak.michael.controller;

import com.xwl.prisonbreak.common.enums.ResponseTypes;
import com.xwl.prisonbreak.common.vo.ResponseResult;
import com.xwl.prisonbreak.config.async.AsyncTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author xwl
 * @date 2020-04-21 15:23
 * @description
 */
@RestController
@RequestMapping("/api/async")
@Api(tags = "异步调用+多线程")
public class AsyncTaskExecutorController {
    @Autowired
    private AsyncTask asyncTask;

    @GetMapping("/testAsyncTask")
    @ApiOperation("测试异步任务调用，会使用线程池ThreadPoolTaskExecutor开启多线程")
    public ResponseResult testAsyncTask() throws Exception {
        List<String> filePaths = Arrays.asList("src/main/resources/audio/news.mp3", "src/main/resources/audio/sichuan.mp3");
        int num = 10;
        if (filePaths.size() > num) {
            return new ResponseResult(ResponseTypes.FAIL,"批量转换一次最多支持10个文件！");
        }
        // 会开启多线程处理
        for (String filePath : filePaths) {
            asyncTask.asyncConvert(filePath);
        }
        return new ResponseResult(ResponseTypes.SUCCESS,"正在转换...");
    }
}
