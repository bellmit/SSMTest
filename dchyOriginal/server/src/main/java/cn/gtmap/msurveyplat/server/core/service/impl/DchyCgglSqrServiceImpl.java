package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSqrDO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSqrService;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DchyCgglSqrServiceImpl implements DchyCgglSqrService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveDchyCgglSqr(DchyCgglSqrDO dchyCgglSqrDO) {
        if (dchyCgglSqrDO != null) {
            if (StringUtils.isBlank(dchyCgglSqrDO.getSqrid())) {
                dchyCgglSqrDO.setSqrid(UUIDGenerator.generate18());
            }
            entityMapper.saveOrUpdate(dchyCgglSqrDO, dchyCgglSqrDO.getSqrid());
        }
    }

    @Override
    public List<DchyCgglSqrDO> getDchyCgglSqrDOListByXmid(String xmid) {
        List<DchyCgglSqrDO> dchyCgglSqrDOList = null;
        if(StringUtils.isNotBlank(xmid)) {
            Example example = new Example(DchyCgglSqrDO.class);
            example.createCriteria().andEqualTo("xmid", xmid);
            dchyCgglSqrDOList = entityMapper.selectByExampleNotNull(example);
        }
        return dchyCgglSqrDOList;
    }
}
