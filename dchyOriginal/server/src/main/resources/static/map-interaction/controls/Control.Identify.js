/**
 * @class Control.Identify
 * A simple identify control that shows the feature attribution .Extends `L.Control`.
 * @example L.control.identify().addTo(map);
 */
L.Control.Identify = L.Control.extend({

    options: {

        // 位置
        position: 'topright',

        // 标题
        title: '属性识别',

        // 图标
        icon: 'src/img/Identify.png',

        // 光标
        cursor: undefined,
    },

    initialize: function (businessLayer, baseLayer, alllayers, options) {
        L.setOptions(this, options);

        this._businessLayer = businessLayer; // 业务数据地图服务Url
        this._baseLayer = baseLayer;    // 基础数据地图服务Url
        this._allLayers = alllayers; // 当前地图上所有图层数组
    },

    _mkFeatureInfo: function (url, feature) {
        if (feature) {
            return {url: url, layerId: feature.layerId, feature: feature};
        }
    },

    // 控件添加
    onAdd: function (map) {
        this.__map = map;
        this.__map.tagMapClick = '0';
        var className = 'leaflet-control-identify',
            container = L.DomUtil.create('div', className),
            options = this.options;

        // 创建图标按钮
        this._createIdentify(options.title, 'leaflet-identify', options.icon, container);

        // 初始化表格
        this._initTable();

        // 绑定图层下拉变化事件
        this._onLayerSelectChange();

        return container;
    },

    // 创建属性识别图标按钮
    _createIdentify: function (title, className, icon, container) {
        MapUtils.createLButton('img', className, container, title, icon, this._onButtonClick, this);
    },

    // 初始化属性表格
    _initTable: function () {
        let self = this;
        let map = this.__map;
        layui.use(['form', 'layer', 'table'], function () {
            let table = layui.table;
            let form = layui.form;
            let columns = [
                [
                    {field: 'field', title: '字段', width: 120, minWidth: 120, fixed: 'left'},
                    {field: 'value', title: '值', width: 190, minWidth: 190, fixed: 'right'}
                ]
            ];
            // 表格渲染
            table.render({
                elem: '#identifyTableId',
                size: 'sm',
                cellMinWidth: 110,
                cols: columns,
                toolbar: '#tableToolBarTpl', // 绑定工具条模板
                defaultToolbar: [],
                done: function (res, curr, count) {     // 数据渲染完的回调
                    if (count == 0) {
                        $("body > div.table-container").hide(); // 根据表格数据记录数控制是否显示表格
                    } else {
                        $("body > div.table-container").show(); // 有数据才显示
                    }

                    // 根据地图高度设置表格高度避免压盖地图上方的按钮
                    // 计算是否有留白区域
                    let mapHeight = $('#map').height();
                    let toolHeight = $('div.layui-table-tool').height();
                    let boxHeight = $('div.layui-table-box').height();
                    let tableHeight = mapHeight * 3 / 4;
                    if (tableHeight < (toolHeight + boxHeight + 10)) {    // 表格高度是否超出
                        $(".layui-table-box").css("height", tableHeight - toolHeight - 10);
                    } else {
                        $(".layui-table-box").css("height", "");    // 高度自适应
                    }

                    // 获取图层id集合
                    var layerUrls = self._getLayerUrls(map.identifyFeatures);
                    $("#layerSelect").empty();
                    if (layerUrls) {
                        // 更新模板中图层下拉内容项
                        for (let i = 0, len = layerUrls.length; i < len; i++) {
                            let layerUrl = layerUrls[i];
                            let layerName = self._getLayerNameByUrl(layerUrl);
                            $("#layerSelect").append("<option value=" + layerUrl + ">" + layerName + "</option>");
                        }
                        // 设置选中项
                        $("#layerSelect").val(self._selectedLayerUrl);
                    }
                    // 渲染select，否则下拉框中不显示
                    form.render('select');

                    // 绑定上下要素事件
                    self._onUpDownClick();
                }
            });
        });
    },

    _getSelectedLayerFeatures: function (url, layerId) {
        let self = this;
        let map = this.__map;
        if (!map) {
            return;
        }
        // 获取当前选中图层中的所有识别到的要素
        let allFeatures = [];
        let identifyFeatures = map.identifyFeatures;
        let layerUrl = MapUtils.concatUrl(url, layerId);
        for (let i = 0, len = identifyFeatures.length; i < len; i++) {
            let url = MapUtils.concatUrl(identifyFeatures[i].url, identifyFeatures[i].layerId);
            if (url == layerUrl) {
                allFeatures.push(identifyFeatures[i]);
            }
        }
        self.layerFeatures = allFeatures;
        console.log("Selected Layer:", layerUrl, "IdentifyFeatures:", allFeatures);
    },

    _onUpDownClick: function () {
        let self = this;
        let map = this.__map;
        if (!map) {
            return;
        }

        function onClick(currentIndex) {
            let featureInfo;
            let allFeatures = self.layerFeatures;       // 当前选中图层中所有识别到的要素
            if (allFeatures && allFeatures.length > 0       // 是否为空
                && currentIndex > -1 && currentIndex < allFeatures.length) {    // 设定的索引是否在范围内
                featureInfo = allFeatures[currentIndex];     // 获取指定索引处要素
            }

            MapUtils.clearHighLightFeature(map);    // 移除高亮图形
            if (!featureInfo) {     // 是否为空
                return;
            }

            self.featureIndex = currentIndex;   // 更新当前要素索引
            self._onIdentifiedFeatureChanged(featureInfo);  // 识别要素切换，要素高亮显示，并更新属性表数据
        }

        $('#previousFeature').click(function () {       // 上一个要素点击事件
            let index = self.featureIndex;
            onClick(--index);
        });
        $('#nextFeature').click(function () {   // 下一个要素点击事件
            let index = self.featureIndex;
            onClick(++index);
        });
    },

    // 图层下拉选择变化事件
    _onLayerSelectChange: function () {
        var map = this.__map;
        var self = this;
        layui.use('form', function () {
            var form = layui.form;
            form.on('select(layerSelect)', function (obj) {
                MapUtils.clearHighLightFeature(map);    // 移除高亮图形

                // 获取选择项对应的图层Id
                let selectedUrl = obj.value;
                let identifyFeatures = map.identifyFeatures;
                if (!identifyFeatures) {
                    return;
                }

                // 根据图层Id找到当前识别要素中该图层的要素
                let featureInfo;
                for (let i = 0, len = identifyFeatures.length; i < len; i++) {
                    let url = MapUtils.concatUrl(identifyFeatures[i].url, identifyFeatures[i].layerId);
                    if (url == selectedUrl) {
                        featureInfo = identifyFeatures[i];
                        break;
                    }
                }

                if (featureInfo) {
                    self.featureIndex = 0;      // 切换图层重置要素索引为0
                    self._selectedLayerUrl = MapUtils.concatUrl(featureInfo.url, featureInfo.layerId);  // 记录当前选中图层的Url
                    self._getSelectedLayerFeatures(featureInfo.url, featureInfo.layerId);   // 获取当前选中图层中所有识别到的要素
                }
                self._onIdentifiedFeatureChanged(featureInfo);      // 要素高亮显示，并更新属性表数据
                form.render('select');
            });
        });
    },

    _closePageLayer: function () {
        layui.use('layer', function () {
            let layer = layui.layer;
            layer.closeAll('page');
        })
    },

    // 点击识别按钮
    _onButtonClick: function () {
        let self = this;
        let map = this.__map;
        let businessLayer = self._businessLayer;
        if (!businessLayer) {
            return;
        }
        self._updateTableData([]);  // 清空表格数据
        self._closePageLayer();         // 关闭layui弹出的自定义层
        MapUtils.clearHighLightFeature(map);    // 移除高亮图形
        map.off('click contextmenu'); // 取消绑定的click事件
        if (map.tagMapClick == '1') {
            map.tagMapClick = '0';
            document.getElementById('map').style.cursor = '';   // 恢复光标为默认
        } else {
            map.tagMapClick = '1';
            document.getElementById('map').style.cursor = 'pointer';    // 修改光标
            map.on('click', function (e) {       // 绑定地图点击事件
                self._onMapClick(e);
            });
        }
    },

    // 地图点击
    _onMapClick: function (e) {
        let self = this,
            map = self.__map,
            baseLayer = self._baseLayer,
            businessLayer = self._businessLayer,
            businessLayerUrl = businessLayer ? businessLayer.options.url : undefined,
            baseLayerUrl = baseLayer ? baseLayer.options.url : undefined,
            slbh = self._slbh;

        // 移除高亮图形
        MapUtils.clearHighLightFeature(map);

        // 图层要素识别
        function identifyLayer(dynamicLayer, filter) {
            return new Promise(function (resolve, reject) {
                if (dynamicLayer) {
                    let layers = 'visible'; // 只识别可见图层
                    if (dynamicLayer.getLayers() && dynamicLayer.getLayers().length > 0) {
                        layers = layers.concat(':', dynamicLayer.getLayers().join(","));
                    }

                    let identify = dynamicLayer.identify().on(map).at(e.latlng).layers(layers);
                    identify.run(function (error, featureCollection) {
                        if (error) {
                            console.log(error);
                            return;
                        }
                        let features = featureCollection.features;
                        features.sort(function (a, b) {
                            // 按图层id排序
                            return a.layerId - b.layerId;
                        });

                        if (filter && slbh) {
                            // 增加受理编号过滤条件
                            features = self._filterBySLBH(features, slbh);
                        }
                        resolve(features);
                    });
                }
            });
        }

        // 识别结果添加至要素集合
        function pushIdentifyFeatures(layerUrl, featureCollection) {
            featureCollection && featureCollection.forEach(function (feature) {
                map.identifyFeatures.push(self._mkFeatureInfo(layerUrl, feature));
            });
        }

        // 等待所有图层识别完成
        let promises = [];
        promises.push(identifyLayer(businessLayer, true));
        promises.push(identifyLayer(baseLayer, false));
        Promise.all(promises).then(function (values) {
                if (!values) {
                    return;
                }
                let identifyBusinessFeatures = values[0];
                let identifyBaseFeatures = values[1];
                console.log("Identified BusinessLayer Features -->", identifyBusinessFeatures);
                console.log("Identified BaseLayer Features -->", identifyBaseFeatures);


                map.identifiedFeature = {}; // 重置识别要素
                map.identifyFeatures = [];  // 重置识别要素集合
                pushIdentifyFeatures(businessLayerUrl, identifyBusinessFeatures);   // 添加业务图层要素
                pushIdentifyFeatures(baseLayerUrl, identifyBaseFeatures);   // 添加基础数据要素
                let featureInfo = map.identifyFeatures[0];
                if (map.identifyFeatures && map.identifyFeatures.length > 0) {
                    featureInfo = map.identifyFeatures[0];      // 默认显示识别到的第一个要素
                    self.featureIndex = 0;      // 重置要素索引为0
                    self._selectedLayerUrl = MapUtils.concatUrl(featureInfo.url, featureInfo.layerId);  // 记录当前选中图层的Url
                    self._getSelectedLayerFeatures(featureInfo.url, featureInfo.layerId);   // 获取当前选中图层中所有识别到的要素
                }
                self._onIdentifiedFeatureChanged(featureInfo);      // 要素高亮显示，并更新属性表数据

                // 更新表格数据
                console.log('Identified features:', map.identifyFeatures);
            }
        );
    },
    /**
     * 切换识别要素
     * @param featureInfo
     * @private
     */
    _onIdentifiedFeatureChanged: function (featureInfo) {
        let self = this;
        let map = this.__map;
        let jsonArray = [];
        if (map && featureInfo) {
            // 识别要素
            let feature = featureInfo.feature;
            // 识别要素高亮显示
            MapUtils.highlightFeature(map, feature);
            // 获取要素字段值
            jsonArray = self._getFeatureProperties(feature);
        }
        // 更新属性表数据
        self._updateTableData(jsonArray);
    },

    /**
     * 按受理编号进行要素过滤
     * @param features
     * @param slbh
     * @returns {*}
     * @private
     */
    _filterBySLBH: function (features, slbh) {
        // 增加受理编号过滤条件
        return features.filter(function (item) {
            if (item.properties.hasOwnProperty("受理编号")) {
                return item.properties["受理编号"] == slbh;
            } else if (item.properties.hasOwnProperty("SLBH")) {
                return item.properties["SLBH"] == slbh;
            }
            return true;
        });
    },

    /**
     * 获取所有要素的图层Url数组
     * @param featureInfos
     * @returns {[]}
     * @private
     */
    _getLayerUrls: function (featureInfos) {
        let urls = [];
        if (featureInfos) {
            featureInfos.forEach(function (item) {
                let url = MapUtils.concatUrl(item.url, item.layerId);
                if (urls.indexOf(url) < 0) {
                    urls.push(url);
                }
            })
        }
        return urls;
    },

    /**
     * 根据Url获取图层名称
     * @param url
     * @private
     */
    _getLayerNameByUrl: function (url) {
        let allLayers = this._allLayers;
        for (let i = 0, len = allLayers.length; i < len; i++) {
            let layer = allLayers[i];
            if ((MapUtils.concatUrl(layer.ServiceUrl, layer.LayerId)) == url) {
                return layer.LayerName;
            }
        }
    },

    /**
     * 获取要素属性字段Json数组
     * @param feature
     * @returns {[]}
     * @private
     */
    _getFeatureProperties: function (feature) {
        let jsonArray = [];
        if (feature && feature.properties) {
            for (let p in feature.properties) {
                jsonArray.push({"field": p, "value": feature.properties[p]});
            }
        }
        return jsonArray;
    },

    // 更新属性表格数据
    _updateTableData: function (data) {
        if (!data) {
            return;
        }
        // 执行重载
        var table = layui.table;
        table.reload('identifyTableId', {
            limit: data.length,
            data: data,
            toolbar: '#tableToolBarTpl'
        });
    }
});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.identifyControl) {
        this.identifyControl = L.control.identify();
        this.addControl(this.identifyControl);
    }
});

L.control.identify = function (businessLayer, baseLayer, alllayers, options) {
    return new L.Control.Identify(businessLayer, baseLayer, alllayers, options);
};