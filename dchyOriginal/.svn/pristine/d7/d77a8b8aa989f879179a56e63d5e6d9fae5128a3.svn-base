/**
 * 列表展示
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 9:38
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "layer",
    "mustache",
    "handlebars",
    "slimScroll",
    "static/thirdparty/laypage/laypage",
    "esri/toolbars/draw",
    "esri/graphic",
    "esri/lang",
    "map/core/QueryTask",
    "esri/tasks/FeatureSet",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "map/core/JsonConverters",
    "map/core/BaseWidget",
    "map/core/GeometryIO",
    "map/component/ListDataRenderer",
    "map/component/ChosenSelect",
    "map/core/GeoDataStore",
    "map/manager/ConfigManager",
    "dojo/topic",
    "ztree",
    "static/thirdparty/laydate/laydate",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min"], function (declare, lang, arrayUtil, on, layer, Mustache, Handlebars, SlimScroll, laypage, Draw, Graphic, esriLang,
                                                                  QueryTask, FeatureSet, MapUtils, MapPopup, JsonConverters, BaseWidget, GeometryIO, ListDataRenderer, ChosenSelect, GeoDataStore,ConfigManager,topic) {

    var _map, listTreeObj, _listConfig, _layer;
    var mapPopup = MapPopup.getInstance();
    var _configManager = ConfigManager.getInstance();
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
            _map = this.getMap().map();
            _listConfig = this.getConfig();
            _layer = {};
            if (_listConfig != null) {
                _layer = _listConfig.layers[0];
            }
            if(_layer){
                var mapLayer = _map.getLayer(_layer.serviceId);
                var optLayer = getOperaLyr(_layer.serviceId);
                if(!mapLayer){
                    optLayer.visible=true;
                    map.addLayer(optLayer, optLayer.index);
                    topic.publish(MapTopic.MAP_LAYERS_REFRESHED, {map: _map});
                }
            }
            init();
        },
        /**
         *
         */
        onPause: function () {
        },
        /**
         *
         */
        onOpen: function () {
            if(_layer){
                var targetLayer = _map.getLayer(_layer.serviceId);
                if(targetLayer){
                    targetLayer.show();
                }
            }
        }
    });


    //存放配置的属性识别图层
    var _listLayers = [];
    var _resultObj = [];

    /**
     * 初始化组件
     */
    function init() {
        //读取弹出层方式
        layer.config();
        if (_listConfig) {
            //exportTypes = _identifyConfig.exportTypes;
            //exportData = _identifyConfig.exportData;
            var lyrs = _listConfig.layers;
            //组织url等参数
            try {
                $.each(lyrs, function (i, item) {
                    var _lyr = _map.getLayer(item.serviceId);
                    if (_lyr != undefined) {
                        _listLayers.push(lang.mixin(item, {url: _lyr.url}));
                    }
                });
            } catch (e) {
                console.log(e.message);
            }
        }

        $.ajax({
            url: root + "/network/fetch/root/groups",
            success: function (r) {
                renderListTreeData(r);
            }
        });
    }

    /**
     * ztree方式加载监控点分组数据
     * @param treeDom
     * @param data
     */
    function renderListTreeData(data) {
        arrayUtil.forEach(data, function (item) {
            item.open = true;
        });
        $("#list_content").empty();
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
                addDiyDom: addRootOperaDom,
                fontCss: setListFontCss
            },
            callback: {
                onClick: onLeafRegionClick
            }
        };
        listTreeObj = $.fn.zTree.init($("#list_content"), treeSetting, data);
        var scrollHeight = $(window).height() - 300;
        var width = $('#panel-ListShow').width();
        $('#list_content').slimScroll({
            height: scrollHeight,
            width: width - 5,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    /**
     * 树节点点击事件
     * @param event
     * @param treeId
     * @param treeNode
     */
    function onLeafRegionClick(event, treeId, treeNode) {
        if (!treeNode.isParent && !treeNode.hasOwnProperty("objectid")) {
            var where = _layer.queryField.name + "='" + treeNode.id + "'";
            var outFieldsStr = "";
            var outFields = [];
            $.each(_layer.returnFields, function (i1, n1) {
                outFields.push(n1.name);
                if (i1 == _layer.returnFields.length - 1)
                    outFieldsStr = outFieldsStr.concat(n1.name);
                else
                    outFieldsStr = outFieldsStr.concat(n1.name).concat(",");
            });
            var data = {
                layerUrl: getLayerUrl(_layer.layerUrl),
                where: where,
                returnFields: outFieldsStr
            };
            var tree = [];
            $.ajax({
                url: "/omp/map/query",
                data: data,
                success: function (rs) {
                    var r = JSON.parse(rs).features;
                    if (r instanceof Array && r.length > 0) {
                        arrayUtil.forEach(r, function (item) {
                            var geo = item;
                            if (!geo.spatialReference) {
                                geo.geometry.spatialReference = _map.spatialReference;
                            }
                            var graphic = new Graphic(item, {spatialReference: _map.spatialReference});
                            var tmp = {};
                            var attr = item.attributes;
                            tmp.objectid = attr.OBJECTID;
                            tmp.name = attr[_layer.titleField.name];
                            tmp.feature = graphic;
                            tree.push(tmp);
                            if (attr.OBJECTID in _resultObj) {
                                _resultObj[attr.OBJECTID].push(tmp);
                            } else {
                                _resultObj[attr.OBJECTID] = [];
                                _resultObj[attr.OBJECTID].push(tmp);
                            }
                        });
                        listTreeObj.addNodes(treeNode, tree);
                    } else {
                        layer.msg("此区域未发现网点！", {icon: 0, time: 3000});
                    }
                }
            });
        } else if (treeNode.hasOwnProperty("objectid")) {
            var obj = findFeatureByLayerIdAndOid(treeNode.objectid);
            if (_listLayers != null) {
                listItemClickHandler(obj, treeNode.objectid);
            }

        }
    }

    /**
     * 添加树节点按钮监听
     * @param treeId
     * @param treeNode
     */
    function addRootOperaDom(treeId, treeNode) {
        var $tObj = $("#" + treeNode.tId + "_a");
        var tpl = '';
        if (!treeNode.isParent && treeNode.hasOwnProperty("indexCode") && treeNode.status === 0) {
            tpl = '<span class="badge b-count pull-right">离线</span>';
            $tObj.append(tpl);
        } else if (!treeNode.isParent && treeNode.hasOwnProperty("indexCode") && treeNode.status === 1) {
            tpl = '<span class="fa fa-video-camera pull-right" id="view_video_{{indexCode}}" style="color:#4285f4;"></span>';
            $tObj.append(Mustache.render(tpl, {indexCode: treeNode.indexCode}));
            /*$("#view_video_" + treeNode.indexCode).on("click", function (event) {
             event.stopPropagation();
             startupVideo(0, treeNode, undefined);
             });*/
        }
    }

    /**
     * 设置树列表样式
     * @param treeId
     * @param treeNode
     * @returns {{margin-left: string}}
     */
    function setListFontCss(treeId, treeNode) {
        if (treeNode.children === undefined || treeNode.children.length === 0) {
            return {"margin-left": "-8px"}
        }
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
     *
     * @param layerId
     * @param oId
     * @returns {*}
     */
    function findFeatureByLayerIdAndOid(oId) {
        for (var i in _resultObj[oId]) {
            var obj = _resultObj[oId][i];
            if (obj.objectid == oId) {
                return obj;
            }
        }
        return null;
    }

    /**
     * 点击左侧网点事件
     * @param vid
     * @param type
     * @param videoNode
     */
    function listItemClickHandler(obj) {
        var geo = obj.feature;
        _map.setScale(40000);
        MapUtils.setMap(_map);
        MapUtils.highlightFeatures([geo], false, _listConfig.locateOpacity, "graphics4Print");
        if (mapPopup.isShowing) mapPopup.closePopup();
        mapPopup.setData(geo.attributes);
        mapPopup.setTitleField(_layer.titleField.name);
        //mapPopup.setFields(_layer.returnFields);
        var param = _layer.link.param;
        var link = {};
        link.param=_layer.link.param;
        link.url=_layer.link.url;
        arrayUtil.forEach(param, function (item) {
            var name = item.name;
            var value = item.value;
            var realValue = "";

            if (name == "coding" || name == "deviceId") {
                if (value != null && value != "" && geo.attributes.hasOwnProperty(value) && geo.attributes[value] != null && geo.attributes[value] != "") {
                    realValue = geo.attributes[value];
                }
            } else {
                realValue = value;
            }
            /*var ip=geo.attributes[value];
             if (name=="szIP"){
             var reg="(?:(?:1[0-9][0-9]\.)|(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))";
             var ips=ip.match(reg);
             if (ips!=null&&ips.length>0){
             ip=ips[0];
             }
             }*/
            link.url += name + "=" + realValue + "&";

        });
        mapPopup.setLink(link);
        mapPopup.openPopup(geo.geometry).then(function () {
            MapUtils.locatePoint(geo);
        });
    }

    /**
     *
     * @param graphic
     * @param returnFields
     * @returns {*}
     */
    function getInfoContent(graphic, returnFields) {

        var data = [];
        var tmpl = $("#infoContentTpl").html();
        var showData = graphic.attributes;
        for (var i in showData) {
            for (var j = 0; j < returnFields.length; j++) {
                if (i.equalsIgnoreCase(returnFields[j].name)) {
                    data.push({key: returnFields[j].alias, value: showData[i]});
                }
            }
        }
        return Mustache.render(tmpl, {data: data});
    }

    /***
     * 处理图层url
     * @param lyrUrl
     * @returns {string}
     */
    function getLayerUrl(lyrUrl) {
        var sr = _map.spatialReference.wkid;
        var realUrl;
        if (lyrUrl.startWith("http://")) {
            realUrl = sr != undefined ? lyrUrl.concat("/query").concat("?outSR=" + sr) : lyrUrl.concat("/query");
        } else {
            realUrl = sr != undefined ? lyrUrl.replace("/oms", omsUrl).concat("/query").concat("?outSR=" + sr) : lyrUrl.replace("/oms", omsUrl).concat("/query");
        }
        return encodeURI(realUrl);
    }

    return me;
});