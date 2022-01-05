package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.TdlyxzXzqDao;
import cn.gtmap.onemap.platform.entity.TdlyxzXzq;
import cn.gtmap.onemap.platform.service.TdlyxzXzqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TdlyxzXzqServiceImpl extends AbstractLogger<TdlyxzXzq> implements TdlyxzXzqService {
    @Autowired
    private TdlyxzXzqDao tdlyxzXzqDao;


    /**
     * 获取行政区信息
     *
     * @param dwdm
     * @return
     */
    @Override
    public TdlyxzXzq queryTdlyxzXzq(final String dwdm) {
        TdlyxzXzq tdlyxzXzq = tdlyxzXzqDao.findOne(dwdm);
        return tdlyxzXzq;
    }

}
