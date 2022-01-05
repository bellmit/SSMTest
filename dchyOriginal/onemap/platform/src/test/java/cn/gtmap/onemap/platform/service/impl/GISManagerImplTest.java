package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import com.alibaba.fastjson.JSON;
import com.esri.sde.sdk.client.SeInstance;
import com.esri.sde.sdk.geom.SeCoordRef;
import com.esri.sde.sdk.geom.SePoint;
import com.esri.sde.sdk.geom.SePolygon;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-21 下午1:59
 */
public class GISManagerImplTest extends BaseServiceTest {

	@Autowired
	private GeometryServiceImpl geometryService;

	@Autowired
	private GISManagerImpl gisManager;

	@Test
	public void testNoVersion() {
		String layerName = "SDE.TEST2";
		String where = "objectid > 0";
		String wkt = "POLYGON ((40587961.13790113 3530166.292699001, 40590466.10149266 3530331.2014278704, 40589575.59435676 3528220.369698341, 40586566.834598534 3528830.531995158, 40587961.13790113 3530166.292699001))";
		String wkt2 = "POLYGON ((40463840.000 3508512.883,40477894.906 3508869.245,40478065.960  3499432.786,40463840.000  3508512.883))";
		String[] columns = "SM,BSM,TDYTQLXDM,TDYTQBH,TDYTQMJ".split(",");
		String[] columns2 = new String[]{"name", "proname"};

		print(gisManager.getGISService().query(layerName, where, columns2, true, null));

		print(gisManager.getGISService().query(layerName, wkt2, columns2, null));

		Geometry geo = geometryService.readWKT(wkt2);
		print(gisManager.getGISService().query(layerName, geo, columns2, null));

		print(gisManager.getGISService().intersect(layerName, wkt2, columns2, null));
		print(gisManager.getGISService().intersect(layerName, (Polygon) geo, columns2, null));

		String insertWkt = "POLYGON (( 40464465.76 3505623.503, 40464278.584 3505614.694, 40464512.003 3505427.518, 40464465.76 3505623.503))";
		Map map = new HashMap();
		map.put("NAME", "inserpp");
		map.put("PRONAME", "pronamesss");
		map.put("SHAPE", insertWkt);

		Map map2 = new HashMap(map);

		gisManager.getGISService().insert(layerName, map, null);

		gisManager.getGISService().update(layerName, "644", map2, null);

		gisManager.getGISService().delete(layerName, "645", null);

		print("----------------pass-------------------");

	}

	@Test
	public void testVersion() {
		String layerName = "SDE.TEST4";
		String where = "objectid > 0";
		String wkt = "POLYGON ((40587961.13790113 3530166.292699001, 40590466.10149266 3530331.2014278704, 40589575.59435676 3528220.369698341, 40586566.834598534 3528830.531995158, 40587961.13790113 3530166.292699001))";
		String wkt2 = "POLYGON ((40463840.000 3508512.883,40477894.906 3508869.245,40478065.960  3499432.786,40463840.000  3508512.883))";
		String[] columns = "SM,BSM,TDYTQLXDM,TDYTQBH,TDYTQMJ".split(",");
		String[] columns2 = new String[]{"PRONAME"};

		print(gisManager.getGISService().query(layerName, where, columns2, true, null));

		print(gisManager.getGISService().query(layerName, wkt2, columns2, null));

		Geometry geo = geometryService.readWKT(wkt2);
		print(gisManager.getGISService().query(layerName, geo, columns2, null));

		print(gisManager.getGISService().intersect(layerName, wkt2, columns2, null));
		print(gisManager.getGISService().intersect(layerName, (Polygon) geo, columns2, null));

		String insertWkt = "POLYGON (( 40464465.76 3505623.503, 40464278.584 3505614.694, 40464512.003 3505427.518, 40464465.76 3505623.503))";
		Map map = new HashMap();
//        map.put("NAME", "inserpp");
		map.put("PRONAME", "pronamesss");
		map.put("SHAPE", insertWkt);

		Map map2 = new HashMap(map);

		String objectid = gisManager.getGISService().insert(layerName, map, null);
		print(objectid);

		gisManager.getGISService().update(layerName, objectid, map2, null);

		gisManager.getGISService().delete(layerName, objectid, null);

		print("----------------pass-------------------");

	}

	@Test
	public void tt() {
		gisManager.getGISService().delete("SDE.TEST4", "321", null);
	}

	@Test
	public void testIntersect() throws Exception {
		//
		String layerName = "SDE.TEST2";
		String wkt2 = "POLYGON ((40463840.000 3508512.883,40477894.906 3508869.245,40478065.960  3499432.786,40463840.000  3508512.883))";
		String[] columns2 = new String[]{"name", "proname"};

		Geometry geo = geometryService.readWKT(wkt2);
		String geoJSON = geometryService.toGeoJSON(geo);
//        print(gisManager.getGISService().intersect(layerName, geoJSON, null, columns2, null));


	}

	@Test
	public void testQuery() throws Exception {
		String layerName = "SDE.XZQ_H_329999";
		String where = "OBJECTID>0";
		List list = gisManager.getGISService().query(layerName, where, null, true, null);

		print("------ok-----");

	}

	@Test
	public void testProject() throws Exception {

		String layerName = "SDE.XZQ_H_329999";
		String where = "OBJECTID>0";
		List list = gisManager.getGISService().query(layerName, where, new String[]{"BSM", "YSDM", "XZQDM"}, true, null);

		Geometry geometry = null;

		if (list.size() > 0) {
			String wkt = (String) ((Map) list.get(0)).get("SHAPE");
			Geometry geo = geometryService.readWKT(wkt);
			geo.setSRID(2364);

			CoordinateReferenceSystem srcCRS = geometryService.getCRSBySRID("2364");
			CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");
			try {
				geometry = geometryService.project(geo, srcCRS, destCRS);
			} catch (GeometryServiceException e) {
				e.printStackTrace();
			}

			print(null);


		}

//        print(list);

	}

	@Test
	public void testtestSdeMa() throws Exception {

		String server = "192.168.0.201";
		int port = 5151;
		String db = "sde";
		String pw = "sde";

		gisManager.getSdeManager().startInstance("192.168.0.201", 5151, "sde", "sde");

		SeInstance instance = gisManager.getSdeManager().getInstance(server, port);

		print(instance);

	}

	@Test
	public void testIntersect2() throws Exception {
		//
		String layerName = "SDE.TEST2";
		String wkt2 = "POLYGON ((40463840.000 3508512.883,40477894.906 3508869.245,40478065.960  3499432.786,40463840.000  3508512.883))";
		String[] columns2 = new String[]{"name", "proname"};

		String json = "{\n" +
				"    \"type\": \"feature\",\n" +
				"    \"crs\": {\n" +
				"        \"type\": \"name\",\n" +
				"        \"properties\": {\n" +
				"            \"name\": \"EPSG:21483\"\n" +
				"        }\n" +
				"    },\n" +
				"    \"geometry\": {\n" +
				"        \"type\": \"Polygon\",\n" +
				"        \"coordinates\": [\n" +
				"            [\n" +
				"                [\n" +
				"                    40463840,\n" +
				"                    3508512.883\n" +
				"                ],\n" +
				"                [\n" +
				"                    40477894.906,\n" +
				"                    3508869.245\n" +
				"                ],\n" +
				"                [\n" +
				"                    40478065.96,\n" +
				"                    3499432.786\n" +
				"                ],\n" +
				"                [\n" +
				"                    40463840,\n" +
				"                    3508512.883\n" +
				"                ]\n" +
				"            ]\n" +
				"        ]\n" +
				"    }\n" +
				"}";

		String json2 = "{\n" +
				"    \"type\": \"feature\",\n" +
				"    \"geometry\": {\n" +
				"        \"type\": \"Polygon\",\n" +
				"        \"coordinates\": [\n" +
				"            [\n" +
				"                [\n" +
				"                    40463840,\n" +
				"                    3508512.883\n" +
				"                ],\n" +
				"                [\n" +
				"                    40477894.906,\n" +
				"                    3508869.245\n" +
				"                ],\n" +
				"                [\n" +
				"                    40478065.96,\n" +
				"                    3499432.786\n" +
				"                ],\n" +
				"                [\n" +
				"                    40463840,\n" +
				"                    3508512.883\n" +
				"                ]\n" +
				"            ]\n" +
				"        ]\n" +
				"    }\n" +
				"}";

		geometryService.readFeatureJSON(json2);

		CoordinateReferenceSystem crs = geometryService.readFeatureJSONCRS(json);

//        String result = gisManager.getGISService().intersect2(layerName, json2, columns2, null);

		Geometry geo = geometryService.readWKT(wkt2);
		String geoJSON = geometryService.toGeoJSON(geo);
//        print(gisManager.getGISService().intersect(layerName, geoJSON, null, columns2, null));


	}


	@Test
	public void testTdlyAnalysis() throws Exception {

		String dltbLayerName = "SDE.DLTB_H_321204";
		String xzdwLayerName = "SDE.XZDW_H_321204";

		String geo = "{\n" +
				"    \"type\": \"feature\",\n" +
//                "    \"crs\": {\n" +
//                "        \"type\": \"name\",\n" +
//                "        \"properties\": {\n" +
//                "            \"name\": \"EPSG:4610\"\n" +
//                "        }\n" +
//                "    },\n" +
				"    \"geometry\": {\n" +
				"        \"type\": \"Polygon\",\n" +
				"        \"coordinates\": [\n" +
				"            [\n" +
				"                [\n" +
				"                    40486073.639,\n" +
				"                    3589015.753\n" +
				"                ],\n" +
				"                [\n" +
				"                    40487287.339,\n" +
				"                    3584970.083\n" +
				"                ],\n" +
				"                [\n" +
				"                    40484961.08,\n" +
				"                    3586520.923\n" +
				"                ],\n" +
				"                [\n" +
				"                    40486073.639,\n" +
				"                    3589015.753\n" +
				"                ]\n" +
				"            ]\n" +
				"        ]\n" +
				"    }\n" +
				"}";

		Map result = gisManager.getGISService().tdlyxzAnalysis(dltbLayerName, xzdwLayerName, geo, null, null);

		print(result);


	}

