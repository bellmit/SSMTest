package cn.gtmap.msurveyplat.portal.service.impl;

import cn.gtmap.msurveyplat.common.domain.ActStProRelDo;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portal.entity.BdcModuleDTO;
import cn.gtmap.msurveyplat.portal.feign.ExchangeFeignService;
import com.gtis.config.AppConfig;
import com.gtis.plat.vo.*;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description
 */
@Service
public class ExchangeFeignServiceImpl {

    private ExchangeFeignService init() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(2000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(ExchangeFeignService.class, AppConfig.getProperty("exchange.url"));
    }

    /**
     * @param useId
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取平台资源
     */
    public List<BdcModuleDTO> getSysMenu(String useId, String systemId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getSysMenu(useId, systemId);
    }

    public List<BdcModuleDTO> getSysMenu(String useId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getSysMenu(useId);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取资源
     */
    public PfResourceVo getResource(String resourceId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getResource(resourceId);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取流程实例
     */
    public PfWorkFlowInstanceVo getWorkFlowInstanceVo(String wiid) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getWorkFlowInstanceVo(wiid);
    }


    /**
     * @param roles 角色id
     * @param wdid  wdid
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取项目菜单
     */
    public List<PfResourceVo> getProjectMenu(String roles, String wdid) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getProjectMenu(roles, wdid);
    }

    /**
     * @param map 参数
     * @return 待办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取待办任务
     */
    public Map<String, Object> getTaskList(Map<String, Object> map) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getTaskList(map);
    }

    /**
     * @param map 参数
     * @return 已办列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取已办任务
     */
    public Map<String, Object> getTaskOverList(Map<String, Object> map) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getTaskOverList(map);
    }

    /**
     * @param map 参数
     * @return 项目列表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 项目列表
     */
    public Map<String, Object> getProjectList(Map<String, Object> map) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getProjectList(map);
    }

    /**
     * @param actStProRelDo 流程扩展表
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 插入或更新
     */
    public void saveOrUpdateTaskExtendDto(ActStProRelDo actStProRelDo) {
        ExchangeFeignService orderFsmService = init();
        orderFsmService.saveOrUpdateTaskExtendDto(actStProRelDo);
    }

    /**
     * @param wiid 流程是咧id
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 根据流程实例id删除流程扩展表
     */
    public void deleteTaskExtendDto(String wiid) {
        ExchangeFeignService orderFsmService = init();
        orderFsmService.deleteTaskExtendDto(wiid);
    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userId
     * @param: resourceId
     * @time 2021/2/20 17:00
     * @description
     */
    public Map<String, String> getResourceAuthorty(String userId, String resourceId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getResourceAuthorty(userId, resourceId);
    }

    public List<PfRoleVo> getRoleListByUserid(String userId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getRoleListByUserid(userId);
    }

    public List<PfOrganVo> getOrganListByUser(String userId) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.getOrganListByUser(userId);
    }

    public PfUserVo user(String loginName) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.user(loginName);
    }

    /**
     * @param taskid
     * @param userid
     * @return
     * @description 2021/5/28 通过工作流节点id删除出当前用户之外的节点
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public ResponseMessage deleteOtherAssignment(String taskid, String userid) {
        ExchangeFeignService orderFsmService = init();
        return orderFsmService.deleteOtherAssignment(taskid, userid);
    }

}
