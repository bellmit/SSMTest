package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.LayerRegion;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import cn.gtmap.onemap.platform.service.DictService;
import cn.gtmap.onemap.platform.service.DocumentService;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import cn.gtmap.onemap.platform.utils.EnumUtils;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.service.GeoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.esri.core.geometry.OperatorFactoryLocal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.Test;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-17 下午5:19
 */
public class GeometryServiceImplTest extends BaseServiceTest {

	@Autowired
	private GeometryServiceImpl geometryService;
	@Autowired
	private GISServiceImpl gisService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private GeoService geoService;
	@Autowired
	private DictService dictService;

	@Autowired
	private AgsGeometryServiceImpl agsGeometryService;

	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

	String path = "E:\\temp\\bh_geo.json";
	String geojson = getGeoFromFile(path);
	String dltbLayer = "SDE.DLTB_H_320211_2015";
	String xzdwLayer = "SDE.XZDW_H_320211_2015";
	String ds = "sde";

	class MyThread extends Thread {
		@Override
		public void run() {
			Map ret = gisService.tdlyxzAnalysis2(dltbLayer, xzdwLayer, "", geojson, ds);
			soutArea(ret);
		}
	}


	@Test
	public void testReportCoord() throws Exception {
		try {
			List<String> names = Lists.newArrayList("32040017040000000.ZIP");//"32040017110000000.ZIP",
			String dltb = "DLTB_2015_3204";
			String xzdw = "XZDW_2015_3204";
			for (String name : names) {
				print("------START---------" + name);
				String old_path = "E:\\temp\\cz\\".concat(name);
				String geojson = getGeoFromBjZip(old_path);
				Map fcMap = JSON.parseObject(geojson, Map.class);
				Map feature = (Map) ((List) fcMap.get("features")).get(0);
				Map properties = (Map) feature.get("properties");
				print("----报件面积----");
				print(MapUtils.getString(properties, "INFO"));
				Map map = gisService.tdlyxzAnalysis2(dltb, xzdw, "", geojson, null);
				List<Map> ret = gisService.tdlyxzResult(map, null, EnumUtils.UNIT.SQUARE, false);
				Map sumMap = ret.get(ret.size() - 1);
				Map categoryB = (Map) sumMap.get("categoryB");
				print("----分析后面积----");
				print("---合计----" + MapUtils.getDoubleValue(sumMap, "sumArea"));
				print("---农用地---" + MapUtils.getDoubleValue(categoryB, "农用地"));
				print("---耕地-----" + MapUtils.getDoubleValue(categoryB, "01"));
				print("---建设用地--" + MapUtils.getDoubleValue(categoryB, "建设用地"));
				print("---未利用地--" + MapUtils.getDoubleValue(categoryB, "未利用地"));
				print("----------END--------------");
			}
//            print(JSON.toJSONString(sumMap));

//            String geo1 = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[4.0483422726E7,3542983.233],[4.0483360436E7,3542889.553],[4.0483367557E7,3542885.813],[4.048342282E7,3542968.926],[4.04834435E7,3542972.94],[4.0483424768E7,3542985.772],[4.0483424037E7,3542984.968],[4.0483423355E7,3542984.1209999993],[4.0483422726E7,3542983.233]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}}}";
//            String geo2 = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[4.0483642156E7,3542836.8499999996],[4.0483660856E7,3542824.04],[4.0483660334E7,3542823.002],[4.0483659747E7,3542822.0000000005],[4.0483659097E7,3542821.038],[4.0483617262E7,3542763.2699999996],[4.0483609815E7,3542766.626],[4.0483645733E7,3542816.224],[4.0483642156E7,3542836.8499999996]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}}}";
//
//            print(geometryService.getGeoArea(geometryService.readFeatureJSON(geo1), geometryService.parseUndefineSR("2364")));
//            print(geometryService.getGeoArea(geometryService.readFeatureJSON(geo2), geometryService.parseUndefineSR("2364")));
//            String geojson = getGeoFromBjZip(old_path);
//            print(geojson);
//            FeatureCollection featureCollection = geometryService.readFeatureCollectionJSON(geojson);
//            SimpleFeature feature = (SimpleFeature) featureCollection.features().next();
//            Polygon polygon = (Polygon) feature.getDefaultGeometry();
//            print(geometryService.getGeoArea(featureCollection, geometryService.parseUndefineSR("2364")));
//            print(geometryService.exportToShp(geojson).getAbsolutePath());

//            geometryService.getGeoArea(geometryService.readFeatureCollectionJSON(geojson))
//            geometryService.exportToShp(geojson);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBhTdlyxz() throws Exception {

		String path = "E:\\temp\\geo.json";
		String geojson = getGeoFromFile(path);
		String dltbLayer = "SDE.DLTB_H_320211_2015";
		String xzdwLayer = "SDE.XZDW_H_320211_2015";
		String ds = "sde";

		MyThread thread1 = new MyThread();
		thread1.run();

		MyThread thread2 = new MyThread();
		thread2.run();

		MyThread thread3 = new MyThread();
		thread3.run();


//        Map ret = gisService.tdlyxzAnalysis2(dltbLayer, xzdwLayer, geojson, ds);
//        soutArea(ret);
//
//        //second
//        Map ret2 = gisService.tdlyxzAnalysis2(dltbLayer, xzdwLayer, geojson, ds);
//        soutArea(ret2);
//
//        //third
//        Map ret3 = gisService.tdlyxzAnalysis2(dltbLayer, xzdwLayer, geojson, ds);
//        soutArea(ret3);
	}

	private void soutArea(Map ret) {
		Map<String, Double> dlMap = (Map<String, Double>) ret.get("analysisArea");
//        System.out.println(JSON.toJSONString(dlMap));
		double totArea = 0;
		for (String s : dlMap.keySet()) {
			totArea += MapUtils.getDoubleValue(dlMap, s, 0.0);
		}
		System.out.println("分析总面积:" + totArea);
	}

	@Test
	public void testTdghResult() throws Exception {

//        File file1 = new File("E:\\test\\太仓1.txt");
//        File file2 = new File("E:\\test\\太仓2.txt");
		File file0405 = new File("I:\\复件太仓港经济技术开发区核准范围4.5.txt");
//        File file0405 = new File("E:\\temp\\太仓港综合保税区去掉扣除区块坐标0405.txt");
//        String geojson1 = geometryService.getTextCoordinates(new FileInputStream(file1));
		String geojson2 = geometryService.getTextCoordinates(new FileInputStream(file0405));

//        System.out.println(geojson2);


		List<SimpleFeature> features = Lists.newArrayList();

//        FeatureCollection fc1 = (FeatureCollection) geometryService.readUnTypeGeoJSON(geojson1);
//
//        FeatureIterator featureIterator = fc1.features();
//        while (featureIterator.hasNext()){
//            features.add((SimpleFeature) featureIterator.next());
//        }

		FeatureCollection fc2 = (FeatureCollection) geometryService.readUnTypeGeoJSON(geojson2);
		FeatureIterator featureIterator2 = fc2.features();
		while (featureIterator2.hasNext()) {
			features.add((SimpleFeature) featureIterator2.next());
		}

//
		for (SimpleFeature feature : features) {
			Geometry geo = (Geometry) feature.getDefaultGeometry();
//            double area =  agsGeometryService.getGeometryArea(geo.toText());
//            double area =  geometryService.getGeoArea(geo,geometryService.parseUndefineSR("2364"));
			TopologyValidationError topologyValidationError = geometryService.validGeometry(geo);
			if (topologyValidationError != null) {
				//System.out.println(topologyValidationError.toString());
			}
			System.out.println(geo.getArea() / 10000);
		}


//        SimpleFeature feature1 = (SimpleFeature) geometryService.readUnTypeGeoJSON(geojson1);
//        SimpleFeature feature2 = (SimpleFeature) geometryService.readUnTypeGeoJSON(geojson2);
//        Geometry geo1 = (Geometry) feature1.getDefaultGeometry();
//        Geometry geo2 = (Geometry) feature2.getDefaultGeometry();
//       double area1 =  agsGeometryService.getGeometryArea(geo1.toText());
//       double area2 =  agsGeometryService.getGeometryArea(geo2.toText());
//
//        System.out.println(area1/10000);
//        System.out.println(area2/10000);

//        String layerType = "JSYDGZQ";
//        String analysisresult = getGeoFromFile("E:\\st_0217_r_9.json");
//        Map result = gisService.tdghscResult(layerType, analysisresult);
//        Map jsydgzq = (Map) result.get("建设用地管制区");
//        List<Map> infos = (List<Map>) jsydgzq.get("info");
//        for (Map info : infos) {
//            String lxmc = MapUtils.getString(info, "LXMC");
//            String area = MapUtils.getString(info, "AREA");
//            System.out.println(lxmc + ": " + area);
//            if (info.containsKey("detail") && "允许建设用地区".equals(lxmc)) {
//                List<Map> detail = (List<Map>) info.get("detail");
//                //按照sm分类
//                Map groupedMap = ArrayUtils.listConvertMap(detail, "SM");
//                for (Object key : groupedMap.keySet()) {
//                    String sm = String.valueOf(key);
//                    double smArea = getSumByField((List<Map>) groupedMap.get(key), "面积");
//                    System.out.println(sm + ": " + String.valueOf(smArea));
//                }
//            }
//        }

	}

	private double getSumByField(List<Map> list, String sumField) {
		double sum = 0;
		for (Map map : list) {
			if (!map.containsKey(sumField)) continue;
			sum += MapUtils.getDoubleValue(map, sumField, 0.0);
		}
		return sum;
	}

	private String getStrByField(List<Map> list, String sumField) {
		List<String> arr = Lists.newArrayList();
		for (Map map : list) {
			if (!map.containsKey(sumField)) continue;
			arr.add(MapUtils.getString(map, sumField));
		}
		return ArrayUtils.listToString(arr, ",");
	}

	public void testCommonAnalysis() {


	}

	@Test
	public void testIntersect() throws Exception {
		String geometry = getGeoFromFile("E:\\0216.json");
		String dltb = "SDE.dltb_320211_2009";
		String xzdw = "SDE.xzdw_320211_2009";
//        String[] outFields = new String[]{"OBJECTID", "TBBH", "DLBM", "DLMC"};
		Map result = gisService.tdlyxzAnalysis2(dltb, xzdw, "", geometry, null);
		Map<String, Double> dlMap = (Map<String, Double>) result.get("analysisArea");
		double totArea = 0;
		for (String s : dlMap.keySet()) {
			totArea += MapUtils.getDoubleValue(dlMap, s, 0.0);
		}
		System.out.println(totArea);
		System.out.println(JSON.toJSONString(dlMap));

		//        List<Map> result = (List<Map>) gisService.intersect3(layerName, geometry, outFields, null);
//        for (Map map : result) {
//            totArea += MapUtils.getDoubleValue(map, Constant.SE_SHAPE_AREA, 0.0);
//        }

	}

	@Test
	public void testValidate0() throws Exception {
//        String geometry = getGeoFromBjZip("E:\\temp\\32032217020000000.ZIP");
//        String geometry = getGeoFromBjZip("E:\\temp\\32068417050000000.ZIP");
//        String geometry = getGeoFromBjZip("E:\\temp\\nt.zip");
//        String geometry = getGeoFromBjZip("D:\\32070017530000000.ZIP");
//        String geometry = getGeoFromShpZip("D:\\zzshp\\dh.zip");
//        String geometry = getGeoFromFile("E:\\temp\\bb0614.json");
//        System.out.println(geometry);
//        print(geometryService.exportToShp(geometry).getAbsolutePath());

//        String geometry = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.70278583326635,34.70773895243104],[116.70512635593074,34.70761603477664],[116.70502616779235,34.70630435260381],[116.70231849468676,34.70644313672877],[116.70241896644984,34.70775821463041],[116.70277086080912,34.70773973800677],[116.70278583326635,34.70773895243104]]]},\"properties\":{\"NAME\":\"2017工矿批次18鹿楼水泥厂\",\"ID\":\"4c351e8a46bb4f46bad6e3ef158c6455\",\"SB_ID\":\"4c351e8a46bb4f46bad6e3ef158c6455\"},\"id\":\"Featuref15ca43fa6444028daac5ca43fa60004\"}";

//        String geometry = getGeoFromFile("E:\\temp\\sq.txt");
		String geometry = getGeoFromFile("E:\\temp\\0811.json");

//        File file = geometryService.exportToShp(geometry);
//        print(file.getAbsolutePath());

		Object obj = geometryService.readUnTypeGeoJSON(geometry);

//        print(geometryService.getGeoArea(obj, geometryService.parseUndefineSR("4610")));

		Geometry geo = null;
		TopologyValidationError validationError = null;
		if (obj instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature) obj;
			geo = (Geometry) feature.getDefaultGeometry();
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));
		} else if (obj instanceof FeatureCollection) {
			FeatureCollection fc = (FeatureCollection) obj;
			print(fc.size());
			FeatureIterator featureIterator = fc.features();
			while (featureIterator.hasNext()) {
				SimpleFeature feature = (SimpleFeature) featureIterator.next();
				geo = (Geometry) feature.getDefaultGeometry();
				validationError = geometryService.validGeometry(geo, true);
				System.out.println(JSON.toJSONString(validationError));
				if (validationError != null) {
					if (validationError.getErrorType() == 5) {
						boolean realSelf = isRealSelfIntersection(validationError.getCoordinate(), geo);
						print(realSelf);
					}
				}
			}
		} else if (obj instanceof Geometry) {
			geo = (Geometry) obj;
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));
		}
		if (validationError != null) {
			if (validationError.getErrorType() == 5) {
				boolean realSelf = isRealSelfIntersection(validationError.getCoordinate(), geo);
				if (!realSelf) {
					Geometry createdGeo = gisService.createValidGeometry(geo);
					System.out.println(createdGeo.getGeometryType());
				}
			}
		}
