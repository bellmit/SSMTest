package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglSqrDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.DchyZdChdwDo;
import cn.gtmap.msurveyplat.server.core.mapper.DchyCgglSqrMapper;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglXmService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;


@Service
public class DchyCgglXmServiceImpl implements DchyCgglXmService {
    private Logger logger = LoggerFactory.getLogger(DchyCgglXmServiceImpl.class);
    @Autowired
    private DchyCgglSqrMapper dchyCgglSqrMapper;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Model initSlxx(Model model, String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            DchyCgglXmDO dchyCgglXmDO = entityMapper.selectByPrimaryKey(DchyCgglXmDO.class,xmid);
            List<DchyCgglSqrDO> dchyCgglSqrDOList = dchyCgglSqrMapper.queryDchyCgglSqrByXmid(xmid);
            if (null == dchyCgglXmDO) {
                dchyCgglXmDO = new DchyCgglXmDO();
            }
            model.addAttribute("dchyCgglXmDO", dchyCgglXmDO);
            model.addAttribute("dchyCgglSqrDOList", dchyCgglSqrDOList);
            model.addAttribute("xmid", xmid);
        }
        return model;
    }

    @Override
    public void saveDchyCgglXm(DchyCgglXmDO dchyCgglXmDO) {
        if (null != dchyCgglXmDO&&StringUtils.isNotBlank(dchyCgglXmDO.getXmid())) {
            entityMapper.saveOrUpdate(dchyCgglXmDO, dchyCgglXmDO.getXmid());
        }
    }

    @Override
    public DchyCgglXmDO getDchyCgglXmDOByXmid(String xmid) {
        DchyCgglXmDO dchyCgglXmDO = null;
        if(StringUtils.isNotBlank(xmid)) {
            dchyCgglXmDO = entityMapper.selectByPrimaryKey(DchyCgglXmDO.class,xmid);
        }
        return dchyCgglXmDO;
    }

    @Override
    public List<DchyZdChdwDo> getChdw() {
        return dchyCgglSqrMapper.getChdw();
    }

    @Override
    public void deleteDchyCgglXmDOByXmid(String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            entityMapper.deleteByPrimaryKey(DchyCgglXmDO.class ,xmid);
        }
    }

    @Override
    public void deleteDchyCgglSqrDOByXmid(String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            List<DchyCgglSqrDO> dchyCgglSqrDOList = dchyCgglSqrMapper.queryDchyCgglSqrByXmid(xmid);
            for (DchyCgglSqrDO dchyCgglSqrDO : dchyCgglSqrDOList) {
                entityMapper.deleteByPrimaryKey(DchyCgglSqrDO.class, dchyCgglSqrDO.getSqrid());
            }
        }
    }

    @Override
    public List<DchyCgglXmDO> getDchyCgglXmDOList(String chxmbh, String rkzt, String xmzt) {
        List<DchyCgglXmDO> dchyCgglXmDOList = new ArrayList<>();
        if(StringUtils.isNotBlank(chxmbh)) {
            Example example = new Example(DchyCgglXmDO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("chxmbh",chxmbh);
            if(StringUtils.isNotBlank(rkzt)) {
                criteria.andEqualTo("rkzt",rkzt);
            }
            if(StringUtils.isNotBlank(xmzt)) {
                criteria.andEqualTo("xmzt",xmzt);
            }
            dchyCgglXmDOList =  entityMapper.selectByExample(example);
        }
        return dchyCgglXmDOList;
    }

    @Override
    public List<DchyCgglXmDO> getDchyCgglXmDOListByDchybh(String dchybh, String rkzt, String xmzt) {
        List<DchyCgglXmDO> dchyCgglXmDOList = null;
        if(StringUtils.isNotBlank(dchybh)) {
            Example example = new Example(DchyCgglXmDO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("dchybh",dchybh);
            if(StringUtils.isNotBlank(rkzt)) {
                criteria.andEqualTo("rkzt",rkzt);
            }
            if(StringUtils.isNotBlank(xmzt)) {
                criteria.andEqualTo("xmzt",xmzt);
            }
            dchyCgglXmDOList =  entityMapper.selectByExample(example);
        }
        return dchyCgglXmDOList==null?new ArrayList<>():dchyCgglXmDOList;
    }
}
