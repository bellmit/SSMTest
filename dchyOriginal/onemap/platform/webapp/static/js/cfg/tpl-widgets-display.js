/**
 *  模板的widgets配置
 */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/topic",
    "jqueryUI",
    "mustache",
    "treeview",
    "layer",
    "static/thirdparty/jquery/jquery.nestable",
    "static/js/cfg/core/packed.layer",
    "static/js/cfg/core/SerializeForm",
    "text!./template/widgets/widgets.html",
    "text!./template/widgets/widgets_tree_item.html",
    "text!./template/widgets/widget_add.html",
    "slimScroll",
    "static/js/UUIDUtils",
    "static/thirdparty/semantic-ui/accordion/accordion.min",
    "static/thirdparty/semantic-ui/checkbox/checkbox",
    "static/thirdparty/semantic-ui/transition/transition",
    "static/thirdparty/semantic-ui/dropdown/dropdown.min",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "css!static/thirdparty/josnVeiw/jsoneditor-min.css",
    "css!static/thirdparty/josnVeiw/app-min.css",
    "css!static/thirdparty/semantic-ui/accordion/accordion.min.css",
    "css!static/thirdparty/semantic-ui/checkbox/checkbox.css",
    "css!static/thirdparty/semantic-ui/divider/divider.css",
    "css!static/thirdparty/semantic-ui/transition/transition.css",
    "css!static/thirdparty/semantic-ui/dropdown/dropdown.css",
    "css!static/thirdparty/treeview/bootstrap-treeview.min.css",
    "css!static/thirdparty/layer/skin/layer-omp.css",
    "dojo/domReady!"
], function (arrayUtils, lang, topic, jqueryUI,Mustache, treeview, layer, nestable, pkLayer, SerializeForm,
             widgetsTpl, treeItem, widgetAdd) {
    var tpl;
    var allWidgets;
    var selectedWidgets;

    /**
     * 初始化树的组件
     * @private
     */
    function _initTree() {
        $('#nestable-menu').on('click', function (e) {
            var target = $(e.target),
                action = target.data('action');
            if (action === 'expand-all') {
                $('.dd').nestable('expandAll');
            }
            if (action === 'collapse-all') {
                $('.dd').nestable('collapseAll');
            }
        });

        $('#nestable3').nestable({
            maxDepth: 3,
            rootClass: 'dd',
            listClass: 'dd-list'
        }).on('change', function (evt, data) {
            var type = $(data).data('type');
            var moveToType = $(data).parent().data("type");  //移动到的父节点
            var url = '';
            var param = {};
            var widgetArray = [];
            var widgetsGroupArray = [];
            if (moveToType == 'rootList' && type == 'widget') {  // widgets之间排序
                url = root + '/config/tpl/' + tpl + '/widget/batchSave';
                $.each($(".widget"), function (i, n) {
                    widgetArray.push(lang.mixin($(n).data("info"), {weight: i + 1}));
                });
                param.widgets = JSON.stringify(widgetArray)
            } else if (moveToType == 'rootList' && type == 'widgetsGroup') {  //组之间的排序
                url = root + '/config/tpl/' + tpl + '/widgetsgroup/batchSave';
                $.each($(".widgetsGroup"), function (i, n) {
                    widgetsGroupArray.push(lang.mixin($(n).data("info"), {weight: i + 1}));
                });
                param.widgetsGroups = JSON.stringify(widgetsGroupArray);
            } else if (!(moveToType == 'rootList') && type == 'group-widget') { //组内移动排序
                url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                var widget = $(data).data("info");
                var widgetsGroup = $(data).parent().parent().data("info");
                $.each($(data).parent().parent().find("li"), function (i, n) {
                    widgetArray.push(lang.mixin($(n).data("info"), {weight: i + 1}));
                });
                widgetsGroup.widgets = widgetArray;
                param.widgetsGroup = JSON.stringify(widgetsGroup);
            } else if (!(moveToType == 'rootList') && type == 'widget') {  //widgets拖进组
                url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                var widget = $(data).data("info");
                var widgetsGroup = $(data).parent().parent().data("info");
                widget.group = widgetsGroup.id;
                var w = lang.clone(widget);
                param.widgetsGroup = JSON.stringify(widgetsGroup);
                param.widget = JSON.stringify(lang.mixin(widget, {id: Math.uuid()}));
            } else if (moveToType == 'rootList' && type == 'group-widget') {  //组内移出功能
                var widget = $(data).data("info");
                var w = lang.clone(widget);
            } else if (moveToType == 'rootList' && type == 'fixWidget') { //固定功能之间的排序
                url = root + "/config/tpl/" + tpl + "/fix/widget/update/all";
                $.each($(".fixWidget"), function (i, n) {
                    widgetArray.push(lang.mixin($(n).data("info"), {weight: i + 1}));
                });
                param.fixWidgets = JSON.stringify(widgetArray)
            }

            $.ajax({
                url: url,
                method: 'post',
                async: false,
                data: param,
                success: function (rp) {
                    if (rp != '') {
                        getWidgets();
                    } else {
                        if (moveToType == 'rootList' && type == 'widget') {
                            allWidgets.widgets = widgetArray;
                            layer.msg('操作成功', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                renderTpl();
                            });
                        } else if (moveToType == 'rootList' && type == 'widgetsGroup') {  //组之间的排序
                            allWidgets.widgetsGroup = widgetsGroupArray;
                            layer.msg('操作成功', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                renderTpl();
                            });
                        } else if (!(moveToType == 'rootList') && type == 'widget') {
                            removeWidget(w);
                        } else if (moveToType == 'rootList' && type == 'group-widget') {
                            removeGroupWidget(getArrayElById(allWidgets.widgetsGroup, null, w.group), w);
                            addWidget(w);
                        } else if (!(moveToType == 'rootList') && type == 'group-widget') { //组内移动排序
                            getArrayElById(allWidgets.widgetsGroup, null, widget.group).widgets = widgetArray;
                            layer.msg('操作成功', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                //do something
                                renderTpl();
                            });
                        } else if (moveToType == 'rootList' && type == 'fixWidget') {
                            layer.msg('操作成功', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                //do something
                                allWidgets.fixWidgets = widgetArray;
                                renderTpl();
                            });
                        } else {
                            renderTpl();
                        }
                    }
                }
            });
        });
    }

    /**
     * 删除widget
     * @param widget
     */
    function removeWidget(widget) {
        $.ajax({
            url: root + '/config/tpl/' + tpl + '/widget/delete',
            method: 'post',
            async: false,
            data: {widget: JSON.stringify(widget)},
            success: function (rp) {

                if (rp != '') {
                    getWidgets();
                } else {
                    removeArrayEl(allWidgets.widgets, widget);
                    //把widget拖进组需要删除后吧新的widget放在组里的数组中
                    if (widget.group != undefined) {
                        var widgetsGroup = getArrayElById(allWidgets.widgetsGroup, null, widget.group);
                        widgetsGroup.widgets.unshift(lang.mixin(widget, {weight: 1}));
                    }

                    layer.msg('操作成功', {
                        icon: 1,
                        time: 1000
                    }, function () {
                        //do something
                        renderTpl();
                    });
                }
            }
        });
    }

    /**
     * 删除组内的widget 或者 组
     * @param widget
     */
    function removeGroupWidget(widgetsGroup, widget) {
        $.ajax({
            url: root + '/config/tpl/' + tpl + '/widgetsgroup/delete',
            method: 'post',
            async: false,
            data: {
                widgetsGroup: JSON.stringify(widgetsGroup),
                widget: JSON.stringify(widget)
            },
            success: function (rp) {
                if (rp != '') {
                    getWidgets();
                } else {
                    if (widget != null || widget != undefined) {  //删除组内的widgets
                        removeArrayEl(widgetsGroup.widgets, widget);
                    } else { //删除组
                        removeArrayEl(allWidgets.widgetsGroup, widgetsGroup);
                    }
                    layer.msg('操作成功', {
                        icon: 1,
                        time: 1000
                    }, function () {
                        //do something
                        renderTpl();
                    });
                }
            }
        });
    }


    function addWidget(widget) {
        $.ajax({
            url: root + '/config/tpl/' + tpl + '/widget/save',
            method: 'post',
            async: false,
            data: {
                widget: JSON.stringify(widget)
            },
            success: function (rp) {
                delete widget.group;
                allWidgets.widgets.push(widget);
                renderTpl();
            }
        });
    }

    /**
     * 渲染模板
     * @param renderObj
     */
    function renderTpl() {
        $(".root").detach();
        Mustache.parse(widgetsTpl);
        Mustache.parse(treeItem);
        $("#sectionWidgets>.widgets-info").empty();

        var partials = {
            gwidgets: '{{#widgets}}' +
            '<li class="dd-item dd3-item group-widget" data-id="{{id}}" data-type="group-widget" data-info="{{getWidgetInfo}}" title="按住我可以拖拽哦！">' +
            '<div class="dd-handle dd3-handle2">Drag</div><div class="dd3-content"><div class="overflowText">{{label}}</div>' +
            '<span class="glyphicon glyphicon-trash pull-right del"></span>'
            + '</div>' +
            '</li>'
            + '{{/widgets}}'
        };

        $("#sectionWidgets>.widgets-info").append(Mustache.render(widgetsTpl, {tplName: tpl}));
        $("#nestable3").append(Mustache.render(treeItem, allWidgets, partials));

        _initTree();

        addListeners();
    }

    /**
     * 获取选择data的状态及显示的数据
     */
    function loadDataState(id, type) {
        $.each($("li ." + type), function (i, n) {
            if ($(n).data("id") == id) {
                $($(n).find(".dd3-content")[0]).addClass("dd3-click");
                return false;
            }
        })
    }

    /**
     * 获取所有的模板功能
     */
    function getWidgets() {
        //获取模板的所有配置
        var widgetsResult = {
            getWidgetInfo: function () {
                return JSON.stringify(this);
            }
        };

        // 异步请求后台获取模板的配置
        $.ajax({
            url: root + '/config/tpl/' + tpl + '/widgets',
            async: false,
            complete: function (rp) {
                $(".root").detach();
                allWidgets = jQuery.parseJSON(rp.responseText);
                allWidgets.getWidgetInfo = widgetsResult.getWidgetInfo;
                renderTpl();
            }
        });
    }

    //事件监听
    function addListeners() {
        try {
            $("#treeScroll").slimScroll({
                height: $(window).height() - 170,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        } catch (e) {
            console.error(e);
        }
        if ($("#viewScoll").length > 0) {
            $("#viewScoll").slimScroll({
                height: $(window).height() - 170,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
        }

        //创建新功能模块
        $("#createWidgetBtn").on("click", function () {
            var layerContent = '<form class="form-horizontal create-custom-content" role="form">' +
                '<div class="form-group create-widgetName-div">' +
                '<label for="createWidgetName" class="pull-left control-label create-widget-label">模块名称</label>' +
                '<div class="pull-left ">' +
                '<input type="text" class="form-control" id="createWidgetName" placeholder="请输入模块名称"></div></div>' +
                '<div class="form-group create-widget-alias">' +
                '<label for="createWidgetAlias" class="pull-left  control-label create-widget-label">模块别名</label>' +
                '<div class="pull-left "><input type="text" class="form-control" id="createWidgetAlias" placeholder="请输入模块别名"></div></div></form>';
            layer.confirm("此操作为高级定制功能，是否继续使用？", {icon: 3, title: '提示'}, function (index) {
                    layer.close(index);
                    layer.open({
                        type: 1,
                        title: "自定义模块",
                        area: ['300px', '190px'],
                        content: layerContent,
                        btn: ["确定"],
                        btn1: function () {
                            var widgetName = $("#createWidgetName").val();
                            var alias = $("#createWidgetAlias").val();
                            if (!widgetName) {
                                layer.msg("请填入模块名称！");
                               return;
                           }else{
                               $.ajax({
                                   url:root+"/config/widget/exists",
                                   data:{name:widgetName,tplName:tpl,alias:alias},
                                   success:function(r){
                                       if(r){
                                           window.location.reload();
                                           window.open(root+"/config/editor?widget="+widgetName);
                                       }else{
                                           layer.msg("已有同名模块存在！",{icon:2,time:2000});
                                       }
                                   }
                               });
                           }
                       }
                   });
                }
            );
        });

        pkLayer.showSimpleDialog($("#addWidgetBtn"), '新增功能', widgetAdd, layerCallback, {area: '600px'});

        //删除widget监听
        $(".widget .del").on('click', function () {
            var $this = this;
            layer.confirm('您是确定要删除这个配置吗?', {icon: 3, title: '提示'}, function (index) {
                removeWidget($($this).parent().parent().data("info"));
                layer.close(index);
            });
        });

        //删除组内widget监听
        $(".group-widget .del").on('click', function (evt) {
            evt.stopPropagation() || (evt.cancelBubble = true);
            var $this = this;
            layer.confirm('您是确定要删除这个配置吗?', {icon: 3, title: '提示'}, function (index) {
                var w = $($this).parent().parent().data("info");
                removeGroupWidget(getArrayElById(allWidgets.widgetsGroup, null, w.group), w);
                layer.close(index);
            });
        });

        //删除组监听
        $(".widgetsGroup .gDel").on('click', function () {
            var $this = this;
            layer.confirm('您是确定要删除这个配置吗?', {icon: 3, title: '提示'}, function (index) {
                removeGroupWidget($($this).parent().parent().data("info"));
                layer.close(index);
            });
        });

        //功能菜单单击出现视图界面
        $(".widget, .fixWidget,.dockWidget").off('click').on('click', function () {
            var d = $(this).data("info");

            $("li").find(".dd3-content").removeClass('dd3-click');
            $(this).find(".dd3-content").addClass("dd3-click");
            renderConfigPage(d.url, d);
            $("#jsonFrame")[0].contentWindow.reloadCfg(d);
        });

        //功能组菜单单击出现视图界面
        $(".widgetsGroup").on('click', function () {
            console.log(allWidgets);
            var d = $(this).data("info");

            $("li").find(".dd3-content").removeClass('dd3-click');
            $($(this).find(".dd3-content")[0]).addClass("dd3-click");

            $("#jsonFrame")[0].contentWindow.reloadCfg(d);
        });

        //功能组单个项菜单单击出现视图界面
        $(".group-widget").on('click', function (evt) {
            evt.stopPropagation();    //取消冒泡
            var d = $(this).data("info");

            $("li").find(".dd3-content").removeClass('dd3-click');
            $(this).find(".dd3-content").addClass("dd3-click");

            renderConfigPage(d.url, d);
            $("#jsonFrame")[0].contentWindow.reloadCfg(d);
        });

        /**
         *  获取json编辑界面上的提交监听
         */
        $("#jsonFrame").on("load", function () {
            console.log('iframe loaded');

            if (selectedWidgets != null || selectedWidgets != undefined) {
                $("#jsonFrame")[0].contentWindow.reloadCfg(selectedWidgets);
            }

            $(window.frames["jsonFrame"].document).find(".saveCfg").on('click', function () {
                var data = $("#jsonFrame")[0].contentWindow.getSubmitJson();
                var url = '';
                var param = {};
                var type = null;
                var replaceData = null;
                selectedWidgets = data;

                try {
                    if ($("li .dd3-click").parent().hasClass("widget")) {
                        type = 'widget';
                        url = root + '/config/tpl/' + tpl + '/widget/save';
                        param.widget = JSON.stringify(data);
                        replaceData = getArrayElById(allWidgets.widgets, data.id);
                    } else if ($("li .dd3-click ").parent().hasClass("widgetsGroup")) {
                        type = 'widgetsGroup';
                        url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                        param.widgetsGroup = JSON.stringify(data);
                        replaceData = getArrayElById(allWidgets.widgetsGroup, data.id);
                    } else if ($("li .dd3-click ").parent().hasClass("group-widget")) {
                        type = 'group-widget';
                        url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                        if (data.group === undefined) {
                            throw Error("当前widget可能是组内成员，但缺少group值.");
                        }
                        var widgetsGroup = getArrayElById(allWidgets.widgetsGroup, null, data.group);
                        replaceData = getArrayElById(widgetsGroup.widgets, data.id);
                        param.widgetsGroup = JSON.stringify(widgetsGroup);
                        param.widget = JSON.stringify(data);
                    } else if ($("li .dd3-click ").parent().hasClass("fixWidget")) {
                        url = root + '/config/tpl/' + tpl + "/fix/widget/update";
                        param.fixWidget = JSON.stringify(data);
                        replaceData = getArrayElById(allWidgets.fixWidgets, data.id);
                    } else if ($("li .dd3-click ").parent().hasClass("dockWidget")) {
                        url = root + '/config/tpl/' + tpl + '/dockwidget/save';
                        param.dockWidget = JSON.stringify(data);
                        replaceData = getArrayElById(allWidgets.dockWidgets, data.id);
                    }
                } catch (e) {
                    layer.msg(e.message,{icon:0,time:3000});
                    return;
                }

                $.ajax({
                    url: url,
                    method: 'post',
                    data: param,
                    success: function (rp) {
                        if (rp != '' && typeof rp != 'object') {
                            getWidgets();
                        } else {
                            layer.msg('修改成功！', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                lang.mixin(replaceData, data);
                                renderTpl();
                                loadDataState(data.id, type);
                            });
                        }
                    }
                });
            });
        });

        /**
         * tab页切换监听
         */
        $('#myTabs a').click(function (e) {
            if (e.target.hash == '#source') {
                var info = $(".dd3-click").parent().data('info');
                if (!info)
                    info = {};
                $("#jsonFrame")[0].contentWindow.reloadCfg(info);
            } else if (e.target.hash == '#view') {
                var info = $(".dd3-click").parent().data('info');
                renderConfigPage(info.url, info);
            }
        });
    }


    /**
     * 新增widgets功能的回调
     */
    function layerCallback() {
        var commonWidgets = [];
        $("#widgetType").on('change', function () {
            var wGroup = [];
            $("#widgetSelect").empty();
            $("#groupSelect").empty();
            $(".groupSelect").slideUp("fast");
            if ($(this).val() == '2') {  //
                var item = '<option value="' +
                    "{'icon':'','id':'69356CBD-DC86-D9C0-5F27-4915E261C8C0','label':'组','type':0,'url':'','weight':6,'widgets':[]}"
                    + '">' + '组</option>';
                $("#widgetSelect").append(item);
            } else if ($(this).val() == '1') {
                $.ajax({
                    url: root + '/config/commonwidgets/all',
                    async: false,
                    complete: function (rp) {
                        var tpl = '{{#commonwidgets}}<option value="{{showInfo}}">{{label}}</option>{{/commonwidgets}}';
                        var result = {
                            commonwidgets: jQuery.parseJSON(rp.responseText),
                            showInfo: function () {
                                return JSON.stringify(this);
                            }
                        };
                        $("#widgetSelect").append(Mustache.to_html(tpl, result));
                    }
                });
                $(".widgetsGroup").each(function(index, item){
                    wGroup.push($(item).data("info"));
                });
                var groupSelectItem = '{{#widgetsGroup}}<option value="{{info}}">{{label}}</option>{{/widgetsGroup}}';
                var result = {
                    widgetsGroup: wGroup,
                    info : function(){
                        return JSON.stringify(this);
                    }
                };
                $("#groupSelect").append('<option value="null">不分组</option>');
                $("#groupSelect").append(Mustache.to_html(groupSelectItem, result));
                $(".groupSelect").slideDown("fast");
            }

            addFornListener();
        });

        function addFornListener() {
            /**
             * 插入新的widget
             */
            $("#submitWidget").on('click', function () {
                var url = '';
                var param = {};
                if ($("#widgetType").val() == '1') {
                    var widget1 = JSON.parse($("#widgetSelect").val());
                    widget1 = lang.mixin(widget1, {id: Math.uuid(), label: $("#name").val()});
                    if ($("#groupSelect").val() === "null"){
                        url = root + '/config/tpl/' + tpl + '/widget/save';
                    }else {
                        url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                        var widgetsGroup = JSON.parse($("#groupSelect").val());
                        widget1.group = widgetsGroup.id;
                        param.widgetsGroup = JSON.stringify(widgetsGroup);
                    }
                    param.widget = JSON.stringify(widget1);

                } else if ($("#widgetType").val() == '2') {
                    var widgetsGroup = JSON.parse($("#widgetSelect").val().replace(/\'/g, '\"'));
                    widgetsGroup = lang.mixin(widgetsGroup, {id: Math.uuid(), label: $("#name").val()});
                    url = root + '/config/tpl/' + tpl + '/widgetsgroup/save';
                    param.widgetsGroup = JSON.stringify(widgetsGroup);
                }

                $.ajax({
                    url: url,
                    method: 'post',
                    data: param,
                    success: function (rp) {
                        if (rp != '') {
                            getWidgets();
                        } else {
                            pkLayer.closeDialog();

                            layer.msg('新增成功', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                //do something
                                //renderTpl();
                                getWidgets();
                            });
                        }
                    }
                });
            });
        }
    }

    /**
     * 渲染配置界面
     */
    function renderConfigPage(confType, data) {
        $('#view').empty();
        var baseUrl = 'static/js/cfg';
        try {
            require([baseUrl + '/module/' + confType, "text!" + baseUrl + '/view/' + "widget-" + confType + ".html"], function (Main, html) {
                var main = new Main(data, html, tpl);
                main.setConfig(data.config);
                main.onCreate();
                console.log('widget [' + confType + '] config loaded ok ');
            });
        } catch (e) {
            $('#myTabs li:eq(1) a').tab('show');
            $("#jsonFrame")[0].contentWindow.reloadCfg(data);
            console.log("widget[" + confType + "] loaded error [" + e.message + "]");
        }
    }

    /**
     * 更新全局配置
     * @param targetType   更新类型
     * @param targetConfig 需更新的目标配置
     */
    function updateGlobeConfig(targetType, targetConfig) {
        var arrys = null;
        if ('widget' == targetType) {
            arrys = allWidgets.widgets;
            $.each(arrys, function (i, n) {
                if (n.id == targetConfig.id) {
                    lang.mixin(n, targetConfig);
                    return false;
                }
            });
        } else if ('widgetsGroup' == targetType) {
            arrys = allWidgets.widgetsGroup;
            $.each(arrys, function (i, n) {
                if (n.id == targetConfig.group) {
                    $.each(n.widgets, function (j, m) {
                        if (m.id == targetConfig.id) {
                            lang.mixin(m, targetConfig);
                            return false;
                        }
                    });
                    return false;
                }
            });
        }
    }

    /**
     * 删除数组的元素
     * @param array
     * @param el
     */
    function removeArrayEl(array, el) {
        $.each(array, function (i, n) {
            if (el.id == n.id) {
                array.splice($.inArray(n, array), 1);
                return false;
            }
        });
    }

    /**
     * 添加数组元素
     * @param array
     * @param el
     */
    function addArrayEl(array, el) {

    }

    /**
     * 获取数组内的元素
     * @param array  可能为widgetGroup | widgets
     * @param id
     * @param groupId
     */
    function getArrayElById(array, id, groupId) {
        var el = null;
        if (id != null || id != undefined) {
            arrayUtils.forEach(array, function (n) {
                if (n.id == id) {
                    el = n;
                    return false;
                }
            });
        } else if (groupId != null || groupId != undefined) {
            arrayUtils.forEach(array, function (n) {
                if (n.id == groupId) {
                    el = n;
                    return false;
                }
            });
        }
        return el;
    }

    /**
     * 初始化方法
     * @private
     */
    function _init() {
        getWidgets();
    }


    return {
        renderWidgets: function (tplName) {
            tpl = tplName;
            _init();
        },
        updateGlobeConfig: updateGlobeConfig
    }
});