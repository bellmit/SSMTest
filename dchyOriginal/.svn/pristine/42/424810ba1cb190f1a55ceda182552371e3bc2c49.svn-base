package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘单位
 */
@Repository
public interface DchyXmglChdwXSBFMapper {

    /**
     * 非名录库机构查看
     * @param
     * @return
     */
    List<Map<String, Object>> queryUnmlkByPage(Map<String,Object> paramMap);

    /**
     * 根据chdwxxid获取建设单位评价记录
     * @param chdwxxid
     * @return
     */
    List<Map<String,Object>> queryChdwPjInfoByPage(@Param("chdwxxid") String chdwxxid);

}
