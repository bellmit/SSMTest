/**
 * 选择图形新增错误
 */
L.Control.AddError = L.Control.extend({

    options: {

        // 位置
        position: 'topright',

        // 标题
        title: '选择图形新增错误',

        // 图标
        icon: 'src/img/Error.png',

        // 光标
        cursor: undefined,

        interfaceUrl: ''
    },

    /**
     * 构造函数
     * @param dynamicLayer 动态图层对象
     * @param allLayers 所有的业务图层
     * @param slbh 受理编号
     * @param options 可选项
     */
    initialize: function (dynamicLayer, allLayers, slbh, options) {
        L.setOptions(this, options);

        this._dynamicLayer = dynamicLayer; // 地图服务
        this._allLayers = allLayers; // 当前地图上所有图层数组
        this._slbh = slbh;  // 受理编号
    },

    // 控件添加
    onAdd: function (map) {
        this.__map = map;
        var className = 'leaflet-control-add-error',
            container = L.DomUtil.create('div', className),
            options = this.options;

        // 创建图标按钮
        this._createButton(options.title, 'leaflet-add-error', options.icon, container);

        // 绑定图层下拉变化事件
        this._onLayerSelectChange();

        return container;
    },

    _createButton: function (title, className, icon, container) {
        MapUtils.createLButton('img', className, container, title, icon, this._onButtonClick, this);
    },

    _mkFeatureInfo: function (url, feature) {
        if (feature) {
            return {url: url, layerId: feature.layerId, feature: feature};
        }
    },
    _clearTableData: function () {
        layui.use('table', function () {
            var table = layui.table;
            var data = [];
            table.reload('identifyTableId', {
                limit: data.length,
                data: data,
                toolbar: '#tableToolBarTpl'
            });
        });
    },
    _closePageLayer: function () {
        layui.use('layer', function () {
            let layer = layui.layer;
            layer.closeAll('page');
        })
    },
    _onButtonClick: function () {
        let self = this;
        let map = this.__map;

        let interfaceUrl = this.options.interfaceUrl;
        if (!interfaceUrl) {
            layui.use('layer', function () {
                let layer = layui.layer;
                layer.msg('未进行新增错误接口Url配置。');
                return;
            });
        }
        let dynamicLayer = this._dynamicLayer;
        if (!dynamicLayer) {
            return;
        }
        self._clearTableData();  // 清空表格数据
        self._closePageLayer();         // 关闭layui弹出的自定义层
        MapUtils.clearHighLightFeature(map);    // 移除高亮图形
        map.off('click contextmenu'); // 取消绑定的click事件
        if (map.tagMapClick == '2') {
            map.tagMapClick = '0';
            document.getElementById('map').style.cursor = '';
        } else {
            map.tagMapClick = '2';
            document.getElementById('map').style.cursor = 'crosshair';
            map.on('click', function (e) {       // 绑定地图点击事件
                self._onMapClick(e);
            });
        }
    },
    _onMapClick: function (e) {
        let self = this,
            map = this.__map,
            dynamicLayer = this._dynamicLayer,
            slbh = this._slbh,
            url = dynamicLayer ? dynamicLayer.options.url : undefined;

        layui.use(['form', 'layer'], function () {
            MapUtils.clearHighLightFeature(map);    // 移除高亮图形

            let layers = 'visible'; // 只识别可见图层
            if (dynamicLayer.getLayers() && dynamicLayer.getLayers().length > 0) {
                layers = layers.concat(':', dynamicLayer.getLayers().join(","));
            }
            let identify = dynamicLayer.identify().on(map).at(e.latlng).layers(layers);
            identify.run(function (error, featureCollection) {
                    if (error) {
                        alert("要素识别失败！");
                        console.log(error);
                        return;
                    }

                    let featureId;
                    let layerUrls;
                    let features = featureCollection.features;
                    map.identifyFeatures = [];
                    if (features.length > 0) {
                        // 按layerId排序
                        features.sort(function (a, b) {
                            return a.layerId - b.layerId;
                        });
                        if (slbh) {
                            // 增加受理编号过滤条件
                            features = self._filterBySLBH(features, slbh);
                        }
                        if (features && features.length > 0) {
                            // 获取识别要素集合
                            features.forEach(function (item) {
                                map.identifyFeatures.push(self._mkFeatureInfo(url, item));
                            });

                            // 重新添加要素并获取要素属性
                            let feature = features[0];
                            featureId = feature.id;
                            MapUtils.highlightFeature(map, feature);    // 要素高亮显示

                            layerUrls = self._getLayerUrls(map.identifyFeatures);       // 获取所有图层url
                        }
                    }

                    // 更新form表单中图层下拉列表
                    self._updateFormLayerSelect(layerUrls);

                    // 弹出表单自定义层
                    self._showFormLayer(featureId);
                }
            );
        });
    },

    _showFormLayer: function (featureId) {
        let self = this;
        layui.use(['form', 'layer'], function () {
            let layer = layui.layer;
            let form = layui.form;
            // 更新form表单中的要素Id，错误等级，错误描述
            form.val('addErrorLayerForm', {
                "featureId": featureId ? featureId : "",    // 更新要素id
                "errorLevel": 1,    // 重置错误等级
                "errorDesc": "",    // 重置错误描述
            });

            // 渲染select，否则下拉框中不显示
            form.render('select');
            if (!featureId) {
                return;
            }

            // 页面层-自定义
            let index = layer.open({
                type: 1,
                id: 'layui-id-layer-add-error', // 设定id，防止重复弹出
                title: '选择图形添加错误',
                content: $('#addErrorLayerForm'), // Dom
                resize: false,
                shade: 0,
                area: ['500px', '390px'],   // size,[宽,高]
                btn: ['确定', '取消'],  // 按钮
                yes: function (index) { // 点击确定
                    let data = form.val('addErrorLayerForm');       // 获取表单json数据
                    console.log("Insert Error Data --> ", data);
                    if (data.errorDesc) {
                        // 插入错误
                        self._insertError(index, data);
                        parent.treeReload();
                    } else {
                        layer.msg('请输入错误描述。'); 
                    }
                }
            });
        });
    },
    _updateFormLayerSelect: function (layerUrls) {
        let self = this;
        $('#addErrorLayerForm').find("select[name='layer']").empty();
        if (layerUrls) {
            // 更新模板中图层下拉内容项
            for (let i = 0, len = layerUrls.length; i < len; i++) {
                let layerUrl = layerUrls[i];
                let layerName = self._getLayerNameByUrl(layerUrl);
                $('#addErrorLayerForm').find("select[name='layer']").append("<option value=" + layerUrl + ">" + layerName + "</option>");
            }
        }
    },
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
    _onLayerSelectChange: function () {
        var map = this.__map;
        var self = this;
        layui.use('form', function () {
            var form = layui.form;
            form.on('select(addErrorLayerForm-layerSelect)', function (obj) {
                MapUtils.clearHighLightFeature(map);    // 移除高亮图形

                // 获取选择项对应的图层Id
                let selectedUrl = obj.value;
                let identifyFeatures = map.identifyFeatures;
                if (!identifyFeatures) {
                    return;
                }

                // 根据图层Id找到当前识别要素中该图层的要素
                let featureInfo;
                for (i = 0; i < identifyFeatures.length; i++) {
                    let url = MapUtils.concatUrl(identifyFeatures[i].url, identifyFeatures[i].layerId);
                    if (url == selectedUrl) {
                        featureInfo = identifyFeatures[i];
                        break;
                    }
                }

                if (featureInfo) {
                    MapUtils.highlightFeature(map, featureInfo.feature);    // 要素高亮显示
                    // 修改表单中featureId的值
                    form.val('addErrorLayerForm', {
                        "featureId": featureInfo.feature.id,
                    });
                }

                form.render('select');
            });
        });
    },

    // 调用接口新增错误
    _insertError: function (layerIndex, data) {
        let url = this.options.interfaceUrl;
        let self = this;
        let slbh = this._slbh;
        let featureId = data.featureId; // 要素id
        let layerName = self._getLayerNameByUrl(data.layer); //图层名称
        let errorLevel = data.errorLevel; // 错误等级
        let errorDesc = data.errorDesc; // 错误描述
        let jsonData = {
            oid: featureId,
            tcmc: layerName,
            cwlx: errorLevel,
            cwms: errorDesc,
            xmbh: slbh,
            f: "json"
        };
        console.log("AddError:", jsonData);

        // 组织url，json格式数据传参失败，服务接收不到
        for (let item in jsonData) {
            // url附加参数
            url = MapUtils.updateUrlParams(url, item, jsonData[item]);
        }

        layui.use(['layer'], function () {
            let layer = layui.layer;
            $.ajax({
                type: 'get',    // 请求方式
                url: url,   // 接口url地址
                crossDomain: true,  // 跨域
                async: false, // 请求是否异步，true为异步，false为同步
                contentType: 'application/json;charset=utf-8',  // 请求的媒体类型
                dataType: 'json',   // 返回格式
                success: function (response) {      // 请求成功
                    console.log(response);
                    if (response.success) {
                        layer.msg('新增错误成功。');
                        layer.close(layerIndex);
                    } else {
                        layer.msg("新增错误失败：" + response.message);
                    }
                },
                error: function (response) {     // 请求失败
                    layer.msg('新增错误请求失败。');
                    console.log(response);
                }
            });
        });
    },

    _getLayerUrls: function (featureInfos) {
        var urls = [];
        var self = this;
        if (featureInfos) {
            featureInfos.forEach(function (item) {
                var url = MapUtils.concatUrl(item.url, item.layerId);
                if (urls.indexOf(url) < 0) {
                    urls.push(url);
                }
            })
        }
        return urls;
    },
    _getLayerNameByUrl: function (url) {
        let self = this;
        let allLayers = this._allLayers;
        for (i = 0; i < allLayers.length; i++) {
            let layer = allLayers[i];
            if ((MapUtils.concatUrl(layer.ServiceUrl, layer.LayerId)) == url) {
                return layer.LayerName;
            }
        }
    },
});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.addErrorControl) {
        this.addErrorControl = L.control.addError();
        this.addControl(this.addErrorControl);
    }
});

L.control.addError = function (dynamicLayer, layers, slbh, options) {
    return new L.Control.AddError(dynamicLayer, layers, slbh, options);
};