package com.xwl.prisonbreak.quartz;

import com.alibaba.fastjson.JSON;
import com.xwl.prisonbreak.michael.entity.TimedTask;
import com.xwl.prisonbreak.michael.service.TimedTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Auther: xwl
 * @Date: 2019/7/25 17:14
 * @Description: 读取数据库配置，执行定时任务
 */
@Configuration
@EnableScheduling
@Component
@Slf4j
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * java8的日期
     */
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TimedTaskService timedTaskService;

    /**
     * 执行定时任务
     *
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 获取所有的消息类型配置
        List<TimedTask> tasks = timedTaskService.list();
        if (tasks != null && tasks.size() > 0) {
            tasks = tasks.stream()
                    .filter(item -> item.getDeleted() == 0)
                    .collect(toList());
        }

        log.info("定时任务启动,预计启动任务数量：" + tasks.size() + "; time=" + LocalDateTime.now().format(dtf));

        int count = 0;
        if (tasks != null && tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    taskRegistrar.addTriggerTask(getRunnable(tasks.get(i)), getTrigger(tasks.get(i)));
                    count++;
                } catch (Exception e) {
                    log.error("消息提醒==>清除过期消息内容定时任务执行错误：消息类型配置数据："
                            + JSON.toJSONString(tasks.get(i)) + "；" + e.getMessage());
                }
            }
        }
        log.info("定时任务实际启动数量：" + count + "; time=" + LocalDateTime.now().format(dtf));
    }

    /**
     * 执行任务
     * @param task
     * @return
     */
    private Runnable getRunnable(TimedTask task) {
        return () -> {
            try {
                // 执行任务
                this.excute(task);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        };
    }

    /**
     * 设置执行周期
     * @param task
     * @return
     */
    private Trigger getTrigger(TimedTask task) {
        return triggerContext -> {
            // 设置执行周期(Trigger)
            String cron = task.getCron();
            // 合法性校验
            if (StringUtils.isBlank(cron)) {
                cron = "0/5 * * * * ?";
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        };
    }

    private void excute(TimedTask task) {
        System.out.println("执行任务名称" + task.getTaskName() + "，执行时间：" + LocalDateTime.now().format(dtf));
    }
}

