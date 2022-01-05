package cn.gtmap.msurveyplat.portalol.core.mapper;

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
public interface DchyXmglChdwMapper {


    /**
     * 根据mlkid获取对应名录库
     * @return
     */
    List<Map<String,Object>> queryMlkXxByMlkId(@Param(value = "mlkId") String mlkId);

}
