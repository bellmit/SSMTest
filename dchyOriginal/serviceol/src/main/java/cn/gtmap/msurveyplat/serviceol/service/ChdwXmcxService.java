package cn.gtmap.msurveyplat.serviceol.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/28
 * @description
 */
public interface ChdwXmcxService {

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位查看项目
     */
    Page<Map<String, Object>> getChdwXmcxList(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位正在进行中我的测绘项目
     */
    String queryChxmCount();

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 点击查看，根据测绘工程ID查看详情
     */
    List<Map<String, Object>> getXmcxxqList(Map<String, Object> param);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 我的测绘项目
     */
    Page<Map<String, Object>> getChdwCkxmList(Map<String, Object> param);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 建设单位委托信息
     */
    List<Map<String, Object>> queryWtxxByChdwxxid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 测绘单位办理信息
     */
    List<Map<String, Object>> queryChdwxxByChdwxxid(Map<String, Object> paramMap);

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 委托单位办理信息
     */
    List<Map<String, Object>> queryQtblxxByChxmid(Map<String, Object> paramMap);

    /**
     * @param chxmid
     * @param mlkid
     * @return
     * @description 2020/12/10 测绘单位 合同下载
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> queryHtxxByChxmidAndHtxxid(String chxmid, String mlkid);

    /**
     * @param wtxmxxList 项目委托信息
     * @return
     * @description 2021/3/19  给测绘单位和建设单位加上统一社会用代码和联系人联系电话
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void formatWTXMXX(List<Map<String, Object>> wtxmxxList);

}
