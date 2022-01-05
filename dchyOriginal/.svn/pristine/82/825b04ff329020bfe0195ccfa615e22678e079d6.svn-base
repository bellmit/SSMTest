package cn.gtmap.msurveyplat.serviceol.core.mapper;

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
    List<Map<String,Object>> queryChjgsByJgmcAndBlsjByPage(Map<String, Object> param);

    /**
     * 根据模版名称，模版类型获取模版集合
     * @param param
     * @return
     */
    List<Map<String,Object>> queryMbXxByMcAndLxByPage(Map<String, Object> param);

    /**
     * 新增模版信息
     * @param param
     */
    void saveMbxx(Map<String, Object> param);

    /**
     * 根据标题，类型获取通知公告信息
     * @param map
     * @return
     */
    List<Map<String,Object>> queryNoticeByTitleAndTypeByPage(Map<String, Object> map);

    /**
     * 获取所有的通知公告信息
     * @return
     */
    List<Map<String,Object>>  queryAllNotices();

    /**
     * 获取待办任务列表(废弃)
     * @return
     */
    List<Map<String,Object>>  queryDblbByPage(Map<String,String> parm);

    /**
     * 获取已办任务列表(废弃)
     * @return
     */
    List<Map<String,Object>>  queryScclListByPage();

    /**
     * 获取上传材料列表(废弃)
     * @return
     */
    List<Map<String,Object>>  queryYblbByPage(Map<String,String> parm);

    /**
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: userid
     * @return java.lang.String
     * @time 2020/12/3 19:43
     * @description 查询申请信息的最新办理意见
     */
    List<Map<String,Object>> getZxBlyjBySqxxid(String sqxxid);

     /**
      * @param map
      * @return
      * @description 2021/3/22 管理单位查看抽查结果
      * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
      */
    List<Map<String,Object>> evaluationCheckResultsByPage(Map<String,Object> map);

}
