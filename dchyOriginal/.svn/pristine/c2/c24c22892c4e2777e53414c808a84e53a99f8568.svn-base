package cn.gtmap.msurveyplat.serviceol.utils;

import com.gtis.plat.service.SysWorkFlowDefineService;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowDefineVo;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.spring.Container;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class WorkFlowXmlUtil {
    @Autowired
    SysWorkFlowInstanceService workFlowIntanceService;
    @Autowired
    SysWorkFlowDefineService workFlowDefineService;

    public SysWorkFlowInstanceService getWorkFlowIntanceService() {
        return workFlowIntanceService;
    }

    public void setWorkFlowIntanceService(SysWorkFlowInstanceService workFlowIntanceService) {
        this.workFlowIntanceService = workFlowIntanceService;
    }

    public SysWorkFlowDefineService getWorkFlowDefineService() {
        return workFlowDefineService;
    }

    public void setWorkFlowDefineService(
            SysWorkFlowDefineService workFlowDefineService) {
        this.workFlowDefineService = workFlowDefineService;
    }

    public WorkFlowXml getWorkFlowDefineModel(PfWorkFlowDefineVo defineVo) {
        String xml = workFlowDefineService.getWorkFlowDefineXml(defineVo);
        WorkFlowXml modelXml = new WorkFlowXml(xml);
        modelXml.setModifyDate(defineVo.getModifyDate());
        return modelXml;
    }

    public WorkFlowXml getWorkFlowInstanceModel(PfWorkFlowInstanceVo instanceVo) {
        String xml = workFlowIntanceService
                .getWorkflowInstanceXml(instanceVo);
        WorkFlowXml modelXml = new WorkFlowXml(xml);
        modelXml.setModifyDate(instanceVo.getModifyDate());
        return modelXml;
    }

    public void updateGobalValByProId(String proId, String valName,
                                      String val) {
        PfWorkFlowInstanceVo vo = workFlowIntanceService.getWorkflowInstanceByProId(proId);
        updateGobalVal(vo.getWorkflowIntanceId(), valName, val);
    }

    public HashMap<String, Object> getGobalValByProId(String proId) {
        PfWorkFlowInstanceVo vo = workFlowIntanceService.getWorkflowInstanceByProId(proId);
        if (vo != null)
            return getGobalVal(vo.getWorkflowIntanceId());
        else
            return new HashMap<String, Object>();
    }

    public void updateGobalVal(String workflowIntanceId, String valName,
                               String val) {
        workFlowIntanceService.updateGobalVal(workflowIntanceId, valName, val);
    }

    public HashMap<String, Object> getGobalVal(String workflowIntanceId) {
        return workFlowIntanceService.getGobalVal(workflowIntanceId);
    }

    public static WorkFlowXml getDefineModel(PfWorkFlowDefineVo defineVo) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("WorkFlowXmlUtil");
        return factory.getWorkFlowDefineModel(defineVo);
    }

    public static WorkFlowXml getInstanceModel(PfWorkFlowInstanceVo instanceVo) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("WorkFlowXmlUtil");
        return factory.getWorkFlowInstanceModel(instanceVo);
    }


    public static void updateGobalVals(String workflowIntanceId, String valName,
                                       String val) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("WorkFlowXmlUtil");
        factory.updateGobalVal(workflowIntanceId, valName, val);
    }

    public static HashMap<String, Object> getGobalVals(String workflowIntanceId) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container.getBean("WorkFlowXmlUtil");
        return factory.getGobalVal(workflowIntanceId);
    }

    public static void updateGobalValsByProId(String proId, String valName,
                                              String val) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("WorkFlowXmlUtil");
        factory.updateGobalValByProId(proId, valName, val);
    }

    public static HashMap<String, Object> getGobalValsByProId(String proId) {
        WorkFlowXmlUtil factory = (WorkFlowXmlUtil) Container
                .getBean("WorkFlowXmlUtil");
        return factory.getGobalValByProId(proId);
    }
}
