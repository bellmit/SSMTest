package cn.gtmap.msurveyplat.portal.entity;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/17
 * @description
 */
public class ForWardVO {
    /**
     * 转发活动
     */
    private List<ForWardTaskVO> forWardTaskVOList;
    /**
     * 节点类型
     * xclusiveGateway ： 独占网关，返回多个人工列表，但只能选择一个走向
     * ParallelGateway：并行网关，返回多个人工列表，必须全部选择
     *
     */
    private String nodeType;

    public List<ForWardTaskVO> getForWardTaskVOList() {
        return forWardTaskVOList;
    }

    public void setForWardTaskVOList(List<ForWardTaskVO> forWardTaskVOList) {
        this.forWardTaskVOList = forWardTaskVOList;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return "ForWardVO{" +
                "forWardTaskVOList=" + forWardTaskVOList +
                ", nodeType='" + nodeType + '\'' +
                '}';
    }
}
