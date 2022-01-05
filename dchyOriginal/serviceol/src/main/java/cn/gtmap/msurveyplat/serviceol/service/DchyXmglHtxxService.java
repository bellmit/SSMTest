package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/28
 * @description
 */
public interface DchyXmglHtxxService {

    Page<Map<String, Object>> queryMbxxByPage(Map<String, Object> paramMap);

    ResponseMessage saveMbxx(Map<String, Object> paramMap);

    Map<String,Object> initMbgl();

    Map<String,Object> deleteMbglByMbid(Map<String, Object> paramMap);

    boolean saveMbqyzt(Map<String,Object> paramMap);

}
