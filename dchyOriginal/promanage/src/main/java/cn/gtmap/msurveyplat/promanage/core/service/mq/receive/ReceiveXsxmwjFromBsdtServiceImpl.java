package cn.gtmap.msurveyplat.promanage.core.service.mq.receive;

/*
 * @author <a href="mailto:juyulin@gtmap.cn">juyulin</a>
 * @version 1.0, 2018/1/26
 * @description 接收来自受理云平台的领导催办信息 发布项目,推送从业人员信息包含名录库
 */

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.promanage.core.service.impl.CommissionFilingServiceImpl;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.rabbitmq.client.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description
 * @time 2020/11/27 15:07
 */
@Service
public class ReceiveXsxmwjFromBsdtServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "entityMapper")
    private EntityMapper entityMapper;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    CommissionFilingServiceImpl commissionFilingServiceImpl;

    @Override
    @RabbitListener(queues = Constants.BSDT_XMGL_TSWJ_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线上项目修改信息文件数据--消息接收成功**********");
            logger.info(Constants.BSDT_XMGL_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_TSWJ_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveOrDeleteXxData(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线上项目修改信息文件数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.BSDT_XMGL_TSWJ_QUEUE +
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
    public void saveOrDeleteXxData(String str) {

        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        String parentId = "";
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.BSDT_XMGL_TSWJ_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.BSDT_XMGL_TSWJ_QUEUE_MC);

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmDto xsxmxxModelSaveOrUpdate = JSON.toJavaObject(slxxJson, DchyXmglChxmDto.class);

            //保存的数据
            if (xsxmxxModelSaveOrUpdate != null) {
                DchyXmglChxm chxm = xsxmxxModelSaveOrUpdate.getDchyXmglChxm();
                if (chxm != null) {
                    String chxmid = chxm.getChxmid();
                    String xqfbbh = chxm.getXqfbbh();
                    if (StringUtils.isNotEmpty(xqfbbh)) {
                        Example example = new Example(DchyXmglChxm.class);
                        example.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                        List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
                        //判断文件是否先于数据到达，否则直接推送文件到线下文件中心,是则存储于数据库
                        if (dchyXmglChxmList != null) {
                            //基础数据已入库
                            this.saveOrDeleteWjData(str);
                        } else {
                            //基础数据未入库
                            DchyXmglMqTswj dchyXmglMqTswj = new DchyXmglMqTswj();
                            //组织数据
                            dchyXmglMqTswj.setTswjid(UUIDGenerator.generate18());
                            dchyXmglMqTswj.setChxmid(chxmid);
                            dchyXmglMqTswj.setJssj(CalendarUtil.getCurHMSDate());
                            dchyXmglMqTswj.setXqfbbh(xqfbbh);
                            dchyXmglMqTswj.setWjnr(str.getBytes(Charsets.UTF_8));
                            entityMapper.saveOrUpdate(dchyXmglMqTswj, dchyXmglMqTswj.getTswjid());
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrDeleteWjData(String str) {

        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        String parentId = "";
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));

            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmDto xsxmxxModelSaveOrUpdate = JSON.toJavaObject(slxxJson, DchyXmglChxmDto.class);

            //保存的数据
            if (xsxmxxModelSaveOrUpdate != null) {
                DchyXmglChxm chxm = xsxmxxModelSaveOrUpdate.getDchyXmglChxm();
                List<DchyXmglSjcl> xmglSjclList = xsxmxxModelSaveOrUpdate.getDchyXmglSjclList();
                List<DchyXmglSjxx> xmglSjxxList = xsxmxxModelSaveOrUpdate.getDchyXmglSjxxList();
                //线上推送的材料文件信息
                List<Map<String, List>> fileList = xsxmxxModelSaveOrUpdate.getFileList();
                //线上推送的材料文件信息
                List<Map<String, String>> sjclClsxList = xsxmxxModelSaveOrUpdate.getSjclClsxList();
                //线上推送的合同文件信息
                List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = xsxmxxModelSaveOrUpdate.getDchyXmglHtxxDtoList();

                if (chxm != null) {
                    String xqfbbh = chxm.getXqfbbh();
                    if (StringUtils.isNotEmpty(xqfbbh)) {
                        Example exampleChxm = new Example(DchyXmglChxm.class);
                        exampleChxm.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                        List<DchyXmglChxm> chxmlist = entityMapper.selectByExampleNotNull(exampleChxm);
                        if (CollectionUtils.isNotEmpty(chxmlist)) {
                            for (DchyXmglChxm newchxm : chxmlist) {
                                String newChxmid = newchxm.getChxmid();
                                if (StringUtils.isNotEmpty(newChxmid)) {
                                    //更新收件材料
                                    Example exampleSjxx = new Example(DchyXmglSjxx.class);
                                    exampleSjxx.createCriteria().andEqualTo("glsxid", newChxmid);
                                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
                                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                                        for (DchyXmglSjxx newSjxx : dchyXmglSjxxList) {
                                            String sjxxid = newSjxx.getSjxxid();
                                            if (StringUtils.isNotEmpty(sjxxid)) {
                                                if (StringUtils.equals(newSjxx.getSsmkid(), SsmkidEnum.JSDWWT.getCode())) {
                                                    //委托单
                                                    if (CollectionUtils.isNotEmpty(xmglSjclList)) {
                                                        for (DchyXmglSjcl xmglSjcl : xmglSjclList) {
                                                            String sjclid = xmglSjcl.getSjclid();
                                                            if (StringUtils.isNotEmpty(sjclid) && CollectionUtils.isNotEmpty(sjclClsxList)) {
                                                                for (Map<String, String> sjclCLsx : sjclClsxList) {
                                                                    String clsx = MapUtils.getString(sjclCLsx, sjclid);
                                                                    if (StringUtils.isNotEmpty(clsx)) {
                                                                        for (Map<String, List> fileMap : fileList) {
                                                                            /*文件上传*/
                                                                            logger.info("chxmid："+newChxmid+"--------------------委托材料名称："+xmglSjcl.getClmc());
                                                                            Map<String, Object> map = this.uploadWjcl(fileMap.get(xmglSjcl.getSjclid()), newChxmid, xmglSjcl.getClmc());
                                                                            String wjzxid = (String) map.get("folderId");
                                                                            if (StringUtils.isEmpty(wjzxid)) {
                                                                                logger.info("-------------------------------文件重新上传失败，更新wjzxid为空");
                                                                            }
                                                                            Example exampleSjcl = new Example(DchyXmglSjcl.class);
                                                                            exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid).andEqualTo("clsx", clsx);
                                                                            List<DchyXmglSjcl> sjclList = entityMapper.selectByExampleNotNull(exampleSjcl);
                                                                            if (CollectionUtils.isNotEmpty(sjclList)) {
                                                                                DchyXmglSjcl dchyXmglSjcl = sjclList.get(0);
                                                                                if (dchyXmglSjcl != null) {
                                                                                    /*更新收件材料表*/
                                                                                    dchyXmglSjcl.setWjzxid(wjzxid);
                                                                                    entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                                                                                    logger.info("************多测合一测绘收件材料入库成功");
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else if (!StringUtils.equals(newSjxx.getSsmkid(), SsmkidEnum.JSDWWT.getCode())) {
                                                    //核验材料
                                                    if (CollectionUtils.isNotEmpty(xmglSjxxList)) {
                                                        for (DchyXmglSjxx oldsjxx : xmglSjxxList) {
                                                            if (StringUtils.equals(oldsjxx.getSsmkid(), SsmkidEnum.CHDWHY.getCode()) && CollectionUtils.isNotEmpty(xmglSjclList) && StringUtils.isNotEmpty(oldsjxx.getSjxxid())) {
                                                                for (DchyXmglSjcl xmglSjcl : xmglSjclList) {
                                                                    if (StringUtils.equals(xmglSjcl.getSjxxid(), oldsjxx.getSjxxid())) {
                                                                        for (Map<String, List> fileMap : fileList) {
                                                                            /*文件上传*/
                                                                            logger.info("chxmid："+newChxmid+"--------------------核验材料名称："+xmglSjcl.getClmc());
                                                                            Map<String, Object> map = this.uploadWjcl(fileMap.get(xmglSjcl.getSjclid()), newChxmid, xmglSjcl.getClmc());
                                                                            String wjzxid = (String) map.get("folderId");
                                                                            if (StringUtils.isEmpty(wjzxid)) {
                                                                                logger.info("-------------------------------文件重新上传失败，更新wjzxid为空");
                                                                            }
                                                                            Example exampleSjclxx = new Example(DchyXmglSjcl.class);
                                                                            exampleSjclxx.createCriteria().andEqualTo("sjxxid", sjxxid);
                                                                            List<DchyXmglSjcl> sjclList = entityMapper.selectByExampleNotNull(exampleSjclxx);
                                                                            if (CollectionUtils.isNotEmpty(sjclList)) {
                                                                                DchyXmglSjcl dchyXmglSjcl = sjclList.get(0);
                                                                                if (dchyXmglSjcl != null) {
                                                                                    /*更新收件材料表*/
                                                                                    dchyXmglSjcl.setWjzxid(wjzxid);
                                                                                    entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                                                                                    logger.info("************多测合一测绘收件材料入库成功");
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            newSjxx.setGlsxid(newChxmid);
                                            entityMapper.saveOrUpdate(newSjxx, newSjxx.getSjxxid());
                                        }
                                    }
                                }

                                //合同文件
                                Example exampleHtxxNew = new Example(DchyXmglHtxx.class);
                                exampleHtxxNew.createCriteria().andEqualTo("chxmid", newChxmid);
                                List<DchyXmglHtxx> newHtxxList = entityMapper.selectByExampleNotNull(exampleHtxxNew);
                                if (CollectionUtils.isNotEmpty(newHtxxList)) {
                                    //拆分后一个项目对应一个测绘阶段，一个测绘阶段对应一份合同
                                    for (DchyXmglHtxx htxx : newHtxxList) {
                                        String newHtxxid = htxx.getHtxxid();
                                        Example exampleClsxHtxx = new Example(DchyXmglClsxHtxxGx.class);
                                        exampleClsxHtxx.createCriteria().andEqualTo("htxxid", newHtxxid).andEqualTo("chxmid", newChxmid);
                                        List<DchyXmglClsxHtxxGx> clsxHtxxGxList = entityMapper.selectByExampleNotNull(exampleClsxHtxx);
                                        if (CollectionUtils.isNotEmpty(clsxHtxxGxList)) {
                                            String clsxid = clsxHtxxGxList.get(0).getClsxid();
                                            if (StringUtils.isNotEmpty(clsxid)) {
                                                DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                                                if (dchyXmglChxmClsx != null) {
                                                    String clsx = dchyXmglChxmClsx.getClsx();
                                                    if (StringUtils.isNotEmpty(clsx)) {
                                                        //线上备案时推送到线下的合同文件信息
                                                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                                                            for (DchyXmglHtxxDto dchyXmglHtxxDto : dchyXmglHtxxDtoList) {
                                                                if (dchyXmglHtxxDto != null) {
                                                                    /*合同信息*/
                                                                    DchyXmglHtxx oldhtxx = new DchyXmglHtxx();
                                                                    if (CollectionUtils.isNotEmpty(dchyXmglHtxxDto.getDchyXmglHtxxList())) {
                                                                        oldhtxx = dchyXmglHtxxDto.getDchyXmglHtxxList().get(0);
                                                                    }
                                                                    /*收件材料*/
                                                                    List<DchyXmglSjcl> sjclList1 = dchyXmglHtxxDto.getDchyXmglSjclList();
                                                                    /*合同文件*/
                                                                    List<Map<String, List>> htFileList = dchyXmglHtxxDto.getHtFileList();
                                                                    //合同测量事项关系
                                                                    List<Map<String, String>> htClsxList = dchyXmglHtxxDto.getHtClsxList();

                                                                    if (CollectionUtils.isNotEmpty(htClsxList)) {
                                                                        for (Map<String, String> htClsx : htClsxList) {
                                                                            if (oldhtxx != null) {
                                                                                String oldhtxxid = oldhtxx.getHtxxid();
                                                                                if (StringUtils.isNotEmpty(oldhtxxid)) {
                                                                                    String oldclsx = MapUtils.getString(htClsx, oldhtxxid);
                                                                                    if (StringUtils.isNotBlank(oldclsx) && StringUtils.equals(oldclsx, clsx)) {
                                                                                        for (DchyXmglSjcl sjcl : sjclList1) {
                                                                                            if (CollectionUtils.isNotEmpty(htFileList)) {
                                                                                                for (Map<String, List> htFile : htFileList) {
                                                                                                    /*合同文件上传*/
                                                                                                    logger.info("合同信息ID："+newHtxxid+"--------------------材料名称："+sjcl.getClmc());
                                                                                                    Map<String, Object> map = this.uploadWjcl(htFile.get(sjcl.getSjclid()), newHtxxid, sjcl.getClmc());
                                                                                                    String wjzxid = (String) map.get("folderId");
                                                                                                    String parentid = (String) map.get("parentId");
                                                                                                    if (StringUtils.isEmpty(wjzxid)) {
                                                                                                        logger.info("-------------------------------文件重新上传失败，更新wjzxid为空");
                                                                                                    }
                                                                                                    Example exampleSjxx = new Example(DchyXmglSjxx.class);
                                                                                                    exampleSjxx.createCriteria().andEqualTo("glsxid", newHtxxid);
                                                                                                    List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
                                                                                                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                                                                                                        for (DchyXmglSjxx sjxx : dchyXmglSjxxList) {
                                                                                                            String sjxxid = sjxx.getSjxxid();
                                                                                                            Example exampleSjcl = new Example(DchyXmglSjcl.class);
                                                                                                            exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                                                                                                            List<DchyXmglSjcl> sjclList = entityMapper.selectByExampleNotNull(exampleSjcl);

                                                                                                            if (CollectionUtils.isNotEmpty(sjclList)) {
                                                                                                                for (DchyXmglSjcl newsjcl : sjclList) {
                                                                                                                    /*更新收件材料表*/
                                                                                                                    if (StringUtils.isNoneBlank(newsjcl.getSjclid())) {
                                                                                                                        newsjcl.setWjzxid(wjzxid);
                                                                                                                        entityMapper.saveOrUpdate(newsjcl, newsjcl.getSjclid());
                                                                                                                        logger.info("************多测合一测绘收件材料入库成功");
                                                                                                                        DchyXmglHtxx dchyXmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, newHtxxid);
                                                                                                                        dchyXmglHtxx.setWjzxid(parentid);
                                                                                                                        entityMapper.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                                                                                                                        logger.info("************更新合同的wjzxid");
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                //文件上传完更新文件同步状态
                                newchxm.setWjsftb(Constants.DCHY_XMGL_WJTS_TB);
                                int tbzt = entityMapper.saveOrUpdate(newchxm, newchxm.getChxmid());
                                if (tbzt > 0) {
                                    logger.info("******************************文件同步完成*****************************");
                                }
                            }
                        }
                    }
                }

                //上传完材料、合同文件后，删除文件存放记录
                if (StringUtils.isNotEmpty(chxm.getXqfbbh())) {
                    Example exampletswj = new Example(DchyXmglMqTswj.class);
                    exampletswj.createCriteria().andEqualTo("xqfbbh", chxm.getXqfbbh());
                    List<DchyXmglMqTswj> dchyXmglMqTswjList = entityMapper.selectByExampleNotNull(exampletswj);
                    if (CollectionUtils.isNotEmpty(dchyXmglMqTswjList)) {
                        for (DchyXmglMqTswj dchyXmglMqTswjLists : dchyXmglMqTswjList) {
                            String tswjid = dchyXmglMqTswjLists.getTswjid();
                            entityMapper.deleteByPrimaryKey(DchyXmglMqTswj.class, tswjid);
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

    private Map<String, Object> uploadWjcl(List<Map<String, String>> wjList, String mainId, String clmc) throws
            IOException {
        Map<String, Object> objectMap = Maps.newHashMap();
        logger.info("-----------------------------------------------------材料ID：" + mainId);
        if (CollectionUtils.isNotEmpty(wjList)) {
            MultipartFile[] files = new MultipartFile[wjList.size()];
            int i = 0;
            for (Map<String, String> map : wjList) {
                // 文件名--文件内容
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    //System.out.println(base64);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    if (bytes == null) {
                        logger.info("-------------------------------------原文件是空文件");
                    }
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    MockMultipartFile file = new MockMultipartFile(key, key, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                    byte[] byte2 = file.getBytes();
                    if (byte2 == null) {
                        logger.info("-------------------------------------现文件是空文件");
                    }
                    files[i] = file;
                    i++;
                }
            }
            // 文件,父的name,收件材料id
            objectMap.putAll(FileUploadUtil.uploadFile(files, mainId, clmc));
        }
        return objectMap;
    }

}
