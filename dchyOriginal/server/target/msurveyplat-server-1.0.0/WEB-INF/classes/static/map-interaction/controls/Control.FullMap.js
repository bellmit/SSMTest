/**
 * @class Control.FullMap
 * A simple fullMap control that shows the FullMap .Extends `L.Control`.
 * @example L.control.fullMap().addTo(map);
 */
L.Control.FullMap = L.Control.extend({

    // 初始化map
    __map: {},

    // 初始化地图范围
    _bounds: [],

    // @section
    // @aka Control.fullMap options
    options: {

        // 位置，默认右下角
        position: 'bottomright',

        // 标题
        title: '全图',

        // 图标
        icon: 'src/img/FullMap.png'
    },

    updateBounds: function (bounds) {
        if (bounds && bounds.isValid()) {
            // 更新边界
            this._bounds = [
                [bounds.getSouthWest().lat, bounds.getSouthWest().lng],
                [bounds.getNorthEast().lat, bounds.getNorthEast().lng]
            ];
            // 更新中心点
            this._initCenter.center = bounds.getCenter();
        }
    },

    // add event
    onAdd: function (map) {
        this.__map = map;

        // 初始化时获取地图中心点和地图缩放级别
        this._initCenter = {
            center: map.getCenter(),
            zoom: map.getZoom()
        };

        // 初始化的时候将地图范围存储在属性_bounds中，点击全图按钮时，为地图设置显示该范围
        let bounds = map.getBounds();
        this._bounds = [
            [bounds.getSouthWest().lat, bounds.getSouthWest().lng],
            [bounds.getNorthEast().lat, bounds.getNorthEast().lng]
        ];

        // 创建div容器
        let fullMapName = 'leaflet-control-full-map',
            options = this.options,
            container = L.DomUtil.create('div', fullMapName);

        // 创建全图按钮
        this._createButton(options.title, 'leaflet-full-map', options.icon, container, this._onClickFullMap);

        return container;
    },

    // remove event
    onRemove: function (map) {
        this.zoomToFullMap();
    },

    // create button
    _createButton: function (title, className, icon, container, fn) {
        var link = L.DomUtil.create('img', className, container);
        link.href = '#';
        link.title = title;
        link.src = icon;

        link.setAttribute('role', 'button');
        link.setAttribute('aria-label', title);

        L.DomEvent
            .on(link, 'click', L.DomEvent.stopPropagation)
            .on(link, 'click', L.DomEvent.preventDefault)
            .on(link, 'click', fn, this)
            .on(link, 'dblclick', L.DomEvent.stopPropagation);
    },

    // 全图显示
    _onClickFullMap: function () {
        this.zoomToFullMap();
    },

/*
    /!**
     * 重写Leaflet中获取边界中心点和缩放级别的方法
     * @param bounds 边界对象
     * @param options 可选项，设置padding
     * @returns {{center: L.LatLng, zoom: number}}
     * @private
     *!/
    _getBoundsCenterZoom: function (bounds, options) {
        let map = this.__map,
            initCenter = this._initCenter,  // 地图初始中心参数
            center = initCenter.center,     // 地图初始中心点
            zoom = initCenter.zoom;         // 地图初始缩放级别
        // 获取边界范围中心点和缩放级别
        if (bounds && bounds.isValid()) {   // 边界是否有效
            let target = map._getBoundsCenterZoom(bounds, options); // 获取边界中心点和缩放级别
            let targetZoom = target.zoom,
                targetCenter = target.center;
            zoom = targetZoom === Infinity ? zoom : targetZoom; // 两个角点如果坐标相同，获取到的边界范围的size就是[0,0]，zoom为无穷
            center = targetCenter || bounds.getCenter() || center;  // 计算的中心点、边界中心点、地图初始中心点三选一
        }
        return {
            center: center,
            zoom: zoom
        };
    },
*/

    zoomToFullMap: function (options) {
        /*记录一下，在缩放定位的时候遇到的两个问题：
        1.如果定位的范围边界的两个角点一样，获取的边界的大小bounds.getSize()就是[0,0]，计算的zoom就是Infinity正无穷，导致地图缩放到最大
        2.如果html绑定的iframe设置了display：none，地图初始化时leaflet在计算地图容器大小map.getSize()会取到[0,0]，
        即（clientHeight=0，clientWidth=0，），从而zoom会计算为-Infinity正无穷，并返回0导致地图缩放到最小。
        即使再修改display属性为block也不会刷新。这里注意，需要加载地图的时候再设置iframe的url*/
        let map = this.__map,
            bounds = this._bounds;
        bounds = L.latLngBounds(bounds);  // 转换为经纬度边界
        /*
        let target = this._getBoundsCenterZoom(bounds, options);    // 获取边界中心点和缩放级别
        return map.setView(target.center, target.zoom, options);    // 缩放定位
        */
        return MapUtils.fitBounds(map, bounds);
    }
});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.fullMapControl) {
        this.fullMapControl = L.control.fullMap();
        this.addControl(this.fullMapControl);
    }
});

L.control.fullMap = function (options) {
    return new L.Control.FullMap(options);
};