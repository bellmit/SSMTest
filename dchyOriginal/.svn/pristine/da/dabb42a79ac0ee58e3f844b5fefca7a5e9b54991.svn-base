package cn.gtmap.onemap.platform.entity;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-12 下午2:33
 */
public final class LayerRegion {

    private String layerName;
    private String regionField;
    private CoordinateReferenceSystem sourceLayerCRS;

    public LayerRegion(String layerName, String regionField) {
        this.layerName = layerName;
        this.regionField = regionField;
    }

    public LayerRegion(String layerName, CoordinateReferenceSystem sourceLayerCRS) {
        this.layerName = layerName;
        this.sourceLayerCRS = sourceLayerCRS;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getRegionField() {
        return regionField;
    }

    public void setRegionField(String regionField) {
        this.regionField = regionField;
    }

    public CoordinateReferenceSystem getSourceLayerCRS() {
        return sourceLayerCRS;
    }

    public void setSourceLayerCRS(CoordinateReferenceSystem sourceLayerCRS) {
        this.sourceLayerCRS = sourceLayerCRS;
    }
}
