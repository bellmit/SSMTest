/**
 * Created by user on 2016-06-19.
 */
define(["esri/graphic",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/topic",
        "dojo/json",
        "dojox/uuid/generateRandomUuid",
        "map/core/BaseWidget",
        "map/core/BaseAnalysis",
        "map/core/JsonConverters",
        "map/core/GeoDataStore",
        "handlebars",
        "esri/lang",
        "map/utils/MapUtils",
        "static/thirdparty/multi-select/multiple-select.min",
        "css!static/thirdparty/multi-select/multiple-select.css"],
    function (Graphic, declare, lang, arrayUtil, topic, dojoJSON, RandomUuid, BaseWidget, BaseAnalysis, JsonConverters, GeoDataStore, Handlebars,
              esriLang, MapUtils) {

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
                _drceiveConfig = this.getConfig();
                _init();
                _addListeners();
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
                dtReAnalysis.clear();
            }
        });

        var _map, _drceiveConfig, dtReAnalysis;
        var $receiveBtn, $drAnalysisPanel, $optContainer, $selectContainer, $listContainer;

        // var xzSubscribe;

        /***
         * init
         * @private
         */
        function _init() {

            //初始化对象
            dtReAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_SJJSMAS, _map);
            dtReAnalysis.setAppConfig(appConfig);
            dtReAnalysis.setAnalysisLyrs(_drceiveConfig.layers);
            dtReAnalysis.setDataSource(_drceiveConfig.queryDataSource);
            dtReAnalysis.setSaveDataSource(_drceiveConfig.saveDataSource);

            //初始化变量
            $drAnalysisPanel = $("#drAnalysisPanel");
            //根据配置控制页面显示内容
            var option = {
                listId: "drList",
                id: dtReAnalysis.getId(),
                queryModeOn: lang.isArray(_drceiveConfig.scopeLayers) ? true : false
            };
            lang.mixin(option, _drceiveConfig);

            if (lang.isArray(_drceiveConfig.layers) && _drceiveConfig.layers.length > 0) {
                //默认选中第一个图层
                var temp = _drceiveConfig.layers.reverse();
                temp[0].selected = 'selected';
                $drAnalysisPanel.append(renderTpl($("#drSelectTpl").html(), {layers: _drceiveConfig.layers}));
                //多选
                $("#layerSelector").multipleSelect({
                    selectAll: false,
                    placeholder: '选择图层',
                    allSelected: '所有图层',
                    minimumCountSelected: 5,
                    delimiter: '|'
                });

            }
            $optContainer = $('#' + dtReAnalysis.getId());
            $selectContainer = $('#select_' + dtReAnalysis.getId());
            $listContainer = $("#drList");
            $receiveBtn = $('#receiveBtn');
        }


        /***
         * add listeners
         * @private
         */
        function _addListeners() {
            $receiveBtn.on('click', 'a', function () {
                var data = {
                    layerType: this.moduleConfig.layerType,
                    year: this.moduleConfig.year,
                    geometry: this.analysisGeometry,
                    outFields: this.outFields,
                    dataSource: this.dataSource
                };
                $.ajax({
                    url: g.ANALYSI_REST_GHSC_URL,
                    data: data,
                    method: "POST",
                    async: false, //关闭异步
                    success: function (r) {
                        restRet = r.result;
                    }
                });
            });

            $('#layerSelector').on("change", function () {
                var selArr = $(this).multipleSelect('getSelects');
                if (selArr.length > 0) {
                    var layers = _drceiveConfig.layers;
                    var data = arrayUtil.filter(layers, function (item) {
                        return arrayUtil.indexOf(selArr, item.layerName) > -1;
                    });
                    dtReAnalysis.setAnalysisLyrs(data);
                }
            });
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

        return me;
    }
);