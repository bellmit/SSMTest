package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.service.GISManager;
import cn.gtmap.onemap.platform.service.GISService;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.SDEManager;
import cn.gtmap.onemap.platform.utils.AppPropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午4:51
 */
@Service
public class GISManagerImpl extends BaseLogger implements GISManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     *
     */
    @Autowired
    private GISService service;

    @Autowired
    private SDEManager sdeManager;

    @Autowired
    private GeometryService geometryService;


    @PostConstruct
    private void initSpatialEngine() {
        try {
            Constant.SpatialType type = Constant.SpatialType.valueOf((String) AppPropertyUtils.getAppEnv(Constant.SPATIAL_ENGINE));
            service.initialize(type);
        } catch (Exception e) {
            logger.error(getMessage("spatialengine.init.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取对应空间处理服务
     *
     * @return
     */
    @Override
    public GISService getGISService() {
        return service;
    }

    @Override
    public SDEManager getSdeManager() {
        return sdeManager;
    }

    /**
     * @return
     */
    @Override
    public GeometryService getGeoService() {
        return geometryService;
    }

}
