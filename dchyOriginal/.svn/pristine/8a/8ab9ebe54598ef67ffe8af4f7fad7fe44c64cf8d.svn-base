package cn.gtmap.msurveyplat.server.core.mapper;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSqrDO;
import cn.gtmap.msurveyplat.common.domain.DchyZdChdwDo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/8
 * @description 成果管理系统申请人Mapper接口
 */
@Repository
public interface DchyCgglSqrMapper {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param sqridList 申请人id 集合
     * @return
     * @description 根据申请人ID批量删除成果管理系统申请人
     */
    void deleteBatchDchyCgglSqr(@Param("sqridList") List sqridList);

    /**
      * @author <a href="mailto:dingweiwei@gtmap.cn">dingweiwei</a>
      * @description 根据xmid查询成果管理系统申请人
      */
    List<DchyCgglSqrDO> queryDchyCgglSqrByXmid(@Param("xmid") String xmid);

    List<DchyZdChdwDo> getChdw();
}
