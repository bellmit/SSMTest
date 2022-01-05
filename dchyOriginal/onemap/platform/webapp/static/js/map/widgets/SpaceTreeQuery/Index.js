/**
 * 地图属性查询
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/11 10:08
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "layer",
    "mustache",
    "handlebars",
    "slimScroll",
    "static/thirdparty/laypage/laypage",
    "esri/toolbars/draw",
    "esri/graphic",
    "esri/lang",
    "esri/tasks/IdentifyParameters",
    "map/core/QueryTask",
    "esri/tasks/IdentifyTask",
    "esri/tasks/FeatureSet",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "esri/geometry/Point",
    "map/core/JsonConverters",
    "map/core/BaseWidget",
    "map/core/GeometryIO",
    "map/component/ListDataRenderer",
    "map/component/ChosenSelect",
    "map/core/GeoDataStore",
    "static/thirdparty/laydate/laydate",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min"], function (declare, lang, arrayUtil, on, layer, Mustache, Handlebars, SlimScroll, laypage, Draw, Graphic, esriLang,
        IdentifyParameters, QueryTask, IdentifyTask, FeatureSet, MapUtils, MapPopup, Point, JsonConverters, BaseWidget, GeometryIO, ListDataRenderer, ChosenSelect, GeoDataStore) {

        var _queryConfig, _map, _spatialReference;
        var mapPopup = MapPopup.getInstance();
        var geoDataStore = GeoDataStore.getInstance();
        var filterServiceId = ""; //过滤图层id
        var highlightFeatureOpacity = undefined;

        var query = declare([BaseWidget], {
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
                _spatialReference = _map.spatialReference;
                _queryConfig = this.getConfig();
                _init();
            },
            onOpen: function () {
                if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").show();

            },
            onPause: function () {
                // if (mapPopup.isShowing) mapPopup.closePopup();
                _map.graphics.clear();
                if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").hide();
                _activated = false;
                _clear2();
            },
            onDestroy: function () {
                // if (mapPopup.isShowing) mapPopup.closePopup();
                dateCondition = {};
                rangeCondition = {};
                clearDefinitionExpression();
                if (_map.getLayer("graphics4Print")) _map.removeLayer(_map.getLayer("graphics4Print"));
                _clear2();

            }
        });
        var _activated = false;

        var drawTool, drawHandler;
        var dateCondition = {};  //存放日期型查询字段查询条件
        var rangeCondition = {}; //存放范围型的字段查询条件
        var pointCondition = {};

        var STATE_QUERY = "query";
        var STATE_RESULT = "result";

        //设置属性识别后信息弹出窗的样式
        var POPUP_OPTION_INFO = "infoWindow";
        var POPUP_OPTION_MODAL = "modal";

        //默认是infowindow样式
        var popUpStyle = POPUP_OPTION_INFO;

        var $attrSpaceTreeQueryPanel, $spatialQueryPanel, $qResultPanel, $spaceTreeqSearchBtn, $lyrSelector, $attrResultPanel,
            $spatialResultPanel;
        var _currentState = STATE_QUERY;

        var geometryIO;

        //存放配置的属性识别图层
        var _identifyLayers = [];

        var showFooter = false;
        var exportData = false;
        var exportTypes = "";
        var queryType = "";
        var $switchBtn2;

        var _mapClickHanlder2 = null;
        var _identifyResults = [];
        var _identifyCount = 0;
        var _listData = [];//页面展示的数据集合
        var _lastClick;
        var _resultObj = {};
        // var identifyLayerData = {};//属性识别数据

        //动态获取url
        var _url = '';
        $.ajax({
            url: getContextPath() + "/oms/rest/v1.0/dchy/geturl",
            type: 'post',
            async: false,
            success: function (data) {
                _url = data;
            }
        });
        /**
         *  初始化图层查询界面及功能
         * @param _queryConfig
         */
        function _init() {
            console.log('spaceTree');
            console.log('项目一棵树：项目图层：', _queryConfig);
            queryType = _queryConfig.queryType;

            geometryIO = new GeometryIO();
            $("#spatialQueryPanel").animate();
            Handlebars.registerHelper('equals', function (left, operator, right, options) {
                if (arguments.length < 3) {
                    throw new Error('Handlerbars Helper "compare" needs 2 parameters');
                }
                var operators = {
                    '==': function (l, r) {
                        return l == r;
                    },
                    '===': function (l, r) {
                        if (l != undefined)
                            return l === r;
                        else
                            return false;
                    },
                    '!=': function (l, r) {
                        return l != r;
                    },
                    '!==': function (l, r) {
                        return l !== r;
                    },
                    '<': function (l, r) {
                        return l < r;
                    },
                    '>': function (l, r) {
                        return l > r;
                    },
                    '<=': function (l, r) {
                        return l <= r;
                    },
                    '>=': function (l, r) {
                        return l >= r;
                    },
                    'typeof': function (l, r) {
                        return typeof l == r;
                    }
                };

                if (!operators[operator]) {
                    throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
                }

                var result = operators[operator](left, right);

                if (result) {
                    return options.fn(this);
                } else {
                    return options.inverse(this);
                }
            });
            Date.prototype.toLocaleString = function () {
                return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 ";
            };

            layer.config();
            if (_queryConfig) {
                exportTypes = _queryConfig.exportTypes;
                exportData = _queryConfig.exportData;
                var lyrs = _queryConfig.layers;
                //组织url等参数
                try {
                    $.each(lyrs, function (i, item) {
                        var _lyr = _map.getLayer(item.serviceId);
                        if (_lyr != undefined) {
                            _identifyLayers.push(lang.mixin(item, { url: _lyr.url }));
                        }
                    });
                } catch (e) {
                    console.log(e.message);
                }
            }
            laydate.skin('molv');
            //初始化jq变量
            $attrSpaceTreeQueryPanel = $("#attrSpaceTreeQueryPanel");
            // $qResultPanel = $("#queryResultPanel");
            $attrResultPanel = $("#attrspaceTreeQueryResultPanel");
            $spatialResultPanel = $("#spatialQueryResultPanel");
            $spatialQueryPanel = $("#spatialQueryPanel");
            //读取弹出层方式
            if (_queryConfig.popUpStyle && _queryConfig.popUpStyle.equalsIgnoreCase('modal')) {
                popUpStyle = POPUP_OPTION_MODAL;
            } else {
                popUpStyle = POPUP_OPTION_INFO;
            }
            exportData = _queryConfig.exportData;
            exportTypes = _queryConfig.exportTypes;
            highlightFeatureOpacity = _queryConfig.locateOpacity;
            //空间查询按钮监听
            $spatialQueryPanel.find('.spatial-geo-icon').on('click', function () {
                spatialClickHandler($(this).data("type"));
            });

            //初始化默认模板
            $attrSpaceTreeQueryPanel.append(renderTpl($("#layersSpaceTreeTpl").html(), { layers: _queryConfig.layers }));
            $lyrSelector = $("#spaceTreeLayersSelect");
            $spaceTreeqSearchBtn = $("#spaceTreeqSearchBtn");

            //默认选中第一个图层
            if (_queryConfig.layers.length > 0) {
                changeActiveLayer(_queryConfig.layers[0].serviceId + "-" + _queryConfig.layers[0].layerIndex);
                ChosenSelect({
                    elem: '.chosen-select'
                });
                $(".layer-input-date").on('click', function () {
                    var id = $(this).attr("id");
                    laydate({
                        elem: "#" + id,
                        format: 'YYYY-MM-DD',
                        max: laydate.now(),
                        istime: false,
                        istoday: false,
                        choose: function (dates) {
                            var $dateInput = $($(this).attr('elem'));
                            var field = $dateInput.data("field");
                            var order = $dateInput.data("order");
                            //存储日期条件
                            if (!dateCondition[field]) {
                                dateCondition[field] = {};
                            }
                            if (order == 0) {
                                dateCondition[field].start = dates;
                            } else if (order == 1) {
                                dateCondition[field].end = dates;
                            }
                        }
                    });
                });
            }

            $(".point-select").on('change', function () {
                var fieldName = $(this).data("field");
                var order = $(this).data("order");
                //存储日期条件
                if (!pointCondition[fieldName]) {
                    pointCondition[fieldName] = {};
                }
                if (order == 0) {
                    pointCondition[fieldName].x = parseFloat($(this).val());
                } else if (order == 1) {
                    pointCondition[fieldName].y = parseFloat($(this).val());
                }
            });
            //改变range类型的选择值
            $(".range-select").on('change', function () {
                var fieldName = $(this).data("field");
                var opt = $(this).data("opt");
                if (!rangeCondition.hasOwnProperty(fieldName)) {
                    rangeCondition[fieldName] = {};
                }
                switch (opt) {
                    case 'from':
                        rangeCondition[fieldName].from = $(this).val();
                        break;
                    case 'to':
                        rangeCondition[fieldName].to = $(this).val();
                        break;
                }
            });

            //监听选择的图层变化时，动态的修改条件
            $lyrSelector.on('change', function () {
                dateCondition = {};
                rangeCondition = {};
                changeActiveLayer($(this).val());
            });

            //项目查询-----查询图层
            $spaceTreeqSearchBtn.on('click', function () {
                var xmbh = $("input[name='XMBH']").val() || '';
                var xmmc = $("input[name='XMMC']").val() || '';
                var xmdz = $("input[name='XMDZ']").val() || '';
                var sqlStr = '';
                $.ajax({
                    url: _url + "/msurveyplat-server/rest/v1.0/onemap/getDchyCgglXm",
                    //url: "http://192.168.50.60:8083/msurveyplat-server/rest/v1.0/onemap/getDchyCgglXm",
                    type: 'post',
                    async: false,
                    data: {
                        xmbh: xmbh,
                        xmmc: xmmc,
                        xmdz: xmdz
                    },
                    success: function (r) {
                        console.log('项目信息获取：-------------------', r);
                        if (r) {
                            r.forEach(function (v, i) {
                                if (i == r.length - 1) {
                                    sqlStr += " dchybh = '" + v.dchybh + "'";
                                    //sqlStr += " dchybh LIKE" + " '%20200428001001%'";
                                } else {
                                    sqlStr += " dchybh = '" + v.dchybh + "' or";
                                    //sqlStr += " dchybh LIKE" + " '%20200428001001%'" + " or";
                                }
                            })
                        } else {
                            layer.msg('未查询到结果', {
                                time: 1500
                            })
                        }

                    }
                });
                console.log('项目信息sqlStr：-------------------', sqlStr);
                var serviceId = $('#spaceTreeLayersSelect').val();
                $spaceTreeqSearchBtn.text('查询中 ...');
                $.each(_queryConfig.layers, function (i, n) {
                    if (serviceId === (n.serviceId + "-" + n.layerIndex)) {
                        var outFields = [];
                        var outFieldsStr = "";
                        var where = '';
                        var geometry = '';
                        $.each(n.returnFields, function (i1, n1) {
                            outFields.push(n1.name);
                            if (i1 == n.returnFields.length - 1)
                                outFieldsStr = outFieldsStr.concat(n1.name);
                            else
                                outFieldsStr = outFieldsStr.concat(n1.name).concat(",");
                        });
                        //从表单获取值组装where的条件
                        $.each($("#spaceTreelayerForm .query-val"), function (index, layerFormItem) {
                            var prefix = n.queryFields.prefix;
                            var operator = ' LIKE ';
                            var field;
                            //获取输入框输入的条件组成sql条件
                            if (layerFormItem.nodeName.toLowerCase() === 'input' || layerFormItem.nodeName.toLowerCase() === 'select') {
                                var tmp = arrayUtil.filter(n.queryFields.fields, function (item) {
                                    return item.name === layerFormItem.name;
                                });
                                field = tmp[0];
                                // if (esriLang.isDefined(field.operator)) {
                                //     operator = field.operator;
                                // }
                                if (layerFormItem.nodeName.toLowerCase() === 'input') {
                                    if (layerFormItem.value != undefined && layerFormItem.value != '') {
                                        if (where.length > 0 && index < $("#spaceTreelayerForm :input").length) {
                                            where += ' AND ';
                                        }
                                        if (prefix == '*') {
                                            where += " " + layerFormItem.name + " " + operator + " '" + layerFormItem.value + "'";
                                        } else {
                                            // where += " " + layerFormItem.name + " " + operator + " '" + prefix + layerFormItem.value + prefix + "'";
                                            where = sqlStr;
                                        }

                                    }
                                } else {
                                    var arr = chosenValsHash[layerFormItem.name];
                                    if (field.type === 'STRING' || field.type === 'string') {
                                        arr = arrayUtil.map(arr, function (item) {
                                            return "'" + item + "'";
                                        });
                                    }
                                    if (arr.length > 0) {
                                        if (where.length > 0) {
                                            where += ' AND ';
                                        }
                                        where += layerFormItem.name + ' in (' + arr.join(',') + ')';
                                    }
                                }
                            }
                        });
                        //单独组织日期条件
                        for (var i in dateCondition) {
                            if (i) {
                                if (dateCondition[i].start) {
                                    if (where.length > 0) {
                                        where += ' AND ';
                                    }
                                    where += "to_date('" + dateCondition[i].start + "','yyyy-mm-dd')" + " < " + i;
                                }
                                if (dateCondition[i].end) {
                                    if (where.length > 0) {
                                        where += ' AND ';
                                    }
                                    where += i + " < " + "to_date('" + dateCondition[i].end + "','yyyy-mm-dd')";
                                }
                            }
                        }
                        var size;
                        !n.queryPaging ? size = 100 : size = parseInt(($(window).height() - 280) / 65, 10);
                        for (var h in pointCondition) {
                            if (h !== undefined) {
                                var coordsSystem = JSON.stringify(_spatialReference);
                                geometry = JSON.stringify(pointCondition.POINT);
                                geometry = geometry.substring(1);
                                geometry = geometry.substring(0, geometry.length - 1);
                                geometry = "{\"type\":\"point\"," + geometry + ",\"spatialReference\":" + coordsSystem + "}";
                                var data1 = {
                                    layerUrl: getLayerUrl(n.layerUrl),
                                    where: "1=1",
                                    geometry: geometry,
                                    returnFields: outFieldsStr,
                                    page: 1,
                                    size: size
                                };
                                var allData1 = {
                                    layerUrl: getLayerUrl(n.layerUrl),
                                    where: "1=1",
                                    geometry: geometry,
                                    returnFields: outFieldsStr,
                                    page: 0
                                };
                                //和信息查询属性查询保持一致，利用后台进行分页
                                $.ajax({
                                    url: "/omp/map/query",
                                    data: data1,
                                    success: function (r) {
                                        parseQueryResult(r, data1, true);
                                        showPageTool("pageTool", r.totalPages, r.number, data1, true);//使用分页
                                    }
                                });
                                $.ajax({
                                    url: "/omp/map/query",
                                    data: allData1,
                                    success: function (t) {
                                        parseQueryAllResult(t, allData1);
                                    }
                                });
                            }
                        }
                        //组织range型的字段数据
                        for (var k in rangeCondition) {
                            if (k != undefined) {
                                var rangeWhere = [];
                                var obj = rangeCondition[k];
                                var rangeFrom = obj.from;
                                var rangeTo = obj.to;
                                var arr = arrayUtil.filter(currentQueryFields, function (item) {
                                    return item.name.equalsIgnoreCase(k);
                                });
                                var field = arr[0];
                                var dArr = arrayUtil.map(field.defaultValue, function (item) {
                                    return item.value;
                                });
                                var ft = field.type;
                                if (ft === 'STRING') {
                                    if (rangeTo === undefined) {
                                        rangeWhere.push(k + " like '" + rangeFrom + "%'");
                                        continue;
                                    }
                                    var si = dArr.indexOf(rangeFrom);
                                    var ei = dArr.indexOf(rangeTo);
                                    for (si; si <= ei; si++) {
                                        rangeWhere.push(k + " like '" + dArr[si] + "%'");
                                    }
                                }
                                if (rangeWhere.length > 0) {
                                    where += (where.length > 0 ? ' AND ' : '') + "( " + rangeWhere.join(" OR ") + " )";
                                }
                            }
                        }

                        if (where.length == 0) {
                            where = null;
                            $spaceTreeqSearchBtn.html('<i class="iconfont">&#xe602;</i>查询');
                            return false;
                        }
                        console.info("---属性查询条件----" + where);
                        //DPF添加

                        var data = {
                            layerUrl: getLayerUrl(n.layerUrl),
                            where: where,
                            returnFields: outFieldsStr,
                            page: 1,
                            size: size
                        };
                        var allData = {
                            layerUrl: getLayerUrl(n.layerUrl),
                            where: where,
                            returnFields: outFieldsStr,
                            page: 0
                        };
                        $.ajax({
                            url: "/omp/map/query",
                            data: data,
                            success: function (r) {
                                parseQueryResult(r, data, true);
                                showPageTool("pageTool", r.totalPages, r.number, data, true);//使用分页
                            }
                        });
                        $.ajax({
                            url: "/omp/map/query",
                            data: allData,
                            success: function (t) {
                                parseQueryAllResult(t, allData);
                            }
                        });
                        if (_queryConfig.filterAfterQuery) {
                            filterServiceId = serviceId.split("-")[0];
                            var layerDefinitions = [];
                            layerDefinitions[parseInt(n.layerIndex, 10)] = where;
                            MapUtils.getLayer(filterServiceId).setLayerDefinitions(layerDefinitions);
                            window.definitionExpression = {};
                            window.definitionExpression[serviceId] = [
                                {
                                    id: parseInt(n.layerIndex, 10),
                                    layerDefinition: {
                                        definitionExpression: where
                                    }
                                }
                            ];
                        }
                        where = null;
                        return false;
                    }
                });
            });

            //清空重置监听
            $("#spaceTreeqResetBtn").on('click', function () {
                var _selItem = $lyrSelector.val();
                $("#spaceTreelayerForm")[0].reset();
                $lyrSelector.val(_selItem);
                clearDefinitionExpression();
                dateCondition = {};
                rangeCondition = {};
            });


            if (queryType === "SpatialQuery") {
                $("#attributeQuery").removeClass("active");
                $("#attrSpaceTreeQueryPanelContent").removeClass("active");
                $("#attrSpaceTreeQueryPanel").removeClass("active");
                $("#spatialQuery").addClass("active");
                $("#spatialQueryPanelContent").addClass("active");
                $("#spatialQueryPanel").addClass("active");
                $(".nav-tabs").hide();
            }


            /**
         * DOM事件监听
         * switch开关
         */
            $switchBtn2 = $("#spaceTreeiSwitchBtn");
            $switchBtn2.on('click', function () {
                var $this = $(this);
                if ($this.hasClass("omp-switch-on")) {
                    $this.removeClass("omp-switch-on");
                    $this.find('em').text("关");
                    $this.attr('title', '打开功能');
                    if (_mapClickHanlder2) _mapClickHanlder2.remove();
                    _map.setMapCursor('default');
                } else {
                    $this.addClass("omp-switch-on");
                    $this.find('em').text("开");
                    $this.attr('title', '关闭功能');
                    _prepare2();
                }
            });
        }
        var currentQueryFields = [];

        /***
         * 改变当前查询图层
         * @param sid
         */
        function changeActiveLayer(sid) {
            showFooter = false;
            $.each(_queryConfig.layers, function (index, layer) {
                var tid = layer.serviceId + "-" + layer.layerIndex;
                if (sid === tid) {
                    if (layer.hasOwnProperty("showFooter")) showFooter = layer.showFooter;
                    for (var i in layer.queryFields.fields) {
                        var field = layer.queryFields.fields[i];
                        if (field.hasOwnProperty("defaultValue")) {
                            //支持key-val 数组或 'xx,yy,zz'
                            var dv = field.defaultValue;
                            if (typeof dv === 'string') {
                                var arr = field.defaultValue.split(",");
                                if (arr.length > 1) {
                                    field.isArray = field.hasOwnProperty("range") ? false : true;
                                    if (field.hasOwnProperty("range")) {
                                        if (!rangeCondition[field.name]) {
                                            rangeCondition[field.name] = {};
                                        }
                                        rangeCondition[field.name].from = arr[0];
                                        rangeCondition[field.name].to = arr[arr.length - 1];
                                    }
                                    field.defaultValue = arrayUtil.map(arr, function (item) {
                                        return { key: item, value: item };
                                    });
                                }
                            } else if (lang.isArray(dv)) {
                                field.isArray = field.hasOwnProperty("range") ? false : true;
                            }
                        }
                    }
                    currentQueryFields = layer.queryFields.fields;
                    $("#spaceTreelayerCondition").empty();
                    $("#spaceTreelayerCondition").append(renderTpl($("#spaceTreelayersSearchTpl").html(), { fields: currentQueryFields }));
                    initQueryChosenSelect();
                    return false;
                }
            });
        }

        //存储多个查询字段多个默认值的对象
        var chosenValsHash = {};

        /***
         * 初始化 多选框
         */
        function initQueryChosenSelect() {
            chosenValsHash = {};
            ChosenSelect({
                elem: '.q-chosen-select'
            });
            $("select.q-chosen-select").on('change', function (evt, params) {
                var fieldName = $(evt.currentTarget).attr("name");
                var chosenVals = chosenValsHash[fieldName] || [];
                if (params.hasOwnProperty('selected')) {
                    if (params.selected.indexOf(',') > 0) {
                        var arr = params.selected.split(',');
                        arrayUtil.forEach(arr, function (item) {
                            chosenVals.push(item);
                        })
                    } else {
                        chosenVals.push(params.selected);
                    }
                    if (!chosenValsHash.hasOwnProperty(fieldName)) {
                        chosenValsHash[fieldName] = chosenVals;
                    }
                } else if (params.hasOwnProperty('deselected')) {
                    if (params.deselected.indexOf(',') > 0) {
                        var arr = params.deselected.split(',');
                        chosenVals = arrayUtil.filter(chosenVals, function (item) {
                            return arr.indexOf(item) < 0;
                        });
                    } else {
                        chosenVals = arrayUtil.filter(chosenVals, function (item) {
                            return item != params.deselected;
                        });
                    }
                    chosenValsHash[fieldName] = chosenVals;
                }
            });
        }

        /***
         * 空间查询handler
         * @param evt
         */
        function spatialClickHandler(type) {
            if (drawTool != undefined) drawTool.deactivate();
            if (drawHandler != undefined) drawHandler.remove();
            drawTool = drawTool ? drawTool : new Draw(_map);
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
                default:
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
            //获取选择的图层id
            var serviceId = $('#spaceTreeLayersSelect').val();
            $.each(_queryConfig.layers, function (i, n) {
                if (serviceId == (n.serviceId + "-" + n.layerIndex)) {
                    var outFields = [];
                    var outFieldsStr = "";
                    $.each(n.returnFields, function (i1, n1) {
                        outFields.push(n1.name);
                        if (i1 == n.returnFields.length - 1)
                            outFieldsStr = outFieldsStr.concat(n1.name);
                        else
                            outFieldsStr = outFieldsStr.concat(n1.name).concat(",");
                    });
                    var where = "1=1";
                    var geometry = JSON.stringify(evt.geometry);
                    var layerUrl = getLayerUrl(n.layerUrl);
                    var data = {
                        layerUrl: getLayerUrl(n.layerUrl),
                        where: where,
                        geometry: geometry,
                        returnFields: outFieldsStr,
                        page: 1,
                        size: parseInt(($(window).height() - 280) / 65, 10)
                    };
                    var allData = {
                        layerUrl: getLayerUrl(n.layerUrl),
                        where: where,
                        geometry: geometry,
                        returnFields: outFieldsStr,
                        page: 0
                    };
                    //和信息查询属性查询保持一致，利用后台进行分页
                    $.ajax({
                        url: "/omp/map/query",
                        data: data,
                        success: function (r) {
                            parseQueryResult(r, data);
                            showPageTool("pageTool", r.totalPages, r.number, data);//使用分页
                        }
                    });
                    $.ajax({
                        url: "/omp/map/query",
                        data: allData,
                        success: function (t) {
                            parseQueryAllResult(t, allData);
                        }
                    });
                }
            });
        }

        /**
         *  图层查询后回调函数处理结果(信息查询)
         */
        function parseQueryResult(r, data, isAttrQuery) {
            console.log('r:-----------：', r)
            console.log('data:-----------：', data)
            console.log('isAttrQuery:-----------：', isAttrQuery)

            $spaceTreeqSearchBtn.html('<i class="iconfont">&#xe602;</i>查询');
            //hasOwnProperty()方法返回一个布尔值，判断对象是否包含特定的自身（非继承）属性
            if (r.hasOwnProperty("success")) {
                layer.open({
                    icon: 0,
                    title: '提示',
                    content: r.msg
                });
                return;
            }
            var rp = {};
            var totalSize = 0;
            if (r.hasOwnProperty("content")) {
                if (r.totalElements == 0) {
                    layer.msg('未查询到结果');
                    return;
                }
                rp.features = r.content;
                totalSize = r.totalElements;
            } else {
                rp = r;
                totalSize = rp.features.length;
            }
            if (rp == undefined || rp.features == undefined) {
                layer.msg("查询结果为空！");
            } else {
                var attrs = [];//存放要进行展示的属性集合
                var layerObj = null; //选定的图层对象
                //遍历
                $.each(_queryConfig.layers, function (n, layerItem) {
                    if ($lyrSelector.val() == (layerItem.serviceId + "-" + layerItem.layerIndex)) {
                        layerObj = layerItem;
                        var tf = layerItem.titleField;
                        var sf = getSubTitleField(layerItem);
                        var rf = layerItem.returnFields;
                        var graphics = [];
                        $.each(rp.features, function (i, feature) {
                            for (var j in rf) {
                                if (rf[j].type == "DATE" && feature.attributes[rf[j].name]) {
                                    feature.attributes[rf[j].name] = new Date(feature.attributes[rf[j].name]).toLocaleString();
                                }
                            }
                            var dchybh = feature.attributes['DCHYBH'] || '空';
                            var titleText = esriLang.substitute({
                                alias: tf.alias,
                                value: feature.attributes[tf.name] || '空'
                            }, '${alias}:${value}');
                            var subTitleText = esriLang.isDefined(sf) ? esriLang.substitute({
                                alias: sf.alias,
                                value: feature.attributes[sf.name] || '空'
                            }, '${alias}:${value}') : '';
                            var g = rp.features[i];
                            var geo = g.geometry;
                            if (!geo.spatialReference) {
                                geo.spatialReference = _map.spatialReference;
                            }
                            var graphic = new Graphic(g);
                            graphics.push(graphic);
                            attrs.push({
                                title: titleText,
                                subtitle: subTitleText,
                                graphic: graphic,
                                dchybh: dchybh
                            });
                        });
                    }
                });
                //切换面板状态 隐藏查询面板
                changeState(STATE_RESULT);
                //渲染模板显示结果
                var tpl;
                if (isAttrQuery) {
                    tpl = $("#attrSpaceTreeTpl").html();
                } else {
                    tpl = $("#spaceTreeSpatialResultTpl").html();
                }

                console.log('查询数据：', attrs);
                // console.log('查询数据exportData：',exportData);   
                //共享数据
                // geoDataStore.push(GeoDataStore.SK_QUERY, attrs);
                var listDataRenderer;
                if (isAttrQuery) {
                    $attrResultPanel.empty();
                    $attrResultPanel.append(Mustache.render(tpl, { result: attrs, size: totalSize, exportData: exportData }));
                    listDataRenderer = new ListDataRenderer({
                        renderTo: $('#spaceTreeAttr_result'),
                        type: exportData ? "export" : "projectLoc",
                        map: map.map(),
                        renderData: attrs
                    });
                } else {
                    $spatialResultPanel.empty();
                    $spatialResultPanel.append(Mustache.render(tpl, {
                        result: attrs,
                        size: totalSize,
                        exportData: exportData
                    }));
                    listDataRenderer = new ListDataRenderer({
                        renderTo: $('#spaceTreeSpatial_result'),
                        type: exportData ? "export" : "loc",
                        map: map.map(),
                        renderData: attrs
                    });
                }
                // $qResultPanel.empty();
                // $qResultPanel.append(Mustache.render(tpl, {result: attrs, size: totalSize,exportData:exportData}));
                listDataRenderer.on('location', function (data) {
                    doLocation(data.graphic, layerObj);
                    setTimeout(function () {
                        _map.graphics.clear();
                        if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").clear();
                    }, 800)

                });
                listDataRenderer.on("export", function (data) {
                    var obj = data.graphic;
                    if (obj != null) {
                        var featureSet = new FeatureSet();
                        featureSet.features = [obj];
                        var exportSelTpl = $("#export-select-tpl").html();
                        var template = Handlebars.compile(exportSelTpl);
                        var content = template({ types: parseTypesArray });

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
                listDataRenderer.render();
                //结果界面可滚动
                if (isAttrQuery) {
                    $("#spaceTreeAttr_result").slimScroll({
                        height: $(window).height() - 170,
                        width: 300,
                        railVisible: true,
                        railColor: '#333',
                        railOpacity: .2,
                        railDraggable: true
                    });
                } else {
                    $("#spaceTreeSpatial_result").slimScroll({
                        height: $(window).height() - 270,
                        width: 300,
                        railVisible: true,
                        railColor: '#333',
                        railOpacity: .2,
                        railDraggable: true
                    });
                }


                //返回查询界面

                $("#spaceTreeqReturnBtn").on('click', function () {
                    changeState(STATE_QUERY);
                });
                // $("#qAddBtn").on('click', function () {
                //     geoDataStore.push(GeoDataStore.SK_QUERY, allData);
                // });
                $("#qExportBtn").on('click', function () {
                    data.page = -1;
                    $.ajax({
                        url: "/omp/map/query",
                        data: data,
                        success: function (r) {
                            var features = $.parseJSON(r).features;
                            var data = [];
                            // var keys = [];
                            for (var key in features[0].attributes) {
                                keys.push(key);
                            }
                            data.push(keys);
                            arrayUtil.forEach(features, function (item) {
                                var att = item.attributes;
                                var str = [];
                                arrayUtil.forEach(keys, function (k) {
                                    str.push(att[k]);
                                });
                                data.push(str);
                            });
                            geometryIO._exportAttInfo(data);
                        }
                    });
                })
            }
        }

        /**
         *  图层查询后回调函数处理结果
         */
        function parseQueryAllResult(r, data, isAttrQuery) {
            var map = JSON.parse(r);
            var rp = map;
            var attrs = [];//存放要进行展示的属性集合
            var layerObj = null; //选定的图层对象
            $.each(_queryConfig.layers, function (n, layerItem) {
                if ($lyrSelector.val() == (layerItem.serviceId + "-" + layerItem.layerIndex)) {
                    layerObj = layerItem;
                    var tf = layerItem.titleField;
                    var sf = getSubTitleField(layerItem);
                    var rf = layerItem.returnFields;
                    var graphics = [];
                    $.each(rp.features, function (i, feature) {
                        for (var j in rf) {
                            if (rf[j].type == "DATE" && feature.attributes[rf[j].name]) {
                                feature.attributes[rf[j].name] = new Date(feature.attributes[rf[j].name]).toLocaleString();
                            }
                        }
                        var titleText = esriLang.substitute({
                            alias: tf.alias,
                            value: feature.attributes[tf.name] || '空'
                        }, '${alias}:${value}');
                        var subTitleText = esriLang.isDefined(sf) ? esriLang.substitute({
                            alias: sf.alias,
                            value: feature.attributes[sf.name] || '空'
                        }, '${alias}:${value}') : '';
                        var g = rp.features[i];
                        var geo = g.geometry;
                        if (!geo.spatialReference) {
                            geo.spatialReference = _map.spatialReference;
                        }
                        var graphic = new Graphic(g);
                        graphics.push(graphic);
                        attrs.push({
                            title: titleText,
                            subtitle: subTitleText,
                            graphic: graphic
                        });
                    });
                    geoDataStore.push(GeoDataStore.SK_QUERY, graphics);
                }
            });
        }

        /**
         * 设置二级标题字段
         * 选取非标题字段的第一个字段
         * @param lyr
         */
        function getSubTitleField(lyr) {
            var tf = lyr.titleField;
            var rf = lyr.returnFields;
            for (var i = 0, l = rf.length; i < l; i++) {
                var item = rf[i];
                if (esriLang.isDefined(item) && item.name != tf.name) {
                    return item;
                }
            }
            return undefined;
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
                    r.push({ alias: getAlias(item), value: item });
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

        /***
         * 处理图层url
         * @param lyrUrl
         * @returns {string}
         */
        function getLayerUrl(lyrUrl) {
            var sr = _map.spatialReference.wkid;
            var realUrl;
            if (lyrUrl.startWith("http://")) {
                realUrl = sr != undefined ? lyrUrl.concat("/query").concat("?outSR=" + sr) : lyrUrl.concat("/query");
            } else {
                realUrl = sr != undefined ? lyrUrl.replace("/oms", omsUrl).concat("/query").concat("?outSR=" + sr) : lyrUrl.replace("/oms", omsUrl).concat("/query");
            }
            return encodeURI(realUrl);
        }

        /***
         * 定位图形
         * @param g
         */
        function doLocation(g, lyr) {
            var geo = g.geometry;
            var geoType = geo.type;
            var geoCenter;
            switch (geoType) {
                case 'point':
                    geoCenter = geo;
                    break;
                case 'polyline':
                case 'polygon':
                    geoCenter = geo.getExtent().getCenter();
                    break;
            }
            geoCenter = lang.mixin(geoCenter, { spatialReference: _map.spatialReference });
            var graphic = new Graphic(lang.mixin(geo, { spatialReference: _map.spatialReference }));
            graphic.setAttributes(g.attributes);
            MapUtils.setMap(map.map());
            if (_queryConfig.type == "noColor") {
                MapUtils.highlightFeatures([graphic], false, 0, "graphics4Print", _queryConfig.type);
            } else {
                MapUtils.highlightFeatures([graphic], false, highlightFeatureOpacity, "graphics4Print");
            }
            if (popUpStyle == POPUP_OPTION_INFO) {
               
                if (geoType == 'point') {
                    MapUtils.locatePoint(graphic);
                } else {
                    MapUtils.locateFeatures([graphic]);
                }
            } else if (popUpStyle == POPUP_OPTION_MODAL) {
                MapUtils.locateFeatures([graphic]);
            }

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
         * @param graphic
         * @param returnFields
         * @returns {*}
         */
        function getInfoContent(graphic, returnFields) {
            var data = [];
            var tmpl = $("#infoContentTpl").html();
            var showData = graphic.attributes;
            for (var i in showData) {
                for (var j = 0; j < returnFields.length; j++) {
                    if (i.equalsIgnoreCase(returnFields[j].name)) {
                        data.push({ key: returnFields[j].alias, value: showData[i] });
                    }
                }
            }
            return Mustache.render(tmpl, { data: data });
        }

        /***
         * 切换当前面板状态
         * @param s
         */
        function changeState(s) {
            _currentState = s;
            switch (_currentState) {
                case STATE_QUERY:
                    // $qResultPanel.empty();
                    if ($("#attrSpaceTreeQueryPanelContent").hasClass("active")) {
                        $attrSpaceTreeQueryPanel.show();
                        $attrResultPanel.empty();
                    } else if ($("#spatialQueryPanelContent").hasClass("active")) {
                        $spatialQueryPanel.show();
                        $spatialResultPanel.empty();
                    }
                    if (mapPopup.isShowing) mapPopup.closePopup();
                    _map.graphics.clear();
                    if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").clear();
                    clearDefinitionExpression();
                    break;
                case STATE_RESULT:
                    if ($("#attrSpaceTreeQueryPanelContent").hasClass("active")) {
                        $attrSpaceTreeQueryPanel.hide();
                    } else if ($("#spatialQueryPanelContent").hasClass("active")) {
                        $spatialQueryPanel.hide();
                    }
                    break;
            }
        }

        /***
         * 显示分页工具
         * @param selector
         * @param pageCount
         * @param currPage
         * @param callback
         */
        function showPageTool(selector, pageCount, currPage, data, isAttr) {
            if (typeof selector === "string")
                selector = $("#" + selector);
            selector.show();
            laypage({
                cont: selector,
                pages: pageCount,
                groups: 3,
                curr: currPage,
                skip: false,
                skin: 'molv',
                first: 1,
                last: pageCount,
                prev: false,
                next: false,
                jump: function (obj, first) {
                    if (!first) {
                        data.page = obj.curr;
                        $.ajax({
                            url: "/omp/map/query",
                            data: data,
                            success: function (r) {
                                parseQueryResult(r, data, isAttr);
                                showPageTool("pageTool", r.totalPages, r.number, data, isAttr);
                            }
                        });
                    }
                }
            });
        }


        /**
         * 清除图层过滤
         */
        function clearDefinitionExpression() {
            if (filterServiceId) {
                MapUtils.getLayer(filterServiceId).setLayerDefinitions([]);
                filterServiceId = null;
                window.definitionExpression = null;
            }
        }

        /***
         *
         * @param tpl
         * @param data
         * @returns {*}
         */
        function renderTpl(tpl, data) {
            var templ = Handlebars.compile(tpl);
            return templ(data);
        }


        /**
         * 添加地图监听
         * @private
         */
        function _prepare2() {
            _mapClickHanlder2 = _map.on('click', doIdentify2);
            _map.setMapCursor('crosshair');
            MapUtils.setMap(_map);
        }

        /**
         * 清除当前的状态 以及地图监听
         * @private
         */
        function _clear2() {
            if (_mapClickHanlder2) _mapClickHanlder2.remove();
            // _map.graphics.clear();
            layer.closeAll();
            if (mapPopup.isShowing) mapPopup.closePopup();
            $switchBtn2.removeClass("omp-switch-on");
            $switchBtn2.attr('title', '打开功能');
            $switchBtn2.find('em').text('关');
        }
        /**
                 * do identify
                 * @param evt
                 */
        function doIdentify2(event) {
            if (_identifyLayers.length == 0) {
                if (_queryConfig.layers.length === 0) {
                    layer.alert('未配置任何属性识别图层!', { btn: [], shadeClose: true });
                } else {
                    layer.msg("待识别的服务尚未加载到地图中！", { icon: 0 });
                }
                return;
            }
            _lastClick = event.mapPoint;
            _identifyCount = _identifyLayers.length;
            layer.closeAll();
            layer.msg('属性识别中..', { time: 10000 });
            _identifyResults = [];
            _listData = [];
            _resultObj = [];
            _map.graphics.clear();
            _map.infoWindow.hide();

            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
            identifyParams.width = _map.width;
            identifyParams.height = _map.height;
            identifyParams.geometry = event.mapPoint;
            identifyParams.mapExtent = _map.extent;

            for (var i in _identifyLayers) {
                var tmp = _identifyLayers[i];
                if (_map.getLayer(tmp.serviceId) == undefined) {
                    _identifyCount -= 1;
                    continue;
                }
                var identifyTask = new IdentifyTask(_map.getLayer(tmp.serviceId).url);
                on(identifyTask, 'error', _identifyResultError);
                identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult2, tmp));
            }
        }

        /**
         *
         * @param error
         * @private
         */
        function _identifyResultError(error) {
            console.error(error.message);
            _map.setMapCursor('default');
        }
        /**
         * 处理识别结果
         * @param event
         * @param result
         * @private
         */
        var slbh = '';
        var xmid = '';
        var xmbh = '';
        function _handleIdentifyResult2(token, result) {
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
                        _data.push({ key: tf.alias, value: attr[tf.name] || attr[tf.alias] });
                        for (var j in rf) {
                            var f = rf[j];
                            if (f.name != tf.name && _data.length == 1)
                                _data.push({ key: f.alias || f.name, value: attr[f.name] || attr[f.alias] });
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
                    doLocation(_resultObj.规划竣工范围[0].feature, token);
                    setTimeout(function () {
                        _map.graphics.clear();
                        if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").clear();
                    }, 800);

                    var dchybh = _resultObj.规划竣工范围[0].feature.attributes.多测合一编号;
                    $('.project-info .finished-box form').empty();
                    $('.project-info').find('.xmbh').text('');
                    $('.project-info').find('.xmmc').text('');
                    $('.project-info').find('.xmdz').text('');
                    $('.project-info').find('.jsdw').text('');
                    $.ajax({
                        url: _url + '/msurveyplat-server/rest/v1.0/onemap/xmxx/'+dchybh,
                        //url: "http://192.168.50.60:8083/msurveyplat-server/rest/v1.0/onemap/xmxx/" + dchybh,
                        type: 'post',
                        async: false,
                        success: function (res) {
                            var xm = res.dchyCgglXmDO;
                            slbh = xm.slbh;
                            xmid = xm.xmid;
                            xmbh = xm.chxmbh;
                            $('.project-info').find('.xmbh').text(xm.chxmbh);
                            $('.project-info').find('.xmmc').text(xm.xmmc);
                            $('.project-info').find('.xmdz').text(xm.xmdz);
                            $('.project-info').find('.jsdw').text(xm.jsdw);

                            //渲染已完成事项
                            layui.use(['form'], function () {
                                var form = layui.form;
                                res.surveyItemVoList.forEach(function (v) {
                                    var isChecked = v.sfwcsx ? 'checked' : 'disabled';
                                    var className = v.sfwcsx ? 'checked-span' : 'disabled-span';
                                    var html = '<p class="finished-item"><input type="checkbox" name="" disabled lay-skin="primary" ' + isChecked + '/><span class="' + className + '" data="' + v.slbh + '">' + v.chsxmc + '</span></p>';
                                    $('.project-info .finished-box form').append(html);
                                })
                                form.render('checkbox');
                                $('.project-info').show();
                            })
                        }
                    })

                } else {
                    layer.msg('无信息', {
                        time: 1500
                    });
                }
            }
        }
        //关闭项目信息弹出框
        $('.project-info-close-btn').click(function () {
            $('.project-info').hide();
        })
        //打开项目一棵树详情
        $('.project-tree-btn').unbind('click');
        $('.project-tree-btn').bind('click', function () {
            window.open(root + '/static/js/map/template/dchyxxglpt/dchyxmsmx.html?slbh=' + slbh + '&xmid=' + xmid + '&xmbh=' + xmbh);
        });
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
        return query;
    });
