/**
 * 地图定位功能
 * 用于其他业务系统实现定位功能
 * eg.http://192.168.90.44:8088/omp/map/tpl_test?action=location&params={"type":"layerLocation","params":{"layerAlias":"SDE.BPDK_320000","where":"JCBH='198'"}}
 * 配置示例：
 *        "locateOpacity": 0.5,
 *       "locateStyle": {
 *         "lineStyle": "solid",
 *         "lineColor": "#2C67F1",
 *         "fillStyle": "solid",
 *         "fillColor": "#2C67C1"
 *       }
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:01
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/on",
    'esri/graphic',
    "esri/lang",
    "esri/tasks/FeatureSet",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/Color",
    "handlebars",
    "layer",
    "map/core/BaseWidget",
    "map/core/QueryTask",
    "map/core/GeoDataStore",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "map/component/ListDataRenderer",
    "map/core/JsonConverters",
    "map/manager/ConfigManager",
    "dojox/uuid/generateRandomUuid",
    "static/js/map/widgets/VideoManager/Index",
    "text!static/js/map/template/location/location-result-tpl.html",
    "map/core/GeometryIO",
    "map/core/EsriSymbolsCreator"], function (declare, arrayUtil, lang, on, Graphic, esriLang, FeatureSet, SimpleMarkerSymbol, Color,
                                              Handlebars, layer, BaseWidget, QueryTask, GeoDataStore, MapUtils, MapPopup, ListDataRenderer, JsonConverters, ConfigManager, RandomUuid, VideoManager,
                                              ResultTpl, GeometryIO, EsriSymbolsCreator) {

    var _map, _locConfig, locType;
    var layerAlias, where;
    var BUFFER_DISTANCE = 2000; // 缓冲距离2公里
    var _showInfo = false;
    var _showResult = false;//是否显示定位结果列表 默认不显示
    var locatedFeatures;
    var _flash=false;
    var design=false;
    var fillColor=[0,0,0,0];
    var lineColor=[255,0,0,1];
    var duration=300000;

    var mapPopup = MapPopup.getInstance();
    var geoDataStore = GeoDataStore.getInstance();
    var highlightFeatureOpacity = undefined, locStyle = undefined;
    //模板中的所有服务
    var operaLayers = ConfigManager.getInstance().getAppConfig().map.operationalLayers;

    var me = declare([BaseWidget], {
        /**
         *
         */
        onCreate: function () {
            _map = this.getMap().map();
            _locConfig = this.getConfig();
            _init();
        },
        onOpen: function () {

        },
        onPause: function () {
            _clear();
        }
    });

    //是否开启预览视频
    var previewVideo = false;

    /**
     * 初始化定位参数
     * @private
     */
    function _init() {
        highlightFeatureOpacity = _locConfig.locateOpacity;
        locStyle = _locConfig.locateStyle;
        layer.config();
        //解析配置
        if (_locConfig.hasOwnProperty("videoSwitch")) {
            previewVideo = _locConfig.videoSwitch === "on" || _locConfig.videoSwitch == true;
        }
        if (_locConfig.hasOwnProperty("design")) {
            design=_locConfig.design;
        }
        if (esriLang.isDefined(_locConfig.showInfo)) {
            _showInfo = _locConfig.showInfo;
        }
        if (esriLang.isDefined(_locConfig.showResult)) {
            _showResult = _locConfig.showResult;
        }
        if(esriLang.isDefined(_locConfig.flash)){
            _flash=_locConfig.flash
        }
        if(esriLang.isDefined(_locConfig.fillColor)){
            fillColor=_locConfig.fillColor
        }
        if(esriLang.isDefined(_locConfig.lineColor)){
            lineColor=_locConfig.lineColor
        }
        if(esriLang.isDefined(_locConfig.duration)){
            duration=_locConfig.duration
        }
        EventBus.listener(EventBus.MAIN_MAP_INIT_EXTENT_SET, function () {
            setTimeout(prepare, 3000);
        });
        MapUtils.setMap(_map);
    }

    var locLyr = null;
    var locGras = [];
    //是否是报件图形
    var isReport = false;

    /***
     * ready for location
     */
    function prepare() {
        log("开始定位...");
        locPostParams = decodeURIComponent(locPostParams);
        if (locPostParams || urlParams.action == "location") {
            var locParams = $.parseJSON(locPostParams || urlParams.params);
            locType = locParams.type;
            var p = locParams.params;
            if (esriLang.isDefined(p.showInfo))
                _showInfo = p.showInfo;
            switch (locType) {
                case 'layerLocation':
                    layerAlias = p.layerAlias;
                    where = p.where;
                    doLayerLocation();
                    break;
                case 'urlLocation':
                    //todo...
                    break;
                case 'coordsLocation':
                    var coords = p;
                    doCoordsLocation(coords);
                    break;
            }
        }
        //地址参数中增加isReport用来区分是否可以进行报件检查分析
        if (esriLang.isDefined(urlParams.isReport)) {
            isReport = true;
        }
    }

    /**
     * 坐标定位
     * @param coords
     */
    function doCoordsLocation(coords) {
        var featuresObj = JsonConverters.toEsri(coords, _map.spatialReference);
        var gras = [];
        if (featuresObj.hasOwnProperty("features") && lang.isArray(featuresObj.features)) {
            arrayUtil.forEach(featuresObj.features, function (feature) {
                var graphic = new Graphic(feature);
                gras.push(graphic);
            });
        } else if (featuresObj.hasOwnProperty("geometry")) {
            var graphic = new Graphic({geometry: featuresObj.geometry});
            gras.push(graphic);
        } else if (featuresObj.hasOwnProperty("rings")) {
            var graphic = new Graphic({geometry: featuresObj});
            gras.push(graphic);
        }

        if(_flash===true){
        var g = gras[0];
        var geo = g.geometry;
        var geoType = geo.type;
        var geoCenter;
        switch (geoType) {
            case 'point':
                geoCenter = geo;
                break;
            case 'polyline':
            case 'polygon':
                geoCenter = geo.getExtent().getCenter();
                break;
        }
        geoCenter = lang.mixin(geoCenter, {spatialReference: _map.spatialReference});
        var graphic = new Graphic(lang.mixin(geo, {spatialReference: _map.spatialReference}));
        graphic.setAttributes(g.attributes);
        var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.solid, new Color(lineColor), 2),
            new Color(fillColor));
        graphic.setSymbol(fillSymbol);
        var graphicsLyr = _map.graphics;
        graphicsLyr.add(graphic);
        graphicsLyr.show();
        setTimeout(function () {
            MapUtils.flashFeatures([graphic], 500, null, duration);
        }, 500);
        var locSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH, new Color(lineColor), 2),
            new Color(fillColor));
            arrayUtil.forEach(gras, function (item) {
                item.setSymbol(locSymbol)
            });
            MapUtils.highlightFeatures_TZ(gras);
        }else if(design===true){
            MapUtils.highlightFeaturesWithStyle(gras,locStyle);
        }else {
        MapUtils.highlightFeaturesWithSymbol('point', gras,
            new SimpleMarkerSymbol('circle', 12, null, new Color([255, 0, 0, 1])));}
        MapUtils.locateFeatures(gras, 3);
    }

    /***
     * 图层定位
     */
    function doLayerLocation() {
        var idx = layer.msg("地图定位中..", {time: 10 * 1000});
        if (layerAlias === undefined || layerAlias === "") {
            layer.msg('定位的图层别名未设置!', {icon: 0, time: 1200});
            return;
        }
        locLyr = getConfigLayer(layerAlias);
        if (!esriLang.isDefined(locLyr)) {
            layer.msg(esriLang.substitute({alias: layerAlias}, "未找到【${alias}】相关配置!"), {icon: 0, time: 1200});
            return;
        }
        var mLyr = getActualLayer(locLyr.serviceId);
        if (esriLang.isDefined(mLyr) && mLyr.hasOwnProperty("url")) {
            var url = mLyr.url + "/" + locLyr.layerIndex;
            var _of = getOutFields(locLyr.returnFields);
            QueryTask.deferredQuery(url, where, null, _of, _map).then(function (result) {
                layer.close(idx);
                if (result.featureSet) {
                    locGras = [];
                    var fs = result.featureSet.features;
                    locatedFeatures = result.featureSet.features;
                    if (fs != null && fs.length > 0) {
                        var zmj = 0.0;
                        var nyd = 0.0;
                        var gd = 0.0;
                        var wlyd = 0.0;
                        var jsyd = 0.0;
                        for (var i = 0; i < fs.length; i++) {
                            var t = fs[i];
                            if (t.attributes.hasOwnProperty("PZZMJ")) {
                                zmj += ((t.attributes["PZZMJ"] == null || t.attributes["PZZMJ"] == "") ? 0.0 : t.attributes["PZZMJ"]);
                            }
                            if (t.attributes.hasOwnProperty("NYD")) {
                                nyd += ((t.attributes["NYD"] == null || t.attributes["NYD"] == "") ? 0.0 : t.attributes["NYD"]);
                            }
                            if (t.attributes.hasOwnProperty("QZGD")) {
                                gd += ((t.attributes["QZGD"] == null || t.attributes["QZGD"] == "") ? 0.0 : t.attributes["QZGD"]);
                            }
                            if (t.attributes.hasOwnProperty("WLYD")) {
                                wlyd += ((t.attributes["WLYD"] == null || t.attributes["WLYD"] == "") ? 0.0 : t.attributes["WLYD"]);
                            }
                            if (t.attributes.hasOwnProperty("JSYD")) {
                                jsyd += ((t.attributes["JSYD"] == null || t.attributes["JSYD"] == "") ? 0.0 : t.attributes["JSYD"]);
                            }
                            /* if (t.attributes.hasOwnProperty("XZDWMJ")){
                                 zmj+=((t.attributes["XZDWMJ"]==null||t.attributes["XZDWMJ"]=="")? 0.0:t.attributes["XZDWMJ"]);
                             }
                             if (t.attributes.hasOwnProperty("LXDWMJ")){
                                 nyd+=((t.attributes["LXDWMJ"]==null||t.attributes["NYD"]=="")? 0.0:t.attributes["LXDWMJ"]);
                             }
                             if (t.attributes.hasOwnProperty("TKMJ")){
                                 gd+=((t.attributes["TKMJ"]==null||t.attributes["TKMJ"]=="")? 0.0:t.attributes["TKMJ"]);
                             }
                             if (t.attributes.hasOwnProperty("TBDLMJ")){
                                 wlyd+=((t.attributes["TBDLMJ"]==null||t.attributes["TBDLMJ"]=="")? 0.0:t.attributes["TBDLMJ"]);
                             }
                             if (t.attributes.hasOwnProperty("TBDLMJ")){
                                 jsyd+=((t.attributes["TBDLMJ"]==null||t.attributes["TBDLMJ"]=="")? 0.0:t.attributes["TBDLMJ"]);
                             }*/
                            var g = new Graphic(t.geometry, null, t.attributes, null);
                            locGras.push(g);
                        }
                        if (zmj != 0.0 || nyd != 0.0 || gd != 0.0 || wlyd != 0.0 || jsyd != 0.0) {
                            var mapArea = {};
                            mapArea.area = zmj;
                            mapArea.nydArea = nyd;
                            mapArea.gdArea = gd;
                            mapArea.jsydArea = jsyd;
                            mapArea.wlydArea = wlyd;
                            //发送到共享数据
                            geoDataStore.push(GeoDataStore.SK_IDENTIFY, lang.clone(mapArea));
                        }
                        if(_flash===true){
                        var gi = locGras[0];
                        var geo = gi.geometry;
                        var geoType = geo.type;
                        var geoCenter;
                        switch (geoType) {
                            case 'point':
                                geoCenter = geo;
                                break;
                            case 'polyline':
                            case 'polygon':
                                geoCenter = geo.getExtent().getCenter();
                                break;
                        }
                        geoCenter = lang.mixin(geoCenter, {spatialReference: _map.spatialReference});
                        var graphic = new Graphic(lang.mixin(geo, {spatialReference: _map.spatialReference}));
                        graphic.setAttributes(gi.attributes);
                        var fillSymbol = EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                            EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.solid, new Color(lineColor), 2),
                            new Color(fillColor));
                        graphic.setSymbol(fillSymbol);
                        var graphicsLyr = _map.graphics;
                        graphicsLyr.add(graphic);
                        graphicsLyr.show();
                        setTimeout(function () {
                            MapUtils.flashFeatures([graphic], 500, null, duration);
                        }, 500);
                            var locSymbol=EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,
                                EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH, new Color(lineColor), 2),
                                new Color(fillColor));
                            arrayUtil.forEach(locGras, function (item) {
                                item.setSymbol(locSymbol)
                            });
                            MapUtils.highlightFeatures_TZ(locGras);
                        }else{
                        MapUtils.highlightFeaturesWithStyle(locGras, locStyle, highlightFeatureOpacity);}
                        MapUtils.locateFeatures(locGras, 3);
                        setMapScale();
                        if (_showInfo === true || _showInfo === "on") {
                            showPopup(locGras[0]);
                        }
                        //添加单击graphic的监听
                        addGraListener();
                        //发送到共享数据
                        geoDataStore.push(GeoDataStore.SK_LOC, lang.clone(locGras));
                        //提示
                        layer.msg("定位成功!", {time: 1200});
                        log("定位成功");
                        //查找附近的监控并打开视频
                        if (previewVideo) findCameraNearBy(locGras);
                        if (_showResult && locGras.length > 1) {
                            //显示定位结果列表
                            showResultWin();
                        }
                    } else {
                        layer.msg('未定位到图形!', {time: 2000});
                    }
                } else {
                    layer.msg('未定位到图形!');
                }
            }, function (e) {
                layer.close(idx);
                layer.open({title: '提示', content: '定位异常:' + e.message});
                error('定位异常:' + e.message);
            });
        } else {
            layer.msg("未找到对应的定位图层[" + layerAlias + "]");
        }
    }

    /***
     * 设置地图比例尺
     */
    function setMapScale() {
        if (esriLang.isDefined(locLyr.scale)) {
            //如果定位结果要素数目大于1 则要比较定位要素的范围和设置的比例尺范围的大小
            if (locGras.length == 1) {
                _map.setScale(parseInt(locLyr.scale));
                MapUtils.centerFeatures(locGras);
            } else {
                var _extent = _map.extent;
                var scaleExtent = MapUtils.getExtentForScale(locLyr.scale);
                if (MapUtils.compareExtent(_extent, scaleExtent) < 0) {
                    _map.setScale(locLyr.scale);
                }
            }
        }
    }

    var _graClickHandle;

    /**
     * 单击要素显示popup
     */
    function addGraListener() {
        _graClickHandle = on(_map.graphics, 'click', function (evt) {
            var g = evt.graphic;
            showPopup(g);
        });
    }

    var winHeight = 200;

    /***
     *
     */
    function showResultWin() {
        //结果窗的高度
        winHeight = _map.height - $("#MenuBar").height() - $("#Navigation").height() - 20;
        if (locGras.length > 0) {
            //第一个字段作为标题字段
            //第二个字段作为副标题字段
            var fields = getOutFields(locLyr.returnFields);
            var titleField = fields[0];
            var subField = fields[1];
            //处理定位数据
            var renderData = [];
            arrayUtil.forEach(locGras, function (g) {
                var attr = g.attributes;
                var rf = locLyr.returnFields;
                for (var j in rf) {
                    if (rf[j].type == "DATE" && attr[rf[j].name]) {
                        attr[rf[j].name] = new Date(attr[rf[j].name]).toLocaleString();
                    }
                }
                var tmp = {};
                lang.mixin(tmp, {title: attr[titleField], subtitle: attr[subField]});
                tmp.graphic = g;
                tmp.uid = RandomUuid();
                renderData.push(tmp);
            });
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '<i class="fa fa-map-marker"></i>&nbsp;&nbsp;定位',
                area: ['320px', winHeight + 'px'],
                content: ResultTpl,
                offset: getWinOffset(),
                shade: 0,
                shadeClose: false,
                closeBtn: 0,
                maxmin: true,
                success: function (layero, index) {
                    //控制报件检查的显隐
                    if (!isReport) $(".loc-func-list").find("li").eq(1).remove();
                    //动态控制结果列表的显示高度
                    $('.loc-result-list').height(winHeight - $(".loc-func-list").height() - 135);
                    //加载定位结果列表
                    var listDataRenderer = new ListDataRenderer({
                        renderTo: $('.loc-result-list'),
                        type: "loc",
                        map: _map,
                        renderData: renderData
                    });
                    listDataRenderer.on('location', function (data) {
                        var g = data.graphic;
                        MapUtils.highlightFeatures([g], false, highlightFeatureOpacity);
                        MapUtils.locateFeatures([g], 4);
                        showPopup(g);
                    });
                    listDataRenderer.render();
                    //添加各功能事件监听
                    $(".loc-func-list").find("a").on('click', lang.hitch(this, exeFunc));
                }
            });

        }

    }

    /***
     * 执行功能
     */
    function exeFunc(evt) {
        var type = $(evt.currentTarget).data("type");
        switch (type) {
            case 'analysis':
                break;
            case 'report':
                break;
            case 'export':
                if (locatedFeatures != undefined && locatedFeatures.length > 0)
                    exportAfterLocation(locatedFeatures);
                break;
        }
    }


    /**
     * 定位成功后导出图形
     * @param gras
     */
    function exportAfterLocation(gras) {
        var featureSet = new FeatureSet();
        featureSet.features = gras;
        var content = '<select class="form-control input-sm">' +
            '<option value="txt">txt文件(*.txt)</option>' +
            '<option value="xls">excel文件(*.xls)</option>' +
            '<option value="cad">cad文件(*.dwg)</option>' +
            '<option value="shp">shapfile压缩包(*.zip)</option>' +
            '</select>';
        layer.open({
            title: '选择导出格式',
            content: content,
            area: '300px',
            yes: function (index, layero) {
                var type = $(layero).find('select').val();
                layer.close(index);
                var locatedGeometryIO = new GeometryIO();
                locatedGeometryIO.expToFile(featureSet, type);
            }
        });
    }


    /***
     * 计算结果窗口的坐标位置
     * @returns {*[]}
     */
    function getWinOffset() {
        var clientHeight = document.body.clientHeight;
        var clientWidth = document.body.clientWidth;
        var left = clientWidth - 320 - 15;
        var top = (clientHeight - winHeight) / 2;
        return [top + 'px', left + 'px'];
    }


    /***
     * 显示地图弹出窗
     * @param g
     */
    function showPopup(g) {
        if (mapPopup.isShowing) mapPopup.closePopup();
        for (var i in g.attributes) {
            if (!g.attributes[i]) {
                continue;
            }
            var item = JSON.stringify(g.attributes[i]);
            if (item.startWith("1") && item.endWith("000")) {
                var t = new Date(g.attributes[i]);
                g.attributes[i] = t.toLocaleString();
            }
        }
        mapPopup.setData(g.attributes);
        mapPopup.setFields(locLyr.returnFields);
        mapPopup.setTitle(layerAlias);
        mapPopup.setSecLink(locLyr.secLink);
        if (locLyr.hasOwnProperty("showFooter")) {
            var showFooter = locLyr.showFooter;
            if (showFooter) mapPopup.setBLinkEnabled(showFooter);
        }
        mapPopup.openPopup(g.geometry.getExtent().getCenter());

    }

    /***
     * 组织返回字段
     * @param fields
     * @returns {string}
     */
    function getOutFields(fields) {
        var r = [];
        $.each(fields, function (idx, item) {
            r.push(item.name);
        });
        return r;
    }

    /***
     * 根据url中的图层别名 获取配置信息
     * @param alias
     */
    function getConfigLayer(alias) {
        if (_locConfig.layers == undefined) {
            layer.msg('未配置任何定位图层!请检查配置', {icon: 0, time: 1000});
            return;
        }
        var lyrs = _locConfig.layers;
        for (var i = 0, l = lyrs.length; i < l; i++) {
            if (lyrs[i].layerAlias === alias) return lyrs[i];
        }
    }

    /***
     * 获取服务图层 地图中/配置中
     * @param sId
     */
    function getActualLayer(sId) {
        var mapLyr = _map.getLayer(sId);
        if (!esriLang.isDefined(mapLyr)) {
            var arr = arrayUtil.filter(operaLayers, function (item) {
                return item.id == sId;
            });
            if (lang.isArray(arr) && arr.length > 0)
                mapLyr = arr[0];
        }
        return mapLyr;
    }


    /***
     *缓冲查找附近的监控点 并预览视频
     * @param gras
     */
    function findCameraNearBy(gras) {
        var graphic = gras[0];
        var geometry = JsonConverters.toGeoJson(graphic);
        $.ajax({
            'url': root + '/video/fetch/poi',
            'method': 'post',
            data: {
                'geometry': JSON.stringify(geometry),
                'inSR': _map.spatialReference.wkid,
                'bufferSize': BUFFER_DISTANCE
            },
            success: function (cameras) {
                if (cameras.length == 0) {
                    layer.msg("此地块附近无监控点信息！")
                } else if (cameras.length > 4) {
                    cameras = arrayUtil.filter(cameras, function (item, idx) {
                        return idx < 4;
                    });
                    log("出于网络传输速率考虑，一次最多支持同时预览4个!");
                    VideoManager.getInstance().callVideo(0, cameras);
                }
            }
        });
    }

    /**
     *
     * @private
     */
    function _clear() {
        if (_graClickHandle != undefined)
            _graClickHandle.remove();
        if (mapPopup.isShowing) mapPopup.closePopup();
    }

    return me;
});
