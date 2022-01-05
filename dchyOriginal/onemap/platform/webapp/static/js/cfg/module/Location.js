/**
 * Created by user on 2016-06-16.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/topic",
    "static/js/cfg/core/BaseWidgetCfg",
    "mustache",
    "static/js/cfg/core/SerializeForm",
    "text!../template/font-icons.html",
    "static/js/cfg/core/ServiceLayerPane"], function (declare, lang, arrayUtils, topic, BaseWidgetCfg, Mustache, SerializeForm, FontIconsTpl, ServiceLayerPane) {

    var configData = null;   //配置数据
    var tpl = null;    //页面模板
    var template = ''; //对应模板名称
    var allFields = []; //图层对应的所有的字段

    var location = declare([BaseWidgetCfg], {
        constructor: function () {
            tpl = location.arguments[1];
            template = location.arguments[2];
        },

        onCreate: function () {
            configData = this;
            configData.isDisplay = function () {
                if (this.display == true)
                    return "checked";
                return "";
            };
            configData.isOpen = function () {
                if (this.open == true)
                    return "checked";
                return "";
            };
            configData.enableVideo = function () {
                return configData.config.videoSwitch === "on" || configData.config.videoSwitch == true;
            };
            configData.enableShowInfo = function () {
                return configData.config.showInfo === "on" || configData.config.showInfo == true;
            };

            configData.enableShowResult = function () {
                return configData.config.showResult === "on" || configData.config.showResult == true;
            };

            this.init(tpl, this);
            renderLayersInfo();
            pageListeners();
        }
    });

    /**
     * 加载已配置图层
     */
    function renderLayersInfo() {
        var layerInfoTpl = '{{#layers}} <a href="#" class="list-group-item locationAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
            '<span>{{layerAlias}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
            '</a>{{/layers}}';
        $("#locationLayers .list-group").append(Mustache.render(layerInfoTpl, {layers: configData.config.layers}));
        $("#locationLayers").slimScroll({
            height: 350,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    /**
     * 编辑返回字段等信息
     * @param mapId
     * @param layerName
     */
    function editFields(mapId, layerName) {
        var fieldsTpl2 = '{{#result}}<option value="{{name}}">{{alias}}</option>{{/result}}';
        $.ajax({
            url: root + '/config/getFields',
            method: 'post',
            async: false,
            data: {id: mapId, layerName: layerName},
            success: function (rp) {
                allFields = rp;
                $("#selectL").append(Mustache.render(fieldsTpl2, {result: rp}));
            }
        });
        /**
         * 查看已有的图层配置
         */
        $.each(configData.config.layers, function (i, item) {
            if (item.layerAlias == layerName && item.serviceId == mapId) {
                $.each(item.returnFields, function (j, m) {
                    $("#selectL option[value='" + m.name + "']").remove();
                });
                $("#scale").val(item.scale);
                $("#selectR").append(Mustache.render(fieldsTpl2, {result: item.returnFields}));
            }
        });
    }


    /**
     * 从服务列表获取图层后进行处理
     * @param mapLayer
     * @param parent
     */
    function afterGetLayer(mapLayer, parent) {
        _clear();
        $('#layerName').val(mapLayer.name);
        $('#layerAlias').val(mapLayer.alias);
        $('#layerIndex').val(mapLayer.index);
        $('#serviceId').val(parent.id);
        editFields(parent.id, mapLayer.alias);
        var tpl = '<a href="#" class="list-group-item locationAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
            '<span>{{layerAlias}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
            '</a>';
        $("#locationLayers .list-group").append(Mustache.render(tpl, {
            serviceId: parent.id,
            layerAlias: mapLayer.alias,
            layerIndex: mapLayer.index,
            layerName:mapLayer.name
        }));

        //添加临时图层数据
        var tempConfigLayer = {
            "layerAlias":mapLayer.alias,
            "layerIndex":mapLayer.index,
            "layerName":mapLayer.name,
            "returnFields":[],
            "scale":"",
            "serviceId":parent.id
        };
        configData.config.layers.push(tempConfigLayer);
        layerListListener();
    }

    /**
     * 页面监听
     */
    function pageListeners() {
        layerListListener(); //对图层列表操作进行监听
        $("#addService").unbind("click").on("click", function () {
            ServiceLayerPane.show(template, configData.config.layers, afterGetLayer);
        });
        $(".left, .right").css({"height": $(window).height() - 350});
        $("#icons").append(FontIconsTpl);
        $('#icons').dropdownSelect();
        /**
         * 向右监听
         */
        $("#toRight").on('click', function () {
            $("#selectL").find("option:selected").each(function () {
                $(this).remove().appendTo($("#selectR"));
            });
        });

        /**
         * 向左监听
         */
        $("#toLeft").on('click', function () {
            $("#selectR").find("option:selected").each(function () {
                $(this).remove().appendTo($("#selectL"));
            });
        });

        $('.locationCheck').on('click', function () {
            configData.display = $("#display").get(0).checked;
            configData.config.videoSwitch = $("#videoSwitch").get(0).checked;
            configData.config.showInfo = $("#showInfo").get(0).checked;
            configData.config.showResult = $("#showResult").get(0).checked;
            saveWidgets(configData);
        });

        /**
         * 保存
         */
        $("#locationSaveBtn").on('click', function () {
            var hasLayers = false;
            var tempConfigData = SerializeForm.serializeObject($("#locationForm"));
            tempConfigData.layerIndex = parseInt(tempConfigData.layerIndex, 10);
            tempConfigData.returnFields = [];
            $("#selectR").find("option").each(function () {
                var value = this.value;
                $.each(allFields, function (i, n) {
                    if (n.name == value) {
                        tempConfigData.returnFields.push(n);
                        return false;
                    }
                });
            });

            if (configData.config.layers != undefined) {
                $.each(configData.config.layers, function (i, n) {
                    if (tempConfigData.serviceId == n.serviceId && tempConfigData.layerAlias == n.layerAlias) {
                        lang.mixin(n, tempConfigData);
                        hasLayers = true;
                        return false
                    }
                });
            } else {
                configData.config.layers = [];
            }

            if (!hasLayers) {
                configData.config.layers.push(tempConfigData);
            }
            saveWidgets(configData);
        });

    }

    /**
     * 清除表单内容
     * @private
     */
    function _clear() {
        $("#titleField .menu").empty();
        $("#scale").val('');
        $("#selectL").empty();
        $("#selectR").empty();
    }

    /**
     * 图层列表操作监听
     */
    function layerListListener() {
        //点击图层
        $(".locationAlias").unbind("click").on("click", function () {
            var mapId = $(this).data("mapid");
            var alias = $(this).data("layeralias")
            var layerIndex = $(this).data("index");
            var layerName = $(this).data("layername");
            _clear();
            $('#layerName').val(layerName);
            $('#layerAlias').val(alias);
            $('#layerIndex').val(layerIndex);
            $('#serviceId').val(mapId);
            editFields(mapId, alias||layerName);
        });

        //删除
        $(".locationAlias .delete-layer").unbind("click").on("click", function (evt) {
            var layerDom = $(this).parent();
            layer.confirm("确定要删除此图层吗？", function (index) {
                evt.stopPropagation();
                var mapId = layerDom.data("mapid");
                var alias = layerDom.data("layername")||layerDom.data("layeralias");
                for (var i in configData.config.layers) {
                    if (configData.config.layers[i].serviceId == mapId && configData.config.layers[i].layerAlias == alias) {
                        configData.config.layers.splice(i, 1);
                        saveWidgets(configData);
                        break;
                    }
                }
                layerDom.remove();
                _clear();
                layer.close(index);
            });

        });
    }

    /**
     * save
     * @param configData
     */
    function saveWidgets(configData) {
        $.ajax({
            url: root + '/config/tpl/' + template + '/locationWidget/save',
            method: 'post',
            async: false,
            data: {data: JSON.stringify(configData)},
            success: function () {
                layer.msg("修改成功！", {time: 1000, icon: 1});
            }
        });
    }

    return location;
});