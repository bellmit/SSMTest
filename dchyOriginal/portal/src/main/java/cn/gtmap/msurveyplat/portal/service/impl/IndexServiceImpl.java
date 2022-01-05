package cn.gtmap.msurveyplat.portal.service.impl;


import cn.gtmap.msurveyplat.portal.entity.DchyCgglSjxx;
import cn.gtmap.msurveyplat.portal.entity.DchyCgglSqr;
import cn.gtmap.msurveyplat.portal.entity.DchyCgglXm;
import cn.gtmap.msurveyplat.portal.mapper.IndexMapper;
import cn.gtmap.msurveyplat.portal.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author szh
 */
@Service
public class IndexServiceImpl  implements IndexService {
    @Autowired
    private IndexMapper indexMapper;

    @Override
    public void insertXm(DchyCgglXm dchyCgglXm) {
        if (dchyCgglXm !=null && StringUtils.isNotBlank(dchyCgglXm.getXmid())) {
            indexMapper.insertXm(dchyCgglXm);
        }
    }

    @Override
    public void insertSjxx(DchyCgglSjxx cgglSjxx) {
        indexMapper.insertSjxx(cgglSjxx);
    }

    @Override
    public DchyCgglXm getXmById(String xmid) {
        return indexMapper.getXmById(xmid);
    }

    @Override
    public void updateXm(DchyCgglXm dchyCgglXm) {
        indexMapper.updateXm(dchyCgglXm);
    }

    @Override
    public void insertSqr(DchyCgglSqr sqr) {
        indexMapper.insertSqr(sqr);
    }
}
