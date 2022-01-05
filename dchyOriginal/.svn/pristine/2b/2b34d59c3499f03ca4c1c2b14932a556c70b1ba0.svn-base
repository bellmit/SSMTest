package cn.gtmap.msurveyplat.promanage.config.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
  * @description
  * @time 2020/11/27 14:29  监听队列配置
  */
@Configuration
public class XmlglListenerConfig {
    @Autowired
    ConnectionFactory connectionFactory;

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: null
      * @return
      * @time 2020/11/27 14:29
      * @description 监听队列
      */
    @Bean(name = "xmglListenerContainerFactory")
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
