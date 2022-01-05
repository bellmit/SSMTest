package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmClsx;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/11/28
 * @description 测量事项mapper
 */
@Repository
public interface DchyXmglChxmClsxMapper {

    List<String> queryClsxByChxmid(Map<String,Object> paramMap);

    /**
     * 通过chxmid查询项目设计的测绘阶段
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> queryClsxJdByChxmid(Map<String,Object> paramMap);

    void updateClsxByClsxid(@Param("clsxid") String clsxid);

    List<DchyXmglChxmClsx> getChxmClsxByHtxxGhxmid(Map<String,Object> paramMap);

    List<Map<String,Object>> queryClsxByChqx(Map<String,Object> paramMap);

}
