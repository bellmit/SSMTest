define(["dojo/_base/declare",
    "dojo/_base/lang",
    "map/core/BaseWidget",
    "esri/tasks/PrintTemplate",
    "esri/tasks/PrintParameters",
    "esri/tasks/PrintTask",
    "map/manager/MapManager",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "dojo/_base/array",
    "esri/request",
    "dojo/json",
    "esri/graphic",
    "esri/toolbars/draw",
    "dojo/on",
    'esri/symbols/SimpleFillSymbol',
    "map/core/EsriSymbolsCreator"
], function (declare, lang, BaseWidget, PrintTemplate, PrintParameters, PrintTask,MapManager, ArcGISTiledMapServiceLayer,
             ArcGISDynamicMapServiceLayer, arrayUtils, esriRequest, Json, Graphic,Draw,on,SimpleFillSymbol,EsriSymbolsCreator) {
    var drawTool,drawHandler;
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            var mapManager = MapManager.getInstance();
            var ScreenShotConfig = this.getConfig();
            var ScreenShotUrl = ScreenShotConfig.url;
            var _map = map.map();
            var mapx = {
                mapOptions: {},
                operationalLayers: [],
                baseMap: {
                    title: "omp_base_map",
                    baseMapLayers: []
                },
                exportOptions: {},
                layoutOptions: {}
            };

            function print(agsurl,extent,size) {
                mapx.operationalLayers = [];
                //组织map参数
                mapx.mapOptions.extent = extent;
                mapx.mapOptions.spatialReference = _map.spatialReference;
                mapx.mapOptions.scale = _map.getScale();
                mapx.mapOptions.showAttribution = true;

                //组织export参数
                mapx.exportOptions.dpi = 96;
                mapx.exportOptions.outputSize = size;

                //组织operationalLayers参数 注意替换图层的url为实际的ags的url
                for (var i = 0; i < _map.layerIds.length; i++) {
                    var id = _map.layerIds[i];
                    var currentLayer = _map.getLayer(id);

                    if (currentLayer instanceof ArcGISTiledMapServiceLayer) {
                        if (getRealBaseLyrId(currentLayer) != undefined) {
                            id = getRealBaseLyrId(currentLayer);
                        }
                        mapx.baseMap.baseMapLayers.push({
                            type: 'tiled',
                            url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url
                        });
                    } else if (currentLayer instanceof ArcGISDynamicMapServiceLayer) {
                        mapx.operationalLayers.push({
                            id: id,
                            title: id,
                            opacity: currentLayer.opacity,
                            minScale: currentLayer.minScale,
                            maxScale: currentLayer.maxScale,
                            url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url,
                            layers: window.definitionExpression ? window.definitionExpression[id] : null
                        });
                    }
                }

                var map_graphics_layer = {
                    id: "",
                    featureCollection: {
                        layers: [
                            {
                                layerDefinition: {},
                                featureSet: {
                                    geometryType: "",
                                    features: []
                                }
                            }
                        ]
                    }
                };
                var map_graphics_layer_all_points = {
                    id: "map_graphics_layer_all_points",
                    featureCollection: {
                        layers: [
                            {
                                layerDefinition: {
                                    name: "pointLayer",
                                    geometryType: "esriGeometryPoint"
                                },

                                featureSet: {
                                    geometryType: "esriGeometryPoint",
                                    features: []
                                }
                            }
                        ]
                    }
                };

                var map_graphics_layer_all_ploylines = {
                    id: "map_graphics_layer_all_ploylines",
                    featureCollection: {
                        layers: [
                            {
                                layerDefinition: {
                                    name: "polylineLayer",
                                    geometryType: "esriGeometryPolyline"
                                },
                                featureSet: {
                                    geometryType: "esriGeometryPolyline",
                                    features: []
                                }
                            }
                        ]
                    }
                };

                var map_graphics_layer_all_ploygons = {
                    id: "map_graphics_layer_all_ploygons",
                    featureCollection: {
                        layers: [
                            {
                                layerDefinition: {
                                    name: "polygonLayer",
                                    geometryType: "esriGeometryPolygon"
                                },

                                featureSet: {
                                    geometryType: "esriGeometryPolygon",
                                    features: []
                                }
                            }
                        ]
                    }
                };
                var graphicsIds = [_map.graphics.id].concat(_map.graphicsLayerIds);
                //组织map的graphics参数
                for (var j = 0, layers_length = graphicsIds.length; j < layers_length; j++) {
                    var currentLayer = _map.getLayer(graphicsIds[j]);
                    //只添加显示的图层 隐藏的不考虑
                    if (currentLayer != undefined && currentLayer.visible == true) {
                        map_graphics_layer.id = currentLayer.id;
                        for (var n = 0, graphics_length = currentLayer.graphics.length; n < graphics_length; n++) {
                            var graphic = new Graphic(currentLayer.graphics[n].geometry, currentLayer.graphics[n].symbol);
                            if (graphic.geometry.type == "point") {
                                if (graphic.geometry.x == 0 && graphic.geometry.y == 0) {
                                    continue;
                                }

                                map_graphics_layer_all_points.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                                // map_graphics_layer_all_points.featureCollection.layers[0].featureSet.features.push(graphic.toJson());
                            } else if (graphic.geometry.type == "polyline") {
                                // var len = map_graphics_layer_all_ploylines.featureCollection.layers.length;
                                // map_graphics_layer_all_ploylines.featureCollection.layers[1 + len].featureSet.features.push(graphic.toJson());
                                map_graphics_layer_all_ploylines.featureCollection.layers[j].featureSet.features.push(graphic.toJson());
                            } else if (graphic.geometry.type == "polygon") {
                                // var len = map_graphics_layer_all_ploygons.featureCollection.layers.length;
                                // map_graphics_layer_all_ploygons.featureCollection.layers[len - 1].featureSet.features.push(graphic.toJson());
                                // map_graphics_layer_all_ploygons.featureCollection.layers[j-1].featureSet.features.push(graphic.toJson());
                                map_graphics_layer_all_ploygons.featureCollection.layers[0].featureSet.features.push(graphic.toJson());
                            }
                        }
                    }
                }
                // 注意图层push顺序，直接决定打印显示结果，遵循面——线——点的先后顺序，防止要素压盖
                mapx.operationalLayers.push(map_graphics_layer_all_ploygons);
                mapx.operationalLayers.push(map_graphics_layer_all_ploylines);
                mapx.operationalLayers.push(map_graphics_layer_all_points);

                $.ajax({
                    type: "POST",
                    url: root + "/map/proxy?requestUrl=" + ScreenShotUrl.concat("/execute"),
                    data: {
                        Web_Map_as_JSON: Json.stringify(mapx),
                        Format: "PNG32",
                        Layout_Template: "MAP_ONLY",
                        method: 'post',
                        f: 'json'
                    },
                    success: function (evt) {
                        var result = $.parseJSON(evt);
                        var target = result.results[0];
                        var val = target.value.url;
                        if (val != undefined) {
                            var img = new Image();
                            var canvas = document.createElement("canvas");
                            img.setAttribute("crossOrigin", "anonymous");
                            img.src = val;
                            img.onload = function () {
                                canvas.width = img.width;
                                canvas.height = img.height;
                                var context = canvas.getContext("2d");
                                context.drawImage(img, 0, 0, img.width, img.height);
                                var url = canvas.toDataURL("image/png"); //得到图片的base64编码数据
                                var a = document.createElement("a"); // 生成一个a元素
                                a.href = url;

                                var event = new MouseEvent("click"); // 创建一个单击事件
                                a.download = name || ""; // 设置图片名称
                                a.href = url; // 将生成的URL设置为a.href属性
                                a.dispatchEvent(event); // 触发a的单击事件
                            }
                        }
                    },
                    complete: function (evt) {
                    }

                });
            }

            function getRealBaseLyrId(mapLyr) {
                var ret = undefined;
                if (appConfig.map.baseLayers.length > 0) {
                    var tmpArr = arrayUtils.filter(appConfig.map.baseLayers, function (lyr) {
                        return lyr.url === mapLyr.url;
                    });
                    if (tmpArr.length > 0) {
                        ret = tmpArr[0].id;
                    }
                }
                return ret;
            }

            $("#fullView").on("click", function () {
                var l = _map.layerIds.length;
                var root = "/" + window.location.pathname.split("/")[1];
                var lyrs = [];
                var extent = _map.extent;
                var size=[];
                var x=$("#main-map_layers").find("svg").attr("width");
                var y=$("#main-map_layers").find("svg").attr("height");
                size.push(x);
                size.push(y);
                for (var i = 0; i < l; i++) {
                    var tmp = _map.getLayer(_map.layerIds[i]);
                    if (tmp.url) {
                        var bId = getRealBaseLyrId(tmp);
                        lyrs.push(bId === undefined ? tmp.id : bId);
                    }
                }
                var request = new esriRequest({
                    "url": root + '/map/agsurl',
                    "content": {"layers": Json.stringify(lyrs)}
                });

                request.then(function result(resp) {
                    print(resp,extent,size);
                })});
            //     var printTask = new PrintTask("http://100.100.100.9:6080/arcgis/rest/services/GP/%E6%89%93%E5%8D%B0%E5%9C%B0%E5%9B%BE/GPServer/Export%20Web%20Map");
            //     var template = new PrintTemplate();
            //
            //
            //     template.exportOptions = {
            //         width: 980,
            //         height: 587,
            //         dpi: 96
            //     };
            //     template.format = "PNG32";
            //     template.layout = "MAP_ONLY";
            //     template.preserveScale = false;
            //     var params = new PrintParameters();
            //     params.map = _map;
            //     params.template = template;
            //     printTask.execute(params);
            // });
            $("#handChoose").on("click", function () {
                _map.graphics.clear();
                drawTool = new Draw(_map);
                drawTool.fillSymbol=EsriSymbolsCreator.defaultFillSymbol;
                if (drawTool != undefined) drawTool.deactivate();
                if (drawHandler != undefined) drawHandler.remove();
                drawTool = drawTool ? drawTool : new Draw(_map);
                drawHandler = on(drawTool, "draw-end", lang.hitch(this,function (evt) {
                    var symbol = drawTool.fillSymbol;
                    _map.graphics.add(new Graphic(evt.geometry,symbol));
                    drawTool.deactivate();
                }));
                drawTool.activate(Draw.EXTENT);
            });
            $("#decide").on("click", function () {
                if(_map.graphics.graphics!==null){
                    var l = _map.layerIds.length;
                    var root = "/" + window.location.pathname.split("/")[1];
                    var lyrs = [];
                    var extent = _map.graphics.graphics[0].geometry;
                    var size=[];
                    var x=$("#main-map_layers").find("rect").attr("width");
                    var y=$("#main-map_layers").find("rect").attr("height");
                    size.push(x);
                    size.push(y);
                    for (var i = 0; i < l; i++) {
                        var tmp = _map.getLayer(_map.layerIds[i]);
                        if (tmp.url) {
                            var bId = getRealBaseLyrId(tmp);
                            lyrs.push(bId === undefined ? tmp.id : bId);
                        }
                    }
                    var request = new esriRequest({
                        "url": root + '/map/agsurl',
                        "content": {"layers": Json.stringify(lyrs)}
                    });

                    request.then(function result(resp) {
                        print(resp,extent,size);
                    })
                }
            });
            $("#reChoose").on("click", function () {
                _map.graphics.clear();
            })
        },

        onPause: function () {
        },

        onOpen: function () {
        }
    });
    return me;
});
