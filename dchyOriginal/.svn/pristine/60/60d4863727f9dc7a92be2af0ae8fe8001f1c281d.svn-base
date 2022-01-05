define([
    "../../../thirdparty/agsapi/3.14/dojo/_base/array",
    "dojo/_base/lang",
    "dojo/topic",
    "mustache",
    "slimScroll",
    "layer",
    "static/js/cfg/core/packed.layer",
    "text!../template/widgets/layer_config_modal.html"
], function (arrayUtils, lang, topic, Mustache, slimScroll, layer, pkLayer, layerConfigModal) {
    var tree = null;

    /**
     * 谈框图层【返回值】配置
     * @param $selector
     */
    function configLayer($selector) {
        tree = [];
        $.ajax({
            url: root + '/config/tpl/' + tpl + '/service/layers',
            async: false,
            method: 'post',
            data: {},
            success: function (rp) {
                $.each(rp, function (i, n) {
                    var item = {
                        text: n.alias,
                        nodes: [],
                        icon: "glyphicon glyphicon-chevron-bookmark",
                        selectedIcon: "glyphicon glyphicon-down"
                    };

                    if (n.layers.length > 0) {

                        $.each(n.layers, function (j, m) {
                            var child = {
                                text: m.alias + '<span class="glyphicon glyphicon-hand-right pull-right point-right" data-params="{\'id\':\''
                                + n.id + '\',\'layerName\':\'' + m.name + '\'}" data-info=\'' + JSON.stringify(m) + '\' data-url="' + n.url + '"></span>',
                                selectable: true
                            }

                            item.nodes.push(child);
                        });
                    }
                    tree.push(item);
                });
            }
        });
        pkLayer.showSimpleDialog($selector, '配置返回字段', Mustache.render(layerConfigModal), afterRender, {area:  ['900px', '550px']});
    }

    /**
     * 谈框后回调
     */
    function afterRender() {
        $("#serviceTree").empty();
        $('#serviceTree').treeview({
            data: tree,         // data is not optional
            levels: 2,
            color: "#428bca",
            expandIcon: 'glyphicon glyphicon-chevron-right',
            collapseIcon: 'glyphicon glyphicon-chevron-down',
            nodeIcon: 'glyphicon glyphicon-bookmark',
            highlightSelected: true
        });

        /**
         * 1.发布两个自定义事件
         * 2.选中图层节点展示图层字段
         */
        $('#serviceTree').on('nodeSelected', function (event, data) {
            //添加图层事件
            var params = $("#serviceTree li[data-nodeid=" + data.nodeId + "]").find("span:last").data('params');
            var info = $("#serviceTree li[data-nodeid=" + data.nodeId + "]").find("span:last").data('info');
            var url = $("#serviceTree li[data-nodeid=" + data.nodeId + "]").find("span:last").data('url');
            topic.publish("add/layer", info, url);

            $.ajax({
                url: root + '/config/getFields',
                method: 'post',
                data: jQuery.parseJSON(params.replace(/\'/g, '\"')),
                success: function (rp) {
                    $("#fieldRows").empty();
                    var rowTpl = '{{#result}}' +
                        "<tr data-fieldInfo='{{info}}'>" +
                        '<td class="text-center">' +
                        '<span class="glyphicon glyphicon-log-in add-field"></span>' +
                        '</td>' +
                        '<td class="text-center">{{alias}}</td>' +
                        '<td class="text-center">{{type}}</td>' +
                        '</tr>"{{/result}}';
                    $("#fieldRows").append(Mustache.render(rowTpl, {
                        result: rp, info: function () {
                            return JSON.stringify(this)
                        }
                    }));

                    /**
                     * 添加字段监听
                     */
                    $(".add-field").on('click', function (evt) {
                        evt.stopPropagation();
                        var fieldInfo = $(this).parent().parent().data("fieldinfo");
                        topic.publish("add/field", info, fieldInfo);
                    });
                }
            });
        });


        $("#opFields,#serviceTree").slimScroll({
            height: '500px',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    return {
        configLayer: configLayer
    }
});