package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.gtis.common.util.UUIDGenerator;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 接收来自线下的消息提醒数据
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveXxtxFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityMapper entityMapper;


    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_XXTX_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线下消息提醒数据--消息接收成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_XXTX_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveXxtx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线下消息提醒数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_XXTX_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            logger.error("消息接收失败", e);
        }
    }

    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("通知服务器移除mq时异常，异常信息：", e);
        }
    }

    //处理异常，mq重回队列
    private void basicNACK(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            logger.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }

    //处理异常，mq重回队列
    private void basicReject(Message message, Channel channel) {
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("mq重新进入服务器时出现异常，异常信息：", e);
        }
    }


    //消息提醒数据推送
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveXxtx(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_XXTX_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_XXTX_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglXxtxDto dchyXmglXxtxDto = JSON.toJavaObject(slxxJson, DchyXmglXxtxDto.class);

            logger.info("*************************************输出*****************************************");
            //保存的数据
            if (dchyXmglXxtxDto != null) {
                List<DchyXmglYhxx> dchyXmglYhxxList = dchyXmglXxtxDto.getDchyXmglYhxxList();

                if (CollectionUtils.isNotEmpty(dchyXmglYhxxList)) {
                    for (DchyXmglYhxx dchyXmglYhxx : dchyXmglYhxxList) {
                        if (StringUtils.isNotBlank(dchyXmglYhxx.getYhxxid())) {
                            entityMapper.saveOrUpdate(dchyXmglYhxx, dchyXmglYhxx.getYhxxid());
                            logger.info("************多测合一用户消息入库成功");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("数据库入库失败:", e);
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            throw new Exception();
        }
    }


}
