package cn.gtmap.msurveyplat.exchange.service.qc;

import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/27
 * @description 成果数据质检接口
 */
public interface QualityCheckService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @description 触发开始质检操作
     **/
    Object startQualityCheck(String slbh, String cgsjlx) throws IOException;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param slbh 受理编号
     * @return
     * @description 检查成果包是否存在
     **/
    Map<String, Object> check(String slbh);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param slbh 受理编号
     * @param cgsjlx 成果数据类型
     * @return
     * @description 触发成果数据入库操作
     **/
    Object importDatabase(String slbh, String cgsjlx) throws IOException;
}
