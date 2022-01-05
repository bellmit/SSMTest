/**
 * @author hanguanghui
 * @Description 兼容老版执法巡查 项目查看功能
 * @version V1.0, 2017/3/23
 * @project onemap-parent
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/on",
    'esri/graphic',
    "esri/lang",
    "esri/tasks/FeatureSet",
    "esri/geometry/Polygon",
    "map/component/MapPopup",
    "handlebars",
    "layer",
    "map/core/BaseWidget",
    "dojox/uuid/generateRandomUuid",
    "map/utils/MapUtils"],function (declare, arrayUtil, lang, on, Graphic, esriLang, FeatureSet,Polygon,MapPopup, Handlebars, layer,BaseWidget,RandomUuid, MapUtils) {

    var _map, _leasLocConfig;
    var mapPopup = MapPopup.getInstance();

    //获取shapeurl
    var GET_PROJECT_INFO= "/leas/project/location/info";


    var me = declare([BaseWidget], {
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            MapUtils.setMap(_map);
            _leasLocConfig = this.getConfig();
            _init();
        },
        onOpen: function () {

        },
        onPause: function () {
            _clear();
            if (mapPopup.isShowing)mapPopup.closePopup();
        },
        onDestroy: function () {
            if (mapPopup.isShowing)mapPopup.closePopup();
        }
    });

    /**
     * 数据来源 0--供地，1-基本农田，2-卫片...
     * */
    var ds ="0";
    var proId="";

    //前端渲染
    var resultObj={};

    //用于定位请求回的图形
    var locationShape;

    function _init(){
        var urlParams = getUrlParams();
        if(urlParams&&urlParams.hasOwnProperty("dataSource")&&(urlParams.hasOwnProperty("proId")||urlParams.hasOwnProperty("proid"))){
            layer.msg("正在查找项目……");
            ds =urlParams.dataSource;
            proId = urlParams.proId||urlParams.proid;
            var data = {proid:proId,ds:Number(ds)};
            getShape(data);
        }else{
            layer.msg("未获取项目ID信息!")
        }
    }

    /**
     * 定位项目
     * @param data
     */
    function doLocation(data){
        var locGras = [];
        var att = locationShape.result.project;
        var coords = att.shape;
        var length = locationShape.result.length;
        var area = locationShape.result.area;

        //组织前台渲染
        resultObj.length = length;
        resultObj.area = area;
        if(coords!= null&&coords!=""){
            var polygon = new Polygon({"rings":coords,"spatialReference":_map.spatialReference});
            var g = new Graphic(polygon,null,att);
            resultObj.gra = g;
            locGras.push(g);
        }

        var fields = _leasLocConfig[ds].fields;
        var resFields =[];
        var index = 0;
        $.each(fields,function(i,item){
            index++;
            if(index > 2)
                return;
            var field ={};
            if(index == 1){
                field.title= true;
            }else{
                field.subtitle=true;
            }
            field.key=item.alias;
            field.value=att[item.name];
            resFields.push(field);
        });
        resultObj.fields = resFields;
        var infoTpl = $("#locProDetailTpl").html();
        var temp = Handlebars.compile(infoTpl);
        var htm = temp({data: resultObj});
        $("#LeasLocationContainer").empty();
        $("#LeasLocationContainer").append(htm);

        if(locGras.length>0){
            MapUtils.highlightFeatures(locGras, false);
            MapUtils.locateFeatures(locGras, 3);
            layer.msg("定位完成！",{icon:1});
        }else{
            layer.msg("定位失败，未获取相关项目！")
        }
        addListener();
    }

    /**
     * 定位事件 弹框
     */
    function addListener(){
        $(".location-pro").on("click",function(){
            var uid = $(this).data("uid");
            var gra = resultObj.gra;

            var geoCenter;
            var geo = gra.geometry;
            var geoType = geo.type;
            switch (geoType) {
                case 'point':
                    geoCenter = geo;
                    break;
                case 'polyline':
                case 'polygon':
                    geoCenter = geo.getExtent().getCenter();
                    break;
            }
            if (mapPopup.isShowing)mapPopup.closePopup();
            mapPopup.setData(gra.attributes);
            mapPopup.setTitleField(_leasLocConfig[ds].fields[0].name);
            mapPopup.setFields(_leasLocConfig[ds].fields);
            mapPopup.openPopup(geoCenter).then(function () {
                if (geoType == 'point')
                    MapUtils.locatePoint(gra);
                else
                    MapUtils.locateFeatures([gra]);
            });
        })
    }


    function getShape(data){
        $.getJSON(GET_PROJECT_INFO,data , function (result) {
            locationShape = result;
            doLocation(data);
        });
    }
    function _clear(){

    }
    return me;
});