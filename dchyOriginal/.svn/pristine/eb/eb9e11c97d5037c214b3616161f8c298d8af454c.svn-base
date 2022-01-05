/**
 * 热力图渲染模块
 * - 基于 esri/renderers/HeatmapRenderer 动态渲染
 * Created by alex on 2018/3/23.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "dojo/on",
    "map/core/BaseWidget",
    "layer",
    "esri/renderers/HeatmapRenderer",
    "esri/layers/FeatureLayer",
    "esri/tasks/query",
    "esri/tasks/QueryTask",
    "esri/graphic",
    "esri/lang"], function (declare, lang, arrayUtils, Deferred, on, BaseWidget, layer, HeatmapRenderer,FeatureLayer,Query,
                            QueryTask,Graphic,esriLang) {
    var _map, _config,heatLayer,heatmapRenderer,isBusy;
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
            _config = this.getConfig();
            _init();
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

    /**
     * 初始化操作
     * 创建监听，根据需要创建热力图
     * @private
     */
    function _init() {
        layer.config();
        _addListener();
    }
    /*
    *
    * */
    function _addListener() {

        //下拉调整参数
        $("a[data-toggle='collapsePreset']").on('click', function () {
            if(heatLayer===undefined){
                return;
            }
            var $this = $(this);
            var $i = $this.find("i");
            if ($i.hasClass('fa-caret-square-o-right')) {
                $i.removeClass('fa-caret-square-o-right').addClass('fa-caret-square-o-down');
            } else
                $i.removeClass('fa-caret-square-o-down').addClass('fa-caret-square-o-right');

            var collapsePanel = $('#t_heatlayer');
            if (esriLang.isDefined(collapsePanel) && collapsePanel.length > 0) {
                collapsePanel.collapse('toggle');
                collapsePanel.on('shown.bs.collapse', lang.hitch(this, _afterCollapse));
            }
        });
        //图层开关
        $(".heatlyr-vis-switch").on('click', function (evt) {
            evt.stopPropagation();
            var $this = $(this);

            if(isBusy){
                layer.msg("热力图生成中，请稍候！", {icon: 0, time: 3000});
                return;
            }
            _changeVisible("heatLayer", !$this.hasClass("omp-switch-on"));

            if ($this.hasClass("omp-switch-on")) {
                $this.removeClass("omp-switch-on");
                $this.find('em').text("关");
            } else {
                $this.addClass("omp-switch-on");
                $this.find('em').text("开");
            }
        });


    }

    /*
    * 调整热力图参数以确定合适的参数值
    * */
    function _afterCollapse() {

        //模糊半径
        var blurRadiusBtn = $('#slider_blurRadius');
        blurRadiusBtn.slider({
            orientation: "horizontal",
            range: "min",
            max: 100,
            value: heatmapRenderer.blurRadius,
            slide: changeblurRadius,
            change: changeblurRadius
        });
        blurRadiusBtn.prev().find('span').html(heatmapRenderer.blurRadius);
        function changeblurRadius() {
            var val = $(this).slider("value");
            $(this).prev().find('span').html(val.toString());
            heatmapRenderer.blurRadius = val;
            heatLayer.redraw();
        }

        //最大像素强度
        var maxPixelIntensityBtn = $('#slider_maxPixelIntensity');
        maxPixelIntensityBtn.slider({
            orientation: "horizontal",
            range: "min",
            max: 500,
            value: heatmapRenderer.maxPixelIntensity,
            slide: changemaxPixelIntensity,
            change: changemaxPixelIntensity
        });
        maxPixelIntensityBtn.prev().find('span').html(heatmapRenderer.maxPixelIntensity);
        function changemaxPixelIntensity() {
            var val = $(this).slider("value");
            $(this).prev().find('span').html(val.toString());
            heatmapRenderer.maxPixelIntensity = val;
            heatLayer.redraw();
        }

        //初始像素强度
        var minPixelIntensityBtn = $('#slider_minPixelIntensity');
        minPixelIntensityBtn.slider({
            orientation: "horizontal",
            range: "min",
            max: 500,
            value: heatmapRenderer.minPixelIntensity,
            slide: changeminPixelIntensity,
            change: changeminPixelIntensity
        });
        minPixelIntensityBtn.prev().find('span').html(heatmapRenderer.minPixelIntensity);
        function changeminPixelIntensity() {
            var val = $(this).slider("value");
            $(this).prev().find('span').html(val.toString());
            heatmapRenderer.minPixelIntensity = val;
            heatLayer.redraw();
        }
    }
    /**
     *
     * @param id
     * @param visible
     */
    function _changeVisible(id, visible) {
        var lyr = _map.getLayer(id);

        if (lyr != undefined) {
            _map.reorderLayer(lyr, 0);
            lyr.setVisibility(visible);
        }
        else {
            isBusy=true;
            doQueryTask();
            layer.msg("热力图生成中，请稍候！", {icon: 0, time: 3000});
        }
    }
    /*
    * 1. 根据服务图层获取数据
    * 2. 循环获取地块中心点构造featurelayer
    * 3. 设置为热力图渲染
    *
    * */
    function doQueryTask() {
        var qtask=new QueryTask(_config.queryUrl);
        var query=new Query();
        query.returnGeometry=true;
        query.outFields=arrObj2arr(_config.fields);
        query.where=_config.where;
        qtask.execute(query,qSuccess,qFailed);
    }

    /*
    * 1. 查询成功，plygonSet转换为pointSet
    * 2. 组装成featurelayer
    * 3. 添加热力图渲染
    * */
    function qSuccess(fset) {
        if(fset.features.length>0){
            var features = [];
            arrayUtils.forEach(fset.features,function (feat) {
                var point=feat.geometry.getCentroid();
                var graphic = new Graphic(point);
                var attr = {};
                arrayUtils.forEach(_config.fields,function (field) {
                    attr[field.name]=feat.attributes[field.name];
                });
                graphic.attributes = attr;
                features.push(graphic);
            });
            createHeatLayer(features);
            addHeatmapRenderer();
            return;
        }
        layer.msg("热力图查询无数据！", {icon: 0, time: 3000});
    }
    /*
    * 查询失败
    * */
    function qFailed() {

        $(".heatlyr-vis-switch").removeClass("omp-switch-on");
        $(".heatlyr-vis-switch").find('em').text("关");

        layer.msg("热力图查询失败！", {icon: 0, time: 3000});

        isBusy=false;
    }
    /*
    * 组装featurelayer
    * */
    function createHeatLayer(fset) {

        var featureCollection = {
            "layerDefinition": {
                "geometryType": "esriGeometryPoint",
                "objectIdField": "OBJECTID",
                "fields":_config.fields
            },
            "featureSet": {
                "features": fset,
                "geometryType": "esriGeometryPoint"
            }
        };
        heatLayer=new FeatureLayer(featureCollection,{
            id:"heatLayer"
        });
        heatLayer.opacity=0.95;
        heatLayer.spatialReference=_map.spatialReference;
    }
    /*
    * 添加热力图渲染
    * */
    function addHeatmapRenderer() {
        heatmapRenderer = new HeatmapRenderer({
            colors: ["rgba(0, 0, 0, 0)","rgba(0, 0, 255, 0.25)","rgba(0, 255, 0,0.5)","rgba(255, 255,0, 0.75)", "rgba(255, 0, 0,1)"],
            blurRadius: _config.blurRadius,
            maxPixelIntensity: _config.maxPixelIntensity,
            minPixelIntensity: _config.minPixelIntensity
        });
        heatLayer.setRenderer(heatmapRenderer);
        _map.addLayer(heatLayer);
        isBusy=false;
        layer.msg("热力图已生成！", {icon: 0, time: 3000});
    }
    /*
* 遍历数组中对象，取出该对象某值组装成新的数组
* */
    function arrObj2arr(arrObj) {
        var fields=[];
        arrayUtils.forEach(arrObj,function (field) {
            fields.push(field.name);
        });
        return fields;
    }
    return me;
});