package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSjclDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSjxxDO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglSjxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/10
 * @description
 */
@Service
public class DchyCgglSjxxServiceImpl implements DchyCgglSjxxService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveDchyCgglSjxx(DchyCgglSjxxDO dchyCgglSjxxDO) {
        if(StringUtils.isNotBlank(dchyCgglSjxxDO.getSjxxid())) {
            entityMapper.saveOrUpdate(dchyCgglSjxxDO,dchyCgglSjxxDO.getSjxxid());
        }
    }

    @Override
    public void deleteDchyCgglSjxx(String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            Example example = new Example(DchyCgglSjxxDO.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            List<DchyCgglSjxxDO> dchyCgglSjxxDOList = entityMapper.selectByExample(example);
            if (dchyCgglSjxxDOList != null) {
                for (DchyCgglSjxxDO dchyCgglSjxxDO : dchyCgglSjxxDOList) {
                    Example sjclExample = new Example(DchyCgglSjclDO.class);
                    sjclExample.createCriteria().andEqualTo("sjxxid",dchyCgglSjxxDO.getSjxxid());
                    entityMapper.deleteByExample(example);
                    entityMapper.deleteByPrimaryKey(DchyCgglSjxxDO.class , dchyCgglSjxxDO.getSjxxid());
                }
            }
        }
    }

    @Override
    public List<DchyCgglSjxxDO> getDchyCgglSjxxDOListByXmid(String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            Example example = new Example(DchyCgglSjxxDO.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            List<DchyCgglSjxxDO> dchyCgglSjxxDOList = entityMapper.selectByExample(example);
            return dchyCgglSjxxDOList;
        }
        return null;
    }
}
