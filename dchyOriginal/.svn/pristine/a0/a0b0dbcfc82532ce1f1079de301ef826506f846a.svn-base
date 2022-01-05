package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglShxxDO;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglShxxService;
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
public class DchyCgglShxxServiceImpl implements DchyCgglShxxService {
    @Autowired
    private EntityMapper entityMapper;


    @Override
    public void saveDchyCgglShxx(DchyCgglShxxDO dchyCgglShxxDO) {
        if(StringUtils.isNotBlank(dchyCgglShxxDO.getShxxid())) {
            entityMapper.saveOrUpdate(dchyCgglShxxDO,dchyCgglShxxDO.getShxxid());
        }
    }


    @Override
    public List<DchyCgglShxxDO> getDchyCgglShxxListByXmid(String xmid) {
        List<DchyCgglShxxDO> dchyCgglShxxDOList = null;
        if(StringUtils.isNotBlank(xmid)){
            Example example = new Example(DchyCgglShxxDO.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            dchyCgglShxxDOList = entityMapper.selectByExample(example);
        }
        return dchyCgglShxxDOList;
    }

    @Override
    public DchyCgglShxxDO getDchyCgglShxxDOByShxxid(String shxxid) {
        DchyCgglShxxDO dchyCgglShxxDO = null;
        if(StringUtils.isNotBlank(shxxid)) {
            dchyCgglShxxDO = entityMapper.selectByPrimaryKey(DchyCgglShxxDO.class,shxxid);
        }
        return dchyCgglShxxDO;
    }



    @Override
    public void deleteDchyCgglShxxByXmid(String xmid) {
        if(StringUtils.isNotBlank(xmid)){
            Example example = new Example(DchyCgglShxxDO.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            entityMapper.deleteByExample(example);
        }
    }

    @Override
    public void deleteDchyCgglShxxByShxxid(String shxxid) {
        if(StringUtils.isNotBlank(shxxid)){
            entityMapper.deleteByPrimaryKey(DchyCgglShxxDO.class,shxxid);
        }
    }
}
