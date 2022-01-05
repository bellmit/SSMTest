/**
 * widget配置基类
 * 所有的widget
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 15:19
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    'dojo/topic',
    "mustache",
    "static/js/cfg/tpl-widgets-display",
    "dojo/domReady!"], function (declare, lang, topic, Mustache,widgetsMain) {
    return declare(null, {

        id: '',
        /**
         *
         */
        label: '',
        /**
         *
         */
        icon: '',
        /**
         *
         */
        url: '',
        /**
         * config
         */
        config: {},

        constructor: function () {
            lang.mixin(this, arguments[0]);
        },

        /***
         * 重写此方法
         */
        onCreate: function () {

        },
        /***
         *
         * @param value
         */
        setConfig: function (value) {
            config = value;
            
        },
        /***
         *
         */
        getConfig: function () {
            return config;
        },

        init: function (tpl, data) {
            $("#view").empty();
            $("#view").append(Mustache.render(tpl, data));

            if($("#serviceTree").length>0){
                $("#serviceTree").slimScroll({
                    height: '500px',
                    railVisible: true,
                    railColor: '#333',
                    railOpacity: .2,
                    railDraggable: true
                });
            }

        },

        /**
         * 订阅者监听添加的或者删除的字段
         */
        subscribersListener: function () {
            var layerSubscriber, fieldSubscriber = null;
            //以下为监听
            if (layerSubscriber != null || layerSubscriber != undefined) {
                layerSubscriber.remove();
            }
            //订阅添加图层
            layerSubscriber = topic.subscribe("add/layer", function (a, b) {
                var layerTpl = '<button type="button" class="btn btn-default" data-id="{{name}}" data-info="{{getInfo}}">' +
                    '<span class="text-left buttonTitle">{{alias}}</span>' +
                    '<span class="glyphicon glyphicon-eye-open text-right buttonIcon"></span>' +
                    '</button>';
                if ($("#layersContainer button[data-id='" + a.name + "']").length == 0) {
                    a.getInfo = function () {
                        var info = {
                            layerName: a.name,
                            layerUrl: b + '/' + a.index,
                            returnFields: []
                        }
                        return JSON.stringify(info);
                    }
                    $("#layersContainer").append(Mustache.to_html(layerTpl, a));
                    /**
                     * 删除图层
                     */
                    $("#layers .buttonIcon").on('click', function (evt) {
                        evt.stopPropagation();
                        $(this).parent().detach();
                        $("#fields button[data-id='" + $(this).parent().text().trim() + "']").detach();
                    });

                    $("#layersContainer button").on('click', function () {
                        var textCss = $(this).find(".buttonTitle").text();  //获取每一类样式，默认把每一类的样式设置为名称
                        $("#layerFieldsContainer button").css({'display': 'none'});
                        $("#layerFieldsContainer button[data-id='" + textCss + "']").css({'display': 'block'});
                    });
                }
            });

            $("#layerFieldsContainer button").css({'display': 'none'});

            $("#layersContainer button").on('click', function () {
                var textCss = $(this).find(".buttonTitle").text();  //获取每一类样式，默认把每一类的样式设置为名称
                $("#layerFieldsContainer button").css({'display': 'none'});
                $("#layerFieldsContainer button[data-id='" + textCss + "']").css({'display': 'block'});
            });


            if (fieldSubscriber != null || fieldSubscriber != undefined) {
                fieldSubscriber.remove();
            }

            //订阅添加字段
            fieldSubscriber = topic.subscribe("add/field", function (a, b) {
                var fieldTpl = '<button type="button" class="btn btn-default {{field.name}}" data-id="{{layerName}}" data-key="{{field.name}}" data-info="{{fieldInfo}}">' +
                    '<span class="text-left buttonTitle">{{field.alias}}</span>' +
                    '<span class="glyphicon glyphicon-remove-sign text-right buttonIcon"></span>' +
                    '</button>';
                $("#layerFieldsContainer button").css({'display': 'none'});
                $("#layerFieldsContainer button[data-id='" + a.name + "']").css({'display': 'block'});

                if ($("#layerFieldsContainer button[data-id='" + a.name + "'][data-key='" + b.name + "']").length == 0) {
                    var scopeLayer = $("#layersContainer button[data-id='" + a.name + "']").data('info');
                    scopeLayer.returnFields.push(b);
                    $("#layersContainer button[data-id='" + a.name + "']").attr('data-info', JSON.stringify(scopeLayer));
                    $("#layerFieldsContainer").append(Mustache.render(fieldTpl, {
                        layerName: a.alias,
                        field: b,
                        fieldInfo: function () {
                            return JSON.stringify(b)
                        }
                    }));

                    /**
                     * 删除返回字段
                     */
                    $("#fields .buttonIcon").on('click', function () {
                        var arrays = $("#layers button[data-id='" + $(this).parent().data('id') + "']").data("info");
                        var key = $(this).parent().data('key');
                        var id = $(this).parent().data('id');
                        for (var i = 0; i < arrays.returnFields.length; i++) {
                            if (key == arrays.returnFields[i].name) {
                                arrays.returnFields.splice(i, 1);
                                break;
                            }
                        }
                        $("#layers button[data-id='" + id + "']").attr("data-info", '');
                        $("#layers button[data-id='" + id + "']").attr("info", JSON.stringify(arrays));
                        $(this).parent().detach();
                    });
                }
            });

            /**
             * 删除返回字段
             */
            $("#fields .buttonIcon").on('click', function () {
                var arrays = $("#layers button[data-id='" + $(this).parent().data('id') + "']").data("info");
                var key = $(this).parent().data('key');
                var id = $(this).parent().data('id');
                for (var i = 0; i < arrays.returnFields.length; i++) {
                    if (key == arrays.returnFields[i].name) {
                        arrays.returnFields.splice(i, 1);
                        break;
                    }
                }
                $("#layers button[data-id='" + id + "']").attr("data-info", '');
                $("#layers button[data-id='" + id + "']").attr("info", JSON.stringify(arrays));
                $(this).parent().detach();
            });

            /**
             * 删除图层
             */
            $("#layers .buttonIcon").on('click', function (evt) {
                evt.stopPropagation();
                $(this).parent().detach();
                $("#fields button[data-id='" + $(this).parent().text().trim() + "']").detach();
            });
        },

        /**
         *  范围图层切换时，需要禁用右边的图层配置界面
         */
        toggleScoupeLayer: function () {
            if ($("input[name='showSelMode']").eq(0).attr('checked') == undefined) {
                $("#layersContainer button").addClass("disabled");
                $("#layerFieldsContainer button").css({'display': 'none'});
                $("#layerBtn").addClass("disabled");
            }


            $(".showSelMode").checkbox({
                onChecked: function () {
                    $("#layersContainer button").removeClass("disabled");
                    $("#layerBtn").removeClass("disabled");

                },
                onUnchecked: function () {
                    $("#layersContainer button").addClass("disabled");
                    $("#layerFieldsContainer button").css({'display': 'none'});
                    $("#layerBtn").addClass("disabled");
                }
            });
        },

        /**
         * 通用的修改配置保存功能
         * @param group object
         * @param widget object
         */
        commomUpdateWidget: function (widget) {
            var url = '';
            var params = {};
            var type = null;
            if ('group' in widget) {
                url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                params.widgetsGroup = JSON.stringify({id:widget.group});
                params.widget = JSON.stringify(widget);
                type = 'group-widget';
            } else {
                url = root + '/config/tpl/' + tpl + '/widget/save';
                params.widget = JSON.stringify(widget);
                type = 'widget'
            }
            $.ajax({
                'url': url,
                method: 'post',
                data: params,
                success: function (rp) {
                    if (rp == '') {
                        layer.msg('修改成功！', {
                            icon: 1,
                            time: 1000
                        }, function () {
                            if ('group' in widget) {
                                widgetsMain.updateGlobeConfig('widgetsGroup', widget);
                            } else {
                                widgetsMain.updateGlobeConfig('widget', widget);
                            }

                            //renderTpl();
                            //loadDataState(data.id, type);
                            //renderConfigPage(confType, data);
                        });
                    }
                }
            });
        },
        /**
         * 更新全局配置
         * @param targetType   更新类型
         * @param targetConfig 需更新的目标配置
         */
        updateGlobeConfig: function (targetType, targetConfig){
            widgetsMain.updateGlobeConfig(targetType, targetConfig);
        },

        /**
         * 初始化树结构
         *
         * @param template  对应模板名称
         * @param treeDataArray  树结构的组织数组
         * @param treeView  treeView的回调函数
         */
        initTree: function (template, treeDataArray, treeView) {
            var allLayers = null;
            if(treeDataArray== null || treeDataArray == undefined || treeDataArray.length > 0){
                treeDataArray = [];
            }

            $.ajax({
                url: root + '/config/tpl/' + template + '/service/layers',
                async: false,
                method: 'post',
                data: {},
                success: function (rp) {
                    allLayers = rp;

                    $.each(rp, function (i, n) {
                        var item = {
                            text: n.alias,
                            nodes: []
                        };

                        if (n.layers.length > 0) {
                            $.each(n.layers, function (j, m) {
                                var child = {
                                    text: m.alias + '<span data-alias="'+ m.alias+'" data-mapid="' + n.id + '" data-layername="' + m.name.trim() + '" data-index="' + m.index + '"></span>'
                                };

                                item.nodes.push(child);
                            });
                        }
                        treeDataArray.push(item);
                    });
                }
            });
            treeView(treeDataArray);

            return allLayers;
        }
    })
});