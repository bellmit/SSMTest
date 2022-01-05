package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;

import java.util.List;
import java.util.Map;

/**
 * . Web Map Service
 * support web map operation
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-10-31 下午8:01
 */
public interface WebMapService {

    enum SC_MODE {
        SIMPLIFY, NORMAL
    }

    enum REGION_LEVEL{
        province,city,county,street
    }

    /**
     * get config by tpl
     *
     * @param tplName
     * @return
     */
    Configuration getConfig(String tplName);

    /**
     * get region info by regionCode
     *
     * @param regionCode
     * @param crs
     * @return
     */
    Map<String, Object> getRegionInfo(String regionCode, String crs,REGION_LEVEL level);

    /**
     * get services by tpl
     *
     * @param tplName
     * @return
     */
    List<Service> getAllServices(String tplName);

    /**
     * get services class by tpl
     *
     * @param tplName
     * @return
     */
    List getServicesWithClassify(String tplName, SC_MODE mode);

    /**
     * clear service cache
     *
     * @param tpl
     * @return
     */
    boolean clearServiceCache(String tpl);

    /**
     * clear region cache
     *
     * @return
     */
    boolean clearRegionCache();


}
