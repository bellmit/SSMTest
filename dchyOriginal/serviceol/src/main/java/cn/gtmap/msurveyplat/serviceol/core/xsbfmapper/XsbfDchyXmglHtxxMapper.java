package cn.gtmap.msurveyplat.serviceol.core.xsbfmapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘工程
 */
@Repository
public interface XsbfDchyXmglHtxxMapper {

    /**
     * 测绘单位查询我的测绘项目
     * @return
     */
    List<Map<String, Object>> queryHtxxByChxmidAndChdwxxid(Map<String, Object> paramMap);


}
