package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgJcDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.AESOperatorUtil;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.CgtjService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
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

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 接收来自办事大厅的成果检查请求
 * @time 2021/3/13 17:04
 */
@Service
public class ReceiveCgjcQqFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;
    @Autowired
    private CgtjService cgtjService;
    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_CGJC_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上成果检查数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGJC_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveCgjc(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上成果检查数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGJC_QUEUE +
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCgjc(String str) {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        ResponseMessage message = new ResponseMessage();
        Map paramMap = Maps.newHashMap();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_CGJC_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_CGJC_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglCgJcDto dchyXmglCgJcDto = JSON.toJavaObject(slxxJson, DchyXmglCgJcDto.class);
            //保存的数据
            if (dchyXmglCgJcDto != null) {
//                map.put("clcgFiles", Base64.getEncoder().encodeToString(bytes));
//                map.put("uploadFileName",mlmc);
//                map.put("glsxid",glsxid);
                String clcgFiles = dchyXmglCgJcDto.getClcgFiles();
                String base64 = AESOperatorUtil.decrypt(clcgFiles);
                String uploadFileName = dchyXmglCgJcDto.getUploadFileName();
                String glsxid = dchyXmglCgJcDto.getGlsxid();

                if (StringUtils.isNoneBlank(base64, uploadFileName, glsxid)) {
                    paramMap.put("glsxid", glsxid);
                    InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
                    message = cgtjService.cgjc(uploadFileName, inputStream, glsxid, null);
                    Map map = message.getData();
                    map.put("glsxid", glsxid);
                    message.setData(map);
                } else {
                    message = ResponseUtil.wrapParamEmptyFailResponse();
                }

//                Map resultMap = Maps.newHashMap();
//                resultMap.put("code", message.getHead().getCode());
//                resultMap.put("msg", message.getHead().getMsg());
//                resultMap.put("data", message.getData());

                dchyXmglCgJcDto.setMessage(message);
                pushDataToMqService.pushCgjcResultToMq(dchyXmglCgJcDto);
            }
        } catch (Exception e) {
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            logger.error("接收失败原因:{}", e);
        }

    }

}
