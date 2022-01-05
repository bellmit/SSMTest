package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘工程
 */
@Repository
public interface DchyXmglChgcMapper {

    /**
     * 根据项目名称和状态获取测绘工程信息
     * @return
     */
    List<Map<String, Object>> queryChgcxxByXmmcOrZtByPage(Map<String, Object> paramMap);

    /**
     * 根据测绘工程ID查看详情
     * @return
     */
    List<Map<String, Object>> queryChxmxxByChxmid(Map<String, Object> paramMap);

    /**
     * 测量事项字典项
     * @return
     */
    List<Map<String, Object>> getClsxByChxmid(Map<String, Object> paramMap);

    /**
     * 测绘单位测量事项字关系
     * @return
     */
    List<Map<String, Object>> getClsxByChdwxxid(Map<String, Object> paramMap);

    /**
     * 测绘项目测绘单位关系
     * @return
     */
    List<Map<String, Object>> getChxmChdwxxByChxmid(Map<String, Object> paramMap);


    /**
     * 测绘项目测绘体量关系
     * @return
     */
    List<Map<String, Object>> queryClsxChtlByChxmid(Map<String, Object> paramMap);





}
