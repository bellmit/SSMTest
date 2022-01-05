package cn.gtmap.msurveyplat.exchange.service.process.impl;

import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.UserTaskDto;
import cn.gtmap.msurveyplat.exchange.service.process.ProcessService;
import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/15
 * @description 流程服务
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessServiceImpl.class);

    @Autowired
    private SysWorkFlowDefineService sysWorkFlowDefineService;

    @Override
    public List<ProcessDefData> getWorkflowDefinitionList() {
        List<ProcessDefData> processDefDataList = new ArrayList<>();
        List<PfWorkFlowDefineVo> pfWorkFlowDefineVoList = sysWorkFlowDefineService.getWorkFlowDefineList();
        if(CollectionUtils.isNotEmpty(pfWorkFlowDefineVoList)) {
            ProcessDefData processDefData = null;
            for(PfWorkFlowDefineVo pfWorkFlowDefineVo:pfWorkFlowDefineVoList) {
                processDefData = new ProcessDefData();
                processDefData.setId(pfWorkFlowDefineVo.getWorkflowDefinitionId());
                processDefData.setName(pfWorkFlowDefineVo.getWorkflowName());
                processDefDataList.add(processDefData);
            }
        }
        return processDefDataList;
    }

    @Override
    public List<UserTaskDto> getUserTaskDtoListByGzldyid(String gzldyid) {
        List<UserTaskDto> userTaskDtoList = new ArrayList<>();
        if(StringUtils.isNotBlank(gzldyid)) {
            try {
                String xml = sysWorkFlowDefineService.getWorkFlowDefineXml(gzldyid);
                Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
                Element rootElt = doc.getRootElement();
                List nodeList = rootElt.selectNodes("//Package/WorkflowProcesses/WorkflowProcess/Activities/Activity");
                UserTaskDto userTaskDto = null;
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (int i = 0; i < nodeList.size(); i++) {
                        userTaskDto = new UserTaskDto();
                        Element element = (Element) nodeList.get(i);
                        String id = element.attributeValue("Id");
                        String name = element.attributeValue("Name");
                        userTaskDto.setId(id);
                        userTaskDto.setName(name);
                        userTaskDtoList.add(userTaskDto);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("ProcessServiceImpl.getUserTaskDtoByGzldyid",e);
            }
        }
        return userTaskDtoList;
    }

}