	@Test
	public void testGhAnalysis() throws Exception {
		//
		String geo = "{\n" +
				"    \"type\": \"feature\",\n" +
//                "    \"crs\": {\n" +
//                "        \"type\": \"name\",\n" +
//                "        \"properties\": {\n" +
//                "            \"name\": \"EPSG:4610\"\n" +
//                "        }\n" +
//                "    },\n" +
				"    \"geometry\": {\n" +
				"        \"type\": \"Polygon\",\n" +
				"        \"coordinates\": [\n" +
				"            [\n" +
				"                [\n" +
				"                    39674531.467,\n" +
				"                    3546502.298\n" +
				"                ],\n" +
				"                [\n" +
				"                    39675759.136,\n" +
				"                    3544676.670\n" +
				"                ],\n" +
				"                [\n" +
				"                    39673536.632,\n" +
				"                    3545533.921\n" +
				"                ],\n" +
				"                [\n" +
				"                    39674531.467,\n" +
				"                    3546502.298\n" +
				"                ]\n" +
				"            ]\n" +
				"        ]\n" +
				"    }\n" +
				"}";

		String layerType = "TDYTQ";
		String regionCOde = "320102";
		gisManager.getGISService().tdghscAnalysis(layerType, regionCOde, geo, null, null);
		gisManager.getGISService().tdghscAnalysis("JSYDGZQ", regionCOde, geo, null, null);
		gisManager.getGISService().tdghscAnalysis("TDYTQ", "320103", geo, null, null);
		gisManager.getGISService().tdghscAnalysis("MZJCSS", "320103", geo, null, null);
		gisManager.getGISService().tdghscAnalysis("JSYDGZQ", "320103", geo, null, null);

		print("----ok----");

	}

	@Test
	public void testGuscAnalysis() throws Exception {

		try {
			print("---start---");

			GeometryJSON geometryJSON = new GeometryJSON();
//        GeometryCollection ceoCollection = geometryJSON.readGeometryCollection(getClass().getResourceAsStream("/coords.json"));
//        Geometry geo = geometryJSON.read(getClass().getResourceAsStream("/coords.json"));
			String value = IOUtils.toString(getClass().getResourceAsStream("/tt.json"));
			GeometryCollection geos = (GeometryCollection) geometryService.readUnTypeGeoJSON(value);
			CoordinateReferenceSystem srcCRS = geometryService.getCRSBySRID("2364");
			CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");

			Geometry projedGeo = geometryService.project(geos, srcCRS, destCRS);

//            projedGeo.s

//        Geometry projedGeo2 = geometryService.project(projedGeo,destCRS,srcCRS);

			List result = gisManager.getGISService().intersect("SDE.TDYTQ_E_320925", projedGeo, destCRS, null, null);


			print("---ok---");
		} catch (Exception e) {
			print(e.getLocalizedMessage());
		}

	}


	@Test
	public void testTdghsc() throws Exception {
		String wktCRS = "PROJCS[\"Xian_1980_3_Degree_GK_CM_120E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",40500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",120.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
		String value = IOUtils.toString(getClass().getResourceAsStream("/t_4610.json"));
//        GeometryCollection geos = (GeometryCollection) geometryService.readUnTypeGeoJSON(value);
		CoordinateReferenceSystem srcCRS = geometryService.getCRSByWKTString(wktCRS);
		CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");

//        print(srcCRS.toWKT());
//
//        Geometry projedGeo = geometryService.project(geos, srcCRS, destCRS);

//        Geometry projedGeo2 = geometryService.project(projedGeo, destCRS, srcCRS);

//        String geoJson = geometryService.toGeoJSON(projedGeo);

		List r = gisManager.getGISService().intersectByGeoJSON("SDE.TDYTQ_E_320925", value, null, null);

		String result = gisManager.getGISService().tdghscAnalysis2("TDYTQ", "320925", value, null, null);

		print("---ok---");


	}

	@Test
	public void testProject2() throws Exception {
		String wktCRS = "PROJCS[\"Xian_1980_3_Degree_GK_CM_120E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",40500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",120.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
		String value = IOUtils.toString(getClass().getResourceAsStream("/tt.json"));
		GeometryCollection geos = (GeometryCollection) geometryService.readUnTypeGeoJSON(value);
		CoordinateReferenceSystem srcCRS = geometryService.getCRSByWKTString(wktCRS);
		CoordinateReferenceSystem srcCRS2 = geometryService.getCRSByWKTString(wktCRS);
		CoordinateReferenceSystem srcCRS3 = geometryService.getCRSByWKTString(wktCRS);
		CoordinateReferenceSystem srcCRS4 = geometryService.getCRSByWKTString(wktCRS);

		CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");
//
		Geometry projedGeo = geometryService.project(geos, srcCRS, destCRS);

		Geometry projedGeo2 = geometryService.project(projedGeo, destCRS, srcCRS);
		Geometry projedGeo3 = geometryService.project(projedGeo2, srcCRS, destCRS);
		Geometry projedGeo4 = geometryService.project(projedGeo3, destCRS, srcCRS);

		print("--ok--");

//        String geoJson = geometryService.toGeoJSON(projedGeo);


	}

	@Test
	public void testProject21() throws Exception {
		String wktCRS = "PROJCS[\"Xian_1980_3_Degree_GK_CM_120E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",40500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",120.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
		String value = IOUtils.toString(getClass().getResourceAsStream("/t_f.json"));
		SimpleFeature feature = (SimpleFeature) geometryService.readUnTypeGeoJSON(value);
		CoordinateReferenceSystem srcCRS = feature.getFeatureType().getCoordinateReferenceSystem();
//        CoordinateReferenceSystem srcCRS2 = geometryService.getCRSByWKTString(wktCRS);
//        CoordinateReferenceSystem srcCRS3 = geometryService.getCRSByWKTString(wktCRS);
//        CoordinateReferenceSystem srcCRS4 = geometryService.getCRSByWKTString(wktCRS);

		CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");
		Geometry geos = (Geometry) feature.getDefaultGeometry();
//
		Geometry projedGeo = geometryService.project(geos, srcCRS, destCRS);

		Geometry projedGeo2 = geometryService.project(projedGeo, destCRS, srcCRS);
		Geometry projedGeo3 = geometryService.project(projedGeo2, srcCRS, destCRS);
		Geometry projedGeo4 = geometryService.project(projedGeo3, destCRS, srcCRS);

		print("--ok--");

//        String geoJson = geometryService.toGeoJSON(projedGeo);


	}

	@Test
	public void testProject22() throws Exception {
		String wktCRS = "PROJCS[\"Xian_1980_3_Degree_GK_CM_120E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",40500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",120.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
		String value = IOUtils.toString(getClass().getResourceAsStream("/g_4610.json"));
		Geometry geos = (Geometry) geometryService.readUnTypeGeoJSON(value);
//        CoordinateReferenceSystem srcCRS = feature.getFeatureType().getCoordinateReferenceSystem();
		CoordinateReferenceSystem srcCRS = geometryService.getCRSByWKTString(wktCRS);
//        CoordinateReferenceSystem srcCRS3 = geometryService.getCRSByWKTString(wktCRS);
//        CoordinateReferenceSystem srcCRS4 = geometryService.getCRSByWKTString(wktCRS);

		CoordinateReferenceSystem destCRS = geometryService.getCRSBySRID("4610");
//        Geometry geos = (Geometry) feature.getDefaultGeometry();
//
		Geometry projedGeo = geometryService.project(geos, destCRS, srcCRS);

		Geometry projedGeo2 = geometryService.project(projedGeo, srcCRS, destCRS);
		Geometry projedGeo3 = geometryService.project(projedGeo2, srcCRS, destCRS);
		Geometry projedGeo4 = geometryService.project(projedGeo3, destCRS, srcCRS);

		print("--ok--");

//        String geoJson = geometryService.toGeoJSON(projedGeo);


	}


	@Test
	public void testProject3() throws Exception {
		String wktCRS = "PROJCS[\"Xian_1980_3_Degree_GK_CM_120E\",GEOGCS[\"GCS_Xian_1980\",DATUM[\"D_Xian_1980\",SPHEROID[\"Xian_1980\",6378140.0,298.257]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",40500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",120.0],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]";
//        String value = IOUtils.toString(getClass().getResourceAsStream("/g_4610.json"));
//        GeometryCollection geos = (GeometryCollection) geometryService.readUnTypeGeoJSON(value);

		String value = IOUtils.toString(getClass().getResourceAsStream("/t_4610.json"));
		SimpleFeature feature = (SimpleFeature) gisManager.getGeoService().readUnTypeGeoJSON(value);
//        SimpleFeature feature = gisManager.getGeoService().readFeatureJSON(value);
		Geometry geos = (Geometry) feature.getDefaultGeometry();

		CoordinateReferenceSystem srcCRS = geometryService.getCRSByWKTString(wktCRS);
//        CoordinateReferenceSystem destCRS = geometryService.getCRSByWKTString(geometryService.getCRSBySRID("4610").toWKT());
//        wktCRS = feature.getFeatureType().getCoordinateReferenceSystem().toWKT();
		CoordinateReferenceSystem destCRS = feature.getFeatureType().getCoordinateReferenceSystem();
//
		Geometry projedGeo = geometryService.project(geos, destCRS, srcCRS);

		Geometry projedGeo2 = geometryService.project(projedGeo, srcCRS, destCRS);
		Geometry projedGeo3 = geometryService.project(projedGeo2, destCRS, srcCRS);
		Geometry projedGeo4 = geometryService.project(projedGeo3, srcCRS, destCRS);

		print("--ok--");

//        String geoJson = geometryService.toGeoJSON(projedGeo);


	}

	@Test
	public void testGeoJSONAnalysis() throws Exception {
		String value = IOUtils.toString(getClass().getResourceAsStream("/t_4610.json"));

		SimpleFeature feature = (SimpleFeature) gisManager.getGeoService().readUnTypeGeoJSON(value);

		Geometry geo = (Geometry) feature.getDefaultGeometry();

//        CoordinateReferenceSystem geoCRS = gisManager.getGeoService().getCRSBySRID("4610");
//        CoordinateReferenceSystem geoCRS = gisManager.getGeoService().readFeatureJSONCRS(value);
		CoordinateReferenceSystem geoCRS = feature.getFeatureType().getCoordinateReferenceSystem();

//        CoordinateReferenceSystem crs = gisManager.getGeoService().readFeatureJSONCRS(value);

		gisManager.getGISService().intersect("SDE.TDYTQ_E_320925", geo, geoCRS, null, null);

//        String result = gisManager.getGISService().intersect2("SDE.TDYTQ_E_320925", value, null, null);

		print("--ok--");

	}

