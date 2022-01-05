package cn.gtmap.msurveyplat.exchange.service.onemap.impl;

import cn.gtmap.msurveyplat.exchange.service.onemap.OnemapService;
import cn.gtmap.msurveyplat.exchange.util.Constants;
import cn.gtmap.msurveyplat.exchange.util.PlatformUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 一张图接口实现
 */
@Service
public class OnemapServiceImpl implements OnemapService {
    @Autowired
    private PlatformUtil platformUtil;


    @Override
    public Map<String, Object> getAttachmentTree(String slbh) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Node node = platformUtil.getNodeBySlbh(slbh);
        if(node != null) {
            resultMap.put("id", node.getId());
            resultMap.put("title", node.getName());
            List<Map<String, Object>> childrenMapList = getchildrenMapList(node.getId());
            resultMap.put("children",childrenMapList);
            resultMap.put("url",null);
            resultMap.put("type",Constants.NODE_TYPE_WJJ);
        }
        return resultMap;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param nodeId 节点ID
     * @return
     * @description 递归获取子附件信息
     */
    private List<Map<String, Object>> getchildrenMapList(Integer nodeId) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Node> childrenNodeList = platformUtil.getChildNodeListByParentId(nodeId);
        Map<String, Object> resultMap;
        if (CollectionUtils.isEmpty(childrenNodeList)) {
            return resultList;
        }
        for (Node childrenNode:childrenNodeList) {
            resultMap = Maps.newHashMap();
            resultMap.put("id", childrenNode.getId());
            resultMap.put("title", childrenNode.getName());
            List<Map<String, Object>> childrenMapList = getchildrenMapList(childrenNode.getId());
            resultMap.put("children",childrenMapList);
            if(childrenNode.getType() == Constants.NODE_TYPE_WJ) {
                resultMap.put("url",AppConfig.getProperty("fileCenter.url") + "/file/get.do?fid=" + childrenNode.getId());
                resultMap.put("type",Constants.NODE_TYPE_WJ);
            }else{
                resultMap.put("url",null);
                resultMap.put("type",Constants.NODE_TYPE_WJJ);
            }
            resultList.add(resultMap);
        }
        return resultList;
    }


}
