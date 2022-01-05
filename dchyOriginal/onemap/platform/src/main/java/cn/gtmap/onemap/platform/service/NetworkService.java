package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.network.NetworkRegion;

import java.util.List;

/**
 * 四全网点
 * Created by fanyoudu on 2018/9/11.
 */
public interface NetworkService {

    /**
     * 查询行政区根目录
     * @return
     */
    List<NetworkRegion> queryRootRegion();
}
