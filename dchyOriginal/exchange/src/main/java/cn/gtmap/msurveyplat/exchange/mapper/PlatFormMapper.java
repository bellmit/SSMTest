package cn.gtmap.msurveyplat.exchange.mapper;


import cn.gtmap.msurveyplat.common.dto.TaskExtendDto;
import cn.gtmap.msurveyplat.common.dto.ProcessInsWithProjectDto;
import com.gtis.plat.vo.PfRoleVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/31
 * @description 平台
 */
@Repository
public interface PlatFormMapper {
    /**
     * @param param 查询参数
     * @return 获取待办任务
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    List<TaskExtendDto> getTaskListByPage(Map<String, String> param);

    /**
     * @param param 查询参数
     * @return 获取已办任务
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取已办任务
     */
    List<TaskExtendDto> getTaskOverListByPage(Map<String, String> param);

    /**
     * @param param 查询参数
     * @return 获取项目列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取项目列表
     */
    List<ProcessInsWithProjectDto> getProjectListByPage(Map<String, String> param);


    /**
     * @param param 参数对象
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取角色列表信息
     */
    List<PfRoleVo> getRoleList(Map param);

    /**
     * @param map
     * @return
     * @description 2021/3/30 通过受理编号获取流程信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<Map> getProcessInfoBySlbh(Map map);

    /**
     * @param map
     * @return
     * @description 2021/1/14 用户管理台账分页 当roleid为空时,为超级管理员,不展示任何内容
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map queryUserByUsernameByPage(Map map);

    /**
     * @param map
     * @return
     * @description 2021/5/31 通过工作流节点id获取当前节点所有的用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryActivityidByGzljdid(Map<String, String> map);
}
