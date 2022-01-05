package cn.gtmap.msurveyplat.promanage.core.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 15:17
 * @description 项目管理-现场受理
 */
public interface DchyXmglXcslMapper {
    /**
     * 测量事项字典项
     * @return
     */
    List<Map<String, Object>> getHtxxByChxmid(Map<String, Object> paramMap);


    List<Map<String, Object>> queryBaClsxListByGcbh(Map<String, Object> paramMap);
}
