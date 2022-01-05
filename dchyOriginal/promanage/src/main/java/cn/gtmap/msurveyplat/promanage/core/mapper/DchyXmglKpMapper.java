package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 考评
 */
@Repository
public interface DchyXmglKpMapper {

    /**
     * 根据考评时间和考评结果获取考评信息
     * @param param
     * @return
     */
    List<Map<String,Object>> queryKpXxByKpSjAndKpJgByPage(Map<String, Object> param);
}
