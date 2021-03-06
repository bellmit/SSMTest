package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChdwMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmClsxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglXcslMapper;
import cn.gtmap.msurveyplat.promanage.core.service.XcsldjService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import cn.gtmap.msurveyplat.promanage.utils.SlbhUtil;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
@Service
public class XcsldjServiceImpl implements XcsldjService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private DchyXmglChxmMapper dchyXmglChxmMapper;
    @Autowired
    private DchyXmglChdwMapper dchyXmglChdwMapper;
    @Autowired
    private DchyXmglXcslMapper dchyXmglXcslMapper;

    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Autowired
    private DchyXmglChxmClsxMapper dchyXmglChxmClsxMapper;

    private final String DELETED = "delete_";

    private Logger logger = LoggerFactory.getLogger(XcsldjServiceImpl.class);

    @Override
    public Map<String, Object> initXcsldj() {
        DchyXmglChxm dchyXmglChxm = new DchyXmglChxm();
        String slbh = SlbhUtil.generateSlbh(null, null);
        String slr = UserUtil.getCurrentUserId();
        Date slsj = new Date();
        dchyXmglChxm.setChxmid(UUIDGenerator.generate());
        dchyXmglChxm.setSlbh(slbh);
        dchyXmglChxm.setSlr(slr);
        dchyXmglChxm.setSlrmc(UserUtil.getUserNameById(slr));
        dchyXmglChxm.setSlsj(slsj);
        dchyXmglChxm.setXmzt("0");
        dchyXmglChxm.setXmly(Constants.XMLY_XXFB);
        try {
            entityMapper.insertSelective(dchyXmglChxm);
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("chxmid", dchyXmglChxm.getChxmid());
        result.put("slbh", slbh);
        result.put("slsj", slsj);

        return result;
    }

    @Override
    public List<Map<String, Object>> queryChdwList() {
        List<Map<String, Object>> chxmList = null;
        try {
            chxmList = dchyXmglChdwMapper.queryChdwList();
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }
        return chxmList;
    }

    @Override
    public Map<String, Object> saveChdw(Map<String, Object> map) {
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String tyshxydm = CommonUtil.formatEmptyValue(data.get("tyshxydm"));
        String msg = ResponseMessage.CODE.SAVE_FAIL.getMsg();
        String code = ResponseMessage.CODE.SAVE_FAIL.getCode();
        if (StringUtils.isNotBlank(tyshxydm)) {
            Example dchyXmglMlkExample = new Example(DchyXmglMlk.class);
            dchyXmglMlkExample.createCriteria().andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm)).andEqualTo("sfyx", Constants.VALID);
            List<DchyXmglMlk> dchyXmglMlkList = entityMapper.selectByExampleNotNull(dchyXmglMlkExample);
            if (CollectionUtils.isNotEmpty(dchyXmglMlkList)) {
                msg = "???????????????????????????????????????????????????????????????????????????";
            } else {
                Example dchyXmglChdwExample = new Example(DchyXmglChdw.class);
                dchyXmglChdwExample.createCriteria().andEqualTo("tyshxydm", SM4Util.encryptData_ECB(tyshxydm));
                List<DchyXmglChdw> dchyXmglChdwList = entityMapper.selectByExampleNotNull(dchyXmglChdwExample);
                if (CollectionUtils.isNotEmpty(dchyXmglChdwList)) {
                    msg = "??????????????????????????????????????????????????????????????????????????????????????????";
                } else {
                    DchyXmglChdw dchyXmglChdw = new DchyXmglChdw();
                    dchyXmglChdw.setChdwid(UUIDGenerator.generate());
                    dchyXmglChdw.setChdwmc(CommonUtil.formatEmptyValue(data.get("chdwmc")));
                    dchyXmglChdw.setTyshxydm(tyshxydm);
                    dchyXmglChdw.setFrdb(CommonUtil.formatEmptyValue(data.get("frdb")));
                    dchyXmglChdw.setZzdj(CommonUtil.formatEmptyValue(data.get("zzdj")));
                    dchyXmglChdw.setBgdzs(CommonUtil.formatEmptyValue(data.get("bgdzs")));
                    dchyXmglChdw.setBgdzss(CommonUtil.formatEmptyValue(data.get("bgdzss")));
                    dchyXmglChdw.setBgdzqx(CommonUtil.formatEmptyValue(data.get("bgdzqx")));
                    dchyXmglChdw.setBgdzxx(CommonUtil.formatEmptyValue(data.get("bgdzxx")));
                    dchyXmglChdw.setLxr(CommonUtil.formatEmptyValue(data.get("lxr")));
                    dchyXmglChdw.setLxdh(CommonUtil.formatEmptyValue(data.get("lxdh")));
                    dchyXmglChdw.setLrr(UserUtil.getCurrentUser().getUsername());
                    dchyXmglChdw.setLrsj(CalendarUtil.getCurHMSDate());
                    DataSecurityUtil.encryptSingleObject(dchyXmglChdw);
                    int flag = entityMapper.insertSelective(dchyXmglChdw);
                    if (flag > 0) {
                        //?????????????????????????????????
                        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
                        List<DchyXmglChdw> chdwList = new ArrayList<>();
                        chdwList.add(dchyXmglChdw);
                        dchyXmglChxmDto.setDchyXmglChdwList(chdwList);
                        pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);
                    }
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                }
            }
        } else {
            msg = "?????????????????????????????????????????????";
            code = ResponseMessage.CODE.SAVE_FAIL.getCode();
        }
        result.put("msg", msg);
        result.put("code", code);
        return result;
    }

    @Override
    @Transactional
    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglChxmDto.class, czmkCode = ProLog.CZMK_XZSLDJ_CODE, czmkMc = ProLog.CZMK_XZSLDJ_MC)
    public boolean saveXcsldj(DchyXmglChxmDto dchyXmglChxmDto) {
        //????????????
        if (null != dchyXmglChxmDto.getDchyXmglChgc()) {
            DataSecurityUtil.encryptSingleObject(dchyXmglChxmDto.getDchyXmglChgc());
            entityMapper.saveOrUpdate(dchyXmglChxmDto.getDchyXmglChgc(), dchyXmglChxmDto.getDchyXmglChgc().getChgcid());
        }

        //????????????
        if (null != dchyXmglChxmDto.getDchyXmglChxm()) {
            entityMapper.saveOrUpdate(dchyXmglChxmDto.getDchyXmglChxm(), dchyXmglChxmDto.getDchyXmglChxm().getChxmid());
        }

        //??????????????????
        List<DchyXmglChxmChdwxx> chdwmcList = dchyXmglChxmDto.getDchyXmglChxmChdwxxList();
        if (CollectionUtils.isNotEmpty(chdwmcList)) {
            for (DchyXmglChxmChdwxx chdwmcLists : chdwmcList) {
                String chdwxxid = chdwmcLists.getChdwxxid();
                if (StringUtils.indexOf(chdwxxid, DELETED) == 0) {
                    chdwxxid = StringUtils.substring(chdwxxid, DELETED.length());
                    chdwmcLists.setChdwxxid(chdwxxid);
                    entityMapper.deleteByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
                } else {
                    entityMapper.saveOrUpdate(chdwmcLists, chdwxxid);
                }
            }
        }

        //????????????
        List<DchyXmglChxmClsx> chxmClsxList = dchyXmglChxmDto.getDchyXmglChxmClsxList();
        if (CollectionUtils.isNotEmpty(chxmClsxList)) {
            for (DchyXmglChxmClsx chxmClsxLists : chxmClsxList) {
                String clsxid = chxmClsxLists.getClsxid();
                if (StringUtils.indexOf(clsxid, DELETED) == 0) {
                    clsxid = StringUtils.substring(clsxid, DELETED.length());
                    chxmClsxLists.setClsxid(clsxid);
                    entityMapper.deleteByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                } else {
                    entityMapper.saveOrUpdate(chxmClsxLists, clsxid);
                    if (StringUtils.isBlank(chxmClsxLists.getYjjfrq()) && (chxmClsxLists.getJcrq() == null)) {
                        dchyXmglChxmClsxMapper.updateClsxByClsxid(clsxid);
                    }
                }
            }
        }

        //???????????????????????????
        List<DchyXmglClsxChtl> dchyXmglClsxChtlLists = dchyXmglChxmDto.getDchyXmglClsxChtlList();
        if (CollectionUtils.isNotEmpty(dchyXmglClsxChtlLists)) {
            for (DchyXmglClsxChtl dchyXmglClsxChtl : dchyXmglClsxChtlLists) {
                String chtlid = dchyXmglClsxChtl.getChtlid();
                if (StringUtils.indexOf(chtlid, DELETED) == 0) {
                    chtlid = StringUtils.substring(chtlid, DELETED.length());
                    dchyXmglClsxChtl.setChtlid(chtlid);
                    entityMapper.deleteByPrimaryKey(DchyXmglClsxChtl.class, chtlid);
                } else {
                    entityMapper.saveOrUpdate(dchyXmglClsxChtl, chtlid);
                }
            }
        }

        //????????????
        List<DchyXmglHtxxDto> htxxDtoList = dchyXmglChxmDto.getDchyXmglHtxxDtoList();
        if (CollectionUtils.isNotEmpty(htxxDtoList)) {
            for (DchyXmglHtxxDto htxxDtoLists : htxxDtoList) {
                //????????????
                if (null != htxxDtoLists.getDchyXmglHtxx()) {
                    String htxxid = htxxDtoLists.getDchyXmglHtxx().getHtxxid();
                    entityMapper.saveOrUpdate(htxxDtoLists.getDchyXmglHtxx(), htxxid);
                    /*if (StringUtils.indexOf(htxxid, DELETED) == 0) {
                        htxxid = StringUtils.substring(htxxid, DELETED.length());
                        htxxDtoLists.getDchyXmglHtxx().setHtxxid(htxxid);
                        entityMapper.deleteByPrimaryKey(DchyXmglHtxx.class, htxxid);
                    } else {
                        entityMapper.saveOrUpdate(htxxDtoLists.getDchyXmglHtxx(), htxxid);
                    }*/
                }

                if (null != htxxDtoLists.getDchyXmglSjcl()) {
                    String sjclid = htxxDtoLists.getDchyXmglSjcl().getSjclid();
                    entityMapper.saveOrUpdate(htxxDtoLists.getDchyXmglSjcl(), sjclid);
                }

                //?????????????????????????????????
                List<DchyXmglClsxChdwxxGx> clsxChdwxxGx = htxxDtoLists.getDchyXmglClsxChdwxxGxList();
                if (CollectionUtils.isNotEmpty(clsxChdwxxGx)) {
                    for (DchyXmglClsxChdwxxGx clsxChdwxxGxs : clsxChdwxxGx) {
                        String gxid = clsxChdwxxGxs.getGxid();
                        if (StringUtils.indexOf(gxid, DELETED) == 0) {
                            gxid = StringUtils.substring(gxid, DELETED.length());
                            clsxChdwxxGxs.setGxid(gxid);
                            entityMapper.deleteByPrimaryKey(DchyXmglClsxChdwxxGx.class, gxid);
                        } else {
                            entityMapper.saveOrUpdate(clsxChdwxxGxs, gxid);
                        }
                    }
                }

                //?????????????????????????????????
                List<DchyXmglHtxxChdwxxGx> htxxChdwxxGx = htxxDtoLists.getDchyXmglHtxxChdwxxGxList();
                if (CollectionUtils.isNotEmpty(htxxChdwxxGx)) {
                    for (DchyXmglHtxxChdwxxGx htxxChdwxxGxs : htxxChdwxxGx) {
                        String gxid = htxxChdwxxGxs.getGxid();
                        if (StringUtils.indexOf(gxid, DELETED) == 0) {
                            gxid = StringUtils.substring(gxid, DELETED.length());
                            htxxChdwxxGxs.setGxid(gxid);
                            entityMapper.deleteByPrimaryKey(DchyXmglHtxxChdwxxGx.class, gxid);
                        } else {
                            entityMapper.saveOrUpdate(htxxChdwxxGxs, gxid);
                        }
                    }
                }

                //?????????????????????????????????
                List<DchyXmglClsxHtxxGx> clsxHtxxGx = htxxDtoLists.getDchyXmglClsxHtxxGxList();
                if (CollectionUtils.isNotEmpty(clsxHtxxGx)) {
                    for (DchyXmglClsxHtxxGx clsxHtxxGxs : clsxHtxxGx) {
                        String gxid = clsxHtxxGxs.getGxid();
                        if (StringUtils.indexOf(gxid, DELETED) == 0) {
                            gxid = StringUtils.substring(gxid, DELETED.length());
                            clsxHtxxGxs.setGxid(gxid);
                            entityMapper.deleteByPrimaryKey(DchyXmglClsxHtxxGx.class, gxid);
                        } else {
                            entityMapper.saveOrUpdate(clsxHtxxGxs, gxid);
                        }
                    }
                }
            }
        }

        //????????????
        List<DchyXmglSjcl> sjclList = dchyXmglChxmDto.getDchyXmglSjclList();
        if (CollectionUtils.isNotEmpty(sjclList)) {
            entityMapper.batchSaveSelective(sjclList);
        }

        return true;
    }

    @Override
    public Map<String, Object> queryChxxByChgcbh(Map<String, Object> map) {
        Map<String, Object> resultList = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chgcbh = CommonUtil.formatEmptyValue(data.get("gcbh"));
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("chgcbh", chgcbh);
        MapperParamUtil.formatRequiredStringParam(paramMap, "chgcbh");
        List<Map<String, Object>> chgcList = dchyXmglChxmMapper.queryChgcByChgcbh(paramMap);
        List<Map<String, Object>> baClsxList = queryBaClsxListByGcbh(chgcbh);
        resultList.put("baClsxList", baClsxList);
        DataSecurityUtil.decryptMapList(chgcList);
        List<Map<String, Object>> chxmList = dchyXmglChxmMapper.queryChxmByChgcbh(paramMap);
        if (CollectionUtils.isNotEmpty(chgcList)) {
            resultList.put("chgcxx", chgcList.get(0));
            resultList.put("chxmxx", chxmList);
        }
        return resultList;
    }

    @Override
    public Map<String, Object> queryChxmByXqfbbh(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String xqfbbh = CommonUtil.formatEmptyValue(data.get("xqfbbh"));
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("xqfbbh", xqfbbh);
        List<Map<String, Object>> chxmList = dchyXmglChxmMapper.queryChxmByXqfbbh(paramMap);
        Map<String, Object> resultMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(chxmList)) {
            String clsx = "";
            resultMap.putAll(chxmList.get(0));
            String chxmid = MapUtils.getString(resultMap, "CHXMID");
            Example example = new Example(DchyXmglChxmClsx.class);
            example.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmClsxList)) {
                List<String> clsxList = Lists.newArrayList();
                for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                    clsxList.add(dchyXmglChxmClsx.getClsx());
                }
                clsx = StringUtils.join(clsxList, Constants.DCHY_XMGL_CLSX_SEPARATOR);
            }
            resultMap.put("CLSX", clsx);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> queryBaxxByChxmid(Map<String, Object> map) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        if (StringUtils.isNotBlank(chxmid)) {
            Example exampleChxm = new Example(DchyXmglChxm.class);
            exampleChxm.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExample(exampleChxm);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                DchyXmglChxm dchyXmglChxm = dchyXmglChxmList.get(0);
                List<Map<String, Object>> baClsxList = queryBaClsxListByGcbh(dchyXmglChxm.getChgcbh());
                resultMap.put("baClsxList", baClsxList);
            }

        }
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);
        List<Map<String, Object>> baxxList = dchyXmglChxmMapper.queryBaxxByChxmid(paramMap);
        DataSecurityUtil.decryptMapList(baxxList);
        List<Map<String, Object>> clsxList = dchyXmglChxmMapper.queryClsxByChxmid(paramMap);
        List<Map<String, Object>> chdwList = dchyXmglChxmMapper.queryChdwByChxmid(paramMap);
        List<Map<String, Object>> chtlList = dchyXmglChxmMapper.queryClsxChtlByChxmid(paramMap);
        if(CollectionUtils.isNotEmpty(chtlList)){
            for(Map<String, Object> chtl:chtlList){
                String dw  = MapUtils.getString(chtl,"DW");
                if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_M)){
                    dw=Constants.CLSX_CHTL_MC_M;
                }else if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_PFM)){
                    dw=Constants.CLSX_CHTL_MC_PFM;
                }else if(StringUtils.isNotEmpty(dw) && StringUtils.equals(dw,Constants.CLSX_CHTL_DM_Z)){
                    dw=Constants.CLSX_CHTL_MC_Z;
                }
                chtl.put("DW",dw);
            }
        }

        resultMap.put("baxx", CollectionUtils.isNotEmpty(baxxList) ? baxxList.get(0) : Maps.newHashMap());
        resultMap.put("clsxList", clsxList);
        resultMap.put("chdwList", chdwList);
        resultMap.put("chtlList", chtlList);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> queryXcsldj(Map<String, Object> map) {
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        String chxmid = CommonUtil.formatEmptyValue(data.get("chxmid"));
        Map<String, Object> paramMap = new HashMap<>();
        map.put("chxmid", chxmid);
        List<Map<String, Object>> resultList = dchyXmglXcslMapper.getHtxxByChxmid(paramMap);
        if (CollectionUtils.isNotEmpty(resultList)) {

            for (Map<String, Object> resultLists : resultList) {
                List clsxList = new ArrayList();
                List chdwxxList = new ArrayList();
                String htxxid = MapUtils.getString(resultLists, "htxxid");
                Example clsxExample = new Example(DchyXmglClsxHtxxGx.class);
                clsxExample.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglClsxHtxxGx> clsxHtxxGx = entityMapper.selectByExample(clsxExample);
                if (CollectionUtils.isNotEmpty(clsxHtxxGx)) {
                    for (DchyXmglClsxHtxxGx clsxHtxxGxs : clsxHtxxGx) {
                        String clsx = clsxHtxxGxs.getClsxid();
                        clsxList.add(clsx);
                    }
                }
                Example chdwExample = new Example(DchyXmglHtxxChdwxxGx.class);
                chdwExample.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglHtxxChdwxxGx> htxxChdwxx = entityMapper.selectByExample(chdwExample);
                if (CollectionUtils.isNotEmpty(htxxChdwxx)) {
                    for (DchyXmglHtxxChdwxxGx htxxChdwxxs : htxxChdwxx) {
                        String clsx = htxxChdwxxs.getChdwxxid();
                        chdwxxList.add(clsx);
                    }
                }
                resultLists.put("CLSX", clsxList);
                resultLists.put("CHDWXX", chdwxxList);
            }
        }
        return resultList;
    }

    @Override
    @Transactional
    public synchronized Map<String, Object> deleteXcsldjByChxmid(Map<String, Object> paramMap) {
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        try {
            if (null != paramMap && paramMap.containsKey("data")) {
                Map<String, Object> data = MapUtils.getMap(paramMap, "data");
                String chxmid = MapUtils.getString(data, "chxmid");

                if (StringUtils.isNotBlank(chxmid)) {
                    DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                    if (null != dchyXmglChxm) {
                        if (StringUtils.isNoneBlank(dchyXmglChxm.getChgcbh(), dchyXmglChxm.getChgcid())) {
                            Example dchyXmglChxmExample = new Example(DchyXmglChxm.class);
                            dchyXmglChxmExample.createCriteria().andEqualTo("chgcbh", dchyXmglChxm.getChgcbh());
                            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(dchyXmglChxmExample);
                            if (CollectionUtils.size(dchyXmglChxmList) == 1 && StringUtils.isNotBlank(dchyXmglChxm.getChgcid())) {
                                // ????????????????????????????????????????????????????????????
                                entityMapper.deleteByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                            }
                        }
                        // ??????????????????
                        Example dchyXmglChxmClsxExample = new Example(DchyXmglChxmClsx.class);
                        dchyXmglChxmClsxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                        entityMapper.deleteByExampleNotNull(dchyXmglChxmClsxExample);

                        // ??????????????????
                        entityMapper.deleteByPrimaryKey(DchyXmglChxm.class, chxmid);

                        //??????????????????
                        Example htxxExample = new Example(DchyXmglHtxx.class);
                        htxxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                        entityMapper.deleteByExampleNotNull(htxxExample);

                        //??????????????????????????????
                        Example htxxClsxExample = new Example(DchyXmglClsxHtxxGx.class);
                        htxxClsxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                        entityMapper.deleteByExampleNotNull(htxxClsxExample);

                        //??????????????????????????????
                        Example htxxChdwxxExample = new Example(DchyXmglHtxxChdwxxGx.class);
                        htxxChdwxxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                        entityMapper.deleteByExampleNotNull(htxxChdwxxExample);

                        //????????????????????????????????????
                        Example clsxChdwxxExample = new Example(DchyXmglClsxChdwxxGx.class);
                        clsxChdwxxExample.createCriteria().andEqualTo("chxmid", dchyXmglChxm.getChxmid());
                        entityMapper.deleteByExampleNotNull(clsxChdwxxExample);

                        // ??????????????????
                        Example dchyXmglSjxxExample = new Example(DchyXmglSjxx.class);
                        dchyXmglSjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
                        List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(dchyXmglSjxxExample);
                        if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                            Example dchyXmglSjclExample = new Example(DchyXmglSjcl.class);
                            for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                                // ??????????????????
                                dchyXmglSjclExample.clear();
                                dchyXmglSjclExample.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                                entityMapper.deleteByExampleNotNull(dchyXmglSjclExample);

                                // todo ??????filecenter
                                // ??????????????????
                                entityMapper.deleteByPrimaryKey(DchyXmglSjxx.class, dchyXmglSjxx.getSjxxid());
                            }
                        }
                        /*fileCenter????????????*/
                        int node = platformUtil.creatNode(chxmid);
                        platformUtil.deleteNodeById(node);
                        code = ResponseMessage.CODE.SUCCESS.getCode();
                        msg = ResponseMessage.CODE.SUCCESS.getMsg();

                        Map<String, Object> idMaps = Maps.newHashMap();
                        idMaps.put("chxmid", chxmid);
                        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
                        dchyXmglChxmDto.setIdMaps(idMaps);
                        pushDataToMqService.pushSlxxMsgToMq(dchyXmglChxmDto);

                    } else {
                        code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                        msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
                    }
                } else {
                    code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                    msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
                }
            } else {
                code = ResponseMessage.CODE.DELETE_FAIL.getCode();
                msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
            }
        } catch (Exception e) {
            logger.error("????????????{}???", e);
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }


    /**
     * @param xqfbbh
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: xqfbbh
     * @time 2020/12/16 8:57
     * @description ????????????????????????????????????????????????
     */
    @Override
    public String getXschxmFbrByXqfbbh(String xqfbbh) {
        if (StringUtils.isNotBlank(xqfbbh)) {
            List xmlyList = Lists.newArrayList();
            xmlyList.add(Constants.XMLY_XSFB);
            xmlyList.add(Constants.XMLY_XSWT);
            Example example = new Example(DchyXmglChxm.class);
            example.createCriteria().andEqualTo("xqfbbh", xqfbbh).andIn("xmly", xmlyList);
            List<DchyXmglChxm> dchyXmglChxmList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglChxmList)) {
                return dchyXmglChxmList.get(0).getFbr();
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<Map<String, Object>> queryBaClsxListByGcbh(String gcbh) {
        Map<String, Object> paramMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(gcbh)) {
            paramMap.put("gcbh", gcbh);
        }
        return dchyXmglXcslMapper.queryBaClsxListByGcbh(paramMap);
    }

    @Override
    public String obtainSlbh() {
        String slbh = "";
        String slbhLsh = dchyXmglChxmMapper.queryMaxSlbh();
        if (slbhLsh.length() > 3) {
            slbh = slbhLsh.substring(slbhLsh.length() - 3, slbhLsh.length());
        } else {
            slbh = slbhLsh;
        }
        return slbh;
    }

    /**
     * @param paramMap
     * @return
     * @description 2021/5/8 ??????????????????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public ResponseMessage saveJsdw(Map<String, Object> paramMap) {
        ResponseMessage message = new ResponseMessage();
        String tyshxydm = CommonUtil.formatEmptyValue(paramMap.get("tyshxydm"));
        String jsdwm = CommonUtil.formatEmptyValue(paramMap.get("jsdwm"));
        String dwmc = CommonUtil.formatEmptyValue(paramMap.get("dwmc"));
        String lxr = CommonUtil.formatEmptyValue(paramMap.get("lxr"));
        String lxdh = CommonUtil.formatEmptyValue(paramMap.get("lxdh"));

        if (isExist("dwmc", dwmc)) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.DWMC_REPET.getMsg(), ResponseMessage.CODE.DWMC_REPET.getCode());
            return message;
        }

        if (isExist("tyshxydm", SM4Util.encryptData_ECB(tyshxydm))) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.TYSHXYDM_REPET.getMsg(), ResponseMessage.CODE.TYSHXYDM_REPET.getCode());
            return message;
        }

        if (isExist("jsdwm", jsdwm)) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.JSDWM_REPET.getMsg(), ResponseMessage.CODE.JSDWM_REPET.getCode());
            return message;
        }

        DchyXmglJsdw dchyXmglJsdw = new DchyXmglJsdw();

        dchyXmglJsdw.setJsdwid(UUIDGenerator.generate18());
        dchyXmglJsdw.setDwbh(UUIDGenerator.generate18());
        dchyXmglJsdw.setDwmc(dwmc);
        dchyXmglJsdw.setLxr(SM4Util.encryptData_ECB(lxr));
        dchyXmglJsdw.setLxdh(SM4Util.encryptData_ECB(lxdh));
        dchyXmglJsdw.setJsdwm(jsdwm);
        dchyXmglJsdw.setTyshxydm(SM4Util.encryptData_ECB(tyshxydm));
        dchyXmglJsdw.setLrr(UserUtil.getCurrentUser().getUsername());
        dchyXmglJsdw.setLrsj(CalendarUtil.getCurHMSDate());
        int flag = entityMapper.saveOrUpdate(dchyXmglJsdw, dchyXmglJsdw.getJsdwid());
        if (flag > 0) {
            message = ResponseUtil.wrapSuccessResponse();

            //????????????????????????????????????????????????
            DchyXmglJsdwlrDto dchyXmglJsdwlrDto = new DchyXmglJsdwlrDto();
            dchyXmglJsdwlrDto.setDchyXmglJsdw(dchyXmglJsdw);
            pushDataToMqService.pushJsdwlrMsgToMq(dchyXmglJsdwlrDto);
        } else {
            message = ResponseUtil.wrapExceptionResponse();
        }
        return message;
    }

    /**
     * @param key
     * @param value
     * @return
     * @description 2021/5/8 ??????????????????
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public boolean isExist(String key, String value) {
        if (StringUtils.isNoneBlank(key, value)) {
            Example exampleJsdw = new Example(DchyXmglJsdw.class);
            exampleJsdw.createCriteria().andEqualTo(key, value);
            List<DchyXmglJsdw> dchyXmglJsdwList = entityMapper.selectByExample(exampleJsdw);
            if (CollectionUtils.isNotEmpty(dchyXmglJsdwList)) {
                return true;
            }
        }
        return false;
    }
}
