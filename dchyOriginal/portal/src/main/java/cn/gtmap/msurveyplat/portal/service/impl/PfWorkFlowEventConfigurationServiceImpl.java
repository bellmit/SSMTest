package cn.gtmap.msurveyplat.portal.service.impl;

import cn.gtmap.msurveyplat.portal.config.Constants;
import cn.gtmap.msurveyplat.portal.dao.BaseDao;
import cn.gtmap.msurveyplat.portal.entity.PfWorkFlowEvent;
import cn.gtmap.msurveyplat.portal.service.PfWorkFlowEventConfigurationService;
import cn.gtmap.msurveyplat.portal.utils.QueryCondition;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowException;
import com.gtis.plat.wf.WorkFlowInfo;
import com.gtis.util.ThreadPool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/*

 *
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2017/11/15
 * @description 不动产登记服务
 */
@Service
public class PfWorkFlowEventConfigurationServiceImpl implements PfWorkFlowEventConfigurationService {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(PfWorkFlowEventConfigurationServiceImpl.class);

    @Resource(name = "serverBaseDaoImpl")
    BaseDao baseDao;
    @Resource
    SysWorkFlowDefineService sysWorkFlowDefineService;
    @Autowired
    SysTaskService taskService;
    @Autowired
    SysWorkFlowInstanceService sysWorkFlowInstanceService;


