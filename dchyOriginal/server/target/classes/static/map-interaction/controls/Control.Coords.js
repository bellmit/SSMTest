/**
 * @class Control.Coords
 * A simple coords control that shows the Coords .Extends `L.Control`.
 * @example L.control.coords().addTo(map);
 */
L.Control.Coords = L.Control.extend({

    options: {
        // 位置，默认左下角
        position: 'bottomleft'
    },

    // 增加坐标
    onAdd: function (map) {
        // 创建容器
        var className = 'leaflet-control-coords',
            container = L.DomUtil.create('div', className),
            options = this.options;

        // 添加坐标控件
        this._createCoords(options, 'leaflet-coords', container);

        // 添加鼠标移动事件
        this._map.on('mousemove', function (e) {
            this._updateCoords(e.latlng);
        }, this);

        return container;
    },

    /**
     * 创建坐标span元素
     * @param options
     * @param className
     * @param container
     * @private
     */
    _createCoords: function (options, className, container) {
        // 创建span
        this._coords = L.DomUtil.create('span', className, container);

        // 获取地图中心点并更新坐标
        var latlng = this._map.getCenter();
        this._updateCoords(latlng);
    },

    /**
     * 更新坐标
     * @param latlng
     * @private
     */
    _updateCoords: function (latlng) {
        this._coords.innerHTML = "经度: " + this._convertDegreeToDms(latlng.lng) + " 纬度: " + this._convertDegreeToDms(latlng.lat);
    },

    /**
     * 经纬度转换为度分秒格式
     * @param degree
     * @returns {string}
     * @private
     */
    _convertDegreeToDms: function (degree) {
        degree = Math.abs(degree);
        var d = Math.floor(degree); //度
        var m = Math.floor((degree - d) * 60); //分
        var s = Math.round((degree - d) * 3600 % 60); //秒
        return d + '°' + m + '\'' + s + '"';
    }
});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.coordsControl) {
        this.coordsControl = L.control.coords();
        this.addControl(this.coordsControl);
    }
});

L.control.coords = function (options) {
    return new L.Control.Coords(options);
};