/**
 *
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/12/3 19:25
 * File:    Index
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    'dojo/topic',
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/dom-construct",
    "dojox/uuid/generateRandomUuid",
    "esri/dijit/Popup",
    'esri/graphic',
    "esri/geometry/Polyline",
    'esri/toolbars/draw',
    "esri/tasks/GeometryService",
    "esri/tasks/AreasAndLengthsParameters",
    "esri/symbols/SimpleFillSymbol",
    "esri/tasks/LengthsParameters",
    "esri/symbols/SimpleLineSymbol",
    "map/core/EsriSymbolsCreator",
    "esri/Color",
    "mustache",
    "map/core/BaseWidget",
    "map/component/MapPopup",
    "map/component/gtmap-marker/marker","map/core/JsonConverters","esri/tasks/FeatureSet","handlebars","map/core/GeometryIO"], function (declare, topic, lang, arrayUtil, domConstruct, generateRandomUuid, Popup, Graphic, Polyline, Draw, GeometryService, AreasAndLengthsParameters,
                                                    SimpleFillSymbol, LengthsParameters, SimpleLineSymbol, EsriSymbolsCreator, Color,
                                                    Mustache, BaseWidget, MapPopup, GTMarker, JsonConverters, FeatureSet, Handlebars, GeometryIO) {

    var url, map, geometryService, tb, _meaConfig;
    var mapPopup = MapPopup.getInstance();
    var openMapPopup = false;       //是否打开地图弹窗显示地图量算结果,默认不显示
    var videoLayer;
    var oldPoint;
    var _showSsls; //是否显示实时量算
    var lsLength = 0.0;  //计算总长
    var geometryTemp;

    var measure = declare([BaseWidget], {
        /**
         *
         */
        onCreate: function () {
            map = this.getMap().map();
            url = this.getAppConfig().geometryService.url;
            openMapPopup = this.getConfig().openMapPopup || false;
            videoLayer = map.getLayer('videoGraphicsLyr');
            _meaConfig = this.getConfig();
            _init();
        },
        onOpen: function () {

        },
        onPause: function () {
            clear();
        },
        onDestroy: function () {
            clear();
        }
    });

    var drawHandler, timeOutHandler, mapClickHandler, mouseMoveHandler, dbClickHandler;
    var $listContainer;
    var _cacheData = [];
    var points = [];        //存放绘制过程的点
    var popUps = [];        //所有的popup
    var newPopUps = [];
    var overallLength = 0.0;        //折现的总长度
    var overallLengthMove = 0.0;    //鼠标移动折线显示的总长
    var pointOutline = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([0, 145, 255]), 2);
    var pointMarker = EsriSymbolsCreator.createSimpleMarkerSymbol(EsriSymbolsCreator.markerStyle.STYLE_CIRCLE, 10, pointOutline, new Color("#fff"));
    var exportSelTpl;

    /**
     *
     * @private
     */
    function _init() {
        if (url == undefined) {
            layer.alert('未配置geometryService的地址!', {btn: [], shade: 0});
            return;
        }
        geometryService = new GeometryService(url);

        $listContainer = $(".area-length-content");
        exportSelTpl = $("#export-select-tpl").html();
        geometryIO = new GeometryIO();
        addListeners();
    }

    /**
     *
     */
    function addListeners() {
        $("#lengthBtn").on('click', function () {
            topic.publish('video-layer-hide', {layer: videoLayer});
            mapPopup.closePopup();
            tb = tb ? tb : new Draw(map);
            tb.activate(Draw.POLYLINE);
            drawHandler = tb.on("draw-end", lang.hitch(map, onDrawEnd, "length"));
            mapClickHandler = map.on('click', function (evt) {
                window.clearTimeout(timeOutHandler);
                //在线段上添加节点
                var currentPoint = evt.mapPoint;
                oldPoint = currentPoint;
                points.push(currentPoint);
                //在节点上展示当前距离
                if (points.length === 1) {
                    _showResultOnMap(currentPoint, "起点");
                    //添加鼠标移动事件
                    if (_meaConfig.hasOwnProperty("showSsls")) {
                        _showSsls = _meaConfig.showSsls;
                    }
                    if(_showSsls){
                        mouseMoveHandler = map.on('mouse-move', function (evt) {
                            var currPoint = evt.mapPoint;
                            var ploy = new Polyline(currPoint.spatialReference);
                            ploy.addPath([oldPoint, currPoint]);
                            geometryService.lengths(_organizeLengthParams([ploy]), lang.hitch(this, _showResultOnMapMove, currPoint));
                        });
                        lsLength = 0;
                        overallLengthMove = 0;
                    }
                } else {
                    for (var i in newPopUps) {
                        newPopUps[i].hide();
                        newPopUps[i].destroy();
                    }
                    var polyline = new Polyline(currentPoint.spatialReference);
                    var lastPoint = points[points.length - 2];
                    polyline.addPath([lastPoint, currentPoint]);
                    addGraphicToMap(polyline);
                    geometryService.lengths(_organizeLengthParams([polyline]), lang.hitch(this, _showResultOnMap, currentPoint));
                }
                lastPoint && (map.graphics.add(new Graphic(lastPoint, pointMarker)));
                map.graphics.add(new Graphic(currentPoint, pointMarker));
            });
            timeOutHandler = window.setTimeout(operateTimeOut, 4000);
        });
        $("#areaBtn").on('click', function () {
            topic.publish('video-layer-hide', {layer: videoLayer});
            mapPopup.closePopup();
            tb = tb ? tb : new Draw(map);
            drawHandler = tb.on("draw-end", lang.hitch(map, onDrawEnd, "area"));
            tb.activate(Draw.POLYGON);
            var mapClickHandler = map.on('click', function () {
                window.clearTimeout(timeOutHandler);
                mapClickHandler.remove();
            });
            timeOutHandler = window.setTimeout(operateTimeOut, 4000);
        });
    }

    /**
     *
     * @param evt
     */
    function onDrawEnd(evtType, evt) {
        switch (evtType) {
            case 'length':
            {
                var polyline = evt.geometry;
                var lengthParams = new LengthsParameters();
                lengthParams.lengthUnit = GeometryService.UNIT_METER;
                lengthParams.calculationType = 'preserveShape';
                lengthParams.polylines = [polyline];
                geometryService.lengths(lengthParams, lang.hitch(this, measureHandler, polyline));
                break;
            }
            case 'area':
            {
                var geometry = evt.geometry || '';
                geometryTemp = geometry;
                var areasAndLengthParams = new AreasAndLengthsParameters();
                areasAndLengthParams.areaUnit = GeometryService.UNIT_SQUARE_METERS;
                areasAndLengthParams.lengthUnit = GeometryService.UNIT_METER;
                areasAndLengthParams.calculationType = "preserveShape";
                geometryService.simplify([geometry], function (simplifiedGeometries) {
                    areasAndLengthParams.polygons = simplifiedGeometries;
                    geometryService.areasAndLengths(areasAndLengthParams, lang.hitch(this, measureHandler, geometry));
                });
                break;
            }
        }
        //移除鼠标点击事件
        tb.deactivate();
        if (drawHandler)drawHandler.remove();
        if (mapClickHandler)mapClickHandler.remove();
        if(mouseMoveHandler)mouseMoveHandler.remove();
        topic.publish('video-layer-show', {layer: videoLayer});
        points = [];        //清空points
        overallLength = 0;      //重置总长度
        if(_showSsls){
            overallLengthMove = 0;
            if(newPopUps.length > 0){
                for (var i in newPopUps) {
                    newPopUps[i].hide();
                    newPopUps[i].destroy();
                }
            }
            $(map).off("mouse-move");
        }
    }

    /**
     *
     * @param evt
     */
    function measureHandler(geo, evt) {
        if (evt.hasOwnProperty("lengths") || evt.hasOwnProperty("areas")) {
            var tmpl = $("#resultTpl").html();
            var length, area = null;
            if (evt.lengths != undefined) {
                length = parseFloat(evt.lengths[0]).toFixed(4);
                if(_showSsls){
                    length = parseFloat(lsLength).toFixed(4);
                }
            }
            if (evt.areas != undefined) {
                area = parseFloat(evt.areas[0]).toFixed(4);
            }

            var obj = {
                length: length,
                area: area,
                loc: geo.getExtent().getCenter(),
                uid: generateRandomUuid(),
                geometry: JSON.stringify(geometryTemp)
            };

            $listContainer.append(Mustache.render(tmpl, obj));
            _cacheData.push(obj);

            if(openMapPopup || area)addGraphicToMap(geo, length, area);

            $(".btn-measure-clear").css("display", "inline-block");
            $(".btn-measure-clear").on('click', function () {
                clear();
                $(this).css("display", "none");
            });
            $(".measure-result").on('click', function () {
                var uid = $(this).data("uid");
                var arr = arrayUtil.filter(_cacheData, function (item) {
                    return item.uid === uid;
                });
                addPopup(makeInfo(arr[0].length, arr[0].area), arr[0].loc);
            });
            //导出绘制图形
            $(".item-export").on('click', function () {
                var geometry = $(this).parent().parent().attr("data-value");
                var geometryObj = [];
                geometryObj["geometry"] = JSON.parse(geometry);
                geometryObj["attributes"] = {PRONAME: generateRandomUuid()};
                _export([geometryObj]);
            })
        }
    }

    /**
     *
     * @param geometry
     * @param length
     * @param area
     */
    function addGraphicToMap(geometry, length, area) {
        var symbol;
        switch (geometry.type) {
            case "polyline":
                symbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color("#0091ff"), 2);
                break;
            case "polygon":
                symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color("#0091ff"), 2), new Color([255, 255, 0, 0.25]));
                break;
        }
        var info = makeInfo(length, area);
        var graphic = new Graphic(geometry, symbol);
        map.graphics.add(graphic);
        if ((openMapPopup && length) || area) addPopup(info, geometry.getExtent().getCenter());
    }

    /***
     * make info
     * @param length
     * @param area
     * @returns {string}
     */
    function makeInfo(length, area) {
        var info = "长度:" + length + "米";
        if (area) {
            info = info.concat("</br>面积:").concat(area).concat("平方米");
        }
        return info;
    }

    /***
     * 添加地图弹出窗口显示
     * @param info
     */
    function addPopup(info, locPnt) {
        mapPopup.setTitle("量算结果");
        mapPopup.setContent(info);
        mapPopup.openPopup(locPnt);
    }

    /**
     * 在地图上展示测距结果
     * @param point
     * @param content      内容
     * @private
     */
    function _showResultOnMap(point, content) {
        var text;
        if (typeof(content) === "string") {
            text = content;
        } else {
            if (content.lengths != undefined) {
                var length = content.lengths[0];
                lsLength += length;
                overallLength += length;
                text = parseFloat(overallLength).toFixed(2) + "米";
            }
        }

        var marker = new GTMarker({
            map: map,
            point: point,
            content: text
        });
        marker.show();
        popUps.push(marker);
    }

    /**
     * 组织长度计算请求参数
     * @param polylines  polyline数组
     * @private
     */
    function _organizeLengthParams(polylines) {
        var lengthParams = new LengthsParameters();
        lengthParams.lengthUnit = GeometryService.UNIT_METER;
        lengthParams.calculationType = 'preserveShape';
        lengthParams.polylines = polylines;
        return lengthParams;
    }

    /**
     * 超时后则清除当前状态
     */
    function operateTimeOut() {
        tb.deactivate();
    }

    /**
     * 清除数据
     */
    function clear() {
        _cacheData = [];
        map.graphics.clear();
        if (drawHandler)drawHandler.remove();
        $listContainer.empty();
        mapPopup.closePopup();
        //清除popup
        for (var i in popUps) {
            popUps[i].hide();
            popUps[i].destroy();
        }
        for (var i in newPopUps) {
            newPopUps[i].hide();
            newPopUps[i].destroy();
        }
        lsLength = 0;
    }

    /**
     * 在地图上展示测距结果(鼠标移动展示)
     * @param point
     * @param content      内容
     * @private
     */
    function _showResultOnMapMove(point, content) {
        var text;
        if (typeof(content) === "string") {
            text = content;
        } else {
            if (content.lengths != undefined) {
                var length = content.lengths[0];
                overallLengthMove = overallLength + length;
                text = parseFloat(overallLengthMove).toFixed(2) + "米";
            }
        }
        var marker = new GTMarker({
            map: map,
            point: point,
            content: text
        });
        marker.show();
        newPopUps.push(marker);
        if(newPopUps.length != 0 && newPopUps[newPopUps.length-2]){
            newPopUps[newPopUps.length-2].hide();
            newPopUps[newPopUps.length-2].destroy();
        }
    }

    function _export(gras) {
        var featureSet = new FeatureSet();
        featureSet.features = gras;

        var template = Handlebars.compile(exportSelTpl);
        var content = template({types: parseTypesArray});

        layer.open({
            title: '选择导出格式',
            content: content,
            area: '300px',
            yes: function (index, layero) {
                var type = $(layero).find('select').val();
                layer.close(index);
                geometryIO.expToFile(featureSet, type);
            }
        });
    }

    function parseTypesArray() {
        var expType;
        if(_meaConfig.hasOwnProperty("exportType")){
            expType = _meaConfig.exportType;
        }else{
            expType = "xls";
        }
        var types = expType.split(",");
        var r = [];
        arrayUtil.forEach(types, function (item) {
            if (item != 'bj') {
                r.push({alias: getAlias(item), value: item});
            }
        });
        return r;
    }

    function getAlias(name) {
        switch (name) {
            case 'xls':
                return 'excel文件(*.xls)';
            case 'txt':
                return 'txt文件(*.txt)';
            case 'dwg':
                return 'cad文件(*.dwg)';
            case 'bj':
                return '电子报件包(*.zip)';
            case 'shp':
                return 'shapfile压缩包(*.zip)';
            default:
                return name;
        }
    }

    return measure;
});