package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.core.support.mybatis.page.repository.Repo;
import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglCgccDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcsjsqDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglXxtxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.JcsjsqMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static cn.gtmap.msurveyplat.promanage.web.main.IndexController.getCurrentUserIds;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/8
 * @description 基础数据申请
 */
@Service
public class DchyXmglJcsjsqServiceImpl implements DchyXmglJcsjsqService {

    protected final Log logger = LogFactory.getLog(DchyXmglJcsjsqServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    PushDataToMqService pushDataToMqService;

    @Autowired
    private Repo repository;

    @Autowired
    private JcsjsqMapper jcsjsqMapper;

    @Autowired
    private ExchangeFeignServiceImpl dispatchOrderFsmServiceImpl;

    @Value("${jcsjsq.gzldyid}")
    private String gzldyid;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    @Override
    public String initSqxx() {
        return gzldyid;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseMessage initBasicDataApplication(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        String babh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "babh"));
        String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "gzlslid"));

        if (StringUtils.isNoneBlank(babh, sqxxid, gzlslid)) {
            //通过备案编号获取一条基础数据申请信息
            generateSqxxBybabh(babh, sqxxid);

            //通过申请信息id更新待办任务字段
            String slbh = "";
            String jsdw = "";
            String chdw = "";
            String xmbh = "";
            String xmmc = "";
            StringBuilder xmdz = new StringBuilder();
            String slr = "";
            // 上传
            if (StringUtils.isNoneBlank(gzlslid, sqxxid)) {
                DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid);
                if (null != dchyXmglSqxx) {
                    slbh = dchyXmglSqxx.getSqbh();
                    slr = dchyXmglSqxx.getSqrmc();
                }

                if (StringUtils.isNotBlank(dchyXmglSqxx.getGlsxid())) {
                    DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, dchyXmglSqxx.getGlsxid());
                    jsdw = dchyXmglJcsjSqxx.getJsdw();
                    chdw = dchyXmglJcsjSqxx.getChdwmc();
                    xmbh = dchyXmglJcsjSqxx.getXmbabh();
                    xmmc = dchyXmglJcsjSqxx.getGcmc();
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
            logger.info(JSONObject.toJSONString(actStProRelDo));
            dispatchOrderFsmServiceImpl.saveOrUpdateTaskExtendDto(actStProRelDo);
            //更新rmark字段
            dispatchOrderFsmServiceImpl.updateRemark(gzlslid, remark.toString());
            message = ResponseUtil.wrapSuccessResponse();
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }

        return message;
    }

    @Override
    public ResponseMessage applicationInfoView(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "taskid"));
        String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "gzlslid"));
        String processDefName = CommonUtil.formatEmptyValue(MapUtils.getString(map, "processDefName"));
        if (StringUtils.isNotBlank(xmid)) {
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);
            DchyXmglJcsjSqxx dchyXmglJcsjsqSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, dchyXmglSqxx.getGlsxid());
            String chxmid = dchyXmglJcsjsqSqxx.getChxmid();
            paramMap.put("chxmid", chxmid);
            List<Map<String, Object>> baxxList = jcsjsqMapper.querBaxxByChxmid(paramMap);
            for (Map<String, Object> baxx : baxxList) {
                String gcdz = "";
                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "XMZT"))) {
                    String xmzt = MapUtils.getString(baxx, "XMZT");
                    DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_ZDLX_XMZT, xmzt);
                    baxx.put("XMZT", gcdzssZd.getMc());
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZS"))) {
                    String gcdzs = MapUtils.getString(baxx, "GCDZS");
                    DchyXmglZd gcdzsZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzs);
                    gcdz += gcdzsZd.getMc();
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZSS"))) {
                    String gcdzss = MapUtils.getString(baxx, "GCDZSS");
                    DchyXmglZd gcdzssZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzss);
                    gcdz += gcdzssZd.getMc();
                }

                if (StringUtils.isNotBlank(MapUtils.getString(baxx, "GCDZQX"))) {
                    String gcdzqx = MapUtils.getString(baxx, "GCDZQX");
                    DchyXmglZd gcdzqxZd = dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_GCDZ, gcdzqx);
                    gcdz += gcdzqxZd.getMc();
                }
                baxx.put("GCDZ", gcdz);
            }
            resultMap.put("baxxList", baxxList);
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);

            paramMap.put("gcbh", dchyXmglChxm.getChgcbh());
            List<Map<String, Object>> baClsxList = jcsjsqMapper.queryBaClsxListByGcbh(paramMap);
            resultMap.put("baClsxList", baClsxList);

