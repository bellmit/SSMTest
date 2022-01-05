package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
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
import java.io.InputStream;
import java.util.List;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 接收来自线下的基础数据申请的结果
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveJcsjFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Autowired
    private PlatformUtil platformUtil;


    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_JCSJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线下基础数据申请数据--消息接收成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_JCSJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveJcsjResult(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线下基础数据申请数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_JCSJ_QUEUE +
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
    public void saveJcsjResult(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_JCSJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_JCSJ_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglJcsjsqDto dchyXmglJcsjsqDto = JSON.toJavaObject(slxxJson, DchyXmglJcsjsqDto.class);
            if (null != dchyXmglJcsjsqDto) {
                DchyXmglSqxx dchyXmglSqxx = dchyXmglJcsjsqDto.getDchyXmglSqxx();
                DchyXmglJcsjSqxx dchyXmglJcsjSqxx = dchyXmglJcsjsqDto.getDchyXmglJcsjSqxx();
                List<DchyXmglCzrz> dchyXmglCzrzList = dchyXmglJcsjsqDto.getDchyXmglCzrzList();
                DchyXmglJcsjShjl dchyXmglJcsjShjl = dchyXmglJcsjsqDto.getDchyXmglJcsjShjl();
                DchyXmglJcsjJdxx dchyXmglJcsjJdxx = dchyXmglJcsjsqDto.getDchyXmglJcsjJdxx();
//                Map sjfw = dchyXmglJcsjsqDto.getSjfw();
//                Map jcsj = dchyXmglJcsjsqDto.getJcsj();
                if (null != dchyXmglSqxx && StringUtils.isNotBlank(dchyXmglSqxx.getSqxxid())) {
                    //由于线上数据推送到线下,线下开始创建流程,所以回推到线上时两个id交换一下
                    String sqxxid = dchyXmglSqxx.getSqxxid();
                    String xssqxxid = dchyXmglSqxx.getXssqxxid();

                    dchyXmglSqxx.setSqxxid(xssqxxid);
                    dchyXmglSqxx.setXssqxxid(sqxxid);
                    entityMapperXSBF.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                    logger.info("************多测合一申请信息入库成功");
                }

                if (null != dchyXmglSqxx && StringUtils.isNotBlank(dchyXmglJcsjSqxx.getJcsjsqxxid())) {
                    entityMapperXSBF.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());
                    logger.info("************多测合一基础数据申请信息入库成功");
                }

//                if (null != sjfw) {
//                    String base64Sjfw = CommonUtil.formatEmptyValue(sjfw.get(dchyXmglJcsjSqxx.getBabh()));
//                    String mlmcSjfw = CommonUtil.formatEmptyValue(sjfw.get("wjmc"));
//                    if (StringUtils.isNoneBlank(base64Sjfw, mlmcSjfw)) {
//                        InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Sjfw));
//                        uploadFile(dchyXmglJcsjSqxx.getBabh(), mlmcSjfw, inputStream);
//                    }
//
//                    String base64Jcsj = CommonUtil.formatEmptyValue(jcsj.get(dchyXmglJcsjSqxx.getJcsjsqxxid()));
//                    String mlmcJcsj = CommonUtil.formatEmptyValue(jcsj.get("wjmc"));
//                    if (StringUtils.isNoneBlank(base64Jcsj, mlmcJcsj)) {
//                        InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Jcsj));
//                        uploadFile(dchyXmglJcsjSqxx.getJcsjsqxxid(), mlmcJcsj, inputStream);
//                    }
//                }

                if (null != dchyXmglJcsjShjl && StringUtils.isNotBlank(dchyXmglJcsjShjl.getShjlid())) {
                    entityMapperXSBF.saveOrUpdate(dchyXmglJcsjShjl, dchyXmglJcsjShjl.getShjlid());
                    logger.info("************多测合一基础数据申请审核记录入库成功");
                }

                if (null != dchyXmglJcsjJdxx && StringUtils.isNotBlank(dchyXmglJcsjJdxx.getJdxxid())) {
                    entityMapperXSBF.saveOrUpdate(dchyXmglJcsjJdxx, dchyXmglJcsjJdxx.getJdxxid());
                    logger.info("************多测合一基础数据申请进度信息入库成功");
                }

                if (CollectionUtils.isNotEmpty(dchyXmglCzrzList)) {
                    for (DchyXmglCzrz dchyXmglCzrz : dchyXmglCzrzList) {
                        if (StringUtils.isNotBlank(dchyXmglCzrz.getCzrzid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglCzrz, dchyXmglCzrz.getCzrzid());
                            logger.info("************多测合一基础数据申请操作日志入库成功");
                        }
                    }
                }

            }

        } catch (Exception e) {
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapperXSBF.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            throw new Exception();
        }

    }

    private void uploadFile(String glsxid, String mlmc, InputStream inputStream) {
        //1,文件上传至文件中心
        try {
            FileService fileService = getFileService();
            Integer parentId = platformUtil.creatNode(glsxid);
            Integer folderId = platformUtil.createFileFolderByclmc(parentId, mlmc);
            List<Node> nodeList = platformUtil.getChildNodeListByParentId(folderId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (Node node : nodeList) {
                    platformUtil.deleteNodeById(node.getId());
                }
            }

            fileService.uploadFile(inputStream, folderId, mlmc);
            logger.info("************多测合一基础数据材料入库成功");
            //2,保存数据到数据库
            DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjxx.setSjsj(CalendarUtil.getCurHMSDate());
            dchyXmglSjxx.setSjxxid(UUIDGenerator.generate18());
            dchyXmglSjxx.setTjr(UserUtil.getCurrentUserId());
            dchyXmglSjxx.setGlsxid(glsxid);
            dchyXmglSjxx.setSsmkid(SsmkidEnum.JCSJSQ.getCode());

            dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
            dchyXmglSjcl.setSjxxid(dchyXmglSjxx.getSjxxid());
            dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
            dchyXmglSjcl.setWjzxid(folderId + "");
            dchyXmglSjcl.setClmc(mlmc);
            dchyXmglSjcl.setCllx("1");
            dchyXmglSjcl.setYs(Constants.DCHY_XMGL_MRYS);
            dchyXmglSjcl.setFs(Constants.DCHY_XMGL_MRFS);

            int flag1 = entityMapperXSBF.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            int flag2 = entityMapperXSBF.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

            if (flag1 > 0) {
                logger.info("************多测合一基础数据材料收件材料入库成功");
            }
            if (flag2 > 0) {
                logger.info("************多测合一基础数据材料收件信息入库成功");
            }
        } catch (IOException e) {
            logger.error("错误原因:{}", e);
        }
    }


}
