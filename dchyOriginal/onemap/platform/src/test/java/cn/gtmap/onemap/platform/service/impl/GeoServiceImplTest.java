package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.BMark;
import cn.gtmap.onemap.model.BMarkGeometry;
import cn.gtmap.onemap.model.GeoBoundingBox;
import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.service.GeoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-1-7  Time: 上午8:47
 * Version: v1.0
 */
public class GeoServiceImplTest extends BaseServiceTest {

    @Autowired
    private GeoService geoService;


    @Test
    public void testGetLayerExtent() throws Exception {
        String layerName = "BPDK_320000";
        String dataSource = "sde";
        GeoBoundingBox box = geoService.getLayerExtent(layerName, dataSource);
        String sql = "objectid < 100";
        GeoBoundingBox boxPart = geoService.getLayerExtent(sql, layerName, dataSource);
        print(box);
        print(boxPart);
    }

    @Test
    public void testGeoJson2BMark() throws Exception {
//        String fc = getGeoFromFile("C:\\Users\\ibm\\Desktop\\test\\fc.json");
        String test1 = "[{\"attributes\":{\"DKID\":\"183F46542VR7H001\",\"XZQDM\":\"320700\"},\"marks\":[{\"borderLength\":0,\"pntIndex\":1,\"polygonNo\":1,\"roundNo\":1,\"x\":9659865.86174776,\"y\":4.4085420203285016E7},{\"borderLength\":0,\"pntIndex\":2,\"polygonNo\":1,\"roundNo\":1,\"x\":9923675.678312497,\"y\":4.05434557520196E7},{\"borderLength\":0,\"pntIndex\":3,\"polygonNo\":1,\"roundNo\":1,\"x\":-1.015225105693544E7,\"y\":3.70275888200734E7},{\"borderLength\":0,\"pntIndex\":4,\"polygonNo\":1,\"roundNo\":1,\"x\":9556604.794202482,\"y\":4.06221421161923E7},{\"borderLength\":0,\"pntIndex\":5,\"polygonNo\":1,\"roundNo\":1,\"x\":9659865.86174776,\"y\":4.4085420203285016E7}]}]";
        String test = "[{\"attributes\":{\"DKID\":\"C891079B96C34C10117013FF\",\"DKMC\":\"Admin\",\"XZQDM\":\"320600\"},\"marks\":[{\"borderLength\":0,\"pcdId\":\"38JA5632G54GV7JS\",\"pntIndex\":1,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548203,\"y\":40572929},{\"borderLength\":0,\"pcdId\":\"38JA5632GC4GV7JT\",\"pntIndex\":2,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548266,\"y\":40572976},{\"borderLength\":0,\"pcdId\":\"38JA5632GI4GV7JU\",\"pntIndex\":3,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548356,\"y\":40573048},{\"borderLength\":0,\"pcdId\":\"38JA5632GO4GV7JV\",\"pntIndex\":4,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548458,\"y\":40573141},{\"borderLength\":0,\"pcdId\":\"38JA5632GU4GV7JW\",\"pntIndex\":5,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548588,\"y\":40573279},{\"borderLength\":0,\"pcdId\":\"38JA5632H04GV7JX\",\"pntIndex\":6,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548709,\"y\":40573426},{\"borderLength\":0,\"pcdId\":\"38JA5632H64GV7JY\",\"pntIndex\":7,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548905,\"y\":40573688}]}]";
        List<BMark> bMarks = new ArrayList<BMark>();

        List<Map> list = JSON.parseObject(test, List.class);
        Map map = list.get(0);
        JSONArray marks = (JSONArray) map.get("marks");
        for (Object mark : marks) {
            JSONObject jsonObject = (JSONObject) mark;
            BMark bMark = new BMark(jsonObject.getInteger("pntIndex"), jsonObject.getInteger("roundNo"),
                    jsonObject.getInteger("polygonNo"), jsonObject.getDoubleValue("x"), jsonObject.getDoubleValue("y"));
            bMarks.add(bMark);
        }

        BMarkGeometry bMarkGeometry = new BMarkGeometry(bMarks);
        String attributes = "{\"attributes\":{\"DKID\":\"C891079B96C34C10117013FF\",\"DKMC\":\"Admin\",\"XZQDM\":\"320600\"}}";
        Map attributeMap = JSON.parseObject(attributes, Map.class);
        bMarkGeometry.setAttributes(attributeMap);
        String result = geoService.toGeoJSONForLine(bMarkGeometry);
        print(result);
//        print(geoService.toGeoJSON(bMarkGeometry));
//        List<BMarkGeometry> bMarkGeometryList = geoService.fc2BMark(fc);
//        print(JSON.toJSONString(bMarkGeometryList));
    }

