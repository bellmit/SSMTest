/**
 * 动态巡查系统
 * 巡查路线回放
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/5/24 22:38
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "dojo/on",
    "dojo/topic",
    "dojox/uuid/generateRandomUuid",
    "map/core/QueryTask",
    "map/core/BaseWidget",
    "map/core/GeometryServiceTask",
    "map/core/EsriSymbolsCreator",
    "map/utils/MapUtils",
    "esri/geometry/Point",
    "esri/geometry/Polyline",
    "esri/geometry/Polygon",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    'esri/toolbars/draw',
    "esri/tasks/IdentifyTask",
    "esri/tasks/IdentifyParameters",
    "handlebars",
    "layer",
    "mock/MockHttps",
    "text!map/template/leas/route-inspect-pnts.html"], function (declare, lang, arrayUtil, Deferred, on, topic, RandomUuid,
                                                                 QueryTask, BaseWidget, GeometryServiceTask, EsriSymbolsCreator,
                                                                 MapUtils, Point, Polyline, Polygon, GraphicsLayer, Graphic,
                                                                 Draw, IdentifyTask, IdentifyParameters, Handlebars, layer, MockHttps, RoutePntsTpl) {

    var me = declare([BaseWidget], {
        onCreate: function () {
            _map = this.getMap().map();
            _routeConfig = this.getConfig();
            init();
        },
        onOpen: function () {
        },
        onPause: function () {
            clearEventHandles();
            clearMap();
        }
    });

    //请求获取巡查路线信息
    var INSPECT_FETCH_URL = leasUrl + "/map/inspect/fetch?id=";

    //计划路线样式
    var planLineSymbol;
    //实际巡查路线样式
    var actualLineSymbol;

    var actualStaticLineSymbol = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID,"#fca474", 3);
    //回放路线起始点
    var planEndPointSymbol = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/directions_run.png', 24, 24);
    //回放路线的终点
    var stopPath="M511.222799 957.503517c0 0-336.095711-381.673769-336.095711-564.724829 0-183.042873 150.474107-331.400782 336.095711-331.400782 185.630814 0 336.069105 148.357909 336.069105 331.400782C847.291904 575.828724 511.222799 957.503517 511.222799 957.503517L511.222799 957.503517zM511.222799 114.039289c-155.926282 0-282.329157 123.942847-282.329157 276.797164 0 152.879899 126.402875 276.804327 282.329157 276.804327 155.925259 0 282.331203-123.924427 282.331203-276.804327C793.554002 237.982136 667.148058 114.039289 511.222799 114.039289L511.222799 114.039289zM682.285826 421.196924c-43.528376-11.771091-79.540567-26.38595-108.042715-43.846624-29.758768 18.181081-64.387449 33.178657-103.910605 44.948725-6.756891-15.22475-14.378475-29.840632-22.87601-43.845601 33.751708-8.351202 63.275115-18.671245 88.530312-30.943756-11.012821-10.813277-21.13332-22.620184-30.388101-35.375695-8.761548 11.542894-17.751293 22.593578-26.995841 33.141818-10.012028-10.54824-20.770046-20.98801-32.276101-31.318286 33.752732-37.344536 59.007929-77.397764 75.765591-120.140241l47.647183 8.469905c-4.267187 9.099239-8.496511 17.952884-12.762675 26.532283l119.694079 0 0 39.442314c-15.517415 29.484521-36.289508 56.027038-62.290695 79.594803 25.01779 11.31572 54.011124 20.040429 87.015819 26.194592C695.157995 388.519688 688.776657 404.266323 682.285826 421.196924L682.285826 421.196924zM623.768061 270.840498l-90.426499 0c-0.74599 1.74269-1.622964 3.338025-2.606361 4.787026 10.994402 18.927071 25.490557 35.256992 43.50791 49.033763C595.761194 308.678268 612.26303 290.735617 623.768061 270.840498L623.768061 270.840498zM423.067017 260.510222l42.396599 17.340947c-22.765493 41.995463-46.77328 84.392062-72.036663 127.134539 17.751293-2.207271 36.659945-4.669346 56.665581-7.367805-2.260483 12.791327-3.765767 27.525913-4.524037 44.229341-39.002292 2.461051-72.637344 6.873548-100.882642 13.247722l-12.764722-41.639353c12.499685-12.035104 27.634384-33.406854 45.3867-64.113203-13.749142 0.983397-27.142174 2.087545-40.136116 3.300162l-12.762675-40.508599c12.990872-14.988366 31.645744-54.203506 55.888891-117.560486l46.161343 13.630439c-18.763342 38.566364-36.277228 72.091922-52.516074 100.593046 6.490831-0.228197 15.108093-0.611937 25.866111-1.104147C407.057391 293.461705 414.834518 277.716092 423.067017 260.510222L423.067017 260.510222zM454.212364 519.592932c-47.52848 7.621585-88.039125 14.642488-121.536031 21.023826l-7.512091-51.586912c48.777937-7.138584 92.276636-13.895475 130.553405-20.276812C454.439538 489.376747 453.948351 506.334977 454.212364 519.592932L454.212364 519.592932zM516.474406 409.052327c57.276495 9.828856 101.149724 17.687848 131.656529 23.568788l-10.885931 42.751686c-42.742477-9.080819-86.871533-17.806551-132.404566-26.158776L516.474406 409.052327 516.474406 409.052327zM666.129868 509.298472l-11.616572 47.911196c-63.512523-13.292748-126.039601-25.546839-187.54542-36.871769l12.353352-44.966121C546.363134 486.677265 608.634386 497.983775 666.129868 509.298472L666.129868 509.298472z";
    var planStopPointSymbol;

    //实际巡查id
    var inspectId;
    var routeGraphicsLayer;
    var routeEventHandles = [];

    //是否自动播放
    var playNow=false;

    //主容器
    var $routeContainer,$routePntsContainer;

    /**
     * 初始化
     */
    function init() {

        //mock data start 测试数据
      /*  var inspectData = {
            "actualInsPoints": "[{\"dkid\":\"015981d84ec3011041375981a418001c\",\"mapServerUrl\":\"015981d851cf011041375981a418001d\"}]",
            "actualInsTracks": "",
            "inspectPlanPoints": "[{\"dkid\":\"015981d84ec3011041375981a418001c\",\"mapServerUrl\":\"015981d851cf011041375981a418001d\"}]",
            "inspectPlanTracks": "[[40455458.406229764,3505185.235743594],[40455147.89722332,3505147.0192504944],[40455076.241298765,3504989.376216458],[40455119.2348535,3504812.6249358724],[40455104.90366859,3504564.2177307243]]"
        };
        MockHttps.mockJson(INSPECT_FETCH_URL + "11", inspectData);*/
        //mock data end

        layer.config();
        //解析配置
        if (urlParams != undefined && urlParams.hasOwnProperty("inspectId")) {
            inspectId = urlParams.inspectId;
        } else {
            layer.msg("巡查id未获取到,检查地址参数", {icon: 0});
        }
        if (_routeConfig != undefined) {
            if (_routeConfig.hasOwnProperty("autoPlay"))
                playNow = _routeConfig.autoPlay;
            if (_routeConfig.hasOwnProperty("titleField"))
                tf = _routeConfig.titleField;
        }
        var lineColor=_routeConfig.hasOwnProperty("lineColor")?_routeConfig.lineColor:"#0091ff";
        planLineSymbol = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH,lineColor, 3);
        actualLineSymbol =  EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID,lineColor, 3);
        planStopPointSymbol= EsriSymbolsCreator.createSvgMarkerSymbol(stopPath,24,lineColor);

        //初始化变量
        routeGraphicsLayer = new GraphicsLayer("route_graphics_layer");

        $routeContainer=$("#routeContainer");
        $routePntsContainer=$(".route-list-container");

        //请求巡查信息
        if (inspectId != undefined) {
            fetchInspect();
            _map.addLayer(routeGraphicsLayer);
        }
        topic.subscribe('map-extent-changed', function () {
            try {
                $sgNode = $(startGra.getNode());
            } catch (e) {
                error(e);
            }
        });

    }

    /***
     * 获取巡查路线信息
     */
    function fetchInspect() {
        $.getJSON(INSPECT_FETCH_URL + inspectId, function (data) {
            if (data.success === true) {
                parseResult(data.result);
            } else {
                layer.msg(data.msg, {icon: 0});
            }
        });
    }


    var actualPath=[];
    var lineGras=[];
    var i=1;
    var t,$sgNode,startGra;
    //标题字段
    var tf="DKMC";

    //用于定位的图形jih集合
    var locFeatures =[];

    /***
     * 回放路线 todo
     */
    function parseResult(data) {
        //添加计划路线
        addRoute(data,data.inspectPlanTracks,'plan');
        //添加实际路线
        addRoute(data,data.actualInsTracks,'actual');
    }

    /***
     * 组织路线显示
     * @param tracks
     * @param type 类型 计划--plan,实际---actual
     */
    function addRoute(data,tracks,type){
        if(tracks==='[]'){
            /*if(type=="plan"){
                layer.msg("计划路线信息为空！", {icon: 0});
            }else{
                layer.msg("实际巡查路线信息为空！", {icon: 0});
            }*/
            return;
        }
        if(tracks =="")
            return;
        var paths;

        //多条路线兼容
        if($.isArray($.parseJSON(tracks)[0][0])){
            paths = $.parseJSON(tracks);
        }else{
            paths = [$.parseJSON(tracks)];
        }

        var polyLineJson = {
            "paths": paths,
            "spatialReference": _map.spatialReference
        };
        var polyLine = new Polyline(polyLineJson);

        var g;
        if(type=="plan"){
            g = new Graphic(polyLine, planLineSymbol, {});
        }else{
            g = new Graphic(polyLine, actualStaticLineSymbol, {});
        }
        routeGraphicsLayer.add(g);
        locFeatures.push(g);

        //当实际询查路线不为空的时候 只播放实际巡查路线，如果只回放计划巡查路线时候默认只播放计划路线
        if(type=="actual"||data.actualInsTracks==="[]"){
            actualPath = paths[0];
            var startPnt = new Point(actualPath[0][0], actualPath[0][1], _map.spatialReference);
            var stopPnt = new Point(actualPath[actualPath.length - 1][0], actualPath[actualPath.length - 1][1], _map.spatialReference);

            startGra=new Graphic(startPnt, planEndPointSymbol, {});
            routeGraphicsLayer.add(startGra);
            routeGraphicsLayer.add(new Graphic(stopPnt, planStopPointSymbol, {}));
        }
        MapUtils.locateFeatures(locFeatures, 2);

        $("#routeCtlBtn").off('click');
        $("#routeCtlBtn").on('click',function(){
            var $i=$(this).find("i");
            if($i.hasClass('fa-play')){
                play();
                $(this).html("<i class='fa fa-pause'></i>&nbsp;暂停");
            }else{
                _map.enablePan();
                clearTimeout(t);
                $(this).html("<i class='fa fa-play'></i>&nbsp;播放");
            }
        });

        $("#routeResetBtn").on('click',function(){
            reset();
        });

        var inspectPnts = [];
        try{
            if (data.hasOwnProperty("actualInsPoints") && data.actualInsPoints != "[]"){
                var actualPnts = $.parseJSON(data.actualInsPoints);
                if (lang.isArray(actualPnts)) {
                    arrayUtil.forEach(actualPnts, function (item) {
                        lang.mixin(item, {type: "actual"});
                    });
                    inspectPnts = inspectPnts.concat(actualPnts);
                }
            }
            if (data.hasOwnProperty("inspectPlanPoints") && data.inspectPlanPoints != "[]") {
                var planPnts = $.parseJSON(data.inspectPlanPoints);
                if (lang.isArray(planPnts)) {
                    arrayUtil.forEach(planPnts, function (item) {
                        lang.mixin(item, {type: "plan"});
                    });
                    inspectPnts = inspectPnts.concat(planPnts);
                }
            }
        }catch(e){

        }

        if (inspectPnts.length > 0)
            queryInspectPnts(inspectPnts).then(lang.hitch(this,showPnts));
        else
            console.log("没有任何巡查点信息!");

        //是否立即播放
        if (playNow) {
            setTimeout(function () {
                $("#routeCtlBtn").trigger('click');
            }, 1500);
        }

    }

    /***
     * 根据接收到的数据进行查询 并定位显示巡查地块
     * @param data
     */
    function queryInspectPnts(data) {
        var d = new Deferred();
        var queryResults = [];
        arrayUtil.forEach(data, function (item, idx) {
            var queryUrl=item.mapServerUrl;
            var dkid=item.dkId||item.dkid;
            if (queryUrl != undefined && dkid != undefined) {
                QueryTask.deferredQuery(queryUrl, "dkid='" + dkid + "'", null, null, _map).then(lang.hitch(this, function resolved(r) {
                    var obj = lang.mixin(r, item);
                    queryResults.push(obj);
                    if (idx === data.length - 1)
                        d.callback(queryResults);
                }), function error(err) {
                    console.error(err.message);
                });
            } else
                layer.msg("服务地址和dkid丢失!", {icon: 0});
        });
        return d;
    }

    var dkList=[];
    /***
     * 显示巡查地块(包括实际和计划)
     * @param data
     */
    function showPnts(data) {
        arrayUtil.forEach(data, function (item) {
            var fs = item.featureSet.features;
            if (fs.length > 0) {
                var feature = fs[0];
                item.uid = RandomUuid();
                item.gra = new Graphic(feature.geometry);
                item.title = feature.attributes[tf];
                MapUtils.highlightFeatures([ item.gra ]);
                dkList.push(item);
            }
        });
        if(dkList.length>0){
            var template = Handlebars.compile(RoutePntsTpl);
            $routePntsContainer.empty();
            $routePntsContainer.append(template({pnts: dkList}));
            //添加定位的监听
            $(".route-list-container ul li").on('click', function () {
                var uid = $(this).data("uid");
                var r = arrayUtil.filter(dkList, function (item) {
                    return item.uid === uid;
                });
                if (r.length > 0)
                    MapUtils.locateFeatures([r[0].gra]);
            });
        }
    }

    /***
     * 动态播放路线
     */
    function play() {
        if (i < actualPath.length) {
            _map.disablePan();
            var pntArray = actualPath[i];
            var path=[actualPath[i-1],actualPath[i]];
            var lineGra=new Graphic(new Polyline({"paths": [path],"spatialReference": _map.spatialReference}),actualLineSymbol,null);
            lineGras.push(lineGra);
            routeGraphicsLayer.add(lineGra);
            var screenPnt = _map.toScreen(new Point(pntArray[0], pntArray[1], _map.spatialReference));
            $sgNode=$(startGra.getNode());

            $sgNode.animate({x: screenPnt.x, y: screenPnt.y}, 'slow');
            i++;
            t = setTimeout(play, 2000);
        } else {
            reset();
        }
    }

    /**
     * 重置状态
     */
    function reset(){
        _map.enablePan();
        clearTimeout(t);
        i=1;
        //修改按钮状态
        $("#routeCtlBtn").html("<i class='fa fa-play'></i>&nbsp;播放");
        arrayUtil.forEach(lineGras,function(item){
            routeGraphicsLayer.remove(item);
        });
        lineGras=[];
        routeGraphicsLayer.remove(startGra);
        if(actualPath.length<1){
            layer.msg("实际巡查未完成！",{icon:0});
        }
        startGra=new Graphic(new Point(actualPath[0][0], actualPath[0][1], _map.spatialReference), planEndPointSymbol, {});
        routeGraphicsLayer.add(startGra);
        $sgNode=$(startGra.getNode());
    }

    /***
     * 移除事件监听
     */
    function clearEventHandles() {
        arrayUtil.forEach(routeEventHandles, function (handle) {
            if (handle != undefined) {
                if (lang.exists("remove", handle))handle.remove();
            }
        });
        routeEventHandles = [];
    }

    function clearMap() {
        MapUtils.clearMapGraphics();
    }

    return me;
});