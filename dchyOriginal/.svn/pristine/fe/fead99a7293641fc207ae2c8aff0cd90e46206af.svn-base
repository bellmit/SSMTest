/**
 * Author: <a href="mailto:monarchcheng1993@gmail.com">wangcheng</a>
 * Date: 2016-12-27
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
    var jctbAnalysis = declare([BaseWidgetCfg], {
        constructor: function () {
            tpl = jctbAnalysis.arguments[1];
            template = jctbAnalysis.arguments[2];
        },

        onCreate: function () {
            configData = this;
            configData.isOpen = function () {
                if (this.open == true)
                    return "checked";
                return "";
            };
            this.init(tpl, this);
            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();
            renderLayersInfo();
            $("#addLayer").addClass("disabled");
            pageListeners();
            xzPageListeners();
        }
    });

    /**
     * 加载已配置信息
     */
    function renderLayersInfo() {
        var layerInfoTpl = '{{#layers}} <a href="#" class="list-group-item analysisAlias" data-fid="{{fid}}" data-service="{{serviceId}}" data-layername="{{layerName}}" data-title="{{title}}">' +
            '<span>{{title}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
            '</a>{{/layers}}';
        $("#jctbAnalysisLayers .list-group").append(Mustache.render(layerInfoTpl, {layers: configData.config.analysis.layers}));
        $("#jctbAnalysisLayers").slimScroll({
            height: 350,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });


        var xzLayerInfoTpl = '{{#years}} <a href="#" class="list-group-item xzYear" data-year="{{year}}" data-xzdw="{{xzdw}}" data-dltb="{{dltb}}">' +
            '<span>{{year}}</span>' +
            '<span class="glyphicon glyphicon-remove-sign pull-right delete-year"></span>' +
            '</a>{{/years}}';
        $("#xzAnalysisYears .list-group").append(Mustache.render(xzLayerInfoTpl, {years: configData.config.analysis.tdlyxz}));
        $("#xzAnalysisYears").slimScroll({
            height: 300,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    /**
     * 页面监听
     */
    function pageListeners(){
        //点击左侧列表事件
        $(".analysisAlias").unbind("click").on("click",function(){
            $("#commonAnalysis").show();
            $("#xzAnalysis").hide();
            $("#jctbCommonAnalysisForm input").attr("readonly","readonly");
            var fid = $(this).data("fid");
            var analysisInfo={};
            for (var i in configData.config.analysis.layers) {
                if (configData.config.analysis.layers[i].fid == fid)
                    analysisInfo = configData.config.analysis.layers[i];
            }
            var title = $(this).data("title");
            $("#fid").val(fid);
            $("#title").val(title);
            $("#addLayer").removeClass("disabled");
            editFields(analysisInfo.serviceId,analysisInfo.layerName,analysisInfo.fields)
        });

        //选择分析图层
        $("#addLayer").unbind("click").on("click",function(){
            ServiceLayerPane.show(template,[],afterGetLayer);
        });

       //向右监听
        $("#toRight").unbind("click").on('click', function () {
            $("#selectL").find("option:selected").each(function () {
                $(this).remove().appendTo($("#selectR"));
            });
        });

        //向左监听
        $("#toLeft").unbind("click").on('click', function () {
            $("#selectR").find("option:selected").each(function () {
                $(this).remove().appendTo($("#selectL"));
            });
        });

        //一般分析保存
        $("#jctbAnalysisSaveBtn").unbind("click").on('click', function () {
            configData.open = $("#open").get(0).checked;
            configData.icon = $("#icon").val();
            configData.config.analysis.dataSource = $("#dataSource").val();
            configData.config.analysis.unit.alias = $("#unit_alias").val();
            configData.config.analysis.unit.format =$("#unit_format").val();
            var layerInfo = SerializeForm.serializeObject($("#jctbCommonAnalysisForm"));
            var fieldArr =[];
            $("#selectR").find("option").each(function () {
                var value = this.value;
                fieldArr.push(value);
            });
            layerInfo.fields =fieldArr.join();
            var notExisted = true;
            for (var i in configData.config.analysis.layers) {
                var layer = configData.config.analysis.layers[i];
                if (layer.fid == layerInfo.fid){
                    notExisted = false;
                    configData.config.analysis.layers.splice(i, 1);
                }
            }
            if (layerInfo.fid != undefined && layerInfo.fid != "") {
                configData.config.analysis.layers.push(layerInfo);
                if (notExisted) {
                    var tpl = '<a href="#" class="list-group-item analysisAlias" data-fid="{{fid}}" data-service="{{serviceId}}" data-layername="{{layerName}}" data-title="{{title}}">' +
                        '<span>{{title}}</span>' +
                        '<span class="glyphicon glyphicon-remove-sign pull-right delete-layer"></span>' +
                        '</a>';
                    $("#jctbAnalysisLayers .list-group").append(Mustache.render(tpl, layerInfo));
                    pageListeners();
                }
            }
            jctbAnalysis.superclass.commomUpdateWidget(configData);
        });

        //删除
        $(".analysisAlias .delete-layer").unbind("click").on("click", function (evt) {
            var layerDom = $(this).parent();
            layer.confirm("确定要删除此图层吗？", function (index) {
                evt.stopPropagation();
                var fid = layerDom.data("fid");
                for (var i in configData.config.analysis.layers) {
                    if (configData.config.analysis.layers[i].fid == fid) {
                        configData.config.analysis.layers.splice(i, 1);
                        jctbAnalysis.superclass.commomUpdateWidget(configData);
                        break;
                    }
                }
                layerDom.remove();
                _clear();
                layer.close(index);
            });
        });

        //添加分析
        $("#addAnalysis").unbind("click").on("click",function(){
            _clear();
            $("#commonAnalysis").show();
            $("#xzAnalysis").hide();
            $("#addLayer").removeClass("disabled");
            $("#jctbCommonAnalysisForm input").removeAttr("readonly");
            layer.msg("现在请填写详细信息!",{icon:1,time:1500});
        });

        //土地利用现状分析
        $("#txlyxzAnalysisAlias").unbind("click").on("click",function(){
            $("#commonAnalysis").hide();
            $("#xzAnalysis").show();
        });
    }


    /**
     * 现状分析监听
     */
    function xzPageListeners(){
        //点击年度
        $(".xzYear").unbind("click").on("click",function(){
            var year = $(this).data("year");
            for (var i in configData.config.analysis.tdlyxz) {
                if (configData.config.analysis.tdlyxz[i].year == year) {
                    $("#year").val(year);
                    $("#xzdw").val(configData.config.analysis.tdlyxz[i].xzdw);
                    $("#dltb").val(configData.config.analysis.tdlyxz[i].dltb);
                }
            }
        });

        //点击保存
        $("#jctbAnalysisXzSaveBtn").unbind("click").on("click", function () {
            configData.open = $("#open").get(0).checked;
            configData.icon = $("#icon").val();
            configData.config.analysis.dataSource = $("#dataSource").val();
            configData.config.analysis.unit.alias = $("#unit_alias").val();
            configData.config.analysis.unit.format = $("#unit_format").val();
            var yearInfo = SerializeForm.serializeObject($("#xzInfo"));
            var notExisted = true;
            for (var i in configData.config.analysis.tdlyxz) {
                if (yearInfo.year == configData.config.analysis.tdlyxz[i].year) {
                    notExisted = false;
                    configData.config.analysis.tdlyxz.splice(i, 1);
                }
            }
            if (yearInfo.year != undefined && yearInfo.year != "") {
                configData.config.analysis.tdlyxz.push(yearInfo);
                if (notExisted) {
                    var tpl = '<a href="#" class="list-group-item xzYear" data-year="{{year}}" data-xzdw="{{xzdw}}" data-dltb="{{dltb}}">' +
                        '<span>{{year}}</span>' +
                        '<span class="glyphicon glyphicon-remove-sign pull-right delete-year"></span>' +
                        '</a>';
                    $("#xzAnalysisYears .list-group").append(Mustache.render(tpl, yearInfo));
                    xzPageListeners();
                }
            }
            jctbAnalysis.superclass.commomUpdateWidget(configData);
        });

        //添加
        $("#addXzAnalysis").unbind("click").on("click",function(){
            _clear();
            layer.msg("请在右侧填写详细信息!",{icon:1,time:1500});
        });

        //删除
        $(".xzYear .delete-year").unbind("click").on("click", function (evt) {
            var layerDom = $(this).parent();
            layer.confirm("确定要删除此年度吗？", function (index) {
                evt.stopPropagation();
                var year = layerDom.data("year");
                for (var i in configData.config.analysis.tdlyxz) {
                    if (configData.config.analysis.tdlyxz[i].year == year) {
                        configData.config.analysis.tdlyxz.splice(i, 1);
                        jctbAnalysis.superclass.commomUpdateWidget(configData);
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
     * 清除表单数据
     * @private
     */
    function _clear(){
        $(".analysis-container input").val("");
        $("#selectL").empty();
        $("#selectR").empty();
    }


    /**
     * 获取图层后处理
     * @param layer
     * @param parent
     */
   function afterGetLayer(layer,parent){
        editFields(parent.id,layer.name);
   }

    /**
     * 编辑返回字段等信息
     * @param mapId
     * @param layerName
     * @param fields
     */
    function editFields(mapId, layerName,fields) {
        $("#selectL").empty();
        $("#selectR").empty();
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
        $("#layerName").val(layerName);
        $("#serviceId").val(mapId);
        /**
         * 查看已有的图层配置
         */
        if (fields) {
            var arr = fields.split(",");
            $.each(arr, function (j, m) {
                $("#selectL option[value='" + m + "']").remove().appendTo($("#selectR"));
            });
        }
    }


    return jctbAnalysis;
});