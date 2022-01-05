package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.video.CameraOfflineTrend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * camera offline service
 *
 * @author <a href="mailto:zhaozhuyi@gtmap.cn">zhaozhuyi</a>
 * @version v1.0, 2018/1/29 (c) Copyright gtmap Corp.
 */
public interface CameraOfflineService {

    /**
     * get trend all time in db
     * @return
     */
    List<CameraOfflineTrend> getTrendAllTime();

    /**
     * get trend by page
     * @return
     */
    Page<CameraOfflineTrend> getTrendPage(Pageable pageable);

}
