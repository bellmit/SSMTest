package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.DchyZdSjlxDO;
import cn.gtmap.msurveyplat.server.core.mapper.DchyZdMapper;
import cn.gtmap.msurveyplat.server.core.service.DchyZdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/10
 * @description 多测合一字典信息接口
 */
@Service
public class DchyZdServiceImpl implements DchyZdService {
    @Autowired
    private DchyZdMapper dchyZdMapper;

    @Override
    public List<DchyZdSjlxDO> getDchyZdSjlxDOList() {
        return dchyZdMapper.getDchyZdSjlxDOList();
    }
}
