package cn.gtmap.msurveyplat.portal.web.rest;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.portal.config.Constants;
import cn.gtmap.msurveyplat.portal.entity.CasProperties;
import cn.gtmap.msurveyplat.portal.entity.JsonResult;
import cn.gtmap.msurveyplat.portal.entity.TaskData;
import cn.gtmap.msurveyplat.portal.service.TaskActionService;
import cn.gtmap.msurveyplat.portal.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.PromanageFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.ServerFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.TaskPerformerFilterServiceContext;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXml;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXmlUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.*;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.plat.wf.model.ActivityModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@RestController
@RequestMapping("/v1.0/dispatch/order")
public class DispatchOrderController {
    @Autowired
    private ExchangeFeignServiceImpl dispatchOrderFsmServiceImpl;
    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    private WorkFlowCoreService workFlowCoreService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private WorkFlowXmlUtil workFlowXmlUtil;
    @Autowired
    private TaskActionService taskActionService;
    @Autowired
    private SysWorkFlowInstanceService sysWorkFlowInstanceService;
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private SysActivityService sysActivityService;
    @Resource
    private CasProperties casProperties;
    @Autowired
    private ServerFeignServiceImpl serverFeignService;
    @Autowired
    private PromanageFeignServiceImpl promanageFeignService;
    @Autowired
    private TaskPerformerFilterServiceContext taskPerformerFilterServiceContext;
    @Autowired
    private SysOpinionService opinionService;
    @Autowired
    private NodeService nodeService;