    @Override
    public boolean doWfEvent(String wiid, String taskid, String userid, String eventName, String workFlowDefinitionId, String proid) throws Exception {

        log.info("**********工作流转发开始***********");
        log.info("**********工作流转发开始,入参wiid***********" + wiid);
        log.info("**********工作流转发开始,入参taskid***********" + taskid);
        log.info("**********工作流转发开始,入参userid***********" + userid);
        log.info("**********工作流转发开始,入参eventName***********" + eventName);
        log.info("**********工作流转发开始,入参workFlowDefinitionId***********" + workFlowDefinitionId);
        WorkFlowInfo infoObj = getInfoObj(taskid, userid, workFlowDefinitionId, proid, wiid);
        List<PfWorkFlowEvent> pfWorkFlowEventList = null;
        if (StringUtils.isNotBlank(eventName)) {
            //处理转发验证
            //第一步  先查询是否配置转发项   通过WORKFLOW_DEFINITION_ID、WORKFLOW_EVENT_NAME
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(wiid);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("workFlowEventName", eventName);
            if (pfWorkFlowInstanceVo != null) {
                param.put("workFlowDefinitionId", pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                pfWorkFlowEventList = listPfWorkFlowEvent(param);
            } else if (StringUtils.isNotBlank(workFlowDefinitionId)) {
                param.put("workFlowDefinitionId", workFlowDefinitionId);
                pfWorkFlowEventList = listPfWorkFlowEvent(param);
            } else {
                pfWorkFlowEventList = null;
            }

            log.info("**********工作流转发配置的接口列表***********" + JSON.toJSONString(pfWorkFlowEventList));

        }

        //第二步 组织数据 调取server 包验证方法
        if (CollectionUtils.isNotEmpty(pfWorkFlowEventList)) {
            try {
                for (int i = 0; i < pfWorkFlowEventList.size(); i++) {
                    log.info("-------------------------事件名称:" + eventName);
                    PfWorkFlowEvent pfWorkFlowEvent = pfWorkFlowEventList.get(i);
                    String activityDefinitionId = pfWorkFlowEvent.getActivityDefinitionId();

                    //查询数据里ACTIVITY_DEFINITION_ID  是否有值

                    PfActivityVo activityVo = infoObj.getSourceActivity();
                    if (activityVo != null && activityDefinitionId != null && !"".equals(activityDefinitionId.trim())
                            && !activityDefinitionId.equals(activityVo.getActivityDefinitionId())) {
                        continue;
                    }
                    excuteCommand(infoObj, pfWorkFlowEvent);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }

        return true;
    }

    public WorkFlowInfo getInfoObj(String taskid, String userid, String workFlowDefinitionId, String proid, String wiid) {
        WorkFlowInfo info = new WorkFlowInfo();
        info.setUserId(userid);
        PfTaskVo pfTaskVo = taskService.getTask(taskid);
        if (pfTaskVo == null) {
            pfTaskVo = taskService.getHistoryTask(taskid);
        }
        if (pfTaskVo != null) {
            info.setSourceTask(pfTaskVo);
            if (StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                PfActivityVo pfActivityVo = taskService.getActivity(pfTaskVo.getActivityId());
                if (pfActivityVo != null) {
                    //转发前的节点
                    info.setSourceActivity(pfActivityVo);
                    if (StringUtils.isNotBlank(pfActivityVo.getWorkflowInstanceId())) {
                        PfWorkFlowInstanceVo pfWorkFlowInstanceVo = sysWorkFlowInstanceService.getWorkflowInstance(pfActivityVo.getWorkflowInstanceId());
                        if (pfWorkFlowInstanceVo != null) {
                            info.setWorkFlowIntanceVo(pfWorkFlowInstanceVo);
                            if (StringUtils.isNotBlank(pfWorkFlowInstanceVo.getWorkflowDefinitionId())) {
                                PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
                                info.setWorkFlowDefineVo(pfWorkFlowDefineVo);
                            }
                        }
                        //获取转发后的节点
                        List<PfActivityVo> targetActivityVoList = taskService.getWorkFlowInstanceActivityList(pfActivityVo.getWorkflowInstanceId());
                        if (CollectionUtils.isNotEmpty(targetActivityVoList)) {
                            List<PfActivityVo> targetActivityList = new ArrayList<PfActivityVo>();
                            for (PfActivityVo activityVo : targetActivityVoList) {
                                if (activityVo.getActivityState() == 1) {
                                    List<PfTaskVo> targetTaskList = taskService.getTaskListByActivity(activityVo.getActivityId());
                                    targetActivityList.add(activityVo);
                                    info.setTargetTasks(targetTaskList);
                                }
                            }
                            info.setTargetActivitys(targetActivityList);
                        }
                    }
                }
            }
        } else {
            PfWorkFlowInstanceVo workFlowIntanceVo = new PfWorkFlowInstanceVo();
            workFlowIntanceVo.setWorkflowIntanceId(wiid);
            workFlowIntanceVo.setProId(proid);
            info.setWorkFlowIntanceVo(workFlowIntanceVo);
            PfWorkFlowDefineVo pfWorkFlowDefineVo = sysWorkFlowDefineService.getWorkFlowDefine(workFlowDefinitionId);
            info.setWorkFlowDefineVo(pfWorkFlowDefineVo);
        }
        return info;
    }

    public List<PfWorkFlowEvent> listPfWorkFlowEvent(Map<String, Object> param) {
        if (MapUtils.isEmpty(param)) {
            return Lists.newArrayList();
        }
        List<QueryCondition> queryConditions = new ArrayList<QueryCondition>();
        Iterator<Map.Entry<String, Object>> entries = param.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            QueryCondition queryCondition = new QueryCondition(entry.getKey(), QueryCondition.EQ, entry.getValue());
            queryConditions.add(queryCondition);
        }
        return baseDao.get(PfWorkFlowEvent.class, queryConditions, " order by o.workflowEventOrder asc");
    }

    private void excuteCommand(WorkFlowInfo infoObj, PfWorkFlowEvent pfWorkFlowEvent) throws UnsupportedEncodingException, WorkFlowException {

        String proId = infoObj.getWorkFlowIntanceVo().getProId();
        if (StringUtils.isNotEmpty(pfWorkFlowEvent.getWorkFlowEventUrl())) {
            String url = AppConfig.getPlaceholderValue(pfWorkFlowEvent.getWorkFlowEventUrl());
            if (url.indexOf("${server.url}") != -1) {
                url = url.replace("${server.url}", AppConfig.getProperty("server.url"));
            }
            if (url.indexOf("${portal.url}") != -1) {
                url = url.replace("${portal.url}", AppConfig.getProperty("portal.url"));
            }
            if (url.indexOf("${platform.url}") != -1) {
                url = url.replace("${platform.url}", AppConfig.getProperty("platform.url"));
            }
            if (url.indexOf("${serviceol.url}") != -1) {
                url = url.replace("${serviceol.url}", AppConfig.getProperty("serviceol.url"));
            }
            if (url.indexOf("${promanage.url}") != -1) {
                url = url.replace("${promanage.url}", AppConfig.getProperty("promanage.url"));
            }
            boolean asyn = false;
            if (StringUtils.isNotBlank(pfWorkFlowEvent.getWorkFlowEventAsyn()) && StringUtils.equals(pfWorkFlowEvent.getWorkFlowEventAsyn(), "1")) {
                asyn = true; //表示异步执行
            }
            log.info("**********工作流转发事件执行的接口***********" + url);
            String result = excuteUrl(url, proId, infoObj, asyn);
            if (StringUtils.isNotBlank(result)) {
                throw new WorkFlowException(java.net.URLDecoder.decode(result, "UTF-8"));
            }

        }

    }

    private String excuteUrl(String urlStr, String proId, WorkFlowInfo infoObj, boolean asyn) throws UnsupportedEncodingException {
        String[] urlStrs = urlStr.split(Constants.URL_SEPARATOR_SPLIT);
        String workflowParaUrl = getWorkFlowInfoUrlFormat(infoObj);
        String platUrl = AppConfig.getProperty("platform.url");
        for (String urlTemp : urlStrs) {
            urlTemp = urlTemp.trim();
            if (!urlTemp.startsWith("http://")) {
                urlTemp = "http://" + platUrl + urlTemp;
            }
            try {
                if (urlTemp.indexOf("?") > 0) {
                    //urlTemp = urlTemp + "&xmid=" + proId + "&" + workflowParaUrl;
                    urlTemp = urlTemp + "&xmid=" + proId + "&gzldyid=" + infoObj.getWorkFlowDefineVo().getWorkflowDefinitionId();
                } else {
                    //urlTemp = urlTemp + "?xmid=" + proId + "&" + workflowParaUrl;
                    urlTemp = urlTemp + "?xmid=" + proId + "&gzldyid=" + infoObj.getWorkFlowDefineVo().getWorkflowDefinitionId();
                }
                if (infoObj.getSourceActivity() != null) {
                    urlTemp = urlTemp + "&adid=" + infoObj.getSourceActivity().getActivityId();
                }
                if (infoObj.getTargetActivitys() != null) {
                    StringBuilder targetActivityDefIds = new StringBuilder();
                    for (PfActivityVo pfActivityVo : infoObj.getTargetActivitys()) {
                        if (pfActivityVo != null) {
                            if (StringUtils.isNotBlank(targetActivityDefIds.toString())) {
                                targetActivityDefIds.append(",").append(pfActivityVo.getActivityDefinitionId());
                            } else {
                                targetActivityDefIds.append(pfActivityVo.getActivityDefinitionId());
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(targetActivityDefIds.toString())) {
                        urlTemp = urlTemp + "&gzljdid=" + targetActivityDefIds;
                    }
                }
/*                if (infoObj.getTargetTasks() != null && !infoObj.getTargetTasks().isEmpty()) {
                    List<HashMap> targetTaskList = new ArrayList<HashMap>();
                    for (int i = 0; i < infoObj.getTargetTasks().size(); i++) {
                        PfTaskVo taskVo = infoObj.getTargetTasks().get(i);
                        HashMap map = new HashMap();
                        map.put("targetTaskId", taskVo.getTaskId());
                        map.put("targetUserId", taskVo.getUserVo().getUserId());
                        map.put("targetActivityId", taskVo.getActivityId());
                        targetTaskList.add(map);
                    }
                    //
                    urlTemp = urlTemp + "&targetTasksInfo=" + java.net.URLEncoder.encode(JSON.toJSONString(targetTaskList), "utf-8");
                }*/

                log.info("-------------------------urlTemp:" + urlTemp);
                return excuteUrl(urlTemp, asyn);
            } catch (Exception e) {
                log.error("工作流事件定义url错误,请检查！" + urlTemp, e);
            }
        }
        return null;
    }

    /*
     *
     * 获取参数的WorkFlowInfo html url格式字符串的形式
     *
     * @return
     */


    private String getWorkFlowInfoUrlFormat(WorkFlowInfo infoObj) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("userid=");
        stringBuilder.append(infoObj.getUserId());
        if (infoObj.getSourceActivity() != null) {
            stringBuilder.append("&activityid=");
            stringBuilder.append(infoObj.getSourceActivity().getActivityId());
        }
        if (infoObj.getSourceTask() != null) {
            stringBuilder.append("&taskid=");
            stringBuilder.append(infoObj.getSourceTask().getTaskId());
        }
        List<PfActivityVo> targetActivitys = infoObj.getTargetActivitys();
        if (targetActivitys != null) {
            stringBuilder.append("&targetActivityIds=");
            for (int index = 0; index < targetActivitys.size(); index++) {
                if (index != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(targetActivitys.get(index).getActivityId());
            }
            stringBuilder.append("&targetActivityNames=");
            for (int index = 0; index < targetActivitys.size(); index++) {
                if (index != 0) {
                    stringBuilder.append(",");
                }
                if (StringUtils.isNotBlank(targetActivitys.get(index).getActivityName())) {
                    stringBuilder.append(java.net.URLEncoder.encode(targetActivitys.get(index).getActivityName(), "utf-8"));

                }
            }
        }
        return stringBuilder.toString();
    }

    private String excuteUrl(String urlString, boolean asyn) throws IOException {
        if (asyn) {
            final String urlStr = urlString;
            ///采用异步的方式执行，这个就没有返回结果了
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    HttpClient httpClient = null;
                    GetMethod getMethod = null;
                    try {
                        httpClient = new HttpClient();
                        httpClient.getHttpConnectionManager().getParams()
                                .setConnectionTimeout(5 * 10000);
                        getMethod = new GetMethod(urlStr);
                        httpClient.executeMethod(getMethod);
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    } finally {
                        if (getMethod != null) {
                            getMethod.releaseConnection();
                        }
                    }
                }
            });
        } else {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setConnectTimeout(5 * 10000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream urlStream = null;
                BufferedReader in = null;
                try {
                    urlStream = httpConnection.getInputStream();
                    in = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (urlStream != null) {
                        urlStream.close();
                    }
                }
            } else {
                throw new RuntimeException(new Exception());
            }
            httpConnection.disconnect();
        }
        return null;
    }

}
