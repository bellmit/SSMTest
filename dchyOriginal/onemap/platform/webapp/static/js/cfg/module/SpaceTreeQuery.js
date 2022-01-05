define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "static/js/cfg/core/BaseWidgetCfg",
        "mustache",
        "slimScroll",
        "static/js/cfg/core/packed.layer",
        "static/js/cfg/core/SerializeForm",
        "text!../template/font-icons.html",
        "text!../template/widgets/query/query_field.html",
        "static/js/cfg/core/ServiceLayerPane",
        "static/thirdparty/selectize/selectize.min",
        "css!static/thirdparty/selectize/selectize.bootstrap3.css"],
    function (declare, lang, arrayUtils, BaseWidgetCfg, Mustache, SlimScroll, pkLayer, SerializeForm, FontIconsTpl, queryFieldTpl, ServiceLayerPane) {
        var configData = null;
        var tpl = null;
        var template = ''; //对应模板名称
        var configLayers = []; //配置的图层信息集合
        var allFields = null;  //所有的字段信息
        var $queryFieldsSelect = undefined;
        var $returnFieldsSelect = undefined;
        var queryFieldsControl = undefined;
        var returnFieldsControl = undefined;
        var metadata = {
            "html": "",
            "layerIndex": 0,
            "layerName": "",
            "layerUrl": "",
            "link": {
                tip: '',
                "url": ""
            },
            "queryFields": {
                "fields": [],
                "prefix": "%"
            },
            "returnFields": [],
            "serviceId": "0146fafab5ab402881fd46d7406500ae",
            "titleField": {
                "alias": "",
                "name": ""
            },
            "type": "field"
        };

        var queryFields = [], returnFields = [];

        var query = declare([BaseWidgetCfg], {
            constructor: function () {
                tpl = query.arguments[1];
                template = query.arguments[2];
            },

            onCreate: function () {
                configData = this;
                configData.init(tpl, this);
                configLayers = configData.config.layers;
                renderLayersInfo();
                pageListeners();

                configData.isQuery = function () {
                    if (this.config.queryPaging) {
                        return 'checked';
                    }
                    return '';
                };
            }
        });

        /**
         * 加载已配置图层
         */
        function renderLayersInfo() {
            var layerInfoTpl = '{{#layers}} <a href="#" class="list-group-item queryAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}" data-url="{{layerUrl}}">' +
                '<span>{{layerName}}</span>' +
                '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
                '</a>{{/layers}}';
            $("#queryLayers .list-group").append(Mustache.render(layerInfoTpl, {layers: configData.config.layers}));
            $("#queryLayers").slimScroll({
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
         * @param url
         */
        function editFields(mapId, layerName, layerIndex, layerUrl) {
            returnFields=[];
            queryFields=[];
            $("#querySaveBtn").removeClass("disabled");
            $("form input, form select").removeAttr("disabled", "disabled");
            var fieldsTpl = '{{#result}}<div class="item" data-value="{{name}}">{{alias}}</div>{{/result}}';
            var queryFieldsInput = '<input class="form-control" id="queryFieldsInput" multiple placeholder="添加字段"></input>';
            var returnFieldsInput = '<input class="form-control" id="returnFieldsInput" multiple placeholder="添加字段"></input>';
            var hasField = false;
            var prefixLayer = arrayUtils.filter(configLayers, function (item, idx) {
                return item.layerName === layerName;
            });
            var prefixValue = prefixLayer[0] ? prefixLayer[0].queryFields.prefix : '%';
            /*$("#prefixs input").val(prefixValue);*/
            var prefixSelectionHtml = '<input type="hidden" name="prefix" value=' + prefixValue + '>' +
                '<i class="dropdown icon"></i>' +
                '<div class="default text"></div>' +
                '<div class="menu">' +
                '<div class="item" data-value="%">%</div>' +
                '<div class="item" data-value="*">*</div>' +
                '</div>';
            $("#prefixs").empty();
            $("#queryFieldsContainer").empty();
            $("#returnFieldsContainer").empty();
            $("#prefixs").append(prefixSelectionHtml);
            $("#prefixs").dropdownSelect();
            $("#queryFieldsContainer").append(queryFieldsInput);
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
                    $queryFieldsSelect = $('#queryFieldsInput').selectize({
                        maxItems: null,
                        valueField: 'name',
                        labelField: 'alias',
                        searchField: 'name',
                        render: {
                            item: function(data, escape) {
                                return '<div title="拖拽排序，双击配置字段信息！">' + escape(data.alias) + '</div>';
                            }
                        },
                        options: allFields,
                        create: false,
                        plugins: ['remove_button', 'drag_drop'],
                        persist: false,
                        onItemAdd: function(value, $item){
                            var field = getField(queryFields, value);
                            if(field == null){
                                queryFields.push(getField(allFields, value));
                            }
                            //双击配置字段信息
                            $item.dblclick(function(){
                                pkLayer.showDialog('配置属性', Mustache.render(queryFieldTpl, getField(queryFields, value)), modifyFieldPropsCall, {area: '600px'});
                            });
                        },
                        onItemRemove: function(value){
                            removeField(queryFields, value);
                        },
                        onChange: function(value){
                            //拖动排序
                            var values = value.split(",");
                            if(values.length === queryFields.length){
                                $.each(queryFields, function(idx, item){
                                    var index = $.inArray(item.name, values);
                                    if(index !== -1 &&index !== idx){
                                        var tmp = lang.clone(queryFields[index]);
                                        queryFields[index] = lang.clone(item);
                                        queryFields[idx] = tmp;
                                    }
                                });
                            }
                        }
                    });
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
                        persist: false,
                        onItemAdd: function(value, $item){
                            var field = getField(returnFields, value);
                            if (field !== null) return;
                            returnFields.push(getField(allFields, value));
                        },
                        onItemRemove: function(value){
                            removeField(returnFields, value);
                        },
                        onChange: function(value){
                            //拖动排序
                            var values = value.split(",");
                            if(values.length === returnFields.length){
                                $.each(returnFields, function(idx, item){
                                    var index = $.inArray(item.name, values);
                                    if(index !== -1 && index !== idx){
                                        var tmp = lang.clone(returnFields[index]);
                                        returnFields[index] = lang.clone(returnFields[idx]);
                                        returnFields[idx] = tmp;
                                    }
                                });
                            }
                        }
                    });
                    queryFieldsControl = $queryFieldsSelect[0].selectize;
                    returnFieldsControl = $returnFieldsSelect[0].selectize;
                    $("input[name='serviceId']").eq(0).val(mapId);
                    $("input[name='layerName']").eq(0).val(layerName);
                    $("input[name='layerIndex']").eq(0).val(layerIndex);
                    $("input[name='layerUrl']").eq(0).val(layerUrl);
                }
            });

            /**
             * 查看已有的图层配置
             */
            $.each(configLayers, function (i, n) {
                if (n.serviceId == mapId && n.layerName == layerName) {
                    $("#titleFields").dropdownSelect('set selected', n.titleField.name);
                    $("#prefixs").dropdownSelect('set selected', n.queryFields.prefix);
                    $("input[name='tip']").eq(0).val(n.link.tip);
                    $("input[name='url']").eq(0).val(n.link.url);
                    $.each(n.returnFields, function (j, m) {
                        returnFieldsControl.addItem(m.name);
                    });

                    $.each(n.queryFields.fields, function (j, m) {
                        queryFieldsControl.addItem(m.name);
                    });

                    hasField = true;
                    returnFields = n.returnFields;
                    queryFields = n.queryFields.fields;
                    //DPF添加
                    if (n.queryPaging) {
                        $("input[name='queryPaging']").attr("checked","checked");
                    }
                    return false;
                }
            });

            if (!hasField) {
                returnFields = [];
                queryFields = [];
                $("#titleFields").dropdownSelect('clear');
            }
        }


        /**
         * 获取到需配置图层后操作
         * @param mapLayer
         * @param parent
         */
        function afterGetLayer(mapLayer, parent) {
            editFields(parent.id, mapLayer.name, mapLayer.index, completeUrl(parent.url, mapLayer.index));
            var tpl = '<a href="#" class="list-group-item queryAlias" data-mapid="{{serviceId}}" data-layeralias="{{layerAlias}}" data-layername="{{layerName}}" data-index="{{layerIndex}}" data-url="{{layerUrl}}">' +
                '<span>{{layerName}}</span>' +
                '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
                '</a>';
            $("#queryLayers .list-group").append(Mustache.render(tpl, {
                serviceId: parent.id,
                layerAlias: mapLayer.alias,
                layerIndex: mapLayer.index,
                layerName: mapLayer.name,
                layerUrl:completeUrl(parent.url, mapLayer.index)
            }));

            //添加临时图层数据
            var tempConfigData = lang.clone(metadata);
            tempConfigData.layerIndex = mapLayer.index;
            tempConfigData.layerName = mapLayer.name;
            tempConfigData.serviceId = parent.id;
            tempConfigData.layerUrl = completeUrl(parent.url, mapLayer.index);
            configLayers.push(tempConfigData);
            layerListListener();
        }

        /**
         * 补齐url
         * @param url
         * @param index
         * @returns {string}
         */
        function completeUrl(url, index) {
            if (!(url.substr(0, "http".length)) === "http") {
                return (window.location.origin + url + "/" + index);
            } else {
                return (url + "/" + index);
            }
        }

        /**
         * 图层列表点击监听
         */
        function layerListListener() {
            //点击图层
            $(".queryAlias").unbind("click").on("click", function () {
                var mapId = $(this).data("mapid");
                var layerIndex = $(this).data("index");
                var layerName = $(this).data("layername");
                var layerUrl = $(this).data("url");
                editFields(mapId, layerName, layerIndex, layerUrl);
            });
            //删除
            $(".queryAlias .delete-layer").unbind("click").on("click", function (evt) {
                var layerDom = $(this).parent();
                layer.confirm("确定要删除此图层吗？", function (index) {
                    evt.stopPropagation();
                    var mapId = layerDom.data("mapid");
                    var layerName = layerDom.data("layername");
                    for (var i in configData.config.layers) {
                        if (configData.config.layers[i].serviceId == mapId && configData.config.layers[i].layerName == layerName) {
                            configData.config.layers.splice(i, 1);
                            query.superclass.commomUpdateWidget(configData);
                            break;
                        }
                    }
                    returnFields=[];
                    queryFields=[];
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

            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();
            $('#prefixs').dropdownSelect();


            /**
             * 保存
             */
            $("#querySaveBtn").on('click', function (evt) {
                var hasLayers = false;
                var tempConfigData = lang.clone(metadata);
                var form = SerializeForm.serializeObject($("#queryForm"));

                tempConfigData.returnFields =lang.clone(returnFields);
                tempConfigData.queryFields.fields = lang.clone(queryFields);

                configData.label = form.label;
                configData.icon = form.icon;
                tempConfigData.queryFields.prefix = form.prefix;
                tempConfigData.link.tip = form.tip;
                tempConfigData.link.url = form.url;
                tempConfigData.titleField.name = form.titleField;
                tempConfigData.titleField.alias = $("#titleFields .text").text() == "选择标题字段" ? "" : $("#titleFields .text").text();
                console.log(form.titleField);
                delete form.label;
                delete form.icon;
                delete form.url;
                delete form.tip;
                delete form.titleField;
                delete form.prefix;
                //DPF添加
                if (form.queryPaging == 'on') {
                    form.queryPaging = true;
                } else {
                    form.queryPaging = false;
                }

                lang.mixin(tempConfigData, form);

                $.each(configData.config.layers, function (i, n) {
                    if (tempConfigData.serviceId == n.serviceId && tempConfigData.layerName == n.layerName) {
                        lang.mixin(n, tempConfigData);
                        hasLayers = true;
                        return false
                    }
                });

                if (!hasLayers) {
                    configData.config.layers.push(tempConfigData);
                }

                query.superclass.commomUpdateWidget(configData);

            });

        }

        /**
         * 获取属性
         * @param name
         */
        function getField(fieldArr, name) {
            var field = null;
            $.each(fieldArr, function (i, n) {
                if (n.name == name) {
                    field = n;
                    return false;
                }
            });

            return field;
        }

        /**
         * 删除属性数组元素
         * @param fieldArr
         * @param name
         */
        function removeField(fieldArr, name) {
            $.each(fieldArr, function (i, n) {
                if (n.name == name) {
                    fieldArr.splice(i, 1);
                    return false;
                }
            });
        }

        /**
         * 修改查询属性回调监听
         */
        function modifyFieldPropsCall() {
            $("#types, #operators").dropdownSelect();
            $("#modifyFieldPropsBtn").on('click', function () {
                var obj = SerializeForm.serializeObject($("#modifyFieldPropsFrom"));
                $.each(queryFields, function(index, item){
                    if(item.name === obj.name){
                        lang.mixin(item, obj);
                    }
                });
                pkLayer.closeDialog();
            });
        }

        return query;
    });