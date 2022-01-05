package cn.gtmap.msurveyplat.promanage.feign;

import cn.gtmap.msurveyplat.common.domain.TaskData;
import feign.Param;
import feign.RequestLine;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description Portal服务
 */
public interface PortalFeignService {

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: wdid
     * @param: userId
      * @return cn.gtmap.msurveyplat.common.domain.TaskData
      * @time 2021/3/13 15:06
      * @description 根据wdid和userId创建项目流程
      */
    @RequestLine("POST /v1.0/dispatch/order/createTask/{wdid}/{userId}")
    TaskData createTask(@Param("wdid") String wdid, @Param("userId") String userId);

    @RequestLine("POST /v1.0/dispatch/order/turnTask/{wiid}/{userId}")
    Object turnTask(@Param("wiid") String wiid, @Param("userId") String userId);
}