	@Test
	public void testGHSC() throws Exception {

		String geo = "{\"type\":\"Polygon\",\"coordinates\":[[[40526633.51,3504683.52],[40526683.83,3504686.12],[40526708.57,3504685.54],[40526873.27,3504669.3],[40526873.16,3504663.78],[40526876.26,3504660.87],[40526872.1,3504628.41],[40526879.71,3504628.29],[40526879.53,3504601.83],[40526877.65,3504601.85],[40526877.65,3504598.4],[40526868.82,3504598.46],[40526868.2,3504568.05],[40526865.02,3504568.05],[40526864.84,3504551.62],[40526862.78,3504551.82],[40526862.48,3504535.4],[40526649.45,3504534.68],[40526646.48,3504537.05],[40526629.8,3504549.49],[40526626.14,3504552.18],[40526619.4,3504560.05],[40526620.06,3504563.44],[40526618.61,3504608.41],[40526621.51,3504656.29],[40526632.64,3504682.25],[40526633.23,3504683.28],[40526633.51,3504683.52]]]}";
		gisManager.getGISService().tdghscAnalysis2("320206", geo, null, null);

		print("--ok--");

	}

	@Test
	public void testGHSC3() throws Exception {
		String value = IOUtils.toString(getClass().getResourceAsStream("/fc.json"));

		gisManager.getGISService().tdghscAnalysis2("320584", value, null, null);

		print("--ok--");

	}


	@Test
	public void testTest() throws Exception {

		List list = gisManager.getGISService().query("SDE.JS_Polygon", "OBJECTID = 1", null, true, null);
		Geometry geo = gisManager.getGeoService().readWKT((String) ((Map) list.get(0)).get("SHAPE"));

		print("");

	}

