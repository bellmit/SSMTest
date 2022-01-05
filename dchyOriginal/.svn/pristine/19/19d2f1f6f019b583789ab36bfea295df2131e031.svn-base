package cn.gtmap.onemap.platform.event;

/**
 * . GeometryServiceException
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-17 下午3:03
 */
public class GeometryServiceException extends RuntimeException {

    public enum ExceptionType {

        WKT_PARSE_EXCEPTION("WKT 格式坐标解析异常"),
        GEOJSON_PARSE_EXCEPTION("GeoJSON 格式坐标解析异常"),
        CRS_PARSE_EXCEPTION("空间参考解析异常"),
        PROJECT_EXCEPTION("投影转换异常"),
        MAP_TO_FEATURE_EXCEPTION("转化为SimpleFeature异常"),
        GEOMETRY_TO_JSON_EXCEPTION("转化为GeoJSON异常"),
        FEATURE_TO_JSON_EXCEPTION("转化为FeatureJSON异常");

        private String label;

        ExceptionType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private ExceptionType type;
    private String msg;

    /**
     * @param type
     * @param message
     */
    public GeometryServiceException(ExceptionType type, String message) {
        this.type = type;
        this.msg = message;
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return "GeometryServiceException:[ type :[ " + this.type.getLabel() + " ] , messsage :[ " + this.msg + " ] ]";
    }
}
