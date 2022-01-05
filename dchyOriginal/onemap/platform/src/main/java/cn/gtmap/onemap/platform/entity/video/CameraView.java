package cn.gtmap.onemap.platform.entity.video;

/**
 * 可视域实体
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 19:02
 */
public class CameraView {

    /**
     * 关联的设备编码
     */
    private String indexCode;

    /**
     * 方位角
     */
    private double azimuth;

    /**
     * 水平视场角
     */
    private double horizontalAngle;

    /**
     * 最大可视半径
     */
    private double viewRadius;

    public CameraView(String indexCode, double azimuth, double horizontalAngle, double viewRadius) {
        this.indexCode = indexCode;
        this.azimuth = azimuth;
        this.horizontalAngle = horizontalAngle;
        this.viewRadius = viewRadius;
    }

    public CameraView() {
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getHorizontalAngle() {
        return horizontalAngle;
    }

    public void setHorizontalAngle(double horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    public double getViewRadius() {
        return viewRadius;
    }

    public void setViewRadius(double viewRadius) {
        this.viewRadius = viewRadius;
    }

    @Override
    public String toString() {
        return "CameraView{" +
                "indexCode='" + indexCode + '\'' +
                ", azimuth=" + azimuth +
                ", horizontalAngle=" + horizontalAngle +
                ", viewRadius=" + viewRadius +
                '}';
    }
}
