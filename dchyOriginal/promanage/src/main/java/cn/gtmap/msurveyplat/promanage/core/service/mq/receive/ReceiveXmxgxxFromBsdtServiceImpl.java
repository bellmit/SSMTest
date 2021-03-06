package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2018/1/16
 * @description 接收来自受理云平台的项目派件信息
 */
@Service
public class ReceiveXmxgxxFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_FWPJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********项目修改信息数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_FWPJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveOrDeleteMlkxx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********项目修改信息数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_FWPJ_QUEUE +
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
    public void saveOrDeleteMlkxx(String str) {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_FWPJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_FWPJ_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglMlkDto dchyXmglMlkDto = JSON.toJavaObject(slxxJson, DchyXmglMlkDto.class);

            //保存的数据
            if (dchyXmglMlkDto != null) {
                List<DchyXmglMlk> dchyXmglMlkListSaveOrUpdate = dchyXmglMlkDto.getDchyXmglMlkList();
                List<DchyXmglCyry> dchyXmglCyryListSaveOrUpdate = dchyXmglMlkDto.getDchyXmglCyryList();
                List<DchyXmglSjcl> dchyXmglSjclListSaveOrUpdate = dchyXmglMlkDto.getDchyXmglSjclList();
                List<DchyXmglSjxx> dchyXmglSjxxListSaveOrUpdate = dchyXmglMlkDto.getDchyXmglSjxxList();
                List<DchyXmglKp> dchyXmglKpListSaveOrUpdate = dchyXmglMlkDto.getDchyXmglKpList();
                List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxList = dchyXmglMlkDto.getDchyXmglMlkClsxGxList();

                if (CollectionUtils.isNotEmpty(dchyXmglMlkListSaveOrUpdate)) {
                    for (DchyXmglMlk dchyXmglMlk : dchyXmglMlkListSaveOrUpdate) {
                        if (StringUtils.isNoneBlank(dchyXmglMlk.getMlkid())) {
                            /*清空之前名录库相关的测量事项数据*/
                            if(CollectionUtils.isNotEmpty(dchyXmglMlkDto.getDchyXmglMlkClsxGxList())){
                                Example mlkClsxGxExample = new Example(DchyXmglMlkClsxGx.class);
                                mlkClsxGxExample.createCriteria().andEqualTo("mlkid", dchyXmglMlk.getMlkid());
                                entityMapper.deleteByExampleNotNull(mlkClsxGxExample);
                            }
                            entityMapper.saveOrUpdate(dchyXmglMlk, dchyXmglMlk.getMlkid());
                            logger.info("************名录库入库成功");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglCyryListSaveOrUpdate)) {
                    for (DchyXmglCyry dchyXmglCyry : dchyXmglCyryListSaveOrUpdate) {
                        if (StringUtils.isNoneBlank(dchyXmglCyry.getCyryid())) {
                            entityMapper.saveOrUpdate(dchyXmglCyry, dchyXmglCyry.getCyryid());
                            logger.info("************从业人员入库成功");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglSjclListSaveOrUpdate)) {
                    for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclListSaveOrUpdate) {
                        if (StringUtils.isNoneBlank(dchyXmglSjcl.getSjclid())) {
                            entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                            logger.info("************收件材料入库成功");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglSjxxListSaveOrUpdate)) {
                    for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxListSaveOrUpdate) {
                        if (StringUtils.isNoneBlank(dchyXmglSjxx.getSjxxid())) {
                            entityMapper.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());
                            logger.info("************收件信息入库成功");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglKpListSaveOrUpdate)) {
                    for (DchyXmglKp dchyXmglKp : dchyXmglKpListSaveOrUpdate) {
                        if (StringUtils.isNoneBlank(dchyXmglKp.getKpid())) {
                            entityMapper.saveOrUpdate(dchyXmglKp, dchyXmglKp.getKpid());
                            logger.info("************考评信息入库成功");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglMlkClsxGxList)) {
                    for (DchyXmglMlkClsxGx dchyXmglMlkClsxGx : dchyXmglMlkClsxGxList) {
                        if (StringUtils.isNoneBlank(dchyXmglMlkClsxGx.getGxid()) && dchyXmglMlkClsxGx.getGxid().indexOf("delete_") == -1) {
                            entityMapper.saveOrUpdate(dchyXmglMlkClsxGx, dchyXmglMlkClsxGx.getGxid());
                            logger.info("************名录库测量事项关系表入库成功");
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