//            String glsxid = dchyXmglChxm.getBabh();

            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", processDefName);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
            DchyXmglSjcl dchyXmglSjcl = null;
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    dchyXmglSjcl = dchyXmglSjclList.get(0);
                }
            }
            resultMap.put("jcsjfw", dchyXmglSjcl);

            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public synchronized String resultsDelivery(Map<String, Object> map) {
        String wjzxid = "";
        String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        String lxdh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "lxdh"));
        String jsr = CommonUtil.formatEmptyValue(MapUtils.getString(map, "jsr"));
        if (StringUtils.isBlank(jsr)) {
            jsr = UserUtil.getCurrentUser().getUsername();
            map.put("jsr", jsr);
        }
        map.put("jssj", CalendarUtil.getCurHMSStrDate());
        DchyXmglCzrz dchyXmglCzrz = new DchyXmglCzrz();
        dchyXmglCzrz.setCzrzid(UUIDGenerator.generate18());
        dchyXmglCzrz.setGlsxid(sqxxid);
        dchyXmglCzrz.setCzr(UserUtil.getCurrentUser().getUsername());
        dchyXmglCzrz.setCzrid(UserUtil.getCurrentUser().getId());
        dchyXmglCzrz.setCzcs(JSONObject.toJSONString(map));
        dchyXmglCzrz.setCzlx(ProLog.CZLX_SAVE_CODE);
        dchyXmglCzrz.setCzlxmc(ProLog.CZLX_SAVE_MC);
        dchyXmglCzrz.setCzsj(CalendarUtil.getCurHMSDate());

        int flag = entityMapper.saveOrUpdate(dchyXmglCzrz, dchyXmglCzrz.getCzrzid());
        if (flag > 0) {
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", sqxxid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    DchyXmglSjcl dchyXmglSjcl = dchyXmglSjclList.get(0);
                    wjzxid = dchyXmglSjcl.getWjzxid();
                }
            }
        }
        return wjzxid;
    }

    @Override
    public ResponseMessage resultsDeliveryLogList(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> dchyXmglJcsjsqJfrzList = Lists.newArrayList();
        String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        if (StringUtils.isNotBlank(glsxid)) {
            Example example = new Example(DchyXmglCzrz.class);
            example.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglCzrz> dchyXmglCzrzList = entityMapper.selectByExample(example);
            if (CollectionUtils.isNotEmpty(dchyXmglCzrzList)) {
                for (DchyXmglCzrz dchyXmglCzrz : dchyXmglCzrzList) {
                    String czcs = dchyXmglCzrz.getCzcs();
                    Map<String, Object> mapJson = (Map) JSON.parse(czcs);
                    dchyXmglJcsjsqJfrzList.add(mapJson);
                }
            }
        }
        resultMap.put("data", dchyXmglJcsjsqJfrzList);
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(resultMap);
        return message;
    }

    @Override
    public ResponseMessage getProcessInfoBySlbh(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String slbh = CommonUtil.formatEmptyValue(MapUtils.getString(map, "slbh"));
        String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        if (StringUtils.isNotBlank(slbh)) {
            List<Map<String, Object>> processInfoList = dispatchOrderFsmServiceImpl.getProcessInfoBySlbh(slbh);

            if (CollectionUtils.isNotEmpty(processInfoList)) {
                for (Map<String, Object> processInfo : processInfoList) {
                    String hj = CommonUtil.formatFileName(MapUtils.getString(processInfo, "HJ"));
                    if (StringUtils.equals(Constants.DCHY_XMGL_JCSJSQ_HJ_SH, hj)) {
                        DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);
                        Example exampleShjl = new Example(DchyXmglJcsjShjl.class);
                        exampleShjl.createCriteria().andEqualTo("jcsjsqxxid", dchyXmglSqxx.getGlsxid());
                        List<DchyXmglJcsjShjl> dchyXmglJcsjShjlList = entityMapper.selectByExample(exampleShjl);
                        if (CollectionUtils.isNotEmpty(dchyXmglJcsjShjlList)) {
                            DchyXmglJcsjShjl dchyXmglJcsjShjl = dchyXmglJcsjShjlList.get(0);
                            processInfo.put("BLJG", convertShjg(dchyXmglJcsjShjl.getShjg()));
                            processInfo.put("YJ", dchyXmglJcsjShjl.getShyj());
                        }
                    }
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", processInfoList);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode(), ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return message;
    }

    @Override
    public synchronized ResponseMessage saveCheckOpinion(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "xmid"));
        String shyj = CommonUtil.formatEmptyValue(MapUtils.getString(map, "shyj"));
        String shjg = CommonUtil.formatEmptyValue(MapUtils.getString(map, "shjg"));
        String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(map, "taskid"));
        if (StringUtils.isNoneBlank(xmid, shjg)) {
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);
            String sqxxid = dchyXmglSqxx.getGlsxid();
            DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, sqxxid);
            dchyXmglJcsjSqxx.setShr(UserUtil.getCurrentUser().getUsername());
            dchyXmglJcsjSqxx.setShrid(UserUtil.getCurrentUser().getId());
            dchyXmglJcsjSqxx.setShsj(CalendarUtil.getCurHMSDate());
            DchyXmglJcsjShjl dchyXmglJcsjShjl = new DchyXmglJcsjShjl();
            dchyXmglJcsjShjl.setShjlid(UUIDGenerator.generate18());
            dchyXmglJcsjShjl.setJcsjsqxxid(sqxxid);
            dchyXmglJcsjShjl.setShjg(shjg);
            dchyXmglJcsjShjl.setShrid(UserUtil.getCurrentUser().getId());
            dchyXmglJcsjShjl.setShr(UserUtil.getCurrentUser().getUsername());
            dchyXmglJcsjShjl.setShsj(CalendarUtil.getCurHMSDate());
            dchyXmglJcsjShjl.setShyj(shyj);
            if (null == dchyXmglJcsjSqxx) {
                dchyXmglJcsjSqxx = new DchyXmglJcsjSqxx();
                dchyXmglJcsjSqxx.setJcsjsqxxid(sqxxid);
            }
            if (StringUtils.equals(shjg, Constants.SHTG)) {
                dchyXmglJcsjSqxx.setDqzt(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_DZZ);
            } else {
                dchyXmglJcsjSqxx.setDqzt(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_YTH);
            }

            DchyXmglJcsjJdxx dchyXmglJcsjJdxx = new DchyXmglJcsjJdxx();
            dchyXmglJcsjJdxx.setJdxxid(UUIDGenerator.generate18());
            dchyXmglJcsjJdxx.setJcsjsqxxid(sqxxid);
            dchyXmglJcsjJdxx.setBjjg(shjg);
            dchyXmglJcsjJdxx.setBjyj(shyj);
            dchyXmglJcsjJdxx.setBjry(UserUtil.getCurrentUser().getUsername());
            dchyXmglJcsjJdxx.setBjryid(UserUtil.getCurrentUserId());
            dchyXmglJcsjJdxx.setBjsj(CalendarUtil.getCurHMSDate());
            dchyXmglJcsjJdxx.setHj(Constants.DCHY_XMGL_JCSJSQ_HJ_SH);

            int flag1 = entityMapper.saveOrUpdate(dchyXmglJcsjShjl, dchyXmglJcsjShjl.getShjlid());
            int flag2 = entityMapper.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());
            if (flag1 > 0 && flag2 > 0) {
                entityMapper.saveOrUpdate(dchyXmglJcsjJdxx, dchyXmglJcsjJdxx.getJdxxid());
                message = ResponseUtil.wrapSuccessResponse();

                //审核的时候推送,sqxxid,基础数据sqxx,进度信息,审核记录信息
                DchyXmglJcsjsqDto dchyXmglJcsjsqDto = new DchyXmglJcsjsqDto();
                dchyXmglJcsjsqDto.setDchyXmglSqxx(dchyXmglSqxx);
                dchyXmglJcsjsqDto.setDchyXmglJcsjSqxx(dchyXmglJcsjSqxx);
                dchyXmglJcsjsqDto.setDchyXmglJcsjJdxx(dchyXmglJcsjJdxx);
                dchyXmglJcsjsqDto.setDchyXmglJcsjShjl(dchyXmglJcsjShjl);
                pushDataToMqService.pushJcsjResultTo(dchyXmglJcsjsqDto);
            } else {
                message = ResponseUtil.wrapExceptionResponse();
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    @Override
    public ResponseMessage queryDbSqxxList(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        HashMap mapParam = Maps.newHashMap();
        //获取流程中的待办列表的参数
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

        String userIds = getCurrentUserIds();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            map.put("userIds", userIds.split(","));
        }
        map.put("lcmc", Constants.DCHY_XMGL_JCSJSQ_LCMC);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //组织参数获取待办列表的数据
        Map<String, Object> result = dispatchOrderFsmServiceImpl.getTaskList(mapParam);
        List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");

        if (CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> m1 : content) {
                String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "text9"));
                String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "taskId"));
                String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "procInsId"));
                //通过sqxxid获取与之关联的基础数据申请信息
                if (StringUtils.isNotBlank(xmid)) {
                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);
                    if (null != dchyXmglSqxx && StringUtils.isNotBlank(dchyXmglSqxx.getGlsxid())) {
                        String jcsjsqxxid = dchyXmglSqxx.getGlsxid();
                        Map<String, Object> map1 = Maps.newHashMap();
                        map1.put("jcsjsqxxid", jcsjsqxxid);
                        List<Map<String, Object>> sqxxList = jcsjsqMapper.querySqxxListByPage(map1);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            Map<String, Object> map2 = sqxxList.get(0);
                            if (StringUtils.equals(CommonUtil.formatEmptyValue(MapUtils.getString(map2, "SQFSDM")), Constants.DCHY_XMGL_SQFS_XS)) {
                                dchyXmglSqxx.setXssqbh(CommonUtil.formatEmptyValue(MapUtils.getString(map2, "TEXT1")));
                                entityMapper.saveOrUpdate(dchyXmglSqxx, xmid);
                            }
                            m1.putAll(map2);
                        }
                    }
                }
            }
        }
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(result);
        return message;
    }

    @Override
    public ResponseMessage queryYbSqxxList(Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();

        int page = Integer.parseInt(map.get("page") != null ? map.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(map.get("size") != null ? map.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);

        HashMap mapParam = Maps.newHashMap();
        //获取流程中的待办列表的参数
        Pageable pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return page;
            }

            @Override
            public int getPageSize() {
                return pageSize;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };

        String userIds = getCurrentUserIds();
        if (StringUtils.isBlank(userIds)) {
            map.put("userIds", null);
        } else {
            map.put("userIds", userIds.split(","));
        }
        map.put("lcmc", Constants.DCHY_XMGL_JCSJSQ_LCMC);
        mapParam.put("pageable", pageable);
        mapParam.put("param", map);
        //组织参数获取已办列表的数据
        Map<String, Object> result = dispatchOrderFsmServiceImpl.getTaskOverList(mapParam);
        List<Map<String, Object>> content = (List<Map<String, Object>>) result.get("content");

        if (CollectionUtils.isNotEmpty(content)) {
            for (Map<String, Object> m1 : content) {
                String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "text9"));
                String taskid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "taskId"));
                String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(m1, "procInsId"));
                //通过sqxxid获取与之关联的基础数据申请信息
                if (StringUtils.isNotBlank(xmid)) {
                    DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, xmid);

                    if (null != dchyXmglSqxx && StringUtils.isNotBlank(dchyXmglSqxx.getGlsxid())) {
                        String jcsjsqxxid = dchyXmglSqxx.getGlsxid();
                        Map<String, Object> map1 = Maps.newHashMap();
                        map1.put("jcsjsqxxid", jcsjsqxxid);
                        List<Map<String, Object>> sqxxList = jcsjsqMapper.querySqxxListByPage(map1);
                        if (CollectionUtils.isNotEmpty(sqxxList)) {
                            Map<String, Object> map2 = sqxxList.get(0);
                            if (StringUtils.equals(CommonUtil.formatEmptyValue(MapUtils.getString(map2, "SQFSDM")), Constants.DCHY_XMGL_SQFS_XS)) {
                                dchyXmglSqxx.setXssqbh(CommonUtil.formatEmptyValue(MapUtils.getString(map2, "TEXT1")));
                                entityMapper.saveOrUpdate(dchyXmglSqxx, xmid);
                            }
                            m1.putAll(map2);
                        }
                    }
                }
            }
        }
        message = ResponseUtil.wrapSuccessResponse();
        message.setData(result);
        return message;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void generateSqxxBybabh(String babh, String sqxxid) {
        DchyXmglJcsjSqxx dchyXmglJcsjSqxx = new DchyXmglJcsjSqxx();
        if (StringUtils.isNotBlank(babh)) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("babh", babh);
            List<Map<String, Object>> baxxList = jcsjsqMapper.queryBaxxByBabh(paramMap);
            if (CollectionUtils.isNotEmpty(baxxList)) {
                Map<String, Object> resultMap = baxxList.get(0);
                String chxmid = CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "CHXMID"));
                dchyXmglJcsjSqxx.setJcsjsqxxid(UUIDGenerator.generate18());
                dchyXmglJcsjSqxx.setBabh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "SLBH")));
                dchyXmglJcsjSqxx.setXmbabh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "BABH")));
                dchyXmglJcsjSqxx.setChxmid(chxmid);
                dchyXmglJcsjSqxx.setDqzt(Constants.DCHY_XMGL_JCSJSQ_DQSQZT_SHZ);
                dchyXmglJcsjSqxx.setGcbh(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "GCBH")));
                dchyXmglJcsjSqxx.setGcmc(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "GCMC")));
                dchyXmglJcsjSqxx.setJsdw(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "WTDW")));
                dchyXmglJcsjSqxx.setChdwmc(CommonUtil.formatEmptyValue(MapUtils.getString(resultMap, "CHDWMC")));
                dchyXmglJcsjSqxx.setSqfs(Constants.DCHY_XMGL_SQFS_XX);
                dchyXmglJcsjSqxx.setSqsj(CalendarUtil.getCurHMSDate());

                DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid);
                if (null == dchyXmglSqxx) {
                    dchyXmglSqxx = new DchyXmglSqxx();
                    dchyXmglSqxx.setSqxxid(sqxxid);
                }
                dchyXmglSqxx.setGlsxid(dchyXmglJcsjSqxx.getJcsjsqxxid());
                dchyXmglSqxx.setXssqxxid(UUIDGenerator.generate18());
                entityMapper.saveOrUpdate(dchyXmglSqxx, sqxxid);
                int flag = entityMapper.saveOrUpdate(dchyXmglJcsjSqxx, dchyXmglJcsjSqxx.getJcsjsqxxid());

                if (flag > 0) {
                    //初始化申请信息的时候推送,sqxxid,基础数据sqxx,
                    DchyXmglJcsjsqDto dchyXmglJcsjsqDto = new DchyXmglJcsjsqDto();
                    dchyXmglJcsjsqDto.setDchyXmglSqxx(dchyXmglSqxx);
                    dchyXmglJcsjsqDto.setDchyXmglJcsjSqxx(dchyXmglJcsjSqxx);

                    pushDataToMqService.pushJcsjResultTo(dchyXmglJcsjsqDto);
                }
            }
        }
    }

    /**
     * 成查随机抽查
     *
     * @param data
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public List<Map<String, Object>> resultsSpotRandCheck(Map<String, Object> data) {
        /*待抽查数量*/
        int num = Integer.parseInt(data.get("num") != null ? data.get("num").toString() : Constants.DCHY_XMGL_CGCC_MRTS);
        List<Map<String, Object>> resultMap = new ArrayList<>();
        try {
            /*随机抽查指定条数据*/
            List<Map<String, Object>> mapList = jcsjsqMapper.queryBasicDataApplicationInfoByPage(num);
            if (CollectionUtils.isNotEmpty(mapList)) {
                for (Map<String, Object> map : mapList) {
                    /*更新chxm的抽查次数*/
                    String chxmid = MapUtils.getString(map, "CHXMID");
                    DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
                    if (null != xmglChxm) {
                        Integer ccnum = xmglChxm.getCcnum();
                        if (null == ccnum) {
                            ccnum = 0;
                        }
                        xmglChxm.setCcnum(ccnum + 1);//抽查次数+1
                        int result = entityMapper.saveOrUpdate(xmglChxm, xmglChxm.getChxmid());
                        if (result > 0) {
                            /*生成抽查记录*/
                            DchyXmglCgcc dchyXmglCgcc = this.generateCgccRecord(map, chxmid);
                            int i = entityMapper.saveOrUpdate(dchyXmglCgcc, dchyXmglCgcc.getCgccid());
                            if (i > 0) {
                                resultMap.add((Map<String, Object>) JSONObject.toJSON(dchyXmglCgcc));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }

        return resultMap;
    }

    /**
     * 抽查评价详情
     *
     * @param data
     * @return
     */
    @Override
    public List<Map<String, Object>> spotCheckEvaluationDetails(Map<String, Object> data) {
        String cgccid = CommonUtil.ternaryOperator(data.get("cgccid"));
        List<Map<String, Object>> resultMap = new ArrayList<>();
        if (StringUtils.isNotBlank(cgccid)) {
            DchyXmglCgcc dchyXmglCgcc = entityMapper.selectByPrimaryKey(DchyXmglCgcc.class, cgccid);
            if (null != dchyXmglCgcc) {
                resultMap.add((Map<String, Object>) JSONObject.toJSON(dchyXmglCgcc));
            }
        }
        return resultMap;
    }

    @Override
    public Page<Map<String, Object>> queryResultsSpotCheckList(Map<String, Object> data) {
        int page = Integer.parseInt(data.get("page") != null ? data.get("page").toString() : Constants.DCHY_XMGL_PAGINATION_PAGE);
        int pageSize = Integer.parseInt(data.get("size") != null ? data.get("size").toString() : Constants.DCHY_XMGL_PAGINATION_SIZE);
        String babh = CommonUtil.ternaryOperator(data.get("babh"));
        String xmbabh = CommonUtil.ternaryOperator(data.get("xmbabh"));
        String gcmc = CommonUtil.ternaryOperator(data.get("gcmc"));
        String gcbh = CommonUtil.ternaryOperator(data.get("gcbh"));
        String chdw = CommonUtil.ternaryOperator(data.get("chdw"));
        String pjzt = CommonUtil.ternaryOperator(data.get("pjzt"));
        /*组织查询参数*/
        Map<String, Object> param = new HashMap<>();
        param.put("babh", babh);
        param.put("xmbabh", xmbabh);
        param.put("gcmc", gcmc);
        param.put("gcbh", gcbh);
        param.put("chdw", chdw);
        param.put("pjzt", pjzt);
        try {
            Page<Map<String, Object>> checkListByPage = repository.selectPaging("queryResultsSpotCheckListByPage", data, page - 1, pageSize);
            if (null != checkListByPage) {
                List<Map<String, Object>> content = checkListByPage.getContent();
                if (CollectionUtils.isNotEmpty(content)) {
                    for (Map<String, Object> map : content) {
                        String tempSfsd = MapUtils.getString(map, "SFSD");//是否首单
                        if (StringUtils.isNotBlank(tempSfsd)) {
                            if (StringUtils.equals("1", tempSfsd)) {
                                map.put("SFSDMC", "是");
                            } else {
                                map.put("SFSDMC", "否");
                            }
                        }
                        String tempPjzt = MapUtils.getString(map, "PJZT");//评价状态
                        if (StringUtils.isNotBlank(tempPjzt)) {
                            map.put("PJZTMC", dchyXmglZdService.getDchyXmglByZdlxAndDm(Constants.DCHY_XMGL_CHXM_PJZT, tempPjzt).getMc());
                        }

                    }
                }
                return checkListByPage;
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }
        return null;
    }


    /**
     * 成果评价
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int achievementEvaluation(Map<String, Object> data) {
        DchyXmglCgccDto dchyXmglCgccDto = new DchyXmglCgccDto();
        int result = 0;
        String cgccid = CommonUtil.ternaryOperator(data.get("cgccid"));
        String cgpj = CommonUtil.ternaryOperator(data.get("cgpj"));//成果评价
        String pjyj = CommonUtil.ternaryOperator(data.get("pjyj"));//评价意见
        if (StringUtils.isNotBlank(cgccid)) {
            DchyXmglCgcc xmglCgcc = entityMapper.selectByPrimaryKey(DchyXmglCgcc.class, cgccid);
            if (null != xmglCgcc) {
                xmglCgcc.setCgpj(cgpj);//成果评价
                xmglCgcc.setPjyj(pjyj);//评价意见
                xmglCgcc.setPjzt(Constants.DCHY_XMGL_JCSJSQ_PJZT_YPJ);//1：已评价
                xmglCgcc.setPjsj(new Date());//评价时间
                xmglCgcc.setPjr(UserUtil.getCurrentUser().getUsername());//评价人
                xmglCgcc.setPjrid(UserUtil.getCurrentUserId());//评价人id
                /*保存*/
                result = entityMapper.saveOrUpdate(xmglCgcc, xmglCgcc.getCgccid());
                if (result > 0) {
                    dchyXmglCgccDto.setGlsxid(xmglCgcc.getCgccid());
                    dchyXmglCgccDto.setDchyXmglCgcc(xmglCgcc);
                    //根据glsxid获取收件材料的文件
                    dchyXmglCgccDto.setCgccPjclList(queryCgccPjclInfoByGlsxid(cgccid));
                    //线下数据推送到线上
                    pushDataToMqService.pushCgccResultToMq(dchyXmglCgccDto);
                }
            }
        }
        return result;
    }

    /**
     * 更新抽查表
     *
     * @param cgccid
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateCgcc(String cgccid) {
        if (StringUtils.isNotBlank(cgccid)) {
            DchyXmglCgcc xmglCgcc = entityMapper.selectByPrimaryKey(DchyXmglCgcc.class, cgccid);
            if (null != xmglCgcc) {
                /*更新抽查时间与评价状态*/
                xmglCgcc.setPjzt(Constants.DCHY_XMGL_JCSJSQ_PJZT_DPJ);//待评价
                //xmglCgcc.setCcsj(new Date()); //抽查时间为当前时间
                entityMapper.saveOrUpdate(xmglCgcc, xmglCgcc.getCgccid());
            }
        }
    }

    /**
     * @param sqxxid
     * @param yhxxpzid
     * @return
     * @description 2021/6/5 基础数据申请过程中,制作完成和交付完提醒用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public void jcsjsqXxtx(String sqxxid, String yhxxpzid) {
        logger.info("**************基础数据申请的消息提醒的入参sqxxid************"+sqxxid);
        logger.info("**************基础数据申请的消息提醒的入参yhxxpzid************"+yhxxpzid);
        DchyXmglXxtxDto dchyXmglXxtxDto = new DchyXmglXxtxDto();
        List<DchyXmglYhxx> dchyXmglYhxxList = Lists.newArrayList();
        DchyXmglYhxx dchyXmglYhxx = new DchyXmglYhxx();

        if (StringUtils.isNoneBlank(sqxxid, yhxxpzid)) {
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid);
            DchyXmglJcsjSqxx dchyXmglJcsjSqxx = entityMapper.selectByPrimaryKey(DchyXmglJcsjSqxx.class, dchyXmglSqxx.getGlsxid());

            Example example = new Example(DchyXmglCzrz.class);
            example.createCriteria().andEqualTo("glsxid", sqxxid);
            if (null != dchyXmglJcsjSqxx) {
                String chdwmc = dchyXmglJcsjSqxx.getChdwmc();
                String chxmid = dchyXmglJcsjSqxx.getChxmid();
                String babh = dchyXmglJcsjSqxx.getXmbabh();
                DchyXmglYhxxPz dchyXmglYhxxpz = entityMapper.selectByPrimaryKey(DchyXmglYhxxPz.class, yhxxpzid);
                logger.info("**************基础数据申请的消息提醒的配置项dchyXmglYhxxpz************"+JSON.toJSONString(dchyXmglYhxxpz));
                String xxnr = dchyXmglYhxxpz.getXxnr().replaceAll("xmbh", CommonUtil.formatEmptyValue(babh)).replaceAll("chdwmc", CommonUtil.formatEmptyValue(chdwmc));
                dchyXmglYhxx.setFsyhid(UserUtil.getCurrentUserId());
                dchyXmglYhxx.setFssj(CalendarUtil.getCurHMSDate());
                dchyXmglYhxx.setDqzt(Constants.INVALID);//未读
                dchyXmglYhxx.setYhxxid(UUIDGenerator.generate18());
                dchyXmglYhxx.setJsyhid(queryMlkidByDwmc(chdwmc));
                dchyXmglYhxx.setGlsxid(chxmid);
                dchyXmglYhxx.setXxnr(xxnr); //消息内容
                dchyXmglYhxx.setXxlx(dchyXmglYhxxpz.getXxlx()); //消息类型
                dchyXmglYhxx.setSftz(dchyXmglYhxxpz.getSftz()); //是否跳转

                dchyXmglYhxxList.add(dchyXmglYhxx);
                dchyXmglXxtxDto.setDchyXmglYhxxList(dchyXmglYhxxList);
                logger.info("**************基础数据申请的消息提醒的推送的实体dchyXmglXxtxDto************"+JSON.toJSONString(dchyXmglXxtxDto));
                pushDataToMqService.pushXxtxResultTo(dchyXmglXxtxDto);
            }
        }
    }

    /**
     * @param dwmc
     * @return
     * @description 2021/6/5 通过单位名称获取mlkid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public String queryMlkidByDwmc(String dwmc) {
        String mlkid = "";
        if (StringUtils.isNotBlank(dwmc)) {
            Example example = new Example(DchyXmglMlk.class);
            example.createCriteria().andEqualTo("dwmc", dwmc);
            List<DchyXmglMlk> dchyXmglMlkList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglMlkList)) {
                mlkid = dchyXmglMlkList.get(0).getMlkid();
            }
        }
        return mlkid;
    }

    /**
     * 生成抽查记录
     *
     * @param map
     * @param chxmid
     * @return
     */
    private DchyXmglCgcc generateCgccRecord(Map<String, Object> map, String chxmid) {
        /*生成抽查记录*/
        DchyXmglCgcc dchyXmglCgcc = new DchyXmglCgcc();
        dchyXmglCgcc.setCgccid(UUIDGenerator.generate18());
        dchyXmglCgcc.setBabh(MapUtils.getString(map, "SLBH"));
        dchyXmglCgcc.setGcbh(MapUtils.getString(map, "GCBH"));
        dchyXmglCgcc.setGcmc(MapUtils.getString(map, "GCMC"));
        dchyXmglCgcc.setJsdw(MapUtils.getString(map, "WTDW"));
        dchyXmglCgcc.setChxmid(chxmid);
        dchyXmglCgcc.setChdwmc(MapUtils.getString(map, "CHDWMC"));
        dchyXmglCgcc.setCgpj("");//成果评价
        dchyXmglCgcc.setPjyj("");//评价意见
        dchyXmglCgcc.setSfsd("0");//是否首单
        dchyXmglCgcc.setPjzt(Constants.DCHY_XMGL_JCSJSQ_PJZT_DCC);//2：待抽查
        dchyXmglCgcc.setCcr(UserUtil.getCurrentUser().getUsername());//抽查人
        dchyXmglCgcc.setCcrid(UserUtil.getCurrentUserId());//抽查人id
        dchyXmglCgcc.setCcsj(new Date());//抽查时间
        dchyXmglCgcc.setCjsj(new Date());//创建时间
        dchyXmglCgcc.setPjsj(null);//评价时间
        dchyXmglCgcc.setCjr(UserUtil.getCurrentUser().getUsername());//创建人
        dchyXmglCgcc.setCjrid(UserUtil.getCurrentUserId());//创建人id
        dchyXmglCgcc.setPjr("");//评价人
        dchyXmglCgcc.setPjrid("");//评价人id
        return dchyXmglCgcc;
    }

    private Map<String, Object> queryFilesbyWjzxid(String glsxid, int wjzxid) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(glsxid, Base64.getEncoder().encodeToString((byte[]) FileDownoadUtil.downLoadFile(wjzxid).get("wjnr")));
        return map;
    }

    private List<Map<String, Object>> queryCgccPjclInfoByGlsxid(String glsxid) {
        List<Map<String, Object>> fileList = Lists.newArrayList();
        Example exampleSjxx = new Example(DchyXmglSjxx.class);
        exampleSjxx.createCriteria().andEqualTo("glsxid", glsxid);
        List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExample(exampleSjxx);
        if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
            for (DchyXmglSjxx dchyXmglSjxx : dchyXmglSjxxList) {
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExample(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                        String wjzxid = dchyXmglSjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            int nodeid = Integer.parseInt(wjzxid);
                            Map<String, Object> resultMap = queryFilesbyWjzxid(glsxid, nodeid);
                            resultMap.put("mlmc", dchyXmglSjcl.getClmc());
                            fileList.add(resultMap);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    private static String convertShjg(String shjg) {
        if (StringUtils.equals(shjg, Constants.SHTG)) {
            return "通过";
        } else {
            return "不通过";
        }
    }
}