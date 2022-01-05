package cn.gtmap.msurveyplat.serviceol.utils;

import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Node;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/2 13:47
 * @description 平台库操作实现类
 */
@Service("platformUtil")
public class PlatformUtil {

    public final static String PAGE_ENTER_FROM_TASKLIST = "task";
    public final static String PAGE_ENTER_FROM_PROJECTLIST = "project";


    public static FileService getFileService() {
        return (FileService) Container.getBean("fileService");
    }

    /**
     * lst 工作流附件存储目录
     *
     * @param proid
     * @return
     */
    public int creatNode(String proid) {
        NodeService nodeService = getNodeService();
        Space space = nodeService.getWorkSpace(Constants.WORK_FLOW_STUFF);
        com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), proid, true);
        return node.getId();
    }

    public static NodeService getNodeService() {

        return (NodeService) Container
                .getBean("FileCenterNodeServiceImpl");
    }

    public Integer createFileFolderByclmc(Integer parentId, String folderNodeName) {
        com.gtis.fileCenter.model.Node tempNode = null;
        NodeService nodeService = getNodeService();
        Integer id = null;
        if (StringUtils.isNotBlank(folderNodeName)) {
            try {
                tempNode = nodeService.getNode(Integer.valueOf(parentId), folderNodeName, true);
            } catch (NodeNotFoundException e) {
                LoggerFactory.getLogger(getClass()).error("Unexpected error in function ", e);
            }
            if (tempNode != null) {
                id = tempNode.getId();
            }
            return id;
        } else {
            return -1;
        }
    }

    /**
     * 获取输入文件扩展名
     */
    public static String getFileExtension(MultipartFile thumbnail) {
        String originalFileName = thumbnail.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * @param parentid 父节点ID
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取子节点集合
     **/
    public List<Node> getChildNodeListByParentId(Integer parentid) {
        List<Node> childNodeList = null;
        if (parentid != null) {
            childNodeList = getNodeService().getChildNodes(parentid);
        }
        return childNodeList;
    }

    /**
     * @param nodeId 节点id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据节点id删除文件节点
     */
    public void deleteNodeById(Integer nodeId) {
        getNodeService().remove(nodeId);
    }
}
