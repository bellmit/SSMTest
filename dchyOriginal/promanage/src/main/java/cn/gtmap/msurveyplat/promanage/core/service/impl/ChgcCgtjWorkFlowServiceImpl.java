package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.WfEventDto;
import cn.gtmap.msurveyplat.common.util.HttpClientUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglSqxxService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.WorkFlowService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXml;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXmlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.model.ActivityModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service("chgcCgtjServiceImpl")
public class ChgcCgtjWorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private WorkFlowXmlUtil workFlowXmlUtil;
    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    private DchyXmglSqxxService dchyXmglSqxxService;
    @Autowired
    private SysTaskService sysTaskService;

    private Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean lczfcl(WfEventDto wfEventDto) {
        if (wfEventDto != null && StringUtils.isNoneBlank(wfEventDto.getGzldyid(), wfEventDto.getGzljdid(), wfEventDto.getXmid())) {
            String[] gzljgids = StringUtils.split(wfEventDto.getGzljdid(), ",");
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, wfEventDto.getXmid());
            List<DchyXmglClcgShjl> dchyXmglClcgShjlList = Lists.newArrayList();
            if (dchyXmglSqxx != null) {
                Example example = new Example(DchyXmglClcg.class);
                example.createCriteria().andEqualTo("sqxxid", dchyXmglSqxx.getSqxxid());
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExampleNotNull(example);
                List clsxList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    // 测量事项对应的测量成果文件分类，方便获取
                    Map<String, List<DchyXmglClcg>> clcg2Clsx = Maps.newHashMap();
                    for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                        List<DchyXmglClcg> temp = (List<DchyXmglClcg>) MapUtils.getObject(clcg2Clsx, dchyXmglClcg.getClsx(), Lists.newArrayList());
                        temp.add(dchyXmglClcg);
                        clcg2Clsx.put(dchyXmglClcg.getClsx(), temp);
                    }
                    // 流程信息获取
                    PfWorkFlowDefineVo pfWorkFlowDefineVo = new PfWorkFlowDefineVo();
                    pfWorkFlowDefineVo.setWorkflowDefinitionId(wfEventDto.getGzldyid());
                    WorkFlowXml workFlowXml = workFlowXmlUtil.getDefineModel(pfWorkFlowDefineVo);
                    if (workFlowXml != null) {
                        LinkedHashSet clsxidList = Sets.newLinkedHashSet();
                        // 循环处理工作流转发记录，每个测量成果单独生成一条审核记录
                        for (int i = 0; i < gzljgids.length; i++) {
                            clsxList.clear();
                            // 获取节点 信息
                            ActivityModel activityModel = workFlowXml.getActivity(gzljgids[i]);
                            if (activityModel != null) {
                                // 节点备注，标识该节点审核的测量成果文件对应的测量事项名称
                                String description = activityModel.getActivityDescription();
                                if (StringUtils.isNotBlank(description)) {
                                    String[] descriptions = StringUtils.split(description, ",");
                                    for (int j = 0; j < descriptions.length; j++) {
                                        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc("CLSX", descriptions[j]);
                                        if (dchyXmglZd != null) {
                                            List<DchyXmglClcg> temp = clcg2Clsx.get(dchyXmglZd.getDm());
                                            if (CollectionUtils.isNotEmpty(temp)) {
                                                for (DchyXmglClcg dchyXmglClcg : temp) {
                                                    // 初始化成果审核记录信息
                                                    clsxidList.add(dchyXmglClcg.getClsxid());
                                                    DchyXmglClcgShjl dchyXmglClcgShjl = new DchyXmglClcgShjl();
                                                    dchyXmglClcgShjl.setClcgid(dchyXmglClcg.getClcgid());
                                                    dchyXmglClcgShjl.setShjlid(UUIDGenerator.generate18());
                                                    dchyXmglClcgShjl.setShjdmc(activityModel.getActivityDefineName());
                                                    dchyXmglClcgShjl.setShzt(Constants.DCHY_XMGL_SHZT_SHZ);
                                                    dchyXmglClcgShjl.setSqxxid(dchyXmglSqxx.getSqxxid());
                                                    dchyXmglClcgShjl.setZrsj(new Date());
                                                    dchyXmglClcgShjlList.add(dchyXmglClcgShjl);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // 已转发审核信息，此时修改测量事项对应的成果提交状态数据，改为审核中
                        if (CollectionUtils.isNotEmpty(clsxidList)) {
                            DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                            dchyXmglChxmClsx.setCgtjzt(Constants.DCHY_XMGL_XMCGZT_SHZ);
                            Example example1 = new Example(DchyXmglChxmClsx.class);
                            example1.createCriteria().andIn("clsxid", Lists.newArrayList(clsxidList));
                            entityMapper.updateByExampleSelective(dchyXmglChxmClsx, example1);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
                entityMapper.insertBatchSelective(dchyXmglClcgShjlList);
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean changeSqzt(WfEventDto wfEventDto, String sqzt, String shzt) {
        if (wfEventDto != null && StringUtils.isNoneBlank(wfEventDto.getGzldyid(), wfEventDto.getGzljdid(), wfEventDto.getXmid())) {
            String[] gzljgids = StringUtils.split(wfEventDto.getGzljdid(), ",");
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, wfEventDto.getXmid());
            if (dchyXmglSqxx != null) {
                dchyXmglSqxx.setSqzt(sqzt);

                // 修改成果记录审核状态
                List clsxList = Lists.newArrayList();
                PfWorkFlowDefineVo pfWorkFlowDefineVo = new PfWorkFlowDefineVo();
                pfWorkFlowDefineVo.setWorkflowDefinitionId(wfEventDto.getGzldyid());
                WorkFlowXml workFlowXml = workFlowXmlUtil.getDefineModel(pfWorkFlowDefineVo);
                List jdmcList = Lists.newArrayList();
                if (workFlowXml != null) {
                    for (int i = 0; i < gzljgids.length; i++) {
                        clsxList.clear();
                        ActivityModel activityModel = workFlowXml.getActivity(gzljgids[i]);
                        jdmcList.add(activityModel.getActivityDefineName());
                        if (activityModel != null) {
                            String description = activityModel.getActivityDescription();
                            if (StringUtils.isNotBlank(description)) {
                                String[] descriptions = StringUtils.split(description, ",");
                                for (int j = 0; j < descriptions.length; j++) {
                                    DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndMc("CLSX", descriptions[j]);
                                    if (dchyXmglZd != null) {
                                        clsxList.add(dchyXmglZd.getDm());
                                    }
                                }
                            }
                        }
                    }
                }

                Example example = new Example(DchyXmglClcg.class);
                List<DchyXmglClcg> dchyXmglClcgList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(clsxList)) {
                    example.createCriteria().andEqualTo("sqxxid", dchyXmglSqxx.getSqxxid()).andIn("clsx", clsxList);
                    dchyXmglClcgList = entityMapper.selectByExampleNotNull(example);
                }
                List clcgidList = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    for (DchyXmglClcg dchyXmglClcg : dchyXmglClcgList) {
                        dchyXmglClcg.setShzt(shzt);
                        clcgidList.add(dchyXmglClcg.getClcgid());
                    }
                    entityMapper.batchSaveSelective(dchyXmglClcgList);
                }

                if (CollectionUtils.isNotEmpty(clcgidList)) {
                    Example dchyXmglClcgShjlExample = new Example(DchyXmglClcgShjl.class);
                    dchyXmglClcgShjlExample.createCriteria().andIn("shjdmc", jdmcList).andIn("clcgid", clcgidList);
                    List<DchyXmglClcgShjl> dchyXmglClcgShjlList = entityMapper.selectByExampleNotNull(dchyXmglClcgShjlExample);
                    if (CollectionUtils.isNotEmpty(dchyXmglClcgShjlList)) {
                        for (DchyXmglClcgShjl dchyXmglClcgShjl : dchyXmglClcgShjlList) {
                            dchyXmglClcgShjl.setShzt(shzt);
                        }
                        entityMapper.batchSaveSelective(dchyXmglClcgShjlList);
                    }
                }

                entityMapper.saveOrUpdate(dchyXmglSqxx, dchyXmglSqxx.getSqxxid());
            }
        }
        return false;
    }

    @Override
    public DchyXmglSqxx initDchySqxx(InitDataParamDTO initDataParamDTO) {
        return dchyXmglSqxxService.initSqxx(initDataParamDTO);
    }

    @Override
    public boolean deleteDchySqxx(String gzldyid, String xmid) {
        logger.info("delXmid-- " + xmid);
        //1.删除申请信息
        //2.删除成果信息
        //3.删除成果提交记录
        //4.删除收件信息
        //5.删除收件材料
        //6.删除文件中心文件信息
        return false;
    }

    @Override
    public boolean qrbzdbj(WfEventDto wfEventDto) throws IOException {
        boolean yxbj = false;
        if (null != wfEventDto && StringUtils.isNotBlank(wfEventDto.getXmid())) {
            String xmid = wfEventDto.getXmid();
            Example example = new Example(DchyXmglClcgShjl.class);
            example.createCriteria().andEqualTo("sqxxid", xmid).andEqualTo("shzt", Constants.DCHY_XMGL_SHZT_SHZ);
            int shzNum = entityMapper.countByExample(example);

            example.clear();
            example.createCriteria().andEqualTo("sqxxid", xmid);
            int shxxNum = entityMapper.countByExample(example);
            if (shzNum == 0 && shxxNum > 0) {
                yxbj = true;
            }
            if (yxbj) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowXmlUtil.getWorkFlowIntanceService().getWorkflowInstanceByProId(xmid);
                List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                String portal = AppConfig.getProperty("portal.url") + "/index/endTask?wiid=" + pfWorkFlowInstanceVo.getWorkflowIntanceId() + "&taskid=" + pfTaskVoList.get(0).getTaskId();
                String response = HttpClientUtil.sendHttpClient(portal);
                logger.info("自动办结返回信息:" + response);
            }
        }
        return yxbj;
    }

    @Override
    @Transactional
    public boolean completeDchySqxx(WfEventDto wfEventDto) {
        if (null != wfEventDto && StringUtils.isNotBlank(wfEventDto.getXmid())) {
            String xmid = wfEventDto.getXmid();
            Example example = new Example(DchyXmglClcgShjl.class);
            example.createCriteria().andEqualTo("shzt", Constants.DCHY_XMGL_SHZT_SHTG).andEqualTo("sqxxid", xmid);
            List<DchyXmglClcgShjl> shtgList = entityMapper.selectByExampleNotNull(example);

            example.clear();
            example.createCriteria().andEqualTo("shzt", Constants.DCHY_XMGL_SHZT_TH).andEqualTo("sqxxid", xmid);
            List<DchyXmglClcgShjl> thList = entityMapper.selectByExampleNotNull(example);

            List shtgCgidList = Lists.newArrayList();
            List thCgidList = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(shtgList)) {
                for (DchyXmglClcgShjl dchyXmglClcgShjl : shtgList) {
                    shtgCgidList.add(dchyXmglClcgShjl.getClcgid());
                }
            }

            if (CollectionUtils.isNotEmpty(thList)) {
                for (DchyXmglClcgShjl dchyXmglClcgShjl : thList) {
                    thCgidList.add(dchyXmglClcgShjl.getClcgid());
                }
            }

            //同一成果不同人审核,有人退回默认退回
            if (CollectionUtils.isNotEmpty(shtgCgidList) && CollectionUtils.isNotEmpty(thCgidList)) {
                shtgCgidList.removeAll(thCgidList);
            }

            if (CollectionUtils.isNotEmpty(shtgCgidList)) {
                // 修改审核状态
                Example shtgExample = new Example(DchyXmglClcg.class);
                shtgExample.createCriteria().andIn("clcgid", shtgCgidList);
                DchyXmglClcg dchyXmglClcg = new DchyXmglClcg();
                dchyXmglClcg.setShzt(Constants.DCHY_XMGL_SHZT_SHTG);
                dchyXmglClcg.setRksj(new Date());
                entityMapper.updateByExampleSelective(dchyXmglClcg, shtgExample);

                // 审核之后修改成果提交状态 已入库
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExampleNotNull(shtgExample);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    LinkedHashSet clsxidList = Sets.newLinkedHashSet();
                    for (DchyXmglClcg dchyXmglClcg1 : dchyXmglClcgList) {
                        clsxidList.add(dchyXmglClcg1.getClsxid());
                    }
                    Example shtgClsxExample = new Example(DchyXmglChxmClsx.class);
                    shtgClsxExample.createCriteria().andIn("clsxid", Lists.newArrayList(clsxidList));
                    DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                    dchyXmglChxmClsx.setCgtjzt(Constants.DCHY_XMGL_XMCGZT_YRK);
                    entityMapper.updateByExampleSelective(dchyXmglChxmClsx, shtgClsxExample);
                }
            }

            if (CollectionUtils.isNotEmpty(thCgidList)) {
                // 修改审核状态
                Example shthExample = new Example(DchyXmglClcg.class);
                shthExample.createCriteria().andIn("clcgid", thCgidList);
                DchyXmglClcg dchyXmglClcg = new DchyXmglClcg();
                dchyXmglClcg.setShzt(Constants.DCHY_XMGL_SHZT_TH);
                entityMapper.updateByExampleSelective(dchyXmglClcg, shthExample);

                // 审核之后修改成果提交状态 已退回
                List<DchyXmglClcg> dchyXmglClcgList = entityMapper.selectByExampleNotNull(shthExample);
                if (CollectionUtils.isNotEmpty(dchyXmglClcgList)) {
                    LinkedHashSet clsxidList = Sets.newLinkedHashSet();
                    for (DchyXmglClcg dchyXmglClcg1 : dchyXmglClcgList) {
                        clsxidList.add(dchyXmglClcg1.getClsxid());
                    }
                    Example shtgClsxExample = new Example(DchyXmglChxmClsx.class);
                    shtgClsxExample.createCriteria().andIn("clsxid", Lists.newArrayList(clsxidList));
                    DchyXmglChxmClsx dchyXmglChxmClsx = new DchyXmglChxmClsx();
                    dchyXmglChxmClsx.setCgtjzt(Constants.DCHY_XMGL_XMCGZT_YTH);
                    entityMapper.updateByExampleSelective(dchyXmglChxmClsx, shtgClsxExample);
                }
            }
        }
        return false;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已提交成果文件
     */
    @Override
    public Map<String, Object> checkYxzf(WfEventDto wfEventDto, String yzfs) {
        Map<String, Object> resultMap = Maps.newHashMap();
        if (StringUtils.equalsIgnoreCase(yzfs, "checkCgytj")) {
            resultMap = checkCgytj(wfEventDto);
        } else if (StringUtils.equalsIgnoreCase(yzfs, "checkShxxytx")) {
            resultMap = checkCgytj(wfEventDto);
        } else {
            resultMap.put("message", "转发验证事项为空");
            resultMap.put("tabName", "");
        }
        return resultMap;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已填写审核信息
     */
    private Map<String, Object> checkCgytj(WfEventDto wfEventDto) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            if (wfEventDto != null && StringUtils.isNotBlank(wfEventDto.getXmid())) {
                Example example = new Example(DchyXmglClcg.class);
                example.createCriteria().andEqualTo("sqxxid", wfEventDto.getXmid());
                int num = entityMapper.countByExample(example);
                if (num == 0) {
                    resultMap.put("message", "成果文件未提交");
                    resultMap.put("tabName", "成果提交");
                }
            } else {
                resultMap.put("message", "成果提交项目信息为空");
            }
        } catch (Exception e) {
            resultMap.put("message", "转发验证失败");
        }
        return resultMap;
    }

    /**
     * @param wfEventDto
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/2/2 11:10
     * @description 流程转发前验证是否已填写审核信息
     */
    private Map<String, Object> checkShxxytx(WfEventDto wfEventDto) {
        Map<String, Object> resultMap = Maps.newHashMap();
        try {
            if (wfEventDto != null && StringUtils.isNotBlank(wfEventDto.getXmid())) {
                PfActivityVo pfActivityVo = sysTaskService.getActivity(wfEventDto.getAdid());
                Example example = new Example(DchyXmglClcgShjl.class);
                example.createCriteria().andEqualTo("sqxxid", wfEventDto.getXmid()).andEqualTo("shjdmc", pfActivityVo.getActivityName()).andIsNotNull("shsj");
                int num = entityMapper.countByExample(example);
                if (num == 0) {
                    resultMap.put("message", "审核信息未提交");
                    resultMap.put("tabName", "成果审核填写");
                }
            } else {
                resultMap.put("message", "成果提交项目信息为空");
            }
        } catch (Exception e) {
            resultMap.put("message", "转发验证失败");
        }
        return resultMap;
    }
}
