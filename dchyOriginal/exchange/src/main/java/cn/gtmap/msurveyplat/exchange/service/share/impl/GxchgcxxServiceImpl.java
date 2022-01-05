package cn.gtmap.msurveyplat.exchange.service.share.impl;

import cn.gtmap.msurveyplat.exchange.service.share.GxchgcxxService;
import cn.gtmap.msurveyplat.exchange.util.PlatformUtil;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/14
 * @description 共享测绘工程信息服务
 */
@Service
public class GxchgcxxServiceImpl implements GxchgcxxService {
    @Autowired
    private PlatformUtil platformUtil;

    private static final int NODE_TYPE = 0; //目录

    @Override
    public Map getGxchgcclDownUrl(Map paramMap, String babh) {
        Map result = new HashMap();
        List<String> cgclmcList = (List<String>) paramMap.get("cgclmcList");
        List<String> cgmlmcList = (List<String>) paramMap.get("cgmlmcList");
        Integer parentId = platformUtil.creatXmNode(babh);
        String gxywxxMlm = AppConfig.getProperty("dchy.cggl.gxywxx.mlm");
        Integer gxywxxMlmNodeId = platformUtil.createFileFolderByClmc(parentId, gxywxxMlm, "");
        List<Node> childNodeList = platformUtil.getChildNodeListByParentId(parentId);
        String zipFileName = UUIDGenerator.generate18() + ".zip";
        if (CollectionUtils.isNotEmpty(childNodeList)) {
            List<Integer> nodeIdList = new ArrayList<>();
            fillFilterNodeId(childNodeList, cgclmcList, nodeIdList, gxywxxMlmNodeId, cgmlmcList);
            if (nodeIdList.size() == 0) {//筛选后节点列表为空，表示共享业务所需文件未入库，返回空
                return result;
            }
            // 将文件夹压缩成zip包
            platformUtil.zip(nodeIdList.toArray(new Integer[nodeIdList.size()]), gxywxxMlmNodeId, zipFileName);
        }
        List<Node> gxywxxChildNodeList = platformUtil.getChildNodeListByParentId(gxywxxMlmNodeId);
        if (CollectionUtils.isNotEmpty(gxywxxChildNodeList)) {
            for (Node gxywxxNode : gxywxxChildNodeList) {
                if (StringUtils.equals(gxywxxNode.getName(), zipFileName)) {
                    result.put("url", AppConfig.getProperty("fileCenter.url") + "/file/get.do?fid=" + gxywxxNode.getId() + "&token=whosyourdaddy");
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * @param childNodeList 子节点
     * @param cgclmcList    成果材料名称信息列表
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 递归获取过滤的节点ID
     */
    void fillFilterNodeId(List<Node> childNodeList, List<String> cgclmcList, List<Integer> nodeIdList, Integer gxywxxMlmNodeId, List<String> cgmlmcList) {
        if (CollectionUtils.isNotEmpty(childNodeList)) {
            for (Node node : childNodeList) {
                if (node.getType() == NODE_TYPE && !node.getId().equals(gxywxxMlmNodeId) && CollectionUtils.isNotEmpty(cgmlmcList) && cgmlmcList.contains(node.getName())) {
                    List<Node> childNodeListTemp = platformUtil.getChildNodeListByParentId(node.getId());
                    fillFilterNodeId(childNodeListTemp, cgclmcList, nodeIdList, gxywxxMlmNodeId, cgmlmcList);
                } else {
                    if (cgclmcList.contains(node.getName())) {
                        nodeIdList.add(node.getId());
                    }
                }
            }
        }
    }

}
