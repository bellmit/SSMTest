package cn.gtmap.msurveyplat.server.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/9
 * @description 表单验证
 */
@Repository
public interface CheckMapper {

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param tableName 表名
     * @param field 字段
     * @return
     * @description 根据表名和字段查找
     * */
    List<Map<String, Object>> getData(@Param("tableName") String tableName, @Param("field") String field, @Param("xmid") String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param gzldyid
     * @param gzljdid 流程
     * @return
     * @description 获取验证sql
     * */
    List<Map<String, String>> getSql(@Param("gzldyid") String gzldyid, @Param("gzljdid") String gzljdid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param sql
     * @return
     * @description 执行验证sql
     * */
    List<Map<String, Object>> executeSql(@Param("sql")String sql);
}
