/**
 *  属性识别
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/12 16:50
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/on",
        "dojox/uuid/generateRandomUuid",
        "esri/lang",
        "esri/graphic",
        "esri/tasks/IdentifyTask",
        "esri/tasks/FeatureSet",
        "esri/tasks/IdentifyParameters",
        "esri/geometry/Point",
        "esri/geometry/Polygon",
        "esri/graphicsUtils",
        "esri/toolbars/draw",
        "map/core/BaseWidget",
        "map/core/GeometryIO",
        "slimScroll",
        "mustache",
        "layer",
        "handlebars",
        "map/utils/MapUtils",
        "map/component/MapPopup",
        "map/component/ListDataRenderer",
        "css!static/thirdparty/h-ui/lib/iconfont/iconfont.css",
        "static/thirdparty/h-ui/js/H-ui"],
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, Polygon, graphicsUtils, Draw, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer) {

        var __map, _faConfig;
        var drawTool, drawHandler;
        var $cadSpatialQueryPanel;
        //查询结果
        var _selectResults = [];
        var _selectCount = 0;
        var _faResults = [];
        var mapPopup = MapPopup.getInstance();


        var me = declare([BaseWidget], {

            onCreate: function () {
                __map = this.getMap().map();
                _faConfig = this.getConfig();
                _init();
                _addListeners();
            },
            onOpen: function () {
                __map = this.getMap().map();
                _faLayers = [];
                _init();
                _activated = true;
                $switchBtn.addClass("omp-switch-on");
                $switchBtn.attr('title', '关闭功能');
                // _prepare();
            },
            onPause: function () {
                _activated = false;
                _clear();
            },
            onDestroy: function () {
                _clear();
            }
        });

        //设置属性识别后信息弹出窗的样式
        var POPUP_OPTION_INFO = "infoWindow";
        var POPUP_OPTION_MODAL = "modal";

        var _activated = false;
        //存放配置的属性识别图层
        var _faLayers = [];

        //默认是infowindow样式
        var popUpStyle = POPUP_OPTION_INFO;

        var _mapClickHanlder = null;
        var _identifyResults = [];
        var _identifyCount = 0;
        var _listData = [];//页面展示的数据集合
        var _lastClick;
        var _resultObj = {};
        var faLayerData = {};//属性识别数据

        var $clearBtn, $descSpan, $switchBtn;

        //是否显示底部按钮
        var showArchiveBtn = false;
        var exportData = false;
        var exportTypes = "";
        var geometryIO;

        /**
         *  初始化组件
         */
        function _init() {
            //读取弹出层方式
            if (_faConfig.popUpStyle && _faConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            if (_faConfig.hasOwnProperty("showFooter")) {
                showArchiveBtn = _faConfig.showFooter;
            }

            layer.config();
            if (_faConfig) {
                exportTypes = _faConfig.exportTypes;
                exportData = _faConfig.exportData;
                var lyrs = _faConfig.layers;
                //组织url等参数
                try {
                    $.each(lyrs, function (i, item) {
                        var _lyr = __map.getLayer(item.serviceId);
                        if (_lyr != undefined) {
                            _faLayers.push(lang.mixin(item, {url: _lyr.url}));
                        }
                    });
                } catch (e) {
                    console.log(e.message);
                }
            }
            //初始化jq变量
            $clearBtn = $("#faClearBtn");
            $descSpan = $("#faDescSpan");
            $switchBtn = $("#iSwitchBtn");

            $cadSpatialQueryPanel = $("#faSpatialQueryPanel");
            //空间查询按钮监听
            $cadSpatialQueryPanel.find('.spatial-geo-icon').on('click', function () {
                spatialClickHandler($(this).data("type"));
            });
            geometryIO = new GeometryIO();
        }

        /***
         * 空间查询handler
         * @param evt
         */
        function spatialClickHandler(type) {
            if (drawTool != undefined) drawTool.deactivate();
            if (drawHandler != undefined) drawHandler.remove();
            drawTool = drawTool ? drawTool : new Draw(__map);
            drawHandler = on(drawTool, "draw-end", lang.hitch(this, doSpatialQuery));
            switch (type) {
                case "point":
                    drawTool.activate(Draw.POINT);
                    break;
                case "polyline":
                    drawTool.activate(Draw.POLYLINE);
                    break;
                case "rect":
                    drawTool.activate(Draw.EXTENT);
                    break;
                case "polygon":
                    drawTool.activate(Draw.POLYGON);
                    break;
                default :
                    console.error(type + " is unsupported yet!");
                    break;
            }
        }

        /***
         * 执行空间查询
         * @param evt
         */
        function doSpatialQuery(evt) {
            drawHandler.remove();
            drawTool.deactivate();
            _selectCount = _faLayers.length;
            _faResults = [];
            _resultObj = [];
            _clear();
            _identifyCount = _faLayers.length;
            $.each(_faLayers, function (i, item) {
                var layerUrl = item.url;
                var identifyParams = new IdentifyParameters();
                identifyParams.tolerance = 3;
                identifyParams.returnGeometry = true;
                identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
                identifyParams.width = __map.width;
                identifyParams.height = __map.height;
                identifyParams.geometry = evt.geometry;
                identifyParams.mapExtent = __map.extent;
                identifyParams.busitype=item.busitype;

                var identifyTask = new IdentifyTask(layerUrl);
                on(identifyTask, 'error', _faResultError);
                // identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult, item));
                identifyTask.execute(identifyParams, lang.hitch(this, _getDAData, item));
            });
        }


        /**
         *
         * @param error
         * @private
         */
        function _faResultError(error) {
            console.error(error.message);
            __map.setMapCursor('default');
        }


        /**
         * DOM事件监听
         */
        function _addListeners() {
            $clearBtn.on("click", function () {
                $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                $("#faLayerFoldl").addClass("hidden").empty();
                $clearBtn.addClass("hidden");
                __map.graphics.clear();
                layer.closeAll();
                if (mapPopup.isShowing) mapPopup.closePopup();
            });
        }


        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            layer.closeAll();
            if (mapPopup.isShowing) mapPopup.closePopup();
        }

        function _getDAData(token, result) {
            var daUrl = root+"/geometryService/analysis/tx/da";
            $.ajax({
                type: "post",
                url: daUrl,
                async: false,
                data: {
                    jsydIpPort: _faConfig.jsydIpPort,
                    modelName: token.modelName,
                    daIpPort: _faConfig.daIpPort,
                    busitype:token.busitype,
                    resultData: JSON.stringify(result)
                },
                transformRequest: [function (data, headers) {
                    console.log(headers)
                    return qs.stringify(data)
                }],
                success: function (data) {
                    if (data != "" && data != null && data != undefined) {
                        _handleIdentifyResult(token, data)
                    }else{
                        _identifyCount-= 1;
                    }
                }
            });
        }

        /**
         * 处理空间查询结果
         * @param event
         * @param result
         * @private
         */
        function _handleIdentifyResult(token, result) {
            result = JSON.parse(result);
            if ((result != null && result.length > 0) && _identifyCount > 0) {
                if (_identifyResults.length > 0)
                    _identifyResults = [];
                _identifyResults = _identifyResults.concat(result);
                arrayUtil.forEach(_identifyResults, function (item) {
                    if (token.layerName === item.layerName) {    //过滤图层
                        var tmp = {};
                        tmp.layerName = item.layerName;
                        tmp.layerId = item.layerId;
                        if (item.feature.geometry.type == "point") {
                            var geometry = new Point(item.feature.geometry);
                            item.feature.geometry = geometry;
                        } else {
                            //默认为polygon
                            var geometry = new Polygon(item.feature.geometry);
                            item.feature.geometry = geometry;
                        }
                        tmp.feature = item.feature;
                        var attr = item.feature.attributes;
                        tmp.objectid = attr.OBJECTID || RandomUuid();
                        var rf = token.returnFields;
                        var tf = token.titleField;
                        var _data = [];
                        _data.push({key: tf.alias, value: attr[tf.name] || attr[tf.alias]});
                        for (var j in rf) {
                            var f = rf[j];
                            if (f.name != tf.name && _data.length == 1)
                                _data.push({key: f.alias || f.name, value: attr[f.name] || attr[f.alias]});
                        }
                        tmp.data = _data;
                        if (item.layerName in _resultObj) {
                            _resultObj[item.layerName].push(tmp);
                        } else {
                            _resultObj[item.layerName] = [];
                            _resultObj[item.layerName].push(tmp);
                        }
                        tmp = null;
                        _data = null;
                    }
                });
            }
            _identifyCount -= 1;
            if (_identifyCount == 0) {
                layer.closeAll();
                if (!isEmpty(_resultObj)) {
                    //清空dom
                    $("#faLayerFoldl").empty();
                    $descSpan.empty();
                    $("#faResultDiv ul").removeClass("hidden");
                    $clearBtn.removeClass("hidden");
                    var options = '';
                    faLayerData = {};
                    for (var name in _resultObj) { //组织识别信息
                        var data = {layerName: name, info: []};
                        for (var i in _resultObj[name]) {
                            var g = _resultObj[name][i].feature;
                            var tmp = {
                                "geometry": {
                                    "rings": g.geometry.rings,
                                    "spatialReference": __map.spatialReference
                                }, "type": "esriSFS"
                            };
                            var graphic = new Graphic(tmp);
                            var info = _resultObj[name][i].data;
                            data.info.push({
                                title: info[0].key.concat(":").concat(info[0].value),
                                subtitle: info[1].key.concat(":").concat(info[1].value),
                                graphic: graphic,
                                objectid: _resultObj[name][i].objectid,
                                layerId: name
                            });
                        }
                        faLayerData[name] = data.info;
                        options += '<option value="' + name + '">' + name + '</option>';
                    }
                    var layerSelect = '<select id="faLayerSelect" style="margin-left: 15px;" class="form-control input-sm">' + options + '</select>';
                    $descSpan.removeClass("text-muted").addClass("text-primary").html(layerSelect);

                    $('#faLayerSelect').on("change", function () {
                        $('#faLayerFoldl').empty();
                        var name = $(this).val();
                        var listDataRenderer;
                        if (!_faConfig.leasPostUrl) {//判断是否需要进行推送渲染模板
                            if (exportData) {
                                listDataRenderer = new ListDataRenderer({
                                    renderTo: $('#faLayerFoldl'),
                                    type: "export",
                                    map: map.map(),
                                    renderData: faLayerData[name]
                                });
                            } else {
                                listDataRenderer = new ListDataRenderer({
                                    renderTo: $('#faLayerFoldl'),
                                    type: "loc",
                                    map: map.map(),
                                    renderData: faLayerData[name]
                                });
                            }
                        } else {
                            listDataRenderer = new ListDataRenderer({
                                renderTo: $('#faLayerFoldl'),
                                type: "locAndPost",
                                map: map.map(),
                                renderData: faLayerData[name]
                            });
                        }

                        //进行定位展示详细信息
                        listDataRenderer.on('location', function (data) {
                            var obj = findFeatureByLayerIdAndOid(data.layerId, data.objectid);
                            if (obj != null) {
                                var lyr = arrayUtil.filter(_faLayers, function (item) {
                                    return item.layerName === obj.layerName;
                                })[0];
                                locGraphic(obj, lyr);
                            }
                        });

                        //自动定位当前
                        arrayUtil.forEach(faLayerData[name], function (item) {
                            var currObj = findFeatureByLayerIdAndOid(item.layerId, item.objectid);
                            if (currObj != null) {
                                var lyr = arrayUtil.filter(_faLayers, function (item) {
                                    return item.layerName === currObj.layerName;
                                })[0];
                                locGraphic(currObj, lyr);
                            }
                        });


                        listDataRenderer.on("export", function (data) {
                            var obj = findFeatureByLayerIdAndOid(data.layerId, data.objectid);
                            if (obj != null) {
                                var featureSet = new FeatureSet();
                                featureSet.features = [obj.feature];
                                var exportSelTpl = $("#export-select-tpl").html();
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
                        });

                        //进行推送
                        listDataRenderer.on('post', function (data) {
                            var obj = findFeatureByLayerIdAndOid(data.layerId, data.objectid);
                            if (obj.feature) {
                                $.ajax({
                                    url: _faConfig.leasPostUrl.matchEL(),
                                    dataType: "jsonp",
                                    data: {data: obj.feature.attributes, proid: getArgumentFormUrl("proid")},
                                    success: function () {
                                        layer.msg("推送完毕！");
                                    }
                                });
                            }
                        });
                        listDataRenderer.render();
                        var scrollHeight = $(window).height() - 220;
                        $("#faLayerFoldl").slimScroll({
                            height: scrollHeight
                        });
                    });
                    $('#faLayerSelect').trigger('change');

                } else {
                    $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                    $("#faResultDiv p").removeClass("hidden");
                    $("#faResultDiv ul").addClass("hidden");
                    $clearBtn.addClass("hidden");
                }
            }
        }

        /***
         *
         * @returns {Array}
         */
        function parseTypesArray() {
            var types = exportTypes != "" ? exportTypes.split(",") : "txt,xls,cad,shp,bj".split(",");
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

        /**
         * 获取url中的参数
         * @param name
         * @returns {*}
         */
        function getArgumentFormUrl(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        };


        /***
         * 定位graphic
         * @param obj
         */
        function locGraphic(obj, lyr) {
            //弹出地图信息窗
            var returnFields = lang.clone(lyr.returnFields);
            MapUtils.highlightFeatures([obj.feature], false, _faConfig.locateOpacity);
            //判断是否需要直接打开配置好的超链接
            if (esriLang.isDefined(lyr.link) && lyr.link.defaultOpen === true) {
                var attr = obj.feature.attributes;
                if (esriLang.isDefined(attr)) {
                    var url = esriLang.substitute(attr, lyr.link.url);
                    layer.open({
                        type: 2,
                        title: "地块详情",
                        area: ['800px', "600px"],
                        shade: 0,
                        content: [url, 'no']
                    });
                    return;
                }
            }
            if (popUpStyle == POPUP_OPTION_MODAL) {
                var linkName = lyr.link.tip;
                var linkUrl = lyr.link.url;
                var returnAlias = [];
                for (var i = 0; i < returnFields.length; i++) {
                    returnAlias.push(returnFields[i].alias);
                }
                var x = (__map.position.x) + 'px';
                if (linkName != "" && linkUrl != "") {
                    layer.open({
                        title: obj.layerName
                        , area: ['300px', '400px']
                        , content: getInfoContent(obj.feature, returnFields)
                        , shade: 0
                        , btn: [linkName]
                        , yes: function (index, layero) {
                            //超链接按钮的回调
                            var linkNewUrl = mapPopup._getLinkUrl(obj.feature.attributes, returnFields, lyr.link);
                            window.open(linkNewUrl);
                        }
                        , offset: ['140px', x]
                    });
                } else {
                    layer.open({
                        title: obj.layerName
                        , area: '300px'
                        , content: getInfoContent(obj.feature, returnFields)
                        , shade: 0
                        , btn: []
                        , offset: ['140px', x]
                    });
                }

                MapUtils.locateFeatures([obj.feature]);
            } else {
                var bdcQueryField = lyr.bdcQueryField;
                var tfs = lang.clone(lyr.titleField.name);
                var attributes = obj.feature.attributes;
                var link = lang.clone(lyr.link);
                if (mapPopup.isShowing)
                    mapPopup.closePopup();
                if (bdcQueryField && esriLang.substitute(attributes, bdcQueryField)) {
                    $.ajax({
                        url: root + "/bdcQuery/bdczt?bdcdybh=" + esriLang.substitute(attributes, bdcQueryField),
                        async: false,
                        success: function (r) {
                            if (r.bdcdybh) {
                                r.empty = 'true';
                                for (var i in r) {
                                    if (r[i] == 1) {
                                        r.empty = false;
                                        break;
                                    }
                                }
                                r.bdcUrl = esriLang.substitute(attributes, lyr.bdcUrl);
                                mapPopup.setBdczt(r);
                            } else {
                                mapPopup.setBdczt({empty: true, bdcUrl: esriLang.substitute(attributes, lyr.bdcUrl)});
                            }
                        }
                    });
                }
                mapPopup.setData(attributes);
                mapPopup.setTitleField(tfs);
                mapPopup.setFields(returnFields);
                mapPopup.setLink(link);
                mapPopup.setSecLink(lyr.secLink);
                if (lyr.hasOwnProperty("showFooter")) {
                    var showFooter = lyr.showFooter;
                    if (showFooter) mapPopup.setBLinkEnabled(showFooter);
                }
                if (showArchiveBtn)
                    mapPopup.setBLinkEnabled(true);
                mapPopup.openPopup(getInfoPosition(obj.feature.geometry)).then(function () {
                    MapUtils.locateFeatures_cz([obj.feature]);
                });

            }
        }

        /**
         *
         * @param error
         * @private
         */
        function _identifyResultError(error) {
            console.error(error.message);
            __map.setMapCursor('default');
        }

        /**
         *
         * @param obj
         * @param returnFields
         * @returns {*}
         */
        function getInfoContent(feature, returnFields) {
            var data = [];
            var tmpl = $("#infoContentTpl").html();
            var showData = feature.attributes;
            for (var i in showData) {
                for (var j = 0; j < returnFields.length; j++) {
                    if (i.equalsIgnoreCase(returnFields[j].name) || i.equalsIgnoreCase(returnFields[j].alias)) {
                        data.push({key: returnFields[j].alias, value: showData[i]});
                    }
                }
            }
            return Mustache.render(tmpl, {data: data});
        }

        /***
         *
         * @param geometry
         * @returns {*}
         */
        function getInfoPosition(geometry) {
            switch (geometry.type) {
                case 'point':
                    return geometry;
                    break;
                case 'polyline':
                    break;
                case 'polygon':
                    var rightPiont = getPostion_(geometry);
                    var point_P = new Point(rightPiont);
                    return point_P;
                    break;

            }
            return null;
        }

        /**
         * 获取点
         * @param geometry
         * @returns {*}
         * @private
         */
        function getPostion_(geometry) {
            var arr = [];
            var rings = geometry.rings;
            rings = rings[0];
            var xMax = geometry.getExtent().xmax;

            for (var i = 0; i < rings.length; i++) {
                var x_ = rings[i];
                if (x_[0] == xMax) {
                    arr.push(x_);
                    if (arr.length != 1) {
                        return arr[arr.length];
                    } else {
                        return arr[0];
                    }

                }
            }
        }

        /**
         *
         * @param id
         * @returns {*}
         */
        function findFeatureByOid(id) {
            for (var i in _listData) {
                var obj = _listData[i];
                if (obj.objectid == id) {
                    return obj;
                }
            }
            return null;
        }

        /**
         *
         * @param layerId
         * @param oId
         * @returns {*}
         */
        function findFeatureByLayerIdAndOid(layerId, oId) {
            for (var i in _resultObj[layerId]) {
                var obj = _resultObj[layerId][i];
                if (obj.objectid == oId) {
                    return obj;
                }
            }
            return null;
        }

        /**
         * 检测对象是否为没有任何属性对象
         * 不检测继承原型链继承的属性
         * @returns {boolean}
         */
        function isEmpty(obj) {
            for (var name in obj) {
                if (obj.hasOwnProperty(name)) {
                    return false;
                }
            }
            return true;
        }

        /***
         * 添加滚动条
         * @param selector  需要添加滚动条的$元素
         * @param height    滚动条设置高度
         */
        function addSlimScroll(selector, height) {
            selector.slimScroll({
                height: height,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        }

        return me;
    });