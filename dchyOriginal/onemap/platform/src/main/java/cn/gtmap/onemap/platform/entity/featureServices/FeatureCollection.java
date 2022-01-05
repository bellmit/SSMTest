package cn.gtmap.onemap.platform.entity.featureServices;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.geotools.referencing.CRS;

import java.util.List;

/**
 * @author Fjj
 * @date 2018/1/18
 */
public class FeatureCollection {

    private String type = "FeatureCollection";

    private List<Feature> features;

    private CRS crs;

    private List<Double> bbox;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public String toJson() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    public static FeatureCollection fromJson(String json) {
        return JSON.parseObject(json, FeatureCollection.class);
    }
}
