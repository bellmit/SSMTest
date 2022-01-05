package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2021/01/18 16:08
 * @description 用户消息
 */
@Repository
public interface DchyXmglYhxxMapper {

    List<Map<String,Object>> queryYhxxByMlkidByPage(Map<String,Object> paramMap);

}
