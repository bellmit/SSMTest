package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.SpatialDao;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午5:28
 */
public class OracleSpatialDaoImpl implements SpatialDao {
    /**
     * 获取数据源
     *
     * @return
     */
    @Override
    public DataSource getDataSource() {
        return null;
    }

    /**
     * 获取图层空间参考
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource) {
        return null;
    }

    /**
     * get layer cs by arcsde
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public PeCoordinateSystem getLayerCS(String layerName, String dataSource) {
        return null;
    }

    @Override
    public SeLayer detectLayer(String layerName, String dbSource) {
        return null;
    }

    @Override
    public boolean checkLayer(String layerName, String dbSource) {
        return false;
    }


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
    @Override
    public List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource) {
        return null;
    }

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
    @Override
    public List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, int limit, String dataSource) {
        return null;
    }

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, String wkt, String[] columns, String dataSource) {
        return null;
    }

    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource) {
        return null;
    }

    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @param where
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource, String where) {
        return null;
    }

    /**
     * 插入数据
     *
     * @param layerName
     * @param columns    包含图形、属性数据
     * @param dataSource
     * @return
     */
    @Override
    public String insert(String layerName, Map<String, Object> columns, String dataSource) {
        return null;
    }

    @Override
    public int insertRows(String layerName, List<Map<String, Object>> rows, String dateSource) {
        return 0;
    }

    /**
     * 更新数据
     *
     * @param layerName
     * @param primaryKey
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public boolean update(String layerName, String primaryKey, Map<String, Object> columns, String dataSource) {
        return false;
    }

    /**
     * 综合分析
     *
     * @param jsonValues
     * @param dataSource
     * @return
     */
    @Override
    public Map multiRelations(Map jsonValues, String geometry, String dataSource) {
        return null;
    }

    /**
     * 删除
     *
     * @param layerName
     * @param primaryKey
     * @param dataSource
     * @return
     */
    @Override
    public boolean delete(String layerName, String primaryKey, String dataSource) {
        return false;
    }

    /**
     * get layer columns
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public String[] getLayerColumns(String layerName, String dataSource) {
        return new String[0];
    }

    /**
     * 组织返回字段
     *
     * @param layerName
     * @param columns
     * @param requiredColumns
     * @param dataSource
     * @return
     */
    @Override
    public String[] processOutField(String layerName, String[] columns, String[] requiredColumns, String dataSource) {
        return new String[0];
    }

    @Override
    public boolean isIntersect(String layerName, String wkt, String dataSource,String where) {
        return false;
    }


}
