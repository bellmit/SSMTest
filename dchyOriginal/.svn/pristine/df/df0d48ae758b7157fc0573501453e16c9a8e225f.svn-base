package cn.gtmap.onemap.platform.geo;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.service.GeometryService;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-24 下午2:11
 */
public class GeoToolsTest extends BaseServiceTest {

    @Autowired
    private GeometryService geometryService;

    @Test
    public void testConnectionOracleSpatial() throws Exception {

        Map params ;

        params = new HashMap();
        params.put("dbtype","oracle");
        params.put("host","192.168.0.201");
        params.put("port","1521");
        params.put("instance","orcl");
        params.put("user","TDLY_2011");
        params.put("passwd","gtis");
        params.put("schema","TDLY_2011");


//        params.put("password","sde@192.168.0.201");

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        List<Name> typeNames = dataStore.getNames();

        SimpleFeatureSource featureSource = dataStore.getFeatureSource("TDLY_2011.XZQ_H_329999");

        Filter filter = CQL.toFilter("OBJECTID > 0");


        Query query = new Query(typeNames.get(0).getLocalPart(), filter);

        FeatureCollection featureCollection = featureSource.getFeatures(filter);

        FeatureIterator iterator = featureCollection.features();
        try {

            while (iterator.hasNext()) {
                Feature feature = iterator.next();

                MultiPolygon multiPolygon = (MultiPolygon) feature.getDefaultGeometryProperty().getValue();
                multiPolygon.setSRID(2364);

//                Geometry geo = polygon.intersection(multiPolygon);

//                System.out.print(geo);
            }

        } catch (Exception e) {

        }


    }


    @Test
    public void testProj() throws Exception {


    }

    @Test
    public void testToplogy() throws Exception {

        String sPoint = "POINT (30 10)";
        String sLine = "LINESTRING (30 10, 10 30, 40 40)";
        String p1 = "POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))";
        String p2 = "POLYGON ((35 10, 10 20, 15 40, 45 45, 35 10),(20 30, 35 35, 30 20, 20 30))";
        //
        Point point = (Point) geometryService.readWKT(sPoint);
        LineString line = (LineString) geometryService.readWKT(sLine);
        Polygon polygon1 = (Polygon) geometryService.readWKT(p1);
        Polygon polygon2 = (Polygon) geometryService.readWKT(p2);
        //

//        polygon1.convexHull()

        print("--ok--");


    }



}
