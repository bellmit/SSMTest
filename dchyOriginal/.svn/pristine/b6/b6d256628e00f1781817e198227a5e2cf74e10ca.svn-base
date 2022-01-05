/**
 * admin模块 用于测试sde服务连接是否正常 展示geojson图形等功能
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/9/18 17:23
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/json",
    "esri/graphic",
    "esri/tasks/FeatureSet",                        /*7.8*/
    "esri/toolbars/draw",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/GeometryIO",                                          /*7.8*/
    "map/utils/MapUtils",
    "map/core/EsriSymbolsCreator",
    "handlebars",
    "layer",
    "ko",
    "map/component/ListDataRenderer"], function (declare, lang, arrayUtil, Json, Graphic,FeatureSet, Draw, BaseWidget, JsonConverters,
                                                 GeometryIO,MapUtils, EsriSymbolsCreator, Handlebars, layer, ko, ListDataRenderer) {

    var _map, vm;
    var me = declare([BaseWidget], {

        onCreate: function () {
            _map = this.getMap().map();
            init();
        },
        /***
         *
         */
        onOpen: function () {

        },
        /***
         *
         */
        onPause: function () {
        },
        /**
         *
         */
        onDestroy: function () {

        }

    });

    var $sdeSelect, $form;
    var $listContainer;
    var drawTool, drawHandler;

    /***
     * 初始化
     */
    function init() {

        drawTool = new Draw(_map);
        drawTool.setFillSymbol(EsriSymbolsCreator.defaultFillSymbol);
        drawTool.setLineSymbol(EsriSymbolsCreator.defaultLineSymbol);
        drawTool.setMarkerSymbol(EsriSymbolsCreator.defaultMarkerSymbol);

        layer.config();
        $sdeSelect = $("#sdeSelect");
        $form = $("#sdeForm");
        $listContainer = $("#listContainer");

        //初始化 viewmodel
        vm = new ViewModel();
        ko.applyBindings(vm);

        //tetscode
        var listDataRenderer = new ListDataRenderer({
            renderTo: $listContainer, type: 'io', renderData: [{title: '标题', subtitle: '副标题', other: {name: 'alex', age: 20}},
                {title: '手绘图形1', subtitle: '来源:手绘'}, {title: '查询图形', subtitle: ''}]
        });
        listDataRenderer.render();

        //解析geojson
        $("#parseGeoBtn").on('click', function () {
            vm.geos([]);
            var $this = $(this);
            $this.html($this.data("loading-text"));
            $this.attr("disabled", true);
            //解析geojson并在地图上生成
            parseGeoJSON();
            $this.removeAttr("disabled");
        });
        $("#exportGeoBtn").on('click', function () {
            var geojson = lang.trim($("#rawTextArea").val());
            if(geojson != undefined && geojson != "") {
                $.ajax('/omp/geometryService/rest/export/shp',{
                    method: 'post',
                    data: {geometry: geojson},
                    success: function (ret) {
                        if(ret != undefined) {
                            window.location.href = '/omp/file/download/' + ret;
                        }
                    }
                })
            }
        });

        $(".draw-menu").find('a').on('click', function () {
            var $this = $(this);
            var type = $this.data("type");
            drawGeometry(type);
        });
    }
    /**
     * 地图上绘制图形
     * @param geoType
     */
    function drawGeometry(geoType) {
        if (drawTool != undefined) drawTool.deactivate();
        if (drawHandler != undefined) drawHandler.remove();
        drawTool = drawTool ? drawTool : new Draw(_map);
        drawHandler = drawTool.on("draw-end", lang.hitch(this, function (evt) {
            drawTool.deactivate();
            var geo = evt.geometry;
            var sb = undefined, n = undefined;
            if (geo != null) {
                var gt = geo.type;
                switch (gt) {
                    case 'point':
                        sb = EsriSymbolsCreator.defaultMarkerSymbol;
                        n = '点';
                        break;
                    case 'polyline':
                        sb = EsriSymbolsCreator.defaultLineSymbol;
                        n = '线';
                        break;
                    case 'polygon':
                        sb = EsriSymbolsCreator.defaultFillSymbol;
                        n = '面';
                        break;
                }
                var gra = new Graphic(geo, sb, {});
                _map.graphics.add(gra);
                vm.draws.push({
                    name: n,
                    g: geo
                });
            }
        }));
        drawTool.activate(geoType);
    }

    /***
     *
     * @constructor
     */
    function ViewModel() {
        var self = this;
        self.geos = ko.observableArray([]);
        self.location = lang.hitch(this,location);
        self.draws = ko.observableArray([]);
        self.exportDraw =  (this, exportDraw);
        self.clear = lang.hitch(this, clearGras);
    }

    /**
     * 绘制的图形进行导出
     * @param data
     * @param type
     */
    function exportDraw(type, data) {
        var geo = data.g;
        var features=[];//
        var geometryIO=new GeometryIO();
        var featureSet = new FeatureSet();


        if(geo === undefined) return;
        var geojson = Json.toJson(JsonConverters.toGeoJson(geo));
        features.push({type: geo.type, geometry: geo});
        featureSet.features =features;
        switch (type) {
            case 'geojson':
                layer.open({
                    type: 1,
                    content: '<p>'+ geojson +'</p>',
                    area: ['400px', '200px']
                });
                break;
            case 'txt':
                //todo
                geometryIO.expToFile(featureSet,type);
                break;
            case 'excel':
                //todo...
                geometryIO.expToFile(featureSet,'xls');
                break;
        }

    }

    /**
     * clear gras
     */
    function clearGras() {
        _map.graphics.clear();
        vm.draws([]);
        vm.geos([]);
    }

    /***
     * 定位图形
     * @param g
     */
    function location(g) {
        MapUtils.locateFeatures([g]);
    }

    /***
     * 解析geojson数据-
     */
    function parseGeoJSON() {
        var geojson = lang.trim($("#rawTextArea").val());
        if (geojson === "")return;
        var featuresObj = JsonConverters.toEsri(JSON.parse(geojson));
        var gras = [];
        if (featuresObj.hasOwnProperty("features") && lang.isArray(featuresObj.features)) {
            arrayUtil.forEach(featuresObj.features, function (feature) {
                var graphic = new Graphic(feature);
                gras.push(graphic);
            });
        } else if (featuresObj.hasOwnProperty("geometry")) {
            var graphic = new Graphic({geometry: featuresObj.geometry});
            gras.push(graphic);
        } else if (featuresObj.hasOwnProperty("rings")) {
            var graphic = new Graphic({geometry: featuresObj});
            gras.push(graphic);
        }
        if(gras.length>0){
            vm.geos(gras);
            MapUtils.setMap(_map);
            MapUtils.locateFeatures(gras, 3);
            MapUtils.highlightFeatures(gras, false);
        }
    }

    return me;
});