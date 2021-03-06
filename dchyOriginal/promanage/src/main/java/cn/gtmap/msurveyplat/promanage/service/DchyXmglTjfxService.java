package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/14
 * @description
 */
public interface DchyXmglTjfxService {


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取测绘单位信息列表
     */
    List<DchyXmglMlk> getChdwList();


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各阶段项目数量统计
     */
    List<Map<String, Object>> getXmsl(Map<String, Object> data);


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取项目备案记录分页信息
     */
    Page<Map<String, Object>> getXmbajlByPage(Map<String, Object> data);


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目数量统计
     */
    List<Map<String, Object>> getXmslByqx(Map<String, Object> data);

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目数量统计
     */
    List<Map<String, Object>> getXmslByqxlys(Map<String, Object> data);

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目数量统计
     */
    List<Map<String, Object>> getXmWtfsByQx(Map<String, Object> data);

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目数量统计
     */
    List<Map<String, Object>> getXmWtfsByQxlys(Map<String, Object> data);




    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 发送mq到线上
     */

    Map<String, Object> getXmslFromBdst(Map<String, Object> data);



}
