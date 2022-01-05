package cn.gtmap.msurveyplat.server.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglGcjsspxxDo;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglGcjsspxxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/5/14
 * @description
 */
@Service
public class DchyCgglGcjsspxxServiceImpl implements DchyCgglGcjsspxxService {
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public void saveDchyCgglGcjsspxx(DchyCgglGcjsspxxDo dchyCgglGcjsspxxDo) {
        if(dchyCgglGcjsspxxDo != null&&StringUtils.isNotBlank(dchyCgglGcjsspxxDo.getSpxxid())) {
            entityMapper.saveOrUpdate(dchyCgglGcjsspxxDo,dchyCgglGcjsspxxDo.getSpxxid());
        }
    }

    @Override
    public DchyCgglGcjsspxxDo getDchyCgglGcjsspxxBySpxxid(String spxxid) {
        DchyCgglGcjsspxxDo dchyCgglGcjsspxxDo = null;
        if(StringUtils.isNotBlank(spxxid)) {
            dchyCgglGcjsspxxDo = entityMapper.selectByPrimaryKey(DchyCgglGcjsspxxDo.class,spxxid);
        }
        return dchyCgglGcjsspxxDo;
    }

    @Override
    public List<DchyCgglGcjsspxxDo> getDchyCgglGcjsspxxDoListByXmid(String xmid) {
        List<DchyCgglGcjsspxxDo>  dchyCgglGcjsspxxDoList = null;
        if(StringUtils.isNotBlank(xmid)) {
            Example example = new Example(DchyCgglGcjsspxxDo.class);
            example.createCriteria().andEqualTo("xmid",xmid);
            dchyCgglGcjsspxxDoList = entityMapper.selectByExample(example);
        }
        return dchyCgglGcjsspxxDoList;
    }
}
