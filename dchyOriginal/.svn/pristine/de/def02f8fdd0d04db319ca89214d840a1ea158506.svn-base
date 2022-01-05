package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.BaseServiceTest;
import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/1/22 13:51
 * File:    CoordinateConversionTest
 * (c) Copyright gtmap Corp.2015
 */
public class CoordinateConversionTest extends BaseServiceTest{


//    public final CoordinateConversion coordinateConversion=CoordinateConversion.class.newInstance();
    @Test
    public void testConversionXian80ToNt94() throws Exception {

        String str="{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40582828.04,3548032.66],[40583083.64,3548034.57],[40583297.76,3548036.18],[40582828.04,3548032.66]]]}}";
        CoordinateConversion coordinateConversion=new CoordinateConversion();
        coordinateConversion.clear();
        coordinateConversion.addPoint(40590893.056867145,3530939.888252615);
        coordinateConversion.addPoint(40590915.255453214,3530937.268872376);
        double[] result = coordinateConversion.convert(coordinateConversion.xian80ToNt94);
        print(JSON.toJSONString(Arrays.asList(result),true));

    }

    @Test
    public void testConversionNt94ToXian80() throws Exception{

        CoordinateConversion coordinateConversion=new CoordinateConversion();
        coordinateConversion.clear();
        coordinateConversion.addPoint(95215.20784115372,32735.91431185219);
        coordinateConversion.addPoint(95441.74125253898,32513.690711667354);
        double[] result = coordinateConversion.convert(coordinateConversion.nt94ToXian80);
        print(JSON.toJSONString(Arrays.asList(result),true));
    }

    @Test
    public void testConversionOfGeometry() throws Exception{
        String geojson ="{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[96335.32362205001,30580.94752937951],[96357.49519745544,30578.122888065758],[96352.33631886676,30535.30972275848],[96327.483221311,30537.12736364227],[96335.32362205001,30580.94752937951]]]}}";



    }
}
