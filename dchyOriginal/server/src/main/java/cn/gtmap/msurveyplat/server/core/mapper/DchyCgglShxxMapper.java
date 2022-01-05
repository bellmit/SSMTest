package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DchyCgglShxxMapper {
    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 根据xmid查询成果管理审核信息
      */
    DchyCgglShxxDO queryDchyCgglShxxByXmid(@Param("xmid") String xmid);
}
