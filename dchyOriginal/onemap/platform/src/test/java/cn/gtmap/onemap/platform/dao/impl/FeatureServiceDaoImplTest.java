package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.service.LoggerService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/12/29.
 */
public class FeatureServiceDaoImplTest extends BaseServiceTest {
    FeatureServiceDaoImpl featureServiceDaoImpl = new FeatureServiceDaoImpl();

    @Test
    public void getDataSource() throws Exception {
    }

    @Test
    public void getLayerCRS() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        CoordinateReferenceSystem result = featureServiceDaoImpl.getLayerCRS(layerName, dataSource);
        print("--getLayerCRS--" + result);
    }

    @Test
    public void query() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        String[] columns = "PZWH,PZRQ,DKNAME".split(",");
//        String[] columns=null;
        List result = featureServiceDaoImpl.query(layerName, "OBjectid=1", columns, true, dataSource);
        print("--result--" + JSON.toJSONString(result));
    }

    @Test
    public void getLayerColumns() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        String[] result = featureServiceDaoImpl.getLayerColumns(layerName, dataSource);
        print("---result---" + JSON.toJSONString(result));
    }

    @Test
    public void insert() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        Map columns = new HashMap();
        columns.put("SHAPE", "POLYGON ((119.01995482824248 34.58997748308731, 119.0208938951661 34.58990906972051, 119.02085371819933 34.58953195432082, 119.0199119173122 34.58960057081108, 119.01995482824248 34.58997748308731))");
        columns.put("PCMC", "test");
        String result = featureServiceDaoImpl.insert(layerName, columns, dataSource);
        print("---result---" + result);
    }


    @Test
    public void update() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        String primaryKey = "105474";
        Map columns = new HashMap();
        columns.put("PCMC", "测试update");
        boolean result = featureServiceDaoImpl.update(layerName, primaryKey, columns, dataSource);
        print("---result---" + result);
    }

    @Test
    public void delete() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        String primaryKey = "18886";
        boolean result = featureServiceDaoImpl.delete(layerName, primaryKey, dataSource);
        print("--result--" + result);
    }

    @Test
    public void query2() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "GL_GLSJ";
        String[] columns = "OBJECTID,PZWH,PZRQ,DKNAME".split(",");

        String wkt = "POLYGON ((119.01995482824248 34.58997748308731, 119.0208938951661 34.58990906972051, 119.02085371819933 34.58953195432082, 119.0199119173122 34.58960057081108, 119.01995482824248 34.58997748308731))";
        List<?> resultList = featureServiceDaoImpl.query(layerName, wkt, columns, dataSource);
        print("---result---" + JSON.toJSONString(resultList));

    }

    @Test
    public void intersect() throws Exception {
    }

    @Test
    public void intersect1() throws Exception {
    }

    @Test
    public void multiRelations() throws Exception {
    }

    @Test
    public void convertToListMap() throws Exception {
    }

    @Test
    public void convertToMap() throws Exception {
    }

    @Test
    public void convertToJtsGeometry() throws Exception {
    }

    @Test
    public void createGeometry() throws Exception {
    }

    @Test
    public void createPoint() throws Exception {
    }

    @Test
    public void createMultiPoint() throws Exception {
    }

    @Test
    public void createLineString() throws Exception {
    }

    @Test
    public void createMultiLineString() throws Exception {
    }

    @Test
    public void createLinearRing() throws Exception {
    }

    @Test
    public void createPolygon() throws Exception {
    }

    @Test
    public void createMultiPolygon() throws Exception {
    }

}