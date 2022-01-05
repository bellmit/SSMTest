package cn.gtmap.onemap.dchy.service.impl;

import cn.gtmap.onemap.dchy.dao.DchyDao;
import cn.gtmap.onemap.dchy.model.DchyCgglXmDO;
import cn.gtmap.onemap.dchy.service.DchyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/21
 * @description 多测合一
 */

public class DchyServiceImpl  implements DchyService {
    @Autowired
    private DchyDao dchyDao;

    @Override
    public List<DchyCgglXmDO> getDchyXm(String dchybh) {
        return dchyDao.getDchyXm(dchybh);
    }

    @Override
    public List<DchyCgglXmDO> getDchyXmByXmbh(String chxmbh) {
        return dchyDao.getDchyXmByXmbh(chxmbh);
    }
}
