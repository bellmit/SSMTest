package cn.gtmap.msurveyplat.promanage.core.service.mq.send;

import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/15
 * @description TODO 推送线上消息数据
 */
@Service
public class SendXxtxToBsdtServiceImpl implements RabbitTemplate.ConfirmCallback {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "dchyXmlglRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * @param exchange:交换器名称
     * @param routingKey:需要发送的消息队列routingKey名称
     * @param dchyXmglXxtxDto:用户需要发送的消息
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description
     */
    public void sendDirectMsg(String exchange, String routingKey, DchyXmglXxtxDto dchyXmglXxtxDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, dchyXmglXxtxDto);
        logger.info("线下消息提醒数据推送到线上****************************************************************");
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description 消息发送并成功处理后的回调函数
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" client  :" + correlationData);
        if (ack) {
            logger.info("**********线下消息提醒数据--消息发送成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_XXTX_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
        } else {
            logger.info("**********线下消息提醒数据--消息发送失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_XXTX_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
            logger.error("消息发送失败", cause);
        }
    }
}
