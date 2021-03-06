package cn.gtmap.msurveyplat.promanage.core.service.mq.service.Impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.TaskData;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.CgtjService;
import cn.gtmap.msurveyplat.promanage.service.impl.PortalFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.ResultsSubmitServiceUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service("chxmCgtjService")
public class ChxmCgtjServiceImpl implements CgtjService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChxmCgtjServiceImpl.class);

    @Value("${cgtj.chxmMode.gzldyid}")
    private String wdid;

    @Value("${cgtj.userId}")
    private String userId;

    @Autowired
    private SysWorkFlowInstanceService workFlowIntanceService;
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    PortalFeignServiceImpl portalFeignService;

    /**
     * @param fileName
     * @param inputStream
     * @param xsSqxxid
     * @param xsSqbh
     * @return cn.gtmap.msurveyplat.common.domain.TaskData
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: fileName
     * @param: inputStream
     * @param: xsSqxxid
     * @param: xsSqbh
     * @time 2021/3/13 14:46
     * @description ????????????
     */
    @Override
    public ResponseMessage cgjc(String fileName, InputStream inputStream, String xsSqxxid, String xsSqbh) throws IOException {
        Example example = new Example(DchyXmglSqxx.class);
        example.createCriteria().andEqualTo("xssqxxid", xsSqxxid);
        List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(example);
        String sqxxid = null;
        if (CollectionUtils.isEmpty(dchyXmglSqxxList)) {
            TaskData taskData = portalFeignService.createTask(wdid, userId);
            if (null != taskData) {
                sqxxid = taskData.getExecutionId();
            }
            xsSqbh = UUIDGenerator.generate18();
        } else {
            sqxxid = dchyXmglSqxxList.get(0).getSqxxid();
            xsSqbh = dchyXmglSqxxList.get(0).getXssqbh();
        }
        ResponseMessage message = null;
        if (StringUtils.isNotBlank(sqxxid)) {
            Map<String, Object> mapParam = Maps.newHashMap();
            DchyXmglSqxx dchyXmglSqxx = entityMapper.selectByPrimaryKey(DchyXmglSqxx.class, sqxxid);
            if (null != dchyXmglSqxx) {
                dchyXmglSqxx.setXssqbh(xsSqbh);
                dchyXmglSqxx.setXssqxxid(xsSqxxid);
                mapParam.put("sqxxid", sqxxid);
                // ???????????????
                fileName = CommonUtil.formatFileName(fileName);
                message = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().checkZipFiles(mapParam, fileName, inputStream);
                entityMapper.updateByPrimaryKeySelective(dchyXmglSqxx);
            } else {
                message = ResponseUtil.wrapParamEmptyFailResponse();
            }
        } else {
            message = ResponseUtil.wrapExceptionResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return cn.gtmap.msurveyplat.common.domain.TaskData
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: fileName
     * @param: inputStream
     * @param: xsSqxxid
     * @param: xsSqbh
     * @time 2021/3/13 14:46
     * @description ????????????
     */
    @Override
    public ResponseMessage cgtj(Map<String, Object> param) throws IOException {
        ResponseMessage message = new ResponseMessage();
        Map<String, String> mapResult = Maps.newHashMap();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String xssqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sqxxid"));

            Example example = new Example(DchyXmglSqxx.class);
            example.createCriteria().andEqualTo("xssqxxid", xssqxxid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(example);
            if (CollectionUtils.isNotEmpty(dchyXmglSqxxList)) {
                PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowIntanceService.getWorkflowInstanceByProId(dchyXmglSqxxList.get(0).getSqxxid());
                List<Map<String, String>> errorInfoModels = (List<Map<String, String>>) data.get("errorInfoModels");
                message = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().zipUpload(pfWorkFlowInstanceVo.getWorkflowIntanceId(), dchyXmglSqxxList.get(0).getSqxxid(), errorInfoModels);
                //????????????
//            WfEventDto wfEventDto = new WfEventDto();

//            List<PfTaskVo> pfTaskVoList =  sysTaskService.getTaskListByInstance(pfWorkFlowInstanceVo.getWorkflowIntanceId());
//            PfWorkFlowDefineVo pfWorkFlowDefineVo = new PfWorkFlowDefineVo();
//            pfWorkFlowDefineVo.setWorkflowDefinitionId(wfEventDto.getGzldyid());
//            WorkFlowXml workFlowXml = workFlowXmlUtil.getDefineModel(pfWorkFlowDefineVo);
                //????????????????????????
//            List<PfActivityVo> targetActivityVoList = sysTaskService.getWorkFlowInstanceActivityList(pfWorkFlowInstanceVo.getWorkflowIntanceId());
//            if (CollectionUtils.isNotEmpty(targetActivityVoList)) {
//                List<PfActivityVo> targetActivityList = new ArrayList<PfActivityVo>();
//                for (PfActivityVo activityVo : targetActivityVoList) {
//                    if (activityVo.getActivityState() == 1) {
//                        List<PfTaskVo> targetTaskList = sysTaskService.getTaskListByActivity(activityVo.getActivityId());
//                        targetActivityList.add(activityVo);
//                        info.setTargetTasks(targetTaskList);
//                    }
//                }
//                info.setTargetActivitys(targetActivityList);
//            }
//            wfEventDto.setAdid(pfTaskVoList.get(0).getActivityId());
//            wfEventDto.setGzldyid(pfWorkFlowInstanceVo.getWorkflowDefinitionId());
//            wfEventDto.setGzljdid(workFlowXml.get);
//            workFlowService.lczfcl(wfEventDto);
//            taskActionService.turnTaskByWorkFlowInfo(wiid, taskid, userid, pfWorkFlowInstanceVo, request, "");
                portalFeignService.turnTask(pfWorkFlowInstanceVo.getWorkflowIntanceId(), userId);
            }


        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
        }
        return message;
    }

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/3/13 14:43
     * @description ???????????????
     */
    @Override
    public String getCode() {
        return Constants.CGTJ_MODE_CHXM;
    }
}
