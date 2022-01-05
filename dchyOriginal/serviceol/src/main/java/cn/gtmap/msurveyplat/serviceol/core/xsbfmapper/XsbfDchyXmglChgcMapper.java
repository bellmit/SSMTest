package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘工程
 */
@Repository
public interface XsbfDchyXmglChgcMapper {

    /**
     * 测绘单位查询我的测绘项目
     * @return
     */
    List<Map<String, Object>> queryChdwCkxmByPage(Map<String, Object> paramMap);

    /**
     * 测绘单位查询正在进行中我的测绘项目
     * @return
     */
    String queryChxmCount(Map<String, Object> paramMap);

    /**
     * 建设单位查询我的项目
     * @return
     */
    List<Map<String, Object>> queryJsdwCkxmByPage(Map<String, Object> paramMap);

    /**
     * 根据测绘项目ID查看详情
     * @return
     */
    List<Map<String, Object>> queryChxmxxByChxmid(Map<String, Object> paramMap);

    /**
     * 我的测绘项目以及项目查询，建设单位委托信息
     * @return
     */
    List<Map<String, Object>> queryWtxxByChdwxxids(Map<String, Object> paramMap);



    /**
     * 我的测绘项目以及项目查询，测绘单位办理信息
     * @return
     */
    List<Map<String, Object>> queryChdwxxByChdwxxids(Map<String, Object> paramMap);

    /**
     * 测绘单位办理信息，从业人数
     * @return
     */
    String queryCyrsByChdwxxids(Map<String, Object> paramMap);

    /**
     * 上传材料信息
     * @return
     */
    List<Map<String, Object>> queryScclByChxmid(Map<String, Object> paramMap);

    /**
     * 委托单位办理信息
     * @return
     */
    List<Map<String, Object>> queryQtblxxByChxmid(Map<String, Object> paramMap);

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
     * 测绘体量测量事项字关系
     * @return
     */
    List<Map<String, Object>> queryClsxChtlByChxmid(Map<String, Object> paramMap);

    /**
     * 测绘超期
     */
    List<Map<String, Object>> queryClsxByChqx(Map<String, Object> paramMap);


}
