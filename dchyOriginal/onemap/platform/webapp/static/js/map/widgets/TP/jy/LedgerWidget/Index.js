/**
 * 江阴订制功能 台帐显示模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/15 10:14
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "dojo/on",
    "esri/graphic",
    "esri/tasks/IdentifyTask",
    "esri/tasks/IdentifyParameters",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/utils/MapUtils",
    "map/core/QueryTask",
    "mustache",
    "layer",
    "slimScroll",
    "ztreecore",
    "ztreeedit",
    "ztreecheck"],function (declare,lang,arrayUtil,topic,on,Graphic,IdentifyTask,IdentifyParameters,BaseWidget,Environment,MapUtils,QueryTask,Mustache) {

    var me=declare([BaseWidget],{

        onCreate: function () {
            _ledgerConfig=this.getConfig();
            _init();
        },
        onOpen: function () {
            _resume();
        },
        onPause: function () {
            _pause();
        },
        onDestroy:function(){
            _destroy()
        }
    });

    var ledgerData=[],panelHeight=300,iframeFuncName="locationProj";
    var ledgerTree;
    var isActive=false;     //是否已打开相应的台帐
    var currentId=undefined;//当前选中的类别关联的服务id
    var _map, mapClickHandler,_ledgerConfig;
    /***
     * 初始化台帐列表
     * @private
     */
    function _init() {
        layer.config();
        _map= map.map();
        MapUtils.setMap(_map);
        if (_ledgerConfig.height)
            panelHeight = _ledgerConfig.height;
        if (_ledgerConfig.iframeFunc)
            iframeFuncName = _ledgerConfig.iframeFunc;
        if (lang.isArray(_ledgerConfig.data)) {
            ledgerData = _ledgerConfig.data;
        } else
            layer.msg("未配置台帐数据!",{icon:0});
        //初始化tree
        if(ledgerData.length>0){
            var treeHeight=$(window).height()-130;
            $(".ledgerContainer").height(treeHeight);
            var setting = {
                view: {
                    selectedMulti: false
                },
                data: {
                    key:{
                        url:"noUrl"
                    }
                },
                callback: {
                    beforeClick: zTreeBeforeClick,
                    onClick: zTreeOnClick
                }
            };

            $(".ledgerContainer").slimScroll({
                height: treeHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
            $.fn.zTree.init($("#ledgerTree"), setting, ledgerData);
            ledgerTree= $.fn.zTree.getZTreeObj("ledgerTree");
            ledgerTree.expandAll(true);//默认展开全部

            //监听iframe中的方法
            EventBus.listener("callMap",function(evt,data){
                _doLocation(data);
            });
        }
    }

    /***
     * 父节点不作操作
     * @param treeId
     * @param treeNode
     * @param clickFlag
     * @returns {boolean}
     */
    function zTreeBeforeClick(treeId, treeNode, clickFlag) {
        return (treeNode.isParent !== true);
    }

    /***
     * 叶子节点打开台帐
     * @param event
     * @param treeId
     * @param treeNode
     */
    function zTreeOnClick(event, treeId, treeNode) {
        showLedger(treeNode);
    }

    /***
     * 显示台帐
     * @param node
     */
    function showLedger(node){
        if(node!=undefined){
            currentId=node.sId;
            //打开相应的地图服务
            if (map.map().getLayer(currentId) == null) {
                var service = _getService(currentId);
                if (service.visible === false)
                    service.visible = true;
                map.addLayer(service, -1);
            } else
                _changeVisible(currentId, true);

            //显示相应的台账
            var iframeSrc=node.url;
            var html=$("#contentTpl").html();
            Mustache.parse(html);
            topic.publish("result/show",{content:Mustache.render(html,{url:iframeSrc}),height:panelHeight});
            isActive=true;
            _addMapListener();
        }

    }
    /***
     * 增加identify事件监听
     * @private
     */
    function _addMapListener(){
        mapClickHandler=_map.on('click',lang.hitch(this,_identify));
    }

    /**
     * 属性识别要素 以进行关联
     * @param event
     * @private
     */
    function _identify(event) {
        var id = arrayUtil.filter(_map.layerIds, function (item) {
            return item === currentId;
        })[0];
        if(id!==undefined){
            var identifyParams = new IdentifyParameters();
            identifyParams.tolerance = 3;
            identifyParams.returnGeometry = true;
            identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
            identifyParams.width = _map.width;
            identifyParams.height = _map.height;
            identifyParams.geometry = event.mapPoint;
            identifyParams.mapExtent = _map.extent;
                var identifyTask = new IdentifyTask(_map.getLayer(currentId).url);
                on.once(identifyTask, 'error', _identifyResultError);
                identifyTask.execute(identifyParams, lang.hitch(this, _handleIdentifyResult));
        }
    }

    /**
     * 处理识别结果 调用iframe中的方法
     * @param event
     * @param result
     * @private
     */
    function _handleIdentifyResult(result) {
        if (result != null && result.length > 0) {
            var features = [];
            var param="";
            arrayUtil.forEach(result, function (item,idx) {
                if (item.feature){
                    var f = item.feature;
                    features.push(f);
                    var property = f.attributes;
                    var idVal = property["地块ID"] ? property["地块ID"] : property["项目ID"];
                    if (idVal !== undefined)
                        param += idVal;
                    if (idx < result.length - 1)
                        param += ",";
                }
            });
            if (features.length > 0) {
                MapUtils.highlightFeatures(features, false);
                MapUtils.locateFeatures(features);
            }
            //将组织好的参数传递给iframe中的js方法
            if(param!=""){
                ledgerIframe.window[iframeFuncName](param);
            }
        }
    }

    /***
     * 实现由台帐定位图形
     * @param param
     */
    function _doLocation(param){
        if(param.layerAlias!==undefined&&param.where){
            var lyrUrl=MapUtils.getLayerUrlByAlias(param.layerAlias);
            layer.msg("地图定位中..", {time: 10 * 1000});
            QueryTask.deferredQuery(lyrUrl, param.where, null, null, _map).then(function(result){
                layer.closeAll();
                if (result.featureSet) {
                    var fs = result.featureSet.features;
                    if (fs != null && fs.length > 0) {
                        MapUtils.highlightFeatures(fs,false);
                        MapUtils.locateFeatures(fs,1.5);
                        layer.msg("定位成功!",{time:1200});
                    }
                }
            },function(err){
                layer.closeAll();
                layer.open({title: '提示', content: '定位异常:' + error.message});
            });

        }
    }

    /**
     * 属性识别异常
     * @param error
     * @private
     */
    function _identifyResultError(error) {
        console.error(error.message);
    }
    /**
     * 获取配置的地图服务
     * @param id
     * @returns {*}
     * @private
     */
    function _getService(id) {
        var operaLayers = appConfig.map.operationalLayers;
        for (var i in operaLayers) {
            var s = operaLayers[i];
            if (s.id === id) {
                return s;
            }
        }
    }
    /***
     * 改变已加载的服务的可见性
     * @param id
     * @param visible
     * @private
     */
    function _changeVisible(id, visible) {
        var lyr = map.map().getLayer(id);
        if (lyr != undefined) {
            lyr.setVisibility(visible);
        }
    }
    /***
     * 切换时 隐藏下方的台帐面板
     * @private
     */
    function _pause() {
        if (isActive)
            topic.publish("result/shrink", null);
        if(mapClickHandler)
            mapClickHandler.remove();
        _map.graphics.clear();
    }

    /***
     *
     * @private
     */
    function _resume() {
        if (isActive)
            _addMapListener();
    }

    /**
     * 关闭选项卡时销毁台账dom和图层相关组件及操作
     * @private
     */
    function _destroy(){
        var operaLayers = appConfig.map.operationalLayers;
        $("#result-container .content").empty();
        $("#result-container").css("display", "none");
        if(mapClickHandler)
            mapClickHandler.remove();
        _map.graphics.clear();

        arrayUtil.forEach(ledgerData, function (item) {
            //依次清除每类台账下的叠加图层，不适用三级类方式
            if (item.children && item.children.length > 0) {
                arrayUtil.forEach(item.children, function (childItem) {
                    if (map.map().getLayer(childItem.sId) != null) {
                        map.removeLayerById(childItem.sId);
                    }
                })
            } else if (item.sId) {
                if (map.map().getLayer(item.sId) != null) {
                    map.removeLayerById(item.sId);
                }
            }
        });
    }

    return me;
});