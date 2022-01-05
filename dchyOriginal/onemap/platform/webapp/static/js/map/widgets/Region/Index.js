/**
 * 行政区控制widget
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/5 12:01
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "handlebars",
    "esri/graphic",
    "esri/geometry/Polygon",
    "esri/symbols/SimpleFillSymbol",
    "esri/symbols/SimpleLineSymbol",
    "map/manager/ConfigManager",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/utils/MapUtils"], function (declare, lang, arrayUtils,Handlebars, Graphic, Polygon,SimpleFillSymbol, SimpleLineSymbol, ConfigManager,
                           BaseWidget, JsonConverters, MapUtils) {

    var __map;
    var Region = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            MapUtils.setMap(__map);
            _initComponent();
        }
    });

    var configManager = ConfigManager.getInstance();
    var regionCode,regionData=[];
    var breads=[];//面包屑
    var $regionContainer;
    var rootRegion={};

    /**
     * 初始化组件外观
     * @private
     */
    function _initComponent() {
        Handlebars.registerHelper("isActive", function (conditional, options) {
            if (conditional === true)
                return options.fn(this);
            else {
                return options.inverse(this);
            }
        });
        regionCode = configManager.getAppConfig().defaultRegionCode;
        //延迟加载行政区widget
        EventBus.listener(EventBus.APP_INITIALIZED, function () {
            setTimeout(function () {
                $.getJSON(root + "/map/" + tpl + "/regioncode?regionCode=" + regionCode || '320000',
                    function (data) {
                        if (data.hasOwnProperty("success")) {
                            error("行政区列表获取异常: " + data.msg);
                            return;
                        }
                        $("#Region").show();
                        $regionContainer = $("#region_container");
                        _parseData(data);
                    });
            }, 2000);
        });
    }

    /**
     * 解析行政区数据
     * @param data
     */
    function _parseData(data) {
        setRootRegion(data);
        breads.push({id:data.id,title:data.title});//往面包屑中加入根路径
        if(data.hasOwnProperty("children")&&lang.isArray(data.children)){
            $.each(data.children,function(i,item){
                var flag = false;
                item.parent={name:data.name,title:data.title};
                $.each(regionData,function(j, it){
                    if(item.id == it.id){
                        flag = true;
                    }
                });
                if(!flag){
                    regionData.push(item);
                }
            });
        }
        _addEventListener();
    }

    /**
     * 添加相关监听
     * @private
     */
    function _addEventListener() {
        $(".region-change-inner").on('click',function () {
            $('.dropdown-body').show();
            $('.fa-caret-down').addClass('fa-rotate-180');
            if ($regionContainer.html() == ""){
                _loadRegions($(this).find("b").eq(0).data("regionCode") || regionCode);
            }
        });
        $("#popCloseBtn").on('click', function () {
            $('.dropdown-body').hide();
            $('.fa-caret-down').removeClass("fa-rotate-180");
        });
    }

    /**
     * 加载子行政区列表
     * @private
     */
    function _loadRegions(code, regions) {
        $regionContainer.empty();
        var template = Handlebars.compile($('#regionTemplate').html());
        if (code === regionCode) {
            //根行政区
            $regionContainer.html(template({data: regionData}));
        } else {
            $regionContainer.html(template({data: regions}));
        }
        $regionContainer.find("a").on('click', function () {
            var title = $(this).text();
            var geo = $(this).data("geo");
            var region = $(this).data("regioncode");
            arrayUtils.forEach(breads, function (item) {
                item.active = false;
            });
            if(breads.length==0){
                breads.push({id:rootRegion.id,title:rootRegion.title});//往面包屑中加入根路径
            }
            breads.push({id: region, title: title, active: true});
            updateRegions(region,geo);
        });
    }
    /***
     *
     * @param regionObj
     * @param regionGeo
     */
    function updateRegions(region, regionGeo) {
        if (regionGeo != undefined && regionGeo != "") {
            var featuresObj = JsonConverters.toEsri(regionGeo,__map.spatialReference);
            var gras = [];
            if (featuresObj.hasOwnProperty("features") && lang.isArray(featuresObj.features)) {
                arrayUtils.forEach(featuresObj.features, function (feature) {
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
            MapUtils.locateFeatures(gras, 3);
            MapUtils.highlightFeatures(gras, true);
        }
        if (region != undefined) {
            //获取子级行政区列表
            $.getJSON(root + "/map/" + tpl + "/regioncode?regionCode=" + region, function (data) {
                if (data.hasOwnProperty("success")) {
                    error("行政区列表获取异常: " + data.msg);
                    return;
                }
                var _regions = [];
                if (data.hasOwnProperty("children") && lang.isArray(data.children)) {
                    arrayUtils.forEach(data.children, function (item) {
                        item.parent = {name: data.name, title: data.title};
                        _regions.push(item);
                    });
                    if (_regions.length > 0) {
                        if (breads.length > 0)
                            updateBreadcrumb();
                        _loadRegions(region, _regions);
                    }else{
                        if(breads.length>0){
                            breads=arrayUtils.filter(breads,function(item){
                                return item.id!=region;
                            });
                        }
                    }
                }
            });
        }
    }
    /**
     * 设置根region
     * @param title
     */
    function setRootRegion(root) {
        lang.mixin(rootRegion,root);
        $(".region-change-inner >b")[0].innerText = root.title;
        $(".region-change-inner").find("b").eq(0).data("regionCode",root.name);
    }

    /***
     * 更新面包屑
     */
    function updateBreadcrumb(){
        var tpl=Handlebars.compile($("#regionBreadcrumb").html());
        var popTitle=$(".popTitle");
        popTitle.empty();
        popTitle.append(tpl({list:breads}));
        $(".region-breadcrumb").find('a').on('click', function () {
            var title = $(this).text();
            var region = $(this).data("region");
            var isRoot = false;
            arrayUtils.forEach(breads, function (item, idx) {
                if(item!=undefined){
                    if (item.id == region) {
                        if (idx === 0) {
                            isRoot = true;
                            return;
                        } else {
                            breads.splice(idx, breads.length - idx);
                        }
                    } else
                        item.active = false;
                }
            });
            if (isRoot === true) {
                breads = [];
                resetPoptitle();
            } else
                breads.push({id: region, title: title, active: true});
            updateRegions(region);
        });
    }

    /**
     * 重置标题
     */
    function resetPoptitle(){
        var popTitle=$(".popTitle");
        popTitle.empty();
        popTitle.append('<span class="text-muted">行政区列表</span>');
    }

    return Region;
});