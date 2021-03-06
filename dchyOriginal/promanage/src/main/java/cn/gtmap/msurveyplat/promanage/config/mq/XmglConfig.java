package cn.gtmap.msurveyplat.promanage.config.mq;


import cn.gtmap.msurveyplat.promanage.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description exchange和queue的配置绑定
 * @time 2020/11/27 14:30
 */
@Configuration
public class XmglConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConnectionFactory connectionFactory;

    @Bean(name = "bsdtToXmglFanoutExchange")
    public DirectExchange bsdtToXmglFanoutExchange() {
        try {
            connectionFactory.createConnection().createChannel(false).exchangeDeclare(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.EXCHANGE_TYPE_DIRECT, true, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new DirectExchange(Constants.BSDT_XMGL_DIRECT_EXCHANGE);
        }
    }

    @Bean(name = "bsdtToXmglHtbgQueue")
    public Queue bsdtToXmglHtbgQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSTD_XMGL_HTBG_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSTD_XMGL_HTBG_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingHtbg(@Qualifier("bsdtToXmglHtbgQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_HTBG_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglXmfbQueue")
    public Queue bsdtToXmglXmfbQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_XMFB_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_XMFB_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingXmfb(@Qualifier("bsdtToXmglXmfbQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglFwpjQueue")
    public Queue bsdtToXmglFwpjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_FWPJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_FWPJ_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingFwpj(@Qualifier("bsdtToXmglFwpjQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglCgjcQueue")
    public Queue bsdtToXmglCgjcQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_CGJC_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_CGJC_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingCgjc(@Qualifier("bsdtToXmglCgjcQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_CGJC_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglCgtjQueue")
    public Queue bsdtToXmglCgtjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_CGTJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_CGTJ_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingCgtj(@Qualifier("bsdtToXmglCgtjQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_CGTJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglZxbjQueue")
    public Queue bsdtToXmglZxbjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_ZXBJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_ZXBJ_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingZxbj(@Qualifier("bsdtToXmglZxbjQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_ZXBJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglCgViewQueue")
    public Queue bsdtToXmglCgViewQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSTD_XMGL_CG_VIEW_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSTD_XMGL_CG_VIEW_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingCgView(@Qualifier("bsdtToXmglCgViewQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        }
        return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_CG_VIEW_QUEUE_ROUTINGKEY);
    }

    @Bean(name = "bsdtToXmglTswjQueue")
    public Queue bsdtToXmglTswjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_TSWJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_TSWJ_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingTswj(@Qualifier("bsdtToXmglTswjQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_TSWJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglJcsjQueue")
    public Queue bsdtToXmglJcsjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_JCSJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_JCSJ_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingJcsj(@Qualifier("bsdtToXmglJcsjQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_JCSJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglXxtxQueue")
    public Queue bsdtToXmglXxtxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_XXTX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_XXTX_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingXxtx(@Qualifier("bsdtToXmglXxtxQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_XXTX_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglTjfxQueue")
    public Queue bsdtToXmglTjfxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_TJFX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_TJFX_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingTjfx(@Qualifier("bsdtToXmglTjfxQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_TJFX_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "bsdtToXmglJsdwlrQueue")
    public Queue bsdtToXmglJsdwlrQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.BSDT_XMGL_JSDWLR_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.BSDT_XMGL_JSDWLR_QUEUE, true);
        }
    }

    @Bean
    public Binding bsdtToXmglBindingJsdwlr(@Qualifier("bsdtToXmglJsdwlrQueue") Queue queue, @Qualifier("bsdtToXmglFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.BSDT_XMGL_JSDWLR_QUEUE_ROUTINGKEY);
        }
    }


    @Bean(name = "xmglToBsdtFanoutExchange")
    public DirectExchange xmglToBsdtFanoutExchange() {
        try {
            connectionFactory.createConnection().createChannel(false).exchangeDeclare(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.EXCHANGE_TYPE_DIRECT, true, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new DirectExchange(Constants.XMGL_BSDT_DIRECT_EXCHANGE);
        }
    }

    @Bean(name = "xmglToBsdtSlxxQueue")
    public Queue xmglToBsdtSlxxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_SLXX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_SLXX_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingSlxx(@Qualifier("xmglToBsdtSlxxQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtCgjcQueue")
    public Queue xmglToBsdtCgjcQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_CGJC_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_CGJC_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingCgjc(@Qualifier("xmglToBsdtCgjcQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_CGJC_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtCgtjQueue")
    public Queue xmglToBsdtCgtjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_CGTJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_CGTJ_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingCgtj(@Qualifier("xmglToBsdtCgtjQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_CGTJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtCgccQueue")
    public Queue xmglToBsdtCgccQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_CGCC_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_CGCC_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingCgcc(@Qualifier("xmglToBsdtCgccQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_CGCC_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtZxbjQueue")
    public Queue xmglToBsdtZxbjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_ZXBJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_ZXBJ_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingZxbj(@Qualifier("xmglToBsdtZxbjQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_ZXBJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtCgViewQueue")
    public Queue xmglToBsdtCgViewQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_GC_VIEW_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_GC_VIEW_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindCgView(@Qualifier("xmglToBsdtCgViewQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_CG_VIEW_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtJcsjQueue")
    public Queue xmglToBsdtJcsjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_JCSJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_JCSJ_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingJcsj(@Qualifier("xmglToBsdtJcsjQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_JCSJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtTswjQueue")
    public Queue xmglToBsdtTswjQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_TSWJ_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_TSWJ_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingTswj(@Qualifier("xmglToBsdtTswjQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_TSWJ_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtXxtxQueue")
    public Queue xmglToBsdtXxtxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_XXTX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_XXTX_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingXxtx(@Qualifier("xmglToBsdtXxtxQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_XXTX_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtTjfxQueue")
    public Queue xmglToBsdtTjfxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_TJFX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_TJFX_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingTjfx(@Qualifier("xmglToBsdtTjfxQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_TJFX_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtJsdwlrQueue")
    public Queue xmglToBsdtJsdwlrQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_JSDWLR_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_JSDWLR_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingJsdwlr(@Qualifier("xmglToBsdtJsdwlrQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_JSDWLR_QUEUE_ROUTINGKEY);
        }
    }

    @Bean(name = "xmglToBsdtBaxxQueue")
    public Queue xmglToBsdtBaxxQueue() {
        try {
            connectionFactory.createConnection().createChannel(false).queueDeclare(Constants.XMGL_BSDT_BAXX_QUEUE, true, false, false, null);
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return new Queue(Constants.XMGL_BSDT_BAXX_QUEUE, true);
        }
    }

    @Bean
    public Binding xmglToBsdtBindingBaxx(@Qualifier("xmglToBsdtBaxxQueue") Queue queue, @Qualifier("xmglToBsdtFanoutExchange") DirectExchange exchange) {
        try {
            connectionFactory.createConnection().createChannel(false).queueBind(queue.getName(), exchange.getName(), queue.getName());
        } catch (Exception e) {
            logger.error("错误原因{}:", e);
        } finally {
            return BindingBuilder.bind(queue).to(exchange).with(Constants.XMGL_BSDT_BAXX_QUEUE_ROUTINGKEY);
        }
    }

}

