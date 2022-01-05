/**
 * 空间分析模块
 * Created by yangjiawei on 2017/3/8.
 */

define(["dojo/_base/declare",
        "dojo/_base/lang",
        "esri/lang",
        "map/component/ChosenSelect",
        "map/core/BaseAnalysis",
        "map/core/BaseWidget",
        "handlebars",
        "layer",
        "map/core/JsonConverters",
        "map/core/QueryTask",
        "text!static/js/map/template/analysis/spatial-analysis-tpl.html",
        "css!static/js/map/template/analysis/analysis.css"], function(declare, lang, esriLang, ChosenSelect, BaseAnalysis, BaseWidget, Handlebars, layer, JsonConverters, QueryTask, basicTpl){

    var _map;
    var _saConfig;      //后台的配置信息
    var spatialAnalysis;
    var _saConfigLyrs;      //存放配置的分析图层
    var _currentLyrId;      //当前选中的分析图层ID

    var me = declare([BaseWidget],{

        onCreate: function(){
            _map = this.getMap().map();
            _saConfig = this.getConfig();
            init();
            _addListener();
        },

        onPause: function(){

        },

        onDestroy: function(){

        }

    });

    var $optContainer, $listContainer, $lyrSelect;


    /**
     * 模块初始化
     */
    function init(){

        layer.config();//初始化layer

        spatialAnalysis = new BaseAnalysis(BaseAnalysis.TYPE_SPATIAL, _map);

        //读取配置信息
        _saConfigLyrs = _saConfig.layers;

        if ( _saConfigLyrs == null){
            layer.alert('未配置任何空间分析图层!', {btn: [], shadeClose: true});
            return;
        }

        //加载左侧功能面板内容
        var option = {
            layers: _saConfigLyrs,
            listId: "saList",
            id: spatialAnalysis.getId()
        };

        $("#spatialAnalysisPanel").append(renderTpl(basicTpl, option));
        $optContainer = $("#" + option.id);
        $listContainer = $("#" + option.listId);
        $lyrSelect = $("#layersSelect");

        //layerSelect 初始化时选中第一个图层
        _currentLyrId = _saConfigLyrs[0].serviceId;
        ChosenSelect({
            elem: '.chosen-select'
        });

    }


    /**
     * 功能监听
     * @private
     */
    function _addListener(){

        //监听选择图层
        $lyrSelect.on('change', function(){
            _currentLyrId = $(this).val();
        });


        //操作监听
        $optContainer.on('click', 'a', function(){
            var opt = $(this).data("opt");
            switch (opt){
                case 'draw':
                    spatialAnalysis.draw('polygon', false).then(function(obj){
                        obj != null && _doSpatialAnalysis(obj);
                    });
                    break;
            }
        });
    }
    /**
     * 模板渲染封装 handlerbars
     * @param temp
     * @param data  format: jsonObject
     */
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }

    /**
     * 进行空间查询
     * @private
     */
    function _doSpatialAnalysis(geometry){
        $.each(_saConfigLyrs, function(index, lyr){
            if (_currentLyrId == lyr.serviceId){
                var outFields = [];
                $.each(lyr.returnFields, function(index, item){
                    outFields.push(item.alias);
                });
                var where = "1=1";
                var outFieldsStr = outFields.toString();
                var data = {
                    layerUrl: getLayerUrl(lyr.layerUrl),
                    where: where,
                    geometry: JSON.stringify(geometry),
                    returnFields: outFieldsStr,
                    page: 1,
                    size: 100
                };

                $.ajax({
                    url: "/omp/map/query",
                    data: data,
                    success: function (r) {
                        _spaAnaRstHandler(r, lyr);
                    }
                });
            }
        });
    }

    /**
     * 处理查询结果
     * @param lyrCfg 图层的配置信息
     * @private
     */
    function _spaAnaRstHandler(r, lyrCfg){
        if (r.hasOwnProperty("success")) {
            layer.open({
                icon: 0,
                title: '提示',
                content: r.msg
            });
            return;
        }
        if (r.hasOwnProperty("content")) {
            if (r.totalElements == 0) {
                layer.msg('未查询到结果');
                return;
            };
        }

        var link = lyrCfg.link.url;
        var immd = lyrCfg.link.openLinkImmd;
        var paramName = lyrCfg.link.paramName;
        if (link != null && immd){
            var param = [];
            $.each(r.content, function(index, item){
                item.attributes[paramName] && param.push(item.attributes[paramName]);
            });
            var url = link + "?" + encodeURI("layerName=" + lyrCfg.layerName + "&" + paramName + "=" + param.toString());
            console.log(url);
            layer.open({
                type: 2,
                content: url,
                area: ["800px", "600px"]
            });
        }
    }
    /***
     * 处理图层url
     * @param lyrUrl
     * @returns {string}
     */
    function getLayerUrl(lyrUrl) {
        if (lyrUrl.startWith("http://"))
            return encodeURI(lyrUrl);
        else {
            return encodeURI(lyrUrl.replace("/oms", omsUrl));
        }
    }

    return me;
});
