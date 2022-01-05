package cn.gtmap.msurveyplat.portal.entity;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/17
 * @description 用户表信息接口
 */
public class ForWardTaskVO {
    /**
     * 转发活动
     */
    private ForwardTaskDto forwardTaskDto;
    /**
     * 转发角色
     */
    private List<RoleDto> roleDtoList;

    public ForwardTaskDto getForwardTaskDto() {
        return forwardTaskDto;
    }

    public void setForwardTaskDto(ForwardTaskDto forwardTaskDto) {
        this.forwardTaskDto = forwardTaskDto;
    }

    public List<RoleDto> getRoleDtoList() {
        return roleDtoList;
    }

    public void setRoleDtoList(List<RoleDto> roleDtoList) {
        this.roleDtoList = roleDtoList;
    }

    @Override
    public String toString() {
        return "ForWardTaskVO{" +
                "forwardTaskDto=" + forwardTaskDto +
                ", roleDtoList=" + roleDtoList +
                '}';
    }
}
