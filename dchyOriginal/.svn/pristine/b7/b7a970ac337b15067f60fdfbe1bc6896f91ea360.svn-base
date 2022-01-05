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
        "slimScroll",
        "static/js/cfg/core/SerializeForm",
        "text!../template/font-icons.html",
        "static/js/cfg/core/ServiceLayerPane",
        "static/thirdparty/selectize/selectize.min",
        "css!static/thirdparty/selectize/selectize.bootstrap3.css"],
    function (declare, lang, arrayUtils, BaseWidgetCfg, Mustache,SlimScroll, SerializeForm, FontIconsTpl,ServiceLayerPane) {

        var configData = null;   //配置数据
        var tpl = null;    //页面模板
        var template = ''; //对应模板名称
        var allFields = []; //图层对应的所有的字段
        var $returnFieldsSelect = undefined;
        var returnFieldsControl = undefined;
        var $groupFieldsSelect = undefined;
        var groupFieldsControl = undefined;

        var analysis = declare([BaseWidgetCfg], {

            constructor: function () {
                tpl = analysis.arguments[1];
                template = analysis.arguments[2];
            },

            onCreate: function () {
                configData = this;
                configData.sdeConfs = sdeConfs;

                configData.isShowPie = function(){
                    if(this.config.showPie){
                        return 'checked';
                    }

                    return '';
                };

                configData.init(tpl, this);
                delete configData.sdeConfs;
                editFields(configData.config.serviceId, configData.config.layerName, configData.config.layerIndex,configData.config.layerAlias);
                pageListeners();
            }
        });


        /**
         * 填充右侧编辑数据
         * @param mapId
         * @param layerName
         * @param layerIndex
         * @param layerAlias
         */
        function editFields(mapId, layerName, layerIndex, layerAlias) {
            var fieldsTpl = '{{#result}}<div class="item" data-value="{{name}}">{{alias}}</div>{{/result}}';

            $.ajax({
                url: root + '/config/getFields',
                method: 'post',
                async: false,
                data: {id: mapId, layerName: layerAlias},
                success: function (rp) {
                    allFields = rp;

                    $("#titleFields .menu").empty();
                    $("#titleFields .menu").append(Mustache.render(fieldsTpl, {result: rp}));

                    $("#titleFields").dropdownSelect('refresh');
                    $("#fieldSelectL,#fieldSelectR,#groupSelectL,#groupSelectR").empty();
                    $("#URL1,#URL2").val('');

                    $("input[name='serviceId']").eq(0).val(mapId);
                    $("input[name='layerName']").eq(0).val(layerName);
                    $("input[name='layerAlias']").eq(0).val(layerAlias);
                    $("input[name='layerIndex']").eq(0).val(layerIndex);

                    $returnFieldsSelect = $('#returnFieldsSelect').selectize({
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

                    $groupFieldsSelect = $('#groupFieldsSelect').selectize({
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

                    groupFieldsControl = $groupFieldsSelect[0].selectize;
                }
            });

            $("#titleFields").dropdownSelect('clear');
            $("#titleFields").dropdownSelect('set selected', configData.config.titleField);

            $("input[name='url']").eq(0).val(configData.config.link.url);

            /**
             * 查看已有的图层配置
             */
            if(configData.config.layerName == layerName && configData.config.serviceId == mapId){
                $.each(configData.config.returnFields, function (j, m) {
                    returnFieldsControl.addItem(m.name);
                });
                $.each(configData.config.groupFields, function(index, item){
                    groupFieldsControl.addItem(item.name);
                });
            }
        }

        /**
         * 获取到需配置图层后操作
         * @param mapLayer
         * @param parent
         */
        function afterGetLayer(mapLayer, parent) {
            editFields(parent.id, mapLayer.name, mapLayer.index,mapLayer.alias);
        }


        /**
         * 页面监听
         */
        function pageListeners() {
            $("#addService").unbind("click").on("click", function () {
                ServiceLayerPane.show(template, [], afterGetLayer);
            });

            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();
            $('#dataSource').dropdownSelect();
            $('#import').dropdownSelect();

            /**
             * 保存
             */
            $("#analysisSaveBtn").on('click', function(){
                var form = SerializeForm.serializeObject($("#analysisForm"));
                configData.config.returnFields = [];
                configData.config.groupFields = [];
                var returnValues = returnFieldsControl.getValue().split(",");
                var groupValues = groupFieldsControl.getValue().split(",");
                $.each(allFields, function (idx, item) {
                    var index = $.inArray(item.name, returnValues);
                    if (index !== -1) {
                        configData.config.returnFields[index] = item;
                    }
                    var index1 = $.inArray(item.name, groupValues);
                    if (index1 !== -1) {
                        configData.config.groupFields[index1] = item;
                    }
                });
                configData.label = form.label;
                configData.icon = form.icon;
                configData.config.link.url = form.url;

                if(form.showPie == 'on'){
                    form.showPie = true;
                }else{
                    form.showPie = false;
                }

                delete  form.label;
                delete  form.icon;
                delete  form.url;

                lang.mixin(configData.config, form);

                analysis.superclass.commomUpdateWidget(configData);
            });
        }

        return analysis;
    });