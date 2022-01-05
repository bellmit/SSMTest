package cn.gtmap.msurveyplat.portal.service.impl;

import cn.gtmap.msurveyplat.portal.entity.ProcessInsExtendDto;
import cn.gtmap.msurveyplat.portal.service.TaskActionService;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXml;
import cn.gtmap.msurveyplat.portal.utils.WorkFlowXmlUtil;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.*;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfUserVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.plat.wf.model.ActivityModel;
import com.gtis.plat.wf.model.PerformerTaskModel;
import com.gtis.spring.Container;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/9/30
 * @description 任务所有动作服务实现
 */
@Service
public class TaskActionServiceImpl implements TaskActionService {
    /**
     * 工作流附件在文件中心的表空间
     */
    public static final String WORK_FLOW_STUFF = "WORK_FLOW_STUFF";
    /**
     * 系统管理员
     */
    public static final String ADMIN_USERID = "0";
    @Autowired
    SysUserService sysUserService;
    @Autowired
    WorkFlowCoreService workFlowCoreService;
    @Autowired
    SysWorkFlowInstanceService workFlowIntanceService;
/*    @Autowired
    NodeService nodeService;*/
    @Autowired
    SysDynamicSignService sysDynamicSignService;
    @Autowired
    SysTaskService taskService;
    @Autowired
    TaskPerformerFilterServiceContext taskPerformerFilterServiceContext;
    @Autowired
    WorkFlowXmlUtil workFlowXmlUtil;
/*    @Resource(name = "taskPerformerMinWorkloadFilterService")
    TaskPerformerFilterService taskPerformerFilterService;*/
    /**
     * 删除
     * 管理员 或 在首节点,同时也是创建人 可删除工作流;
     * 在首节点，不是创建人,是参与者 可删除所属任务;
     * 在首节点，不是创建人和参与者 或 不在首节点  无法删除
     *
     * @return
     * @throws Exception
     */
    @Override
    public String delTask(String wiid, String taskid, String userid, String reason, String proid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        String msg = "";
        if (StringUtils.isBlank(wiid)) {
            wiid = pfWorkFlowInstanceVo.getWorkflowIntanceId();
        }
        PfTaskVo taskVo = taskService.getTask(taskid);
        try {
            switch (permitDel(taskVo, userid)) {
                case 0: {
                    delProject(wiid, userid,taskid);
                    msg = "1";
                    break;
                }
                case 1: {
                    delProject(wiid, userid,taskid);
                    msg = "1";
                    break;
                }
                case 3: {
                    msg = "3";
                    throw new RuntimeException(msg);
                }
                default: {
                    msg = "2";
                    throw new RuntimeException(msg);
                }
            }
        } catch (Exception e) {
            if(StringUtils.isBlank(msg)) {
                msg="3";
            }
            throw new RuntimeException(msg);
        }

        return msg;
    }

