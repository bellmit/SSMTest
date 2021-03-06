package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglZxbjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.ContractRegistrationFileService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.main.FileUploadController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
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

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 接收来自办事大厅的成果检查请求
 * @time 2021/3/13 17:04
 */
@Service
public class ReceiveZxBjFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;
    @Autowired
    private ContractRegistrationFileService contractService;
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private FileUploadController fileUploadController;


    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_ZXBJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上在线办结数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_ZXBJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            logger.info("**********接收来自线上推送办结数据的请求数据:**********" + str);

            saveZxbj(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上在线办结数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_ZXBJ_QUEUE +
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
    public void saveZxbj(String str) {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes("UTF-8"));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_ZXBJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_ZXBJ_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglZxbjDto dchyXmglZxbjDto = JSON.toJavaObject(slxxJson, DchyXmglZxbjDto.class);
            DchyXmglZxbjDto dchyXmglZxbjDtoReturn = new DchyXmglZxbjDto();


            //保存的数据
            if (dchyXmglZxbjDto != null) {

                List<DchyXmglChxm> xmglChxmList = dchyXmglZxbjDto.getDchyXmglChxmList();
                List<DchyXmglChxmChdwxx> xmglChxmChdwxxList = dchyXmglZxbjDto.getDchyXmglChxmChdwxxList();
                if (CollectionUtils.isNotEmpty(xmglChxmList)) {
                    for (DchyXmglChxm chxm : xmglChxmList) {
                        if (null != chxm) {
                            int i = entityMapper.saveOrUpdate(chxm, chxm.getChxmid());
                            if (i > 0) {
                                logger.info("************测绘项目入库成功");
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(xmglChxmChdwxxList)) {
                    for (DchyXmglChxmChdwxx chdwxx : xmglChxmChdwxxList) {
                        if (null != chdwxx) {
                            int i = entityMapper.saveOrUpdate(chdwxx, chdwxx.getChdwxxid());
                            if (i > 0) {
                                logger.info("************测绘单位入库成功");
                            }
                        }
                    }
                }

                String model = dchyXmglZxbjDto.getModel();//获取模式
                if (StringUtils.isNotBlank(model)) {
                    if (StringUtils.equals(model, "onlineCompleteCheck")) {//办结前的成果状态审核
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        String chxmid = dchyXmglZxbjDto.getChxmid();
                        map.put("chxmid", chxmid);
                        data.putAll(map);
                        /*办结前成果状态的检查*/
                        Map<String, Object> checkMap = contractService.checkProjectArchStatus(data);
                        logger.info("checkMap: " + checkMap.toString());
                        Map resultMap = Maps.newHashMap();

                        resultMap.put("data", checkMap);

                        dchyXmglZxbjDtoReturn.setChxmid(chxmid);
                        dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ_JC);
                        dchyXmglZxbjDtoReturn.setData(checkMap);
                        /*在线办结后的结果推到线上*/
                        pushDataToMqService.pushZxbjResultTo(dchyXmglZxbjDtoReturn);
                    } else if (StringUtils.equals(model, "onlineComplete")) {//办结
                        Map<String, Object> data = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        String chxmid = dchyXmglZxbjDto.getChxmid();
                        map.put("chxmid", chxmid);
                        data.putAll(map);
                        /*在线办结*/
                        ResponseMessage message = new ResponseMessage();
                        int result = contractService.projectComplete(data);
                        if (result > 0) {
                            message = ResponseUtil.wrapSuccessResponse();
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("chxmid", chxmid);
                            message.setData(map2);
                        } else {
                            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getMsg(), ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getCode());
                        }
                        dchyXmglZxbjDtoReturn.setMessage(message);
                        dchyXmglZxbjDtoReturn.setModel(Constants.DCHY_XMGL_ZXBJ);
                        dchyXmglZxbjDtoReturn.setChxmid(chxmid);
                        /*在线办结后的结果推到线上*/
                        pushDataToMqService.pushZxbjResultTo(dchyXmglZxbjDtoReturn);
                    } else if (StringUtils.equals(model, "onlineFileView")) {//在线文件预览
                        String wjzxid = dchyXmglZxbjDto.getWjzxid();
                        String fileName = dchyXmglZxbjDto.getFileName();
                        //fileUploadController.OnlinePreview(wjzxid,fileName,)
                    }
                }
            }
        } catch (Exception e) {
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            logger.error("接收失败原因:{}", e);
        }

    }

}
