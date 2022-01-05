/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:47
 * File:    MainMap
 * (c) Copyright gtmap Corp.2015
 */
define(["esri/map",
        "esri/basemaps",
        'dojo/topic',
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/on",
        "esri/layers/ArcGISDynamicMapServiceLayer",
        "esri/layers/ArcGISTiledMapServiceLayer",
        "esri/layers/layer",
        "esri/layers/FeatureLayer",
        "esri/geometry/Extent",
        "esri/SpatialReference",
        "esri/lang",
        "map/core/JsonConverters",
        "map/core/SWMTSLayer",
        "esri/layers/WMSLayer",
        "esri/layers/WMSLayerInfo",
        "esri/geometry/Point",
        "esri/dijit/BasemapToggle",
        "dijit/Menu",
        "dijit/MenuItem",
        "dijit/MenuSeparator",
        "dojo/domReady!"],
    function (Map, esriBasemaps, topic, arrayUtil, lang, on, ArcGISDynamicMapServiceLayer, ArcGISTiledMapServiceLayer, Layer, FeatureLayer,
              Extent, SpatialReference, esriLang, JsonConverters, SWMTSLayer, WMSLayer, WMSLayerInfo, Point, BasemapToggle, Menu, MenuItem,
              MenuSeparator) {

        var __map, _operationalLayers, _baseLayers;
        var cent = false;

        /**
         * 添加事件监听
         */
        function addEventListener() {
            if (__map) {
                __map.on("load", function (e) {
                    //地图加载后 进行resize等
                    __map.resize();
                    __map.reposition();
                    try {
                        var baseIndex = 0;
                        if (lang.isArray(_operationalLayers)) {
                            //对地图服务按照配置中的index顺序重新排序
                            baseIndex += _operationalLayers.length;
                            var arr = [];
                            arrayUtil.forEach(__map.layerIds, function (id, idx) {
                                arr.concat(arrayUtil.filter(_operationalLayers, function (item) {
                                    return item.id === id;
                                }));
                            });
                            arrayUtil.forEach(arr, function (item) {
                                if (esriLang.isDefined(item)) {
                                    var index = item.index;
                                    __map.reorderLayer(__map.getLayer(item.id), index);
                                }
                            });
                        }

                        //遍历baseLayers 检测是否存在"top":true,若存在 则需要reorder其至顶层
                        if (esriLang.isDefined(_baseLayers)) {
                            if (_baseLayers.length > 0) {
                                var baseLyr = _baseLayers[0];
                                if (baseLyr.top === true) {
                                    __map.reorderLayer(__map.getLayer(baseLyr.id), baseIndex + 1);
                                }
                            }
                        }
                        EventBus.trigger(EventBus.MAIN_MAP_INITIALIZED, e.target);
                    } catch (e) {
                        //console.debug(e);
                    }
                    //鼠标移动时 显示坐标点
                    __map.on("mouse-move", showCoordinates);
                    //地图右键菜单
                    addMapContextMenu();
                });

                __map.on("extent-change", function (evt) {
                    if (!cent) {
                        cent = true;
                        return;
                    }
                    topic.publish('map-extent-changed', {id: __map.id, extent: evt.extent});
                });

                topic.subscribe('map-extent-changed', function (evt) {
                    if (evt.id == __map.id) return;
                    cent = false;
                    __map.setExtent(evt.extent);
                });
            }
        }

        /***
         * 添加地图右键菜单
         */
        function addMapContextMenu() {
            var ctxLoc;
            var ctxMenuForMap = new Menu({
                onOpen: function (box) {
                    var x = box.x, y = box.y;
                    switch (box.corner) {
                        case "TR":
                            x += box.w;
                            break;
                        case "BL":
                            y += box.h;
                            break;
                        case "BR":
                            x += box.w;
                            y += box.h;
                            break;
                    }
                    var screenPoint = new Point(x - __map.position.x, y - __map.position.y);
                    ctxLoc = __map.toMap(screenPoint);
                }
            });
            ctxMenuForMap.addChild(new MenuItem({
                label: "&nbsp;清除",
                onClick: function () {
                    //清除map自身的graphics
                    __map.graphics.clear();
                    //通知各widget进行清除图形
                    topic.publish(EventBus.MAIN_MAP_GRAPHICS_REMOVED);
                }
            }));
            ctxMenuForMap.addChild(new MenuItem({
                label: "&nbsp;放大",
                onClick: function () {
                    if (__map.getMinZoom() === -1 && __map.getMaxZoom() === -1) {
                        __map.setZoom(0);
                    } else {
                        if (__map.getZoom() < __map.getMaxZoom()) {
                            __map.setZoom(__map.getZoom() + 1);
                        }
                    }
                }
            }));
            ctxMenuForMap.addChild(new MenuItem({
                label: "&nbsp;缩小",
                onClick: function () {
                    if (__map.getMinZoom() === -1 && __map.getMaxZoom() === -1) {
                        __map.setZoom(1);
                    } else {
                        if (__map.getZoom() > __map.getMinZoom()) {
                            __map.setZoom(__map.getZoom() - 1);
                        }
                    }
                }
            }));
            ctxMenuForMap.addChild(new MenuItem({
                label: "&nbsp;居中",
                onClick: function () {
                    __map.centerAt(ctxLoc);
                }
            }));
            ctxMenuForMap.addChild(new MenuSeparator());
            ctxMenuForMap.addChild(new MenuItem({
                label: "&nbsp;全屏",
                onClick: function () {
                    var dom = document.documentElement;
                    var rfs = dom.requestFullScreen || dom.webkitRequestFullScreen || dom.mozRequestFullScreen || dom.msRequestFullScreen,
                        wscript;
                    if (typeof rfs != "undefined" && rfs) {
                        rfs.call(dom);
                        return;
                    }
                    if (typeof window.ActiveXObject != "undefined") {
                        wscript = new ActiveXObject("WScript.Shell");
                        if (wscript) {
                            wscript.SendKeys("{F11}");
                        }
                    }
                }
            }));
            ctxMenuForMap.startup();
            ctxMenuForMap.bindDomNode(__map.container);
        }

        /**
         *  显示当前坐标点
         * @param evt
         */
        function showCoordinates(evt) {
            try {
                var x = evt.mapPoint.x;
                var y = evt.mapPoint.y;
                //经纬度的坐标要转换成度分秒格式的
                if (x < 180 && x > 0) {
                    $("#coordsInfo").find("span").eq(0).text(convertJwd(x));
                    $("#coordsInfo").find("span").eq(1).text(convertJwd(y));

                } else {
                    $("#coordsInfo").find("span").eq(0).text(x.toFixed(4));
                    $("#coordsInfo").find("span").eq(1).text(y.toFixed(4));
                }
            } catch (e) {
                console.log(e.message || 'id "coordsInfo" is undefined');
            }
        }

        /**
         * 转换经纬度至 度分秒
         * @param value
         * @returns {string}
         */
        function convertJwd(value) {
            var degree = Math.floor(value);
            var minute = Math.floor((value - degree) * 60);
            var second = Math.round(((value - degree) * 60 - minute) * 60);
            return degree.toString() + "°" + minute.toString() + "′" + second.toString() + "″";
        }

        /**
         * 添加图层
         * 只有visible：true的初始化地图时才加载
         * @param layer
         * @private
         */
        function _addLayer(layer, index) {
            if (layer === null) return;
            var valid = true;
            if (layer.url.startWith("/oms")) {
                //兼容相对路径
                layer.url = omsUrl.replace("/oms", "") + layer.url;
            }
            if (__map.layerIds.length === 0) {
                //验证服务是否有效,只对加载的第一个服务验证
                var lyrUrl = layer.url;
                if (!lyrUrl.startWith("http://")) {
                    lyrUrl = window.location.origin + lyrUrl;
                }
                if (layer.type === 'swmts') {
                    // swmts 类型服务不做验证
                    valid = true;
                } else {
                    var ajaxRequest = $.ajax({
                        url: '/omp/map/proxy',
                        data: {requestUrl: lyrUrl.concat("?f=json")},
                        timeout: 3000,
                        async: false,
                        success: function (data) {
                            if (data.success === false) {
                                //地图服务异常,不加载
                                valid = false;
                                console.warn(esriLang.substitute({
                                    name: layer.name,
                                    msg: data.msg
                                }, "地图服务 ${name} 无法访问，原因: ${msg}"));
                            }
                        },
                        complete: function (XMLHttpRequest, status) {
                            if (status === 'timeout') {
                                ajaxRequest.abort();
                                console.warn(esriLang.substitute({
                                    name: layer.name,
                                    msg: '访问地图服务超时'
                                }, "地图服务 ${name} 无法访问，原因: ${msg}"));
                            }
                        }
                    });
                }
            }
            if (!valid) return;
            var nLayer = undefined;
            var opt = {};
            lang.mixin(opt, layer);
            if (!esriLang.isDefined(opt.opacity)) {
                opt.opacity = opt.alpha;
            }
            try {
                switch (layer.type) {
                    case "export":
                    case "dynamic":
                        if (opt.visible === true) {
                            nLayer = new ArcGISDynamicMapServiceLayer(layer.url, opt);
                            if (opt.visibleAtMapScale == true) {
                                nLayer.visibleAtMapScale = true;
                            }
                            on.once(nLayer, 'error', layerErrorHandler);
                            __map.addLayer(nLayer, index);
                        }
                        break;
                    case "tiled":
                        if (opt.visible === true) {
                            nLayer = new ArcGISTiledMapServiceLayer(layer.url, opt);
                            if (opt.visibleAtMapScale == true) {
                                nLayer.visibleAtMapScale = true;
                            }
                            on.once(nLayer, 'error', layerErrorHandler);
                            __map.addLayer(nLayer, index);
                        }
                        break;
                    case "swmts":
                        if (opt.visible === true) {
                            //testcode  for nj
                            var wkt_nj = 'PROJCS[\"Xian_1980_3_Degree_GK_CM_118_50E\",GEOGCS[\"GCS_XiAn_1980\",DATUM[\"D_XiAn_1980\",SPHEROID[\"International_1975\",6378140.0,298.2570000000004]],PRIMEM[\"Greenwich\",0.0],UNIT[\"DEGREE\",0.017453292519943295]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",500000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",118.8333333333333],PARAMETER[\"Latitude_Of_Origin\",0.0],PARAMETER[\"Scale_Factor\",1.0],UNIT[\"METER\",1.0]]';
                            opt.fullExtent = new Extent(opt.xMinExtent, opt.yMinExtent, opt.xMaxExtent, opt.yMaxExtent, new SpatialReference({wkt: wkt_nj}));
                            opt.origin = {x: opt.xMinExtent, y: opt.yMaxExtent};
                            // 检测是否包含 "layer" 属性 若没有 默认将 name 做为 "layer" 值
                            if (!opt.hasOwnProperty("layer")) {
                                opt.layer = layer.name;
                            }
                            // nj
                            opt.wkt = wkt_nj;
                            opt.tileMatrixSet = 'Custom_'.concat(opt.layer);
                            opt.origin = {
                                "x": 452516.9228609864,
                                "y": 3610474.0237544077
                            };
                            opt.lods = [
                                {
                                    "level": 0,
                                    "resolution": 323.5633576,
                                    "scale": 1155583.42
                                },
                                {
                                    "level": 1,
                                    "resolution": 161.7816788,
                                    "scale": 577791.71
                                },
                                {
                                    "level": 2,
                                    "resolution": 80.8908380,
                                    "scale": 288895.85
                                },
                                {
                                    "level": 3,
                                    "resolution": 40.4454204,
                                    "scale": 144447.93
                                },
                                {
                                    "level": 4,
                                    "resolution": 20.2227088,
                                    "scale": 72223.96
                                },
                                {
                                    "level": 5,
                                    "resolution": 10.1113544,
                                    "scale": 0.00002769164138881335
                                },
                                {
                                    "level": 6,
                                    "resolution": 5.055677,
                                    "scale": 36111.98
                                },
                                {
                                    "level": 7,
                                    "resolution": 2.52784,
                                    "scale": 9028.0
                                },
                                {
                                    "level": 8,
                                    "resolution": 1.26392,
                                    "scale": 4514.0
                                },
                                {
                                    "level": 9,
                                    "resolution": 0.63196,
                                    "scale": 2257.0
                                },
                                {
                                    "level": 10,
                                    "resolution": 0.31598,
                                    "scale": 1128.5
                                },
                                {
                                    "level": 11,
                                    "resolution": 0.15799,
                                    "scale": 564.25
                                }
                            ];
                            opt.dpi = 90.7142857142857;
                            nLayer = new SWMTSLayer(layer.url, opt);
                            __map.addLayer(nLayer, index);
                        }
                        break;
                    case "wms":
                        var layer1 = new WMSLayerInfo();
                        var layer2 = new WMSLayerInfo();
                        var wkid = __map.spatialReference ? __map.spatialReference.wkid : 2364;
                        var resourceInfo = {
                            extent: new Extent(layer.xMinExtent, layer.yMinExtent, layer.xMaxExtent, layer.yMaxExtent, {wkid: wkid}),
                            layerInfos: [layer1, layer2]
                        };
                        nLayer = new WMSLayer(layer.url,
                            {
                                resourceInfo: resourceInfo,
                                visibleLayers: [layer.index],
                                className: layer.className,
                                id: layer.id
                            }
                        );
                        __map.addLayer(nLayer, index);
                        break;
                    default :
                        console.log(" current layer type" + layer.type + " not supported ! ");
                }
            } catch (e) {
                console.info(e);
            }
        }

        /***
         * 图层加载异常事件
         * @param evt
         */
        function layerErrorHandler(evt) {
            var tid = evt.target.id;
            var tmp = arrayUtil.filter(__map.layerIds, function (item) {
                return item === tid;
            });
            if (tmp.length > 0) {
                var lyr = __map.getLayer(tid);
                if (lyr instanceof ArcGISDynamicMapServiceLayer)
                    __map.removeLayer(lyr);
            }
            console.warn(esriLang.substitute({msg: evt.error}, '服务加载异常: ${msg}'));
        }

        /**
         * add layers
         * @param layers
         */
        function _addLayers(layers) {
            if (esriLang.isDefined(layers) && layers.length > 0) {
                //先加载静态服务 后加载动态服务
                var dynamicLyrs = [];
                arrayUtil.forEach(layers, function (value) {
                    if (value.type === "tiled") {
                        try {
                            _addLayer(value, value.index);
                        } catch (e) {
                            console.error("addLayer " + value.name + "error:" + e.message);
                        }
                    } else
                        dynamicLyrs.push(value);
                });
                arrayUtil.forEach(dynamicLyrs, function (value) {
                    try {
                        _addLayer(value, value.index);
                    } catch (e) {
                        console.error("addLayer " + value.name + "error:" + e.message);
                    }
                });
            } else {
                Pace.stop();
                console.warn("当前模板未配置地图服务!");
            }
        }

        /**
         * 移除图层
         * @param layerId
         * @private
         */
        function _removeLayerById(layerId) {
            __map.removeLayer(__map.getLayer(layerId));
        }

        /***
         * init basemaps for mainmap
         * @param layers
         * @private
         */
        function _initBaseMaps(layers) {
            var basemaps = [];
            if (layers.length > 0) {
                arrayUtil.forEach(layers, function (item) {
                    if (esriLang.isDefined(item.url)) {
                        var _name = item.name;
                        var _url = item.url;
                        if (esriLang.isDefined(_url) && _url != "") {
                            //判断是否已经添加同名的basemap 如果存在 则将服务加入该组中
                            if (esriBasemaps.hasOwnProperty(_name)) {
                                var layers = esriBasemaps[_name].baseMapLayers;
                                layers.push({url: _url});
                            } else {
                                esriBasemaps[_name] = {
                                    title: item.title,
                                    baseMapLayers: [{url: _url}],
                                    thumbnailUrl: item.thumbnailUrl === "" ? _url.concat("/info/thumbnail") : item.thumbnailUrl,
                                    top: item.top
                                }
                            }
                            //名称加入数组管理
                            basemaps.push(_name);
                        }
                    }
                });
                if (basemaps.length > 0) {
                    //加载第一个底图 并加载toggle
                    __map.setBasemap(basemaps[0]);
                    if (basemaps.length > 1) {
                        var toggleMap = basemaps.length > 1 ? basemaps[1] : basemaps[0];
                        var toggle = new BasemapToggle({
                            map: __map,
                            basemap: toggleMap
                        }, "basemapToggle");
                        toggle.startup();
                    }
                }
            }
        }

        return {
            /**
             * 初始化地图，自定义前台显示比例尺
             * @param _map_div
             * @param option
             */
            // init: function (_map_div, option) {
            //     var lods = [
            //         {
            //             "endTileCol": 288,
            //             "endTileRow": 316,
            //             "level": 0,
            //             "resolution": 76.43717985352637,
            //             "scale": 288895.85,
            //             "startTileCol": 286,
            //             "startTileRow": 313
            //         },
            //         {
            //             "endTileCol": 576,
            //             "endTileRow": 632,
            //             "level": 1,
            //             "resolution": 38.2185912496825,
            //             "scale": 144447.93,
            //             "startTileCol": 573,
            //             "startTileRow": 626
            //         },
            //         {
            //             "endTileCol": 1153,
            //             "endTileRow": 1264,
            //             "level": 2,
            //             "resolution": 19.10929430192194,
            //             "scale": 72223.96,
            //             "startTileCol": 1146,
            //             "startTileRow": 1253
            //         },
            //         {
            //             "endTileCol": 2306,
            //             "endTileRow": 2528,
            //             "level": 3,
            //             "resolution": 9.55464715096097,
            //             "scale": 36111.98,
            //             "startTileCol": 2293,
            //             "startTileRow": 2507
            //         },
            //         {
            //             "endTileCol": 4613,
            //             "endTileRow": 5057,
            //             "level": 4,
            //             "resolution": 4.777323575480485,
            //             "scale": 18055.99,
            //             "startTileCol": 4586,
            //             "startTileRow": 5014
            //         },
            //         {
            //             "endTileCol": 9226,
            //             "endTileRow": 10115,
            //             "level": 5,
            //             "resolution": 2.3886631106595546,
            //             "scale": 9028,
            //             "startTileCol": 9173,
            //             "startTileRow": 10028
            //         },
            //         {
            //             "endTileCol": 18453,
            //             "endTileRow": 20230,
            //             "level": 6,
            //             "resolution": 1.1943315553297773,
            //             "scale": 4514,
            //             "startTileCol": 18346,
            //             "startTileRow": 20057
            //         },
            //         {
            //             "endTileCol": 36906,
            //             "endTileRow": 40461,
            //             "level": 7,
            //             "resolution": 0.5971657776648887,
            //             "scale": 2257,
            //             "startTileCol": 36693,
            //             "startTileRow": 40115
            //         },
            //         {
            //             "endTileCol": 73812,
            //             "endTileRow": 80923,
            //             "level": 8,
            //             "resolution": 0.2985828888324443,
            //             "scale": 1128.5,
            //             "startTileCol": 73387,
            //             "startTileRow": 80231
            //         },
            //         {
            //             "endTileCol": 147625,
            //             "endTileRow": 161847,
            //             "level": 9,
            //             "resolution": 0.14929144441622216,
            //             "scale": 564.25,
            //             "startTileCol": 146775,
            //             "startTileRow": 160462
            //         },
            //         {
            //             "endTileCol": 295250,
            //             "endTileRow": 323694,
            //             "level": 10,
            //             "resolution": 0.07464572220811108,
            //             "scale": 282.1255,
            //             "startTileCol": 293550,
            //             "startTileRow": 320924
            //         },
            //         {
            //             "endTileCol": 590500,
            //             "endTileRow": 647388,
            //             "level": 11,
            //             "resolution": 0.03732286110405554,
            //             "scale": 141.06275,
            //             "startTileCol": 587100,
            //             "startTileRow": 641848
            //
            //         },
            //         {
            //             "endTileCol": 1181000,
            //             "endTileRow": 1294776,
            //             "level": 12,
            //             "resolution": 0.01866143055202777,
            //             "scale": 70.531375,
            //             "startTileCol": 1174200,
            //             "startTileRow": 1283696
            //
            //         },
            //         {
            //             "endTileCol": 2362000,
            //             "endTileRow": 2589552,
            //             "level": 13,
            //             "resolution": 0.009330715276013885,
            //             "scale": 35.2656875,
            //             "startTileCol": 2348400,
            //             "startTileRow": 2567392
            //
            //         },
            //         {
            //             "endTileCol": 4724000,
            //             "endTileRow": 5179104,
            //             "level": 14,
            //             "resolution": 0.004665357638006943,
            //             "scale": 17.63284375,
            //             "startTileCol": 4696800,
            //             "startTileRow": 5134784
            //
            //         },
            //         {
            //             "endTileCol": 9448000,
            //             "endTileRow": 10358208,
            //             "level": 15,
            //             "resolution": 0.002332678819003471,
            //             "scale": 8.816421875,
            //             "startTileCol": 9393600,
            //             "startTileRow": 10269568
            //         },
            //         {
            //             "endTileCol": 18896000,
            //             "endTileRow": 20716416,
            //             "level": 16,
            //             "resolution": 0.001166339409501736,
            //             "scale": 4.408210938,
            //             "startTileCol": 18787200,
            //             "startTileRow": 20539136
            //         },
            //         {
            //             "endTileCol": 37792000,
            //             "endTileRow": 41432832,
            //             "level": 17,
            //             "resolution": 0.0005831697047508678,
            //             "scale": 2.204105469,
            //             "startTileCol": 37574400,
            //             "startTileRow": 41078272
            //         },
            //         {
            //             "endTileCol": 75584000,
            //             "endTileRow": 82865664,
            //             "level": 18,
            //             "resolution": 0.0002915848523754339,
            //             "scale": 1.102052734,
            //             "startTileCol": 75148800,
            //             "startTileRow": 82156544
            //         }
            //     ];
            //     __map = new Map(_map_div, lang.mixin(option, {
            //         maxZoom: 18,
            //         maxScale: 1.102052734,
            //         logo: false,
            //         slider: false,
            //         lods: lods,
            //         autoResize: true
            //     }));
            //     addEventListener();
            // },
            /**
             * 初始化地图
             * @param _map_div
             * @param option
             */
            init: function (_map_div, option) {

                __map = new Map(_map_div, lang.mixin(option, {
                    logo: false,
                    slider: false,
                    autoResize: true
                }));
                addEventListener();
            },
            /**
             * add layers
             *
             * @param layers
             */
            addLayers: function (layers) {
                _operationalLayers = layers;
                _addLayers(layers);
            },
            /**
             * add layer
             *
             * @param layer {type:"dynamic",url:"..."}  or Layer instance
             */
            addLayer: function (layer, index) {
                _addLayer(layer, index);
            },
            /**
             * return map instance
             *
             * mainMap.map();
             *
             * @returns {*}
             */
            map: function () {
                return __map;
            },
            /**
             * remove layer by id
             *
             * @param layerId
             */
            removeLayerById: function (layerId) {
                _removeLayerById(layerId);
            },
            /**
             * get map default graphics layer
             *
             * @returns {*}
             */
            getDefaultGraphicsLayer: function () {
                return __map.graphics;
            },
            /**
             * add Graphics
             *
             * @param graphics
             */
            addGraphics: function (graphics) {
                $.each(graphics, function (index, value) {
                    __map.graphics.add(value);
                })
            },
            /**
             * remove graphics
             *
             * @param graphics
             */
            removeGraphics: function (graphics) {
                $.each(graphics, function (index, value) {
                    __map.graphics.remove(value);
                })
            },
            /**
             * clear default graphics layer
             *
             */
            clearDefaultGraphicsLayer: function () {
                __map.graphics.clear();
            },
            /**
             * change base layers
             *
             * @param layer [{type:"dynamic",url:"..."}]  or Layer instance
             */
            changeBaseLayer: function (layers) {
                //@deprecated
            },

            /***
             * init basemaps of mainmap
             * @param layers
             */
            initBaseMaps: function (layers) {
                _baseLayers = layers;
                _addLayers(layers);
                //_initBaseMaps(layers);
            },
            /**
             * set to extent
             * @param extent
             */
            setExtent: function (extent) {
                if (!__map) {
                    throw null;
                }
                extent.spatialReference = __map.spatialReference;
                return __map.setExtent(new Extent(extent));
            },
            /***
             * set default scale
             * @param scale
             */
            setScale: function (scale) {
                __map.setScale(scale);
            },
            /**
             * resize map
             */
            resize: function () {
                try {
                    __map.resize(true);
                    __map.reposition();
                } catch (e) {
                    console.error(e);
                }
            }
        };

    });