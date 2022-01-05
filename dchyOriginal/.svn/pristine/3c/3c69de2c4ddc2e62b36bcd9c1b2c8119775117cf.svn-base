package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.serviceol.core.mapper.DchyXmglChxmChdwxxMapper;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglChdwxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-28
 * description
 */

@Service
public class DchyXmglChdwxxServiceImpl implements DchyXmglChdwxxService {
    @Autowired
    DchyXmglChxmChdwxxMapper dchyXmglchdwxxMapperChxm;

    @Override
    public List<DchyXmglChxmChdwxx> getChdwxxList(Map<String, Object> param){
        return dchyXmglchdwxxMapperChxm.getChdwxxList(param);
    }

}
