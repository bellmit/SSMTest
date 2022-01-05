/**
 * Created by zhayw on 2016/6/13.
 */
define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "static/js/cfg/core/BaseWidgetCfg",
    "static/js/cfg/core/SerializeForm",
    "text!../template/font-icons.html",
    "mustache",
    "layer",
    "text!static/js/cfg/template/newprolayer-info.html",
    "text!static/js/cfg/template/projectType-info.html",
    "css!static/thirdparty/bootstrap-colorpicker/css/bootstrap-colorpicker.min.css",
    "static/thirdparty/bootstrap-colorpicker/js/bootstrap-colorpicker.min"
], function (declare, lang, BaseWidgetCfg, SerializeForm, FontIconsTpl, Mustache, layer, proLayerTpl, proTypeTpl) {
    var configData = null;   //配置数据
    var tpl = null;    //页面模板
    var template = ''; //对应模板名称

    var proTypeHandler;
    var proType=[];//项目类型
    var newProjectLayer=[];//项目识别图层信息

    var videoManager = declare([BaseWidgetCfg], {
        constructor: function () {
            tpl = videoManager.arguments[1];
            template = videoManager.arguments[2];
        },

        onCreate: function () {
            configData = this;

            if (this.config.proType != undefined)
                proType = this.config.proType;

            if(this.config.newProjectLayer!=undefined)
                newProjectLayer=this.config.newProjectLayer;

            configData.isSupervise = function(){
                if(this.config.supervise == true)
                    return "checked";
                return "";
            };

            configData.isModifyProVideo = function(){
                if(this.config.modifyProVideo == true)
                    return "checked";
                return "";
            };

            configData.isPanoramaSwitch = function(){
                if(this.config.panoramaSwitch == 'on')
                    return "checked";
                return "";
            };

            configData.isMultipleVideo = function(){
                if(this.config.multipleVideo == true)
                    return "checked";
                return "";
            };

            configData.isExportXls = function(){
                if(this.config.exportXls == true)
                    return "checked";
                return "";
            };

            configData.isAddProjectByCamera = function(){
                if(this.config.addProjectByCamera == true)
                    return "checked";
                return "";
            };

            configData.isShowStatus = function(){
                if(this.config.showStatus == true)
                    return "checked";
                return "";
            };


            this.init(tpl, this);
            addListeners();
        }
    });

    function addListeners(){
        $(".color").colorpicker();
        $("#icons").append(FontIconsTpl);
        //生成字体大小下拉item
        $("#fontSize").append("<div class='menu'></div>");
        for (var i = 5;i <= 30;i++){
            $("#fontSize .menu").append("<div class='item' data-value=" + i + ">" + i + "pt</div>");
        }
        $(".ui.dropdown").dropdownSelect();
        $.ajax({
            url: root + '/config/tpl/' + template + '/service/layers',
            async: false,
            method: 'post',
            data: {},
            success: function (rp) {
                $('#layerContent').empty();
                for(var i in rp){
                    var layer = rp[i].layers;
                    var html = '<div class="item" id="layer_{{id}}"  data-id="{{id}}" data-name="{{alias}}" data-returnname="">{{alias}}</div>';
                    for(var j in layer){
                        $('#layerContent').append(Mustache.render(html,{id:rp[i].id,alias:layer[j].alias}));
                    }
                }
            }
        });
        //显示配置中的项目关联字段
        var newProLayerInfo = [];
        $.each(newProjectLayer, function(index, item){
            newProLayerInfo.push(item.layerName);
        });
        $("#newProjectLayer").val(newProLayerInfo.toString());
        setTimeout(function () {
            newProLayerListener();
        }, 1500);

        var id = $('#serviceId').val();
        $('#layer_'+id).trigger('click');

        delProType();
        $("#proTypeList #addProType").on('click',function(){
            if(proTypeHandler!=undefined) layer.close(proTypeHandler);
            var content = Mustache.render(proTypeTpl);
            proTypeHandler =  layer.open({
                title: "详情",
                type: 1,
                shade: 0,
                zIndex:0,
                area: ['300px', '170px'],
                content: content
            });
            $(".typeColor").colorpicker();

            $("#saveProType").on('click',function(){
                var name =$("#typeName").val();
                var color =$("#typeColorInput").val();
                if(name==""||color==""){
                    layer.msg("请将信息填写完整！",{time:1000})
                    return;
                }
                var p = {name: name, color: color};
                proType.push(p);
                var html = '<a class="ui label" style="color: {{color}};">{{name}}<i class="delete icon pro-type-del" data-name="{{name}}" data-color="{{color}}"></i></a>';
                $("#proTypeList").append(Mustache.render(html,{name:name,color:color}));
                $("#projectManagerSaveBtn").click();
                delProType();
                layer.close(proTypeHandler);
            });

        });

        $("#videoManagerSaveBtn").on('click', function(){
            var form = SerializeForm.serializeObject($("#videoManagerForm"));
            configData.label = form.label;
            configData.icon = form.icon;
            if(form.showStatus && form.showStatus=='on'){
                configData.config.showStatus = true;
                delete form.showStatus;
            }else {
                configData.config.showStatus = false;
            }
            var txtSymbol = {};
            txtSymbol.color = form.txtSymbol_color;
            txtSymbol.fontSize = form.txtSymbol_fontSize;
            configData.config.txtSymbol=txtSymbol;
            delete form.label;
            delete form.icon;
            delete form.txtSymbol_color;
            delete form.txtSymbol_fontSize;

            lang.mixin(configData.config, form);

            $.ajax({
                url: root + '/config/tpl/'+template+"/fix/widget/update",
                method:'post',
                data:{
                    fixWidget: JSON.stringify(configData)
                },
                success:function(rp){
                    if (rp == '')
                        layer.msg('修改成功！', {
                            icon: 1,
                            time: 1000
                        }, function(){
                            $(".fixWidget").each (function(){
                                if($(this).data("id") == configData.id){
                                    $(this).attr("data-info", JSON.stringify(configData));
                                    return false;
                                }
                            });
                        });
                    else
                        layer.msg(rp.message, {
                            icon: 5,
                            time: 1000
                        });
                }
            });
        });

        $("#projectManagerSaveBtn").on('click', function(){
            var form = SerializeForm.serializeObject($("#projectManagerForm"));
            configData.config.newProjectLayer = newProjectLayer;
            delete form.newProjectLayer;
            configData.config.inspectRecordTimeLimit = form.inspectRecordTimeLimit;
            delete form.inspectRecordTimeLimit;

            if(form.supervise && form.supervise=='on'){
                configData.config.supervise = true;
                delete form.supervise;
            }else {
                configData.config.supervise = false;
            }

            if(form.modifyProVideo && form.modifyProVideo=='on'){
                configData.config.modifyProVideo = true;
                delete form.modifyProVideo;
            }else {
                configData.config.modifyProVideo = false;
            }

            if(form.multipleVideo && form.multipleVideo=='on'){
                configData.config.multipleVideo = true;
                delete form.multipleVideo;
            }else {
                configData.config.multipleVideo = false;
                configData.config.modifyProVideo = false;
            }

            if(form.exportXls && form.exportXls=='on'){
                configData.config.exportXls = true;
                delete form.exportXls;
            }else {
                configData.config.exportXls = false;
            }

            if(form.addProjectByCamera && form.addProjectByCamera=='on'){
                configData.config.addProjectByCamera = true;
                delete form.addProjectByCamera;
            }else {
                configData.config.addProjectByCamera = false;
            }

            lang.mixin(configData.config, form);

            configData.config.proType=proType;

            $.ajax({
                url: root + '/config/tpl/'+template+"/fix/widget/update",
                method:'post',
                data:{
                    fixWidget: JSON.stringify(configData)
                },
                success:function(rp){
                    if (rp == '')
                        layer.msg('修改成功！', {
                            icon: 1,
                            time: 1000
                        }, function(){
                            $(".fixWidget").each (function(){
                                if($(this).data("id") == configData.id){
                                    $(this).attr("data-info", JSON.stringify(configData));
                                    return false;
                                }
                            });
                        });
                    else
                        layer.msg(rp.message, {
                            icon: 5,
                            time: 1000
                        });
                }
            });
        });

        $('#proLayerSelect').dropdownSelect({
            action: 'select',
            keys: false,
            metadata: {
                toggle: 'popover',
                trigger: 'focus'
            },
            onChange: function (value, text, $selectedItem) {
                // custom action
                $("#proLayerSelect .label").popover('hide');
                $(".popover ").detach();
            },
            onAdd: function (value, text, $selected) {
                setTimeout(function () {
                    newProLayerListener();
                }, 1500);
            }
        })
    }

    /**
     * 删除项目类型
     */
    function delProType() {
        $(".pro-type-del").unbind("click").on('click', function () {
            var color = $(this).data("color");
            var name = $(this).data("name");

            for (var i in proType) {
                if (proType[i].color == color && proType[i].name == name) {
                    proType.splice(i, 1);
                }
            }
            $(this).parent().remove();
            $("#projectManagerSaveBtn").click();
        });
    }

    /**
     * 删除项目需要识别的图层
     */
    function delProLayer() {
        $("#proLayerSelect .label .delete").unbind().on("click", function () {
            var name = $(this).parent().data("value").toUpperCase();
            for (var i in newProjectLayer) {
                if (newProjectLayer[i].layerName.toUpperCase() == name) {
                    newProjectLayer.splice(i, 1);
                    $("#projectManagerSaveBtn").click();
                }
            }
        });
    }

    function newProLayerListener(){
        delProLayer();
        //初始化popover
        $("#proLayerSelect .label:not(.delete)").popover({
            title: '配置字段',
            trigger: 'click',
            html: true,
            container: 'body',
            animation: true,
            placement: 'top',
            content: function () {
                if ($(".popover ").length != 0) {
                    $("#proLayerSelect .label").popover('hide');
                    $(".popover").detach();
                }
                var value = $(this).data('value').toLocaleUpperCase();
                var $data = $("#proLayerSelect .menu div[data-name='" + value + "']");
                var _returnName="";
                for(var i in newProjectLayer){
                    if(newProjectLayer[i].layerName==($data).data("name"))
                    _returnName=newProjectLayer[i].returnName;
                }
                var content = Mustache.render(proLayerTpl,{mapId: $($data).data("id"),layerName: $($data).data("name"), returnName:_returnName});
                return content;
            }
        });
        //追加popover监听
        $("#proLayerSelect .label").on('shown.bs.popover', function () {
            var mapId = $("#serviceId").val();
            var layerName = $("#layerName").val();
            $.ajax({
                url: root + '/config/getFields',
                method: 'post',
                async: false,
                data: {id: mapId, layerName: layerName},
                success: function (rp) {
                    var html = '<div class="item" data-name="{{alias}}">{{alias}}</div>';
                    $('#returnNameContent').empty();
                    for (var i in rp) {
                        $('#returnNameContent').append(Mustache.render(html, rp[i]));
                    }
                    $('#returnNameContent .item').unbind('click').on('click', function () {
                        var returnName = $(this).data("name");
                        $('#returnName').val(returnName);
                    });
                }
            });
            $("#returnNameShow").dropdownSelect();

            /**
             *  修改layer中的信息
             */
            $("#layerOkBtn").on('click', function (evt) {
                evt.stopPropagation();
                var data = SerializeForm.serializeObject($("#newproLayerForm"));
                for (var i in newProjectLayer) {
                    if (newProjectLayer[i].layerName == data.layerName){
                        newProjectLayer.splice(i, 1);
                        break;
                    }
                }
                if (data.layerName != undefined)
                    newProjectLayer.push(data);
                $(".popover").detach();
                $("#projectManagerSaveBtn").click();
            });

            $("#layerCancelBtn").on('click', function (evt) {
                evt.stopPropagation();
                $("#proLayerSelect .label").popover('hide');
                $(".popover ").detach();
            });
        });

    }

    return videoManager;
});