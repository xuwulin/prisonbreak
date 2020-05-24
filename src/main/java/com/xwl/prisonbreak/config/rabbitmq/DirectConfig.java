package com.xwl.prisonbreak.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xwl
 * @date 2020-05-11 10:41
 * @description 直连型交换机 Direct Exchange
 */
@Configuration
public class DirectConfig {
    /**
     * 绑定键，队列名
     */
    public final static String REGISTER_DIRECT_QUEUE = "registerDirectQueue";

    /**
     * 绑定键，交换机名
     */
    public final static String REGISTER_DIRECT_EXCHANGE = "registerDirectExchange";

    /**
     * 绑定键，路由名
     */
    public final static String REGISTER_DIRECT_ROUTING = "registerDirectRouting";

    /**
     * Direct队列，名称：registerDirectQueue
     * @return
     */
    @Bean
    public Queue registerDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        // 一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(REGISTER_DIRECT_QUEUE,true);
    }

    /**
     * Direct交换机，名称：registerDirectExchange
     * @return
     */
    @Bean
    DirectExchange registerDirectExchange() {
        //  return new DirectExchange(REGISTER_DIRECT_EXCHANGE, true, true);
        return new DirectExchange(REGISTER_DIRECT_EXCHANGE,true,false);
    }

    /**
     * 绑定，将队列和交换机绑定, 并设置用于匹配键：registerDirectRouting
     * @return
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(registerDirectQueue())
                .to(registerDirectExchange())
                .with(REGISTER_DIRECT_ROUTING);
    }
}
