package cn.gtmap.msurveyplat.promanage.core.service.mq.send;

import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/10 15:27
 * @description
 */
@Service
public class SendCgViewToBsdtServerImpl implements RabbitTemplate.ConfirmCallback {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "dchyXmlglRabbitTemplate")
    private RabbitTemplate rabbitTemplate;



    /**
     * @param exchange:交换器名称
     * @param routingKey:需要发送的消息队列routingKey名称
     * @param dchyXmglZxbjDto:用户需要发送的消息
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @description
     */
    public void sendDirectMsg(String exchange, String routingKey, DchyXmglZxbjDto dchyXmglZxbjDto) {
        rabbitTemplate.convertAndSend(exchange, routingKey, dchyXmglZxbjDto);
        logger.info("线下在线成果文件查看数据推送到线上****************************************************************");
        logger.info("线下在线成果文件查看返回值数据*************************" + JSONObject.toJSONString(dchyXmglZxbjDto));
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" client  :" + correlationData);
        if (ack) {
            logger.info("**********线下在线成果文件查看数据--消息发送成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_GC_VIEW_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
        } else {
            logger.info("**********线下在线成果文件查看数据--消息发送失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_GC_VIEW_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
            logger.error("消息发送失败", cause);
        }
    }
}
