package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TableMapper {

    List<Map<String, Object>> queryTableZdmc(@Param("tableName") String tableName, @Param("owner") String owner);
}
