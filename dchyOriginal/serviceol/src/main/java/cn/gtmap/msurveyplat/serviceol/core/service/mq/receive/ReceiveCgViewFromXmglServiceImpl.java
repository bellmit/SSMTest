package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/10 16:40
 * @description
 */
@Service
public class ReceiveCgViewFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityMapper entityMapper;


    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_GC_VIEW_QUEUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            logger.info("**********接收线下成果文件数据--消息接收成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_GC_VIEW_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            logger.info("**********来自线下成果文件数据**********" + str);
            saveOrDeleteXxSlxx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);
            logger.error("消息接收失败:{}", e);
        }
    }


    public void saveOrDeleteXxSlxx(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_GC_VIEW_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_GC_VIEW_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglZxbjDto dchyXmglZxbjDto = JSON.toJavaObject(slxxJson, DchyXmglZxbjDto.class);

            String model = dchyXmglZxbjDto.getModel();
            String chxmid = dchyXmglZxbjDto.getChxmid();
            Map<String, Object> data = dchyXmglZxbjDto.getData();

            if(StringUtils.isNotBlank(model)){
                if(StringUtils.equals(model,"onlineGcPreview")){
                    /*文件结果放入缓存中*/
                    EhcacheUtil.putDataToEhcache(chxmid + "preview", data);
                }
                else if (StringUtils.equals(model, "onlineGcPreviewById")) {//在线办结文件子结点查询
                    // JSONObject jsonObject = (JSONObject) data.get("node");
                    Integer id = Integer.parseInt(dchyXmglZxbjDto.getId());
                    EhcacheUtil.putDataToEhcache(chxmid + "previewById" + id, data);
                }
                else if (StringUtils.equals(model, "onlineGcDownload")) {
                    String wjzxid = CommonUtil.formatEmptyValue(dchyXmglZxbjDto.getWjzxid());
                    String fileName = CommonUtil.formatEmptyValue(dchyXmglZxbjDto.getFileName());
                    String fileBody = CommonUtil.formatEmptyValue(dchyXmglZxbjDto.getFileBody());
                    /*base64进行解码*/
                    byte[] fileBytes = Base64.getDecoder().decode(fileBody);
                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("fileName", fileName);
                    data1.put("fileBytes", fileBytes);
                    /*文件结果放入缓存中*/
                    EhcacheUtil.putDataToEhcache(wjzxid + "getGcDownload", data1);
                }
                else if (StringUtils.equals(model, "onlineGetUploadFileNums")) {//线上下载前获取可下载文件数量
                    String wjzxid = CommonUtil.formatEmptyValue(dchyXmglZxbjDto.getWjzxid());
                    /*文件结果放入缓存中*/
                    EhcacheUtil.putDataToEhcache(wjzxid + "getFileNums", dchyXmglZxbjDto.getMessage());
                }
            }
        }
        catch (Exception e){
            logger.error("数据库入库失败:{}" + e);
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
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


    //正常消费掉后通知mq服务器移除此条mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("通知服务器移除mq时异常，异常信息：", e);
        }
    }
}
