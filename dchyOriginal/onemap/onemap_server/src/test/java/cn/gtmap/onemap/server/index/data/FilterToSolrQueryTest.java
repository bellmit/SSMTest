package cn.gtmap.onemap.server.index.data;

import cn.gtmap.onemap.core.gis.GeoUtils;
import com.gtis.config.EgovConfigLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.io.IOUtils;
import org.geotools.data.jdbc.FilterToSQL;
//import org.geotools.data.mongodb.FilterToMongoQuery;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.oracle.OracleFilterToSQL;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.StringWriter;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext-test.xml")
public class FilterToSolrQueryTest {
    @Test
    public void testVisit() throws Exception {
        FilterToSQL f2s = new OracleFilterToSQL(new OracleDialect(null));
        StringWriter w=new StringWriter();
        f2s.setWriter(w);
        Filter filter= ECQL.toFilter("a=b+1");
        filter.accept(f2s, null);
        System.out.println(w);
    }

    @Test
    public void testVisit1() throws Exception {
        FilterVisitor f2s = new FilterToSolrQuery();
        Filter filter= ECQL.toFilter("DISJOINT(geo,POLYGON((-10 30, -40 40, -10 -20, 40 20, 0 0, -10 30)))");
        filter.accept(f2s, null);
        System.out.println(f2s);
    }

    @Test
    public void testVisit2() throws Exception {
        Point point = GeoUtils.getGFactory().createPoint(new Coordinate(40601523.3793, 3668076.4282));
        System.out.println(GeoUtils.project(point,2364,4610));
    }

    @Test
    public void testVisit3() throws Exception {
        Geometry geo=GeoUtils.parse(IOUtils.toString(getClass().getResourceAsStream("/m.txt")));
        System.out.println(GeoUtils.project(geo,2364,4610));
    }
}