//
//        String geojson="{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[39534311.265,3795068.109],[39534320.717,3795027.441],[39534330.833,3794986.932],[39534341.611,3794946.595],[39534353.049,3794906.439],[39534365.142,3794866.476],[39534377.888,3794826.717],[39534391.283,3794787.171],[39534405.324,3794747.85],[39534420.007,3794708.765],[39534276.405,3794690.326],[39534240.09,3794964.176],[39533982.336,3794930.652],[39533982.336,3794930.652],[39534240.09,3794964.176],[39533982.336,3794930.652],[39533958.911,3795110.76],[39534292.024,3795154.085],[39534311.265,3795068.109]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2363\"}}}";
//        File file = geometryService.exportToShp(geometry);
//        print(file.getAbsolutePath());
	}


	@Test
	public void testValidate0111() throws Exception {


//        String geometry=getGeoFromShpZip("E:\\data\\0113\\0113_.zip");

		// String geometry =getGeoFromFile("E:\\连云港路段地块相交问题13.txt");

//        String geometry = getGeoFromFile("E:\\35_33_1.json");
//        String geometry = getGeoFromFile("E:\\0124_all.json");
//        String geometry = getGeoFromFile("E:\\0321.json");
//        String geometry = getGeoFromFile("E:\\0322.json");
//        String geometry = getGeoFromFile("E:\\temp\\st0417.txt");

//        File file = geometryService.exportToShp(geometry);
//        print(file.getAbsolutePath());

//        String geometry = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.81568733479774,31.29274756418927],[120.81568733477576,31.29274756430569],[120.81543888693425,31.29277421400068],[120.81525369469637,31.2928471609801],[120.81525369478268,31.292847160514],[120.81543889000002,31.29277421],[120.81568733479774,31.29274756418927]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4610\"}},\"properties\":{}}";

//        String path= "D:\\0424.json";
//        String geometry = getGeoFromFile("D:\\0831.json");


//                File file = new File("D:\\gt1.xml");
//            byte[] buffer = null;
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int n;
//            while ((n = fis.read(b)) != -1)
//            {
//                bos.write(b, 0, n);
//            }
//            fis.close();
//            bos.close();
//            buffer = bos.toByteArray();
//            Map map = geometryService.parseBjXmlAdv(buffer);
//            String features = MapUtils.getString(map,"feature");

//            FeatureCollection featureCollection  =(FeatureCollection) geometryService.readUnTypeGeoJSON(features);
//
//        String path = "D:\\gt.zip";
		String path = "D:\\tbj.ZIP";
		String geometry = getGeoFromBjZip(path);
////        String path = "D:\\0428.json";
////        String geometry = getGeoFromFile(path);
//
////        gisService.insert("SDE.CSDK_G",geometry,"sde");
//       String f =JSON.toJSONString(JSON.parseObject(features,Map.class).get("features")) ;

//        Object obj = geometryService.readUnTypeGeoJSON(geometryService.union(f));
		Object obj = geometryService.readUnTypeGeoJSON(geometry);
		Geometry geo = null;
		List point = new ArrayList();
		point.add("40450664.887");
		point.add("3558635.039");
		Point targetPnt = GeometryUtils.createPoint(new JSONArray(point));
		int x = 0;
		TopologyValidationError validationError = null;
		FeatureCollection fc = (FeatureCollection) obj;
		List<Polygon> polygons = new ArrayList<Polygon>(fc.size());
		if (obj instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature) obj;
			geo = (Geometry) feature.getDefaultGeometry();
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));

		} else if (obj instanceof FeatureCollection) {

			FeatureIterator featureIterator = fc.features();
			while (featureIterator.hasNext()) {
				SimpleFeature feature = (SimpleFeature) featureIterator.next();
				geo = (Geometry) feature.getDefaultGeometry();
//                if( geo.intersects(targetPnt)){
//                    x++;
//                }
				polygons.add((Polygon) geo);
				validationError = geometryService.validGeometry(geo, true);
				if (validationError != null) {
					print(validationError.toString());
					if (validationError.getErrorType() == 5) {
						print(isRealSelfIntersection(validationError.getCoordinate(), geo));
					}
				}
			}
			validationError = geometryService.validGeometry(makeMultiPolygon(polygons), false);
			System.out.println(geometryService.toGeoJSON(makeMultiPolygon(polygons)));
			File file = geometryService.exportToShp(geometry);
			System.out.println(file.getAbsolutePath());
		} else if (obj instanceof Geometry) {
			geo = (Geometry) obj;
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));
		}

		if (validationError != null) {
			if (validationError.getErrorType() == 5) {
				boolean realSelf = isRealSelfIntersection(validationError.getCoordinate(), makeMultiPolygon(polygons));
				print(realSelf);
				if (!realSelf) {
					Geometry createdGeo = gisService.createValidGeometry(geo);
					System.out.println(createdGeo.getGeometryType());
				}
			}
		}
	}


	private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();

	/**
	 * @param geometries
	 * @return
	 */
	private Geometry makeMultiPolygon(List<Polygon> geometries) {

		List<Polygon> polygons = Lists.newArrayList();
		for (Polygon geometry : geometries) {
			polygons.add(geometry);
		}
		return factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
	}


	private boolean isRealSelfIntersection(Coordinate coordinate, Geometry geo) {
		boolean result = true;
		double tolerance = 0.001;
		Geometry newgeo = gisService.createValidGeometry(geo);
		//找出包含自相交点的polygon
		List<Geometry> targetPolygons = Lists.newArrayList();
		if (newgeo != null && newgeo instanceof MultiPolygon) {
			MultiPolygon multiPolygon = (MultiPolygon) newgeo;
			for (int j = 0; j < multiPolygon.getNumGeometries(); j++) {
				Polygon polygon = (Polygon) multiPolygon.getGeometryN(j);
				List<Coordinate> coordinates = Arrays.asList(polygon.getCoordinates());
				Point targetPnt = geometryFactory.createPoint(coordinate);
				if (polygon.covers(targetPnt)) {
					targetPolygons.add(polygon);
					continue;
				}
				for (Coordinate coord : coordinates) {
					if (coord.equals2D(coordinate) || Math.abs(coord.distance(coordinate)) <= tolerance) {
						targetPolygons.add(polygon);
						break;
					}
				}
			}
		}
		for (Geometry geometry : targetPolygons) {
			System.out.println(geometryService.toGeoJSON(geometry));
		}
		if (targetPolygons.size() > 1) {
			Geometry duplicatedGeo = getDuplicatedGeo(targetPolygons, null);
			System.out.println(geometryService.toGeoJSON(duplicatedGeo));
//            geometryService.getGeoArea(duplicatedGeo,geometryService.parseUndefineSR("4610"))
			//判断重叠区域是什么类型图形 如果是多边形 还要计算其面积的大小 对于面积非常小的仍然会忽略
			duplicatedGeo.getClass();
			result = (((duplicatedGeo instanceof Polygon)) || ((duplicatedGeo instanceof MultiPolygon)) || (duplicatedGeo instanceof GeometryCollection)) && (!(duplicatedGeo instanceof MultiLineString)) && (!duplicatedGeo.isEmpty());
		}
		return result;
	}

	/**
	 * 从一系列geometry中找出所有重叠的部分
	 *
	 * @param geos [geometry] 必须都是geometry 不支持geometryCollection
	 * @return
	 */
	public Geometry getDuplicatedGeo(List<Geometry> geos, CoordinateReferenceSystem crs) {
		try {
			if (geos.size() > 0) {
				Geometry geo0 = geos.get(0);
				geos.remove(0);
				if (geos.size() == 0)
					return geo0;
				else {
					if (crs == null)
						return geo0.intersection(getDuplicatedGeo(geos, null));
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	private String getGeoFromShpZip(String path) {
		try {
			String geometry = geometryService.getShpCoordinates(new File(path));
			return geometry;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getGeoFromBjZip(String path) {
		try {
			Map map = (Map) geometryService.getZipCoordinates(new FileInputStream(new File(path)));
			Map valueMap = (Map) map.get("value");
			String geojson = MapUtils.getString(valueMap, "feature");
			return geojson;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param path
	 * @return
	 */
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
	public void testHttp() throws Exception {
//        String url = "http://192.168.90.44:8000/320981163100000000.ZIP";
//        String url = "http://192.168.90.44:8000/gh17.zip";
//        String url = "http://192.168.90.44:8000/0118.ZIP";

		File zipFile = new File("E:\\0124.ZIP");
		Map map = (Map) geometryService.getZipCoordinates(new FileInputStream(zipFile));
		Map valueMap = (Map) map.get("value");

		String geojson = MapUtils.getString(valueMap, "feature");

//        String url = "http://192.168.90.44:8000/0118_s.zip";
//        byte[] bytes = (byte[]) HttpRequest.get(url, null, HttpRequest.RES_DATA_TYPE.bytes.name());
//        InputStream inputStream = new ByteArrayInputStream(bytes);
//        Map bjMap = geometryService.getBJCoordinates(inputStream);
//        String geojson=MapUtils.getString(bjMap, "feature");

		Object obj = geometryService.readUnTypeGeoJSON(geojson);
		Geometry geo = null;
		TopologyValidationError validationError = null;
		if (obj instanceof SimpleFeature) {
			SimpleFeature feature = (SimpleFeature) obj;
			geo = (Geometry) feature.getDefaultGeometry();
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));
		} else if (obj instanceof FeatureCollection) {
			FeatureCollection fc = (FeatureCollection) obj;
			print(fc.size());
			FeatureIterator featureIterator = fc.features();
			while (featureIterator.hasNext()) {
				SimpleFeature feature = (SimpleFeature) featureIterator.next();
				geo = (Geometry) feature.getDefaultGeometry();
				validationError = geometryService.validGeometry(geo, true);
				System.out.println(JSON.toJSONString(validationError));
			}

		} else if (obj instanceof Geometry) {
			geo = (Geometry) obj;
			validationError = geometryService.validGeometry(geo, true);
			System.out.println(JSON.toJSONString(validationError));
			if (validationError.getErrorType() == 2) {
			}
		}

		if (validationError != null) {
			if (validationError.getErrorType() == 5) {
				print(isRealSelfIntersection(validationError.getCoordinate(), geo));
			}
//            print(JSON.toJSONString(geometryService.validGeometry(validGeo)));
		}

//        String yearList="[{\"dltb\":\"DLTB_H_2014\",\"xzdw\":\"XZDW_H_2014\",\"year\":\"2014\",\"selected\":\"selected\"},{\"dltb\":\"DLTB_320981\",\"xzdw\":\"XZDW_320981\",\"year\":\"2013\"}]";
//        List<Map> result=gisService.tdlyxzAnalysisMulti(JSON.parseObject(yearList, List.class), geojson, null);
//        print(JSON.toJSONString(result));

	}

	@Test
	public void testMultiAnalysisXz() throws Exception {
		String url = "http://192.168.90.44:8000/320703160100000000.ZIP";
		byte[] bytes = (byte[]) HttpRequest.get(url, null, HttpRequest.RES_DATA_TYPE.bytes.name());
		InputStream inputStream = new ByteArrayInputStream(bytes);
		Map bjMap = geometryService.getBJCoordinates(inputStream);

		Map reportMap = Maps.newHashMap();
		reportMap.put("area", MapUtils.getString(bjMap, "area"));
		reportMap.put("jsydArea", MapUtils.getString(bjMap, "jsydArea"));
		reportMap.put("wlydArea", MapUtils.getString(bjMap, "wlydArea"));
		reportMap.put("gdArea", MapUtils.getString(bjMap, "gdArea"));
		reportMap.put("nydArea", MapUtils.getString(bjMap, "nydArea"));


		String geojson = MapUtils.getString(bjMap, "feature");
		print(geojson);
//        String dltb = "DLTB_H_2014";
//        String xzdw = "XZDW_H_2014";
//
//        List<Map> analysisList = Lists.newArrayList();
//        if (StringUtils.isNotBlank(dltb) && StringUtils.isNotBlank(xzdw)) {
//            Map single = Maps.newHashMap();
//            single.put("year", Constant.DEFAULT_YEAR);
//            single.put("data", gisService.tdlyxzAnalysis2(dltb, xzdw, geojson, null));
//            analysisList.add(single);
//        }
//        if (analysisList != null && analysisList.size() > 0) {
//            //一次处理所有年份的分析结果
//            List<Map> result = gisService.processXzAnalysis(analysisList, reportMap, EnumUtils.UNIT.HECTARE, true);
//            print(result);
//        }
	}

	@Test
	public void testTz() throws Exception {

		String geojson2 = "{\"features\":[{\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMJ\":232610,\"PWH\":null,\"BZ\":null,\"XMMC\":\"泰州市2015年度第2批次城市建设用地地块一\",\"XMLX\":null,\"XMBH\":\"20160023\"},\"geometry\":{\"coordinates\":[[[40486918.4603,3589818.0496],[40486907.4228,3589722.7759],[40486907.4808,3589722.7737],[40486907.3417,3589721.5947],[40486889.6615,3589722.2352],[40486863.0613,3589723.195],[40486875.1117,3589683.5148],[40486887.652,3589682.9949],[40486900.1919,3589682.475],[40486949.5323,3589680.4454],[40486955.2924,3589679.9655],[40486979.5726,3589679.1256],[40486993.9527,3589678.6158],[40487001.9927,3589678.3358],[40487011.2028,3589678.0059],[40487027.9629,3589677.416],[40487044.3031,3589676.8362],[40487056.3632,3589676.4163],[40487070.9933,3589675.8364],[40487081.4734,3589675.4164],[40487093.0634,3589674.9565],[40487102.4935,3589674.5766],[40487110.7436,3589674.2367],[40487122.2637,3589673.7868],[40487127.5137,3589673.5768],[40487145.9539,3589672.8469],[40487146.9391,3589672.8065],[40487148.1439,3589672.757],[40487151.3839,3589672.647],[40487166.744,3589672.0271],[40487168.684,3589671.9571],[40487182.1441,3589671.4272],[40487188.8442,3589671.1573],[40487197.5643,3589670.8174],[40487205.9543,3589670.4874],[40487211.9544,3589670.2575],[40487214.7244,3589670.1475],[40487229.1945,3589669.5776],[40487245.9646,3589668.9277],[40487249.0947,3589668.8078],[40487266.9448,3589668.1179],[40487284.1149,3589667.448],[40487286.865,3589667.348],[40487315.2052,3589666.2583],[40487317.8754,3589638.3281],[40487318.4455,3589635.2281],[40487319.1655,3589627.848],[40487321.2957,3589611.1279],[40487321.7457,3589603.5179],[40487321.1458,3589598.3178],[40487320.5358,3589593.7478],[40487320.4358,3589589.9877],[40487320.3159,3589588.6377],[40487320.0959,3589588.1977],[40487319.3358,3589587.9677],[40487317.6858,3589587.7577],[40487315.5258,3589587.3477],[40487295.9757,3589584.5675],[40487293.0357,3589584.1475],[40487283.5156,3589582.8474],[40487274.3656,3589582.3173],[40487273.0155,3589582.1873],[40487270.4755,3589581.5273],[40487258.9255,3589579.0072],[40487256.3955,3589578.4571],[40487240.1454,3589576.017],[40487234.6353,3589575.1069],[40487229.5353,3589574.6569],[40487225.2653,3589574.2569],[40487222.3952,3589573.6468],[40487217.1852,3589572.9068],[40487206.1651,3589571.8367],[40487193.275,3589571.1866],[40487193.275,3589570.1466],[40487192.615,3589569.7466],[40487182.875,3589568.9865],[40487182.835,3589569.9665],[40487182.255,3589571.3665],[40487182.1349,3589572.5365],[40487181.5849,3589573.6365],[40487181.0749,3589574.0565],[40487180.5149,3589574.1465],[40487178.7449,3589573.8065],[40487172.7749,3589572.8864],[40487172.0449,3589572.8864],[40487171.4749,3589573.2964],[40487171.3649,3589573.9864],[40487170.9448,3589575.0564],[40487170.5648,3589575.3264],[40487169.3848,3589575.3664],[40487168.4748,3589574.8364],[40487160.2448,3589574.9863],[40487159.2047,3589575.2763],[40487158.3147,3589576.0763],[40487157.1947,3589579.0863],[40487155.6247,3589582.4763],[40487155.3147,3589582.8963],[40487155.0447,3589582.9363],[40487154.8247,3589582.6163],[40487153.9647,3589579.1363],[40487152.0747,3589577.0563],[40487151.1847,3589576.4963],[40487149.2247,3589575.9162],[40487140.4346,3589574.4062],[40487131.2746,3589572.6261],[40487125.4245,3589572.066],[40487112.2144,3589570.8059],[40487101.2744,3589569.4858],[40487087.4943,3589568.5957],[40487069.4841,3589566.9755],[40487057.0841,3589566.4454],[40487056.424,3589569.2454],[40487054.964,3589570.6954],[40487053.014,3589571.5954],[40487050.814,3589572.2154],[40487048.7039,3589572.3854],[40487045.5939,3589572.0754],[40487043.3239,3589571.5654],[40487040.7639,3589570.6353],[40487039.4339,3589569.9753],[40487036.6839,3589569.2353],[40487033.6539,3589568.7653],[40487030.3438,3589568.6652],[40487027.3238,3589568.2752],[40487021.5638,3589566.8251],[40487016.7438,3589565.7151],[40487013.9037,3589565.1251],[40487009.2937,3589563.555],[40487007.5837,3589563.035],[40487005.3437,3589562.585],[40487002.9937,3589562.405],[40487000.1637,3589562.5149],[40486993.6436,3589562.3049],[40486987.2436,3589561.6548],[40486984.8236,3589561.5448],[40486982.0635,3589561.9648],[40486980.0935,3589562.6548],[40486977.4235,3589563.0948],[40486975.7835,3589563.2947],[40486974.3635,3589563.1847],[40486970.0434,3589561.9347],[40486968.8934,3589561.8247],[40486968.1634,3589561.9847],[40486966.8834,3589563.0147],[40486965.5634,3589564.0747],[40486964.1434,3589564.8747],[40486963.0234,3589565.2247],[40486962.0433,3589565.2447],[40486961.5733,3589564.8546],[40486961.0734,3589563.6946],[40486960.4934,3589563.2746],[40486959.7833,3589563.2746],[40486958.8033,3589563.5446],[40486957.2033,3589563.8946],[40486955.4033,3589564.0546],[40486953.0933,3589563.7846],[40486948.0133,3589563.1945],[40486930.0431,3589561.1144],[40486920.0331,3589560.4643],[40486901.2529,3589560.4441],[40486861.7126,3589559.2438],[40486868.3829,3589533.3837],[40486918.4133,3589532.1641],[40486955.3635,3589534.2744],[40486977.4337,3589534.9445],[40487033.7541,3589539.075],[40487063.0843,3589542.3253],[40487076.8744,3589544.2954],[40487079.0344,3589545.0454],[40487080.7644,3589545.4955],[40487092.5145,3589545.7156],[40487108.8146,3589546.6657],[40487113.9646,3589547.3157],[40487115.9247,3589547.3558],[40487117.1247,3589547.3058],[40487126.0647,3589546.9558],[40487129.0848,3589546.7859],[40487137.5548,3589549.516],[40487139.2448,3589550.166],[40487139.6048,3589548.846],[40487140.5449,3589545.4559],[40487141.2649,3589541.0559],[40487142.015,3589534.2459],[40487142.015,3589527.6558],[40487142.4451,3589521.8358],[40487142.6551,3589520.8358],[40487142.1951,3589519.6558],[40487142.0951,3589518.3758],[40487142.0951,3589517.3958],[40487142.6051,3589515.4557],[40487142.4051,3589515.1057],[40487140.4151,3589513.0457],[40487139.3951,3589511.2957],[40487139.0051,3589510.0857],[40487139.0551,3589509.1857],[40487139.4451,3589508.6757],[40487140.4452,3589508.1757],[40487141.9852,3589508.0857],[40487142.6052,3589507.9257],[40487142.8652,3589507.2757],[40487143.6352,3589501.1056],[40487144.1253,3589498.0456],[40487146.7654,3589488.9956],[40487146.7654,3589488.0156],[40487146.5954,3589487.3756],[40487144.2254,3589485.9055],[40487142.9454,3589484.8855],[40487142.8154,3589484.3355],[40487143.5054,3589483.3655],[40487143.7954,3589482.6455],[40487143.6354,3589481.9255],[40487142.6054,3589481.0455],[40487141.8854,3589480.8655],[40487140.7054,3589479.9655],[40487140.6754,3589479.5055],[40487140.8354,3589478.7855],[40487143.7654,3589475.4054],[40487143.7654,3589474.7254],[40487142.7954,3589473.7454],[40487142.0955,3589472.5654],[40487142.1455,3589471.7654],[40487142.5255,3589470.7054],[40487146.5955,3589466.5954],[40487150.6056,3589462.4054],[40487152.0956,3589460.4654],[40487152.9456,3589459.1054],[40487153.1557,3589457.6654],[40487152.7357,3589455.6754],[40487153.2357,3589451.1453],[40487153.4557,3589449.0053],[40487152.9958,3589445.8653],[40487152.6158,3589443.6353],[40487151.5058,3589441.3653],[40487149.9458,3589439.7152],[40487147.4558,3589436.9652],[40487145.6358,3589433.9352],[40487144.6658,3589430.9151],[40487143.3858,3589425.5051],[40487142.5159,3589422.315],[40487139.4959,3589413.5749],[40487138.3459,3589410.6549],[40487136.1359,3589406.9249],[40487134.6259,3589404.5348],[40487133.446,3589402.6548],[40487131.316,3589398.6548],[40487129.886,3589394.0247],[40487129.616,3589391.2647],[40487129.8161,3589387.2047],[40487129.6161,3589386.6547],[40487128.0069,3589386.323],[40487077.9596,3589376.8425],[40487041.6155,3589368.3538],[40486941.8244,3589340.0011],[40486935.5928,3589338.2337],[40486934.705,3589338.0227],[40486922.0049,3589334.3826],[40486914.5749,3589332.2725],[40486911.7649,3589331.2525],[40486910.8249,3589330.3225],[40486908.8149,3589327.6824],[40486907.9549,3589325.7424],[40486906.615,3589315.7623],[40486904.9114,3589313.9046],[40486904.145,3589313.2923],[40486899.655,3589309.2322],[40486891.2505,3589299.0074],[40486885.278,3589292.4944],[40486882.4752,3589291.2006],[40486881.3615,3589292.1333],[40486878.172,3589305.4978],[40486876.28,3589325.0485],[40486876.2694,3589325.4721],[40486876.3546,3589337.262],[40486877.2945,3589344.3723],[40486877.2985,3589344.4769],[40486877.5269,3589346.222],[40486878.5742,3589378.8326],[40486879.3442,3589381.8726],[40486880.2743,3589384.0126],[40486883.0996,3589387.6473],[40486883.9242,3589388.3327],[40486884.6842,3589389.0727],[40486885.1042,3589390.8627],[40486885.5242,3589393.2327],[40486885.6141,3589395.5028],[40486885.0041,3589398.8328],[40486883.8841,3589403.2128],[40486882.5506,3589407.6333],[40486880.644,3589415.1829],[40486870.8975,3589407.5603],[40486871.1069,3589418.8069],[40486871.1142,3589419.1973],[40486871.1144,3589419.2086],[40486871.1148,3589419.2273],[40486871.1462,3589420.9151],[40486871.1651,3589421.9315],[40486870.5463,3589429.2171],[40486869.8318,3589437.6297],[40486869.045,3589446.8932],[40486832.2975,3589548.0096],[40486831.553,3589547.9203],[40486831.5062,3589548.0463],[40486821.5309,3589547.0691],[40486801.5386,3589545.1105],[40486800.0185,3589544.9616],[40486799.9511,3589545.5242],[40486791.0231,3589545.9702],[40486820.6537,3589797.727],[40486838.2691,3589801.2273],[40486839.0042,3589801.3734],[40486839.6474,3589801.5084],[40486912.685,3589816.8375],[40486916.5899,3589817.657],[40486918.4603,3589818.0496]],[[40487435.2442,3589586.2454],[40487433.3699,3589571.2246],[40487404.8785,3589342.9166],[40487399.1588,3589297.0762],[40487392.8909,3589246.8372],[40487388.8191,3589247.2657],[40487387.2591,3589247.3757],[40487386.1991,3589247.4457],[40487379.9691,3589247.5756],[40487381.5092,3589237.6156],[40487382.5192,3589229.3355],[40487388.9193,3589228.8956],[40487390.6564,3589228.9273],[40487381.5275,3589155.757],[40487367.8921,3589046.4817],[40487367.5406,3589046.694],[40487365.9306,3589047.404],[40487362.2005,3589048.374],[40487357.2205,3589048.484],[40487352.8605,3589048.2139],[40487351.0705,3589047.8439],[40487348.6904,3589047.9439],[40487346.1004,3589048.8139],[40487337.2003,3589054.2539],[40487334.1003,3589055.5638],[40487334.0103,3589055.6038],[40487331.8902,3589056.3038],[40487326.7102,3589056.4738],[40487323.7102,3589056.2238],[40487323.5202,3589056.2038],[40487320.4002,3589056.1037],[40487318.1101,3589056.6437],[40487314.4501,3589058.6237],[40487310.28,3589062.6237],[40487306.56,3589065.8937],[40487306.46,3589065.9837],[40487304.0699,3589067.9337],[40487301.8199,3589069.3737],[40487298.8899,3589070.2637],[40487296.5099,3589071.2236],[40487290.8884,3589090.6696],[40487301.3647,3589103.5457],[40487257.5237,3589224.5036],[40487144.0051,3589186.1877],[40487140.3725,3589228.9178],[40487142.4174,3589229.7536],[40487138.2471,3589259.8838],[40487138.3771,3589260.6138],[40487152.8272,3589260.7639],[40487159.9573,3589261.034],[40487163.8573,3589261.134],[40487187.1375,3589258.4442],[40487208.3177,3589256.1643],[40487215.2378,3589255.2844],[40487216.8378,3589255.0844],[40487225.4779,3589253.2044],[40487227.4379,3589252.6545],[40487228.3979,3589252.3945],[40487229.8696,3589251.991],[40487231.4979,3589251.5445],[40487233.8079,3589250.8845],[40487242.108,3589248.2745],[40487245.028,3589247.3546],[40487247.2481,3589248.9346],[40487247.588,3589249.6546],[40487248.828,3589251.6246],[40487249.198,3589253.9046],[40487249.348,3589255.3046],[40487249.4514,3589256.2716],[40487249.548,3589257.1747],[40487249.838,3589259.7947],[40487249.8379,3589265.2947],[40487249.8379,3589267.2547],[40487249.7379,3589269.0348],[40487248.2279,3589269.9348],[40487240.2978,3589271.9847],[40487229.6377,3589273.6646],[40487222.1777,3589274.7446],[40487219.0575,3589275.1321],[40487201.6475,3589277.2944],[40487190.7971,3589278.1416],[40487189.0974,3589278.2743],[40487168.8272,3589278.9042],[40487152.6671,3589278.804],[40487149.8971,3589278.724],[40487137.647,3589278.3739],[40487136.577,3589278.7439],[40487135.6669,3589284.5839],[40487135.5553,3589285.5817],[40487132.4666,3589321.9142],[40487136.1965,3589334.5142],[40487136.3265,3589335.3443],[40487136.4665,3589336.2343],[40487137.2765,3589342.6444],[40487137.4465,3589344.3244],[40487139.7564,3589350.3745],[40487141.5464,3589355.8145],[40487145.0063,3589364.6946],[40487146.1166,3589368.0524],[40487150.1719,3589378.1176],[40487149.4072,3589378.4007],[40487150.0763,3589380.6148],[40487153.6362,3589391.7049],[40487155.8561,3589400.175],[40487157.7661,3589404.475],[40487161.1461,3589411.6251],[40487163.1461,3589415.3552],[40487166.0361,3589419.7252],[40487168.686,3589423.4053],[40487169.196,3589424.5653],[40487169.196,3589427.8953],[40487169.366,3589435.4554],[40487169.0959,3589438.6554],[40487168.2859,3589443.6454],[40487167.9458,3589448.2654],[40487167.7458,3589450.6755],[40487169.9958,3589451.8155],[40487172.9358,3589452.6855],[40487176.9259,3589452.9555],[40487179.3059,3589453.5056],[40487179.8959,3589453.6356],[40487181.0759,3589453.9656],[40487186.6959,3589455.8156],[40487188.3459,3589456.4657],[40487190.4959,3589456.6057],[40487192.816,3589456.5057],[40487193.796,3589457.0757],[40487194.006,3589457.9657],[40487193.966,3589458.8057],[40487193.5059,3589459.6557],[40487192.3559,3589460.5457],[40487191.0859,3589460.9657],[40487229.3662,3589465.3761],[40487251.7463,3589467.9563],[40487251.5863,3589468.7263],[40487251.4463,3589470.1963],[40487250.7363,3589470.1163],[40487239.8562,3589468.8462],[40487237.9162,3589468.6262],[40487224.3361,3589467.026],[40487211.666,3589465.5459],[40487198.3659,3589463.9758],[40487185.0559,3589462.4157],[40487184.2259,3589462.3257],[40487184.3059,3589461.2257],[40487184.3559,3589460.5957],[40487180.5058,3589460.4156],[40487170.4658,3589460.6756],[40487169.1558,3589460.3355],[40487168.4058,3589459.1355],[40487168.0958,3589458.6455],[40487166.7858,3589457.9155],[40487164.6357,3589457.4855],[40487163.9857,3589457.0955],[40487162.7657,3589456.3555],[40487161.0757,3589456.0554],[40487159.2057,3589456.8154],[40487157.8857,3589458.4654],[40487157.6457,3589459.7754],[40487157.9357,3589462.4055],[40487158.9656,3589466.1755],[40487159.8456,3589468.3755],[40487160.1356,3589469.4755],[40487159.8556,3589469.8955],[40487159.5056,3589470.3055],[40487158.2356,3589470.3455],[40487157.0756,3589471.1455],[40487156.5256,3589472.6155],[40487156.2655,3589475.9556],[40487155.3155,3589484.3856],[40487154.4454,3589489.6956],[40487153.5354,3589494.3057],[40487153.2853,3589497.1757],[40487153.0753,3589505.7158],[40487152.5352,3589509.6758],[40487151.9952,3589510.9158],[40487151.3952,3589511.4858],[40487150.5652,3589511.5558],[40487149.4752,3589511.3858],[40487148.6452,3589511.6158],[40487147.5852,3589512.6058],[40487146.8852,3589513.8458],[40487146.7251,3589516.2158],[40487146.8051,3589517.1058],[40487146.9751,3589517.5258],[40487148.4151,3589518.5758],[40487148.6251,3589518.9658],[40487148.4451,3589519.6858],[40487147.1251,3589524.0458],[40487146.315,3589528.4259],[40487146.235,3589529.4059],[40487146.445,3589530.1259],[40487146.915,3589530.8859],[40487147.915,3589532.0059],[40487148.725,3589532.8059],[40487149.535,3589534.3759],[40487150.045,3589536.576],[40487149.955,3589537.446],[40487149.705,3589537.966],[40487148.455,3589539.076],[40487147.6249,3589539.796],[40487146.9549,3589540.956],[40487146.8549,3589542.366],[40487147.3549,3589544.366],[40487148.4749,3589546.446],[40487148.9649,3589548.296],[40487148.9649,3589550.1761],[40487152.7849,3589549.5161],[40487155.4449,3589549.0461],[40487156.6349,3589548.6261],[40487158.295,3589548.0361],[40487160.435,3589546.8461],[40487160.725,3589548.6861],[40487161.355,3589550.0562],[40487162.185,3589551.1062],[40487163.335,3589551.8862],[40487165.895,3589552.6062],[40487167.865,3589552.9262],[40487169.855,3589553.0162],[40487170.145,3589555.3863],[40487170.175,3589557.9063],[40487171.875,3589559.6863],[40487172.805,3589560.2463],[40487177.745,3589561.5064],[40487179.635,3589561.8664],[40487185.005,3589562.8964],[40487196.3351,3589565.0765],[40487219.3853,3589567.9868],[40487227.9853,3589569.6868],[40487244.7654,3589572.257],[40487247.1354,3589572.257],[40487250.2755,3589572.257],[40487259.3655,3589572.3571],[40487259.6655,3589572.3671],[40487261.1055,3589572.4371],[40487261.4855,3589572.9471],[40487261.6355,3589574.0071],[40487262.4655,3589575.0172],[40487267.7956,3589575.6972],[40487274.6156,3589576.3473],[40487284.6557,3589577.2974],[40487289.2757,3589577.8374],[40487290.9857,3589578.0374],[40487299.2858,3589578.9975],[40487304.7458,3589579.4275],[40487314.2759,3589580.1876],[40487316.0959,3589580.3176],[40487325.1559,3589581.0277],[40487326.716,3589580.0277],[40487327.526,3589579.4977],[40487329.596,3589577.9677],[40487331.646,3589577.4777],[40487334.086,3589577.5678],[40487340.5761,3589578.7778],[40487342.1761,3589579.0778],[40487348.0861,3589580.7079],[40487351.1061,3589581.5379],[40487353.3962,3589581.6179],[40487356.0762,3589581.238],[40487357.2062,3589581.078],[40487360.5962,3589581.158],[40487364.5262,3589581.288],[40487367.9563,3589582.2281],[40487369.2663,3589582.4681],[40487369.6263,3589582.2481],[40487370.3263,3589581.8281],[40487371.0863,3589580.2581],[40487371.4763,3589580.0581],[40487371.9263,3589580.1181],[40487372.9863,3589580.2581],[40487376.7563,3589581.0281],[40487383.4264,3589581.8482],[40487386.8364,3589582.2582],[40487393.6465,3589582.4183],[40487395.8865,3589582.1683],[40487398.6465,3589581.4483],[40487400.8365,3589581.4483],[40487401.6465,3589581.4983],[40487403.7265,3589581.6184],[40487405.1265,3589581.6984],[40487412.9166,3589583.3184],[40487414.7266,3589583.7385],[40487420.7066,3589585.1385],[40487424.9667,3589585.6386],[40487429.2967,3589585.6086],[40487430.6867,3589585.6086],[40487431.8967,3589585.8186],[40487433.8267,3589586.1586],[40487435.2442,3589586.2454]],[[40487444.5068,3589660.4762],[40487437.3904,3589603.4453],[40487425.5665,3589602.9987],[40487412.0864,3589602.4986],[40487398.7963,3589601.9985],[40487384.7662,3589601.4683],[40487369.0561,3589600.8782],[40487355.306,3589600.3681],[40487343.1159,3589598.788],[40487341.5359,3589599.768],[40487335.1054,3589655.2183],[40487327.9854,3589654.2883],[40487327.7453,3589657.8283],[40487327.2053,3589665.7484],[40487374.2356,3589663.7787],[40487389.6058,3589663.1288],[40487393.8958,3589662.9189],[40487403.0559,3589662.479],[40487417.186,3589661.7991],[40487431.4261,3589661.1092],[40487444.5068,3589660.4762]]],\"type\":\"Polygon\"},\"type\":\"Feature\"},{\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMJ\":125397,\"PWH\":null,\"BZ\":null,\"XMMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"XMLX\":null,\"XMBH\":\"20160024\"},\"geometry\":{\"coordinates\":[[[40487638.618,3589614.3305],[40487641.2881,3589613.8905],[40487647.6481,3589613.6806],[40487649.3081,3589613.4406],[40487650.2681,3589613.3006],[40487654.7982,3589612.2906],[40487661.1882,3589611.5407],[40487665.8683,3589610.6507],[40487667.2983,3589610.3707],[40487673.6983,3589609.1407],[40487681.9684,3589607.8408],[40487683.3231,3589607.6994],[40487627.7202,3589334.1183],[40487620.5972,3589299.0773],[40487619.7292,3589306.0483],[40487608.8896,3589393.0986],[40487604.7596,3589391.6386],[40487577.7294,3589387.9983],[40487577.3695,3589373.7682],[40487593.1906,3589251.9874],[40487588.5606,3589250.9174],[40487584.5406,3589250.5273],[40487582.6206,3589250.3373],[40487574.1505,3589250.1172],[40487571.1605,3589249.8072],[40487566.0904,3589249.2772],[40487563.5004,3589249.0071],[40487551.6803,3589247.727],[40487544.2403,3589246.917],[40487538.3103,3589246.2869],[40487529.5102,3589245.3468],[40487527.5602,3589245.1468],[40487523.2702,3589245.0868],[40487519.7601,3589245.0568],[40487509.7801,3589244.9467],[40487507.86,3589244.9267],[40487503.91,3589245.1466],[40487500.38,3589245.6466],[40487497.7,3589246.0266],[40487486.2399,3589247.0765],[40487480.8398,3589247.5765],[40487474.3898,3589247.9064],[40487455.6696,3589248.8363],[40487451.1396,3589248.8362],[40487449.8296,3589248.6362],[40487446.2696,3589247.8262],[40487445.6896,3589247.6962],[40487437.8795,3589246.4161],[40487433.8795,3589246.0361],[40487433.5595,3589246.0161],[40487428.8694,3589245.666],[40487422.9494,3589245.156],[40487421.4694,3589245.016],[40487417.6494,3589244.6559],[40487414.5193,3589244.3959],[40487412.4993,3589244.3959],[40487410.0893,3589244.5659],[40487407.8293,3589244.7259],[40487397.5492,3589246.2258],[40487394.7092,3589246.6458],[40487392.8909,3589246.8372],[40487399.1588,3589297.0762],[40487404.8785,3589342.9166],[40487433.3699,3589571.2246],[40487435.2443,3589586.2454],[40487438.5668,3589586.4487],[40487441.8068,3589586.5387],[40487443.4468,3589586.5787],[40487445.5868,3589586.5387],[40487447.5368,3589586.9987],[40487450.7568,3589587.8488],[40487452.4869,3589587.8488],[40487453.9169,3589587.4688],[40487455.5069,3589587.0188],[40487457.2669,3589587.0788],[40487464.5069,3589587.5989],[40487471.037,3589587.7589],[40487473.697,3589587.299],[40487475.057,3589587.079],[40487476.547,3589586.719],[40487479.9471,3589586.549],[40487482.4271,3589586.419],[40487485.8971,3589586.3891],[40487489.1371,3589586.3691],[40487493.2572,3589586.0191],[40487495.8672,3589585.5591],[40487498.7772,3589585.0391],[40487503.6673,3589584.7192],[40487505.2773,3589584.5992],[40487507.4973,3589584.4192],[40487509.5173,3589584.2492],[40487510.7873,3589584.1492],[40487517.3174,3589584.0593],[40487523.0574,3589583.9593],[40487529.0475,3589583.5394],[40487532.4575,3589583.6694],[40487538.8275,3589583.9595],[40487542.1176,3589583.9595],[40487547.7576,3589583.9595],[40487551.0076,3589584.1596],[40487555.4977,3589584.4196],[40487562.2677,3589584.9397],[40487564.2177,3589585.0697],[40487571.2878,3589585.5297],[40487575.8578,3589585.6198],[40487584.9279,3589585.7498],[40487589.2479,3589585.8799],[40487592.5179,3589585.7999],[40487593.5479,3589584.9799],[40487593.8979,3589584.3699],[40487593.998,3589583.0399],[40487592.708,3589574.6398],[40487591.818,3589570.8998],[40487591.418,3589570.4098],[40487589.868,3589569.6898],[40487588.898,3589569.1098],[40487588.488,3589568.4897],[40487588.358,3589567.4397],[40487589.2481,3589558.5797],[40487588.3081,3589554.5496],[40487588.2182,3589551.0396],[40487588.4382,3589544.9496],[40487588.0083,3589539.9995],[40487587.2483,3589536.1895],[40487586.9783,3589533.2195],[40487585.2783,3589528.6794],[40487583.8183,3589525.2494],[40487582.7483,3589524.2794],[40487581.2883,3589523.6094],[40487576.5283,3589522.5493],[40487574.7383,3589522.0693],[40487572.3083,3589521.4193],[40487567.7283,3589519.7392],[40487563.2383,3589517.0392],[40487561.8983,3589515.6391],[40487563.1083,3589506.5391],[40487564.8884,3589505.3991],[40487566.2384,3589504.9891],[40487566.7753,3589499.2532],[40487570.5385,3589499.5891],[40487575.7185,3589500.0491],[40487581.4685,3589500.1392],[40487583.7385,3589499.6992],[40487585.0086,3589498.7592],[40487585.7686,3589497.5692],[40487586.5786,3589495.0492],[40487588.2587,3589479.5591],[40487591.279,3589455.9989],[40487598.3495,3589400.2186],[40487602.1395,3589398.8786],[40487604.6495,3589398.9786],[40487604.5695,3589400.1286],[40487597.679,3589455.899],[40487592.9287,3589492.3592],[40487597.0987,3589493.8693],[40487599.5687,3589494.7593],[40487602.7587,3589495.6493],[40487602.4387,3589498.9393],[40487602.3187,3589500.1393],[40487602.1287,3589502.0394],[40487601.8686,3589516.3995],[40487601.9985,3589522.5695],[40487603.2384,3589536.8896],[40487604.5683,3589550.5397],[40487605.4183,3589556.7698],[40487607.0682,3589561.7398],[40487609.2082,3589565.7899],[40487609.5882,3589567.8399],[40487610.6181,3589575.57],[40487611.0981,3589579.79],[40487611.678,3589592.2801],[40487612.308,3589598.6002],[40487612.478,3589599.8102],[40487613.1079,3589604.3802],[40487613.4179,3589612.1603],[40487613.6379,3589613.8903],[40487614.3979,3589614.3803],[40487615.7379,3589614.4503],[40487621.2879,3589614.1004],[40487622.9279,3589614.1804],[40487625.1679,3589614.2904],[40487631.328,3589615.3004],[40487633.868,3589615.3005],[40487635.738,3589615.1005],[40487636.618,3589615.0005],[40487638.618,3589614.3305]],[[40487594.0507,3589245.4174],[40487600.117,3589198.3262],[40487590.3314,3589150.1866],[40487589.1213,3589138.4636],[40487576.6791,3589017.9233],[40487573.7324,3589018.9055],[40487570.2623,3589019.1555],[40487567.1623,3589018.8254],[40487562.6123,3589017.7754],[40487559.1423,3589017.8554],[40487556.7122,3589017.9554],[40487550.3922,3589018.2053],[40487550.0122,3589020.9353],[40487538.5621,3589023.5452],[40487537.152,3589024.3852],[40487536.712,3589024.9652],[40487534.922,3589025.3152],[40487530.392,3589024.6952],[40487529.942,3589024.3852],[40487529.992,3589020.7452],[40487529.812,3589018.5051],[40487529.022,3589018.1551],[40487522.302,3589017.8051],[40487519.362,3589018.2551],[40487513.5819,3589019.045],[40487502.1618,3589019.9249],[40487498.5618,3589020.2049],[40487491.5417,3589020.6948],[40487490.1617,3589021.4448],[40487489.4617,3589022.8748],[40487488.5015,3589042.975],[40487488.4515,3589044.125],[40487485.1015,3589042.655],[40487479.3415,3589038.6749],[40487479.5415,3589036.5549],[40487479.4315,3589033.1748],[40487478.8616,3589027.9548],[40487478.2916,3589025.8048],[40487477.7416,3589025.2248],[40487477.1416,3589025.2248],[40487472.2515,3589026.3847],[40487467.9415,3589026.8247],[40487463.6915,3589026.7647],[40487460.1414,3589026.7446],[40487459.7714,3589027.2546],[40487459.7014,3589031.3447],[40487459.3314,3589031.8047],[40487453.7614,3589032.3846],[40487445.3213,3589032.8246],[40487438.6412,3589033.7345],[40487434.5712,3589034.4445],[40487422.9811,3589035.7544],[40487412.491,3589036.2843],[40487398.6509,3589035.6142],[40487390.8209,3589035.9441],[40487389.0308,3589036.5641],[40487386.4608,3589037.4541],[40487384.0308,3589038.3841],[40487380.6807,3589039.6841],[40487374.5407,3589042.7441],[40487371.8106,3589044.1141],[40487367.8931,3589046.4811],[40487381.5275,3589155.757],[40487390.6564,3589228.9273],[40487391.1093,3589228.9356],[40487402.8394,3589229.1357],[40487417.8595,3589229.3858],[40487428.7096,3589229.5759],[40487431.9196,3589229.9959],[40487433.5296,3589230.216],[40487444.3697,3589230.216],[40487447.8797,3589229.6161],[40487449.6997,3589228.5161],[40487450.8098,3589226.4061],[40487453.13,3589203.3759],[40487455.7196,3589177.0582],[40487455.8802,3589175.4257],[40487457.4903,3589168.6557],[40487457.7003,3589166.5457],[40487457.4103,3589164.5157],[40487457.4103,3589164.0856],[40487459.1904,3589160.9456],[40487460.0804,3589159.8856],[40487461.2504,3589153.8456],[40487468.1808,3589120.6454],[40487468.3036,3589119.7529],[40487477.3252,3589120.9938],[40487477.2108,3589121.8255],[40487476.8908,3589124.0055],[40487476.6407,3589131.9856],[40487477.1807,3589136.8556],[40487477.2307,3589138.7156],[40487477.2607,3589139.6958],[40487476.8106,3589143.2158],[40487476.8906,3589150.5858],[40487476.6605,3589154.4557],[40487475.9505,3589157.5958],[40487473.6704,3589162.9258],[40487472.6904,3589164.8158],[40487472.0004,3589166.1358],[40487471.2004,3589169.576],[40487469.5002,3589177.0582],[40487469.0803,3589178.906],[40487465.1301,3589191.9959],[40487463.5301,3589199.4362],[40487462.14,3589207.766],[40487461.4499,3589215.1061],[40487461.0499,3589223.6061],[40487460.5998,3589230.2563],[40487460.3798,3589231.2764],[40487460.7598,3589231.8562],[40487461.6798,3589232.3663],[40487467.6198,3589232.2262],[40487473.0199,3589232.0963],[40487481.14,3589230.9863],[40487493.26,3589231.1264],[40487497.5001,3589231.3665],[40487503.6301,3589231.7065],[40487513.2702,3589232.7267],[40487515.6002,3589233.1067],[40487521.3002,3589234.0467],[40487525.2403,3589234.7868],[40487531.2803,3589236.0469],[40487532.9703,3589236.4068],[40487534.5903,3589236.7268],[40487542.4404,3589238.3271],[40487544.0704,3589238.6569],[40487550.0904,3589239.497],[40487555.6504,3589240.267],[40487561.3705,3589241.0672],[40487564.3105,3589241.4671],[40487573.5906,3589242.7372],[40487576.2806,3589243.1072],[40487580.6106,3589243.6673],[40487594.0507,3589245.4174]],[[40487604.144,3589654.8332],[40487602.6678,3589604.4901],[40487590.4377,3589604.78],[40487577.7277,3589605.0599],[40487564.0475,3589605.3798],[40487550.0374,3589604.4197],[40487539.1474,3589603.5996],[40487535.6773,3589603.5996],[40487520.5672,3589604.4195],[40487512.4772,3589604.3094],[40487511.2872,3589603.8994],[40487509.5672,3589603.7094],[40487508.3571,3589603.8494],[40487507.7071,3589604.6794],[40487506.9471,3589604.9894],[40487505.9571,3589605.0494],[40487497.577,3589605.5793],[40487493.277,3589605.9593],[40487479.5169,3589605.3391],[40487466.0768,3589604.659],[40487455.4467,3589604.1289],[40487452.9567,3589604.0289],[40487439.3366,3589603.5188],[40487437.3906,3589603.4453],[40487444.497,3589660.3926],[40487450.4937,3589660.1837],[40487455.2863,3589659.9494],[40487458.6963,3589659.8094],[40487472.0364,3589659.3595],[40487485.3065,3589658.9196],[40487497.6266,3589658.5097],[40487499.4466,3589658.4497],[40487512.5767,3589658.0098],[40487522.797,3589657.6659],[40487604.144,3589654.8332]],[[40487691.5065,3589651.791],[40487687.2181,3589627.4843],[40487681.8682,3589628.161],[40487680.4682,3589629.0009],[40487678.9982,3589630.0909],[40487671.0482,3589629.8809],[40487670.9281,3589630.9909],[40487670.4781,3589631.6309],[40487669.9281,3589632.0109],[40487665.6281,3589632.0508],[40487656.738,3589631.1508],[40487644.0279,3589631.2307],[40487636.0179,3589631.6906],[40487633.9779,3589632.1006],[40487632.2878,3589632.2106],[40487625.6478,3589631.6005],[40487621.7678,3589631.6305],[40487618.8777,3589631.8905],[40487617.7277,3589632.4105],[40487617.0977,3589633.2305],[40487616.8777,3589633.7805],[40487617.1677,3589637.4505],[40487617.5576,3589644.8606],[40487618.7505,3589654.3246],[40487691.5065,3589651.791]],[[40487335.6106,3589019.1536],[40487340.6906,3589018.8736],[40487342.2006,3589019.4136],[40487344.4206,3589020.6537],[40487346.1006,3589020.7237],[40487351.8907,3589019.0437],[40487357.5708,3589017.7037],[40487362.3608,3589017.4338],[40487364.1308,3589016.8038],[40487364.1892,3589016.7956],[40487368.9509,3589016.1238],[40487372.0709,3589016.2838],[40487375.8709,3589016.0239],[40487382.731,3589014.8839],[40487385.851,3589013.9739],[40487389.581,3589013.704],[40487394.5411,3589013.804],[40487398.9911,3589013.264],[40487406.5512,3589011.8541],[40487412.3312,3589010.7841],[40487413.438,3589010.4332],[40487363.7732,3589013.462],[40487312.9081,3589016.564],[40487313.2104,3589018.3434],[40487313.2104,3589023.7534],[40487314.0104,3589024.5534],[40487317.8404,3589025.4535],[40487321.6304,3589025.2535],[40487325.2005,3589024.6535],[40487328.2305,3589022.7035],[40487330.4405,3589020.7235],[40487332.0605,3589019.8536],[40487335.6106,3589019.1536]]],\"type\":\"Polygon\"},\"type\":\"Feature\"}],\"type\":\"FeatureCollection\"}";
		String geojson8 = "{\"features\":[{\"geometry\":{\"coordinates\":[[[40486820.65,3589797.73],[40486838.27,3589801.23],[40486839,3589801.37],[40486839.65,3589801.51],[40486912.69,3589816.84],[40486916.59,3589817.66],[40486918.46,3589818.05],[40486907.42,3589722.78],[40486907.48,3589722.77],[40486907.34,3589721.59],[40486889.66,3589722.24],[40486863.06,3589723.2],[40486875.11,3589683.51],[40486887.65,3589682.99],[40486900.19,3589682.48],[40486949.53,3589680.45],[40486955.29,3589679.97],[40486979.57,3589679.13],[40486993.95,3589678.62],[40487001.99,3589678.34],[40487011.2,3589678.01],[40487027.96,3589677.42],[40487044.3,3589676.84],[40487056.36,3589676.42],[40487070.99,3589675.84],[40487081.47,3589675.42],[40487093.06,3589674.96],[40487102.49,3589674.58],[40487110.74,3589674.24],[40487122.26,3589673.79],[40487127.51,3589673.58],[40487145.95,3589672.85],[40487146.94,3589672.81],[40487148.14,3589672.76],[40487151.38,3589672.65],[40487166.74,3589672.03],[40487168.68,3589671.96],[40487182.14,3589671.43],[40487188.84,3589671.16],[40487197.56,3589670.82],[40487205.95,3589670.49],[40487211.95,3589670.26],[40487214.72,3589670.15],[40487229.19,3589669.58],[40487245.96,3589668.93],[40487249.09,3589668.81],[40487266.94,3589668.12],[40487284.11,3589667.45],[40487286.87,3589667.35],[40487315.21,3589666.26],[40487317.88,3589638.33],[40487318.45,3589635.23],[40487319.17,3589627.85],[40487321.3,3589611.13],[40487321.75,3589603.52],[40487321.15,3589598.32],[40487320.54,3589593.75],[40487320.44,3589589.99],[40487320.32,3589588.64],[40487320.1,3589588.2],[40487319.34,3589587.97],[40487317.69,3589587.76],[40487315.53,3589587.35],[40487295.98,3589584.57],[40487293.04,3589584.15],[40487283.52,3589582.85],[40487274.37,3589582.32],[40487273.02,3589582.19],[40487270.48,3589581.53],[40487258.93,3589579.01],[40487256.4,3589578.46],[40487240.15,3589576.02],[40487234.64,3589575.11],[40487229.54,3589574.66],[40487225.27,3589574.26],[40487222.4,3589573.65],[40487217.19,3589572.91],[40487206.17,3589571.84],[40487193.28,3589571.19],[40487193.28,3589570.15],[40487192.62,3589569.75],[40487182.88,3589568.99],[40487182.84,3589569.97],[40487182.26,3589571.37],[40487182.13,3589572.54],[40487181.58,3589573.64],[40487181.07,3589574.06],[40487180.51,3589574.15],[40487178.74,3589573.81],[40487172.77,3589572.89],[40487172.04,3589572.89],[40487171.47,3589573.3],[40487171.36,3589573.99],[40487170.94,3589575.06],[40487170.56,3589575.33],[40487169.38,3589575.37],[40487168.47,3589574.84],[40487160.24,3589574.99],[40487159.2,3589575.28],[40487158.31,3589576.08],[40487157.19,3589579.09],[40487155.62,3589582.48],[40487155.31,3589582.9],[40487155.04,3589582.94],[40487154.82,3589582.62],[40487153.96,3589579.14],[40487152.07,3589577.06],[40487151.18,3589576.5],[40487149.22,3589575.92],[40487140.43,3589574.41],[40487131.27,3589572.63],[40487125.42,3589572.07],[40487112.21,3589570.81],[40487101.27,3589569.49],[40487087.49,3589568.6],[40487069.48,3589566.98],[40487057.08,3589566.45],[40487056.42,3589569.25],[40487054.96,3589570.7],[40487053.01,3589571.6],[40487050.81,3589572.22],[40487048.7,3589572.39],[40487045.59,3589572.08],[40487043.32,3589571.57],[40487040.76,3589570.64],[40487039.43,3589569.98],[40487036.68,3589569.24],[40487033.65,3589568.77],[40487030.34,3589568.67],[40487027.32,3589568.28],[40487021.56,3589566.83],[40487016.74,3589565.72],[40487013.9,3589565.13],[40487009.29,3589563.56],[40487007.58,3589563.04],[40487005.34,3589562.59],[40487002.99,3589562.41],[40487000.16,3589562.51],[40486993.64,3589562.3],[40486987.24,3589561.65],[40486984.82,3589561.54],[40486982.06,3589561.96],[40486980.09,3589562.65],[40486977.42,3589563.09],[40486975.78,3589563.29],[40486974.36,3589563.18],[40486970.04,3589561.93],[40486968.89,3589561.82],[40486968.16,3589561.98],[40486966.88,3589563.01],[40486965.56,3589564.07],[40486964.14,3589564.87],[40486963.02,3589565.22],[40486962.04,3589565.24],[40486961.57,3589564.85],[40486961.07,3589563.69],[40486960.49,3589563.27],[40486959.78,3589563.27],[40486958.8,3589563.54],[40486957.2,3589563.89],[40486955.4,3589564.05],[40486953.09,3589563.78],[40486948.01,3589563.19],[40486930.04,3589561.11],[40486920.03,3589560.46],[40486901.25,3589560.44],[40486861.71,3589559.24],[40486868.38,3589533.38],[40486918.41,3589532.16],[40486955.36,3589534.27],[40486977.43,3589534.94],[40487033.75,3589539.08],[40487063.08,3589542.33],[40487076.87,3589544.3],[40487079.03,3589545.05],[40487080.76,3589545.5],[40487092.51,3589545.72],[40487108.81,3589546.67],[40487113.96,3589547.32],[40487115.92,3589547.36],[40487117.12,3589547.31],[40487126.06,3589546.96],[40487129.08,3589546.79],[40487137.55,3589549.52],[40487139.24,3589550.17],[40487139.6,3589548.85],[40487140.54,3589545.46],[40487141.26,3589541.06],[40487142.02,3589534.25],[40487142.02,3589527.66],[40487142.45,3589521.84],[40487142.66,3589520.84],[40487142.2,3589519.66],[40487142.1,3589518.38],[40487142.1,3589517.4],[40487142.61,3589515.46],[40487142.41,3589515.11],[40487140.42,3589513.05],[40487139.4,3589511.3],[40487139.01,3589510.09],[40487139.06,3589509.19],[40487139.45,3589508.68],[40487140.45,3589508.18],[40487141.99,3589508.09],[40487142.61,3589507.93],[40487142.87,3589507.28],[40487143.64,3589501.11],[40487144.13,3589498.05],[40487146.77,3589489],[40487146.77,3589488.02],[40487146.6,3589487.38],[40487144.23,3589485.91],[40487142.95,3589484.89],[40487142.82,3589484.34],[40487143.51,3589483.37],[40487143.8,3589482.65],[40487143.64,3589481.93],[40487142.61,3589481.05],[40487141.89,3589480.87],[40487140.71,3589479.97],[40487140.68,3589479.51],[40487140.84,3589478.79],[40487143.77,3589475.41],[40487143.77,3589474.73],[40487142.8,3589473.75],[40487142.1,3589472.57],[40487142.15,3589471.77],[40487142.53,3589470.71],[40487146.6,3589466.6],[40487150.61,3589462.41],[40487152.1,3589460.47],[40487152.95,3589459.11],[40487153.16,3589457.67],[40487152.74,3589455.68],[40487153.24,3589451.15],[40487153.46,3589449.01],[40487153,3589445.87],[40487152.62,3589443.64],[40487151.51,3589441.37],[40487149.95,3589439.72],[40487147.46,3589436.97],[40487145.64,3589433.94],[40487144.67,3589430.92],[40487143.39,3589425.51],[40487142.52,3589422.32],[40487139.5,3589413.57],[40487138.35,3589410.65],[40487136.14,3589406.92],[40487134.63,3589404.53],[40487133.45,3589402.65],[40487131.32,3589398.65],[40487129.89,3589394.02],[40487129.62,3589391.26],[40487129.82,3589387.2],[40487129.62,3589386.65],[40487128.01,3589386.32],[40487077.96,3589376.84],[40487041.62,3589368.35],[40486941.82,3589340],[40486935.59,3589338.23],[40486934.71,3589338.02],[40486922,3589334.38],[40486914.57,3589332.27],[40486911.76,3589331.25],[40486910.82,3589330.32],[40486908.81,3589327.68],[40486907.95,3589325.74],[40486906.62,3589315.76],[40486904.91,3589313.9],[40486904.15,3589313.29],[40486899.66,3589309.23],[40486891.25,3589299.01],[40486885.28,3589292.49],[40486882.48,3589291.2],[40486881.36,3589292.13],[40486878.17,3589305.5],[40486876.28,3589325.05],[40486876.27,3589325.47],[40486876.35,3589337.26],[40486877.29,3589344.37],[40486877.3,3589344.48],[40486877.53,3589346.22],[40486878.57,3589378.83],[40486879.34,3589381.87],[40486880.27,3589384.01],[40486883.1,3589387.65],[40486883.92,3589388.33],[40486884.68,3589389.07],[40486885.1,3589390.86],[40486885.52,3589393.23],[40486885.61,3589395.5],[40486885,3589398.83],[40486883.88,3589403.21],[40486882.55,3589407.63],[40486880.64,3589415.18],[40486870.9,3589407.56],[40486871.11,3589418.81],[40486871.11,3589419.2],[40486871.11,3589419.21],[40486871.11,3589419.23],[40486871.15,3589420.92],[40486871.17,3589421.93],[40486870.55,3589429.22],[40486869.83,3589437.63],[40486869.05,3589446.89],[40486832.3,3589548.01],[40486831.55,3589547.92],[40486831.51,3589548.05],[40486821.53,3589547.07],[40486801.54,3589545.11],[40486800.02,3589544.96],[40486799.95,3589545.52],[40486791.02,3589545.97],[40486820.65,3589797.73]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块一\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":23.2611,\"nydArea\":21.4894,\"jsydArea\":1.7717,\"gdArea\":20.0141},\"DKID\":\"054F0954QHQ6R7LH\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487148.96,3589550.18],[40487152.78,3589549.52],[40487155.44,3589549.05],[40487156.63,3589548.63],[40487158.3,3589548.04],[40487160.44,3589546.85],[40487160.73,3589548.69],[40487161.36,3589550.06],[40487162.19,3589551.11],[40487163.34,3589551.89],[40487165.9,3589552.61],[40487167.87,3589552.93],[40487169.86,3589553.02],[40487170.15,3589555.39],[40487170.18,3589557.91],[40487171.88,3589559.69],[40487172.81,3589560.25],[40487177.75,3589561.51],[40487179.64,3589561.87],[40487185.01,3589562.9],[40487196.34,3589565.08],[40487219.39,3589567.99],[40487227.99,3589569.69],[40487244.77,3589572.26],[40487247.14,3589572.26],[40487250.28,3589572.26],[40487259.37,3589572.36],[40487259.67,3589572.37],[40487261.11,3589572.44],[40487261.49,3589572.95],[40487261.64,3589574.01],[40487262.47,3589575.02],[40487267.8,3589575.7],[40487274.62,3589576.35],[40487284.66,3589577.3],[40487289.28,3589577.84],[40487290.99,3589578.04],[40487299.29,3589579],[40487304.75,3589579.43],[40487314.28,3589580.19],[40487316.1,3589580.32],[40487325.16,3589581.03],[40487326.72,3589580.03],[40487327.53,3589579.5],[40487329.6,3589577.97],[40487331.65,3589577.48],[40487334.09,3589577.57],[40487340.58,3589578.78],[40487342.18,3589579.08],[40487348.09,3589580.71],[40487351.11,3589581.54],[40487353.4,3589581.62],[40487356.08,3589581.24],[40487357.21,3589581.08],[40487360.6,3589581.16],[40487364.53,3589581.29],[40487367.96,3589582.23],[40487369.27,3589582.47],[40487369.63,3589582.25],[40487370.33,3589581.83],[40487371.09,3589580.26],[40487371.48,3589580.06],[40487371.93,3589580.12],[40487372.99,3589580.26],[40487376.76,3589581.03],[40487383.43,3589581.85],[40487386.84,3589582.26],[40487393.65,3589582.42],[40487395.89,3589582.17],[40487398.65,3589581.45],[40487400.84,3589581.45],[40487401.65,3589581.5],[40487403.73,3589581.62],[40487405.13,3589581.7],[40487412.92,3589583.32],[40487414.73,3589583.74],[40487420.71,3589585.14],[40487424.97,3589585.64],[40487429.3,3589585.61],[40487430.69,3589585.61],[40487431.9,3589585.82],[40487433.83,3589586.16],[40487435.24,3589586.25],[40487433.37,3589571.22],[40487404.88,3589342.92],[40487399.16,3589297.08],[40487392.89,3589246.84],[40487388.82,3589247.27],[40487387.26,3589247.38],[40487386.2,3589247.45],[40487379.97,3589247.58],[40487381.51,3589237.62],[40487382.52,3589229.34],[40487388.92,3589228.9],[40487390.66,3589228.93],[40487381.53,3589155.76],[40487367.89,3589046.48],[40487367.54,3589046.69],[40487365.93,3589047.4],[40487362.2,3589048.37],[40487357.22,3589048.48],[40487352.86,3589048.21],[40487351.07,3589047.84],[40487348.69,3589047.94],[40487346.1,3589048.81],[40487337.2,3589054.25],[40487334.1,3589055.56],[40487334.01,3589055.6],[40487331.89,3589056.3],[40487326.71,3589056.47],[40487323.71,3589056.22],[40487323.52,3589056.2],[40487320.4,3589056.1],[40487318.11,3589056.64],[40487314.45,3589058.62],[40487310.28,3589062.62],[40487306.56,3589065.89],[40487306.46,3589065.98],[40487304.07,3589067.93],[40487301.82,3589069.37],[40487298.89,3589070.26],[40487296.51,3589071.22],[40487290.89,3589090.67],[40487301.36,3589103.55],[40487257.52,3589224.5],[40487144.01,3589186.19],[40487140.37,3589228.92],[40487142.42,3589229.75],[40487138.25,3589259.88],[40487138.38,3589260.61],[40487152.83,3589260.76],[40487159.96,3589261.03],[40487163.86,3589261.13],[40487187.14,3589258.44],[40487208.32,3589256.16],[40487215.24,3589255.28],[40487216.84,3589255.08],[40487225.48,3589253.2],[40487227.44,3589252.65],[40487228.4,3589252.39],[40487229.87,3589251.99],[40487231.5,3589251.54],[40487233.81,3589250.88],[40487242.11,3589248.27],[40487245.03,3589247.35],[40487247.25,3589248.93],[40487247.59,3589249.65],[40487248.83,3589251.62],[40487249.2,3589253.9],[40487249.35,3589255.3],[40487249.45,3589256.27],[40487249.55,3589257.17],[40487249.84,3589259.79],[40487249.84,3589265.29],[40487249.84,3589267.25],[40487249.74,3589269.03],[40487248.23,3589269.93],[40487240.3,3589271.98],[40487229.64,3589273.66],[40487222.18,3589274.74],[40487219.06,3589275.13],[40487201.65,3589277.29],[40487190.8,3589278.14],[40487189.1,3589278.27],[40487168.83,3589278.9],[40487152.67,3589278.8],[40487149.9,3589278.72],[40487137.65,3589278.37],[40487136.58,3589278.74],[40487135.67,3589284.58],[40487135.56,3589285.58],[40487132.47,3589321.91],[40487136.2,3589334.51],[40487136.33,3589335.34],[40487136.47,3589336.23],[40487137.28,3589342.64],[40487137.45,3589344.32],[40487139.76,3589350.37],[40487141.55,3589355.81],[40487145.01,3589364.69],[40487146.12,3589368.05],[40487150.17,3589378.12],[40487149.41,3589378.4],[40487150.08,3589380.61],[40487153.64,3589391.7],[40487155.86,3589400.18],[40487157.77,3589404.48],[40487161.15,3589411.63],[40487163.15,3589415.36],[40487166.04,3589419.73],[40487168.69,3589423.41],[40487169.2,3589424.57],[40487169.2,3589427.9],[40487169.37,3589435.46],[40487169.1,3589438.66],[40487168.29,3589443.65],[40487167.95,3589448.27],[40487167.75,3589450.68],[40487170,3589451.82],[40487172.94,3589452.69],[40487176.93,3589452.96],[40487179.31,3589453.51],[40487179.9,3589453.64],[40487181.08,3589453.97],[40487186.7,3589455.82],[40487188.35,3589456.47],[40487190.5,3589456.61],[40487192.82,3589456.51],[40487193.8,3589457.08],[40487194.01,3589457.97],[40487193.97,3589458.81],[40487193.51,3589459.66],[40487192.36,3589460.55],[40487191.09,3589460.97],[40487229.37,3589465.38],[40487251.75,3589467.96],[40487251.59,3589468.73],[40487251.45,3589470.2],[40487250.74,3589470.12],[40487239.86,3589468.85],[40487237.92,3589468.63],[40487224.34,3589467.03],[40487211.67,3589465.55],[40487198.37,3589463.98],[40487185.06,3589462.42],[40487184.23,3589462.33],[40487184.31,3589461.23],[40487184.36,3589460.6],[40487180.51,3589460.42],[40487170.47,3589460.68],[40487169.16,3589460.34],[40487168.41,3589459.14],[40487168.1,3589458.65],[40487166.79,3589457.92],[40487164.64,3589457.49],[40487163.99,3589457.1],[40487162.77,3589456.36],[40487161.08,3589456.06],[40487159.21,3589456.82],[40487157.89,3589458.47],[40487157.65,3589459.78],[40487157.94,3589462.41],[40487158.97,3589466.18],[40487159.85,3589468.38],[40487160.14,3589469.48],[40487159.86,3589469.9],[40487159.51,3589470.31],[40487158.24,3589470.35],[40487157.08,3589471.15],[40487156.53,3589472.62],[40487156.27,3589475.96],[40487155.32,3589484.39],[40487154.45,3589489.7],[40487153.54,3589494.31],[40487153.29,3589497.18],[40487153.08,3589505.72],[40487152.54,3589509.68],[40487152,3589510.92],[40487151.4,3589511.49],[40487150.57,3589511.56],[40487149.48,3589511.39],[40487148.65,3589511.62],[40487147.59,3589512.61],[40487146.89,3589513.85],[40487146.73,3589516.22],[40487146.81,3589517.11],[40487146.98,3589517.53],[40487148.42,3589518.58],[40487148.63,3589518.97],[40487148.45,3589519.69],[40487147.13,3589524.05],[40487146.32,3589528.43],[40487146.24,3589529.41],[40487146.45,3589530.13],[40487146.92,3589530.89],[40487147.92,3589532.01],[40487148.73,3589532.81],[40487149.54,3589534.38],[40487150.05,3589536.58],[40487149.96,3589537.45],[40487149.71,3589537.97],[40487148.46,3589539.08],[40487147.62,3589539.8],[40487146.95,3589540.96],[40487146.85,3589542.37],[40487147.35,3589544.37],[40487148.47,3589546.45],[40487148.96,3589548.3],[40487148.96,3589550.18]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块一\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":23.2611,\"nydArea\":21.4894,\"jsydArea\":1.7717,\"gdArea\":20.0141},\"DKID\":\"054F0954QHQ6R7LH\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487327.21,3589665.75],[40487374.24,3589663.78],[40487389.61,3589663.13],[40487393.9,3589662.92],[40487403.06,3589662.48],[40487417.19,3589661.8],[40487431.43,3589661.11],[40487444.51,3589660.48],[40487437.39,3589603.45],[40487425.57,3589603],[40487412.09,3589602.5],[40487398.8,3589602],[40487384.77,3589601.47],[40487369.06,3589600.88],[40487355.31,3589600.37],[40487343.12,3589598.79],[40487341.54,3589599.77],[40487335.11,3589655.22],[40487327.99,3589654.29],[40487327.75,3589657.83],[40487327.21,3589665.75]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块一\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":23.2611,\"nydArea\":21.4894,\"jsydArea\":1.7717,\"gdArea\":20.0141},\"DKID\":\"054F0954QHQ6R7LH\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487435.24,3589586.25],[40487438.57,3589586.45],[40487441.81,3589586.54],[40487443.45,3589586.58],[40487445.59,3589586.54],[40487447.54,3589587],[40487450.76,3589587.85],[40487452.49,3589587.85],[40487453.92,3589587.47],[40487455.51,3589587.02],[40487457.27,3589587.08],[40487464.51,3589587.6],[40487471.04,3589587.76],[40487473.7,3589587.3],[40487475.06,3589587.08],[40487476.55,3589586.72],[40487479.95,3589586.55],[40487482.43,3589586.42],[40487485.9,3589586.39],[40487489.14,3589586.37],[40487493.26,3589586.02],[40487495.87,3589585.56],[40487498.78,3589585.04],[40487503.67,3589584.72],[40487505.28,3589584.6],[40487507.5,3589584.42],[40487509.52,3589584.25],[40487510.79,3589584.15],[40487517.32,3589584.06],[40487523.06,3589583.96],[40487529.05,3589583.54],[40487532.46,3589583.67],[40487538.83,3589583.96],[40487542.12,3589583.96],[40487547.76,3589583.96],[40487551.01,3589584.16],[40487555.5,3589584.42],[40487562.27,3589584.94],[40487564.22,3589585.07],[40487571.29,3589585.53],[40487575.86,3589585.62],[40487584.93,3589585.75],[40487589.25,3589585.88],[40487592.52,3589585.8],[40487593.55,3589584.98],[40487593.9,3589584.37],[40487594,3589583.04],[40487592.71,3589574.64],[40487591.82,3589570.9],[40487591.42,3589570.41],[40487589.87,3589569.69],[40487588.9,3589569.11],[40487588.49,3589568.49],[40487588.36,3589567.44],[40487589.25,3589558.58],[40487588.31,3589554.55],[40487588.22,3589551.04],[40487588.44,3589544.95],[40487588.01,3589540],[40487587.25,3589536.19],[40487586.98,3589533.22],[40487585.28,3589528.68],[40487583.82,3589525.25],[40487582.75,3589524.28],[40487581.29,3589523.61],[40487576.53,3589522.55],[40487574.74,3589522.07],[40487572.31,3589521.42],[40487567.73,3589519.74],[40487563.24,3589517.04],[40487561.9,3589515.64],[40487563.11,3589506.54],[40487564.89,3589505.4],[40487566.24,3589504.99],[40487566.78,3589499.25],[40487570.54,3589499.59],[40487575.72,3589500.05],[40487581.47,3589500.14],[40487583.74,3589499.7],[40487585.01,3589498.76],[40487585.77,3589497.57],[40487586.58,3589495.05],[40487588.26,3589479.56],[40487591.28,3589456],[40487598.35,3589400.22],[40487602.14,3589398.88],[40487604.65,3589398.98],[40487604.57,3589400.13],[40487597.68,3589455.9],[40487592.93,3589492.36],[40487597.1,3589493.87],[40487599.57,3589494.76],[40487602.76,3589495.65],[40487602.44,3589498.94],[40487602.32,3589500.14],[40487602.13,3589502.04],[40487601.87,3589516.4],[40487602,3589522.57],[40487603.24,3589536.89],[40487604.57,3589550.54],[40487605.42,3589556.77],[40487607.07,3589561.74],[40487609.21,3589565.79],[40487609.59,3589567.84],[40487610.62,3589575.57],[40487611.1,3589579.79],[40487611.68,3589592.28],[40487612.31,3589598.6],[40487612.48,3589599.81],[40487613.11,3589604.38],[40487613.42,3589612.16],[40487613.64,3589613.89],[40487614.4,3589614.38],[40487615.74,3589614.45],[40487621.29,3589614.1],[40487622.93,3589614.18],[40487625.17,3589614.29],[40487631.33,3589615.3],[40487633.87,3589615.3],[40487635.74,3589615.1],[40487636.62,3589615],[40487638.62,3589614.33],[40487641.29,3589613.89],[40487647.65,3589613.68],[40487649.31,3589613.44],[40487650.27,3589613.3],[40487654.8,3589612.29],[40487661.19,3589611.54],[40487665.87,3589610.65],[40487667.3,3589610.37],[40487673.7,3589609.14],[40487681.97,3589607.84],[40487683.32,3589607.7],[40487627.72,3589334.12],[40487620.6,3589299.08],[40487619.73,3589306.05],[40487608.89,3589393.1],[40487604.76,3589391.64],[40487577.73,3589388],[40487577.37,3589373.77],[40487593.19,3589251.99],[40487588.56,3589250.92],[40487584.54,3589250.53],[40487582.62,3589250.34],[40487574.15,3589250.12],[40487571.16,3589249.81],[40487566.09,3589249.28],[40487563.5,3589249.01],[40487551.68,3589247.73],[40487544.24,3589246.92],[40487538.31,3589246.29],[40487529.51,3589245.35],[40487527.56,3589245.15],[40487523.27,3589245.09],[40487519.76,3589245.06],[40487509.78,3589244.95],[40487507.86,3589244.93],[40487503.91,3589245.15],[40487500.38,3589245.65],[40487497.7,3589246.03],[40487486.24,3589247.08],[40487480.84,3589247.58],[40487474.39,3589247.91],[40487455.67,3589248.84],[40487451.14,3589248.84],[40487449.83,3589248.64],[40487446.27,3589247.83],[40487445.69,3589247.7],[40487437.88,3589246.42],[40487433.88,3589246.04],[40487433.56,3589246.02],[40487428.87,3589245.67],[40487422.95,3589245.16],[40487421.47,3589245.02],[40487417.65,3589244.66],[40487414.52,3589244.4],[40487412.5,3589244.4],[40487410.09,3589244.57],[40487407.83,3589244.73],[40487397.55,3589246.23],[40487394.71,3589246.65],[40487392.89,3589246.84],[40487399.16,3589297.08],[40487404.88,3589342.92],[40487433.37,3589571.22],[40487435.24,3589586.25]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":12.539699999999996,\"nydArea\":10.979199999999995,\"jsydArea\":1.5605,\"gdArea\":10.0874},\"DKID\":\"054G1336RCQ6R73T\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487390.66,3589228.93],[40487391.11,3589228.94],[40487402.84,3589229.14],[40487417.86,3589229.39],[40487428.71,3589229.58],[40487431.92,3589230],[40487433.53,3589230.22],[40487444.37,3589230.22],[40487447.88,3589229.62],[40487449.7,3589228.52],[40487450.81,3589226.41],[40487453.13,3589203.38],[40487455.72,3589177.06],[40487455.88,3589175.43],[40487457.49,3589168.66],[40487457.7,3589166.55],[40487457.41,3589164.52],[40487457.41,3589164.09],[40487459.19,3589160.95],[40487460.08,3589159.89],[40487461.25,3589153.85],[40487468.18,3589120.65],[40487468.3,3589119.75],[40487477.33,3589120.99],[40487477.21,3589121.83],[40487476.89,3589124.01],[40487476.64,3589131.99],[40487477.18,3589136.86],[40487477.23,3589138.72],[40487477.26,3589139.7],[40487476.81,3589143.22],[40487476.89,3589150.59],[40487476.66,3589154.46],[40487475.95,3589157.6],[40487473.67,3589162.93],[40487472.69,3589164.82],[40487472,3589166.14],[40487471.2,3589169.58],[40487469.5,3589177.06],[40487469.08,3589178.91],[40487465.13,3589192],[40487463.53,3589199.44],[40487462.14,3589207.77],[40487461.45,3589215.11],[40487461.05,3589223.61],[40487460.6,3589230.26],[40487460.38,3589231.28],[40487460.76,3589231.86],[40487461.68,3589232.37],[40487467.62,3589232.23],[40487473.02,3589232.1],[40487481.14,3589230.99],[40487493.26,3589231.13],[40487497.5,3589231.37],[40487503.63,3589231.71],[40487513.27,3589232.73],[40487515.6,3589233.11],[40487521.3,3589234.05],[40487525.24,3589234.79],[40487531.28,3589236.05],[40487532.97,3589236.41],[40487534.59,3589236.73],[40487542.44,3589238.33],[40487544.07,3589238.66],[40487550.09,3589239.5],[40487555.65,3589240.27],[40487561.37,3589241.07],[40487564.31,3589241.47],[40487573.59,3589242.74],[40487576.28,3589243.11],[40487580.61,3589243.67],[40487594.05,3589245.42],[40487600.12,3589198.33],[40487590.33,3589150.19],[40487589.12,3589138.46],[40487576.68,3589017.92],[40487573.73,3589018.91],[40487570.26,3589019.16],[40487567.16,3589018.83],[40487562.61,3589017.78],[40487559.14,3589017.86],[40487556.71,3589017.96],[40487550.39,3589018.21],[40487550.01,3589020.94],[40487538.56,3589023.55],[40487537.15,3589024.39],[40487536.71,3589024.97],[40487534.92,3589025.32],[40487530.39,3589024.7],[40487529.94,3589024.39],[40487529.99,3589020.75],[40487529.81,3589018.51],[40487529.02,3589018.16],[40487522.3,3589017.81],[40487519.36,3589018.26],[40487513.58,3589019.05],[40487502.16,3589019.92],[40487498.56,3589020.2],[40487491.54,3589020.69],[40487490.16,3589021.44],[40487489.46,3589022.87],[40487488.5,3589042.98],[40487488.45,3589044.13],[40487485.1,3589042.66],[40487479.34,3589038.67],[40487479.54,3589036.55],[40487479.43,3589033.17],[40487478.86,3589027.95],[40487478.29,3589025.8],[40487477.74,3589025.22],[40487477.14,3589025.22],[40487472.25,3589026.38],[40487467.94,3589026.82],[40487463.69,3589026.76],[40487460.14,3589026.74],[40487459.77,3589027.25],[40487459.7,3589031.34],[40487459.33,3589031.8],[40487453.76,3589032.38],[40487445.32,3589032.82],[40487438.64,3589033.73],[40487434.57,3589034.44],[40487422.98,3589035.75],[40487412.49,3589036.28],[40487398.65,3589035.61],[40487390.82,3589035.94],[40487389.03,3589036.56],[40487386.46,3589037.45],[40487384.03,3589038.38],[40487380.68,3589039.68],[40487374.54,3589042.74],[40487371.81,3589044.11],[40487367.89,3589046.48],[40487381.53,3589155.76],[40487390.66,3589228.93]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":12.539699999999996,\"nydArea\":10.979199999999995,\"jsydArea\":1.5605,\"gdArea\":10.0874},\"DKID\":\"054G1336RCQ6R73T\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487444.5,3589660.39],[40487450.49,3589660.18],[40487455.29,3589659.95],[40487458.7,3589659.81],[40487472.04,3589659.36],[40487485.31,3589658.92],[40487497.63,3589658.51],[40487499.45,3589658.45],[40487512.58,3589658.01],[40487522.8,3589657.67],[40487604.14,3589654.83],[40487602.67,3589604.49],[40487590.44,3589604.78],[40487577.73,3589605.06],[40487564.05,3589605.38],[40487550.04,3589604.42],[40487539.15,3589603.6],[40487535.68,3589603.6],[40487520.57,3589604.42],[40487512.48,3589604.31],[40487511.29,3589603.9],[40487509.57,3589603.71],[40487508.36,3589603.85],[40487507.71,3589604.68],[40487506.95,3589604.99],[40487505.96,3589605.05],[40487497.58,3589605.58],[40487493.28,3589605.96],[40487479.52,3589605.34],[40487466.08,3589604.66],[40487455.45,3589604.13],[40487452.96,3589604.03],[40487439.34,3589603.52],[40487437.39,3589603.45],[40487444.5,3589660.39]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":12.539699999999996,\"nydArea\":10.979199999999995,\"jsydArea\":1.5605,\"gdArea\":10.0874},\"DKID\":\"054G1336RCQ6R73T\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487618.75,3589654.32],[40487691.51,3589651.79],[40487687.22,3589627.48],[40487681.87,3589628.16],[40487680.47,3589629],[40487679,3589630.09],[40487671.05,3589629.88],[40487670.93,3589630.99],[40487670.48,3589631.63],[40487669.93,3589632.01],[40487665.63,3589632.05],[40487656.74,3589631.15],[40487644.03,3589631.23],[40487636.02,3589631.69],[40487633.98,3589632.1],[40487632.29,3589632.21],[40487625.65,3589631.6],[40487621.77,3589631.63],[40487618.88,3589631.89],[40487617.73,3589632.41],[40487617.1,3589633.23],[40487616.88,3589633.78],[40487617.17,3589637.45],[40487617.56,3589644.86],[40487618.75,3589654.32]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":12.539699999999996,\"nydArea\":10.979199999999995,\"jsydArea\":1.5605,\"gdArea\":10.0874},\"DKID\":\"054G1336RCQ6R73T\",\"PZWH\":null},\"type\":\"Feature\"},{\"geometry\":{\"coordinates\":[[[40487314.01,3589024.55],[40487317.84,3589025.45],[40487321.63,3589025.25],[40487325.2,3589024.65],[40487328.23,3589022.7],[40487330.44,3589020.72],[40487332.06,3589019.85],[40487335.61,3589019.15],[40487340.69,3589018.87],[40487342.2,3589019.41],[40487344.42,3589020.65],[40487346.1,3589020.72],[40487351.89,3589019.04],[40487357.57,3589017.7],[40487362.36,3589017.43],[40487364.13,3589016.8],[40487364.19,3589016.8],[40487368.95,3589016.12],[40487372.07,3589016.28],[40487375.87,3589016.02],[40487382.73,3589014.88],[40487385.85,3589013.97],[40487389.58,3589013.7],[40487394.54,3589013.8],[40487398.99,3589013.26],[40487406.55,3589011.85],[40487412.33,3589010.78],[40487413.44,3589010.43],[40487363.77,3589013.46],[40487312.91,3589016.56],[40487313.21,3589018.34],[40487313.21,3589023.75],[40487314.01,3589024.55]]],\"type\":\"Polygon\"},\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"properties\":{\"XMMC\":\"泰州市2015年度国务院批准建设用地城市农用地转用和土地征收第2批次实施方案\",\"XMID\":\"054H0833FMQ6R7KI\",\"PZRQ\":null,\"DKMC\":\"泰州市2015年度第2批次城市建设用地地块二\",\"PZJG\":\"江苏省人民政府\",\"XMZT\":\"待上报\",\"INFO\":{\"wlydArea\":0,\"area\":12.539699999999996,\"nydArea\":10.979199999999995,\"jsydArea\":1.5605,\"gdArea\":10.0874},\"DKID\":\"054G1336RCQ6R73T\",\"PZWH\":null},\"type\":\"Feature\"}],\"type\":\"FeatureCollection\"}";

		FeatureCollection fc2 = (FeatureCollection) geometryService.readUnTypeGeoJSON(geojson2);
		FeatureCollection fc8 = (FeatureCollection) geometryService.readUnTypeGeoJSON(geojson8);
		FeatureIterator featureIterator2 = fc2.features();
		FeatureIterator featureIterator8 = fc8.features();

		List<Geometry> geos2 = new ArrayList<Geometry>();
		List<Geometry> geos8 = new ArrayList<Geometry>();

		while (featureIterator2.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator2.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			geos2.add(geometry);
		}

		while (featureIterator8.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator8.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			geos8.add(geometry);
		}

		for (Geometry geo2 : geos2) {
			for (Geometry geo8 : geos8) {
				if (geo2.covers(geo8))
					System.out.println("covers..");
				else
					System.out.println("not covers..");
			}
		}

//        double area2 = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geojson2), geometryService.parseUndefineSR("EPSG:2364"));
//        double area8 = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geojson8), geometryService.parseUndefineSR("EPSG:2364"));
//        System.out.println(area2);
//        System.out.println(area8);
	}


	@Test
	public void testAspectLog() throws Exception {

//        LayerRegion layerRegion = geometryService.getLayerRegion("SDE.GDDK");
//        print(layerRegion.getLayerName());
//        Dict dict = dictService.getDictByName("config_test", "tblx");
//        print(dict.getName());

		String geojson = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40492713.59,3598113.09],[40492715.93,3598113.94],[40492715.91,3598114.01],[40492821.1,3598116.52],[40492826.43,3598098.28],[40492829.92,3598097.85],[40492840.78,3598096.64],[40492843.7,3598117.06],[40492949.09,3598119.57],[40492917.93,3597987.36],[40492911.82,3597958.73],[40492910.05,3597949.29],[40492908.56,3597942.82],[40492908.32,3597941.29],[40492879.9,3597945.14],[40492858.48,3597948],[40492857.36,3597939.48],[40492858.41,3597939.29],[40492856.97,3597928.45],[40492856.83,3597925.77],[40492856.7,3597925.77],[40492853.8,3597899.47],[40492850.97,3597873.63],[40492850.66,3597849.59],[40492849.5,3597837.34],[40492848.34,3597825.5],[40492848.35,3597825.5],[40492846.9,3597825.53],[40492846.95,3597821.79],[40492846.29,3597821.74],[40492829.71,3597822.02],[40492829.67,3597821.61],[40492829.53,3597817.95],[40492817.06,3597818.37],[40492817.22,3597822.69],[40492816.41,3597822.67],[40492798.55,3597822.42],[40492798.24,3597822.12],[40492794.84,3597822.12],[40492778.99,3597821.64],[40492776.29,3597821.63],[40492774.88,3597822.21],[40492773.96,3597823.15],[40492765.47,3597823.25],[40492757.78,3597823.34],[40492754.84,3597824.5],[40492754.36,3597830.03],[40492751.82,3597833.95],[40492751.67,3597835.13],[40492751.52,3597837.06],[40492751.67,3597840.62],[40492751.77,3597844.1],[40492752.56,3597849.96],[40492753.45,3597853.81],[40492753.45,3597859.3],[40492748.24,3597863.8],[40492748.02,3597876.4],[40492749.38,3597876.28],[40492750.28,3597885.79],[40492750.89,3597886.31],[40492750.95,3597887.8],[40492751.68,3597888.22],[40492753.34,3597888.19],[40492755.08,3597892.36],[40492754.79,3597896.82],[40492754.51,3597901.09],[40492751.19,3597903.85],[40492750.81,3597907.07],[40492750.81,3597911.06],[40492752.81,3597912.07],[40492755.56,3597912.45],[40492755.75,3597915.29],[40492754.9,3597917.76],[40492754.53,3597922.58],[40492753.44,3597927.16],[40492753.25,3597933.02],[40492752.16,3597936.36],[40492751.33,3597950.26],[40492749.11,3597954.97],[40492749.3,3597955.38],[40492749.74,3597955.96],[40492750.5,3597956.13],[40492752.02,3597956.09],[40492753.64,3597956.07],[40492753.82,3597957.52],[40492753.72,3597957.92],[40492752.57,3597958.99],[40492752.62,3597959.58],[40492753.08,3597960.19],[40492753.97,3597961.18],[40492755.2,3597961.61],[40492757.05,3597962.61],[40492758.05,3597964.97],[40492759.48,3597968.36],[40492760.12,3597970.67],[40492761.24,3597972.74],[40492763.52,3597974.34],[40492763.77,3597976.38],[40492757.59,3597999.31],[40492757.59,3598009.09],[40492756.89,3598019.95],[40492753.96,3598027.94],[40492755.63,3598039.45],[40492757.37,3598050.81],[40492755.51,3598051.06],[40492755.39,3598059.56],[40492770.56,3598064.55],[40492788.17,3598061.21],[40492783.42,3598078.02],[40492758.2,3598072.1],[40492758.95,3598070.22],[40492741.58,3598065.67],[40492741.06,3598066.42],[40492739.84,3598070.9],[40492735.59,3598076.29],[40492731.41,3598078.28],[40492730.8,3598080.62],[40492728.14,3598082.67],[40492725.79,3598083.58],[40492722.82,3598082.21],[40492713.18,3598083.2],[40492711.06,3598084.35],[40492710.49,3598085.89],[40492709.79,3598087.78],[40492709.95,3598094.3],[40492709.98,3598095.49],[40492709.83,3598099.34],[40492706.88,3598109.89],[40492710.49,3598110.68],[40492714.19,3598111.5],[40492713.59,3598113.09]]]},\"properties\":{\"XZQDM\":\"321200\",\"DKMC\":\"泰州市2016年第8批次村镇建设用地地块五\",\"DKMJ\":\"0\",\"DKID\":\"08G9323294Q6R2HL\",\"XMLX\":null}}";
		Object geo = geometryService.readUnTypeGeoJSON(geojson);
		SimpleFeature feature = (SimpleFeature) geo;
		Geometry geometry = (Geometry) feature.getDefaultGeometry();
		TopologyValidationError topologyValidationError = geometryService.validGeometry(geometry);
		print(topologyValidationError == null ? "valid" : topologyValidationError.getMessage());


	}

	@Test
	public void testExport2Shp() throws Exception {

		String layerName = "SDE.BPDK_JY";
		String where = "objectid <15 and objectid >2";
		String dataSource = "sde";
		String geojson = "{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40541893.740723,3523544.481506],[40541980.917725,3523540.6325070006],[40541981.341169,3523540.6823019995],[40542008.073727,3523539.501954],[40542011.5062,3523440.6986000002],[40542011.5374,3523439.7997],[40542011.9344,3523428.3713],[40542013.1652,3523392.9418],[40541871.859777,3523387.6378610004],[40541870.738672,3523435.0773789994],[40541876.281,3523435.1588],[40541880.2788,3523435.2174],[40541882.0424,3523435.2739999997],[40541885.7983,3523435.3944000006],[40541887.19728001,3523435.5517469994],[40541888.126709,3523435.526489],[40541888.933716,3523435.600525],[40541888.940217,3523435.74778],[40541888.9973,3523435.7542000003],[40541888.9573,3523436.134706],[40541893.740723,3523544.481506]]]]},\"properties\":{\"XMMJ\":19003,\"OG_PRO_OBJECTID\":\"12\",\"XMID\":\"{3E12C778-4926-41EE-80ED-B497F0A08822}\",\"PZWH\":\"国土资函[2013]620号、皖政地[2013]550号\",\"SSQY\":\"320281014\"}}";
		String geojson1 = "{\"type\":\"FeatureCollection\",\"bbox\":[40361509.141,3504505.6898999996,40362609.09995,3505266.8301999997],\"features\":[{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361675.0828,3504843.1281500002],[40361675.0828,3504837.25],[40361674.17815,3504834.763],[40361672.3698,3504833.858850001],[40361667.39585,3504833.858850001],[40361662.421849996,3504835.2150999997],[40361653.8302,3504836.3458499997],[40361641.8479,3504833.6328],[40361628.282800004,3504830.4672],[40361621.273949996,3504825.9458500003],[40361613.47395,3504814.9802000006],[40361617.09115,3504812.04115],[40361612.152150005,3504785.34385],[40361608.726050004,3504766.8239499996],[40361584.0828,3504764.563],[40361565.09115,3504757.6672000005],[40361566.67395,3504755.1802000003],[40361570.743750006,3504749.3021],[40361571.87395,3504746.8151000002],[40361571.87395,3504742.7458499996],[40361565.824200004,3504737.4012500006],[40361551.692200005,3504746.85485],[40361546.1951,3504730.655],[40361555.7651,3504724.8281500004],[40361568.6521,3504717.1411499996],[40361581.313,3504709.6802000003],[40361598.2698,3504701.7672],[40361606.86095,3504695.21095],[40361625.4,3504685.9411500003],[40361640.095850006,3504676.8979],[40361653.4349,3504669.6629999997],[40361661.5608,3504667.4551999993],[40361660.443749994,3504669.21095],[40361660.443749994,3504672.15],[40361662.478149995,3504675.54115],[40361668.5828,3504681.1937499996],[40361671.521850005,3504681.64585],[40361676.49585,3504681.1937499996],[40361681.243750006,3504677.3500000006],[40361685.53905,3504671.4718500003],[40361698.313,3504668.19375],[40361723.6349,3504656.8890500003],[40361734.713,3504652.3672],[40361739.687,3504648.75],[40361745.113,3504642.64585],[40361745.79115,3504640.1588500007],[40361745.33905,3504637.8978999997],[40361742.4,3504632.245849999],[40361740.59115,3504627.72395],[40361743.90979999,3504613.5988499997],[40361745,3504614.2718499997],[40361766.53195,3504622.86785],[40361766.3651,3504624.7849000003],[40361766.817200005,3504628.6281500002],[40361770.887,3504632.0198],[40361786.034899995,3504634.95885],[40361774.98205,3504638.71315],[40361773.1152,3504642.21315],[40361772.415199995,3504647.81315],[40361773.8152,3504654.1131499996],[40361786.18205,3504655.0467999997],[40361787.34885,3504651.0799499997],[40361815.1152,3504652.0131500005],[40361827.98915,3504660.6668999996],[40361823.33905,3504661.4109499995],[40361818.3651,3504665.9328],[40361810.67815,3504675.6541500003],[40361804.3479,3504693.28905],[40361799.82605,3504711.3760500005],[40361801.182799995,3504717.4802],[40361803.89585,3504721.7760500005],[40361806.15674999,3504724.9411500003],[40361813.1651,3504728.1067500003],[40361819.04375,3504730.141149999],[40361828.087,3504735.79375],[40361834.191149995,3504738.732800001],[40361838.26095,3504740.08905],[40361841.59885,3504746.8631499996],[40361846.73205,3504752.2299499996],[40361851.427200004,3504756.20605],[40361846.513,3504755.5760500003],[40361842.6698,3504756.25415],[40361838.6,3504759.8718499998],[40361836.79115,3504763.7150999997],[40361836.5651,3504769.1411499996],[40361838.37395,3504772.9848999996],[40361842.6698,3504779.99325],[40361848.5479,3504785.64585],[40361855.3302,3504792.2021],[40361867.087,3504799.437],[40361876.1302,3504813.2281500003],[40361881.556250006,3504822.72395],[40361888.113,3504829.7328000003],[40361898.060949996,3504834.0281499997],[40361911.4,3504841.9411500003],[40361917.7302,3504842.1672],[40361917.53174999,3504844.5149499993],[40361917.1651,3504848.837],[40361915.808850005,3504852.4541499997],[40361910.8349,3504856.2978999997],[40361902.469799995,3504864.437],[40361888.67815,3504878.0020999997],[40361883.704150006,3504883.8802],[40361877.1479,3504887.95],[40361859.060949996,3504898.12395],[40361855.6698,3504901.28905],[40361844.4728,3504912.72425],[40361838.6,3504907.05415],[40361824.808850005,3504900.95],[40361808.530200005,3504895.9760499997],[40361798.808850005,3504894.6198000005],[40361788.86095,3504896.65415],[40361782.530200005,3504901.62815],[40361772.35625,3504913.1588500002],[40361766.704150006,3504921.0718500004],[40361766.2521,3504925.1411499996],[40361767.8349,3504927.6281500002],[40361772.1302,3504933.05415],[40361777.5,3504935.14585],[40361757.2651,3504951.0849],[40361731.03905,3504964.87605],[40361718.37815,3504970.0760500003],[40361713.404149994,3504969.3978999997],[40361705.4349,3504966.28905],[40361703.334,3504965.0288],[40361693.282800004,3504958.9979],[40361675.19585,3504949.5021],[40361668.187,3504943.1718499996],[40361664.795849994,3504940.2328000003],[40361659.82185,3504937.5198000004],[40361657.949200004,3504935.9399],[40361645.3521,3504925.31095],[40361643.54375,3504919.2067499994],[40361647.16095,3504914.45885],[40361650.5521,3504909.0328000006],[40361650.70385,3504908.0127499998],[40361652.2479,3504897.6151000005],[40361653.60415,3504893.5458500003],[40361658.80415,3504888.1198],[40361672.63425,3504875.8260500003],[40361675.0828,3504873.65],[40361683.67395,3504864.8328],[40361684.12605,3504860.537],[40361681.8651,3504854.4328],[40361679.8969,3504851.152],[40361675.0828,3504843.1281500002]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":63330.31796751469,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2304,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":63330.30909116659,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b01f8bb020e348bc49900003\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40362099.140200004,3504652.7717999993],[40362062.15705,3504605.7549499995],[40362052.35705,3504592.4549499997],[40362052.35705,3504585.9218],[40362053.9902,3504579.15495],[40362059.59020001,3504566.7881500004],[40362077.687,3504540.9067499996],[40362080.4,3504542.94115],[40362083.5651,3504542.94115],[40362088.99115,3504540.6802],[40362099.84375,3504535.9328000005],[40362101.449200004,3504532.8441500003],[40362116.46520001,3504538.72995],[40362129.648849994,3504538.6131499996],[40362142.71520001,3504539.3131500003],[40362149.59885,3504544.7967999997],[40362157.29885,3504547.1299500004],[40362161.49885,3504549.46315],[40362164.53205,3504552.26315],[40362161.73205,3504562.296799999],[40362155.898849994,3504578.3968],[40362156.13205,3504584.6968],[40362159.63205,3504587.26315],[40362183.665199995,3504603.82995],[40362197.665199995,3504612.9299500003],[40362205.59885,3504615.029950001],[40362217.265200004,3504602.6631500004],[40362230.93305,3504606.40525],[40362230.86095,3504607.48905],[40362234.2521,3504612.237],[40362247.13905,3504621.9588500005],[40362250.403850004,3504624.0571999997],[40362270.28919999,3504657.33795],[40362178.6349,3504724.37605],[40362172.530200005,3504716.237],[40362167.782800004,3504710.5849],[40362165.069800004,3504709.2281500003],[40362159.64375,3504709.2281500003],[40362156.704150006,3504710.3588500004],[40362151.95625,3504715.3328],[40362149.00905,3504720.2141500004],[40362141.140200004,3504711.33815],[40362124.80705,3504687.5381500004],[40362099.140200004,3504652.7717999993]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":21638.0490209454,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2305,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":21638.0703890996,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b01f8bb020e348bc49900004\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361534.5871,3505266.8301999997],[40361534.56287658,3505266.7826119335],[40361529.911,3505257.65495],[40361524.73995,3505249.0032],[40361521.20595,3505243.48015],[40361525.02268781,3505242.2970606806],[40361539.882,3505237.6908500004],[40361552.58325,3505232.92785],[40361561.42905,3505227.9379000003],[40361566.645899996,3505223.4017999996],[40361578.2131,3505220.6799500003],[40361599.30685,3505215.009799999],[40361604.75005,3505212.96815],[40361612.68880001,3505204.8030499998],[40361623.57574999,3505192.2150000003],[40361629.4728,3505184.2767999996],[40361632.3079,3505182.91585],[40361632.4211,3505187.7921],[40361632.528,3505199.00705],[40361669.560949996,3505174.54195],[40361669.675,3505171.4050499997],[40361672.62375,3505152.12605],[40361671.94305,3505143.734],[40361668.76785,3505139.8781],[40361660.80295,3505135.04815],[40361673.5759,3505127.1716],[40361681.1545,3505122.3165000007],[40361681.355900005,3505127.0631999997],[40361682.1498,3505130.8059],[40361684.75795,3505134.5481],[40361691.90275,3505136.4760499997],[40361700.7481,3505135.1151],[40361707.325849995,3505132.6201499994],[40361711.63505,3505126.0428500003],[40361711.63505,3505121.50675],[40361710.2741,3505117.65085],[40361706.645150006,3505113.3411499998],[40361699.55585,3505110.5281499997],[40361700.8251,3505109.7151],[40361742.5378,3505084.4789],[40361743.4019,3505083.9561],[40361751.9173,3505078.4211],[40361768.4167,3505067.2319],[40361770.4382,3505065.8610000005],[40361813.1215,3505038.5054],[40361814.0001,3505037.9029999995],[40361821.015,3505050.4578999993],[40361820.56119999,3505052.95285],[40361818.06285,3505056.04895],[40361823.56028958,3505075.290802978],[40361823.68150086,3505075.7150997557],[40361823.6961,3505075.7662],[40361770.4951,3505110.92525],[40361534.5871,3505266.8301999997]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":8247.039215886189,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2301,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":8246.939110023006,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b01f8bb020e348bc49900005\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361950.26557557,3504939.8385670106],[40361909.028850004,3504970.3801499996],[40361929.1433,3504955.4806],[40361950.26557557,3504939.8385670106]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":6337.9018893185985,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"交通运输地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2163,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":35.5638,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"9批次实施方案\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\"国土资函[2013]620号、皖政地[2013]550号\",\"PZSJ\":\"2013-10-21\",\"SHAPE_AREA\":0.0407553567775949,\"BTBBL\":35.5638},\"id\":\"Featuref148bc49b01f8bb020e348bc49900006\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361950.474199995,3504939.6839999994],[40361950.26557557,3504939.8385670106],[40361929.1433,3504955.4806],[40361909.028850004,3504970.3801499996],[40361865.91685,3505002.31505],[40361814.0001,3505037.9029999995],[40361813.1215,3505038.5054],[40361770.4382,3505065.8610000005],[40361768.4167,3505067.2319],[40361751.9173,3505078.4211],[40361743.4019,3505083.9561],[40361742.5378,3505084.4789],[40361700.8251,3505109.7151],[40361699.55585,3505110.5281499997],[40361681.15543022,3505122.3160577286],[40361673.5759,3505127.1716],[40361660.80295,3505135.04815],[40361589.5931,3505179.4348],[40361512.05172885,3505229.3071893803],[40361512.04525,3505229.3112],[40361509.141,3505224.81905],[40361543.75480001,3505201.095200001],[40361600.57080001,3505164.3619999997],[40361800.119849995,3505037.74615],[40361820.134100005,3505024.778],[40361830.80305,3505017.8220999995],[40361847.24595,3505006.8608],[40361858.61205,3504999.10195],[40361872.3259,3504989.5832],[40361883.7289,3504981.5239999997],[40361891.061900005,3504976.3248],[40361905.70485,3504965.6308],[40361921.579799995,3504954.037],[40361949.63165671,3504933.1884417413],[40361949.839,3504933.03425],[40361950.474199995,3504939.6839999994]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":3697.8237257118553,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2302,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":3697.807294924882,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b02f8bb020e348bc49900007\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361906.93205,3504603.82995],[40361905.0652,3504611.9968],[40361901.79885,3504623.42995],[40361897.59885,3504629.72995],[40361892.46520001,3504633.22995],[40361885.93205,3504635.7967999997],[40361881.265200004,3504637.8968],[40361873.915199995,3504641.9799500005],[40361871.34885,3504644.546799999],[40361863.415199995,3504646.4131500004],[40361819.045200005,3504630.3540500007],[40361823.5651,3504627.95],[40361826.50415,3504625.01095],[40361826.50415,3504622.75],[40361825.37395,3504619.1328],[40361823.5651,3504616.4198000003],[40361819.2698,3504613.7062500003],[40361803.44325,3504609.1848999998],[40361786.487,3504602.4020999996],[40361778.57395,3504602.17605],[40361774.7302,3504603.5328],[40361772.69580001,3504606.0198],[40361771.702800006,3504607.58725],[40361770.5479,3504600.4802],[40361767.60885,3504593.0197999994],[40361761.7302,3504579.9062500005],[40361756.078150004,3504565.6629999997],[40361749.01205,3504552.6179],[40361746.144150004,3504547.3232],[40361744.32185,3504543.95885],[40361740.2521,3504539.21095],[40361740.2500968,3504535.5034999503],[40361799.0763,3504533.4849],[40361800.18804788,3504533.4467104157],[40361800.27815,3504533.8978999997],[40361804.12185,3504544.52395],[40361808.4172,3504550.6281500002],[40361811.5828,3504553.1150999996],[40361821.530200005,3504555.3760500005],[40361829.89585,3504555.6021],[40361833.287,3504553.79325],[40361834.25485,3504551.96525],[40361876.3652,3504543.8631499996],[40361891.0652,3504554.3631499996],[40361891.0652,3504600.7967999997],[40361906.93205,3504603.82995]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":10916.25411498121,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":1845,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":10916.257900118215,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b02f8bb020e348bc49900008\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40361800.113,3504533.0710000005],[40361800.18803185,3504533.446713596],[40361799.0763,3504533.4849],[40361740.25009219,3504535.503499955],[40361740.25,3504535.1270000003],[40361800.113,3504533.0710000005]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":187816.51942467468,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"九华东路（东环路-东部环路）\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":880,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":18.7378,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2011\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"A-C-A\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\"皖政地[2011]341号\",\"PZSJ\":\"2011-8-11\",\"SHAPE_AREA\":22.607324561570923,\"BTBBL\":0},\"id\":\"Featuref148bc49b02f8bb020e348bc49900009\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40362366.17395,3504540.0021],[40362366.17395,3504534.12395],[40362365.49585,3504531.41095],[40362361.87815,3504527.1151],[40362352.8349,3504523.72395],[40362341.530200005,3504522.59375],[40362330.226050004,3504523.0458500003],[40362324.8,3504526.4369999995],[40362321.86095,3504529.8281500004],[40362321.154,3504532.0371000003],[40362280.0052,3504529.01095],[40362267.963149995,3504517.40405],[40362268.4921,3504517.38595],[40362360.6901,3504514.2198],[40362443.42919999,3504511.37805],[40362539.8121,3504508.0680000004],[40362609.06105,3504505.6898999996],[40362609.09995,3504508.5082],[40362541.5942,3504511.18095],[40362448.29285,3504537.5788000007],[40362386.1461,3504574.6457499997],[40362419.3821,3504541.4091999996],[40362391.592199996,3504538.8441],[40362366.17395,3504540.0021]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":5328.090979529055,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2306,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":5328.094999909094,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b02f8bb020e348bc4990000a\"},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40362140.73205,3504797.8762499993],[40362131.097,3504805.3249500003],[40362128.456,3504807.36625],[40362119.94595595,3504813.6935407477],[40362119.93225303,3504813.703636829],[40362048.78405,3504866.6012500003],[40361988.2185352,3504911.732813039],[40361988.0211,3504911.8798],[40361987.6542,3504908.0329],[40361987.38775,3504905.23885],[40362003.8129,3504893.12975],[40362030.131799996,3504873.7511500004],[40362140.206149995,3504792.29505],[40362140.73205,3504797.8762499993]]]]},\"properties\":{\"LXR\":\"\",\"PB_PB_ID\":\"\",\"UNIT_NAME\":\"\",\"OG_SHAPE_AREA\":1012.4703881175193,\"ADD_AREA\":\"\",\"XMBH\":\"\",\"DKBH\":\"\",\"USAGE_\":\"\",\"PZDW\":\" \",\"XMMC\":\"河海大学南侧地块\",\"PRONAME\":\"\",\"SFLRXC\":\"\",\"KCMJ\":\"\",\"TDZL\":\"\",\"DH\":\"\",\"OBJECTID\":2303,\"XMFZR\":\"\",\"XZ\":\"\",\"DKMC\":\"\",\"DKPZMJ\":\"\",\"PZDJT\":11.4193,\"XZDM\":\"\",\"APP_TYPE\":\"\",\"YT\":\"\",\"FROM_BPJ_NO\":\"\",\"XS\":\"\",\"PZJG\":\" \",\"PROID\":\"\",\"OG_PRO_OBJECTID\":236,\"TFH\":\"\",\"TM\":\"\",\"OG_PRO_AREA\":114192.42421,\"CTID\":\" \",\"BZ\":\"\",\"DIKUAI_AREA\":\"\",\"REGIONCODE\":\"\",\"PWSJ\":\"2013\",\"YDDW\":\" \",\"APP_AREA\":\"\",\"PCMC\":\"\",\"PZRQ\":\"\",\"DKDW\":\"\",\"KCRQ\":\"\",\"PWBH\":\"7\",\"LJMJ\":\"\",\"JSYDXXK_ID\":\"\",\"PZWH\":\" \",\"PZSJ\":\" \",\"SHAPE_AREA\":1012.4404774720009,\"BTBBL\":11.4193},\"id\":\"Featuref148bc49b02f8bb020e348bc4990000b\"}]}";
		File file = geometryService.exportToShp(geojson);
//        File file= geometryService.exportToShp(layerName, where, null, null, dataSource);
		print(file.exists());
		print(file.getPath());

//        Date now = new Date();
//        String date = DateFormat.getDateInstance().format(now);
//        print(date);
		print("-----");
	}

	@Test
	public void testExport2Shp2() throws Exception {
		String layerName = "JSXZ.XZQ_D_2013_SIMPLIFYPOLYGON";
		String where = "xzqmc='句容市'";
		String ds = "jsxz";
		File file = geometryService.exportToShp(layerName, where, null, new String[]{"XZQDM", "XZQMC"}, ds);
		print(file.exists());

	}

	@Test
	public void testExport2ShpGeojson() throws Exception {
		String geojson = "{\"properties\":{\"PROID\":\"Z4F93740PAWVD302\",\"REGIONCODE\":\"340521\",\"TZGM\":\"29.33\",\"XMGM\":\"8.1162\",\"XMBH\":\"34052120110035\",\"YSRQ\":\"2011-08-04\",\"ZJGDMJ\":\"6.8382\"},\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40360902.851,3489520.949],[40361227.315,3489498.266],[40361253.948,3489496.378],[40361287.315,3489493.769],[40361309.833,3489491.712],[40361333.102,3489489.216],[40361362.93,3489485.302],[40361383.295,3489482.111],[40361404.073,3489478.415],[40361431.738,3489472.795],[40361460.284,3489466.145],[40361491.386,3489457.895],[40361530.873,3489445.87],[40361557.467,3489436.764],[40361583.261,3489427.13],[40361606.507,3489417.753],[40361630.523,3489407.35],[40361658.306,3489394.376],[40361691.506,3489381.455],[40361703.898,3489374.782],[40361727.408,3489361.501],[40361744.424,3489351.364],[40361748.479,3489349.376],[40361753.108,3489348.098],[40361759.089,3489347.779],[40361763.345,3489348.435],[40361768.781,3489350.451],[40361773.47,3489353.549],[40361777.26,3489357.369],[40361779.325,3489360.653],[40361814.919,3489425.26],[40361834.873,3489454.226],[40362017.358,3489785.463],[40362036.505,3489773.589],[40361854.58,3489443.369],[40361840.757,3489411.025],[40361802.147,3489340.943],[40361800.68,3489337.775],[40361799.395,3489333.056],[40361799.074,3489327.647],[40361799.739,3489323.024],[40361801.913,3489317.25],[40361804.497,3489313.294],[40361808.059,3489309.658],[40361809.718,3489308.391],[40361794.677,3489287.183],[40361780.046,3489297.301],[40361766.931,3489306.155],[40361754.154,3489314.551],[40361742.464,3489322.006],[40361723.384,3489333.69],[40361702.151,3489346.01],[40361683.354,3489356.34],[40361666.921,3489364.942],[40361648.492,3489374.129],[40361622.616,3489386.235],[40361594.988,3489398.176],[40361563.453,3489410.609],[40361525.244,3489424.037],[40361496.019,3489433.141],[40361465.452,3489441.616],[40361428.498,3489450.472],[40361394.996,3489457.218],[40361364.444,3489462.329],[40361336.089,3489466.2],[40361312.1,3489468.873],[40361285.41,3489471.35],[40361255.103,3489473.733],[40361225.746,3489475.821],[40360999.818,3489491.615],[40360964.659,3489490.565],[40360901.038,3489495.013],[40360902.851,3489520.949]],[[40362025.23,3489780.582],[40362028.634,3489778.471],[40361909.722,3489562.63],[40361891.297,3489527.112],[40361801.513,3489364.142],[40361800.33,3489363.498],[40361799.468,3489363.932],[40361799.169,3489364.715],[40361799.323,3489365.348],[40361828.276,3489417.901],[40361842.099,3489450.245],[40361886.041,3489530.007],[40361906.219,3489564.56],[40362025.23,3489780.582]],[[40360905.014,3489508.754],[40360905.799,3489508.965],[40360965.653,3489504.78],[40361000.812,3489505.831],[40361226.74,3489490.036],[40361233.391,3489489.571],[40361240.043,3489489.103],[40361246.695,3489488.63],[40361260.002,3489487.659],[40361273.31,3489486.639],[40361286.616,3489485.549],[40361304.036,3489483.984],[40361313.727,3489483.031],[40361322.438,3489482.115],[40361334.179,3489480.782],[40361337.815,3489480.345],[40361346.417,3489479.259],[40361356.394,3489477.905],[40361370.208,3489475.863],[40361388.067,3489472.93],[40361402.409,3489470.334],[40361421.765,3489466.487],[40361437.296,3489463.113],[40361456.334,3489458.623],[40361474.328,3489454.017],[40361485.13,3489451.08],[40361505.343,3489445.234],[40361519.532,3489440.854],[40361534.556,3489435.963],[40361550.093,3489430.628],[40361564.174,3489425.546],[40361574.798,3489421.552],[40361585.379,3489417.438],[40361594.911,3489413.612],[40361608.681,3489407.885],[40361618.659,3489403.583],[40361625.964,3489400.352],[40361633.528,3489396.933],[40361646.733,3489390.781],[40361660.991,3489383.874],[40361669.15,3489379.795],[40361678.637,3489374.936],[40361689.344,3489369.296],[40361697.902,3489364.669],[40361713.48,3489355.968],[40361723.874,3489349.957],[40361733.629,3489344.164],[40361738.313,3489341.329],[40361738.771,3489340.84],[40361738.888,3489340.506],[40361738.765,3489339.673],[40361738.157,3489339.113],[40361737.232,3489339.088],[40361737.012,3489339.194],[40361684.837,3489368.862],[40361652.081,3489381.557],[40361650.119,3489382.508],[40361647.108,3489383.956],[40361642.821,3489385.995],[40361635.291,3489389.518],[40361624.051,3489394.635],[40361610.183,3489400.719],[40361599.783,3489405.117],[40361571.109,3489416.538],[40361549.985,3489424.302],[40361518.55,3489434.871],[40361486.999,3489444.326],[40361449.922,3489454.009],[40361421.405,3489460.432],[40361394.165,3489465.755],[40361362.621,3489470.944],[40361340.587,3489473.956],[40361313.12,3489477.062],[40361279.474,3489480.124],[40361252.913,3489482.165],[40361226.322,3489484.051],[40361200.333,3489485.867],[40360905.625,3489506.471],[40360904.776,3489506.89],[40360904.496,3489507.429],[40360904.563,3489508.21],[40360905.014,3489508.754]]]}}";
		File file = geometryService.exportToShp(geojson);
		print(file.exists());
		print(file.getPath());

	}

	@Test
	public void testValidate() throws Exception {
//        InputStream inputStream = new FileInputStream("E:\\test\\mas.txt");
//        String geometry = IOUtils.toString(inputStream);
		String geometry = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40498164.19,3594879.12],[40498426.82,3594904.86],[40498436.89,3594905.85],[40498470.65,3594909.16],[40498481.78,3594905.64],[40498487.06,3594895.53],[40498492.3,3594835.16],[40498499.29,3594754.68],[40498498.83,3594719.48],[40498504.17,3594658.01],[40498501.86,3594645.27],[40498498.14,3594640.7],[40498493.75,3594635.39],[40498481.62,3594630.96],[40498462.31,3594629.13],[40498450.38,3594628],[40498419.18,3594625.04],[40498384.75,3594618.27],[40498297.85,3594610.03],[40498262.68,3594610.22],[40498193.86,3594603.7],[40498179.36,3594608.15],[40498172.07,3594621.72],[40498167.68,3594668.01],[40498161.84,3594692.57],[40498149.44,3594823.46],[40498148,3594838.55],[40498146.5,3594854.45],[40498146.23,3594857.33],[40498150.97,3594871.69],[40498164.19,3594879.12],[40498136.55,3595065.27],[40498129.99,3595061.64],[40498127.56,3595054.37],[40498140.09,3594922.04],[40498147.59,3594908.46],[40498147.74,3594908.41],[40498161.96,3594904.02],[40498424.19,3594929.73],[40498434.52,3594930.74],[40498468.82,3594934.1],[40498478.9,3594939.54],[40498482.3,3594950.33],[40498470.83,3595082.36],[40498465.38,3595092.4],[40498454.45,3595095.99],[40498411.36,3595091.83],[40498406.32,3595091.34],[40498136.55,3595065.27]]]]},\"properties\":{\"XZQDM\":\"321200\",\"DKMC\":\"泰州市11111\",\"DKMJ\":\"0\",\"DKID\":\"04QH14023WQ6R2NJ\",\"XMLX\":null}}]}";
