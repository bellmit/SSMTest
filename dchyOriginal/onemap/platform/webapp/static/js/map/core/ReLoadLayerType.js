/**
 *动静态切换
 *针对土地利用现状
 **/

define(['map/MainMap','leaflet','mapUtils'], function (MainMap,L,MapUtils){

    // 动态服务类型
    var REST_LAYER = 'ags_rest';
    // 静态服务类型
    var TILE_LAYER = 'ags_tile';
    // 所有生成的图层
    var $that, layerAll = [];

    var ReLoadLayerType = {
        //主地图
        _map: {},
        //动态图层
        _restLayer: {},
        //静态图层
        _tileLayer: {},
        // 图层参数
        // layer _capables
        _capables: '',
        // tileLayers
        // _tileLayers: '',
        //所有加载图层的节点，为了实时更新图层切换后的图层，方便删除
        _nodes:[],

        _serLayers: null,

        // 所有勾选的土地利用图层
        _capablesAll: [],

        //设置土地利用图层的属性
        // setOptions: function (options, capables, tileLayer, node) {
        //     debugger;
        //     $that = this;
        //     $that._capables = capables;
        //     $that._capables._optionsLyr = options;
        //     //控制不进行动静态切换的属性
        //     $that._capables.hasLayer = false;
        //     //将配置图层增加至全部的勾选的土地利用图层
        //     $that._capablesAll.push($that._capables);
        //     //将配置图层的node节点记录
        //     $that._nodes.push(node);
        //     $that._tileLayer = tileLayer;
        //     //因为list加载顺序的问题，在这里直接帮图层配置
        //     capables.restId = tileLayer._leaflet_id;
        //     layerAll.push(tileLayer);
        //     $that._serLayers = L.layerGroup(layerAll);
        // },

        // 根据id 删除被移除的图层的相关记录
        removeResetLayer: function (capables) {
            var restId = capables.restId;
            //自己写findIndex方法兼容ie8
            var index = $that._findLayerIndex(layerAll,restId);
            layerAll.splice(index, 1);

            index = $that._findLayerIndex($that._capablesAll,restId);
            delete(capables["restId"]);

            var nodeCount = $that._findNodeIndex($that._nodes,restId);

            $that._nodes.splice(nodeCount,1);

            $that._capablesAll.splice(index, 1);
        },

        //转换为动态图层
        _setRestLayer:function (capables) {
            var options = capables._optionsLyr;

            options.type = REST_LAYER;

            $that._restLayer = MapUtils.createMapLayer(options);
        },

        //转换为静态图层
        _setTileLayer:function (capables) {

            var options = capables._optionsLyr;

            options.type = TILE_LAYER;

            $that._tileLayer = MapUtils.createMapLayer(options);
        },

        //兼容ie8的findIndex
        _findLayerIndex:function(arrs,restId){
            var index = 0;

            for(var i = 0;i<arrs.length;i++){
                if (arrs[i]._leaflet_id !== restId && arrs[i]._leaflet_id !== undefined) {
                    index++;
                } else if(arrs[i].restId !== restId && arrs[i].restId !== undefined) {
                    index++;
                }else{
                    break;
                }
            }

            return index;
        },

        //兼容ie8的findIndex
        _findNodeIndex:function(arrs,restId){
            var index = 0;
            for(var i = 0;i<arrs.length;i++) {
                if (arrs[i].capables[0].restId !== restId && arrs[i].capables[0].restId !== undefined) {
                    index++;
                }else{
                    break;
                }
            }
            return index;
        },

        //给外界调用的接口
        changeLayerType:function() {
            if ($that._capablesAll.length > 0) {
                $that._capablesAll.forEach(function(capables){
                    $that.changeLayerReLoadType(capables);
                })
            }
        },

        //主要的更改方法
        changeLayerReLoadType:function (capables) {

            $that._map = MainMap.getBaseMap();
            // 获取最大层级
            var maxZoom = $that._map.getMaxZoom();

            var nodeCount = 0;

            var zoom = $that._map.getZoom();
            // 倒数三级进行切换
            if (zoom > maxZoom - 3 && !capables.hasLayer) {
                $that._setRestLayer(capables);

                // 判断坐标系添加
                if (capables.hasOwnProperty('internalId')) {
                    // 根据id删除图层
                    var internalId = capables.internalId;

                    $that._map.removeLayer(capables._tileLayers.getLayer(internalId));

                    delete(capables["internalId"]);

                    var index = $that._findLayerIndex(layerAll,restId);

                    layerAll.splice(index, 1);

                } else if (capables.hasOwnProperty('restId')) {

                    var restId = capables.restId;

                    $that._map.removeLayer($that._serLayers.getLayer(restId));

                    delete(capables["restId"]);
                    // 根数据据id 删除数组中的数据
                    var index = $that._findLayerIndex(layerAll,restId);

                    nodeCount = $that._findNodeIndex($that._nodes,restId);

                    layerAll.splice(index, 1);
                }
                // 添加地图
                if (!capables.hasOwnProperty('restId')) {
                    // 如果不进行vue html渲染不要使用$set

                    var index = $that._findLayerIndex(layerAll,restId);

                    capables.restId = $that._restLayer._leaflet_id;

                    layerAll.push($that._restLayer);

                    // 使用layerGroup是为了在getlayerId的时候获取
                    $that._serLayers = L.layerGroup(layerAll);

                    $that._map.addLayer($that._restLayer);

                    $that._nodes[nodeCount].layer = $that._restLayer;

                    capables.hasLayer = true;

                }
            } else if (zoom < maxZoom - 2 && capables.hasLayer) {

                capables.hasLayer = false;

                if (capables.hasOwnProperty('restId')) {

                    var restId = capables.restId;
                    // 根据id删除图层
                    $that._map.removeLayer($that._serLayers.getLayer(restId));

                    delete(capables["restId"]);

                    var index = $that._findLayerIndex(layerAll,restId);

                    nodeCount = $that._findNodeIndex($that._nodes,restId);

                    layerAll.splice(index, 1);
                }

                $that._setTileLayer(capables);
                // 添加地图
                capables.restId = $that._tileLayer._leaflet_id;

                layerAll.push($that._tileLayer);

                $that._serLayers = L.layerGroup(layerAll);

                $that._map.addLayer($that._tileLayer);

                $that._nodes[nodeCount].layer = $that._tileLayer;

            }
        }
    };
    return ReLoadLayerType;
});