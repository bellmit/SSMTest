package cn.gtmap.msurveyplat.portalol.core.mapper;

import com.gtis.common.Page;
import org.springframework.stereotype.Repository;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘单位
 */
@Repository
public interface DchyXmglMbMapper {

    /**
     * 获取模板启用状态信息列表
     * @return
     */
    Page<Map<String,Object>> queryMbQyztListByPage(Map<String, Object> param);


}