    @RequestMapping(value = "createTask/{wdid}/{userId}")
    public TaskData createTask(@PathVariable(name = "userId") String userId, @PathVariable(name = "wdid") String wdid) throws Exception {
        Map result = Maps.newHashMap();
        TaskData taskData = new TaskData();
        if (StringUtils.isNotBlank(wdid)) {
            try {
                PfUserVo userInfo = sysUserService.getUserVo(userId);
                PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(wdid);
                String proId = UUIDGenerator.generate();
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = new PfWorkFlowInstanceVo();
                InitDataParamDTO initDataParamDTO = new InitDataParamDTO();
                initDataParamDTO.setGzlslid(proId);
                initDataParamDTO.setGzldyid(wdid);
                initDataParamDTO.setUserName(userInfo.getUserName());
                initDataParamDTO.setUserId(userId);
                // 调用server接口
                DchyCgglXmDO dchyCgglXmDO = serverFeignService.initData(initDataParamDTO);
                String slbh = dchyCgglXmDO.getSlbh();
                // 在文件中心创建节点
//                createNode(slbh);
                if (StringUtils.isBlank(slbh)) {
                    throw new Exception();
                }

                initDataParamDTO.setXmid(dchyCgglXmDO.getXmid());
                initDataParamDTO.setSlbh(slbh);
                promanageFeignService.initData(initDataParamDTO);

                StringBuilder workflowInstanceName = new StringBuilder();
                workflowInstanceName.append(slbh);
                pfWorkFlowInstanceVo.setWorkflowIntanceName(workflowInstanceName.toString());
                pfWorkFlowInstanceVo.setWorkflowDefinitionId(wdid);
                pfWorkFlowInstanceVo.setCreateTime(Calendar.getInstance().getTime());
                pfWorkFlowInstanceVo.setTimeLimit(pfWorkFlowDefineVo.getTimeLimit());
                pfWorkFlowInstanceVo.setPriority("1");
                pfWorkFlowInstanceVo.setCreateUser(userId);
                pfWorkFlowInstanceVo.setWorkflowIntanceId(proId);
                pfWorkFlowInstanceVo.setProId(dchyCgglXmDO.getXmid());
                pfWorkFlowInstanceVo.setRemark(slbh + Constants.SPLIT_STR + Constants.SPLIT_STR + Constants.SPLIT_STR + Constants.SPLIT_STR + Constants.SPLIT_STR);
                WorkFlowInfo infoObj = workFlowCoreService.createWorkFlowInstance(
                        pfWorkFlowInstanceVo, userId);

                // 初始化流程扩展表
                ActStProRelDo actStProRelDo = new ActStProRelDo();
                actStProRelDo.setId(UUIDGenerator.generate());
                actStProRelDo.setText1(slbh);
                actStProRelDo.setProcInsId(proId);
                actStProRelDo.setText9(dchyCgglXmDO.getXmid());
                actStProRelDo.setProjectId(dchyCgglXmDO.getXmid());
                actStProRelDo.setText5(userInfo.getUserName());
                dispatchOrderFsmServiceImpl.saveOrUpdateTaskExtendDto(actStProRelDo);
                String taskId = null;
                for (PfTaskVo taskVo : infoObj.getTargetTasks()) {
                    if (taskVo.getUserVo().getUserId().equals(userId)) {
                        taskId = taskVo.getTaskId();
                        break;
                    }
                }
                if (StringUtils.isBlank(taskId)) {
                    for (PfTaskVo taskVo : infoObj.getTargetTasks()) {
                        taskId = taskVo.getTaskId();
                        break;
                    }
                }

                result.put("taskId", taskId);
                result.put("wfid", proId);
                result.put("proid", dchyCgglXmDO.getXmid());
                result.put("userid", pfWorkFlowInstanceVo.getCreateUser());
                taskData.setTaskId(taskId);
                taskData.setProcessDefId(wdid);
                taskData.setProcessDefName(pfWorkFlowInstanceVo.getWorkflowIntanceName());
                taskData.setProcessInstanceId(proId);
                taskData.setProcStartUserName(userInfo.getUserName());
                taskData.setExecutionId(dchyCgglXmDO.getXmid());
                taskData.setFormKey(AppConfig.getProperty("platform.url"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
   /*         r.setResult("wdid不能为空");
            r.setStatus("error");*/
            throw new Exception();
        }
        return taskData;
    }

    @RequestMapping(value = "turnTask/{wiid}/{userId}")
    public Object turnTask(HttpServletRequest request, @PathVariable(name = "userId") String userId, @PathVariable(name = "wiid") String wiid) throws Exception {
        JsonResult r = new JsonResult();
        try {
            List<PfTaskVo> pfTaskVoList = sysTaskService.getTaskListByInstance(wiid);
            String taskid = null;
            if (CollectionUtils.isNotEmpty(pfTaskVoList)) {
                for (PfTaskVo pfTaskVo : pfTaskVoList) {
                    taskid = pfTaskVo.getTaskId();
                    PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
                    if (StringUtils.isNotBlank(taskid)) {
//                        PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
                        if (pfTaskVo != null && StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                            PfActivityVo pfActivityVo = sysActivityService.getActivityById(pfTaskVo.getActivityId());
                            if (pfActivityVo != null && StringUtils.isNotBlank(pfActivityVo.getWorkflowInstanceId())) {
                                pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(pfActivityVo.getWorkflowInstanceId());
                            }
                            if (pfWorkFlowInstanceVo != null) {
                                wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
                            }
                        }
                    }
                    // 转发检查是否办结，代办任务使用
                    String userid = userId;
                    WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userid, taskid);
                    // 处理活动
                    List<ActivityModel> lstActivityModel = info.getTransInfo().getTranActivitys();

                    WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(info.getWorkFlowIntanceVo());
                    String activityDefineId = info.getSourceActivity().getActivityDefinitionId();
                    // 判断是否任务办结，两种情况（1为最后一个活动，2为中间活动的）
                    if (info.getTransInfo().getTranActivitys() != null && info.getTransInfo().getTranActivitys().size() == 0) {
                        ActivityModel aModel = workXml.getActivity(activityDefineId);
                        //办结
                        taskActionService.endTask(wiid, taskid, userid, pfWorkFlowInstanceVo, request, "", info);
                        break;
                    } else {
                        //转发
                        taskActionService.turnTaskByWorkFlowInfo(wiid, taskid, userid, pfWorkFlowInstanceVo, request, "");
                    }
                }
            }
            r.setResult(taskid);
            r.setStatus("ok");

        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }

        return ResponseEntity.ok(r);
    }

    /**
     * @param slbh 受理编号
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 在文件中心创建节点
     */
    private void createNode(String slbh, String username) {
        Space rootSpace = nodeService.getWorkSpace("WORK_FLOW_STUFF", true);
        nodeService.createNode(rootSpace.getId(), slbh, username);
    }
}
