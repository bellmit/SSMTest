package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.*;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChgcMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmChdwxxMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmMapper;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglClcgMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.SignService;
import cn.gtmap.msurveyplat.promanage.service.ResultsSubmitService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXml;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXmlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.model.ActivityModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/8
 * @description ?????????????????????---????????????
 */
@Primary
@Service("chgcResultsSubmitServiceImpl")
public class ChgcResultsSubmitServiceImpl implements ResultsSubmitService {

    private static final Logger logger = LoggerFactory.getLogger(ChgcResultsSubmitServiceImpl.class);

    @Value("${cgtj.chgcMode.gzldyid}")
    private String gzldyid;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    WorkFlowXmlUtil workFlowXmlUtil;

    @Autowired
    private SysTaskService sysTaskService;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Autowired
    private DchyXmglChxmMapper dchyXmglChxmMapper;

    @Autowired
    private SignService signService;

    @Autowired
    private DchyXmglChgcMapper dchyXmglChgcMapper;

    @Autowired
    private DchyXmglChxmChdwxxMapper dchyXmglChxmChdwxxMapper;

    @Autowired
    private ExchangeFeignServiceImpl exchangeFeignServiceImpl;

    @Autowired
    private DchyXmglClcgMapper dchyXmglClcgMapper;

    @Override
    public String initSqxx() {
        String result = "";
        DchyXmglSqxx dchyXmglSqxx = new DchyXmglSqxx();
        dchyXmglSqxx.setSqxxid(UUIDGenerator.generate18());
        dchyXmglSqxx.setSqr(UserUtil.getCurrentUserId());
        dchyXmglSqxx.setSqrmc(UserUtil.getCurrentUser().getUsername());
        dchyXmglSqxx.setSqsj(CalendarUtil.getCurHMSDate());
        dchyXmglSqxx.setSqzt(Constants.DCHY_XMGL_SQZT_DSH);

        int flag = entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
        if (flag > 0) {
            result = dchyXmglSqxx.getSqxxid();
        }
        return gzldyid;
    }

