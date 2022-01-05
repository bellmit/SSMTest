/**
 *
 * 简单的地图标注----用于展示一些tips  需要完善
 * 支持跟随地图的拖动，缩放
 * Created by Yang Jiawei on 2017/3/26.
 * version 1.0.0
 *
 * 使用方式 var marker = new GTMaker({
 *              map: currentMapObject,
 *              point: mapPoint,
 *              content: text
 *         });
 *         marker.show()        //显示
 *         marker.hide()        //隐藏
 *         marker.destroy()     //销毁当前对象(移除dom)
 */

define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/dom-construct",
    "dojo/on",
    "esri/geometry/screenUtils",
    "handlebars",
    "text!static/js/map/component/gtmap-marker/template.html",
    "css!static/js/map/component/gtmap-marker/style.css"], function (declare, lang, domConstruct, on, screenUtils, Handlebars, template) {

    var marker = declare(null, {

        /**
         * 关联的map
         */
        map: undefined,

        /**
         * 与之关联的geometry  todo
         */
        geometry: undefined,

        /**
         * 与之关联的point
         */
        point: undefined,

        /**
         * 位置
         */
        position: undefined,
        /**
         * 展示的内容
         */
        content: null,

        /**
         * marker的dom对象
         */
        d: undefined,


        constructor: function (settings) {
            lang.mixin(this, settings);
            var temp = Handlebars.compile(template);
            this._setPosition(this.point);
            this.d = domConstruct.create("div", {
                innerHTML: temp({"text": this.content}),
                style: {display: "none", position: "absolute", left: this.position.left, top: this.position.top}
            }, document.body);
            this._followMap();
        },

        show: function () {
            $(this.d).css({display: "block"});
        },

        hide: function () {
            $(this.d).css({"display": "none"});
        },

        destroy: function () {
            $(this.d).remove();
        },

        setPoint: function (point) {
            this.point = point;
        },

        refreshContent: function (content) {
            this.content = content;
            this._refresh();
        },

        _setVisiable: function (b) {
            if (b) {
                $(this.d).css({"display": "block"});
            } else {
                this.hide();
            }
        },

        /**
         * @private
         */
        _setPosition: function (mapPoint) {
            this.position = lang.mixin(this.position, _getPosition(mapPoint, this.map));
        },

        /**
         * marker跟随地图拖动，缩放进行位置调整
         * @private
         */
        _followMap: function () {

            //跟随地图拖动
            on(this.map, "pan-start", lang.hitch(this, _onPanStart));
            on(this.map, "pan", lang.hitch(this, _onPan));

            //跟随地图缩放
            on(this.map, "zoom-start", lang.hitch(this, _onZoomStart));
            on(this.map, "extent-change", lang.hitch(this, _onExtentChange));

        },

        /**
         * 刷新marker的位置
         * @private
         */
        _refresh: function () {
            $(this.d).css(this.position);
        }


    });

    /**
     * 获取mappoint在当前地图下的屏幕坐标
     * @param mapPoint
     * @param map
     * @private
     */
    function _getPosition(mapPoint, map) {
        var screentPoint = screenUtils.toScreenPoint(map.extent, map.width, map.height, mapPoint);
        var mapPosition = map.position;
        return {top: mapPosition.y + screentPoint.y + 5 + "px", left: mapPosition.x + screentPoint.x + "px"};
    }

    function _onPanStart() {
        this._panOrigin = {top: this.d.style.top, left: this.d.style.left};
    }

    function _onPan(evt) {
        var newPosition = {
            top: evt.delta.y + parseFloat(this._panOrigin.top) + "px",
            left: evt.delta.x + parseFloat(this._panOrigin.left) + "px"
        };
        this.position = newPosition || this.position;
        this._refresh();
        this._setVisiable(_checkPosition(this.position, this.map));
    }

    function _onZoomStart() {
        this._setVisiable(!1);
    }

    function _onExtentChange(evt) {
        this.position = _getPosition(this.point, this.map);
        this._refresh();
        this._setVisiable(_checkPosition(this.position, this.map));
    }

    /**
     * 检查位置是否超过地图边界 如果超出则隐藏
     * @private
     * return 未超出则为true 超出则返回false
     */
    function _checkPosition(position, map) {
        var mapTop = map.position.y;
        var mapLeft = map.position.x;
        var mapRight = map.position.x + map.width;
        var mapBottom = map.position.y + map.height;
        var top = parseFloat(position.top);
        var left = parseFloat(position.left);
        if (top < mapTop || top + $(this.d).height() > mapBottom) {
            return false;
        }
        //if (left < mapLeft || left > mapRight) {
        //    return false;
        //}
        //return true;
        return !(left < mapLeft || left > mapRight);
    }

    return marker;
});
