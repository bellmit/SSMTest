package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.promanage.core.mapper.ChdwxxMapper;
import cn.gtmap.msurveyplat.promanage.core.service.ChdwxxService;
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
public class ChdwxxServiceImpl implements ChdwxxService {
    @Autowired
    ChdwxxMapper chdwxxMapper;

    @Override
    public List<DchyXmglChxmChdwxx> getChdwxxList(Map<String, Object> param){
        return chdwxxMapper.getChdwxxList(param);
    }

}
