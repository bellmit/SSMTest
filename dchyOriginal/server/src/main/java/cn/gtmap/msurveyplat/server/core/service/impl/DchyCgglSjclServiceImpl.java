package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglSjclMapper;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjclService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/16
 * @description 成果管理收件材料信息接口
 */
@Service
public class DchyCgglSjclServiceImpl implements DchyCgglSjclService {
    @Autowired
    private DchyCgglSjclMapper dchyCgglSjclMapper;

    @Autowired
    private EntityMapper entityMapper;


    @Override
    public List<DchyCgglSjclDO> getDchyCgglSjclDOListByXmid(String xmid) {
        if(StringUtils.isNotBlank(xmid)) {
            return dchyCgglSjclMapper.getDchyCgglSjclDOListByXmid(xmid);
        }
        return null;
    }

    @Override
    public List<DchyCgglSjclDO> getDefaultDchyCgglSjclDOListByXmid(String xmid) {
        if(StringUtils.isNotBlank(xmid)) {
            return dchyCgglSjclMapper.getDefaultDchyCgglSjclDOListByXmid(xmid);
        }
        return null;
    }

    @Override
    public List<DchyCgglSjclDO> getDchyCgglSjclDOListByGzldyid(String gzldyid) {
        if (StringUtils.isNotBlank(gzldyid)) {
            return dchyCgglSjclMapper.getDchyCgglSjclDOListByGzldyid(gzldyid);
        }
        return null;
    }

    @Override
    public void saveOrUpdateDchyCgglSjcl(DchyCgglSjclDO dchyCgglSjclDO) {
        if (dchyCgglSjclDO != null) {
            entityMapper.saveOrUpdate(dchyCgglSjclDO, dchyCgglSjclDO.getSjclid());
        }
    }

    @Override
    public void deleteSjcl(List<String> slclidList) {
        dchyCgglSjclMapper.deleteSjcl(slclidList);
    }

    @Override
    public DchyCgglSjclDO getDchyCgglSjclDOById(String sjclid) {
        return entityMapper.selectByPrimaryKey(DchyCgglSjclDO.class, sjclid);
    }
}
