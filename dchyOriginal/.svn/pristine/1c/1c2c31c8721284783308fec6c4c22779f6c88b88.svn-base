/**
 * 动态巡查系统
 * 制定巡查计划
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/5/24 22:36
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "dojo/on",
    "dojox/uuid/generateRandomUuid",
    "map/core/BaseWidget",
    "map/core/GeometryServiceTask",
    "map/core/QueryTask",
    "map/core/JsonConverters",
    "map/core/EsriSymbolsCreator",
    "map/utils/MapUtils",
    "esri/geometry/Point",
    "esri/geometry/Polyline",
    "esri/geometry/Polygon",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/Color",
    'esri/toolbars/draw',
    "esri/tasks/IdentifyTask",
    "esri/tasks/IdentifyParameters",
    "handlebars",
    "layer",
    "text!map/template/leas/plan-list.html"], function (declare, lang, arrayUtil, Deferred, on, generateRandomUuid, BaseWidget, GeometryServiceTask, QueryTask,
                                                        JsonConverters, EsriSymbolsCreator,MapUtils, Point, Polyline, Polygon, GraphicsLayer, Graphic,
                                                        Color, Draw, IdentifyTask, IdentifyParameters, Handlebars, layer, planListTpl) {

    var me = declare([BaseWidget], {
        onCreate: function () {
            _map = this.getMap().map();
            _planConfig = this.getConfig();
            init();
        },
        onOpen: function () {
            addListeners();
        },
        onPause: function () {
            clearEventHandle();
            clearMapState();
        }
    });

    //计划上传地址
    var PLAN_ROUTE_UPLOAD_URL = leasUrl + "/map/plan/route/upload";

    var planLineSymbol = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH,"#0091ff", 3);

    //计划路线起始点
    var planstartPointSymbol = EsriSymbolsCreator.createSvgMarkerSymbol("M512.587889 958.414259c0 0-335.035566-380.468314-335.035566-562.941206 0-182.464705 150.007479-330.35394 335.035566-330.35394 185.043437 0 335.004867 147.889235 335.004867 330.35394C847.592756 577.945945 512.587889 958.414259 512.587889 958.414259L512.587889 958.414259zM512.587889 117.615745c-155.435095 0-281.440927 123.55092-281.440927 275.922237 0 152.397922 126.005832 275.930423 281.440927 275.930423 155.434072 0 281.436834-123.533524 281.436834-275.930423C794.024723 241.166665 668.021961 117.615745 512.587889 117.615745L512.587889 117.615745zM471.923748 549.643342c-39.698141-2.345418-68.202335-17.523095-85.541235-45.561685-5.237281 22.058388-12.143575 41.34464-20.75879 57.867966-9.234315-10.050913-22.323425-23.86657-39.263236-41.49916 16.214287-31.864732 24.212448-77.653591 23.938202-137.40137l47.862078 0c0 21.413706-0.801249 41.508369-2.418073 60.238966 6.217608 15.287171 14.887059 27.603685 25.975605 36.918841L421.718299 368.334973l-94.230128 0 0-45.189202 72.907497 0L400.395668 289.353131l-60.184731 0 0-45.178969 60.184731 0 0-47.034223 51.243081 0 0 47.034223 55.331189 0 0 45.178969-55.331189 0 0 33.791617L517.438361 323.144748l0 45.189202-46.75179 0 0 40.780799 41.900296 0 0 45.189202-41.900296 0 0 46.478568c12.252045 1.471514 38.081317 2.199085 77.600379 2.199085 37.643342 0 87.812975-0.663102 150.526295-2.026146-6.489808 15.931854-12.472056 33.30964-17.959024 52.17736C581.278341 553.132816 511.622912 551.951921 471.923748 549.643342L471.923748 549.643342zM598.582449 437.762205 615.778086 437.762205c10.087753 0 16.741289-2.744507 19.923772-8.262174 3.180436-5.498224 5.379521-24.057929 6.653537-55.648414 20.665669 8.81476 37.752836 15.440667 51.222615 19.829628-3.508917 29.138644-7.487531 48.134277-11.977799 56.951083-7.218402 21.303188-26.302039 31.973202-57.202816 31.973202l-44.51689 0c-33.881668 0-50.841945-17.640776-50.841945-52.90493L529.03856 315.429019l94.994538 0 0-52.895721-102.484116 0 0-44.806485 153.325038 0 0 142.53632-94.993514 0 0 60.622706C579.879482 432.126858 586.132906 437.762205 598.582449 437.762205L598.582449 437.762205z",
        24,"#0091ff");

    //计划路线的终点
    var planStopPointSymbol = EsriSymbolsCreator.createSvgMarkerSymbol("M511.222799 957.503517c0 0-336.095711-381.673769-336.095711-564.724829 0-183.042873 150.474107-331.400782 336.095711-331.400782 185.630814 0 336.069105 148.357909 336.069105 331.400782C847.291904 575.828724 511.222799 957.503517 511.222799 957.503517L511.222799 957.503517zM511.222799 114.039289c-155.926282 0-282.329157 123.942847-282.329157 276.797164 0 152.879899 126.402875 276.804327 282.329157 276.804327 155.925259 0 282.331203-123.924427 282.331203-276.804327C793.554002 237.982136 667.148058 114.039289 511.222799 114.039289L511.222799 114.039289zM682.285826 421.196924c-43.528376-11.771091-79.540567-26.38595-108.042715-43.846624-29.758768 18.181081-64.387449 33.178657-103.910605 44.948725-6.756891-15.22475-14.378475-29.840632-22.87601-43.845601 33.751708-8.351202 63.275115-18.671245 88.530312-30.943756-11.012821-10.813277-21.13332-22.620184-30.388101-35.375695-8.761548 11.542894-17.751293 22.593578-26.995841 33.141818-10.012028-10.54824-20.770046-20.98801-32.276101-31.318286 33.752732-37.344536 59.007929-77.397764 75.765591-120.140241l47.647183 8.469905c-4.267187 9.099239-8.496511 17.952884-12.762675 26.532283l119.694079 0 0 39.442314c-15.517415 29.484521-36.289508 56.027038-62.290695 79.594803 25.01779 11.31572 54.011124 20.040429 87.015819 26.194592C695.157995 388.519688 688.776657 404.266323 682.285826 421.196924L682.285826 421.196924zM623.768061 270.840498l-90.426499 0c-0.74599 1.74269-1.622964 3.338025-2.606361 4.787026 10.994402 18.927071 25.490557 35.256992 43.50791 49.033763C595.761194 308.678268 612.26303 290.735617 623.768061 270.840498L623.768061 270.840498zM423.067017 260.510222l42.396599 17.340947c-22.765493 41.995463-46.77328 84.392062-72.036663 127.134539 17.751293-2.207271 36.659945-4.669346 56.665581-7.367805-2.260483 12.791327-3.765767 27.525913-4.524037 44.229341-39.002292 2.461051-72.637344 6.873548-100.882642 13.247722l-12.764722-41.639353c12.499685-12.035104 27.634384-33.406854 45.3867-64.113203-13.749142 0.983397-27.142174 2.087545-40.136116 3.300162l-12.762675-40.508599c12.990872-14.988366 31.645744-54.203506 55.888891-117.560486l46.161343 13.630439c-18.763342 38.566364-36.277228 72.091922-52.516074 100.593046 6.490831-0.228197 15.108093-0.611937 25.866111-1.104147C407.057391 293.461705 414.834518 277.716092 423.067017 260.510222L423.067017 260.510222zM454.212364 519.592932c-47.52848 7.621585-88.039125 14.642488-121.536031 21.023826l-7.512091-51.586912c48.777937-7.138584 92.276636-13.895475 130.553405-20.276812C454.439538 489.376747 453.948351 506.334977 454.212364 519.592932L454.212364 519.592932zM516.474406 409.052327c57.276495 9.828856 101.149724 17.687848 131.656529 23.568788l-10.885931 42.751686c-42.742477-9.080819-86.871533-17.806551-132.404566-26.158776L516.474406 409.052327 516.474406 409.052327zM666.129868 509.298472l-11.616572 47.911196c-63.512523-13.292748-126.039601-25.546839-187.54542-36.871769l12.353352-44.966121C546.363134 486.677265 608.634386 497.983775 666.129868 509.298472L666.129868 509.298472z",
        24,"#0091ff");

    var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
        EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID,"#0091ff", 2), new Color([0, 145, 255, 0.25]));

    //计划id
    var planId,userId;

    //巡查路线(wkt)
    var tracks;

    //巡查图形
    var inspectGras = [];

    var eventHandles = [];

    var drawTool, planGraphicsLayer;

    //缓冲距离
    var bufferDistance = undefined;

    //需要查询的图层
    var queryLayers = [];
    //列表展示集合
    var showLists = [];
    //字段集合
    var fields = [];

    /**
     *初始化
     */
    function init() {
        layer.config();
        //解析配置
        if(urlParams != undefined){
            if (urlParams.hasOwnProperty("planId")) {
                planId = urlParams.planId;
            } else {
                layer.msg("计划id未获取到,检查地址参数", {icon: 0});
            }
            if (urlParams.hasOwnProperty("userId")) {
                userId = urlParams.userId;
            } else {
                layer.msg("userId未获取到,检查地址参数", {icon: 0});
            }
        }

        if (_planConfig != undefined) {
            if (_planConfig.hasOwnProperty("bufferDistance")) {
                bufferDistance = _planConfig.bufferDistance;
            }
            queryLayers = _planConfig.layers;
        }
        //初始化变量
        planGraphicsLayer = new GraphicsLayer("plan_graphicsLayer");

        esri.config.defaults.io.proxyUrl = root + "/map/proxy";

        $(".plan-footer").hide();
    }

    /***
     * 添加事件监听
     */
    function addListeners() {
        //使用on要监听dom对象 不能是jquery对象
        eventHandles.push(on($("#planAddLineBtn")[0], 'click', lang.hitch(this, onAddPolyline)));
        eventHandles.push(on($("#planAddPointBtn")[0], 'click', lang.hitch(this, onAddPoint)));
        eventHandles.push(on($("#planSubmitBtn")[0], 'click', lang.hitch(this, onSubmitPlan)));
        eventHandles.push(on($("#planClearBtn")[0], 'click', lang.hitch(this, clearTracks)));
        _map.addLayer(planGraphicsLayer);
    }

    /***
     * 添加计划路线
     */
    function onAddPolyline() {
        MapUtils.clearMapGraphics();
        planGraphicsLayer.clear();
        drawTool = drawTool ? drawTool : new Draw(_map);
        on.once(drawTool, "draw-end", function (evt) {
            drawTool.deactivate();
            var g = new Graphic(evt.geometry, planLineSymbol, {});
            planGraphicsLayer.add(g);
            var path = evt.geometry.paths[0];
            tracks = JSON.stringify((evt.geometry).paths);
            var startPnt = new Point(path[0][0], path[0][1], _map.spatialReference);
            var stopPnt = new Point(path[path.length - 1][0], path[path.length - 1][1], _map.spatialReference);
            planGraphicsLayer.add(new Graphic(startPnt, planstartPointSymbol, {}));
            planGraphicsLayer.add(new Graphic(stopPnt, planStopPointSymbol, {}));
            //显示提交与清除按钮
            $(".plan-footer").show();

            GeometryServiceTask.buffer(evt.geometry, bufferDistance, lang.hitch(this, function afterBuffer(r) {
                var geos = r.geometries;
                if (geos != undefined && geos.length > 0) {
                    var bufferGeo = geos[0];
                    MapUtils.locateFeatures([new Graphic(bufferGeo, null, {})], 2);
                    layer.msg('搜索附近的地块信息...', {time: 8000});
                    query(bufferGeo).then(lang.hitch(this, afterQuery), function error(err) {
                        layer.closeAll();
                        layer.msg(err);
                    });
                }
            }), {outSpatialReference: _map.spatialReference});
        });
        drawTool.activate(Draw.POLYLINE);
    }

    /***
     * 添加额外的巡查点(地块)
     */
    function onAddPoint() {
        if (tracks == undefined) {
            layer.msg('请先绘制路线!', {icon: 0});
            return;
        }
        drawTool = drawTool ? drawTool : new Draw(_map);
        on.once(drawTool, "draw-end", function (evt) {
            drawTool.deactivate();
            query(evt.geometry).then(lang.hitch(this, afterQuery));
        });
        drawTool.activate(Draw.POINT);
    }

    /***
     * 根据图形(线/点)查询出附近的巡查地块
     * @param queryGeo
     */
    function query(queryGeo) {
        var d = new Deferred();
        var queryResults = [];
        if (queryLayers == undefined || queryLayers.length == 0) {
            d.errback("查询图层未配置!");
        } else {
            arrayUtil.forEach(queryLayers, function (item, idx) {
                var lyr = _map.getLayer(item.sId);
                if (lyr != undefined) {
                    var _url = lyr.url + "/" + item.layerIndex||0;
                    var _of = getOutFields(item);
                    QueryTask.deferredQuery(_url, undefined, queryGeo, _of, _map).then(lang.hitch(this, function resolved(r) {
                        var obj = lang.mixin(r, item);
                        queryResults.push(obj);
                        if (idx === queryLayers.length - 1)
                            d.callback(queryResults);
                    }), function error(err) {
                        layer.msg(err.message);
                        console.error(err.message);
                    });
                }
            });
        }
        return d;
    }

    /***
     * after query
     * @param data
     */
    function afterQuery(data) {
        layer.closeAll();
        arrayUtil.forEach(data, function (item) {
            if (item.featureSet) {
                var fs = item.featureSet.features;
                if (lang.isArray(fs) && fs.length > 0) {
                    var gras = [];
                    arrayUtil.forEach(fs, function (f) {
                        var attr = f.attributes;
                        var geo = f.geometry;
                        //添加额外属性
                        attr.mapUrl=item.url;
                        attr.serviceName=item.layerName;
                        attr.uid = generateRandomUuid();
                        fields = item.fields;
                        showLists.push(convert2RenderData(attr, item.fields));
                        var g = new Graphic(geo, fillSymbol, attr);
                        gras.push(g);
                        MapUtils.highlightFeature(g, false);
                        inspectGras.push(g);//存放graphic
                    });
                }
            }
        });
        if (showLists.length > 0) {
            listRender(planListTpl, showLists);
        }
    }

    /***
     *
     * @param attr
     * @param fields
     */
    function convert2RenderData(attr, fields) {
        var data = {};
        data.uid = attr.uid;
        arrayUtil.forEach(fields, function (field) {
            data[field.alias] = attr[field.name];
        });
        return data;
    }

    /**
     * 列表展示渲染方法
     * 只取第一个字段进行列表展示
     * 其余字段信息用tooltip方式展示
     * @param tmpl
     * @param _data
     */
    function listRender(tmpl, _data) {
        $("#planFoldle").empty();
        Handlebars.registerHelper("title", function (data) {
            var content = "";
            for (var k in data) {
                if (typeof data[k] != "function") {
                    if (k != "uid")
                    {
                        content += "<h4>" + ((data[k] == "null" ||data[k]===null)? "" : data[k]) + "</h4>"
                        break;
                    }
                }
            }
            return content;
        });
        var listTmpl = Handlebars.compile(tmpl);
        $("#planFoldle").append(listTmpl({items: _data}));
        var scrollHeight = $(window).height() - 108 - (40) * $("#planFoldle .planItems").length;
        var screenHeight = $(window).height()-300;
        $(".scrollContent").slimScroll({
            height: scrollHeight > screenHeight ? screenHeight : scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        //定位无属性显示
        $(".plan-locate").on('click', function (evt) {
            var uid = $(this).data("uid");
            for (var tm in inspectGras) {
                if (inspectGras[tm].attributes.uid === uid) {
                    var flashgeo = [];
                    flashgeo.push(inspectGras[tm]);
                    MapUtils.flashFeatures(flashgeo,100);
                }
            }
        });
        //捕获关闭点击事件
        $(".plan-clear").on('click', function (evt) {
            var uid = $(this).data("uid");
            //移除巡查点
            inspectGras = arrayUtil.filter(inspectGras, function (item) {
                return item.attributes.uid != uid;
            });
            //更新移除后的列表
            showLists = arrayUtil.filter(showLists, function (item) {
                return item.uid != uid;
            });
            MapUtils.highlightFeatures(inspectGras, false);
            listRender(planListTpl, showLists);
        });
    }

    /***
     * 获取配置的查询图层的返回字段
     * @param lyr
     */
    function getOutFields(lyr) {
        var r = [];
        if (lang.isArray(lyr.fields)) {
            arrayUtil.forEach(lyr.fields, function (f) {
                r.push(f.name);
            });
        }
        return r.length > 0 ? r : undefined;
    }

    /***
     * 提交计划路线
     */
    function onSubmitPlan() {
        if (planId != undefined) {
            var msgHandle = layer.msg('提交数据中...', {time: 6000});
            $.ajax({
                url: PLAN_ROUTE_UPLOAD_URL,
                method: 'POST',
                data: {
                    planId: planId,
                    tracks: tracks,
                    inspectPoints: JSON.stringify(inspectGras)
                }
            }).done(function (rp) {
                layer.close(msgHandle);
                if (rp.success == true) {
                    layer.msg('提交成功!', {icon: 1});
                }
                else
                    layer.alert('提交数据失败:' + rp.result, {icon: 0, time: 4000});
            });
        }
    }
    /**
     * 清除路线
     */
    function clearTracks() {
        tracks = undefined;
        planGraphicsLayer.clear();
        $("#planFoldle").empty();
        showLists = [];
        inspectGras = [];
        $(".plan-footer").hide();
        _map.graphics.clear();
    }

    /***
     * 清除事件句柄 避免重复监听
     */
    function clearEventHandle() {
        arrayUtil.forEach(eventHandles, function (handle) {
            if (handle != undefined) {
                if (lang.exists("remove", handle))handle.remove();
            }
        });
        eventHandles = [];
    }

    /***
     * 清除地图绘制状态 、移除要素图层等
     */
    function clearMapState() {
        if (drawTool != undefined && lang.exists("deactivate", drawTool))
            drawTool.deactivate();
        _map.removeLayer(planGraphicsLayer);
        MapUtils.clearMapGraphics();
    }

    return me;
});