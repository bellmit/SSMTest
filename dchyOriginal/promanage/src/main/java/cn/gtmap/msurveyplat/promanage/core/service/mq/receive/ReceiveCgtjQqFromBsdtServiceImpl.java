package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgtjDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
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
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 接收来自办事大厅的成果提交请求
 * @time 2021/3/13 17:04
 */
@Service
public class ReceiveCgtjQqFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Autowired
    CgtjService cgtjService;

    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_CGTJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上成果提交数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGTJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveCgtj(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上成果提交数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_CGTJ_QUEUE +
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
    public void saveCgtj(String str) {
        ResponseMessage message = new ResponseMessage();
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_CGTJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_CGTJ_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglCgtjDto dchyXmglCgtjDto = JSON.toJavaObject(slxxJson, DchyXmglCgtjDto.class);

            if (null != dchyXmglCgtjDto) {
                String glsxid = dchyXmglCgtjDto.getGlsxid();
                String dwmc = dchyXmglCgtjDto.getDwmc();
                List<Map<String, String>> errorInfoModels = dchyXmglCgtjDto.getErrorInfoModels();

                if (StringUtils.isNotBlank(glsxid)) {
                    Map param = Maps.newHashMap();
                    Map data = Maps.newHashMap();
                    param.put("data", data);
                    data.put("sqxxid", glsxid);
                    data.put("dwmc", dwmc);
                    data.put("errorInfoModels", errorInfoModels);
                    message = cgtjService.cgtj(param);
                    Map map = message.getData();
                    map.put("glsxid", glsxid);
                    message.setData(map);
                } else {
                    message = ResponseUtil.wrapParamEmptyFailResponse();
                }
                Example exampleclcg = new Example(DchyXmglClcg.class);
                exampleclcg.createCriteria().andEqualTo("sqxxid", glsxid);
                List<DchyXmglClcg> ClcgList = entityMapper.selectByExample(exampleclcg);
                dchyXmglCgtjDto.setDchyXmglClcgList(ClcgList);
                dchyXmglCgtjDto.setMessage(message);
                pushDataToMqService.pushCgtjResultToMq(dchyXmglCgtjDto);

                //线上提交测量成果修改提交人名称为测绘单位名称
                Example example = new Example(DchyXmglSqxx.class);
                example.createCriteria().andEqualTo("xssqxxid", glsxid);
                List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(example);
                if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                    Example exampleClcg = new Example(DchyXmglClcg.class);
                    exampleClcg.createCriteria().andEqualTo("sqxxid", dchyXmglSqxxList.get(0).getSqxxid());
                    logger.info("********线上提交成果的sqxxid:" + dchyXmglSqxxList.get(0).getSqxxid());
                    List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExample(exampleClcg);
                    if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                        for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                            dchyXmglClcg.setTjrmc(dwmc);
                            logger.info("********线上提交成果的clcgid:" + dchyXmglClcg.getClcgid());
                            entityMapper.saveOrUpdate(dchyXmglClcg, dchyXmglClcg.getClcgid());
                        }
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
