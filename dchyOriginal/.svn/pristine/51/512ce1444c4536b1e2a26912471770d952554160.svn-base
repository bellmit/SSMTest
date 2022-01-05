package cn.gtmap.msurveyplat.serviceol.core.service.mq.send;

import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/10 14:06
 * @description
 */
@Service
public class SendCgViewToXmglServiceImpl implements RabbitTemplate.ConfirmCallback {

    private Logger logger = LoggerFactory.getLogger(SendCgViewToXmglServiceImpl.class);

    @Resource(name = "bsdtRabbitTemplate")
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
        logger.info("**********在线成果文件查看--消息发送成功**********");
        logger.info("**********线上推送成果查看数据的请求数据:**********" + JSONObject.toJSONString(dchyXmglZxbjDto));
        rabbitTemplate.convertAndSend(exchange, routingKey, dchyXmglZxbjDto);
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
            logger.info("**********在线成果查看--消息发送成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSTD_XMGL_CG_VIEW_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
        } else {
            logger.info("**********在线办结数据--消息发送失败**********" +
                    Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSTD_XMGL_CG_VIEW_QUEUE +
                    "消息发送时间为:" + CalendarUtil.getCurHMSStrDate());
            logger.error("消息发送失败", cause);
        }
    }
}
