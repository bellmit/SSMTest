package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repository;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglMlkMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglGldwService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.core.service.MessagePushService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/10 21:20
 * @description
 */
@Service
public class DchyXmglGldwServiceImpl implements DchyXmglGldwService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private Repo repository;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Resource(name = "repositoryXSBF")
    @Autowired
    private Repository repositoryXSBF;
    @Resource(name = "entityMapperXSBF")
    private EntityMapper entityMapperXSBF;
    @Autowired
    private PushDataToMqService pushDataToMqService;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private DchyXmglMlkMapper dchyXmglMlkMapper;

    private Logger logger = LoggerFactory.getLogger(DchyXmglGldwServiceImpl.class);

    @Override
    public Page<Map<String, Object>> queryChdwKpStatusByPage(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        HashMap<String, Object> map = Maps.newHashMap();
        String chdwmc = CommonUtil.ternaryOperator(data.get("chdwmc"));
        map.put("chdwmc", chdwmc.trim());
        Page<Map<String, Object>> kpInfoByPage = repository.selectPaging("queryChdwKpInfoByPage", map, page - 1, pageSize);
        if (CollectionUtils.isNotEmpty(kpInfoByPage.getContent())) {
            List<Map<String, Object>> content = kpInfoByPage.getContent();
            for (Map<String, Object> kp : content) {
                String sfyx = MapUtils.getString(kp, "SFYX");
                if (StringUtils.equals(sfyx, "0")) {
                    content.remove(kp);
                }
                if (StringUtils.equals(sfyx, "1") || null == sfyx) {
                    if (null != MapUtils.getString(kp, "XY")) {
                        kp.put("XYMC", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XYD, MapUtils.getString(kp, "XY")).getMc());
                        kp.put("XYDJ", this.getCreditRate(MapUtils.getIntValue(kp, "XYMC")));
                    }
                    kp.put("ZZDJMC", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_ZZDJ, MapUtils.getString(kp, "ZZDJ")).getMc());
                }
            }
        }
        DataSecurityUtil.decryptMapList(kpInfoByPage.getContent());
        return kpInfoByPage;
    }

    /**
     * ?????????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    @Transactional
    public DchyXmglKp saveChdwKpxx(Map<String, Object> data) {
        String kpId = CommonUtil.ternaryOperator(data.get("kpid"));
        String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
        String xy = CommonUtil.ternaryOperator(data.get("xy"));
        String cgzl = CommonUtil.ternaryOperator(data.get("cgzl"));
        String ndkp = CommonUtil.ternaryOperator(data.get("ndkp"));//????????????
        String kpyj = CommonUtil.ternaryOperator(data.get("kpyj"));
        if (StringUtils.isNotBlank(kpId)) {
            /*????????????????????????*/
            List<DchyXmglKp> xmglKpList = dchyXmglMlkMapper.getLasterKpById(mlkId);
            if (CollectionUtils.isNotEmpty(xmglKpList)) {
                DchyXmglKp xmglKp = xmglKpList.get(0);
                if (null != xmglKp) {
                    /*?????????????????????????????????*/
                    xmglKp.setSfyx(Constants.INVALID);
                    entityMapper.saveOrUpdate(xmglKp, xmglKp.getKpid());
                }
            }
        }
        /*??????????????????*/
        kpId = UUIDGenerator.generate18();
        DchyXmglKp dchyXmglKp = new DchyXmglKp();
        dchyXmglKp.setKpid(kpId);
        dchyXmglKp.setMlkid(mlkId);
        dchyXmglKp.setKpyj(kpyj);
        dchyXmglKp.setKpsj(new Date());
        dchyXmglKp.setKpry(UserUtil.getCurrentUserId());
        dchyXmglKp.setXy(xy);
        dchyXmglKp.setCgzl(cgzl);
        if (StringUtils.isNotBlank(ndkp)) {
            dchyXmglKp.setNdkp(ndkp);//????????????
            dchyXmglKp.setSfndkp("1");//??????????????????
        }
        dchyXmglKp.setSfyx(Constants.VALID);
        Double result = Double.parseDouble(xy) + Double.parseDouble(cgzl);
        dchyXmglKp.setKpjg((int) Math.round(result / 4.0) + "");
        int flag = entityMapper.saveOrUpdate(dchyXmglKp, dchyXmglKp.getKpid());
        if (flag > 0) {
            //this.gldwKpMessageReminder(dchyXmglKp.getMlkid(), dchyXmglKp.getKpid());

            //????????????
            String yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_GLDWPJ;
            String jsyhid = dchyXmglKp.getMlkid();
            String glsxid = dchyXmglKp.getKpid();
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("yhxxpzid", yhxxpzid);
            paramMap.put("jsyhid", jsyhid);
            paramMap.put("glsxid", glsxid);
            messagePushService.updateYhxxInfo(paramMap);

            pushDataToMqService.pushKpMsgToMq(dchyXmglKp.getMlkid());
        }
        return dchyXmglKp;
    }

    /**
     * ?????????????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public Map<String, String> saveChdwCxjl(Map<String, Object> param) {
        Map<String, String> resultMap = Maps.newHashMap();
        String code = ResponseMessage.CODE.SUCCESS.getCode();
        String msg = ResponseMessage.CODE.SUCCESS.getMsg();
        String cxjlid = UUIDGenerator.generate18();
        String mlkId = CommonUtil.ternaryOperator(param.get("mlkid"));
        String cxpj = CommonUtil.ternaryOperator(param.get("cxpj"));
        if (StringUtils.isNoneBlank(code, msg)) {
            DchyXmglMlkCxjl dchyXmglMlkCxjl = new DchyXmglMlkCxjl();
            dchyXmglMlkCxjl.setCxjlid(cxjlid);
            dchyXmglMlkCxjl.setMlkid(mlkId);
            dchyXmglMlkCxjl.setJlsj(Calendar.getInstance().getTime());
            dchyXmglMlkCxjl.setCxpj(cxpj);
            dchyXmglMlkCxjl.setJlrid(UserUtil.getCurrentUserId());
            dchyXmglMlkCxjl.setJlrmc(UserUtil.getCurrentUser().getUsername());
            entityMapper.saveOrUpdate(dchyXmglMlkCxjl, dchyXmglMlkCxjl.getCxjlid());
        } else {
            code = ResponseMessage.CODE.PARAMETER_FAIL.getCode();
            msg = ResponseMessage.CODE.PARAMETER_FAIL.getMsg();
        }
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * ??????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public Page<Map<String, Object>> getGldwKpxxByMlkId(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        String kpkssj = CommonUtil.ternaryOperator(data.get("kpkssj"));
        String kpjssj = CommonUtil.ternaryOperator(data.get("kpjssj"));
        String kpjg = CommonUtil.ternaryOperator(data.get("kpjg"));
        map.put("mlkid", mlkid);
        map.put("kpkssj", kpkssj);
        map.put("kpjssj", kpjssj);
        map.put("kpjg", kpjg);
        Page<Map<String, Object>> kpInfoByPage = repository.selectPaging("queryGldwKpInfoByPage", map, page - 1, pageSize);
        if (CollectionUtils.isNotEmpty(kpInfoByPage.getContent())) {
            List<Map<String, Object>> content = kpInfoByPage.getContent();
            for (Map<String, Object> kp : content) {
                if (null != MapUtils.getString(kp, "KPJG")) {
                    kp.put("KPJG", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_XYD, MapUtils.getString(kp, "KPJG")).getMc());
                    String str = this.getCreditRate(MapUtils.getIntValue(kp, "KPJG"));
                    kp.put("KPJG", str.substring(0, str.indexOf("???")));
                }
            }
        }
        DataSecurityUtil.decryptMapList(kpInfoByPage.getContent());
        return kpInfoByPage;
    }

    @Override
    @Transactional
    public void removeMlkById(Map<String, Object> data) {
        try {
            String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
            List<String> list = (List<String>) data.get("ycyy");//????????????
            String ycbcsm = CommonUtil.ternaryOperator(data.get("ycbcsm"));
            StringBuilder builder = new StringBuilder();
            String ycdw = CommonUtil.ternaryOperator(data.get("ycdw"));
            DchyXmglMlk xmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != xmglMlk) {
                Example sqxxExample = new Example(DchyXmglSqxx.class);
                sqxxExample.createCriteria().andEqualTo("glsxid", xmglMlk.getMlkid());
                List<DchyXmglSqxx> sqxxList = entityMapper.selectByExample(sqxxExample);
                if (CollectionUtils.isNotEmpty(sqxxList)) {
                    DchyXmglSqxx xmglSqxx = sqxxList.get(0);
                    if (null != xmglSqxx) {
                        xmglSqxx.setBlsx("2");//2:???????????????
                        entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
                    }
                } else {
                    /*?????????????????????*/
                    DchyXmglSqxx xmglSqxx = new DchyXmglSqxx();
                    String sqxxid = UUIDGenerator.generate18();
                    xmglSqxx.setSqxxid(sqxxid);
                    xmglSqxx.setSqjgmc(xmglMlk.getDwmc());
                    xmglSqxx.setBlsx("2");//2:???????????????
                    xmglSqxx.setSqr(UserUtil.getCurrentUserId());
                    xmglSqxx.setSqsj(new Date());
                    xmglSqxx.setSqzt("1");//1:?????????
                    xmglSqxx.setGlsxid(xmglMlk.getMlkid());
                    xmglSqxx.setSqbh(Calendar.getInstance().getTimeInMillis() + sqxxid);
                    xmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
                    entityMapper.saveOrUpdate(xmglSqxx, xmglSqxx.getSqxxid());
                }
                /*??????????????????*/
                xmglMlk.setSfyx(Constants.INVALID);
                xmglMlk.setYcdw(ycdw);
                xmglMlk.setYcsj(new Date());
                xmglMlk.setYcr(UserUtil.getCurrentUserId());
                if (CollectionUtils.isNotEmpty(list)) {
                    for (String ycyy : list) {
                        builder.append(ycyy);
                    }
                }
                xmglMlk.setYcyy(builder.toString());
                xmglMlk.setYcbcsm(ycbcsm);
                int flag = entityMapper.saveOrUpdate(xmglMlk, xmglMlk.getMlkid());
                if (flag > 0) {

                    //????????????
                    String yhxxpzid = Constants.DCHY_XMGL_ZD_XXNR_MLKYC;
                    Map<String, Object> paramMap = Maps.newHashMap();
                    paramMap.put("yhxxpzid", yhxxpzid);
                    paramMap.put("jsyhid", mlkid);
                    paramMap.put("glsxid", mlkid);
                    messagePushService.updateYhxxInfo(paramMap);

                    /*????????????*/
                    DchyXmglMlkDto mlkDto = new DchyXmglMlkDto();
                    List<DchyXmglMlk> mlkList = new ArrayList<>();
                    mlkList.add(xmglMlk);
                    mlkDto.setDchyXmglMlkList(mlkList);
                    pushDataToMqService.pushMlkDtoToMq(mlkDto);
                }
            }
        } catch (Exception e) {
            logger.error("????????????:{}", e);
        }
    }

    @Override
    public Page<Map<String, Object>> getChdwEvalByid(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        String pjkssj = CommonUtil.ternaryOperator(data.get("pjkssj"));
        String pjjssj = CommonUtil.ternaryOperator(data.get("pjjssj"));
        String fwpj = CommonUtil.ternaryOperator(data.get("fwpj"));
        map.put("mlkid", mlkid);
        map.put("pjkssj", pjkssj);
        map.put("pjjssj", pjjssj);
        map.put("fwpj", fwpj);
        Page<Map<String, Object>> jsdwPlInfoByPage = repositoryXSBF.selectPaging("queryJsdwPlInfoByPage", map, page - 1, pageSize);
        List<Map<String, Object>> content = jsdwPlInfoByPage.getContent();
        if (null != content && CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> plInfo : content) {
                String tempFwpj = MapUtils.getString(plInfo, "FWPJ");
                if (StringUtils.isNotBlank(tempFwpj)) {
                    plInfo.put("MYD", this.getServiceEval(Integer.parseInt(tempFwpj)));
                }
            }
        }
        DataSecurityUtil.decryptMapList(jsdwPlInfoByPage.getContent());//??????
        return jsdwPlInfoByPage;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param data
     * @return
     */
    @Override
    public boolean isConstructProject(Map<String, Object> data) {
        boolean flag = true;
        String mlkid = CommonUtil.ternaryOperator(data.get("mlkid"));
        DchyXmglMlk mlkXx = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
        if (null != mlkXx) {
            try {
                Example chdwExample = new Example(DchyXmglChxmChdwxx.class);
                chdwExample.createCriteria().andEqualTo("mlkid", mlkid);
                List<DchyXmglChxmChdwxx> chxmChdwxxList = entityMapper.selectByExample(chdwExample);
                if (CollectionUtils.isNotEmpty(chxmChdwxxList)) {
                    for (DchyXmglChxmChdwxx chdwxx : chxmChdwxxList) {
                        if (null != chdwxx) {
                            String chxmid = chdwxx.getChxmid();
                            if (StringUtils.isNotBlank(chxmid)) {
                                DchyXmglChxm chxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                                /*????????????????????????????????????????????????*/
                                if (null != chxm) {
                                    String xmzt = chxm.getXmzt();
                                    if (StringUtils.isNotBlank(xmzt) && !StringUtils.equals("99", xmzt)) {
                                        //??????????????????
                                        flag = false;
                                        /*??????????????????????????????????????????*/
                                        mlkXx.setSfdj("1");//1?????????
                                        entityMapper.saveOrUpdate(mlkXx, mlkXx.getMlkid());
                                        /*??????????????? ???????????????????????????*/
                                        /*????????????*/
                                        DchyXmglMlkDto mlkDto = new DchyXmglMlkDto();
                                        List<DchyXmglMlk> mlkList = new ArrayList<>();
                                        mlkList.add(mlkXx);
                                        mlkDto.setDchyXmglMlkList(mlkList);
                                        pushDataToMqService.pushMlkDtoToMq(mlkDto);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("????????????:{}", e);
            }
        }
        return flag;
    }

    @Override
    public Page<Map<String, Object>> getChdwPJxxById(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : "1");
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : "10");
        HashMap<String, Object> map = Maps.newHashMap();
        String chdwxxid = CommonUtil.ternaryOperator(data.get("chdwxxid"));
        map.put("chdwxxid", chdwxxid);
        return repositoryXSBF.selectPaging("queryChdwPjInfoByPage", map, page - 1, pageSize);
    }

    private String getCreditRate(int xy) {
        String value = "";
        if (xy <= 4) {
            value = "?????????4???????????????";
        } else if (xy >= 9) {
            value = "?????????9???????????????";
        } else if (xy >= 7 && xy < 9) {
            value = "?????????7???????????????";
        } else if (xy >= 5 && xy < 7) {
            value = "?????????5???????????????";
        }
        return value;
    }

    private String getServiceEval(int fwpj) {
        String result = "";
        switch (fwpj) {
            case 1:
                result = "???";
                break;
            case 2:
                result = "??????";
                break;
            case 3:
                result = "??????";
                break;
            case 4:
                result = "??????";
                break;
            case 5:
                result = "????????????";
                break;
        }
        return result;
    }

    @Override
    public ResponseMessage evaluationCheckResults(Map<String, Object> map) {
        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        logger.info("******************page:" + page);
        logger.info("******************pageSize:" + pageSize);

        String chdwmc = "";
        String mlkid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "mlkid"));
        if (StringUtils.isNotBlank(mlkid)) {
            DchyXmglMlk xmglMlk = entityMapper.selectByPrimaryKey(DchyXmglMlk.class, mlkid);
            if (null != xmglMlk) {
                chdwmc = xmglMlk.getDwmc();
            }
        }
        map.put("chdwmc", chdwmc);
        Page<Map<String, Object>> evaluationCheckResultsByPage = repositoryXSBF.selectPaging("evaluationCheckResultsByPage", map, page - 1, pageSize);

        List<Map<String, Object>> rows = evaluationCheckResultsByPage.getContent();
        logger.info("******************rows:" + rows);
        if (CollectionUtils.isNotEmpty(rows)) {
            for (Map<String, Object> dataMap : rows) {
                String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "CGCCID"));
                List<String> wjzxids = queryWjzxidListByGlsxid(glsxid);

                if (CollectionUtils.isNotEmpty(wjzxids)) {
                    dataMap.put("SFSC", "1");
                } else {
                    dataMap.put("SFSC", "0");
                }
            }
        }

        return ResponseUtil.wrapResponseBodyByPage(evaluationCheckResultsByPage);
    }

    @Override
    public List<String> queryWjzxidListByGlsxid(String glsxid) {
        List<String> wjzxids = Lists.newArrayList();
        if (StringUtils.isNotBlank(glsxid)) {
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapperXSBF.selectByExample(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                    Example exampleSjcl = new Example(DchyXmglSjcl.class);
                    exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                    List<DchyXmglSjcl> dchyXmglSjclList = entityMapperXSBF.selectByExample(exampleSjcl);
                    if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                        for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                            if (StringUtils.isNotBlank(dchyXmglSjcl.getWjzxid())) {
                                wjzxids.add(dchyXmglSjcl.getWjzxid());
                            }
                        }
                    }
                }
            }

        }

        return wjzxids;
    }

    @Override
    public Set<String> getKpNdkpByMlkid(String mlkid) {
        Set<String> ndkpList = new HashSet<>();
        if (StringUtils.isNotBlank(mlkid)) {
            Example kpExample = new Example(DchyXmglKp.class);
            kpExample.createCriteria().andEqualTo("mlkid", mlkid);
            List<DchyXmglKp> kpList = entityMapper.selectByExample(kpExample);
            if (CollectionUtils.isNotEmpty(kpList)) {
                kpList.forEach(kp -> {
                    if (null != kp.getNdkp()) {
                        ndkpList.add(kp.getNdkp());
                    }
                });
            }
        }
        return ndkpList;
    }
}
