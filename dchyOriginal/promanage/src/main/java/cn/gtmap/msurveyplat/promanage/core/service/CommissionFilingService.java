package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
public interface CommissionFilingService {

    /**
     * @param
     * @return
     * @description 2021/3/3 审核线上委托
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     */
    boolean reviewCommission(Map<String, Object> map);

    /**
     * @param
     * @return
     * @description 2021/3/3 线上委托待办任务
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     */
    Page<Map<String, Object>> getCommissionTask(Map<String, Object> paramMap);

    /**
     * @param paramMap
     * @return
     * @description 2021/6/1 线下备案数据补推
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage supplementaryPushData(Map<String, Object> paramMap);

}
