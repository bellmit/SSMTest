package cn.gtmap.msurveyplat.exchange.util;

import cn.gtmap.msurveyplat.exchange.service.qc.impl.QualityCheckServiceImpl;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.NodeService;
import com.gtis.plat.service.SysTaskService;
import com.gtis.plat.vo.PfActivityVo;
import com.gtis.plat.vo.PfTaskVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class PlatformUtil {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private SysTaskService sysTaskService;

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @return
     * @description 根据受理编号创建文件中心节点
     */
    public Integer creatXmNode(final String slbh) {
        if(StringUtils.isNotBlank(slbh)) {
            Space rootSpace = nodeService.getWorkSpace("WORK_FLOW_STUFF",true);
            if(rootSpace == null) {
                return null;
            }
            Node sournode = nodeService.getNode(rootSpace.getId(), slbh, true);
            return sournode.getId();
        }
        return null;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @return
     * @description 根据受理编号获取文件中心节点
     */
    public Node getNodeBySlbh(final String slbh) {
        if(StringUtils.isNotBlank(slbh)) {
            Space rootSpace = nodeService.getWorkSpace("WORK_FLOW_STUFF",true);
            if(rootSpace == null) {
                return null;
            }
            Node sournode = nodeService.getNode(rootSpace.getId(), slbh, true);
            return sournode;
        }
        return null;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param parentId 父节点节点ID
     * @param folderNodeName 文件夹名称
     * @return
     * @description 根据父节点ID和文件夹名称创建子目录
     **/
    public Integer createFileFolderByClmc(Integer parentId, String folderNodeName, String owner) {
        Node tempNode = null;
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                if(StringUtils.isNotBlank(owner)) {
                    tempNode = nodeService.getNode(parentId, folderNodeName, true,owner);
                }else{
                    tempNode = nodeService.getNode(parentId, folderNodeName, true);
                }
            } catch (NodeNotFoundException e) {
                LOGGER.error("PlatformUtil.createFileFolderByClmc",e);
            }
            return tempNode!=null?tempNode.getId():-1;
        } else {
            return -1;
        }
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param parentNode 父节点节点
     * @param folderNodeName 文件夹名称
     * @return
     * @description 根据父节点和文件夹名称创建子目录节点
     **/
    public Integer createChildNodeByClmc(Node parentNode, String folderNodeName) {
        Node tempNode = null;
        if (parentNode!= null&&StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = nodeService.getNode(parentNode.getId(), folderNodeName, true,parentNode.getOwner());
            } catch (NodeNotFoundException e) {
                LOGGER.error("PlatformUtil.createChildNodeByClmc",e);
            }
            return tempNode!=null?tempNode.getId():-1;
        }
        return -1;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @return
     * @description 根据项目ID获取附件存储根目录
     **/
    public Integer creatNode(String slbh) {
        Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
        Node node = nodeService.getNode(space.getId(), slbh, true);
        return node.getId();
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slbh 受理编号
     * @return
     * @description 删除文件节点
     **/
    public void deleteNode(String slbh) {
        Integer nodeId = creatNode(slbh);
        nodeService.remove(nodeId);
    }


    public String getPfActivityNameByTaskId(String taskid) {
        String activityName = "";
        if (StringUtils.isNotBlank(taskid)) {
            PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
            if(pfTaskVo == null) {
                pfTaskVo = sysTaskService.getHistoryTask(taskid);
            }
            PfActivityVo pfActivityVo = null;
            if (pfTaskVo != null&&StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                pfActivityVo = sysTaskService.getActivity(pfTaskVo.getActivityId());
            }
            if(pfActivityVo != null) {
                activityName = pfActivityVo.getActivityName();
            }
        }
        return activityName;
    }

    public PfActivityVo getPfActivityByTaskId(String taskid) {
        if (StringUtils.isNotBlank(taskid)) {
            PfTaskVo pfTaskVo = sysTaskService.getTask(taskid);
            if(pfTaskVo == null) {
                pfTaskVo = sysTaskService.getHistoryTask(taskid);
            }
            PfActivityVo pfActivityVo = null;
            if (pfTaskVo != null&&StringUtils.isNotBlank(pfTaskVo.getActivityId())) {
                return sysTaskService.getActivity(pfTaskVo.getActivityId());
            }
        }
        return null;
    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param parentid 父节点ID
     * @return
     * @description 获取子节点集合
     **/
    public List<Node> getChildNodeListByParentId(Integer parentid) {
        List<Node>  childNodeList = null;
        if(parentid != null) {
            childNodeList = nodeService.getChildNodes(parentid);
        }
        return childNodeList;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param nodeId 节点id
     * @return
     * @description 根据节点id删除文件节点
     * */
    public void deleteNodeById(Integer nodeId) {
        nodeService.remove(nodeId);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slbh 受理编号
     * @param clmc 节点名称
     * @return 子节点
     * @description 根据受理编号和材料名称 获取子节点
     * */
    public List<Node> getChildNodeByClmc(String slbh, String clmc) {
        List<Node> nodes = null;
        Node tempNode;
        Node node = getNodeBySlbh(slbh);
        if (node != null && StringUtils.isNotBlank(clmc)) {
            tempNode = nodeService.getNode(node.getId(), clmc, false);
            if (tempNode !=null) {
                nodes = getChildNodeListByParentId(tempNode.getId());
            }
        }
        return nodes;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param nodeIds    节点列表
     * @param destNodeId 文件位置
     * @param name       文件名
     * @description 压缩一批节点到指定文件
     * */
    public void zip(Integer[] nodeIds, Integer destNodeId, String name) {
        nodeService.zip(nodeIds, destNodeId, name);
    }


}
