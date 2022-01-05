package cn.gtmap.onemap.platform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DBAService {
     <T extends Object>Page<T> searchByPage(String sql, Pageable pageable, Class entityClass);

     List<Map> search(String sql);

     String generateWhereIn(List<String> data);

     String generateWhereIn(List<String> data,String fieldName);

     String toDataYYYYmmDD(Date date);
}
