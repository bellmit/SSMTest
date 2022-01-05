package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/3/17
 * @description 项目管理常州接口
 */

public interface XmglchangzhouService {

    /**
     * @param param 项目状态参数
     * @return 字典项
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */

    Object getAllZzdjs(@RequestBody Map<String, Object> param);


    /**
     * @param param 查询信息
     * @return 项目查看台账
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    ResponseMessage queryResultsManagement(@RequestBody Map<String, Object> param);

    /**
     * @param param chxmid 测绘项目id
     * @return 成果提交记录
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description
     */
    ResponseMessage getProjectConstrctInfo(@RequestBody Map<String, Object> param);
}
