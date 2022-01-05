package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 17:43
 * @description 建设单位
 */
@Repository
public interface DchyXmglJsdwMapper {

    /**
     * 根据项目名称和状态获取建设单位项目信息
     * @return
     */
    List<Map<String,Object>> queryJsxmByXmmcOrZtByPage(Map<String, Object> param);

    /**
     * 根据合同编号，合同状态，测绘单位名称获取合同信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryHtxxByBhAndChdwmcByPage(Map<String,Object> param);

    /**
     * 根据项目名称，承揽测绘机构，状态获取测绘项目评价
     * @param param
     * @return
     */
    List<Map<String,Object>> queryXmpjByXmmcAndClchjgByPage(Map<String,Object> param);

    /**
     * 根据项目编号，项目名称，状态获取建设项目信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryJsxmByHbAndMc(Map<String,Object> param);

    /**
     * 查询建设单位台账
     * @return
     */
    List<Map<String, Object>> queryJsdwmByPage(Map<String, Object> paramMap);

    /**
     * 查询建设单位列表
     * @return
     */
    List<Map<String, Object>> queryJsdwList(Map<String, Object> paramMap);


}
