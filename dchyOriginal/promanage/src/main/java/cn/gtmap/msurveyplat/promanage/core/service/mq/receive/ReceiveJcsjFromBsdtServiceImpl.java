package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.TaskData;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.service.impl.PortalFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @description 接收来自办事大厅的基础数据
 * @time 2021/04/12 17:04
 */
@Service
public class ReceiveJcsjFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Autowired
    PortalFeignServiceImpl portalFeignService;

    @Autowired
    private ExchangeFeignServiceImpl dispatchOrderFsmServiceImpl;

    @Autowired
    private PlatformUtil platformUtil;

    @Value("${jcsjsq.gzldyid}")
    private String wdid;

    @Value("${jcsjsq.userId}")
    private String userid;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_JCSJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上基础数据申请数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_JCSJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveJcsj(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上基础数据申请数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_JCSJ_QUEUE +
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
    public void saveJcsj(String str) {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_JCSJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_JCSJ_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglJcsjsqDto dchyXmglJcsjsqDto = JSON.toJavaObject(slxxJson, DchyXmglJcsjsqDto.class);
            if (null != dchyXmglJcsjsqDto) {
                DchyXmglJcsjSqxx dchyXmglJcsjSqxx = dchyXmglJcsjsqDto.getDchyXmglJcsjSqxx();

                Map sjfw = dchyXmglJcsjsqDto.getSjfw();
                //接收到线上的基础数据范围文件,创建流程,拿流程中的受理编号作为glsxid上传文件
                String glsxid = "";

                if (dchyXmglJcsjSqxx != null) {
                    if (StringUtils.isNotBlank(dchyXmglJcsjSqxx.getJcsjsqxxid())) {
                        int flag = entityMapper.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());
                        if (flag > 0) {
                            logger.info("************基础数据申请信息入库成功");
                            logger.info("************基础数据申请信息入库成功userid:" + userid);
                            logger.info("************基础数据申请信息入库成功wdid:" + wdid);
                            if (StringUtils.isNoneBlank(userid, wdid)) {
                                TaskData taskData = portalFeignService.createTask(wdid, userid);
                                if (null != taskData) {
                                    glsxid = taskData.getProcessDefName();
                                    logger.info("************基础数据申请流程创建成功" + taskData.toString());
                                    String sqxxid = taskData.getExecutionId();
                                    String gzlslid = taskData.getProcessInstanceId();
                                    String jcsjsqxxid = dchyXmglJcsjSqxx.getJcsjsqxxid();
                                    DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
                                    logger.info("************基础数据申请流程创建成功sqxxid:" + sqxxid);
                                    logger.info("************基础数据申请流程创建成功jcsjsqxxid:" + jcsjsqxxid);
                                    dchyXmglSqxx.setSqxxid(sqxxid);
                                    dchyXmglSqxx.setGlsxid(jcsjsqxxid);
                                    dchyXmglSqxx.setXssqxxid(UUIDGenerator.generate18());
                                    int flagSqxx = entityMapper.saveOrUpdate(dchyXmglSqxx, sqxxid);
                                    //通过申请信息id更新待办任务字段
                                    if (flagSqxx > 0) {
                                        String slbh = "";
                                        String jsdw = "";
                                        String chdw = "";
                                        String xmbh = "";
                                        String xmmc = "";
                                        StringBuilder xmdz = new StringBuilder();
                                        String slr = "";
                                        // 上传
                                        if (StringUtils.isNoneBlank(gzlslid, sqxxid)) {
                                            if (null != dchyXmglSqxx) {
                                                slbh = dchyXmglSqxx.getSqbh();
                                                slr = dchyXmglSqxx.getSqrmc();
                                            }
                                            jsdw = dchyXmglJcsjSqxx.getJsdw();
                                            chdw = dchyXmglJcsjSqxx.getChdwmc();
                                            xmbh = dchyXmglJcsjSqxx.getXmbabh();
                                            xmmc = dchyXmglJcsjSqxx.getGcmc();
                                        }
                                        StringBuilder remark = new StringBuilder();
                                        remark.append(slbh).append(Constants.PLATFORM_SPLIT_STR);
                                        remark.append(jsdw).append(Constants.PLATFORM_SPLIT_STR);
                                        remark.append(chdw).append(Constants.PLATFORM_SPLIT_STR);
                                        remark.append(xmdz).append(Constants.PLATFORM_SPLIT_STR);
                                        remark.append(slr);
                                        remark.append(xmbh).append(Constants.PLATFORM_SPLIT_STR);
                                        remark.append(xmmc).append(Constants.PLATFORM_SPLIT_STR);
                                        ActStProRelDo actStProRelDo = new ActStProRelDo();
                                        actStProRelDo.setProcInsId(gzlslid);
                                        actStProRelDo.setText1(slbh);
                                        actStProRelDo.setText2(jsdw);
                                        actStProRelDo.setText3(chdw);
                                        actStProRelDo.setText4(xmdz.toString());
                                        actStProRelDo.setText5(slr);
                                        actStProRelDo.setText6(xmbh);
                                        actStProRelDo.setText10(xmmc);
                                        logger.info(JSONObject.toJSONString(actStProRelDo));
                                        dispatchOrderFsmServiceImpl.saveOrUpdateTaskExtendDto(actStProRelDo);
                                        //更新rmark字段
                                        dispatchOrderFsmServiceImpl.updateRemark(gzlslid, remark.toString());
                                    }
                                }
                            }
                        }
                    }
                }

                if (null != dchyXmglJcsjSqxx && StringUtils.isNoneBlank(dchyXmglJcsjSqxx.getJcsjsqxxid(), dchyXmglJcsjSqxx.getBabh())) {
                    String base64Sjfw = CommonUtil.formatEmptyValue(sjfw.get(dchyXmglJcsjSqxx.getXmbabh()));
                    String mlmcSjfw = CommonUtil.formatEmptyValue(sjfw.get("wjmc"));
                    if (StringUtils.isNoneBlank(base64Sjfw, mlmcSjfw, glsxid)) {
                        InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Sjfw));
                        uploadFile(glsxid, mlmcSjfw, inputStream);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("接收失败原因:{}", e);
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapper.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
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

            dchyXmglSjcl.setSjclid(UUIDGenerator.generate18());
            dchyXmglSjcl.setSjxxid(dchyXmglSjxx.getSjxxid());
            dchyXmglSjcl.setClrq(CalendarUtil.getCurHMSDate());
            dchyXmglSjcl.setWjzxid(folderId + "");
            dchyXmglSjcl.setClmc(mlmc);
            dchyXmglSjcl.setCllx("1");
            dchyXmglSjcl.setYs(Constants.DCHY_XMGL_MRYS);
            dchyXmglSjcl.setFs(Constants.DCHY_XMGL_MRFS);

            int flag1 = entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
            int flag2 = entityMapper.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());

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
