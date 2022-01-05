package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglTzgg;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglTzggService;
import cn.gtmap.msurveyplat.serviceol.mapper.DchyGldwTzggglMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DchyXmglTzggServiceImpl implements DchyXmglTzggService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    private DchyGldwTzggglMapper dchyGldwTzggglMapper;

    @Override
    public DchyXmglTzgg getDchyXmglTzggByid(String tzggid) {
        return dchyGldwTzggglMapper.getDchyXmglTzggByTzggid(tzggid);
    }
}
