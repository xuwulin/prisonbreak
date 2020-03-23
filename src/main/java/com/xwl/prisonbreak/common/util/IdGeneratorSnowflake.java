package com.xwl.prisonbreak.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xwl
 * @date 2020-03-23 12:19
 * @description 使用hutool工具包，雪花算法
 */
@Component
@Slf4j
public class IdGeneratorSnowflake {
    /**
     * 表示0号机房，取值0~31
     */
    private long workerId = 0L;

    /**
     * 表示1号机器，取值0~31
     */
    private long datacenterId = 1L;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);

    /**
     * 初始化工作
     */
    @PostConstruct
    public void init() {
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId：{}", workerId);
        } catch (Exception e) {
            log.info("当前机器的workerId获取失败", e);
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(new IdGeneratorSnowflake().snowflakeId());
    }
}
