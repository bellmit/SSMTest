package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.ContractRegistrationFileService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.main.FileUploadController;
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
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/10 14:26
 * @description
 */
@Service
public class ReceiveCgViewFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(ReceiveCgViewFromBsdtServiceImpl.class);

    @Autowired
    private ContractRegistrationFileService contractService;
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private FileUploadController fileUploadController;

    @Override
    @RabbitListener(queues = Constants.BSTD_XMGL_CG_VIEW_QUEUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********??????????????????????????????--??????????????????**********");
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            logger.info("**********???????????????????????????????????????????????????????????????:**********" + str);
            doOnlineCgView(str);
            basicACK(message, channel); //????????????????????????mq?????????????????????mq
        } catch (Exception e) {
            basicReject(message, channel);
            logger.error("???????????????????????????????????????{}", e);
        }
    }

    public void doOnlineCgView(String str) {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSTD_XMGL_CG_VIEW_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSTD_XMGL_CG_VIEW_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglZxbjDto dchyXmglZxbjDto = JSON.toJavaObject(slxxJson, DchyXmglZxbjDto.class);
            DchyXmglZxbjDto dchyXmglZxbjDtoReturn = new DchyXmglZxbjDto();

            if (null != dchyXmglZxbjDto) {
                String model = dchyXmglZxbjDto.getModel();//????????????
                if (StringUtils.isNotBlank(model)) {
                    if (StringUtils.equals(model, "onlineGcPreview")) {
                        String chgcid = dchyXmglZxbjDto.getChgcid();
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        String chxmid = dchyXmglZxbjDto.getChxmid();
                        map.put("chxmid", chxmid);
                        map.put("chgcid", chgcid);
                        data.putAll(map);
                        Map<String, Object> fileMap = contractService.viewattachments2(data);

                        dchyXmglZxbjDtoReturn.setChxmid(chxmid);
                        dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ_YL);
                        dchyXmglZxbjDtoReturn.setData(fileMap);
                        /*????????????????????????????????????*/
                        pushDataToMqService.pushCgViewResultToXs(dchyXmglZxbjDtoReturn);
                    } else if (StringUtils.equals(model, "onlineGcPreviewById")) {//???????????????????????????
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        String chxmid = dchyXmglZxbjDto.getChxmid();
                        map.put("chxmid", chxmid);
                        String chgcid = dchyXmglZxbjDto.getChgcid();
                        String id = dchyXmglZxbjDto.getId();
                        map.put("chgcid", chgcid);
                        map.put("id", id);
                        data.putAll(map);
                        Map<String, Object> fileMap = contractService.viewChildAttachments(data);
                        dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ_YLZJD);
                        dchyXmglZxbjDtoReturn.setChxmid(chxmid);
                        dchyXmglZxbjDtoReturn.setData(fileMap);
                        dchyXmglZxbjDtoReturn.setId(id);
                        /*???????????????????????????????????????*/
                        pushDataToMqService.pushCgViewResultToXs(dchyXmglZxbjDtoReturn);
                    } else if (StringUtils.equals(model, "onlineGcDownload")) {//?????????????????????
                        String wjzxid = dchyXmglZxbjDto.getWjzxid();
                        /*????????????????????????????????????*/
                        Map<String, byte[]> fileMap = fileUploadController.downFileByteArrayByWjzxid(wjzxid);
                        Iterator<Map.Entry<String, byte[]>> iterator = fileMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, byte[]> next = iterator.next();
                            String fileName = next.getKey();
                            byte[] fileBytes = next.getValue();
                            String fileBody = Base64.getEncoder().encodeToString(fileBytes);

                            dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ_ZXXZ);
                            dchyXmglZxbjDtoReturn.setWjzxid(wjzxid);
                            dchyXmglZxbjDtoReturn.setFileName(fileName);
                            dchyXmglZxbjDtoReturn.setFileBody(fileBody);
                            /*????????????????????????????????????*/
                            pushDataToMqService.pushCgViewResultToXs(dchyXmglZxbjDtoReturn);
                        }
                    } else if (StringUtils.equals(model, "onlineGetUploadFileNums")) {//??????????????????????????????????????????
                        String wjzxid = dchyXmglZxbjDto.getWjzxid();
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        map.put("wjzxid", wjzxid);
                        data.put("data", map);
                        ResponseMessage uploadFileNums = fileUploadController.getUploadFileNums(data);
                        dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ_ZXSC);
                        dchyXmglZxbjDtoReturn.setWjzxid(wjzxid);
                        dchyXmglZxbjDtoReturn.setResponseHead(uploadFileNums.getHead());
                        dchyXmglZxbjDtoReturn.setMessage(uploadFileNums);
                        /*????????????????????????????????????*/
                        pushDataToMqService.pushCgViewResultToXs(dchyXmglZxbjDtoReturn);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("??????????????????:{}", e);
        }
    }

    //???????????????mq????????????
    private void basicReject(Message message, Channel channel) {
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("mq??????????????????????????????????????????????????????", e);
        }
    }

    //????????????????????????mq?????????????????????mq
    private void basicACK(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            logger.error("?????????????????????mq???????????????????????????", e);
        }
    }
}
