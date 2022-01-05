/**
 * 地图交互
 */

// 声明地图对象
let map = L.map('map', {
    maxZoom: 22,      // 最大缩放
    minZoom: 10,      // 最小缩放
    zoomControl: false,
    attributionControl: false,
}).setView([31.75055692, 119.63408421], 18);    // 初始化地图中心点

// 缩放
L.control.zoom({
    zoomInTitle: '放大',
    zoomOutTitle: '缩小'
}).addTo(map);

// 全图显示
let fullMapControl = L.control.fullMap({
    position: 'topleft'
}).addTo(map);

// 坐标
L.control.coords().addTo(map);

// 比例尺
L.control.scaleBar().addTo(map);

// 获取html传参
let slbh = UrlParams.param('slbh');
let ywlx = UrlParams.param('ywlx');
console.log('受理编号：', slbh);
console.log('业务类型：', ywlx);

const FIELD_SLBH = "SLBH";  // 受理编号字段名称
let businessUrl, baseUrl, imageUrl, addErrorUrl, layerConfig;
let mapConfig = MapUtils.readConfig(ywlx);  // 读取配置文件获取地图服务配置
businessUrl = mapConfig.businessUrl;        // 业务数据地图服务Url
baseUrl = mapConfig.baseUrl;                // 基础数据地图服务Url
imageUrl = mapConfig.imageUrl;              // 影像数据地图服务Url
addErrorUrl = mapConfig.addErrorUrl;        // 新增错误接口Url
layerConfig = mapConfig.layerConfig;        // 图层配置
console.log("MapConfig --> ", mapConfig);
if (imageUrl) {
    // 添加影像服务
    L.esri.dynamicMapLayer({
        pan: "tilePane",
        url: imageUrl,
        zIndex: -1000,
        useCors: false
    }).addTo(map);
}

if (!businessUrl) {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.msg('未进行业务数据地图服务Url配置。');
        return;
    });
}
// 添加基础数据图层
let baseLayer = L.esri.dynamicMapLayer({
    url: baseUrl,
    pan: "overlayPane",
    useCors: false
}).addTo(map);

// 添加业务数据图层
let businessLayer = L.esri.dynamicMapLayer({
    url: businessUrl,
    pan: "overlayPane",
    zIndex: 1000,
    useCors: false,
});

/**
 * 根据图层英文名称获取业务图层的中文名称
 * @param layerName 图层英文名称
 * @returns {string} 图层中文名称
 */
function getTCZWMC(tcmc) {
    if (layerConfig && tcmc) {
        for (let item in layerConfig) {
            if (MapUtils.equalsIgnoreCase(tcmc, layerConfig[item])) {
                return item;
            }
        }
    }
}

/**
 * 根据图层名称获取业务图层的英文名称
 * @param layerName 图层中文名称
 * @returns {string} 图层英文名称
 */
function getTCMC(tczwmc) {
    if (layerConfig && tczwmc) {
        for (let item in layerConfig) {
            if (MapUtils.equalsIgnoreCase(tczwmc, item)) {
                return layerConfig[item];
            }
        }
    }
}

