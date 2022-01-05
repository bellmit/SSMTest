package cn.gtmap.msurveyplat.server.core.mapper;


import cn.gtmap.msurveyplat.common.domain.DchyZdSjlxDO;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 多测合一字典表接口
 */
public interface DchyZdMapper {


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取收件材料字典表信息
     */
    List<DchyZdSjlxDO> getDchyZdSjlxDOList();

}
