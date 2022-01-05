package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.portalol.core.mapper.DchyXmglChdwMapper;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglChdwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/30 14:42
 * @description
 */
@Service
public class DchyXmglChdwServiceImpl implements DchyXmglChdwService {

    @Autowired
    private DchyXmglChdwMapper dchyXmglChdwMapper;


    @Override
    public List<Map<String, Object>> queryMlkXxByMlkId(String mlkId) {
        return dchyXmglChdwMapper.queryMlkXxByMlkId(mlkId);
    }

}
