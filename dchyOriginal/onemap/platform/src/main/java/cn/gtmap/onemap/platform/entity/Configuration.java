package cn.gtmap.onemap.platform.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * . 配置
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-27 下午3:41
 */
public final class Configuration {

    /***
     * 默认行政区代码
     */
    private String defaultRegionCode;

    /***
     * 创建时间
     */
    private String createAt;

    /***
     * 描述信息
     */
    private String description;

    /***
     * 名称
     */
    private String name;

    /**
     * 坐标可见性
     */
    private boolean coordinateVisible;

    /***
     * 标题
     */
    private String title;

    /***
     * logo 图片
     */
    private String logo;

    /***
     * 地理服务地址 用于测量等功能
     */
    private GeometryService geometryService;

    /***
     * 主地图
     */
    private Map map;

    /***
     * 默认加载的widget
     */
    private List<Widget> dockWidgets;

    /***
     * 存放widget功能组 以及单个的widget功能
     * 并以菜单方式显示
     */
    private WidgetContainer widgetContainer;

    /***
     * 用于存放固定位置的widget
     * 如 region navigation等
     */
    private List<Widget> widgets;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public GeometryService getGeometryService() {
        return geometryService;
    }

    public void setGeometryService(GeometryService geometryService) {
        this.geometryService = geometryService;
    }


    public boolean isCoordinateVisible() {
        return coordinateVisible;
    }

    public void setCoordinateVisible(boolean coordinateVisible) {
        this.coordinateVisible = coordinateVisible;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }


    public List<Widget> getDockWidgets() {
        return dockWidgets;
    }

    public void setDockWidgets(List<Widget> dockWidgets) {
        this.dockWidgets = dockWidgets;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    public WidgetContainer getWidgetContainer() {
        return widgetContainer;
    }

    public void setWidgetContainer(WidgetContainer widgetContainer) {
        this.widgetContainer = widgetContainer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getDefaultRegionCode() {
        return defaultRegionCode;
    }

    public void setDefaultRegionCode(String defaultRegionCode) {
        this.defaultRegionCode = defaultRegionCode;
    }

    /***
     * map
     */
    public static final class Map {

        private java.util.Map initExtent;
        private List<Object> lods;
        private List<BaseMapLayer> baseLayers;
        private List<Service> operationalLayers;
        private double defaultScale;

        public Map() {
            baseLayers = new ArrayList<BaseMapLayer>();
            operationalLayers = new ArrayList<Service>();
        }

        public List<BaseMapLayer> getBaseLayers() {
            return baseLayers;
        }

        public void setBaseLayers(List<BaseMapLayer> baseLayers) {
            this.baseLayers = baseLayers;
        }

        public List<Service> getOperationalLayers() {
            return operationalLayers;
        }

        public void setOperationalLayers(List<Service> operationalLayers) {
            this.operationalLayers = operationalLayers;
        }

        public List<Object> getLods() {
            return lods;
        }

        public void setLods(List<Object> lods) {
            this.lods = lods;
        }

        public java.util.Map getInitExtent() {
            return initExtent;
        }

        public void setInitExtent(java.util.Map initExtent) {
            this.initExtent = initExtent;
        }
        public double getDefaultScale() {
            return defaultScale;
        }

        public void setDefaultScale(double defaultScale) {
            this.defaultScale = defaultScale;
        }

    }

    /***
     * geometry service
     */
    public static final class GeometryService {
        
        private String url;

        public GeometryService() {
        }

        public GeometryService(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * position
     *
     */
    public static final class Position {

        private float left;
        private float right;
        private float top;
        private float bottom;

        public Position() {
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }
    }

    /***
     * widget
     */
    public static final class Widget implements Comparable<Widget> {

        /***
         * id
         */
        private String id;
        
        /***
         * label
         */
        private String label;

        /***
         * icon
         */
        private String icon;

        /***
         * url
         */
        private String url;

        /**
         * widget是否可折叠
         * @since v3.1.0
         */
        private boolean collapsible;

        /***
         * 位置 对dock widget有效
         */
        private Position position;

        /***
         * 所属组
         */
        private String group;
        
        /***
         * 是否默认打开此功能
         */
        private boolean open;
        
        /***
         * 是否显示此widget 菜单中显示以及
         * dock widget的显隐
         */
        private boolean display;
        
        /***
         *widget的配置
         */
        private java.util.Map config;
        
        /***
         * 决定在 widget 组中的显示顺序
         */
        private int weight;


        public Widget() {
//            this.id = UUIDGenerator.generate();
        }

        public Widget(String id, String label, String url) {
            this.id = id;
            this.label = label;
            this.url = url;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public boolean isDisplay() {
            return display;
        }

        public void setDisplay(boolean display) {
            this.display = display;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isCollapsible() {
            return collapsible;
        }

        public void setCollapsible(boolean collapsible) {
            this.collapsible = collapsible;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public java.util.Map getConfig() {
            return config;
        }

        public void setConfig(java.util.Map config) {
            this.config = config;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        /**
         * 覆写比较方法
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         *         is less than, equal to, or greater than the specified object.
         * @throws ClassCastException if the specified object's type prevents it
         *                            from being compared to this object.
         */
        @Override
        public int compareTo(Widget o) {
            return this.getWeight() - o.getWeight();
        }
    }

    /***
     *
     */
    public static final class WidgetContainer {

        private List<Widget> widgets;
        
        private List<WidgetsGroup> widgetsGroup;

        public List<Widget> getWidgets() {
            return widgets;
        }

        public void setWidgets(List<Widget> widgets) {
            this.widgets = widgets;
        }

        public List<WidgetsGroup> getWidgetsGroup() {
            return widgetsGroup;
        }

        public void setWidgetsGroup(List<WidgetsGroup> widgetsGroup) {
            this.widgetsGroup = widgetsGroup;
        }

    }

    /***
     * widget 组
     */
    public static final class WidgetsGroup implements Comparable<WidgetsGroup> {
        private String id;
        private String label;
        private String icon;
        private int type;
        private String url;
        private List<Widget> widgets;
        private int weight;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<Widget> getWidgets() {
            return widgets;
        }

        public void setWidgets(List<Widget> widgets) {
            this.widgets = widgets;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        /**
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         *         is less than, equal to, or greater than the specified object.
         * @throws ClassCastException if the specified object's type prevents it
         *                            from being compared to this object.
         */
        @Override
        public int compareTo(WidgetsGroup o) {
            return this.getWeight() - o.getWeight();
        }
    }


}
