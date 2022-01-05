/**
 * 视频管理模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 9:38
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
        "dojo/_base/array",
        "dojo/_base/lang",
        "dojo/topic",
        "dojo/date/locale",
        'dojo/parser',
        'esri/toolbars/draw',
        "esri/SpatialReference",
        "esri/tasks/GeometryService",
        "esri/tasks/BufferParameters",
        "esri/geometry/Point",
        "esri/geometry/Polygon",
        "esri/geometry/Polyline",
        "esri/layers/GraphicsLayer",
        "esri/graphic",
        "esri/lang",
        'esri/symbols/SimpleFillSymbol',
        'esri/renderers/ClassBreaksRenderer',
        "esri/Color",
        "mustache",
        "layer",
        "laypage",
        "slimScroll",
        "map/core/JsonConverters",
        "map/core/GeometryServiceTask",
        "map/manager/LayoutManager",
        "map/core/BaseWidget",
        "map/core/entity/Sector",
        "map/core/support/ScreenHelper",
        "map/core/EsriSymbolsCreator",
        'map/core/ClusterFeatureLayer',
        "map/component/MapInfoWindow",
        "map/component/CameraViewResolver",
        "map/utils/DateUtils",
        "map/core/support/Environment",
        "static/js/cfg/core/SerializeForm",
        "map/utils/ImagesViewerUtil",
        "hbarsUtils",
        "map/utils/MapUtils",
        "esri/tasks/IdentifyParameters",
        "esri/tasks/IdentifyTask",
        "map/component/RecordViewer",
        "esri/symbols/SimpleLineSymbol",
        "esri/geometry/Circle",
        "map/widgets/VideoManager/VideoOptions",
        "ztree",
        "static/thirdparty/Validform/5.3.2/Validform.min",
        "static/thirdparty/viewer/js/viewer-jquery",
        "icheck",
        "static/thirdparty/bootstrap/js/tip",
        "static/js/UUIDUtils",
        "static/thirdparty/bootstrap/js/bootstrap-v3.min",
        "static/thirdparty/semantic-ui/checkbox/checkbox",
        "static/thirdparty/icheck/icheck",
        "static/thirdparty/laydate/laydate",
        "static/thirdparty/jquery/jquery.xdomainrequest.min",
        "css!static/thirdparty/semantic-ui/checkbox/checkbox.css",
        "static/thirdparty/jquery/jquery.easy-autocomplete.min",
        "css!static/thirdparty/viewer/css/viewer-jquery.css",
        "css!static/thirdparty/jquery/easy-autocomplete.min.css",
        "css!static/thirdparty/icheck/icheck.css",
        "dojo/domReady!"
    ],
    function (declare, arrayUtil, lang, topic, locale, parser, Draw, SpatialReference, GeometryService, BufferParameters, Point,
              Polygon, Polyline, GraphicsLayer, Graphic, esriLang, SimpleFillSymbol, ClassBreaksRenderer, Color, Mustache, layer, Laypage, SlimScroll,
              JsonConverters, GeometryServiceTask, LayoutManager, BaseWidget, Sector, ScreenHelper, EsriSymbolsCreator, ClusterFeatureLayer,
              MapInfoWindow, CameraViewResolver, DateUtils, Environment, SerializeForm, ImagesViewerUtil, HbarsUtils,
              MapUtils, IdentifyParameters, IdentifyTask, RecordViewer,SimpleLineSymbol,Circle) {



        var __map, __config, recordFormTpl, proTypeSelectTpl, newProjectLayer, newProjectPnt, singleCameraViewScope,countResult,
            videoTreeObj;
        //上升作用域为全局作用供其他模块使用，请勿删除！
        window.Global=window.Global||{};
        window.Global.HbarsUtils = HbarsUtils;
        window.Global.cameraGraClickHandler = cameraGraClickHandler;


        var $btn = null; //新建项目状态按钮
        var recentOpenVideo = [];
        var mapInfoWindow = MapInfoWindow.getInstance();
        var screenHelper = ScreenHelper.getInstance();
        var cameraViewResolver = CameraViewResolver.getInstance();
        var widgetPath="/map/widgets/TP/st/VideoManagerST";
        var SimpleLineSymbol = SimpleLineSymbol;
        var instance, me = declare([BaseWidget], {
            /**
             *
             */
            constructor: function () {

            },
            /**
             * 重写
             */
            onCreate: function () {
                $('[data-toggle="tooltip"]').tooltip({
                    placement: 'bottom',
                    container: 'body'
                });
                __config = this.getConfig();
                __map = this.getMap().map();
                geoUrl = this.getAppConfig().geometryService.url;
                recordFormTpl = null;
                initVars();
                if (appInitialized) {
                    initComponent();
                } else {
                    EventBus.listener(EventBus.APP_INITIALIZED, function () {
                        setTimeout(function () {
                            initComponent();
                        }, 500);
                    });
                }
            },
            /**
             *
             */
            onPause: function () {
                //清除一些状态以及监听
                if (drawTool !== undefined)
                    drawTool.deactivate();
                if (drawHandler !== undefined)
                    drawHandler.remove();
                if (statusCacheHandler !== undefined)
                    clearInterval(statusCacheHandler);
            },
            /**
             *
             */
            onOpen: function () {
                if (!VmOptions.showVideoByCluster) {
                    //添加动态图层到数据列表
                    topic.publish("data/list!add", {
                        alias: '监控点',
                        graphicsId: [graphicsLayer.id, videoLblGraphicsLyr.id]
                    });
                    //当数据列表处于下方的时候触发这个
                    topic.subscribe("data/list!ready", function () {
                        topic.publish("data/list!add", {
                            alias: '监控点',
                            graphicsId: [graphicsLayer.id, videoLblGraphicsLyr.id]
                        });
                    });
                }
            },
            /**
             *  供其他模块调用视频
             * @param type
             * @param camera
             * @param pNo
             */
            callVideo: function (type, camera, pNo) {
                startupVideo(type, camera, pNo);
            }
        });
        //默认缓冲距离2km
        var BUFFER_DISTANCE = 2000;
        //视频点显示样式——列表方式(默认)
        var VIDEO_STYLE_LIST = "list";
        //视频点显示样式——按区域分组显示
        var VIDEO_STYLE_GROUP = "group";

        var geometryService, geoUrl;
        var layoutManager = LayoutManager.getInstance();
        var videoList = [], graphics = [], proList = [], proGraphics = [], viewGraphics = [], lblGras = [];
        var normalVideoList = [];
        var pmsNormal = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/video_mark.png', 18, 18);
        var pmsSelected = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/video_mark_select.png', 18, 18);
        var highLightSymbol = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/video_mark_select.png', 30, 30);
        var locPntSym = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/loc.png', 16, 16);

        //缓冲区样式
        var bufferSymbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([0, 145, 255]), 1),
            new Color([0, 145, 255, 0.2]));

        //可视域的样式(渐变样式)
        var sectorFs = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID,
            new Color([255, 0, 0, 0]), 1), {
            type: 'linear',
            x1: 0,
            x2: 1,
            y1: 0,
            y2: 1,
            colors: [{color: new Color([179, 10, 10, 0.9]), offset: 0}, {
                color: new Color([179, 10, 10, 0.4]),
                offset: 1
            }]
        });

        //可视域样式(适配IE)
        var sectorFsForIE = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([179, 10, 100, 0.2]), 1),
            new Color([179, 10, 14, 0.8]));

        var centerLineFs = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH, new Color([255, 255, 33, 1]), 2);

        var graphicsLayer = new GraphicsLayer({id: 'videoGraphicsLyr'});

        // 显示监控点文本的graphics
        var videoLblGraphicsLyr = new GraphicsLayer({id: 'lblGraphicslyr'});

        var loadHandler, msgHandler;
        var pageSize = 10;
        var searchCondition = undefined;
        var drawTool;
        //新建项目时的graphic 保存/关闭后 清空该变量
        var proGra, bufferGra = null;
        var addProHandler, openVideoHandler, timeOutHandler, drawHandler, proDetailHandler;

        var defaultPageSize = 20;
        var $videoListContent;

        var picRecords;  //照片记录

        var videosCache;//所有监控点
        var statusCache;//摄像头状态
        var statusCacheHandler;

        var _mapClickHanlder = null;
        var _identifyResults = [];
        var _identifyCount = 0;
        var _listData = [];//页面展示的数据集合
        var _resultObj = {};

        //存放配置的属性识别图层
        var _identifyLayers = [];
        var identifyPos;
        var proidRecord;
        //请求后台获取可视域的标识
        var scopeFlag = 0, scopeVisible = false, cameraViewInterval, priorityCameraViewInterval;

        //监控点 分类 选中的摄像头
        var clusterLayer, selectedGraphics = [];

        var videoSearchCondition = undefined;

        /**
         * 初始化配置变量值
         */
        function initVars() {
            Date.prototype.format = function (format) {
                var o = {
                    "M+": this.getMonth() + 1, //month
                    "d+": this.getDate(), //day
                    "h+": this.getHours(), //hour
                    "m+": this.getMinutes(), //minute
                    "s+": this.getSeconds(), //second
                    "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
                    "S": this.getMilliseconds() //millisecond
                };

                if (/(y+)/i.test(format)) {
                    format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
                }

                for (var k in o) {
                    if (new RegExp("(" + k + ")").test(format)) {
                        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                    }
                }
                return format;
            };

            console.clear();
            lang.mixin(VmOptions, __config);
            if (__config.hasOwnProperty("style"))
                VmOptions.videoStyle = __config.style;
            if (esriLang.isDefined(__config.proType) && lang.isArray(__config.proType)) {
                VmOptions.proType = __config.proType;
            }
            //设置分页每页数目
            switch (screenHelper.heightLevel()) {
                case ScreenHelper.ULTIMATE:
                case ScreenHelper.HIGH:
                    defaultPageSize = 20;
                    break;
                case ScreenHelper.MEDIUM:
                    defaultPageSize = 15;
                    break;
                case ScreenHelper.LOW:
                    defaultPageSize = 13;
                    break;
            }
            //设置视频点文本层可见比例尺范围
            videoLblGraphicsLyr.setScaleRange(100000, __map.getMaxScale());
            HbarsUtils.regHelper("dateFmt", function (context, options) {
                return context.split(" ")[0];
            });
            HbarsUtils.regHelper("order", function (context, options) {
                return Number(context) + 1;
            });

            // for recogs
            HbarsUtils.regHelper("rowspan", function (context, options) {
                return context.length + 1;
            });

            HbarsUtils.registerPartial("cameraInfo",
                '<td style="vertical-align: middle!important;text-align: center !important;">' +
                '<button type="button" class="btn btn-link open-video" style="text-decoration: none;" ' +
                'data-indexcode="{{indexCode}}">{{name}}</button></td>');

            HbarsUtils.registerPartial("cogResult",
                '<td>{{dateFmt createAt}}</td><td><img src="/omp/file/thumb/{{resultFile}}" data-fid="{{resultFile}}" class="recog-img"/></td>' +
                '<td><img src="/omp/file/thumb/{{originFile}}" data-fid="{{originFile}}" class="recog-img"/></td>');
        }

        /**
         * 初始化组件
         */
        function initComponent() {
            //添加graphicsLayer 并移至顶层
            __map.addLayer(graphicsLayer);
            __map.addLayer(videoLblGraphicsLyr);
            $("#main-map_graphics_layer").insertBefore($("#videoGraphicsLyr_layer"));
            addOptionListeners();
            if (__config.hasOwnProperty("identifyLayers") && __config.identifyLayers.length > 0) {
                $("#identifyBtn").show();
            }
            layer.config({
                extend: '../thirdparty/layer/extend/layer.ext.js'
            });
            //初始化可视域解析器
            cameraViewResolver.setType(__config.isBatch ? CameraViewResolver.BATCH : CameraViewResolver.SOLO);
            cameraViewResolver.on('success', lang.hitch(this, updateCameraView));
            //项目类型渲染模板
            var typeTpl = '{{#list}}<option value="{{name}}" data-color = "{{color}}">{{name}}</option>{{/list}}';
            proTypeSelectTpl = Mustache.render(typeTpl, {list: VmOptions.proType});
            $('#proTypeSelector').append(proTypeSelectTpl);
            addProjectListeners();
            fetchVideoData();
            // 获取小网识别结果
            setTimeout(function () {
                getRecogs();
            }, 5000);
            topic.subscribe('video-layer-hide', function(evt) {
                evt.layer.hide();
            });
            topic.subscribe('video-layer-show', function(evt) {
                evt.layer.show();
            });
        }

        /**
         *  获取数据
         */
        function fetchVideoData() {
            $.ajax({
                url: root + "/video/fetch/all",
                success: function (r) {
                    if (r.length > 0) {
                        videoList = r;
                        addGroupVideo2Map(r);
                        $("#bfb").text(52.32+"%");
                    }
                }
            });
            var lstHeight = $(window).height() - $('#video_list_content').offset().top - 32;
            defaultPageSize = Math.floor((lstHeight / 32));
            $.ajax({
                url: root + "/video/fetch/root/groups",
                success: function (r) {
                    //获取在线离线状态
                    //用于下文匹配
                    $.ajax({
                        url: root + "/video/getCameraRegionCount",
                        success:function (count) {
                            countResult = count;
                            renderVideoTreeData(r);
                        }
                    });
                }
            });
            $.ajax({
                url: root + "/video/getCamerasCountByRegID",
                success: function (r) {
                    $("[name=videoOnline]").text(r["onLine"]);
                    $("[name=videoOffline]").text(r["offLine"]);
                }
            });

        }

        /**
         * 请求识别结果 并展示
         */
        function getRecogs() {

            $.getJSON(root + '/video/recogs', {}, function (data) {
                if (lang.isArray(data)) {
                    var content = HbarsUtils.renderTpl($("#recogs-tpl").html(), {data: data});
                    topic.publish("result/mini-show", {
                        content: content,
                        header: '智能识别预警',
                        height: 270
                    });
                    var imgHandler;
                    $('.recog-img').on('click', function () {
                        if (esriLang.isDefined(imgHandler)) {
                            layer.close(imgHandler);
                        }
                        var content = '<div style="padding: 10px;"><img id="img_{{imgId}}" style="width: 880px;height: 580px;" src="/omp/file/original/{{imgId}}"></div>';
                        imgHandler = layer.open({
                                title: false,
                                type: 1,
                                shadeClose: true,
                                closeBtn: 1,
                                area: ['900px', '600px'],
                                content: HbarsUtils.renderTpl(content, {imgId: $(this).data('fid')}),
                                success: function (layero, index) {
                                }
                            }
                        );
                    });
                    $(".open-video").on('click', function () {
                        var indexCode = $(this).data("indexcode");
                        var arr = arrayUtil.filter(data, function (item) {
                            return item.camera.indexCode === indexCode;
                        });
                        if (arr[0] !== undefined) {
                            var tmp = arr[0].camera;
                            startupVideo(0, tmp.indexCode);
                        }
                    });

                    $(".change-recog").on('click', function () {
                        var oid = $(this).data('oid');
                        // 询问框
                        layer.confirm('确认把当前记录加入白名单？', {
                            btn: ['确认', '取消']
                        }, function (index) {
                            // 添加白名单
                            layer.close(index);
                            var url = root + '/video/recogs/whitelist/'.concat(oid);
                            var msgHand = layer.msg('服务器通信中...', {icon: 16, shade: 0.4});
                            $.getJSON(url, {}, function (res) {
                                layer.close(msgHand);
                                if (res.success === false) {
                                    console.error(res.msg);
                                } else {
                                    layer.msg('添加成功!', {icon: 1});
                                    getRecogs();
                                }
                            });
                        }, function () {
                        });
                    });
                    //


                } else {
                    console.error('获取识别结果异常: %o', data);
                }
            });
        }

        /**
         * 功能列表监听
         */
        function addOptionListeners() {
            videoSearchListener();
            initBuffer();
            //可视域控制
            $("#viewScopeBtn").on('click', function () {
                if (scopeVisible) {
                    //关闭可视域
                    $(this).attr('title', '显示可视域');
                    scopeVisible = false;
                    $(this).find('i').removeClass('fa-eye-slash').addClass('fa-eye');
                    var newViewGraphics = [];//保存ViewGraphics中有价值的数据
                    arrayUtil.forEach(viewGraphics, function (item) {
                        newViewGraphics.push(item);
                        graphicsLayer.remove(item);
                    });
                    viewGraphics = newViewGraphics;
                    scopeFlag = 0;
                    //停止更新可视域
                    clearInterval(priorityCameraViewInterval);
                    clearInterval(cameraViewInterval);
                    cameraViewResolver.stop();
                } else {
                    //打开可视域
                    $(this).attr('title', '关闭可视域');
                    scopeVisible = true;
                    $(this).find('i').removeClass('fa-eye').addClass('fa-eye-slash');
                    scopeFlag = 1;
                    cameraViewResolver.setCameras(videoList);//每次start前都需要设置数据
                    cameraViewResolver.start();
                    //间隔30s更新一次可视域  时间应当做成可配置
                    cameraViewInterval = setInterval(function () {
                        cameraViewResolver.setCameras(videoList);
                        cameraViewResolver.start();
                    }, 30000);
                }
            });
            //属性查看按钮监听
            $("#identifyBtn").on('click', function () {
                _mapClickHanlder = __map.on('click', doIdentify);
                __map.setMapCursor('crosshair');
                MapUtils.setMap(__map);
            });
            //点选查看监控点
            $("#poiBtn").on('click', function () {
                clear();
                drawTool = drawTool ? drawTool : new Draw(__map);
                drawHandler = drawTool.on("draw-end", function (evt) {
                    drawHandler.remove();
                    var pnt = evt.geometry;
                    drawTool.deactivate();
                    msgHandler = layer.msg('搜索附近的监控点..', {time: 20000});
                    searchVideosByPoint(pnt,true);
                });
                esri.bundle.toolbars.draw.addPoint = "单击查看附近的监控点";
                drawTool.activate(Draw.POINT);
                var mapClickHandler = __map.on('click', function () {
                    window.clearTimeout(timeOutHandler);
                    mapClickHandler.remove();
                });
                timeOutHandler = window.setTimeout(function () {
                    drawTool.deactivate();
                    esri.bundle.toolbars.draw.addPoint = "单击以添加点";
                }, 5000);
            });
            //框选监控点
            $("#polyBtn").on('click', function () {
                clear();
                drawTool = drawTool ? drawTool : new Draw(__map);
                drawHandler = drawTool.on("draw-end", function (evt) {
                    drawHandler.remove();
                    drawTool.deactivate();
                    //查询范围内的cameras
                    var extent = evt.geometry;
                    var geojson = JSON.stringify(JsonConverters.toGeoJson(extent.getCenter()));
                    __map.setExtent(extent.expand(2.5));
                    //查询范围内的监控点
                    var cameras = findNearCameras(extent);
                    if (cameras.length > 0) {
                        //对于查询出来的监控点 请求后台转动监控点对准此点
                        arrayUtil.forEach(cameras, function (item) {
                            setCameraPtz(item.indexCode, geojson, null, item.platform);
                        });
                        showPoiCameras(cameras);
                    } else
                        layer.msg("附近没有监控点!", {time: 1500});
                });
                drawTool.activate(Draw.EXTENT);
                var mapClickHandler = __map.on('click', function (evt) {
                    window.clearTimeout(timeOutHandler);
                    mapClickHandler.remove();
                });
                timeOutHandler = window.setTimeout(function () {
                    drawTool.deactivate();
                }, 5000);
            });

            $("#openCamera").on('click',function () {
                //获取当前页面宽度、高度
                var height = $(window).height();
                var width = $(window).width();
                height = height+"px";
                width = width*0.9+"px";
                var host = window.location.host;
                //打开页面
                layer.open({
                    type: 2,
                    title: '查看图片',
                    shadeClose: true,
                    shade: false,
                    maxmin: true, //开启最大化最小化按钮
                    area: [width,height],
                    content: 'http://'+host+'/omp/video/cameraView'
                });
            });
            addGraphicLayerListener();
        }

        function initBuffer() {
            //缓冲区
            $("#buffer").iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
                increaseArea: '20%'
            });

            //获取所有元素
            var videoLayer = __map.getLayer("videoGraphicsLyr");
            var circleLayer = __map.getLayer("circleLayer");
            var graphics = videoLayer.graphics;

            $("#buffer").on("ifUnchecked", function (evt) {
                circleLayer.hide();
                videoLayer.show();
            });

            var lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([179, 182, 185]), 2);
            var fill = new SimpleFillSymbol();
            fill.outline = lineSymbol;
            $("#buffer").on("ifChecked", function (evt) {
                if (!circleLayer) {
                    circleLayer = new GraphicsLayer({id: 'circleLayer'});
                    for (var i = 0; i < graphics.length; i++) {
                        var graphic = graphics[i];
                        var point = graphic.geometry;
                        var circleGeometry = new Circle(point, {
                            "radius": __config.bufferRadius || 1000,
                            "numberOfPoints": 400
                        });
                        var g = new Graphic(circleGeometry, fill);
                        circleLayer.add(g);
                    }
                    __map.addLayer(circleLayer);
                    videoLayer.hide();
                } else {
                    circleLayer.show();
                    videoLayer.hide();
                }
            });
        }

        /**
         * 将监控点加载到地图
         * @param cameras
         */
        function addGroupVideo2Map(cameras) {

            arrayUtil.forEach(cameras, function (data) {
                var pnt = new Point(data.x, data.y, __map.spatialReference);
                var g = new Graphic(pnt, pmsNormal, data, null);
                //文本标识
                var txtSymbolColor = undefined;
                var txtSymbolFontSize = undefined;
                if (__config.hasOwnProperty("txtSymbol")) {
                    txtSymbolColor = __config.txtSymbol.color;
                    txtSymbolFontSize = __config.txtSymbol.fontSize;
                } else {
                    txtSymbolColor = "#38B03F";
                    txtSymbolFontSize = 12;
                }
                var txtSymbol = EsriSymbolsCreator.createTextSymbol(data.name, txtSymbolColor, txtSymbolFontSize, false);
                //浏览器显示矢量图标的问题，增大偏移量
                txtSymbol.setOffset(0, 10);
                var txtGra = new Graphic(pnt, txtSymbol, data);
                videoLblGraphicsLyr.add(txtGra);
                graphicsLayer.add(g);
                graphics.push(g);
                lblGras.push(txtGra);
            });
            if (graphicsLayer !== undefined && graphics.length > 0 && __map.getLayer("videoGraphicsLyr") === undefined) {
                map.addLayer(graphicsLayer);
                addGraphicLayerListener();
            }
        }

        /**
         * 添加监控点图层监听
         */
        function addGraphicLayerListener() {
            graphicsLayer.on('click', function (evt) {
                var gra = evt.graphic;
                var attr = gra.attributes;
                var type = 0; //0--视频点 1---项目位置点
                if (esriLang.isDefined(attr) && esriLang.isDefined(attr.proId))
                    type = 1;
                switch (type) {
                    case 0: {
                        cameraGraClickHandler(gra);
                        break;
                    }
                    case 1:
                        if (VmOptions.showProLocAll) {
                            //根据proid找到项目信息
                            var proid = attr.proId;
                            var arr = arrayUtil.filter(proList, function (item) {
                                return item.proId == proid;
                            });
                            if (arr.length > 0) {
                                openProDetail(arr[0]);
                            } else {
                                //请求获取项目信息
                                $.getJSON('/omp/project/get/'.concat(proid), null, function (data) {
                                    if (esriLang.isDefined(data.success)) {
                                        console.error(data.msg);
                                        return;
                                    }
                                    openProDetail(data);
                                });
                            }

                        } else
                            openProDetail(attr);
                        break;
                }
            });
        }

        /***
         * 兴趣点搜索监控点
         * @param pnt
         */
        function searchVideosByPoint(pnt,show) {
            if (!esriLang.isDefined(pnt.type)) {
                if (pnt.x == "" || pnt.y == "") {
                    layer.msg("请输入正确的xy坐标", {time: 1000, icon: 0});
                    return;
                } else
                    pnt = new Point(pnt.x, pnt.y, __map.spatialReference)
            }
            msgHandler = layer.msg('搜索附近的监控点..', {time: 20000});
            GeometryServiceTask.buffer(pnt, BUFFER_DISTANCE, function (r) {
                var geos = r.geometries;
                //弹出层列出附近的监控点
                layer.close(msgHandler);
                if (geos !== undefined && geos.length > 0) {
                    var bufferGeo = geos[0];
                    //搜索缓冲范围内的监控点
                    var cameras = findNearCameras(bufferGeo);
                    if (cameras.length > 0) {
                        //对于查询出来的监控点 请求后台转动监控点对准此点
                        arrayUtil.forEach(cameras, function (item) {
                            if(show){
                                singleCameraViewScope = setInterval(function () {
                                    getSingleViewScope(item, true);
                                }, 5000);
                            }
                            setCameraPtz(item.indexCode, JSON.stringify(JsonConverters.toGeoJson(pnt)), null, item.platform);
                        });
                        showPoiCameras(cameras);
                    } else
                        layer.msg("附近没有监控点!", {time: 1500});
                }

            }, {outSpatialReference: __map.spatialReference});
        }

        /**
         * 更新可视域
         * @param data
         * @param isSingle
         */
        function updateCameraView(data, isSingle) {
            // 请求成功 更新可视域
            //移除所有具有新的范围参数的监控点扇形
            arrayUtil.forEach(data, function (videoScope, i) {
                arrayUtil.forEach(viewGraphics, function (item, i) {
                    if (item && item.attributes.indexCode == videoScope.indexCode) {
                        graphicsLayer.remove(item);
                        viewGraphics.splice(i, 1, "");
                    }
                });
            });
            //适当放大地图到比例尺10000
            var _scale = __map.getScale();
            if (_scale > 10000) {
                //__map.setScale(10000);
            }
            //重新计算覆盖区域
            for (var i in videoList) {
                var videoData = videoList[i];
                for (var j in data) {
                    if (videoData.indexCode == data[j].indexCode) {
                        if (videoData.viewRadius != null && videoData.viewRadius > 0) {
                            if(videoData.viewRadius<10){
                                videoData.viewRadius=10;
                            }
                            var center = new Point(videoData.x, videoData.y, __map.spatialReference);
                            var base = videoData.platform ? (videoData.platform === "hk" ? (360 - data[j].azimuth) : data[j].azimuth) : (platform === "hk" ? (360 - data[j].azimuth) : data[j].azimuth);
                            //测试代码开始
                            if (0<base&&base<180){
                                base = base+180;
                            }else if(base>180&&base<360){
                                base = base-180;
                            }
                            //测试代码结束
                            var start = base - data[j].horizontalAngle / 2;  //起始角
                            var end = base + data[j].horizontalAngle / 2;    //结束角
                            var s = new Sector(center, {
                                radius: (data[j].viewRadius) * 10,
                                startAngle: start,
                                endAngle: end
                            });
                            var t = new Graphic(s.getPolygon(), Environment.isIE() ? sectorFsForIE : sectorFs, {indexCode: videoData.indexCode});
                            viewGraphics.push(t);
                            if (VmOptions.showArcCenter) {
                                var polyline = new Polyline(s.getArcCenter());
                                var l = new Graphic(polyline, centerLineFs, {indexCode: videoData.indexCode});
                                viewGraphics.push(l);
                            }
                            break;
                        }
                    }
                }
            }
            //如果此时关闭了可视域 则不进行渲染
            if (scopeFlag === 1 || isSingle) {
                $.each(viewGraphics, function (idx, item) {
                    if (item)
                        graphicsLayer.add(item);
                });
            }
        }


        /**
         * 定时任务请求探头信息刷新在线状态
         */
        function modifyCamerasStatus(url) {
            getAllCameras(url, function (newCameras) {
                for (var i in newCameras) {
                    var newCamera = newCameras[i];
                    for (var j in statusCache) {
                        var oldCamera = statusCache[j];
                        if (oldCamera.indexCode == newCamera.indexCode && oldCamera.status != newCamera.status) {
                            if (newCamera.status == 1) {
                                $('#device_' + newCamera.indexCode).find('.status').remove();
                                layer.msg("监控点" + newCamera.name + "已上线", {icon: 1, time: 5000, offset: 'rb'});
                            }
                            else {
                                $('#device_' + newCamera.indexCode).append('<span class="label label-primary pull-right status" title="设备离线">离线</span>');
                                layer.msg("监控点" + newCamera.name + "已断开连接", {icon: 2, time: 5000, offset: 'rb'});
                            }
                        }
                    }
                }
                statusCache = newCameras;
            });
        }

        /**
         * 后台请求监控点
         * @param url
         * @param callBack
         */
        function getAllCameras(url, callBack) {
            loadHandler = layer.load(2, {offset: getOffset(), time: 15000});
            $.ajax({
                url: url,
                success: function (r) {
                    layer.close(loadHandler);
                    if (r.hasOwnProperty("success")) {
                        layer.msg(r.msg, {icon: 0});
                        return;
                    }
                    if (callBack != undefined)
                        callBack(r);
                }
            });
        }

        /**
         * 获取panel偏移
         */
        function getOffset() {
            var left = $("#popupwin_area").width() / 2;
            var top = document.body.clientHeight / 2;
            return [top + 'px', left];
        }

        /**
         * 监控点检索监听
         */
        function videoSearchListener() {
            var $searchInput = $('#searchInput');
            $searchInput.on('keydown', function (evt) {
                if (evt.keyCode == 13) {
                    execVideoSearch($(this).val());
                }
            });
            $searchInput.on('keyup', function (evt) {
                if (evt.keyCode == 8) {
                    if ($(this).val().length == 0) {
                        $searchInput.focus();
                        $('#clearBtn').hide();
                        videoSearchCondition = undefined;
                        $("#videoPageTool").hide();
                        fetchVideoData();
                    }
                }
            });
            $searchInput.on('input propertychange', function () {
                if ($(this).val().length > 0) {
                    $('#clearBtn').show();
                }
            });
            $('#clearBtn').on('click', function () {
                videoSearchCondition = undefined;
                $searchInput.val('');
                $searchInput.focus();
                $(this).hide();
                $("#videoPageTool").hide();
                fetchVideoData();
            });
            $('#searchBtn').on('click', function () {
                execVideoSearch($searchInput.val());
            });
        }

        /***
         * 执行video查询
         * @param condition
         */
        function execVideoSearch(condition) {
            if (!esriLang.isDefined(condition)) {
                return;
            }
            videoSearchCondition = condition;
            var url = root + '/video/fetch/condition';
            layer.msg('查询中...', {time: 10000, icon: 16, shade: 0.1});
            $.ajax({
                url: url,
                data: {condition: condition},
                success: function (r) {
                    layer.closeAll();
                    if (r.totalElements < 1) {
                        layer.msg('没有查到监控点!', {time: 2000});
                        return;
                    }
                    renderVideoListData(r);
                    $('#clearBtn').show();
                }
            })
        }

        /**
         * list形式加载数据
         * @param videoPage
         */
        function renderVideoListData(videoPage) {
            $videoListContent = $("#video_list_content");
            $videoListContent.empty();
            var template = $('#videoListTpl').html();
            $videoListContent.append(HbarsUtils.renderTpl(template, {list: videoPage.content}));
            showVideoPageTool(videoPage, renderVideoPageData);
            addVideoListListener();
        }

        /**
         *  video list 增加监听
         */
        function addVideoListListener() {
            $(".view-video").on("click", function (e) {
                e.stopPropagation();
                startupVideo(0, $(this).data("indexcode"));
            });

            $(".video-li").on("click", function () {
                var indexCode = $(this).data("indexcode");
                var camera = queryCameraByIndex(indexCode)
                videoItemClickHandler(indexCode, undefined, camera);
            })
        }

        /**
         * 显示分页组件
         * @param videoPage
         * @param callback
         */
        function showVideoPageTool(videoPage, callback) {
            $("#videoPageTool").show();
            Laypage({
                cont: $("#videoPageTool"),
                pages: videoPage.totalPages,
                groups: 0,
                first: false,
                last: false,
                curr: videoPage.number + 1,
                jump: function (obj, first) {
                    if (!first) {
                        //点击跳页触发函数自身，并传递当前页：obj.curr注意带条件查询的分页
                        callback(obj.curr - 1);
                    }
                }
            });
        }

        /**
         * 获取分页video
         * @param page
         * @param size
         * @returns {undefined}
         */
        function renderVideoPageData(page, size) {
            var pageData = undefined;
            if (!size) {
                size = defaultPageSize;
            }
            if (videoSearchCondition) {
                $.ajax({
                    url: root + "/video/fetch/condition",
                    async: false,
                    data: {page: page, size: size, condition: videoSearchCondition},
                    success: function (r) {
                        if (r.content !== undefined) {
                            renderVideoListData(r);
                        } else {
                            layer.msg(r);
                        }
                    }
                });
            } else {
                $.ajax({
                    url: root + "/video/fetch/page",
                    async: false,
                    data: {page: page, size: size},
                    success: function (r) {
                        if (r.content !== undefined) {
                            renderVideoListData(r);
                        } else {
                            layer.msg(r);
                        }
                    }
                });
            }
            return pageData;
        }

        /**
         * ztree方式加载监控点分组数据
         * @param treeDom
         * @param data
         */
        function renderVideoTreeData(data) {
            arrayUtil.forEach(data, function (item) {
                item.open = true;
                var id = item.id;

            });
            $("#video_list_content").empty();
            var treeSetting = {
                data: {
                    key: {
                        name: "name"
                    }
                },
                view: {
                    showTitle: false,
                    showLine: true,
                    selectedMulti: false,
                    showIcon: true,
                    addDiyDom: addVideoOperaDom,
                    fontCss: setVideoFontCss
                },
                callback: {
                    onClick: onLeafRegionClick
                }
            };
            videoTreeObj = $.fn.zTree.init($("#video_list_content"), treeSetting, data);
            var scrollHeight = $(window).height() - 300;
            var width = $('#video_pane').width();
            $('#video_list_content').slimScroll({
                height: scrollHeight,
                width: width - 5,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        }

        /**
         * 添加树节点按钮监听
         * @param treeId
         * @param treeNode
         */
        function addVideoOperaDom(treeId, treeNode) {
            var $tObj = $("#" + treeNode.tId + "_a");
            var tpl = '';
            if (!treeNode.isParent && treeNode.hasOwnProperty("indexCode") && treeNode.status === 0) {
                tpl = '<span class="badge b-count pull-right">离线</span>';
                $tObj.append(tpl);
            } else if (!treeNode.isParent && treeNode.hasOwnProperty("indexCode") && treeNode.status === 1) {
                tpl = '<span class="fa fa-video-camera pull-right" id="view_video_{{indexCode}}" style="color:#4285f4;"></span>';
                $tObj.append(Mustache.render(tpl, {indexCode: treeNode.indexCode}));
                $("#view_video_" + treeNode.indexCode).on("click", function (event) {
                    event.stopPropagation();
                    startupVideo(0, treeNode.indexCode, undefined);
                });
            }
            if(countResult[treeNode.id]){
                var online =  countResult[treeNode.id]['regOnLine'];
                var total = (countResult[treeNode.id]['regOffLine']+countResult[treeNode.id]['regOnLine']);
                if(total==0){
                    return;
                }
                tpl = '<span class="pull-right" style="background-color: #d9e7fd ;color:#4285f4 !important;font-size: 2px;position: absolute;right: 20px">'+online+'/'+total+'</span>';

                $tObj.append(tpl);
            }
        }

        /**
         * 设置树列表样式
         * @param treeId
         * @param treeNode
         * @returns {{margin-left: string}}
         */
        function setVideoFontCss(treeId, treeNode) {
            if (treeNode.children === undefined || treeNode.children.length === 0) {
                return {"margin-left": "-8px"}
            }
        }

        /**
         * 树节点点击事件
         * @param event
         * @param treeId
         * @param treeNode
         */
        function onLeafRegionClick(event, treeId, treeNode) {
            if (!treeNode.isParent && !treeNode.hasOwnProperty("indexCode")) {
                $.ajax({
                    url: root + "/video/fetch/region/cameras",
                    data: {regionName: treeNode.name},
                    success: function (r) {
                        if (r instanceof Array && r.length > 0) {
                            videoTreeObj.addNodes(treeNode, r);
                        } else {
                            console.warn('此区域未发现监控点！');
                        }
                    }
                });
            } else if (treeNode.hasOwnProperty("indexCode")) {
                videoItemClickHandler(treeNode.indexCode, undefined, treeNode);
            }
        }

        /**
         * 添加监控点到地图
         * 添加label标识监控点名称
         */
        function addVideoGraphics() {
            if (VmOptions.showVideoByCluster) {
                //文本标识
                var txtSymbolColor = undefined;
                var txtSymbolFontSize = undefined;
                if (__config.hasOwnProperty("txtSymbol")) {
                    txtSymbolColor = __config.txtSymbol.color;
                    txtSymbolFontSize = __config.txtSymbol.fontSize;
                } else {
                    txtSymbolColor = "#38B03F";
                    // txtSymbolColor = "#32bfef";
                    txtSymbolFontSize = 12;
                }
                var options = {
                    map: __map,                    //map
                    config: videoList,             //vedio 集合
                    displayOnPan: true,           //
                    vedioSymbol: pmsNormal,         // 默认摄像头符号
                    lableField: "name",             //摄像头标识字段
                    uniqueField: "indexCode",       //唯一标识符
                    labelColor: txtSymbolColor,     //摄像头label 颜色
                    labelFont: txtSymbolFontSize,   //摄像头label 字体大小
                    labelOffset: 10,                //摄像头label 偏移量
                    gridPixel: 200
                };
                clusterLayer = new ClusterFeatureLayer.ClusterFeatureLayer(options);
                __map.addLayer(clusterLayer);
                //添加动态图层到数据列表
                topic.publish("data/list!add", {alias: '监控点', graphicsId: clusterLayer.id});
                //当数据列表处于下方的时候触发这个
                topic.subscribe("data/list!ready", function () {
                    topic.publish("data/list!add", {alias: '监控点', graphicsId: clusterLayer.id});
                });
            } else {
                for (var i in videoList) {
                    var data = videoList[i];
                    var pnt = new Point(data.x, data.y, __map.spatialReference);
                    var g = new Graphic(pnt, pmsNormal, data, null);
                    //文本标识
                    var txtSymbolColor = undefined;
                    var txtSymbolFontSize = undefined;
                    if (__config.hasOwnProperty("txtSymbol")) {
                        txtSymbolColor = __config.txtSymbol.color;
                        txtSymbolFontSize = __config.txtSymbol.fontSize;
                    } else {
                        txtSymbolColor = "#38B03F";
                        txtSymbolFontSize = 12;
                    }
                    var txtSymbol = EsriSymbolsCreator.createTextSymbol(data.name, txtSymbolColor, txtSymbolFontSize, false);
                    //浏览器显示矢量图标的问题，增大偏移量
                    txtSymbol.setOffset(0, 10);
                    var txtGra = new Graphic(pnt, txtSymbol, data);
                    // graphicsLayer.add(txtGra);
                    videoLblGraphicsLyr.add(txtGra);
                    graphicsLayer.add(g);

                    graphics.push(g);
                    lblGras.push(txtGra);
                }
                if (graphicsLayer != undefined && graphics.length > 0 && __map.getLayer("videoGraphicsLyr") == undefined)
                    map.addLayer(graphicsLayer);
            }

        }

        /**
         * 点击左侧监控点项事件
         * @param vid
         * @param type
         */
        function videoItemClickHandler(vid, type, videoNode) {
            var tmp;
            if (videoNode !== undefined) {
                tmp = videoNode;
            } else {
                if (type == undefined || type != 'mobile') {
                    tmp = arrayUtil.filter(videoList, function (item) {
                        return item.id === vid || item.indexCode === vid;
                    })[0];
                } else {
                    tmp = arrayUtil.filter(mobileVideoList, function (item) {
                        return item.id === vid || item.indexCode === vid;
                    })[0];
                }
            }
            if (tmp != undefined) {
                var pnt = new Point(tmp.x, tmp.y, __map.spatialReference);
                __map.setScale(40000);
                __map.centerAt(pnt);
                clearHighLightSymbol();
                arrayUtil.forEach(graphics, function (item) {
                    var indexCode = item.attributes.indexCode;
                    if (indexCode === tmp.indexCode) {
                        item.setSymbol(highLightSymbol);
                        for (var i = 0; i < $("#videoGraphicsLyr_layer").find("text").length; i++) {
                            if ($("#videoGraphicsLyr_layer").find("text").eq(i).html() == tmp.name) {
                                var n = $("#videoGraphicsLyr_layer").find("text").eq(i);
                                var nImg = n.next();
                                var m = $("#videoGraphicsLyr_layer").find("text:last");
                                var mImg = m.next();
                                var exchange = function (a, b) {
                                    var ap = a.prev(), bp = b.prev();
                                    var n = a.next(), p = b.prev();
                                    b.insertBefore(n);
                                    a.insertAfter(p);
                                    ap.insertBefore(a);
                                    bp.insertBefore(b);
                                };
                                exchange(nImg, mImg);
                            }
                        }
                    }

                });
            }
        }

        /**
         * 移除高亮图标
         */
        function clearHighLightSymbol() {
            arrayUtil.forEach(graphics, function (item) {
                var symbol = item.symbol;
                if (symbol === highLightSymbol)
                    item.setSymbol(pmsNormal);
            });
        }

        /**
         * 点击地图上的监控点要素触发事件
         * @param gra
         */
        function cameraGraClickHandler(gra) {
            var attr = gra.attributes;
            //高亮显示选中的视频点
            highlightGras([gra]);
            var basicTpl = $("#videoPopupTpl").html();
            if (openVideoHandler) {
                layer.close(openVideoHandler);
            }
            var optbtns = [['视频', 'fa fa-video-camera', attr.indexCode]];
            if (VmOptions.showSingleViewScope) {
                optbtns.push(['可视域', 'fa fa-eye', attr.indexCode]);
            }
            //弹出窗口
            openVideoHandler = layer.open({
                type: 1,
                title: attr.name,
                area: '600px',
                shade: 0,
                content: HbarsUtils.renderTpl(basicTpl, {basic: attr}),
                btn: [],
                optbtn: optbtns,
                optbtn1: function (index, btn) {
                    startupVideo(0, $(btn).data("token"));
                },
                optbtn2: function (index, btn) {
                    //单个监控点的可视域
                    if (singleCameraViewScope) clearInterval(singleCameraViewScope);
                    var indexCode = $(btn).data("token");
                    var singleCamera = arrayUtil.filter(videoList, function (item) {
                        return item.indexCode === indexCode;
                    })[0];
                    singleCameraViewScope = setInterval(function () {
                        getSingleViewScope(singleCamera, true);
                    }, 5000);
                },
                cancel: function () {
                    clearInterval(singleCameraViewScope);
                    if (scopeFlag != 1 && singleCameraViewScope) {
                        $.each(viewGraphics, function (j, n) {
                            graphicsLayer.remove(this);
                        });
                    }
                    singleCameraViewScope = undefined;
                    clearHighlight();
                    layoutManager.hideRightPanel();
                },
                success: function () {
                    var pros = [];
                    //请求项目信息
                    $.getJSON(root + "/project/list?indexCode=" + attr.indexCode, function (ret) {
                        if (lang.isArray(ret)) {
                            pros = ret;
                            $("#proListPanel").html(HbarsUtils.renderTpl($("#cameraProsTpl").html(), {projects: pros}));
                            if (VmOptions.addProjectByCamera) {
                                $(".camera-add-project").unbind("click").on("click", function () {
                                    $('#proTab').click();
                                    setTimeout(function () {
                                        $('#addBtn').click();
                                    }, 300);
                                });
                            } else {
                                $(".camera-add-project").hide();
                            }
                            if (pros.length === 0) {
                                $("#proListPanel").append('<div class="text-muted text-center">无记录</div>');
                                return;
                            }
                            proTableListener(attr, pros);
                        } else {
                            $("#proListPanel").html();
                            console.warn(ret);
                        }
                    });
                }
            });

        }


        function startupVideo(type, camera, pNo,cameras) {
            var platConfig;
            var vcuList = [];
            var platformType;
            var server;
            var userName = null, userPwd = null, userDomain = null;
            if (typeof camera === "string") {
                var tmp = queryCameraByIndex(camera);
                if (tmp !== undefined) {
                    var _a = {};
                    if (tmp.hasOwnProperty("platform")) {
                        platConfig = getCameraConfig(tmp.platform.toLowerCase());
                    } else {
                        platConfig = getCameraConfig("");
                    }
                    if(!platformType){
                        platformType = platConfig.platType;
                    }
                    if(!server){
                        server = platConfig.server;
                    }
                    if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh" || platConfig.platType === 'HW' || platConfig.platType === "DH")) {
                        _a.VcuId = tmp.vcuId;
                        _a.VcuName = tmp.regionName;
                        userName = platConfig.userName.split("@")[0];
                        userPwd = platConfig.password;
                        userDomain = platConfig.userName.split("@")[1];
                    }else if(platformType=="ivs"){
                        openIvsVideo(tmp.name,tmp.indexCode,tmp.vcuId,platConfig,type,pNo);
                        return;
                    }
                    _a.devicelist = [
                        {
                            DeviceId: tmp.indexCode,
                            DeviceName: encodeURIComponent(tmp.name),
                            // DeviceName: tmp.name,
                            IndexCode: tmp.indexCode,
                            nodaname: encodeURIComponent(tmp.name),
                            // nodaname: tmp.name,
                            devid: tmp.id,
                            cmrip: tmp.ip,
                            cmrport: tmp.port,
                            cmruser: platConfig.userName,
                            cmrpass: platConfig.password,
                            serverip: platConfig.server,
                            serverport: platConfig.port
                        }
                    ];
                    vcuList.push(_a);
                }
            } else if (typeof camera === "object") {
                if (camera instanceof Array) {
                    $.each(camera, function (cameraIndex) {
                        var item =camera[cameraIndex];
                        var v = {};
                        if (item.hasOwnProperty("platform")) {
                            platConfig = getCameraConfig(item.platform.toLowerCase());
                        } else {
                            platConfig = getCameraConfig("");
                        }
                        if(!platformType){
                            platformType = platConfig.platType;
                        }
                        if(!server){
                            server = platConfig.server;
                        }
                        if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh" || platConfig.platType === 'HW' || platConfig.platType === "DH")) {
                            v.VcuId = item.vcuId;
                            v.VcuName = item.regionName;
                            userName = platConfig.userName.split("@")[0];
                            userPwd = platConfig.password;
                            userDomain = platConfig.userName.split("@")[1];
                        }else if(platformType=="ivs"){
                            openIvsVideo(item.name,item.indexCode,item.vcuId,platConfig,type,pNo);
                            return;
                        }
                        v.devicelist = [{
                            DeviceId: item.indexCode,
                            DeviceName: encodeURIComponent(item.name),
                            // DeviceName: item.name,
                            IndexCode: item.indexCode,
                            nodaname: encodeURIComponent(item.name),
                            // nodaname: item.name,
                            devid: item.id,
                            cmrip: item.ip,
                            cmrport: item.port,
                            cmruser: platConfig.userName,
                            cmrpass: platConfig.password,
                            serverip: platConfig.server,
                            serverport: platConfig.port
                        }];
                        vcuList.push(v);
                    });
                } else {
                    var vcu = {};
                    if (camera.hasOwnProperty("platform")) {
                        platConfig = getCameraConfig(camera.platform.toLowerCase());
                    } else {
                        platConfig = getCameraConfig("");
                    }
                    if(!platformType){
                        platformType = platConfig.platType;
                    }

                    if (platConfig && (platConfig.platType === 'hw' || platConfig.platType === "dh")) {
                        vcu.VcuId = camera.vcuId;
                        vcu.VcuName = camera.regionName;
                        userName = platConfig.userName.split("@")[0];
                        userPwd = platConfig.password;
                        userDomain = platConfig.userName.split("@")[1];
                    }else if(platformType=="ivs"){
                        openIvsVideo(camera.name,camera.indexCode,camera.vcuId,platConfig,type,pNo);
                        return;
                    }
                    vcu.devicelist = [
                        {
                            DeviceId: camera.indexCode,
                            DeviceName: encodeURIComponent(camera.name),
                            IndexCode: camera.indexCode,
                            nodaname: encodeURIComponent(camera.name),
                            devid: camera.id,
                            cmrip: camera.ip,
                            cmrport: camera.port,
                            cmruser: platConfig.userName,
                            cmrpass: platConfig.password,
                            serverip: platConfig.server,
                            serverport: platConfig.port
                        }
                    ];
                    vcuList.push(vcu);
                }
            }
            if (vcuList.length > 0) {
                switch (type) {
                    case 0:
                        previewCamera(pNo, vcuList, userName, userPwd, userDomain, platformType, server);
                        break;
                    case 1:
                        preSetCamera(pNo, "预置位" + pNo, vcuList, userName, userPwd, userDomain, platformType,server);
                        break;
                }
            } else
                console.log("打开视频错误.");
        }

        /**
         * ivs 查看、设置
         * @param name
         * @param cameraId
         * @param vcuId
         * @param setPtz
         * @param id
         * @param proid
         * @param platform
         * @param type
         * @param pNo
         */
        function openIvsVideo(name,cameraId,vcuId,platform,type,pNo){
            var cmrvParam = {
                //ExeVersionCheck:window.location.origin+"/omp/static/bin/camaraViewer7.inf",
                RunPrefNum: "1",
                platfrom: "HWIVS",
                UserName: platform.userName,
                Password: platform.password,
                Port: platform.port,
                Server: platform.server,
                vculist: [
                    {
                        VcuId: cameraId + "#" + vcuId,
                        VcuName: name
                    }
                ]
            };

            if(type==0){
                cmrvParam.RunPrefNum = pNo;
            }else if(type==1){
                cmrvParam.SetPrefNum = pNo;
                cmrvParam.SetPrefName = "预设位"+pNo+Math.random();
            }

            postVideoCache(JSON.stringify(cmrvParam));
        }

        /**
         * 根据indxcode查询摄像头
         * @param indexcode
         * @returns {undefined}
         */
        function queryCameraByIndex(indexcode) {
            var camera = undefined;
            $.ajax({
                url: root + "/video/query/indexcode/" + indexcode,
                async: false,
                success: function (r) {
                    if (r) {
                        camera = r;
                    }
                }
            });
            return camera;
        }


        /**
         * 单个监控点面板中的项目事件
         * @param pros
         */
        function proTableListener(attr, pros) {
            //打开历史记录面板
            $(".btn-pro-history").on('click', function () {
                var proid = $(this).data("proid");
                if (!__config.multipleVideo) {
                    renderHistoryRecords(proid, attr.indexCode);
                } else {
                    RecordViewer.renderRecord(proid, attr.indexCode, __config, true);
                }
            });
            //点击项目名称 预览项目预设位视频
            $(".btn-pro-startupVideo").on('click', function () {
                var pNo = undefined;
                var proid = $(this).data("proid");
                $.each(pros, function (index, pro) {
                    if (pro.proId === proid) {
                        $.each(pro.presets, function (i, preset) {
                            if (preset.indexCode === attr.indexCode) {
                                pNo = preset.presetNo;
                            }
                        });
                    }
                });
                startupVideo(0, attr.indexCode, pNo);
            });
        }

        /***
         * 高亮要素
         * @param data
         */
        function highlightGras(data) {
            if (lang.isArray(data)) {
                clearHighlight();
                arrayUtil.forEach(data, function (g) {
                    var attr = g.attributes;
                    var symbol = g.symbol;
                    if (!symbol.hasOwnProperty('align'))
                        g.setSymbol(pmsSelected);
                    if (lang.exists("setText", symbol)) {
                        var arr = arrayUtil.filter(graphics, function (item) {
                            return item.attributes === attr;
                        });
                        if (arr != undefined && arr.length > 0) {
                            var tmp = arr[0];
                            tmp.setSymbol(pmsSelected);
                        }
                    }
                });
            }
        }

        /***
         * 清除高亮要素
         * @param data
         */
        function clearHighlight() {
            arrayUtil.forEach(graphics, function (item) {
                var symbol = item.symbol;
                if (symbol === pmsSelected)
                    item.setSymbol(pmsNormal);
            });
            if (VmOptions.showVideoByCluster) {
                arrayUtil.forEach(selectedGraphics, function (item) {
                    var symbol = item.symbol;
                    if (symbol === pmsSelected)
                        item.setSymbol(pmsNormal);
                });
                selectedGraphics = [];
            }
            clearBiggerSymbol();
        }

        /**
         *
         */
        function clearBiggerSymbol() {
            arrayUtil.forEach(graphics, function (item) {
                var symbol = item.symbol;
                if (symbol === highLightSymbol)
                    item.setSymbol(pmsNormal);
            });
        }

        /***
         * 清除动态要素图层
         * @param gras  清除的要素集合
         */
        function clearGraphicsLayer(gras) {
            if (esriLang.isDefined(gras) && lang.isArray(gras)) {
                arrayUtil.forEach(gras, function (item) {
                    try {
                        graphicsLayer.remove(item);
                    } catch (e) {
                    }
                    item = null;
                });
                gras = [];
            } else
                graphicsLayer.clear();
            graphicsLayer.redraw();
        }

        /***
         * 展示附近的监控点列表
         * @param cameras
         */
        function showPoiCameras(cameras) {
            //高亮要素
            var data = arrayUtil.filter(graphics, function (g) {
                return arrayUtil.indexOf(cameras, g.attributes) > -1;
            });
            highlightGras(data);
            var tpl = $("#videoPoiTpl").html();
            Mustache.parse(tpl);
            layer.closeAll();
            layer.open({
                type: 1,
                title: '附近监控点',
                area: '300px',
                offset: ['100px', '460px'],
                shade: 0,
                content: Mustache.render(tpl, {cameras: cameras}),
                optbtn: [['查看所有', 'fa fa-eye']],
                success: function () {
                    $(".cameras-item").on('click', "a", function () {
                        clearHighlight();
                        var indexCode = $(this).data("indexcode");
                        var tmpdata = arrayUtil.filter(graphics, function (g) {
                            return g.attributes.indexCode === indexCode;
                        });
                        highlightGras(tmpdata);
                        startupVideo(0, $(this).data("indexcode"));
                    });
                },
                cancel: function () {
                    clearHighlight();
                    if(singleCameraViewScope){
                        clearInterval(singleCameraViewScope);
                    }
                },
                optbtn1: function () {
                    //由于目前控件最多支持9个同时预览 故要进行截取
                    if (cameras.length > 9) {
                        cameras = arrayUtil.filter(cameras, function (item, idx) {
                            return idx < 9;
                        });
                        console.log("出于网络传输速率考虑，一次最多支持同时预览4个!");
                    }
                    var hwCameras = [], hkCameras = [], dhCameras = [], dfCameras = [];
                    for (var i in cameras) {
                        var camera = cameras[i];
                        switch (camera.platform) {
                            case "hw":
                                hwCameras.push(camera);
                                break;
                            case "hk":
                                hkCameras.push(camera);
                                break;
                            case "dh":
                                dhCameras.push(camera);
                                break;
                            default :
                                dfCameras.push(camera);
                        }
                    }
                    switch (platform) {
                        case "hw":
                            hwCameras = $.merge(hwCameras, dfCameras);
                            break;
                        case "hk":
                            hkCameras = $.merge(hkCameras, dfCameras);
                            break;
                        case "dh":
                            dhCameras = $.merge(dhCameras, dfCameras);
                            break;
                    }
                    //组织附近的监控点 以多宫格形式打开预览
                    if (hwCameras.length > 0 && hkCameras.length > 0) {
                        layer.open({
                            title: "平台选择",
                            content: "所选监控点来自不同平台，请选择其中一个进行查看！",
                            btn: ['海康', '千里眼'],
                            btn1: function (index, layero) {
                                startupVideo(0, hkCameras);
                            },
                            btn2: function (index, layero) {
                                startupVideo(0, hwCameras);
                            }
                        });
                    } else {
                        startupVideo(0, cameras);
                    }
                }
            });
        }

        /***
         * 控制云台转向某个点(hk)
         * 或传递ptz参数 控制云台方位
         * @param indexCode
         * @param geometry
         * @param ptz
         */
        function setCameraPtz(indexCode, geometry, ptz, platform) {
            if (indexCode === null || indexCode === undefined) return;
            if (geometry !== null && geometry !== undefined) {
                var sr = __map.spatialReference;
                var inSR = sr.hasOwnProperty("wkid") ? sr.wkid : sr.wkt;
                $.ajax({
                    url: root + '/video/camera/ptz',
                    data: {indexCode: indexCode, inSR: inSR, point: geometry, platform: platform},
                    success: function (r) {
                        if (r.hasOwnProperty("success") && r.success === false) {
                            console.warn(r.msg);
                        }
                    }
                });
            }
        }

        var pro = undefined;
        /**
         *添加监听
         */
        var selectCamera;

        /***
         * 项目列表相关监听
         */
        function addProjectListeners() {
            //第一次显示项目标签页时 请求后台数据 监听新建按钮事件
            $('#proTab').on('shown.bs.tab', function () {
                if (esriLang.isDefined(proList) && proList.length == 0) {
                    if (VmOptions.showProLocAll) {
                        requestProLocAll();
                    }
                    refreshProPage(0);
                }
                //新建项目
                $("#addBtn").off("click").on('click', function () {
                    $btn = $(this).addClass("disabled");
                    $btn.attr("disabled", "disabled");
                    $btn.text("新建中");
                    clear();
                    drawTool = drawTool ? drawTool : new Draw(__map);
                    drawHandler = drawTool.on("draw-end", lang.hitch(map, addProToMap));
                    esri.bundle.toolbars.draw.addPoint = "单击添加项目位置";
                    drawTool.activate(Draw.POINT);
                    var mapClickHandler = __map.on('click', function (evt) {
                        window.clearTimeout(timeOutHandler);
                        mapClickHandler.remove();
                    });
                    timeOutHandler = window.setTimeout(function () {
                        drawTool.deactivate();
                        if ($btn) {
                            $btn.removeClass("disabled");
                            $btn.removeAttr("disabled");
                            $btn.html('<i class="fa fa-plus"></i> 新建');
                        }
                    }, 5000);
                });

                $("#screenBtn").unbind("click").on('click', function () {
                    $("#orderBtn").removeClass('btn-success');
                    $(this).toggleClass('btn-success');
                    $('#orderPanel').hide();
                    $('#screenPanel').toggle();
                });

                $("#orderBtn").unbind("click").on('click', function () {
                    $("#screenBtn").removeClass('btn-success');
                    $(this).toggleClass('btn-success');
                    $('#screenPanel').hide();
                    $('#orderPanel').toggle();
                });

                $('#orderPanel li i').unbind("click").on('click', function () {
                    $('#orderPanel li i').removeClass('order-selected');
                    $(this).addClass('order-selected');
                    var orderField = $(this).data('type');
                    $('#orderPanel li span').removeClass('order-selected');
                    $("#" + orderField).addClass('order-selected');
                    var order = 'desc';
                    if ($(this).hasClass('fa-caret-up'))
                        order = 'asc';
                    var ajaxUrl = "/omp/project/page?ieRefresh=" + Math.uuid(32);
                    var postData = {ownerId: loginUser.id, orderField: orderField, order: order};
                    $.ajax({
                        url: ajaxUrl,
                        data: postData,
                        async: false
                    }).done(function (s, n) {
                        layer.close(loadHandler);
                        if (n == "success") {
                            proList = s.content;
                            renderProList();
                            //显示分页控件
                            if (s.pageCount > 1) {
                                showPageTool("proPageTool", s.pageCount, 0, refreshProPage);
                            } else
                                $("#proPageTool").hide();
                        }
                    });
                });

                $("#proWarnBtn").unbind("click").on("click",function () {
                    $("#screenBtn").removeClass('btn-success');
                    $(this).toggleClass('btn-success');
                    $.get(dojoConfig.baseUrl+widgetPath+"/warnHtml.html",function (html) {
                        //打开图层
                        layer.open({
                            type: 1
                            , title: '预警信息'
                            , area: ['755px', '500px']
                            , shade: 0.5
                            , maxmin: true
                            , zIndex: layer.zIndex
                            , content:html
                        });
                    })
                })
            });
            //根据条件分页查询数据
            $('input[data-name="proName"]').on('input propertychange', lang.hitch(this, searchProPage));
            $('input[data-name="year"]').on('input propertychange', lang.hitch(this, searchProPage));
            $('select[data-name]').on('change', lang.hitch(this, searchProPage));
            $('#organSelector').on('change', lang.hitch(this, searchProPage));

            var scrollHeight = $(window).height() - 300;

            if (VmOptions.videoStyle === VIDEO_STYLE_GROUP) {
                var width = $('#panel-VideoManagerST').width();
                $('#video_list_content').slimScroll({
                    height: scrollHeight + 90,
                    width: width - 5,
                    railVisible: true,
                    railColor: '#333',
                    railOpacity: .2,
                    railDraggable: true
                });
            }

            var proContentHeight;
            var clientHeight = document.body.clientHeight;
            if (clientHeight < 600) {
                proContentHeight = 350;
            } else if (clientHeight < 800) {
                proContentHeight = 385;
            } else if (clientHeight < 1000) {
                proContentHeight = 700;
            }
            $("#proContent").slimScroll({
                height: proContentHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        }

        /**
         * 请求所有项目的位置信息
         */
        function requestProLocAll() {
            $.getJSON('/omp/project/loc/all', {ownerId: loginUser.id}, function (data) {
                if (esriLang.isDefined(data.success)) {
                    console.error(data.msg);
                    return;
                }
                if (lang.isArray(data)) {
                    arrayUtil.forEach(data, function (item) {
                        var proid = item[0];
                        var protype = item[1];
                        var location = item[2].replace(/\"/, '"');
                        var g = new Graphic(JsonConverters.toEsri($.parseJSON(location)));
                        g.setSymbol(getProSymbol(protype));
                        g.setAttributes({proId: proid, proType: protype});
                        graphicsLayer.add(g);
                        proGraphics.push(g);
                    });
                }
            });
        }

        /***
         * 获取项目位置的symbol
         * @param type
         */
        function getProSymbol(type) {
            if (esriLang.isDefined(type)) {
                var proColor = arrayUtil.filter(VmOptions.proType, function (item) {
                    if (item.name === type)
                        return item;
                })[0];
                var color = [255, 255, 255];
                if (proColor != undefined)
                    color = EsriSymbolsCreator.colorFromHex(proColor.color);
                return EsriSymbolsCreator.createSimpleMarkerSymbol(EsriSymbolsCreator.markerStyle.STYLE_CIRCLE, 12,
                    EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color(color), 1), Color(color));
            }
        }

        /**
         * 查询项目信息并分页显示
         */
        function searchProPage() {
            var sel = $('select[data-name="proType"]')[0];
            var $input = $('input[data-name="proName"]')[0];
            var $year = null;
            if (__config.showYearInfo != undefined && __config.showYearInfo) {
                $year = $('input[data-name="year"]')[0];
            }
            var regionCode = $('#organSelector').val();
            var proName = $input.value;
            searchCondition = undefined;
            var val = sel.value;
            if (val != undefined && val != "") {
                if (searchCondition === undefined)
                    searchCondition = {};
                searchCondition.proType = val;
            }
            if (proName != undefined && proName != "") {
                if (searchCondition === undefined)
                    searchCondition = {};
                searchCondition.proName = proName;
            }
            if ($year != null) {
                if (searchCondition === undefined)
                    searchCondition = {};
                var yearVal = $year.value;
                searchCondition.year = yearVal;
            }
            refreshProPage(0, searchCondition);
        }

        /**
         * 创建项目
         * @param cameras
         */
        function createProject(cameras) {
            var geojson = JsonConverters.toGeoJson(proGra);
            if (cameras != undefined && cameras.length > 0) {
                arrayUtil.forEach(cameras, function (item) {
                    console.log('%o', item);
                    setCameraPtz(item.indexCode, JSON.stringify(geojson.geometry), null, item.platform);
                });
            }
            var tpl = $("#proFormTpl").html();
            var proFormTemplate = HbarsUtils.compile(tpl);
            HbarsUtils.regHelper('if', function (conditional, options) {
                if (conditional) {
                    return options.fn(this);
                } else {
                    return options.inverse(this);
                }
            });
            if (addProHandler) layer.close(addProHandler);

            addProHandler = layer.open({
                type: 1,
                title: ['<i class="fa fa-plus"></i>&nbsp;新建项目', 'background-color:#428bca;color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
                area: '620px',
                shade: 0,
                content: proFormTemplate({
                    cameras: cameras,
                    bufferDis: BUFFER_DISTANCE,
                    location: JSON.stringify(geojson),
                    txVersion: false,
                    isMultiple: (__config.multipleVideo != undefined && __config.multipleVideo != false) ? true : false,
                    showYearInfo: (__config.showYearInfo != undefined && __config.showYearInfo != false) ? true : false,
                    relateToPlotInfos: VmOptions.relateToPlotInfos
                }),
                btn: [],
                offset: ['140px', '600px'],
                cancel: function () {
                    //clear();
                    graphicsLayer.remove(proGra);
                    graphicsLayer.remove(bufferGra);
                    proGra = null;
                    bufferGra = null;
                    esri.bundle.toolbars.draw.addPoint = "单击以添加点";
                    layer.close(addProHandler);
                },
                success: function () {
                    $(".radio-inline>input[type='radio']").eq(0).attr("checked", true);
                    if ($btn) {
                        // $btn.button('reset');
                        $btn.removeClass("disabled");
                        $btn.removeAttr("disabled");
                        $btn.html('<i class="fa fa-plus"></i> 新建');
                    }
                }
            });
            if (newProjectLayer != undefined && newProjectLayer.length > 0) {
                getRelationInfo();
            }
            $('#proTypeSelect').append(proTypeSelectTpl);
            $("#addProForm").Validform({
                tiptype: function (msg, o, cssctl) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        cssctl(objtip, o.type);
                        objtip.text(msg);

                        var infoObj = o.obj.next();
                        if (o.type == 2) {
                            infoObj.fadeOut(200);
                        } else {
                            if (infoObj.is(":visible")) {
                                return;
                            }

                            var left = o.obj.get(0).offsetLeft + 15,
                                top = o.obj.get(0).offsetTop + o.obj.get(0).offsetHeight + 6;

                            infoObj.css({
                                left: left,
                                top: top
                            }).show().animate({
                                //top:0
                            }, 200);
                        }
                    }
                },
                callback: function (data) {
                    proGra.symbol = getProSymbol(VmOptions.proType);
                    graphicsLayer.add(proGra);
                    proGraphics.push(proGra);
                    $.ajax({
                        type: 'post',
                        url: '/omp/project/save?ownerId=' + loginUser.id,
                        data: data.serialize(),
                        dataType: 'json',
                        success: function (_r, _s) {
                            if (_r.hasOwnProperty('success')) return;
                            //保存成功之后 需要刷新项目列表以及视频监控列表
                            layer.msg('保存成功', {
                                time: 1000, end: function () {
                                    layer.close(addProHandler);
                                    graphicsLayer.remove(bufferGra);
                                    bufferGra = null;
                                    refreshProPage(0, null);
                                    // refreshVideos();
                                }
                            });
                        },
                        error: function (_ex) {
                            console.error(_ex.responseText);
                        }
                    });

                    return false;
                },
                btnSubmit: "#proSaveBtn"
            });

            $(".pro-open-video").on('click', function () {
                startupVideo(0, $(this).data("indexcode"));
            });
        }

        /***
         * 添加项目位置到地图上
         * @param evt
         */
        function addProToMap(evt) {
            drawHandler.remove();
            var pnt = evt.geometry;
            newProjectPnt = pnt;        //项目位置， 用于进行属性识别 生成项目名称等关联信息
            proGra = new Graphic(pnt, locPntSym, {}, null);
            drawTool.deactivate();
            //用绘制的点做缓冲区 并查询附近的监控点
            if (geoUrl == undefined) {
                layer.alert('未配置geometryService的地址!', {btn: [], shade: 0});
                return;
            }
            msgHandler = layer.msg('搜索附近的监控点..', {time: 20000});
            if (geometryService == undefined) {
                geometryService = new GeometryService(geoUrl);
                geometryService.on('buffer-complete', lang.hitch(evt.geometry, onBufferCompleteHandler));
                geometryService.on('error', onBufferErrorhandler);
            }
            var params = new BufferParameters();
            params.geometries = [evt.geometry];
            params.distances = [BUFFER_DISTANCE];
            params.unit = GeometryService.UNIT_METER;
            params.bufferSpatialReference = new SpatialReference({wkid: 2364});
            params.outSpatialReference = __map.spatialReference;
            geometryService.buffer(params);
        }

        /**
         * 缓冲完成后 搜索缓冲范围内的监控点
         * @param evt
         */
        function onBufferCompleteHandler(evt) {
            var geos = evt.geometries;
            if (geos != undefined && geos.length > 0) {
                var bufferGeo = geos[0];
                bufferGra = new Graphic(bufferGeo, bufferSymbol, {}, null);
                graphicsLayer.add(bufferGra);
                graphicsLayer.add(proGra);//添加位置标注点
                __map.setExtent(bufferGeo.getExtent().expand(2.5));
                //搜索缓冲范围内的监控点
                var cameras = findNearCameras(bufferGeo);
                layer.close(msgHandler);
                createProject(cameras);
            } else
                layer.close(msgHandler);
        }

        /***
         * buffer 异常
         * @param ex
         */
        function onBufferErrorhandler(ex) {
            console.error(ex.message);
        }

        /***
         * 找出附近的监控点
         * @param geo
         */
        function findNearCameras(geo) {
            var data = [];
            arrayUtil.forEach(videoList, function (item) {
                var pnt = new Point(item.x, item.y, __map.spatialReference);
                if (geo.contains(pnt)) {
                    data.push(item);
                }
            });
            return data;
        }

        /***
         * clear
         */
        function clear(keepRightPanel) {
            $('.pro-info-content').removeClass('detail-clicked');
            clearIdentify();
            $('.contentPane .close').click();
            msgHandler = null;
            addProHandler = null;
            openVideoHandler = null;
            layer.closeAll();
            clearHighlight();
            if (!keepRightPanel || keepRightPanel == undefined)
                layoutManager.hideRightPanel();
            graphicsLayer.remove(proGra);
            graphicsLayer.remove(bufferGra);
            proGra = null;
            bufferGra = null;
            esri.bundle.toolbars.draw.addPoint = "单击以添加点";
        }

        /**
         * 刷新项目页面
         * @param currPage
         */
        function refreshProPage(page, condition) {
            pageSize = parseInt((($("#proContent").height()) / 70), 10) + 1;
            loadHandler = layer.load(2, {offset: ['300px', '245px']});
            var ajaxUrl = "/omp/project/page?ieRefresh=" + Math.uuid(32);
            var postData = {page: page, size: pageSize, ownerId: loginUser.id};
            if (esriLang.isDefined(condition) && ("object" == typeof condition)) {
                ajaxUrl = "/omp/project/search?ieRefresh=" + Math.uuid(32);
                postData = lang.mixin(postData, condition);
            }
            $.ajax({
                url: ajaxUrl,
                data: postData,
                async: false
            }).done(function (s, n) {
                layer.close(loadHandler);
                if (n == "success") {
                    proList = s.content;
                    renderProList(condition);
                    //显示分页控件
                    if (s.pageCount > 1) {
                        showPageTool("proPageTool", s.pageCount, page, refreshProPage);
                    } else
                        $("#proPageTool").hide();
                }
            });
        }

        /*
         * 渲染项目列表页面
         * 有位置信息的将其位置显示到地图上
         */
        var lastPostData = null;

        function renderProList(condition) {
            if (esriLang.isDefined(proList) && proList.length > 0) {
                if (!VmOptions.showProLocAll) {
                    //在地图上定位项目点
                    clearGraphicsLayer(proGraphics);
                    $.each(proList, function (index, value) {
                        var location = value.location;
                        if (esriLang.isDefined(location)) {
                            var obj = JsonConverters.toEsri($.parseJSON(location));
                            var g = new Graphic(obj);
                            g.setSymbol(getProSymbol(value.proType));
                            g.setAttributes(value);
                            graphicsLayer.add(g);
                            proGraphics.push(g);
                        }
                    });
                } else {
                    var postData = {ownerId: loginUser.id};
                    postData = lang.mixin(postData, condition);
                    if (JSON.stringify(postData) !== JSON.stringify(lastPostData)) {
                        //只当查询条件改变时才去重新请求
                        clearGraphicsLayer(proGraphics);
                        lastPostData = postData;
                        $.getJSON('/omp/project/searchAll', postData, function (data) {
                            if (esriLang.isDefined(data.success)) {
                                console.error(data.msg);
                                return;
                            }
                            if (lang.isArray(data)) {
                                arrayUtil.forEach(data, function (item) {
                                    var proid = item.id;
                                    var protype = item.proType;
                                    var location = item.location.replace(/\"/, '"');
                                    var g = new Graphic(JsonConverters.toEsri($.parseJSON(location)));
                                    var g = new Graphic(JsonConverters.toEsri($.parseJSON(location)));
                                    g.setSymbol(getProSymbol(protype));
                                    g.setAttributes({proId: proid, proType: protype});
                                    graphicsLayer.add(g);
                                    proGraphics.push(g);
                                });
                            }
                        });
                    }
                }
                $("#proContent").empty();
                $("#proContent").append(HbarsUtils.renderTpl($("#proContentTplNew").html(), {pros: proList}));
                //点击项目详情 定位到项目 并打开详细信息
                $(".pro-info-opt").on('click', 'a', function () {
                    var proid = $(this).data("proid");
                    var indexCode = $(this).data("indexcode");
                    var op = $(this).data("type");
                    if (proid != proidRecord) {
                        $('.pro-info-content').removeClass('detail-clicked').removeClass('record-clicked');
                        proidRecord = proid;
                    }
                    //项目的记录查看、定位、删除
                    switch (op) {
                        case 'loc': {
                            var results = arrayUtil.filter(proList, function (item) {
                                return item.proId === proid;
                            });
                            if (results.length > 0) {
                                var tmp = JsonConverters.toEsri($.parseJSON(results[0].location)).geometry;
                                var pnt = new Point(tmp.x, tmp.y, __map.spatialReference);
                                __map.setScale(40000);
                                __map.centerAt(pnt);
                            }
                            break;
                        }
                        case 'record': {
                            $('.pro-info-content').removeClass('record-clicked');
                            $('#' + proid).addClass('record-clicked');
                            if (!__config.multipleVideo) {
                                renderHistoryRecords(proid, indexCode);
                            } else {
                                RecordViewer.renderRecord(proid, indexCode, __config, true);
                            }
                            break;
                        }
                        case 'del': {
                            layer.confirm('确定要删除该项目吗？', {
                                btn: ['是', '否'] //按钮
                            }, function () {
                                $.ajax({
                                    url: root + '/project/delete/' + proid,
                                    method: 'post',
                                    success: function (rp) {
                                        if (rp.result == true) {
                                            var result = arrayUtil.filter(proGraphics, function (item) {
                                                return item.attributes.proId === proid;
                                            });
                                            graphicsLayer.remove(result[0]);
                                            proGraphics.splice(result, 1);
                                            $('#' + proid).remove();
                                            $('.history-close-btn').click();
                                            layer.msg("删除成功！", {time: 1500})
                                        }
                                    }
                                });
                            });
                            break;
                        }

                    }
                });
                //打开项目的详细信息
                $(".pro-info-header").on('click', function () {
                    var proid = $(this).parent().parent().data("proid");
                    $('.pro-info-content').removeClass('detail-clicked');
                    $('#' + proid).addClass('detail-clicked');
                    var results = arrayUtil.filter(proList, function (item) {
                        return item.proId === proid;
                    });
                    if (results[0] != undefined) {
                        openProDetail(results[0]);
                    }
                });
            }
            else {
                $("#proContent").empty();
                var result = "<div class='text-muted text-center m-t-5'>无记录！</div>";
                $("#proContent").append(result);
            }
        }

        // 当前选中的 项目
        var currentPro = undefined;

        /**
         * 打开项目的详细信息
         * @param pro
         */
        function openProDetail(pro) {
            currentPro = pro;
            var linkedCameras = null;
            var presets = [];
            var results = [];
            //兼容2.0版本（直接读cameraId字段） 和 2.1版本没有摄像头信息从关系表获取摄像头关系
            if (!pro.cameraId && (!pro.cameras || pro.cameras.length == 0)) {
                $.ajax({
                    url: root + "/project/ref/cameras",
                    async: false,
                    data: {proId: pro.proId},
                    success: function (cameraIds) {
                        arrayUtil.forEach(cameraIds, function (cameraId) {
                            var tempArr = arrayUtil.filter(videoList, function (item) {
                                return item.indexCode === cameraId;
                            });
                            if (esriLang.isDefined(tempArr) && tempArr.length > 0) {
                                $.merge(results, tempArr);
                            }
                        });
                        //核心方法
                        results = bulidProRefCameras(pro, results);
                    }
                });
            } else if (esriLang.isDefined(pro.cameras)) {
                results = pro.cameras;
            }
            //2.0版本数据
            if (esriLang.isDefined(pro.cameraId)) {
                results = arrayUtil.filter(videoList, function (item) {
                    return item.indexCode === pro.cameraId;
                });
                results = bulidProRefCameras(pro, results);
            }

            if (results.length > 0) {
                linkedCameras = results;
            }
            if (proDetailHandler) {
                layer.close(proDetailHandler);
            }
            var proDetailTpl = $("#proDetailTpl").html();
            presets = presets.concat(pro.presets);  // 项目里关联的所有预置位

            HbarsUtils.registerPartial('presetTpl', $("#presetListTpl").html());
            HbarsUtils.regHelper('if', function (conditional, options) {
                if (conditional) {
                    return options.fn(this);
                } else {
                    return options.inverse(this);
                }
            });

            var pos = JsonConverters.toEsri($.parseJSON(pro.location), __map.spatialReference);
            if (proDetailHandler) layer.close(proDetailHandler);
            var optBtns = [];
            if (__config.modifyProVideo) {
                optBtns.push(["其他监控点", "fa fa-search", ""]);
                optBtns.push(["照片", "fa fa-file-image-o", ""]);
            }

            proDetailHandler = layer.open({
                type: 1,
                title: ['<i class="fa fa-bars"></i>&nbsp;&nbsp;&nbsp;项目详情', 'color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
                area: '550px',
                shade: 0,
                content: HbarsUtils.renderTpl(proDetailTpl, lang.mixin(pro, {
                    cameras: linkedCameras,
                    layerHide: 'layerHide',
                    showYearInfo: (__config.showYearInfo !== undefined && __config.showYearInfo !== false) ? true : false
                })),
                btn: [],
                optbtn: optBtns,
                offset: ['140px', '500px'],
                optbtn1: function (index, btn) {
                    bufferProNearVideos(pro);
                },
                optbtn2: function (index, btn) {
                    viewPhotos(pro.proId);
                },
                cancel: function () {
                    clear(true);
                    clearHighlight();
                }
            });
            var pnt = new Point(pos.geometry.x, pos.geometry.y, __map.spatialReference);
            __map.setScale(40000);
            __map.centerAt(pnt);

            //预置位列表切换，功能监听
            $(".pro-camera-span").on('click', function (event) {
                event.stopPropagation();

                if ($(this).hasClass("pro-cameras-selected")) {
                    $(this).removeClass("pro-cameras-selected");
                } else {
                    $(".pro-camera-span").removeClass("pro-cameras-selected");
                    $(this).addClass("pro-cameras-selected");
                }

                $($(this).attr("href")).collapse('toggle');
                $($(this).attr("href")).siblings().collapse('hide');
            });

            $('.collapse').collapse({
                toggle: false
            });

            $(".disabledPreset").iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue'
            });
            $(".icheckbox-blue").css({'display': 'inline-block', 'top': '4px'});

            if (!isAdmin) {
                $(".disabledPreset").parent().addClass("hidden");
                $(".disabledPreset").parent().siblings("label").addClass("hidden");
            }

            // 预设位直接拍照
            $(".preset-capture").off('click').on('click', function () {
                var cameraId = $(this).data("indexcode");
                var presetNo = $(this).data("pno");
                // 请求后台进行预设位拍照
                msgHandler = layer.msg('通信中...', {icon: 16, shade: 0.1, time: 5000});
                var proId = currentPro.proId;
                $.getJSON(root + '/video/capture/preset', {
                    proId: proId,
                    indexCode: cameraId,
                    preset: presetNo
                }, function (res) {
                    if (res.hasOwnProperty("success")) {
                        layer.close(msgHandler);
                        console.error('预设位拍照异常: %o', res);
                        return;
                    }
                    console.log("预设位拍照结果: %s", res);
                    // 开始轮询后台 获取照片信息
                    var startTime, endTime;
                    var now = new Date();
                    startTime = now.format("yyyy-MM-dd hh:mm:ss");
                    now.setHours(23, 59, 59);
                    endTime = now.format("yyyy-MM-dd hh:mm:ss");

                    pollingCount = 0;
                    if (msgHandler !== undefined) {
                        layer.close(msgHandler);
                    }
                    msgHandler = layer.msg('获取照片...', {icon: 16, shade: 0.1, time: 20000});
                    console.time('拍照耗时');
                    pollingIntervalId = setInterval(function () {
                        pollingGetPics(proId, cameraId, startTime, endTime);
                    }, 2000);
                });
            });

            // 预设位 查看 事件
            $(".preset-view").off('click').on('click', function () {
                var cameraId = $(this).data("indexcode");
                //查看时高亮显示项目关联的摄像头
                var tmpGra = arrayUtil.filter(graphics, function (item) {
                    return item.attributes.indexCode == cameraId;
                });
                highlightGras(tmpGra);
                startupVideo(0, cameraId, $(this).data("pno"));
            });
            // 预设位 设置 事件
            $(".preset-set").off('click').on('click', function () {
                var cameraId = $(this).data("indexcode");
                //设置时高亮显示项目关联的摄像头
                var tmpGra = arrayUtil.filter(graphics, function (item) {
                    return item.attributes.indexCode == cameraId;
                });
                highlightGras(tmpGra);
                var pNo = $(this).data("pno");
                $.getJSON(root + "/video/preset/isCapture", function (rp) {
                    if (rp.result) {
                        layer.msg('预设位正在拍照，暂时不能操作！', {icon: 5});
                    } else {
                        startupVideo(1, cameraId, pNo);
                    }
                })
            });
            //预设位 删除 事件
            $(".preset-del").off('click').on('click', function () {
                deletePreset(pro, $(this).data("id"));
            });

            //预设位失效监听
            $('.disabledPreset').off('ifChecked').on('ifChecked', function (event) {
                setEnabledPreset({enabled: false, id: $(event.currentTarget).data('id')}, presets, function (flag) {
                    $(event.currentTarget).parent().siblings(".preset-set, .preset-view").addClass("hidden");
                });
            });

            $('.disabledPreset').off('ifUnchecked').on('ifUnchecked', function (event) {
                setEnabledPreset({enabled: true, id: $(event.currentTarget).data('id')}, presets, function (flag) {
                    $(event.currentTarget).parent().siblings(".preset-set, .preset-view").removeClass("hidden");
                });
            });

            $("a[name='addPresetBtn']").off('click').on('click', function (event) {
                event.stopPropagation();
                addNewPreset(pro, $(this).data("indexcode"));
            });

            // 显示或隐藏预置位列表
            $("span[data-toggle='collapsePreset']").off('click').on('click', function () {
                collapsePanel.collapse('toggle');
            });

            //只是预览
            $("a[name=previewBtn]").off('click').on('click', function () {
                startupVideo(0, $(this).data("indexcode"));
            });
        }

        // 轮询次数
        var pollingCount = 0;
        // 轮询句柄
        var pollingIntervalId;

        /**
         * 轮询获取拍摄的照片
         * 1. 取到结果后 去除轮询
         * 2. 轮询20次后 仍无响应 则去除轮询
         * @param proid
         * @param startTime
         * @param endTime
         */
        function pollingGetPics(proid, indexcode, startTime, endTime) {
            pollingCount++;
            if (pollingCount === 20) {
                if (msgHandler !== undefined) {
                    layer.close(msgHandler);
                }
                window.clearInterval(pollingIntervalId);
                return false;
            }
            $.ajax(root + "/project/records/files", {
                data: {
                    proid: proid,
                    startTime: startTime,
                    endTime: endTime
                },
                async: false,
                success: function (res) {
                    if (lang.isArray(res) && res.length > 0) {
                        window.clearInterval(pollingIntervalId);
                        // 延迟几秒后 刷新识别列表
                        setTimeout(function () {
                            if (msgHandler !== undefined) {
                                layer.close(msgHandler);
                            }
                            console.timeEnd('拍照耗时');
                            layer.msg('拍照成功!', {icon: 1});
                            // 刷新右侧面板 显示拍照结果
                            renderHistoryRecords(proid, indexcode);
                            getRecogs();
                        }, 2000);
                    }
                }
            });
        }

        /**
         * 构建项目关联的摄像头信息和每个摄像头的预设位信息
         * @param pro
         * @param cameras
         */
        function bulidProRefCameras(pro, cameras) {
            var newCameras = [];
            arrayUtil.forEach(cameras, function (camera) {
                var hasPreset = false;//是否有预设位
                var newCamera = lang.clone(camera);
                newCamera.presets = [];
                arrayUtil.forEach(pro.presets, function (preset) {
                    if (newCamera.indexCode === preset.indexCode) {
                        hasPreset = true;
                        newCamera.hasPresets = true;
                        newCamera.presets.push(preset);
                        return false;
                    }
                });

                if (!hasPreset)
                    newCamera.hasPresets = false;
                newCameras.push(newCamera);
            });

            return newCameras;
        }

        /**
         * 缓冲项目点附近的监控并展示
         */
        function bufferProNearVideos(pro) {
            var searchLoading = layer.msg('正在缓冲查询项目点『' + pro.name + "』附近的其他监控点...", {time: 20000});
            var pos = JsonConverters.toEsri($.parseJSON(pro.location), __map.spatialReference);
            var pnt = new Point(pos.geometry.x, pos.geometry.y, __map.spatialReference);
            var bufferIndex = null;
            GeometryServiceTask.buffer(pnt, BUFFER_DISTANCE, function (r) {
                var geos = r.geometries;
                if (geos != undefined && geos.length > 0) {
                    var bufferGeo = geos[0];
                    //搜索缓冲范围内的监控点
                    var cameras = findNearCameras(bufferGeo);
                    layer.close(searchLoading);
                    if (cameras.length > 0) {
                        arrayUtil.forEach(pro.cameras, function (proCameraItem) {
                            cameras = arrayUtil.filter(cameras, function (item) {
                                if (proCameraItem.indexCode != item.indexCode) {
                                    //对于查询出来的监控点 请求后台转动监控点对准此点
                                    console.log('%o', item);
                                    setCameraPtz(item.indexCode, pro.location, null, item.platform);
                                    return item;
                                }
                            });
                        });

                        var bufferVideosTemplate = HbarsUtils.compile($("#bufferOtherVideosTpl").html());
                        bufferIndex = layer.open({
                            type: 1,
                            title: "项目『" + pro.name + "』附近监控点缓冲结果",
                            content: bufferVideosTemplate({cameras: cameras, exsitCameras: pro.cameras}),
                            area: '500px',
                            success: function () {
                                listeners();
                            },
                            end: function () {
                                layer.closeAll();
                                openProDetail(pro);
                            }
                        });
                    } else
                        layer.msg("附近没有监控点!", {time: 1500});
                }

            }, {outSpatialReference: __map.spatialReference});

            function listeners() {
                $("#addProVideo").off("click").on('click', function () {
                    var indexcodes = [];
                    $(":checkbox:checked").each(function () {
                        indexcodes.push($(this).val());
                    });

                    if (indexcodes.length > 0)
                        $.ajax({
                            url: root + "/project/cameras/add",
                            method: "post",
                            data: {
                                id: pro.id,
                                proId: pro.proId,
                                indexCodes: indexcodes.toString()
                            },
                            success: function (presets) {
                                pro.presets.push(presets);
                                if (pro.cameras == null) pro.cameras = [];
                                //更新pro对象的属性值
                                arrayUtil.forEach(videoList, function (video) {
                                    arrayUtil.forEach(indexcodes, function (indexCode) {
                                        if (video.indexCode == indexCode) {
                                            var camera = lang.clone(video);
                                            camera.presets = [];
                                            pro.cameras.push(camera);
                                            arrayUtil.forEach(presets, function (preset) {
                                                if (preset.indexCode == video.indexCode) {
                                                    camera.presets.push(preset);
                                                }
                                            })
                                        }
                                    })
                                });
                                var tpl = null;
                                arrayUtil.forEach(indexcodes, function (indexCode) {
                                    tpl = '<li>' +
                                        '<a title="打开视频" class="pro-open-video" href="#" data-indexcode="' + indexCode + '">' +
                                        $(":checkbox:checked").parent("li").find("a:first").text() + '</a>' +
                                        '<a class="label label-danger pull-right video-del"  data-indexcode="' + indexCode + '" data-type="del" title="删除">' +
                                        '<i class="fa fa-times"></i></a>' +
                                        '</li>'
                                    $("#exsitVideos").append(tpl);
                                });

                                $(":checkbox:checked").parent("li").remove();
                                if ($("#bufferVideosCt").find(":checkbox").length == 0) {
                                    $("#bufferVideosCt").append('<p class="empty-worn">项目附近无新的缓冲监控点</p>');
                                    $("#addProVideo").addClass("disabled");
                                }
                                listeners();
                            },
                            error: function () {

                            }
                        })
                });

                $("#closeBufferPanel").off("click").on('click', function () {
                    layer.close(bufferIndex);
                });


                $(".video-del").off("click").on('click', function () {
                    var indexCode = $(this).data("indexcode");
                    var $this = $(this);
                    $.ajax({
                        url: root + "/project/camera/delete",
                        method: "post",
                        data: {
                            id: pro.id,
                            proId: pro.proId,
                            indexCode: indexCode
                        },
                        success: function (rp) {
                            if (rp.result) {
                                $.each(pro.cameras, function (i, camera) {
                                    if (camera.indexCode == indexCode)
                                        pro.cameras = pro.cameras.slice(0, i).concat(pro.cameras.slice(i + 1, pro.cameras.length)); //删除这个数组元素
                                });

                                $.each(pro.presets, function (i, preset) {
                                    if (preset.indexCode == indexCode)
                                        pro.presets = pro.presets.slice(0, i).concat(pro.presets.slice(i + 1, pro.presets.length)); //删除这个数组元素
                                });

                                var tpl = "<li>" +
                                    '<input type="checkbox" name="cameraId" value="' + indexCode + '"/>' +
                                    '<a title="打开视频" class="pro-open-video" href="#" data-indexcode="' + indexCode + '">' +
                                    $this.parent("li").find("a:first").text() + '</a>' +
                                    "</li>";
                                $this.parent("li").detach();
                                if ($("#bufferVideosCt p").hasClass("empty-worn")) {
                                    $("#bufferVideosCt .empty-worn").remove();
                                    $("#addProVideo").removeClass("disabled");
                                }
                                ;
                                $("#bufferVideosCt").append(tpl);
                            } else
                                layer.msg(rp.message)
                        },
                        error: function () {

                        }
                    });
                });

                $(".pro-open-video").on('click', function () {
                    startupVideo(0, $(this).data("indexcode"));
                });
            }
        }

        /**
         * 项目直接查看照片
         * @param proId
         */
        function viewPhotos(proId) {
            $.getJSON(root + "/file/records/getByProId", {proId: proId}, function (rp) {
                var json = {
                    "title": "照片记录",
                    "id": proId,
                    "start": rp.length - 1
                };

                var data = [];
                for (var i in rp) {
                    var d = {
                        "pid": rp[i],
                        "src": "/omp/file/original/" + rp[i]
                    };

                    data.push(d);
                }
                json.data = data;
                layer.photos({
                    //type: 1,
                    //shadeClose: false,
                    closeBtn: 1,
                    area: ['900px', '600px'],
                    photos: json
                });

            });
        }

        $('#myTabs a').click(function (e) {
            e.preventDefault()
            $(this).tab('show')
        })

        /**
         * 照片和巡查记录的tab页监听
         * 当选择巡查记录时 加载showInspectRecords
         */
        $('#recordsTab a').click(function (e) {
            e.preventDefault();
            if ($(e.target).attr('aria-controls') === 'record') {
                if ($("#record").children().length)
                    return;
                showInspectRecords(1);
            }
            $(this).tab('show')
        })

        /***
         * 渲染历史记录页面
         * @param proid
         */
        function renderHistoryRecords(proid, indexCode) {
            $("#record").removeAttr("data-indexcode");
            if (indexCode === undefined || indexCode === "") {
                $.ajax({
                    url: root + "/project/ref/cameras?proId=" + proid,
                    async: false,
                    success: function (r) {
                        if (r.length = 1) {
                            indexCode = r[0];
                        }
                        else {
                            indexCode = JSON.stringify(r);
                        }
                    }
                });
            }
            if (__config.supervise) {
                $("#record").attr("data-proid", proid);
                $("#record").attr("data-indexcode", indexCode);
            }
            //获取缩略图数据并处理
            getQuarterDataAndDetail(proid, JSON.stringify({}), function (result) {

                var recordsTpl = $("#historyRecordsTpl").html();
                var years = [];
                var date = new Date();  //当前日期
                var obj = {};           //右侧展示面板控制数据

                picRecords = result.records;
                years = years.concat(result.allYear);
                obj.hasRecords = true;
                obj.proName = result.project.proName;
                if (years.length === 0) {
                    obj.year = [];
                    obj.year.push(date.getFullYear());
                    obj.upload_btn_id = proid;
                    lang.mixin(obj, {hasRecords: false});
                    $('.btn-quarter').attr("disabled", "disabled");
                    $('#recordYear').attr("disabled", "disabled");
                } else {
                    if ($.inArray(date.getFullYear().toString(), years) === -1) {
                        years.push(date.getFullYear().toString());
                    }
                    obj.year = years;
                }

                $(".right_panel").empty();
                obj.showClose = function () { //显示关闭按钮
                    return "";
                };
                $(".right_panel").append(HbarsUtils.renderTpl(recordsTpl, obj));
                $("#recordYear").val(date.getFullYear());

                $(".history-records-container").slimScroll({
                    height: $(window).height() - 170,
                    railVisible: true,
                    railColor: '#333',
                    railOpacity: .2,
                    railDraggable: true
                });

                if ($("#recordsTab .active a").attr('aria-controls') == 'record') {
                    showInspectRecords(1);
                }
                //监听切换年份后渲染
                $("#recordYear").on('change', function () {
                    var condition = {
                        year: $(this).val()
                    };
                    getQuarterDataAndDetail(proid, JSON.stringify(condition), function (result) {
                        picRecords = result.records;
                        quarterRender(condition.year, 1, proid, result.records); //切换后默认在第一季度
                        try {
                            //选中当前的季度
                            if (date.getFullYear() == condition.year) {
                                $('#option' + DateUtils.quarter(date.toISOString())).trigger('click');
                            } else {
                                $('#option' + 1).trigger('click');
                            }
                        } catch (e) {
                            console.error(e);
                        }
                        addHistoryImgListener(result.records, proid);
                    });
                });
                addHistoryImgListener(result.records, proid);
                layoutManager.openRightPanel();
            });

            // 切选季度
            $('.btn-quarter').unbind('click').on('click', function (event) {
                var season = $(this).data('id');
                quarterRender($("#recordYear").val(), season, proid, picRecords);
                addHistoryImgListener(picRecords, proid);
            });

            try {
                //选中当前的季度, 江阴现场环境发现必须放在此处，否则脚本报错
                $('#option' + DateUtils.quarter(new Date().toISOString())).click();
            } catch (e) {
                console.error(e);
            }
        }

        /**
         * 获取缩略图数据并处理
         * @param proId
         * @param condtion  json序列化为串的参数
         * @param callback
         */
        function getQuarterDataAndDetail(proId, condition, callback) {
            var msgHandler = layer.msg("请求数据中..", {time: 3000});
            $.ajax({
                url: '/omp/project/records/all',
                method: 'post',
                data: {proid: proId, condition: condition},
                async: false
            }).done(function (s, n) {
                layer.close(msgHandler);
                if (n === "success") {
                    callback(s);
                }
            });
        }

        /**
         * 渲染缩略图界面
         * @param year
         * @param season
         * @param proId
         * @param records
         */
        function quarterRender(year, season, proId, records) {
            var yearObj, monthObj = null, seasonObjs = [];
            for (var i in records) {
                yearObj = records[i];
                if (yearObj.year === year) {
                    //找出符合当前季度的数据
                    var arrData = lang.clone(yearObj.yearData).reverse();
                    if (lang.isArray(arrData)) {
                        arrayUtil.forEach(arrData, function (item) {
                            var picMonth = parseInt(item.month, 10);
                            var quarter = DateUtils.quarter(year + "-" + (picMonth < 10 ? "0" + picMonth : picMonth) + "-01");
                            if (season === quarter) {
                                seasonObjs = seasonObjs.concat(item.monthData);
                            }
                        });
                    }
                    var seasons = {};
                    seasons.seasonObjs = seasonObjs;
                    seasons.generateSuffix = function () { //此方法用来生成img的后缀代码，解决ie不能加载重复的资源
                        return Math.random() * 1000;
                    };
                    $('#img_panel').empty();
                    var imgTpl = $('#seasonImgPanleTpl').html();

                    $("#img_panel").append(HbarsUtils.renderTpl(imgTpl, seasons));
                    yearObj = null, monthObj = null, seasonObjs = null;
                }
            }

            try {
                var filestores = records[0].yearData[0].monthData;
                var ids = arrayUtil.map(filestores, function (item) {
                    return item.id;
                });
                // 查询是否存在 智能识别结果 若存在则添加预警
                $.getJSON(root + '/video/recogs/origin', {ids: JSON.stringify(ids)}, function (res) {
                    if (lang.isArray(res)) {
                        //添加预警提示
                        console.log(res);
                        arrayUtil.forEach(res, function (item) {
                            var oid = item.originFile;
                            var rid = item.resultFile;
                            $("#".concat(oid)).before('<div class="alarm-info-btn" data-rid="' + rid + '"><span>预&nbsp;警!</span></div>');
                        });
                        $(".alarm-info-btn").off('click').on('click', function () {
                            var content = '<div style="padding: 10px;"><img id="img_{{img}}" style="width: 100%;height: 100%;" src="/omp/file/original/{{img}}"></div>';
                            var imgId = $(this).data('rid');
                            layer.open({
                                    title: false,
                                    type: 1,
                                    shadeClose: true,
                                    closeBtn: 1,
                                    area: '1000px',
                                    offset: '100px',
                                    content: HbarsUtils.renderTpl(content, {img: imgId}),
                                    success: function (layero, index) {
                                    }
                                }
                            );
                        });

                    }
                });
            } catch (ex) {
                console.error(ex);
            }
        }

        /**
         * 缩略图点击事件
         *  @param rs
         */
        function addHistoryImgListener(rs, proId) {

            $(".history-img-content img").on('error', function () {
                var alt = $(this).attr('alt');
                if (alt.endWith("mp4")) {
                    this.src = root + "/static/img/map/defaultmp4.png";
                    $(this).attr('title', '点击查看视频');
                    $(this).attr('data-type', 'mp4');
                } else {
                    this.src = root + "/static/img/map/defaultpic.gif";
                }
            });

            $(".history-img-content").unbind('click').on('click', 'img', function () {
                var id = $(this).data("id");
                var type = $(this).data("type");
                if (type != undefined && type === "mp4") {
                    //打开视频文件预览
                    layer.open({
                        type: 2,
                        title: '视频播放',
                        shade: 0,
                        area: ['640px', '320px'],
                        content: ['/omp/video/mp4player?fileId=' + id, 'no']
                    });
                } else {
                    var timestr = $(this).data('time').replace(/-/g, "/");
                    var date = new Date(timestr);
                    ImagesViewerUtil.showImages(date, rs);
                    //添加上传照片刷新照片记录列表功能
                    var handle;
                    if (handle) handle.remove();
                    handle = topic.subscribe("img/upload/quarterRender", function (result) {
                        picRecords = result.datas;
                        quarterRender(result.year, result.season, result.proId, result.datas);
                        addHistoryImgListener(result.datas, proId);
                    });
                }
            });

            //没有任何数据时的上传图片按钮监听
            $('.btn-upload').unbind('click').on('click', function () {
                var id = $(this).data('id');
                ImagesViewerUtil.queryProImages(null, id);
            });

            //监听右侧面板关闭事件
            $(".history-close-btn").unbind('click').on('click', function () {
                layoutManager.hideRightPanel();
                $('.pro-info-content').removeClass('record-clicked');
            });

            //多选照片监听
            $('.img_chk').iCheck({
                checkboxClass: 'icheckbox-green hover',
                radioClass: 'iradio-green'
            }).on('ifChecked', function (event) {
                $("#imgDel").removeClass("hidden");
                $("#imgCancel").addClass("hidden");
            }).on('ifUnchecked', function (event) {
                if ($('.img_chk:checked').length == 0) {
                    $("#imgDel").addClass("hidden");
                    $("#imgCancel").removeClass("hidden");
                }
            });

            //编辑照片监听
            $("#imgEdit").unbind('click').on('click', function () {
                $(this).addClass("hidden");
                $("#imgCancel").removeClass("hidden");
                $(".history-img-content .icheckbox-green").css({"display": "block"});
            });

            //删除照片
            $("#imgDel").unbind('click').on('click', function () {
                var fileIds = [];
                $('.img_chk:checked').each(function () {
                    fileIds.push($(this).val());
                });
                layer.confirm('您确定是要删除照片记录吗？', {
                    icon: 3,
                    btn: ['是的', '不要'] //按钮
                }, function () {
                    $.ajax({
                        url: root + '/project/record/remove',
                        method: "post",
                        data: {
                            proid: proId,
                            fileIds: fileIds.toString()
                        },
                        success: function (rp) {
                            if (rp.result) {
                                layoutManager.hideRightPanel();
                                layer.msg('删除成功！', {icon: 6, time: 2000}, function () {
                                    if (__config.supervise) {
                                        if (!__config.multipleVideo) {
                                            renderHistoryRecords(proId, $("#record").attr("data-indexcode"));
                                        } else {
                                            RecordViewer.renderRecord(proId, $("#record").attr("data-indexcode"), __config, true);
                                        }
                                    } else {
                                        if (!__config.multipleVideo) {
                                            renderHistoryRecords(proId);
                                        } else {
                                            RecordViewer.renderRecord(proId, $("#record").attr("data-indexcode"), __config, true);
                                        }
                                    }
                                });
                            } else {
                                layer.msg('删除出现异常,请联系管理员！', {icon: 5});
                            }
                        }
                    });
                });
            });

            //取消删除监听
            $("#imgCancel").unbind('click').on('click', function () {
                $(this).addClass("hidden");
                $("#imgEdit").removeClass("hidden");
                $(".history-img-content .icheckbox-green").css({"display": "none"});
            });
        }

        /**
         * 展示巡查记录信息
         */
        function showInspectRecords(page, size) {
            Mustache.parse($("#inspectRecordsTpl").html());
            $(".right_panel #record").empty();
            $.ajax({
                url: root + '/project/inspect/record/' + $("#record").attr("data-proid"),
                method: 'post',
                data: {
                    page: page ? page : 1,
                    size: size ? size : parseInt(($(window).height() - 210) / 42)
                },
                success: function (rp) {
                    rp.result.isIllegal = function () {
                        if (this.sfyswf) {
                            return ''
                        }
                        return 'hidden';
                    };
                    rp.result.isSend = function () {
                        if (this.leasProId) {
                            return ''
                        }
                        return 'hidden';
                    };
                    $(".right_panel #record").empty();
                    $(".right_panel #record").append(Mustache.render($("#inspectRecordsTpl").html(), rp.result));
                    $("#addInspectRecord").attr("data-user", rp.userName);
                    if (rp.showBtn == false) {
                        $("#addInspectRecord").addClass("hidden");
                    } else {
                        $("#addInspectRecord").removeClass("hidden");
                    }

                    Laypage({
                        cont: $('#pageCont'),
                        pages: parseInt(rp.result.totalPages),
                        first: false,
                        last: false,
                        curr: function () {
                            return page ? page : 1;
                        }(),
                        jump: function (e, first) { //触发分页后的回调
                            if (!first) { //一定要加此判断，否则初始时会无限刷新
                                showInspectRecords(e.curr, rp.size);
                            }
                        }
                    });
                    inspectRecordListeners(page);
                }
            });
        }

        /**
         * 巡查结果页面监听
         */
        function inspectRecordListeners(recordPage) {
            /**
             * 获取巡查表单模板
             */
            if (recordFormTpl == null) {
                $.ajax({
                    "url": root + "/project/inspect/record/tpl",
                    success: function (tpl) {
                        recordFormTpl = tpl.result;
                    }
                });
            }

            /**
             * 新增巡查记录
             */
            $("#addInspectRecord").on('click', function () {
                var date = new Date();
                var name = $("#addInspectRecord").data('user') + '  ' + locale.format(date, {
                    datePattern: 'yyyy-MM-dd',
                    selector: 'date'
                }) + ' 巡查记录';
                var createAt = locale.format(date, {datePattern: 'yyyy-MM-dd HH:mm:ss', selector: 'date'});
                //功能按钮数组
                var presetNo = undefined;
                $.ajaxSettings.async = false;
                $.getJSON(root + '/project/get/' + $("#record").attr('data-proid'), {}, function (r) {
                    if (r.presets[0] != undefined)
                        presetNo = r.presets[0].presetNo;
                });
                var layerBtns = [
                    ['照片', 'fa fa-file-image-o', $("#record").attr('data-proid')],
                ];

                if ($("#record").attr("data-indexcode")) {
                    layerBtns.push(['视频', 'fa fa-eye', $("#record").attr("data-indexcode")]);
                }
                //开启开关需要推送才会添加推送功能
                if (isNeedSend) {
                    layerBtns.push(['推送', 'fa fa-paper-plane', '']);
                }
                var index = layer.open({
                    title: '新增巡查记录',
                    type: 1,
                    content: Mustache.to_html(recordFormTpl, {
                        id: Math.uuid(),
                        name: name,
                        inspector: $("#addInspectRecord").data('user'),
                        organs: oragnList,
                        createAt: createAt
                    }), //这里content是一个普通的String
                    area: '700px',
                    optbtn: layerBtns,
                    optbtn1: function (index, btn) {
                        ImagesViewerUtil.queryProImages(null, $(btn).data('token'))
                    },
                    optbtn2: function (index, btn) {
                        startupVideo(0, $(btn).data("token"), presetNo);
                    },
                    optbtn3: function (index, btn) {
                        var inspectInfo = SerializeForm.serializeObject($("#inpectForm"));
                        layer.close(index);
                        sendInspectRecord(0, 7, "{}", inspectInfo.id, recordPage);
                    },
                    success: function () {
                        var pzyd = {
                            elem: '#pzyd_first',
                            path: "/laydate",
                            format: 'YYYY-MM-DD',
                            max: '2099-06-16 23:59:59', //最大日期
                            istime: false,
                            istoday: true,
                            choose: function (datas) {
                                //end.min = datas; //开始日选好后，重置结束日的最小日期
                                //end.start = datas; //将结束日的初始值设定为开始日
                            }
                        };
                        $('#pzyd_first').on('click', function () {
                            laydate(pzyd);
                        });

                        var wfyd = {
                            elem: '#wfyd_first',
                            path: "/laydate",
                            format: 'YYYY-MM-DD',
                            max: '2099-06-16 23:59:59', //最大日期
                            istime: false,
                            istoday: true,
                            choose: function (datas) {
                                //end.min = datas; //开始日选好后，重置结束日的最小日期
                                //end.start = datas; //将结束日的初始值设定为开始日
                            }
                        };
                        $('#wfyd_first').on('click', function () {
                            laydate(wfyd);
                        });
                        //未新增巡查记录不显示推送按钮
                        $(".layui-layer-optbtn .btn").each(function () {
                            if ($.trim($(this).text()) == "推送") {
                                $(this).css("display", "none");
                            }
                        });
                        $('#inpectForm').find(":checkbox").iCheck({
                            checkboxClass: 'icheckbox-blue',
                            radioClass: 'iradio-blue'
                        });

                        $("#sfyswf").on('ifChecked', function (event) {
                            $("input[name=sfyswf]").val(true);
                        });

                        $("#sfyswf").on('ifUnchecked', function (event) {
                            $("input[name=sfyswf]").val(false);
                        });

                        save("生成记录成功！", 'save', function (record) {
                            //记录保存成功后添加推送按钮
                            //如果是疑似违法，按钮灰掉
                            $(".layui-layer-optbtn .btn").each(function () {
                                if ($.trim($(this).text()) == "推送" && (record.sfyswf == true || record.sfyswf == 'true')) {
                                    //$(this).removeAttr("disabled");
                                    $(this).css("display", "inline-block");
                                } else if ($.trim($(this).text()) == "推送" && record.sfyswf == '') {
                                    $(this).css("display", "none");
                                    //$(this).attr({"disabled":"disabled"});
                                }
                            });
                        });
                    }
                });
            });

            /**
             * 查看巡查记录
             */
            $("#recordList li .record-name").on('click', function () {
                $.ajax({
                    url: root + '/project/inspect/record/get/' + $(this).data('id'),
                    method: 'post',
                    success: function (rp) {
                        if (rp.result.sfyswf != 'false') {
                            rp.result.isChecked = "checked";
                        }

                        HbarsUtils.regHelper('selected', function (param, itemName) {
                            if (param == itemName)
                                return "selected"
                            return "";
                        });
                        var formContent = HbarsUtils.compile(recordFormTpl);
                        //功能按钮数组
                        var layerBtns = [
                            ['照片', 'fa fa-file-image-o', $("#record").attr('data-proid')]
                        ];
                        if ($("#record").attr("data-indexcode")) {
                            layerBtns.push(['视频', 'fa fa-eye', $("#record").attr("data-indexcode")]);
                        }
                        var index = layer.open({
                            title: '查看巡查记录',
                            type: 1,
                            content: formContent(rp.result), //这里content是一个普通的String
                            area: '700px',
                            optbtn: layerBtns,
                            optbtn1: function (index, btn) {
                                ImagesViewerUtil.queryProImages(null, $(btn).data('token'))
                            },
                            optbtn2: function (index, btn) {
                                startupVideo(0, $(btn).data("token"));
                                //发送日志信息

                            },
                            success: function () {
                                $('#inpectForm').find(":checkbox").iCheck({
                                    checkboxClass: 'icheckbox-blue',
                                    radioClass: 'iradio-blue'
                                });

                                //如果是疑似违法或者已推送的，按钮灰掉
                                if (rp.result.leasProId != undefined || rp.result.sfyswf == 'false') {
                                    $(".layui-layer-optbtn .btn").each(function () {
                                        if ($.trim($(this).text()) == "推送") {
                                            $(this).attr({"disabled": "disabled"});
                                        }
                                    });
                                }

                                $("#sfyswf").on('ifChecked', function (event) {
                                    $("input[name=sfyswf]").val(true);
                                });

                                $("#sfyswf").on('ifUnchecked', function (event) {
                                    $("input[name=sfyswf]").val(false);
                                });

                                save("编辑记录成功！", "edit", function () {
                                    layer.close(index);
                                }, recordPage);
                            }
                        });
                    }
                });
            });

            /**
             * 删除巡查记录
             */
            $("#recordList li .record-delete").on('click', function () {
                var id = $(this).data('id');
                layer.confirm('确定要删除该记录吗？', {
                    btn: ['是', '否'] //按钮
                }, function () {
                    $.ajax({
                        url: root + '/project/inspect/record/del/' + id,
                        method: 'post',
                        success: function (rp) {
                            if (rp.result == true) {
                                $('#' + id).remove();
                                $("#addInspectRecord").removeClass("hidden");
                                layer.msg("删除成功！", {time: 1500})
                            }
                        }
                    });
                });
            });

            /**
             * 保存巡查记录
             * @param msg
             * @param type
             * @param callback
             */
            function save(msg, type, callback, recordPage) {
                $("#inpectForm").Validform({
                    tiptype: 3,
                    label: ".label",
                    showAllError: true,
                    ajaxPost: true,
                    callback: function (data) {
                        $("#addInspectRecord").attr("data-user");
                        var data = JSON.stringify(SerializeForm.serializeObject($("#inpectForm")));
                        $.ajax({
                            url: root + '/project/inspect/record/save',
                            method: "post",
                            data: {data: data, proId: $("#record").attr("data-proid"), type: type},
                            success: function (rp) {
                                if (rp.id) {
                                    $("#inpectForm").find("input[name='id']").val(rp.id);
                                    callback(rp);
                                    layer.msg(msg, {
                                        time: 2000
                                    }, function () {
                                        if (type != undefined) {
                                            showInspectRecords(recordPage);
                                        }
                                    });
                                    layer.closeAll();
                                    layer.msg("保存成功");
                                } else {
                                    layer.msg(rp.message);
                                }
                            }
                        });
                        return false;
                    }
                });
            }
        }



        /***
         * 显示分页工具(监控点/项目列表)
         * @param selector
         * @param pageCount
         * @param currPage
         * @param callback
         */
        function showPageTool(selector, pageCount, currPage, callback) {
            if (typeof selector === "string")
                selector = $("#" + selector);
            selector.show();
            Laypage({
                cont: selector,
                pages: pageCount,
                groups: 0,
                first: false,
                last: false,
                // groups: 3,
                // first: 1,
                // last: pageCount,
                curr: currPage + 1,
                // skip: true,
                // skin: 'molv',
                // prev: false,
                // next: false,
                jump: function (obj, first) {
                    if (!first) {
                        //点击跳页触发函数自身，并传递当前页：obj.curr注意带条件查询的分页
                        callback(obj.curr - 1, searchCondition);
                    }
                }
            });
        }

        /**
         * 新增预设信息
         * @param proId
         * @param indexCode
         */
        function addNewPreset(pro, indexCode) {
            $.ajax({
                url: root + '/video/preset/insert',
                method: 'post',
                data: {
                    id: pro.id,
                    proId: pro.proId,
                    indexCode: indexCode
                },
                success: function (rp) {
                    if (rp.result !== undefined) {
                        pro.presets.push(rp.result);
                        var cameras = arrayUtil.filter(pro.cameras, function (camera) {
                            return camera.indexCode === indexCode;
                        });
                        cameras[0].presets.push(rp.result);
                        cameras[0].hasPresets = true;
                        //删除2.0版本里存储项目里存储cameraId信息（单一关系），2.1版本后统一进入关系表维护
                        if (pro.cameraId) {
                            var project = {
                                id: pro.id,
                                cameraId: ''
                            };
                            $.ajax({
                                type: 'post',
                                url: root + '/project/save?ownerId=' + loginUser.id,
                                dataType: 'json',
                                data: $.param(project).toString(),
                                success: function (rp) {
                                    pro.cameraId = '';
                                }
                            });
                        }
                        reloadPresetList(pro, indexCode);
                    }
                }
            });
        }

        /**
         * 删除预设位
         * @param pro
         * @param presetId
         */
        function deletePreset(pro, presetId) {
            $.ajax({
                url: root + '/video/preset/delete',
                method: 'post',
                data: {
                    presetId: presetId,
                    proId: pro.proId
                },
                success: function (rp) {
                    var deletePreset = null;
                    if (rp.result == true) {
                        $.each(pro.presets, function (i, n) {
                            if (n.id == presetId) {
                                pro.presets = pro.presets.slice(0, i).concat(pro.presets.slice(i + 1, pro.presets.length)); //删除这个数组元素
                                deletePreset = n;
                                return false;
                            }
                        });
                        var cameras = arrayUtil.filter(pro.cameras, function (camera) {
                            return camera.indexCode === deletePreset.indexCode;
                        });

                        $.each(cameras[0].presets, function (i, n) {
                            if (n.id == presetId) {
                                cameras[0].presets = cameras[0].presets.slice(0, i).concat(cameras[0].presets.slice(i + 1, cameras[0].presets.length)); //删除这个数组元素
                                return false;
                            }
                        });

                        if (cameras[0].presets.length == 0) cameras[0].hasPresets = false;

                        reloadPresetList(pro, deletePreset.indexCode);
                    }
                }
            });
        }

        /**
         * 设置预设位有效性
         * @param data
         */
        function setEnabledPreset(data, presets, callback) {
            $.ajax({
                url: root + '/video/preset/enabled',
                method: 'post',
                data: data,
                success: function (rp) {
                    if (rp.result) {
                        if (data.enabled) {
                            layer.msg('预设位设置为可用');
                            callback(true);
                        } else {
                            layer.msg('预设位设置为不可用');
                            callback(false);
                        }
                        $.each(presets, function (i, n) {
                            if (n.id == data.id) {
                                n.enabled = data.enabled;
                                return false;
                            }
                        });
                    } else {
                        layer.msg("操作出现异常！");
                    }

                }
            });
        }


        var autoCompleteProNames = [];
        var relationInfos = [];
        var layerSize;

        /**
         * 获取关联地块信息 进行自动生成项目名称，生成关联地块等
         */
        function getRelationInfo() {
            autoCompleteProNames = [];
            relationInfos = [];
            var pnt = newProjectPnt;
            layerSize = newProjectLayer.length;
            $.each(newProjectLayer, function (index, item) {
                var identifyTask = new IdentifyTask(item.layer.url);
                var identifyParams = new IdentifyParameters();
                identifyParams.tolerance = 3;
                identifyParams.returnGeometry = true;
                identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_ALL;
                identifyParams.width = __map.width;
                identifyParams.height = __map.height;
                identifyPos = pnt;
                identifyParams.geometry = pnt;
                identifyParams.mapExtent = __map.extent;
                identifyTask.execute(identifyParams, lang.hitch(this, generateProjNameAndInfo, item));
            });
        }

        /**
         * 生成推荐的项目名称及关联信息
         * @param token
         * @param result
         */
        function generateProjNameAndInfo(token, result) {
            layerSize--;
            if (result != null && result.length > 0) {
                for (var idx in result) {
                    if (typeof result[idx].feature !== "undefined") {
                        var attrs = result[idx].feature.attributes;  //识别得到的某个图层的图形属性信息
                        if (attrs[token.fields.projNameFields] !== undefined) {
                            autoCompleteProNames.push(attrs[token.fields.projNameFields]);
                        }
                        if (attrs[token.fields.relationField] !== undefined) {
                            relationInfos.push(attrs[token.fields.relationField]);
                        }
                    }
                }

            }
            if (layerSize === 0) {
                if (autoCompleteProNames.length > 0) {
                    var options = {
                        data: autoCompleteProNames,
                        list: {
                            maxNumberOfElements: 3,
                            showAnimation: {
                                type: "slide",
                                time: 200
                            },

                            hideAnimation: {
                                type: "slide",
                                time: 200
                            }
                        }
                    };
                    $("#projectNameInput").easyAutocomplete(options);
                    $("#projectNameInput").val(autoCompleteProNames[0]);
                }

                if (VmOptions.relateToPlotInfos && relationInfos.length > 0) {
                    var opts = {
                        data: relationInfos,
                        list: {
                            maxNumberOfElements: 3,
                            showAnimation: {
                                type: "slide",
                                time: 200
                            },

                            hideAnimation: {
                                type: "slide",
                                time: 200
                            }
                        }
                    };
                    $("#realationInfoInput").easyAutocomplete(opts);
                    $("#realationInfoInput").val(relationInfos[0]);
                }
            }
        }

        /**
         * 进行属性识别
         * @param event
         */
        function doIdentify(event) {
            _identifyLayers = __config.identifyLayers;
            _identifyCount = _identifyLayers.length;
            layer.closeAll();
            layer.msg('属性识别中..', {time: 5000});
            _identifyResults = [];
            _listData = [];
            _resultObj = [];
            __map.graphics.clear();
            __map.infoWindow.hide();

            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_ALL;
            identifyParams.width = __map.width;
            identifyParams.height = __map.height;
            identifyPos = event.mapPoint;
            identifyParams.geometry = event.mapPoint;
            identifyParams.mapExtent = __map.extent;

            for (var i in _identifyLayers) {
                var tmp = _identifyLayers[i];
                if (tmp.url == undefined || tmp.url == '') {
                    layer.msg('图形服务地址未配置', {time: 3000});
                    return;
                }
                var identifyTask = new IdentifyTask(tmp.url);
                identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult, tmp));
            }
        }

        /**
         * 识别后处理
         * @param token
         * @param result
         * @private
         */
        function _handleIdentifyResult(token, result) {
            if ((result != null && result.length > 0) && _identifyCount > 0) {
                if (_identifyResults.length > 0)
                    _identifyResults = [];
                _identifyResults = _identifyResults.concat(result);
                arrayUtil.forEach(_identifyResults, function (item) {
                    if (token.layerName === item.layerName) {    //过滤图层
                        var tmp = {};
                        tmp.layerName = item.layerName;
                        tmp.layerId = item.layerId;
                        tmp.feature = item.feature;
                        var attr = item.feature.attributes;
                        tmp.objectid = attr.OBJECTID;
                        var rf = token.returnFields;
                        var _data = [];
                        for (var j in rf) {
                            var f = rf[j];
                            _data.push({key: f.alias || f.name, value: attr[f.name] || attr[f.alias]});
                        }
                        tmp.data = _data;
                        if (item.layerName in _resultObj) {
                            _resultObj[item.layerName].push(tmp);
                        } else {
                            _resultObj[item.layerName] = [];
                            _resultObj[item.layerName].push(tmp);
                        }
                        tmp = null;
                        _data = null;
                    }
                });
            }
            _identifyCount -= 1;
            if (_identifyCount == 0) {
                layer.closeAll();
                if (!isEmpty(_resultObj)) {
                    var identifyData = [];
                    for (var name in _resultObj) {
                        var data = {};
                        data.data = _resultObj[name][0].data;
                        data.title = _resultObj[name][0].layerName;
                        data.content = JSON.stringify(_resultObj[name][0].data);
                        identifyData.push(data);
                    }
                    var identifyResult = {};
                    identifyResult.list = identifyData;
                    var tpl = $('#identifyTpl').html();
                    clearHighlight();
                    mapInfoWindow.showDetail(Mustache.render(tpl, identifyResult), identifyPos, '属性查看').then(function (pos) {
                        var screenPnt = __map.toScreen(pos);
                        //y偏移
                        var deltaHeight = $("#main-map").height() - $(".esriPopupWrapper").height();
                        if (screenPnt.y < deltaHeight && Environment.isIE()) {
                            screenPnt = screenPnt.offset(0, deltaHeight - screenPnt.y + 60);
                        }
                        //x偏移
                        var deltaWidth = $("#main-map").width() - $(".esriPopupWrapper").width();
                        if (screenPnt.x > deltaWidth && Environment.isIE()) {
                            screenPnt = screenPnt.offset(deltaWidth - screenPnt.x, 0);
                        }
                        var nPnt = __map.toMap(screenPnt);
                        var extent = __map.extent.offset(pos.x - nPnt.x, pos.y - nPnt.y);
                        __map.setExtent(extent);
                    });
                    _identifyLayers = [];
                    $('.identify-title').on("click", function () {
                        $('.identify-title').removeClass('selected');
                        $(this).addClass("selected");
                        var content = {};
                        content.data = $(this).data('content');
                        var contentTpl = $('#identifyContentTpl').html();
                        $('#tabContent').html(Mustache.render(contentTpl, content));
                        if ($("#identifyTab").height() > $("#tabContent").height()) {
                            $("#identifyTab").css('border-right', '1px solid #408FC6');
                            $("#tabContent").css('border-left', '');
                        } else {
                            $("#identifyTab").css('border-right', '');
                            $("#tabContent").css('border-left', '1px solid #408FC6');
                        }
                    });
                    $('.identify-title:first').trigger('click');
                    clearIdentify();
                } else {
                    clearIdentify();
                    layer.msg('未查找到信息！', {time: 1000});
                }

            }
        }


        /**
         * 自动创建项目
         * @param gra
         * @param indexCode
         */
        function autoCreateProject(gra, indexCode) {
            var geojson = JsonConverters.toGeoJson(gra);
            var tpl = $("#autoProFormTpl").html();
            if (addProHandler) layer.close(addProHandler);
            var addPro = layer.open({
                type: 1,
                title: ['<i class="fa fa-plus"></i>&nbsp;新建项目', 'background-color:#428bca;color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
                area: '620px',
                shade: 0,
                content: Mustache.render(tpl, {location: JSON.stringify(geojson), indexCode: indexCode}),
                btn: [],
                success: function () {
                    if (selectCamera) layer.close(selectCamera);
                }
            });
            $('#proTypeSelect').append(proTypeSelectTpl);
            $("#autoProForm").Validform({
                tiptype: function (msg, o, cssctl) {
                    if (!o.obj.is("form")) {
                        var objtip = o.obj.next().find(".Validform_checktip");
                        cssctl(objtip, o.type);
                        objtip.text(msg);

                        var infoObj = o.obj.next();
                        if (o.type == 2) {
                            infoObj.fadeOut(200);
                        } else {
                            if (infoObj.is(":visible")) {
                                return;
                            }
                            var left = o.obj.get(0).offsetLeft + 15,
                                top = o.obj.get(0).offsetTop + o.obj.get(0).offsetHeight + 6;

                            infoObj.css({
                                left: left,
                                top: top
                            }).show().animate({}, 200);
                        }
                    }
                },
                callback: function (data) {
                    gra.symbol = getProSymbol(VmOptions.proType);
                    graphicsLayer.add(gra);
                    proGraphics.push(gra);
                    $.ajax({
                        type: 'post',
                        url: '/omp/project/save?ownerId=' + loginUser.id + "&autoCreate=autoCreate ",
                        data: data.serialize(),
                        success: function (_r, _s) {
                            if (_r.hasOwnProperty('success')) return;
                            //保存成功之后 需要刷新项目列表以及视频监控列表
                            layer.msg('保存成功', {
                                time: 1000, end: function () {
                                    layer.close(addPro);
                                    refreshProPage(0, null);
                                    // refreshVideos();
                                }
                            });
                        },
                        error: function (_ex) {
                            console.error(_ex.responseText);
                        }
                    });
                    return false;
                },
                btnSubmit: "#autoProSaveBtn"
            });
        }

        /**
         * 判断对象是否为空
         * @param obj
         * @returns {boolean}
         */
        function isEmpty(obj) {
            for (var name in obj) {
                if (obj.hasOwnProperty(name)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 清除状态
         * @private
         */
        function clearIdentify() {
            if (_mapClickHanlder) _mapClickHanlder.remove();
            __map.graphics.clear();
            __map.setMapCursor('default');
            layer.closeAll();
        }

        /**
         * 重新加载预设位列表
         * @param pro
         */
        function reloadPresetList(pro, indexCode) {
            console.log(pro);
            var collapsePanel = $("#" + indexCode);
            collapsePanel.empty();
            var cameras = arrayUtil.filter(pro.cameras, function (camera) {
                return camera.indexCode === indexCode;
            });
            if (cameras[0].presets.length > 0) {
                //预设位信息存在则渲染预设位列表展示
                var presetTpl = $("#presetListTpl").html();
                HbarsUtils.regHelper('if', function (conditional, options) {
                    return conditional ? options.fn(this) : options.inverse(this);
                });
                collapsePanel.replaceWith(HbarsUtils.renderTpl(presetTpl, cameras[0]));

                $("#" + indexCode).addClass("in");
                $(".disabledPreset").iCheck({
                    checkboxClass: 'icheckbox-blue',
                    radioClass: 'iradio-blue'
                });
                $(".icheckbox-blue").css({'display': 'inline-block', 'top': '4px'});
                if (!isAdmin) {
                    $(".disabledPreset").parent().addClass("hidden");
                    $(".disabledPreset").parent().siblings("label").addClass("hidden");
                }
                //添加监听事件 查看 设置 删除
                $(".preset-view").off('click').on('click', function () {
                    startupVideo(0, indexCode, $(this).data("pno"));
                });
                $(".preset-set").off('click').on('click', function () {
                    var pNo = $(this).data("pno");
                    $.getJSON(root + "/video/preset/isCapture", function (rp) {
                        if (rp.result) {
                            layer.msg('预设位正在拍照，暂时不能操作！', {icon: 5});
                        } else {
                            startupVideo(1, indexCode, pNo);
                        }
                    })
                });

                $(".preset-del").off('click').on('click', function () {
                    deletePreset(pro, $(this).data("id"));
                });

                //预设位失效监听
                $('.disabledPreset').off('ifChecked').on('ifChecked', function (event) {
                    setEnabledPreset({
                        enabled: false,
                        id: $(event.currentTarget).data('id')
                    }, pro.presets, function (flag) {
                        $(event.currentTarget).parent().siblings(".preset-set, .preset-view").addClass("hidden");
                    });
                });
                $('.disabledPreset').off('ifUnchecked').on('ifUnchecked', function (event) {
                    setEnabledPreset({
                        enabled: true,
                        id: $(event.currentTarget).data('id')
                    }, pro.presets, function (flag) {
                        $(event.currentTarget).parent().siblings(".preset-set, .preset-view").removeClass("hidden");
                    });
                });
            } else {
                //没有预设位信息的恢复新增的界面
                var tpl = '<div class="preset-new-panel"><strong>没有预置位! </strong>' +
                    '<a class="btn btn-sm btn-primary preset-add" name="addPresetBtn"' +
                    'data-indexcode="' + indexCode + '">新增</a>' +
                    '<a class="btn btn-link" style="padding: 0;margin: 0;" name="previewBtn">查看视频</a></div>';
                Mustache.parse(tpl);
                collapsePanel.append(Mustache.render(tpl));
                $("a[name=addPresetBtn]").on('click', function () {
                    addNewPreset(pro, $(this).data('indexcode'));
                });
            }
        }


        /**
         * 获取单个摄像头可视域信息
         * @param priorityCamera
         * @param isSingle  查看单个探头
         */
        function getSingleViewScope(priorityCamera, isSingle) {
            if (!priorityCamera)
                return;
            var indexCode = priorityCamera.indexCode;
            $.each(viewGraphics, function (j, n) {
                if(this||this["attributes"]||this["attributes"]["indexCode"]){
                    if(this["attributes"]["indexCode"]==indexCode){
                        graphicsLayer.remove(this);
                    }
                }
            });
            $.ajax({
                url: root + "/video/camera/view/scope?ieRefresh=" + Math.random(),
                data: {indexCode: indexCode, platform: priorityCamera.platform},
                async: false,
                success: function (r) {
                    var videoData = r[0];
                    if (videoData != undefined && videoData.viewRadius != null && videoData.viewRadius > 0) {
                        if(videoData.viewRadius<10){
                            videoData.viewRadius=10;
                        }
                        var center = new Point(priorityCamera.x, priorityCamera.y, __map.spatialReference);
                        var base = priorityCamera.platform ? (priorityCamera.platform === "hk" ? (360 - videoData.azimuth) : videoData.azimuth) : (platform === "hk" ? (360 - videoData.azimuth) : videoData.azimuth);
                        //测试代码开始
                        if (0<base&&base<180){
                            base = base+180;
                        }else if(base>180&&base<360){
                            base = base-180;
                        }
                        //测试代码结束
                        var start = base - videoData.horizontalAngle / 2;  //起始角
                        var end = base + videoData.horizontalAngle / 2;    //结束角
                        var s = new Sector(center, {
                            radius: (videoData.viewRadius) * 10,
                            startAngle: start,
                            endAngle: end
                        });
                        var t = new Graphic(s.getPolygon(), Environment.isIE() ? sectorFsForIE : sectorFs, {indexCode: videoData.indexCode});
                        if (VmOptions.showArcCenter) {
                            var polyline = new Polyline(s.getArcCenter());
                            var l = new Graphic(polyline, centerLineFs, {indexCode: videoData.indexCode});
                            viewGraphics.push(l);
                            if (scopeFlag == 1 || isSingle) graphicsLayer.add(l);
                        }
                        viewGraphics.push(t);
                        if (scopeFlag == 1 || isSingle) graphicsLayer.add(t);
                    } else {
                        console.log("刷新可视域失败！");
                    }
                }
            });
        }

        /**
         * 查看预警图片
         * @param indexCode
         * @param eventTime
         * @param name
         */
        function preWarning(indexCode, eventTime, name) {
            layer.open(
                {
                    title: name + " —— 预警信息",
                    type: 1,
                    area: '800px',
                    shade: 0,
                    content: "<div style='width:800px;height:400px;'><img width='100%' height='100%'  src='" + __config.preWarningUrl + "/qly/detect/snap?camera_id=" + indexCode + "&event_time=" + eventTime + "'></img></div>",
                    success: function () {
                    }
                });
        }

        me.getInstance = function () {
            if (instance == undefined || instance == null) {
                instance = new me();
            }
            return instance;
        };

        return me;
    });