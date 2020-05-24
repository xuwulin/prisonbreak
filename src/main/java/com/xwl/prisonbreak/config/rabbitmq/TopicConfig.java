package com.xwl.prisonbreak.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xwl
 * @date 2020-05-11 10:48
 * @description 主题型交换机 Topic Exchange
 */
@Configuration
public class TopicConfig {
    /**
     * 绑定键，队列名
     */
    public final static String REGISTER_TOPIC_QUEUE = "topic.register";

    /**
     * 绑定键，交换机名
     */
    public final static String REGISTER_TOPIC_EXCHANGE = "registerTopicExchange";

    /**
     * Topic队列，名称：registerTopicQueue
     * @return
     */
    @Bean
    public Queue registerTopicQueue() {
        return new Queue(REGISTER_TOPIC_QUEUE);
    }

    /**
     * Topic交换机，名称：registerTopicExchange
     * @return
     */
    @Bean
    TopicExchange registerTopicExchange() {
        return new TopicExchange(REGISTER_TOPIC_EXCHANGE);
    }

    /**
     * 将registerTopicQueue和registerTopicExchange绑定，而且绑定的键值为topic.register
     * 这样只要是消息携带的路由键是topic.register，才会分发到该队列
     * @return
     */
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(registerTopicQueue())
                .to(registerTopicExchange())
                .with(REGISTER_TOPIC_QUEUE);
    }

    /**
     * 将registerTopicQueue和registerTopicExchange绑定，而且绑定的键值为用上通配路由键规则topic.#
     * 这样只要是消息携带的路由键是以 topic. 开头，都会分发到该队列
     * @return
     */
    @Bean
    Binding bindingExchangeMessage2() {
        return BindingBuilder.bind(registerTopicQueue())
                .to(registerTopicExchange())
                .with("topic.#");
    }
}
