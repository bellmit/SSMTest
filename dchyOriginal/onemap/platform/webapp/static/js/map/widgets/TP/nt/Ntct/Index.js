/***
 * 南通成图功能
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/topic",
    "map/utils/MapUtils",
    "esri/lang",
    "esri/geometry/Polygon",
    "esri/tasks/IdentifyParameters",
    "esri/tasks/IdentifyTask",
    "layer",
    "handlebars",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/BaseAnalysis",
    "text!static/js/map/template/analysis/analysis-list-item.html"
], function (declare, arrayUtils, lang, topic, MapUtils, EsriLang, Polygon, IdentifyParameters, IdentifyTask,
             layer, Handlebars, BaseWidget, JsonConverters, BaseAnalysis, ListItemTpl) {
    var _map, _id, _label, _analysisConfig, ntctJctbAnalysis, subscribe, submitPart;
    var submitGraphics = [];//保存提交图形
    var _mapClickHanlder = null;
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
            _id = this.id;
            _label = this.getLabel();
            _analysisConfig = this.getConfig();
            _init();
            _addListener();
            MapUtils.setMap(_map);
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
            _resume();
        },

        /**
         *
         */
        onDestroy: function () {
            ntctJctbAnalysis.clear();
        }
    });

    //监测图斑分析自定义选项
    var analysisOption = {};
    var $optContainer, $listContainer, $clearBtn;

    /**
     * 初始化
     * @private
     */
    function _init() {
        Handlebars.registerHelper('existSrc', function (context, options) {
            if (context != undefined || context != "")
                return new Handlebars.SafeString("<div class='meta'>来源:&nbsp;" + context + "</div>");
        });

        ntctJctbAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_JCTB, _map);

        if (!EsriLang.isDefined(_analysisConfig.analysis)) {
            layer.msg("分析图层尚未配置!", {icon: 0});
            return;
        }

        var analysisPart = _analysisConfig.analysis;
        submitPart = _analysisConfig.editLayer;
        ntctJctbAnalysis.setDataSource(_analysisConfig.dataSource);
        ntctJctbAnalysis.setAnalysisLyrs(analysisPart.layers);
        analysisOption.year = analysisPart.year;
        analysisOption.unit = analysisPart.unit;
        analysisOption.template = _analysisConfig.ftl;

        $optContainer = $("#ntctJctbAnalysis");
        $clearBtn = $optContainer.find('a[data-opt="clear"]');
        $listContainer = $("#ntctJctbAnalysisList");
        listenerFileInput();
        //获取可删除图层
        _identifyLayers = _analysisConfig.deleteLayers;
    }

    /**
     * 添加监听
     * @private
     */
    function _addListener() {
        $optContainer.on('click', 'a', function () {
            var opt = $(this).data("opt");
            switch (opt) {
                case 'draw':
                    ntctJctbAnalysis.draw('polygon', true).then(function (obj) {
                        if (obj != undefined) {
                            _appendList(obj);
                        }
                        $clearBtn.show();
                    });
                    break;
                case 'analysis':
                    ntctJctbAnalysis.setAnalysisGeometry();
                    try {
                        ntctJctbAnalysis.doAnalysis(analysisOption);
                    } catch (e) {
                        layer.msg(e.message);
                    }
                    break;
                case 'clear':
                    _clear();
                    break;
                case 'submit':
                    if (submitGraphics.length > 0) {
                        selectSubmitLayer();
                    } else {
                        layer.msg("未发现可提交图形！");
                    }
                    break;
                case 'delete':
                    deleteGraphic();
                    break;
                default :
                    break;
            }
        });
    }


    /**
     * 导入监听
     */
    function listenerFileInput() {
        $('#analysis-file-input-ntctJctbAnalysis').off('change');
        $('#analysis-file-input-ntctJctbAnalysis').on('change', function () {
            ntctJctbAnalysis.importFile($('#analysis-file-input-ntctJctbAnalysis'));
        });
        setTimeout(function () {
            listenerFileInput();
        }, 2000);
    }

    /***
     * render tpl
     * @param tpl
     * @param data
     */
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }


    /**
     * 添加要素
     * @param obj
     * @private
     */
    function _appendList(obj) {
        if (obj.graphic)
            if (obj.src === "手绘") {
                var polygonTmp = new Polygon(obj.graphic.geometry);
                var isSelfIntersect = polygonTmp.isSelfIntersecting(polygonTmp);
                if (isSelfIntersect) {
                    layer.msg("所绘制图形自相交 请重新绘制!");
                    return;
                } else {
                    submitGraphics.push(obj.graphic);
                }
            } else {
                submitGraphics.push(obj.graphic);
            }
        $listContainer.append(renderTpl(ListItemTpl, obj));
        var scrollHeight = $(window).height() - 400;
        $listContainer.slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        $clearBtn.show();
        $("#ntctJctbAnalysisList .a-r-btn").unbind().on('click', function () {
            var t = $(this).data("type");
            var id = $(this).data("id");
            var isContain = false;
            var gra = null;
            ntctJctbAnalysis.findGraById(id).then(function (g) {
                isContain = true;
                gra = g;
            });
            switch (t) {
                case 'location':
                    _map.setExtent(gra.geometry.getExtent().expand(2.5));
                    break;
                case 'analysis':
                    ntctJctbAnalysis.setAnalysisGeometry(null);
                    var geometry = JsonConverters.toGeoJson(gra);
                    ntctJctbAnalysis.setAnalysisGeometry(JSON.stringify(geometry));
                    try {
                        ntctJctbAnalysis.doAnalysis(analysisOption);
                    } catch (e) {
                        layer.msg(e.message);
                    }
                    break;
            }
        });
    }

    /**
     * 切换当前widget后 移除监听等
     * @private
     */
    function _pause() {
        if (subscribe != null && subscribe != undefined)
            subscribe.remove();
    }

    /**
     *导入后返回
     * @private
     */
    function _resume() {
        subscribe = topic.subscribe(BaseAnalysis.EVENT_QUERY_RESULT, function (data) {
            _appendList(data);
        });
    }

    /**
     * 选择成图图层
     */
    function selectSubmitLayer() {
        var tpl = Handlebars.compile($("#ntctLayerSelectTpl").html());
        layer.open({
            type: 1,
            title: "选择上图图层",
            area: ['300px', '150px'],
            content: tpl(_analysisConfig),
            btn: ["确定"],
            btn1: function () {
                var layerName = $("#ntctLayerSelect").val();
                var $option = $("#ntctLayerSelect option:selected").eq(0);
                var serviceId = $option.attr("data-serviceid");
                submitGeometry(layerName, serviceId);
            }
        });
    }

    /**
     * 提交图形
     * @param layerName
     * @param serviceId
     */
    function submitGeometry(layerName, serviceId) {
        var geometry = JsonConverters.toGeoJson({features: submitGraphics});
        var indexLoad = layer.load(2, {time: 5 * 1000});
        $.ajax({
            url: root + "/geometryService/rest/insert",
            method: "post",
            async: false,
            data: {
                layerName: layerName,
                geometry: JSON.stringify(geometry),
                check: false,
                dataSource: _analysisConfig.dataSource
            },
            success: function (r) {
                MapUtils.getLayer(serviceId).refresh();
                layer.close(indexLoad);
                if (!r.hasOwnProperty("success")) {
                    _clear();
                    layer.msg("提交完毕！", {icon: 1, time: 1000});
                } else {
                    layer.msg(r.msg);
                }

            }
        });
    }

    /**
     * clear
     * @private
     */
    function _clear() {
        ntctJctbAnalysis.clear().then(lang.hitch(this, function () {
            $clearBtn.hide();
            $listContainer.empty();
        }));
        submitGraphics = [];
        var fileInput = document.getElementById('analysis-file-input-ntctJctbAnalysis');
        //清除fileInput内容
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
        clearIdentify();
    }

    /**
     * 删除图形
     */
    function deleteGraphic() {
        _mapClickHanlder = _map.on('click', doIdentify);
        _map.setMapCursor('crosshair');
        MapUtils.setMap(_map);
    }

    var _identifyLayers, _identifyCount, _identifyResults, _listData, _resultObj, identifyPos;

    /**
     * 识别图形
     * @param event
     */
    function doIdentify(event) {
        _identifyCount = _identifyLayers.length;
        layer.closeAll();
        layer.msg('图形识别中..', {time: 5000});
        _identifyResults = [];
        _listData = [];
        _resultObj = [];
        _map.graphics.clear();
        _map.infoWindow.hide();

        var identifyParams = new IdentifyParameters();
        identifyParams.tolerance = 3;
        identifyParams.returnGeometry = true;
        identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_ALL;
        identifyParams.width = _map.width;
        identifyParams.height = _map.height;
        identifyPos = event.mapPoint;
        identifyParams.geometry = event.mapPoint;
        identifyParams.mapExtent = _map.extent;

        for (var i in _identifyLayers) {
            var tmp = _identifyLayers[i];
            if (tmp.url == undefined || tmp.url == '') {
                layer.msg('图形服务地址未配置', {time: 3000});
                return;
            }
            var identifyTask = new IdentifyTask(tmp.url);
            identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult, tmp));
        }
    }

    /**
     * 识别后处理
     * @param token
     * @param result
     * @private
     */
    function _handleIdentifyResult(token, result) {
        if ((result != null && result.length > 0) && _identifyCount > 0) {
            if (_identifyResults.length > 0)
                _identifyResults = [];
            _identifyResults = _identifyResults.concat(result);
            arrayUtils.forEach(_identifyResults, function (item) {
                if (token.layerName === item.layerName) {    //过滤图层
                    var tmp = {};
                    tmp.layerName = item.layerName;
                    tmp.layerId = item.layerId;
                    tmp.feature = item.feature;
                    var attr = item.feature.attributes;
                    tmp.objectid = attr.OBJECTID;
                    var rf = token.returnFields;
                    var _data = [];
                    for (var j in rf) {
                        var f = rf[j];
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
            clearIdentify();
            if (!isEmpty(_resultObj)) {
                var identifyData = [];
                for (var name in _resultObj) {
                    var data = {};
                    var delateLayer = arrayUtils.filter(_identifyLayers, function (item) {
                        return item.layerName == name;
                    })[0];
                    data.layerName = _resultObj[name][0].layerName;
                    data.objectid = _resultObj[name][0].objectid;
                    data.title = _resultObj[name][0].feature.attributes[delateLayer.titleField.alias];
                    data.serviceid = delateLayer.serviceId;
                    identifyData.push(data);
                }
                var identifyResult = {};
                identifyResult.list = identifyData;
                console.log(identifyResult);
                //打开删除弹窗
                doDelete(identifyResult);


            } else {
                layer.msg("未查询到图形！", {time: 1000});
            }
        }
    }


    /**
     * 判断对象是否为空
     * @param obj
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

    /**
     * 清除状态
     * @private
     */
    function clearIdentify() {
        if (_mapClickHanlder)_mapClickHanlder.remove();
        _map.graphics.clear();
        _map.setMapCursor('default');
        layer.closeAll();
    }

    /**
     * 打开删除弹窗
     * @param identifyResult
     */
    function doDelete(identifyResult) {
        var tpl = Handlebars.compile($("#ntctDeleteLayerSelectTpl").html());
        layer.open({
            type: 1,
            title: "选择删除图层",
            area: ['300px', '150px'],
            content: tpl(identifyResult),
            btn: ["确定"],
            btn1: function () {
                var objectid = $("#ntctDeleteLayerSelect").val();
                var $option = $("#ntctDeleteLayerSelect option:selected").eq(0);
                var layerName = $option.attr("data-layerName");
                var title = $option.attr("data-title");
                var serviceId = $option.attr("data-serviceid");
                var tip = "确定要删除【" + layerName + "】中名称为：【" + title + "】的图形吗？";
                layer.confirm(tip, {icon: 3, title: "提示"}, function (index) {
                    var indexLoad = layer.load(2, {time: 5 * 1000});
                    $.ajax({
                        url: root + "/geometryService/rest/delete",
                        data: {
                            layerName: layerName,
                            primaryKey: objectid,
                            dataSource: _analysisConfig.dataSource
                        },
                        success: function (r) {
                            layer.close(index);
                            layer.close(indexLoad);
                            MapUtils.getLayer(serviceId).refresh();
                            if (r.result)
                                layer.msg("删除成功！");
                        }
                    });
                })
            }
        });
    }

    return me;
});