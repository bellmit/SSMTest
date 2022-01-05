package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 管理单位
 */
@Repository
public interface DchyXmglGldwMapper {

    /**
     * 根据测绘单位名称和申请时间获取测绘单位集合
     * @param param
     * @return
     */
    List<Map<String,Object>> queryChdysByDwmcAndSqsjByPage(Map<String, Object> param);

    /**
     * 根据测绘机构名称，办理时间获取测绘机会集合
     * @param param
     * @return
     */
    List<Map<String,Object>> queryChjgsByJgmcAndBlsjByPage(Map<String,Object> param);

    /**
     * 根据模版名称，模版类型获取模版集合
     * @param param
     * @return
     */
    List<Map<String,Object>> queryMbXxByMcAndLxByPage(Map<String,Object> param);

    /**
     * 新增模版信息
     * @param param
     */
    void saveMbxx(Map<String,Object> param);

    /**
     * 根据标题，类型获取通知公告信息
     * @param map
     * @return
     */
    List<Map<String,Object>> queryNoticeByTitleAndTypeByPage(Map<String,Object> map);

    /**
     * 获取所有的通知公告信息
     * @return
     */
    List<Map<String,Object>>  queryAllNotices();
}
