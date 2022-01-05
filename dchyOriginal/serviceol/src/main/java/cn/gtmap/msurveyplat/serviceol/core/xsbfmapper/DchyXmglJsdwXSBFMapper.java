package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglJsdwXSBFMapper {

    /**
     * 根据项目名称，承揽测绘机构，状态,受理编码获取测绘项目评价
     * @param param
     * @return
     */
    List<Map<String,Object>> queryXmPjStatusByPage(Map<String, Object> param);


    /**
     * 根据chdwid获取评价信息详情
     * @param
     * @return
     */
    List<Map<String,Object>> queryXmPjInfoByChdwId(@Param("chxmid")String chxmid,@Param("chdwxxid") String chdwxxid);

    /**
     * 根据评价时间，评价结果获取测绘单位评论信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryJsdwPlXxByPage(Map<String,Object> param);

    /**
     * 获取建设单位对应的评价信息(管理单位查看)
     * @param mlkid
     * @return
     */
    List<Map<String,Object>> queryJsdwPlInfoByPage(@Param("mlkid") String mlkid);
}
