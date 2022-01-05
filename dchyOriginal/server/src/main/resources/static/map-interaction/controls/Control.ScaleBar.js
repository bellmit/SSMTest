/**
 * @class Control.ScaleBar
 * A simple scale control that shows the scale .Extends `L.Control`.
 * @example L.control.scaleBar().addTo(map);
 */
L.Control.ScaleBar = L.Control.extend({

    // 初始化map
    __map: {},

    // Control.ScaleBar options
    options: {

        position: 'bottomleft',

        maxWidth: 100
    },

    // add to map
    onAdd: function (map) {
        this.__map = map;
        var className = 'leaflet-control-scaleBar',
            container = L.DomUtil.create('div', className),
            options = this.options;

        // 创建比例尺控件
        this._scale = L.DomUtil.create('span', 'leaflet-scaleBar', container);

        // 绑定事件
        map.on('moveend', this._update, this);
        map.whenReady(this._update, this);

        return container;
    },

    //  remove control
    onRemove: function (map) {
        map.off("moveend", this._update, this);
    },

    _update: function () {
        // 地图中间选取两个坐标点，计算距离
        var map = this._map;
        var y = map.getSize().y / 2;
        var x1 = 0;
        var x2 = this.options.maxWidth;
        var latlng1 = map.containerPointToLatLng([x1, y]);
        var latlng2 = map.containerPointToLatLng([x2, y]);
        var distance = map.distance(latlng1, latlng2);
        this._updateScales(distance)
    },

    _updateScales: function (distance) {
        var i = this._getRoundNum(distance);
        this._scale.style.width = Math.round(this.options.maxWidth * (i / distance)) + "px";
        this._scale.innerHTML = "比例尺  1 : " + i;
    },

    _getRoundNum: function (t) {
        // 参考leaflet的L.Control.Scale
        var i = Math.pow(10, (Math.floor(t) + "").length - 1),
            e = t / i;
        e = (10 <= e ? 10 : (5 <= e ? 5 : (3 <= e ? 3 : (2 <= e ? 2 : 1))));
        return i * e;
    }
});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.scaleBarControl) {
        this.scaleBarControl = L.control.scaleBar();
        this.addControl(this.scaleBarControl);
    }
});

// 控件实例化方法
L.control.scaleBar = function (options) {
    return new L.Control.ScaleBar(options);
};