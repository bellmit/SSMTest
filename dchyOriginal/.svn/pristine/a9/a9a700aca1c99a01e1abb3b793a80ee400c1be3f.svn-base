/**
 * 图层管理
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/11 10:07
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Deferred",
    "dojo/topic",
    "esri/lang",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/layer",
    "handlebars",
    "map/manager/ConfigManager",
    "map/component/SpyTool",
    "map/component/SwipeTool",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "jqueryUI",
    "layer",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "css!static/thirdparty/jquery/jquery-ui.min"], function (declare, lang, arrayUtil, Deferred, topic, esriLang, ArcGISDynamicMapServiceLayer,
        ArcGISTiledMapServiceLayer, Layer, Handlebars, ConfigManager, SpyTool, SwipeTool,
        BaseWidget, Environment) {
        var __map, $layerList;

        var layerList = declare([BaseWidget], {
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
                _widgetConfig = this.getConfig();
                _init();
            },
            /***
             * fires before destroy widget
             */
            onDestroy: function () {
                if (spyTool.isActive()) spyTool.deactivate();
                if (swipeTool.isActive()) swipeTool.deactivate();
            }
        });

        var layers = [], configManager = ConfigManager.getInstance();
        var layersTpl, spyTool, swipeTool;     //通透镜，卷帘工具

        /**
         *
         * @private
         */
        function _init() {
            $layerList = $("#layer-list");
            layersTpl = $('#layersTpl').html();
            _refreshComponent();
            spyTool = new SpyTool({ map: __map });
            swipeTool = new SwipeTool({ map: __map });
        }

        /**
         *
         * @private
         */
        function _refreshComponent() {
            layers = [];
            $layerList.empty();
            arrayUtil.forEach(__map.layerIds, function (item, i) {
                var lyrid = item;
                var lyr = __map.getLayer(lyrid);
                if ((lyr instanceof ArcGISDynamicMapServiceLayer) || (lyr instanceof ArcGISTiledMapServiceLayer)) {
                    var tmp = {};
                    var s = _getService(lyr.id);
                    if (esriLang.isDefined(s)) {
                        tmp.visible = lyr.visible;
                        tmp.opacity = lyr.opacity;
                        tmp.opacityOfPercent = lyr.opacity * 100;
                        tmp.id = lyr.id;
                        tmp.name = s.name;
                        tmp.alias = s.alias;
                        tmp.index = i;
                        layers.push(tmp);
                    }
                }
                if (layers.length > 0) {
                    $layerList.empty();
                    var scrollHeight = $(window).height() - 170;
                    $layerList.slimScroll({
                        height: scrollHeight,
                        railVisible: true,
                        railColor: '#333',
                        railOpacity: .2,
                        railDraggable: true
                    });
                    console.log('layers.reverse()：', layers.reverse())
                    $layerList.append(renderTpl(layersTpl, { layers: layers.reverse() }));

                    //阻止拖动图层操作
                    $(".operation-content").on('mousedown', function (e) {
                        e.stopPropagation();
                    });

                    $("a[data-toggle='collapsePreset']").on('click', function () {
                        var $this = $(this);
                        var $i = $this.find("i");
                        if ($i.hasClass('fa-caret-square-o-right')) {
                            $i.removeClass('fa-caret-square-o-right').addClass('fa-caret-square-o-down');
                        } else
                            $i.removeClass('fa-caret-square-o-down').addClass('fa-caret-square-o-right');
                        var id = $this.data("target");
                        var serviceId = $this.data("service");
                        var opacity = $this.data("opacity");
                        var collapsePanel = $(id);
                        if (esriLang.isDefined(collapsePanel) && collapsePanel.length > 0) {
                            collapsePanel.collapse('toggle');
                            collapsePanel.on('shown.bs.collapse', lang.hitch(this, _afterCollapse, serviceId, opacity));
                        }
                       
                    });
                }
            });

            _addListener();
        }

        /**
         *
         * @private
         */
        function _addListener() {
            $layerList.sortable({
                cursor: "move",
                opacity: 0.6,
                placeholder: "ui-state-highlight",
                start: function (evt, ui) {
                    $(this).find(".in").collapse('hide');
                },
                update: function (evt, ui) {
                    var arr = $layerList.sortable("toArray").reverse();
                    if (arr.length > 1) {
                        arrayUtil.forEach(arr, function (item, index) {
                            __map.reorderLayer(__map.getLayer(item), index);
                        });
                    }
                }
            }).disableSelection();
            /**
         *
         * 图层树
         */
            layui.use(['jquery', 'element', 'tree'], function () {
                var $ = layui.jquery
                    , element = layui.element
                    , tree = layui.tree;
                $(function () {

                    var isSimple = esriLang.isDefined(_widgetConfig.simple) ? _widgetConfig.simple : true;
                    var _url = esriLang.substitute({ val: isSimple }, "/omp/map/" + tpl + "/services?simple=${val}");
                   
                    function _layerTree() {
                        //获取图层对应的url
                        $.ajax({
                            url: _url,
                            dataType: "json",
                            async: false,
                            success: function (result) {
                                result.forEach(function (v) {
                                    alias = v.children[0].alias;
                                    url = v.children[0].url;
                                    //获取图层数据
                                    $.ajax({
                                        url: url + '?f=json&dpi=96&transparent=true&format=png',
                                        dataType: "json",
                                        async: false,
                                        success: function (res) {
                                            res.layers.forEach(function (item) {
                                                item.title = item.name;
                                                item.checked = true;
                                                delete item.name
                                            })
                                            console.log(alias + '----------', res.layers);
                                            var $treeDom = $('.bdc-layer-tree');
                                            for (var i = 0; i < $treeDom.length; i++) {
                                                var domAlias = $($treeDom[i]).attr('data-alias');
                                                var domID = $($treeDom[i]).attr('id');
                                                if (domAlias == alias) {
                                                    //渲染图层树
                                                    var idArr = [];
                                                    tree.render({
                                                        elem: '#' + domID  //绑定元素
                                                        , data: res.layers
                                                        , showCheckbox: true
                                                        , showLine: false
                                                        , oncheck: function (obj) {
                                                            var parent_li = $(obj.elem).parents('.list-group-item');
                                                            var dId = $(obj.elem).parents('.bdc-layer-tree').attr('id').split('_')[1];
                                                            var idList = getCheckId(obj.data);
                                                            console.log('当前id--------------:', idList);
                                                            if (obj.checked) {
                                                                idArr = idArr.concat(idList);
                                                            } else {
                                                                for (var i = 0; i < idList.length; i++) {
                                                                    var idx = idArr.indexOf(idList[i]);
                                                                    idArr.splice(idx, 1);
                                                                }
                                                            }
                                                            if (idArr.length == 0) {
                                                                parent_li.find('.lyr-vis-switch').removeClass("omp-switch-on").find('em').text("关");
                                                            } else {
                                                                parent_li.find('.lyr-vis-switch').addClass("omp-switch-on").find('em').text("开");
                                                            }
                                                            console.log(dId + '--------------:', idArr);
                                                            map.map().getLayer(dId).setVisibleLayers(idArr);
                                                        }
                                                    })
                                                }
                                            }
                                        }
                                    })
                                })
                            }
                        })
                    }
                    _layerTree();
                    //获取id
                    function getCheckId(obj) {
                        var idArr = [];
                        if (obj.hasOwnProperty('children')) {
                            var arr = obj.children;
                            for (var i = 0; i < arr.length; i++) {
                                if (arr[i].hasOwnProperty('children')) {
                                    var c = arr[i].children;
                                    for (var j = 0; j < c.length; j++) {
                                        idArr.push(c[j].id);
                                    }
                                } else {
                                    idArr.push(arr[i].id);
                                }
                            }
                        } else {
                            idArr.push(obj.id);
                        }
                        return idArr;
                    }

                    //图层开关
                    $(".lyr-vis-switch").on('click', function (evt) {
                        evt.stopPropagation();
                        var $this = $(this);
                        var id = $this.data("id");
                        _changeVisible(id, !$this.hasClass("omp-switch-on"));
                        if ($this.hasClass("omp-switch-on")) {
                            $this.removeClass("omp-switch-on");
                            $this.find('em').text("关");
                            $this.parents('.list-group-item').find('.layui-form-checkbox').removeClass('layui-form-checked');
                        } else {
                            $this.addClass("omp-switch-on");
                            $this.find('em').text("开");
                            $this.parents('.list-group-item').find('.layui-form-checkbox').addClass('layui-form-checked');
                        }
                    });
                    __map.on('layer-add', function () {
                        _refreshComponent();
                    });

                    __map.on('layer-remove', function () {
                        _refreshComponent();
                    });
                })
            })


        }

        /**
         *
         * @param id
         * @returns {*}
         * @private
         */
        function _getService(id) {
            var operaLayers = configManager.getAppConfig().map.operationalLayers;
            for (var i in operaLayers) {
                var s = operaLayers[i];
                if (s.id === id) {
                    return s;
                }
            }
        }

        /**
         *
         * @param id
         * @param visible
         */
        function _changeVisible(id, visible) {
            var lyr = __map.getLayer(id);
            if (lyr != undefined) {
                lyr.setVisibility(visible);
            }
        }

        /**
         *
         * @param serviceId
         * @param opacity
         * @private
         */
        function _afterCollapse(serviceId, opacity) {
            var transparencyBtn = $('#slider_' + serviceId);
            transparencyBtn.slider({
                orientation: "horizontal",
                range: "min",
                max: 100,
                value: parseInt(opacity),
                slide: changeTrans,
                change: changeTrans
            });
            transparencyBtn.prev().find('span').html(parseInt(opacity) + '%');
            function changeTrans() {
                var val = $(this).slider("value");
                $(this).prev().find('span').html(val.toString() + '%');
                opacity = val;
                var _mLyr = __map.getLayer(serviceId);
                if (_mLyr != undefined) {
                    _mLyr.setOpacity(opacity * 0.01);
                    //改变相关的属性值
                    var lyrId = _mLyr.id;
                    $("a[data-target *='" + lyrId + "']").data('opacity', opacity);
                }
            }

            _addSpySwitchListener();
        }

        /***
         * render handlebars tpl
         * @param tpl
         * @param data
         * @returns {*}
         */
        function renderTpl(tpl, data) {
            var templ = Handlebars.compile(tpl);
            return templ(data);
        }

        /**
         * 通透镜/卷帘监听
         * @private
         */
        function _addSpySwitchListener() {
            if (Environment.isWebkit()) {
                //webkit内核浏览器监听
                $(".operation-btn").checkbox({
                    onChecked: function () {
                        $("label.operation-btn").filter(".active").checkbox("uncheck");
                        $("label.operation-btn").filter(".active").removeClass("active");
                        $("label.operation-btn").filter(".operation-btn-on").removeClass("operation-btn-on");
                        var that = $(this).parent();
                        var layerId = $(that).data("id");
                        var layer = __map.getLayer(layerId);
                        var operationLayers = appConfig.map.operationalLayers;
                        var lyr = arrayUtil.filter(operationLayers, function (l) {
                            return l.id === layerId;
                        })[0];
                        var currentLayer = {
                            id: lyr.id,
                            alias: lyr.alias,
                            layer: layer,
                            index: lyr.index,
                            type: lyr.type,
                            opacity: layer.opacity
                        };
                        switch ($(that).data("type")) {
                            case "spy":
                                spyTool.setLayer(currentLayer);
                                if (swipeTool.isActive()) {
                                    swipeTool.deactivate();
                                }
                                spyTool.activate();
                                break;
                            case "swipe":
                                swipeTool.setLayer(currentLayer);
                                swipeTool.setDirection("horizontal");
                                if (spyTool.isActive()) {
                                    spyTool.deactivate();
                                }
                                swipeTool.activate();
                        }
                        $(that).addClass("operation-btn-on");
                    },
                    onUnchecked: function () {
                        var that = $(this).parent();
                        switch ($(that).data("type")) {
                            case "spy":
                                spyTool.deactivate();
                                break;
                            case "swipe":
                                swipeTool.deactivate();
                        }
                        $(that).removeClass("operation-btn-on");
                    }
                });
            } else {
                //Trident(IE) or other
                $(".operation-btn").off().on('click', function (evt) {
                    evt.stopPropagation();
                    var $this = $(this);
                    if ($this.hasClass("operation-btn-on")) {
                        //关闭通透镜或者卷帘
                        $this.removeClass("operation-btn-on");
                        switch ($this.data("type")) {
                            case "spy":
                                spyTool.deactivate();
                                break;
                            case "swipe":
                                swipeTool.deactivate();
                                break;
                        }
                    } else {
                        //开启相应的功能
                        $("label.operation-btn").filter(".operation-btn-on").trigger("click");
                        var layerId = $this.data("id");
                        var layer = __map.getLayer(layerId);
                        var operationLayers = appConfig.map.operationalLayers;
                        var lyr = arrayUtil.filter(operationLayers, function (l) {
                            return l.id === layerId;
                        })[0];
                        var currentLayer = {
                            id: lyr.id,
                            alias: lyr.alias,
                            layer: layer,
                            index: lyr.index,
                            type: lyr.type,
                            opacity: layer.opacity
                        };
                        switch ($this.data("type")) {
                            case "spy":
                                spyTool.setLayer(currentLayer);
                                spyTool.activate();
                                break;
                            case "swipe":
                                swipeTool.setLayer(currentLayer);
                                swipeTool.setDirection("horizontal");
                                swipeTool.activate();
                        }
                        $this.addClass("operation-btn-on");

                    }
                });
            }
        }

        return layerList;
    });