/**
 * Author: <a href="mailto:monarchcheng1993@gmail.com">wangcheng</a>
 * Date: 2016-12-02
 * 调用show方法，参数为：template（模板名称）,existed（已配置图层数组）,callBack（回调函数）
 * callBack返回参数为选择的图层信息，以及它所属服务信息
 */
define(["dojo/_base/lang",
        "dojo/_base/array",
        "dojo/topic",
        "layer",
        "handlebars",
        "slimScroll",
        "text!static/js/cfg/template/ServiceLyrPaneTpl.html"
],function (lang, arrayUtils,topic,layer, Handlebars,SlimScroll, ServiceLyrPaneTpl) {
    var  serviceData = undefined;
    var existedLayers = [];

    /**
     * 获取服务数据
     * @param template
     * @param callback
     */
    function getData(template,callback) {
        $.ajax({
            url: root + '/config/tpl/' + template + '/service/layers',
            async: false,
            method: 'post',
            data: {},
            success: function (rp) {
                serviceData = rp;
                var serviceTree = {
                    text: "所有服务",
                    icon:"glyphicon glyphicon-tree-deciduous",
                    nodes: []
                };

                var treeArr = [];
                $.each(rp, function (i, n) {
                    serviceTree.nodes.push({icon:"glyphicon glyphicon-globe",text:n.alias,serviceId: n.id});
                });
                treeArr.push(serviceTree);
                treeView(treeArr,callback);

                $("#serviceSearchInput").on("keyup", function(evt){
                    var value = $(this)[0].value;
                    if (value == "" || value == undefined) {
                        treeView(treeArr,callback);
                    } else {
                        var services = arrayUtils.filter(serviceTree.nodes, function (item) {
                            if (item.text.toUpperCase().indexOf(value.toUpperCase()) > -1) {
                                return item;
                            }
                        });

                        var serviceSearchTree = {
                            text: "检索服务",
                            icon:"glyphicon glyphicon-tree-deciduous",
                            nodes: []
                        };

                        var treeSearchArr = [];
                            serviceSearchTree.nodes=services;
                        treeSearchArr.push(serviceSearchTree);
                        treeView(treeSearchArr,callback);
                    }

                });
            }
        });
    }


    /**
     * 展示数据框
     * @param template
     * @param existed
     * @param callBack
     */
    function showInLayerPane(template,existed,callBack) {
        existedLayers = existed;
        layer.open({
            title: "服务图层信息",
            type: 1,
            shadeClose: true,
            shade: false,
            content: ServiceLyrPaneTpl,
            area: ['800px', '500px'],
            success: function () {
                getData(template,callBack);
                return serviceData;
            }
        });
    }

    /**
     * 服务树
     * @param tree
     * @param callBack
     */
    function treeView(tree,callBack){
        $('#serviceItem').treeview({
            data: tree,         // data is not optional
            levels: 2,
            color: "#428bca",
            showCheckbox: false
        });

        $('#serviceItem').on('nodeSelected', function (event, data) {
            var result=[];
            $("#layers").empty();
            var layersDom = '{{#result}}<tr><td style="width: 10%;line-height: 30px;" class="text-center">{{addOne @index}}</td>' +
                ' <td style="width: 70%;line-height: 30px;" class="text-center">{{alias}}</td>' +
                '<td style="width: 20%;line-height: 30px;" class="text-center">'+
                '<button class="btn btn-primary btn-sm return-layer-info" data-id="{{id}}" {{available}}>添加</button></td></tr>{{/result}}';
            $.each(serviceData, function (i, n) {
                if (n.id == data.serviceId) {
                    result = lang.clone(n.layers);
                    for (var j in existedLayers) {
                        for (var i in result) {
                            if (result[i].name == existedLayers[j].layerName) {
                                result[i].available = "disabled";
                            }
                        }
                    }

                }
            });
            Handlebars.registerHelper("addOne", function (index, options) {
                return parseInt(index) + 1;
            });
            var renderTpl = Handlebars.compile(layersDom);
            $("#layers").append(renderTpl({result:result}));

            $(".return-layer-info").unbind("click").on('click',function(){
                var layerId = $(this).data('id');
                for(var i in serviceData){
                    for(var j in serviceData[i].layers){
                        var selectedLayer =serviceData[i].layers[j];
                        if(selectedLayer.id == layerId){
                            callBack(selectedLayer,serviceData[i]);
                            layer.closeAll();
                            layer.msg("图层选择成功，请继续完成配置！",{icon:1,time:1500});
                        }
                    }
                }
            });
        });

        $("#serviceItem").slimScroll({
            height: '430px',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        $("#layersContainer").slimScroll({
            height: '400px',
            width:'535px',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    return {
        show:showInLayerPane
    };
});