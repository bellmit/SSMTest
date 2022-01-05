package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChgc;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/28
 * @description
 */
public interface JsdwFbxqglService {

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位查看发布的需求
     */
    Page<Map<String, Object>> getJsdwCkxqList(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布需求
     */
    boolean saveChgcxx(Map<String, Object> param);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位发布需求,检测工程名称是否存在
     */
    DchyXmglChgc queryGcmcByGcbh(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位查看我的项目
     */
    Page<Map<String, Object>> getJsdwCkxmList(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看详情
     */
    List<Map<String, Object>> queryChxmxxByChxmid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看详情,获取clsx
     */
    List<Map<String, Object>> queryClsxByChxmid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位委托信息
     */
    List<Map<String, Object>> queryWtxxByChdwxxid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位信息办理信息
     */
    List<Map<String, Object>> queryChdwxxByChdwxxid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 委托单位办理信息
     */
    List<Map<String, Object>> queryQtblxxByChxmid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description  发布需求初始化
     */
    Map<String,Object> initJsdwFbxq();

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description  发布需求取消删除
     */
    Map<String,Object> deleteJsdwFbxqByChxmid(Map<String, Object> paramMap);

    /**
     * @param
     * @return
     * @description 2020/12/10 建设单位我的项目  合同下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryHtxxByChxmid(Map<String,Object> paramMap);
}