//        String geojson = "{\"type\":\"Feature\",\"crs\":{\"properties\":{\"name\":\"EPSG:4610\"},\"type\":\"name\"},\"properties\":{\"purpose\":\"住宅\",\"dkArea\":\"0\",\"plotName\":\"地块10\",\"sbId\":\"025011040000157571\",\"mapNo\":\"地块10\",\"plotId\":\"025010960000161471\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.34337808483791,31.878763282947435],[120.34332378600062,31.878755727955962],[120.34329277171923,31.878751419789065],[120.34327069140166,31.878748007360485],[120.34320912733932,31.878739245446422],[120.34319342443192,31.87873702424108],[120.34297904696653,31.878719944901505],[120.34297711252773,31.878719850918596],[120.3427546083931,31.878722930989884],[120.34274300633547,31.87872360256648],[120.34257787648363,31.878739955897817],[120.34257276353645,31.878740591937294],[120.34238441478253,31.878774313698298],[120.34237844860222,31.878775718583324],[120.34216804753015,31.878833650419132],[120.34215628732088,31.8788372893891],[120.34204445021835,31.878877902121324],[120.34202626697902,31.878885165679606],[120.3419265498748,31.878928009296708],[120.34191977658115,31.878931229009492],[120.34180414840458,31.87898992440227],[120.34178253503488,31.879001345565996],[120.34158841564043,31.879124065243225],[120.34158159470131,31.87912870093934],[120.34142609240612,31.879248286461866],[120.3413798901397,31.879283978707694],[120.34125539555521,31.879383261727124],[120.34096381295709,31.879622009945844],[120.34087080163283,31.87969815737337],[120.34076133115904,31.87978779513474],[120.34073699031447,31.879807727688572],[120.3406729716964,31.8798673208298],[120.34066858842377,31.87987383476978],[120.34065163726645,31.87991442646962],[120.3406512653507,31.879916754187693],[120.34065513693902,31.87997223341795],[120.34065633707712,31.87997669426106],[120.34067758321389,31.880014721614973],[120.34073068834788,31.88007971848761],[120.34079287090272,31.880155819587834],[120.34085517034897,31.8802320646357],[120.34092453008842,31.880316948287327],[120.34097690302832,31.880381054204285],[120.3410436088061,31.88046269831792],[120.34110072761261,31.880532599235764],[120.34114633785642,31.88058843540628],[120.3413589156659,31.880626562338616],[120.34138683261423,31.880610497934143],[120.34140776763532,31.880598438361115],[120.34143414473243,31.88058325285635],[120.34146751420292,31.880564035422687],[120.34150767572794,31.880540921867194],[120.3415412245064,31.880521613749544],[120.3415874291618,31.880495011889078],[120.34162438447169,31.880473746644164],[120.34166959770015,31.880447715570728],[120.34170856856774,31.880425615201133],[120.3417611594798,31.880396876812476],[120.3418126047111,31.88036986397751],[120.34186756711838,31.880342237438132],[120.34190082094335,31.880326095441877],[120.34193215611775,31.88031128428718],[120.3419628279415,31.88029715128146],[120.34199492555443,31.880282743884027],[120.34203415788552,31.880265656885932],[120.3420759460026,31.880248057974374],[120.34209539563608,31.88024021383067],[120.34210096720065,31.880237818007704],[120.34210942086662,31.880234548672387],[120.34213355610561,31.880224807089792],[120.34218079545313,31.880206580229732],[120.34220056426851,31.880199312382082],[120.34220686491282,31.8801968695006],[120.34221177323387,31.880195178885366],[120.34223610162529,31.880186230373784],[120.34233954664958,31.8801508436312],[120.34235057846699,31.88014739599003],[120.34235580344829,31.880145596296956],[120.34236699423094,31.88014226546419],[120.34242380020623,31.88012452679481],[120.34249538140939,31.880103889496937],[120.34250223385536,31.88010204934042],[120.34250463038248,31.880101339456886],[120.34251043781295,31.88009985382886],[120.34255864784139,31.88008692697379],[120.34260132772238,31.88007616135816],[120.3426452978275,31.880065617708492],[120.34269938676923,31.88005339641984],[120.34274189349222,31.880044398810583],[120.34279472055898,31.880033912396776],[120.34282292224327,31.880029209967095],[120.34289979172034,31.880015348961717],[120.34291223590478,31.88001371915914],[120.34293123705932,31.880010376228913],[120.34303203536284,31.879996721146593],[120.34311251329488,31.8799869265481],[120.34312461073895,31.8799858477768],[120.34313046310163,31.879985083463183],[120.34320365101851,31.87997883463692],[120.34323802048341,31.87997578385081],[120.34324224714939,31.879975537964075],[120.34324583945094,31.879975230661742],[120.34334633940239,31.879969494202957],[120.34334763923562,31.879969454619438],[120.34344974473794,31.879966320031528],[120.34345041050774,31.87996630019642],[120.34352152684092,31.879965738312098],[120.34360403163481,31.879966714793966],[120.34367360455938,31.8799689075771],[120.34374336187899,31.879972371403802],[120.34381942479604,31.879977585726735],[120.3438893505762,31.879983718434268],[120.34389883477797,31.879984756939777],[120.3439094389753,31.87998571125029],[120.34396692658339,31.87999223829194],[120.34398778761707,31.879994526609096],[120.3439902092219,31.879994880789067],[120.34399548535187,31.879995479757913],[120.34405798937304,31.880003814881768],[120.34411468097802,31.880012282944932],[120.34414030591094,31.88001668666138],[120.34415425389184,31.880018714096202],[120.34425731806039,31.880037472694536],[120.34433022697108,31.880052831819697],[120.34435074636706,31.880057032881822],[120.34435510465696,31.88005807621832],[120.344359557664,31.88005901107854],[120.34445936470259,31.880082918765012],[120.34446162871834,31.880083525875307],[120.344463437636,31.880083962870913],[120.34448047090153,31.88008859721727],[120.34455953184303,31.880109855545285],[120.34457034519434,31.880113045771818],[120.3445768305479,31.880114813818047],[120.34458935850527,31.88011865773169],[120.34463389223181,31.880131811956595],[120.34467140284409,31.88014353325973],[120.34469396291321,31.880150794955462],[120.34475488592398,31.88015741152714],[120.3447567458414,31.880157352373843],[120.34480879791765,31.88014598343229],[120.34481105676389,31.880145201732365],[120.34488664327671,31.88008206702262],[120.344888787461,31.880078877746623],[120.34489785038188,31.880063287577574],[120.34490668434692,31.880048653968636],[120.34491622410312,31.880033396181688],[120.34493113637389,31.880010566483833],[120.34494320380529,31.879992930024482],[120.34495920650141,31.87997059337119],[120.34497485546214,31.879949763731567],[120.34499226490689,31.879927720860817],[120.34501852807368,31.879896356095138],[120.34504229359125,31.879869759764222],[120.34507565673266,31.879834939745738],[120.34511047224848,31.879801315209768],[120.34514997450833,31.87976612679344],[120.3451948720858,31.87972949882473],[120.34521932909935,31.879553936317343],[120.34519672140947,31.879525373620066],[120.34518553968248,31.87951124524937],[120.34514777877756,31.879463523724205],[120.34510139535462,31.87940490443156],[120.34507138644561,31.879367009823728],[120.34502500311974,31.879308390501635],[120.34498167803815,31.879253622695884],[120.3449458182234,31.8792083308904],[120.34491443945467,31.879168663355298],[120.34483123023449,31.879063510229056],[120.34478261730175,31.87900208315192],[120.34474503732149,31.8789545864684],[120.34469919600039,31.878896659961306],[120.3446990687057,31.878896534050224],[120.34467004331836,31.878870071881703],[120.34465426001337,31.87886046508695],[120.34462527205706,31.878846889957938],[120.34461885906046,31.878844661783994],[120.34457080917261,31.8788378930164],[120.34456064284834,31.878838127989347],[120.34445402872795,31.87886194562446],[120.34442171508228,31.878866343913774],[120.34439867066261,31.878873684107504],[120.34436614885641,31.878878948703438],[120.34432597392318,31.8788838642675],[120.34429696105938,31.87888631465115],[120.34426665391446,31.87888745186095],[120.34426196371992,31.878888086823597],[120.34425747087656,31.878887792366747],[120.3442372344065,31.87888855058699],[120.34417206017605,31.87888669791485],[120.34409856835603,31.878879177173015],[120.34407772528984,31.87887596895727],[120.34407769358369,31.87887596904308],[120.34392342081838,31.87885044080502],[120.34388492669642,31.878844078807006],[120.3438635116955,31.878840538425866],[120.34382557802456,31.87883425605877],[120.34373729533488,31.878819650648165],[120.34373727419744,31.878819650705307],[120.34359755325629,31.878796535705384],[120.34337817998964,31.878763291708754],[120.34337783105529,31.878763247559633],[120.34337808483791,31.878763282947435]]]}}";
//        SimpleFeature feature = geometryService.readFeatureJSON(geometry);
		Object obj = geometryService.readUnTypeGeoJSON(geometry);
		SimpleFeature feature = (SimpleFeature) obj;
		Geometry geo = (Geometry) feature.getDefaultGeometry();
		print(geo.toText());
