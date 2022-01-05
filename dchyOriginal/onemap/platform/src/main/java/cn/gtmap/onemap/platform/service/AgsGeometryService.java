package cn.gtmap.onemap.platform.service;


import java.io.File;

import com.esri.core.geometry.*;

/**
 * Created by shenjian on 2014-05-20.
 */
public interface AgsGeometryService {
    /**
     * 相交运算 by arcgis geometry api
     *
     * @param inputGeometry
     * @param intersector
     * @param sr
     * @return
     */
    String intersection(String inputGeometry, String intersector, String sr);

    /**
     * Creates the difference of two geometries. The dimension of geometry2 has to be equal to or greater than that of geometry1.
     *
     * @param geometry1
     * @param substractor
     * @param sr
     * @return
     */
    String difference(String geometry1, String substractor, String sr);

    /**
     * 图形融合
     *
     * @param geometries geometry集合
     * @param sr
     * @return
     */
    String union(String[] geometries, String sr);

    /**
     * 获取图形面积 by arcgis geometry api
     *
     * @param wkt
     * @return
     */
    Double getGeometryArea(String wkt);

    /**
     * 验证图形是否有效
     *
     * @param geometry wkt
     * @param sr       wkt的crs
     * @return
     */
    boolean validGeometry(String geometry, Object sr);

    /**
     * import shapefile
     *
     * @param shpFile
     * @return
     */
    @Deprecated
    String importShape(File shpFile);
}
