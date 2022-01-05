package cn.gtmap.msurveyplat.server.service.xmxx;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;
import cn.gtmap.msurveyplat.server.service.InitDataService;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/10
 * @description 初始化成果管理系统项目基本信息抽象类
 */
public abstract class InitDchyCgglXmAbstractService implements InitDataService {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param initDataParamDTO 初始化数据参数对象
     * @param initDataResultDTO 初始化数据返回结果对象
     * @return 成果管理系统项目基本信息
     * @description 初始化成果管理系统项目基本项目
     */
    public abstract DchyCgglXmDO initDchyCgglXm(InitDataParamDTO initDataParamDTO, InitDataResultDTO initDataResultDTO);

}
