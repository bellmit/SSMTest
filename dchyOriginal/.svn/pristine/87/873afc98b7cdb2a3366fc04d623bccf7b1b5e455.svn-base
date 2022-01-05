package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.network.NetworkRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fanyoudu on 2018/9/11.
 */
public interface NetworkDao extends JpaRepository<NetworkRegion, String> {

    /**
     * find root region
     * @return
     */
    List<NetworkRegion> findByParentNullOrderBySerialNumberAsc();
}
