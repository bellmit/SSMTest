package cn.gtmap.msurveyplat.portal.web.main;


import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.PingYinUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portal.config.Constants;
import cn.gtmap.msurveyplat.portal.entity.*;
import cn.gtmap.msurveyplat.portal.entity.UserInfo;
import cn.gtmap.msurveyplat.portal.service.TaskActionService;
import cn.gtmap.msurveyplat.portal.service.impl.ExchangeFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.PromanageFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.ServerFeignServiceImpl;
import cn.gtmap.msurveyplat.portal.service.impl.TaskPerformerFilterServiceContext;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXml;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXmlUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gtis.common.Page;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.*;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.plat.wf.WorkFlowTransInfo;
import com.gtis.plat.wf.model.ActivityModel;
import com.gtis.plat.wf.model.PerformerTaskModel;
import groovyjarjarcommonscli.MissingArgumentException;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Api(tags = "首页接口")
@RestController
@RequestMapping("/index")
public class IndexController {
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
    private ExchangeFeignServiceImpl dispatchOrderFsmServiceImpl;
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

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 新建任务滚动大小
     */
    @Value("${workflow.list.size}")
    private Integer size;
    @Value("${workflowName}")
    private String workflowName;

    public static UserInfo getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    UserInfo guest = new UserInfo();
                    guest.setAdmin(true);
                    guest.setUsername("Admin");
                    guest.setId("0");
                    guest.setLoginName("Admin");
                    return guest;
                }
                return (UserInfo) authentication.getPrincipal();
            }
        }
        return null;
    }

    public static String getCurrentUserId() {
        UserInfo user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUserIds() {
        UserInfo info = getCurrentUser();
        return info != null ? (StringUtils.isNotBlank(info.getUsersIdAll()) ? info.getUsersIdAll() : "'" + info.getId() + "'") : null;
    }

    /**
     * @param systemId
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取平台资源
     */
    @RequestMapping("getSysMenu")
    public List<BdcModuleDTO> getSysMenu(String systemId) {
        if (StringUtils.isNoneBlank(systemId)) {
            return dispatchOrderFsmServiceImpl.getSysMenu(getCurrentUserId(), systemId);
        } else {
            return dispatchOrderFsmServiceImpl.getSysMenu(getCurrentUserId());
        }
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取当前用户详细信息
     */
    @ApiOperation(value = "获取当前用户详细信息", notes = "获取当前用户详细信息")
    @RequestMapping("getUser")
    public UserDto getUser() {
        UserInfo userInfo = getCurrentUser();
        UserDto userDto = new UserDto();
        userDto.setAlias(userInfo.getUsername());
        userDto.setId(userInfo.getId());
        userDto.setUsername(userInfo.getLoginName());
        List<PfRoleVo> pfRoleVoList = dispatchOrderFsmServiceImpl.getRoleListByUserid(userInfo.getId());
        List<PfOrganVo> pfOrganVoList = dispatchOrderFsmServiceImpl.getOrganListByUser(userInfo.getId());
        PfUserVo pfUserVo = sysUserService.getUserVo(userInfo.getId());
        if (CollectionUtils.isNotEmpty(pfOrganVoList)) {
            PfOrganVo pfOrgan = pfOrganVoList.get(0);
            userDto.setDepartmentName(pfOrgan.getOrganName());
        }

        if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
            PfRoleVo pfRoleVo = pfRoleVoList.get(0);
            userDto.setRoleName(pfRoleVo.getRoleName());
        }

        if (null != pfUserVo) {
            userDto.setMobile(pfUserVo.getMobilePhone());
        }

        return userDto;
    }

    @RequestMapping("getUrl")
    public String getUrl() {
        String url = AppConfig.getProperty("cggl.url");
        return url;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取新建任务列表
     */
    @ApiOperation(value = "获取新建任务列表", notes = "获取新建任务列表")
    @RequestMapping("list")
    public Object listCategoryProcess() {
        List<Map<String, Object>> resultData = new ArrayList<>();
        Map<String, Object> resultMapData = new HashMap<>();

        UserInfo userInfo = getCurrentUser();

        List<PfBusinessVo> allBusiness;
        List<PfWorkFlowDefineVo> lstWorkFlowDefine;
        if (userInfo.isAdmin()) {
            allBusiness = sysWorkFlowDefineService.getBusinessList();
            lstWorkFlowDefine = sysWorkFlowDefineService.getWorkFlowDefineList();
        } else {
            lstWorkFlowDefine = sysWorkFlowDefineService.getWorkFlowDefineListByRole(userInfo.getRoleIds(), "", null);
            allBusiness = Lists.newArrayList();
            LinkedHashSet<String> businessIdSets = Sets.newLinkedHashSet();
            if (CollectionUtils.isNotEmpty(lstWorkFlowDefine)) {
                for (PfWorkFlowDefineVo pfWorkFlowDefineVo : lstWorkFlowDefine) {
                    businessIdSets.add(pfWorkFlowDefineVo.getBusinessId());
                }
                for (String businessId : businessIdSets) {
                    PfBusinessVo pfBusinessVo = sysWorkFlowDefineService.getBusiness(businessId);
                    if (pfBusinessVo != null) {
                        allBusiness.add(pfBusinessVo);
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(allBusiness)) {
            for (int i = 0; i < allBusiness.size(); i++) {
                String businessName = allBusiness.get(i).getBusinessName();
                Map<String, Object> bussinessmap = new HashMap<>();
                bussinessmap.put("id", allBusiness.get(i).getBusinessId());
                bussinessmap.put("description", allBusiness.get(i).getBusinessName());
                bussinessmap.put("name", allBusiness.get(i).getBusinessName());
                List<Map<String, Object>> processList = new ArrayList<>();
                //循坏遍历每个
                for (int j = 0; j < lstWorkFlowDefine.size(); j++) {
                    if (StringUtils.equals(businessName, lstWorkFlowDefine.get(j).getBusinessVo().getBusinessName())) {
                        Map<String, Object> mapWork = new HashMap<>();
                        mapWork.put("category", lstWorkFlowDefine.get(j).getWorkflowName().toString());
                        mapWork.put("commonUse", 0);
                        mapWork.put("description", "");
                        mapWork.put("id", lstWorkFlowDefine.get(j).getWorkflowDefinitionId().toString());
                        mapWork.put("key", "");
                        mapWork.put("name", lstWorkFlowDefine.get(j).getWorkflowName().toString());
                        mapWork.put("processDefKey", "");
                        mapWork.put("suspensionState", 0);
                        mapWork.put("variables", "");
                        mapWork.put("version", "");
                        processList.add(mapWork);
                    }
                }
                bussinessmap.put("processList", processList);
                resultData.add(bussinessmap);
            }
        }
        resultMapData.put("size", size);
        resultMapData.put("content", resultData);

        return resultMapData;
    }


    /**
     * @param wdid
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 创建任务
     */
    @ApiOperation(value = "创建任务", notes = "创建任务")
    @RequestMapping("createTask")
    public TaskData createTask(String wdid) throws Exception {
        Map result = Maps.newHashMap();
        TaskData taskData = new TaskData();
        if (StringUtils.isNotBlank(wdid)) {
            try {
                PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(wdid);
                String userId = getCurrentUserId();
                UserInfo userInfo = getCurrentUser();
                String proId = UUIDGenerator.generate();
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = new PfWorkFlowInstanceVo();
                InitDataParamDTO initDataParamDTO = new InitDataParamDTO();
                initDataParamDTO.setGzlslid(proId);
                initDataParamDTO.setGzldyid(wdid);
                initDataParamDTO.setUserName(userInfo.getUsername());
                initDataParamDTO.setUserId(userId);
                // 调用server接口
                DchyCgglXmDO dchyCgglXmDO = serverFeignService.initData(initDataParamDTO);
                String slbh = dchyCgglXmDO.getSlbh();
                // 在文件中心创建节点
                createNode(slbh);
                if (StringUtils.isBlank(slbh)) {
                    throw new Exception();
                }

                initDataParamDTO.setXmid(dchyCgglXmDO.getXmid());
                initDataParamDTO.setSlbh(slbh);
                try {
                    promanageFeignService.initData(initDataParamDTO);
                } catch (Exception e){
                }
                StringBuilder workflowInstanceName = new StringBuilder();
                workflowInstanceName.append(slbh);
                pfWorkFlowInstanceVo.setWorkflowIntanceName(workflowInstanceName.toString());
                pfWorkFlowInstanceVo.setWorkflowDefinitionId(wdid);
                pfWorkFlowInstanceVo.setCreateTime(Calendar.getInstance().getTime());
                pfWorkFlowInstanceVo.setTimeLimit(pfWorkFlowDefineVo.getTimeLimit());
                pfWorkFlowInstanceVo.setPriority("1");
                pfWorkFlowInstanceVo.setCreateUser(getCurrentUserId());
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
                actStProRelDo.setText5(userInfo.getUsername());
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
                taskData.setProcStartUserName(userInfo.getUsername());
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

    /**
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: moduleCode
     * @time 2021/2/20 17:15
     * @description
     */
    @ApiOperation(value = "获取资源授权信息", notes = "获取资源授权信息")
    @RequestMapping("/getResourceAuthorty")
    public Map<String, String> getResourceAuthorty(String moduleCode) throws Exception {
        Map<String, String> result;
        if (StringUtils.isNotBlank(moduleCode)) {
            result = dispatchOrderFsmServiceImpl.getResourceAuthorty(getCurrentUser().getId(), moduleCode);
        } else {
            throw new Exception();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 项目列表
     */
    @ApiOperation(value = "项目列表", notes = "项目列表")
    @RequestMapping(value = "/getProjectList")
    public Map<String, Object> getProjectList(Pageable pageable, String slbh, String jsdw, String chdw, String xmdz, String slr, String lcmc) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            HashMap mapParam = Maps.newHashMap();
            String userIds = getCurrentUserIds();
            if (StringUtils.isBlank(userIds)) {
                mapParam.put("userIds", null);
            } else {
                mapParam.put("userIds", userIds.split(","));
            }
            if (StringUtils.isNotBlank(slbh)) {
                mapParam.put("slbh", slbh);
            }
            if (StringUtils.isNotBlank(lcmc)) {
                mapParam.put("lcmc", lcmc);
            }
            if (StringUtils.isNotBlank(jsdw)) {
                mapParam.put("jsdw", jsdw);
            }
            if (StringUtils.isNotBlank(chdw)) {
                mapParam.put("chdw", chdw);
            }
            if (StringUtils.isNotBlank(xmdz)) {
                mapParam.put("xmdz", xmdz);
            }
            if (StringUtils.isNotBlank(slr)) {
                mapParam.put("slr", slr);
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("pageable", pageable);
            map.put("param", mapParam);
            result = dispatchOrderFsmServiceImpl.getProjectList(map);
            result.put("code", "0");
            List<Map<String, Object>> content = (List) result.get("content");
            if (CollectionUtils.isEmpty(content)) {
                result.put("msg", "无数据");
            } else {
                result.put("msg", "成功");
            }
        } catch (Exception e) {
            result.put("msg", e.getClass().getName() + ":" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 已办任务
     */
    @ApiOperation(value = "已办任务", notes = "已办任务")
    @RequestMapping(value = "/getTaskOverList")
    public Map<String, Object> getTaskOverList(Pageable pageable, String slbh, String jsdw, String chdw, String xmdz, String slr, String lcmc) {
        Map<String, Object> result = Maps.newHashMap();
        try {
            HashMap mapParam = Maps.newHashMap();
            String userIds = getCurrentUserIds();
            if (StringUtils.isBlank(userIds)) {
                mapParam.put("userIds", null);
            } else {
                mapParam.put("userIds", userIds.split(","));
            }
            if (StringUtils.isNotBlank(slbh)) {
                mapParam.put("slbh", slbh);
            }
            if (StringUtils.isNotBlank(lcmc)) {
                mapParam.put("lcmc", lcmc);
            }
            if (StringUtils.isNotBlank(jsdw)) {
                mapParam.put("jsdw", jsdw);
            }
            if (StringUtils.isNotBlank(chdw)) {
                mapParam.put("chdw", chdw);
            }
            if (StringUtils.isNotBlank(xmdz)) {
                mapParam.put("xmdz", xmdz);
            }
            if (StringUtils.isNotBlank(slr)) {
                mapParam.put("slr", slr);
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("pageable", pageable);
            map.put("param", mapParam);
            result = dispatchOrderFsmServiceImpl.getTaskOverList(map);
            result.put("code", "0");
            List content = (List) result.get("content");
            if (CollectionUtils.isEmpty(content)) {
                result.put("msg", "无数据");
            } else {
                result.put("msg", "成功");
            }
        } catch (Exception e) {
            result.put("msg", e.getClass().getName() + ":" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 待办任务
     */
    @ApiOperation(value = "待办任务", notes = "待办任务")
    @RequestMapping(value = "/getTaskList")
    public Map<String, Object> getTaskList(Pageable pageable, String slbh, String jsdw, String chdw, String xmdz, String slr, String lcmc) {
        Map<String, Object> result;
        try {
            HashMap mapParam = Maps.newHashMap();
            String userIds = getCurrentUserIds();
            if (StringUtils.isBlank(userIds)) {
                mapParam.put("userIds", null);
            } else {
                mapParam.put("userIds", userIds.split(","));
            }
            if (StringUtils.isNotBlank(slbh)) {
                mapParam.put("slbh", slbh);
            }
            if (StringUtils.isNotBlank(lcmc)) {
                mapParam.put("lcmc", lcmc);
            }
            if (StringUtils.isNotBlank(jsdw)) {
                mapParam.put("jsdw", jsdw);
            }
            if (StringUtils.isNotBlank(chdw)) {
                mapParam.put("chdw", chdw);
            }
            if (StringUtils.isNotBlank(xmdz)) {
                mapParam.put("xmdz", xmdz);
            }
            if (StringUtils.isNotBlank(slr)) {
                mapParam.put("slr", slr);
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("pageable", pageable);
            map.put("param", mapParam);
            result = dispatchOrderFsmServiceImpl.getTaskList(map);
            result.put("code", "0");
            List content = (List) result.get("content");
            if (CollectionUtils.isEmpty(content)) {
                result.put("msg", "无数据");
            } else {
                result.put("msg", "成功");
            }
        } catch (Exception e) {
            result = Maps.newHashMap();
            result.put("msg", e.getClass().getName() + ":" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 删除任务
     */
    @RequestMapping("delTask")
    public ResponseEntity<JsonResult> delTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) List<String> taskid, @RequestParam(value = "reason", required = false) String reason, @RequestParam(value = "xmid", required = false) List<String> xmid) throws Exception {
        JsonResult r = new JsonResult();
        List<ProcessInsExtendDto> deleteReultList = taskActionService.getXmTaskId(taskid, xmid); //拼凑数据可以循坏删除
        //调用 paltfrom 执行平台工作流事件
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
        if (StringUtils.isNotBlank(wiid)) {
            pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
        } else if (CollectionUtils.isNotEmpty(deleteReultList)) {
            for (int i = 0; i < deleteReultList.size(); i++) {
                String taskiddel = deleteReultList.get(i).getProjectId(); //"taskid"
                String xmiddel = deleteReultList.get(i).getText9(); //xmid
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskiddel);
                if (pfTaskVo != null && StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                    PfActivityVo pfActivityVo = sysActivityService.getActivityById(pfTaskVo.getActivityId());
                    if (pfActivityVo != null && StringUtils.isNotBlank(pfActivityVo.getWorkflowInstanceId())) {
                        pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(pfActivityVo.getWorkflowInstanceId());
                    }
                }
                String msg = "";
                try {
                    msg = taskActionService.delTask(pfWorkFlowInstanceVo.getWorkflowIntanceId(), taskiddel, getCurrentUserId(), reason, pfWorkFlowInstanceVo.getProId(), pfWorkFlowInstanceVo, request);
                    // 调用server接口 删除业务数据
                    serverFeignService.deleteYwxx(xmiddel);
                    //
                    promanageFeignService.deleteYwxx(xmiddel, pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                    // 调用exchange接口 根据流程实例id删除流程扩展表数据
                    dispatchOrderFsmServiceImpl.deleteTaskExtendDto(pfWorkFlowInstanceVo.getWorkflowIntanceId());
                    r.setResult(msg);
                    r.setStatus("ok");
                } catch (Exception e) {
                    r.setResult(e.getClass().getName() + ":" + e.getMessage());
                    r.setStatus("error");
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok(r);

    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 代待办任务以WorkFlowInfo转发
     */
    @RequestMapping("turnTaskByWorkFlowInfo")
    public Object turnTaskByWorkFlowInfo(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
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
            String userid = getCurrentUserId();
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
            } else {
                //转发
                taskActionService.turnTaskByWorkFlowInfo(wiid, taskid, userid, pfWorkFlowInstanceVo, request, "");
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
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 代待办任务以WorkFlowInfo转发
     */
    @RequestMapping("endTask")
    public Object endTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            String userid = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
                userid = pfTaskVo.getUserVo().getUserId();
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
            WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userid, taskid);

            //办结
            taskActionService.endTask(wiid, taskid, userid, pfWorkFlowInstanceVo, request, "", info);

            r.setResult(taskid);
            r.setStatus("ok");

        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }

        return ResponseEntity.ok(r);
    }

    private String inputStream2String(InputStream is) throws IOException {
        String result = "";
        try {
            result = IOUtils.toString(is, "UTF-8");
            result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + result;
        } catch (Exception e) {
        } finally {
            is.close();
        }
        return result;
    }

    /**
     * 退回
     *
     * @param request
     * @param wiid
     * @param taskid
     * @param adids
     * @param remark
     * @return
     */
    @RequestMapping("turnBackTask")
    public ResponseEntity<JsonResult> turnBackTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid
            , @RequestParam(value = "adids", required = false) String adids, @RequestParam(value = "remark", required = false) String remark) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
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
            taskActionService.turnBackTask(wiid, taskid, getCurrentUserId(), pfWorkFlowInstanceVo, request, adids, remark);
            r.setResult(taskid);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping("stopTask")
    public ResponseEntity<JsonResult> stopTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getHistoryTask(taskid);
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
            taskActionService.stopTask(wiid, taskid, getCurrentUserId(), pfWorkFlowInstanceVo, request);
            r.setResult(wiid);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }

        return ResponseEntity.ok(r);
    }

    /**
     * 处理分页结构数据 添加code字段
     *
     * @param page
     * @return
     */
    public Object addLayUiCode(Page page) {
        Map map = null;
        if (page != null) {
            map = JSONObject.parseObject(JSONObject.toJSONString(page), Map.class);
            if (map != null) {
                map.put("msg", "成功");
            }
            if (map == null) {
                map = new HashMap(1);
                map.put("msg", "无数据");
            }
            map.put("code", 0);
        }
        return map;
    }

    /**
     * 任务挂起
     *
     * @param request
     * @param wiid
     * @param taskid
     * @param remark
     * @return
     */
    @RequestMapping("postTask")
    public ResponseEntity<JsonResult> postTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid
            , @RequestParam(value = "remark", required = false) String remark) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
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
            taskActionService.postTask(wiid, taskid, getCurrentUserId(), remark, pfWorkFlowInstanceVo, request);
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
     * 解挂
     *
     * @param request
     * @param wiid
     * @param taskid
     * @return
     */
    @RequestMapping("unPostTask")
    public ResponseEntity<JsonResult> unPostTask(HttpServletRequest request, @RequestParam(value = "wiid", required = false) String wiid, @RequestParam(value = "taskid", required = false) String taskid
    ) {
        JsonResult r = new JsonResult();
        try {
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
            if (StringUtils.isNotBlank(taskid)) {
                PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
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
            taskActionService.upPostTask(wiid, taskid, getCurrentUserId(), pfWorkFlowInstanceVo, request);
            r.setResult(wiid);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }

        return ResponseEntity.ok(r);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取权限表单
     */
    @RequestMapping("getformMunu")
    public Object getformMunu(String taskid, String zt, String wiid) throws Exception {
        PfWorkFlowInstanceVo workFlowInstanceVo;
        List<Menu> menuList;
        List<FormStateDTO> formList = null;
        String platformUrl = AppConfig.getPlatFormUrl();
        if (StringUtils.isNotBlank(taskid) && !StringUtils.equals("undefined", taskid)) {
            PfTaskVo taskVo = null;
            if (StringUtils.equals("db", zt)) {
                taskVo = sysTaskService.getTask(taskid);
            }
            if (StringUtils.equals("yb", zt)) {
                taskVo = sysTaskService.getHistoryTask(taskid);
            }
            PfActivityVo activityVo = sysTaskService.getActivity(taskVo.getActivityId());
            workFlowInstanceVo = sysWorkFlowInstanceService
                    .getWorkflowInstance(activityVo.getWorkflowInstanceId());

            // 菜单
            WorkFlowXml xmlModel = workFlowXmlUtil.getWorkFlowInstanceModel(workFlowInstanceVo);
            ActivityModel aModel = xmlModel.getActivity(activityVo.getActivityDefinitionId());

            //取办理菜单
            String menuXml = aModel.getResources();
            if (StringUtils.isNotBlank(menuXml)) {
                menuXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + menuXml;
                Document document = DocumentHelper.parseText(menuXml);
                if (document != null) {
                    List resourceNodeList = document.selectNodes("//Resources/Resource");
                    if (resourceNodeList != null) {
                        menuList = new ArrayList<>();
                        formList = new ArrayList<>();
                        for (int i = 0; i < resourceNodeList.size(); i++) {
                            Menu menu = new Menu();
                            FormStateDTO formStateDTO = new FormStateDTO();
                            Element resourceEl = (Element) resourceNodeList.get(i);
                            menu.setId(resourceEl.attributeValue("Id"));
                            menu.setText(resourceEl.attributeValue("Name"));
                            formStateDTO.setFormStateId(resourceEl.attributeValue("Id"));
                            formStateDTO.setFormStateName(resourceEl.attributeValue("Name"));
                            String firstLink = platformUrl + "/SysResource.action?from=task&taskid=" + taskid + "&proid=" + workFlowInstanceVo.getProId() + "&wiid=" + workFlowInstanceVo.getWorkflowIntanceId() + "&rid=" + resourceEl.attributeValue("Id");
                            PfResourceVo pfResourceVo = dispatchOrderFsmServiceImpl.getResource(resourceEl.attributeValue("Id"));
                            String url = pfResourceVo.getResourceUrl();
                            if (url != null) {
                                if (url.indexOf("${server.url}") != -1) {
                                    url = url.replace("${server.url}", AppConfig.getProperty("server.url"));
                                }
                                if (url.indexOf("${portal.url}") != -1) {
                                    url = url.replace("${portal.url}", AppConfig.getProperty("portal.url"));
                                }
                                if (url.indexOf("${platform.url}") != -1) {
                                    url = url.replace("${platform.url}", AppConfig.getProperty("platform.url"));
                                }

                            }
                            formStateDTO.setThirdPath(url);
                            List childrenNodeList1 = document.getRootElement().selectNodes("//Resources/Resource[@Id='" + resourceEl.attributeValue("Id") + "']/Resource");
                            if (childrenNodeList1 != null && childrenNodeList1.size() > 0) {
                                Element childEl1 = (Element) childrenNodeList1.get(0);
                                firstLink = platformUrl + "/SysResource.action?from=task&taskid=" + taskid + "&proid=" + workFlowInstanceVo.getProId() + "&wiid=" + workFlowInstanceVo.getWorkflowIntanceId() + "&rid=" + childEl1.attributeValue("Id");
                            }
                            menu.setLink(firstLink);
                            List childrenNodeList = resourceEl.selectNodes("//Resources/Resource[@Id='" + resourceEl.attributeValue("Id") + "']/Resource");
                            if (childrenNodeList != null && childrenNodeList.size() > 0) {
                                List<Menu> childMenuList = new ArrayList<Menu>();
                                for (int j = 1; j < childrenNodeList.size(); j++) {
                                    Menu childMenu = new Menu();
                                    Element childEl = (Element) childrenNodeList.get(j);
                                    childMenu.setId(childEl.attributeValue("Id"));
                                    childMenu.setText(childEl.attributeValue("Name"));
                                    String firstChildLink = platformUrl + "/SysResource.action?from=task&taskid=" + taskid + "&proid=" + workFlowInstanceVo.getProId() + "&wiid=" + workFlowInstanceVo.getWorkflowIntanceId() + "&rid=" + childEl.attributeValue("Id");
                                    List childChildNodeList = resourceEl.selectNodes("//Resources/Resource[@Id='" + resourceEl.attributeValue("Id") + "']/Resource[@Id='" + childEl.attributeValue("Id") + "']/Resource");
                                    if (childChildNodeList != null && childChildNodeList.size() > 0) {
                                        Element childEl1 = (Element) childChildNodeList.get(0);
                                        firstChildLink = platformUrl + "/SysResource.action?from=task&taskid=" + taskid + "&proid=" + workFlowInstanceVo.getProId() + "&wiid=" + workFlowInstanceVo.getWorkflowIntanceId() + "&rid=" + childEl1.attributeValue("Id");
                                    }
                                    childMenu.setLink(firstChildLink);
                                    PfResourceVo pfResourceChildVo = dispatchOrderFsmServiceImpl.getResource(resourceEl.attributeValue("Id"));
                                    formStateDTO.setThirdPath(pfResourceChildVo.getResourceUrl());

                                    if (childChildNodeList != null && childChildNodeList.size() > 0) {
                                        List<Menu> childChildMenuList = new ArrayList<Menu>();
                                        for (int k = 1; k < childChildNodeList.size(); k++) {
                                            Menu childChildMenu = new Menu();
                                            Element childChildEl = (Element) childChildNodeList.get(k);
                                            childChildMenu.setId(childChildEl.attributeValue("Id"));
                                            childChildMenu.setText(childChildEl.attributeValue("Name"));
                                            childChildMenu.setLink(platformUrl + "/SysResource.action?from=task&taskid=" + taskid + "&proid=" + workFlowInstanceVo.getProId() + "&wiid=" + workFlowInstanceVo.getWorkflowIntanceId() + "&rid=" + childChildEl.attributeValue("Id"));
                                            childChildMenuList.add(childChildMenu);
                                        }
                                        childMenu.setChildren(childChildMenuList);
                                    }

                                    childMenuList.add(childMenu);
                                }
                                menu.setChildren(childMenuList);
                                menu.setExpanded(true);
                            }
                            menuList.add(menu);
                            formList.add(formStateDTO);
                        }
                    }
                }

            }
        } else {
            workFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            UserInfo userInfo = getCurrentUser();
            String roles = userInfo.getRoleIds();
            if (StringUtils.isBlank(roles)) {
                List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(userInfo.getId());
                if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
                    roles = "'" + pfRoleVoList.get(0).getRoleId() + "'";
                } else {
                    roles = "roles";
                }
            }
            List<PfResourceVo> lstResult = dispatchOrderFsmServiceImpl.getProjectMenu(roles, workFlowInstanceVo.getWorkflowDefinitionId());
            formList = new ArrayList<>();
            for (PfResourceVo pfResourceVo : lstResult) {
                FormStateDTO formStateDTO = new FormStateDTO();
                formStateDTO.setFormStateId(pfResourceVo.getResourceId());
                formStateDTO.setFormStateName(pfResourceVo.getResourceName());
                String url = pfResourceVo.getResourceUrl();
                if (url.indexOf("${server.url}") != -1) {
                    url = url.replace("${server.url}", AppConfig.getProperty("server.url"));
                }
                if (url.indexOf("${portal.url}") != -1) {
                    url = url.replace("${portal.url}", AppConfig.getProperty("portal.url"));
                }
                if (url.indexOf("${platform.url}") != -1) {
                    url = url.replace("${platform.url}", AppConfig.getProperty("platform.url"));
                }
                formStateDTO.setThirdPath(url);
                formList.add(formStateDTO);
            }
        }
        return formList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取cas地址
     */
    @RequestMapping("getCasUrl")
    public String getCasUrl() {
        return casProperties.getCasHost();
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 修改用户密码
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("修改用户密码")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "old", value = "原密码", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "string", paramType = "query")})
    @RequestMapping("password")
    public String updateUserPwd(String old, String password) {
        if (StringUtils.isBlank(old) || StringUtils.isBlank(password)) {
            return "修改密码原密码和新密码均不能为空！";
        }
        try {
            UserInfo userInfo = getCurrentUser();
            if (!StringUtils.equals(userInfo.getPassword(), DigestUtils.md5DigestAsHex(old.getBytes()))) {
                return "原密码错误！";
            }
            sysUserService.savePassWord(userInfo.getId(), password);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getClass().getName() + ":" + e.getMessage();
        }
        return "success";
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取流程名称
     */
    @RequestMapping("getWorkflowDefinitions")
    public Object getWorkflowDefinitions(String businessId) {
        List<ProcessDefData> processDefDataList = new ArrayList();
        List<PfWorkFlowDefineVo> workFlowDefineVoList;
        if (StringUtils.isBlank(businessId)) {
            workFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineList();
        } else {
            workFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineByBusiness(businessId);
        }
        ProcessDefData processDefData;
        for (PfWorkFlowDefineVo pfWorkFlowDefineVo : workFlowDefineVoList) {
            if (StringUtils.isBlank(workflowName)) {
                processDefData = new ProcessDefData();
                processDefData.setId(pfWorkFlowDefineVo.getWorkflowDefinitionId());
                processDefData.setName(pfWorkFlowDefineVo.getWorkflowName());
                processDefDataList.add(processDefData);
            }
            if (StringUtils.isNotBlank(workflowName) && StringUtils.equals(workflowName, pfWorkFlowDefineVo.getWorkflowName())) {
                processDefData = new ProcessDefData();
                processDefData.setId(pfWorkFlowDefineVo.getWorkflowDefinitionId());
                processDefData.setName(pfWorkFlowDefineVo.getWorkflowName());
                processDefDataList.add(processDefData);
            }
        }
        return processDefDataList;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取按钮权限
     */
    @RequestMapping("getButtnAuth")
    public Map<String, Object> getButtnAuth(String taskid, String zt) throws Exception {
        Map<String, Object> resultMap = Maps.newHashMap();
        PfWorkFlowInstanceVo workFlowInstanceVo = null;
        boolean hasFinish = false; //办结
        boolean hasDel = false; //删除
        boolean hasSave = false; //保存
        boolean hasPrint = false; //打印
        boolean hasRk = false; //入库
        boolean hasBack = false; //退回
        boolean hasGcjsxmsp = false; //工程建设项目审批
        resultMap.put("hasDel", hasDel);
        resultMap.put("hasSave", hasSave);
        resultMap.put("hasPrint", hasPrint);
        resultMap.put("hasRk", hasRk);
        resultMap.put("hasBack", hasBack);
        resultMap.put("hasGcjsxmsp", hasGcjsxmsp);
        if (StringUtils.isNotBlank(taskid) && !StringUtils.equals("undefined", taskid)) {
            PfTaskVo taskVo = null;
            if (StringUtils.equals("db", zt)) {
                sysTaskService.updateTaskStadus(taskid);
                taskVo = sysTaskService.getTask(taskid);
            }
            if (StringUtils.equals("yb", zt)) {
                taskVo = sysTaskService.getHistoryTask(taskid);
            }
            PfActivityVo activityVo = null;
            if (taskVo != null) {
                activityVo = sysTaskService.getActivity(taskVo.getActivityId());
            }
            if (activityVo != null) {
                workFlowInstanceVo = sysWorkFlowInstanceService
                        .getWorkflowInstance(activityVo.getWorkflowInstanceId());
            }
            // 菜单
            WorkFlowXml xmlModel = workFlowXmlUtil.getWorkFlowInstanceModel(workFlowInstanceVo);
            ActivityModel aModel = null;
            if (xmlModel != null) {
                aModel = xmlModel.getActivity(activityVo.getActivityDefinitionId());
            }
            if (aModel != null) {
                if ("结束活动".equals(aModel.getActivityDescription())) {
                    hasFinish = true;
                } else {
                    if (aModel.getDefineId().equals(xmlModel.getEndActivityDefine()) && aModel.getTransitionsList().size() == 0) {
                        hasFinish = true;
                    }
                }
            }
            if (activityVo != null) {
                if (StringUtils.equals(Constants.ACTIVITY_SL, activityVo.getActivityName())) {
                    String slBtn = AppConfig.getProperty("sl.btn");
                    JSONObject sl = JSONObject.parseObject(slBtn);
                    resultMap.putAll(sl);
                    resultMap.put("jdmc", "sl");
                } else if (StringUtils.equals(Constants.ACTIVITY_CS, activityVo.getActivityName())) {
                    String csBtn = AppConfig.getProperty("cs.btn");
                    JSONObject cs = JSONObject.parseObject(csBtn);
                    resultMap.putAll(cs);
                    resultMap.put("jdmc", "cs");
                } else if (StringUtils.equals(Constants.ACTIVITY_FS, activityVo.getActivityName())) {
                    String fsBtn = AppConfig.getProperty("fs.btn");
                    JSONObject fs = JSONObject.parseObject(fsBtn);
                    resultMap.putAll(fs);
                    resultMap.put("jdmc", "fs");
                } else if (StringUtils.equals(Constants.ACTIVITY_BJ, activityVo.getActivityName())) {
                    String bjBtn = AppConfig.getProperty("bj.btn");
                    JSONObject bj = JSONObject.parseObject(bjBtn);
                    resultMap.putAll(bj);
                    resultMap.put("jdmc", "bj");
                } else {
                    String activityPinyin = PingYinUtil.getFirstSpell(activityVo.getActivityName());
                    String bjBtn = AppConfig.getProperty(activityPinyin + ".btn");
                    if (StringUtils.isNotBlank(bjBtn)) {
                        JSONObject bj = JSONObject.parseObject(bjBtn);
                        resultMap.putAll(bj);
                    }
                    resultMap.put("jdmc", activityPinyin);
                }
                resultMap.put("gzljdid", activityVo.getActivityDefinitionId());
            }
            if (workFlowInstanceVo != null) {
                resultMap.put("gzldyid", workFlowInstanceVo.getWorkflowDefinitionId());
            }

        }
        resultMap.put("hasFinish", hasFinish);
        return resultMap;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 是否允许删除
     */
    private boolean permitDel(PfTaskVo taskVo) {
        if (!getCurrentUser().isAdmin()) {
            PfActivityVo activityVo = sysTaskService.getActivity(taskVo
                    .getActivityId());
            PfWorkFlowInstanceVo workFlowInstanceVo = sysWorkFlowInstanceService
                    .getWorkflowInstance(activityVo.getWorkflowInstanceId());
            WorkFlowXml workXml = WorkFlowXmlUtil
                    .getInstanceModel(workFlowInstanceVo);
            if (!workXml.getBeginActivityDefine().equals(
                    activityVo.getActivityDefinitionId())) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param roleId
     * @return
     * @author <a href ="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取转发的用户
     */
    @GetMapping("/forward/users")
    @ApiOperation(value = "获取转发的用户")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query", dataType = "string")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> listForwardUsers(@RequestParam(value = "roleId") String roleId, @RequestParam(value = "taskid") String taskid) throws Exception {
        if (StringUtils.isBlank(roleId)) {
            throw new Exception();
        }
        String userId = getCurrentUserId();
        WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userId, taskid);
        List<UserDto> userDtoList = new ArrayList<>();
        List<Map<String, Object>> taskInfo = turnTaskInfo(info);
        Set userIds = Sets.newLinkedHashSet();
        for (Map<String, Object> map : taskInfo) {
            List<Map<String, Object>> bmList = (List<Map<String, Object>>) map.get("bm");
            for (Map<String, Object> bm : bmList) {
                if (StringUtils.equals(roleId, bm.get("Id").toString())) {
                    List<Map<String, Object>> userList = (List<Map<String, Object>>) bm.get("user");
                    for (Map<String, Object> use : userList) {
                        // 多节点转发，不同部门可能会有用户重复，去重
                        if (userIds.add(use.get("Id").toString())) {
                            UserDto userDto = new UserDto();
                            userDto.setId(use.get("Id").toString());
                            userDto.setUsername(use.get("Name").toString());
                            userDto.setAlias(use.get("Name").toString());
                            userDtoList.add(userDto);
                        }
                    }
                }
            }
        }
        return userDtoList;
    }

    /**
     * @param taskId taskId
     * @return
     * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
     * @description 获取转发的角色
     */
    @GetMapping(value = "/forward/taskAndRole")
    @ApiOperation(value = "获取转发的角色")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "taskId", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "filters", value = "filters", paramType = "query", dataType = "String")})
    @ResponseStatus(HttpStatus.OK)
    public ForWardVO queryForwardTaskAndRole(@RequestParam("taskId") String taskId, @RequestParam("filters") String filters) throws Exception {
        if (StringUtils.isBlank(taskId)) {
            throw new MissingArgumentException("获取转发活动、角色、节点类型的taskId不能为空！");
        }
        String userId = getCurrentUserId();
        WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userId, taskId);
        List<Map<String, Object>> taskInfo = turnTaskInfo(info);
        List<ForWardTaskVO> forWardTaskVOS = Lists.newArrayList();
        String nodeType = StringUtils.equalsIgnoreCase(info.getTransInfo().getTransType(), "AND") ? "ParallelGateway" : "ExclusiveGateway";
        // ParallelGateway：并行网关，返回多个人工列表，必须全部选择
        List<String> filterList = null;
        if (StringUtils.isNotBlank(filters)) {
            filterList = Lists.newArrayList(filters.split(","));
        }
        for (Map<String, Object> map : taskInfo) {
            // 移除任务转发列表
            if (filterTaskVO(filterList, MapUtils.getString(map, "Description"))) {
                continue;
            }
            List<String> taskVOFilters = taskVOFilters(filterList, MapUtils.getString(map, "Description"));
            String activityName = map.get("Name").toString();
            if (CollectionUtils.isNotEmpty(taskVOFilters)) {
                activityName += "【" + StringUtils.join(taskVOFilters, "、 ") + "】";
            }
            ForWardTaskVO forWardTaskVO = new ForWardTaskVO();
            List<RoleDto> roleDtoList = new ArrayList<>();
            ForwardTaskDto forwardTaskDto = new ForwardTaskDto();
            forwardTaskDto.setActivityId(map.get("Id").toString());
            forwardTaskDto.setActivityName(activityName);
            List<String> roleIdList = new ArrayList<>();
            List<Map<String, Object>> bmList = (List<Map<String, Object>>) map.get("bm");
            for (Map<String, Object> bm : bmList) {
                roleIdList.add(bm.get("Id").toString());
                RoleDto roleDto = new RoleDto();
                roleDto.setId(bm.get("Id").toString());
                roleDto.setName(bm.get("Name").toString());
                roleDto.setAlias(bm.get("Name").toString());
                roleDtoList.add(roleDto);
            }
            forwardTaskDto.setRoleIds(roleIdList);
            forWardTaskVO.setForwardTaskDto(forwardTaskDto);
            forWardTaskVO.setRoleDtoList(roleDtoList);
            forWardTaskVOS.add(forWardTaskVO);
        }
        ForWardVO forWardVO = new ForWardVO();
        forWardVO.setForWardTaskVOList(forWardTaskVOS);
        forWardVO.setNodeType(nodeType);
        return forWardVO;
    }

    @RequestMapping("deleteOtherAssignment/{taskid}/{userid}")
    public ResponseMessage deleteOtherAssignment(@PathVariable("taskid") String taskid, @PathVariable("userid") String userid) {
        ResponseMessage message;
        if (StringUtils.isNoneBlank(taskid, userid)) {
            try {
                message = dispatchOrderFsmServiceImpl.deleteOtherAssignment(taskid, userid);
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }

        return message;
    }

    // 移除任务转发列表
    private boolean filterTaskVO(List<String> filterList, String description) {
        if (CollectionUtils.isEmpty(filterList)) {
            // 过滤事项为空，不需要移除
            return false;
        } else if (CollectionUtils.isNotEmpty(filterList) && StringUtils.isNotBlank(description)) {
            List<String> bzList = Lists.newArrayList(description.split(","));
            if (CollectionUtils.containsAny(bzList, filterList)) {
                return false;
            }
        }
        return true;
    }

    // 任务转发列表过滤信息显示
    private List<String> taskVOFilters(List<String> filterList, String description) {
        if (CollectionUtils.isNotEmpty(filterList) && StringUtils.isNotBlank(description)) {
            List<String> bzList = Lists.newArrayList(description.split(","));
            if (CollectionUtils.containsAny(bzList, filterList)) {
                return (List<String>) CollectionUtils.intersection(filterList, bzList);
            }
        }
        return null;
    }

    /**
     * @param info 流程实例
     * @return 转发信息
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取转发信息
     */
    private List<Map<String, Object>> turnTaskInfo(WorkFlowInfo info) throws Exception {
        // 获取工作流实例模型
        WorkFlowXml xmlDao = workFlowXmlUtil.getWorkFlowInstanceModel(info.getWorkFlowIntanceVo());
        // 获取当前活动定义
        ActivityModel activityModel = xmlDao.getActivity(info.getSourceActivity().getActivityDefinitionId());
        String filterType = null;
        List<String> filterStr = null;
        if (org.apache.commons.lang.StringUtils.isNotBlank(activityModel.getFilterInfo())) {
            //过滤信息对象
            Document fileDoc = DocumentHelper.parseText(activityModel.getFilterInfo());
            //过滤类型：User,Organ,Role
            String user = fileDoc.getRootElement().valueOf("@type");
            org.dom4j.Node corNode = fileDoc.selectSingleNode("//Filter/" + user + "[@Id='" + getCurrentUserId() + "']/Correspondence");
            if (corNode != null) {
                filterType = corNode.valueOf("@type");
                List<DefaultText> organList = corNode.selectNodes(filterType + "/text()");
                if (organList != null && !organList.isEmpty()) {
                    filterStr = new ArrayList<String>();
                    for (DefaultText text : organList) {
                        filterStr.add(text.getText());
                    }
                }
            }
        }

        WorkFlowTransInfo transInfo = info.getTransInfo();
        List<ActivityModel> lstTrans = transInfo.getTranActivitys();
        List<Map<String, Object>> result = new ArrayList<>();
        for (ActivityModel aModel : lstTrans) {
            Map<String, Object> hdMap = Maps.newHashMap();
            hdMap.put("Name", aModel.getActivityDefineName());
            hdMap.put("Id", aModel.getDefineId());
            hdMap.put("Description", aModel.getActivityDescription());
            List<Map<String, Object>> bmList = new ArrayList<>();

            for (PerformerTaskModel userModel : aModel.getPerformerModelList()) {
                if (filterStr != null && userModel.getType().equals(filterType) && !filterStr.contains(userModel.getId())) {
                    continue;
                }
                Map<String, Object> bmMap = Maps.newHashMap();
                bmMap.put("Name", userModel.getName());
                bmMap.put("Id", userModel.getId());
                List<Map<String, Object>> userList = new ArrayList<>();

                // 修改转发 退回会能转发到全部
                List<PfUserVo> performerUsers = userModel.getUserList();
                for (PfUserVo vo : performerUsers) {
                    Map<String, Object> yhMap = Maps.newHashMap();
                    yhMap.put("Name", vo.getUserName());
                    yhMap.put("Id", vo.getUserId());
                    userList.add(yhMap);
                }
                bmMap.put("user", userList);
                bmList.add(bmMap);
            }
            hdMap.put("bm", bmList);
            result.add(hdMap);
        }
        return result;
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 转发流程任务
     */
    @RequestMapping("/forward")
    @ApiOperation(value = "转发流程任务")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ApiImplicitParams({@ApiImplicitParam(name = "forwardTaskDto", value = "转发流程任务", required = true, dataType = "ForwardTaskDto")})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> forwardTask(HttpServletRequest request, ForwardTaskDto forwardTaskDto) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String taskId = forwardTaskDto.getTaskId();
        if (StringUtils.isBlank(taskId)) {
            throw new MissingArgumentException("转发流程的taskId不能为空！");
        }
        try {
            String turnXml = getTurnXml(forwardTaskDto);
            //String turnXml = "<Activitys RelType=\"or\" ReqOpinion=\"false\" SendSMS=\"false\"><Activity Name=\"初审\" Id=\"AC52AC9564DE4CB1A492E135518A2B29\" SelectAll=\"false\" DefaultSelected=\"false\"><UserInfo RoleId=\"DFBF7D4936C14993ACD4FA38BC83611B\" Id=\"CE88A7AB17234DF1A9EECF670C0191E8\"></UserInfo></Activity></Activitys>";
            turnTask(request, taskId, turnXml);
        } catch (Exception e) {
            map.put("code", "fail");
            map.put("msg", e);
            return map;
        }
        return null;
    }

    /**
     * @param forwardTaskDto
     * @return 转发xml
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 将参数转为xml
     */
    public String getTurnXml(ForwardTaskDto forwardTaskDto) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("Activitys");
        root.addAttribute("RelType", "or");
        root.addAttribute("ReqOpinion", "false");
        root.addAttribute("SendSMS", "false");
        String ActivityName = forwardTaskDto.getActivityName();
        String[] ActivityNames = ActivityName.split(",");
        String ActivityId = forwardTaskDto.getActivityId();
        String[] ActivityIds = ActivityId.split(",");
        String roleId = forwardTaskDto.getSelectRoleIds();
        String[] roleIds = roleId.split(",");
        String userName = forwardTaskDto.getSelectUserNames();
        String[] userNames = userName.split(",");
        for (int i = 0; i < ActivityNames.length; i++) {
            Element ActivityEle = root.addElement("Activity");
            ActivityEle.addAttribute("Name", ActivityNames[i]);
            ActivityEle.addAttribute("Id", ActivityIds[i]);
            ActivityEle.addAttribute("SelectAll", "false");
            ActivityEle.addAttribute("DefaultSelected", "false");
            Element userInfo = ActivityEle.addElement("UserInfo");
            userInfo.addAttribute("RoleId", roleIds[i]);
            userInfo.addAttribute("Id", userNames[i]);
        }
        return doc.getRootElement().asXML();
    }

    /**
     * @param taskid  任务id
     * @param request request
     * @param turnXml 转发xml
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 以xml形式转发任务
     */
    private void turnTask(HttpServletRequest request, String taskid, String turnXml) throws Exception {
        String wiid = "";
        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = null;
        if (StringUtils.isNotBlank(taskid)) {
            PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
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
        String userid = getCurrentUserId();
        String xml = turnXml;
        if (StringUtils.isBlank(turnXml)) {
            xml = inputStream2String(request.getInputStream());
        }
        if (StringUtils.isNotBlank(taskid)) {
            //转发检查是否办结，代办任务使用
            WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userid, taskid);
            WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(info.getWorkFlowIntanceVo());
            String activityDefineId = info.getSourceActivity().getActivityDefinitionId();
            // 判断是否任务办结，两种情况（1为最后一个活动，2为中间活动的）
            if (info.getTransInfo().getTranActivitys() != null && info.getTransInfo().getTranActivitys().size() == 0) {
                ActivityModel aModel = workXml.getActivity(activityDefineId);
                ///////排除WaitAllUsers情况，如果其他人还在办理///////////
                PfActivityVo sourceActivityVo = info.getSourceActivity();
                //办结
                String endActivityDefine = workXml.getEndActivityDefine();
                if (StringUtils.isBlank(endActivityDefine)) {
                    ///没有结束节点
                    endActivityDefine = "";
                }
                if (endActivityDefine.equals(activityDefineId) || aModel.isCanFinish() || workXml.getActivitiesCount() == 1) {
                    taskActionService.endTask(wiid, taskid, userid, pfWorkFlowInstanceVo, request, xml, null);
                } else {
                    taskActionService.turnTaskByXml(wiid, taskid, getCurrentUserId(), pfWorkFlowInstanceVo, request, xml);
                }
            } else {
                if (StringUtils.indexOf(xml, "<Activity Id=\"-1\">") > -1) {
                    //办结
                    taskActionService.endTask(wiid, taskid, userid, pfWorkFlowInstanceVo, request, xml, null);
                } else {
                    //转发
                    taskActionService.turnTaskByXml(wiid, taskid, getCurrentUserId(), pfWorkFlowInstanceVo, request, xml);
                }
            }
        }
    }

    /**
     * @param slbh 受理编号
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 在文件中心创建节点
     */
    private void createNode(String slbh) {
        Space rootSpace = nodeService.getWorkSpace("WORK_FLOW_STUFF", true);
        UserInfo userInfo = getCurrentUser();
        nodeService.createNode(rootSpace.getId(), slbh, userInfo.getUsername());
    }

}
