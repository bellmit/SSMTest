package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxm;
import cn.gtmap.msurveyplat.promanage.service.CgtjCxService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CgtjCxServiceImpl implements CgtjCxService {

    @Autowired
    private EntityMapper entityMapper;

    /**
     * @param param
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: chxmid
     * @time 2021/6/2 11:25
     * @description 成果统一交付确认
     */
    @Override
    public Map cgtyjfqr(Map param) {
        Map resultMap = Maps.newHashMap();
        boolean cgtyjf = false;
        boolean chxmcz = false;
        String chxmid = MapUtils.getString(param,"chxmid");
        if (StringUtils.isNotBlank(chxmid)) {
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            if (null != dchyXmglChxm) {
                chxmcz = true;
                if (StringUtils.equals(Constants.VALID, dchyXmglChxm.getTyjfzt())) {
                    cgtyjf = true;
                }
            }
        }
        resultMap.put("cgtyjf",cgtyjf);
        resultMap.put("chxmcz",chxmcz);
        return resultMap;
    }
}