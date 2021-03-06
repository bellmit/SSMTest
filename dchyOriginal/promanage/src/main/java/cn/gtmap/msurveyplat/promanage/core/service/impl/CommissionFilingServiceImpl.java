package cn.gtmap.msurveyplat.promanage.core.service.impl;


import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.*;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglHtxxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglJsdwMapper;
import cn.gtmap.msurveyplat.promanage.core.service.CommissionFilingService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSjclService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.MessagePushService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.ConstructionCodeUtil;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileUploadUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.XmbhFormatUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
@Service
public class CommissionFilingServiceImpl implements CommissionFilingService {

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    DchyXmglChxmMapper dchyXmglChxmMapper;

    @Autowired
    DchyXmglZdService dchyXmglZdService;

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    Repository repository;

    @Autowired
    MessagePushService messagePushService;

    @Autowired
    DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper;

    @Autowired
    DchyXmglJsdwMapper dchyXmglJsdwMapper;

    @Autowired
    DchyXmglHtxxMapper dchyXmglHtxxMapper;

    private Logger logger = LoggerFactory.getLogger(CommissionFilingServiceImpl.class);

    @Override
    @Transactional
    public boolean reviewCommission(Map<String, Object> map) {
        logger.info("****************************?????????????????????????????????????????????********************************");
        //??????????????????
        DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
        String chxmid = CommonUtil.formatEmptyValue(map.get("chxmid"));
        String sftg = CommonUtil.formatEmptyValue(map.get("sftg"));
        String gcmc = CommonUtil.formatEmptyValue(map.get("gcmc"));
        String wtdw = CommonUtil.formatEmptyValue(map.get("wtdw"));
        String shyj = CommonUtil.formatEmptyValue(map.get("shyj"));
        try {
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if(dchyXmglChxm != null){
                dchyXmglChxm.setBasj(CalendarUtil.getCurHMSDate());
                if(StringUtils.isEmpty(dchyXmglChxm.getBar())){
                    dchyXmglChxm.setBar(UserUtil.getCurrentUserId());
                }
                if(StringUtils.isEmpty(dchyXmglChxm.getBarmc())){
                    dchyXmglChxm.setBarmc(UserUtil.getUserNameById(UserUtil.getCurrentUserId()));
                }
                dchyXmglChxm.setChxmid(chxmid);
                dchyXmglChxm.setXmzt(Constants.DCHY_XMGL_CHXM_XMZT_YSL);
                //??????????????????
                String wtzt = dchyXmglChxm.getWtzt();
                if (StringUtils.equals(sftg, Constants.SHTG)) {
                    //??????????????????
                    if (StringUtils.isNotBlank(wtzt)) {
                        dchyXmglChxm.setWtzt(Constants.WTZT_YBA); //?????????????????????
                    }
                } else if (StringUtils.equals(sftg, Constants.SHBTG) && StringUtils.isNotBlank(wtzt)) {
                    //?????????????????????
                    dchyXmglChxm.setWtzt(Constants.WTZT_YTH); //?????????????????????
                    dchyXmglChxm.setXmzt(Constants.DCHY_XMGL_CHXM_XMZT_WSL);
                }
                logger.info("???????????????" + wtzt + "**********************************");
                int result = entityMapper.saveOrUpdate(dchyXmglChxm, dchyXmglChxm.getChxmid());
                if (result > 0) {
                    //??????????????????
                    List<DchyXmglChxmDto> dchyXmglChxmDtoList = this.generateChxmDto(chxmid, wtzt, sftg, shyj, wtdw);
                    DchyXmglChxmListDto dchyXmglChxmListDto = new DchyXmglChxmListDto();
                    if (CollectionUtils.isNotEmpty(dchyXmglChxmDtoList)) {
                        dchyXmglChxmListDto.setDchyXmglChxmListDto(dchyXmglChxmDtoList);
                    }

                    //????????????
                    List<Map<String, Object>> chdwxxList = (List<Map<String, Object>>) map.get("chdwxx");
                    List<DchyXmglYhxx> yhxxList = this.generateYhxx(gcmc, wtdw, chxmid, sftg, chdwxxList);
                    //??????????????????????????????
                    dchyXmglXxtxDto.setDchyXmglYhxxList(yhxxList);

                    Map<String, Object> resultMap = new HashMap<>();
                    Map<String, Object> resultMapXxtx = new HashMap<>();
                    resultMap.put("saveOrUpdate", dchyXmglChxmDtoList);
                    resultMapXxtx.put("saveOrUpdate", dchyXmglXxtxDto);
                    pushDataToMqService.pushBaxxMsgToMq(dchyXmglChxmListDto);
                    pushDataToMqService.pushXxtxResultTo(dchyXmglXxtxDto);

                    logger.info("*********************************??????????????????????????????????????????***********************************");

                }
                //??????????????????????????????
                if (StringUtils.isNotEmpty(chxmid)) {
                    entityMapper.deleteByPrimaryKey(DchyXmglChxm.class, chxmid);
                    logger.info("------------????????????????????????id:" + chxmid);
                }
            }

            return true;
        } catch (Exception e) {
            DchyXmglChxmListDto failDchyXmglChxmListDto = new DchyXmglChxmListDto();
            List<DchyXmglChxmDto> failDchyXmglChxmDtoList = Lists.newArrayList();
            DchyXmglChxmDto failDchyXmglChxmDto = new DchyXmglChxmDto();
            DchyXmglXxtxDto failDchyXmglXxtxDto = new DchyXmglXxtxDto();
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            dchyXmglChxm.setWtzt(Constants.WTZT_CXBA);
            failDchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);
            failDchyXmglChxmDtoList.add(failDchyXmglChxmDto);
            failDchyXmglChxmListDto.setDchyXmglChxmListDto(failDchyXmglChxmDtoList);

            //????????????
            List<Map<String, Object>> chdwxxList = (List<Map<String, Object>>) map.get("chdwxx");
            List<DchyXmglYhxx> yhxxList = this.generateYhxx(gcmc, wtdw, chxmid, sftg, chdwxxList);
            //??????????????????????????????
            failDchyXmglXxtxDto.setDchyXmglYhxxList(yhxxList);

            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> failResultMap = new HashMap<>();
            resultMap.put("saveOrUpdate", failDchyXmglChxmDtoList);
            failResultMap.put("saveOrUpdate", failDchyXmglXxtxDto);
            pushDataToMqService.pushBaxxMsgToMq(failDchyXmglChxmListDto);
            pushDataToMqService.pushXxtxResultTo(failDchyXmglXxtxDto);
            logger.error("????????????{}", e);
            return false;
        }
    }

    public List<DchyXmglChxmDto> generateChxmDto(String chxmid, String wtzt, String sftg, String shyj, String wtdw) {
        List<DchyXmglChxmDto> dchyXmglChxmDtoList = Lists.newArrayList();
        //???????????????????????????????????????
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> clsxLists = dchyXmglChxmClsxMapper.queryClsxJdByChxmid(paramMap);

        if (null != clsxLists) {
            //??????????????????
            Set set = new HashSet();
            List newList = new ArrayList();
            for (Map<String, Object> clsxMap : clsxLists) {
                if (set.add(MapUtils.getString(clsxMap, Constants.DCHY_XMGL_CLSX_FDM))) {
                    newList.add(MapUtils.getString(clsxMap, Constants.DCHY_XMGL_CLSX_FDM));
                }
            }

            //????????????????????????
            if (CollectionUtils.isNotEmpty(newList)) {

                for (Iterator<String> it = newList.iterator(); it.hasNext(); ) {
                    List<DchyXmglChxmClsx> dchyXmglChxmClsxList = Lists.newArrayList();
                    List<DchyXmglClsxChtl> dchyXmglClsxChtlList = Lists.newArrayList();
                    List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = Lists.newArrayList();
                    List<DchyXmglSjxx> dchyXmglSjxxList = Lists.newArrayList();
                    List<DchyXmglSjcl> dchyXmglSjclList = Lists.newArrayList();
                    List<DchyXmglYbrw> dchyXmglYbrwList = Lists.newArrayList();
                    List<DchyXmglSqxx> dchyXmglSqxxList = Lists.newArrayList();
                    String cljd = it.next();
                    DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
                    //????????????????????????
                    // ???????????????????????????????????????????????????????????????
                    DchyXmglChxm oldDchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                    if (oldDchyXmglChxm != null) {
                        List<String> oldClsxLists = Lists.newArrayList();
                        String newChxmid = UUIDGenerator.generate18();
                        oldDchyXmglChxm.setChxmid(newChxmid);
                        oldDchyXmglChxm.setWtzt(wtzt);
                        //????????????
                        String babh = obtainBabh(chxmid, wtdw, cljd);
                        logger.info("----------------------------------------???????????????" + babh + "_______????????????ID???" + newChxmid);
                        if (StringUtils.isNotEmpty(babh)) {
                            oldDchyXmglChxm.setBabh(babh);
                        }
                        entityMapper.saveOrUpdate(oldDchyXmglChxm, oldDchyXmglChxm.getChxmid());
                        dchyXmglChxmDto.setDchyXmglChxm(oldDchyXmglChxm);

                        //????????????????????????????????????(???????????????????????????)
                        String newChdwxxid = null;
                        Example exampleChxmChdwxx = new Example(DchyXmglChxmChdwxx.class);
                        exampleChxmChdwxx.createCriteria().andEqualTo("chxmid", chxmid);
                        List<DchyXmglChxmChdwxx> oldDchyXmglChxmChdwxxList = entityMapper.selectByExampleNotNull(exampleChxmChdwxx);
                        if (CollectionUtils.isNotEmpty(oldDchyXmglChxmChdwxxList)) {
                            for (DchyXmglChxmChdwxx oldDchyXmglChxmChdwxx : oldDchyXmglChxmChdwxxList) {
                                newChdwxxid = UUIDGenerator.generate18();
                                oldDchyXmglChxmChdwxx.setChdwxxid(newChdwxxid);
                                oldDchyXmglChxmChdwxx.setChxmid(newChxmid);
                                entityMapper.saveOrUpdate(oldDchyXmglChxmChdwxx, oldDchyXmglChxmChdwxx.getChdwxxid());
                                dchyXmglChxmChdwxxList.add(oldDchyXmglChxmChdwxx);
                            }
                            dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);
                        }

                        //????????????????????????????????????
                        List<Map<String, List>> fileList = new ArrayList<>();
                        List oldClsxidList = Lists.newArrayList();

                        //????????????????????????????????????
                        for (Map<String, Object> clsxMap : clsxLists) {
                            if (StringUtils.equals(MapUtils.getString(clsxMap, Constants.DCHY_XMGL_CLSX_FDM), cljd)) {
                                String oldClsx = MapUtils.getString(clsxMap, Constants.DCHY_XMGL_CHXM_CLSX);
                                oldClsxLists.add(oldClsx);
                                Example exampleChxmClsx = new Example(DchyXmglChxmClsx.class);
                                exampleChxmClsx.createCriteria().andEqualTo("chxmid", chxmid).andEqualTo("clsx", oldClsx);
                                List<DchyXmglChxmClsx> oldDchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(exampleChxmClsx);
                                if (CollectionUtils.isNotEmpty(oldDchyXmglChxmClsxList)) {
                                    for (DchyXmglChxmClsx oldDchyXmglChxmClsx : oldDchyXmglChxmClsxList) {
                                        String oldClsxid = oldDchyXmglChxmClsx.getClsxid();
                                        oldClsxidList.add(oldClsxid);
                                        oldDchyXmglChxmClsx.setChxmid(newChxmid);
                                        entityMapper.saveOrUpdate(oldDchyXmglChxmClsx, oldDchyXmglChxmClsx.getClsxid());
                                        dchyXmglChxmClsxList.add(oldDchyXmglChxmClsx);
                                    }
                                }
                                dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);
                            }
                        }

                        //??????????????????
                        Example exampleChtl = new Example(DchyXmglClsxChtl.class);
                        exampleChtl.createCriteria().andEqualTo("chxmid", chxmid);
                        List<DchyXmglClsxChtl> oldDchyXmglClsxChtlList = entityMapper.selectByExampleNotNull(exampleChtl);
                        if(CollectionUtils.isNotEmpty(oldDchyXmglClsxChtlList)){
                            for(DchyXmglClsxChtl oldDchyXmglClsxChtl:oldDchyXmglClsxChtlList){
                                String dm = oldDchyXmglClsxChtl.getClsx();
                                DchyXmglZd zdxx = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX",dm);
                                if(zdxx != null && StringUtils.isNotEmpty(zdxx.getFdm())){
                                    if(StringUtils.equals(zdxx.getFdm(),cljd)){
                                        oldDchyXmglClsxChtl.setChxmid(newChxmid);
                                        entityMapper.saveOrUpdate(oldDchyXmglClsxChtl, oldDchyXmglClsxChtl.getChtlid());
                                        dchyXmglClsxChtlList.add(oldDchyXmglClsxChtl);
                                    }
                                }else if(zdxx != null && StringUtils.isEmpty(zdxx.getFdm())){
                                    if(StringUtils.equals(zdxx.getDm(),cljd)){
                                        oldDchyXmglClsxChtl.setChxmid(newChxmid);
                                        entityMapper.saveOrUpdate(oldDchyXmglClsxChtl, oldDchyXmglClsxChtl.getChtlid());
                                        dchyXmglClsxChtlList.add(oldDchyXmglClsxChtl);
                                    }
                                }
                            }
                            dchyXmglChxmDto.setDchyXmglClsxChtlList(dchyXmglClsxChtlList);
                        }

                        //????????????????????????
                        Example exampleSjxx = new Example(DchyXmglSjxx.class);
                        exampleSjxx.createCriteria().andEqualTo("glsxid", chxmid);
                        List<DchyXmglSjxx> oldDchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
                        if (CollectionUtils.isNotEmpty(oldDchyXmglSjxxList)) {
                            for (DchyXmglSjxx oldDchyXmglSjxx : oldDchyXmglSjxxList) {
                                String sjxxid = oldDchyXmglSjxx.getSjxxid();
                                String newSjxxid = UUIDGenerator.generate18();
                                oldDchyXmglSjxx.setSjxxid(newSjxxid);
                                oldDchyXmglSjxx.setGlsxid(newChxmid);
                                entityMapper.saveOrUpdate(oldDchyXmglSjxx, oldDchyXmglSjxx.getSjxxid());
                                dchyXmglSjxxList.add(oldDchyXmglSjxx);

                                //????????????????????????????????????
                                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                                exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                                List<DchyXmglSjcl> oldDchyXmglSjclList = entityMapper.selectByExampleNotNull(exampleSjcl);
                                if (CollectionUtils.isNotEmpty(oldDchyXmglSjclList)) {
                                    for (DchyXmglSjcl oldDchyXmglSjcl : oldDchyXmglSjclList) {
                                        if ((StringUtils.equals(oldDchyXmglSjxx.getSsmkid(), SsmkidEnum.XXBA.getCode()) || StringUtils.equals(oldDchyXmglSjxx.getSsmkid(), SsmkidEnum.JSDWWT.getCode())) && StringUtils.isNotEmpty(oldDchyXmglSjcl.getClsx()) && StringUtils.isNotEmpty(oldDchyXmglSjcl.getWjzxid())) {
                                            List<String> oldClsxList = Lists.newArrayList();
                                            String clsx = oldDchyXmglSjcl.getClsx();
                                            boolean statuss = clsx.contains(",");
                                            if (statuss) {
                                                Example example = new Example(DchyXmglSjcl.class);
                                                example.createCriteria().andEqualTo("sjxxid", newSjxxid).andEqualTo("clsx", clsx);
                                                List<DchyXmglSjcl> newSjclList = entityMapper.selectByExampleNotNull(example);
                                                if (CollectionUtils.isEmpty(newSjclList)) {
                                                    oldClsxList = Arrays.asList(clsx.split(Constants.DCHY_XMGL_CLSX_SEPARATOR2));//???????????????????????????list
                                                }
                                            } else {
                                                oldClsxList.add(clsx);
                                            }
                                            //??????????????????????????????????????????????????????????????????????????????
                                            boolean isMatched = true;
                                            int countClsx = 0;
                                            for (Object obj : oldClsxList) {
                                                if (oldClsxLists.contains(obj)) {
                                                    countClsx = countClsx + 1;
                                                }
                                            }
                                            if (countClsx >= 1) {
                                                if (StringUtils.isNotEmpty(oldDchyXmglSjcl.getWjzxid())) {
                                                    //?????????????????????????????????????????????
                                                    String newSjclid = UUIDGenerator.generate18();
                                                    oldDchyXmglSjcl.setSjclid(newSjclid);
                                                    oldDchyXmglSjcl.setSjxxid(newSjxxid);
                                                    entityMapper.saveOrUpdate(oldDchyXmglSjcl, oldDchyXmglSjcl.getSjclid());
                                                    dchyXmglSjclList.add(oldDchyXmglSjcl);

                                                    List<Map<String, String>> file;
                                                    Map<String, List> listMap = new HashMap<>();
                                                    String wjzxid = oldDchyXmglSjcl.getWjzxid();
                                                    if (StringUtils.isNotBlank(wjzxid)) {
                                                        /*????????????*/
                                                        file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                                        if (CollectionUtils.isNotEmpty(file)) {
                                                            listMap.put(newSjclid, file);
                                                            fileList.add(listMap);
                                                            try {
                                                                this.uploadWjcl(file, newChxmid, oldDchyXmglSjcl.getClmc());
                                                            } catch (Exception e) {
                                                                logger.error("????????????:{}", e);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (StringUtils.equals(oldDchyXmglSjxx.getSsmkid(), SsmkidEnum.CHDWHY.getCode()) && StringUtils.isNotEmpty(oldDchyXmglSjcl.getWjzxid())) {
                                            //????????????
                                            String newSjclid = UUIDGenerator.generate18();
                                            oldDchyXmglSjcl.setSjclid(newSjclid);
                                            oldDchyXmglSjcl.setSjxxid(newSjxxid);
                                            entityMapper.saveOrUpdate(oldDchyXmglSjcl, oldDchyXmglSjcl.getSjclid());
                                            dchyXmglSjclList.add(oldDchyXmglSjcl);

                                            List<Map<String, String>> file;
                                            Map<String, List> listMap = new HashMap<>();
                                            String wjzxid = oldDchyXmglSjcl.getWjzxid();
                                            if (StringUtils.isNotBlank(wjzxid)) {
                                                /*????????????*/
                                                file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                                if (CollectionUtils.isNotEmpty(file)) {
                                                    listMap.put(newSjclid, file);
                                                    fileList.add(listMap);
                                                    try {
                                                        this.uploadWjcl(file, newChxmid, oldDchyXmglSjcl.getClmc());
                                                    } catch (Exception e) {
                                                        logger.error("????????????:{}", e);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        dchyXmglChxmDto.setDchyXmglSjxxList(dchyXmglSjxxList);
                        dchyXmglChxmDto.setDchyXmglSjclList(dchyXmglSjclList);
                        dchyXmglChxmDto.setFileList(fileList);

                        //????????????

                        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = generateHtxxList(chxmid, newChxmid, newChdwxxid, oldClsxidList);

                        dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);
                    }

                    //????????????
                    if (oldDchyXmglChxm != null) {
                        String chgcid = oldDchyXmglChxm.getChgcid();
                        if (StringUtils.isNotEmpty(chgcid)) {
                            DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                            dchyXmglChxmDto.setDchyXmglChgc(dchyXmglChgc);
                        }
                    }

                    //??????chxmid???????????????????????????dbrwid??????????????????????????????
                    String dbrwid = null;
                    Example sqxxExample = new Example(DchyXmglSqxx.class);
                    sqxxExample.createCriteria().andEqualTo("glsxid", chxmid);
                    List<DchyXmglSqxx> sqxxList = entityMapper.selectByExampleNotNull(sqxxExample);
                    if (CollectionUtils.isNotEmpty(sqxxList)) {
                        for (DchyXmglSqxx sqxxLists : sqxxList) {
                            String sqxxid = sqxxLists.getSqxxid();
                            Example dbrwExample = new Example(DchyXmglDbrw.class);
                            dbrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                            List<DchyXmglDbrw> dbrwList = entityMapper.selectByExampleNotNull(dbrwExample);
                            if (CollectionUtils.isNotEmpty(dbrwList)) {
                                for (DchyXmglDbrw dbrwLists : dbrwList) {
                                    dbrwid = dbrwLists.getDbrwid();
                                    boolean shtg = StringUtils.equalsIgnoreCase(sftg, Constants.SHTG);
                                    /*???????????????????????????*/
                                    DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                                    dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());
                                    dchyXmglYbrw.setBlyj(shyj);
                                    dchyXmglYbrw.setJssj(CalendarUtil.getCurHMSDate());
                                    dchyXmglYbrw.setSqxxid(sqxxid);
                                    dchyXmglYbrw.setBljg(shtg ? Constants.INVALID : Constants.VALID);
                                    dchyXmglYbrw.setDqjd(dbrwLists.getDqjd());
                                    dchyXmglYbrw.setZrsj(dbrwLists.getZrsj());
                                    dchyXmglYbrw.setYbrwid(dbrwLists.getDbrwid());

                                    /*??????????????????*/
                                    entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                                    dchyXmglYbrwList.add(dchyXmglYbrw);

                                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid);
                                    if (shtg) {
                                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_TH);
                                    } else {
                                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_SHTG);
                                    }
                                    entityMapper.saveOrUpdate(dchyXmglSqxx, sqxxid);
                                    dchyXmglSqxxList.add(dchyXmglSqxx);
                                }
                            }
                        }
                    }
                    dchyXmglChxmDtoList.add(dchyXmglChxmDto);
                }
            }

            Example sqxxExample = new Example(DchyXmglSqxx.class);
            sqxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> sqxxList = entityMapper.selectByExampleNotNull(sqxxExample);
            if (CollectionUtils.isNotEmpty(sqxxList)) {
                for (DchyXmglSqxx sqxxLists : sqxxList) {
                    String sqxxid = sqxxLists.getSqxxid();
                    Example dbrwExample = new Example(DchyXmglDbrw.class);
                    dbrwExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                    List<DchyXmglDbrw> dbrwList = entityMapper.selectByExampleNotNull(dbrwExample);
                    if (CollectionUtils.isNotEmpty(dbrwList)) {
                        for (DchyXmglDbrw dbrwLists : dbrwList) {
                            String dbrwid = dbrwLists.getDbrwid();
                            //??????????????????
                            entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dbrwid);
                        }
                    }
                }
            }
        }

        return dchyXmglChxmDtoList;
    }

    public List<Map<String, String>> generateFileByWjzxid(int nodeId) {
        List<Map<String, String>> sjclList = new ArrayList<>();
        try {
            List<Node> nodeList = getNodeService().getChildNodes(nodeId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                /*????????????*/
                if (nodeList.size() == 1) {
                    Node node = nodeList.get(0);
                    byte[] body = FileDownoadUtil.downloadWj(node.getId());
                    String fileName = node.getName();
                    String file = Base64.getEncoder().encodeToString(body);
                    Map<String, String> sjcl = new HashMap<>();
                    sjcl.put(fileName, file);
                    sjclList.add(sjcl);
                } else {
                    for (Node node : nodeList) {
                        byte[] body = FileDownoadUtil.downloadWj(node.getId());
                        String fileName = node.getName();
                        String file = Base64.getEncoder().encodeToString(body);
                        Map<String, String> sjcl = new HashMap<>();
                        sjcl.put(fileName, file);
                        sjclList.add(sjcl);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return sjclList;
    }

    private List<DchyXmglHtxxDto> generateHtxxList(String chxmid, String newChxmid, String newChdwxxid, List oldClsxidList) {
        List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = Lists.newArrayList();
        List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList = Lists.newArrayList();
        List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = Lists.newArrayList();
        List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList = Lists.newArrayList();
        List<DchyXmglSjxx> dchyXmglSjxxList = Lists.newArrayList();
        List<DchyXmglSjcl> dchyXmglSjclList = Lists.newArrayList();

        //????????????????????????????????????id?????????????????????????????????
        Set set = new HashSet();
        Example exampleClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
        exampleClsxHtxxGx.createCriteria().andEqualTo("chxmid", chxmid).andIn("clsxid", oldClsxidList);
        List<DchyXmglClsxHtxxGx> oldClsxHtxxGxList = entityMapper.selectByExampleNotNull(exampleClsxHtxxGx);
        if (CollectionUtils.isNotEmpty(oldClsxHtxxGxList)) {
            List<String> oldHtxxidList = Lists.newArrayList();
            for (DchyXmglClsxHtxxGx oldClsxHtxxGx : oldClsxHtxxGxList) {
                if (set.add(StringUtils.isNotEmpty(oldClsxHtxxGx.getHtxxid()))) {
                    oldHtxxidList.add(oldClsxHtxxGx.getHtxxid());
                }
            }

            //????????????
            if (CollectionUtils.isNotEmpty(oldHtxxidList)) {
                for (String htxxid : oldHtxxidList) {
                    DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
                    if (StringUtils.isNotEmpty(htxxid)) {
                        DchyXmglHtxx oldDchyXmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
                        oldDchyXmglHtxx.setChxmid(newChxmid);
                        entityMapper.saveOrUpdate(oldDchyXmglHtxx, oldDchyXmglHtxx.getHtxxid());
                        dchyXmglHtxxDto.setDchyXmglHtxx(oldDchyXmglHtxx);

                        //????????????????????????
                        List<Map<String, List>> htFileList = new ArrayList<>();
                        Example exampleSjxx = new Example(DchyXmglSjxx.class);
                        exampleSjxx.createCriteria().andEqualTo("glsxid", htxxid);
                        List<DchyXmglSjxx> oldDchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
                        if (CollectionUtils.isNotEmpty(oldDchyXmglSjxxList)) {
                            for (DchyXmglSjxx oldDchyXmglSjxx : oldDchyXmglSjxxList) {
                                String sjxxid = oldDchyXmglSjxx.getSjxxid();
                                dchyXmglSjxxList.add(oldDchyXmglSjxx);

                                Example exampleSjcl1 = new Example(DchyXmglSjcl.class);
                                exampleSjcl1.createCriteria().andEqualTo("sjxxid", sjxxid);
                                List<DchyXmglSjcl> oldDchyXmglSjclList1 = entityMapper.selectByExampleNotNull(exampleSjcl1);
                                if (CollectionUtils.isNotEmpty(oldDchyXmglSjclList1)) {
                                    for (DchyXmglSjcl oldDchyXmglSjclLists1 : oldDchyXmglSjclList1) {
                                        if (StringUtils.isNotEmpty(oldDchyXmglSjclLists1.getClmc())) {
                                            String sjclid = oldDchyXmglSjclLists1.getSjclid();

                                            dchyXmglSjclList.add(oldDchyXmglSjclLists1);

                                            /*??????????????????*/
                                            List<Map<String, String>> htFile;
                                            Map<String, List> listMap = new HashMap<>();
                                            String wjzxid = oldDchyXmglSjclLists1.getWjzxid();
                                            if (StringUtils.isNotBlank(wjzxid)) {
                                                htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                                if (CollectionUtils.isNotEmpty(htFile)) {
                                                    listMap.put(sjclid, htFile);
                                                    htFileList.add(listMap);
                                                    try {
                                                        this.uploadWjcl(htFile, htxxid, oldDchyXmglSjclLists1.getClmc());
                                                    } catch (Exception e) {
                                                        logger.error("????????????:{}", e);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            dchyXmglHtxxDto.setDchyXmglSjxxList(dchyXmglSjxxList);
                            dchyXmglHtxxDto.setDchyXmglSjclList(dchyXmglSjclList);
                            dchyXmglHtxxDto.setHtFileList(htFileList);
                        }

                        //?????????????????????
                        if (StringUtils.isNotEmpty(newChdwxxid)) {
                            DchyXmglHtxxChdwxxGx dchyXmglHtxxChdwxxGx = new DchyXmglHtxxChdwxxGx();
                            String newGxid = UUIDGenerator.generate18();
                            dchyXmglHtxxChdwxxGx.setGxid(newGxid);
                            dchyXmglHtxxChdwxxGx.setHtxxid(htxxid);
                            dchyXmglHtxxChdwxxGx.setChdwxxid(newChdwxxid);
                            dchyXmglHtxxChdwxxGx.setChxmid(newChxmid);
                            entityMapper.saveOrUpdate(dchyXmglHtxxChdwxxGx, dchyXmglHtxxChdwxxGx.getGxid());
                            dchyXmglHtxxChdwxxGxList.add(dchyXmglHtxxChdwxxGx);
                            dchyXmglHtxxDto.setDchyXmglHtxxChdwxxGxList(dchyXmglHtxxChdwxxGxList);
                        }

                        //?????????????????????????????????????????????????????????
                        Iterator<String> iterator = oldClsxidList.iterator();
                        while (iterator.hasNext()) {
                            String newClsxid = iterator.next();
                            //??????????????????
                            DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx = new DchyXmglClsxHtxxGx();
                            dchyXmglClsxHtxxGx.setGxid(UUIDGenerator.generate18());
                            dchyXmglClsxHtxxGx.setClsxid(newClsxid);
                            dchyXmglClsxHtxxGx.setHtxxid(htxxid);
                            dchyXmglClsxHtxxGx.setChxmid(newChxmid);
                            entityMapper.saveOrUpdate(dchyXmglClsxHtxxGx, dchyXmglClsxHtxxGx.getGxid());
                            dchyXmglClsxHtxxGxList.add(dchyXmglClsxHtxxGx);

                            //???????????????????????????????????????
                            DchyXmglClsxChdwxxGx dchyXmglClsxChdwxxGx = new DchyXmglClsxChdwxxGx();
                            dchyXmglClsxChdwxxGx.setGxid(UUIDGenerator.generate18());
                            dchyXmglClsxChdwxxGx.setClsxid(newClsxid);
                            dchyXmglClsxChdwxxGx.setChdwxxid(newChdwxxid);
                            entityMapper.saveOrUpdate(dchyXmglClsxChdwxxGx, dchyXmglClsxChdwxxGx.getGxid());
                            dchyXmglClsxChdwxxGxList.add(dchyXmglClsxChdwxxGx);
                        }
                        dchyXmglHtxxDto.setDchyXmglClsxHtxxGxList(dchyXmglClsxHtxxGxList);
                        dchyXmglHtxxDto.setDchyXmglClsxChdwxxGxList(dchyXmglClsxChdwxxGxList);
                    }
                    dchyXmglHtxxDtoList.add(dchyXmglHtxxDto);
                }
            }
        }
        return dchyXmglHtxxDtoList;
    }

    public List<DchyXmglYhxx> generateYhxx(String xmmc, String wtdw, String chxmid, String
            sftg, List<Map<String, Object>> chdwxxList) {
        String xxnr = null;
        String xxlx = null;
        String sftz = null;
        String yhxxpzid = null;
        DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
        if (dchyXmglChxm != null) {
            String wtzt = dchyXmglChxm.getWtzt();
            if (StringUtils.isNotEmpty(wtzt)) {
                if (StringUtils.equals(sftg, Constants.SHTG)) {
                    yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_XSWTSHTG;
                } else if (StringUtils.equals(sftg, Constants.SHBTG)) {
                    yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_XSWTSHBTG;
                }
            } else {
                if (StringUtils.equals(sftg, Constants.SHTG)) {
                    yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_HTBAYTG;
                } else if (StringUtils.equals(sftg, Constants.SHBTG)) {
                    yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_HTBAYBTG;
                }
            }
        }
        //??????????????????
        List<DchyXmglYhxx> dchyXmglYhxxList = Lists.newArrayList();
        Example example = new Example(DchyXmglYhxxPz.class);
        example.createCriteria().andEqualTo("id", yhxxpzid);
        List<DchyXmglYhxxPz> xxnrzd = entityMapper.selectByExampleNotNull(example);
        if (CollectionUtils.isNotEmpty(xxnrzd)) {
            for (DchyXmglYhxxPz xxnrzds : xxnrzd) {
                if (StringUtils.isNotEmpty(xmmc)) {
                    xxnr = xxnrzds.getXxnr().replaceAll("????????????", xmmc);
                } else {
                    xxnr = xxnrzds.getXxnr();
                }
                xxlx = xxnrzds.getXxlx();
                sftz = xxnrzds.getSftz();
            }
        }

        //????????????????????????
        if (chdwxxList != null) {
            for (Map<String, Object> chdwxxLists : chdwxxList) {
                DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();
                dchyXmglYhxx.setFsyhid(UserUtil.getCurrentUserId());
                dchyXmglYhxx.setFssj(CalendarUtil.getCurHMSDate());
                dchyXmglYhxx.setDqzt(Constants.INVALID);//??????
                dchyXmglYhxx.setYhxxid(UUIDGenerator.generate18());
                String jsyhid = MapUtils.getString(chdwxxLists, "chdwid");
                dchyXmglYhxx.setJsyhid(jsyhid);
                dchyXmglYhxx.setGlsxid(chxmid);
                dchyXmglYhxx.setXxnr(xxnr); //????????????
                dchyXmglYhxx.setXxlx(xxlx); //????????????
                dchyXmglYhxx.setSftz(sftz); //????????????
                dchyXmglYhxxList.add(dchyXmglYhxx);
            }
        }
        //????????????????????????
        if (StringUtils.isNotBlank(wtdw)) {
            DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();
            dchyXmglYhxx.setFsyhid(UserUtil.getCurrentUserId());
            dchyXmglYhxx.setFssj(CalendarUtil.getCurHMSDate());
            dchyXmglYhxx.setDqzt(Constants.INVALID);//??????
            dchyXmglYhxx.setYhxxid(UUIDGenerator.generate18());
            dchyXmglYhxx.setJsyhid(wtdw);
            dchyXmglYhxx.setGlsxid(chxmid);
            dchyXmglYhxx.setXxnr(xxnr); //????????????
            dchyXmglYhxx.setXxlx(xxlx); //????????????
            dchyXmglYhxx.setSftz(sftz); //????????????
            dchyXmglYhxxList.add(dchyXmglYhxx);
        }
        return dchyXmglYhxxList;
    }

    @Override
    public Page<Map<String, Object>> getCommissionTask(Map<String, Object> param) {
        Page<Map<String, Object>> resultList = null;
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String gcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
            String gcmc = CommonUtil.formatEmptyValue(data.get("gcmc"));
            String babh = CommonUtil.formatEmptyValue(data.get("babh"));
            String jsdwmc = CommonUtil.formatEmptyValue(data.get("jsdwmc"));
            String chdwmc = CommonUtil.formatEmptyValue(data.get("chdwmc"));
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("gcbh", gcbh);
            paramMap.put("gcmc", gcmc);
            paramMap.put("xqfbbh", babh);
            paramMap.put("wtdw", jsdwmc);
            paramMap.put("chdwmc", chdwmc);
            int page = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("page")));
            int pageSize = Integer.parseInt(CommonUtil.formatEmptyValue(data.get("pageSize")));

            resultList = repository.selectPaging("queryCommissionTaskByPage", paramMap, page - 1, pageSize);

            if (CollectionUtils.isNotEmpty(resultList.getContent())) {
                for (Map<String, Object> mapTemp : resultList.getContent()) {
                    if (StringUtils.isNotEmpty(CommonUtil.ternaryOperator(mapTemp.get("WTZT")))) {
                        String wtztdm = CommonUtil.ternaryOperator(mapTemp.get("WTZT"));
                        DchyXmglZd zdWtzt = dchyXmglZdService.getDchyXmglByZdlxAndDm("WTZT", wtztdm);
                        if (zdWtzt != null) {
                            mapTemp.put("XMZT", zdWtzt.getMc());
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return resultList;
    }

    public String obtainBabh(String chxmid, String wtdw, String cljd) {
        String babh = null;
        String jsdwm = null;
        String cljdm = null;
        String babhLsh = dchyXmglChxmMapper.queryMaxBabh();
        if (StringUtils.isNotEmpty(babhLsh)) {
            if (babhLsh.length() > 4) {
                babhLsh = babhLsh.substring(babhLsh.length() - 4, babhLsh.length());
            }
        } else {
            babhLsh = "0001";
        }

        if (StringUtils.isNotEmpty(chxmid)) {
            //???????????????
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (dchyXmglChxm != null) {
                String chgcid = dchyXmglChxm.getChgcid();
                if (StringUtils.isNotEmpty(chgcid)) {
                    DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, chgcid);
                    if (dchyXmglChgc != null) {
                        if (StringUtils.isNotEmpty(dchyXmglChgc.getJsdwm())) {
                            jsdwm = dchyXmglChgc.getJsdwm();
                        } else {
                            jsdwm = ConstructionCodeUtil.getPinYinHeadChar(wtdw).substring(0, 6);
                        }
                    } else {
                        if (StringUtils.isNotEmpty(wtdw)) {
                            jsdwm = ConstructionCodeUtil.getPinYinHeadChar(wtdw).substring(0, 6);
                        }
                    }
                }
            }

            //????????????
            if (StringUtils.isNotEmpty(cljd)) {
                Example exampleZd = new Example(DchyXmglZd.class);
                exampleZd.createCriteria().andEqualTo("zdlx", "CLSX").andEqualTo("dm", cljd);
                List<DchyXmglZd> clsxzd = entityMapper.selectByExampleNotNull(exampleZd);
                if (CollectionUtils.isNotEmpty(clsxzd)) {
                    cljdm = clsxzd.get(0).getQtsx();
                } else {
                    cljdm = "XX";
                }
            }
        }
        babh = CalendarUtil.getCurStrYear() + babhLsh + XmbhFormatUtil.numberAfterFillZero(jsdwm, 6) + cljdm;
        return babh;
    }

    /**
     * @param babh
     * @return
     * @description 2021/5/31 ??????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    private List<DchyXmglChxmDto> generateChxmDtoBtByBabh(String babh) {
        List<DchyXmglChxmDto> dchyXmglChxmDtoList = Lists.newArrayList();
        if (StringUtils.isNotBlank(babh)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("babh", babh);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                for (DchyXmglChxm dchyXmglChxm : dchyXmglChxmList) {
                    //?????????????????????????????????,????????????????????????,?????????????????????
                    if (!StringUtils.equals(dchyXmglChxm.getXmly(), Constants.XMLY_XXFB)) {
                        return null;
                    }
                    dchyXmglChxmDtoList.add(this.generateChxmDtoBtByChxmid(dchyXmglChxm.getChxmid()));
                }
            }
        }
        return dchyXmglChxmDtoList;
    }

    /**
     * @param chxmid
     * @return
     * @description 2021/5/31 ??????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    private DchyXmglChxmDto generateChxmDtoBtByChxmid(String chxmid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        List<DchyXmglSjcl> dchyXmglSjclList = Lists.newArrayList();
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            dchyXmglChxmDto.setDchyXmglChxm(dchyXmglChxm);

            Example exampleHtxx = new Example(DchyXmglHtxx.class);
            Example exampleClsx = new Example(DchyXmglChxmClsx.class);
            Example exampleChdwxx = new Example(DchyXmglChxmChdwxx.class);
            exampleHtxx.createCriteria().andEqualTo("chxmid", chxmid);
            exampleClsx.createCriteria().andEqualTo("chxmid", chxmid);
            exampleChdwxx.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> dchyXmglHtxxList = entityMapper.selectByExample(exampleHtxx);
            List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(exampleClsx);
            List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapper.selectByExample(exampleChdwxx);
            dchyXmglChxmDto.setDchyXmglChxmClsxList(dchyXmglChxmClsxList);
            dchyXmglChxmDto.setDchyXmglChxmChdwxxList(dchyXmglChxmChdwxxList);
            dchyXmglChxmDto.setDchyXmglChgc(entityMapper.selectByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid()));

            //????????????????????????
            List<Map<String, List>> fileList = new ArrayList<>();
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                    String sjxxid = dchyXmglSjxx.getSjxxid();
                    Example exampleSjcl = new Example(DchyXmglSjcl.class);
                    exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                    List<DchyXmglSjcl> dchyXmglSjclList1 = entityMapper.selectByExample(exampleSjcl);
                    dchyXmglSjclList.addAll(dchyXmglSjclList1);
                }
            }

            dchyXmglChxmDto.setDchyXmglSjxxList(dchyXmglSjxxList);
            dchyXmglChxmDto.setDchyXmglSjclList(dchyXmglSjclList);

            if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                    if (StringUtils.isNotEmpty(dchyXmglSjcl.getWjzxid())) {
                        List<Map<String, String>> file;
                        Map<String, List> listMap = new HashMap<>();
                        String wjzxid = dchyXmglSjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*????????????*/
                            file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            if (CollectionUtils.isNotEmpty(file)) {
                                listMap.put(dchyXmglSjcl.getSjclid(), file);
                                fileList.add(listMap);
                            }
                        }
                    }
                }
            }

            //??????????????????dto
            List<DchyXmglHtxxDto> dchyXmglHtxxDtoList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(dchyXmglHtxxList)) {
                for (DchyXmglHtxx dchyXmglHtxx : dchyXmglHtxxList) {
                    dchyXmglHtxxDtoList.add(this.generateHtxxDtoBtByHtxxid(dchyXmglHtxx.getHtxxid()));
                }
            }
            dchyXmglChxmDto.setDchyXmglHtxxDtoList(dchyXmglHtxxDtoList);
        }
        return dchyXmglChxmDto;
    }

    private DchyXmglHtxxDto generateHtxxDtoBtByHtxxid(String htxxid) {
        DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
        if (StringUtils.isNotBlank(htxxid)) {
            List<DchyXmglSjxx> dchyXmglSjxxList = Lists.newArrayList();
            List<DchyXmglSjcl> dchyXmglSjclList = Lists.newArrayList();

            DchyXmglHtxx dchyXmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
            dchyXmglHtxxDto.setDchyXmglHtxx(dchyXmglHtxx);

            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", htxxid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                List<Map<String, List>> htFileList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    String sjxxid = sjxx.getSjxxid();
                    if (StringUtils.isNotEmpty(sjxxid)) {
                        Example exampleSjcl = new Example(DchyXmglSjcl.class);
                        exampleSjcl.createCriteria().andEqualTo("sjxxid", sjxxid);
                        List<DchyXmglSjcl> xmglsjclList = entityMapper.selectByExampleNotNull(exampleSjcl);
                        if (CollectionUtils.isNotEmpty(xmglsjclList)) {
                            for (DchyXmglSjcl sjcl : dchyXmglSjclList) {
                                /*??????????????????*/
                                List<Map<String, String>> htFile;
                                Map<String, List> listMap = new HashMap<>();
                                String wjzxid = sjcl.getWjzxid();
                                if (StringUtils.isNotBlank(wjzxid)) {
                                    htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                                    if (htFile != null) {
                                        listMap.put(sjcl.getSjclid(), htFile);
                                        htFileList.add(listMap);
                                    }
                                }
                            }
                        }
                        dchyXmglHtxxDto.setDchyXmglSjclList(xmglsjclList);
                    }
                }
                dchyXmglHtxxDto.setHtFileList(htFileList);
                dchyXmglHtxxDto.setDchyXmglSjxxList(dchyXmglSjxxList);
            }
        }
        return dchyXmglHtxxDto;
    }

    /**
     * @param paramMap
     * @return
     * @description 2021/6/1 ????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage supplementaryPushData(Map<String, Object> paramMap) {
        ResponseMessage message;
        String babh = CommonUtil.formatEmptyValue(MapUtils.getString(paramMap, "babh"));
        if (StringUtils.isNotBlank(babh)) {
            try {
                Map<String, Object> resultMap = Maps.newHashMap();
                List<DchyXmglChxmDto> dchyXmglChxmDtoList = generateChxmDtoBtByBabh(babh);
                if (CollectionUtils.isNotEmpty(dchyXmglChxmDtoList)) {
                    DchyXmglChxmListDto dchyXmglChxmListDto = new DchyXmglChxmListDto();
                    dchyXmglChxmListDto.setDchyXmglChxmListDto(dchyXmglChxmDtoList);
                    pushDataToMqService.pushBaxxMsgToMq(dchyXmglChxmListDto);
                    message = ResponseUtil.wrapSuccessResponse();
//                    message.setData(resultMap);
                } else {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.XMLY_FAIL.getMsg(), ResponseMessage.CODE.XMLY_FAIL.getCode());
                }

            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                logger.error("????????????:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    private Map<String, Object> uploadWjcl(List<Map<String, String>> wjList, String mainId, String clmc) throws
            IOException {
        Map<String, Object> objectMap = Maps.newHashMap();
        logger.info("----------------------------------------------------------??????ID???" + mainId);
        if (CollectionUtils.isNotEmpty(wjList)) {
            MultipartFile[] files = new MultipartFile[wjList.size()];
            int i = 0;
            for (Map<String, String> map : wjList) {
                // ?????????--????????????
                for (String key : map.keySet()) {
                    String base64 = map.get(key);
                    //System.out.println(base64);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    if (bytes != null) {
                        InputStream inputStream = new ByteArrayInputStream(bytes);
                        MockMultipartFile file = new MockMultipartFile(key, key, ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
                        byte[] byte2 = file.getBytes();
                        if (byte2 == null) {
                            logger.info("--------------------------------------??????????????????");
                        }
                        files[i] = file;
                        i++;
                    } else {
                        logger.info("------------------------------------------??????????????????");
                    }
                }
            }
            // ??????,??????name,????????????id
            objectMap.putAll(FileUploadUtil.uploadFile(files, mainId, clmc));
        }
        return objectMap;
    }

}
