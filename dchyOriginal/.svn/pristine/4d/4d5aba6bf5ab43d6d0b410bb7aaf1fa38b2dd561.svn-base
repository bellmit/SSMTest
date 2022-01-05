package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘单位
 */
@Repository
public interface DchyXmglChdwMapper {

    /**
     * 根据测绘单位名称，或全局分页查询对应考评信息
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryChdwKpInfoByPage(Map<String, Object> param);


    /**
     * 获取管理单位的考评记录
     *
     * @param
     * @return
     */
    List<Map<String, Object>> queryGldwKpInfoByPage(Map<String, Object> param);

    List<Map<String, Object>> queryGldwKpxxByPage(Map<String, Object> param);

    List<Map<String, Object>> queryFwpjZd();

    //测绘单位办理信息
    List<Map<String, Object>> queryChxxByChdwxxids(Map<String, Object> param);

    //测绘单位办理信息,
    String queryCyrsByChdwxxids(Map<String, Object> param);


}