//        TopologyValidationError validationError = geometryService.validGeometry(geo);
//        System.out.println(validationError.getMessage());

	}

	private OperatorFactoryLocal operatorFactory = OperatorFactoryLocal.getInstance();

	@Test
	public void testGetGeo() throws Exception {

		InputStream inputStream = new FileInputStream("E:\\coords_4610.json");
		String geometry = IOUtils.toString(inputStream);
		Geometry polygon = (Geometry) ((SimpleFeature) geometryService.readUnTypeGeoJSON(geometry)).getDefaultGeometry();
//        OperatorSimplify operatorSimplify = (OperatorSimplify)operatorFactory.getOperator(Operator.Type.Simplify);
//        if(!operatorSimplify.isSimpleAsFeature(polygon,spatialReference,null))
//            tmpGeo1 = GeometryEngine.simplify(tmpGeo1, spatialReference);
		print(polygon.getArea());
		double area = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), geometryService.parseUndefineSR("4610"));
//        agsGeometryService.getGeometryArea();
		print(area);
//        Object obj = geometryService.readUnTypeGeoJSON(geometry);
//        if(obj instanceof SimpleFeature){
//            SimpleFeature feature = (SimpleFeature)obj;
//
//        }
	}

	@Test
	public void testGetGeoResult() throws Exception {

		InputStream inputStream = new FileInputStream("E:\\coords_result.json");
		String geometry = IOUtils.toString(inputStream);
		double total = 0;
		double area_010 = 0;
		double area_020 = 0;
		double area_030 = 0;
		FeatureCollection featureCollection = geometryService.readFeatureCollectionJSON(geometry);
		FeatureIterator featureIterator = featureCollection.features();
		while (featureIterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator.next();
			Property property_area = feature.getProperty(Constant.SE_SHAPE_AREA);
			Object dm = feature.getProperty("GZQLXDM").getValue();
			double area = Double.valueOf(String.valueOf(property_area.getValue()));
			if (dm.equals("010"))
				area_010 += area;
			else if (dm.equals("020"))
				area_020 += area;
			else if (dm.equals("030"))
				area_030 += area;
			total += area;
		}
		print(area_010);
		print(area_020);
		print(area_030);
		print(total);
	}

	/**
	 * validate featureCollection
	 *
	 * @throws Exception
	 */
	@Test
	public void testValidate1() throws Exception {

		String geojson = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.27438337433551,32.06352841592101],[120.27438368939592,32.06352753146859],[120.274058301141,32.06354206017253],[120.27361212072483,32.063570096402565],[120.27283095972186,32.063592260390514],[120.27284321428992,32.06383190713105],[120.27322683197772,32.06377598936201],[120.27325466868956,32.06377089726923],[120.27353671384154,32.063710463027164],[120.27365779605157,32.063687638450055],[120.27396824887111,32.06363622310019],[120.2742783789022,32.06358322050971],[120.27438337433551,32.06352841592101]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.0903770528695,31.99052898282032],[120.0899264869038,31.989110188279177],[120.08976803346688,31.989147698987328],[120.0895589589429,31.98919719436259],[120.08938640325186,31.989238033247453],[120.08914592950302,31.989294963024523],[120.0890989914778,31.989307648539192],[120.08964166407858,31.990698604872097],[120.0903770528695,31.99052898282032]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.09220517817768,31.999172445063092],[120.09199795211458,31.998444944181497],[120.09195045876316,31.99824476559003],[120.09169933447528,31.998297938260738],[120.09131755187234,31.99837758276588],[120.09087941708916,31.998500932611865],[120.09089898722506,31.998557525822495],[120.09125173561824,31.998459858047536],[120.09161644646868,31.998388396556425],[120.09171563819898,31.99836832253235],[120.091746723558,31.998478944331524],[120.09174362347107,31.99847955981057],[120.09171428273169,31.998558796873215],[120.09147665278252,31.998625152970376],[120.09130841956689,31.998666388111317],[120.0911013615757,31.99871066298924],[120.09099662150018,31.99870672505115],[120.09095396197948,31.998716450200828],[120.09099883341148,31.998846171855668],[120.0912094262976,31.99959294379666],[120.09200925155724,31.9994065734277],[120.09225551701508,31.999349192184752],[120.09220517817768,31.999172445063092]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.1158983412892,32.04596341951868],[120.11578043264396,32.04591285439306],[120.1154246783352,32.046337842391026],[120.11571268314714,32.046513063584584],[120.11581206993752,32.046568885219756],[120.11625600070076,32.046043308298465],[120.1158983412892,32.04596341951868]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.1034351820864,31.99285642194891],[120.10333875449034,31.99283317041725],[120.1010000277254,31.992919229364663],[120.10105887633024,31.994089060009223],[120.10300695899932,31.99401782795681],[120.10325862951557,31.993948976848102],[120.10339752946638,31.993910969253033],[120.10359969096876,31.99385600074173],[120.10355715713536,31.99301381638917],[120.10353400944392,31.992943484264625],[120.10349745038472,31.992897791790995],[120.1034351820864,31.99285642194891]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.25608928579834,31.976645699642415],[120.25650464532522,31.97652688492544],[120.25573467905951,31.97669697960936],[120.25492931890804,31.976924234906942],[120.25486156012462,31.976945771153904],[120.25476558700764,31.97701810035936],[120.25474874637848,31.97712637954443],[120.2550647776842,31.977816240886657],[120.2552163735458,31.978153748638046],[120.25601120050972,31.979959591814865],[120.25605747632848,31.97993566338937],[120.25617381533536,31.97984306433032],[120.25629807821116,31.97974643606887],[120.25639394601032,31.97967370012469],[120.25506953710614,31.97708886892492],[120.25507351550566,31.976958917771025],[120.25517540229676,31.97688266254807],[120.25608928579834,31.976645699642415]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.306461052736,32.08844268188683],[120.30645276640024,32.08827683351712],[120.30606850169572,32.08829020730541],[120.30607604298692,32.08845520079076],[120.306461052736,32.08844268188683]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.18934284871816,32.03712781737588],[120.18897444643645,32.03703733798514],[120.18885500848712,32.03739032307261],[120.18922341206856,32.03748080280035],[120.18934284871816,32.03712781737588]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.09089898722506,31.998557525822495],[120.09087941708916,31.998500932611865],[120.0905973957626,31.99857043914737],[120.09086054123438,31.99967423163026],[120.0912094262976,31.99959294379666],[120.09099883341148,31.998846171855668],[120.09089898722506,31.998557525822495]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.25960732406732,31.980352622632857],[120.25972428345273,31.98025597891793],[120.25984652040974,31.980154932432235],[120.2598991940433,31.980111365933695],[120.26001198607396,31.980037925265716],[120.26010012747678,31.980002681952623],[120.26011239898014,31.979777003692277],[120.2601136149582,31.979382906345],[120.26013180856154,31.97915624195995],[120.26013525615936,31.97910317186675],[120.26014164711115,31.979005816429066],[120.26001023924448,31.978705509421],[120.25673330456507,31.98033599534891],[120.2567233041936,31.980316337888823],[120.2562916604184,31.98059692740356],[120.25652335612696,31.98112316661037],[120.25662125735336,31.98121035493888],[120.25676282480184,31.981210149784975],[120.25746359913452,31.98095322686476],[120.25784074603988,31.98082207628349],[120.25822353387632,31.98070318685944],[120.2586113284283,31.980596731179695],[120.25900360124405,31.98050286358615],[120.25932169081749,31.98040426753258],[120.25960732406732,31.980352622632857]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.27442798260732,32.06257891542241],[120.27438786474973,32.061272392544055],[120.27413067757526,32.06132074481272],[120.27272491258135,32.061508494514435],[120.27274504272869,32.06190427224584],[120.27281285362793,32.06323645551034],[120.27282499694786,32.063474334965015],[120.27374122983748,32.063433446433244],[120.27442213086825,32.063421017037236],[120.27441415633362,32.06316200841225],[120.27441016139842,32.06302992945437],[120.27440999493058,32.06293870306126],[120.27442311005746,32.062755860431885],[120.2744281410119,32.062667436418195],[120.27442798260732,32.06257891542241]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.09200266611842,31.998036353251308],[120.09191159476572,31.99770947347193],[120.091782850131,31.9976903488832],[120.09178761767788,31.997759533057945],[120.09172607340793,31.997773790245517],[120.09167468598278,31.997785686315343],[120.09164759997628,31.997769662506137],[120.0915863142633,31.997671435441628],[120.0913851504612,31.997723723711346],[120.09101345118783,31.99787440541555],[120.09087464447356,31.997921634012705],[120.09046585196992,31.998018655333368],[120.09055991643127,31.99841325165479],[120.09077035448809,31.998362933047964],[120.09121738938126,31.99824027157973],[120.09200266611842,31.998036353251308]]]}},{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.23707258805176,32.00002666138196],[120.23705490067664,31.999815479940622],[120.2367270967942,31.99982613820689],[120.23648820663152,31.99982358064491],[120.23630347417324,31.999827468969503],[120.23617520804218,31.999830178768587],[120.23547462434142,31.999814625955636],[120.2354872430286,32.0000191165372],[120.23707258805176,32.00002666138196]]]}}]}";
		FeatureCollection featureCollection = (FeatureCollection) geometryService.readUnTypeGeoJSON(geojson);
		FeatureIterator featureIterator = featureCollection.features();
		while (featureIterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) featureIterator.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			TopologyValidationError validationError = geometryService.validGeometry(geometry);
			if (validationError != null || !geometry.isValid())
				print("invalid " + validationError.getMessage());
			else {
				if (geometry instanceof Polygon)
					print("valid Polygon");
				else
					print("valid");
			}


		}
		print("--");

	}

	@Test
	public void testConvertShp2Dwg() throws Exception {

		String shpfile = "http://192.168.50.169:8088/shp.zip";
		String gpUrl = "http://192.168.1.125:6080/arcgis/rest/services/GP/ShpExportToCAD1/GPServer/ShpExportToCAD";
		String result = geometryService.convertShpToDwg(shpfile, gpUrl);
		print(result);
	}

	@Test
	public void testGetShpCoordinates() throws Exception {

		String path = "D:\\WFYD_H_320604_SHP";
		File file = new File(path);

		if (file.exists() && file.isDirectory()) {
			File shpFile = null;
			File dbfFile = null;
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File tmp = files[i];
				InputStream inputStream = new FileInputStream(tmp);
				if (tmp.isFile()) {
					if (tmp.getName().endsWith(".shp"))
						shpFile = tmp;
					if (tmp.getName().endsWith(".dbf"))
						dbfFile = tmp;
				}
			}

			if (!shpFile.exists() && !dbfFile.exists()) {

				print("文件夹中不存在shp和dbf文件！");
				file.delete();
			}
//            geometryService.parseShapefile(shpFile,dbfFile);
		}

