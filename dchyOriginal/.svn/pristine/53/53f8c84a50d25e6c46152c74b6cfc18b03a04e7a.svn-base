define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "static/js/cfg/core/BaseWidgetCfg",
        "static/js/cfg/core/SerializeForm",
        "mustache",
        "static/js/cfg/core/ServiceLayerPane",
        "text!../template/font-icons.html",
        "css!static/thirdparty/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css",
        "static/thirdparty/bootstrap-colorpicker/js/bootstrap-colorpicker.min"],
    function (declare, lang, arrayUtils, BaseWidgetCfg, SerializeForm, Mustache,ServiceLayerPane, FontIconsTpl) {
        var tpl = null, configData = null;
        var template = ''; //对应模板名称
        var configLayers = []; //配置的图层信息集合
        var allFields = null;  //所有的字段信息
        //元数据
        var metadata = {
            "layerIndex": 0,
            "layerName": "",
            "serviceId": "",
            "returnFields": [],
            "titleField": {},
            "type": "field"
        };

        var iOWidget = declare([BaseWidgetCfg], {
            constructor: function () {
                tpl = iOWidget.arguments[1];
                template = iOWidget.arguments[2];
            },
            onCreate: function () {
                configData = this;
                configData.isOpen = function () {
                    if (this.open == true)
                        return 'checked';
                    return '';
                };

                configData.isDisplay = function(){
                    if (this.display == true)
                        return 'checked';
                    return '';
                };
                iOWidget.superclass.init(tpl, this);

                configLayers = configData.config.layers;
                if(configLayers == undefined){
                    configLayers = configData.config.layers = [];

                }
                renderLayersInfo();
                pageListeners();
            }
        });

        /**
         * 加载已配置图层
         */
        function renderLayersInfo() {
            var layerInfoTpl = '{{#layers}} <a href="#" class="list-group-item ioWidgetAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
                '<span>{{layerName}}</span>' +
                '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
                '</a>{{/layers}}';
            $("#ioWidgetLayers .list-group").append(Mustache.render(layerInfoTpl, {layers: configData.config.layers}));
            $("#ioWidgetLayers").slimScroll({
                height: 250,
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
            var fieldsTpl = '{{#result}}<div class="item" data-value="{{name}}">{{alias}}</div>{{/result}}';
            var fieldsTpl2 = '{{#result}}<option value="{{name}}">{{alias}}</option>{{/result}}';
            var hasField = false;

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
                    $("#selectL,#selectR").empty();
                    $("#selectL").append(Mustache.render(fieldsTpl2, {result: rp}));
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
                    $.each(n.returnFields, function (j, m) {
                        $("#selectL option[value='" + m.name + "']").remove();
                    });
                    $("#selectR").append(Mustache.render(fieldsTpl2, {result: n.returnFields}));
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
            var tpl = '<a href="#" class="list-group-item ioWidgetAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}">' +
                '<span>{{layerName}}</span>' +
                '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
                '</a>';
            $("#ioWidgetLayers .list-group").append(Mustache.render(tpl, {
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
            $(".ioWidgetAlias").unbind("click").on("click", function () {
                var mapId = $(this).data("mapid");
                var layerIndex = $(this).data("index");
                var layerName = $(this).data("layername");
                editFields(mapId, layerName, layerIndex);
            });

            //删除
            $(".ioWidgetAlias .delete-layer").unbind("click").on("click", function (evt) {
                var layerDom = $(this).parent();
                layer.confirm("确定要删除此图层吗？", function (index) {
                    evt.stopPropagation();
                    var mapId = layerDom.data("mapid");
                    var layerName = layerDom.data("layername");
                    for (var i in configData.config.layers) {
                        if (configData.config.layers[i].serviceId == mapId && configData.config.layers[i].layerName == layerName) {
                            configData.config.layers.splice(i, 1);
                            iOWidget.superclass.commomUpdateWidget(configData);
                            break;
                        }
                    }
                    layerDom.remove();
                    layer.close(index);
                });

            });
        }

        /**
         * 事件监听
         */
        function pageListeners() {
            $('.demo2').colorpicker();
            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();
            $("#import").dropdownSelect();
            $("#export").dropdownSelect();

            layerListListener();
            $("#addService").unbind("click").on("click", function () {
                ServiceLayerPane.show(template, configData.config.layers, afterGetLayer);
            });

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



            /**
             * 保存
             */
            $("#iOWidgetSaveBtn").on('click', function(){
                var hasLayers = false;
                var form = SerializeForm.serializeObject($("#iOWidgetForm"));
                var tempConfigData = lang.clone(metadata);

                $("#selectR").find("option").each(function () {
                    var value = this.value;
                    $.each(allFields, function (i, n) {
                        if (n.name == value) {
                            tempConfigData.returnFields.push(n);
                            return false;
                        }
                    });
                });

                $.each(allFields, function (i, n) {
                    if (n.name == form.titleField) {
                        tempConfigData.titleField = lang.clone(n);
                        delete tempConfigData.titleField.type;
                        return false;
                    }
                });


                if(form.open == 'on'){
                    configData.open = true;
                }else{
                    configData.open = false;
                }

                if(form.display == 'on'){
                    configData.display = true;
                }else{
                    configData.display = false;
                }

                configData.label = form.label;
                configData.icon = form.icon;
                configData.config.importStyle.fill_color = form.import_fill_color;
                configData.config.importStyle.outline_color = form.import_outline_color;
                configData.config.selectStyle.fill_color = form.select_fill_color;
                configData.config.selectStyle.outline_color = form.select_outline_color;

                tempConfigData.layerIndex = form.layerIndex;
                tempConfigData.layerName = form.layerName;
                tempConfigData.serviceId = form.serviceId;

                delete form.label;
                delete form.icon;
                delete form.import_fill_color;
                delete form.import_outline_color;
                delete form.select_fill_color;
                delete form.select_outline_color;
                delete form.open;
                delete form.display;
                delete form.titleField;
                delete form.returnFields;
                delete form.layerIndex;
                delete form.layerName;
                delete form.serviceId;

                if(configData.config.layers != undefined){
                    $.each(configData.config.layers, function (i, n) {
                        if (tempConfigData.serviceId == n.serviceId && tempConfigData.layerName == n.layerName) {
                            lang.mixin(n,tempConfigData);
                            hasLayers = true;
                            return false
                        }
                    });
                }else{
                    configData.config.layers = [];
                }

                if (!hasLayers) {
                    configData.config.layers.push(tempConfigData);
                }

                lang.mixin(configData.config, form);

                iOWidget.superclass.commomUpdateWidget(configData);

            });
        }

        return iOWidget;
    });