package cn.gtmap.onemap.platform.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-6-17 上午9:44
 */
public class GeometryUtilsTest {
    @Test
    public void testParseGeoJSON() throws Exception {
        String value = IOUtils.toString(getClass().getResourceAsStream("/single_polygon"));
        Map map = JSON.parseObject(value, Map.class);
        String c = ((JSONArray) map.get("coordinates")).toJSONString();
        List list = JSON.parseArray(c, Object.class);

        Object geo = GeometryUtils.parseGeoJSON(value);


        value = IOUtils.toString(getClass().getResourceAsStream("/polygon_collection.json"));

        geo = GeometryUtils.parseGeoJSON(value);


        value = IOUtils.toString(getClass().getResourceAsStream("/single_feature.json"));

        geo = GeometryUtils.parseGeoJSON(value);
        if (geo instanceof SimpleFeature) {
            Geometry geometry = (Geometry) ((SimpleFeature) geo).getDefaultGeometry();
            CoordinateReferenceSystem crs = ((SimpleFeature) geo).getFeatureType().getCoordinateReferenceSystem();
            List<Object> pros = ((SimpleFeature) geo).getAttributes();

            System.out.print(geo.toString());

        }

        value =  IOUtils.toString(getClass().getResourceAsStream("/t_4610.json"));

        geo = GeometryUtils.parseGeoJSON(value);

        CoordinateReferenceSystem crs = ((SimpleFeature) geo).getFeatureType().getCoordinateReferenceSystem();

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/mp.json"));
        geo = GeometryUtils.parseGeoJSON(value);

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/point.json"));
        geo = GeometryUtils.parseGeoJSON(value);

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/linestring.json"));
        geo = GeometryUtils.parseGeoJSON(value);

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/multipoint.json"));
        geo = GeometryUtils.parseGeoJSON(value);

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/mline.json"));
        geo = GeometryUtils.parseGeoJSON(value);

        value =  IOUtils.toString(getClass().getResourceAsStream("/geojson/fc.json"));
        geo = GeometryUtils.parseGeoJSON(value);



        System.out.println(geo);

    }


    @Test
    public void testName() throws Exception {
        Map<String,Object> map2 = new LinkedHashMap<String, Object>();
        map2.put("type","Polygon");
        map2.put("geometry","ddsdssd");
        map2.put("crs","ss");

        String v = JSON.toJSONString(map2);

        System.out.print(v);

    }

    @Test
    public void testImportRegions() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/region.txt")));
        String line = null;
        Map<String, String> region = new LinkedHashMap<String, String>();
        while ((line = reader.readLine()) != null) {
            region.put(line, "2364");
        }
        String value = JSON.toJSONString(region);
        System.out.print(value);
    }
}
