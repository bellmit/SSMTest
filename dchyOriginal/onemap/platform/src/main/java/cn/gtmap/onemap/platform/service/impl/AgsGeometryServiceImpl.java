package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.service.AgsGeometryService;
import com.esri.core.geometry.*;
import com.gtis.config.AppConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 基于ESRI Geometry Api的Geometry Service
 * Created by shenjian on 2014-05-20.
 */
@Service
public class AgsGeometryServiceImpl extends BaseLogger implements AgsGeometryService {

    private OperatorFactoryLocal operatorFactory = OperatorFactoryLocal.getInstance();


    /**
     * intersect
     *
     * @param inputGeometry
     * @param intersector
     * @param sr
     * @return
     */
    @Override
    public String intersection(String inputGeometry, String intersector, String sr) {
        try {
            Geometry tmpGeo1 = GeometryEngine.geometryFromWkt(inputGeometry, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
            Geometry tmpGeo2 = GeometryEngine.geometryFromWkt(intersector, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
            if (AppConfig.getBooleanProperty("simplify.enable", false)) {
                SpatialReference spatialReference = SpatialReference.create(sr);
                OperatorSimplify operatorSimplify = (OperatorSimplify) operatorFactory.getOperator(Operator.Type.Simplify);
                if (!operatorSimplify.isSimpleAsFeature(tmpGeo1, spatialReference, null))
                    tmpGeo1 = GeometryEngine.simplify(tmpGeo1, spatialReference);
                if (!operatorSimplify.isSimpleAsFeature(tmpGeo2, spatialReference, null))
                    tmpGeo2 = GeometryEngine.simplify(tmpGeo2, spatialReference);
            }
            Geometry resultGeoemtry = GeometryEngine.intersect(tmpGeo1, tmpGeo2, null);
            return resultGeoemtry != null ? GeometryEngine.geometryToWkt(resultGeoemtry, WktExportFlags.wktExportDefaults) : "";
        } catch (Exception e) {
            throw new RuntimeException("ags service intersection error: " + e.getLocalizedMessage());
        }
    }

    /**
     * Creates the difference of two geometries. The dimension of geometry2 has to be equal to or greater than that of geometry1.
     *
     * @param geometry1
     * @param substractor
     * @param sr
     * @return
     */
    @Override
    public String difference(String geometry1, String substractor, String sr) {
        Geometry tmpGeo1 = GeometryEngine.geometryFromWkt(geometry1, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
        Geometry tmpGeo2 = GeometryEngine.geometryFromWkt(substractor, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
        SpatialReference spatialReference = SpatialReference.create(sr);

        OperatorSimplify operatorSimplify = (OperatorSimplify) operatorFactory.getOperator(Operator.Type.Simplify);
        if (!operatorSimplify.isSimpleAsFeature(tmpGeo1, spatialReference, null)) ;
        tmpGeo1 = GeometryEngine.simplify(tmpGeo1, spatialReference);
        if (!operatorSimplify.isSimpleAsFeature(tmpGeo2, spatialReference, null))
            tmpGeo2 = GeometryEngine.simplify(tmpGeo2, spatialReference);
        Geometry resultGeometry = GeometryEngine.difference(tmpGeo1, tmpGeo2, null);
        return resultGeometry != null ? GeometryEngine.geometryToWkt(resultGeometry, WktExportFlags.wktExportDefaults) : null;
    }

    /**
     * union geometry
     *
     * @param geometries geometry集合
     * @param sr
     * @return
     */
    @Override
    public String union(String[] geometries, String sr) {
        Geometry[] geos = new Geometry[geometries.length];
        for (int i = 0; i < geometries.length; i++) {
            String wkt = geometries[i];
            Geometry geometry = GeometryEngine.geometryFromWkt(wkt, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
            geos[i] = geometry;
        }
        Geometry resultGeometry = GeometryEngine.union(geos, null);
        return resultGeometry != null ? GeometryEngine.geometryToWkt(resultGeometry, WktExportFlags.wktExportDefaults) : null;
    }

    @Override
    public Double getGeometryArea(String wkt) {
        Geometry geometry = GeometryEngine.geometryFromWkt(wkt, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
        if (geometry != null) {
            if (geometry.getType().compareTo(Geometry.Type.Polygon) == 0) {
                return geometry.calculateArea2D();
            } else {
                return geometry.calculateLength2D();
            }
        }
        return 0.0;
    }

    /**
     * 验证图形是否有效
     *
     * @param wktGeometry
     * @param sr          wkt的crs
     * @return
     */
    @Override
    public boolean validGeometry(String wktGeometry, Object sr) {
        try {
            Geometry geo = GeometryEngine.geometryFromWkt(wktGeometry, WktImportFlags.wktImportDefaults, Geometry.Type.Unknown);
            OperatorFactoryLocal factory = OperatorFactoryLocal.getInstance();
            OperatorSimplify operatorSimplify = (OperatorSimplify) factory.getOperator(Operator.Type.Simplify);
            SpatialReference targetSr = sr instanceof String ? SpatialReference.create(String.valueOf(sr)) :
                    SpatialReference.create(Integer.valueOf(String.valueOf(sr)));
            return operatorSimplify.isSimpleAsFeature(geo, targetSr, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * @param shpFile
     * @return
     */
    @Override
    public String importShape(File shpFile) {

        ByteBuffer shapeBuffer = null;
        OperatorFactoryLocal factory = OperatorFactoryLocal.getInstance();
        OperatorExportToGeoJson operatorExportToGeoJson = (OperatorExportToGeoJson) factory.getOperator(Operator.Type.ExportToGeoJson);
        try (FileInputStream fileInputStream = new FileInputStream(shpFile);){

            byte[] bytes = IOUtils.toByteArray(shpFile.toURI());
            OperatorImportFromESRIShape operatorImportFromESRIShape = (OperatorImportFromESRIShape) factory.getOperator(Operator.Type.ImportFromESRIShape);
            Geometry geometry = operatorImportFromESRIShape.execute(ShapeImportFlags.ShapeImportDefaults, Geometry.Type.Unknown, ByteBuffer.wrap(bytes).order(
                    ByteOrder.BIG_ENDIAN));
            return geometry == null ? null : operatorExportToGeoJson.execute(geometry);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
