package cn.gtmap.msurveyplat.promanage.config.mq;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
  * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
  * @description  生产者配置
  * @time 2020/11/27 14:29
  */
@Configuration
public class XmglProducerConfig {

    @Autowired
    ConnectionFactory connectionFactory;

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param:
      * @return org.springframework.amqp.rabbit.core.RabbitTemplate
      * @time 2020/11/27 14:28
      * @description 生产者配置
      */
    @Bean(name = "dchyXmlglRabbitTemplate")
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate acpAndAcpExcRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
