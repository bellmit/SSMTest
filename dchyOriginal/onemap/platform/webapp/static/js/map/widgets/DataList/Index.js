/**
 * 数据列表 用于展示该地图模板中涉及到的地图服务
 * new: 支持显示动态图层（graphicsLayer及其他动态添加服务）
 * config:{
 *   "expandLevel":1,
 *   "simple":true
 * }
 * expandLevel:展开级别
 * simple:true/false 服务请求类型 true--只加载已配置的相关服务 false--加载所有的服务及分组 默认true
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/8 16:41
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "dojox/uuid/generateRandomUuid",
    "esri/layers/GraphicsLayer",
    "esri/lang",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/manager/ConfigManager",
    "mustache",
    "layer",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "static/thirdparty/jquery/jquery.pseudo",
    "slimScroll",
    "ztree",
    "css!exstyle/thirdparty/ztree/css/zTreeBlue.css"], function (declare, lang, arrayUtil, topic, RandomUuid, GraphicsLayer,
        esriLang, BaseWidget, Environment, ConfigManager, Mustache,
        layer) {

        var _map, _widgetConfig, expandLevel;
        var _configManager = ConfigManager.getInstance();
        var defaultYear;
        var groupByYear;
        var zTreeContainer;
        var unVisiableIndex;

        var me = declare([BaseWidget], {
            /**
             *
             */
            constructor: function () {

            },
            /**
             * 重写
             */
            onCreate: function () {
                _map = this.getMap().map();
                _widgetConfig = this.getConfig();
                expandLevel = _widgetConfig.expandLevel;
                defaultYear = appConfig.defaultYear;
                groupByYear = _widgetConfig.groupByYear;
                initDataList();

                //初始化执法监察
                if (_widgetConfig.hasOwnProperty("zfjc")) {
                    //zfjc(_widgetConfig.zfjc);
                    $("#menu-content").append('<li><span id="menu_22" data-toggle="tooltip" title="执法监察" class="fa fa-video-camera" onclick="openVideo();"><b class="menu-gap"></b></span></li>');
                }
            },
            /**
             *
             */
            onOpen: function () {
                this.getMap();
            },
            /**
             *
             */
            onPause: function () {
            }
        });

        var treeObj;
        var addarr = [];
        //动态节点
        var dynamicNode = {
            id: RandomUuid(),
            alias: '动态数据',
            name: 'dynamicNode',
            isDynamic: true,
            isParent: true,
            children: []
        };
        var dynamicChildrenNodes = [];

        /* function zfjc(zfjc) {
             if (zfjc.hasOwnProperty("open")&&zfjc["open"]=="true"){
                 var imgUrl=(zfjc.hasOwnProperty("imgUrl")?zfjc["imgUrl"]:"")+"?proid="+proid;
                 if (indexCodePost==null||indexCodePost==""){
                     layer.msg("摄像头未关联，请关联摄像头！", {time: 1000, icon: 0});
                 }
                 var videoUrl=(zfjc.hasOwnProperty("videoUrl")?zfjc["videoUrl"]:"")+indexCodePost;
                 var content = Mustache.render($("#zfjc-tpl").html(), {imgUrl: imgUrl,videoUrl:videoUrl});
                 topic.publish("result/mini-show", {
                     content: content,
                     header: '执法监察',
                     height: 500
                 });
             }
         }*/
        /**
         *初始化外观
         */
        function initDataList() {
            //zTree setting
            var setting = {
                check: {
                    enable: true,
                    autoCheckTrigger: true,
                    chkPosition: 'before'
                },
                view: {
                    showLine: true,
                    selectedMulti: false,
                    addDiyDom: addDiyDom
                },
                data: {
                    key: {
                        checked: "visible",
                        name: "alias",
                        url: "nourl"
                    }
                },
                callback: {
                    onDblClick: zTreeOnDblClick,
                    onCheck: onNodeCheck,
                    beforeClick: zTreeBeforeClick,
                    onExpand: zTreeOnExpand
                }
            };

            var treeHeight = $(window).height() - 130;

            $(".om-tab-content").height(treeHeight);
            if (groupByYear) {
                //分类成现势数据和历史数据
                zTreeContainer = $("#classificationTree");
                $("#dataListContainer").append(Mustache.render(zTreeContainer.html()));
                treeHeight -= 40;
                //现势数据/历史数据tab切换
                $(".data-list-tab").on("click", function () {
                    var $this = $(this);
                    if ($this.hasClass(".tab-active")) return;
                    $(".om-nav-tabs").children(".tab-active").removeClass("tab-active");
                    $this.addClass("tab-active");
                    $(".om-tab-content").children(".tab-content-active").removeClass("tab-content-active");
                    $("#" + $this.data("id")).addClass("tab-content-active");
                });
            } else {
                //不进行分类
                zTreeContainer = $("#mixingTree");
                $("#dataListContainer").append(Mustache.render(zTreeContainer.html()));
            }


            $(".ztreeContainer").height(treeHeight);

            layer.config();
            _map.on('layer-add', function (evt) {
                if (esriLang.isDefined(evt.layer))
                    detectLayer(evt.layer);
            });

            _map.on("extent-change", lang.hitch(this, _mapExtentChangeHandle));

            $(".ztreeContainer").slimScroll({
                height: treeHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });

            //请求数据

            var isSimple = esriLang.isDefined(_widgetConfig.simple) ? _widgetConfig.simple : true;
            var _url = esriLang.substitute({ val: isSimple }, "/omp/map/" + tpl + "/services?simple=${val}");
            $.ajax({
                url: _url,
                dataType: "json",
                success: function (result) {
                    if (esriLang.isDefined(result.success)) {
                        layer.msg(result.msg, { icon: 0, time: 8000 });
                        return;
                    }
                    if (groupByYear) {
                        //根据服务年份分类成现势数据和历史数据
                        var data = _getCurrentAndHistoricalServices(result, defaultYear);
                        $.fn.zTree.init($("#currentDataTree"), setting, data.currentData);
                        $.fn.zTree.init($("#historicalDataTree"), setting, data.historicalData);
                        treeObj = [$.fn.zTree.getZTreeObj("currentDataTree"), $.fn.zTree.getZTreeObj("historicalDataTree")];
                        arrayUtil.forEach(treeObj, function (item) {
                            _dispose(item);
                        });
                        var noDataTpl = '<li class="no-data-tips" style="text-align: center;margin-top: 50px;">Orz...当前分组下找不到数据</li>';
                        if (data.currentData.length === 0) $("#currentDataTree").append(noDataTpl);
                        if (data.historicalData.length === 0) $("#historicalDataTree").append(noDataTpl);
                    } else {
                        $.fn.zTree.init($("#dirTree"), setting, result);
                        treeObj = $.fn.zTree.getZTreeObj("dirTree");
                        _dispose(treeObj);
                    }
                    afterTreeLoaded();

                    /**
                     * expand tree
                     * @param obj
                     * @private
                     */
                    function _dispose(obj) {
                        if (obj) {
                            var nodes = obj.getNodes();
                            if (nodes.length > 0) {
                                if (expandLevel == 1) {
                                    for (var i in nodes) {
                                        obj.expandNode(nodes[i], true, false, false);
                                    }
                                } else {
                                    obj.expandAll(true);
                                }
                            }
                        }
                    }
                }
            });
        }

        /**
         * 添加对比按钮
         * @param treeId
         * @param treeNode
         */
        function addDiyDom(treeId, treeNode) {
            var aObj = $("#" + treeNode.tId + "_a");
            var parent = treeNode.getParentNode();
            if ((parent != undefined && parent.id === dynamicNode.id) || treeNode.id === dynamicNode.id) return;
            if ($("#compareBtn_" + treeNode.id).length > 0) return;
            aObj.append(Mustache.to_html($("#layerMenuTpl").html(), { id: treeNode.id }));
            var compareBtn = $('#compareBtn_' + treeNode.id);
            if (esriLang.isDefined(compareBtn)) {
                //绑定对比按钮点击事件
                compareBtn.unbind('click').on("click", function () {
                    var arr = [];
                    getChildNodes(treeNode);
                    function getChildNodes(parentNode) {
                        if (!parentNode.isParent) {
                            parentNode.visible = true;
                            arr.push(parentNode);
                        } else {
                            for (var i in parentNode.children) {
                                var child = parentNode.children[i];
                                getChildNodes(child);
                            }
                        }
                    }
                    topic.publish(MapTopic.MAP_CONTRAST_ADD, { layers: arr });
                });
            }
        }

        /***
         * 缩放至服务全图范围
         * @param alias
         */
        function zoomToFullExtent(alias) {
            var layer = getOperaLyr(alias);
            if (esriLang.isDefined(layer)) {
                var mLyr = _map.getLayer(layer.id);
                //服务已加载且可见时
                //缩放至服务全图范围
                if (esriLang.isDefined(mLyr) && mLyr.visible == true) {
                    if (esriLang.isDefined(mLyr.fullExtent)) {
                        _map.setExtent(mLyr.fullExtent);
                    }
                }
            }
        }

        /**
         * 数据树加载成功之后 注册事件监听等
         */
        function afterTreeLoaded() {

            //订阅事件 用于添加动态数据列表
            topic.subscribe("data/list!add", lang.hitch(this, addDynamicNode));

            //ready for list add
            topic.publish("data/list!ready");

            //点击图标实现全图
            $(".ztree").find(".ico_docu").on('click', function () {
                zoomToFullExtent($(this).next().text().trim());
            });

            //地图加载服务发生更新后 触发此订阅事件 用于更新数据列表的check状态
            topic.subscribe(MapTopic.MAP_LAYERS_REFRESHED, function (data) {
                var _map = data.map;
                var isMainMap = data.isMainMap;
                if (esriLang.isDefined(isMainMap)) {
                    topic.publish(MapTopic.MAP_MAIN_CHECKED);
                } else {
                    refreshCheckStatus(_map);
                }
            });
            topic.subscribe(MapTopic.MAP_MAIN_CHECKED, function () {
                refreshCheckStatus(map.map());
            });
        }

        /**
         * 更新数据列表的check状态
         * @param m 关联的地图
         */
        function refreshCheckStatus(m) {
            if (esriLang.isDefined(m)) {
                if (treeObj instanceof Array) {
                    arrayUtil.forEach(treeObj, function (item) {
                        _dispose(item);
                    });
                } else {
                    _dispose(treeObj);
                }

                //找出普通子节点
                function _dispose(obj) {
                    var normalNodes = obj.getNodesByFilter(function (node) {
                        return !(node.isParent || node.isDynamic);
                    });
                    if (lang.isArray(normalNodes)) {
                        arrayUtil.forEach(obj.transformToArray(normalNodes), function (item) {
                            if (esriLang.isDefined(m.getLayer(item.id))) {
                                obj.checkNode(item, true, true);
                            } else {
                                obj.checkNode(item, false, true);
                            }
                        });
                    }
                }
            }
        }

        /**
         * 点击之前事件
         * @param evt
         */
        function zTreeBeforeClick(treeId, treeNode, clickFlag) {
            return false;
        }

        /**
         * 节点展开事件
         */
        function zTreeOnExpand() {
            $(".layer-opacity").prev().addClass("layer-name");
            //图层hover时显示透明度按钮
            $(".layer-name").parent('a').unbind('mouseover').on('mouseover', function () {
                $(this).find(".layer-opacity").show();
            });

            $(".layer-name").parent('a').unbind('mouseout').on('mouseout', function () {
                $(this).find(".layer-opacity").hide();
            });
        }

        /***
         *  双击缩放至该服务
         * @param event
         * @param treeId
         * @param treeNode
         */
        function zTreeOnDblClick(event, treeId, treeNode) {
            var sId = treeNode.id;
            var lyr = _map.getLayer(sId);
            if (esriLang.isDefined(lyr)) {
                //缩放至服务可见范围
                _map.setScale(lyr.minScale);
            }
        };

        /***
         * 检测图层是否当前比例尺可见 若不可见 则给出提示
         * @param id
         */
        function detectLayer(lyr) {
            if (lyr.visibleAtMapScale === false) {
                var oLyr = getOperaLyr(lyr.id);
                if (esriLang.isDefined(oLyr)) {
                    console.warn(esriLang.substitute({ alias: oLyr.alias }, "『${alias}』在当前级别不可见，请双击服务名称查看"), { icon: 0 });
                    unVisiableIndex = layer.msg(esriLang.substitute({ alias: oLyr.alias }, "『${alias}』在当前级别不可见，请双击服务名称查看"), {
                        time: 2500,
                        offset: 'rb'
                    });
                }
            }
        }


        /**
         * 地图缩放时 判断图层是否在可见比例尺中
         * @private
         */
        function _mapExtentChangeHandle() {
            var ids = _map.layerIds;
            layer.close(unVisiableIndex);
            unVisiableIndex = undefined;
            arrayUtil.forEach(ids, function (item) {
                var lyr = _map.getLayer(item);
                detectLayer(lyr);
            })
        }

        /**
         * opacity tooltip
         * @param treeNode
         */
        function afterShowTip(treeNode) {
            var transparencyBtn = $('.popover-clone .opacity-slider');
            transparencyBtn.slider({
                orientation: "horizontal",
                range: "min",
                max: 100,
                value: parseInt(treeNode.alpha * 100),
                slide: changeTrans,
                change: changeTrans
            });
            transparencyBtn.prev().find('span').html(parseInt(treeNode.alpha * 100) + '%');
            function changeTrans() {
                var val = $(this).slider("value");
                $(this).prev().find('span').html(val.toString() + '%');
                treeNode.alpha = val * 0.01;
                var _mLyr = _map.getLayer(treeNode.id);
                if (_mLyr != undefined)
                    _mLyr.setOpacity(treeNode.alpha);
            }
        }

        /***
         * 增加动态子节点
         * @param data
         */
        function addDynamicNode(data) {
            if (treeObj !== undefined) {
                if (treeObj instanceof Array) {
                    $("#currentDataTree li").remove(".no-data-tips");
                    _dispose(treeObj[0]);
                } else {
                    _dispose(treeObj);
                }

                //增加动态节点
                function _dispose(obj) {
                    //检查动态节点是否已存在
                    var node = obj.getNodesByFilter(function (node) {
                        return node.id === dynamicNode.id;
                    }, true);
                    try {
                        if (node === null) {
                            node = obj.addNodes(null, dynamicNode)[0];
                            dynamicNode = node;
                            //移动至第一个节点
                            var nodes = obj.getNodes();
                            obj.moveNode(nodes[0], dynamicNode, "prev");
                        }
                        //添加动态子节点
                        if (data != undefined) {
                            if (lang.isArray(data)) {
                                data = arrayUtil.filter(data, function (item) {
                                    return isExist(item) === false;
                                });
                                if (data.length > 0) {
                                    arrayUtil.forEach(data, function (item) {
                                        item.isDynamic = true;
                                        item.visible = getDynamicNodeVisible(item);
                                    });
                                } else return;
                            } else {
                                if (isExist(data) === false) {
                                    data.isDynamic = true;
                                    data.visible = getDynamicNodeVisible(data);
                                } else return;
                            }
                            var nodes = obj.addNodes(dynamicNode, data);
                            if (nodes != null && nodes.length > 0)
                                dynamicChildrenNodes = dynamicChildrenNodes.concat(nodes);
                        }
                    }
                    catch (e) {
                        console.error(e);
                    }
                }
            }
        }

        /**
         * 改变check状态时触发
         * @param event
         * @param treeId
         * @param treeNode
         */
        function onNodeCheck(event, treeId, treeNode) {
            if ($('.selected-contrast').length > 0 && $('.selected-contrast:eq(0)').hasClass('contrast-map')) {
                if (!treeNode.isParent) {
                    var checked = false;
                    if (treeNode.getCheckStatus().checked)
                        checked = true;
                    topic.publish(MapTopic.MAP_CONTRAST_CHANGE, { layers: [treeNode], checked: checked });
                }
            } else {
                if (!treeNode.isParent) {
                    if (treeNode.isDynamic) {
                        changeDynamicNode([treeNode], treeNode.getCheckStatus().checked);
                    } else {
                        if (treeNode.getCheckStatus().checked) {
                            if (treeNode.url != "") {
                                addLayer(treeNode);
                            }
                        } else {
                            if (map.map().getLayer(treeNode.id) != undefined && map.map().getLayer(treeNode.id).loaded) {
                                map.removeLayerById(treeNode.id);
                                for (var i = 0; i < addarr.length; i++) {
                                    if (addarr[i] == treeNode.index) {
                                        addarr.splice(i, 1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (treeNode.isDynamic == true) {
                        changeDynamicNode(treeNode.children, treeNode.getCheckStatus().checked);
                    }
                }
            }

        }

        /***
         * 改变动态节点的状态
         * @param nodes
         * @param checked
         */
        function changeDynamicNode(nodes, checked) {
            arrayUtil.forEach(nodes, function (node) {
                if (node.graphicsId !== undefined) {
                    if (lang.isArrayLike(node.graphicsId)) {
                        arrayUtil.forEach(node.graphicsId, function (gid) {
                            var graphicsLayer = _map.getLayer(gid);
                            checked ? graphicsLayer.show() : graphicsLayer.hide();
                        });
                    }
                }
            });
        }

        /***
         * get visible of dynamic node
         * @param node
         */
        function getDynamicNodeVisible(node) {
            if (node.graphicsId !== undefined) {
                var graphicsLayer = _map.getLayer(lang.isArrayLike(node.graphicsId) ? node.graphicsId[0] : node.graphicsId);
                return graphicsLayer.visible;
            }
            return false;
        }

        /***
         * 节点是否已经存在
         * @param node
         */
        function isExist(node) {
            var childrenNodes = dynamicNode.children;
            if (childrenNodes != undefined && childrenNodes.length > 0) {
                var r = arrayUtil.filter(childrenNodes, function (item) {
                    return item.id === node.id || item.graphicsId === node.graphicsId;
                });
                if (r.length > 0) return true;
            } else
                return false;
        }

        /***
         * 获取模板里的服务图层
         * @param sId
         * @returns {*}
         */
        function getOperaLyr(opt) {
            var operaLyrs = _configManager.getOperaLayers();
            if (lang.isArray(operaLyrs)) {
                var temp = arrayUtil.filter(operaLyrs, function (item) {
                    return item.id == opt || item.alias == opt;
                });
                return temp[0];
            }
            return undefined;
        }

        /**
         * 添加图层
         * @param layer
         */
        function addLayer(node) {
            var layer = getOperaLyr(node.id);
            if (esriLang.isDefined(layer)) {
                addarr.push(layer.index);
                addarr.sort(function (a, b) {
                    return a > b ? 1 : -1
                });
                var index = -1;
                for (var i = 0; i < addarr.length; i++) {
                    if (addarr[i] == layer.index) {
                        index = i;
                        break;
                    }
                }
                layer.visible = true;
                map.addLayer(layer, layer.index); //按照配置的顺序加载
                //处理baselayers
                var baseLyrs = _configManager.getBaseLayers();
                baseLyrs = arrayUtil.filter(baseLyrs, function (obj) {
                    return obj.top;
                });
                if (baseLyrs.length > 0) {
                    //对存在置顶的base layer进行重新排序
                    /* var baseLyr = arrayUtil.filter(map.map().layerIds, function (item) {
                         return "layer0" === item;
                     })[0];*/
                    var baseLyr = baseLyrs[0];
                    if (baseLyr != undefined) {
                        map.map().reorderLayer(baseLyr, map.map().layerIds.length);
                    }
                }
            }
        }

        /**
         * 根据年份对服务进行分类成现势数据和历史数据
         * @param services
         * @param year
         * @returns {{currentData: Array, historicalData: Array}}
         */
        function _getCurrentAndHistoricalServices(services, year) {
            var currentYear = (typeof year === "undefined" || year === "current") ? function () {
                var date = new Date();
                return date.getFullYear();
            }() : parseInt(year);
            if (typeof currentYear === "undefined") {
                console.error('services classify failed! please check the tpl global config [defaultYear], that must be string of the full year or "current"');
                return { currentData: [], historicalData: [] };
            }

            return {
                currentData: _serviceClassify(services, currentYear, "current"),
                historicalData: _serviceClassify(services, currentYear, "historical")
            };
        }

        /**
         * 服务分类
         * @param data
         * @param currentYear
         * @param type  "current" or "historical"
         * @returns {Array}
         * @private
         */
        function _serviceClassify(data, currentYear, type) {
            var dataArray = [];
            arrayUtil.forEach(data, function (item) {
                var group = {
                    "id": item.id,
                    "alias": item.alias,
                    "name": item.name,
                    "visible": item.visible
                };
                if (item.hasOwnProperty("children")) {
                    var children = [];
                    arrayUtil.forEach(item.children, function (child) {
                        if (child.hasOwnProperty("children")) {
                            var childGroup = {
                                "id": child.id,
                                "alias": child.alias,
                                "name": child.name,
                                "visible": child.visible
                            };
                            childGroup.children = _serviceClassify(child.children, currentYear, type);
                            if (childGroup.children.length > 0) children.push(childGroup);
                        } else {
                            if (child.hasOwnProperty("year")) {
                                switch (type) {
                                    case "current":
                                        if (child.year >= currentYear) children.push(child);
                                        break;
                                    case "historical":
                                        if (child.year < currentYear) children.push(child);
                                        break;
                                }
                            }
                        }
                    });
                    group.children = children;
                    if (group.children.length > 0) dataArray.push(group);
                } else if (item.hasOwnProperty("year")) {
                    switch (type) {
                        case "current":
                            if (item.year >= currentYear) dataArray.push(item);
                            break;
                        case "historical":
                            if (item.year < currentYear) dataArray.push(item);
                            break;
                    }
                }
            });

            return dataArray;
        }

        return me;
    });