    @Override
    public void retrieveTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        try {
            workFlowCoreService.retrieveWorkFlow(userid, taskid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除项目
     *
     * @param wiid
     */
    private String delProject(String wiid, String userId,String taskid){
        String msg = "";
        PfWorkFlowInstanceVo workFlowInstanceVo = workFlowIntanceService.getWorkflowInstance(wiid);
        try {
            //删除附件
            PfWorkFlowInstanceVo intanceVo = workFlowIntanceService.getWorkflowInstance(wiid);
//            Space space = nodeService.getWorkSpace(WORK_FLOW_STUFF);
//            Node tempNode = nodeService.getNode(space.getId(), intanceVo.getProId());
//            nodeService.remove(tempNode.getId());
        } catch (Exception e) {
        }
        //删除用户签名
        try {
            sysDynamicSignService.deleteUserSignByProId(workFlowInstanceVo.getProId());
        } catch (Exception e) {
        }

        //删除工作流实例
        try {
            workFlowCoreService.deleteWorkFlowInstance(userId, wiid);
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }


    /**
     * 判断删除情况
     *
     * @param taskVo
     * @return 0 超级用户 或 在首节点,同时也是创建人;
     * 1 在首节点，不是创建人;是参与者(参与者只有一人时);
     * 2 在首节点，不是创建人;是参与者(参与者有多人时)
     * 3 该项目不在首节点，无法删除
     * 5 无返回
     */
    private int permitDel(PfTaskVo taskVo, String userId) throws Exception {
        if (StringUtils.equals(userId, ADMIN_USERID) || taskVo == null) {
            return 0;
        } else {
            PfActivityVo activityVo = taskService.getActivity(taskVo.getActivityId());
            PfWorkFlowInstanceVo workFlowInstanceVo = workFlowIntanceService.getWorkflowInstance(activityVo.getWorkflowInstanceId());
            WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(workFlowInstanceVo);
            if (workXml.getBeginActivityDefine().equals(activityVo.getActivityDefinitionId())) {
                if (workFlowInstanceVo.getCreateUser().equals(userId)) {
                    return 0;
                } else {
                    if (userId.equals(taskVo.getUserVo().getUserId())) {
                        return 0;
                    } else {
                        return 3;
                    }
                }
            }else{
                return 2;
            }
        }
    }

    @Override
    public void turnTaskByXml(String wiid,String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request,String turnXml){
        try {
            workFlowCoreService.turnTask(turnXml, taskid,userid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void turnTaskByWorkFlowInfo(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request,String turnXml) {
        try {
            WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userid,taskid);
            // 处理活动
            List<ActivityModel> lstActivityModel = info.getTransInfo().getTranActivitys();

            WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(info.getWorkFlowIntanceVo());
            ActivityModel activityModel = workXml.getActivity(info.getSourceActivity().getActivityDefinitionId());
            //下一个默认活动的处理
            String defaultSelectNextActivityName = activityModel.getDefaultSelectName();
            if(StringUtils.isNotBlank(defaultSelectNextActivityName)){
                for (Iterator<ActivityModel> itModel = lstActivityModel.iterator(); itModel.hasNext(); ) {
                    ActivityModel model = itModel.next();
                    if(!defaultSelectNextActivityName.equalsIgnoreCase(model.getActivityDefineName())) {
                        itModel.remove();
                    }
                }
            }

            //如果splittype为xor
            if ( StringUtils.isBlank(activityModel.getSplitType()) || activityModel.getSplitType().equalsIgnoreCase("XOR")) {
                int i = 0;
                for (Iterator<ActivityModel> it = lstActivityModel.iterator();it.hasNext();) {
                    if (i > 0) {
                        it.remove();
                    }
                    it.next();
                    i++;
                }
            }

            workFlowCoreService.postWorkFlow(userid, taskid, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void turnBackTask(String wiid,String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request,String adids,String remark){
        try {
            if (StringUtils.isNotBlank(adids)) {
                String[] backDefineActivitys=adids.split(",");
                workFlowCoreService.postBackWorkFlow(userid, taskid, backDefineActivitys,remark);
            }else{
                WorkFlowInfo info=workFlowCoreService.getWorkFlowTurnBackInfo(userid, taskid);
                List<PfActivityVo> backActivitys=info.getTargetActivitys();
                if (backActivitys!=null&&backActivitys.size()>0) {
                    String[] backDefineActivitys = {backActivitys.get(0).getActivityDefinitionId()};
                    workFlowCoreService.postBackWorkFlow(userid, taskid, backDefineActivitys, remark);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endTask(String wiid,String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request,String turnXml,WorkFlowInfo info){
        try {
            if(info==null){
                if(StringUtils.isBlank(turnXml)) {
                    turnXml=inputStream2String(request.getInputStream());
                }
                workFlowCoreService.turnTask(turnXml, taskid,userid);
            }else {
                workFlowCoreService.postWorkFlow(userid, taskid, info);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopTask(String wiid,String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request){


    }

    @Override
    public void createTask(String wiid,String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request){

    }

    @Override
    public void postTask(String wiid, String taskid, String userid, String remark, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        try {
            PfTaskVo taskVo = taskService.getTask(taskid);
            PfActivityVo activityVo = taskService.getActivity(taskVo
                    .getActivityId());
            String decode =null;
            if (StringUtils.isNotBlank(remark)) {
                decode = java.net.URLDecoder.decode(remark,"utf-8");
            }
            //workFlowCoreService.lockWorkFlowInstance(activityVo.getWorkflowInstanceId(),taskid,decode, SessionUtil.getUserId(request));
            workFlowCoreService.lockWorkFlowInstance(activityVo.getWorkflowInstanceId(),taskid,decode);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upPostTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        try {
            PfTaskVo taskVo = taskService.getTask(taskid);
            PfActivityVo activityVo = taskService.getActivity(taskVo.getActivityId());
            //此处解挂，需要判断是否有未办结子流程，并且是否设置了该节点启动子流程后自动挂起
            //判断是否是子流程
            boolean hasFinishSub = true;
            PfWorkFlowInstanceVo instanceVo = workFlowIntanceService.getWorkflowInstance(activityVo.getWorkflowInstanceId());
            WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(instanceVo);
            ActivityModel sourceActivityModel = workXml.getActivity(activityVo.getActivityDefinitionId());
            if (sourceActivityModel.getSubProcess()!=null) {
                SysWorkFlowInstanceRelService sysWorkFlowInstanceRelService = (SysWorkFlowInstanceRelService) Container.getBean("SysWorkFlowInstanceServiceRelImpl");
                //查找关联流程
                List<PfWorkFlowInstanceVo> relList = sysWorkFlowInstanceRelService.getWorkFlowRelList(instanceVo.getWorkflowIntanceId());
                if (relList != null && relList.size() > 0){
                    for (int i = 0; i < relList.size(); i++) {
                        //查找制定的子流程定义的工作流，只处理未办结的关联流程
                        if (relList.get(i).getWorkflowState() != 2 && relList.get(i).getWorkflowDefinitionId().equals(sourceActivityModel.getSubProcess().getId())){
                            //验证该流程是否是子流程（流程定义id和开始任务的主键id一致）
                            PfTaskVo subTaskVo = taskService.getTaskAll(relList.get(i).getWorkflowIntanceId());
                            if (subTaskVo != null && StringUtils.isNotBlank(subTaskVo.getTaskId())){
                                hasFinishSub = false;
                            }
                        }
                    }
                }
            }
            if (hasFinishSub){
                workFlowCoreService.unLockWorkFlowInstance(activityVo.getWorkflowInstanceId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void priorityTask(String wiid,String taskid, String userid, String priority, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        try {
            workFlowIntanceService.updateWorkFlowIntancePriority(wiid, priority);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取提交的XML信息
     *
     * @return
     */
    private Document parseTurnInfo(String xmlStr) throws Exception{
        Document doc = null;
        doc = DocumentHelper.parseText(xmlStr);
        return doc;
    }


    @Override
    public void autoTurnTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request) {
        try {
            WorkFlowInfo info = workFlowCoreService.getWorkFlowTurnInfo(userid, taskid);
            // 处理活动
            List<ActivityModel> lstActivityModel = info.getTransInfo().getTranActivitys();

            WorkFlowXml workXml = workFlowXmlUtil.getWorkFlowInstanceModel(info.getWorkFlowIntanceVo());
            ActivityModel activityModel = workXml.getActivity(info.getSourceActivity().getActivityDefinitionId());
            //下一个默认活动的处理
            String defaultSelectNextActivityName = activityModel.getDefaultSelectName();
            if (StringUtils.isNotBlank(defaultSelectNextActivityName)) {
                for (Iterator<ActivityModel> itModel = lstActivityModel.iterator(); itModel.hasNext(); ) {
                    ActivityModel model = itModel.next();
                    if (!defaultSelectNextActivityName.equalsIgnoreCase(model.getActivityDefineName())) {
                        itModel.remove();
                    }
                }
            }
            //根据模型过滤用户

            boolean isTaskBack = info.getSourceTask() != null ? info.getSourceTask().isBackState() : false;
            String autoTurnTaskEnable = AppConfig.getProperty("autoTurnTask.enble");
            for (ActivityModel aModel : lstActivityModel) {
                List<PfUserVo> performerUsers = null;
                String performerStrategy = aModel.getExtendedAttribute("FilterTransferPerformerStrategy");
                List<PfTaskVo> targetTaskVoList = info.getTargetTasks();
                for (PerformerTaskModel userModel : aModel.getPerformerModelList()) {
                    if (isTaskBack && AppConfig.getBooleanProperty("task.turnPerformer.useTurnBack", false)) {
/*
                        performerUsers = taskPerformerFilterServiceContext.getTaskPerformerFilterServiceByName(TaskPerformerFilterServiceContext.USE_TURN_BACK_PERFORMER)
                                .getTaskPerformers(info.getWorkFlowIntanceVo().getWorkflowIntanceId(), aModel.getDefineId(), info.getSourceTask(), userModel.getUserList(), info.getWorkFlowDefineVo().getWorkflowDefinitionId());
*/

                    } else if (StringUtils.isNotBlank(performerStrategy)) {
/*
                        performerUsers = taskPerformerFilterServiceContext.getTaskPerformerFilterServiceByName(performerStrategy)
                                .getTaskPerformers(info.getWorkFlowIntanceVo().getWorkflowIntanceId(), aModel.getDefineId(), info.getSourceTask(), userModel.getUserList(), info.getWorkFlowDefineVo().getWorkflowDefinitionId());
*/

                    } else if (StringUtils.equals(autoTurnTaskEnable, "true")) {
        /*                performerUsers = taskPerformerFilterService.getTaskPerformers(info.getWorkFlowIntanceVo().getWorkflowIntanceId(), aModel.getDefineId(), info.getSourceTask(), userModel.getUserList(), info.getWorkFlowDefineVo().getWorkflowDefinitionId());
*/
                    } else {
                        performerUsers = userModel.getUserList();
                    }
                    if (CollectionUtils.isNotEmpty(performerUsers)) {
                        userModel.setUserList(performerUsers);
                    }

                }
            }

            workFlowCoreService.postWorkFlow(userid, taskid, info);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProcessInsExtendDto> getXmTaskId(List<String> tasktdList,List<String>xmidList) {
        List<ProcessInsExtendDto> processInsExtendDtoList = new ArrayList<>();
          for(int i = 0;i< tasktdList.size();i++){
              ProcessInsExtendDto processInsExtendDto = new ProcessInsExtendDto();
              processInsExtendDto.setProjectId(tasktdList.get(i));//taskid；给taskid赋值
              processInsExtendDtoList.add(processInsExtendDto);
          }
         for(int i =0;i<xmidList.size();i++){
             processInsExtendDtoList.get(i).setText9(xmidList.get(i));//xmid:给xmid赋值 text9与前台获取xmid变量text9相对应
         }

        return  processInsExtendDtoList;
    }

    private  String inputStream2String(InputStream is) throws IOException {
        String result = "";
        try{
            result=IOUtils.toString(is, "UTF-8");
            result="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+result;
        }catch (Exception e){
        }finally {
            is.close();
        }
        return result;
    }
}
