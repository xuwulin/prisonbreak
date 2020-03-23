package com.xwl.prisonbreak.michael.service.impl;

import com.xwl.prisonbreak.common.util.IdGeneratorSnowflake;
import com.xwl.prisonbreak.michael.service.SnowflakeUseHutoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xwl
 * @date 2020-03-23 12:16
 * @description
 */
@Service
public class SnowflakeUseHutoolServiceImpl implements SnowflakeUseHutoolService {

    @Autowired
    private IdGeneratorSnowflake idGeneratorSnowflake;

    @Override
    public List<Long> getIDBySnowflake() {
        List list = new ArrayList();
        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 工作中不推荐使用Executors创建线程

        for (int i = 1; i <= 20; i++) {
            threadPool.submit(() -> {
                long id = idGeneratorSnowflake.snowflakeId();
                list.add(id);
                System.out.println(idGeneratorSnowflake.snowflakeId());
            });
        }
        threadPool.shutdown();

        return list;
    }
}
