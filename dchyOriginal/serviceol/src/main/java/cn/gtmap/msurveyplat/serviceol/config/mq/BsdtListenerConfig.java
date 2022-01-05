package cn.gtmap.msurveyplat.serviceol.config.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description  监听队列配置
 * @time 2020/11/27 14:30
 */
@Configuration
public class BsdtListenerConfig {
    @Autowired
    ConnectionFactory connectionFactory;

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @return org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
     * @time 2020/11/27 14:27
     * @description 监听队列配置
     */
    @Bean(name = "bsdtListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory acpExcAndLyRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
