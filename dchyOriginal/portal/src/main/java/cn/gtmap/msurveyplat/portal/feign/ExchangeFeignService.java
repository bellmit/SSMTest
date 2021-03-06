package cn.gtmap.msurveyplat.portal.feign;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import cn.gtmap.msurveyplat.portal.entity.BdcModuleDTO;
import com.gtis.plat.vo.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
public interface ExchangeFeignService {
    /**
     * @param useId
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取平台资源
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getSysMenu/{useId}/{systemId}")
    List<BdcModuleDTO> getSysMenu(@Param("useId") String useId, @Param("systemId") String systemId);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getSysMenu/{useId}")
    List<BdcModuleDTO> getSysMenu(@Param("useId") String useId);

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取资源
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getResource/{resourceId}")
    PfResourceVo getResource(@Param("resourceId") String resourceId);

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取流程实例
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getWorkFlowInstanceVo/{wiid}")
    PfWorkFlowInstanceVo getWorkFlowInstanceVo(@Param("wiid") String wiid);

    /**
     * @param roles 角色id
     * @param wdid  wdid
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取项目菜单
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getProjectMenu/{roles}/{wdid}")
    List<PfResourceVo> getProjectMenu(@Param("roles") String roles, @Param("wdid") String wdid);

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/tasklist")
    Map<String, Object> getTaskList(Map<String, Object> map);

    /**
     * @param map 参数
     * @return 已办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取已办任务
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/taskoverlist")
    Map<String, Object> getTaskOverList(Map<String, Object> map);

    /**
     * @param map 参数
     * @return 项目列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 项目列表
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/projectlist")
    Map<String, Object> getProjectList(Map<String, Object> map);

    /**
     * @param actStProRelDo 流程扩展表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 插入或更新
     */
    @Headers({"Content-Type: application/json", "token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/saveorupdatetaskextenddto")
    void saveOrUpdateTaskExtendDto(@RequestBody ActStProRelDo actStProRelDo);

    /**
     * @param wiid 流程是咧id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据流程实例id删除流程扩展表
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/deleteTaskExtendDto/{wiid}")
    List<PfResourceVo> deleteTaskExtendDto(@Param("wiid") String wiid);

    /**
     * @return java.util.List<com.gtis.plat.vo.PfResourceVo>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: useId
     * @param: resourceId
     * @time 2021/2/20 16:58
     * @description 根据用户id和资源id，获取资源分区授权数据
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/getResourceAuthorty/{useId}/{resourceId}")
    Map<String, String> getResourceAuthorty(@Param("useId") String useId, @Param("resourceId") String resourceId);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/role/list/user/{useId}")
    List<PfRoleVo> getRoleListByUserid(@Param("useId") String useId);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/organ/{shr}")
    List<PfOrganVo> getOrganListByUser(@Param("shr") String shr);

    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("GET /rest/v1.0/platform/user/{loginName}")
    PfUserVo user(@Param("loginName") String loginName);

    /**
     * @param gzljdid
     * @param userid
     * @return
     * @description 2021/5/28 通过工作流节点id删除出当前用户之外的节点
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Headers({"token: " + TokenCheckUtil.TOKEN})
    @RequestLine("POST /rest/v1.0/platform/deleteOtherAssignment/{taskid}/{userid}")
    ResponseMessage deleteOtherAssignment(@Param("taskid") String taskid, @Param("userid") String userid);

}
