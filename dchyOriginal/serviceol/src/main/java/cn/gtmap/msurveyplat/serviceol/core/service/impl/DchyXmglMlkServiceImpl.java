package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.annotion.AuditLog;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.common.vo.PfUserRoleRel;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglGldwMapper;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglSjclMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.UploadService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.core.xsbfmapper.DchyXmglMlkXSBFMapper;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 16:37
 * @description
 */
@Service
public class DchyXmglMlkServiceImpl implements DchyXmglMlkService, UploadService {

    @Autowired
    private DchyXmglMlkMapper mlkMapper;
    @Autowired
    private DchyXmglMlkXSBFMapper dchyXmglMlkXSBFMapper;
    @Autowired
    private DchyXmglGldwMapper dchyXmglGldwMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private EntityMapper entityMapper;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    private Repository repository;
    @Autowired
    private PlatformUtil platformUtil;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    private Logger logger = Logger.getLogger(DchyXmglMlkServiceImpl.class);
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private DchyXmglSjclMapper dchyXmglSjclMapper;

    @Override
    public List<Map<String, Object>> getAllCyrysList() {
        return mlkMapper.getAllCyrysList();
    }

    @Override
    public List<Map<String, Object>> queryCyryByMlkId(String mlkid) {
        return mlkMapper.queryCyryByMlkIdByPage(mlkid);
    }

    @Override
    public List<Map<String, Object>> queryUploadFileBySsmkId(String ssmkid) {
        return mlkMapper.queryUploadFileBySsmkId(ssmkid);
    }

