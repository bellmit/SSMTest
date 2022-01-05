/**
 *  属性识别配置
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 15:51
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "static/js/cfg/core/BaseWidgetCfg",
    "mustache",
    "static/js/cfg/core/SerializeForm",
    "text!../template/font-icons.html",
    "static/js/cfg/core/ServiceLayerPane",
    "static/thirdparty/selectize/selectize.min",
    "css!static/thirdparty/selectize/selectize.bootstrap3.css"
], function (declare, lang, arrayUtils, BaseWidgetCfg, Mustache, SerializeForm, FontIconsTpl, ServiceLayerPane) {
    var configData = null;   //配置数据
    var tpl = null;    //页面模板
    var template = ''; //对应模板名称
    var configLayers = []; //配置的图层信息集合
    var allFields = null;  //所有的字段信息
    var $returnFieldsSelect = undefined;
    var returnFieldsControl = undefined;
    //元数据
    var metadata = {
        enableVideo: false,
        html: '',
        layerIndex: 0,
        layerName: '',
        link: {
            tip: '',
            url: ''
        },
        returnFields: [],
        serviceId: '',
        titleField: {
            alias: '',
            name: ''
        },
        type: "field"
    };

    var identify = declare([BaseWidgetCfg], {

        constructor: function () {
            tpl = identify.arguments[1];
            template = identify.arguments[2];
        },
        onCreate: function () {
            configData = this;
            configData.init(tpl, this);
            configLayers = configData.config.layers||[];
            renderLayersInfo();
            pageListeners();
            $("#identifySaveBtn").addClass("disabled");
            $("form input, form select").attr("disabled", "disabled");
        }
    });


    /**
     * 加载已配置图层
     */
    function renderLayersInfo() {
        var layerInfoTpl = '{{#layers}} <a href="#" class="list-group-item identifyAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
            '<span>{{layerName}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
            '</a>{{/layers}}';
        $("#identifyLayers .list-group").append(Mustache.render(layerInfoTpl, {layers: configData.config.layers}));
        $("#identifyLayers").slimScroll({
            height: 450,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    /**
     * 填充右侧编辑数据
     * @param mapId
     * @param layerName
     * @param layerIndex
     */
    function editFields(mapId, layerName, layerIndex) {
        $("#identifySaveBtn").removeClass("disabled");
        $("form input, form select").removeAttr("disabled", "disabled");

        var fieldsTpl = '{{#result}}<div class="item" data-value="{{name}}">{{alias}}</div>{{/result}}';
        var returnFieldsInput = '<input class="form-control" id="returnFieldsInput" multiple placeholder="添加字段"></input>';
        var hasField = false;

        $("#returnFieldsContainer").empty();
        $("#returnFieldsContainer").append(returnFieldsInput);

        $.ajax({
            url: root + '/config/getFields',
            method: 'post',
            async: false,
            data: {id: mapId, layerName: layerName},
            success: function (rp) {
                allFields = rp;
                $("#titleFields .menu").empty();
                $("#titleFields .menu").append(Mustache.render(fieldsTpl, {result: rp}));

                $("#titleFields").dropdownSelect('refresh');
                $("#URL1,#URL2").val('');

                $returnFieldsSelect = $('#returnFieldsInput').selectize({
                    maxItems: null,
                    valueField: 'name',
                    labelField: 'alias',
                    searchField: 'name',
                    render: {
                        item: function(data, escape) {
                            return '<div title="拖拽进行排序！">' + escape(data.alias) + '</div>';
                        }
                    },
                    options: allFields,
                    create: false,
                    plugins: ['remove_button', 'drag_drop'],
                    persist: false
                });

                returnFieldsControl = $returnFieldsSelect[0].selectize;

                $("input[name='serviceId']").eq(0).val(mapId);
                $("input[name='layerName']").eq(0).val(layerName);
                $("input[name='layerIndex']").eq(0).val(layerIndex);
            }
        });

        /**
         * 查看已有的图层配置
         */
        $.each(configLayers, function (i, n) {
            if (n.serviceId == mapId && n.layerName == layerName) {
                $("#titleFields").dropdownSelect('set selected', n.titleField.name);
                $("input[name='tip']").eq(0).val(n.link.tip);
                $("input[name='url']").eq(0).val(n.link.url);
                $.each(n.returnFields, function(index, item){
                    returnFieldsControl.addItem(item.name);
                });
                hasField = true;
                return false;
            }
        });

        if (!hasField) {
            $("#titleFields").dropdownSelect('clear');
        }
    }

    /**
     * 获取到需配置图层后操作
     * @param mapLayer
     * @param parent
     */
    function afterGetLayer(mapLayer, parent) {
        editFields(parent.id, mapLayer.name, mapLayer.index);
        var tpl = '<a href="#" class="list-group-item identifyAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
            '<span>{{layerName}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
            '</a>';
        $("#identifyLayers .list-group").append(Mustache.render(tpl, {
            serviceId: parent.id,
            layerAlias: mapLayer.alias,
            layerIndex: mapLayer.index,
            layerName: mapLayer.name
        }));

        //添加临时图层数据
        var tempConfigData = lang.clone(metadata);
        tempConfigData.layerIndex = mapLayer.index;
        tempConfigData.layerName = mapLayer.name;
        tempConfigData.serviceId = parent.id;
        configLayers.push(tempConfigData);
        layerListListener();
    }

    /**
     * 图层列表点击监听
     */
    function layerListListener() {
        //点击图层
        $(".identifyAlias").unbind("click").on("click", function () {
            var mapId = $(this).data("mapid");
            var layerIndex = $(this).data("index");
            var layerName = $(this).data("layername");
            editFields(mapId, layerName, layerIndex);
        });
        //删除
        $(".identifyAlias .delete-layer").unbind("click").on("click", function (evt) {
            var layerDom = $(this).parent();
            layer.confirm("确定要删除此图层吗？", function (index) {
                evt.stopPropagation();
                var mapId = layerDom.data("mapid");
                var layerName = layerDom.data("layername");
                for (var i in configData.config.layers) {
                    if (configData.config.layers[i].serviceId == mapId && configData.config.layers[i].layerName == layerName) {
                        configData.config.layers.splice(i, 1);
                        identify.superclass.commomUpdateWidget(configData);
                        break;
                    }
                }
                layerDom.remove();
                layer.close(index);
            });

        });
    }

    /**
     * 页面监听
     */
    function pageListeners() {
        layerListListener();
        $("#addService").unbind("click").on("click", function () {
            ServiceLayerPane.show(template, configData.config.layers, afterGetLayer);
        });

        $(".left, .right").css({"height": $(window).height() - 250});

        $("#icons").append(FontIconsTpl);
        $('#icons').dropdownSelect();

        $("#identifySaveBtn").on('click', function () {
            var hasLayers = false;
            var tempConfigData = lang.clone(metadata);
            var form = SerializeForm.serializeObject($("#identifyForm"));
            var values = returnFieldsControl.getValue().split(",");
            if (values.length > 0) {
                $.each(allFields, function (index, item) {
                    var idx = $.inArray(item.name, values);
                    if (idx !== -1) {
                        tempConfigData.returnFields[idx] = item;
                    }
                });
            }

            $.each(allFields, function (i, n) {
                if (n.name == form.titleField) {
                    tempConfigData.titleField = lang.clone(n);
                    delete tempConfigData.titleField.type;
                    return false;
                }
            });

            configData.label = form.label;
            configData.icon = form.icon;
            tempConfigData.link.tip = form.tip;
            tempConfigData.link.url = form.url;
            tempConfigData.link.defaultOpen = false;
            delete form.label;
            delete form.icon;
            delete form.url;
            delete form.tip;
            delete form.titleField;
            delete form.returnFields;

            lang.mixin(tempConfigData, form);

            if (configData.config.layers != undefined) {
                $.each(configData.config.layers, function (i, n) {
                    if (tempConfigData.serviceId == n.serviceId && tempConfigData.layerName == n.layerName) {
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

            identify.superclass.commomUpdateWidget(configData);
        });

    }

    return identify;
});