//        if (file.exists()&&file.isDirectory()) {
//            Map param = new HashMap();
//            try {
//                param.put("url", file.toURI().toURL());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            DirectoryDataStore directoryDataStore = (DirectoryDataStore) DataStoreFinder.getDataStore(param);
//            ShapefileDataStore shapefileDataStore = (ShapefileDataStore) directoryDataStore.getDataStore("WFYD_H_320604");
//
//
////            DataStore dataStore = DataStoreFinder.getDataStore(param);
//
////            ShapefileDataStore sourceShpSource = (ShapefileDataStore) dataStore;
////            sourceShpSource.setStringCharset(Charset.forName("GBK"));
////            String typeName = sourceShpSource.getTypeNames()[0];
////            FeatureSource sourceFeatureSource = sourceShpSource.getFeatureSource(typeName);
//
//
//
//        }


		print("--");

	}

	@Test
	public void testGetShpCoordinates1() throws Exception {

//        Map map = new HashMap();
//        map.put("type","Feature");
//        map.put("crs","xxx");
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("result", JSON.toJSONString(map));
//        print(JSON.toJSONString(result));

		String fileName1 = "D:\\华士标准.zip";
		String fileName = "D:\\ceshi.zip";
		String fileName2 = "D:\\WFYD_H_320604_SHP.zip";
		String fileName3 = "D:\\shapeImport\\lsm.zip";
		String fileName4 = "D:\\shapeImport\\shp.zip";
		String fileName5 = "D:\\shapeImport\\shp2.zip";

		String filename6 = "D:\\shapeImport\\tdlyxz.zip";

		String fileName7 = "D:\\shapeImport\\TEMP_1411559068949.zip";

		String fileName8 = "D:\\shapeImport\\铁路.zip";

		String properties = "{\"proid\":\"testproid\",\"REGIONCODE\":\"320000\"}";

		File file = new File(fileName8);
		List<Document> documents = documentService.readZipFile(file);
		print(documents.size());
		String geometry = geometryService.getShpCoordinates(file, null);
//        FeatureCollection featureCollection =  geometryService.readFeatureCollectionJSON(geometry);
//        FeatureIterator featureIterator = featureCollection.features();
//        while (featureIterator.hasNext()){
//            SimpleFeature simpleFeature = (SimpleFeature) featureIterator.next();
//
//            OperatorFactoryLocal factory = OperatorFactoryLocal.getInstance();
//            OperatorImportFromGeoJson operatorImportFromGeoJson = (OperatorImportFromGeoJson) factory.getOperator(Operator.Type.ImportFromGeoJson);
////            operatorImportFromGeoJson.execute(ShapeImportFlags.ShapeImportDefaults,)
//            OperatorImportFromESRIShape operatorImportFromESRIShape = (OperatorImportFromESRIShape) factory.getOperator(Operator.Type.ImportFromESRIShape);
//            Geometry geometry1 = (Geometry) simpleFeature.getDefaultGeometry();
//            print(agsGeometryService.validGeometry(geometry1.toText()));
//        }
		print(geometry);


		print("--");


	}

	@Test
	public void testGetShpCoordinates2() throws Exception {
		// 大数据量，遇到topo数据允许跳过
		String path = "D:\\Documents\\项目文档\\淮安土地执法\\WPJC.zip";
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			System.out.println(path);
			long date = System.currentTimeMillis();
			String result = geometryService.getShpCoordinates(file, "{}", true);
			System.out.println(System.currentTimeMillis() - date);
		}
	}

	@Test
	public void testProjectFeature() throws Exception {

		String geometry = "{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40506046.8480,3686220.5690],[40506171.1440,3686256.3630],[40506204.54,3686153.3130],[40506080.0680,3686118.0620],[40506046.8480,3686220.5690]]]},\"properties\":{\"JZDSX_GUID\":\"3A03E8DBE6BC497387FF5B00E433467D\",\"ZD_GUID\":\"ACCA9E0C0E2446B09F21BC45AAA67421\",\"DKBH\":\"2013Q3646\",\"DKMC\":\"盐龙街道\",\"DKMJ\":\"1.3967\",\"DKYT\":\"061\",\"GD_GUID\":\"ACCA9E0C0E2446B09F21BC45AAA67421\",\"XZQ_DM\":\"320903\",\"BH\":\"3209012014CR0042\",\"DZ_BA_BH\":\"3209032014B00315\",\"XM_MC\":\"盐城市振升液压机械有限公司\",\"LAND_USER\":\"\",\"TD_JB\":\"dj12\",\"TD_ZL\":\"盐都区盐龙街道办黄刘居委会二组、四组\",\"GD_ZMJ\":\"1.3967\",\"GY_MJ\":\"1.3967\",\"GY_FS\":\"23\",\"PC_MJ\":\"1.3967\",\"TD_YT\":\"061\",\"CR_NX\":\"50\",\"CJ_PRICE\":\"268.1664\",\"JE\":\"268.1664\",\"MIN_RJL\":\"0.70\",\"MAX_RJL\":\"1.20\",\"MIN_RJL_TAG\":\"<\",\"MAX_RJL_TAG\":\"<\",\"JD_SJ\":\"2014-8-20 0:00:00\",\"DG_SJ\":\"2014-10-20 0:00:00\",\"JG_SJ\":\"2016-10-19 0:00:00\",\"SJ_JD_SJ\":\"\",\"SJ_DG_SJ\":\"\",\"SJ_JG_SJ\":\"\",\"KFJS_QX\":\"\",\"QD_RQ\":\"2014-2-20 0:00:00\",\"XM_ZT\":\"211\",\"PZ_WH\":\"盐都区工挂〔2014〕1号\",\"PZ_RQ\":\"2014-2-14 0:00:00\"}}";
		String geometry1 = "{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40413944.081,3723675.678],[40414126.16,3723708.684],[40414179.634,3723724.207],[40414187.262,33723724.894],[40414194.848,3723722.524],[40414200.914,3723717.384],[40414204.416,3723710.575],[40414248.132,3723559.984],[40414246.915,3723559.631],[40414246.762,3723560.151],[40414240.677,3723580.383],[40414228.719,3723620.138],[40414204.738,3723699.865],[40414195.181,3723711.566],[40414194.857,3723711.74],[40414193.484,3723712.478],[40414190.173,3723713.571],[40414186.723,3723714.072],[40414183.238,3723713.966],[40414179.825,3723713.256],[40414126.503,3723697.218],[40414027.568,3723678.296],[40413999.991,3723673.022],[40413999.997,3723673.004],[40413999.991,3723673.022],[40414054.074,3723493.224],[40414001.476,3723477.955],[40413944.081,3723675.678]]]},\"properties\":{\"JZDSX_GUID\":\"3A03E8DBE6BC497387FF5B00E433467D\"}}";
		String inSR = "2364";
		String outSR = "4610";
		Object geo = geometryService.readUnTypeGeoJSON(geometry1);
		CoordinateReferenceSystem in = geometryService.parseUndefineSR(inSR);
		CoordinateReferenceSystem out = geometryService.parseUndefineSR(outSR);
		if (geo instanceof Geometry) {

			TopologyValidationError validationError = geometryService.validGeometry((Geometry) geo);
			if (validationError != null || !((Geometry) geo).isValid())
				print("invalid " + validationError.getMessage());
			else {
				print("valid");

			}
			Geometry g = geometryService.project((Geometry) geo, in, out);
			print(geometryService.toGeoJSON(g));

		} else if ((geo instanceof FeatureCollection) || (geo instanceof SimpleFeature)) {
			if (geo instanceof SimpleFeature) {
				Geometry geometry2 = (Geometry) ((SimpleFeature) geo).getDefaultGeometry();
				TopologyValidationError validationError = geometryService.validGeometry(geometry2);
				if (!geometry2.isValid()) {
					print("invalid");
					geometry2 = geometryService.forceSimplify(geometry2, geometryService.getSimplifyTolerance());

				} else {
					if (validationError != null)
						print("invalid " + validationError.getMessage());
					else
						print("valid");
				}
				if (geometry2.isValid()) {
					Object feature = geometryService.project(geo, in, out);
					print(geometryService.toFeatureJSON(feature));
				}

			}

		}
		print("--");
	}

	@Test
	public void testReadWKT() throws Exception {
		String wkt = "POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))";
		String gc_wkt = "GEOMETRYCOLLECTION (LINESTRING (54675.88836562512 34442.8268516166, 54675.8182420644 34429.414115177016), POLYGON ((54815.501647517725 34472.57778526618,54816.516049073856 34471.95642229412, 54795.7588 34472.0647, 54795.7689 34473.9881, 54720.56238184346 34474.483075217126, 54790.1807 34474.0249, 54790.250141111945 34488.04537891723, 54796.50369876218 34484.214815819214, 54796.4428 34472.6784, 54815.501647517725 34472.57778526618)), POLYGON ((54926.09962156894 34404.8319449284, 54997.57666930818 34361.04929243863, 54931.42625207157 34375.104438055765, 54926.09962156894 34404.8319449284)), POLYGON ((54692.11164544723 34425.95221475808, 54692.2611 34454.5973, 54827.7892 34453.8903, 54827.49335595339 34397.187322040096, 54692.11164544723 34425.95221475808)))";
		Geometry geometry = geometryService.readWKT(gc_wkt);
		System.out.print(geometry.toText());
	}

	@Test
	public void testReadUnTypeGeoJSON() throws Exception {

		String geojson = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":0,\"DKMJ\":\"0.15770000\"},\"geometry\":{\"coordinates\":[[[51082.724867868434,38519.21641362272],[51086.910485684144,38514.98633196158],[51091.15926342667,38509.65092490567],[51097.777163587256,38510.77795334626],[51099.01449877162,38514.25039043557],[51111.20967143801,38514.30196143314],[51111.3704132823,38521.44605876738],[51132.91450827723,38519.98324939748],[51152.97766279593,38515.69028544612],[51161.93727054751,38510.8979868833],[51167.98896386716,38507.253042608965],[51169.90407935078,38504.62794468133],[51168.288866786024,38503.4494869397],[51166.55682345078,38495.48869767832],[51165.18308930182,38476.98709504539],[51164.345186799736,38478.5185101754],[51165.35421469202,38489.69309497066],[51147.23063637866,38496.558791792486],[51145.14216094676,38496.65482469555],[51114.968775634086,38498.049223294016],[51094.09169520721,38498.56951516634],[51086.16887964818,38509.38129170006],[51079.58701781931,38516.590010832064],[51081.59374109703,38519.0013911142],[51082.724867868434,38519.21641362272]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":1,\"DKMJ\":\"11.16480000\"},\"geometry\":{\"coordinates\":[[[51068.8555119697,38419.753425647505],[51073.90585519735,38393.325941112824],[51072.21686478195,38393.1358653917],[51069.92314376522,38392.42798827961],[51070.14078920421,38388.19686989207],[51074.55369267831,38388.94255119562],[51074.48385748361,38370.234059189446],[51063.88909285888,38365.53006545082],[51064.35560904539,38336.47781643411],[51062.696095457446,38305.71281164838],[51061.08086059484,38275.75756681431],[51060.97444683925,38273.78614359675],[51061.62386757226,38264.212783843745],[51062.04173348669,38258.13062146399],[51062.38536030296,38253.13884319179],[51069.968045337235,38253.65878177574],[51071.48925836126,38253.89074383862],[51081.70227106533,38254.47678650543],[51082.94407524248,38254.82022378314],[51093.557337491846,38255.45415164204],[51093.75266600541,38256.46311223833],[51104.74475221947,38256.87504048087],[51107.51296182993,38256.540419164114],[51115.97297336181,38256.55572674004],[51116.35438592271,38256.82370981574],[51123.8344327484,38256.84419428045],[51125.12092716739,38265.647331964225],[51133.412740710475,38266.00352542568],[51139.299780945534,38265.452429602854],[51144.59048778091,38265.59447887074],[51152.22135848287,38267.66415101802],[51159.01375421744,38268.1282647294],[51158.98534006709,38268.42841257015],[51163.6469675879,38268.74378379574],[51183.19170153856,38269.67052605562],[51187.23229483804,38269.78917954676],[51193.839678339864,38269.304277805146],[51200.72868923285,38269.12788594701],[51209.900830240615,38277.11937055644],[51212.08233601146,38277.40784389572],[51211.03532281369,38285.5433130553],[51222.763138833725,38287.04134575557],[51223.33442325395,38289.17831144342],[51232.837776580134,38289.82810198609],[51245.570484330616,38290.36083330866],[51250.26927898445,38293.92598306062],[51249.3929218776,38311.65047669038],[51252.32879126159,38318.444914772175],[51254.23847168148,38324.44478012202],[51255.53192582851,38328.507915763184],[51258.65574271564,38391.7009273693],[51267.53459285657,38394.52599902358],[51277.81207581023,38397.85167781496],[51294.509717384666,38405.00340940291],[51313.99701537827,38415.88037402788],[51353.70549824452,38438.371418623254],[51353.36717079879,38442.47317396244],[51333.8691402411,38432.02526400052],[51311.8979488246,38419.83943266841],[51296.709591654966,38410.66174373776],[51294.53010877432,38408.86327182315],[51286.10812994844,38404.87879576348],[51290.10476460451,38410.494638233446],[51265.73876477115,38450.963052074425],[51250.82746245234,38446.71786165889],[51249.634677827635,38446.378165786155],[51239.09718078016,38452.51378875505],[51222.456561486775,38473.1915439805],[51200.459796363175,38466.19780760957],[51173.07081822013,38462.5625327304],[51170.32437493183,38467.58500435529],[51196.10516843545,38468.532795629],[51197.091087449706,38471.547564045526],[51215.07233783783,38471.81256586546],[51215.33974705267,38477.00111356331],[51230.67241381186,38477.53010588605],[51236.484301156466,38479.789383951575],[51238.82224957608,38480.91902373498],[51230.717832049806,38489.91277126782],[51221.13306971096,38498.460343222134],[51215.118804913815,38503.32208014652],[51211.67438044528,38505.88625788875],[51212.04120516418,38510.96428122092],[51212.79299616669,38513.19729239959],[51216.412653908184,38517.11313943658],[51221.77548461,38519.92878541257],[51226.932653876866,38520.34753619833],[51231.1477281042,38519.04327736469],[51234.234731580415,38518.48097261414],[51243.169244667675,38521.6207463434],[51248.80714017908,38527.66691419901],[51244.73171368418,38543.47932408331],[51243.27874825146,38543.10500321025],[51242.386054968934,38546.568692924455],[51239.90098405688,38556.96174239833],[51239.200768409355,38560.70541310636],[51235.197281412846,38578.58842747752],[51235.308335498536,38578.59884067532],[51264.727950156615,38579.140406365506],[51281.36204795406,38579.94251864683],[51298.11582023737,38580.494000561535],[51316.41434298633,38582.704308315646],[51334.45441125245,38584.638983574696],[51361.62747467855,38587.155402491335],[51391.859157549414,38589.41566382814],[51407.52756711196,38591.03287123423],[51409.38855580465,38591.22303769458],[51442.98033220416,38594.64253732096],[51448.04636359696,38594.65677215019],[51448.03518897501,38590.64886301756],[51448.31654382463,38585.22741961107],[51449.24573668774,38562.36269210139],[51449.30536698909,38556.61442272598],[51409.225994871165,38554.207188548055],[51359.31659236877,38551.21089250967],[51309.40818115498,38548.21359079424],[51311.89585776676,38518.06068182876],[51353.77815769135,38473.32476162119],[51361.72669594759,38465.67882875027],[51401.11319103733,38427.79004599154],[51440.86466545812,38401.25524905976],[51411.34565833918,38396.28423646232],[51389.19842203976,38390.14628785569],[51363.67034433766,38386.11518319091],[51364.85880840891,38371.81901653949],[51366.798114203586,38334.02206740901],[51368.82448005521,38305.51158579346],[51370.83952579013,38277.50816017576],[51370.95850378241,38275.800545027014],[51371.76227250001,38266.29337342875],[51372.95050803656,38251.764210190624],[51372.977302014784,38250.96807488892],[51373.04536429803,38248.89773157053],[51373.119596979435,38246.670356889255],[51373.83124651354,38225.21476573311],[51374.76759876601,38220.73985434743],[51377.226525766135,38205.39698495064],[51379.208657340634,38179.11272034468],[51380.153293526026,38155.95091207465],[51380.441987931146,38148.88944247598],[51382.43302093051,38128.83308219677],[51382.46689725976,38113.28702541068],[51383.42127454293,38096.51211568853],[51371.853752418625,38096.20522418339],[51352.7471428693,38096.81615113421],[51326.85072716182,38097.45294544893],[51312.77388065084,38098.02730273036],[51305.36545470163,38100.206421269104],[51299.22094461264,38103.12885738071],[51295.60167803653,38107.047946054954],[51290.36101209207,38114.485572726466],[51286.87040422569,38120.04396918602],[51286.12770513984,38123.317867262755],[51286.146932920354,38126.95773756644],[51286.930834405655,38131.48356155865],[51289.08796119994,38136.62212659139],[51291.57816076926,38146.12889831513],[51292.58212819854,38154.453530343715],[51292.99000546772,38159.73133484973],[51292.3753473448,38162.63455941295],[51287.23608347102,38166.55167805357],[51281.46891848344,38168.9721252881],[51276.41913786322,38169.76279543433],[51264.24343113194,38168.663123956416],[51262.60947568298,38167.91176135652],[51262.22283363205,38166.65381353069],[51261.12454852944,38149.93974394817],[51261.59866260718,38143.14729163144],[51261.56552096579,38136.87351496657],[51254.13722331113,38136.42675919831],[51246.47972787884,38135.942214469425],[51241.17562731944,38133.075255930424],[51236.12790256347,38129.71194681991],[51231.26758867796,38128.88762821909],[51228.38169333505,38128.90287312865],[51227.320240061505,38128.247485370375],[51225.01001221795,38120.06075194478],[51217.99698664104,38118.15181342745],[51216.20166449388,38118.277296408545],[51211.439744896474,38121.69242561003],[51208.23344570427,38123.90234632976],[51206.1734353443,38123.329232888296],[51203.774858870864,38121.70191597566],[51201.38017388716,38121.000571447425],[51180.0089287253,38121.488463212736],[51155.42874553134,38123.308296443895],[51149.2219810111,38123.91107968893],[51139.954856514414,38130.11998738302],[51138.208865574634,38132.76919082133],[51135.34152573014,38137.05430561444],[51133.720865659845,38140.71283942135],[51130.46757047796,38143.87000160292],[51125.19108911187,38144.527870299295],[51122.30494348639,38144.68511552503],[51121.04475447272,38144.268775746226],[51116.86368547428,38138.38090686081],[51110.6507672459,38128.35380234616],[51103.70715147479,38121.97953032004],[51100.67732963928,38120.11554954667],[51094.20729112891,38119.15173526574],[51088.845582833725,38118.82006140752],[51079.431257735814,38119.8797854702],[51070.88885561875,38121.30490083387],[51059.57924722046,38123.25463035004],[51049.80881169843,38128.8362022168],[51045.672219893415,38131.36803543242],[51038.40306765991,38137.19639239507],[51027.00198796998,38144.54656563839],[51018.39008928036,38155.53197815316],[51016.16001615791,38161.19371716026],[51012.41073727693,38167.00348054338],[51009.19934714639,38176.20037762076],[51003.35131132382,38187.92118479451],[51001.495926479525,38194.47093814565],[51000.39649970672,38200.256703638006],[50998.53113110467,38204.916523644235],[50994.792486010614,38210.84623033926],[50989.294538105096,38216.90523004672],[50982.15337397229,38222.35291496944],[50976.40348050993,38228.043248517904],[50975.67141512512,38231.437091047876],[50974.68670178202,38236.22225810448],[50971.45532874214,38243.52927531954],[50967.59800877092,38249.70960753877],[50960.74735277736,38262.825702440925],[50957.115391628526,38266.23486448778],[50952.34889082,38266.89004011452],[50951.22426674175,38267.90597387822],[50948.85622227677,38272.05845359061],[50948.73885225622,38274.449056271],[50949.244169705686,38275.45637946715],[50951.77205596268,38276.95301428763],[50950.176594058714,38285.38138156524],[50943.84917956999,38301.00469473656],[50939.6092322577,38306.68705246551],[50934.62521289279,38315.38331925031],[50926.665722380945,38326.825285302475],[50922.89132090229,38331.66518998286],[50917.288629180046,38342.504710044246],[50915.175979794905,38345.785847256426],[50914.56063533877,38348.55907804798],[50914.701862079106,38352.57730297232],[50917.25273698439,38350.85383939464],[50918.240518883584,38346.64965139376],[50920.2896838517,38342.70885433862],[50921.913369933995,38337.7303125551],[50926.56458568729,38330.3957937751],[50928.487536853456,38327.92865292076],[50935.09669702683,38319.450799010694],[50941.71520091766,38311.60589130316],[50943.25151618527,38309.017793962266],[50944.43694596521,38306.26155152405],[50945.452804460656,38303.58620432392],[50949.103950643854,38296.236969297286],[50954.62593726889,38281.47790491441],[50956.40315762807,38277.168547549285],[50963.708038906094,38262.9600608144],[50966.60115158371,38257.874814196024],[50968.44190919228,38254.23511642683],[50978.54828494402,38232.741883778945],[50980.86458810557,38228.25968010817],[51022.320927494904,38240.88058447372],[51039.17700149197,38213.66173912352],[51061.06464679623,38249.215849306434],[51035.150864951625,38269.28259750595],[50996.28092015558,38297.6277314825],[50995.24231547918,38299.78320246143],[50994.16343258452,38303.77887275163],[50994.26264652336,38328.238169483375],[50989.47329823787,38347.28333162516],[50963.33520454055,38339.08647608431],[50937.19711000137,38330.88961995253],[50936.75178452555,38332.5309606581],[50936.34522328327,38332.42410924239],[50935.4135352943,38335.889006135985],[50935.80410240212,38335.99694203446],[50930.84963547512,38354.07298507169],[50917.25274226921,38350.85483935848],[50914.70186736416,38352.578302938025],[50915.48112187769,38356.22415990848],[50914.63432862855,38362.50858809892],[50913.02560807816,38368.42704414716],[50910.28229461232,38371.5815141215],[50909.67624781411,38376.114683150314],[50907.55159148574,38379.015886664856],[50906.298926750394,38380.40249442682],[50904.38134068044,38397.89249899238],[50902.813841045456,38409.72069490142],[50901.86299124304,38420.9156376645],[50900.37359528022,38424.813478093594],[50898.12769797961,38431.26529677491],[50897.068384730504,38436.12585819233],[50894.77567061719,38435.797972989734],[50893.06016569989,38443.08198370226],[50901.169610799014,38456.23904665141],[50905.85747221666,38463.79122610018],[50909.51242128087,38464.35491275042],[50914.54690729656,38466.34730092203],[50922.81729240844,38466.43260739744],[50954.47476151219,38466.758356826846],[50970.61545150839,38466.724084885325],[50974.965341495605,38453.64919955144],[50985.70819377132,38439.06255126838],[50993.74616221193,38425.440186258405],[50997.3998970909,38417.06694492046],[51001.87765994447,38406.80736417789],[51068.8555119697,38419.753425647505]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":2,\"DKMJ\":\"1.84050000\"},\"geometry\":{\"coordinates\":[[[52673.78246353878,49354.02323510824],[52666.144939205486,49386.56339103588],[52665.82113034821,49387.920093763154],[52648.39067206411,49386.10145811178],[52647.47713332056,49390.339258120395],[52641.91573358637,49415.545485210605],[52638.54746381526,49430.39919071365],[52636.267104647195,49433.91323920152],[52633.54165240699,49433.067686870694],[52626.124995143786,49431.79699298693],[52616.53106191642,49429.148847415585],[52579.519747411076,49419.3930278169],[52555.1039092646,49412.582448174246],[52557.78491216528,49388.23947052844],[52558.9652120208,49376.973322102334],[52560.87937887753,49355.11338430084],[52564.1178580833,49307.45567074977],[52565.89534401343,49282.446486767854],[52569.00173507175,49251.57031725813],[52576.38246042509,49251.721213887446],[52585.818402435965,49253.23820951814],[52600.51494383523,49254.12334114034],[52629.91971384831,49256.77853403008],[52637.72519039899,49257.827171862125],[52650.514608739664,49259.62939833803],[52655.02999889414,49260.65546674327],[52660.4957235473,49261.7464997815],[52687.29465861976,49263.48450618889],[52693.38549829632,49264.032232533675],[52689.35396729667,49280.72343271552],[52687.266548782856,49290.64439754793],[52680.258199398726,49320.19924712879],[52673.78246353878,49354.02323510824]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":3,\"DKMJ\":\"31.98120000\"},\"geometry\":{\"coordinates\":[[[49983.16014584307,49025.252973837785],[49988.24831065424,49034.53996548196],[49976.002555346306,49073.07961493357],[49963.756801858275,49111.619265687186],[49989.23666220077,49120.307232769206],[50014.71652175587,49128.99519924214],[50020.128262205675,49130.8405190045],[50020.88091665768,49152.106407598134],[50023.344846428685,49154.36134133208],[50042.18406604376,49171.60343608773],[50076.93488464346,49177.27530307182],[50076.90255378946,49175.89148253109],[50076.90455372165,49175.891471935895],[50076.93688458265,49177.27529247617],[50114.127788875405,49183.34422964649],[50128.17784559245,49185.637782673344],[50128.493840194686,49188.84608942596],[50131.72907549913,49221.73875311064],[50134.70790647056,49244.17283755215],[50138.04024557013,49258.20909933141],[50140.01633925464,49266.534580407664],[50139.22981598613,49306.82850567717],[50143.70356195192,49350.954539448954],[50145.00336404339,49354.882629584055],[50150.63654549592,49371.911682894446],[50152.45465803079,49377.409017520025],[50154.963829897155,49384.99467832875],[50191.521371640665,49384.01100259367],[50228.079911518485,49383.02732165158],[50264.63744960369,49382.04364609392],[50273.63986242747,49408.93578522326],[50282.61378735451,49447.43800276379],[50290.708022653685,49480.33491485054],[50312.32574958,49479.74538503634],[50344.122932343,49478.877924944274],[50382.16416873144,49477.84038420441],[50410.30157925958,49479.46529831412],[50438.43799404174,49481.09121755884],[50438.43491595868,49480.51023758529],[50438.435915918875,49480.510232287925],[50438.438993999465,49481.09121226054],[50448.157894453514,49481.65171665372],[50492.53716057176,49484.21557282144],[50497.20102000167,49484.57286074292],[50533.92463963883,49487.38227561815],[50570.64825744339,49490.191690202795],[50599.25783335437,49490.90610776795],[50627.55837140663,49491.61316264467],[50655.85890836609,49492.32021745947],[50703.074807981466,49493.50005255825],[50741.19629979382,49494.21307422966],[50779.31778432134,49494.92509585107],[50801.69183148472,49495.34355178755],[50843.12761827374,49496.11801312817],[50875.126393677354,49499.51945475442],[50909.6935962501,49503.19428618252],[50944.26079191064,49506.86811728962],[50953.85812185877,49507.88826171682],[50956.39053035021,49508.1578426552],[50962.7612932053,49491.32420908799],[50959.86345041491,49459.26279046107],[50956.965614328416,49427.20237155212],[50952.735719465665,49380.40611441807],[50948.5058275794,49333.6098567578],[50944.45436369701,49288.78163933009],[50943.24341098744,49275.38714985736],[50942.98023741803,49283.47048675502],[50898.61298023238,49282.0355487112],[50898.48054842818,49286.29522034991],[50898.425038827154,49288.08650183073],[50893.189214418424,49287.92324171448],[50891.31391601232,49287.86417722376],[50887.72335583001,49287.753200335894],[50887.246800972855,49301.61562775447],[50887.25764824149,49301.77556916279],[50842.72624916253,49300.125503188],[50798.19584203646,49298.474431735],[50753.66443753923,49296.8243653262],[50709.134030295165,49295.17429340631],[50707.36369589121,49295.10867286986],[50708.51743845838,49246.17389202584],[50709.67118959687,49197.24011132028],[50709.707066936906,49195.70693164366],[50753.17949037816,49197.35861454252],[50796.65191123147,49199.01029723045],[50840.1243295052,49200.6619797079],[50883.596745200426,49202.313661975786],[50927.06915831341,49203.96534403134],[50926.34259814138,49215.9391084672],[50938.33492294745,49216.395572585054],[50936.66698472844,49262.08408510918],[50942.05901511907,49262.286517566536],[50940.581391039304,49245.93346177554],[50939.39054309384,49208.02003954584],[50933.7247287368,49204.649079579394],[50916.02707496161,49194.11791185383],[50910.8407637468,49191.03140937863],[50883.84869402917,49192.866393495344],[50883.83184122948,49194.21547331288],[50883.829841302424,49194.21548390762],[50883.846694094595,49192.86640409193],[50869.56394659452,49193.83706362685],[50863.458193998216,49194.252407441374],[50813.59905407424,49192.83255824726],[50763.7399107507,49191.41270884173],[50713.8807640223,49189.99285922432],[50664.0216139031,49188.573009395506],[50614.162460385385,49187.15315935435],[50614.58862866314,49185.29791396391],[50622.099277186055,49152.586342391565],[50629.6099323901,49119.87577146664],[50637.12059427217,49087.16620118497],[50613.019330898685,49086.32788548525],[50616.87934794247,49063.68458732078],[50616.88134786996,49063.68457672699],[50613.02132023654,49086.32587495865],[50637.1215836433,49087.16419595759],[50644.50517564155,49055.007295823656],[50651.67079871006,49023.79954468692],[50652.75476681121,49019.07583401771],[50661.00536491297,48983.14536769921],[50662.75009037536,48975.54517614795],[50670.10046160361,48943.534453319386],[50676.991434521486,48913.52415126702],[50677.52022132444,48911.21936562471],[50662.09710584443,48908.533085381154],[50660.9510552651,48908.333157755435],[50622.794127049056,48901.686332658865],[50584.63720218922,48895.040506822064],[50546.48027011066,48888.39368031453],[50544.38817086236,48897.05570604839],[50533.07631047498,48943.88232248835],[50522.647086163095,48987.054288522806],[50512.21786424541,49030.22625580197],[50501.78864472433,49073.39822432678],[50498.58100858342,49086.67613083357],[50478.714615267214,49077.32543532038],[50458.31188953829,49067.72258217307],[50421.245734860066,49050.277053945705],[50384.17957873305,49032.83152394089],[50344.36458313693,49014.091562237125],[50338.84385493013,49011.49282454512],[50335.13845283723,49020.095399943646],[50329.378338801704,49033.46883042343],[50319.14033810684,49057.23991794093],[50308.9023380387,49081.011006124776],[50265.42600085437,49060.88144979579],[50226.654607745666,49050.37490760535],[50187.88321272559,49039.868364288945],[50149.111815811535,49029.36181984609],[50110.34041700396,49018.85527428007],[50071.56901629123,49008.34872758808],[50058.81720089887,49007.04628772847],[50048.67077831343,49006.01004425668],[50020.608778275106,49003.14371873578],[49992.546777178606,49000.27739298902],[49977.99928436787,49015.834367937416],[49979.98850367953,49019.464809080586],[49983.16014584307,49025.252973837785]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":4,\"DKMJ\":\"11.50010000\"},\"geometry\":{\"coordinates\":[[[50930.28907461575,49606.74543527701],[50937.20282670253,49606.7088043308],[50939.21491738309,49572.18938827003],[50941.22800965108,49537.669967108406],[50941.38290036208,49535.00716534257],[50941.94673739699,49528.37122507113],[50943.0961340207,49514.85823117662],[50909.99821295621,49512.55960626807],[50866.84467122669,49509.56326247472],[50857.335244323054,49508.90264970204],[50818.49471794964,49507.80344121903],[50779.65418951292,49506.7042326089],[50731.10571293648,49504.09146850416],[50682.55723313901,49501.47870403668],[50654.855797235774,49507.39743109606],[50629.57760417147,49512.79932326684],[50606.671329653866,49513.47068031458],[50606.58447011946,49520.85809151641],[50606.214539839675,49552.390842367895],[50605.84461621805,49583.92459322512],[50601.05419379494,49584.02697351901],[50581.89050386744,49584.43650530139],[50571.334777930555,49584.66243094765],[50530.66268013209,49585.531917212065],[50523.315564697914,49585.68884323025],[50507.57646052849,49586.02523115464],[50507.43956692184,49595.67089380091],[50506.95866162303,49629.66122102458],[50506.4777579127,49663.65154830087],[50505.99685577881,49697.641875629786],[50505.90589587495,49704.06631586794],[50505.69333182593,49719.05834480702],[50505.18685913863,49754.827796175145],[50504.67320727235,49791.12928219745],[50504.670639499855,49794.41927445587],[50532.43475155941,49795.233159299474],[50560.19786790328,49796.04804933863],[50594.66896975299,49797.0583953131],[50629.1390753085,49798.0697464468],[50631.94584969572,49797.46487859265],[50632.5088744755,49801.999865148224],[50633.50229115923,49810.006548093166],[50633.65664719215,49812.338714681566],[50633.9913771187,49817.383907455485],[50640.20154619129,49817.047004539054],[50658.5241487879,49816.053927159395],[50693.07832642901,49816.89083298901],[50712.138405045574,49818.81982829701],[50730.91351267421,49819.81033982569],[50733.442960680506,49820.27593412995],[50752.88477259526,49823.85389531823],[50772.88464293651,49823.85792387882],[50799.993213727925,49825.514274844434],[50817.97286140142,49821.699034132995],[50819.6587374251,49809.03318898659],[50820.04055406208,49806.16818572442],[50846.13109158915,49807.25093534589],[50853.37762877715,49807.55153687018],[50879.32846336677,49809.634019609075],[50903.466235841515,49811.13611294655],[50924.222433208575,49812.33612589724],[50926.806123048635,49801.71251104958],[50929.465942380746,49790.35849806573],[50932.448777305886,49766.3628630857],[50932.67693244584,49747.14179019071],[50932.72875929173,49746.16552249249],[50934.9642735769,49704.36697349371],[50937.19879028411,49662.56843006285],[50937.19927295883,49656.24247231055],[50937.20282670253,49606.7088043308],[50930.28907461575,49606.74543527701],[50929.82612695975,49616.75781729677],[50929.170234394565,49632.63118016906],[50929.13320277587,49633.191372414585],[50920.06140864377,49632.8394404063],[50919.36127478479,49632.81315014185],[50914.431995059145,49634.82925295503],[50873.33460456752,49631.48702461925],[50872.10051825553,49638.26351583795],[50871.88656598953,49639.404641451314],[50835.92589119757,49639.225175078966],[50840.26250585854,49595.18250428419],[50844.59912313744,49551.13983402727],[50842.56765925822,49550.86059913737],[50842.96936614946,49547.408494811505],[50878.819826439074,49550.378530094866],[50914.670284983826,49553.34856507321],[50914.52840084015,49577.52914626524],[50931.53388425316,49578.5890391185],[50930.02264120034,49582.127021066844],[50929.66307966799,49589.758872140665],[50931.01949489595,49590.971676884226],[50930.28907461575,49606.74543527701]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":5,\"DKMJ\":\"0.66240000\"},\"geometry\":{\"coordinates\":[[[49271.35891956588,46465.740100274794],[49249.322975102645,46477.81670009345],[49227.28703017431,46489.8933006362],[49206.767271658995,46501.81787588075],[49206.75532348876,46499.56094987551],[49206.75319367195,46498.59196576197],[49207.40893083848,46495.89850709075],[49211.001805469314,46488.88951971289],[49211.7762568927,46482.17545175506],[49211.601322514915,46475.57640931104],[49211.49856779656,46475.05595577555],[49211.171684881134,46473.377694292],[49222.70701045673,46467.21765576909],[49262.859493495,46446.950187006965],[49303.01197488015,46426.68272046419],[49330.673547014085,46413.78634550004],[49350.93982522595,46405.54909834638],[49393.113777950624,46388.39591943752],[49435.287728631585,46371.24274249793],[49476.99503611723,46357.55702005932],[49518.70234147385,46343.87129917368],[49562.794989765214,46331.19794697082],[49587.89406778305,46325.38510800152],[49636.60111276892,46315.43731587799],[49594.82013696362,46328.42842667177],[49571.01795259901,46336.48938835692],[49547.2157675425,46344.55035056361],[49523.62169807667,46353.20020850189],[49500.02762795319,46361.850066992454],[49476.65167313495,46371.08876834344],[49453.27571768944,46380.32747028256],[49430.14282382586,46390.14488313301],[49407.00992936323,46399.96229660232],[49375.54579369327,46414.25879318733],[49361.25026247869,46420.75444026152],[49352.19359615222,46425.15236377716],[49316.02661034737,46442.683743279435],[49293.692765203545,46454.21192142554],[49271.35891956588,46465.740100274794]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":6,\"DKMJ\":\"0.00210000\"},\"geometry\":{\"coordinates\":[[[51699.25319446755,46438.50203916664],[51694.66380151996,46441.63130979101],[51690.1314110363,46435.50535420328],[51699.25319446755,46438.50203916664]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":7,\"DKMJ\":\"0.05150000\"},\"geometry\":{\"coordinates\":[[[51647.1417641766,46448.72283349],[51646.66886436305,46446.28535676421],[51645.68280153426,46441.36061685997],[51644.64668763736,46436.42614199267],[51643.93919078063,46433.11991423555],[51643.20169504347,46429.813845286146],[51642.444200049795,46426.50788220391],[51641.65665325328,46423.19207828864],[51640.82916086523,46419.886485753115],[51640.18594977234,46417.389911087696],[51642.95135360251,46418.41526286211],[51655.7941097716,46418.95726946462],[51672.38347288386,46428.30936999014],[51647.1417641766,46448.72283349]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":8,\"DKMJ\":\"0.03830000\"},\"geometry\":{\"coordinates\":[[[51652.20340316595,46475.485820202615],[51653.11888647512,46480.30093455827],[51654.04431650763,46485.10599632561],[51654.98963996124,46489.89095292706],[51655.62650438528,46493.07755548321],[51655.85242536307,46494.196350367274],[51672.63024330899,46484.36760753533],[51657.80618615748,46466.57623117231],[51647.45022314115,46450.32118756045],[51647.62487538673,46451.20025582984],[51648.560781304936,46456.09526147926],[51649.47658211967,46460.970373715274],[51650.38227745965,46465.82553960057],[51651.28786694464,46470.660706195515],[51652.20340316595,46475.485820202615]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":9,\"DKMJ\":\"0.00740000\"},\"geometry\":{\"coordinates\":[[[51785.25476225626,46452.73263011547],[51770.89745152118,46463.22755101137],[51780.02776266837,46450.07932385941],[51780.8042928551,46449.425218325574],[51787.97439239717,46447.567275217734],[51791.09608899787,46448.459741552826],[51785.25476225626,46452.73263011547]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":10,\"DKMJ\":\"0.00590000\"},\"geometry\":{\"coordinates\":[[[49671.69636564759,46699.919454567134],[49683.30914576398,46700.4579699575],[49682.63134576242,46702.761545985006],[49671.285432900135,46701.63162094308],[49670.5850862024,46701.56532915635],[49662.04055997759,46700.70057122595],[49656.52868324616,46700.33975477284],[49638.06041858262,46698.757540017825],[49636.05509983099,46698.50615814188],[49633.333302534265,46698.16357001383],[49671.69636564759,46699.919454567134]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":11,\"DKMJ\":\"0.02660000\"},\"geometry\":{\"coordinates\":[[[51695.16560867081,46521.30800259299],[51674.53261004692,46537.70710227731],[51667.9207536055,46543.01206312468],[51667.85969704194,46542.812387987506],[51666.943689748165,46539.78726203274],[51666.16990073858,46537.181379739195],[51683.53276705554,46524.5295617464],[51686.98501627266,46523.0712970756],[51720.256654660094,46499.82534427475],[51753.45755428199,46476.62876766919],[51742.56533657726,46485.06936201127],[51723.59606901724,46499.15367069468],[51715.68919345841,46505.01948217256],[51695.16560867081,46521.30800259299]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":12,\"DKMJ\":\"0.11370000\"},\"geometry\":{\"coordinates\":[[[51608.97400876429,46590.939749036916],[51599.09461092964,46591.22605016967],[51584.878880569726,46591.6313077393],[51567.77146791102,46592.09187400342],[51567.40462195425,46591.36482196674],[51566.8841245689,46590.325585881255],[51603.96237280128,46570.60044512432],[51641.04261430522,46550.87429580977],[51641.10361792769,46551.063971315045],[51641.947986766805,46553.77947913436],[51642.82235451626,46556.49482813198],[51643.71672152536,46559.21007124289],[51644.60045365413,46561.80537162303],[51642.45476560216,46563.56071701879],[51608.97400876429,46590.939749036916]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":13,\"DKMJ\":\"0.00100000\"},\"geometry\":{\"coordinates\":[[[51192.6087869472,46339.75994378933],[51199.97399927092,46350.200875311624],[51199.503433999686,46350.09336722177],[51192.21890693945,46339.78200758854],[51185.9395686588,46324.785361455746],[51186.38013501978,46324.89302837197],[51192.6087869472,46339.75994378933]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":14,\"DKMJ\":\"0.00110000\"},\"geometry\":{\"coordinates\":[[[51129.66557102912,46286.16355154291],[51129.205005357755,46286.055990464985],[51116.395237718505,46259.63399730902],[51116.84580375895,46259.74161134054],[51129.66557102912,46286.16355154291]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":15,\"DKMJ\":\"0.65860000\"},\"geometry\":{\"coordinates\":[[[49119.94327717677,46555.18627750408],[49098.87590021001,46568.88274730509],[49082.9040325169,46579.847254066255],[49057.42668123329,46597.322055165656],[49033.84714869261,46614.38681192603],[49016.712889558854,46626.7874681265],[48986.48507760339,46649.83739761171],[48976.14260885296,46647.47216357664],[48974.48771141847,46654.10089556035],[48954.19180151149,46647.298376996994],[48974.14614149503,46631.13780584326],[48994.10048129301,46614.97723557195],[49010.5744454511,46602.52007524017],[49040.673823921185,46579.76582708303],[49070.77320186039,46557.01158079645],[49076.10184802837,46552.98338881041],[49116.32802292608,46527.74554423429],[49156.554196474666,46502.50770242838],[49197.95703174439,46480.41861919267],[49197.6999576648,46482.85996855469],[49197.14554245222,46484.6698950436],[49196.47799981158,46486.83341875533],[49194.90023691341,46490.08775602048],[49192.65920733503,46494.04660130805],[49190.919435700234,46497.86579360813],[49190.24724038554,46499.339345250286],[49189.77987280048,46507.39178137342],[49189.43370746999,46511.89359266916],[49183.83099466694,46515.15323829418],[49162.42082442427,46528.32152300794],[49141.01065378924,46541.48980849236],[49119.94327717677,46555.18627750408]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":16,\"DKMJ\":\"0.44800000\"},\"geometry\":{\"coordinates\":[[[48864.61579356753,46751.852164682],[48861.58013561856,46754.55822499656],[48848.02723902223,46767.21992513677],[48824.96140558007,46788.76195355411],[48824.85199157288,46788.872532370035],[48802.52788076523,46810.7406337671],[48788.96826680762,46824.02236894425],[48781.2101895143,46831.93341084895],[48769.66312469632,46821.14459140831],[48788.178718119794,46801.44164361525],[48806.69431159146,46781.738696821965],[48809.44876123268,46778.804125986055],[48814.93781999351,46772.96508929971],[48839.45661459129,46749.15037824353],[48863.97540912337,46725.3356687841],[48891.7121352479,46700.18892997457],[48919.92948056479,46675.15964803519],[48948.04826440547,46652.290880320135],[48947.615425935845,46653.64316591667],[48959.448645893295,46658.04049858404],[48954.4471072463,46670.966920932755],[48958.4821618197,46671.92555405711],[48937.551269452575,46688.73629397108],[48936.555643729225,46689.561561479226],[48922.517678436554,46684.26590564753],[48915.55461046139,46695.01272325217],[48906.68941295473,46714.799572243355],[48899.15317516908,46721.169443903025],[48864.61579356753,46751.852164682]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":17,\"DKMJ\":\"0.00180000\"},\"geometry\":{\"coordinates\":[[[49676.86621850967,46324.00411468231],[49658.25527593384,46327.58261801489],[49679.19369869866,46321.64180624764],[49676.86621850967,46324.00411468231]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":18,\"DKMJ\":\"0.01550000\"},\"geometry\":{\"coordinates\":[[[49529.96806872299,46480.97094707284],[49526.48786918712,46480.92937150597],[49528.246951224384,46476.980079440866],[49531.67438845872,46472.721956728026],[49535.305259885514,46469.11275369488],[49538.02563281398,46465.4083713647],[49538.02557987621,46465.39837169648],[49538.68089895001,46464.51490705088],[49545.79166336135,46464.66726194416],[49547.67361115646,46465.037296998315],[49548.67320663092,46464.96200554306],[49548.36658021039,46475.0435761204],[49530.28375304533,46474.48930955818],[49529.96806872299,46480.97094707284]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":19,\"DKMJ\":\"0.03870000\"},\"geometry\":{\"coordinates\":[[[49465.814546149864,46504.790454725735],[49452.85572584531,46504.99905821728],[49451.56606441927,46497.505923913326],[49409.728490502675,46496.40741893742],[49407.37817625966,46492.189882803716],[49407.36764721867,46492.08993904944],[49408.37734842007,46492.0345939449],[49435.69690176317,46491.9799638167],[49462.79932019627,46492.46648043487],[49465.4995464418,46492.51218513213],[49466.29983698489,46492.56794808619],[49465.814546149864,46504.790454725735]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":20,\"DKMJ\":\"0.00530000\"},\"geometry\":{\"coordinates\":[[[49338.03076627597,46658.826186873484],[49334.479774053696,46658.63498766394],[49333.3666697277,46652.38091155421],[49333.72002795442,46647.349065601826],[49338.901865268366,46647.70162998886],[49338.03076627597,46658.826186873484]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":21,\"DKMJ\":\"1.55050000\"},\"geometry\":{\"coordinates\":[[[51616.81666445442,46440.15344357537],[51617.81161525546,46444.86813831608],[51618.77667306313,46449.6029911358],[51619.721890452514,46454.36794873513],[51620.63721484966,46459.15306441346],[51621.55269809112,46463.96817899821],[51622.45828760149,46468.80334578641],[51623.363983025534,46473.65851184074],[51624.279783987105,46478.53362422437],[51625.195584980385,46483.40873659542],[51626.06964052168,46487.95407261932],[51619.06566307857,46487.1911581885],[51603.60377965357,46481.14306280995],[51599.307172643945,46483.66578895924],[51595.46373103393,46488.67609608453],[51592.676151538224,46485.35088036349],[51587.6144147924,46479.34772557719],[51601.35175837487,46467.53509448236],[51590.384557611265,46454.82325708307],[51579.18462101507,46462.37248916272],[51576.28117512926,46459.82788041886],[51563.73844016351,46470.624195592005],[51554.33193384661,46461.82406405826],[51551.130315569295,46465.290985845495],[51544.56793189506,46472.38567075971],[51531.56551648442,46460.57459973125],[51518.71668972458,46447.55272478192],[51520.29027929464,46446.34440378426],[51516.55516410599,46441.594215090394],[51509.80309891367,46446.85991876713],[51486.42491681059,46432.05379939964],[51467.09298805202,46447.90301762475],[51445.96562456061,46410.3991592601],[51470.057060091945,46417.32056617644],[51485.1298545132,46421.65073739271],[51485.40026812864,46421.72930521564],[51509.30639691691,46428.59269283758],[51519.638084812126,46393.03827903187],[51513.05805590081,46391.13312826678],[51504.87565046811,46388.77646387974],[51473.948714736085,46379.86025853967],[51456.1617315921,46374.73446126329],[51445.96061944978,46410.398185763974],[51467.08900408305,46447.906038691755],[51465.37642533347,46449.305094088886],[51481.82192576234,46473.03784478968],[51500.245013415646,46492.540158371914],[51515.59206708032,46509.00877977442],[51525.943839596024,46520.69388396526],[51530.46935555984,46527.40987212769],[51533.38290736924,46529.97442710819],[51548.522322107616,46537.44421791844],[51549.73984794876,46538.86776085198],[51528.77445793154,46549.14867254533],[51516.84876056436,46532.94193715835],[51503.72971315568,46514.211538891774],[51478.66572519242,46484.139466441695],[51453.601737627534,46454.06739192875],[51434.69246817969,46431.50767384376],[51416.09069958851,46408.47633037203],[51383.37862504112,46375.91975812289],[51379.43748751389,46371.920653025154],[51398.64933840986,46376.078915335704],[51404.82631258818,46377.40620476799],[51411.51747041278,46349.301001106855],[51413.52287625869,46340.88045030227],[51415.93649378419,46341.56766762864],[51417.739180723045,46342.07812050264],[51441.7852529052,46348.93077062676],[51465.83132437485,46355.783420296386],[51476.1367757835,46358.71884149592],[51513.91346547967,46369.48877120763],[51523.64803725012,46372.25721551152],[51532.2910078485,46374.72144098208],[51556.66754089349,46381.662338788155],[51552.0926225148,46397.72642991785],[51564.64143152373,46401.29996888805],[51566.303964476945,46401.781163705506],[51576.45915548921,46404.66737970291],[51579.06308317708,46405.41358869709],[51598.301915978314,46410.89169549011],[51609.018504337626,46410.26496749604],[51609.60838223117,46412.13182968553],[51610.514177684105,46415.11701041833],[51611.3800804944,46418.12240217067],[51612.21609029045,46421.14795200526],[51613.032153768414,46424.18360734685],[51613.80832460672,46427.23947371123],[51614.56460206977,46430.3154452201],[51615.30098615378,46433.4115218781],[51616.00742429922,46436.51775697945],[51616.81666445442,46440.15344357537]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":22,\"DKMJ\":\"0.00210000\"},\"geometry\":{\"coordinates\":[[[49128.47264365643,47227.46299833758],[49126.82941561911,47226.8517021113],[49127.477404686186,47218.9183078073],[49129.88136398084,47210.225619171746],[49128.47264365643,47227.46299833758]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":23,\"DKMJ\":\"0.03970000\"},\"geometry\":{\"coordinates\":[[[50522.036041756895,47671.66111971159],[50524.13311681598,47673.000005558126],[50543.03665030569,47677.46986953774],[50553.90606317916,47679.26229697],[50555.17919556662,47679.85555097787],[50527.56098754882,47679.21181203797],[50499.94277848607,47678.56807304499],[50499.3471331773,47677.501234329306],[50498.83882824677,47674.04394865269],[50499.37882440955,47670.4561123806],[50499.54698801163,47669.354229020886],[50499.97697030103,47667.65196307051],[50501.38550881931,47662.090540175326],[50503.03979286796,47662.90177439153],[50506.201011362245,47664.45802355511],[50515.2878503695,47666.89488695236],[50516.57266908583,47667.24008074775],[50518.92846407379,47668.3375981059],[50522.036041756895,47671.66111971159]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}},{\"type\":\"Feature\",\"properties\":{\"SSQY\":\"\",\"PCBH\":\"\",\"DKZL\":\"\",\"DKH\":24,\"DKMJ\":\"0.00500000\"},\"geometry\":{\"coordinates\":[[[49339.227669006825,46578.91024486814],[49338.5145400814,46585.87398588378],[49342.96211668058,46517.42077823542],[49343.44220638874,46517.43823652669],[49343.01704972482,46522.13046412496],[49341.804388586155,46555.62671844056],[49339.227669006825,46578.91024486814]]],\"type\":\"Polygon\"},\"crs\":{\"type\":\"value\",\"properties\":{\"value\":\"PROJCS[\\\"Suzhou_1954_3_Degree_GK_CM_120E\\\",GEOGCS[\\\"GCS_Beijing_1954\\\",DATUM[\\\"D_Beijing_1954\\\",SPHEROID[\\\"Krasovsky_1940\\\",6378245.0,298.3]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Gauss_Kruger\\\"],PARAMETER[\\\"False_Easting\\\",50805.0],PARAMETER[\\\"False_Northing\\\",-3421129.0],PARAMETER[\\\"Central_Meridian\\\",120.5833333333333],PARAMETER[\\\"Scale_Factor\\\",1.0],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}}}]}";
		Object object = geometryService.readUnTypeGeoJSON(geojson);

		String layerName = "SDE.SGBWM_TEMP";

		String result = gisService.insert2(layerName, geojson, true, "sde");
		print(result);
		print("--");
	}


	@Test
	public void testReadGeoJSON() throws Exception {
		String geo = "{ \"type\": \"Polygon\"," +
				"  \"coordinates\": [" +
				"    [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]" +
				"    ]" +
				" }";

		SimpleFeature geometry = geometryService.readFeatureJSON(geo);
		geometry.getDefaultGeometry();
		String test = "{\"供地分析\":\"{\\\"type\\\":\\\"FeatureCollection\\\",\\\"bbox\\\":[4.0351502752852134E7,3486255.776239368,4.035194853590048E7,3486515.8163509057],\\\"features\\\":[{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.0351502752852134E7,3486255.776239368],[4.035181246273991E7,3486267.5337443664],[4.03518592204E7,3486352.8491000007],[4.0351931312E7,3486484.3891999996],[4.035194853590048E7,3486515.8163509057],[4.0351502752852134E7,3486255.776239368]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":37647.84048921002,\\\"OG_SHAPE_AREA\\\":245752.98595745812,\\\"XMBH\\\":\\\"挂10-45\\\"},\\\"id\\\":\\\"Featuref14c73ea5cd24028b2294c73cd6f00c9\\\"}]}\",\"报批分析\":\"{\\\"type\\\":\\\"FeatureCollection\\\",\\\"bbox\\\":[4.0351502752852134E7,3486255.776239368,4.03528271419E7,3486724.1218103743],\\\"features\\\":[{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.03524135654E7,3486539.8603000003],[4.03524641358E7,3486626.7441],[4.035230563097386E7,3486724.1218103743],[4.035220616223908E7,3486666.098381755],[4.03524135654E7,3486539.8603000003]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":21643.458076760944,\\\"OG_SHAPE_AREA\\\":26905.995302458872,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2b4028b2294c73cd6f00cc\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.035198639649245E7,3486274.1367849736],[4.0352159269995905E7,3486280.6995753814],[4.03521530621E7,3486284.6062],[4.03521464549E7,3486288.2651],[4.0352139085E7,3486292.4618],[4.03521287944E7,3486294.8864],[4.03521267003E7,3486295.8358000005],[4.03521257088E7,3486296.2104],[4.03521225076E7,3486297.4092],[4.03521179167E7,3486299.0038],[4.03521144613E7,3486300.7371999994],[4.03521107131E7,3486303.999],[4.0352108967E7,3486305.7547],[4.03521061777E7,3486307.6284999996],[4.03521024331E7,3486310.8283],[4.0352098047E7,3486314.683],[4.03520874687E7,3486316.8069],[4.03520820317E7,3486318.0518],[4.03520773626E7,3486319.4408000004],[4.03520717573E7,3486321.3133000005],[4.03520673231E7,3486325.5201],[4.03520655108E7,3486327.9174000006],[4.03520611913E7,3486330.5363000003],[4.03520599461E7,3486331.7062],[4.03520644121E7,3486340.0285999994],[4.03520882314E7,3486328.1167000006],[4.03521159519E7,3486317.4824999995],[4.03521284339E7,3486318.7779],[4.03521331984E7,3486319.0818],[4.03521332066E7,3486319.0822],[4.03521361073E7,3486317.5685000005],[4.03521361096E7,3486317.5614],[4.03521361161E7,3486317.558],[4.0352137528E7,3486313.1616999996],[4.03521407317E7,3486306.1616],[4.03521428848E7,3486302.8243],[4.03521450544E7,3486300.9592],[4.0352175281069905E7,3486281.307403191],[4.035226506347361E7,3486284.7158092577],[4.03522744887E7,3486300.8957],[4.03522493287E7,3486317.5745],[4.03522640141E7,3486338.7519],[4.03522381552E7,3486354.5313],[4.03521865434E7,3486386.0526],[4.03521588015E7,3486402.9698],[4.03520838292E7,3486448.7324],[4.03520751694E7,3486433.4735999997],[4.03520536572E7,3486398.6237],[4.0352031771E7,3486359.2735999995],[4.03520227111E7,3486342.982],[4.03520098027E7,3486319.7646],[4.03520000229E7,3486297.9857],[4.03519966081E7,3486292.0186],[4.03519922179E7,3486284.3267999995],[4.035198639649245E7,3486274.1367849736]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":27187.009617323813,\\\"OG_SHAPE_AREA\\\":54571.26691738332,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2c4028b2294c73cd6f00cd\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.0352542910763055E7,3486295.2637156155],[4.035263111258869E7,3486298.612118256],[4.03526855082E7,3486392.0692],[4.03526559531E7,3486409.3350999993],[4.03526283404E7,3486425.4662000006],[4.0352615667E7,3486432.8699],[4.03525149976E7,3486491.6802000003],[4.03524529235E7,3486527.9435],[4.03524045555E7,3486444.8413],[4.0352399977E7,3486436.9748999993],[4.03524004533E7,3486436.4722999996],[4.03524015231E7,3486435.3431999995],[4.03524038774E7,3486434.2578],[4.03524044185E7,3486429.1384],[4.03524035983E7,3486424.0561],[4.03524031463E7,3486419.9848],[4.03524029695E7,3486413.5237],[4.03524045551E7,3486409.2367999996],[4.03524038574E7,3486409.2559],[4.03524034783E7,3486408.9488],[4.03524030135E7,3486408.5723],[4.03524021696E7,3486407.8886],[4.03523996895E7,3486407.7208999996],[4.03523978016E7,3486407.6548],[4.03523949842E7,3486408.0852],[4.03523932302E7,3486408.6044],[4.03523916827E7,3486408.0578],[4.03523903677E7,3486407.3870000006],[4.03523893641E7,3486405.1765],[4.03523852423E7,3486401.9843999995],[4.03523817343E7,3486403.3859],[4.03523808579E7,3486404.1266],[4.03523728226E7,3486390.3213],[4.03523570376E7,3486363.2014],[4.03523646028E7,3486361.1827999996],[4.03523804981E7,3486377.5838],[4.03523809808E7,3486376.8156],[4.03523836383E7,3486376.1317000003],[4.03523904533E7,3486374.3778],[4.03523972843E7,3486375.0079],[4.03524033876E7,3486378.9259],[4.03524067768E7,3486380.0009],[4.0352409711E7,3486380.9315],[4.03524120382E7,3486381.9402],[4.03524138611E7,3486380.7543],[4.03524447772E7,3486360.642],[4.03524998821E7,3486324.7936],[4.03525016082E7,3486328.8197],[4.0352506667E7,3486340.6191000002],[4.03525155325E7,3486365.8998],[4.03525216223E7,3486383.0889],[4.03525277899E7,3486409.9155],[4.0352546691E7,3486413.5429999996],[4.0352536518E7,3486367.3674],[4.03525216603E7,3486328.4682],[4.03525156437E7,3486310.7164000003],[4.03525334195E7,3486300.6426],[4.0352542910763055E7,3486295.2637156155]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":37718.44985740608,\\\"OG_SHAPE_AREA\\\":39555.65197596982,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2d4028b2294c73cd6f00ce\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.03523896325806E7,3486289.444821652],[4.0352542910763055E7,3486295.2637156155],[4.03525334195E7,3486300.6426],[4.03525156437E7,3486310.7164000003],[4.03525144627E7,3486311.8555],[4.0352507535E7,3486318.4371],[4.03525012708E7,3486323.296],[4.03524998821E7,3486324.7936],[4.03524447772E7,3486360.642],[4.03524138611E7,3486380.7543],[4.03524120382E7,3486381.9402],[4.03524194277E7,3486351.6282],[4.03523825909E7,3486331.1581],[4.03523749027E7,3486344.7926],[4.03523646028E7,3486361.1827999996],[4.03523570376E7,3486363.2014],[4.03523307684E7,3486318.0687000006],[4.03523430917E7,3486310.8382],[4.03523473295E7,3486318.7893],[4.0352380389E7,3486299.3923000004],[4.03523852321E7,3486294.1804],[4.03523896325806E7,3486289.444821652]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":9140.903038352173,\\\"OG_SHAPE_AREA\\\":28136.164467967315,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2d4028b2294c73cd6f00cf\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.035263111258869E7,3486298.612118256],[4.0352826345197976E7,3486306.023726572],[4.0352826755E7,3486307.6365000005],[4.03528271419E7,3486309.328],[4.03527149806E7,3486374.8516],[4.0352711492E7,3486376.8896],[4.03526855082E7,3486392.0692],[4.035263111258869E7,3486298.612118256]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":9188.342746580987,\\\"OG_SHAPE_AREA\\\":27521.607536741994,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2e4028b2294c73cd6f00d0\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.0351502752852134E7,3486255.776239368],[4.035181246273991E7,3486267.5337443664],[4.03518592204E7,3486352.8491000007],[4.0351931312E7,3486484.3891999996],[4.035194853590048E7,3486515.8163509057],[4.0351502752852134E7,3486255.776239368]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"马鞍山四方物流中心\\\",\\\"BPZT\\\":\\\"1\\\",\\\"SHAPE_AREA\\\":37647.84048921002,\\\"OG_SHAPE_AREA\\\":245752.98595745812,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d2f4028b2294c73cd6f00d1\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.035193655098124E7,3486272.244501677],[4.035198638731999E7,3486274.136436758],[4.03519922182E7,3486284.3475],[4.03519966081E7,3486292.0186],[4.0352000014E7,3486297.9858],[4.03520098027E7,3486319.7646],[4.03520227113E7,3486342.9923],[4.0352031771E7,3486359.2735999995],[4.03520536483E7,3486398.6237999997],[4.03520751694E7,3486433.4735999997],[4.03520838292E7,3486448.7324],[4.03520459044E7,3486471.8862000005],[4.035193655098124E7,3486272.244501677]]],[[[4.0352159269995905E7,3486280.6995753814],[4.035217527937072E7,3486281.3073386843],[4.03521450456E7,3486300.9594],[4.03521428848E7,3486302.8243],[4.03521407227E7,3486306.1514],[4.0352137528E7,3486313.1616999996],[4.03521361096E7,3486317.5614],[4.03521331984E7,3486319.0818],[4.03521284337E7,3486318.7676],[4.03521159519E7,3486317.4824999995],[4.03520882314E7,3486328.1167000006],[4.03520644121E7,3486340.0285999994],[4.03520599549E7,3486331.7061000005],[4.03520611913E7,3486330.5363000003],[4.03520655108E7,3486327.9174000006],[4.03520673231E7,3486325.5201],[4.03520717662E7,3486321.3132],[4.03520773626E7,3486319.4408000004],[4.03520820317E7,3486318.0518],[4.03520874776E7,3486316.8067999994],[4.03520980559E7,3486314.6828999994],[4.03521024331E7,3486310.8283],[4.03521061777E7,3486307.6284999996],[4.0352108976E7,3486305.7648999994],[4.0352110722E7,3486303.9989],[4.03521144613E7,3486300.7371999994],[4.03521179167E7,3486299.0038],[4.03521225077E7,3486297.4196000006],[4.03521257088E7,3486296.2104],[4.03521267092E7,3486295.8357],[4.03521287944E7,3486294.8864],[4.0352139085E7,3486292.4618],[4.03521464551E7,3486288.2754],[4.03521530621E7,3486284.6062],[4.0352159269995905E7,3486280.6995753814]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"BPZT\\\":\\\"示范园区\\\",\\\"SHAPE_AREA\\\":10619.352030481876,\\\"OG_SHAPE_AREA\\\":116331.68524018126,\\\"YT\\\":\\\"\\\"},\\\"id\\\":\\\"Featuref14c73ea5d304028b2294c73cd6f00d2\\\"}]}\",\"监测图斑分析\":\"{\\\"type\\\":\\\"FeatureCollection\\\",\\\"bbox\\\":[4.0351930677266404E7,3486272.0215180577,4.035291327140238E7,3486818.3175777895],\\\"features\\\":[{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.035244884334678E7,3486291.6926377756],[4.0352478928204715E7,3486292.834748123],[4.0352502907E7,3486306.3228999996],[4.0352515520850256E7,3486294.223913371],[4.035281242576678E7,3486305.4953037207],[4.03528162432E7,3486326.3443],[4.03528693001E7,3486328.3465],[4.03528923248E7,3486328.3465],[4.035291327140238E7,3486450.721312375],[4.035287254977556E7,3486493.592136162],[4.03528001547E7,3486363.0437],[4.03527515724E7,3486385.3653],[4.03527279377E7,3486357.7916000006],[4.03524416961E7,3486527.1731],[4.03523936242E7,3486447.7896],[4.03524144795E7,3486442.1646],[4.03524352059E7,3486437.8342],[4.03524300289E7,3486422.1853999994],[4.03524223467E7,3486402.6165],[4.03524153664E7,3486404.5623],[4.03524116078E7,3486408.2919],[4.03524028529E7,3486408.9994],[4.03523961543E7,3486407.8958],[4.0352390985E7,3486407.1013],[4.03523871386E7,3486403.3459000005],[4.03523833753E7,3486402.6299000005],[4.03523795943E7,3486405.5410999996],[4.03523790272E7,3486410.4702],[4.03523781872E7,3486419.2675],[4.03523796267E7,3486424.6748],[4.03523260289E7,3486336.1662],[4.03523847803E7,3486301.3175],[4.03524448448E7,3486292.3077999996],[4.035244884334678E7,3486291.6926377756]],[[4.03523826219E7,3486332.3396],[4.0352363563E7,3486366.6785],[4.03523814016E7,3486377.5311],[4.03523853123E7,3486375.0843],[4.03523932329E7,3486373.8147],[4.03523982949E7,3486374.963],[4.0352407333E7,3486380.3313],[4.03524133814E7,3486383.3245],[4.03524191972E7,3486352.163],[4.03523826219E7,3486332.3396]]],[[[4.035280908531448E7,3486560.4061104627],[4.035279298304351E7,3486577.358223515],[4.03527905134E7,3486573.2514],[4.03527942727E7,3486570.9806],[4.035280908531448E7,3486560.4061104627]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":69601.88019097602,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":174106.7850052754,\\\"JCBH\\\":\\\"787\\\"},\\\"id\\\":\\\"Featuref14c73ea5d844028b2294c73cd6f00d4\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.03526995796E7,3486641.8937000004],[4.035271150682149E7,3486663.1345794797],[4.035264921723498E7,3486728.711671949],[4.0352652969E7,3486713.3068000004],[4.03526678912E7,3486707.4247999997],[4.03526565214E7,3486687.5439],[4.03526366427E7,3486701.8768000007],[4.03526305876E7,3486707.19],[4.03526238553E7,3486709.1291],[4.03526180262E7,3486709.8736],[4.03526106645E7,3486710.192],[4.03526069638E7,3486707.4855],[4.03526043312E7,3486705.3348],[4.03526044899E7,3486698.311],[4.03525975127E7,3486695.5772],[4.03525925005E7,3486691.9707],[4.03526305838E7,3486673.1462999997],[4.03526957637E7,3486642.8434],[4.03526995796E7,3486641.8937000004]]],[[[4.035263756045485E7,3486740.9836710193],[4.035260194024673E7,3486778.483834569],[4.03525871377E7,3486755.6978999996],[4.03526025771E7,3486756.5294],[4.03526058278E7,3486741.9654000006],[4.035263756045485E7,3486740.9836710193]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":4553.899683114466,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":28440.61657819622,\\\"JCBH\\\":\\\"778\\\"},\\\"id\\\":\\\"Featuref14c73ea5d864028b2294c73cd6f00d5\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.03525771172E7,3486755.1582000004],[4.03525871377E7,3486755.6978999996],[4.035260194024673E7,3486778.483834569],[4.035256410344578E7,3486818.3175777895],[4.03525578317E7,3486806.912],[4.03525554643E7,3486781.7814],[4.03525555296E7,3486770.8321],[4.03525580089E7,3486768.0882],[4.03525772333E7,3486768.2919999994],[4.03525771172E7,3486755.1582000004]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":1623.3403095807444,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":95125.45213851533,\\\"JCBH\\\":\\\"783\\\"},\\\"id\\\":\\\"Featuref14c73ea5d864028b2294c73cd6f00d6\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.0351930677266404E7,3486272.0215180577],[4.0352267880724914E7,3486284.822760464],[4.03523983659E7,3486498.2863999996],[4.03524154353E7,3486542.9296000004],[4.03524104921E7,3486545.9086],[4.0352217167E7,3486662.4158],[4.03521488891E7,3486591.5119000003],[4.03520136465E7,3486343.3482],[4.03519624381E7,3486353.8525],[4.03519532469E7,3486313.1484],[4.0351930677266404E7,3486272.0215180577]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":99171.74894380978,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":150100.29896367047,\\\"JCBH\\\":\\\"790\\\"},\\\"id\\\":\\\"Featuref14c73ea5d874028b2294c73cd6f00d7\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.0352267880724914E7,3486284.8227604646],[4.035229711598577E7,3486285.93261759],[4.03523174349E7,3486321.9744999995],[4.03523260289E7,3486336.1662],[4.03523796267E7,3486424.6748],[4.03523812172E7,3486430.6496],[4.03523826503E7,3486435.9919000003],[4.03523910372E7,3486448.4873],[4.03523936242E7,3486447.7896],[4.03524416961E7,3486527.1731],[4.0352537695E7,3486686.0287],[4.03525279312E7,3486699.2507],[4.03525222588E7,3486705.7234],[4.03525156114E7,3486706.4902],[4.03525152796E7,3486715.7415],[4.03525106955E7,3486721.1918],[4.03524627549E7,3486634.0087],[4.03524104921E7,3486545.9086],[4.03524154353E7,3486542.9296000004],[4.03523983659E7,3486498.2863999996],[4.0352267880724914E7,3486284.8227604646]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":14002.0977813706,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":24773.606383931838,\\\"JCBH\\\":\\\"792\\\"},\\\"id\\\":\\\"Featuref14c73ea5d884028b2294c73cd6f00d8\\\"},{\\\"type\\\":\\\"Feature\\\",\\\"geometry\\\":{\\\"type\\\":\\\"MultiPolygon\\\",\\\"coordinates\\\":[[[[4.03527279377E7,3486357.7916000006],[4.03527515724E7,3486385.3653],[4.03528001547E7,3486363.0437],[4.035287254977556E7,3486493.592136163],[4.035283879495812E7,3486529.128457857],[4.03528342866E7,3486528.3543999996],[4.03528269907E7,3486526.7992],[4.03528220169E7,3486524.5954999994],[4.0352819639E7,3486518.9281000006],[4.03528196535E7,3486515.184],[4.03528177145E7,3486508.4517],[4.03528133798E7,3486499.6791],[4.03528086122E7,3486481.5007],[4.03528034145E7,3486471.1138],[4.03527939701E7,3486450.8991],[4.03527912346E7,3486462.0879000006],[4.0352785778E7,3486480.7215999993],[4.03527774325E7,3486496.3925999994],[4.03527743928E7,3486505.0159999994],[4.03527750008E7,3486518.6872],[4.03527717369E7,3486527.6677000006],[4.03527656527E7,3486540.469],[4.03527643601E7,3486548.8107],[4.03527676395E7,3486557.4952],[4.03527697562E7,3486566.4453999996],[4.03527695069E7,3486576.5719],[4.03527732971E7,3486576.3778000004],[4.03527744136E7,3486576.3206],[4.0352778196E7,3486577.738],[4.03527823036E7,3486578.2105],[4.03527905134E7,3486573.2514],[4.0352792983043514E7,3486577.3582235165],[4.035271322780839E7,3486661.322762711],[4.03527017743E7,3486640.2731],[4.0352707148E7,3486636.3051],[4.03527279107E7,3486621.2302000006],[4.03527195452E7,3486614.7907],[4.03527155292E7,3486604.8394999993],[4.03527057833E7,3486599.2567],[4.03526992304E7,3486599.202],[4.03526918638E7,3486603.6152],[4.03526886993E7,3486607.6795],[4.03526817521E7,3486618.8665999994],[4.03526807878E7,3486626.3803],[4.03526842849E7,3486634.4739],[4.03526879598E7,3486644.7855],[4.03526957637E7,3486642.8434],[4.03526305838E7,3486673.1462999997],[4.03525917078E7,3486691.2202],[4.03525831326E7,3486683.1018],[4.03525799076E7,3486676.4047],[4.03525789164E7,3486676.0188999996],[4.03525494349E7,3486670.516],[4.03525478806E7,3486673.0703999996],[4.03525432733E7,3486678.4747000006],[4.03525403348E7,3486682.4539],[4.0352537695E7,3486686.0287],[4.03524416961E7,3486527.1731],[4.03527279377E7,3486357.7916000006]]]]},\\\"properties\\\":{\\\"IN_SHAPE_AREA\\\":431219.12359276,\\\"XMMC\\\":\\\"\\\",\\\"SHAPE_AREA\\\":77052.45997993274,\\\"BSM\\\":\\\"\\\",\\\"OG_SHAPE_AREA\\\":100558.11596473299,\\\"JCBH\\\":\\\"786\\\"},\\\"id\\\":\\\"Featuref14c73ea5d894028b2294c73cd6f00d9\\\"}]}\"}";


//        Geometry geometry = geometryService.readGeoJSON(geo);
//
//        System.out.print(geometry.toText());

		//
		String geoCollection = "{ \"type\": \"GeometryCollection\"," +
				"  \"geometries\": [" +
				"    { \"type\": \"Point\"," +
				"      \"coordinates\": [100.0, 0.0]" +
				"      }," +
				"    { \"type\": \"LineString\"," +
				"      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]" +
				"      }" +
				"  ]" +
				"}";

//        geometry = geometryService.readGeoJSON(geoCollection);

//        System.out.print(geometry);
		//
	}

	public static enum Type {
		ARC_SDE, ORACLE_SPATIAL
	}

	@Test
	public void testProject() throws Exception {

//        String geoJson ="{\"type\":\"Feature\",\"id\":\"OpenLayers.Feature.Vector_314\",\"properties\":{},\"geometry\":{\"type\":\"Point\",\"coordinates\":[97.03125,39.7265625]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}}}";
		String geoJson = "{" +
				"    \"type\": \"Feature\"," +
				"    \"id\": \"OpenLayers.Feature.Vector_314\"," +
				"    \"properties\": {}," +
				"    \"geometry\": {" +
				"        \"type\": \"Point\"," +
				"        \"coordinates\": [" +
				"            97.03125," +
				"            39.7265625" +
				"        ]" +
				"    }," +
				"    \"crs\": {" +
				"        \"type\": \"name\"," +
				"        \"properties\": {" +
				"            \"name\": \"EPSG:4610\"" +
				"        }" +
				"    }" +
				"}";

		String feaCollection = "{ \"type\": \"Feature\",\n" +
				"       \"geometry\": {\n" +
				"         \"type\": \"Polygon\",\n" +
				"         \"coordinates\": [\n" +
				"           [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],\n" +
				"             [100.0, 1.0], [100.0, 0.0] ]\n" +
				"           ]\n" +
				"       },\n" +
				"       \"properties\": {\n" +
				"         \"prop0\": \"value0\",\n" +
				"         \"prop1\": {\"this\": \"that\"}\n" +
				"         }\n" +
				"       }";


//        FeatureJSON json = new FeatureJSON();
//        SimpleFeature sp = json.readFeature(feaCollection);


//        SimpleFeature feature = geometryService.readFeatureJSON(feaCollection);
//        Object geo = feature.getDefaultGeometry();
//        Object attr = feature.getDefaultGeometryProperty();

		Geometry geo = geometryService.readGeoJSON(feaCollection);


		System.out.println(geo);
	}

	@Test
	public void testGetCRSByWKTString() throws Exception {
		String geoJson = "{" +
				"    \"type\": \"Feature\"," +
				"    \"id\": \"OpenLayers.Feature.Vector_314\"," +
				"      \"properties\": {\n" +
				"        \"prop0\": \"value0\",\n" +
				"        \"prop5\": \"we\"\n" +
				"        },\n" +
				"    \"geometry\": {" +
				"        \"type\": \"Point\"," +
				"        \"coordinates\": [" +
				"            97.03125," +
				"            39.7265625" +
				"        ]" +
				"    }," +
				"    \"crs\": {" +
				"        \"type\": \"name\"," +
				"        \"properties\": {" +
				"            \"name\": \"EPSG:4610\"" +
				"        }" +
				"    }" +
				"}";

		String geoCollection = "{ \"type\": \"GeometryCollection\",\n" +
				"  \"geometries\": [\n" +
				"    { \"type\": \"Point\",\n" +
				"      \"coordinates\": [100.0, 0.0]\n" +
				"      },\n" +
				"    { \"type\": \"LineString\",\n" +
				"      \"coordinates\": [ [101.0, 0.0], [102.0, 1.0] ]\n" +
				"      }\n" +
				"  ]\n" +
				"}";

		String feaCollection = "{ \"type\": \"FeatureCollection\",\n" +
				"  \"features\": [\n" +
				"    { \"type\": \"Feature\",\n" +
				"      \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]},\n" +
//                "      \"properties\": {\"prop0\": \"value0\"}\n" +
				"      },\n" +
				"    { \"type\": \"Feature\",\n" +
				"      \"geometry\": {\n" +
				"        \"type\": \"LineString\",\n" +
				"        \"coordinates\": [\n" +
				"          [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
				"          ]\n" +
				"        },\n" +
				"      \"properties\": {\n" +
				"        \"prop0\": \"value0\",\n" +
				"        \"prop5\": \"we\"\n" +
				"        }\n" +
				"      },\n" +
				"    { \"type\": \"Feature\",\n" +
				"       \"geometry\": {\n" +
				"         \"type\": \"Polygon\",\n" +
				"         \"coordinates\": [\n" +
				"           [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],\n" +
				"             [100.0, 1.0], [100.0, 0.0] ]\n" +
				"           ]\n" +
				"       },\n" +
				"       \"properties\": {\n" +
				"         \"prop0\": \"value0\",\n" +
				"         \"prop2\": \"value2\"\n" +
				"         }\n" +
				"       }\n" +
				"     ]\n" +
				"   }";

		String feaJ = "{\n" +
				"   \"type\": \"Feature\",\n" +
				"   \"geometry\": {\n" +
				"       \"type\": \"LineString\",\n" +
				"       \"coordinates\": [\n" +
				"           [100.0, 0.0], [101.0, 1.0]\n" +
				"       ]\n" +
				"   },\n" +
				"   \"properties\": {\n" +
				"       \"prop0\": \"value0\",\n" +
				"       \"prop1\": \"value1\"\n" +
				"   }\n" +
				"}";

		String feaColl = "{\n" +
				"   \"type\": \"Feature\",\n" +
				"   \"geometry\": {\n" +
				"      \"type\": \"GeometryCollection\",\n" +
				"      \"geometries\": [\n" +
				"          {\n" +
				"            \"type\": \"Point\",\n" +
				"            \"coordinates\": [100.0, 0.0]\n" +
				"          },\n" +
				"          {\n" +
				"            \"type\": \"LineString\",\n" +
				"            \"coordinates\": [\n" +
				"                [101.0, 0.0], [102.0, 1.0]\n" +
				"            ]\n" +
				"          }\n" +
				"      ]\n" +
				"   },\n" +
				"   \"properties\": {\n" +
				"       \"prop0\": \"value0\",\n" +
				"       \"prop1\": \"value1\"\n" +
				"   }\n" +
				"}";

		String feaCol2 = "{\n" +
				"   \"type\": \"FeatureCollection\",\n" +
				"   \"features\": [\n" +
				"       {\n" +
				"           \"type\": \"Feature\",\n" +
				"           \"id\": \"id0\",\n" +
				"           \"geometry\": {\n" +
				"               \"type\": \"LineString\",\n" +
				"               \"coordinates\": [\n" +
				"                   [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
				"               ]\n" +
				"           },\n" +
				"           \"properties\": {\n" +
				"               \"prop0\": \"value0\",\n" +
				"               \"prop1\": \"value1\"\n" +
				"           }\n" +
				"       },\n" +
				"       {\n" +
				"           \"type\": \"Feature\",\n" +
				"           \"id\": \"id1\",\n" +
				"           \"geometry\": {\n" +
				"               \"type\": \"Polygon\",\n" +
				"               \"coordinates\": [\n" +
				"                   [\n" +
				"                       [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]\n" +
				"                   ]\n" +
				"               ]\n" +
				"           },\n" +
				"           \"properties\": {\n" +
				"               \"prop0\": \"value0\",\n" +
				"               \"prop1\": \"value1\"\n" +
				"           }\n" +
				"       }\n" +
				"   ]\n" +
				"}";

		String feaColl3 = "{\n" +
				"   \"type\": \"FeatureCollection\",\n" +
				"   \"crs\": {\n" +
				"      \"type\": \"EPSG\",\n" +
				"      \"properties\": { \n" +
				"         \"code\": 4326,\n" +
				"         \"coordinate_order\": [1, 0]\n" +
				"      }\n" +
				"    },\n" +
				"   \"features\": [\n" +
				"       {\n" +
				"           \"type\": \"Feature\",\n" +
				"           \"id\": \"id0\",\n" +
				"           \"geometry\": {\n" +
				"               \"type\": \"LineString\",\n" +
				"               \"coordinates\": [\n" +
				"                   [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
				"               ]\n" +
				"           },\n" +
				"           \"properties\": {\n" +
				"               \"prop0\": \"value0\",\n" +
				"               \"prop1\": \"value1\"\n" +
				"           }\n" +
				"       },\n" +
				"       {\n" +
				"           \"type\": \"Feature\",\n" +
				"           \"id\": \"id1\",\n" +
				"           \"geometry\": {\n" +
				"               \"type\": \"Polygon\",\n" +
				"               \"coordinates\": [\n" +
				"                   [\n" +
				"                       [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]\n" +
				"                   ]\n" +
				"               ]\n" +
				"           },\n" +
				"           \"properties\": {\n" +
				"               \"prop0\": \"value0\",\n" +
				"               \"prop1\": \"value1\"\n" +
				"           }\n" +
				"       }\n" +
				"   ]\n" +
				"}";

		Object v = geometryService.readUnTypeGeoJSON(geoJson);
		if (v instanceof SimpleFeature) {
			SimpleFeature sf = (SimpleFeature) v;
			print(sf.getType());
			print(sf.getValue());
			print(sf.getProperties());
			print(sf.getName());
			print(sf.getDefaultGeometry());
			print(sf.getAttributes());

			for (Property o : sf.getProperties()) {
				print(o.getValue());
			}

			Map map = geometryService.simpleFeature2Map(sf);
			print(map);
		}

		print(geometryService.readUnTypeGeoJSON(feaColl3).toString());
		print(geometryService.readFeatureJSON(geoJson));

//        print(GeometryUtils.parseGeoJSON(geoCollection));
//          print(geometryService.readGeoJSON(geoCollection));

	}

	@Test
	public void testGetCRSByCommnonString() throws Exception {

	}

	@Test
	public void testGenFeature() {
		String geo = "{ \"type\": \"Polygon\"," +
				"  \"coordinates\": [" +
				"    [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]" +
				"    ]" +
				" }";

		SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
		ftb.setName("feature");
		ftb.add("geometry", Polygon.class);
		ftb.setDefaultGeometry("geometry");
		ftb.add("name", String.class);

		SimpleFeatureType type = ftb.buildFeatureType();

		SimpleFeatureBuilder fb = new SimpleFeatureBuilder(type);
		fb.add(geometryService.readWKT(geo));

		SimpleFeature sf = fb.buildFeature("fid.001");

		Map map = geometryService.simpleFeature2Map(sf);

		for (Property o : sf.getProperties()) {
			print(o.getName().getLocalPart() + "--" + o.getValue());
		}


		print(map);

		FeatureJSON featureJSON = new FeatureJSON();

		StringWriter out = new StringWriter();

		try {
			featureJSON.writeFeature(sf, out);
		} catch (IOException e) {
			e.printStackTrace();
		}

		print(out.toString());


//        SimpleFeature sf = SimpleFeatureBuilder.build(SimpleFeatureTypeBuilder.)

	}

