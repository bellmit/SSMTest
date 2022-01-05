package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.AnalysisLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisLogDao extends JpaRepository<AnalysisLog,String> , JpaSpecificationExecutor<AnalysisLog> {


    @Query(value = "select distinct to_char(t.time, 'yyyy') years from OMP_ANALYSIS_LOG t where t.time is not null order by years desc ", nativeQuery = true)
    List<Object> findLogYear();

}
