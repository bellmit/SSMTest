package cn.gtmap.msurveyplat.server.service.check;

import cn.gtmap.msurveyplat.common.domain.DchyXtBdbtzdDO;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/9
 * @description 表单验证
 */
public interface CheckService {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param gzldyid 工作流定义id
     * @param gzljdid 工作流节点id
     * @param xmid 项目id
     * @return 表单必填项验证
     * @description 表单必填项验证
     * */
    String checkBdbtx(String gzldyid, String gzljdid, String xmid);

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param gzldyid 工作流定义id
     * @param gzljdid 工作流节点id
     * @return 获取表单必填字段
     * @description 获取表单必填字段
     * */
    Map<String,List<DchyXtBdbtzdDO>> getBtxZd(String gzldyid, String gzljdid);
}
