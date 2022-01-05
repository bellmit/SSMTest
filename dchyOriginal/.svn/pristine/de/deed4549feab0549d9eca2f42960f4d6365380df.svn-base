package cn.gtmap.onemap.platform.entity.featureServices;


import java.util.List;
import java.util.Map;

/**
 * .Feature
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 2017/11/23 22:12
 */
public class Feature {

    private String id;
    private String type = "Feature";
    private Geometry geometry;
    private Map properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Feature setType(String type) {
        this.type = type;
        return this;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Feature setGeometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public static class Geometry {
        private String type;
        private List coordinates;

        public String getType() {
            return type;
        }

        public Geometry setType(String type) {
            this.type = type;
            return this;
        }

        public List getCoordinates() {
            return coordinates;
        }

        public Geometry setCoordinates(List coordinates) {
            this.coordinates = coordinates;
            return this;
        }
    }
}
