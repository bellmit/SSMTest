define(["dojo/_base/declare",
        "dojo/_base/lang",
        "static/js/cfg/core/BaseWidgetCfg",
        "mustache",
        "slimScroll",
        "validator",
        "static/js/cfg/core/packed.layer",
        "static/js/cfg/core/SerializeForm",
        "text!../template/widgets/print/customText_items.html",
        "text!../template/widgets/print/customText_form.html",
        "text!../template/font-icons.html"],
    function (declare, lang, BaseWidgetCfg, Mustache, SlimScroll, validator, pkLayer, SerializeForm, customTextItemsTpl, customTextFormTpl, FontIconsTpl) {
        var tpl = null;
        var configData = null;
        var customTextItems = [];

        var print = declare([BaseWidgetCfg], {
            constructor: function () {
                tpl = print.arguments[1];
            },
            onCreate: function () {
                configData = this;
                print.superclass.init(tpl, this);
                customTextItems = configData.config.customTextItems;
                $("#customItems").append(Mustache.render(customTextItemsTpl, {result: customTextItems}));
                pageListeners();
            }
        });

        function pageListeners() {
            var scrollHeight = $(window).height()-400;
            $("#scrollContent").slimScroll({
                height: scrollHeight>350?350:scrollHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });

            $("#icons").append(FontIconsTpl);
            $('#icons').dropdownSelect();

            /**
             *  新增自定义属性
             */
            $("#addCustomProps").on('click', function () {
                pkLayer.showDialog('配置属性', Mustache.render(customTextFormTpl, {}), addPropsCallback, {area: '600px'});
            });

            /**
             * 删除自定义属性
             */
            $("#customItems .close").on('click', function(){
                var id = $(this).data("id");
                $.each(customTextItems, function(i, n){
                    if(n.key == id){
                        customTextItems.splice(i, 1);
                        return false;
                    }
                });
                $(this).parent().parent().detach();
            });

            /**
             * 保存
             */
            $("#printSaveBtn").on('click', function () {
                var form = SerializeForm.serializeObject($("#printForm"));
                configData.label = form.label;
                configData.icon = form.icon;

                delete form.label;
                delete form.icon;

                lang.mixin(configData.config, form);

                configData.config.customTextItems = customTextItems;

                print.superclass.commomUpdateWidget(configData);
            });

        }

        /**
         * 添加回调
         */
        function addPropsCallback() {
            $("#customForm").bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields:{
                    key:{
                        validators:{
                            notEmpty: {
                                message: 'key 不能为空！'
                            },
                            callback: {
                                message: 'key 不唯一',
                                callback: function(value, validator) {
                                    var flag = true;
                                    $.each(customTextItems, function(i, n){
                                        if(value == n.key){
                                            flag = false
                                        }
                                    });

                                    return flag;
                                }
                            }
                        }
                    },
                    name:{
                        validators: {
                            notEmpty: {
                                message: '元素名称不能为空！'
                            }
                        }
                    },
                    value:{
                        validators: {
                            notEmpty: {
                                message: '元素默认值不能为空！'
                            }
                        }
                    }
                }
            }).on('success.form.bv', function (e) {
                var obj = SerializeForm.serializeObject($("#customForm"));
                customTextItems.push(obj);
                $("#customItems").append(Mustache.render(customTextItemsTpl, {result: obj}));

                $("#customItems .close").on('click', function(){
                    var id = $(this).data("id");
                    $.each(customTextItems, function(i, n){
                        if(n.key == id){
                            customTextItems.splice(i, 1);
                            return false;
                        }
                    });
                    $(this).parent().parent().detach();
                });

                pkLayer.closeDialog();

                return false;
            });
        }

        return print;
    });