package cn.gtmap.msurveyplat.serviceol.core.service.mq.receive;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
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
 * @description ????????????????????????????????????????????????
 * @time 2020/11/27 15:05
 */
@Service
public class ReceiveSlxxFromXmglServiceImpl implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlatformUtil platformUtil;

    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;

    @Override
    @RabbitListener(queues = Constants.XMGL_BSDT_SLXX_QUEUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public void onMessage(Message message, Channel channel) {
        try {
            logger.info("**********????????????????????????--??????????????????**********");
            logger.info(Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_SLXX_QUEUE +
                    "?????????????????????:" + CalendarUtil.getCurHMSStrDate());
            byte[] bytes = message.getBody();
            String str = new String(bytes, Charsets.UTF_8);
            saveOrDeleteXxSlxx(str);
            basicACK(message, channel); //????????????????????????mq?????????????????????mq
        } catch (Exception e) {
            basicReject(message, channel);

            logger.info("**********????????????????????????--??????????????????**********" +
                    Constants.XMGL_BSDT_DIRECT_EXCHANGE + "-" + Constants.XMGL_BSDT_SLXX_QUEUE +
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


    //?????????????????????????????????
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrDeleteXxSlxx(String str) throws Exception {
        String parentId = "";

        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        try {
            dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
            dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
            dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
            dchyXmglMqCzrz.setDldm(Constants.XMGL_BSDT_SLXX_QUEUE);
            dchyXmglMqCzrz.setDlmc(Constants.XMGL_BSDT_SLXX_QUEUE_MC);
            JSONObject slxxJson = JSONObject.parseObject(str);
            DchyXmglChxmDto dchyXmglChxmDto = JSON.toJavaObject(slxxJson, DchyXmglChxmDto.class);

            Map<String, String> idMap = dchyXmglChxmDto.getIdMaps();

            logger.info("*************************************??????*****************************************");

            //???????????????
            if (dchyXmglChxmDto != null) {
                MultipartFile file;
                DchyXmglChgc dchyXmglChgc = dchyXmglChxmDto.getDchyXmglChgc();
                DchyXmglChxm dchyXmglChxm = dchyXmglChxmDto.getDchyXmglChxm();
                List<DchyXmglChdw> dchyXmglChdwList = dchyXmglChxmDto.getDchyXmglChdwList();
                List<DchyXmglSjxx> dchyXmglSjxxList = dchyXmglChxmDto.getDchyXmglSjxxList();
                List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = dchyXmglChxmDto.getDchyXmglChxmChdwxxList();
                List<DchyXmglChxmClsx> dchyXmglChxmClsxList = dchyXmglChxmDto.getDchyXmglChxmClsxList();
                List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = dchyXmglChxmDto.getDchyXmglHtxxDtoList();
                List<DchyXmglSjcl> dchyXmglSjclList = dchyXmglChxmDto.getDchyXmglSjclList();
                Map<String, List<Map<String, String>>> stringListMap = dchyXmglChxmDto.getSjcl();
                List<DchyXmglYbrw> dchyXmglYbrwList = dchyXmglChxmDto.getDchyXmglYbrwList();
                List<DchyXmglSqxx> dchyXmglSqxxList = dchyXmglChxmDto.getDchyXmglSqxxList();
                DchyXmglChxm failDchyXmglChxm = dchyXmglChxmDto.getDchyXmglChxm();

                if (failDchyXmglChxm != null && StringUtils.isNoneBlank(failDchyXmglChxm.getChxmid())) {
                    int falg = entityMapperXSBF.saveOrUpdate(failDchyXmglChxm, failDchyXmglChxm.getChxmid());
                    if (falg > 0) {
                        logger.info("************???????????????????????????????????????????????????????????????");
                    } else {
                        logger.info("************???????????????????????????????????????????????????????????????");
                    }
                }

                if (dchyXmglChgc != null && StringUtils.isNoneBlank(dchyXmglChgc.getChgcid())) {
                    entityMapperXSBF.saveOrUpdate(dchyXmglChgc, dchyXmglChgc.getChgcid());
                    logger.info("************????????????????????????????????????");
                }

                if (CollectionUtils.isNotEmpty(dchyXmglChdwList)) {
                    for (DchyXmglChdw dchyXmglChdw : dchyXmglChdwList) {
                        if (StringUtils.isNoneBlank(dchyXmglChdw.getChdwid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglChdw, dchyXmglChdw.getChdwid());
                            logger.info("************????????????????????????????????????");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                    for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                        if (StringUtils.isNoneBlank(dchyXmglChxmChdwxx.getChdwxxid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglChxmChdwxx, dchyXmglChxmChdwxx.getChdwxxid());
                            logger.info("************??????????????????????????????????????????????????????");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                    for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                        if (dchyXmglChxmClsx != null && StringUtils.isNoneBlank(dchyXmglChxmClsx.getClsxid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglChxmClsx, dchyXmglChxmClsx.getClsxid());
                            logger.info("************??????????????????????????????????????????????????????");
                        }

                    }
                }

                if (dchyXmglChxm != null && CollectionUtils.isNotEmpty(dchyXmglHtxxDtoList)) {
                    for (DchyXmglHtxxDto dchyXmglHtxxDto1 : dchyXmglHtxxDtoList) {
                        List<DchyXmglHtxx> dchyXmglHtxxList = dchyXmglHtxxDto1.getDchyXmglHtxxList();
                        List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList = dchyXmglHtxxDto1.getDchyXmglClsxChdwxxGxList();
                        List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList = dchyXmglHtxxDto1.getDchyXmglHtxxChdwxxGxList();
                        List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = dchyXmglHtxxDto1.getDchyXmglClsxHtxxGxList();
                        List<DchyXmglSjcl> dchyXmglHtxxSjcl = dchyXmglHtxxDto1.getDchyXmglSjclList();
                        List<DchyXmglSjxx> dchyXmglHtxxSjxx = dchyXmglHtxxDto1.getDchyXmglSjxxList();

                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxList)) {
                            for (DchyXmglHtxx dchyXmglHtxxLists : dchyXmglHtxxList) {
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
                                            logger.info("************??????????????????????????????????????????" + dchyXmglHtxxSjcls.getSjclid());
                                        }
                                    }
                                }

                                if (CollectionUtils.isNotEmpty(dchyXmglHtxxSjxx)) {
                                    for (DchyXmglSjxx dchyXmglHtxxSjxxs : dchyXmglHtxxSjxx) {
                                        if (StringUtils.isNoneBlank(dchyXmglHtxxSjxxs.getSjxxid())) {
                                            entityMapperXSBF.saveOrUpdate(dchyXmglHtxxSjxxs, dchyXmglHtxxSjxxs.getSjxxid());
                                            logger.info("************??????????????????????????????????????????" + dchyXmglHtxxSjxxs.getSjxxid());
                                        }
                                    }
                                }


                                Example htxxExample = new Example(DchyXmglHtxx.class);
                                htxxExample.createCriteria().andEqualTo("htxxid", dchyXmglHtxxLists.getHtxxid());
                                List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExampleNotNull(htxxExample);
                                if (CollectionUtils.isNotEmpty(htxxList)) {
                                    for (DchyXmglHtxx htxxLists : htxxList) {
                                        String wjzxidxsbf = htxxLists.getWjzxid();
                                        dchyXmglHtxxLists.setWjzxid(wjzxidxsbf);
                                    }
                                }
                                entityMapperXSBF.saveOrUpdate(dchyXmglHtxxLists, dchyXmglHtxxLists.getHtxxid());
                                logger.info("************????????????????????????????????????" + dchyXmglHtxxLists.getHtxxid());

                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglClsxChdwxxGxList)) {
                            for (DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx : dchyXmglClsxChdwxxGxList) {
                                if (StringUtils.isNoneBlank(dchyXmglClsxChdwxxGx.getGxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglClsxChdwxxGx, dchyXmglClsxChdwxxGx.getGxid());
                                    logger.info("************????????????????????????????????????????????????????????????" + dchyXmglClsxChdwxxGx.getGxid());
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglHtxxChdwxxGxList)) {
                            for (DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx : dchyXmglHtxxChdwxxGxList) {
                                if (StringUtils.isNoneBlank(dchyXmglHtxxChdwxxGx.getGxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglHtxxChdwxxGx, dchyXmglHtxxChdwxxGx.getGxid());
                                    logger.info("************????????????????????????????????????????????????????????????" + dchyXmglHtxxChdwxxGx.getGxid());
                                }
                            }
                        }

                        if (CollectionUtils.isNotEmpty(dchyXmglClsxHtxxGxList)) {
                            for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : dchyXmglClsxHtxxGxList) {
                                if (StringUtils.isNoneBlank(dchyXmglClsxHtxxGx.getGxid())) {
                                    entityMapperXSBF.saveOrUpdate(dchyXmglClsxHtxxGx, dchyXmglClsxHtxxGx.getGxid());
                                    logger.info("************????????????????????????????????????????????????????????????" + dchyXmglClsxHtxxGx.getGxid());
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
                                logger.info("************??????????????????????????????????????????");
                            }

                            if (null != dchyXmglSjxx1 && StringUtils.isNoneBlank(dchyXmglSjxx1.getSjxxid())) {
                                entityMapperXSBF.saveOrUpdate(dchyXmglSjxx1, dchyXmglSjxx1.getSjxxid());
                                logger.info("************??????????????????????????????????????????");
                            }

                            entityMapperXSBF.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                            logger.info("************????????????????????????????????????");
                        }
                    }
                }


                if (dchyXmglChxm != null) {
                    if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                        Map<String, String> getWjzxidBySjclid = Maps.newHashMap();
                        Map<String, String> getClmcBySjclid = Maps.newHashMap();
                        if (MapUtils.isNotEmpty(stringListMap)) {
                            if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                                for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                                    String clmc = dchyXmglSjcl.getClmc();
                                    if (StringUtils.isBlank(clmc)) {
                                        clmc = UUIDGenerator.generate18();
                                        dchyXmglSjcl.setClmc(clmc);
                                    }
                                    getClmcBySjclid.put(dchyXmglSjcl.getSjclid(), clmc);
                                }
                            }
                            for (Map.Entry<String, List<Map<String, String>>> entry : stringListMap.entrySet()) {
                                // ????????????id - ????????????
                                List<Map<String, String>> wjList = entry.getValue();
                                String clmc = getClmcBySjclid.get(entry.getKey());
                                Map<String, Object> objectMap = uploadWjcl(wjList, dchyXmglChxm.getChxmid(), clmc);
                                parentId = MapUtils.getString(objectMap, "parentId");
                                getWjzxidBySjclid.put(entry.getKey(), MapUtils.getString(objectMap, "folderId"));
                            }
                        }
                        for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                            if (StringUtils.isNoneBlank(dchyXmglSjcl.getSjclid())) {
                                dchyXmglSjcl.setWjzxid(getWjzxidBySjclid.get(dchyXmglSjcl.getSjclid()));
                                entityMapperXSBF.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
                                logger.info("************???????????????????????????????????????????????????");
                            }
                        }
                    }
                    if (StringUtils.isNoneBlank(dchyXmglChxm.getChxmid())) {
                        dchyXmglChxm.setWjzxid(parentId);
                        int i = entityMapperXSBF.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());
                        if (i > 0) {
                            logger.info("************????????????????????????????????????");
                            logger.info("************????????????ID???" + dchyXmglChxm.getChxmid());
                            logger.info("************???????????????" + dchyXmglChxm.getSlbh());
                        }
                    }

                    if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                        for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                            if (StringUtils.isNoneBlank(dchyXmglSjxx.getSjxxid())) {
                                entityMapperXSBF.saveOrUpdate(dchyXmglSjxx, dchyXmglSjxx.getSjxxid());
                                logger.info("************????????????????????????????????????");
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglYbrwList)) {
                    for (DchyXmglYbrw dchyXmglYbrw : dchyXmglYbrwList) {
                        if (StringUtils.isNoneBlank(dchyXmglYbrw.getYbrwid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                            logger.info("************??????????????????????????????????????????");
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                    for (DchyXmglSqxx dchyXmglSqxx : dchyXmglSqxxList) {
                        if (StringUtils.isNoneBlank(dchyXmglSqxx.getSqxxid())) {
                            entityMapperXSBF.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                            logger.info("************????????????????????????????????????");
                        }
                    }
                }
            }
            //???????????????
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
        } catch (Exception e) {
            logger.error("?????????????????????:", e);
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
                // ?????????--????????????
                int i = 0;
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    MockMultipartFile file = new MockMultipartFile(key, key, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                    files[i] = file;
                    i++;
                }
            }
            // ??????,??????name,????????????id
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
            //????????????????????????
            int sjclResult = entityMapperXSBF.deleteByPrimaryKey(DchyXmglSjcl.class, sjclid);
            if (sjclResult > 0) {
                logger.info("************????????????????????????????????????");
            }

            // ??????????????????
            Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
            dchyXmglSjxx.createCriteria().andEqualTo("glsxid", sjclid);
            int sjxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglSjxx);
            if (sjxxResult > 0) {
                logger.info("************????????????????????????????????????");
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
            //??????????????????
            int htxxResult = entityMapperXSBF.deleteByPrimaryKey(DchyXmglHtxx.class, htxxid);
            if (htxxResult > 0) {
                logger.info("************????????????????????????????????????");
            }

            // ????????????????????????????????????
            Example dchyXmglClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
            dchyXmglClsxHtxxGx.createCriteria().andEqualTo("chxmid", htxxid);
            int htxxClsxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglClsxHtxxGx);
            if (htxxClsxResult > 0) {
                logger.info("************??????????????????????????????????????????");
            }

            // ????????????????????????????????????
            Example dchyXmglHtxxChdwxxGx = new Example(DchyXmglHtxxChdwxxGx.class);
            dchyXmglHtxxChdwxxGx.createCriteria().andEqualTo("chxmid", htxxid);
            int htxxChdwxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglHtxxChdwxxGx);
            if (htxxChdwxxResult > 0) {
                logger.info("************????????????????????????????????????????????????");
            }

            List<Map<String, Object>> chdwxxidList = entityMapperXSBF.selectByExampleNotNull(dchyXmglHtxxChdwxxGx);

            if (CollectionUtils.isNotEmpty(chdwxxidList)) {
                for (Map<String, Object> chdwxxidLists : chdwxxidList) {
                    String chdwxxid = MapUtils.getString(chdwxxidLists, "CHDWXXID");
                    Example dchyXmglClsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
                    dchyXmglClsxChdwxxGx.createCriteria().andEqualTo("chdwxxid", chdwxxid);
                    int clsxChdwxxGxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglClsxChdwxxGx);
                    if (clsxChdwxxGxResult > 0) {
                        logger.info("************??????????????????????????????????????????????????????????????????");
                    }
                }
            }

            // ??????????????????
            Example dchyXmglSjxx = new Example(DchyXmglSjxx.class);
            dchyXmglSjxx.createCriteria().andEqualTo("glsxid", htxxid);
            int sjxxResult = entityMapperXSBF.deleteByExampleNotNull(dchyXmglSjxx);
            if (sjxxResult > 0) {
                logger.info("************????????????????????????????????????");
            }
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