	@Test
	public void testInsert() throws Exception {
		String layerName = "tdzz_320000";
		String geoJSON = "{\"type\": \"Feature\",\"crs\": {\"type\": \"name\",\"properties\": {\"name\": \"EPSG:2364\"}},\"properties\":{\"XMID\":\"9999999999\"},\"geometry\": {\"type\":\"Polygon\",\"coordinates\":[[[40535112.381,3483837.387],[40535052.45,3483834.42],[40535051.097,3483869.397],[40535111.133,3483872.363],[40535112.381,3483837.387]]]}}";
		try {
			gisManager.getGISService().insert(layerName, geoJSON, null);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

		print("--ok--");
	}

	@Test
	public void testInsert2() throws Exception {

		String geoJSON = "{\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"properties\":{\"PROID\":\"9999999999\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40589264.4762,3533165.0765],[40588793.531,3532155.9084],[40590475.4779,3532122.2694],[40589264.4762,3533165.0765]]]}}";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod("http://192.168.51.164:8088/omp/geometryService/rest/intersect");
		postMethod.addParameter("layerName", "SDE.GDDK_H_320604");
		postMethod.addParameter("geometry", geoJSON);
		httpClient.executeMethod(postMethod);
		String responseBody = postMethod.getResponseBodyAsString();
		print(responseBody);
		Map map = JSON.parseObject(responseBody, Map.class);
		String result = (String) map.get("result");

		FeatureJSON featureJSON = new FeatureJSON();
		FeatureCollection featureCollection = featureJSON.readFeatureCollection(result);
		FeatureIterator iterator = featureCollection.features();
		while (iterator.hasNext()) {
			SimpleFeature feature = (SimpleFeature) iterator.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			String attr1 = (String) feature.getAttribute("proid");
			SimpleFeatureType type = feature.getFeatureType();

		}

		print("--");

	}


	@Test
	public void testProject5() throws Exception {

		String coords = "{\"type\":\"Polygon\",\"coordinates\":[[[40535112.381,3483837.387],[40535052.45,3483834.42],[40535051.097,3483869.397],[40535111.133,3483872.363],[40535112.381,3483837.387]]]}";
		Geometry srcGeo = gisManager.getGeoService().readGeoJSON(coords);

		CoordinateReferenceSystem crs1 = gisManager.getGeoService().getCRSBySRID("2364");
		CoordinateReferenceSystem crs2 = gisManager.getGeoService().getCRSBySRID("4610");
		Geometry geometry = gisManager.getGeoService().project(srcGeo, crs1, crs2);
		print("--");
	}

	@Test
	public void testMPolygin() throws Exception {
		String coords = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[40541635.746,3483119.112],[40541648.875,3483103.659],[40541658.799,3483120.763],[40541701.195,3483093.851],[40541695.097,3483073.364],[40541718.003,3483059.67],[40541742.77,3483111.569],[40541744.433,3483139.807],[40541773.813,3483138.7],[40541774.577,3483147.635],[40541806.886,3483149.589],[40541771.485,3482931.504],[40541768.269,3482911.728],[40541758.499,3482851.635],[40541744.989,3482768.546],[40541719.899,3482800.543],[40541538.689,3482814.998],[40541591.612,3482900.178],[40541602.662,3482917.965],[40541624.564,3482953.215],[40541602.225,3482963.862],[40541594.145,3482971.932],[40541596.045,3482975.142],[40541595.096,3482980.118],[40541591.185,3482983.212],[40541582.625,3482988.432],[40541585.005,3482993.652],[40541649.703,3483101.297],[40541635.746,3483119.112]]],[[[40541847.619,3483156.464],[40541947.307,3483157.288],[40541993.821,3483128.008],[40541994.575,3483129.605],[40542100.574,3483068.695],[40542061.752,3482828.13],[40542060.021,3482817.429],[40542049.807,3482754.109],[40542041.706,3482704.226],[40542037.407,3482677.651],[40542034.565,3482660.088],[40542027.274,3482615.022],[40541838.251,3482626.605],[40541773.14,3482630.595],[40541763.087,3482631.211],[40541785.7,3482770.29],[40541797.583,3482843.377],[40541808.035,3482907.662],[40541836.515,3483082.82],[40541844.238,3483130.321],[40541847.619,3483156.464]]],[[[40539563.954,3483001.667],[40539608.263,3483014.028],[40539610.986,3483008.372],[40539611.063,3483008.443],[40539619.63,3482991.42],[40539615.891,3482985.139],[40539607.785,3482981.198],[40539599.479,3482977.182],[40539595.139,3482975.461],[40539588.505,3482972.917],[40539581.371,3482970.173],[40539577.184,3482968.542],[40539569.15,3482965.434],[40539565.633,3482963.888],[40539561.592,3482962.067],[40539554.983,3482959.148],[40539546.876,3482955.557],[40539538.97,3482952.04],[40539530.34,3482948.149],[40539527.072,3482947.301],[40539556.672,3482990.966],[40539563.954,3483001.667]]],[[[40539519.862,3482936.791],[40539530.504,3482940.93],[40539547.531,3482947.611],[40539570.53,3482956.657],[40539583.445,3482961.697],[40539587.498,3482963.279],[40539599.322,3482967.89],[40539630.657,3482981.134],[40539652.768,3482994.141],[40539670.978,3483001.59],[40539671.269,3483001.859],[40539704.23,3482967.583],[40539702.512,3482966.813],[40539700.946,3482966.44],[40539694.552,3482964.892],[40539687.393,3482963.14],[40539684.634,3482962.469],[40539676.431,3482960.493],[40539673.634,3482959.616],[40539662.076,3482956.111],[40539654.209,3482953.706],[40539645.39,3482952.047],[40539637.337,3482950.537],[40539630.141,3482949.157],[40539621.546,3482947.554],[40539611.74,3482945.727],[40539607.62,3482943.881],[40539602.158,3482941.439],[40539593.955,3482937.748],[40539591.829,3482936.946],[40539585.454,3482934.597],[40539576.505,3482931.278],[40539568.339,3482928.258],[40539559.447,3482924.958],[40539551.729,3482922.087],[40539545.781,3482920.763],[40539536.591,3482918.619],[40539522.832,3482921.639],[40539517.034,3482928.929],[40539515.724,3482930.594],[40539519.862,3482936.791]]],[[[40539499.779,3482887.449],[40539512.555,3482892.144],[40539519.33,3482879.428],[40539523.415,3482873.562],[40539526.452,3482874.19],[40539534.204,3482893.464],[40539543.002,3482904.463],[40539555.677,3482907.605],[40539566.14,3482910.64],[40539592.842,3482882.917],[40539510.489,3482858.319],[40539499.779,3482887.449]]],[[[40539617.119,3482924.883],[40539628.871,3482928.158],[40539644.922,3482932.651],[40539655.546,3482935.634],[40539658.622,3482936.497],[40539661.935,3482937.426],[40539662.847,3482937.68],[40539663.898,3482937.977],[40539664.691,3482938.199],[40539665.77,3482938.501],[40539666.629,3482938.742],[40539667.467,3482938.975],[40539668.562,3482939.283],[40539669.861,3482939.649],[40539671.151,3482940.011],[40539672.409,3482940.364],[40539673.327,3482940.621],[40539674.518,3482940.954],[40539675.824,3482941.319],[40539677.261,3482941.725],[40539678.654,3482942.115],[40539679.925,3482942.471],[40539680.989,3482942.77],[40539681.942,3482943.038],[40539682.935,3482943.313],[40539684.35,3482943.71],[40539685.223,3482943.955],[40539686.149,3482944.214],[40539687.217,3482944.517],[40539688.081,3482944.756],[40539689.117,3482945.049],[40539690.52,3482945.441],[40539691.457,3482945.706],[40539692.579,3482946.019],[40539693.245,3482946.208],[40539694.104,3482946.448],[40539694.974,3482946.689],[40539696.11,3482947.01],[40539697.064,3482947.277],[40539697.959,3482947.528],[40539698.708,3482947.835],[40539699.49,3482948.16],[40539700.237,3482948.466],[40539701.885,3482949.149],[40539702.807,3482949.526],[40539703.613,3482949.86],[40539704.341,3482950.161],[40539705.015,3482950.439],[40539705.935,3482950.82],[40539706.969,3482951.247],[40539707.951,3482951.654],[40539708.859,3482952.029],[40539709.879,3482952.447],[40539710.914,3482952.877],[40539711.982,3482953.317],[40539713.225,3482953.83],[40539713.996,3482954.14],[40539714.937,3482954.505],[40539738.826,3482928.6],[40539653.129,3482900.93],[40539642.056,3482897.622],[40539617.119,3482924.883]]],[[[40539702.647,3482831.382],[40539766.959,3482889.681],[40539769.381,3482887.292],[40539704.986,3482828.825],[40539702.647,3482831.382]]],[[[40542106.963,3482239.363],[40542135.613,3482236.951],[40542126.229,3482211.599],[40542102.983,3482140.328],[40542047.738,3482145.027],[40542106.963,3482239.363]]]]}";
		String coords2 = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[40541635.746,3483119.112],[40541648.875,3483103.659],[40541658.799,3483120.763],[40541701.195,3483093.851],[40541695.097,3483073.364],[40541718.003,3483059.67],[40541742.77,3483111.569],[40541744.433,3483139.807],[40541773.813,3483138.7],[40541774.577,3483147.635],[40541806.886,3483149.589],[40541771.485,3482931.504],[40541768.269,3482911.728],[40541758.499,3482851.635],[40541744.989,3482768.546],[40541719.899,3482800.543],[40541538.689,3482814.998],[40541591.612,3482900.178],[40541602.662,3482917.965],[40541624.564,3482953.215],[40541602.225,3482963.862],[40541594.145,3482971.932],[40541596.045,3482975.142],[40541595.096,3482980.118],[40541591.185,3482983.212],[40541582.625,3482988.432],[40541585.005,3482993.652],[40541649.703,3483101.297],[40541635.746,3483119.112]]],[[[40541847.619,3483156.464],[40541947.307,3483157.288],[40541993.821,3483128.008],[40541994.575,3483129.605],[40542100.574,3483068.695],[40542061.752,3482828.13],[40542060.021,3482817.429],[40542049.807,3482754.109],[40542041.706,3482704.226],[40542037.407,3482677.651],[40542034.565,3482660.088],[40542027.274,3482615.022],[40541838.251,3482626.605],[40541773.14,3482630.595],[40541763.087,3482631.211],[40541785.7,3482770.29],[40541797.583,3482843.377],[40541808.035,3482907.662],[40541836.515,3483082.82],[40541844.238,3483130.321],[40541847.619,3483156.464]]],[[[40539563.954,3483001.667],[40539608.263,3483014.028],[40539610.986,3483008.372],[40539611.063,3483008.443],[40539619.63,3482991.42],[40539615.891,3482985.139],[40539607.785,3482981.198],[40539599.479,3482977.182],[40539595.139,3482975.461],[40539588.505,3482972.917],[40539581.371,3482970.173],[40539577.184,3482968.542],[40539569.15,3482965.434],[40539565.633,3482963.888],[40539561.592,3482962.067],[40539554.983,3482959.148],[40539546.876,3482955.557],[40539538.97,3482952.04],[40539530.34,3482948.149],[40539527.072,3482947.301],[40539556.672,3482990.966],[40539563.954,3483001.667]]],[[[40539519.862,3482936.791],[40539530.504,3482940.93],[40539547.531,3482947.611],[40539570.53,3482956.657],[40539583.445,3482961.697],[40539587.498,3482963.279],[40539599.322,3482967.89],[40539630.657,3482981.134],[40539652.768,3482994.141],[40539670.978,3483001.59],[40539671.269,3483001.859],[40539704.23,3482967.583],[40539702.512,3482966.813],[40539700.946,3482966.44],[40539694.552,3482964.892],[40539687.393,3482963.14],[40539684.634,3482962.469],[40539676.431,3482960.493],[40539673.634,3482959.616],[40539662.076,3482956.111],[40539654.209,3482953.706],[40539645.39,3482952.047],[40539637.337,3482950.537],[40539630.141,3482949.157],[40539621.546,3482947.554],[40539611.74,3482945.727],[40539607.62,3482943.881],[40539602.158,3482941.439],[40539593.955,3482937.748],[40539591.829,3482936.946],[40539585.454,3482934.597],[40539576.505,3482931.278],[40539568.339,3482928.258],[40539559.447,3482924.958],[40539551.729,3482922.087],[40539545.781,3482920.763],[40539536.591,3482918.619],[40539522.832,3482921.639],[40539517.034,3482928.929],[40539515.724,3482930.594],[40539519.862,3482936.791]]],[[[40539499.779,3482887.449],[40539512.555,3482892.144],[40539519.33,3482879.428],[40539523.415,3482873.562],[40539526.452,3482874.19],[40539534.204,3482893.464],[40539543.002,3482904.463],[40539555.677,3482907.605],[40539566.14,3482910.64],[40539592.842,3482882.917],[40539510.489,3482858.319],[40539499.779,3482887.449]]],[[[40539617.119,3482924.883],[40539628.871,3482928.158],[40539644.922,3482932.651],[40539655.546,3482935.634],[40539658.622,3482936.497],[40539661.935,3482937.426],[40539662.847,3482937.68],[40539663.898,3482937.977],[40539664.691,3482938.199],[40539665.77,3482938.501],[40539666.629,3482938.742],[40539667.467,3482938.975],[40539668.562,3482939.283],[40539669.861,3482939.649],[40539671.151,3482940.011],[40539672.409,3482940.364],[40539673.327,3482940.621],[40539674.518,3482940.954],[40539675.824,3482941.319],[40539677.261,3482941.725],[40539678.654,3482942.115],[40539679.925,3482942.471],[40539680.989,3482942.77],[40539681.942,3482943.038],[40539682.935,3482943.313],[40539684.35,3482943.71],[40539685.223,3482943.955],[40539686.149,3482944.214],[40539687.217,3482944.517],[40539688.081,3482944.756],[40539689.117,3482945.049],[40539690.52,3482945.441],[40539691.457,3482945.706],[40539692.579,3482946.019],[40539693.245,3482946.208],[40539694.104,3482946.448],[40539694.974,3482946.689],[40539696.11,3482947.01],[40539697.064,3482947.277],[40539697.959,3482947.528],[40539698.708,3482947.835],[40539699.49,3482948.16],[40539700.237,3482948.466],[40539701.885,3482949.149],[40539702.807,3482949.526],[40539703.613,3482949.86],[40539704.341,3482950.161],[40539705.015,3482950.439],[40539705.935,3482950.82],[40539706.969,3482951.247],[40539707.951,3482951.654],[40539708.859,3482952.029],[40539709.879,3482952.447],[40539710.914,3482952.877],[40539711.982,3482953.317],[40539713.225,3482953.83],[40539713.996,3482954.14],[40539714.937,3482954.505],[40539738.826,3482928.6],[40539653.129,3482900.93],[40539642.056,3482897.622],[40539617.119,3482924.883]]],[[[40539702.647,3482831.382],[40539766.959,3482889.681],[40539769.381,3482887.292],[40539704.986,3482828.825],[40539702.647,3482831.382]]],[[[40542106.963,3482239.363],[40542135.613,3482236.951],[40542126.229,3482211.599],[40542102.983,3482140.328],[40542047.738,3482145.027],[40542106.963,3482239.363]]]] }";
		Geometry geo = (Geometry) geometryService.readUnTypeGeoJSON(coords2);
		CoordinateReferenceSystem srcCrs = geometryService.getCRSBySRID("2364");
		CoordinateReferenceSystem destCrs = geometryService.getCRSBySRID("4610");

		Geometry geo2 = geometryService.project(geo, srcCrs, destCrs);

		print(geo);

		String geoJson = geometryService.toGeoJSON(geo2);

		gisManager.getGISService().tdlyxzAnalysis("2010", geoJson, null);


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
		print(value);
	}

	@Test
	public void testQuery3() throws Exception {

		try {
			List list = gisManager.getGISService().query("JCDL.XZQ_F", "OBJECTID > 0", null, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		print("--out--");

	}

	@Test
	public void testParseGeoJOSN() throws Exception {
		String coords = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[120.35252021952965,31.505160840825408],[120.35260873487029,31.505017197652954],[120.35261223333029,31.505018171065029],[120.35161897103389,31.504562468195527],[120.35051759105448,31.506056139087693],[120.35055327134789,31.506176081893209],[120.35077946625893,31.506260148803815],[120.35100369663063,31.506347981549524],[120.35121307904478,31.506434276357499],[120.35144594760554,31.506534828210036],[120.35171621240117,31.50666479546172],[120.35178719529912,31.506527415034856],[120.35186768006905,31.506375046212771],[120.35196702388411,31.506196200316666],[120.35198458952681,31.50616440574489],[120.35227411324917,31.505641960317185],[120.35239144666245,31.505407319204544],[120.3524549247329,31.505293056263721],[120.35247190515749,31.505273709211128],[120.35252021952965,31.505160840825408]]],[[[120.35064126125579,31.531027341427773],[120.3506596677945,31.530416159784497],[120.35053553063364,31.53049056233554],[120.3502016334596,31.53068808653893],[120.35013805945195,31.530750850976982],[120.35004038116507,31.530901281159963],[120.34949301969043,31.531401787492154],[120.34947012174126,31.531530007040264],[120.35016649138477,31.532018490109767],[120.35018022616732,31.532005519566919],[120.35042715469231,31.531771491192313],[120.35060123156441,31.531605952308283],[120.35064855424113,31.53155887077758],[120.3507064548804,31.531499413505934],[120.35078474480781,31.531415747978652],[120.35096903089639,31.531198710367914],[120.35099468254353,31.531168246663984],[120.35064126125579,31.531027341427773]]],[[[120.35013513195545,31.530615306586768],[120.35063936739539,31.530326045576796],[120.35066130696934,31.530361708858464],[120.35066919437988,31.5300998988256],[120.35055565103374,31.530174533899601],[120.3498144007913,31.530661783131023],[120.34968772648121,31.530745048250832],[120.3498321325431,31.53085956178132],[120.34981865906036,31.530871981427797],[120.34986377234787,31.530907274826095],[120.34995942016798,31.530819341522971],[120.35013513195545,31.530615306586768]]],[[[120.35066919437988,31.5300998988256],[120.35069887972449,31.529114577698206],[120.35057053726838,31.529111303951453],[120.34981758386354,31.52909211705099],[120.34972902322382,31.529069694989765],[120.34965057665167,31.529026267511028],[120.34947525210652,31.529022760328026],[120.34942531906677,31.529021823503697],[120.34942524512788,31.529021760574139],[120.34941139817197,31.529021482741417],[120.34934724913893,31.529008815194537],[120.34894162967998,31.529421603130192],[120.34896577275052,31.529466766506932],[120.34956012771157,31.530509528615084],[120.3496298292059,31.53058784667871],[120.34955287809947,31.530638111274708],[120.34968772648121,31.530745048250832],[120.35066919437988,31.5300998988256]],[[120.35006506986416,31.529537184734828],[120.34975209358805,31.529092521604994],[120.35002452895921,31.529273784636974],[120.35032481947449,31.529554889977916],[120.35042523903715,31.529772508895473],[120.35052530377003,31.529782245580826],[120.35042019817585,31.529867971269844],[120.350353777257,31.529922146006264],[120.35032433847499,31.529905541873699],[120.3500724055353,31.529547608418838],[120.35006506986416,31.529537184734828]]],[[[120.36933383235116,31.478242890109144],[120.36810462858412,31.478145013134991],[120.36792737312049,31.478266681947051],[120.36787034339021,31.478647955569564],[120.36798382096246,31.478734517102112],[120.3682708813034,31.479032351050378],[120.36836509312865,31.479100659379363],[120.36842717339715,31.479149380968945],[120.36848461803223,31.479202120240888],[120.36849706335499,31.479214494362804],[120.36850828490527,31.479223408766217],[120.36875755883671,31.47946511779535],[120.3688788154297,31.479551737278211],[120.369138321706,31.479649069329756],[120.36948981043879,31.479750329184544],[120.36942926273051,31.478329403214932],[120.36933383235116,31.478242890109144]]],[[[120.46584473837042,31.457026008191434],[120.46540713012332,31.457020789907801],[120.46496947703709,31.457023524902809],[120.46453199993977,31.457034212375728],[120.46409488814116,31.457052860661868],[120.46365833077364,31.457079442024366],[120.4638286199183,31.460058395124953],[120.4639748607911,31.460071925759188],[120.46602338354883,31.4602739863032],[120.46628212303412,31.457039180404216],[120.46584473837042,31.457026008191434]]],[[[120.36950021762874,31.476239042585753],[120.36886944832696,31.476214102451763],[120.36885644774082,31.476529592474932],[120.36948832405839,31.476554520447934],[120.36950021762874,31.476239042585753]]]]}";


		Geometry geometry = (Geometry) gisManager.getGeoService().readUnTypeGeoJSON(coords);
