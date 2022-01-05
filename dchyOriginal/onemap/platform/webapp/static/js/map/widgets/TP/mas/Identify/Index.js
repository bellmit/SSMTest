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
        "esri/graphicsUtils",
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
    function (declare, lang, arrayUtil, on, RandomUuid, esriLang, Graphic, IdentifyTask, FeatureSet, IdentifyParameters, Point, graphicsUtils, BaseWidget, GeometryIO, SlimScroll, Mustache, layer, Handlebars,
              MapUtils, MapPopup, ListDataRenderer) {

        var __map, _identifyConfig;
        var mapPopup = MapPopup.getInstance();
        //进行拓展
        var closePopup = mapPopup.closePopup;
        var openPopup =mapPopup.openPopup;
        mapPopup.closePopup =function () {
            closePopup(this);
            $(".esriPopup").removeClass("visible");
        };

        mapPopup.openPopup =function (obj) {
            $(".esriPopup").addClass("visible");
            return openPopup(obj,this);
        };




        var me = declare([BaseWidget], {

            onCreate: function () {
                __map = this.getMap().map();
                _identifyConfig = this.getConfig();
                _init();
                _addListeners();
            },
            onOpen: function () {
                __map = this.getMap().map();
                _identifyLayers = [];
                _init();
                _activated = true;
                $switchBtn.addClass("omp-switch-on");
                $switchBtn.attr('title', '关闭功能');
                _prepare();
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
        var _identifyLayers = [];

        //默认是infowindow样式
        var popUpStyle = POPUP_OPTION_INFO;

        var _mapClickHanlder = null;
        var _identifyResults = [];
        var _identifyCount = 0;
        var _listData = [];//页面展示的数据集合
        var _lastClick;
        var _resultObj = {};
        var identifyLayerData = {};//属性识别数据

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
            if (_identifyConfig.popUpStyle && _identifyConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            if (_identifyConfig.hasOwnProperty("showFooter")) {
                showArchiveBtn = _identifyConfig.showFooter;
            }

            layer.config();
            if (_identifyConfig) {
                exportTypes = _identifyConfig.exportTypes;
                exportData = _identifyConfig.exportData;
                var lyrs = _identifyConfig.layers;
                //组织url等参数
                try {
                    $.each(lyrs, function (i, item) {
                        var _lyr = __map.getLayer(item.serviceId);
                        if (_lyr != undefined) {
                            _identifyLayers.push(lang.mixin(item, {url: _lyr.url}));
                        }
                    });
                } catch (e) {
                    console.log(e.message);
                }
            }
            //初始化jq变量
            $clearBtn = $("#iClearBtn");
            $descSpan = $("#iDescSpan");
            $switchBtn = $("#iSwitchBtn");
            geometryIO = new GeometryIO();
        }

        /**
         * DOM事件监听
         */
        function _addListeners() {
            $clearBtn.on("click", function () {
                $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                $("#layerFoldl").addClass("hidden").empty();
                $clearBtn.addClass("hidden");
                __map.graphics.clear();
                layer.closeAll();
                if (mapPopup.isShowing) mapPopup.closePopup();
            });
            $switchBtn.on('click', function () {
                var $this = $(this);
                if ($this.hasClass("omp-switch-on")) {
                    $this.removeClass("omp-switch-on");
                    $this.find('em').text("关");
                    $this.attr('title', '打开功能');
                    if (_mapClickHanlder) _mapClickHanlder.remove();
                    __map.setMapCursor('default');
                } else {
                    $this.addClass("omp-switch-on");
                    $this.find('em').text("开");
                    $this.attr('title', '关闭功能');
                    _prepare();
                }
            });
        }

        /**
         * 添加地图监听
         * @private
         */
        function _prepare() {
            _mapClickHanlder = __map.on('click', doIdentify);
            __map.setMapCursor('crosshair');
            MapUtils.setMap(__map);
        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear() {
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            //layer.closeAll();
            //if (mapPopup.isShowing) mapPopup.closePopup();
        }

        /**
         * do identify
         * @param evt
         */
        function doIdentify(event) {
            if (_identifyLayers.length == 0) {
                if (_identifyConfig.layers.length === 0) {
                    layer.alert('未配置任何属性识别图层!', {btn: [], shadeClose: true});
                } else {
                    layer.msg("待识别的服务尚未加载到地图中！", {icon: 0});
                }
                return;
            }
            _lastClick = event.mapPoint;
            _identifyCount = _identifyLayers.length;
            layer.closeAll();
            layer.msg('属性识别中..', {time: 10000});
            _identifyResults = [];
            _listData = [];
            _resultObj = [];
            __map.graphics.clear();
            __map.infoWindow.hide();

            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
            identifyParams.width = __map.width;
            identifyParams.height = __map.height;
            identifyParams.geometry = event.mapPoint;
            identifyParams.mapExtent = __map.extent;

            for (var i in _identifyLayers) {
                var tmp = _identifyLayers[i];
                if (__map.getLayer(tmp.serviceId) == undefined) {
                    _identifyCount -= 1;
                    continue;
                }
                var identifyTask = new IdentifyTask(__map.getLayer(tmp.serviceId).url);
                on(identifyTask, 'error', _identifyResultError);
                identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult, tmp));
            }
        }

        /**
         * 处理识别结果
         * @param event
         * @param result
         * @private
         */
        function _handleIdentifyResult(token, result) {
            if ((result != null && result.length > 0) && _identifyCount > 0) {
                if (_identifyResults.length > 0)
                    _identifyResults = [];
                _identifyResults = _identifyResults.concat(result);
                arrayUtil.forEach(_identifyResults, function (item) {
                    if (token.layerName === item.layerName) {    //过滤图层
                        var tmp = {};
                        tmp.layerName = item.layerName;
                        tmp.layerId = item.layerId;
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
                    $("#layerFoldl").empty();
                    $descSpan.empty();
                    $("#resultDiv ul").removeClass("hidden");
                    $clearBtn.removeClass("hidden");
                    var options = '';
                    identifyLayerData = {};
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
                        identifyLayerData[name] = data.info;
                        options += '<option value="' + name + '">' + name + '</option>';
                    }
                    var layerSelect = '<select id="identifyLayerSelect" style="margin-left: 15px;" class="form-control input-sm">' + options + '</select>';
                    $descSpan.removeClass("text-muted").addClass("text-primary").html(layerSelect);

                    $('#identifyLayerSelect').on("change", function () {
                        $('#layerFoldl').empty();
                        var name = $(this).val();
                        var listDataRenderer;
                        if (!_identifyConfig.leasPostUrl) {//判断是否需要进行推送渲染模板
                            if (exportData) {
                                listDataRenderer = new ListDataRenderer({
                                    renderTo: $('#layerFoldl'),
                                    type: "export",
                                    map: map.map(),
                                    renderData: identifyLayerData[name]
                                });
                            } else {
                                listDataRenderer = new ListDataRenderer({
                                    renderTo: $('#layerFoldl'),
                                    type: "loc",
                                    map: map.map(),
                                    renderData: identifyLayerData[name]
                                });
                            }
                        } else {
                            listDataRenderer = new ListDataRenderer({
                                renderTo: $('#layerFoldl'),
                                type: "locAndPost",
                                map: map.map(),
                                renderData: identifyLayerData[name]
                            });
                        }

                        //进行定位展示详细信息
                        listDataRenderer.on('location', function (data) {
                            var obj = findFeatureByLayerIdAndOid(data.layerId, data.objectid);
                            if (obj != null) {
                                var lyr = arrayUtil.filter(_identifyLayers, function (item) {
                                    return item.layerName === obj.layerName;
                                })[0];
                                locGraphic(obj, lyr);
                            }
                        });

                        //自动定位当前
                        arrayUtil.forEach(identifyLayerData[name], function (item) {
                            var currObj = findFeatureByLayerIdAndOid(item.layerId, item.objectid);
                            if (currObj != null) {
                                var lyr = arrayUtil.filter(_identifyLayers, function (item) {
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
                                    url: _identifyConfig.leasPostUrl.matchEL(),
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
                        $("#layerFoldl").slimScroll({
                            height: scrollHeight
                        });
                    });
                    $('#identifyLayerSelect').trigger('change');

                } else {
                    $descSpan.empty().removeClass("text-primary").addClass("text-muted").html("无信息");
                    $("#resultDiv p").removeClass("hidden");
                    $("#resultDiv ul").addClass("hidden");
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
            MapUtils.highlightFeatures([obj.feature], false, _identifyConfig.locateOpacity);
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
                        , area:  ['300px', '400px']
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
                    if (i.equalsIgnoreCase(returnFields[j].name)||i.equalsIgnoreCase(returnFields[j].alias)) {
                        data.push({key: returnFields[j].alias, value: showData[i]});
                    }
                }
            }
            return Mustache.render(tmpl, {data: data});

            /*var data = [];
            var tmpl = $("#infoContentTpl").html();
            var showData = obj.feature.attributes;
            for (var i in showData) {
                if ($.inArray(i, returnFields) != -1)
                    data.push({key: i, value: showData[i]});
            }
            return Mustache.render(tmpl, {data: data});*/
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