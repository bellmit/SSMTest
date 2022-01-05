package cn.gtmap.msurveyplat.server.service.ywxx;

import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.InitDataResultDTO;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 初始化数据处理主模块
 */
public interface InitDataDealService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param sfrk 是否入库
     * @param initDataParamDTO 初始化参数对象
     * @return 初始化结果
     * @description 初始化主逻辑
     */
    InitDataResultDTO init(InitDataParamDTO initDataParamDTO, boolean sfrk) throws Exception;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param initDataResultDTO 初始化返回结果对象
     * @return
     * @description 处理结果数据
     */
    Boolean dealResultDTO(InitDataResultDTO initDataResultDTO) throws Exception;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param xmid 项目id
     * @return
     * @description 删除业务数据
     * */
    void delYwxx(String xmid) throws Exception;

}
