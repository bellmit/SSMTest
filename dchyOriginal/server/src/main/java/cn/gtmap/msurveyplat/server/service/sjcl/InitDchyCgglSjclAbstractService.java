package cn.gtmap.msurveyplat.server.service.sjcl;

import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.service.InitDataService;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/25
 * @description 初始化成果管理系统收件材料信息
 */
public abstract class InitDchyCgglSjclAbstractService  implements InitDataService {
    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param initDataParamDTO 初始化数据参数对象
     * @param initDataResultDTO 初始化数据返回结果对象
     * @return 成果管理系统项目基本信息
     * @description 初始化成果管理系统项目收件材料信息
     */
    public abstract List<DchyCgglSjclDO> initDchyCgglSjcl(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO);

}
