package com.xwl.prisonbreak.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xwl
 * @date 2020-05-11 11:21
 * @description
 */
@Component
@Slf4j
@RabbitListener(queues = "topic.register")
public class TopicReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        log.info("TopicReceiver消费者收到消息：" + testMessage.toString());
    }
}
