MapUtils = {
    /**
     * 读取配置文件
     * @param ywlx 业务类型
     * @returns {{baseUrl: string, imageUrl: string, businessUrl: string, layerConfig: string[], addErrorUrl: string}}
     */
    readConfig: function (ywlx) {
        let mapConfig = {};
        $.ajax({
            type: 'get',
            url: 'config/mapService.json',
            async: false,
            dataType: 'json',
            success: function (data) {
                mapConfig = data;
                console.log("Map Service Config --> ", data);
            }
        });

        let businessUrl, baseUrl, imageUrl, addErrorUrl, layerConfig;
        if (mapConfig["BusinessServer"]) {
            businessUrl = mapConfig["BusinessServer"]["Url"];
        }

        if (mapConfig["BaseServer"]) {
            baseUrl = mapConfig["BaseServer"]["Url"];
        }

        if (mapConfig["ImageServer"]) {
            imageUrl = mapConfig["ImageServer"]["Url"];
        }

        if (mapConfig["AddError"]) {
            addErrorUrl = mapConfig["AddError"]["Url"];
        }

        if (ywlx && mapConfig["LayerConfig"] && mapConfig["LayerConfig"][ywlx]) {
            layerConfig = mapConfig["LayerConfig"][ywlx];
        }
        return {
            "businessUrl": businessUrl,
            "baseUrl": baseUrl,
            "imageUrl": imageUrl,
            "addErrorUrl": addErrorUrl,
            "layerConfig": layerConfig,
        }
    },

    /**
     * 获取图层元数据Promise
     * @param dynamicLayer
     * @returns {Promise<metadata>}
     */
    getMetadataPromise: function (dynamicLayer) {
        return new Promise(function (resolve) {
            if (dynamicLayer) {
                dynamicLayer.metadata(function (error, metadata) {
                    resolve(metadata);
                });
            }
        });
    },

    /**
     * 查询图层边界Promise
     * @param url
     * @param where
     * @returns {Promise<latLngBounds>}
     */
    queryBoundsPromise: function (url, where) {
        // 返回Promise代理
        return new Promise(function (resolve) {
            if (url) {
                let query = L.esri.query({url: url});
                if (where) {
                    query.where(where);     // 增加过滤条件
                }
                let geoJson = L.geoJSON();  // 创建一个GeoJson图层
                query.run(function (error, featureCollection) {
                    if (error) {
                        console.log(error);
                    }
                    // 循环查询结果要素集
                    if (featureCollection && featureCollection.features) {
                        featureCollection.features.forEach(function (feature) {
                            geoJson.addData(feature);   // GeoJson图层添加要素
                        });
                    }
                    // 返回查询结果要素范围
                    resolve(geoJson.getBounds());   // 使用L.geoJson().getBounds()计算范围
                });
            }
        });
    },

    /**
     * 要素定位
     * @param map 地图
     * @param url 图层url
     * @param where 过滤条件
     * @param callback 回调函数
     */
    locateFeature: function (map, url, where, callback) {
        if (map && url && where) {
            // 根据条件进行图层要素查询
            L.esri.query({url: url}).where(where).run(function (error, featureCollection) {
                if (error) {
                    callback(-1, error, featureCollection); // 回调函数
                    return;
                }
                // 循环查询结果要素集
                let count = 0;
                if (featureCollection && featureCollection.features && (count = featureCollection.features.length) > 0) {
                    // 先清除原有高亮要素
                    MapUtils.clearHighLightFeature(map);

                    // 循环要素集合
                    featureCollection.features.forEach(function (feature) {
                        MapUtils.highlightFeature(map, feature);    // 每个要素高亮显示
                    });

                    // 查询结果要素定位
                    let bounds = map.identifyLayer.getBounds(); // 获取识别图层边界
                    if (bounds && bounds.isValid()) {
                        MapUtils.fitBounds(map, bounds);      // 定位
                    }
                }

                callback(count, error, featureCollection);  // 回调函数
            });
        }
    },

    fitBounds: function (map, bounds) {
        if (bounds && bounds.isValid()) {
            let ne = bounds.getNorthEast(),
                sw = bounds.getSouthWest();
            ne = ne ? ne : sw;
            sw = sw ? sw : ne;
            if (ne.equals(sw)) {
                bounds.extend(L.latLng(ne.lat + 0.001, ne.lng + 0.001));
                bounds.extend(L.latLng(sw.lat - 0.001, sw.lng - 0.001));
            }
            return map.fitBounds(bounds);
        }
    },

    /**
     * 清除高亮要素
     * @param map 地图
     */
    clearHighLightFeature: function (map) {
        if (map && map.identifyLayer) {
            map.identifyLayer.clearLayers();    // 清除高亮图层中所有要素
        }
    },

    /**
     * 要素高亮显示
     * @param map 地图
     * @param feature 要素
     */
    highlightFeature: function (map, feature) {
        if (map) {
            if (!map.identifyLayer) {
                map.identifyLayer = L.geoJSON().addTo(map); // 添加一个空的GeoJson图层，用于添加高亮要素
            }

            if (feature) {
                map.identifyLayer.addData(feature); // GeoJson图层添加要素
                map.identifyLayer.setStyle({    // 设置高亮要素style样式
                    color: 'red'
                });
            }
        }
    },
    /**
     * 拼接图层Url
     * @param layerUrl 图层Url
     * @param layerId 图层Id
     * @returns {string|*}
     */
    concatUrl: function (layerUrl, layerId) {
        if (layerUrl) {
            if (layerUrl.substr(layerUrl.length - 1, 1) == "/") {
                return layerUrl + layerId;
            } else {
                return layerUrl + "/" + layerId;
            }
        }
    },
    /**
     * 忽略大小写判断字符串是否相等
     * @param str1
     * @param str2
     * @returns {boolean}
     */
    equalsIgnoreCase: function (str1, str2) {
        if (str1 == str2) {
            return true;
        }
        if (typeof (str1) === 'string' && typeof (str2) === 'string') {
            return str1.toLowerCase() == str2.toLowerCase();
        }
        return false;
    },
    /**
     * Url添加或更新参数
     * @param url
     * @param name
     * @param value
     * @returns {string}
     * @constructor
     */
    updateUrlParams: function (url, name, value) {
        let r = url;
        if (r != null && r != 'undefined' && r != "") {
            value = encodeURIComponent(value);
            let reg = new RegExp("(^|)" + name + "=([^&]*)(|$)");
            let tmp = name + "=" + value;
            if (url.match(reg) != null) {
                r = url.replace(eval(reg), tmp);
            } else {
                if (url.match("[\?]")) {
                    r = url + "&" + tmp;
                } else {
                    r = url + "?" + tmp;
                }
            }
        }
        return r;
    },

    /**
     * 创建一个leaflet的Button按钮
     * @param type 类型：img/span
     * @param className Dom元素的类名
     * @param container 容器
     * @param title 标题
     * @param icon 图标文件路径
     * @param onclick 按钮点击事件绑定的方法
     * @param context 上下文
     */
    createLButton: function (type, className, container, title, icon, onclick, context) {
        let link = L.DomUtil.create(type, className, container);
        link.href = '#';
        link.title = title;
        link.src = icon;

        link.setAttribute('role', 'button');
        link.setAttribute('aria-label', title);

        L.DomEvent
            .on(link, 'click', L.DomEvent.stopPropagation)
            .on(link, 'click', L.DomEvent.preventDefault)
            .on(link, 'click', onclick, context)
            .on(link, 'dblclick', L.DomEvent.stopPropagation);
    }
}