package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglZd;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglZdMapper {

    List<Map<String, Object>> getDchyZdBlsxList();

    List<DchyXmglZd> getAllDchyXmglZdList();

    List<Map<String,Object>> getZdClsx(Map<String,Object> paramMap);
}