//        Geometry geometry = (Geometry) gisManager.getGeoService().readGeoJSON(coords);

		TopologyValidationError topologyValidationError = gisManager.getGeoService().validGeometry(geometry);

		Map result = gisManager.getGISService().tdlyxzAnalysis("2010", coords, null);

		String c = "";
		if (geometry instanceof MultiPolygon) {
			int num = geometry.getNumGeometries();
			for (int i = 0; i < num; i++) {
				Geometry geo = geometry.getGeometryN(i);
//                c += (i + 1) + ",";
				if (geo instanceof Polygon) {
					Polygon polygon = (Polygon) geo.getGeometryN(i);
					boolean bhole = polygon.getNumInteriorRing() > 0 ? true : false;

					Coordinate[] shell = polygon.getExteriorRing().getCoordinates();
					for (int j = 0; j < shell.length; j++) {
						c += (i + 1) + "," + (j + 1) + ",";
						Coordinate coordinate = shell[j];
						c += coordinate.x + "," + coordinate.y + (bhole ? ",1\n" : ",NULL\n");
					}
					if (bhole) {
						for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
							Coordinate[] holes = polygon.getInteriorRingN(n).getCoordinates();
							for (int m = 0; m < holes.length; m++) {
								c += (i + 1) + "," + (m + 1) + ",";
								Coordinate coordinate = holes[m];
								c += coordinate.x + "," + coordinate.y + "," + (n + 2) + "\n";
							}
						}
					}

				}
			}

		}


		print("----\n");
		print(c);
		print("----\n");

	}

	@Test
	public void testTdlyxzAnalysis() throws Exception {

		String geometry = "{\"crs\":{\"properties\":{\"name\":\"EPSG:4610\"},\"type\":\"name\"},\"geometry\":{\"coordinates\":[[[119.49033281977158,32.62680426874257],[119.48767982068743,32.62724518215311],[119.48813093794061,32.62848038415584],[119.49106320008624,32.62859853391263],[119.4908698641206,32.627836467981375],[119.49033281977158,32.62680426874257]]],\"type\":\"Polygon\"},\"type\":\"Feature\"}";
		String geometry2 = "{\"crs\":{\"properties\":{\"name\":\"EPSG:4610\"},\"type\":\"name\"},\"geometry\":{\"coordinates\":[[[119.04037174089011,33.02815208835639],[119.04724590475038,33.02144977859263],[119.04896444571544,33.00340509845944],[119.0195773952128,33.007701450872105],[119.04037174089011,33.02815208835639]]],\"type\":\"Polygon\"},\"type\":\"Feature\"}";

		Map result = gisManager.getGISService().tdlyxzAnalysis("2011", geometry, null);

//        String r = gisManager.getGISService().intersect2("TDLY.JBNTBHPK_H_2011", geometry, null, null);
//        r = gisManager.getGISService().intersect2("BPDK_320000", geometry2, null, "glsj");

		print(result);

	}

	@Test
	public void testForTest() throws Exception {
		String geometry = "{\"crs\":{\"properties\":{\"name\":\"EPSG:4610\"},\"type\":\"name\"},\"geometry\":{\"coordinates\":[[[119.49033281977158,32.62680426874257],[119.48767982068743,32.62724518215311],[119.48813093794061,32.62848038415584],[119.49106320008624,32.62859853391263],[119.4908698641206,32.627836467981375],[119.49033281977158,32.62680426874257]]],\"type\":\"Polygon\"},\"type\":\"Feature\"}";

		String geometry2 = "{\"crs\":{\"properties\":{\"name\":\"EPSG:4610\"},\"type\":\"name\"},\"geometry\":{\"coordinates\":[[[119.04037174089011,33.02815208835639],[119.04724590475038,33.02144977859263],[119.04896444571544,33.00340509845944],[119.0195773952128,33.007701450872105],[119.04037174089011,33.02815208835639]]],\"type\":\"Polygon\"},\"type\":\"Feature\"}";

		for (int i = 0; i < 2000; i++) {
//            String r = gisManager.getGISService().intersect2("TDLY.JBNTBHPK_H_2011", geometry, null, null);
//            r = gisManager.getGISService().intersect2("BPDK_320000", geometry2, null, "glsj");
			print("--index" + i + "--time: " + System.currentTimeMillis());
		}

        /*for (int i = 0; i < 100; i++) {
            Map result = gisManager.getGISService().tdlyxzAnalysis("2011", geometry, null);
            print("--time: " + System.currentTimeMillis());
        }*/

		print("---ok---");
	}

	@Test
	public void testTDlyCQ() throws Exception {

		String coords = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[120.4399182625246,31.469813324125333],[120.43953657025675,31.467847762951742],[120.43950189779224,31.467669525511234],[120.43939656529557,31.46712791820357],[120.43925091343701,31.466379051929302],[120.43898826348807,31.466668528264645],[120.43708217495305,31.466805415655866],[120.43764260608009,31.467571725921303],[120.4377596219247,31.467731743955142],[120.4379915578203,31.468048864749555],[120.43775694992567,31.468145690516106],[120.43767226932883,31.468218761965144],[120.43769239618274,31.46824764362734],[120.43768261942036,31.468292554761028],[120.43764159692617,31.468320599241419],[120.43755174587032,31.468367984605599],[120.43757700770104,31.468414976594268],[120.43826229653961,31.469383465204668],[120.43811618486228,31.46954463569249],[120.43825368330599,31.469404797164731],[120.43835882564552,31.46955869536162],[120.4388037984216,31.469314456487322],[120.43873877162385,31.46912991050203],[120.43897921882127,31.469005582544185],[120.43924200953568,31.469472749708842],[120.43926069738538,31.469727359349815],[120.43956979749387,31.469716314783707],[120.4395782131366,31.469796869144314],[120.4399182625246,31.469813324125333]]],[[[120.44139614889176,31.469877674174445],[120.44188434582166,31.469611919049832],[120.44189234732379,31.469626294507631],[120.44300511763002,31.469073112638817],[120.44258641442072,31.466904945363119],[120.44256774677054,31.466808499350641],[120.44245758905593,31.466237807484632],[120.44237023529506,31.465788222772368],[120.44232387478682,31.4655487073831],[120.44229322695659,31.465390415441046],[120.44221460167923,31.464984244139124],[120.44022622421973,31.465095560179915],[120.43954130510026,31.465133898049345],[120.43943555498824,31.465139816580287],[120.43967934725053,31.466393310467073],[120.43980746142783,31.46705203008905],[120.43992014907204,31.467631418733106],[120.44022721149469,31.469210084042775],[120.44031048041255,31.469638201211598],[120.44034716017616,31.46987385435289],[120.44139614889176,31.469877674174445]]],[[[120.41677809140211,31.468668129567142],[120.41680651752063,31.468617026459402],[120.41680733057187,31.468617664150869],[120.4168967943975,31.468463845124845],[120.41685720071033,31.46840732666103],[120.41677174998448,31.468372061540947],[120.41668419188025,31.468336126807422],[120.4166384566601,31.468320754191229],[120.41656855059934,31.468298037682366],[120.41649337547436,31.468273534504032],[120.41644925380834,31.468258968267659],[120.41636459421818,31.468231212998564],[120.41632752586949,31.468217390387501],[120.41628493291554,31.46820110554329],[120.41621527511896,31.46817500596611],[120.4161298282816,31.468142896997136],[120.41604649941691,31.468111448483864],[120.41595553761896,31.468076651662177],[120.41592111725012,31.468069115485182],[120.41623431682035,31.468461905685551],[120.41631136690589,31.468558165866924],[120.41677809140211,31.468668129567142]]],[[[120.41611691842725,31.46782913723489],[120.41602013413888,31.467810115300541],[120.41587547998063,31.467837822088793],[120.41581476301545,31.46790376665124],[120.41580104532726,31.467918827573282],[120.41584483317993,31.467974575169212],[120.41595697538318,31.468011539945255],[120.41613640332309,31.468071211884183],[120.41637876451651,31.468152008518274],[120.41651486010601,31.468197020891793],[120.41655756981893,31.468211149775513],[120.41668216869721,31.468252330284145],[120.41701241195021,31.468370700821222],[120.41724558934747,31.468487249352648],[120.41743749755447,31.468553805260083],[120.41744057029571,31.468556221312198],[120.41778602140226,31.468245964788611],[120.41776791339159,31.468239079356955],[120.41775142066034,31.468235769134342],[120.41768407961622,31.468222027636401],[120.41760868093009,31.468206472536828],[120.41757962327905,31.468200515653027],[120.417493230472,31.468182976115603],[120.4174637647466,31.468175162653864],[120.41734200879635,31.468143948483409],[120.41725913438374,31.46812252823587],[120.41716627270648,31.468107868529476],[120.41708147702198,31.468094526291761],[120.41700570408427,31.46808232706503],[120.41691520169503,31.468068164548232],[120.41681194799949,31.468052023252319],[120.41676852278638,31.468035515806481],[120.41671095299363,31.468013679144445],[120.4166244920138,31.467980671920607],[120.41660208980615,31.467973511691316],[120.41653491704625,31.467952544943778],[120.41644062148606,31.467922918089815],[120.41635457678672,31.467895960973884],[120.41626088186094,31.467866503391814],[120.41617955714277,31.467840874622166],[120.41611691842725,31.46782913723489]]],[[[120.41661059086994,31.467486204818183],[120.41574308105801,31.467267178655181],[120.41563154993909,31.467530259710635],[120.41576616794794,31.467572166185018],[120.41583694855609,31.467457252702211],[120.41587969759445,31.467404209319508],[120.41591167833278,31.467409769287336],[120.41599401451545,31.467583331082249],[120.41608702710398,31.46768222717629],[120.41622052082148,31.467710130555158],[120.41633073532061,31.467737144466223],[120.41661059086994,31.467486204818183]]],[[[120.41724565930711,31.467646593570908],[120.41712901523302,31.467617139247277],[120.41686771387378,31.467863852910519],[120.4169915014166,31.467892986586076],[120.41716057261026,31.46793295758086],[120.41727247962349,31.467959496183759],[120.41730488039602,31.467967173861521],[120.41733977757315,31.467975438638739],[120.41734938397192,31.467977698116453],[120.41736045467594,31.4679803406312],[120.41736880765473,31.467982315589673],[120.41738017318116,31.467985002236009],[120.41738922138563,31.467987146285154],[120.41739804830434,31.46798921890419],[120.4174095824276,31.467991959111401],[120.41742326539948,31.467995215405807],[120.41743685351246,31.467998435932593],[120.41745010455605,31.46800157638717],[120.41745977421326,31.468003862706883],[120.41747231947103,31.468006825083172],[120.41748607606247,31.468010072111973],[120.41750121270313,31.468013684412423],[120.41751588572798,31.468017153921192],[120.41752927368535,31.468020320977676],[120.41754048126788,31.468022981068145],[120.41755051964655,31.468025365385987],[120.41756097919369,31.468027811461567],[120.41757588399138,31.468031343339568],[120.41758507967539,31.468033522969254],[120.41759483359675,31.468035827041625],[120.41760608343179,31.468038523063939],[120.41761518417724,31.468040648887985],[120.41762609690279,31.468043255819506],[120.41764087523872,31.468046743008106],[120.41765074514718,31.46804910081098],[120.41766256358434,31.468051885162208],[120.41766957893523,31.468053566841984],[120.41767862711265,31.468055701851831],[120.41768779107458,31.468057845502191],[120.41769975714512,31.468060701519185],[120.41770980601386,31.468063076771415],[120.41771923343262,31.468065309748575],[120.41772712685353,31.468068052784492],[120.41773536822841,31.468070957024015],[120.41774324056583,31.468073691108867],[120.4177606085043,31.468079794319316],[120.4177703250718,31.468083162723783],[120.41777881934254,31.468086147305019],[120.41778649156039,31.468088836945881],[120.4177935946586,31.468091321009698],[120.41780329034431,31.468094725555165],[120.41781418740418,31.468098541046828],[120.41782453650939,31.468102177948616],[120.41783410569049,31.468105528791423],[120.41784485508158,31.468109263592723],[120.4178557627872,31.468113106102624],[120.4178670181271,31.468117037665611],[120.41788011777818,31.468121621584611],[120.41788824281709,31.468124390911214],[120.41789815883483,31.468127650428876],[120.41814848530829,31.467893200075473],[120.41724565930711,31.467646593570908]]],[[[120.41846833326484,31.467519604789889],[120.41778841854892,31.466994520036927],[120.41776390981374,31.467017661185874],[120.41844294448639,31.467541233776707],[120.41846833326484,31.467519604789889]]],[[[120.44323864850662,31.461342297228061],[120.44299104177179,31.460700372146547],[120.44240998958574,31.460744760232668],[120.44303712193644,31.461593393629425],[120.44333845867403,31.461570597380216],[120.44323864850662,31.461342297228061]]]]}";

		Map result = gisManager.getGISService().tdlyxzAnalysis("2011", coords, null);

		print("--ok--");

	}

	@Test
	public void testNewIntersect() throws Exception {
		String coords = "{\"type\":\"Polygon\",\"coordinates\":[[[501292.104,3466858.417],[501903.954,3466266.411],[500858.847,3466239.953],[501292.104,3466858.417]]]}";
		String s = "PROJCS[\"Xian_1980_3_Degree_GK_CM_118.83E\", \n" +
				"  GEOGCS[\"GCS_Xian_1980\", \n" +
				"    DATUM[\"D_Xian_1980\", \n" +
				"      SPHEROID[\"Xian_1980\", 6378140.0, 298.257]], \n" +
				"    PRIMEM[\"Greenwich\", 0.0], \n" +
				"    UNIT[\"degree\", 0.017453292519943295], \n" +
				"    AXIS[\"Longitude\", EAST], \n" +
				"    AXIS[\"Latitude\", NORTH]], \n" +
				"  PROJECTION[\"Transverse_Mercator\"], \n" +
				"  PARAMETER[\"central_meridian\", 118.83333333333333], \n" +
				"  PARAMETER[\"latitude_of_origin\", 0.0], \n" +
				"  PARAMETER[\"scale_factor\", 1.0], \n" +
				"  PARAMETER[\"false_easting\", 500000.0], \n" +
				"  PARAMETER[\"false_northing\", 0.0], \n" +
				"  UNIT[\"m\", 1.0], \n" +
				"  AXIS[\"x\", EAST], \n" +
				"  AXIS[\"y\", NORTH]]";
		String dd = "PROJCS[\"Xian_1980_3_Degree_GK_CM_118.83E\",GEOGCS[\"GCS_Xian_1980\",    DATUM[\"D_Xian_1980\",      SPHEROID[\"Xian_1980\", 6378140.0, 298.257]],     PRIMEM[\"Greenwich\", 0.0],     UNIT[\"degree\", 0.017453292519943295],     AXIS[\"Longitude\", EAST],     AXIS[\"Latitude\", NORTH]],   PROJECTION[\"Transverse_Mercator\"],   PARAMETER[\"central_meridian\", 118.83333333333334],   PARAMETER[\"latitude_of_origin\", 0.0],   PARAMETER[\"scale_factor\", 1.0],   PARAMETER[\"false_easting\", 500000.0],   PARAMETER[\"false_northing\", 0.0],   UNIT[\"m\", 1.0],   AXIS[\"x\", EAST],   AXIS[\"y\", NORTH]]";

		CoordinateReferenceSystem srcCrs = geometryService.getCRSByWKTString(dd);
		CoordinateReferenceSystem destCrs = geometryService.getCRSBySRID("4610");
		Geometry geometry = (Geometry) geometryService.readUnTypeGeoJSON(coords);
		geometry = geometryService.project(geometry, srcCrs, destCrs);
		gisManager.getGISService().intersect("DWH.DLTB_320125", geometry, null, null);


	}

	@Test
	public void testNewInsertByRegionField() throws Exception {

		String layerName = "DWH.BPDK_320000";
		Map column = new HashMap();
		column.put("PROID", "DDDDD");
		column.put("REGIONCODE", "320116");
		column.put("PB_PB_ID", "dddddddd");
		try {
			print(gisManager.getGISService().insert(layerName, column, null));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Test
	public void testGHSCAnalysis() throws Exception {

		String geometry = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[120.03697382973239,33.439553763501493],[120.03566511968226,33.439312289147551],[120.03556467237071,33.439540750599548],[120.03687207345465,33.439781973940995],[120.03697382973239,33.439553763501493]]],[[[120.03718185311004,33.439087233915735],[120.03587045000705,33.438845257612073],[120.03566511968226,33.439312289147551],[120.03697382973239,33.439553763501493],[120.03718185311004,33.439087233915735]]],[[[120.28787568350546,33.4428231431665],[120.28845628510413,33.441561373225483],[120.28881145018187,33.440789491410278],[120.28869466175867,33.440608567158712],[120.28731004042075,33.440360486470468],[120.28699465200073,33.441686797044206],[120.28694700297754,33.441887181490735],[120.28620618112056,33.44237718628063],[120.28610511857002,33.442337027446243],[120.28606692227989,33.442420036444659],[120.28752200821786,33.442744324657099],[120.28787568350546,33.4428231431665]]],[[[120.08424682124453,33.458969456368841],[120.08360142343062,33.45858460840374],[120.08297616554957,33.45931568384222],[120.08354175729251,33.459639162161167],[120.08367509470609,33.459715411518239],[120.08424682124453,33.458969456368841]]],[[[120.28145030975067,33.42902426854792],[120.28096932792502,33.42887165253196],[120.28060588788766,33.429655126858734],[120.2802840551324,33.430344819745557],[120.28050902546036,33.430413852333999],[120.28172570869145,33.430774200072122],[120.2820815129472,33.430879582989441],[120.28222490033853,33.430924670888963],[120.28270278786164,33.429919676151421],[120.28290856078679,33.429486939136659],[120.28145030975067,33.42902426854792]]],[[[120.28500568410051,33.439947601652918],[120.28498349598512,33.439943622453093],[120.28484057779842,33.440007613354993],[120.28478445945683,33.440115313123613],[120.28476700732341,33.44014882089909],[120.28558700165448,33.440295754760058],[120.28567534180996,33.440067588660327],[120.28500568410051,33.439947601652918]]],[[[120.0740964783396,33.458119444932095],[120.07298952873828,33.457514039397743],[120.07218337207811,33.458503676518703],[120.07333877714775,33.459135585851243],[120.0740964783396,33.458119444932095]]],[[[120.03827986900528,33.439794728970334],[120.03707991725649,33.439573332888443],[120.03659811050164,33.440653851391289],[120.03665200302729,33.440736919072783],[120.03777159602829,33.440943485174415],[120.03827986900528,33.439794728970334]]],[[[120.0812211320563,33.453367716701116],[120.08057111990742,33.452969935510723],[120.08027949097314,33.45338350405158],[120.0809164291815,33.453785064006809],[120.0812211320563,33.453367716701116]]],[[[120.03728386562254,33.43885845510399],[120.03595612274864,33.438650412299246],[120.03587045000705,33.438845257612073],[120.03718185311004,33.439087233915735],[120.03728386562254,33.43885845510399]]],[[[120.26376529614735,33.424183916267879],[120.26430931897258,33.423301279964001],[120.26379005352481,33.423115688619376],[120.26318637497839,33.424095092983045],[120.26264872559507,33.423875355809287],[120.26219728057083,33.424643302561009],[120.26322126238429,33.42506655913018],[120.26376529614735,33.424183916267879]]],[[[119.98896961110886,33.437606395830805],[119.98922807264566,33.436985793156808],[119.98877654580899,33.436853549577663],[119.98861238959833,33.436844166852673],[119.98916355778211,33.437005523848789],[119.98894004592228,33.437542225394829],[119.9881450368722,33.437329308145038],[119.98838680794334,33.436831279914678],[119.98835575108595,33.436829500824722],[119.98810957759054,33.437356163805141],[119.98896961110886,33.437606395830805]]],[[[120.29423732262262,33.433138417878823],[120.29292893538077,33.432441460520259],[120.29266136819631,33.433023062039894],[120.29214484173838,33.434145827306708],[120.29358283142609,33.43459772622397],[120.29415056142548,33.433331865535187],[120.29423732262262,33.433138417878823]]],[[[120.03849930463022,33.439298774048204],[120.03730102606966,33.439077442678943],[120.03707990650237,33.439573332891655],[120.03827986900528,33.439794728970334],[120.03849930463022,33.439298774048204]]],[[[120.03860269903994,33.439065088958152],[120.03739117715283,33.438875264923972],[120.03730102606966,33.439077442678943],[120.03849930463022,33.439298774048204],[120.03860269903994,33.439065088958152]]]]}";
		String year = "2020";
		String outFields = "*";
		String dataSource = "dwh";

		try {
			Map result = gisManager.getGISService().tdghscAnalysis2(year, geometry, null, dataSource);
			print("----");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Object geo = gisManager.getGeoService().readUnTypeGeoJSON(IOUtils.toString(getClass().getResourceAsStream("/fc.json")));
			String info = gisManager.getGeoService().toFeatureJSON(geo);
			print("----");
		} catch (GeometryServiceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void test94SDE() throws Exception {

		String layerName = "TDYTQ_E_2020";
		String geometry = "{\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4610\"}},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[119.25233585960136,33.0648985765072],[119.25774926364132,33.06403930602467],[119.25963965870288,33.05961406303962],[119.25383958294579,33.055876236440604],[119.24632096622362,33.05965702656375],[119.25233585960136,33.0648985765072]]]},\"type\":\"Feature\"}";

		String dataSource = "tdgh";

		try {
			List result = gisManager.getGISService().intersect3(layerName, geometry, null, dataSource);

			Map res = gisManager.getGISService().tdghscAnalysis2("2020", geometry, null, dataSource);
			print("----");


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Test
	public void testSimpleGeo() throws Exception {

		String layerName = "JCDL.XZQ_C_2011";

		List result = gisManager.getGISService().query(layerName, "objectid > 0", null, true, null);

		Map item = (Map) result.get(0);

		Geometry geometry = gisManager.getGeoService().readWKT((String) item.get("SHAPE"));
//        Geometry g1 = gisManager.getGeoService().forceSimplify(geometry, 1);
//        print("area--" + g1.getArea() + "count--" + g1.getNumPoints());
//        Geometry g2 = gisManager.getGeoService().forceSimplify(geometry, 0);
//        print("area--" + g2.getArea() + "count--" + g1.getNumPoints());
		Geometry g3 = gisManager.getGeoService().forceSimplify(geometry, 0.1);
		print("area--" + g3.getArea() + "count--" + g3.getNumPoints());
		Geometry g4 = gisManager.getGeoService().forceSimplify(geometry, 0.01);
		print("area--" + g4.getArea() + "count--" + g4.getNumPoints());
		Geometry g5 = gisManager.getGeoService().forceSimplify(geometry, 0.001);
		print("area--" + g5.getArea() + "count--" + g5.getNumPoints());
		Geometry g6 = gisManager.getGeoService().forceSimplify(geometry, 0.0001);
		print("area--" + g6.getArea() + "count--" + g6.getNumPoints());
		Geometry g7 = gisManager.getGeoService().forceSimplify(geometry, 0.00001);
		print("area--" + g7.getArea() + "count--" + g7.getNumPoints());

		print("--- --");
//        return gisManager.getGeoService().toGeoJSON(geometry);

	}

	@Test
	public void testGHSC36() throws Exception {

		String geometry = "{\"properties\":{\"dkArea\":\"1470\",\"purpose\":\"公共建筑\",\"plotId\":\"025010960000156993\",\"mapNo\":\"图幅号\",\"plotName\":\"镇江东区污水处理厂及配套工程（1）\"},\"type\":\"Feature\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4610\"}},\"geometry\":{\"coordinates\":[[[119.75933902338662,32.1785952726506],[119.75966302820305,32.17874919314357],[119.7598802044381,32.178418521956374],[119.7595561898166,32.17826460199535],[119.75933902338662,32.1785952726506]]],\"type\":\"Polygon\"}}";
		Map result = gisManager.getGISService().tdghscAnalysis2("2020", geometry, null, "tdgh");

		geometry = "{\"properties\":{\"purpose\":\"公共建筑\",\"mapNo\":\"图幅号\",\"dkArea\":\"1470\",\"plotId\":\"025010960000156993\",\"plotName\":\"镇江东区污水处理厂及配套工程（1）\"},\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[119.75933902338662,32.1785952726506],[119.75966302820305,32.17874919314357],[119.7598802044381,32.178418521956374],[119.7595561898166,32.17826460199535],[119.75933902338662,32.1785952726506]]]}}";
		result = gisManager.getGISService().tdghscAnalysis2("2020", geometry, null, "tdgh");

		print("----");
	}

	@Test
	public void testBPDK() throws Exception {
//        String coords = IOUtils.toString(getClass().getResourceAsStream("/bpdk1.txt"));
		String coords = "{\"type\": \"MultiPolygon\",\"coordinates\":[[[[120.25998313613991,33.372472544179971],[120.25959372159676,33.372275291469585],[120.24785454440359,33.392346719643804],[120.24814214193565,33.392476987490596],[120.25998313613991,33.372472544179971]]]]}";
		Map result = gisManager.getGISService().tdlyxzAnalysis("2010", coords, null);

		String geos1 = "{\"type\":\"Polygon\",\"coordinates\":[[[4.139586396520549E7,3509942.8849526425],[4.139585945827792E7,3509943.4804510316],[4.1395845601679824E7,3509930.7632241035],[4.139582518697331E7,3509909.57376962],[4.139580654087912E7,3509889.655202555],[4.139580511205965E7,3509887.9594981624],[4.1395803351238444E7,3509885.871220917],[4.139580244183299E7,3509881.998813524],[4.139579744115996E7,3509875.942965392],[4.139576533873521E7,3509842.2286397065],[4.1395744726681046E7,3509819.179276606],[4.1395716120019734E7,3509789.453357872],[4.139570767643176E7,3509779.8046848252],[4.139567256470868E7,3509757.2311908836],[4.139560833532075E7,3509715.14034468],[4.1395526330536716E7,3509662.5019967617],[4.139548181506982E7,3509634.3593110135],[4.1395452516463816E7,3509615.836670784],[4.139536053972156E7,3509554.042348971],[4.139531079692007E7,3509521.09910515],[4.139531079877121E7,3509521.094650302],[4.139542586014635E7,3509269.079775269],[4.1395481573167846E7,3509233.9413160793],[4.139550387915428E7,3509235.0221916703],[4.139597933238752E7,3509355.357876867],[4.139634303502636E7,3509448.0995320077],[4.1396351123766065E7,3509457.47612065],[4.139611919152073E7,3509690.8371538934],[4.139607108993201E7,3509738.4586458774],[4.139596250991324E7,3509845.7822199236],[4.139587982906472E7,3509926.6420456166],[4.13958779437871E7,3509928.571888249],[4.139586534467E7,3509941.472573125],[4.139586396520549E7,3509942.8849526425]]]}";
		String geos2 = "{\"type\":\"Polygon\",\"coordinates\":[[[4.1395822992E7,3509212.9770000004],[4.1395820219E7,3509212.755999999],[4.1395817447E7,3509212.9899999998],[4.139581475E7,3509213.673],[4.1395812201E7,3509214.787000001],[4.1395809867E7,3509216.3019999987],[4.1395807812E7,3509218.1779999994],[4.1395797396E7,3509229.8410000005],[4.1395787459E7,3509241.914999999],[4.1395778016E7,3509254.380000001],[4.1395769084E7,3509267.2149999994],[4.1395768348E7,3509268.369000001],[4.1395889775E7,3509300.251],[4.139588604E7,3509295.6040000007],[4.1395874955E7,3509280.616],[4.1395867837E7,3509270.2860000003],[4.1395834382E7,3509220.2990000006],[4.1395832651E7,3509218.121],[4.1395830587E7,3509216.255000001],[4.1395828247E7,3509214.751],[4.1395825693E7,3509213.6480000005],[4.1395822992E7,3509212.9770000004]],[[4.1396037038E7,3509569.079],[4.1396051902E7,3509568.686],[4.1396066765E7,3509569.0840000003],[4.1396081587E7,3509570.27],[4.1396096325E7,3509572.2410000004],[4.1396110938E7,3509574.9930000002],[4.1396125383E7,3509578.5160000008],[4.139612689E7,3509578.7909999993],[4.1396128422E7,3509578.807],[4.1396129934E7,3509578.5620000013],[4.1396131383E7,3509578.064999999],[4.1396132725999996E7,3509577.329],[4.1396133925E7,3509576.3759999997],[4.1396134945E7,3509575.2329999995],[4.1396135756E7,3509573.9340000004],[4.1396170878E7,3509505.308],[4.1396171632E7,3509503.497999999],[4.1396172092E7,3509501.592],[4.139617225E7,3509499.637],[4.1396172099E7,3509497.681],[4.1396171644E7,3509495.7740000007],[4.1396170896E7,3509493.960999999],[4.1396169874E7,3509492.2870000005],[4.1396168603E7,3509490.7939999998],[4.1396167114E7,3509489.5179999988],[4.1396165443E7,3509488.491000001],[4.1396042495E7,3509425.6099999985],[4.1396026071E7,3509416.7899999996],[4.1396010014E7,3509407.319],[4.139599435E7,3509397.211999999],[4.1395979103E7,3509386.485],[4.1395964299E7,3509375.1549999993],[4.139594996E7,3509363.241],[4.1395936111E7,3509350.761],[4.1395927346E7,3509342.2009999994],[4.1395750885E7,3509297.5390000003],[4.1395745487E7,3509307.732000001],[4.1395741981E7,3509314.585999999],[4.139568284E7,3509430.181],[4.1395765822E7,3509472.6369999996],[4.1395778095E7,3509479.2929999996],[4.1395790033E7,3509486.533],[4.1395801609E7,3509494.3389999997],[4.1395812795E7,3509502.6929999995],[4.1395823566E7,3509511.575999999],[4.1395833897E7,3509520.9680000003],[4.1395843764E7,3509530.8450000007],[4.1395853144E7,3509541.187000001],[4.1395862016E7,3509551.968000001],[4.139590683E7,3509609.182],[4.1395918663E7,3509602.403000001],[4.1395930813E7,3509596.208],[4.139594325E7,3509590.6119999993],[4.1395955946E7,3509585.629000001],[4.1395968868E7,3509581.269],[4.1395981988E7,3509577.545],[4.1395995273E7,3509574.463999999],[4.1396008693E7,3509572.0330000003],[4.1396022215E7,3509570.26],[4.1396037038E7,3509569.079]]]}";

		Polygon geo1 = (Polygon) gisManager.getGeoService().readGeoJSON(geos1);
		Polygon geo2 = (Polygon) gisManager.getGeoService().readGeoJSON(geos2);

		//
		SeCoordRef coordRef = new SeCoordRef();
		LineString linearRing1 = geo1.getExteriorRing();
		List<SePoint> points1 = new ArrayList<SePoint>();
		for (Coordinate coordinate : linearRing1.getCoordinates()) {
			points1.add(new SePoint(coordRef, coordinate.x, coordinate.y));
		}
		SePolygon polygon1 = new SePolygon(coordRef, points1.toArray(new SePoint[0]));
		print(polygon1.asText());
		//
		LineString exteriorRing = geo2.getExteriorRing();
		LineString interiorRing = geo2.getInteriorRingN(0);
		List<SePoint> exPnts = new ArrayList<SePoint>();
		List<SePoint> inPnts = new ArrayList<SePoint>();
		for (Coordinate coordinate : exteriorRing.getCoordinates()) {
			exPnts.add(new SePoint(coordRef, coordinate.x, coordinate.y));
		}
		for (Coordinate coordinate : interiorRing.getCoordinates()) {
			inPnts.add(new SePoint(coordRef, coordinate.x, coordinate.y));
		}
		SePoint[][] mp = {exPnts.toArray(new SePoint[0]), inPnts.toArray(new SePoint[0])};
		SePolygon polygon2 = new SePolygon(coordRef, mp);
		print(polygon2.asText());

		com.esri.sde.sdk.geom.Geometry[] polygons = polygon2.intersection(polygon1);

//        List list = gisManager.getGISService().query("GLSJ.BPDK_320000","PL_PL_ID=025010960000152293",null,true,"glsj");

		print("----");

	}

	@Test
	public void testBJ() throws Exception {
		try {
//            InputStream is = FileUtils.openInputStream(new File("G:\\Temp\\yasuo\\ys.zip"));
			InputStream is = getClass().getResourceAsStream("/bj/321000131200000000.ZIP");
			Map coords = gisManager.getGeoService().getBJCoordinates(is);
			print(coords);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @作者 王建明
	 * @创建日期 2018/5/9 0009
	 * @创建时间 上午 7:52
	 * @描述 —— 测试土地利用现状分析
	 */
	@Test
	public void testTdlyxzAnalysis2() throws Exception {
		Map result = gisManager.getGISService().tdlyxzAnalysis2("DLTB_2016_320400", "XZDW_2016_320400","" ,"{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40488247.268,3523495.1970000006],[40488275.931,3523478.044],[40488298.924,3523467.78],[40488406.601,3523403.341],[40488426.513,3523387.929],[40488471.798,3523360.8270000005],[40488473.442,3523359.566],[40488474.733,3523358.3410000005],[40488475.888,3523356.988],[40488476.896,3523355.5220000003],[40488477.745,3523353.9580000006],[40488478.426,3523352.314],[40488478.931,3523350.6080000005],[40488479.256,3523348.8580000005],[40488479.395,3523347.0840000003],[40488479.349,3523345.305],[40488479.116,3523343.541],[40488478.701,3523341.8110000007],[40488478.107,3523340.1329999994],[40488477.341,3523338.527],[40488451.096,3523290.192],[40488441.803,3523266.7899999996],[40488338.149,3523075.893],[40488335.948,3523071.4049999993],[40488334.13,3523066.749],[40488332.704,3523061.9580000006],[40488331.683,3523057.065],[40488331.073,3523052.104],[40488330.877,3523047.109],[40488331.098,3523042.115],[40488331.734,3523037.157],[40488332.543,3523033.414],[40488332.425,3523033.479],[40488334.229,3523027.524],[40488336.072,3523022.877],[40488338.296,3523018.4],[40488340.884,3523014.1240000003],[40488343.819,3523010.0780000007],[40488347.067,3523006.305],[40488347.554,3523005.827],[40488350.514,3523002.919],[40488354.358,3522999.725],[40488358.456,3522996.8619999997],[40488360.461,3522995.627],[40488361.299,3522995.133],[40488361.143,3522995.1970000006],[40488346.235,3523003.264],[40488202.864,3523080.849],[40488190.826,3523087.364],[40488176.125,3523095.318],[40488093.73,3523139.907],[40488072.786,3523151.241],[40488070.833,3523152.483],[40488247.268,3523495.1970000006]],[[40488274.376,3523395.916],[40488270.136,3523389.4959999993],[40488266.986,3523384.8460000004],[40488264.956,3523381.046],[40488260.556,3523365.356],[40488258.146,3523358.276],[40488244.096,3523335.705],[40488253.666,3523329.805],[40488253.608,3523329.653],[40488236.817,3523286.045],[40488235.437,3523281.395],[40488235.612,3523281.325],[40488247.567,3523276.535],[40488247.482,3523276.352],[40488236.437,3523252.605],[40488261.788,3523239.8849999993],[40488262.798,3523241.1479999996],[40488263.188,3523241.6349999993],[40488270.858,3523252.1949999994],[40488279.518,3523261.255],[40488288.177,3523277.385],[40488305.384,3523312.45],[40488308.706,3523319.262],[40488314.937,3523332.0359999994],[40488338.365,3523379.797],[40488343.657,3523390.5839999993],[40488343.737,3523390.7469999995],[40488335.163,3523394.812],[40488335.047,3523394.867],[40488335.057,3523395.857],[40488336.914,3523401.4769999995],[40488336.967,3523401.637],[40488330.177,3523405.4769999995],[40488330.497,3523409.897],[40488331.007,3523421.607],[40488332.082,3523424.63],[40488333.056,3523427.3669999996],[40488325.858,3523428.34],[40488303.236,3523431.397],[40488296.836,3523432.237],[40488274.376,3523395.916]],[[40488346.367,3523363.327],[40488346.17,3523362.916],[40488341.117,3523352.3769999994],[40488327.692,3523328.1019999995],[40488324.967,3523323.176],[40488320.427,3523313.545],[40488320.622,3523313.45],[40488344.898,3523301.476],[40488402.719,3523272.9659999995],[40488406.829,3523278.007],[40488428.398,3523316.737],[40488436.615,3523331.0329999994],[40488446.468,3523348.178],[40488441.29,3523351.0650000004],[40488431.188,3523356.698],[40488412.098,3523369.7570000007],[40488409.008,3523374.607],[40488400.08,3523379.7270000004],[40488384.557,3523388.627],[40488381.657,3523390.2870000005],[40488376.164,3523379.754],[40488374.867,3523377.267],[40488369.113,3523379.7619999996],[40488363.357,3523382.257],[40488361.981,3523379.7700000005],[40488354.697,3523366.607],[40488349.777,3523364.837],[40488346.367,3523363.327]],[[40488225.858,3523140.681],[40488211.618,3523120.143],[40488245.149,3523098.763],[40488263.389,3523132.8040000005],[40488233.392,3523144.793],[40488230.388,3523145.994],[40488227.628,3523143.234],[40488225.858,3523140.681]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"properties\":{\"PL_NAME\":\"\"}}]}",  "TDLY");
		System.out.println(result);
	}
}
