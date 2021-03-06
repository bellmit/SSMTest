package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglSjclMapper;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglSjclpzMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglSjclService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 6k
 */
@Service
public class DchyXmglSjclServiceImpl implements DchyXmglSjclService {

    @Autowired
    private DchyXmglSjclMapper dchyXmglSjclMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    DchyXmglSjclService dchyXmglSjclService;
    @Autowired
    DchyXmglMlkService dchyXmglMlkService;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    DchyXmglZdService dchyXmglZdService;
    @Autowired
    DchyXmglSjclpzMapper dchyXmglSjclpzMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Map<String, Object>> querySjclList(Map<String, Object> map) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        List<Object> clsxList = (List<Object>) data.get("clsxList");
        String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
        String glsxId = CommonUtil.ternaryOperator(data.get("glsxid"));
        if (CollectionUtils.isEmpty(clsxList)) {
            resultList = querySjclTableByGlsxIdAndSsmkId(glsxId, ssmkId);
            return resultList;
        } else {
            List<Map<String, Object>> sjclpzList = querySjclpzList(clsxList, ssmkId);
            List<Map<String, Object>> sjclpzTmpList = Lists.newArrayList(sjclpzList);
            List<Map<String, Object>> sjclList;
            if (StringUtils.isNotBlank(glsxId) && StringUtils.isNotEmpty(glsxId) && StringUtils.isNotBlank(ssmkId) && StringUtils.isNotEmpty(ssmkId)) {
                sjclList = dchyXmglSjclMapper.querySjclBySsmkIdAndGlsxid(ssmkId, glsxId);
                int countSjcl = sjclList.size();
                ListIterator<Map<String, Object>> sjclIterator = sjclList.listIterator();
                ListIterator<Map<String, Object>> sjclpzIterator = sjclpzList.listIterator();
                while (sjclIterator.hasNext()) {
                    Map<String, Object> sjcl = sjclIterator.next();
                    if (null != sjcl.get("SJCLPZID") && !"".equals(sjcl.get("SJCLPZID").toString())) {
                        String sjclpzIdSjcl = sjcl.get("SJCLPZID").toString();
                        while (sjclpzIterator.hasNext()) {
                            Map<String, Object> sjclpz = sjclpzIterator.next();
                            String sjclpzIdStr = sjclpz.get("SJCLPZID").toString();
                            logger.info("sjclpzIdSjcl:  " + sjclpzIdSjcl + " == " + sjclpzIdStr + "   sjclpzIdStr");
                            if (sjclpzIdSjcl.equals(sjclpzIdStr)) {
                                sjclIterator.remove();
                                sjclpzIterator.remove();
                                if (sjclIterator.hasNext()) {
                                    sjcl = sjclIterator.next();
                                    if (null != sjcl.get("SJCLPZID")) {
                                        sjclpzIdSjcl = sjcl.get("SJCLPZID").toString();
                                    }
                                } else {
                                    sjcl = null;
                                    break;
                                }
                            }
                        }
                    }
                    if (null != sjcl && null != sjcl.get("SJCLID")) {
                        //????????????????????????????????????????????????????????????
                        String sjclId = sjcl.get("SJCLID").toString();
                        //???sjclpzId??????????????????
                        if (null != sjcl.get("SJCLPZID") && !containsInPz(sjcl.get("SJCLPZID"), sjclpzList)) {
                            entityMapper.deleteByPrimaryKey(DchyXmglSjcl.class, sjclId);
                        }
                        sjclIterator.remove();
                    }
                }
                if (sjclpzIterator.hasNext()) {
                    String sjxxId;
                    if (0 < countSjcl) {
                        sjxxId = getSjxxByGlsxId(glsxId, ssmkId);
                    } else {
                        //?????????????????????????????????
                        sjxxId = initSjxx(glsxId, ssmkId);
                    }
                    while (sjclpzIterator.hasNext()) {
                        Map<String, Object> sjclpz = sjclpzIterator.next();
                        //????????????????????????????????????
                        initSjcl(sjxxId, sjclpz);
                    }
                } else {
                    if (sjclpzIterator.hasPrevious()) {
                        //?????????????????????????????????
                        String sjxxId;
                        if (0 < countSjcl) {
                            sjxxId = getSjxxByGlsxId(glsxId, ssmkId);
                        } else {
                            //?????????????????????????????????
                            sjxxId = initSjxx(glsxId, ssmkId);
                        }
                        while (sjclpzIterator.hasPrevious()) {
                            Map<String, Object> sjclpz = sjclpzIterator.previous();
                            //????????????????????????????????????
                            initSjcl(sjxxId, sjclpz);
                        }
                    }
                }
                resultList = querySjclTable(sjclpzTmpList, glsxId);
            } else {
                return resultList;
            }
        }
        return resultList;
    }

    private boolean containsInPz(Object sjclpzid, List<Map<String, Object>> sjclpzList) {
        for (Map<String, Object> sjclpz : sjclpzList) {
            String sjclpzIdStr = sjclpz.get("SJCLPZID").toString();
            if (sjclpzid.toString().equals(sjclpzIdStr)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getSjclList(Map<String, Object> map) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            List<Object> clsxList = (List<Object>) data.get("clsxList");
            String ssmkId = CommonUtil.ternaryOperator(data.get("ssmkid"));
            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"));
            Map paramMap = Maps.newHashMap();
            paramMap.put("clsxList", clsxList);
            paramMap.put("ssmkId", ssmkId);
            paramMap.put("glsxid", glsxid);
            if (StringUtils.isNotBlank(glsxid) && CollectionUtils.isNotEmpty(clsxList)) {
                //??????clsx????????????????????????
                List<String> clsxParam = (List<String>) data.get("clsxList");
                Example exampleSjclpz = new Example(DchyXmglSjclpz.class);
                exampleSjclpz.createCriteria().andIn("ssclsx", clsxList).andEqualTo("ssmkid", ssmkId);
                List<DchyXmglSjclpz> dchyXmglSjclpzList = entityMapper.selectByExampleNotNull(exampleSjclpz);
                //?????????????????????????????????sjxxid,sjclid
                if (CollectionUtils.isNotEmpty(dchyXmglSjclpzList)) {
                    for (DchyXmglSjclpz dchyXmglSjclpz : dchyXmglSjclpzList) {
                        Map<String, Object> resultMap = Maps.newHashMap();
                        resultMap.put("SJCLPZID", dchyXmglSjclpz.getSjclpzid());
                        resultMap.put("SSMKID", dchyXmglSjclpz.getSsmkid());
                        resultMap.put("CLLX", dchyXmglSjclpz.getCllx());
                        resultMap.put("CLMC", dchyXmglSjclpz.getClmc());
                        resultMap.put("FS", BigDecimal.valueOf(dchyXmglSjclpz.getMrfs()));
                        resultMap.put("NEED", dchyXmglSjclpz.getNeed());


                    }
                }

                for (Iterator<Map<String, Object>> it = mapList.iterator(); it.hasNext(); ) {
                    Map<String, Object> maps = it.next();
                    String sjclid = MapUtils.getString(maps, "SJCLID");
                    if (org.apache.commons.lang3.StringUtils.isBlank(sjclid)) {
                        it.remove();
                    }

                    String clsxdm = MapUtils.getString(maps, "SSCLSX");
                    if (StringUtils.isNotBlank(clsxdm)) {
                        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", clsxdm);
                        if (dchyXmglZd != null) {
                            maps.put("SSCLSXMC", dchyXmglZd.getMc());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return mapList;
    }

    private Map<String, Object> initSjclxx(String glsxid, String ssmkId, String clsx, Map<String, Object> resultMap) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        //?????????????????????????????????clsxmc???????????????????????????????????????
        if (StringUtils.isNotEmpty(clsx)) {
            //Map<String, Object> sjclxxMap = Maps.newHashMap();
            if (StringUtils.isNotBlank(clsx)) {
                if (StringUtils.isNotBlank(MapUtils.getString(resultMap, "CLMC"))) {
                    resultMap.put("CLMC", MapUtils.getString(resultMap, "CLMC"));
                } else {
                    DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", clsx);
                    if (dchyXmglZd != null) {
                        resultMap.put("CLMC", dchyXmglZd.getMc());
                    }
                }
            }
            mapList.add(resultMap);
        }
        if (CollectionUtils.isNotEmpty(mapList)) {
            /*?????????????????????*/
            dchyXmglMlkService.initSjxxAndClxx(glsxid, ssmkId, mapList);
        }
        return resultMap;
    }

    /**
     * ????????????????????????????????????
     *
     * @param glsxid
     * @param ssmkId
     * @param mapList
     */
    @Override
    @Transactional
    public void initHtxxSjxxAndClxx(String glsxid, String ssmkId, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            /*????????????*/
            for (Map<String, Object> map : mapList) {
                String htxxid = UUIDGenerator.generate18();
                DchyXmglHtxx htxx = new DchyXmglHtxx();
                htxx.setHtxxid(htxxid);
                htxx.setChxmid(glsxid);
                entityMapper.saveOrUpdate(htxx, htxx.getHtxxid());

                String sjxxId = UUIDGenerator.generate18();
                Date sjsj = new Date();
                String sjr = UserUtil.getCurrentUserId();
                String tjr = "";
                DchyXmglSjxx sjxx = new DchyXmglSjxx();
                sjxx.setSjxxid(sjxxId);
                sjxx.setGlsxid(htxxid);
                sjxx.setSsmkid(ssmkId);
                sjxx.setSjsj(sjsj);
                sjxx.setTjr(tjr);
                sjxx.setSjr(sjr);
                entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());

                String sjclid = UUIDGenerator.generate18();
                map.put("SJCLID", sjclid);
                map.put("SJXXID", sjxxId);
                map.put("HTXXID", htxxid);
                int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                String mlmc = (String) map.get("CLMC");
                DchyXmglSjcl sjcl = new DchyXmglSjcl();
                sjcl.setSjclid(sjclid);
                sjcl.setSjxxid(sjxxId);
                sjcl.setClmc(mlmc);
                sjcl.setCllx(CommonUtil.formatEmptyValue(map.get("CLLX")));
                sjcl.setFs(fs);
                sjcl.setClrq(new Date());
                sjcl.setYs(1);
                entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            }
        }
    }

    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public Map<String, Object> saveSjclpz(Map<String, Object> map) {
        String code = ResponseMessage.CODE.OPERA_FAIL.getCode();
        String msg = ResponseMessage.CODE.OPERA_FAIL.getMsg();
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String clmc = CommonUtil.formatEmptyValue(data.get("clmc"));
            String cllx = CommonUtil.formatEmptyValue(data.get("cllx"));
            String sjxxId = CommonUtil.formatEmptyValue(data.get("sjxxid"));
            String mrfs = CommonUtil.formatEmptyValue(data.get("fs"));
            List<String> ssclsxList = (List<String>) data.get("ssclsxlist");
            String ssclsxStr = String.join(",", ssclsxList);
            DchyXmglSjcl sjcl = new DchyXmglSjcl();
            sjcl.setSjclid(UUIDGenerator.generate18());
            sjcl.setSjxxid(sjxxId);
            sjcl.setClmc(clmc);
            sjcl.setFs(Integer.parseInt(mrfs));
            sjcl.setCllx(cllx);
            sjcl.setClsx(ssclsxStr);
            sjcl.setClrq(new Date());
            int result = entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
            if (result > 0) {
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
            }
        } catch (Exception e) {
            logger.error("errorMsg:", e);
            code = ResponseMessage.CODE.OPERA_FAIL.getCode();
            msg = ResponseMessage.CODE.OPERA_FAIL.getMsg();
        }
        Map resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * ????????????????????????
     */
    @Override
    @Transactional
    public Map<String, Object> deleteSjclpz(Map<String, Object> map) {
        String code = ResponseMessage.CODE.DELETE_FAIL.getCode();
        String msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        try {
            Map<String, Object> data = (Map<String, Object>) map.get("data");
            String sjclpzid = CommonUtil.formatEmptyValue(data.get("sjclpzid"));
            if (StringUtils.isNotBlank(sjclpzid)) {
                int result = entityMapper.deleteByPrimaryKey(DchyXmglSjclpz.class, sjclpzid);
                if (result > 0) {
                    code = ResponseMessage.CODE.SUCCESS.getCode();
                    msg = ResponseMessage.CODE.SUCCESS.getMsg();
                }
            }
        } catch (Exception e) {
            logger.error("errorMsg:", e);
            code = ResponseMessage.CODE.DELETE_FAIL.getCode();
            msg = ResponseMessage.CODE.DELETE_FAIL.getMsg();
        }
        Map resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    @Override
    @Transactional
    public ResponseMessage updateSjxxAndSjcl4Xsbf(String htxxid, String folderId, String ssmkid, String glsxid) {
        ResponseMessage message = new ResponseMessage();
        try {
            /*??????????????????*/
            DchyXmglHtxx xmglHtxx = entityMapperXSBF.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
            Optional<DchyXmglHtxx> htxxOptional = Optional.ofNullable(xmglHtxx);
            if (htxxOptional.isPresent()) {
                /*??????wjzxid*/
                xmglHtxx.setWjzxid(folderId);
                entityMapperXSBF.saveOrUpdate(xmglHtxx, xmglHtxx.getHtxxid());
                /*??????sjxx???sjcl*/
                Example sjxxExampel = new Example(DchyXmglSjxx.class);
                sjxxExampel.createCriteria().andEqualTo("glsxid", htxxid).andEqualTo("ssmkid", ssmkid);
                List<DchyXmglSjxx> sjxxList = entityMapperXSBF.selectByExample(sjxxExampel);
                List<Object> sjxxidList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(sjxxList)) {
                    for (DchyXmglSjxx sjxx : sjxxList) {
                        sjxx.setSjr(UserUtil.getCurrentUserId());//?????????
                        sjxx.setSjsj(new Date());//????????????
                        entityMapperXSBF.saveOrUpdate(sjxx, sjxx.getSjxxid());
                        sjxxidList.add(sjxx.getSjxxid());
                    }
                }
                if (CollectionUtils.isNotEmpty(sjxxidList)) {
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andIn("sjxxid", sjxxidList);
                    List<DchyXmglSjcl> sjclList = entityMapperXSBF.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        sjclList.forEach(sjcl -> {
                            sjcl.setClrq(new Date());
                            sjcl.setWjzxid(folderId);//??????wjzxid
                            entityMapperXSBF.saveOrUpdate(sjcl, sjcl.getSjclid());
                        });
                    }
                }

                /*???????????????,????????????????????????wjzxid??????????????????wjzxid??????*/
                DchyXmglHtxx htxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
                Optional<DchyXmglHtxx> optional = Optional.ofNullable(htxx);
                if (optional.isPresent()) {
                    DchyXmglHtxx dchyXmglHtxx = optional.get();
                    dchyXmglHtxx.setWjzxid(folderId);
                    entityMapper.saveOrUpdate(dchyXmglHtxx, dchyXmglHtxx.getHtxxid());
                    /*??????sjxx???sjcl*/
                    Example xsSjxxExampel = new Example(DchyXmglSjxx.class);
                    xsSjxxExampel.createCriteria().andEqualTo("glsxid", htxxid).andEqualTo("ssmkid", ssmkid);
                    List<DchyXmglSjxx> xsSjxxList = entityMapper.selectByExample(xsSjxxExampel);
                    List<Object> xsSjxxidList = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(xsSjxxList)) {
                        for (DchyXmglSjxx sjxx : xsSjxxList) {
                            sjxx.setSjr(UserUtil.getCurrentUserId());//?????????
                            sjxx.setSjsj(new Date());//????????????
                            entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
                            xsSjxxidList.add(sjxx.getSjxxid());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(xsSjxxidList)) {
                        Example xsSjclExample = new Example(DchyXmglSjcl.class);
                        xsSjclExample.createCriteria().andIn("sjxxid", xsSjxxidList);
                        List<DchyXmglSjcl> xsSjclList = entityMapper.selectByExample(xsSjclExample);
                        if (CollectionUtils.isNotEmpty(xsSjclList)) {
                            xsSjclList.forEach(sjcl -> {
                                sjcl.setClrq(new Date());
                                sjcl.setWjzxid(folderId);//??????wjzxid
                                entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                            });
                        }
                    }
                }
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", folderId);
            }
        } catch (Exception e) {
            logger.error("?????????????????????????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    private List<Map<String, Object>> querySjclTableByGlsxIdAndSsmkId(String glsxId, String ssmkId) {
        List<Map<String, Object>> resultList = dchyXmglSjclMapper.querySjclsByGlsxIdAndSsmkId(glsxId, ssmkId);
        resultList.forEach(row -> {
            if (null != row.get("CLSX")) {
                String clsxStr = row.get("CLSX").toString();
                String[] clsxIdArg = clsxStr.split(",");
                List<Object> clsxObjList = Lists.newArrayList(clsxIdArg);
                List<Map<String, Object>> clsxList = dchyXmglSjclMapper.queryZdByInDm(clsxObjList);
                row.put("CLSXS", clsxList);
            } else {
                row.put("CLSXS", null);
            }
            String userName = row.get("SJR").toString();
            //?????????????????????,???????????????
            //String userName = UserUtil.getUserNameById(row.get("SJR").toString());
            row.put("SJRMC", (null == userName) ? "" : userName);
        });
        return resultList;
    }

    private List<Map<String, Object>> querySjclTable(List<Map<String, Object>> sjclpzList, String glsxId) {
        List<Map<String, Object>> resultList = Lists.newArrayList();
        List<Map<String, Object>> rowTap = dchyXmglSjclMapper.querySjclBySjxxId(glsxId);
        rowTap.forEach(row -> {
            if (MapUtils.isNotEmpty(row)) {
                if (null != row.get("SJCLPZID")) {
                    List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(row.get("SJCLPZID").toString());
                    if (CollectionUtils.isNotEmpty(ssclsxList)) {
                        row.put("CLSXLIST", ssclsxList);
                    } else {
                        row.put("CLSXLIST", null);
                    }
                } else {
                    List<Object> clsxList = Lists.newArrayList(CommonUtil.formatEmptyValue(MapUtils.getString(row, "CLSX")).split(","));
                    List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.queryZdByInDm(clsxList);
                    if (CollectionUtils.isNotEmpty(ssclsxList)) {
                        row.put("CLSXLIST", ssclsxList);
                    } else {
                        row.put("CLSXLIST", null);
                    }
                }
                //????????????????????????
                String userName = CommonUtil.formatEmptyValue(MapUtils.getString(row, "SJR"));
                //?????????????????????,???????????????
                //String userName = UserUtil.getUserNameById(row.get("SJR").toString());
                row.put("SJCLPZCLMC", null);
                row.put("SJRMC", userName);
                row.put("NEED", null);
                row.put("CLLXID", null);
                row.put("MRFS", null);
                resultList.add(row);
            }
        });
        sjclpzList.forEach(sjclpz -> {
            //????????????????????????
            String sjclpzId = sjclpz.get("SJCLPZID").toString();
            List<Map<String, Object>> rows = dchyXmglSjclMapper.querySjclBySjclpzIdAndSjxxId(sjclpzId, glsxId);
            rows.forEach(row -> {
                if (MapUtils.isNotEmpty(row)) {
                    if (null != row.get("SJCLPZID")) {
                        List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(row.get("SJCLPZID").toString());
                        if (CollectionUtils.isNotEmpty(ssclsxList)) {
                            row.put("CLSXLIST", ssclsxList);
                        } else {
                            row.put("CLSXLIST", null);
                        }
                    } else {
                        List<Object> clsxList = Lists.newArrayList(CommonUtil.formatEmptyValue(MapUtils.getString(row, "CLSX")).split(","));
                        List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.queryZdByInDm(clsxList);
                        if (CollectionUtils.isNotEmpty(ssclsxList)) {
                            row.put("CLSXLIST", ssclsxList);
                        } else {
                            row.put("CLSXLIST", null);
                        }
                    }
                    //????????????????????????
                    String sjclpzClmc = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLMC"));
                    String userName = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "SJR"));
                    String needStr = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "NEED"));
                    String cllxStr = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLLXID"));
                    String mrfs = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "MRFS"));
                    //?????????????????????,???????????????
                    row.put("SJCLPZCLMC", sjclpzClmc);
                    row.put("SJRMC", userName);
                    row.put("NEED", needStr);
                    row.put("CLLXID", cllxStr);
                    row.put("MRFS", mrfs);
                    resultList.add(row);
                }
            });
        });
        return resultList;
    }

    private String getSjxxByGlsxId(String glsxId, String ssmkid) {
        String sjxxid = UUIDGenerator.generate18();
        Example exampleSjxx = new Example(DchyXmglSjxx.class);
        exampleSjxx.createCriteria().andEqualTo("glsxid", glsxId).andEqualTo("ssmkid", ssmkid);
        List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
        if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
            sjxxid = dchyXmglSjxxList.get(0).getSjxxid();
        }
        return sjxxid;
    }

    private void initSjcl(String sjxxId, Map<String, Object> sjclpz) {
//        if (existSjcl(sjxxId, sjclpz.get("SJCLPZID").toString())) {
        if (existSjcl(sjxxId, CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "SJCLPZID")))) {
        } else {
            String sjclId = UUIDGenerator.generate18();
            Integer fs = Integer.parseInt(CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "MRFS")));
            String cllx = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLLXID"));
            Integer ys = Constants.DCHY_XMGL_MRYS;
            Integer xh = Integer.parseInt(CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "XH")));
            String sjclpzId = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "SJCLPZID"));
            String ssclsxId = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "clsxStr"));
            String clmcStr = CommonUtil.formatEmptyValue(MapUtils.getString(sjclpz, "CLMC"));

            DchyXmglSjcl dchyXmglSjcl = new DchyXmglSjcl();
            dchyXmglSjcl.setSjclid(sjclId);
            dchyXmglSjcl.setSjxxid(sjxxId);
            dchyXmglSjcl.setFs(fs);
            dchyXmglSjcl.setCllx(cllx);
            dchyXmglSjcl.setYs(ys);
            dchyXmglSjcl.setXh(xh);
            dchyXmglSjcl.setSjclpzid(sjclpzId);
            dchyXmglSjcl.setClsx(ssclsxId);
            dchyXmglSjcl.setClmc(clmcStr);
            entityMapper.saveOrUpdate(dchyXmglSjcl, dchyXmglSjcl.getSjclid());
        }
    }

    private boolean existSjcl(String sjxxId, String sjclpzId) {
        int a = dchyXmglSjclMapper.countSjclBySjxxIdAndSjclpzId(sjxxId, sjclpzId);
        return (a > 0);
    }

    private String initSjxx(String glsxId, String ssmkId) {
        String uuid = UUIDGenerator.generate18();
        Date sjsj = new Date();
        String sjr = UserUtil.getCurrentUserId();
        String tjr = "";
        DchyXmglSjxx sjxx = new DchyXmglSjxx();
        sjxx.setSjxxid(uuid);
        sjxx.setGlsxid(glsxId);
        sjxx.setSsmkid(ssmkId);
        sjxx.setSjsj(sjsj);
        sjxx.setTjr(tjr);
        sjxx.setSjr(sjr);
        entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
        return uuid;
    }

    private List<Map<String, Object>> querySjclpzList(List<Object> clsxList, String ssmkId) {
        logger.info("*******************querySjclpzList?????????clsxList:" + JSON.toJSONString(clsxList));
        logger.info("*******************querySjclpzList?????????ssmkId:" + ssmkId);
        List<String> sjclpzIdList = dchyXmglSjclpzMapper.querySjclpzIdsBySsclsxIdIn(clsxList);
        List<Map<String, Object>> sjclpzList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(sjclpzIdList)) {
            sjclpzIdList.forEach(sjclpzId -> {
                Map<String, Object> sjclpzMap = dchyXmglSjclMapper.querySjclpzBySjclpzIdAndSsmkId(sjclpzId, ssmkId);
                List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.querySsclsxsBySjclpzId(sjclpzId);
                logger.info("*******************sjclpzMap:" + JSON.toJSONString(sjclpzMap));
                logger.info("*******************sjclpzMap:" + JSON.toJSONString(ssclsxList));
                if (CollectionUtils.isNotEmpty(ssclsxList) && MapUtils.isNotEmpty(sjclpzMap)) {
                    sjclpzMap.put("clsx", ssclsxList);
                    StringBuilder ssclsxStr = new StringBuilder();
                    for (int i = 0; i < ssclsxList.size(); i++) {
                        Map<String, Object> ssclsx = ssclsxList.get(i);
                        if (i < (ssclsxList.size() - 1)) {
                            ssclsxStr.append(ssclsx.get("DM")).append(",");
                        } else {
                            ssclsxStr.append(ssclsx.get("DM"));
                        }
                    }
                    sjclpzMap.put("clsxStr", ssclsxStr.toString());
                } else {
                    sjclpzMap.put("clsx", null);
                    sjclpzMap.put("clsxStr", null);
                }
                sjclpzList.add(sjclpzMap);
            });
        }
        return sjclpzList;
    }

    //endregion

}
