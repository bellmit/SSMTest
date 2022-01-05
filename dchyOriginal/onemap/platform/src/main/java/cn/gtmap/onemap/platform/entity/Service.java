package cn.gtmap.onemap.platform.entity;

import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.ServiceType;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;

import java.util.Collections;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-22 下午3:27
 */
public final class Service implements Comparable<Service> {

    /**
     * 服务ID
     */
    private String id;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务别名
     */
    private String alias;

    /**
     * 服务地址
     */
    private String url;

    /**
     * 服务类型
     */
    private String type;

    /**
     * 是否可见
     */
    private boolean visible;

    /**
     * 透明度
     */
    private Float alpha;

    /**
     * 图层序号
     */
    private int index;

    /**
     * 行政区代码
     */
    private String xzdm;

    /**
     * 行政区名称
     */
    private String xzmc;

    /**
     * 最小参考比例尺
     */
    private Double minRefScale;

    /**
     * 最大参考比例尺
     */
    private Double maxRefScale;

    /**
     * 服务空间范围最小X
     */
    private Double xMinExtent;

    /**
     * 服务空间范围最大X
     */
    private Double xMaxExtent;

    /**
     * 服务空间范围最小Y
     */
    private Double yMinExtent;

    /**
     * 服务空间范围最大Y
     */
    private Double yMaxExtent;

    /**
     * 服务年份
     */
    private int year;

    /**
     * 服务分类
     */
    private String category;


    /**
     * 组
     */
    private String group;

    /**
     * 备注
     */
    private String note;

    private List<Function> functions;

    public Service() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Float getAlpha() {
        return alpha;
    }

    public void setAlpha(Float alpha) {
        this.alpha = alpha;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getXzdm() {
        return xzdm;
    }

    public void setXzdm(String xzdm) {
        this.xzdm = xzdm;
    }

    public String getXzmc() {
        return xzmc;
    }

    public void setXzmc(String xzmc) {
        this.xzmc = xzmc;
    }

    public Double getMinRefScale() {
        return minRefScale;
    }

    public void setMinRefScale(Double minRefScale) {
        this.minRefScale = minRefScale;
    }

    public Double getMaxRefScale() {
        return maxRefScale;
    }

    public void setMaxRefScale(Double maxRefScale) {
        this.maxRefScale = maxRefScale;
    }

    public Double getXMinExtent() {
        return xMinExtent;
    }

    public void setXMinExtent(Double xMinExtent) {
        this.xMinExtent = xMinExtent;
    }

    public Double getXMaxExtent() {
        return xMaxExtent;
    }

    public void setXMaxExtent(Double xMaxExtent) {
        this.xMaxExtent = xMaxExtent;
    }

    public Double getYMinExtent() {
        return yMinExtent;
    }

    public void setYMinExtent(Double yMinExtent) {
        this.yMinExtent = yMinExtent;
    }

    public Double getYMaxExtent() {
        return yMaxExtent;
    }

    public void setYMaxExtent(Double yMaxExtent) {
        this.yMaxExtent = yMaxExtent;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Service clearFunction() {
        setFunctions(null);
        return this;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *                            from being compared to this object.
     */
    @Override
    public int compareTo(Service o) {
        return this.getIndex() - o.getIndex();
    }

    /**
     * @param map
     * @return
     */
    public static Service fromMap(Map map, java.util.Map<ServiceType, cn.gtmap.onemap.model.Service> type, String groupId) {
        Service service = new Service();
        if (map == null) return service;
        service.setId(map.getId());
        service.setAlias(map.getAlias());
        service.setCategory(map.getGroup() == null ? "" : map.getGroup().getName());
        service.setAlpha(1f);
        service.setName(map.getName());
        service.setVisible(false);
        service.setYear(Integer.valueOf(map.getYear() != null ? map.getYear() : "0"));
        service.setXzdm(map.getRegionCode());
        service.setGroup(groupId);
        service.setMinRefScale(map.getMinScale());
        service.setMaxRefScale(map.getMaxScale());
        service.setXMinExtent(map.getExtent().getXmin());
        service.setYMinExtent(map.getExtent().getYmin());
        service.setXMaxExtent(map.getExtent().getXmax());
        service.setYMaxExtent(map.getExtent().getYmax());

        if (type != null && !Collections.emptyMap().equals(type)) {
            if (type.containsKey(ServiceType.ARCGIS_TILE)) {
                service.setType(ServiceType.ARCGIS_TILE.getValue());
                service.setUrl(type.get(ServiceType.ARCGIS_TILE).getUrl());
            } else if (type.containsKey(ServiceType.ARCGIS_REST)) {
                service.setType(ServiceType.ARCGIS_REST.getValue());
                service.setUrl(type.get(ServiceType.ARCGIS_REST).getUrl());
            } else {
                java.util.Map.Entry<ServiceType, cn.gtmap.onemap.model.Service> defaultType = type.entrySet().iterator().next();
                service.setType(defaultType.getKey().getValue());
                service.setUrl(defaultType.getValue().getUrl());
            }
        }
        return service;
    }
}
