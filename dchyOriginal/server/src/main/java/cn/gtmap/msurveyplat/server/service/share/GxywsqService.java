package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywsqDO;
import cn.gtmap.msurveyplat.common.dto.DchyCgglGxywsqDto;
import cn.gtmap.msurveyplat.common.dto.GxywsqFycxDto;
import org.springframework.data.domain.Page;

import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务日志信息接口
 */
public interface GxywsqService {

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: dchyCgglGxywsqDto
      * @return java.util.Map
      * @time 2021/6/10 10:20
      * @description 初始化共享业务申请工作流和申请信息
      */
    Map initGxywsq(DchyCgglGxywsqDto dchyCgglGxywsqDto);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: dchyCgglGxywsqDO
      * @return  java.util.Map
     * @time 2021/6/10 10:22
      * @description 获取共享业务申请状态信息
      */
    String getGxywsqShzt(DchyCgglGxywsqDO dchyCgglGxywsqDO);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: dchyCgglGxywsqDO
      * @return java.util.Map
      * @time 2021/6/10 10:22
      * @description 共享业务申请审核
      */
    Map gxywsqSh(DchyCgglGxywsqDO dchyCgglGxywsqDO);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: gxywsqFycxDto
      * @return org.springframework.data.domain.Page<java.util.Map>
      * @time 2021/6/10 10:26
      * @description 共享业务申待办审核
      */
    Page<Map<String,Object>> gxywsqDbSh(GxywsqFycxDto gxywsqFycxDto);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: gxywsqFycxDto
      * @return org.springframework.data.domain.Page<java.util.Map>
      * @time 2021/6/10 10:26
      * @description 共享业务申已办审核
      */
    Page<Map<String,Object>> gxywsqYbSh(GxywsqFycxDto gxywsqFycxDto);

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: gxywsqFycxDto
      * @return org.springframework.data.domain.Page<java.util.Map>
      * @time 2021/6/10 10:26
      * @description 共享业务申已办申请
      */
    Page<Map<String,Object>> gxywsqYbSq(GxywsqFycxDto gxywsqFycxDto);
}
