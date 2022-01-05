package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmListDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileUploadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.service.FileService;
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

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getFileService;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 接收线下备案拆分信息
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveBaxxFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlatformUtil platformUtil;

    @Autowired
    private EntityMapper entityMapper;

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_BAXX_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********线下备案拆分信息数据--消息接收成功**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_BAXX_QUEUE +
                    "消息接收时间为:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveOrDeleteXxSlxx(str);
            basicACK(message, channel); //正常消费掉后通知mq服务器移除此条mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********线下受理信息数据--消息接收失败**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_BAXX_QUEUE +
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
    public void saveOrDeleteXxSlxx(String str) throws Exception {
        String parentId = "";

        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_BAXX_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_BAXX_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmListDto dchyXmglChxmDtoListSaveOrUpdate = JSON.toJavaObject(slxxJson, DchyXmglChxmListDto.class);
            Map<String, String> idMap = (Map<String, String>) slxxJson.get("delete");

            if (dchyXmglChxmDtoListSaveOrUpdate != null) {
                logger.info("*************************************输出*****************************************");
                List<DchyXmglChxmDto> dchyXmglChxmDtoList = dchyXmglChxmDtoListSaveOrUpdate.getDchyXmglChxmListDto();
                for (DchyXmglChxmDto dchyXmglChxmDtoSaveOrUpdate : dchyXmglChxmDtoList) {
                    //保存的数据
                    if (dchyXmglChxmDtoSaveOrUpdate != null) {
                        DchyXmglChgc dchyXmglChgc = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChgc();
                        DchyXmglChxm dchyXmglChxm = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChxm();
                        List<DchyXmglChdw> dchyXmglChdwList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChdwList();
                        List<DchyXmglSjxx> dchyXmglSjxxList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglSjxxList();
                        List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChxmChdwxxList();
                        List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChxmClsxList();
                        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglHtxxDtoList();
                        List<DchyXmglSjcl> dchyXmglSjclList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglSjclList();
                        Map<String, List<Map<String, String>>> stringListMap = dchyXmglChxmDtoSaveOrUpdate.getSjcl();
                        List<DchyXmglYbrw> dchyXmglYbrwList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglYbrwList();
                        List<DchyXmglSqxx> dchyXmglSqxxList = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglSqxxList();
                        List<Map<String, List>> fileList = dchyXmglChxmDtoSaveOrUpdate.getFileList();
                        List<DchyXmglClsxChtl> dchyXmglClsxChtlList= dchyXmglChxmDtoSaveOrUpdate.getDchyXmglClsxChtlList();
                        //DchyXmglChxm failDchyXmglChxm = dchyXmglChxmDtoSaveOrUpdate.getDchyXmglChxm();

                        /*if (failDchyXmglChxm != null && StringUtils.isNoneBlank(failDchyXmglChxm.getChxmid())) {
                            int falg = entityMapperXSBF.saveOrUpdate(failDchyXmglChxm, failDchyXmglChxm.getChxmid());
                            if (falg > 0) {
                                logger.info("************多测合一备案失败，测绘项目委托状态更新成功");
                            } else {
                                logger.info("************多测合一备案失败，测绘项目委托状态更新失败");
                            }
                        }*/

                        if (dchyXmglChgc != null && StringUtils.isNoneBlank(dchyXmglChgc.getChgcid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglChgc, dchyXmglChgc.getChgcid());
                            logger.info("************多测合一测绘工程入库成功");
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglChdwList)) {
                            for (DchyXmglChdw dchyXmglChdw : dchyXmglChdwList) {
                                if (StringUtils.isNoneBlank(dchyXmglChdw.getChdwid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglChdw, dchyXmglChdw.getChdwid());
                                    logger.info("************多测合一测绘单位入库成功");
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                            for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                                if (StringUtils.isNoneBlank(dchyXmglChxmChdwxx.getChdwxxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglChxmChdwxx, dchyXmglChxmChdwxx.getChdwxxid());
                                    logger.info("************多测合一测绘项目测绘单位信息入库成功");
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                            for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                                if (dchyXmglChxmClsx != null && StringUtils.isNoneBlank(dchyXmglChxmClsx.getClsxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglChxmClsx, dchyXmglChxmClsx.getClsxid());
                                    logger.info("************多测合一测绘项目测量事项信息入库成功");
                                }

                            }
                        }

                        if (dchyXmglChxm != null) {
                            String xqfbbh = dchyXmglChxm.getXqfbbh();
                            String oldchxmid = null;
                            if (StringUtils.isNotEmpty(xqfbbh)) {
                                Example exampleOldChxm = new Example(DchyXmglChxm.class);
                                exampleOldChxm.createCriteria().andEqualTo("xqfbbh", xqfbbh);
                                List<DchyXmglChxm> oldChxmList = entityMapper.selectByExampleNotNull(exampleOldChxm);
                                if (CollectionUtils.isNotEmpty(oldChxmList)) {
                                    oldchxmid = oldChxmList.get(0).getChxmid();
                                }
                            }

                            if (StringUtils.isNoneBlank(dchyXmglChxm.getChxmid())) {
                                dchyXmglChxm.setWjzxid(parentId);
                                int i = entityMapperXSBF.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());
                                if (i > 0) {
                                    logger.info("************多测合一测绘项目入库成功");
                                    logger.info("************测绘项目ID：" + dchyXmglChxm.getChxmid());
                                    logger.info("************备案编号：" + dchyXmglChxm.getSlbh());
                                }
                            }

                            if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                                if (CollectionUtils.isNotEmpty(fileList) && StringUtils.equals(dchyXmglChxm.getXmly().trim(), Constants.XMLY_XXBA)) {
                                    //线下直接备案推送的材料文件信息
                                    for (DchyXmglSjcl xmglSjcl : dchyXmglSjclList) {
                                        if (StringUtils.isNotEmpty(xmglSjcl.getSjclid())) {
                                            for (Map<String, List> fileMap : fileList) {
                                                /*文件上传*/
                                                Map<String, Object> map = this.uploadWjcl(fileMap.get(xmglSjcl.getSjclid()), dchyXmglChxm.getChxmid(), xmglSjcl.getClmc());
                                                String wjzxid = (String) map.get("folderId");
                                                if (xmglSjcl != null) {
                                                    /*更新收件材料表*/
                                                    xmglSjcl.setWjzxid(wjzxid);
                                                    entityMapperXSBF.saveOrUpdate(xmglSjcl, xmglSjcl.getSjclid());
                                                    logger.info("************多测合一测绘收件材料入库成功");
                                                }
                                            }
                                        }
                                    }
                                } else if (CollectionUtils.isNotEmpty(fileList) && !StringUtils.equals(dchyXmglChxm.getXmly().trim(), Constants.XMLY_XXBA)) {
                                    //属于线上备案推送的材料文件信息，将备份库的wjzxid替换为线上库的wjzxid
                                    Example exampleOldSjxx = new Example(DchyXmglSjxx.class);
                                    exampleOldSjxx.createCriteria().andEqualTo("glsxid", oldchxmid);
                                    List<DchyXmglSjxx> oldSjxxList = entityMapper.selectByExample(exampleOldSjxx);
                                    if(CollectionUtils.isNotEmpty(oldSjxxList)){
                                        List<Object> oldSjxxidList = Lists.newArrayList();
                                        for(DchyXmglSjxx oldSjxx:oldSjxxList){
                                            if(StringUtils.isNotEmpty(oldSjxx.getSjxxid())){
                                                oldSjxxidList.add(oldSjxx.getSjxxid());
                                            }
                                        }
                                        Example exampleOldSjcl = new Example(DchyXmglSjcl.class);
                                        exampleOldSjcl.createCriteria().andIn("sjxxid", oldSjxxidList);
                                        List<DchyXmglSjcl> oldSjclList = entityMapper.selectByExample(exampleOldSjcl);

                                        for (DchyXmglSjcl xmglSjcl : dchyXmglSjclList) {
                                            if (CollectionUtils.isNotEmpty(oldSjclList)) {
                                                for (DchyXmglSjcl oldSjcl : oldSjclList) {
                                                    if (StringUtils.equals(oldSjcl.getClmc(), xmglSjcl.getClmc())) {
                                                        xmglSjcl.setWjzxid(oldSjcl.getWjzxid());
                                                        entityMapperXSBF.saveOrUpdate(xmglSjcl, xmglSjcl.getSjclid());
                                                        logger.info("************多测合一测绘收件材料入库成功");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                                    if (StringUtils.isNoneBlank(dchyXmglSjxx.getSjxxid())) {
                                        entityMapperXSBF.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());
                                        logger.info("************多测合一收件信息入库成功");
                                    }
                                }
                            }

                            if(CollectionUtils.isNotEmpty(dchyXmglClsxChtlList)){
                                for(DchyXmglClsxChtl dchyXmglClsxChtl:dchyXmglClsxChtlList){
                                    if (StringUtils.isNoneBlank(dchyXmglClsxChtl.getChtlid())) {
                                        entityMapperXSBF.saveOrUpdate(dchyXmglClsxChtl, dchyXmglClsxChtl.getChtlid());
                                        logger.info("************多测合一测绘事项测绘体量入库成功");
                                    }
                                }
                            }

                            if (CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                                for (DchyXmglHtxxDto dchyXmglHtxxDto : dchyXmglHtxxDtoList) {
                                    List<DchyXmglHtxx> dchyXmglHtxxList = dchyXmglHtxxDto.getDchyXmglHtxxList();
                                    List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList = dchyXmglHtxxDto.getDchyXmglClsxChdwxxGxList();
                                    List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList = dchyXmglHtxxDto.getDchyXmglHtxxChdwxxGxList();
                                    List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = dchyXmglHtxxDto.getDchyXmglClsxHtxxGxList();
                                    List<DchyXmglSjcl> dchyXmglHtxxSjcl = dchyXmglHtxxDto.getDchyXmglSjclList();
                                    List<DchyXmglSjxx> dchyXmglHtxxSjxx = dchyXmglHtxxDto.getDchyXmglSjxxList();
                                    DchyXmglHtxx dchyXmglHtxx = dchyXmglHtxxDto.getDchyXmglHtxx();

                                    /*合同文件*/
                                    List<Map<String, List>> htFileList = dchyXmglHtxxDto.getHtFileList();
                                    if (dchyXmglHtxx != null) {
                                        //合同信息入库
                                        entityMapperXSBF.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                                        //收件材料入库
                                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxSjcl)) {
                                            for (DchyXmglSjcl dchyXmglHtxxSjcls : dchyXmglHtxxSjcl) {
                                                if (CollectionUtils.isNotEmpty(htFileList)) {
                                                    for (Map<String, List> htFile : htFileList) {
                                                        /*合同文件上传*/
                                                        Map<String, Object> map = this.uploadWjcl(htFile.get(dchyXmglHtxxSjcls.getSjclid()), dchyXmglHtxx.getHtxxid(), dchyXmglHtxxSjcls.getClmc());
                                                        String wjzxid = (String) map.get("folderId");
                                                        if (dchyXmglHtxxSjcl != null) {
                                                            String sjclid = null;
                                                            DchyXmglSjcl dchyXmglSjcl = entityMapperXSBF.selectByPrimaryKey(DchyXmglSjcl.class, dchyXmglHtxxSjcls.getSjclid());
                                                            if (dchyXmglSjcl != null) {
                                                                sjclid = dchyXmglSjcl.getSjclid();
                                                            } else {
                                                                sjclid = dchyXmglHtxxSjcls.getSjclid();
                                                            }
                                                            /*更新收件材料表*/
                                                            if (StringUtils.isNoneBlank(sjclid)) {
                                                                dchyXmglHtxxSjcls.setWjzxid(wjzxid);
                                                                entityMapperXSBF.saveOrUpdate(dchyXmglHtxxSjcls, sjclid);
                                                                logger.info("************多测合一测绘收件材料入库成功");
                                                                dchyXmglHtxx.setWjzxid(wjzxid);
                                                                entityMapperXSBF.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                                                                logger.info("************更新合同的wjzxid");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxSjxx)) {
                                            for (DchyXmglSjxx dchyXmglHtxxSjxxs : dchyXmglHtxxSjxx) {
                                                if (StringUtils.isNoneBlank(dchyXmglHtxxSjxxs.getSjxxid())) {
                                                    entityMapperXSBF.saveOrUpdate(dchyXmglHtxxSjxxs, dchyXmglHtxxSjxxs.getSjxxid());
                                                    logger.info("************多测合一合同收件信息入库成功" + dchyXmglHtxxSjxxs.getSjxxid());
                                                }
                                            }
                                        }
                                    }

                                    if (CollectionUtils.isNotEmpty(dchyXmglHtxxList)) {
                                        for (DchyXmglHtxx newDchyXmglHtxx : dchyXmglHtxxList) {
                                            if (CollectionUtils.isNotEmpty(dchyXmglHtxxSjcl)) {
                                                for (DchyXmglSjcl dchyXmglHtxxSjcls : dchyXmglHtxxSjcl) {
                                                    if (StringUtils.isNoneBlank(dchyXmglHtxxSjcls.getSjclid())) {
                                                        Example sjclExample = new Example(DchyXmglSjcl.class);
                                                        sjclExample.createCriteria().andEqualTo("sjclid", dchyXmglHtxxSjcls.getSjclid());
                                                        List<DchyXmglSjcl> sjlcList = entityMapperXSBF.selectByExampleNotNull(sjclExample);
                                                        if (CollectionUtils.isNotEmpty(sjlcList)) {
                                                            for (DchyXmglSjcl sjlcLists : sjlcList) {
                                                                String wjzxidxsbf = sjlcLists.getWjzxid();
                                                                dchyXmglHtxxSjcls.setWjzxid(wjzxidxsbf);
                                                            }
                                                        }
                                                        entityMapperXSBF.saveOrUpdate(dchyXmglHtxxSjcls, dchyXmglHtxxSjcls.getSjclid());
                                                        logger.info("************多测合一合同收件材料入库成功" + dchyXmglHtxxSjcls.getSjclid());
                                                    }
                                                }
                                            }

                                            if (CollectionUtils.isNotEmpty(dchyXmglHtxxSjxx)) {
                                                for (DchyXmglSjxx dchyXmglHtxxSjxxs : dchyXmglHtxxSjxx) {
                                                    if (StringUtils.isNoneBlank(dchyXmglHtxxSjxxs.getSjxxid())) {
                                                        entityMapperXSBF.saveOrUpdate(dchyXmglHtxxSjxxs, dchyXmglHtxxSjxxs.getSjxxid());
                                                        logger.info("************多测合一合同收件信息入库成功" + dchyXmglHtxxSjxxs.getSjxxid());
                                                    }
                                                }
                                            }

                                            Example htxxExample = new Example(DchyXmglHtxx.class);
                                            htxxExample.createCriteria().andEqualTo("htxxid", newDchyXmglHtxx.getHtxxid());
                                            List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExampleNotNull(htxxExample);
                                            if (CollectionUtils.isNotEmpty(htxxList)) {
                                                for (DchyXmglHtxx htxxLists : htxxList) {
                                                    String wjzxidxsbf = htxxLists.getWjzxid();
                                                    newDchyXmglHtxx.setWjzxid(wjzxidxsbf);
                                                }
                                            }
                                            entityMapperXSBF.saveOrUpdate(newDchyXmglHtxx, newDchyXmglHtxx.getHtxxid());
                                            logger.info("************多测合一合同信息入库成功" + newDchyXmglHtxx.getHtxxid());
                                        }
                                    }

                                    if (CollectionUtils.isNotEmpty(dchyXmglClsxChdwxxGxList)) {
                                        for (DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx : dchyXmglClsxChdwxxGxList) {
                                            if (StringUtils.isNoneBlank(dchyXmglClsxChdwxxGx.getGxid())) {
                                                entityMapperXSBF.saveOrUpdate(dchyXmglClsxChdwxxGx, dchyXmglClsxChdwxxGx.getGxid());
                                                logger.info("************多测合一测量事项与测绘单位关系表入库成功" + dchyXmglClsxChdwxxGx.getGxid());
                                            }
                                        }
                                    }

                                    if (CollectionUtils.isNotEmpty(dchyXmglHtxxChdwxxGxList)) {
                                        for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : dchyXmglHtxxChdwxxGxList) {
                                            if (StringUtils.isNoneBlank(dchyXmglHtxxChdwxxGx.getGxid())) {
                                                entityMapperXSBF.saveOrUpdate(dchyXmglHtxxChdwxxGx, dchyXmglHtxxChdwxxGx.getGxid());
                                                logger.info("************多测合一合同信息与测绘单位关系表入库成功" + dchyXmglHtxxChdwxxGx.getGxid());
                                            }
                                        }
                                    }

                                    if (CollectionUtils.isNotEmpty(dchyXmglClsxHtxxGxList)) {
                                        for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : dchyXmglClsxHtxxGxList) {
                                            if (StringUtils.isNoneBlank(dchyXmglClsxHtxxGx.getGxid())) {
                                                entityMapperXSBF.saveOrUpdate(dchyXmglClsxHtxxGx, dchyXmglClsxHtxxGx.getGxid());
                                                logger.info("************多测合一测量事项与合同信息关系表入库成功" + dchyXmglClsxHtxxGx.getGxid());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (dchyXmglChxm == null && CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                            for (DchyXmglHtxxDto dchyXmglHtxxDto1 : dchyXmglHtxxDtoList) {
                                DchyXmglHtxx dchyXmglHtxx = dchyXmglHtxxDto1.getDchyXmglHtxx();
                                DchyXmglSjcl dchyXmglSjcl = dchyXmglHtxxDto1.getDchyXmglSjcl();
                                DchyXmglSjxx dchyXmglSjxx1 = dchyXmglHtxxDto1.getDchyXmglSjxx();

                                if (dchyXmglHtxx != null) {
                                    if (dchyXmglSjcl != null && StringUtils.isNotBlank(dchyXmglSjcl.getSjclid())) {
                                        String wjzxid = "";
                                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxDto1.getSjclList())) {
                                            String clmc = dchyXmglSjcl.getClmc();
                                            Map<String, Object> objectMap = uploadWjcl(dchyXmglHtxxDto1.getSjclList(), dchyXmglHtxx.getHtxxid(), clmc);
                                            parentId = MapUtils.getString(objectMap, "parentId");
                                            wjzxid = MapUtils.getString(objectMap, "folderId");
                                        }
                                        dchyXmglSjcl.setWjzxid(parentId);
                                        dchyXmglHtxx.setWjzxid(wjzxid);
                                        entityMapperXSBF.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                                        logger.info("************多测合一合同收件材料入库成功");
                                    }

                                    if (null != dchyXmglSjxx1 && StringUtils.isNoneBlank(dchyXmglSjxx1.getSjxxid())) {
                                        entityMapperXSBF.saveOrUpdate(dchyXmglSjxx1, dchyXmglSjxx1.getSjxxid());
                                        logger.info("************多测合一合同收件信息入库成功");
                                    }

                                    entityMapperXSBF.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                                    logger.info("************多测合一合同信息入库成功");
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglYbrwList)) {
                            for (DchyXmglYbrw dchyXmglYbrw : dchyXmglYbrwList) {
                                if (StringUtils.isNoneBlank(dchyXmglYbrw.getYbrwid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                                    logger.info("************多测合一已办任务消息入库成功");
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                            for (DchyXmglSqxx dchyXmglSqxx : dchyXmglSqxxList) {
                                if (StringUtils.isNoneBlank(dchyXmglSqxx.getSqxxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                                    logger.info("************多测合一申请信息入库成功");
                                }
                            }
                        }
                    }
                    //删除的数据
                    if (MapUtils.isNotEmpty(idMap)) {
                        if (idMap.containsKey("sjclid")) {
                            String sjclid = MapUtils.getString(idMap, "sjclid");
                            if (StringUtils.isNoneBlank(sjclid)) {
                                delFile(sjclid);
                            }
                        }

                        if (idMap.containsKey("htxxid")) {
                            String htxxid = MapUtils.getString(idMap, "htxxid");

                            if (StringUtils.isNoneBlank(htxxid)) {
                                delHtxxFile(htxxid);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("数据库入库失败:", e);
            dchyXmglMqCzrz.setSbyy(e.toString());
            entityMapperXSBF.saveOrUpdate(dchyXmglMqCzrz, dchyXmglMqCzrz.getCzrzid());
            throw new Exception();
        }
    }

    private Map<String, Object> uploadWjcl(List<Map<String, String>> wjList, String mainId, String clmc) throws IOException {
        Map<String, Object> objectMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(wjList)) {
            MultipartFile[] files = new MultipartFile[wjList.size()];
            for (Map<String, String> map : wjList) {
                // 文件名--文件内容
                int i = 0;
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    if (StringUtils.isNotEmpty(base64) && base64 != "") {
                        byte[] bytes = Base64.getDecoder().decode(base64);
                        InputStream inputStream = new ByteArrayInputStream(bytes);
                        MockMultipartFile file = new MockMultipartFile(key, key, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                        files[i] = file;
                        i++;
                    }
                }
            }
            // 文件,父的name,收件材料id
            objectMap.putAll(FileUploadUtil.uploadFile(files, mainId, clmc));
        }
        return objectMap;
    }

    private void delFile(String sjclid) {
        DchyXmglSjcl dchyXmglSjcl = entityMapperXSBF.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
        String wjzxid = dchyXmglSjcl.getWjzxid();
        if (StringUtils.isNotBlank(wjzxid)) {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxid));
        }
        if (StringUtils.isNotBlank(sjclid)) {
            //删除收件材料信息
            int sjclResult = entityMapperXSBF.deleteByPrimaryKey(DchyXmglSjcl.class, sjclid);
            if (sjclResult > 0) {
                logger.info("************多测合一收件材料删除成功");
            }

            // 删除收件信息
            Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
            dchyXmglSjxx.createCriteria().andEqualTo("glsxid", sjclid);
            int sjxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglSjxx);
            if (sjxxResult > 0) {
                logger.info("************多测合一收件信息删除成功");
            }
        }
    }

    private void delHtxxFile(String htxxid) {
        DchyXmglHtxx dchyXmglHtxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
        String wjzxid = dchyXmglHtxx.getWjzxid();
        if (StringUtils.isNotBlank(wjzxid)) {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxid));
        }
        if (StringUtils.isNotBlank(htxxid)) {
            //删除合同信息
            int htxxResult = entityMapperXSBF.deleteByPrimaryKey(DchyXmglHtxx.class, htxxid);
            if (htxxResult > 0) {
                logger.info("************多测合一合同信息删除成功");
            }

            // 删除合同信息测量事项关系
            Example dchyXmglClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
            dchyXmglClsxHtxxGx.createCriteria().andEqualTo("chxmid", htxxid);
            int htxxClsxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglClsxHtxxGx);
            if (htxxClsxResult > 0) {
                logger.info("************多测合一合同测量事项删除成功");
            }

            // 删除合同信息测量单位关系
            Example dchyXmglHtxxChdwxxGx = new Example(DchyXmglHtxxChdwxxGx.class);
            dchyXmglHtxxChdwxxGx.createCriteria().andEqualTo("chxmid", htxxid);
            int htxxChdwxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglHtxxChdwxxGx);
            if (htxxChdwxxResult > 0) {
                logger.info("************多测合一合同测绘单位信息删除成功");
            }

            List<Map<String, Object>> chdwxxidList = entityMapperXSBF.selectByExampleNotNull(dchyXmglHtxxChdwxxGx);

            if (CollectionUtils.isNotEmpty(chdwxxidList)) {
                for (Map<String, Object> chdwxxidLists : chdwxxidList) {
                    String chdwxxid = MapUtils.getString(chdwxxidLists, "CHDWXXID");
                    Example dchyXmglClsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
                    dchyXmglClsxChdwxxGx.createCriteria().andEqualTo("chdwxxid", chdwxxid);
                    int clsxChdwxxGxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglClsxChdwxxGx);
                    if (clsxChdwxxGxResult > 0) {
                        logger.info("************多测合一合同测量事项与测绘单位关系表删除成功");
                    }
                }
            }

            // 删除收件信息
            Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
            dchyXmglSjxx.createCriteria().andEqualTo("glsxid", htxxid);
            int sjxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglSjxx);
            if (sjxxResult > 0) {
                logger.info("************多测合一收件信息删除成功");
            }
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
            logger.info("************多测合一成果抽查材料入库成功");
            //2,保存数据到数据库
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
                logger.info("************多测合一成果抽查收件材料入库成功");
            }
            if (flag2 > 0) {
                logger.info("************多测合一成果抽查收件信息入库成功");
            }
        } catch (IOException e) {
            logger.error("错误原因:{}", e);
        }
    }

}