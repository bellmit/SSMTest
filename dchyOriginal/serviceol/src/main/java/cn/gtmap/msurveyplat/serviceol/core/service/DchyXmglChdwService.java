package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 测绘单位
 */
public interface DchyXmglChdwService {


    /**
     * 根据cyryid获取对应从业人员信息
     * @param cyryId
     * @return
     */
    DchyXmglCyry qeuryCyryByCyryId(String cyryId);

    /**
     * 根据考评时间和考评结果获取对应信息
     * @param data
     * @return
     */
    Page<Map<String,Object>> queryJsdwPlXx(Map<String,Object> data);

    List<Map<String,Object>> queryFwpjZd();

    /**
     * 测绘单位查看对应考评记录
     */
    Page<Map<String,Object>> queryChdwKpInfo(Map<String,Object> data);

    /**
     * 获取信用等级
     * @param
     * @return
     */
    Map<String,Object> getCreditRate();

    /**
     * 非名录库机构查看
     * @param
     * @return
     */
    Page<Map<String, Object>> queryUnmlkByPage(Map<String,Object> paramMap);
}
