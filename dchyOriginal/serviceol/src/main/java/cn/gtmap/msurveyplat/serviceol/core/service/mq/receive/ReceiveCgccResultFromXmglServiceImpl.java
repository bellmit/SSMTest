package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCgcc;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjxx;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgccDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.utils.CommonUtil;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/15
 * @description ??????????????????????????????????????????
 */
@Service
public class ReceiveCgccResultFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Autowired
    private PlatformUtil platformUtil;

    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_CGCC_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********??????????????????--??????????????????**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_CGCC_QUEUE +
                    "?????????????????????:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveCgccResult(str);
            basicACK(message, channel); //????????????????????????mq?????????????????????mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********??????????????????--??????????????????**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_CGCC_QUEUE +
                    "?????????????????????:" + CalendarUtil.getCurHMSStrDate());
            logger.error("??????????????????", e);
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

    //???????????????mq????????????
    private void basicNACK(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            logger.error("mq??????????????????????????????????????????????????????", e);
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveCgccResult(String str) throws Exception {
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_CGCC_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_CGCC_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglCgccDto dchyXmglCgccDto = JSON.toJavaObject(slxxJson, DchyXmglCgccDto.class);
            if (null != dchyXmglCgccDto) {
                String glsxid = dchyXmglCgccDto.getGlsxid();
                DchyXmglCgcc dchyXmglCgcc = dchyXmglCgccDto.getDchyXmglCgcc();
                List<Map<String, Object>> fileList = dchyXmglCgccDto.getCgccPjclList();

                //??????glsxid???????????????
                if (StringUtils.isNotBlank(glsxid)) {
                    Example exampleSjxx = new Example(DchyXmglSjxx.class);
                    exampleSjxx.createCriteria().andEqualTo("glsxid", glsxid);
                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapperXSBF.selectByExample(exampleSjxx);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                        for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                            String sjxxid = dchyXmglSjxx.getSjxxid();
                            Example exampleSjcl = new Example(DchyXmglSjcl.class);
                            exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                            entityMapperXSBF.deleteByExample(exampleSjcl);
                        }
                        entityMapperXSBF.deleteByExample(exampleSjxx);
                    }
                }

                if (null != dchyXmglCgcc && StringUtils.isNotBlank(dchyXmglCgcc.getCgccid())) {
                    if (CollectionUtils.isNotEmpty(fileList)) {
                        for (Map cgccPjcl : fileList) {
                            String base64 = CommonUtil.formatEmptyValue(cgccPjcl.get(dchyXmglCgcc.getCgccid()));
                            String mlmc = CommonUtil.formatEmptyValue(cgccPjcl.get("mlmc"));
                            if (StringUtils.isNotBlank(base64)) {
                                InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
                                uploadFile(dchyXmglCgcc.getCgccid(), mlmc, inputStream);
                            }

                        }
                        entityMapperXSBF.saveOrUpdate(dchyXmglCgcc, dchyXmglCgcc.getCgccid());
                        logger.info("************????????????????????????????????????");
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
        //1,???????????????????????????
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
            logger.info("************??????????????????????????????????????????");
            //2,????????????????????????
            DchyXmglSjxx dchyXmglSjxx = new DchyXmglSjxx();
            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjxx.setSjsj(CalendarUtil.getCurHMSDate());
            dchyXmglSjxx.setSjxxid(UUIDGenerator.generate18());
            dchyXmglSjxx.setTjr(UserUtil.getCurrentUserId());
            dchyXmglSjxx.setGlsxid(glsxid);
            dchyXmglSjxx.setSsmkid(SsmkidEnum.CGCCPJ.getCode());

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
                logger.info("************????????????????????????????????????????????????");
            }
            if (flag2 > 0) {
                logger.info("************????????????????????????????????????????????????");
            }
        } catch (IOException e) {
            logger.error("????????????:{}", e);
        }
    }
}
