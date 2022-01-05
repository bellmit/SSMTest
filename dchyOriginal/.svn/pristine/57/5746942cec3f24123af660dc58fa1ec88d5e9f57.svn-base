package cn.gtmap.msurveyplat.serviceol.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/27 16:08
 * @description 测绘项目
 */
@Repository
public interface DchyXmglChxmMapper {

    /**
     * 查询需求编号
     *
     * @return
     */
    String queryMaxXqfbbh();

    /**
     * 查询受理编号
     *
     * @return
     */
    String queryMaxSlbh();

    /**
     * 查询线上委托信息
     * @return
     */
    List<Map<String,Object>> queryEntrustByPage(Map<String, Object> param);

    /**
     * 查询线上委托信息详情
     * @return
     */
    List<Map<String,Object>> queryEntrustByChxmid(Map<String, Object> param);

    String queryUsername(String userid);

}