    @Override
    public ResponseMessage initCgtj(String xmid) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(xmid)) {
            try {
                Map<String, Object> map = ResultsSubmitServiceImpl.initCgtj(xmid);
                resultMap.put("data", map);
                message = ResponseUtil.wrapSuccessResponse();
                message.setData(resultMap);
            } catch (Exception e) {
                logger.error("????????????{}???", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    @Override
    public ResponseMessage checkZipFiles(Map<String, Object> map, String uploadFileName, InputStream inputStream) {
        Map<String, String> mapResult = Maps.newHashMap();
        ResponseMessage message;
        try {
            message = ResultsSubmitServiceImpl.checkZipFiles(uploadFileName, inputStream, map);
        } catch (IOException e) {
            mapResult.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            mapResult.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
            logger.error("????????????:{}", e);
        }
        return message;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseMessage zipUpload(String gzlslid, String glsxid, List<Map<String, String>> errorInfoModels) {

        Map<String, String> mapResult = Maps.newHashMap();

        try {
            // ?????????????????????
            ResponseMessage responseMessage = ResultsSubmitServiceImpl.zipUpload(glsxid, errorInfoModels);
            if (!StringUtils.equals(responseMessage.getHead().getCode(), ResponseMessage.CODE.SUCCESS.getCode())) {
                return responseMessage;
            }
            String slbh = "";
            String jsdw = "";
            String chdw = "";
            String xmbh = "";
            String xmmc = "";
            StringBuilder xmdz = new StringBuilder();
            String slr = "";
            // ??????
            if (StringUtils.isNoneBlank(gzlslid, glsxid)) {
                DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, glsxid);
                if (null != dchyXmglSqxx) {
                    slbh = dchyXmglSqxx.getSqbh();
                    slr = dchyXmglSqxx.getSqrmc();
                }
                Map<String, Object> param = Maps.newHashMap();
                param.put("sqxxid", glsxid);
                MapperParamUtil.formatRequiredStringParam(param, "sqxxid");
                List<DchyXmglChgc> dchyXmglChgcList = dchyXmglChgcMapper.queryChgcxxByClcgxx(param);
                if (CollectionUtils.isNotEmpty(dchyXmglChgcList)) {
                    jsdw = dchyXmglChgcList.get(0).getWtdw();
                    xmbh = dchyXmglChgcList.get(0).getGcbh();
                    xmmc = dchyXmglChgcList.get(0).getGcmc();
                    DchyXmglZd gcdzs = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, dchyXmglChgcList.get(0).getGcdzs());
                    DchyXmglZd gcdzss = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, dchyXmglChgcList.get(0).getGcdzss());
                    DchyXmglZd gcdzqx = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, dchyXmglChgcList.get(0).getGcdzqx());
                    if (null != gcdzs) {
                        xmdz.append(gcdzs.getMc());
                    }
                    if (null != gcdzss) {
                        xmdz.append(gcdzss.getMc());
                    }
                    if (null != gcdzqx) {
                        xmdz.append(gcdzqx.getMc());
                    }
                    xmdz.append(dchyXmglChgcList.get(0).getGcdzxx());
                }
                List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = dchyXmglChxmChdwxxMapper.queryChdwxxByClcgxx(param);
                if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                    chdw = dchyXmglChxmChdwxxList.get(0).getChdwmc();
                    if (dchyXmglChxmChdwxxList.size() > 1) {
                        chdw += "???";
                    }
                }
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
            exchangeFeignServiceImpl.saveOrUpdateTaskExtendDto(actStProRelDo);
            //??????rmark??????
            exchangeFeignServiceImpl.updateRemark(gzlslid, remark.toString());
            mapResult.put("code", ResponseMessage.CODE.SUCCESS.getCode());
            mapResult.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("??????????????????", e);
            mapResult.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
            mapResult.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
        }
        return ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
    }

    @Override
    public Map<String, Object> delSqxx(String sqxxid) {
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(sqxxid)) {
            try {
                entityMapper.deleteByPrimaryKey(DchyXmglSqxx.class, sqxxid);
                map.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                map.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
            } catch (Exception e) {
                map.put("code", ResponseMessage.CODE.EXCEPTION_MGS.getCode());
                map.put("msg", ResponseMessage.CODE.EXCEPTION_MGS.getMsg());
                logger.error("????????????{}???", e);
            }
        } else {
            map.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            map.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }

        return map;
    }

    @Override
    public ResponseMessage queryXmxxByTaskid(Map<String, String> paramMap) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> clsxList = null;
        try {
            Map<String, Object> map = queryClsxListByTaskid(paramMap);
            clsxList = (List<Map<String, Object>>) map.get("clsxList");
            Map<String, Object> jdxx = (Map<String, Object>) map.get("jdxx");
            MapperParamUtil.formatRequiredStringParam(paramMap, "chxmid");
            List<Map<String, Object>> baxxList = dchyXmglChxmMapper.queryBaxxByChxmid(paramMap);
            DataSecurityUtil.decryptMapList(baxxList);
            MapperParamUtil.formatRequiredStringParam(paramMap, "chxmid");
            List<Map<String, Object>> chdwList = dchyXmglChxmMapper.queryChdwByChxmid(paramMap);
            resultMap.put("baxx", CollectionUtils.isNotEmpty(baxxList) ? baxxList.get(0) : Maps.newHashMap());
            resultMap.put("clsxList", clsxList);
            resultMap.put("jdxx", jdxx);
            resultMap.put("chdwList", chdwList);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", resultMap);
        } catch (Exception e) {
            logger.error("????????????:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @Override
    public ResponseMessage getShjlByTaskid(Map paramMap) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<DchyXmglClcgShjl> dchyXmglClcgShjlList = getAllShjlByTaskid(paramMap);

        if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
            DchyXmglClcgShjl dchyXmglClcgShjl = dchyXmglClcgShjlList.get(0);
            String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(paramMap, "taskid"));
            String dqjd = CommonUtil.formatEmptyValue(MapUtils.getString(paramMap, "shjdmc"));
            String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(paramMap, "xmid"));
            if (StringUtils.isNoneBlank(taskid, dqjd, xmid)) {
                ShxxParamDTO shxxParamDTO = new ShxxParamDTO();
                shxxParamDTO.setSfzfhdqjdxx(true);
                shxxParamDTO.setTaskid(taskid);
                shxxParamDTO.setUserid(UserUtil.getCurrentUserId());
                shxxParamDTO.setSignKey(dqjd);
                shxxParamDTO.setXmid(xmid);
                ShxxVO shxxVO = signService.updateShxx(shxxParamDTO);
                if (shxxVO != null && StringUtils.isNotBlank(shxxVO.getQmid())) {
                    resultMap.put("qmid", shxxVO.getQmid());
                    resultMap.put("qmsj", shxxVO.getQmsj());
                }
            }
            String clsx = "";
            for (DchyXmglClcgShjl dchyXmglClcgShjl1 : dchyXmglClcgShjlList) {
                if (StringUtils.equals(dchyXmglClcgShjl1.getShzt(), Constants.DCHY_XMGL_SHZT_TH)) {
                    dchyXmglClcgShjl = dchyXmglClcgShjl1;
                    DchyXmglClcg dchyXmglClcg = entityMapper.selectByPrimaryKey(DchyXmglClcg.class, dchyXmglClcgShjl1.getClcgid());
                    String clsxDm = dchyXmglClcg.getClsx();
                    clsx = clsxDm + ";";
                }
            }
            resultMap.put("clsx", clsx);
            resultMap.put("data", dchyXmglClcgShjl);
        }
        message = ResponseUtil.wrapSuccessResponse();
        message.getData().put("dataList", resultMap);
        return message;
    }

    @Override
    public List<DchyXmglClcgShjl> getAllShjlByTaskid(Map<String, String> paramMap) {
        List<DchyXmglClcgShjl> dchyXmglClcgShjlList = Lists.newArrayList();

        String taskid = CommonUtil.formatEmptyValue(paramMap.get("taskid"));
        String gzlslid = CommonUtil.formatEmptyValue(paramMap.get("gzlslid"));

        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("xmid"));
        PfWorkFlowInstanceVo flowInstanceVo = new PfWorkFlowInstanceVo();
        flowInstanceVo.setWorkflowIntanceId(gzlslid);
        WorkFlowXml workFlowXml = workFlowXmlUtil.getWorkFlowInstanceModel(flowInstanceVo);

        PfTaskVo taskVo = sysTaskService.getTask(taskid);
        PfActivityVo pfActivityVo = sysTaskService.getActivity(taskVo.getActivityId());
        ActivityModel activityModel = workFlowXml.getActivity(pfActivityVo.getActivityDefinitionId());
        String shjdmc = CommonUtil.formatEmptyValue(activityModel.getActivityDefineName());
        paramMap.put("shjdmc", shjdmc);

        List<DchyXmglClcg> dchyXmglClcgList = getClcgByTaskid(paramMap);

        if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
            for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                Example example = new Example(DchyXmglClcgShjl.class);
                example.createCriteria()
                        .andEqualTo("clcgid", dchyXmglClcg.getClcgid())
                        .andEqualTo("shjdmc", shjdmc)
                        .andEqualTo("sqxxid", sqxxid);
                List<DchyXmglClcgShjl> dchyXmglClcgShjlList1 = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList1)) {
                    dchyXmglClcgShjlList.addAll(dchyXmglClcgShjlList1);
                }
            }
        }

        return dchyXmglClcgShjlList;
    }

    @Override
    public List<DchyXmglClcg> getClcgByTaskid(Map<String, String> paramMap) {
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("xmid"));
        List<DchyXmglClcg> dchyXmglClcgList = Lists.newArrayList();
        Map<String, Object> map = queryClsxListByTaskid(paramMap);
        List<Map<String, Object>> clsxList = (List<Map<String, Object>>) map.get("clsxList");

        if (CollectionUtils.isNotEmpty(clsxList)) {
            for (Map<String, Object> clsxMap : clsxList) {
                String clsx = CommonUtil.formatEmptyValue(MapUtils.getString(clsxMap, "CLSX"));
                Example example = new Example(DchyXmglClcg.class);
                example.createCriteria().andEqualTo("clsx", clsx).andEqualTo("sqxxid", sqxxid);
                List<DchyXmglClcg> dchyXmglClcgs = entityMapper.selectByExample(example);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgs)) {
                    dchyXmglClcgList.addAll(dchyXmglClcgs);
                }
            }
        }
        return dchyXmglClcgList;
    }

    @Override
    public ResponseMessage checkFinish(Map paramMap) {
        int flag = 0;
        ResponseMessage message = new ResponseMessage();
        String taskid = CommonUtil.formatEmptyValue(paramMap.get("taskid"));
        String gzlslid = CommonUtil.formatEmptyValue(paramMap.get("gzlslid"));
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("xmid"));

        String shzt = CommonUtil.formatEmptyValue(paramMap.get("shzt"));
        String shyj = CommonUtil.formatEmptyValue(paramMap.get("shyj"));
        String clsx = CommonUtil.formatEmptyValue(paramMap.get("clsx"));
        String[] clsxs = clsx.split(";");
        List<String> clsxList = Lists.newArrayList();
        if (clsxs.length > 0) {
            Collections.addAll(clsxList, clsxs);
        }

        if (StringUtils.isNoneBlank(taskid, gzlslid, sqxxid)) {
            List<DchyXmglClcgShjl> dchyXmglClcgShjlList = getAllShjlByTaskid(paramMap);

            if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
                for (DchyXmglClcgShjl dchyXmglClcgShjl : dchyXmglClcgShjlList) {
                    String clcgid = dchyXmglClcgShjl.getClcgid();
                    String shjdmc = dchyXmglClcgShjl.getShjdmc();

                    dchyXmglClcgShjl.setClcgid(clcgid);
                    dchyXmglClcgShjl.setShzt(shzt);
                    dchyXmglClcgShjl.setShyj(shyj);
                    dchyXmglClcgShjl.setShsj(CalendarUtil.getCurHMSDate());
                    dchyXmglClcgShjl.setShr(UserUtil.getCurrentUserId());
                    dchyXmglClcgShjl.setShrmc(UserUtil.getUserNameById(UserUtil.getCurrentUserId()));
                    dchyXmglClcgShjl.setShjdmc(shjdmc);
                    dchyXmglClcgShjl.setSqxxid(sqxxid);

                    DchyXmglClcg dchyXmglClcg = entityMapper.selectByPrimaryKey(DchyXmglClcg.class, clcgid);
                    if (null != dchyXmglClcg) {
                        String clsxDm = dchyXmglClcg.getClsx();
                        //???????????????????????????????????????,?????????????????? 1001 1002    1003
                        if (StringUtils.equals(shzt, Constants.DCHY_XMGL_SHZT_TH)) {
                            if (clsxList.contains(clsxDm)) {
                                flag = entityMapper.saveOrUpdate(dchyXmglClcgShjl, dchyXmglClcgShjl.getShjlid());
                            }
                        } else {
                            //?????????????????????????????????????????????
                            entityMapper.saveOrUpdate(dchyXmglClcg, dchyXmglClcg.getClcgid());
                            flag = entityMapper.saveOrUpdate(dchyXmglClcgShjl, dchyXmglClcgShjl.getShjlid());
                        }
                    }
                    if (flag > 0) {
                        message = ResponseUtil.wrapSuccessResponse();
                    } else {
                        message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.OPERA_FAIL.getMsg(), ResponseMessage.CODE.OPERA_FAIL.getCode());
                        break;
                    }
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.OPERA_FAIL.getMsg(), ResponseMessage.CODE.OPERA_FAIL.getCode());
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_FAIL.getMsg(), ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return message;
    }

    @Override
    public Map<String, Object> queryClsxListByTaskid(Map<String, String> paramMap) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> clsxList = Lists.newArrayList();
        List<String> clsxZdList = Lists.newArrayList();
        String taskid = CommonUtil.formatEmptyValue(paramMap.get("taskid"));
        String gzlslid = CommonUtil.formatEmptyValue(paramMap.get("gzlslid"));
        String sqxxid = CommonUtil.formatEmptyValue(paramMap.get("xmid"));

        //??????sqxxid?????????????????????????????????
        List<String> clsxs = dchyXmglClcgMapper.queryClcgBysqxxidAndClcgmc(sqxxid, "");

        //???????????????taskid, gzlslid, sqxxid????????????????????????????????????
        if (StringUtils.isNoneBlank(taskid, gzlslid, sqxxid)) {
            Example exampleClcg = new Example(DchyXmglClcg.class);
            exampleClcg.createCriteria().andEqualTo("sqxxid", sqxxid);
            List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExample(DchyXmglClcg.class, exampleClcg);
            if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                DchyXmglClcg dchyXmglClcg = dchyXmglClcgList.get(0);
                paramMap.put("chxmid", dchyXmglClcg.getChxmid());
                paramMap.put("chgcid", dchyXmglClcg.getChgcid());
            }

            ActivityModel activityModel = getActivityModel(gzlslid, taskid);

            if (null != activityModel) {
                String chxmid = CommonUtil.formatEmptyValue(paramMap.get("chxmid"));
                String chgcid = CommonUtil.formatEmptyValue(paramMap.get("chgcid"));
                String shjdmc = CommonUtil.formatEmptyValue(activityModel.getActivityDefineName());
                Map<String, Object> jdxx = new HashMap<>();
                jdxx.put("CHXMID", chxmid);
                jdxx.put("CHGCID", chgcid);
                jdxx.put("SHJDMC", shjdmc);
                resultMap.put("jdxx", jdxx);
                String description = activityModel.getActivityDescription();
                if (StringUtils.isNotBlank(description)) {
                    String[] descriptions = StringUtils.split(description, ",");
                    for (int j = 0; j < descriptions.length; j++) {
                        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc("CLSX", descriptions[j]);
                        if (null != dchyXmglZd) {
                            String clsx = dchyXmglZd.getDm();
                            if (clsxs.contains(clsx)) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("CLSX", dchyXmglZd.getDm());
                                clsxList.add(map);
                                clsxZdList.add(dchyXmglZd.getDm());
                            }
                        }
                    }
                }
            }
            resultMap.put("clsxList", clsxList);
            resultMap.put("clsxZdList", clsxZdList);
        }
        return resultMap;
    }

    public ActivityModel getActivityModel(String gzlslid, String taskid) {
        PfWorkFlowInstanceVo flowInstanceVo = new PfWorkFlowInstanceVo();
        flowInstanceVo.setWorkflowIntanceId(gzlslid);
        WorkFlowXml workFlowXml = workFlowXmlUtil.getWorkFlowInstanceModel(flowInstanceVo);
        ActivityModel activityModel = null;
        PfTaskVo taskVo = sysTaskService.getTask(taskid);
        if (null != taskVo && null != workFlowXml) {
            PfActivityVo pfActivityVo = sysTaskService.getActivity(taskVo.getActivityId());
            activityModel = workFlowXml.getActivity(pfActivityVo.getActivityDefinitionId());
        }
        return activityModel;
    }

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/3/12 10:04
     * @description ??????code??????????????????????????????
     */
    @Override
    public String getCode() {
        return Constants.CGTJ_MODE_CHGC;
    }
}
