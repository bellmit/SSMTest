package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.NetworkDao;
import cn.gtmap.onemap.platform.entity.network.NetworkRegion;
import cn.gtmap.onemap.platform.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fanyoudu on 2018/9/11.
 */
@Service
public class NetworkServiceImpl implements NetworkService {

    @Autowired
    private NetworkDao networkDao;

    @Override
    public List<NetworkRegion> queryRootRegion() {
        return networkDao.findByParentNullOrderBySerialNumberAsc();
    }
}
