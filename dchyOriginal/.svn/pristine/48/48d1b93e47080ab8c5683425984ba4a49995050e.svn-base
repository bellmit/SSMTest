/**
 * 导入导出
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/14 13:40
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "dojo/_base/Deferred",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/tasks/IdentifyTask",
    "esri/tasks/FeatureSet",
    "esri/tasks/IdentifyParameters",
    "esri/geometry/Geometry",
    "esri/geometry/Polygon",
    "esri/geometry/Point",
    "esri/toolbars/draw",
    "esri/Color",
    "esri/symbols/SimpleMarkerSymbol",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/GeometryIO",
    "map/core/EsriSymbolsCreator",
    "map/component/MapInfoWindow",
    "map/utils/MapUtils",
    "layer",
    "handlebars",
    "map/core/GeoDataStore",
    "static/thirdparty/h-ui/js/H-ui"
], function (declare, lang, arrayUtil, on, Deferred, RandomUuid, GraphicsLayer, Graphic, IdentifyTask, FeatureSet, IdentifyParameters,
             Geometry, Polygon, Point, Draw, Color, SimpleMarkerSymbol, BaseWidget, JsonConverters,
             GeometryIO, EsriSymbolsCreator, MapInfoWindow, MapUtils, layer, Handlebars, GeoDataStore) {

    var STYLE_MARKER_CONSTANT = {
        STYLE_CIRCLE: "circle",
        STYLE_SQUARE: "square",
        STYLE_CROSS: "cross",
        STYLE_X: "x",
        STYLE_DIAMOND: "diamond",
        STYLE_TARGET: "target"
    };

    var me = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            _ioConfig = this.getConfig();
            _init();
            geoDataStore.fetch(GeoDataStore.SK_QUERY).then(lang.hitch(this, getShareData));
        },
        /**
         *
         */
        onPause: function () {
            _pause();
        },
        /**
         *
         */
        onOpen: function () {
            _map = this.getMap().map();
            _selectLayers = [];
            if (_ioConfig != undefined) {
                //解析配置
                var lyrs = _ioConfig.layers;
                if (lyrs != undefined && lyrs.length > 0) {
                    arrayUtil.forEach(lyrs, function (item) {
                        var _lyr = _map.getLayer(item.serviceId);
                        if (_lyr != undefined)
                            _selectLayers.push(lang.mixin(item, {url: _lyr.url}));
                    });
                }
            }
            MapUtils.setMap(_map);
            _addListener();
        }
    });

    var pointSymbol = new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 12, null, new Color([0, 0, 255, 1]));

    var lineSymbol = EsriSymbolsCreator.defaultLineSymbol;

    var fillSymbol = EsriSymbolsCreator.defaultFillSymbol;


    var _map, geometryIO, graphicsLyr, _ioConfig;

    //选择/导入的数据集合
    var dataList = [];
    //存放用户选择的导出地块
    var selectedFeatures = [];

    var _resultObj = null;

    //存放配置的可选择别图层
    var _selectLayers = [];
    //查询结果
    var _selectResults = [];
    var _selectCount = 0;
    //所有要素
    var wid_features;

    var $optContainer, $listContainer, $footer, $fileInput;

    var itemsTpl, exportSelTpl;

    var geoDataStore = GeoDataStore.getInstance();

    /***
     * init
     * @private
     */
    function _init() {
        layer.config();
        if (_ioConfig != undefined) {
            //解析配置
            var lyrs = _ioConfig.layers;
            if (lyrs != undefined && lyrs.length > 0) {
                arrayUtil.forEach(lyrs, function (item) {
                    var _lyr = _map.getLayer(item.serviceId);
                    if (_lyr != undefined)
                        _selectLayers.push(lang.mixin(item, {url: _lyr.url}));
                });
            }
        }
        geometryIO = new GeometryIO();

        graphicsLyr = new GraphicsLayer({id: 'import'.concat(Math.random().toString())});
        _map.addLayer(graphicsLyr);

        //初始化dom变量
        $optContainer = $(".io-opt-container");
        $footer = $(".io-footer");
        $listContainer = $("#ioList");
        $fileInput = $('#io-file-input');

        //初始化其他
        MapUtils.setMap(_map);

        itemsTpl = $("#items").html();
        exportSelTpl = $("#export-select-tpl").html();

        geoDataStore.on('onChange', lang.hitch(this, getShareData));
    }

    /**
     * DPF添加
     * 获取查询数据
     * @param data
     */
    var flag;

    function getShareData(data) {
        wid_features = data.features;
        if (typeof(wid_features) != "undefined") {
            // flag = -1;
            // renderFeatures(wid_features);
            flag = "";
        }

    }

    var _lastClick;

    /***
     * 添加事件监听
     * @private
     */
    function _addListener() {
        listenerFileInput();
        //监听头部按钮事件
        $optContainer.off('click', 'a');
        $optContainer.on('click', 'a', function () {
            var o = $(this).data("opt");
            switch (o) {
                case 'imp':
                    _clear();
                    break;
                case 'select':
                    _clear();
                    on.once(_map, 'click', doSelect);
                    _map.setMapCursor('crosshair');
                    break;
            }
        });

        //监听尾部按钮事件
        $footer.off('click', 'a');
        $footer.on('click', 'a', function () {
            var _opt = $(this).data("opt");
            switch (_opt) {
                //根据用户选择导出相应地块坐标
                case 'export':
                    if (selectedFeatures.length > 0) {
                        var gras = [];
                        $.each(selectedFeatures, function (index, item) {
                            gras.push(item.graphic);
                        });
                        _export(gras);
                        //console.log(wid_features.graphic.geometry);
                    } else {
                        //DPF添加
                        console.log("默认输出所有要素");
                        _export(wid_geometry);
                        //layer.msg("请选择需要导出的地块！", {time: 1500});
                    }
                    break;
                case 'clear':
                    _clear();
                    break;
            }
        });
    }

    /***
     * 文件导入监听
     * 注：此处一定要用 $('#io-file-input') 不能用变量
     */
    function listenerFileInput() {
        $('#io-file-input').off('change');
        $('#io-file-input').on('change', function () {
            //此处一定要用 $('#io-file-input') 不能用变量
            try {
                geometryIO.impFromFile($('#io-file-input'), _map.spatialReference, true).then(function (data) {
                    var featureSet = data.fs;
                    var token = data.token;
                    var dwgFileName = data.fileName;
                    if (featureSet.hasOwnProperty("features")) {
                        layer.msg('解析导入数据...', {time: 8000});
                        renderFeatures(featureSet.features, token, dwgFileName);
                    }
                }, function (msg) {
                    layer.msg(msg, {icon: 2, time: 3000});
                });
            } catch (e) {
            }
            timeout = setTimeout(function () {
                listenerFileInput();
            }, 2000);
        });
    }

    /***
     * 选择要素
     * @param event
     */
    function doSelect(event) {
        _map.setMapCursor('default');
        _selectLayers = [];
        //解析配置
        var lyrs = _ioConfig.layers;
        if (lyrs != undefined && lyrs.length > 0) {
            arrayUtil.forEach(lyrs, function (item) {
                var _lyr = _map.getLayer(item.serviceId);
                if (_lyr != undefined) {
                    _selectLayers.push(lang.mixin(item, {url: _lyr.url}));
                }
            });
        }

        if (_selectLayers.length == 0) {
            if (lyrs != undefined && lyrs.length > 0) {
                layer.alert('未打开选择要素图层!', {icon: 0});
            }
            else {
                layer.alert('未配置选择要素图层!', {icon: 0});
            }
            return;
        }
        _lastClick = event.mapPoint;
        _selectCount = _selectLayers.length;
        layer.closeAll();
        layer.msg('查询中..', {time: 10000});

        _selectResults = [];
        _map.graphics.clear();
        _map.infoWindow.hide();

        var identifyParams = new IdentifyParameters();
        identifyParams.tolerance = 5;
        identifyParams.returnGeometry = true;
        identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
        identifyParams.width = _map.width;
        identifyParams.height = _map.height;
        identifyParams.geometry = event.mapPoint;
        identifyParams.mapExtent = _map.extent;

        for (var i in _selectLayers) {
            var tmp = _selectLayers[i];
            var lyr = _map.getLayer(tmp.serviceId);
            if (lyr === undefined) {
                continue;
            }
            var identifyTask = new IdentifyTask(lyr.url);
            on(identifyTask, 'error', _selectResultError);
            identifyTask.execute(identifyParams, lang.hitch(this, _handSelectResult, tmp));
        }
    }

    function _selectResultError(error) {
        console.error(error.message);
        _map.setMapCursor('default');
    }

    /***
     * 处理查询结果
     * @param token
     * @param result
     * @private
     */
    function _handSelectResult(token, result) {
        _resultObj = {};
        if ((result != null && result.length > 0) && _selectCount > 0) {
            if (lang.isArray(result)) {
                arrayUtil.forEach(result, function (item) {
                    _selectResults.push(item.feature);
                });
            }
        }
        _selectCount -= 1;
        if (_selectCount == 0) {
            layer.closeAll();
            if (_selectResults.length > 0) {
                renderFeatures(_selectResults, token);
            } else {
                $("#ioList p").removeClass("hidden");
                $("#ioList ul").addClass("hidden");
            }
        }
    }

    /***
     * 渲染图形要素(选择/导入)
     * @param features
     */
    var wid_geometry = [];

    function renderFeatures(features, token, dwgFileName) {
        if (lang.isArray(features)) {
            //清空已有的列表
            $listContainer.empty();
            wid_geometry = [];
            dataList = [];
            //DPF 添加
            if (flag == -1) {
                arrayUtil.forEach(features, function (feature) {
                    var geometry = feature.geometry;
                    wid_geometry.push(feature.graphic);//获取数据的所有geom
                    var attributes = {
                        subtitle: feature.subtitle,
                        title: feature.title
                    };//属性
                    var gra = new Graphic(geometry, fillSymbol, attributes);
                    graphicsLyr.add(gra);
                    dataList.push({id: feature.uid, graphic: feature.graphic, data: attributes});
                });
            } else {
                arrayUtil.forEach(features, function (feature) {
                    var geometry = feature.geometry;
                    //当type和rings不存在时默认是point
                    if (geometry.type == undefined) {
                        if (geometry.rings == undefined) {
                            geometry = new Point(geometry);
                            var attributes = generateProperties(feature.attributes, token, dwgFileName);
                            var gra = new Graphic(geometry, pointSymbol, attributes);
                        }
                        else {
                            //当只有type不存在时默认为polygon
                            geometry = new Polygon(geometry);
                            var attributes = generateProperties(feature.attributes, token, dwgFileName);
                            var gra = new Graphic(geometry, fillSymbol, attributes);
                        }
                    }
                    else {
                        var attributes = generateProperties(feature.attributes, token, dwgFileName);
                        var gra = new Graphic(geometry, fillSymbol, attributes);
                    }
                    //添加至图层显示以及添加至结果列表
                    graphicsLyr.add(gra);
                    wid_geometry.push(gra);
                    dataList.push({id: RandomUuid(), graphic: gra, data: attributes});
                });
            }

            //渲染显示列表
            var template = Handlebars.compile(itemsTpl);
            $listContainer.append(template({list: dataList}));
            console.log(dataList);
            layer.closeAll();

            $.Huifold("#ioList .item h4", "#ioList .item .info", "", 2, "click");

            var scrollHeight = $(window).height() - 280;
            $(".scrollContent").slimScroll({
                height: scrollHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });

            //单击列表元素高亮显示并进行定位
            $(".item-content").on('click', function () {
                var selected = "item-content-selected";
                if ($(this).hasClass(selected)) {
                    $(this).removeClass(selected);
                    var featureId = $(this).data("id");
                    $.each(selectedFeatures, function (index, item) {
                        if (item != undefined) {
                            if (item.id === featureId) {
                                selectedFeatures.splice(arrayUtil.indexOf(selectedFeatures, item), 1);
                            }
                        }
                    });
                    highlightSelected(selectedFeatures, false);
                } else {
                    $(this).addClass(selected);
                    var id = $(this).data("id");
                    $.each(dataList, function (idx, item) {
                        if (item.id === id) {
                            selectedFeatures.push(item);
                            //由于需要读取后台配置 不调用MapUtils.highlightFeatures()
                            if (item.graphic.geometry.type != "point") {
                                MapUtils.flashFeatures([item.graphic], "fast");
                            }
                            highlightSelected(selectedFeatures, false);
                            MapUtils.locateFeatures([item.graphic]);
                        }
                    });
                }
            });
            $(".item-export").on('click', function () {
                var id = $(this).parent().data("id");
                $.each(dataList, function (idx, item) {
                    if (item.id === id) {
                        _export([item.graphic]);
                    }
                });
            });
            //显示导出按钮
            $footer.show();

        }
    }

    /***
     * 生成图形的属性信息
     * @param attr
     * @param token
     */
    function generateProperties(attr, token, dwgFileName) {
        var prop = {};
        //选择的图形带有属性
        if (attr != undefined && token.url != undefined) {
            var arr = arrayUtil.filter(_selectLayers, function (lyr) {
                return token.url === lyr.url;
            });
            if (arr.length > 0) {
                var tf = arr[0].titleField;
                var rf = arr[0].returnFields;
                if (attr["xmmc"]) {
                    prop.title = attr["xmmc"];
                } else if (attr["XMMC"]) {
                    prop.title = attr["XMMC"];
                } else {
                    prop.title = attr[tf.name] || attr[tf.alias];
                }
                arrayUtil.forEach(rf, function (item) {
                    var val = attr[item.name] || attr[item.alias];
                    if (item.name != tf.name && prop.subtitle == undefined)
                        prop.subtitle = val;
                    else
                        prop[item.alias] = val;
                });
            }
        } else {
            if (attr["xmmc"]) {
                prop.title = attr["xmmc"];
            } else if (attr["XMMC"]) {
                prop.title = attr["XMMC"];
            } else {
                if (dwgFileName)
                    prop.title = dwgFileName.concat("-").concat(dataList.length + 1);
                else
                    prop.title = "导入图形 ".concat(dataList.length + 1);
            }
            //来自导入的文件
            prop.subtitle = "文件来源:" + getAlias(token);
        }
        return prop;
    }

    /***
     * 数据导出
     */
    function _export(gras) {
        var featureSet = new FeatureSet();
        featureSet.features = gras;

        console.log(featureSet);

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

    /***
     * 切换当前widget后 移除相关显示图层以及数据
     * @private
     */
    function _pause() {
        //var id= graphicsLyr.id;
        //var lyr=_map.getLayer(id);
        //if(lyr!=null){
        //    _map.removeLayer(lyr);
        //}
        _map.graphics.clear();
    }

    /***
     * 清除相关变量
     * @private
     */
    function _clear() {
        $listContainer.empty();
        if (dataList.length > 0) {
            graphicsLyr.clear();
            dataList = [];
        }
        selectedFeatures = [];
        _map.graphics.clear();

        //清除导入的共享数据
        geoDataStore.remove(GeoDataStore.SK_UPLOAD);

        $footer.css('display', 'none');
    }

    /***
     *
     * @returns {Array}
     */
    function parseTypesArray() {
        var expType = _ioConfig.exportType;
        var types = expType.split(",");
        var r = [];
        arrayUtil.forEach(types, function (item) {
            if (item != 'bj') {
                r.push({alias: getAlias(item), value: item});
            }
        });
        return r;
    }

    /***
     * 获取别名
     * @param name
     * @returns {*}
     */
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

    var timeout = 0;

    /**
     * 高亮显示被选中的地块
     * @param feaure
     * @param autoclear
     */
    function highlightSelected(features, autoClear) {
        _map.graphics.clear();
        $.each(features, function (i, feature) {
            var graphic = feature.graphic;
            var fillColor = _ioConfig.selectStyle.fill_color;
            var outlineColor = _ioConfig.selectStyle.outline_color;
            var geo = graphic.geometry;
            var symbol = undefined;
            if (geo != undefined) {
                switch (geo.type) {
                    case "point":
                        symbol = new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 12, null, new Color([255, 0, 0, 1]));
                        // symbol = EsriSymbolsCreator.createSimpleMarkerSymbol(EsriSymbolsCreator.markerStyle.STYLE_SQUARE, 10, lineSymbol, EsriSymbolsCreator.colorFromHex(fillColor));
                        break;
                    case "polyline":
                        symbol = lineSymbol;
                        break;
                    case "polygon":
                        symbol = fillSymbol;
                        break;
                }
                if (symbol == undefined) {
                    symbol = graphic.symbol;
                }
                var graphic = new Graphic(graphic.geometry, symbol, graphic.attributes, graphic.infoTemplate);
                _map.graphics.add(graphic);

                if (autoClear === true) {
                    clearTimeout(timeout);
                    timeout = setTimeout(function () {
                        _map.graphics.clear();
                    }, 3000);
                }

            }
        });
    }

    return me;
});