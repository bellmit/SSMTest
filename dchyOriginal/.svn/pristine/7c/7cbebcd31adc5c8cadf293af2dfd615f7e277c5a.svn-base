package cn.gtmap.msurveyplat.portal.utils;

import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.wf.model.ActivityModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * 操作工作流实例XML
 * @author jiff
 */
public class WorkFlowXml implements Serializable {

	private static final long serialVersionUID = -5905728708914724839L;
	private static final Log log = LogFactory.getLog(WorkFlowXml.class);

    /** 工作流定义xml */
	private String instanceXml;
    /** 工作流定义xml Dom4j对象 */
	private Document doc;
    /** 修改时间 */
	private Date modifyDate;
	
	/**
	 * 获取工作流的第一个活动定义
	 * @return
	 */
	public String getBeginActivityDefine() {
		if (doc != null) {
			Node tmpNode = doc
					.selectSingleNode("//WorkflowProcess/ExtendedAttributes/ExtendedAttribute[@Name='PROCESS_BEGIN_ACTIVITYID']");
			if (tmpNode != null) {

				return tmpNode.valueOf("@Value");
			}
		}
		return null;
	}

	public String getEndActivityDefine() {
		if (doc != null) {
			Node tmpNode = doc
					.selectSingleNode("//WorkflowProcess/ExtendedAttributes/ExtendedAttribute[@Name='PROCESS_END_ACTIVITYID']");
			if (tmpNode != null) {
				return tmpNode.valueOf("@Value");

			}
		}
		return null;
	}

	public ActivityModel getActivity(String activityId) {
		if (doc != null) {
			Node tmpNode = doc.selectSingleNode("//Activities/Activity[@Id='"
					+ activityId + "']");
			if (tmpNode != null) {
				return new ActivityModel(tmpNode);

			}
		}
		return null;
	}

    /**
     * 获得活动模型列表
     * @return
     */
    public List<ActivityModel> getActivityList(){
        List<ActivityModel> activityList = new ArrayList<ActivityModel>();
        if (doc != null) {
            List<Node> activityNodeList = doc.selectNodes("//Activities/Activity");
            if(activityNodeList != null && !activityNodeList.isEmpty()){
                for(Node actNode : activityNodeList){
                    activityList.add(new ActivityModel(actNode));
                }
            }
        }
        return activityList;
    }
    
	/**
	 * 是否存在协办，以及是否强行协办 1代表存在协办，不强行办理 2代表存在协办，且强行办理 -1代表不存在协办
	 * 
	 * @param activityId
	 * @return
	 */
	// public int isHasCooperNode(String activityId) {
	// int reValue = -1;
	// if (doc != null) {
	// Node tmpNode = doc
	// .selectSingleNode("//Activities/Activity[@Id='"
	// + activityId
	// + "']/ExtendedAttributes/ExtendedAttribute[@Name='COOPERATION']");
	// if (tmpNode != null) {
	// if (tmpNode.valueOf("@Value").equals("1"))
	// return 1;
	// else if (tmpNode.valueOf("@Value").equals("2"))
	// return 2;
	// }
	// }
	// return reValue;
	// }
	public String CooperRootId(String activityId) {
		if (doc != null) {
			Node tmpNode = doc
					.selectSingleNode("//Activities/Activity[@Id='"
							+ activityId
							+ "']/ExtendedAttributes/ExtendedAttribute[@Name='COOPERROOTID']");
			if (tmpNode != null) {
				return tmpNode.valueOf("@Value");
			}
		}
		return "";
	}

    public int getActivitiesCount(){
        if (doc != null) {
			List<Node> lstNodes = doc.selectNodes("//Activities/Activity");
			return lstNodes.size();
		}
        return 0;
    }

	public List<String> getToActivitys(String activityId) {
		List<String> lstActivitys = new ArrayList<String>();
		List<Node> lstNodes = doc.selectNodes("//Transition[@To='" + activityId + "']");
		for (Node tmpNode : lstNodes) {
			lstActivitys.add(tmpNode.valueOf("@From"));
		}
		return lstActivitys;
	}

	/*
	 * 获取流程转发情况
	 */
	public List<String[]> getTransitionList() {
		List<String[]> transitionsList = new Vector<String[]>();
		List<Node> lstNodes = doc.selectNodes("//Transitions/Transition");
		for (Node tmpNode : lstNodes) {
			String[] activityArray = new String[3];
			activityArray[0] = tmpNode.valueOf("@Id");
			activityArray[1] = tmpNode.valueOf("@From");
			activityArray[2] = tmpNode.valueOf("@To");
			transitionsList.add(activityArray);
		}
		return transitionsList;
	}

    public WorkFlowXml(String xml) {
		this.setInstanceXml(xml);
	}

	public String getInstanceXml() {
		return instanceXml;
	}

	public void setInstanceXml(String xml) {
		instanceXml = xml;
		if (instanceXml != null && !instanceXml.equals("")) {
			try {
				doc = DocumentHelper.parseText(instanceXml);
			} catch (Exception e) {
				log.error("---解析工作流XML失败---");
				e.printStackTrace();
			}
		}
	}

    public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

    public String getExtendedAttribute(String aName) {
        Node tmpNode = doc
                .selectSingleNode("//WorkflowProcess/ExtendedAttributes/ExtendedAttribute[@Name='"
                        + aName + "']");
        if (tmpNode != null) {
            return tmpNode.valueOf("@Value");
        } else {
            return "";
        }
    }

    public boolean isDisable(){
        if ("false".equals(getExtendedAttribute("disable")) || "".equals(getExtendedAttribute("disable"))){
            return false;
        }
        return true;
    }
	/**
	 * 获得所有节点列表
	 * @return
	 */
	public List<PfActivityVo> getPfActivityVoList(){
		List<PfActivityVo> pfActivityList = new ArrayList<PfActivityVo>();
		List<ActivityModel> activityList = new ArrayList<ActivityModel>();
		if (doc != null) {
			List<Node> activityNodeList = doc.selectNodes("//Activities/Activity");
			if(activityNodeList != null && !activityNodeList.isEmpty()){
				for(Node actNode : activityNodeList){
					activityList.add(new ActivityModel(actNode));
				}
				if(CollectionUtils.isNotEmpty(activityList)){
					for(ActivityModel activityModel:activityList){
						if(activityModel!=null){
							PfActivityVo pfActivityVo=new PfActivityVo();
							pfActivityVo.setActivityId(activityModel.getDefineId());
							pfActivityVo.setActivityName(activityModel.getActivityDefineName());
							pfActivityList.add(pfActivityVo);
						}
					}
				}
			}
		}
		return pfActivityList;
	}
}
