/**
 *  综合分析配置
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 15:51
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
        "dojo/_base/lang",
        'dojo/topic',
        "dojo/_base/array",
        "static/js/cfg/core/BaseWidgetCfg",
        "mustache",
        "slimScroll",
        "validator",
        "static/js/cfg/core/SerializeForm",
        "static/js/cfg/core/packed.layer",
        "static/js/cfg/core/layer-config",
        "text!../template/font-icons.html"],
    function (declare, lang, topic, arrayUtil, BaseWidgetCfg, Mustache, SlimScroll, validator, SerializeForm, pkLayer, layerConfig, FontIconsTpl) {

        var configData = null;
        var tpl = null;
        var allAlias = null;  //所有的别名

        var multipleAnalysis = declare([BaseWidgetCfg], {
            constructor: function () {
                tpl = multipleAnalysis.arguments[1];
            },
            onCreate: function () {
                configData = this;
                configData.init(tpl, this);
                initAnalysisType();
                // layerConfig.configLayer($('#layerBtn'));
                pageListeners();
                subscriberListener();
            }
        });

        /**
         * 初始化左侧分析类别
         */
        function initAnalysisType() {
            $("#analysisType").empty();
            $("#analysisType").append(Mustache.render($("#analysisList").html(), {}));
            $.ajax({
                url: root + '/config/analysis/config/alias',
                method: 'post',
                data: {},
                async: false,
                success: function (rp) {
                    allAlias = rp;
                    arrayUtil.forEach(config.jsonParams,function (item) {
                        var id = item.funid;
                        if(!item.hasOwnProperty("alias") && rp.hasOwnProperty(id)){
                            item.alias = rp[id];
                        }
                    });
                    $("#analysisType").empty();
                    $("#analysisType").append(Mustache.render($("#analysisList").html(), {}));
                    $.each($("#analysisType .list-group-item"), function () {
                        if($(this).data('funid')=='xz'){
                            $(this).attr("data-tpl", 'analysisTpl2');
                        }else if($(this).data('funid')=='gh'){
                            $(this).attr("data-tpl", 'analysisTpl3');
                        }else{
                            $(this).attr("data-tpl", 'analysisTpl1');
                        }
                    });
                }
            });
        }

        /**
         * 页面监听
         */
        function pageListeners() {
            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();
            $("#import").dropdownSelect();
            $('.ui.accordion').accordion();

            $("#analysisType").slimScroll({
                height: '300px',
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true,
                disableFadeOut: true,
                alwaysVisible: false
            });

            var scrollHeight = $(window).height()-400;
            $("#scrollAttr").slimScroll({
                height: scrollHeight>360?360:scrollHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true,
                disableFadeOut: true,
                alwaysVisible: false
            });

            /**
             * 点击分类选择模板
             */
            $("#analysisType .list-group-item").off("click").on('click', function () {
                var formData = SerializeForm.serializeObject($("#analysisForm"));
                var tplType = $(this).data('tpl');
                var data = null;
                var funid = $(this).data('funid');

                /**************************** 现状模板 ********************************/
                var layerTpl = '<div class="row">'+
                    '<div class="col-xs-2 lableTitle text-right">地类图斑</div>' +
                    '<div class="col-xs-10">'+
                    '<input type="text" name="dltb" class="form-control" id="dltb" placeholder="请填写地类图斑" value="{{dltb}}"/>'+
                    '</div>'+

                    '</div>'+
                    '<div class="row" style="margin-top:10px;">'+
                    '<div class="col-xs-2 lableTitle text-right">线状地物</div>'+
                    '<div class="col-xs-10">'+
                    '<input type="text" name="xzdw" class="form-control" id="xzdw" placeholder="请填写线状地物" value="{{xzdw}}"/>'+
                    '</div>'+
                    '</div>';

                var singleTpl = '<div class="col-xs-4">' +
                        '<input type="text" name="year" class="form-control" id="year" placeholder="请填写年份" value="{{year}}"/>' +
                    '</div>' +
                    '<div class="col-xs-4">' +
                        '<input type="text" name="dltb" class="form-control" id="dltb" placeholder="地类图斑图层名" value="{{dltb}}"/>' +
                    '</div>' +
                    '<div class="col-xs-4">' +
                        '<input type="text" name="xzdw" class="form-control" id="xzdw" placeholder="线状地物图层名" value="{{xzdw}}"/>' +
                    '</div>';

                var multipleTpl = '{{#year}}' +
                    '<div class="row" style="margin-bottom: 5px">'+
                    '<div class="col-xs-3">'+
                    '<input type="text" name="year" class="form-control" disabled placeholder="请填写年份" value="{{year}}"/>'+
                    '</div>' +
                    '<div class="col-xs-4">' +
                    '<input type="text" name="dltb" class="form-control" disabled placeholder="请填写地类图斑" value="{{dltb}}"/>'+
                    '</div>' +
                    '<div class="col-xs-4">'+
                    '<input type="text" name="xzdw" class="form-control" disabled placeholder="请填写线状地物" value="{{xzdw}}"/>'+
                    '</div>' +
                    '<div class="col-xs-1">'+
                        '<span class="glyphicon glyphicon-remove-sign" style="line-height: 34px;"></span>'+
                    '</div>' +
                    '</div>'+
                    '{{/year}}';

                $.each(configData.config.jsonParams, function(i, n){
                    if(n.funid == funid){
                        data = n;
                        return false;
                    }
                });
                $("#tplPostion").empty();
                $("#tplPostion").append(Mustache.render($("#" + tplType + "").html(), data));
                $.each($("#tplPostion").find(":input"), function () {
                    if (this.name in data) {
                        $(this).val(data[this.name]);
                    }
                });
                if (data.funid == 'xz') {
                    if (data.year == undefined) {
                        $(".layerRadio").siblings().checkbox("set checked");
                        $("#yearRow").addClass('hidden');
                        $("#layerContent").parent().removeClass('hidden');
                        $("#layerContent").append(Mustache.render(layerTpl, data));
                    } else if (typeof data.year == 'object') {
                        $(".layerRadio").checkbox().first().checkbox("set checked");
                        $(".yearRadio").siblings().checkbox("set checked");
                        $("#years").append(Mustache.render(multipleTpl, data));

                        $("#addMultipleBtn").removeClass("hidden");

                        $("#years .glyphicon-remove-sign").on('click', function(){
                            var inputs = $(this).parent().parent().find(":input");
                            var o = {};
                            $.each(inputs , function(){
                                o[$(this).attr('name')] = $(this).val();
                            });

                            $.each(data.year, function(i, n){
                                if(n.year == o.year && n.xzdw == o.xzdw && n.dltb == o.dltb){
                                    data.year.splice(i, 1);
                                    return false;
                                }
                            });

                            $(this).parent().parent().detach();
                        });
                    } else if (typeof data.year == 'string') {
                        $(".layerRadio").checkbox().first().checkbox("set checked");
                        $(".yearRadio").checkbox().first().checkbox("set checked");
                        $("#singleYear").append(Mustache.render(singleTpl, data));
                    }
                    $("#analysisForm input[name='funid']").val(data.funid);
                } else if (data.funid == 'gh') {
                    $("#analysisType1").val(data.year!=""?"year":"layerType");
                    $("#analysisType1Label").text($("#analysisType1").find("option:selected").text());
                    if(data.year!=""){
                        $("#layerType").addClass("hidden");
                        $("#layerType").val("");
                        $("#year").val(data.year);
                        $("#year").removeClass("hidden");
                    }else{
                        $("#year").addClass("hidden");
                        $("#year").val("");
                        $("#layerType").val(data.layerType);
                        $("#layerType").removeClass("hidden");
                    }
                } else {

                }

                $('#analysisType1').off("change").on('change', function(){
                    $("#analysisType1Label").text($(this).find("option:selected").text());
                    if($(this).val()=='year'){
                        $("#layerType").addClass("hidden");
                        $("#layerType").val("");
                        $("#year").removeClass("hidden");
                    }else if($(this).val()=='layerType'){
                        $("#year").addClass("hidden");
                        $("#year").val("");
                        $("#layerType").removeClass("hidden");
                    }
                });


                $(".layerRadio").checkbox().first()
                    .checkbox({
                        onChecked: function () {
                            $("#layerContent").empty();
                            $("#layerContent").parent().addClass('hidden');
                            $("#yearRow").removeClass("hidden");
                            if($(".yearRadio").checkbox().first().checkbox("is checked")){
                                $("#addMultipleBtn").addClass("hidden");
                                $("#singleYear").append(Mustache.render(singleTpl, data));
                            }else if($(".yearRadio").siblings().checkbox("is checked")){
                                $("#years").append(Mustache.render(multipleTpl, data));
                            }else{
                                $("#singleYear").append(Mustache.render(singleTpl));
                                $(".yearRadio").checkbox().first().checkbox("set checked");
                            }

                            $(".yearRadio").checkbox({
                                onChecked: function () {
                                    $("#addMultipleBtn").addClass("hidden");
                                    $("#singleYear").append(Mustache.render(singleTpl, {}));
                                    $("#years").empty();
                                }
                            });
                        }
                    });

                $(".layerRadio").siblings().checkbox({
                    onChecked: function () {
                        $("#yearRow").addClass("hidden");
                        $("#singleYear").empty();
                        $("#years").empty();
                        $("#layerContent").append(Mustache.to_html(layerTpl));
                        $("#layerContent").parent().removeClass('hidden');
                    }
                });

                $(".yearRadio").checkbox({
                    onChecked: function () {
                        $("#addMultipleBtn").addClass("hidden");
                        $("#singleYear").append(Mustache.render(singleTpl, {}));
                        $("#years").empty();
                    }
                });

                $(".yearRadio").siblings().checkbox({
                    onChecked: function () {
                        $("#years").empty();
                        $("#addMultipleBtn").removeClass("hidden");
                        if(typeof data.year == 'object'){
                            $("#years").append(Mustache.render(multipleTpl, data));
                        }else{
                            $("#years").append(Mustache.render(multipleTpl, {year:[]}));
                        }

                        $("#singleYear").empty();

                        //移除单个年份配置的地类图斑和线状地物以及年度配置
                        if(data.dltb || data.xzdw){
                            delete data.dltb;
                            delete data.xzdw;
                            delete data.year;
                        }

                        $("#years .glyphicon-remove-sign").on('click', function(){
                            var inputs = $(this).parent().parent().find(":input");
                            var o = {};
                            $.each(inputs , function(){
                                o[$(this).attr('name')] = $(this).val();
                            });

                            $.each(data.year, function(i, n){
                                if(n.year == o.year && n.xzdw == o.xzdw && n.dltb == o.dltb){
                                    data.year.splice(i, 1);
                                    return false;
                                }
                            });

                            $(this).parent().parent().detach();
                        });
                    }
                });



                //$("#analysisType").dropdownSelect('set selected',true?"year":"layerType");
                //$("#analysisType").dropdownSelect('refresh');

                saveEditData(formData);

                pkLayer.showSimpleDialog($("#addMultipleBtn"), '新增', $("#addYearsTpl").html(),
                    function(){
                    $("#addYearsBtn").off("click").on('click', function(event){
                        if(typeof  data.year == 'string' || data.year == undefined){
                            data.year = [];
                        }
                        data.year.push(SerializeForm.serializeObject($("#addYearsForm")));
                        pkLayer.closeDialog();
                        $("#years").empty();
                        $("#years").append(Mustache.render(multipleTpl, data));

                        $("#years .glyphicon-remove-sign").on('click', function(){

                            var inputs = $(this).parent().parent().find(":input");
                            var o = {};
                            $.each(inputs , function(){
                                o[$(this).attr('name')] = $(this).val();
                            });

                            $.each(data.year, function(i, n){
                                if(n.year == o.year && n.xzdw == o.xzdw && n.dltb == o.dltb){
                                    data.year.splice(i, 1);
                                    return false;
                                }
                            });

                            $(this).parent().parent().detach();
                        });

                    });
                }, {area: '600px'});
            });

            /**
             * 及时编辑的数据同步对象存储
             * @param formData
             */
            function saveEditData(formData){
                if(formData.funid != null){
                    $.each(configData.config.jsonParams, function(i, n){
                        if(n.funid == formData.funid){
                            if(formData.funid == 'xz' || formData.funid == 'gh'){
                                delete formData.layerRadio;
                                delete formData.yearRadio;
                                if('xzdw' in formData){
                                    delete n.year;
                                }else if(typeof formData.year == 'string'){
                                    delete n.xzdw;
                                    delete n.dltb;
                                }else if(typeof formData.year == 'object'){
                                    delete n.xzdw;
                                    delete n.dltb;
                                }
                            }
                            lang.mixin(n, formData);
                            return false;
                        }
                    });
                }
            }

            pkLayer.showSimpleDialog($("#addAnalysis"), '新增分析', $("#addAnalysisTpl").html(), layerCallback, {area: '600px'});


            /**
             * 保存配置
             */
            $("#zzAnalysisSaveBtn").off("click").on('click', function(evt){
                var formData = SerializeForm.serializeObject($("#analysisForm"));
                saveEditData(formData);

                configData.label = $("input[name='label']").val();
                configData.icon = $("input[name='icon']").val();
                configData.config.importType = $("input[name='importType']").val();

                configData.config.query.layers = [];
                $.each($(".accordion .title"), function(){
                    configData.config.query.layers.push($(this).data("info"));
                });

                multipleAnalysis.superclass.commomUpdateWidget(configData);

            });

            /**
             * 删除分类
             */
            $("#analysisType .glyphicon-remove-sign").off("click").on('click', function(evt){
                evt.stopPropagation();
                var funid = $(this).parent().data('funid');
                var that = $(this).parent();
                layer.confirm(
                    '确定要删除吗？',
                    {icon: 3, title:'提示'},
                    function(index){
                    $.each(configData.config.jsonParams, function(i, n){
                        if(n.funid == funid){
                            configData.config.jsonParams.splice(i,1);
                            multipleAnalysis.superclass.commomUpdateWidget(configData);
                            return false;
                        }
                    });
                    that.detach();
                    layer.close(index);
                });
            });
        }

        /**
         * 图层和字段的订阅监听
         */
        function subscriberListener() {
            var layerSubscriber = null, fieldSubscriber=null;

            if (layerSubscriber != null)
                layerSubscriber.remove();

            //订阅添加图层
            layerSubscriber = topic.subscribe("add/layer", function (a, b) {
                var layerObj = {
                    layerName: a.name,
                    layerUrl: b,
                    returnFields: [],
                    getInfo: function () {
                        return JSON.stringify(this);
                    }
                }
                var layerTpl =
                    '<div class="title" style="text-align: left!important;" data-info="{{getInfo}}"  data-id="{{layerName}}">' +
                    '<i class="dropdown icon"></i> {{layerName}} ' +
                    '<span class="glyphicon glyphicon-remove-sign pull-right"></span>'+
                    '</div>' +
                    '<div class="content">' +
                    '<div class="transition hidden">' +
                    '<ul class="field-list">' +
                    '{{#returnFields}}' +
                    '<li data-parent="{{layerName}}">' +
                        '{{alias}}' +
                        '<span class="glyphicon glyphicon-remove-sign pull-right"></span>'+
                    '</li>' +
                    '{{/returnFields}}' +
                    '</ul>' +
                    '</div>' +
                    '</div>';

                if($("#layers div[data-id='"+ a.name+"']").length==0){
                    $("#layers .accordion").append(Mustache.render(layerTpl, layerObj));
                    $('.ui.accordion').accordion();
                    subscriberListener();
                }
            });


            if(fieldSubscriber!=null){
                fieldSubscriber.remove();
            }
            //订阅添加字段
            fieldSubscriber = topic.subscribe("add/field", function (a, b) {
                var fieldsTpl = '<li data-id="{{name}}" data-parent="{{parent}}">{{alias}}<span class="glyphicon glyphicon-remove-sign pull-right"></span></li>';

                if($("#layers div[data-id='"+ a.name+"']").next().find(".field-list li[data-id='"+ b.name+"']").length==0){
                    b.parent = a.name;
                    $("#layers div[data-id='"+ a.name+"']").next().find(".field-list").append(Mustache.render(fieldsTpl, b));
                    delete b.parent;
                    var addData = $("#layers div[data-id='"+ a.name+"']").data("info");
                    addData.returnFields.push(b);

                    $("#layers div[data-id='"+ a.name+"']").attr("data-info", JSON.stringify(addData));
                    subscriberListener();
                }
            });

            /**
             * 删除图层
             */
            $("#layers .accordion .title .glyphicon-remove-sign").off("click").on('click', function(evt){
                evt.stopPropagation();
                $(this).parent().next().detach();
                $(this).parent().detach();
            });

            /**
             * 删除图层字段
             */
            $("#layers .accordion .content .glyphicon-remove-sign").off("click").on('click', function(evt){
                evt.stopPropagation();
                var objectId = $(this).parent().data("id");
                var layerId = $(this).parent().data("parent");
                var modifyData = $("#layers div[data-id='"+ layerId +"']").data("info");
                $.each(modifyData.returnFields, function(i, n){
                    if(objectId == n.name){
                        modifyData.returnFields.splice(i,1)
                        $("#layers div[data-id='"+ layerId+"']").attr("data-info", JSON.stringify(modifyData));
                        return false;
                    }
                });
                $(this).parent().detach();
            });


        }

        /**
         * 新增分析类别回调处理函数
         */
        function layerCallback() {
            $("#addAnalysisForm").bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    funid: {
                        validators: {
                            notEmpty: {
                                message: 'funid 不能为空！'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9_]+$/,
                                message: '只能是数字、字母和下划线的组成'
                            },
                            callback: {
                                message: 'funid不唯一',
                                callback: function(value, validator) {
                                    var flag = true;
                                    $.each(configData.config.jsonParams, function(i, n){
                                        if(n.funid == value){
                                            flag = false;
                                            return false
                                        }
                                    });
                                    return flag;
                                }
                            }
                        }
                    },
                    alias:{
                        validators: {
                            notEmpty: {
                                message: '别名不能为空！'
                            }
                        }
                    }
                }
            }).on('success.form.bv', function (e) {
                var form = SerializeForm.serializeObject($("#addAnalysisForm"));
                var tplType = '';
                switch (form.funid){
                    case 'xz':tplType = 'analysisTpl2'; break;
                    case 'gh':tplType = 'analysisTpl3'; break;
                    default :tplType = 'analysisTpl1';
                };

                var itemTpl =
                    '<a href="#" class="list-group-item" data-funid="{{funid}}" data-tpl="'+tplType+'" data-info="">'+
                    '<span class="analysisAlias">{{alias}}</span>'+
                    '<span class="glyphicon glyphicon-remove-sign pull-right"></span>'+
                    '</a>';
                configData.config.jsonParams.push(form);
                $("#analysisType .list-group a:first").before(Mustache.render(itemTpl, form));
                //allAlias[form.funid] = form.alias;
                multipleAnalysis.superclass.commomUpdateWidget(configData);
                pageListeners();
                /**
                 * 删除分类
                 */
                $("#analysisType .glyphicon-remove-sign").off("click").on('click', function(evt){
                    evt.stopPropagation();
                    var funid = $(this).data('funid');
                    var that = $(this).parent();
                    layer.confirm(
                        '确定要删除吗？',
                        {icon: 3, title:'提示'},
                        function(index){
                            $.each(configData.config.jsonParams, function(i, n){
                                if(n.funid == funid){
                                    configData.config.jsonParams.splice(i,1);
                                    //delete allAlias[funid];
                                    return false;
                                }
                            });
                            that.detach();
                            layer.close(index);
                        });
                });
                pkLayer.closeDialog();

                return false;
            });
        }


        return multipleAnalysis;
    });