// 获取图层列表
let allLayers = [];
let layers = [];
// 同步获取所有业务图层及基础图层
Promise.all([MapUtils.getMetadataPromise(businessLayer), MapUtils.getMetadataPromise(baseLayer)]).then(function (values) {
    if (!values) {
        return;
    }

    let queryBoundsPromises = [];       // 查询边界Promise对象数组
    let businessMetadata = values[0];   // 业务数据元数据
    let baseMetadata = values[1];       // 基础数据元数据

    /**
     * 添加动态图层子项信息
     * @param metadata  动态图层元数据
     * @param dynamicLayer  动态图层对象
     * @param dynamicUrl   动态图层Url
     * @param dynamicLayerName 动态图层类型名
     */
    function pushLayerItems(metadata, dynamicLayer, dynamicUrl, dynamicLayerName) {
        let subLayers = [];
        metadata.layers.forEach(function (layer) {
            let layerId = layer.id;
            let layerName = layer.name;
            if (dynamicLayerName == "业务数据" && layerConfig && !getTCMC(layerName)) {
                return true;            // 如果配置了业务类型，且该图层不属于该业务类型则跳过
            }
            allLayers.push({
                "ServiceUrl": dynamicUrl,
                "LayerId": layerId,
                "LayerName": layerName
            });
            subLayers.push({layerId: layerId, layerName: layerName});
        });
        layers.push({
            layerName: dynamicLayerName,        // 动态图层类型名称
            layer: dynamicLayer,                // 动态图层对象
            subLayers: subLayers                // 动态图层子图层数组
        });
    }

    if (businessMetadata) {
        let layerIds = [];
        let where = slbh ? FIELD_SLBH + "='" + slbh + "'" : "";

        // 获取业务数据各图层信息
        pushLayerItems(businessMetadata, businessLayer, businessUrl, "业务数据");

        businessMetadata.layers.forEach(function (layer) {
            let url = businessUrl + '/' + layer.id;
            if (layerConfig && !getTCMC(layer.name)) {
                return true;        // 如果配置了业务类型，且该图层不属于该业务类型则跳过
            }

            layerIds.push(layer.id);
            queryBoundsPromises.push(MapUtils.queryBoundsPromise(url, where));  // 查询边界QueryBounds Promise对象数组添加
        });
        if (layerIds && layerIds.length > 0) {
            businessLayer.setLayers(layerIds);      // 配置的图层可见
        }
        businessLayer.addTo(map);       // 配置完成再添加至地图
    }
    if (baseMetadata) {
        // 获取基础数据各图层信息
        pushLayerItems(baseMetadata, baseLayer, baseUrl, "基础数据");
    }
    if (queryBoundsPromises) {
        // 同步获取所有图层的边界，并将结果进行合并
        Promise.all(queryBoundsPromises).then(function (results) {
                if (results && results.length > 0) {
                    let bounds = L.latLngBounds([]);
                    results.forEach(function (layerBounds) {
                        if (layerBounds && layerBounds.isValid()) {
                            bounds.extend(layerBounds);     // 合并每个图层的边界
                        }
                    });
                    if (bounds.isValid()) {           // 查询的结果是否有效
                        fullMapControl.updateBounds(bounds);    // 更新全图控件中的地图边界
                        fullMapControl.zoomToFullMap();     // 缩放至全图
                    }
                }
            }
        );
    }
    console.log('All Layers --> ', allLayers);
    console.log('Layer Control --> ', layers);

    // 图层控制
    L.control.layerControl(layers, {
        position: "topright"
    }).addTo(map);

    // 属性识别按钮
    L.control.identify(businessLayer, baseLayer, allLayers, {
        position: 'topright',
    }).addTo(map);

    // 选择图形新增错误
    L.control.addError(businessLayer, allLayers.filter(function (item) {
        return item.ServiceUrl == businessUrl;      // 只需要对业务数据进行错误添加
    }), slbh, {
        position: 'topright',
        interfaceUrl: addErrorUrl       // 新增错误接口
    }).addTo(map);
});

// 监听并接收传递的消息
window.addEventListener('message', function (e) {
    console.log("要素定位：", e.data);
    layui.use('layer', function () {
        // 根据传递的参数进行图层要素定位
        let layer = layui.layer,
            data = e.data,
            tcmc, where;
        if (data && (tcmc = data.tcmc) && (where = data.where)) {
            // 传入的为图层英文名称，需要进行转换
            let tczwmc = getTCZWMC(tcmc);
            if (!tczwmc) {
                layer.msg('错误要素图层名称获取失败，无法定位。');
                return;
            }

            // 根据图层中文名称找到地图中的图层url及id
            if (allLayers.length > 0) {
                let url, layerId;
                for (let i = 0, len = allLayers.length; i < len; i++) {
                    let layerInfo = allLayers[i];
                    if (MapUtils.equalsIgnoreCase(layerInfo.LayerName, tczwmc)) {
                        url = layerInfo.ServiceUrl;
                        layerId = layerInfo.LayerId;
                        break;
                    }
                }

                // if (url && layerId)      // 之前是这样写的，如果layerId为0的时候，条件一直是false，无法进入到下面
                if (url && layerId != undefined) {
                    // 图层要素定位
                    let layerUrl = url + '/' + layerId;
                    MapUtils.locateFeature(map, layerUrl, where, function (count, error) {
                        if (count == -1) {  // 定位失败
                            MapUtils.clearHighLightFeature(map);    // 清空高亮要素
                            layer.msg('定位失败' + (error ? '，' + error.message : '') + '。');
                        } else if (count == 0) {
                            layer.msg('无当前记录要素。');
                        }
                    });
                }
            }
        }
    });
}, false);