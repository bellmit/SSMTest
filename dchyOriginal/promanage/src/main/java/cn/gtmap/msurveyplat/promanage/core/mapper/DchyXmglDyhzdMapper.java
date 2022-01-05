package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DchyXmglDyhzdMapper {

    List<Map<String, Object>> getHtxxList(Map<String, String> param);

    List<Map<String, Object>> getQtclxxList(Map<String, String> param);

    String getHtxxListByHtxxidAndChxmid(String htxxid,String chxmid);

    String getSjclxxList(String sjclid,String chxmid);

}
