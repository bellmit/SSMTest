/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:35
 * File:    WidgetsManager
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/topic",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "./MapManager",
    "./LayoutManager",
    "map/component/MapInfoWindow",
    "map/component/MapPopup",
    "map/core/MiniMap",
    "mustache",
    "layer",
    "dojox/uuid/generateRandomUuid",
    "dojo/domReady!"], function (declare, topic, lang, arrayUtil, Deferred, MapManager, LayoutManager, MapInfoWindow,
        MapPopup, MiniMap, Mustache, layer, RandomUuid) {

        var contrastMapDiv, contrasted = false;
        var contrastNum = 0;

        var instance, me = declare(null, {
            __id: Math.random(),
            /**
             *
             */
            widgetsContext: undefined,
            /**
             *
             * @param config
             * @param context
             */
            initWidgets: function (config, context) {
                widgetsContext = context;
                _initWidgets(config);
                layer.config();
            },
            /**
             * get widgets context
             *
             * @returns {*}
             */
            getWidgetsContext: function () {
                return widgetsContext;
            },
            /**
             * switch widget
             *
             * @param widgetId
             */
            switchWidget: function (widgetId) {
                _switchWidget(widgetId);
            },
            /**
             * 加载地图功能widget(菜单项)
             * @param widget
             */
            loadFuncWidget: function (widget) {
                _loadFuncWidget(widget);
            },

            /**
             * 加载功能组
             * @param group
             */
            loadWidgetsGroup: function (group) {
                _loadWidgetsGroup(group);
            }
        });

        var widgets = [];
        var mapManager = MapManager.getInstance(), layoutManager = LayoutManager.getInstance(),
            mapInfoWindow = MapInfoWindow.getInstance();

        var mapPopup = MapPopup.getInstance();


        var freeWidgetTpl = '<div id="{{id}}" class="b-widget usel"></div>';

        var singleWidgetTpl = '<a class="optbtn" id="btn_{{id}}" toggle-ac="{{id}}"  title="{{label}}" >' +
            '<span class="widget-title">{{abbr}}</span></a>';

        var widgetGroupTpl = '<a class="groupbtn default-icon {{url}}-icon" title="{{label}}"  style="background-position-x:center;">' +
            '<span class="widget-title">{{abbr}}</span>' +
            '<ul class="list-group widget-group-content">' +
            '{{#widgets}}<li class="group-widget-btn list-group-item" id="btn_{{wid}}" toggle-ac="{{wid}}" title="{{label}}">{{label}}</li>{{/widgets}}</ul></a>';

        var dockWidgetTpl = '<a class="optbtn" id="btn_{{id}}" toggle-ac="{{id}}" title="{{desc}}">' +
            '<span class="widget-title">{{label}}</span></a>';

        var dockWidgets = [{ display: true, url: "LayerList" }, {
            display: true,
            url: "LayerList",
            config: {
                "hideInvisibleLayer":true
            }
        }];

        var urlParams;

        /**
         * 初始化 widgets
         *
         * @param config
         * @private
         */
        function _initWidgets(config) {
           
            urlParams = getUrlParams();
            log('初始化模块...');
            _initFreeWidgets(config.widgets);
            _initDockWidgets(config.hasOwnProperty("dockWidgets") ? config.dockWidgets : dockWidgets);
            _loadSearch();//加载检索功能
            _initInfoWindow();
            _addContrastMap();//加载对比地图
        }


        /**
         * 加载检索功能
         * @private
         */
        function _loadSearch() {
            require(["js/map/widgets/Search/Index", "css!js/map/widgets/Search/Style"], function (Main) {
                if (Main instanceof Function) {
                    Main.getInstance();
                }
            });
        }


        /**
         * 初始化 free widgets
         *
         * @param widgets
         * @private
         */
       
        function _initFreeWidgets(widgets) {
            loadWidgets(widgets, function (main, html) {
                $(Mustache.render(freeWidgetTpl, main)).appendTo($('#' + instance.getWidgetsContext())).append(html);
            }, function (widget) {
                try {
                    if (widget) {
                        widget.onCreate();
                        widget.created = true;
                    }
                } catch (e) {
                    widgetToLoaded--;
                    error('widget [' + widget.url + '] created error [' + e.message + ']');
                }
            });
        }

        /**
         * 初始化 左侧固定widget ---数据、视频
         *
         * @param widgets
         * @private
         */
        function _initDockWidgets(widgets) {
            var $operaArea = $('#verticalMenu');
            var $operaPop = $('#popupwin_area');
            loadWidgets(widgets, function (main, html) {
                var wid = main.id;
                $operaArea.append(Mustache.render(dockWidgetTpl, main));
                if (wid == 'Statistics') {
                    $operaPop.append('<div class="popup-Statistics-win" id="panel-' + wid + '"  style="display:none">' + html + '</div>');
                } else {
                    $operaPop.append('<div class="popup-window" id="panel-' + wid + '"  style="display:none"><div class="popup-close-btn" title="收起"><span class="fa fa-bars"></span></div><div id="' + wid + '">' + html + '</div></div>');
                }

                if (validateImage(root + '/static/css/theme/light-blue/img/widget-icons/' + main.url.split("\/").reverse()[0].toLocaleLowerCase() + '.png')) {
                    $('#btn_' + wid).css('background', 'url(' + root + '/static/css/theme/light-blue/img/widget-icons/' + main.url.split("\/").reverse()[0].toLocaleLowerCase() + '_hover.png) no-repeat top');
                } else {
                    $('#btn_' + wid).css('background', 'url(' + root + '/static/css/theme/light-blue/img/widget-icons/default.png) no-repeat top');
                }

                $('#btn_' + wid).on('click', function (event) {
                    event.stopPropagation();
                    _openWidget($(this));
                });
                $('#btn_' + wid).on('mouseout', function () {
                    _toggleBtnBg($(this), false);
                });
                $('#btn_' + wid).on('mouseover', function () {
                    _toggleBtnBg($(this), true);
                });
            }, function (widget) {
            });
        }

        var widgetToLoaded = 0, widgetLoaded = 0;

        /**
         * 验证背景图片是否存在
         * @param url
         * @returns {boolean}
         */
        function validateImage(url) {
            var xmlHttp;
            try {
                if (window.ActiveXObject) {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                else if (window.XMLHttpRequest) {
                    xmlHttp = new XMLHttpRequest();
                }
                xmlHttp.open("Get", url, false);
                xmlHttp.send();
            } catch (e) {
                return false;
            }
            if (xmlHttp.status == 404)
                return false;
            else
                return true;
        }

        /**
         * 加载widgets
         *
         * @param widgets
         */
        function loadWidgets(widgets, callback, afterCreated) {
            arrayUtil.forEach(widgets, function (value) {
                try {
                    //使用open判断以兼容旧系统地图模板
                    if (value.display || value.open) {
                        loadWidget(value, callback, afterCreated);
                    }
                } catch (e) {
                    error("模块 [" + value.url + "] 加载异常 [" + e + "]");
                }
            })
        }

        /**
         * 加载widget
         * widget的url需要注意:
         * 1、直接传id，类似 Navigation 则程序会默认加上前缀 map/widgets/
         * 2、完全定位到某个widget 则需要写完整地址 如  map/widgets/Navigation
         * @param widget
         */
        function loadWidget(widget, callback, afterCreated) {
            // console.log('widget.url3------', widget.url)
            var url = widget.url;
            var urlArr = url.split("/");
            url = "map/widgets/" + url;
            var widgetId = urlArr.reverse()[0];
            //在加载定位功能时 需判断url地址中是否传入了action=location
            if (widgetId == "Location") {
                if (!(urlParams.hasOwnProperty("action") && urlParams.action.equalsIgnoreCase("Location"))) {
                    return;
                }
            }
            //在加载地图对比功能时 需判断url地址中是否传入了action=compare
            if (widgetId == "Compare") {
                if (!(urlParams.hasOwnProperty("action") && urlParams.action.equalsIgnoreCase("Compare"))) {
                    return;
                }
            }
            widgetToLoaded++;
            // log('加载模块 [' + widgetId + '] ');
            try {
                require([url + "/Index", "text!" + url + "/Index.html", "css!" + url + "/Style"], function (Main, html) {
                    widgetLoaded++;
                    if (!(Main instanceof Function)) {
                        log("widget [" + widget.url + "] must return BaseWidget");
                        return;
                    }
                    var main = new Main({
                        id: widgetId,
                        label: widget.label,
                        desc: widget.desc,
                        icon: widget.icon,
                        url: widget.url.indexOf("/") > -1 ? widget.url : "map/widgets/" + widget.url
                    });
                    if (callback) callback(main, html);
                    try {
                        main.setMap(mapManager.getCurrentMap()); //此处set的map对象是MainMap
                    } catch (e) {

                    }
                    main.setConfig(widget.config || {});
                    widgets.push(main);
                    if (afterCreated) afterCreated(main);
                    log('模块 [' + widgetId + '] 加载完成 ');
                    if (widgetToLoaded == widgetLoaded) allWidgetsLoaded();
                });
            } catch (e) {
                widgetToLoaded--;
                error("模块 [" + widgetId + "] 加载异常 [" + e.message + "]");
            } finally {
                if (widgetToLoaded == widgetLoaded) allWidgetsLoaded();
            }
        }

        /**
         * 加载地图配置功能widget
         * 点击菜单项 功能直接加载在左侧 (后期需要完善)
         * @param widget
         * @param inGroup 
         */
     
        function _loadFuncWidget(widget, inGroup) {
            console.log('widget------', widget);
            // console.log('widget.url2------', widget.url)
            if (!widget) return;
            var url = widget.url;
            var urlArr = url.split("/");
            var label = widget.label;
            url = "map/widgets/" + url;
            var widgetId = urlArr.reverse()[0];
            if (_isCreated(widgetId)) {
                if (_isDuplicated(widget)) {
                    console.info("不支持加载多个同类型的功能模块!");
                    return;
                }
                if ($(".main-operation").css('display') == "none") {
                    if (!(urlParams.hasOwnProperty("hideLeftPanel") && urlParams["hideLeftPanel"] != "true")) {
                        $(".main-operation-handle").trigger('click');
                    }
                }
                layoutManager.openWidget(widgetId);
                _switchWidget(widgetId);
            }
            else {
                try {
                    log('加载模块 [' + label + '] ');
                    require([url + "/Index", "text!" + url + "/Index.html", "css!" + url + "/Style"], function (Main, html) {
                        if (!(Main instanceof Function)) {
                            log("widget [" + widget.url + "] must return BaseWidget");
                            return;
                        }
                        var main = new Main({
                            id: widgetId,
                            label: widget.label,
                            icon: widget.icon,
                            url: widget.url.indexOf("/") > -1 ? widget.url : "map/widgets/" + widget.url
                        });
                        //加载到左侧
                        var wid = main.id;
                        if (!inGroup) {
                            var attr = lang.clone(main);

                            if (typeof showAllLabel != "undefined") {
                                lang.mixin(attr, { abbr: attr.label });
                            } else {
                                lang.mixin(attr, { abbr: attr.label.substr(0, 2) });
                            }
                            $("#verticalMenu").append(Mustache.render(singleWidgetTpl, attr));
                        }
                        if (wid == 'Statistics' && html) {
                            $('#popupwin_area').append('<div class="popup-Statistics-win" id="panel-' + wid + '"  style="display:none">' + html + '</div>');
                        } else {
                            if (html) {
                                $('#popupwin_area').append('<div class="popup-window" id="panel-' + wid + '"  style="display:none"><div class="popup-close-btn" title="收起"><span class="fa fa-bars"></span></div><div id="' + wid + '">' + html + '</div></div>');
                            }
                        }


                        if (validateImage(root + '/static/css/theme/light-blue/img/widget-icons/' + widget.url.toLocaleLowerCase() + '.png')) {
                            $('#btn_' + wid).css('background', 'url(' + root + '/static/css/theme/light-blue/img/widget-icons/' + widget.url.toLocaleLowerCase() + '.png) no-repeat top');
                        } else {
                            if (!inGroup)
                                $('#btn_' + wid).css('background', 'url(' + root + '/static/css/theme/light-blue/img/widget-icons/default.png) no-repeat top');
                        }

                        $('#btn_' + wid).on('click', function () {
                            // if (wid == 'Statistics') {
                            //     $('.popup-window').hide();
                            //     $(this).show();
                            // } else {
                            //     $('.popup-Statistics-win').hide();
                            _openWidget($(this));
                            //}

                        });
                        $('#btn_' + wid).on('mouseover', function () {
                            _toggleBtnBg($(this), true);
                        });
                        $('#btn_' + wid).on('mouseout', function () {
                            _toggleBtnBg($(this), false);
                        });
                        //此处是MainMap
                        main.setMap(mapManager.getCurrentMap());
                        main.setConfig(widget.config || {});
                        widgets.push(main);
                        // $('#btn_' + wid).trigger('click');
                        log('模块 [' + label + '] 加载成功');
                    });
                } catch (e) {
                    error("模块 [" + label + "] 加载异常 [" + e.message + "]");
                }
            }
        }

        // var _groupWidgets
        /**
         * 加载左侧功能组，仅加载图标，不加载实际功能
         * @param group
         * @private
         */
        function _loadWidgetsGroup(group) {

            setTimeout(function () {
                arrayUtil.forEach(group.widgets, function (widget) {
                    var urlArr = widget.url.split("/");
                    lang.mixin(widget, { wid: urlArr.reverse()[0] });
                });

                $("#verticalMenu").append(Mustache.render(widgetGroupTpl, lang.mixin(group, { abbr: group.label.substr(0, 2) })));

                $(".groupbtn").on("mouseover", function () {
                    $(this).find("ul").show();
                });

                $(".groupbtn").on("mouseout", function () {
                    $(this).find("ul").hide();
                });

                // widgets = widgets.concat(group.widgets);
                //   $(".group-widget-btn").on('click', function () {
                //
                //   });
                arrayUtil.forEach(group.widgets, function (widget) {
                    _loadFuncWidget(widget, true);
                });
            }, 500);
        }

        /**
         * 打开/切换左侧 widget
         * @param $trigger
         * @returns {boolean}
         * @private
         */
        function _openWidget($trigger) {
            if ($trigger.hasClass('optBtnActive')) return false;
            $(".main-operation-handle").trigger('click');
            var id = $trigger.attr("toggle-ac");
            layoutManager.openWidget(id);
            _switchWidget(id);
            _toggleBtnBg($trigger, true);
            arrayUtil.forEach($(".optbtn"), function (item) {
                var $dom = $(item);
                if (!$dom.hasClass('optBtnActive')) {
                    $dom.css('background-image', $dom.css('background-image').replace("_hover.png", ".png"));
                }
            });
        }

        /**
         * switch bg to :hover
         * @param $trigger
         * @private
         */
        function _toggleBtnBg($trigger, hover) {
            if ($trigger.hasClass('optBtnActive')) return false;
            var id = $trigger.attr("toggle-ac");
            var bgImg = $trigger.css('background-image');
            if (hover) {
                if (bgImg.indexOf("default") > -1) {
                    $trigger.css('background-image',
                        bgImg.replace('default', 'default_hover'));
                } else {
                    $trigger.css('background-image',
                        bgImg.replace(id.toLocaleLowerCase(), id.toLocaleLowerCase().concat('_hover')));
                }
            } else {
                $trigger.css('background-image',
                    bgImg.replace('_hover.png', '.png'));
            }
        }

        /**
         * 检测当前widget是否已经创建
         * @param widgetId
         * @returns {boolean}
         * @private
         */
        function _isCreated(widgetId) {
            for (var i in widgets) {
                var wid = widgets[i].id;
                if (wid === widgetId) return true;
            }
            return false;
        }

        /***
         * 检测是否是相同url的不同widget
         * @param widget
         * @private
         */
        function _isDuplicated(widget) {
            for (var i in widgets) {
                var wid = widgets[i].id;
                var lbl = widgets[i].label;
                if (wid === widget.url && lbl != widget.label) return true;
            }
            return false;
        }

        /***
         * 监听左侧widget功能关闭按钮
         */
        function _widgetCloseListener() {

            $("#verticalMenu .optbtn").on("mouseenter", function () {
                $(this).find(".close-this").css("display", "block");
            });

            $("#verticalMenu .optbtn").on("mouseleave", function () {
                $(this).find(".close-this").css("display", "none");
            });
            /**
             * 左侧栏关闭按钮
             */
            $("#verticalMenu .close-this").unbind('click').on("click", function (event) {
                event.stopPropagation();
                var widgetId = $(this).parent().attr("toggle-ac");                    // 当前widget的id
                var preWidgetId = $(this).parent().prev(".optbtn").attr("toggle-ac"); //自动切到前面一个widge的id
                _removeWidget(widgetId).then(lang.hitch(this, function () {
                    //移除dom元素
                    $(this).parent().remove();
                    $("#panel-" + widgetId).remove();
                    if (preWidgetId != undefined)
                        $("#btn_" + preWidgetId).trigger('click');
                    else
                        $("#btn_VideoManager").trigger('click');
                }));
            });
        }

        /***
         * remove widget
         * @param widgetId
         * @private
         */
        function _removeWidget(widgetId) {
            var d = new Deferred();
            for (var i in widgets) {
                var w = widgets[i];
                if (w.id === widgetId) {
                    w.onDestroy();
                    widgets.splice(i, 1);
                    d.callback();
                    break;
                }
            }
            return d;
        }

        /**
         * 切换widget
         *
         * @param widgetId
         */
        function _switchWidget(widgetId) {
            arrayUtil.forEach(widgets, function (value) {
                if (value.id == widgetId) {
                    if (!value.created) {
                        value.onCreate();
                        value.created = true;
                    }
                    try {
                        value.onOpen();
                    } catch (e) {
                        error('widget [' + value.label + '] on open error [' + e.info + ']');
                    }
                }
                else {
                    try {
                        if (value.created) value.onPause();
                    } catch (e) {
                        error('widget [' + value.label + '] on pause error [' + e.message + ']');
                    }
                }
            })
        }

        /**
         * 所有widget成功加载后触发
         */
        function allWidgetsLoaded() {
            setTimeout(function () {
                $('#verticalMenu >.optbtn:first-child').click();
                log("all widgets loaded");
                EventBus.trigger(EventBus.WIDGETS_LOADED);
                //是否收起左侧面板
                if (urlParams.hasOwnProperty("hideLeftPanel") && urlParams["hideLeftPanel"] === "true") {
                    layoutManager.hideLeftPanel();
                }

                if ($('#btn_RoutePlayBack').length > 0) {
                    $('#btn_RoutePlayBack').click();
                }
            }, 100);
        }

        /**
         *初始化地图infowindow的标题、内容以及消息样式等
         * @private
         */
        function _initInfoWindow() {
            mapInfoWindow.bindMap(mapManager.getCurrentMap().map());
            mapPopup.setMap(mapManager.getCurrentMap().map());
        }

        /**
         *  添加对比地图
         *
         * @param content
         * @private
         */
        var contrastArray = [];
        var treeObj;
        var nodeCheck = false;

        function _addContrastMap() {
            topic.subscribe(MapTopic.MAP_CONTRAST_REMOVE, function () {
                contrasted = false;
                _refreshContrastMap();
            });
            topic.subscribe(MapTopic.MAP_CONTRAST_RESIZE, function () {
                if (contrasted === true)
                    _refreshContrastMap();
            });
            topic.subscribe(MapTopic.MAP_CONTRAST_ADD, function (evt) {
                //对比的服务
                var contrastLayers = evt.layers;
                var contrastName = evt.contrastName;
                var contrastMiniName = evt.name;
                var layerAlias = "";
                if (contrastLayers.length > 0) {
                    layerAlias = contrastLayers[0].alias;
                }

                if (contrastMiniName != "" && contrastMiniName != "undefined" && contrastMiniName != undefined) {
                    layerAlias = contrastMiniName;
                }

                contrasted = true;
                //添加对比图层的div contrastMapDiv
                var div = "<div class='contrast-map' data-order='{{order}}'><div class='layer-alias-div' style='position:absolute;z-index: 10;margin-top: 30px;font-size: 20px;color: red;text-align: center;'>{{layerAlias}}</div><span class='iconfont contrast-map-close' data-order='{{order}}' title='移除'>&#xe641;</span></div>";
                var contrastId = RandomUuid();
                if (contrastNum < 3) {
                    $('#map-content').append(Mustache.render(div, { order: contrastId, layerAlias: layerAlias }));
                    contrastNum += 1;
                } else {
                    layer.msg('出于性能考虑，当前最多可比对四个地图', { time: 2000, icon: 0 });
                    return;
                }
                var order = contrastNum - 1;
                contrastMapDiv = $('#map-content .contrast-map:eq(' + order + ')');
                _refreshContrastMap(contrastName);
                treeObj = $.fn.zTree.getZTreeObj("dirTree");
                if (contrastMapDiv != undefined) {
                    //初始化对比地图
                    var contrastMap = new MiniMap(contrastMapDiv[0], {
                        extent: mapManager.getMainMap().map().extent
                    }, lang.hitch(this, _refreshContrastLayers, contrastId, contrastLayers));
                } else {
                    layer.msg('出于性能考虑，当前最多可比对四个地图', { time: 2000, icon: 0 });
                }
            });
            topic.subscribe(MapTopic.MAP_CONTRAST_CHANGE, function (evt) {
                if (nodeCheck) {
                    var order = $('.selected-contrast:eq(0)').data('order');
                    for (var i in contrastArray) {
                        if (contrastArray[i].order == order) {
                            var contrastMap = contrastArray[i].map;
                            var layerIds = contrastArray[i].layerIds;
                            if (evt.checked) {
                                contrastMap.addLayers(evt.layers);
                                layerIds.push(evt.layers[0].id);
                            } else {
                                contrastMap.removeLayerById(evt.layers[0].id);
                                arrayUtil.forEach(layerIds, function (item, i) {
                                    if (item == evt.layers[0].id)
                                        layerIds.splice(i, 1);
                                });
                            }
                        }
                    }
                }
            });
        }

        /***
         *
         * @param cId
         * @param cLayers
         * @param cMap  minimap实例
         * @private
         */
        function _refreshContrastLayers(cId, cLayers, cMap) {
            var mapObj = cMap.getMap();
            $(mapObj.root).css("top", "-46px");
            cMap.removeAllLayers();
            for (var i in cLayers) {
                cLayers[i].visible = true;
            }
            cMap.addLayers(cLayers);
            //设置对比地图和主地图相同的比例尺
            cMap.setScale(mainScale);
            var layerIds = [];
            for (var i in cLayers) {
                layerIds.push(cLayers[i].id);
            }
            contrastArray.push({ order: cId, map: cMap, layerIds: layerIds });
            require(["map/core/GeoDataStore",
                "map/core/EsriSymbolsCreator",
                "esri/Color",
                "esri/geometry/Polygon",
                "esri/graphic",
                "esri/layers/GraphicsLayer"
            ], function (GeoDataStore, EsriSymbolsCreator, Color, Polygon, Graphic, GraphicsLayer) {
                var geoDataStore = GeoDataStore.getInstance();
                geoDataStore.fetch(GeoDataStore.SK_UPLOAD).then(lang.hitch(this, onShareData));
                geoDataStore.on("onChange", lang.hitch(this, onShareData));
                function onShareData(data) {
                    var type = data.type;
                    switch (type) {
                        case GeoDataStore.SK_UPLOAD: {
                            var features = data.features ? data.features : null;
                            if (lang.isArray(features) && features.length > 0) {
                                var graphicsLayer = new GraphicsLayer({ id: GeoDataStore.SK_UPLOAD });
                                var lineSymbol = EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([255, 0, 0]), 2);
                                var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID, lineSymbol, new Color([0, 0, 0, 0.25]));
                                arrayUtil.forEach(features, function (feature) {
                                    var geometry = feature.geometry;
                                    if (geometry.type == undefined) geometry = new Polygon(geometry);
                                    var gra = new Graphic(geometry, fillSymbol);
                                    graphicsLayer.add(gra);

                                });
                                cMap.getMap().addLayer(graphicsLayer);
                            }
                        }
                    }
                }
            });
            //移除对比地图事件
            $('.contrast-map-close').unbind('click').on('click', function () {
                var order = $(this).data('order');
                for (var i in contrastArray) {
                    if (contrastArray[i].order == order) {
                        contrastArray.splice(i, 1);
                    }
                }
                contrastNum -= 1;
                $(this).parent().remove();
                _refreshContrastMap();
            });
            topic.publish(MapTopic.MAP_LAYERS_REFRESHED, { map: mapObj });
            nodeCheck = true;
        }

        var mainScale;

        /**
         * 重新计算主地图和对比地图的位置
         * @private
         */
        function _refreshContrastMap(contrastName) {
            //记录主地图的比例尺
            mainScale = mapManager.getMainMap().map().getScale();
            var mainLayers = mapManager.getMainMap().map().layerIds;
            var $mapContent = $('#map-content');
            var $mainMapCtx = $('#map-content > #main-map');
            var mainWidth = $mapContent.width();
            var mainHeight = $mapContent.height();
            var contrastWidth;
            var contrastHeight;
            switch (contrastNum) {
                case 0:
                    $mainMapCtx.width(mainWidth);
                    $mainMapCtx.height(mainHeight);
                    $mainMapCtx.removeClass('contrast-border');
                    $mainMapCtx.removeClass("contrast-main-map");
                    $mainMapCtx.removeClass("selected-contrast");
                    $mainMapCtx.unbind('click');
                    topic.publish(MapTopic.MAP_MAIN_CHECKED, { isSingle: true });
                    //移除地图中心的+
                    $("#map-content #main-map .esriControlsBR-new .esriAttribution-new .esriAttributionList").html("");
                    $("#map-content #main-map .esriControlsBR-new .esriAttribution-new").removeClass("esriAttribution-new").addClass("esriAttribution");
                    $("#map-content #main-map .esriControlsBR-new").removeClass("esriControlsBR-new").addClass("esriControlsBR");
                    break;
                case 1:
                    contrastWidth = mainWidth * 0.5 - 6;
                    contrastHeight = mainHeight - 6;
                    $mainMapCtx.addClass("contrast-main-map");
                    $mainMapCtx.addClass('contrast-border');
                    break;
                case 2:
                    contrastWidth = mainWidth / 3 - 6;
                    contrastHeight = mainHeight - 6;
                    break;
                case 3:
                    contrastWidth = mainWidth * 0.5 - 6;
                    contrastHeight = mainHeight * 0.5 - 6;
                    break;
            }
            if (contrastNum > 0) {
                if ($mainMapCtx.find(".layer-alias-div").length <= 0) {
                    var mainLayerAliasDiv = null;
                    if (mainLayers != null && mainLayers.length > 0) {
                        var obj = getServiceById(mainLayers[0]);
                        if (contrastName == undefined || contrastName == "undefined" || contrastName == "") {
                            contrastName = obj.alias;
                        }
                        mainLayerAliasDiv = $("<div class='layer-alias-div' style='position:absolute;z-index: 10;margin-top: 30px;font-size: 20px;color: red;text-align: center;'>" + contrastName + "</div>");
                    }
                    mainLayerAliasDiv.insertBefore("#map-content > #main-map>#main-map_root");
                }

                $('.contrast-map').css({ display: 'block' });
                $mainMapCtx.width(contrastWidth);
                $mainMapCtx.height(contrastHeight);
                $('.contrast-map').addClass('contrast-border');
                $('.contrast-map').width(contrastWidth);
                $('.contrast-map').height(contrastHeight);
                $('.layer-alias-div').width(contrastWidth);

                $mainMapCtx.removeClass("selected-contrast");
                $('.contrast-map').removeClass("selected-contrast");
                $('.contrast-map:last').addClass("selected-contrast");
                $('.contrast-map-close').css('top', contrastHeight - 37);
                selectContrast();
                setTimeout(function () {//添加地图中心的+
                    $(".contrast-border .esriControlsBR").addClass("esriControlsBR-new").removeClass("esriControlsBR").css({
                        "position": "absolute",
                        "z-index": "30", "left": "50%", "top": "50%", "text-align": "center"
                    });
                    $(".contrast-border .esriControlsBR-new .esriAttribution").addClass("esriAttribution-new").removeClass("esriAttribution").css({
                        "display": "inline-block",
                        "font-size": "40px", "color": "#f7061a", "background": ""
                    });
                    $(".contrast-border .esriControlsBR-new .esriAttribution-new .esriAttributionList").html("+");

                }, 300);

            } else {
                $("#map-content > #main-map>.layer-alias-div").remove();
            }
        }

        /***
         * get service name
         * @param id
         */
        function getServiceById(id) {
            var operaLayers = MapManager.getInstance().getMapConfig().operationalLayers;
            if (operaLayers != undefined) {
                var data = arrayUtil.filter(operaLayers, function (item) {
                    return item.id === id;
                });
                if (data.length > 0)
                    return data[0];
            }
        }

        /**
         * 对比地图点击监听
         */
        function selectContrast() {
            $('.contrast-border').unbind('click').on('click', function () {
                $('.contrast-border').removeClass('selected-contrast');
                $(this).addClass('selected-contrast');
                var flag = $(this).hasClass('contrast-main-map');
                var order = $(this).data('order');
                if (order == undefined && flag) {
                    topic.publish(MapTopic.MAP_MAIN_CHECKED, null);
                } else {
                    arrayUtil.forEach(contrastArray, function (item) {
                        if (item.order === order) {
                            var childrenNodes = [];
                            if (treeObj == null) {
                                treeObj = $.fn.zTree.getZTreeObj("dirTree");
                            }
                            arrayUtil.forEach(treeObj.transformToArray(treeObj.getNodes()), function (treeNode) {
                                if (!treeNode.isParent && !treeNode.isDynamic)
                                    childrenNodes.push(treeNode);
                            });
                            var tmp = childrenNodes;
                            arrayUtil.forEach(item.layerIds, function (id) {
                                for (var i in tmp) {
                                    var childNode = tmp[i];
                                    if (id == childNode.id) {
                                        treeObj.checkNode(childNode, true, true);
                                        tmp.splice(i, 1);
                                    }
                                }
                            });
                            arrayUtil.forEach(tmp, function (childNode) {
                                treeObj.checkNode(childNode, false, true);
                            });
                            nodeCheck = true;
                        }
                    });
                }
            });
        }

        /**
         * get instance
         *
         * @returns {*}
         */
        me.getInstance = function () {
            if (instance === undefined) {
                instance = new me();
            } else {
                //
            }
            return instance;
        };


        return me;
    });