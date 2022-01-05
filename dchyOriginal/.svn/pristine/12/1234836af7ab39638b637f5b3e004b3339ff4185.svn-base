/**
 * 常州出图
 * Date:  2017/7/13 13:40
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "dojo/_base/Deferred",
    "dojo/json",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/tasks/IdentifyTask",
    "esri/tasks/FeatureSet",
    "esri/tasks/IdentifyParameters",
    "esri/geometry/Geometry",
    "esri/geometry/Polygon",
    "esri/toolbars/draw",
    "esri/Color",
    "esri/config",
    "esri/request",
    'esri/symbols/SimpleFillSymbol',
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "map/core/GeometryIO",
    "map/core/EsriSymbolsCreator",
    "map/component/MapInfoWindow",
    "map/utils/MapUtils",
    "mustache",
    "layer",
    "handlebars",
    "map/core/GeoDataStore",
    "map/core/support/Environment",
    "static/thirdparty/h-ui/js/H-ui"
], function (declare, lang, arrayUtil, on, Deferred, Json, RandomUuid, GraphicsLayer, Graphic, IdentifyTask, FeatureSet, IdentifyParameters,
             Geometry, Polygon, Draw, Color, esriConfig, esriRequest, SimpleFillSymbol, ArcGISTiledMapServiceLayer, ArcGISDynamicMapServiceLayer, BaseWidget, JsonConverters,
             GeometryIO, EsriSymbolsCreator, MapInfoWindow, MapUtils, Mustache, layer, Handlebars, GeoDataStore, Environment) {

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
            _map = map.map();
            _exportMapConfig = this.getConfig();
            _init();
            _exportMapInit();

        },
        /**
         *
         */
        onPause: function () {
            if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").hide();
            _pause();
        },
        /**
         *
         */
        onOpen: function () {
            if (_map.getLayer("graphics4Print")) _map.getLayer("graphics4Print").show();
            _addListener();
        },
        onDestroy: function () {
            if (_map.getLayer("graphics4Print")) _map.removeLayer(_map.getLayer("graphics4Print"));
        }
    });

    // var lineSymbol = EsriSymbolsCreator.defaultLineSymbol;

	//蓝色
    var fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_NULL, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([44, 103, 241]), 2),
        new Color([0, 145, 255, 0.0]));
	//红色
	 var otherSymbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_NULL, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([255, 0, 0]), 2),
        new Color([255,0 ,0, 0.0]));


    var _map, geometryIO, graphicsLyr, _exportMapConfig;

    //选择/导入的数据集合
    var dataList = [];

    //存放配置的可选择别图层
    var _selectLayers = [];

    var $optContainer, $printPanel, $print, $clear, $fileInput, $handDrawing;

    var itemsTpl, exportSelTpl;

    var toolbar,selectColor,controlId = [];//绘制变量

    var geoDataStore = GeoDataStore.getInstance();

    var printUrl, msgHandler;
    var templateNames, printFormates, authorText, copyrightText, titleText, watermake;
	var userName;
	var selectLayersId;
	var arr = [];

    /***
     * init
     * @private
     */
    function _init() {
		
        layer.config();
        geometryIO = new GeometryIO();
        //初始化对象
        graphicsLyr = new GraphicsLayer({id: 'import'.concat(Math.random().toString())});
        _map.addLayer(graphicsLyr);

        //初始化dom变量
        $optContainer = $(".io-opt-container");
        $print = $("#exportMap");
        $clear = $("#clearMap");

        $handDrawing = $("#handDrawing");//绘图
        // $printPanel=$("#exportMapPanel");
        $fileInput = $('#export-map-file-input');

        //初始化其他
        MapUtils.setMap(_map);

        itemsTpl = $("#items").html();
        exportSelTpl = $("#export-select-tpl").html();
        toolbar = new Draw(_map);

		//获取转换的userName
		var urlId = getUrlParams().userId
           
           var url_ =  "http://10.4.1.15:8080/portal/config/user/info?userId="+ urlId;
             $.ajax({
                url: url_,
                dataType:"json",
                success: function (data) {
									
                     userName = data.user.userName;
                    
                }
            });
		
    }
	
    /***
     * 添加事件监听
     * @private
     */
    function _addListener() {

        listenerFileInput();
        //监听头部按钮事件
        $optContainer.off('click', 'a');
        $optContainer.on('click', 'a', function () {
            var o = $(this).data("opt");
            switch (o) {
                case 'imp':
                    break;
                case 'clear':
                    _clear();
					 break;
                case 'handDrawing':
                    toolbar.activate(Draw.POLYGON);
                    toolbar.on("draw-end", function (evt) {
                        var graphic1 = new Graphic(evt.geometry, fillSymbol);
                        _map.graphics.add(graphic1);
                        toolbar.deactivate();
                    });
                    break;
            }
        });
    }

    /***
     * 文件导入监听
     * 注：此处一定要用 $('#io-file-input') 不能用变量
     */
    function listenerFileInput() {
        $('#export-map-file-input').off('change');
        $('#export-map-file-input').on('change', function () {
            //此处一定要用 $('#io-file-input') 不能用变量
            try {
                geometryIO.impFromFile($('#export-map-file-input'), _map.spatialReference, true).then(function (data) {
                    var featureSet = data.fs;
                    var token = data.token;
                    var dwgFileName = data.fileName;
                    if (featureSet.hasOwnProperty("features")) {
                        renderFeatures(featureSet.features, token, dwgFileName);
                    }
                }, function (msg) {
                    layer.msg(msg, {icon: 2, time: 3000});
                });
            } catch (e) {
            }
            timeout = setTimeout(function () {
                listenerFileInput();
            }, 2000);
        });
    }

    /***
     * 渲染图形要素(选择/导入)
     * @param features
     */
    function renderFeatures(features, token, dwgFileName) {
        if (lang.isArray(features)) {
            //清空已有的列表
            dataList = [];
            arrayUtil.forEach(features, function (feature) {
                var geometry = feature.geometry;
                if (geometry.type == undefined) geometry = new Polygon(geometry);//默认是polygon
                var attributes = generateProperties(feature.attributes, token, dwgFileName);
				selectColor = $("#selectColor  option:selected").text();
				 var color;
                if(selectColor == "红色"){
                    $("#selectColor").find("option[name='red']").attr("selected",true);
                    color = otherSymbol;
                }else{
					$("#selectColor").find("option[name='blue']").attr("selected",true);
                    color = fillSymbol;
                }

                var gra = new Graphic(geometry, color, attributes);

                //添加至图层显示以及添加至结果列表
                graphicsLyr.add(gra);
                dataList.push({id: RandomUuid(), graphic: gra, data: attributes});
            });

            for (var i in dataList) {
                var item = dataList[i];
                if (item.graphic != undefined) {
                    MapUtils.locateFeatures([item.graphic], 4.9);
                    break;
                }
            }

            //显示打印清除按钮
            $print.show();
            $clear.show();
            $handDrawing.show();
        }
    }


    /***
     * 生成图形的属性信息
     * @param attr
     * @param token
     */
    function generateProperties(attr, token, dwgFileName) {
        var prop = {};
        //选择的图形带有属性
        if (attr != undefined && token.url != undefined) {
            var arr = arrayUtil.filter(_selectLayers, function (lyr) {
                return token.url === lyr.url;
            });
            if (arr.length > 0) {
                var tf = arr[0].titleField;
                var rf = arr[0].returnFields;
                if (attr["xmmc"]) {
                    prop.title = arr["XMMC"];
                } else if (attr["XMMC"]) {
                    prop.title = arr["xmmc"];
                } else {
                    prop.title = attr[tf.name] || attr[tf.alias];
                }
                arrayUtil.forEach(rf, function (item) {
                    var val = attr[item.name] || attr[item.alias];
                    if (item.name != tf.name && prop.subtitle == undefined)
                        prop.subtitle = val;
                    else
                        prop[item.alias] = val;
                });
            }
        } else {
            if (attr["xmmc"]) {
                prop.title = attr["xmmc"];
            } else if (attr["XMMC"]) {
                prop.title = attr["XMMC"];
            } else {
                if (dwgFileName)
                    prop.title = dwgFileName.concat("-").concat(dataList.length + 1);
                else
                    prop.title = "导入图形 ".concat(dataList.length + 1);
            }
        }
        return prop;
    }


    /***
     * 切换当前widget后 移除相关显示图层以及数据
     * @private
     */
    function _pause() {
        //var id= graphicsLyr.id;
        //var lyr=_map.getLayer(id);
        //if(lyr!=null){
        //    _map.removeLayer(lyr);
        //}
        _map.graphics.clear();
    }

    /***
     * 清除相关变量
     * @private
     */
    function _clear() {
        if (dataList.length > 0) {
            graphicsLyr.clear();
            dataList = [];
        }
        _map.graphics.clear();

        //清除导入的共享数据
        geoDataStore.remove(GeoDataStore.SK_UPLOAD);
        var fileInput = document.getElementById('export-map-file-input');
        fileInput.outerHTML = fileInput.outerHTML;
        listenerFileInput();
        //隐藏打印清除按钮
        /* $print.hide();
         $clear.hide();*/

        _map.graphics.clear();//清除绘制图形
    }


    var root = "/" + window.location.pathname.split("/")[1];
    /***
     * 作为 JSON 的 Web 地图:
     * @type {{mapOptions: {}, operationalLayers: Array, baseMap: {title: string, baseMapLayers: Array}, exportOptions: {}, layoutOptions: {}}}
     */
    var web_map_as_json = {
        mapOptions: {},
        operationalLayers: [],
        baseMap: {
            title: "omp_base_map",
            baseMapLayers: []
        },
        exportOptions: {},
        layoutOptions: {}
    };
    var format, layout_template;

    /***
     * 初始化页面
     * @private
     */

    function _exportMapInit() {
        _map = map.map();
        layer.config();
        esriConfig.defaults.io.proxyUrl = "/omp/proxy.jsp";
        if (_exportMapConfig) {
            printUrl = _exportMapConfig.url;
            var printInfo = new esriRequest({
                "url": _exportMapConfig.url,
                "content": {"f": "json"}
            });
            printInfo.then(handlePrintInfo, handleError);

            //获取配置中默认的各项参数值
            authorText = _exportMapConfig.author;
            titleText = _exportMapConfig.title;
            copyrightText = _exportMapConfig.copyright;
            watermake = _exportMapConfig.author;


        }

    }

    /**
     * 解析打印服务信息
     * @param resp
     */
    function handlePrintInfo(resp) {

        var layoutTemplate, formats, mapOnlyIndex;
        layoutTemplate = arrayUtil.filter(resp.parameters, function (param, idx) {
            return param.name === "Layout_Template";
        });
        formats = arrayUtil.filter(resp.parameters, function (param, idx) {
            return param.name === "Format";
        });
        if (layoutTemplate.length == 0) {
            console.log("print service parameters name for templates must be \"Layout_Template\"");
            return;
        }
        if (formats.length == 0) {
            console.log("print service parameters name for formates must be \"Format\"");
            return;
        }
        templateNames = layoutTemplate[0].choiceList;
        mapOnlyIndex = arrayUtil.indexOf(templateNames, "MAP_ONLY"); // 把MAP_ONLY加到templateNames的底部
        if (mapOnlyIndex > -1) {
            var mapOnly = templateNames.splice(mapOnlyIndex, mapOnlyIndex + 1)[0];
            templateNames.push(mapOnly);
        }
        printFormates = formats[0].choiceList;
        renderComponents();
    }

    /***
     * 渲染页面
     */
    function renderComponents() {
        var tmpl = $("#exportMapFormTpl").html();
        
        Mustache.parse(tmpl);
        var form = Mustache.render(tmpl, {
            templates: templateNames,
            formats: printFormates,
            author: authorText,
            copyright: copyrightText,
            title: titleText
        });
        $("#exportMapPanel").append(form);
        addListeners();
        //label变换
        typeof(urlParams.proid) != "undefined" ? $("#mappingPeople").css('display', 'none') : $("#fillText").css('display', 'none');
		if( typeof(urlParams.proid) != "undefined"){
			$("#editText").css('display', 'none');
			$("#chooseColor").css('display', 'none');
		}
    }

    /**
     * 添加事件监听
     */
    function addListeners() {
        $("#exportMap").on('click', function () {
            getActualAGSServices();
        });
    }

    /***
     *
     */
    function getActualAGSServices() {
        var l = _map.layerIds.length;
        var lyrs = [];
        for (var i = 0; i < l; i++) {
            var tmp = _map.getLayer(_map.layerIds[i]);
            if (tmp.url) {
                lyrs.push(tmp.id);
            }
        }
        var request = new esriRequest({
            "url": root + '/map/agsurl',
            "content": {"layers": Json.stringify(lyrs)}
        });

        request.then(function result(resp) {
            print(resp);
        }, handleError);
    }

    /**
     * DPF添加
     * 获取当前时间
     * @returns {string}
     */

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "年";
        var seperator3 = "月";
        var seperator4 = "日";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator3 + strDate + seperator4;
        /*  + " " + date.getHours() + seperator2 + date.getMinutes()
          + seperator2 + date.getSeconds()*/
        return currentdate;
    }


  

    /**
     * 输出
     */

    function print(agsurl) {
		
		selectLayersId = _exportMapConfig.selectLayersId;
		
		var text_sjzx = $("#text_sjzx").val();
       

       // web_map_as_json.operationalLayers = [];
        //组织map参数
        web_map_as_json.mapOptions.extent = _map.extent;
        web_map_as_json.mapOptions.spatialReference = _map.spatialReference;
        web_map_as_json.mapOptions.scale = _map.getScale();
        web_map_as_json.mapOptions.showAttribution = true;

        var arr = $("#printSetForm").serializeArray();
        format = "JPG";
        layout_template = "A4 Portrait";


        //DPF添加
        var custom;
		var sjzx_date = getNowFormatDate();
        if (typeof(urlParams.proid) != "undefined") {
			
            custom = arr[1].value;
            watermake = userName;
            ywxt_text = arr[1].value;
        } else {
            typeof(watermake) == "undefined" || watermake == "" ? watermake = "常州市国土资源局" : watermake = arr[1].value;
            custom = "";
            
			if(text_sjzx != ""){
				ywxt_text = text_sjzx;
			}else{
				ywxt_text = "";
			}
            
        }

		

		
        web_map_as_json.layoutOptions.customTextElements = [{custom: custom}, {watermake: watermake}, {sjzx_date: sjzx_date}, {ywxt_text: ywxt_text}];
        //web_map_as_json.layoutOptions.name = name;

        //组织layout参数
        web_map_as_json.layoutOptions.titleText = arr[0].value;
		
        web_map_as_json.layoutOptions.authorText = arr[1].value;
        web_map_as_json.layoutOptions.copyrightText = arr[2].value;
        web_map_as_json.layoutOptions.scaleBarOptions = {};
		
		 

        //web_map_as_json.layoutOptions.legendOptions = {operationalLayers: web_map_as_json.operationalLayers};
        //
        //组织export参数
        web_map_as_json.exportOptions.dpi = 96;
        web_map_as_json.exportOptions.outputSize = [800, 1100];
		
		
		var legendLayers = [];
		
          //组织operationalLayers参数 注意替换图层的url为实际的ags的url
        for (var i = 0; i < _map.layerIds.length; i++) {
			 //web_map_as_json.operationalLayers = [];
            var id = _map.layerIds[i];
            var currentLayer = _map.getLayer(id);
            var lyrData = {};
            if (currentLayer instanceof ArcGISTiledMapServiceLayer) {
			
                lyrData = {
                    id: id,
                    type: 'tiled',
                    url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url
                };
                web_map_as_json.baseMap.baseMapLayers.push(lyrData);

				
            } else if (currentLayer instanceof ArcGISDynamicMapServiceLayer) {
				
                lyrData = {
                    id: id,
                    title: id,
                    opacity: currentLayer.opacity,
                    minScale: currentLayer.minScale,
                    maxScale: currentLayer.maxScale,
                    url: agsurl.hasOwnProperty(id) ? agsurl[id] : currentLayer.url,
                    layers: window.definitionExpression ? window.definitionExpression[id] : null
                };
                
            }
			
			web_map_as_json.operationalLayers.push(lyrData);//静态和动态都可以添加图例
			
			 $.each(selectLayersId,function(i,n){
            		if(lyrData.id === n){
					 legendLayers.push(lyrData);
					
				}

            })
        }
		

     //web_map_as_json.layoutOptions.legendOptions = {operationalLayers: lyrData};
	
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

        //所有点
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
        // map_graphics_layer_all_ploylines 存放当前map的graphics_layer的ploylines
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

        // map_graphics_layer_all_ploygons 存放当前map的graphics_layer的ploygons
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

// map_graphics_layer_all_ploygons 存放当前map的graphics_layer的ploygons
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

        //删除数组制定元素
        function removeByValue(arr, val) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == val) {
                    arr.splice(i, 1);
                    break;
                }
            }
        }

        var geomeryAll = _map.graphics.graphics;


        //组织map的graphics参数
        //单独添加 默认graphicsLayer的要素
        arrayUtil.forEach(geomeryAll, function (g) {
            switch (g.geometry.type) {
                case 'point':
                   
                    map_graphics_layer_all_points.featureCollection.layers[0].featureSet.features.push(g.toJson());
                    break;
                case 'polyline':
                    map_graphics_layer_all_ploylines.featureCollection.layers[0].featureSet.features.push(g.toJson());
                    break;
                case 'polygon':
                    map_graphics_layer_all_ploygons.featureCollection.layers[0].featureSet.features.push(g.toJson());
                    break;
            }
        });

        for (var j = 0, layers_length = _map.graphicsLayerIds.length; j < layers_length; j++) {
            var currentLayer = _map.getLayer(_map.graphicsLayerIds[j]);

            //只添加显示的图层 隐藏的不考虑
            if (currentLayer.visible == true && currentLayer.id != _map.graphics.id) {
                map_graphics_layer.id = currentLayer.id;
                for (var n = 0, graphics_length = currentLayer.graphics.length; n < graphics_length; n++) {
                    var graphic = new Graphic(currentLayer.graphics[n].geometry, currentLayer.graphics[n].symbol);


                    if (graphic.geometry.type == "point") {
                      
                        map_graphics_layer_all_points.featureCollection.layers[0].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polyline") {
                        var len = map_graphics_layer_all_ploylines.featureCollection.layers.length;
                        map_graphics_layer_all_ploylines.featureCollection.layers[1 + len].featureSet.features.push(graphic.toJson());
                    }
                    else if (graphic.geometry.type == "polygon") {
                        var len = map_graphics_layer_all_ploygons.featureCollection.layers.length;
                        map_graphics_layer_all_ploygons.featureCollection.layers[len - 1].featureSet.features.push(graphic.toJson());
                        console.log("success");
                    }
                }
            }
        }
        // 注意图层push顺序，直接决定打印显示结果，遵循面——线——点的先后顺序，防止要素压盖
        //web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploygons);//绘制图层

       

		function unique(arr) {
		  var result = [], isRepeated;
		  for (var i = 0, len = arr.length; i < len; i++) {
			isRepeated = false;
			for (var j = 0, len = result.length; j < len; j++) {
			  if (arr[i].id == result[j].id) {  
				isRepeated = true;
				break;
			  }
			}
			if (!isRepeated) {
			  result.push(arr[i]);
			}
		  }
		  return result;
		}
		
		//web_map_as_json.operationalLayers = unique(web_map_as_json.operationalLayers);
		web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploygons);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_ploylines);
        web_map_as_json.operationalLayers.push(map_graphics_layer_all_points);
		
		web_map_as_json.layoutOptions.legendOptions = {operationalLayers: legendLayers};//
      
		//url : root + "/map/proxy?requestUrl=" + printUrl.concat("/execute")
        $.ajax({
            type: "POST",
            url: printUrl.concat("/execute"),
            data: {
                Web_Map_as_JSON: Json.stringify(web_map_as_json),
                Format: format,
                Layout_Template: layout_template,
                method: 'post',
                f: 'json'
            },
            beforeSend: function () {
               // setTimeout(function () {
                    msgHandler = layer.msg('处理中...', {time: 15000});
                    $("#printBtn").addClass('disabled');
              //  }, 1000)

            },
            success: function (evt) {
				
                var result = $.parseJSON(evt);
				if (result.hasOwnProperty('error'))
				{
					layer.closeAll();
					layer.msg(result.error.message, {icon: 0, time: 4000});
					return false;
				}
                var target = result.results[0];
                var val = target.value.url;

				 $.ajax({
                    url: root + "/file/file/save",
                    data: {fileUrl: val},
                    success: function (r) {
                        
                        var id = r.id;
                        if (id) {
                            var index1 = window.location.origin+"/omp/static/js/map/widgets/TP/cz/ExportMap/index3.html";
                            window.open(index1.concat("?img=" + id),'_blank');
                        }
                        
                    }
                });

                $.ajax({
                    url: root + "/file/upload/map",
                    data: {mapUrl: val, proid: getQueryString("proid")},
                    success: function (r) {
                        if (r.success) {
                            layer.msg("出图成功！");
                        }
                    }
                });

            },
            error: function (e) {
                console.error("出图失败！" + e);
            },
            complete: function (evt) {
                if (msgHandler != undefined) layer.close(msgHandler);
                $("#printBtn").removeClass('disabled');
            }

        });
    }

    /***
     * 调用gp服务成功
     * @param evt
     */
    function onPrintCompleteHandler(evt) {
        layer.close(msgHandler);
        if (evt != undefined && evt.hasOwnProperty("url")) {
            window.open(evt.url, '_blank');
        }
    }

    /***
     * 调用gp服务异常
     * @param e
     */
    function onPrintError(e) {
        layer.close(msgHandler);
        console.error("print task error:" + e.error.message);
    }

    /***
     * 异常
     * @param err
     */
    function handleError(err) {
        console.error("Something error: ", err);
    }

    /**
     * 检测浏览器 adboe pdf 插件是否安装
     * @returns {boolean}
     */
    function isAcrobatPluginInstall() {
        var flag = false;

        //chrome 支持pdf预览
        if (Environment.isChrome()) {
            flag = true;
        }
        // 如果是firefox浏览器
        if (navigator.plugins && navigator.plugins.length) {
            for (var x = 0; x < navigator.plugins.length; x++) {
                if (navigator.plugins[x].name == 'Adobe Acrobat')
                    flag = true;
            }
        }
        // 下面代码都是处理IE浏览器的情况
        else if (window.ActiveXObject) {
            for (var x = 2; x < 10; x++) {
                try {
                    var oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
                    if (oAcro) {
                        flag = true;
                    }
                } catch (e) {
                    flag = false;
                }
            }
            try {
                var oAcro4 = new ActiveXObject('PDF.PdfCtrl.1');
                if (oAcro4)
                    flag = true;
            } catch (e) {
                flag = false;
            }
            try {
                var oAcro7 = new ActiveXObject('AcroPDF.PDF.1');
                if (oAcro7)
                    flag = true;
            } catch (e) {
                flag = false;
            }
        }
        return flag;
    }


    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }


    var timeout = 0;

    return me;
});