    @Test
    public void testExtendProps() throws Exception {
        String feature = getGeoFromFile("E:\\temp\\0815.json");
        String featureWithoutProp = getGeoFromFile("E:\\temp\\bb0614.json");
        String geometry = getGeoFromFile("E:\\temp\\zj.json");
        String featureCollection = getGeoFromFile("E:\\test\\bj.json");
        Map<String, Object> prop = Maps.newLinkedHashMap();
        prop.put("NAME", "测试属性");
        prop.put("author", "alex");
        prop.put("bool", true);
        prop.put("number", 1.234);

        print(geoService.extendProps(feature, prop));
        print(geoService.extendProps(featureWithoutProp, prop));
        print(geoService.extendProps(geometry, prop));
        print(geoService.extendProps(featureCollection, prop));
    }


    @Test
    public void testFindTopoError() throws Exception {
        String fc = getGeoFromFile("C:\\Users\\ibm\\Desktop\\test\\bb0614_fc.json");
//        String fc = getGeoFromFile("C:\\Users\\ibm\\Desktop\\test\\fc.json");
        print(geoService.findTopoError(fc));
    }


    private String getGeoFromFile(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            String geometry = IOUtils.toString(inputStream);
            return geometry;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testUpdate() throws Exception {

        String layerName = "sde.BPDKGH";
        String dataSource = "sde";
        String feature = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.65095174026622,32.831911755266184],[118.65174871363877,32.82997839668048],[118.64927616282529,32.83015025077699],[118.65095174026622,32.831911755266184]]]},\"properties\":{\"PRONAME\":\"焦山new\"}}";
        Boolean result = geoService.update(layerName, "PRONAME = '焦山'", feature, dataSource);
        print(result);


    }

    @Test
    public void testInsert() throws Exception {

        String layerName = "SDE.CSDK";
        String dataSource = "sde";
        String geojson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40445561.41,3568211.0300000003],[40445607.12,3568282.1400000006],[40445683.77,3568216.12],[40445717.94,3568177.68],[40445714.96,3568160.8499999996],[40445692.89,3568160.95],[40445677.74,3568160.6800000006],[40445662.47,3568159.71],[40445644.73,3568157.7],[40445629.81,3568155.26],[40445618.13,3568152.86],[40445603.71,3568149.3],[40445590.33,3568145.37],[40445577.59,3568141.06],[40445565.72,3568136.52],[40445549.04,3568129.23],[40445536.09,3568122.8199999994],[40445523.37,3568115.81],[40445521.36,3568123.21],[40445520.64,3568129.3],[40445520.23,3568129.3],[40445520.22,3568129.3299999996],[40445505.88,3568129.36],[40445505.85,3568129.36],[40445553.62,3568203.56],[40445556.38,3568203.2],[40445557.03,3568204.22],[40445559.43,3568203.96],[40445562.52,3568203.2199999997],[40445563.07,3568204.7099999995],[40445563.61,3568206.18],[40445561.46,3568210.8400000003],[40445561.41,3568211.0300000003]]]},\"properties\":{\"PZSJ\":\"2017-08-07\",\"DKMC\":\"测试时间地块1\"}}";
        try {
            String result = geoService.insert(layerName, geojson, false, dataSource);
            print(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() throws Exception {

        String layerName = "sde.BPDKGH";
        String dataSource = "sde";
        Boolean result = geoService.delete(layerName, "PRONAME like '%地块1%'", dataSource);
        print(result);

    }


    @Test
    public void testInsertShp() {
        String path = "D:\\Documents\\项目文档\\淮安土地执法\\WPJC.zip";
        System.out.println(path);
        long date = System.currentTimeMillis();
        try {
            String result = geoService.insertShpCoordinates(path, "{}", "YZTDATA.WPSJ_INSERT8", "YZTDATA", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - date);
    }

    @Test
    public void testReadFeatureJSONCRS() throws Exception {

//        String crs = "EPSG:4610";
//        String geojson = "{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},\"properties\":{\"PRONAME\":\"xxx\"}}";
//        CoordinateReferenceSystem coordinateReferenceSystem = geoService.readCRS(geojson);
//        print(coordinateReferenceSystem.toWKT());
//        print("--");

    }

    @Test
    public void testParseUndefineSR() throws Exception {

//        String wkid = "4610";
//        String wkid1 = "EPSG:2364";
//        String wkt = "GEOGCS[\"Xian 1980\", \n" +
//                "  DATUM[\"Xian 1980\", \n" +
//                "    SPHEROID[\"IAG 1975\", 6378140.0, 298.257, AUTHORITY[\"EPSG\",\"7049\"]], \n" +
//                "    AUTHORITY[\"EPSG\",\"6610\"]], \n" +
//                "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n" +
//                "  UNIT[\"degree\", 0.017453292519943295], \n" +
//                "  AXIS[\"Geodetic latitude\", NORTH], \n" +
//                "  AXIS[\"Geodetic longitude\", EAST], \n" +
//                "  AUTHORITY[\"EPSG\",\"4610\"]]";
//
//        CoordinateReferenceSystem coordinateReferenceSystem = geoService.parseUndefineSR(wkt);
//        print(coordinateReferenceSystem.toWKT());
//        print("-----");

    }

    @Test
    public void testDwgCoordinates() throws Exception {

        String dwgFile = "http://192.168.50.169:8088/cad.dwg";
        String gpUrl = "http://192.168.1.125:6080/arcgis/rest/services/GP/CAD2PoylgonFeatureSets/GPServer/CAD%20File%20Transform%20to%20Poylgon%20FeatureSets%20Python%20Script";

    }

    @Test
    public void testExport2Shp() throws Exception {

        String layerName = "sczd_k_210402";
        String where = "XMID='Z8QB5346FDWKB80F'";
        String[] of = new String[]{"XZQDM", "XMMC", "XMID"};
        File file = geoService.exportToShp(layerName, where, null, of, "sde");
        if (file.exists())
            print(file.getAbsolutePath());
    }

    @Test
    public void mergeImage() throws IOException {

        int chunks = 6;

        int chunkWidth = 1920, chunkHeight = 1080;
        int type;

        //读入小图
        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File("C:\\Cap\\" + i + ".jpg");
        }

        //创建BufferedImage
        BufferedImage[] buffImages = new BufferedImage[chunks];
        try {
            for (int i = 0; i < chunks; i++) {
                buffImages[i] = ImageIO.read(imgFiles[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        type = buffImages[0].getType();
        chunkWidth = buffImages[0].getWidth();
        chunkHeight = buffImages[0].getHeight();

        //设置拼接后图的大小和类型
        BufferedImage finalImg = new BufferedImage(chunkWidth * 6, chunkHeight, BufferedImage.TYPE_INT_RGB);

        //写入图像内容
        int num = 0;
        for (int i = 0; i < 6; i++) {
            finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * i, chunkHeight, null);
            num++;
        }

        //输出拼接后的图像
        ImageIO.write(finalImg, "jpeg", new File("C:\\Cap\\finalImg.jpg"));

        System.out.println("完成拼接！");
    }

    @Test
    public void testisLine() throws Exception {
        String line = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[4.044354051E7,3512509.94],[4.044356005E7,3512486.61],[4.044357951E7,3512494.62],[4.044362526E7,3512433.57],[4.044362289E7,3512433.22],[4.044361926E7,3512431.94],[4.044359942E7,3512425.21],[4.044359916E7,3512425.96],[4.044358298E7,3512420.65],[4.044352738E7,3512498.07],[4.044352936E7,3512500.12],[4.044354051E7,3512509.94]]]},\"properties\":{\"DKID\":\"38G925246WJ7W20B\",\"XZQDM\":\"320600\"}}";
//        String line ="{\"result\":\"{\\\"type\\\":\\\"FeatureCollection\\\",\\\"bbox\\\":[4.0448306885E7,3563238.092,4.0448565773E7,3563421.694],\\\"features\\\":[{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"Polygon\\\",\\\"coordinates\\\":[[[4.0448322473E7,3563421.6939999997],[4.0448317398E7,3563394.764],[4.0448317311E7,3563392.5279999995],[4.0448306885E7,3563365.781],[4.0448329167E7,3563357.4339999994],[4.0448353792E7,3563348.108],[4.0448411779E7,3563323.1109999996],[4.0448434507E7,3563314.5350000006],[4.0448433349E7,3563311.748],[4.0448439861E7,3563309.228],[4.0448439017E7,3563307.086],[4.0448448982E7,3563303.085],[4.0448495114E7,3563284.4969999995],[4.0448496731E7,3563283.315],[4.0448537947E7,3563253.197],[4.0448541289E7,3563250.971],[4.0448562574E7,3563238.092],[4.0448563003E7,3563240.309],[4.0448565773E7,3563254.613],[4.0448563649E7,3563255.865],[4.0448515163E7,3563277.3690000004],[4.0448513555E7,3563278.564],[4.0448514716E7,3563280.162],[4.0448521814E7,3563289.962],[4.0448522988E7,3563291.582],[4.0448521368E7,3563292.755],[4.0448495266E7,3563311.6610000003],[4.0448492027E7,3563314.007],[4.044841421E7,3563370.3709999993],[4.044841097E7,3563372.717],[4.0448391034E7,3563387.157],[4.0448389437E7,3563388.314],[4.0448388241E7,3563386.711],[4.044838174E7,3563377.736],[4.0448380606E7,3563376.17],[4.0448378992E7,3563377.352],[4.0448322473E7,3563421.6939999997]]]},\\\"properties\\\":{\\\"PZSJ\\\":\\\"\\\",\\\"XZQDM\\\":\\\"320211\\\",\\\"DKMC\\\":\\\"4\\\",\\\"ID\\\":\\\"\\\",\\\"XZQMC\\\":\\\"\\\",\\\"DKID\\\":\\\"23ME1204IBY3N7IH\\\",\\\"OBJECTID\\\":11523},\\\"id\\\":\\\"Featuref164cbb9cafa297ea40064cb89e50007\\\"}]}\"}";
        String s = "";
        s = geoService.expandLine(line, 300);
    }

    @Test
    public void testToGeoJSONForLine() throws Exception {
        String bMarksString = "[{\"borderLength\":0,\"pcdId\":\"38JA5632G54GV7JS\",\"pntIndex\":1,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548203,\"y\":40572929},{\"borderLength\":0,\"pcdId\":\"38JA5632GC4GV7JT\",\"pntIndex\":2,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548266,\"y\":40572976},{\"borderLength\":0,\"pcdId\":\"38JA5632GI4GV7JU\",\"pntIndex\":3,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548356,\"y\":40573048},{\"borderLength\":0,\"pcdId\":\"38JA5632GO4GV7JV\",\"pntIndex\":4,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548458,\"y\":40573141},{\"borderLength\":0,\"pcdId\":\"38JA5632GU4GV7JW\",\"pntIndex\":5,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548588,\"y\":40573279},{\"borderLength\":0,\"pcdId\":\"38JA5632H04GV7JX\",\"pntIndex\":6,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548709,\"y\":40573426},{\"borderLength\":0,\"pcdId\":\"38JA5632H64GV7JY\",\"pntIndex\":7,\"polygonNo\":1,\"roundNo\":1,\"sbId\":\"38JA54493W4GV7JO\",\"x\":3548905,\"y\":40573688}]";
        List<BMark> marks = JSONObject.parseObject(bMarksString, List.class);
        List bMarks=new ArrayList();
        for (Object mark : marks) {
            JSONObject jsonObject = (JSONObject) mark;
            BMark bMark = new BMark(jsonObject.getInteger("pntIndex"), jsonObject.getInteger("roundNo"),
                    jsonObject.getInteger("polygonNo"), jsonObject.getDoubleValue("x"), jsonObject.getDoubleValue("y"));
            bMarks.add(bMark);
        }
        BMarkGeometry bMarkGeometry = new BMarkGeometry(bMarks);
        String attributes = "{\"attributes\":{\"DKID\":\"C891079B96C34C10117013FF\",\"DKMC\":\"Admin\",\"XZQDM\":\"320600\"}}";
        Map attributeMap = JSON.parseObject(attributes, Map.class);
        bMarkGeometry.setAttributes(attributeMap);
        String result = geoService.toGeoJSONForLine(bMarkGeometry);
        print(result);
    }

}
