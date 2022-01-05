package cn.gtmap.msurveyplat.promanage.core.mapper;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglChxmChdwxxMapper {

    List<String> queryChdwxxByChxmid(Map<String,Object> paramMap);

    List<DchyXmglChxmChdwxx> queryChdwxxByClcgxx(Map<String,Object> paramMap);

    List<DchyXmglChxmChdwxx> getChdwxxList(Map<String, Object> param);
}
