package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.web.util.EhcacheUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.gtis.common.util.UUIDGenerator;
import com.rabbitmq.client.Channel;
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
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 接收来自线下的在线办结数据
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveZxbjFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityMapper entityMapper;


    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_ZXBJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线下在线办结数据--消息接收成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_ZXBJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            logger.info("**********来自线下在线办结数据**********" + str);
            saveOrDeleteXxSlxx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线下在线办结数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_ZXBJ_QUEUE +
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


    //新增或删除线下受理数据
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrDeleteXxSlxx(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_ZXBJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_ZXBJ_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglZxbjDto dchyXmglZxbjDto = JSON.toJavaObject(slxxJson, DchyXmglZxbjDto.class);

            String model = dchyXmglZxbjDto.getModel();
            String chxmid = dchyXmglZxbjDto.getChxmid();
            Map<String, Object> data = dchyXmglZxbjDto.getData();

            if (StringUtils.equals("onlineCompleteCheck", model)) {//在线办结前的成果检查
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.getHead().setCode((String) data.get("code"));
                responseMessage.getHead().setMsg((String) data.get("msg"));
                /*办结检查结果放入缓存中*/
                EhcacheUtil.putDataToEhcache(chxmid + "zsbjcheck", responseMessage);
            } else if (StringUtils.equals("onlineComplete", model)) {//在线办结
                ResponseMessage message = dchyXmglZxbjDto.getMessage();
                /*办结结果放入缓存中*/
                EhcacheUtil.putDataToEhcache(chxmid + "zsbj", message);
            }
        } catch (Exception e) {
            logger.error("数据库入库失败:" + e);
//            e.printStackTrace();
            dchyXmglMqCzrz.setSbyy(e.toString());
//            mqMsgMapper.insertMqMsg(dchyXmglMqCzrz);
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            throw new Exception();
        }
    }
}

