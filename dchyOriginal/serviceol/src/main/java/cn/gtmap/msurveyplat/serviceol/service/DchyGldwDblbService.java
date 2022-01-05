package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyGldwDblbService {

    Page<Map<String, Object>> getDblbByDwmcAndSqsj(Map<String, Object> map);

    List<Map<String,Object>> queryMlkxxByMlkid(String mlkid);

    ResponseMessage isEnterPage(Map<String, Object> map);

    Map<String, Object> moveDblbxxByzrryid(String zrryid,String sqxxid, String glsxid);

    Map<String, Object> insertDbrw(String zrryid,String sqxxid,String sqzt);
}