//    private void print(Object value) {
//        System.out.println(value);
//    }

	@Test
	public void testGeoJSON() throws Exception {
		String geo = "{ \"type\": \"Polygon\"," +
				"  \"coordinates\": [" +
				"    [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]" +
				"    ]" +
				" }";

		SimpleFeatureType featureType;
		SimpleFeatureBuilder fb;

		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName("feature");
		tb.setSRS("EPSG:4326");
		tb.add("geometry", Geometry.class);
		tb.add("int", Integer.class);
		tb.add("double", Double.class);
		tb.add("string", String.class);


		featureType = tb.buildFeatureType();
		fb = new SimpleFeatureBuilder(featureType);
		fb.add(geometryService.readGeoJSON(geo));
		fb.add(1.1);
		fb.add(3.3);
		fb.add("test1");

		SimpleFeature sf = fb.buildFeature("feature");

		FeatureJSON fj = new FeatureJSON();
		StringWriter out = new StringWriter();
		fj.writeFeature(sf, out);
		print(out.toString());


	}

	@Test
	public void testFeatureCollection() throws Exception {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName("feature");
		tb.setSRS("EPSG:4326");
//        tb.add("geometry", Geometry.class);
		tb.add("geometry", Geometry.class);
		tb.add("pro1", Integer.class);
		tb.add("pro2", Double.class);
		tb.add("pro3", String.class);

		SimpleFeatureType featureType;
		featureType = tb.buildFeatureType();

		DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureType);
		for (int i = 0; i < 100; i++) {
			collection.add(simpleFeature(i));
		}

		FeatureJSON fj = new FeatureJSON();
		StringWriter out = new StringWriter();
		fj.writeFeatureCollection(collection, out);
		String j = out.toString();
		print(j);

		FeatureJSON featureJSON = new FeatureJSON();
		FeatureCollection fc = featureJSON.readFeatureCollection(j);

		Object o = geometryService.readUnTypeGeoJSON(j);

		print(o);

	}

	private SimpleFeature simpleFeature(int i) {

		String geo = "{ \"type\": \"Polygon\"," +
				"  \"coordinates\": [" +
				"    [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]" +
				"    ]" +
				" }";

		SimpleFeatureType featureType;
		SimpleFeatureBuilder fb;

		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName("feature");
		tb.setSRS("EPSG:4326");
		tb.add("geometry", Geometry.class);
		tb.add("pro1", Integer.class);
		tb.add("pro2", Double.class);
		tb.add("pro3", String.class);


		featureType = tb.buildFeatureType();
		fb = new SimpleFeatureBuilder(featureType);
		fb.add(geometryService.readGeoJSON(geo));
		fb.add(1);
		fb.add(3.3);
		fb.add("test1");

		SimpleFeature sf = fb.buildFeature("feature" + i + System.currentTimeMillis());
		return sf;
	}

	@Test
	public void testNormal() throws Exception {

		DecimalFormat df = new DecimalFormat("000000");

		String value = df.format(320 * 100000);

		String value1 = String.format("%06d", 320 * 100000);

		String value2 = formatRegionCode("320");
		value2 = formatRegionCode("3201");
		value2 = formatRegionCode("32012");
		value2 = formatRegionCode("320123");
		value2 = formatRegionCode("3201234");
		value2 = formatRegionCode("");


		Object obj = null;
		String str1 = (String) obj;
		print(str1);


	}

	private String formatRegionCode(String rc) {
		final int maxLength = 6;
//        if (StringUtils.isBlank(rc)) throw new RuntimeException("");
		StringBuilder sb = new StringBuilder();
		sb.append(rc);
		while (sb.length() < maxLength) {
			sb.append("0");
		}
		return sb.toString();
	}

	private enum GHSC {
		TDYTQ("土地用途分区"),
		JSYDGZQ("建设用地管制区"),
		GHJBNTBHQ("基本农田"),
		MZZDJSXM("重点建设项目");

		private String label;

		private GHSC(String value) {
			this.label = value;
		}

		private String getLabel() {
			return label;
		}
	}

	@Test
	public void testEnum() throws Exception {

		for (GHSC item : GHSC.values()) {
			print(item.name() + "----" + item.getLabel());
		}

	}


	@Test
	public void testGeoOP() throws Exception {
		String value = IOUtils.toString(getClass().getResourceAsStream("/single_feature.json"));
		SimpleFeature feature = (SimpleFeature) geometryService.readUnTypeGeoJSON(value);
		Geometry geo = (Geometry) feature.getDefaultGeometry();
		CoordinateReferenceSystem srcCrs = geometryService.getCRSBySRID("4610");
		CoordinateReferenceSystem destCrs = geometryService.getCRSBySRID("2364");
		geo = geometryService.project(geo, srcCrs, destCrs);
		Geometry geo1 = geometryService.simplify(geo, 100);
		print(geo1.getArea() + "--" + geo1.getLength());
		Geometry geo2 = geometryService.densify(geo, 100);
		print(geo2.getArea() + "--" + geo2.getLength());
		Geometry geo3 = geometryService.densify(geo, 1000);
		print(geo3.getArea() + "--" + geo3.getLength());
		Geometry geo4 = geometryService.densify(geo, 10000);
		print(geo4.getArea() + "--" + geo4.getLength());

		for (int i = 1; i < 1000; i++) {
			Geometry gg = geometryService.densify(geo, i);
			print(gg.getArea() + "----" + gg.getLength());
		}

		print(geo);

	}

	@Test
	public void testValidGeo() throws Exception {

		String geo = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[40533301.296,3487150.867],[40533322.754,3487160.257],[40533344.024,3487170.064],[40533363.884,3487179.696],[40533385.97,3487190.916],[40533411.598,3487205.409],[40533418.39,3487190.198],[40533426.09,3487173.328],[40533435.591,3487153.528],[40533437.271,3487150.008],[40533464.961,3487092.168],[40533476.191,3487066.187],[40533482.262,3487053.537],[40533483.882,3487051.397],[40533488.512,3487038.897],[40533496.972,3487022.997],[40533497.304,3487023.106],[40533403.108,3486972.275],[40533297.949,3487137.557],[40533301.296,3487150.867]]],[[[40533189.436,3489961.767],[40533255.398,3490016.141],[40533256.707,3490014.707],[40533280.241,3489988.833],[40533296.832,3489970.531],[40533301.343,3489965.325],[40533306.863,3489958.75],[40533314.328,3489949.497],[40533331.907,3489925.488],[40533334.354,3489922.118],[40533300.839,3489906.387],[40533302.804,3489838.625],[40533290.988,3489846.837],[40533259.207,3489868.637],[40533253.147,3489875.577],[40533243.817,3489892.227],[40533191.656,3489947.557],[40533189.436,3489961.767]]],[[[40533227.042,3489892.838],[40533236.157,3489883.117],[40533252.917,3489860.547],[40533300.908,3489828.627],[40533302.979,3489832.588],[40533303.821,3489803.561],[40533293.011,3489811.802],[40533222.44,3489865.603],[40533210.38,3489874.797],[40533224.054,3489887.538],[40533222.77,3489888.911],[40533227.042,3489892.838]]],[[[40533303.821,3489803.561],[40533210.38,3489874.797],[40533197.611,3489862.899],[40533204.937,3489857.349],[40533198.345,3489848.644],[40533142.266,3489732.843],[40533139.989,3489727.828],[40533178.658,3489682.181],[40533184.746,3489683.605],[40533186.061,3489683.64],[40533186.068,3489683.647],[40533190.81,3489683.766],[40533207.46,3489684.208],[40533214.895,3489689.047],[40533223.298,3489691.56],[40533294.802,3489693.916],[40533306.99,3489694.318],[40533303.821,3489803.561]],[[40533303.821,3489803.561],[40533246.645,3489740.984],[40533247.338,3489742.142],[40533271.138,3489781.906],[40533273.928,3489783.756],[40533290.268,3489768.296],[40533280.768,3489767.186],[40533271.308,3489743.026],[40533242.888,3489711.766],[40533217.078,3489691.585],[40533246.645,3489740.984],[40533303.821,3489803.561]]],[[[40534956.603,3484103.964],[40534967.354,3484113.598],[40534994.521,3484146.713],[40535003.448,3484154.317],[40535009.329,3484159.739],[40535014.768,3484165.605],[40535015.946,3484166.981],[40535017.009,3484167.973],[40535040.606,3484194.853],[40535052.096,3484204.496],[40535076.719,3484215.371],[40535110.081,3484226.711],[40535104.858,3484069.141],[40535095.822,3484059.518],[40534979.053,3484048.273],[40534962.164,3484061.707],[40534956.603,3484103.964]]],[[[40544083.759,3482077.326],[40544097.652,3482078.885],[40544292.257,3482102.114],[40544318.372,3481743.546],[40544276.806,3481741.909],[40544235.215,3481741.154],[40544193.616,3481741.281],[40544152.03,3481742.29],[40544110.475,3481744.182],[40544068.969,3481746.954],[40544083.759,3482077.326]]],[[[40535112.381,3483837.387],[40535052.45,3483834.42],[40535051.097,3483869.397],[40535111.133,3483872.363],[40535112.381,3483837.387]]]] }";

		try {
			Geometry geometry = geometryService.project((Geometry) geometryService.readUnTypeGeoJSON(geo), null, null);
		} catch (GeometryServiceException e) {
			e.printStackTrace();
		}


	}

	@Test
	public void testMPolygin() throws Exception {
		String coords = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40541635.746,3483119.112],[40541648.875,3483103.659],[40541658.799,3483120.763],[40541701.195,3483093.851],[40541695.097,3483073.364],[40541718.003,3483059.67],[40541742.77,3483111.569],[40541744.433,3483139.807],[40541773.813,3483138.7],[40541774.577,3483147.635],[40541806.886,3483149.589],[40541771.485,3482931.504],[40541768.269,3482911.728],[40541758.499,3482851.635],[40541744.989,3482768.546],[40541719.899,3482800.543],[40541538.689,3482814.998],[40541591.612,3482900.178],[40541602.662,3482917.965],[40541624.564,3482953.215],[40541602.225,3482963.862],[40541594.145,3482971.932],[40541596.045,3482975.142],[40541595.096,3482980.118],[40541591.185,3482983.212],[40541582.625,3482988.432],[40541585.005,3482993.652],[40541649.703,3483101.297],[40541635.746,3483119.112]]],[[[40541847.619,3483156.464],[40541947.307,3483157.288],[40541993.821,3483128.008],[40541994.575,3483129.605],[40542100.574,3483068.695],[40542061.752,3482828.13],[40542060.021,3482817.429],[40542049.807,3482754.109],[40542041.706,3482704.226],[40542037.407,3482677.651],[40542034.565,3482660.088],[40542027.274,3482615.022],[40541838.251,3482626.605],[40541773.14,3482630.595],[40541763.087,3482631.211],[40541785.7,3482770.29],[40541797.583,3482843.377],[40541808.035,3482907.662],[40541836.515,3483082.82],[40541844.238,3483130.321],[40541847.619,3483156.464]]],[[[40539563.954,3483001.667],[40539608.263,3483014.028],[40539610.986,3483008.372],[40539611.063,3483008.443],[40539619.63,3482991.42],[40539615.891,3482985.139],[40539607.785,3482981.198],[40539599.479,3482977.182],[40539595.139,3482975.461],[40539588.505,3482972.917],[40539581.371,3482970.173],[40539577.184,3482968.542],[40539569.15,3482965.434],[40539565.633,3482963.888],[40539561.592,3482962.067],[40539554.983,3482959.148],[40539546.876,3482955.557],[40539538.97,3482952.04],[40539530.34,3482948.149],[40539527.072,3482947.301],[40539556.672,3482990.966],[40539563.954,3483001.667]]],[[[40539519.862,3482936.791],[40539530.504,3482940.93],[40539547.531,3482947.611],[40539570.53,3482956.657],[40539583.445,3482961.697],[40539587.498,3482963.279],[40539599.322,3482967.89],[40539630.657,3482981.134],[40539652.768,3482994.141],[40539670.978,3483001.59],[40539671.269,3483001.859],[40539704.23,3482967.583],[40539702.512,3482966.813],[40539700.946,3482966.44],[40539694.552,3482964.892],[40539687.393,3482963.14],[40539684.634,3482962.469],[40539676.431,3482960.493],[40539673.634,3482959.616],[40539662.076,3482956.111],[40539654.209,3482953.706],[40539645.39,3482952.047],[40539637.337,3482950.537],[40539630.141,3482949.157],[40539621.546,3482947.554],[40539611.74,3482945.727],[40539607.62,3482943.881],[40539602.158,3482941.439],[40539593.955,3482937.748],[40539591.829,3482936.946],[40539585.454,3482934.597],[40539576.505,3482931.278],[40539568.339,3482928.258],[40539559.447,3482924.958],[40539551.729,3482922.087],[40539545.781,3482920.763],[40539536.591,3482918.619],[40539522.832,3482921.639],[40539517.034,3482928.929],[40539515.724,3482930.594],[40539519.862,3482936.791]]],[[[40539499.779,3482887.449],[40539512.555,3482892.144],[40539519.33,3482879.428],[40539523.415,3482873.562],[40539526.452,3482874.19],[40539534.204,3482893.464],[40539543.002,3482904.463],[40539555.677,3482907.605],[40539566.14,3482910.64],[40539592.842,3482882.917],[40539510.489,3482858.319],[40539499.779,3482887.449]]],[[[40539617.119,3482924.883],[40539628.871,3482928.158],[40539644.922,3482932.651],[40539655.546,3482935.634],[40539658.622,3482936.497],[40539661.935,3482937.426],[40539662.847,3482937.68],[40539663.898,3482937.977],[40539664.691,3482938.199],[40539665.77,3482938.501],[40539666.629,3482938.742],[40539667.467,3482938.975],[40539668.562,3482939.283],[40539669.861,3482939.649],[40539671.151,3482940.011],[40539672.409,3482940.364],[40539673.327,3482940.621],[40539674.518,3482940.954],[40539675.824,3482941.319],[40539677.261,3482941.725],[40539678.654,3482942.115],[40539679.925,3482942.471],[40539680.989,3482942.77],[40539681.942,3482943.038],[40539682.935,3482943.313],[40539684.35,3482943.71],[40539685.223,3482943.955],[40539686.149,3482944.214],[40539687.217,3482944.517],[40539688.081,3482944.756],[40539689.117,3482945.049],[40539690.52,3482945.441],[40539691.457,3482945.706],[40539692.579,3482946.019],[40539693.245,3482946.208],[40539694.104,3482946.448],[40539694.974,3482946.689],[40539696.11,3482947.01],[40539697.064,3482947.277],[40539697.959,3482947.528],[40539698.708,3482947.835],[40539699.49,3482948.16],[40539700.237,3482948.466],[40539701.885,3482949.149],[40539702.807,3482949.526],[40539703.613,3482949.86],[40539704.341,3482950.161],[40539705.015,3482950.439],[40539705.935,3482950.82],[40539706.969,3482951.247],[40539707.951,3482951.654],[40539708.859,3482952.029],[40539709.879,3482952.447],[40539710.914,3482952.877],[40539711.982,3482953.317],[40539713.225,3482953.83],[40539713.996,3482954.14],[40539714.937,3482954.505],[40539738.826,3482928.6],[40539653.129,3482900.93],[40539642.056,3482897.622],[40539617.119,3482924.883]]],[[[40539702.647,3482831.382],[40539766.959,3482889.681],[40539769.381,3482887.292],[40539704.986,3482828.825],[40539702.647,3482831.382]]],[[[40542106.963,3482239.363],[40542135.613,3482236.951],[40542126.229,3482211.599],[40542102.983,3482140.328],[40542047.738,3482145.027],[40542106.963,3482239.363]]]]}";
		Geometry geo = (Geometry) geometryService.readUnTypeGeoJSON(coords);
		print(geo);


	}

	@Test
	public void testLayerRegion() throws Exception {

		LayerRegion layerRegion = geometryService.getLayerRegion("TDLY.DLTB_H_2011");

		print(layerRegion);

		layerRegion = geometryService.getLayerRegion("UNKNOWLAYER");
		layerRegion = geometryService.getLayerRegion("UNKNOWLAYER");
		layerRegion = geometryService.getLayerRegion("UNKNOWLAYER");

		print(layerRegion);

		layerRegion = geometryService.getLayerRegion("XZDW_H_2011");
		layerRegion = geometryService.getLayerRegion("BPDK_320000");
		layerRegion = geometryService.getLayerRegion("GHJBNTBHQ_E_2020");

		print(layerRegion);

	}

	@Test
	public void testProjectPre() throws Exception {

		String coords = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[120.4399182625246,31.469813324125333],[120.43953657025675,31.467847762951742],[120.43950189779224,31.467669525511234],[120.43939656529557,31.46712791820357],[120.43925091343701,31.466379051929302],[120.43898826348807,31.466668528264645],[120.43708217495305,31.466805415655866],[120.43764260608009,31.467571725921303],[120.4377596219247,31.467731743955142],[120.4379915578203,31.468048864749555],[120.43775694992567,31.468145690516106],[120.43767226932883,31.468218761965144],[120.43769239618274,31.46824764362734],[120.43768261942036,31.468292554761028],[120.43764159692617,31.468320599241419],[120.43755174587032,31.468367984605599],[120.43757700770104,31.468414976594268],[120.43826229653961,31.469383465204668],[120.43811618486228,31.46954463569249],[120.43825368330599,31.469404797164731],[120.43835882564552,31.46955869536162],[120.4388037984216,31.469314456487322],[120.43873877162385,31.46912991050203],[120.43897921882127,31.469005582544185],[120.43924200953568,31.469472749708842],[120.43926069738538,31.469727359349815],[120.43956979749387,31.469716314783707],[120.4395782131366,31.469796869144314],[120.4399182625246,31.469813324125333]]],[[[120.44139614889176,31.469877674174445],[120.44188434582166,31.469611919049832],[120.44189234732379,31.469626294507631],[120.44300511763002,31.469073112638817],[120.44258641442072,31.466904945363119],[120.44256774677054,31.466808499350641],[120.44245758905593,31.466237807484632],[120.44237023529506,31.465788222772368],[120.44232387478682,31.4655487073831],[120.44229322695659,31.465390415441046],[120.44221460167923,31.464984244139124],[120.44022622421973,31.465095560179915],[120.43954130510026,31.465133898049345],[120.43943555498824,31.465139816580287],[120.43967934725053,31.466393310467073],[120.43980746142783,31.46705203008905],[120.43992014907204,31.467631418733106],[120.44022721149469,31.469210084042775],[120.44031048041255,31.469638201211598],[120.44034716017616,31.46987385435289],[120.44139614889176,31.469877674174445]]],[[[120.41677809140211,31.468668129567142],[120.41680651752063,31.468617026459402],[120.41680733057187,31.468617664150869],[120.4168967943975,31.468463845124845],[120.41685720071033,31.46840732666103],[120.41677174998448,31.468372061540947],[120.41668419188025,31.468336126807422],[120.4166384566601,31.468320754191229],[120.41656855059934,31.468298037682366],[120.41649337547436,31.468273534504032],[120.41644925380834,31.468258968267659],[120.41636459421818,31.468231212998564],[120.41632752586949,31.468217390387501],[120.41628493291554,31.46820110554329],[120.41621527511896,31.46817500596611],[120.4161298282816,31.468142896997136],[120.41604649941691,31.468111448483864],[120.41595553761896,31.468076651662177],[120.41592111725012,31.468069115485182],[120.41623431682035,31.468461905685551],[120.41631136690589,31.468558165866924],[120.41677809140211,31.468668129567142]]],[[[120.41611691842725,31.46782913723489],[120.41602013413888,31.467810115300541],[120.41587547998063,31.467837822088793],[120.41581476301545,31.46790376665124],[120.41580104532726,31.467918827573282],[120.41584483317993,31.467974575169212],[120.41595697538318,31.468011539945255],[120.41613640332309,31.468071211884183],[120.41637876451651,31.468152008518274],[120.41651486010601,31.468197020891793],[120.41655756981893,31.468211149775513],[120.41668216869721,31.468252330284145],[120.41701241195021,31.468370700821222],[120.41724558934747,31.468487249352648],[120.41743749755447,31.468553805260083],[120.41744057029571,31.468556221312198],[120.41778602140226,31.468245964788611],[120.41776791339159,31.468239079356955],[120.41775142066034,31.468235769134342],[120.41768407961622,31.468222027636401],[120.41760868093009,31.468206472536828],[120.41757962327905,31.468200515653027],[120.417493230472,31.468182976115603],[120.4174637647466,31.468175162653864],[120.41734200879635,31.468143948483409],[120.41725913438374,31.46812252823587],[120.41716627270648,31.468107868529476],[120.41708147702198,31.468094526291761],[120.41700570408427,31.46808232706503],[120.41691520169503,31.468068164548232],[120.41681194799949,31.468052023252319],[120.41676852278638,31.468035515806481],[120.41671095299363,31.468013679144445],[120.4166244920138,31.467980671920607],[120.41660208980615,31.467973511691316],[120.41653491704625,31.467952544943778],[120.41644062148606,31.467922918089815],[120.41635457678672,31.467895960973884],[120.41626088186094,31.467866503391814],[120.41617955714277,31.467840874622166],[120.41611691842725,31.46782913723489]]],[[[120.41661059086994,31.467486204818183],[120.41574308105801,31.467267178655181],[120.41563154993909,31.467530259710635],[120.41576616794794,31.467572166185018],[120.41583694855609,31.467457252702211],[120.41587969759445,31.467404209319508],[120.41591167833278,31.467409769287336],[120.41599401451545,31.467583331082249],[120.41608702710398,31.46768222717629],[120.41622052082148,31.467710130555158],[120.41633073532061,31.467737144466223],[120.41661059086994,31.467486204818183]]],[[[120.41724565930711,31.467646593570908],[120.41712901523302,31.467617139247277],[120.41686771387378,31.467863852910519],[120.4169915014166,31.467892986586076],[120.41716057261026,31.46793295758086],[120.41727247962349,31.467959496183759],[120.41730488039602,31.467967173861521],[120.41733977757315,31.467975438638739],[120.41734938397192,31.467977698116453],[120.41736045467594,31.4679803406312],[120.41736880765473,31.467982315589673],[120.41738017318116,31.467985002236009],[120.41738922138563,31.467987146285154],[120.41739804830434,31.46798921890419],[120.4174095824276,31.467991959111401],[120.41742326539948,31.467995215405807],[120.41743685351246,31.467998435932593],[120.41745010455605,31.46800157638717],[120.41745977421326,31.468003862706883],[120.41747231947103,31.468006825083172],[120.41748607606247,31.468010072111973],[120.41750121270313,31.468013684412423],[120.41751588572798,31.468017153921192],[120.41752927368535,31.468020320977676],[120.41754048126788,31.468022981068145],[120.41755051964655,31.468025365385987],[120.41756097919369,31.468027811461567],[120.41757588399138,31.468031343339568],[120.41758507967539,31.468033522969254],[120.41759483359675,31.468035827041625],[120.41760608343179,31.468038523063939],[120.41761518417724,31.468040648887985],[120.41762609690279,31.468043255819506],[120.41764087523872,31.468046743008106],[120.41765074514718,31.46804910081098],[120.41766256358434,31.468051885162208],[120.41766957893523,31.468053566841984],[120.41767862711265,31.468055701851831],[120.41768779107458,31.468057845502191],[120.41769975714512,31.468060701519185],[120.41770980601386,31.468063076771415],[120.41771923343262,31.468065309748575],[120.41772712685353,31.468068052784492],[120.41773536822841,31.468070957024015],[120.41774324056583,31.468073691108867],[120.4177606085043,31.468079794319316],[120.4177703250718,31.468083162723783],[120.41777881934254,31.468086147305019],[120.41778649156039,31.468088836945881],[120.4177935946586,31.468091321009698],[120.41780329034431,31.468094725555165],[120.41781418740418,31.468098541046828],[120.41782453650939,31.468102177948616],[120.41783410569049,31.468105528791423],[120.41784485508158,31.468109263592723],[120.4178557627872,31.468113106102624],[120.4178670181271,31.468117037665611],[120.41788011777818,31.468121621584611],[120.41788824281709,31.468124390911214],[120.41789815883483,31.468127650428876],[120.41814848530829,31.467893200075473],[120.41724565930711,31.467646593570908]]],[[[120.41846833326484,31.467519604789889],[120.41778841854892,31.466994520036927],[120.41776390981374,31.467017661185874],[120.41844294448639,31.467541233776707],[120.41846833326484,31.467519604789889]]],[[[120.44323864850662,31.461342297228061],[120.44299104177179,31.460700372146547],[120.44240998958574,31.460744760232668],[120.44303712193644,31.461593393629425],[120.44333845867403,31.461570597380216],[120.44323864850662,31.461342297228061]]]]}";
		Geometry geometry = (Geometry) geometryService.readUnTypeGeoJSON(coords);
		CoordinateReferenceSystem sourceCrs = geometryService.getCRSBySRID("4610");
		CoordinateReferenceSystem targetCrs = geometryService.getCRSBySRID("2364");
		Geometry geo = geometryService.project(geometry, sourceCrs, targetCrs);

		String line = "{\"type\":\"LineString\",\"coordinates\":[[40541806.886,3483149.5890000574],[40541807.886,3483147.5890000574]]}";
		geometry = (Geometry) geometryService.readUnTypeGeoJSON(line);
		geo = geometryService.project(geometry, targetCrs, sourceCrs);

		print("--ok--");
	}


	@Test
	public void testProjectByAGS() throws Exception {

		String coords = IOUtils.toString(getClass().getResourceAsStream("/coords.txt"));
		Geometry geometry = (Geometry) geometryService.readUnTypeGeoJSON(coords);

		geometry = geometryService.projectByAGS(geometry, geometryService.getCRSBySRID("4610"), geometryService.getCRSBySRID("2364"));

		IsValidOp isValidOp = new IsValidOp(geometry);
		TopologyValidationError topologyValidationError = isValidOp.getValidationError();

		print("----");


	}

//@Test
//    public void testGetBj(){
//        File file = new File("D:\\gt.xml");
//        try {
//            byte[] buffer = null;
//            FileInputStream fis = new FileInputStream(file);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            byte[] b = new byte[1024];
//            int n;
//            while ((n = fis.read(b)) != -1)
//            {
//                bos.write(b, 0, n);
//            }
//            fis.close();
//            bos.close();
//            buffer = bos.toByteArray();
//            Map map = geometryService.parseBjXmlAdv(buffer);
//            String feature = MapUtils.getString(map,"feature");
//            FeatureCollection featureCollection  =(FeatureCollection) geometryService.readUnTypeGeoJSON(feature);
//            FeatureIterator  iterator  = featureCollection.features();
//            while(iterator.hasNext()){
//                iterator.next();
//            }
//            geometryService.validGeometry(geometry);
//            System.out.println(JSON.toJSONString(map));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