    /**
     * @param userid
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @time 2020/12/3 19:48
     * @description ????????????id?????????????????????
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> queryMlkXxZtByUserid(String userid) {
        Map<String, Object> mlkzt = Maps.newHashMap();
        String sfyx = "???";//????????????
        String sfdj = "???";//????????????
        String mlkid = "";
        Date ycsj = null;
        String ycr = "";
        String ycyy = "";
        String sfyc = "???";//????????????
        String sfshz = "???";//???????????????
        DchyXmglMlk mlk = this.getMlkByUserId(userid);
        if (null != mlk) {
            ycsj = mlk.getYcsj();
            ycr = mlk.getYcr();
            ycyy = mlk.getYcyy();
            if (StringUtils.equals("0", mlk.getSfyx()) && null != mlk.getYcsj()) {
                sfyc = "???";
            }
            if (StringUtils.equals("1", mlk.getSfdj()) && null != mlk.getYcsj()) {
                sfdj = "???";
            }
            Example sqxxExample = new Example(DchyXmglSqxx.class);
            sqxxExample.createCriteria().andEqualTo("glsxid", mlk.getMlkid()).andEqualTo("blsx", Constants.BLSX_MLKZX);
            List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
            if (CollectionUtils.isNotEmpty(sqxxList)) {
                String sqxxid = sqxxList.get(0).getSqxxid();
                mlkzt.put("blsx", sqxxList.get(0).getBlsx());
                Example dbExample = new Example(DchyXmglDbrw.class);
                dbExample.createCriteria().andEqualTo("sqxxid", sqxxid);
                List<DchyXmglDbrw> dbrwList = entityMapper.selectByExample(dbExample);
                if (CollectionUtils.isNotEmpty(dbrwList)) {
                    sfshz = "???";
                }
            }

        }
        if (StringUtils.isNotBlank(userid)) {
            List<Map<String, Object>> yxqkMapList = mlkMapper.getMlkYxztByUserid(userid);
            if (CollectionUtils.isNotEmpty(yxqkMapList)) {
                sfyx = "???";
                mlkid = MapUtils.getString(yxqkMapList.get(0), "MLKID");
            } else {
                List<Map<String, Object>> sqxxByUserid = mlkMapper.getMlkSqztByUserid(userid);
                String sqxxid = "";
                if (CollectionUtils.isNotEmpty(sqxxByUserid)) {
                    Map<String, Object> sqxxMap = sqxxByUserid.get(0);
                    if (StringUtils.equalsIgnoreCase(Constants.SHI, MapUtils.getString(sqxxMap, "SFTH"))) {
                        // ????????????
                        sqxxid = MapUtils.getString(sqxxMap, "SQXXID");
                        List<Map<String, Object>> blyjList = dchyXmglGldwMapper.getZxBlyjBySqxxid(sqxxid);
                        if (CollectionUtils.isNotEmpty(blyjList)) {
                            mlkzt.put("thyj", MapUtils.getString(blyjList.get(0), "BLYJ"));
                        }
                    }
                    mlkzt.put("blsx", MapUtils.getString(sqxxMap, "BLSX"));
                    mlkzt.put("sqztmc", MapUtils.getString(sqxxMap, "SQZTMC"));
                    mlkzt.put("sfbrsq", MapUtils.getString(sqxxMap, "SFBRSQ"));
                    mlkzt.put("sfth", MapUtils.getString(sqxxMap, "SFTH"));
                    mlkzt.put("sfytj", MapUtils.getString(sqxxMap, "SFYTJ"));
                    mlkzt.put("sqxxid", MapUtils.getString(sqxxMap, "SQXXID"));
                    if (StringUtils.equalsIgnoreCase(Constants.SHI, MapUtils.getString(sqxxMap, "SFBRSQ"))) {
                        PfUserVo pfUserVo = sysUserService.getUserVo(MapUtils.getString(sqxxMap, "SQR"));
                        mlkzt.put("sqrmc", pfUserVo != null ? pfUserVo.getUserName() : "");
                    } else {
                        UserInfo userInfo = UserUtil.getCurrentUser();
                        mlkzt.put("sqrmc", userInfo != null ? userInfo.getUsername() : "");
                    }
                    mlkid = MapUtils.getString(sqxxMap, "MLKID");
                }
            }
        }
        mlkzt.put("sfshz", sfshz);
        mlkzt.put("sfyx", sfyx);
        mlkzt.put("sfdj", sfdj);
        mlkzt.put("mlkid", mlkid);
        mlkzt.put("ycsj", ycsj);
        mlkzt.put("ycr", ycr);
        mlkzt.put("ycyy", ycyy);
        mlkzt.put("sfyc", sfyc);
        return mlkzt;
    }

    @Override
    public List<Map<String, Object>> getSjclXx(String mlkid, String ssmkid) {
        return mlkMapper.getSjclXx(mlkid, ssmkid);
    }

    @Override
    public List<Map<String, Object>> getSjclXx2(String mlkid, String ssmkid) {
        return mlkMapper.getSjclXx2(mlkid, ssmkid);
    }

    @Override
    public List<Map<String, Object>> getSjclXxFromSxbf(String mlkid, String ssmkid) {
        return dchyXmglMlkXSBFMapper.getSjclXxFromSxbf(mlkid, ssmkid);
    }

    @Override
    public List<Map<String, Object>> getSjclXxByGlsxid(String glsxid) {
        if (StringUtils.equals(AppConfig.getProperty(Constants.DCHY_XMGL_ZDBA_PZ), "true")) {
            List<Map<String, Object>> xxByGlsxid = mlkMapper.getSjclXxByGlsxid(glsxid);
            if (CollectionUtils.isEmpty(xxByGlsxid)) {
                return dchyXmglMlkXSBFMapper.getSjclXxByGlsxid(glsxid);
            } else {
                return xxByGlsxid;
            }
        } else {
            return dchyXmglMlkXSBFMapper.getSjclXxByGlsxid(glsxid);
        }
    }

    @Override
    public List<Map<String, Object>> getSjclByGlsxId(String glsxid) {
        List<Map<String, Object>> xxByGlsxid = null;
        if (StringUtils.equals(AppConfig.getProperty(Constants.DCHY_XMGL_ZDBA_PZ), "true")) {
            xxByGlsxid = querySjclXxByGlsxId(glsxid, false);
            if (CollectionUtils.isEmpty(xxByGlsxid)) {
                xxByGlsxid = dchyXmglMlkXSBFMapper.getSjclXxByGlsxid(glsxid);
            }
        } else {
            xxByGlsxid = querySjclXxByGlsxId(glsxid, true);
        }

        if (CollectionUtils.isNotEmpty(xxByGlsxid)) {
            xxByGlsxid.forEach(row -> {
                if (null != row.get("CLSX")) {
                    String clsxStr = row.get("CLSX").toString();
                    String[] clsxIdArg = clsxStr.split(",");
                    List<Object> clsxObjList = Lists.newArrayList(clsxIdArg);
                    List<Map<String, Object>> clsxList = dchyXmglSjclMapper.queryZdByInDm(clsxObjList);
                    row.put("CLSXS", clsxList);
                } else {
                    row.put("CLSXS", null);
                }

            });
        }
        return xxByGlsxid;
    }


    /**
     * ?????????????????????
     *
     * @param data
     * @return
     */
    @Override
    @Transactional
    public boolean mlkLogoutAudit(Map<String, Object> data) {
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        String shjg = CommonUtil.ternaryOperator(data.get("shjg"));//????????????
        String bayj = CommonUtil.ternaryOperator(data.get("shyj"));//????????????
        try {
            if (StringUtils.isNotBlank(mlkid) && StringUtils.isNotBlank(shjg)) {
                DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
                Optional<DchyXmglMlk> mlkOptional = Optional.ofNullable(mlk);
                if (StringUtils.equals(shjg, Constants.DCHY_XMGL_SQZT_SHTG)) {//??????
                    if (mlkOptional.isPresent()) {
                        mlk.setSfyx(Constants.INVALID);//0:??????
                        mlk.setSfdj(Constants.VALID);//1:??????
                        entityMapper.saveOrUpdate(mlk, mlk.getMlkid());
                        /*??????sqxx*/
                        Example sqxxExample = new Example(DchyXmglSqxx.class);
                        sqxxExample.createCriteria().andEqualTo("glsxid", mlk.getMlkid());
                        List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            DchyXmglSqxx sqxx = sqxxList.get(0);
                            Optional<DchyXmglSqxx> sqxxOptional = Optional.ofNullable(sqxx);
                            if (sqxxOptional.isPresent()) {
                                String sqxxid = sqxx.getSqxxid();//????????????id
                                /*??????????????????*/
                                DchyXmglDbrw dbrw = null;
                                List<DchyXmglDbrw> dbrws = mlkMapper.getDbrwBySqxxid(sqxxid);
                                if (CollectionUtils.isNotEmpty(dbrws)) {
                                    logger.info("???????????????????????????" + dbrws);
                                    dbrw = dbrws.get(0);
                                }
                                Optional<DchyXmglDbrw> dbrwOptional = Optional.ofNullable(dbrw);
                                if (dbrwOptional.isPresent()) {
                                    /*??????????????????*/
                                    DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                                    dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());//????????????id  -> mlkid
                                    dchyXmglYbrw.setBlyj(bayj);//????????????  --> ????????????
                                    dchyXmglYbrw.setJssj(new Date());//????????????
                                    dchyXmglYbrw.setSqxxid(dbrw.getSqxxid());//????????????id
                                    dchyXmglYbrw.setBljg(Constants.DCHY_XMGL_SQZT_SHTG);//????????????
                                    dchyXmglYbrw.setDqjd(dbrw.getDqjd());//????????????
                                    dchyXmglYbrw.setZrsj(dbrw.getZrsj());//????????????
                                    dchyXmglYbrw.setYbrwid(dbrw.getDbrwid());//????????????id
                                    int i = entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                                    if (i > 0) {
                                        /*????????????*/
                                        entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dbrw.getDbrwid());
                                    }
                                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, dbrw.getSqxxid());
                                    /*??????????????????????????????*/
                                    if (null != dchyXmglSqxx) {
                                        /*98: ????????????*/
                                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_SHTG);
                                        entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                                    }
                                    DchyXmglMlkDto dchyXmglMlkDto = new DchyXmglMlkDto();
                                    List<DchyXmglMlk> mlkList = new ArrayList<>();
                                    mlkList.add(mlk);
                                    dchyXmglMlkDto.setDchyXmglMlkList(mlkList);
                                    try {
                                        /*mlk??????????????????????????????*/
                                        pushDataToMqService.pushMlkDtoToMq(dchyXmglMlkDto);
                                    } catch (Exception e) {
                                        logger.error("????????????????????????????????????{}", e);
                                    }
                                }
                            }
                        }
                        return true;
                    }
                } else if (StringUtils.equals(shjg, Constants.DCHY_XMGL_SQZT_TH)) {//??????
                    if (mlkOptional.isPresent()) {
                        mlk.setYcdw("");
                        mlk.setYcsj(null);
                        mlk.setYcyy("");
                        mlk.setYcr("");
                        mlk.setSfdj("0");
                        mlk.setSfyx("1");
                        entityMapper.saveOrUpdate(mlk, mlk.getMlkid());
                        /*??????sqxx*/
                        Example sqxxExample = new Example(DchyXmglSqxx.class);
                        sqxxExample.createCriteria().andEqualTo("glsxid", mlk.getMlkid());
                        List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            DchyXmglSqxx sqxx = sqxxList.get(0);
                            Optional<DchyXmglSqxx> sqxxOptional = Optional.ofNullable(sqxx);
                            if (sqxxOptional.isPresent()) {
                                String sqxxid = sqxx.getSqxxid();//????????????id
                                /*??????????????????*/
                                DchyXmglDbrw dbrw = null;
                                List<DchyXmglDbrw> dbrws = mlkMapper.getDbrwBySqxxid(sqxxid);
                                if (CollectionUtils.isNotEmpty(dbrws)) {
                                    logger.info("???????????????????????????" + dbrws);
                                    dbrw = dbrws.get(0);
                                }
                                Optional<DchyXmglDbrw> dbrwOptional = Optional.ofNullable(dbrw);
                                if (dbrwOptional.isPresent()) {
                                    /*??????????????????*/
                                    DchyXmglYbrw dchyXmglYbrw = new DchyXmglYbrw();
                                    dchyXmglYbrw.setBlryid(UserUtil.getCurrentUserId());//????????????id
                                    dchyXmglYbrw.setBlyj(bayj);//????????????  --> ????????????
                                    dchyXmglYbrw.setJssj(new Date());//????????????
                                    dchyXmglYbrw.setSqxxid(dbrw.getSqxxid());//????????????id
                                    dchyXmglYbrw.setBljg(Constants.DCHY_XMGL_SQZT_TH);//????????????
                                    dchyXmglYbrw.setDqjd(dbrw.getDqjd());//????????????
                                    dchyXmglYbrw.setZrsj(dbrw.getZrsj());//????????????
                                    dchyXmglYbrw.setYbrwid(dbrw.getDbrwid());//????????????id
                                    entityMapper.saveOrUpdate(dchyXmglYbrw, dchyXmglYbrw.getYbrwid());
                                    /*????????????*/
                                    entityMapper.deleteByPrimaryKey(DchyXmglDbrw.class, dbrw.getDbrwid());
                                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, dbrw.getSqxxid());
                                    /*??????????????????????????????*/
                                    if (null != dchyXmglSqxx) {
                                        /*97: ????????????*/
                                        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_TH);
                                        entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
                                    }
                                    DchyXmglMlkDto dchyXmglMlkDto = new DchyXmglMlkDto();
                                    List<DchyXmglMlk> mlkList = new ArrayList<>();
                                    mlkList.add(mlk);
                                    dchyXmglMlkDto.setDchyXmglMlkList(mlkList);
                                    try {
                                        /*mlk??????????????????????????????*/
                                        pushDataToMqService.pushMlkDtoToMq(dchyXmglMlkDto);
                                    } catch (Exception e) {
                                        logger.error("????????????????????????????????????{}", e);
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("?????????????????????????????????:{}", e);
        }
        return false;
    }

    @Override
    public String getYcyyByMlkid(String mlkid) {
        if (StringUtils.isNotBlank(mlkid)) {
            DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            Optional<DchyXmglMlk> mlkOptional = Optional.ofNullable(mlk);
            if (mlkOptional.isPresent()) {
                return mlk.getYcyy();
            }
        }
        return "";
    }

    private List<Map<String, Object>> querySjclXxByGlsxId(String glsxid, boolean onlineBackDbBool) {
        List<Map<String, Object>> resultList;
        if (onlineBackDbBool) {
            resultList = dchyXmglMlkXSBFMapper.getSjclXxByGlsxid(glsxid);
        } else {
            resultList = mlkMapper.getSjclXxByGlsxid(glsxid);
        }
        resultList.forEach(row -> {
            if (null != row.get("CLSX")) {
                List<Object> clsxList = Lists.newArrayList(row.get("CLSX").toString().split(","));
                List<Map<String, Object>> ssclsxList = dchyXmglSjclMapper.queryZdByInDm(clsxList);
                if (CollectionUtils.isNotEmpty(ssclsxList)) {
                    row.put("CLSXLIST", ssclsxList);
                } else {
                    row.put("CLSXLIST", Lists.newArrayList());
                }
            } else {
                row.put("CLSXLIST", Lists.newArrayList());
            }
        });
        return resultList;
    }

    /**
     * ???????????????????????????
     *
     * @param mlkid
     * @return
     */
    @Override
    public List<Map<String, Object>> getXsbfSjclXx(String mlkid) {
        return dchyXmglMlkXSBFMapper.getSjclXx(mlkid);
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void initSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(mapList)) {
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", glsxId).andEqualTo("ssmkid", ssmkId);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                DchyXmglSjxx sjxx = sjxxList.get(0);
                sjxx.setSsmkid(ssmkId);
                sjxx.setSjsj(new Date());
                sjxx.setTjr("");
                sjxx.setSjr(UserUtil.getCurrentUserId());
                entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
                for (Map<String, Object> map : mapList) {
                    String sjclid = UUIDGenerator.generate18();
                    if (StringUtils.isNotEmpty((String) map.get("SJCLID"))) {
                        sjclid = (String) map.get("SJCLID");
                    }
                    map.put("SJCLID", sjclid);
                    map.put("SJXXID", sjxx.getSjxxid());
                    int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                    String mlmc = (String) map.get("CLMC");
                    DchyXmglSjcl sjcl = new DchyXmglSjcl();
                    sjcl.setSjclid(sjclid);
                    sjcl.setSjxxid(sjxx.getSjxxid());
                    sjcl.setClmc(mlmc);
                    sjcl.setFs(fs);
                    sjcl.setCllx("1");
                    sjcl.setClrq(new Date());
                    sjcl.setYs(1);
                    entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                }
            } else {
                /*????????????*/
                String sjxxId = UUIDGenerator.generate18();
                Date sjsj = new Date();
                String sjr = UserUtil.getCurrentUserId();
                String tjr = "";
                DchyXmglSjxx sjxx = new DchyXmglSjxx();
                sjxx.setSjxxid(sjxxId);
                sjxx.setGlsxid(glsxId);
                sjxx.setSsmkid(ssmkId);
                sjxx.setSjsj(sjsj);
                sjxx.setTjr(tjr);
                sjxx.setSjr(sjr);
                entityMapper.saveOrUpdate(sjxx, sjxx.getSjxxid());
                for (Map<String, Object> map : mapList) {
                    String sjclid = UUIDGenerator.generate18();
                    map.put("SJCLID", sjclid);
                    map.put("SJXXID", sjxxId);
                    int fs = map.containsKey("FS") ? (((BigDecimal) map.get("FS")).intValue()) : 1;
                    String mlmc = (String) map.get("CLMC");
                    DchyXmglSjcl sjcl = new DchyXmglSjcl();
                    sjcl.setSjclid(sjclid);
                    sjcl.setSjxxid(sjxxId);
                    sjcl.setClmc(mlmc);
                    sjcl.setFs(fs);
                    sjcl.setCllx("1");
                    sjcl.setClrq(new Date());
                    sjcl.setYs(1);
                    entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getMlkXxById(String mlkId) {
        return mlkMapper.getMlkXxById(mlkId);
    }

    /**
     * ???????????????
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @SystemLog(czmkMc = ProLog.CZMC_MLK_LOGOUT_CODE, czmkCode = ProLog.CZMC_MLK_LOGOUT_MC, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public void logoutMlk(Map<String, Object> data) {
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        String ycyy = CommonUtil.ternaryOperator(data.get("ycyy"));//????????????
        String sqxxId = "";
        try {
            DchyXmglMlk xmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != xmglMlk) {
                Example sqxxExample = new Example(DchyXmglSqxx.class);
                sqxxExample.createCriteria().andEqualTo("glsxid", xmglMlk.getMlkid());
                List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                if (CollectionUtils.isNotEmpty(sqxxList)) {
                    DchyXmglSqxx xmglSqxx = sqxxList.get(0);
                    if (null != xmglSqxx) {
                        xmglSqxx.setBlsx("6");//6:???????????????
                        entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
                        sqxxId = xmglSqxx.getSqxxid();
                    }
                } else {
                    /*?????????????????????*/
                    DchyXmglSqxx xmglSqxx = new DchyXmglSqxx();
                    String sqxxid = UUIDGenerator.generate18();
                    xmglSqxx.setSqxxid(sqxxid);
                    xmglSqxx.setSqjgmc(xmglMlk.getDwmc());
                    xmglSqxx.setBlsx(Constants.BLSX_MLKZX);//6:???????????????
                    xmglSqxx.setSqr(UserUtil.getCurrentUserId());
                    xmglSqxx.setSqsj(new Date());
                    xmglSqxx.setSqzt(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_SHZ);//1:?????????
                    xmglSqxx.setGlsxid(xmglMlk.getMlkid());
                    xmglSqxx.setSqbh(Calendar.getInstance().getTimeInMillis() + sqxxid);
                    xmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
                    entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
                    sqxxId = xmglSqxx.getSqxxid();
                }
                Object obj = new Object();
                synchronized (obj) {
                    if (StringUtils.isNotBlank(sqxxId)) {
                        /*??????????????????*/
                        DchyXmglDbrw xmglDbrw = new DchyXmglDbrw();
                        xmglDbrw.setDbrwid(UUIDGenerator.generate18());
                        xmglDbrw.setSqxxid(sqxxId);
                        xmglDbrw.setZrsj(new Date());
                        xmglDbrw.setBlryid(UserUtil.getCurrentUserId());//????????????id
                        xmglDbrw.setBljsid(queryRoleidByUserid(UserUtil.getCurrentUserId()));//????????????id
                        xmglDbrw.setZrryid(UserUtil.getCurrentUserId());//????????????id
                        xmglDbrw.setDqjd(Constants.DQJD_SH);
                        entityMapper.saveOrUpdate(xmglDbrw, xmglDbrw.getDbrwid());
                        logger.info("????????????????????????????????????: " + xmglDbrw);
                    }
                }
                xmglMlk.setYcdw(xmglMlk.getDwmc());
                xmglMlk.setYcsj(new Date());
                xmglMlk.setYcyy(ycyy);
                xmglMlk.setYcr(UserUtil.getCurrentUserId());
                entityMapper.saveOrUpdate(xmglMlk, xmglMlk.getMlkid());
                DchyXmglMlkDto dchyXmglMlkDto = new DchyXmglMlkDto();
                List<DchyXmglMlk> mlkList = new ArrayList<>();
                mlkList.add(xmglMlk);
                dchyXmglMlkDto.setDchyXmglMlkList(mlkList);
                try {
                    /*mlk??????????????????????????????*/
                    pushDataToMqService.pushMlkDtoToMq(dchyXmglMlkDto);
                } catch (Exception e) {
                    logger.error("????????????????????????????????????{}", e);
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
    }

    @SystemLog(czmkMc = ProLog.CZMC_ZXKF_MC, czmkCode = ProLog.CZMC_ZXKF_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_ZXKF_CODE)
    private String queryRoleidByUserid(String userid) {
        String roleid = "";
        if (StringUtils.isNotBlank(userid)) {
            Example exampleRole = new Example(PfUserRoleRel.class);
            exampleRole.createCriteria().andEqualTo("user_id", userid);
            List<PfUserRoleRel> pfUserRoleRels = entityMapper.selectByExample(exampleRole);
            if (CollectionUtils.isNotEmpty(pfUserRoleRels)) {
                roleid = pfUserRoleRels.get(0).getRole_id();
            }
        }
        return roleid;
    }

    /**
     * ?????????????????????(???????????????????????????????????????????????????)
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    @SystemLog(czmkMc = ProLog.CZMC_MLK_ALTER_CODE, czmkCode = ProLog.CZMC_MLK_ALTER_MC, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE)
    public DchyXmglMlkDto alterMlkInfo(Map<String, Object> data) {
        DchyXmglMlkDto dchyXmglMlkDto = new DchyXmglMlkDto();
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        if (StringUtils.isNotBlank(mlkid)) {
            DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != mlk && StringUtils.equals(Constants.VALID, mlk.getSfyx())) {
                //1:??????
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String frdb = CommonUtil.ternaryOperator(data.get("frdb"));
                //????????????
                String clsxs = CommonUtil.ternaryOperator(data.get("clsx"));//????????????
                //????????????????????????
                String chzzzsbh = CommonUtil.ternaryOperator(data.get("chzzzsbh"));
                //????????????
                Date yxqksrq = null;//??????????????????
                //????????????
                Date yxqjsrq = null;//??????????????????
                try {
                    yxqksrq = sdf.parse(CommonUtil.ternaryOperator(data.get("yxqksrq")));
                    yxqjsrq = sdf.parse(CommonUtil.ternaryOperator(data.get("yxqjsrq")));
                } catch (ParseException e) {
                    logger.error("????????????{}", e);
                }
                String dwxz = CommonUtil.ternaryOperator(data.get("dwxz"));//????????????
                String bgdz = CommonUtil.ternaryOperator(data.get("bgdz"));//????????????
                String chzyfw = CommonUtil.ternaryOperator(data.get("chzyfw"));//??????????????????
                String lxr = CommonUtil.ternaryOperator(data.get("lxr"));//?????????
                String lxdh = CommonUtil.ternaryOperator(data.get("lxdh"));//????????????
                String zzdj = CommonUtil.ternaryOperator(data.get("zzdj"));//????????????
                String mlktp = CommonUtil.ternaryOperator(data.get("mlktp"));//???????????????

                if (StringUtils.isNotBlank(mlktp)) {
                    mlk.setMlktp(mlktp.getBytes());
                }
                mlk.setFrdb(frdb);
                mlk.setChzzzsbh(chzzzsbh);
                mlk.setYxqksrq(yxqksrq);
                mlk.setYxqjsrq(yxqjsrq);
                mlk.setDwxz(dwxz);
                mlk.setBgdz(bgdz);
                mlk.setChzyfw(chzyfw);
                mlk.setLxr(SM4Util.encryptData_ECB(lxr));
                mlk.setLxdh(SM4Util.encryptData_ECB(lxdh));
                mlk.setZzdj(zzdj);
                List<DchyXmglMlk> mlkList = new ArrayList<>();
                mlkList.add(mlk);
                dchyXmglMlkDto.setGlsxid(mlk.getMlkid());
                dchyXmglMlkDto.setDchyXmglMlkList(mlkList);
                if (StringUtils.isNotBlank(clsxs)) {
                    List<DchyXmglMlkClsxGx> mlkClsxGxList = new ArrayList<>();
                    /*??????????????????*/
                    String[] clsxArray = clsxs.split(",");
                    for (String clsx : clsxArray) {
                        DchyXmglMlkClsxGx mlkClsxGx = new DchyXmglMlkClsxGx();
                        mlkClsxGx.setGxid(UUIDGenerator.generate18());
                        mlkClsxGx.setMlkid(mlk.getMlkid());
                        mlkClsxGx.setClsx(clsx);
                        mlkClsxGxList.add(mlkClsxGx);
                    }
                    dchyXmglMlkDto.setDchyXmglMlkClsxGxList(mlkClsxGxList);

                    Example example = new Example(DchyXmglMlkClsxGx.class);
                    example.createCriteria().andEqualTo("mlkid", mlk.getMlkid());
                    List<DchyXmglMlkClsxGx> oldMlkClsxGxList = entityMapper.selectByExample(example);
                    if (CollectionUtils.isNotEmpty(oldMlkClsxGxList) && oldMlkClsxGxList.size() > mlkClsxGxList.size()) {
                        Map<String, String> deletedMlkClsx = this.getDeletedMlkClsx(oldMlkClsxGxList, mlkClsxGxList);
                        if (MapUtils.isNotEmpty(deletedMlkClsx)) {
                            /*???????????????delete_*/
                            for (Map.Entry<String, String> entry : deletedMlkClsx.entrySet()) {
                                String gxid = entry.getValue();
                                DchyXmglMlkClsxGx mlkClsxGx = entityMapper.selectByPrimaryKey(DchyXmglMlkClsxGx.class, gxid);
                                if (null != mlkClsxGx) {
                                    mlkClsxGx.setGxid("delete_" + mlkClsxGx.getGxid());
                                    mlkClsxGxList.add(mlkClsxGx);
                                }
                            }
                        }
                    }
                }
                return dchyXmglMlkDto;
            }
        }
        return null;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param oldMlkClsxs
     * @param newMlkClsxs
     * @return
     */
    private Map<String, String> getDeletedMlkClsx(List<DchyXmglMlkClsxGx> oldMlkClsxs, List<DchyXmglMlkClsxGx> newMlkClsxs) {
        Map<String, String> newClsx = new HashMap<>();
        Map<String, String> oldClsx = new HashMap<>();
        for (DchyXmglMlkClsxGx newMlkClsx : newMlkClsxs) {
            newClsx.put(newMlkClsx.getClsx(), newMlkClsx.getGxid());
        }
        for (DchyXmglMlkClsxGx oldMlkClsx : oldMlkClsxs) {
            oldClsx.put(oldMlkClsx.getClsx(), oldMlkClsx.getGxid());
        }
        if (MapUtils.isNotEmpty(newClsx) && MapUtils.isNotEmpty(oldClsx)) {
            for (Map.Entry<String, String> clsx : newClsx.entrySet()) {
                for (Map.Entry<String, String> old : oldClsx.entrySet()) {
                    if (StringUtils.equals(old.getKey(), clsx.getKey())) {
                        oldClsx.remove(old.getKey());
                        break;
                    }
                }
            }
        }
        return oldClsx;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @AuditLog(czlxCode = ProLog.CZLX_SAVE_CODE, czlxMc = ProLog.CZLX_SAVE_MC, clazz = DchyXmglMlkDto.class, czmkCode = ProLog.CZMC_MLK_ALTER_CODE, czmkMc = ProLog.CZMC_MLK_ALTER_MC)
    public boolean saveChangeAfterMlkxx(DchyXmglMlkDto mlkDto) {
        List<DchyXmglMlk> xmglMlkList = mlkDto.getDchyXmglMlkList();
        if (CollectionUtils.isNotEmpty(xmglMlkList)) {
            for (DchyXmglMlk mlk : xmglMlkList) {
                entityMapper.saveOrUpdate(mlk, mlk.getMlkid());
                /*????????????????????????????????????????????????*/
                Example mlkClsxGxExample = new Example(DchyXmglMlkClsxGx.class);
                mlkClsxGxExample.createCriteria().andEqualTo("mlkid", mlk.getMlkid());
                entityMapper.deleteByExampleNotNull(mlkClsxGxExample);

                /*???????????????????????????*/
                List<DchyXmglMlkClsxGx> xmglMlkClsxGxList = mlkDto.getDchyXmglMlkClsxGxList();
                if (CollectionUtils.isNotEmpty(xmglMlkClsxGxList)) {
                    for (DchyXmglMlkClsxGx mlkClsxGx : xmglMlkClsxGxList) {
                        String gxid = mlkClsxGx.getGxid();
                        if (StringUtils.isNotBlank(gxid) && gxid.indexOf("delete_") == -1) {
                            entityMapper.saveOrUpdate(mlkClsxGx, mlkClsxGx.getGxid());
                        }
                    }
                }

            }
        }

        List<DchyXmglCyry> xmglCyryList = mlkDto.getDchyXmglCyryList();
        if (CollectionUtils.isNotEmpty(xmglCyryList)) {
            for (DchyXmglCyry xmglCyry : xmglCyryList) {
                entityMapper.saveOrUpdate(xmglCyry, xmglCyry.getCyryid());
            }
        }
        /*????????????????????????????????????????????????*/
        pushDataToMqService.pushMlkDtoToMq(mlkDto);
        return true;
    }

    @Override
    @SystemLog(czmkMc = ProLog.CZMC_MLK_LOGOUT_CODE, czmkCode = ProLog.CZMC_MLK_LOGOUT_MC, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE)
    public boolean checkBeforeLogoutMlk(Map<String, Object> data) {
        boolean flag = true;
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        try {
            Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
            chdwExample.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglChxmChdwxx> chxmChdwxxList = entityMapperXSBF.selectByExample(chdwExample);
            if (CollectionUtils.isNotEmpty(chxmChdwxxList)) {
                for (DchyXmglChxmChdwxx chdwxx : chxmChdwxxList) {
                    String chxmid = chdwxx.getChxmid();
                    if (StringUtils.isNotBlank(chxmid)) {
                        DchyXmglChxm chxm = entityMapperXSBF.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                        /*????????????????????????????????????????????????*/
                        if (null != chxm && StringUtils.isNotBlank(chxm.getXmzt()) && !StringUtils.equals("99", chxm.getXmzt())) {
                            //??????????????????
                            flag = false;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return flag;
    }

    @Override
    public DchyXmglSqxx initSqxx(Map<String, Object> data) {
        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
        /*??????sqxxid*/
        String sqxxid = UUIDGenerator.generate18();
        String blsx = CommonUtil.ternaryOperator(data.get("blsx"));
        /*?????????????????????*/
        if (StringUtils.isBlank(blsx)) {
            return null;
        }
        String sqr = UserUtil.getCurrentUserId();
        String sqzt = "1";//1:?????????
        /*??????glsxid ??? mlkid*/
        String glsxid = UUIDGenerator.generate18();
        String sqbh = Calendar.getInstance().getTimeInMillis() + sqxxid;
        Date sqsj = new Date();
        dchyXmglSqxx.setSqxxid(sqxxid);
        //????????????id???dchy_xmgl_yhdw?????????dwmc??????
        dchyXmglSqxx.setSqjgmc(getDwmc());
        dchyXmglSqxx.setBlsx(blsx);
        dchyXmglSqxx.setSqr(sqr);
        dchyXmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
        dchyXmglSqxx.setSqrmc(sqr);
        dchyXmglSqxx.setSqsj(sqsj);
        dchyXmglSqxx.setSqzt(sqzt);
        dchyXmglSqxx.setGlsxid(glsxid);
        dchyXmglSqxx.setSqbh(sqbh);
        int result = entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        if (result > 0) {
            return dchyXmglSqxx;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean initMlk(Map<String, Object> data) {
        DchyXmglMlk mlk = new DchyXmglMlk();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        String mlktp = CommonUtil.ternaryOperator(data.get("mlktp"));
        String dwmc = CommonUtil.ternaryOperator(data.get("dwmc"));
        String frdb = CommonUtil.ternaryOperator(data.get("frdb"));
        String clsxs = CommonUtil.ternaryOperator(data.get("clsx"));//????????????
        //????????????????????????
        String chzzzsbh = CommonUtil.ternaryOperator(data.get("chzzzsbh"));
        //????????????
        Date yxqksrq = null;
        //????????????
        Date yxqjsrq = null;
        try {
            yxqksrq = sdf.parse(CommonUtil.ternaryOperator(data.get("yxqksrq")));
            yxqjsrq = sdf.parse(CommonUtil.ternaryOperator(data.get("yxqjsrq")));
        } catch (ParseException e) {
            logger.error("????????????:{}", e);
        }
        //????????????????????????
        String tyshxydm = CommonUtil.ternaryOperator(data.get("tyshxydm"));
        String dwxz = CommonUtil.ternaryOperator(data.get("dwxz"));
        String bgdz = CommonUtil.ternaryOperator(data.get("bgdz"));
        //??????????????????
        String chzyfw = CommonUtil.ternaryOperator(data.get("chzyfw"));
        String lxr = CommonUtil.ternaryOperator(data.get("lxr"));
        String lxdh = CommonUtil.ternaryOperator(data.get("lxdh"));
        String zzdj = CommonUtil.ternaryOperator(data.get("zzdj"));
        String sqxxId = CommonUtil.ternaryOperator(data.get("sqxxid"));
        mlk.setMlkid(mlkId);
        mlk.setDwmc(dwmc);
        mlk.setFrdb(frdb);
        mlk.setChzzzsbh(chzzzsbh);
        mlk.setSfyx("0");//0?????????
        mlk.setYxqksrq(yxqksrq);
        mlk.setYxqjsrq(yxqjsrq);
        mlk.setTyshxydm(tyshxydm);
        mlk.setDwxz(dwxz);
        mlk.setBgdz(bgdz);
        mlk.setChzyfw(chzyfw);
        mlk.setLxr(lxr);
        mlk.setLxdh(lxdh);
        mlk.setSqr(UserUtil.getCurrentUserId());
        mlk.setZzdj(zzdj);
        if (StringUtils.isNotBlank(mlktp)) {
            mlk.setMlktp(mlktp.getBytes());
        }
        //????????????????????????????????????
        mlk.setDwbh(getDwbh());
        int result = 0;
        synchronized (this) {
            DataSecurityUtil.encryptSingleObject(mlk);
            result = entityMapper.saveOrUpdate(mlk, mlk.getMlkid());
        }
        if (result > 0 && StringUtils.isNotBlank(sqxxId)) {

            //??????????????????
            DchyXmglSqxx xmglSqxx = null;
            try {
                xmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxId);
            } catch (Exception e) {
                logger.error("????????????:{}", e);
            }
            if (null != xmglSqxx) {
                xmglSqxx.setGlsxid(mlk.getMlkid());
                entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
            }
            /*????????????????????????????????????*/
            List<Map<String, Object>> uploadList = (List<Map<String, Object>>) data.get("uploadList");
            if (CollectionUtils.isNotEmpty(uploadList)) {
                for (Map<String, Object> map : uploadList) {
                    String sjclid = CommonUtil.ternaryOperator(MapUtils.getString(map, "SJCLID"));
                    String fs = CommonUtil.ternaryOperator(MapUtils.getString(map, "FS"));
                    String ys = CommonUtil.ternaryOperator(MapUtils.getString(map, "YS"));
                    String cllx = CommonUtil.ternaryOperator(MapUtils.getString(map, "CLLX"));
                    DchyXmglSjcl sjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclid);
                    if (null != sjcl) {
                        sjcl.setFs(Integer.parseInt(fs));
                        sjcl.setYs(Integer.parseInt(ys));
                        sjcl.setCllx(cllx);
                        entityMapper.saveOrUpdate(sjcl, sjcl.getSjclid());
                    } else {
                        DchyXmglSjcl xmglSjcl = new DchyXmglSjcl();
                        xmglSjcl.setSjclid(sjclid);
                        xmglSjcl.setSjxxid(MapUtils.getString(map, "SJXXID"));
                        xmglSjcl.setClmc(MapUtils.getString(map, "CLMC"));
                        xmglSjcl.setCllx(MapUtils.getString(map, "CLLX"));
                        xmglSjcl.setWjzxid(MapUtils.getString(map, "WJZXID"));
                        xmglSjcl.setClrq(new Date());
                        xmglSjcl.setXh(1);
                        xmglSjcl.setFs(Integer.parseInt(fs));
                        xmglSjcl.setYs(Integer.parseInt(ys));
                        entityMapper.saveOrUpdate(xmglSjcl, xmglSjcl.getSjclid());
                    }
                }
            }
            if (StringUtils.isNotBlank(clsxs)) {
                /*????????????????????????????????????????????????*/
                Example mlkClsxGxExample = new Example(DchyXmglMlkClsxGx.class);
                mlkClsxGxExample.createCriteria().andEqualTo("mlkid", mlkId);
                entityMapper.deleteByExampleNotNull(mlkClsxGxExample);
                /*??????????????????*/
                String[] clsxArray = clsxs.split(",");
                for (String clsx : clsxArray) {
                    DchyXmglMlkClsxGx mlkClsxGx = new DchyXmglMlkClsxGx();
                    mlkClsxGx.setGxid(UUIDGenerator.generate18());
                    mlkClsxGx.setMlkid(mlk.getMlkid());
                    mlkClsxGx.setClsx(clsx);
                    entityMapper.saveOrUpdate(mlkClsxGx, mlkClsxGx.getGxid());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseMessage delFile(String sjclId, String wjzxId) {
        ResponseMessage message = new ResponseMessage();
        int i = 0;
        try {
            platformUtil.deleteNodeById(Integer.parseInt(wjzxId));
            /*??????sjclid?????????wjzxid??????*/
            DchyXmglSjcl xmglSjcl = entityMapper.selectByPrimaryKey(DchyXmglSjcl.class, sjclId);
            if (null != xmglSjcl) {
                xmglSjcl.setWjzxid("");
                i = entityMapper.saveOrUpdate(xmglSjcl, xmglSjcl.getSjclid());
            }
            if (i > 0) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("wjzxid", wjzxId);
            }
        } catch (Exception e) {
            logger.error("???????????????{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    @Override
    public Map<String, Object> getInitMlkParam() {
        String userId = UserUtil.getCurrentUserId();
        Map<String, Object> hashMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(userId)) {
            Example yhdwExample = new Example(DchyXmglYhdw.class);
            yhdwExample.createCriteria().andEqualTo("yhid", userId);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                DchyXmglYhdw yhdw = yhdwList.get(0);
                if (null != yhdw) {
                    hashMap.put("DWMC", yhdw.getDwmc());
                    hashMap.put("TYSHXYDM", SM4Util.decryptData_ECB(yhdw.getTyshxydm()));
                }
            }
        }
        return hashMap;
    }

    /**
     * ????????????
     *
     * @param data
     * @return
     */
    @Override
    public Map<String, Object> viewattachments(Map<String, Object> data) {
        Map<String, Object> returnMap = new LinkedHashMap<>();
        String ssmkid = "";
        String sjxxid = "";
        String glsxid = CommonUtil.ternaryOperator(MapUtils.getString(data, "glsxid"));
        //"4CI912518D6JT202";
        String mlkWjzxid = "";
        String sjr = "";
        Date sjsj = null;
        List<Map<String, Object>> resultList = new LinkedList<>();
        Example sjxxExample = new Example(DchyXmglSjxx.class);
        sjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
        /*????????????????????????*/
        List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
        if (CollectionUtils.isNotEmpty(sjxxList)) {
            DchyXmglSjxx sjxx = sjxxList.get(0);
            if (null != sjxx) {
                ssmkid = sjxx.getSsmkid();
                sjxxid = sjxx.getSjxxid();
                sjr = sjxx.getSjr();
                sjsj = sjxx.getSjsj();
            }
        }
        /*?????????????????????*/
        Example mlkExample = new Example(DchyXmglMlk.class);
        mlkExample.createCriteria().andEqualTo("mlkid", glsxid);
        List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
        if (CollectionUtils.isNotEmpty(mlkList)) {
            DchyXmglMlk mlk = mlkList.get(0);
            if (null != mlk) {
                mlkWjzxid = mlk.getWjzxid() != null ? mlk.getWjzxid() : "";
            }
        }
        if (StringUtils.isNotBlank(ssmkid) && StringUtils.isNotBlank(sjxxid)) {
            Map<String, Object> cyryMap = new LinkedHashMap<>();
            Map<String, Object> mlkMap = new LinkedHashMap<>();
            Example cyryExample = new Example(DchyXmglCyry.class);
            cyryExample.createCriteria().andEqualTo("mlkid", glsxid);
            List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
            String cyryWjzxids = "";
            if (CollectionUtils.isNotEmpty(cyryList)) {
                for (DchyXmglCyry xmglCyry : cyryList) {
                    cyryWjzxids += xmglCyry.getWjzxid() + "-";
                }
            }
            if (StringUtils.equals(SsmkidEnum.MLKRZ.getCode(), ssmkid)) {//?????????
                cyryMap.put("title", "??????????????????");
                cyryMap.put("wjzxid", cyryWjzxids);
                cyryMap.put("ssmkid", "2");
                cyryMap.put("type", "");
                cyryMap.put("children", this.getCyryByGlsxid(glsxid));//??????????????????
                mlkMap.put("title", "????????????");
                mlkMap.put("wjzxid", mlkWjzxid);
                mlkMap.put("ssmkid", "1");
                mlkMap.put("type", "");
                mlkMap.put("children", this.getUploadClxx(mlkWjzxid));//?????????????????????????????????
            }
            resultList.add(cyryMap);
            resultList.add(mlkMap);
            returnMap.put("lists", resultList);
            returnMap.put("sjr", UserUtil.getUserNameById(sjr));
            returnMap.put("sjsj", sjsj);

        }
        return returnMap;
    }

    /**
     * ??????glsxid???????????????????????????
     *
     * @param glsxid
     * @return
     */
    private List<Map<String, Object>> getCyryByGlsxid(String glsxid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        /*????????????????????????*/
        Example cyryExample = new Example(DchyXmglCyry.class);
        cyryExample.createCriteria().andEqualTo("mlkid", glsxid);
        List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
        if (CollectionUtils.isNotEmpty(cyryList)) {
            for (DchyXmglCyry xmglCyry : cyryList) {
                Map<String, Object> cyryMap = new LinkedHashMap<>();
                String ryxm = xmglCyry.getRyxm();
                String wjzxid = xmglCyry.getWjzxid();
                String cyryid = xmglCyry.getCyryid();
                cyryMap.put("title", ryxm);
                cyryMap.put("wjzxid", wjzxid);
                cyryMap.put("cyryid", cyryid);
                cyryMap.put("type", "");
                cyryMap.put("children", this.getUploadClxx(wjzxid));//????????????????????????????????????????????????????????????
                resultList.add(cyryMap);
            }
        }
        return resultList;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @return
     */
    private List<Map<String, Object>> getUploadClxx(String wjzxid) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        try {
            if (StringUtils.isNotBlank(wjzxid)) {
                List<Node> nodeList = getNodeService().getChildNodes(Integer.parseInt(wjzxid));
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node node : nodeList) {
                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                        uploadFileMap.put("title", node.getName());
                        uploadFileMap.put("wjzxid", node.getId());
                        uploadFileMap.put("type", node.getType());
                        uploadFileMap.put("children", this.getUploadFiles(wjzxid, node.getId()));//?????????????????????????????????
                        resultList.add(uploadFileMap);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}", e);
        }
        return resultList;
    }

    /**
     * ????????????????????????
     *
     * @param wjzxid
     * @return
     */
    private List<Map<String, Object>> getUploadFiles(String wjzxid, int parentNodeId) {
        List<Map<String, Object>> resultList = new LinkedList<>();
        int nodeId = Integer.parseInt(wjzxid);
        int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
        try {
            if (1 == num) {
                Node node = getNodeService().getNode(nodeId);
                if (node != null) {
                    if (node.getType() == 1) {//1???????????????  0:??????
                        Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                        // ??????
                        uploadFileMap.put("title", node.getName());
                        uploadFileMap.put("type", node.getType());
                        uploadFileMap.put("wjzxid", node.getId());
                        resultList.add(uploadFileMap);
                    } else {
                        List<Node> nodeList = getNodeService().getAllChildNodes(nodeId);
                        if (CollectionUtils.isNotEmpty(nodeList)) {
                            for (Node nodeTemp : nodeList) {
                                if (nodeTemp.getType() == 1) {
                                    Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                                    uploadFileMap.put("title", nodeTemp.getName());
                                    uploadFileMap.put("type", nodeTemp.getType());
                                    uploadFileMap.put("wjzxid", nodeTemp.getId());
                                    resultList.add(uploadFileMap);
                                }
                            }
                        }
                    }
                }
            } else {
                List<Node> nodeList = getNodeService().getAllChildNodes(nodeId);
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node nodeTemp : nodeList) {
                        if (nodeTemp.getType() == 1 && (nodeTemp.getParentId().intValue() == parentNodeId)) {
                            Map<String, Object> uploadFileMap = new LinkedHashMap<>();
                            uploadFileMap.put("title", nodeTemp.getName());
                            uploadFileMap.put("type", nodeTemp.getType());
                            uploadFileMap.put("wjzxid", nodeTemp.getId());
                            resultList.add(uploadFileMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("????????????{}", e);
        }
        return resultList;
    }


    @Override
    public List<Map<String, Object>> getchdwbymlkid(String mlkId) {
        int count = 0;
        List<Map<String, Object>> mlkXx = null;
        /*??????mlkid??????sqxxid*/
        Example sqxx = new Example(DchyXmglSqxx.class);
        List sqztList = Lists.newArrayList();
        sqztList.add(Constants.DCHY_XMGL_SQZT_YGD);//?????????
        sqxx.createCriteria().andEqualTo("glsxid", mlkId).andEqualTo("blsx", 1).andNotIn("sqzt", sqztList);
        List<DchyXmglSqxx> objects = entityMapper.selectByExample(sqxx);
        mlkXx = getMlkXxById(mlkId);

        DataSecurityUtil.decryptMapList(mlkXx);

        Example example = new Example(DchyXmglCyry.class);
        example.createCriteria().andEqualTo("mlkid", mlkId);
        count = entityMapper.countByExample(example);
        if (CollectionUtils.isNotEmpty(mlkXx)) {
            //??????blob?????????????????????base64?????????
            if (null != mlkXx.get(0).get("MLKTP")) {
                byte[] bytes = blobToBytes((Blob) mlkXx.get(0).get("MLKTP"));
                if (null != bytes && bytes.length > 0) {
                    mlkXx.get(0).put("MLKTP", new String(bytes));
                }
            }
            if (CollectionUtils.isNotEmpty(mlkXx)) {
                mlkXx.get(0).put("CYRYNUM", count);
            }
            if (CollectionUtils.isNotEmpty(objects)) {
                mlkXx.get(0).put("SQXXID", objects.get(0).getSqxxid());

            }
        }
        //??????????????????
        if (CollectionUtils.isNotEmpty(mlkXx)) {
            for (Map<String, Object> map : mlkXx) {
                String mlkid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "MLKID"));
                Map<String, Object> resultMap = queryClsxByMlkid(mlkid);
                map.put("CLSXDMS", MapUtils.getString(resultMap, "clsxDms"));
                map.put("CLSXMCS", MapUtils.getString(resultMap, "clsxMcs"));
            }
        }
        /*??????????????????*/
        Example sqxxExample = new Example(DchyXmglSqxx.class);
        sqxxExample.createCriteria().andEqualTo("glsxid", mlkId).andEqualTo("blsx", "6");
        List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
        if (CollectionUtils.isNotEmpty(sqxxList)) {
            String sqxxid = sqxxList.get(0).getSqxxid();
            if (StringUtils.isNotBlank(sqxxid)) {
                List<DchyXmglYbrw> ybrwList = mlkMapper.queryybrwLaster(sqxxid);
                if (CollectionUtils.isNotEmpty(ybrwList)) {
                    mlkXx.get(0).put("BLYJ", ybrwList.get(0).getBlyj() == null ? "" : ybrwList.get(0).getBlyj());
                    mlkXx.get(0).put("BLJG", ybrwList.get(0).getBljg());
                }
            }
        }
        return mlkXx;
    }

    @Override
    @Transactional
    public boolean updateWjzxid(String wjzxid, String glsxid) {
        boolean updated = false;
        if (StringUtils.isNotBlank(glsxid)) {
            DchyXmglMlk dchyXmglMlk = new DchyXmglMlk();
            dchyXmglMlk.setMlkid(glsxid);
            dchyXmglMlk.setWjzxid(wjzxid);
            int i = entityMapper.saveOrUpdate(dchyXmglMlk, dchyXmglMlk.getMlkid());
            if (i > 0) {
                updated = true;
            }
        }
        return updated;
    }

    @Override
    public String getSsmkid() {
        return SsmkidEnum.MLKRZ.getCode();
    }

    /**
     * ??????userid???????????????????????????
     *
     * @param userId
     * @return
     */
    public DchyXmglMlk getMlkByUserId(String userId) {
        String dwbh = "";
        Example yhdwExample = new Example(DchyXmglYhdw.class);
        yhdwExample.createCriteria().andEqualTo("yhid", userId);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            dwbh = yhdwList.get(0).getDwbh();
        }
        /*??????????????????????????????mlk*/
        if (StringUtils.isNotBlank(dwbh)) {
            List<DchyXmglMlk> mlkByDwbh = mlkMapper.getMlkByDwbh(dwbh);
            if (CollectionUtils.isNotEmpty(mlkByDwbh)) {
                return mlkByDwbh.get(0);
            }
        }
        return null;
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private String getDwmc() {
        String yhId = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", yhId);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwmc = yhdwList.get(0).getDwmc();
            if (StringUtils.isNotBlank(dwmc)) {
                return dwmc;
            }
        }
        return "";
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private String getDwbh() {
        String yhId = UserUtil.getCurrentUserId();
        Example example = new Example(DchyXmglYhdw.class);
        example.createCriteria().andEqualTo("yhid", yhId);
        List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(yhdwList)) {
            String dwbh = yhdwList.get(0).getDwbh();
            if (StringUtils.isNotBlank(dwbh)) {
                return dwbh;
            }
        }
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getMlkYbrw(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String dwmc = CommonUtil.formatEmptyValue(map.get("dwmc"));//??????????????????
        String ksshsj = CommonUtil.formatEmptyValue(map.get("kssqsj"));//??????????????????
        String jsshsj = CommonUtil.formatEmptyValue(map.get("jssqsj"));//??????????????????
        String shjg = CommonUtil.formatEmptyValue(map.get("shjg"));//????????????
        Map<String, Object> param = new HashMap<>();
        param.put("dwmc", dwmc);
        param.put("ksshsj", ksshsj);
        param.put("jsshsj", jsshsj);
        param.put("shjg", shjg);
        param.put("dqjd", Constants.DQJD_SH);

        List<DchyXmglYbrw> ybrwList = mlkMapper.queryYbrw(param);
        List<Object> sqxxidList = new ArrayList<>();//????????????id??????
        if (CollectionUtils.isNotEmpty(ybrwList)) {
            ybrwList.forEach(dbrw -> {
                sqxxidList.add(dbrw.getSqxxid());
            });
        }
        List<Object> mlkidList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sqxxidList)) {
            Example sqxxExample = new Example(DchyXmglSqxx.class);
            sqxxExample.createCriteria().andIn("sqxxid", sqxxidList);
            List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
            if (CollectionUtils.isNotEmpty(sqxxList)) {
                sqxxList.forEach(sqxx -> {
                    DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, sqxx.getGlsxid());
                    if (null != mlk) {
                        mlkidList.add(mlk.getMlkid());
                    }
                });
            }
        }
        if (CollectionUtils.isNotEmpty(mlkidList)) {
            param.put("mlkidList", mlkidList);
            Page<Map<String, Object>> mlkListByPage = repository.selectPaging("queryMlkYbrwByPage", param, page - 1, pageSize);
            if (null != mlkListByPage) {
                List<Map<String, Object>> content = mlkListByPage.getContent();
                if (CollectionUtils.isNotEmpty(content)) {
                    /*??????*/
                    DataSecurityUtil.decryptMapList(content);
                    content.forEach(mlk -> {
                        String zzdj = MapUtils.getString(mlk, "ZZDJ");//????????????
                        if (StringUtils.isNotBlank(zzdj)) {
                            mlk.put("ZZDJMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("ZZDJ", zzdj).getMc());
                        }
                        String dwxz = MapUtils.getString(mlk, "DWXZ");//????????????
                        if (StringUtils.isNotBlank(dwxz)) {
                            mlk.put("DWXZMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("DWXZ", dwxz).getMc());
                        }
                        /*??????????????????*/
                        String mlkid = MapUtils.getString(mlk, "MLKID");
                        if (StringUtils.isNotBlank(mlkid)) {
                            Example cyryExample = new Example(DchyXmglCyry.class);
                            cyryExample.createCriteria().andEqualTo("mlkid", mlkid);
                            List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
                            mlk.put("CYRYNUM", cyryList == null ? 0 : cyryList.size());//?????????????????????????????????
                        }
                        /*???????????????????????????*/
                        String bljg = MapUtils.getString(mlk, "BLJG");
                        String bljg2 = "";
                        if (StringUtils.equals(bljg, Constants.DCHY_XMGL_SQZT_SHTG)) {
                            bljg2 = "??????";
                        } else if (StringUtils.equals(bljg, Constants.DCHY_XMGL_SQZT_TH)) {
                            bljg2 = "??????";
                        }
                        mlk.put("SHJG", bljg2);
                    });
                }
            }
            return mlkListByPage;
        }
        return null;
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getMlkDbrw(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String dwmc = CommonUtil.formatEmptyValue(map.get("dwmc"));//??????????????????
        String kssqsj = CommonUtil.formatEmptyValue(map.get("kssqsj"));//????????????
        String jssqsj = CommonUtil.formatEmptyValue(map.get("jssqsj"));//????????????
        Map<String, Object> param = new HashMap<>();
        param.put("dwmc", dwmc);
        param.put("kssqsj", kssqsj);
        param.put("jssqsj", jssqsj);
        Example example = new Example(DchyXmglDbrw.class);
        example.createCriteria().andEqualTo("dqjd", Constants.DQJD_SH);
        List<DchyXmglDbrw> dbrwList = entityMapper.selectByExample(example);
        List<Object> sqxxidList = new ArrayList<>();//????????????id??????
        if (CollectionUtils.isNotEmpty(dbrwList)) {
            dbrwList.forEach(dbrw -> {
                sqxxidList.add(dbrw.getSqxxid());
            });
        }
        List<Object> mlkidList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sqxxidList)) {
            Example sqxxExample = new Example(DchyXmglSqxx.class);
            sqxxExample.createCriteria().andIn("sqxxid", sqxxidList);
            List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
            if (CollectionUtils.isNotEmpty(sqxxList)) {
                sqxxList.forEach(sqxx -> {
                    DchyXmglMlk mlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, sqxx.getGlsxid());
                    if (null != mlk) {
                        mlkidList.add(mlk.getMlkid());
                    }
                });
            }
        }
        if (CollectionUtils.isNotEmpty(mlkidList)) {
            param.put("mlkidList", mlkidList);
            Page<Map<String, Object>> mlkListByPage = repository.selectPaging("queryMlkDbrwByPage", param, page - 1, pageSize);
            if (null != mlkListByPage) {
                List<Map<String, Object>> content = mlkListByPage.getContent();
                if (CollectionUtils.isNotEmpty(content)) {
                    /*??????*/
                    DataSecurityUtil.decryptMapList(content);
                    content.forEach(mlk -> {
                        String zzdj = MapUtils.getString(mlk, "ZZDJ");//????????????
                        if (StringUtils.isNotBlank(zzdj)) {
                            mlk.put("ZZDJMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("ZZDJ", zzdj).getMc());
                        }
                        String dwxz = MapUtils.getString(mlk, "DWXZ");//????????????
                        if (StringUtils.isNotBlank(dwxz)) {
                            mlk.put("DWXZMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("DWXZ", dwxz).getMc());
                        }
                        /*??????????????????*/
                        String mlkid = MapUtils.getString(mlk, "MLKID");
                        if (StringUtils.isNotBlank(mlkid)) {
                            Example cyryExample = new Example(DchyXmglCyry.class);
                            cyryExample.createCriteria().andEqualTo("mlkid", mlkid);
                            List<DchyXmglCyry> cyryList = entityMapper.selectByExample(cyryExample);
                            mlk.put("CYRYNUM", cyryList == null ? 0 : cyryList.size());//?????????????????????????????????
                        }
                    });
                }
            }
            return mlkListByPage;
        }
        return null;
    }

    /**
     * ??????mlkid
     *
     * @param
     * @return
     */
    private String getMlkByUserId() {
        String userId = UserUtil.getCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            Example yhdwExample = new Example(DchyXmglYhdw.class);
            yhdwExample.createCriteria().andEqualTo("yhid", userId);
            List<DchyXmglYhdw> yhdwList = entityMapper.selectByExample(yhdwExample);
            if (CollectionUtils.isNotEmpty(yhdwList)) {
                DchyXmglYhdw yhdw = yhdwList.get(0);
                if (null != yhdw) {
                    String dwbh = yhdw.getDwbh();
                    if (StringUtils.isNotBlank(dwbh)) {
                        /*??????????????????????????????mlkid*/
                        Example mlkExample = new Example(DchyXmglMlk.class);
                        mlkExample.createCriteria().andEqualTo("dwbh", dwbh);
                        List<DchyXmglMlk> mlkList = entityMapper.selectByExample(mlkExample);
                        if (CollectionUtils.isNotEmpty(mlkList)) {
                            DchyXmglMlk mlk = mlkList.get(0);
                            if (null != mlk) {
                                return mlk.getMlkid();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    @Override
    public Page<Map<String, Object>> mlkck(Map map) {
        int page = Integer.parseInt(map.get("page") != null ? CommonUtil.formatEmptyValue(map.get("page")) : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? CommonUtil.formatEmptyValue(map.get("size")) : Constants.DCHY_XMGL_PAGINATION_SIZE);

        /*????????????*/
        Page<Map<String, Object>> dataPaging = repository.selectPaging("queryMlkListByPage", map, page - 1, pageSize);

        if (CollectionUtils.isNotEmpty(dataPaging.getContent())) {
            List<Map<String, Object>> mlks = dataPaging.getContent();
            for (Map<String, Object> mlk : mlks) {
                String mlkid = CommonUtil.formatEmptyValue(MapUtils.getString(mlk, "MLKID"));

                Map<String, Object> resultMap = queryClsxByMlkid(mlkid);
                mlk.put("CLSXDMS", MapUtils.getString(resultMap, "clsxDms"));
                mlk.put("CLSXMCS", MapUtils.getString(resultMap, "clsxMcs"));
                mlk.put("PJ", getKpResultByMlkId(mlkid));

                if (null != mlk.get("MLKTP")) {
                    byte[] bytes = blobToBytes((Blob) mlk.get("MLKTP"));
                    if (null != bytes && bytes.length > 0) {
                        mlk.put("MLKTP", new String(bytes));
                    }
                }

            }
        }

//        List<Map> mlkList = mlkMapper.queryMlkListByPage(map);
//        if (CollectionUtils.isNotEmpty(mlkList)) {
//            for (Map mlk : mlkList) {
//                String mlkid = MapUtils.getString(mlk, "MLKID");
//                int pjdj = getKpResultByMlkId(MapUtils.getString(mlk, "MLKID"));
//                DchyXmglMlk dchyXmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
//                dchyXmglMlk.setPjdj(pjdj / 2 + "");
//                entityMapper.saveOrUpdate(dchyXmglMlk, mlkid);
//            }
//        }
        DataSecurityUtil.decryptMapList(dataPaging.getContent());
        return dataPaging;

    }

    /**
     * ?????????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> queryChdwsByMultConditions(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        String dwmc = CommonUtil.ternaryOperator(data.get("dwmc"));
        String dwxz = CommonUtil.ternaryOperator(data.get("dwxz"));//????????????
        String zzdj = CommonUtil.ternaryOperator(data.get("zzdj"));//????????????
        Map<String, Object> param = new HashMap<>();
        param.put("dwmc", dwmc);
        param.put("dwxz", dwxz);
        param.put("zzdj", zzdj);
        try {
            Page<Map<String, Object>> chdwPage = repository.selectPaging("queryChdwsByMultConditionsByPage", param, page - 1, pageSize);
            if (null != chdwPage) {
                List<Map<String, Object>> chdwPageContent = chdwPage.getContent();
                if (CollectionUtils.isNotEmpty(chdwPageContent)) {
                    DataSecurityUtil.decryptMapList(chdwPageContent);
                    for (Map<String, Object> chdwMap : chdwPageContent) {
                        String tempDwxz = MapUtils.getString(chdwMap, "DWXZ");
                        String tempZzdj = MapUtils.getString(chdwMap, "ZZDJ");
                        if (StringUtils.isNotBlank(tempDwxz)) {
                            chdwMap.put("DWXZMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("DWXZ", tempDwxz).getMc());
                        }
                        if (StringUtils.isNotBlank(tempZzdj)) {
                            chdwMap.put("ZZDJMC", dchyXmglZdService.getDchyXmglByZdlxAndDm("ZZDJ", tempZzdj).getMc());
                        }
                    }
                    return chdwPage;
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
        return null;
    }

    /**
     * ?????????????????????????????????htxxid????????????????????????????????????
     *
     * @param chxmid
     * @return
     */
    @Override
    public String initHtxx(String chxmid) {
        if (StringUtils.isNotBlank(chxmid)) {
            /*??????chxmid????????????htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx xmglHtxx = htxxList.get(0);
                if (null != xmglHtxx) {
                    return xmglHtxx.getHtxxid();
                }
            } else {
                /*?????????????????????*/
                DchyXmglHtxx xmglHtxx = new DchyXmglHtxx();
                xmglHtxx.setHtxxid(UUIDGenerator.generate18());
                xmglHtxx.setChxmid(chxmid);
                xmglHtxx.setBazt(Constants.WTZT_YBA);//????????????
                xmglHtxx.setBasj(new Date());//????????????
                xmglHtxx.setQysj(null);//????????????
                xmglHtxx.setWjzxid("");
                xmglHtxx.setHtlx("0");//????????????
                xmglHtxx.setHtbmid("");//????????????id
                entityMapper.saveOrUpdate(xmglHtxx, xmglHtxx.getHtxxid());
                /*htxx????????????????????????htxxid??????*/
                return xmglHtxx.getHtxxid();
            }
        }
        return "";
    }

    @Override
    public String getHtxxIdByChxmid(String chxmid) {
        if (StringUtils.isNotBlank(chxmid)) {
            /*??????chxmid????????????htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                DchyXmglHtxx xmglHtxx = htxxList.get(0);
                if (null != xmglHtxx) {
                    return xmglHtxx.getHtxxid();
                }
            }
        }
        return "";
    }

    @Override
    public List<String> getHtxxIdByChxmid2(String chxmid) {
        List<String> htxxidList = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid)) {
            /*??????chxmid????????????htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx xmglHtxx : htxxList) {
                    htxxidList.add(xmglHtxx.getHtxxid());
                }
            }
        }
        return htxxidList;
    }

    @Override
    public List<String> getXsbfHtxxIdByChxmid(String chxmid) {
        List<String> htxxidList = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid)) {
            /*??????chxmid????????????htxx*/
            Example htxxExample = new Example(DchyXmglHtxx.class);
            htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglHtxx> htxxList = entityMapperXSBF.selectByExample(htxxExample);
            if (CollectionUtils.isNotEmpty(htxxList)) {
                for (DchyXmglHtxx xmglHtxx : htxxList) {
                    htxxidList.add(xmglHtxx.getHtxxid());
                }
            }
        }
        return htxxidList;
    }

    /**
     * ???????????????chxm?????????????????????????????????sjcl
     *
     * @param data
     * @return
     */
    @Override
    public List<Map<String, Object>> afterBaxxForSjcl(Map<String, Object> data) {
        String chxmid = CommonUtil.ternaryOperator(data.get("chxmid"));
        List<Map<String, Object>> resultMap = new ArrayList<>();
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != xmglChxm && StringUtils.isNotBlank(xmglChxm.getWtzt())) {
                String wtzt = xmglChxm.getWtzt();
                /*????????????*/
                if (StringUtils.equals(Constants.WTZT_DJS, wtzt)) {
                    //??????????????????
                    resultMap.addAll(getSjclXx(chxmid, SsmkidEnum.JSDWWT.getCode()));
                }
                /*??????*/
                if (StringUtils.equals(Constants.WTZT_YJS, wtzt)) {
                    //??????????????????,????????????
                    resultMap.addAll(getSjclXx(chxmid, SsmkidEnum.JSDWWT.getCode()));
                    resultMap.addAll(getSjclXx(chxmid, SsmkidEnum.CHDWHY.getCode()));
                }
                /*?????????*/
                if (StringUtils.equals(Constants.WTZT_DBA, wtzt)) {
                    resultMap.addAll(getSjclXx(chxmid, SsmkidEnum.JSDWWT.getCode()));
                    resultMap.addAll(getSjclXx(chxmid, SsmkidEnum.CHDWHY.getCode()));
                    Example htxxExample = new Example(DchyXmglHtxx.class);
                    htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
                    List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
                    if (CollectionUtils.isNotEmpty(htxxList)) {
                        String htxxid = htxxList.get(0).getHtxxid();
                        //????????????
                        resultMap.addAll(getSjclXx(htxxid, SsmkidEnum.CGCCPJ.getCode()));
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    @Transactional(readOnly = true)
    public int getKpResultByMlkId(String mlkid) {
        Example kpExample = new Example(DchyXmglKp.class);
        kpExample.createCriteria().andEqualTo("mlkid", mlkid);
        List<DchyXmglKp> kpList = entityMapper.selectByExample(kpExample);
        double kpAvg = 0.0;//?????????????????????
        if (CollectionUtils.isNotEmpty(kpList)) {
            kpAvg = (kpList.stream().map(DchyXmglKp::getKpjg).mapToDouble(Double::parseDouble).sum()) / kpList.size();
        }
        double fwAvg = 0.0;//?????????????????????
        Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
        chdwExample.createCriteria().andEqualTo("mlkid", mlkid).andIsNotNull("fwpj").andIsNotNull("pjsj");
        List<DchyXmglChxmChdwxx> chdwxxList = entityMapperXSBF.selectByExample(chdwExample);
        if (CollectionUtils.isNotEmpty(chdwxxList)) {
            fwAvg = (chdwxxList.stream().map(DchyXmglChxmChdwxx::getFwpj).mapToDouble(fwpj -> fwpj).sum()) / chdwxxList.size();
        }

        if (0.0 != kpAvg && 0.0 != fwAvg) {
            return (int) ((Math.round(kpAvg) + Math.round(fwAvg)) / 2);
        } else if (0.0 != kpAvg) {
            return (int) Math.round(kpAvg);
        } else if (0.0 != fwAvg) {
            return (int) Math.round(fwAvg);
        }
        return 0;
    }


    @Override
    public Map<String, Object> queryClsxByMlkid(String mlkid) {
        Map<String, Object> resultMap = Maps.newHashMap();
        String clsxDms = "";
        String clsxMcs = "";
        if (StringUtils.isNotBlank(mlkid)) {
            Example exampleGx = new Example(DchyXmglMlkClsxGx.class);
            exampleGx.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxes = entityMapper.selectByExample(exampleGx);
            if (CollectionUtils.isNotEmpty(dchyXmglMlkClsxGxes)) {
                for (DchyXmglMlkClsxGx dchyXmglMlkClsxGx : dchyXmglMlkClsxGxes) {
                    String clsxDm = dchyXmglMlkClsxGx.getClsx();
                    String clsxMc = dchyXmglZdService.getZdmcByZdlxAndDm(Constants.DCHY_XMGL_CHXM_CLSX, clsxDm);
                    if (!clsxDms.contains(clsxDm)) {
                        clsxDms = clsxDms + clsxDm + ";";
                    }
                    if (!clsxMcs.contains(clsxMc)) {
                        clsxMcs = clsxMcs + clsxMc + ";";
                    }
                }
            }
        }
        resultMap.put("clsxDms", clsxDms);
        resultMap.put("clsxMcs", clsxMcs);
        return resultMap;
    }

    @Override
    public ResponseMessage uploadMlktp(Map map) {
        ResponseMessage message = new ResponseMessage();
        DchyXmglMlk dchyXmglMlk = (DchyXmglMlk) map.get("dchyXmglMlk");
        int flag = entityMapper.saveOrUpdate(dchyXmglMlk, dchyXmglMlk.getMlkid());
        if (flag > 0) {
            message = ResponseUtil.wrapSuccessResponse();
        } else {
            message = ResponseUtil.wrapExceptionResponse();
        }

        return message;
    }

    /**
     * ???Blob???????????????byte????????????
     *
     * @param blob
     * @return
     */
    private byte[] blobToBytes(Blob blob) {
        if (null != blob) {
            BufferedInputStream is = null;

            try {
                is = new BufferedInputStream(blob.getBinaryStream());

                byte[] bytes = new byte[(int) blob.length()];

                int len = bytes.length;

                int offset = 0;

                int read = 0;

                if (null != is) {
                    while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0) {
                        offset += read;

                    }
                }
                return bytes;

            } catch (Exception e) {
                return null;

            } finally {
                try {
                    if (null != is) {
                        is.close();

                        is = null;
                    }

                } catch (IOException e) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}
