package cn.gtmap.onemap.platform.dao;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 空间操作Dao
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午2:30
 */
public interface SpatialDao {

    /**
     * 获取数据源
     *
     * @return
     */
    DataSource getDataSource();

    /**
     * 获取图层空间参考
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource);

    /**
     * get layer cs by arcsde
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    PeCoordinateSystem getLayerCS(String layerName, String dataSource);

    /**
     * 监测图层是否存在
     *
     * @param layerName
     * @param dbSource
     * @return
     */
    SeLayer detectLayer(String layerName, String dbSource);

    /**
     * 监测图层是否存在
     *
     * @param layerName
     * @param dbSource
     * @return
     */
    boolean checkLayer(String layerName, String dbSource);

    /**
     * 属性查询
     *
     * @param layerName
     * @param where
     * @param columns
     * @param returnGeometry
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource);


    /**
     * 属性查询
     *
     * @param layerName
     * @param where
     * @param columns
     * @param returnGeometry
     * @param limit
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, int limit, String dataSource);

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, String wkt, String[] columns, String dataSource);


    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource, String where);

    /**
     * 综合分析
     *
     * @param jsonValues
     * @param dataSource
     * @return
     */
    Map multiRelations(Map jsonValues, String geometry, String dataSource);

    /**
     * 插入数据
     *
     * @param layerName
     * @param columns    包含图形、属性数据
     * @param dataSource
     * @return rowId
     */
    String insert(String layerName, Map<String, Object> columns, String dataSource);

    /***
     *
     * @param layerName
     * @param rows
     * @param dateSource
     * @return
     */
    int insertRows(String layerName, List<Map<String, Object>> rows, String dateSource);

    /**
     * 更新数据
     *
     * @param layerName
     * @param primaryKey
     * @param columns
     * @param dataSource
     * @return
     */
    boolean update(String layerName, String primaryKey, Map<String, Object> columns, String dataSource);


    /**
     * 删除
     *
     * @param layerName
     * @param primaryKey
     * @param dataSource
     * @return
     */
    boolean delete(String layerName, String primaryKey, String dataSource);

    /**
     * get layer columns
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    String[] getLayerColumns(String layerName, String dataSource);

    /**
     * 组织返回字段
     *
     * @param layerName
     * @param columns
     * @param requiredColumns
     * @param dataSource
     * @return
     */
    String[] processOutField(String layerName, String[] columns, String[] requiredColumns, String dataSource);

    /**
     * 判断图形是否与图层产生叠加
     * @param layerName
     * @param wkt
     * @param dataSource
     * @return
     */
    boolean isIntersect(String layerName, String wkt, String dataSource, String where);

}
