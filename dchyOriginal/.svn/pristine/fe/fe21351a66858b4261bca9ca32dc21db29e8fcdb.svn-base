package cn.gtmap.msurveyplat.serviceol.core.service.mq.send;

import cn.gtmap.msurveyplat.common.dto.DchyXmglCgJcDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 成果检查  线上-->线下库
 * @time 2021/04/12 15:04
 */
@Service
public class SendCgjcQqToXmglServiceImpl implements RabbitTemplate.ConfirmCallback {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "bsdtRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * @param exchange:交换器名称
     * @param routingKey:需要发送的消息队列routingKey名称
     * @param dchyXmglCgjcDto:用户需要发送的消息
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description
     */
    public void sendDirectMsg(String exchange, String routingKey, DchyXmglCgJcDto dchyXmglCgjcDto) {
        logger.info("**********线上成果文件检查--消息发送成功**********");
        rabbitTemplate.convertAndSend(exchange, routingKey, dchyXmglCgjcDto);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description 消息发送并成功处理后的回调函数
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("**********线上成果文件检查--消息发送成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGJC_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
        } else {
            logger.info("**********线上成果文件检查--消息发送失败**********" +
                    Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGJC_QUEUE +
                    CalendarUtil.getCurHMSStrDate());
            logger.error("消息发送失败", cause);
        }
    }
}
