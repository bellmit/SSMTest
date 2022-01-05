/**
 * 菜单栏模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/10 19:50
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "map/manager/ConfigManager",
    "map/manager/WidgetsManager",
    "dojox/uuid/generateRandomUuid",
    "handlebars",
    "map/core/BaseWidget",
    "text!map/template/widget/collapse-widget-tpl.html",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min"], function (declare, lang, arrayUtil, ConfigManager, WidgetsManager, RandomUuid,
                                                                  Handlebars, BaseWidget, collapseWidgetTpl) {
    var Menu = declare([BaseWidget], {

        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            initMenu();
            addListener();
            $('[data-toggle="tooltip"]').tooltip({
                placement: 'bottom',
                container: 'body'
            });
        }
    });

    var timeout = 500;
    var closetimer = 0;
    var $ddmenuitem = 0;

    var $groupIcon = undefined;

    var singleMenuTpl = '<li><span id="menu_{{id}}" data-wid="{{id}}" title="{{label}}" data-toggle="tooltip" class="{{parseIcon}}">' +
        '{{#if hideGap}}{{else}}<b class="menu-gap"></b>{{/if}}</span></li>';//widget菜单

    var appConfig = ConfigManager.getInstance().getAppConfig();
    var widgetsManager = WidgetsManager.getInstance();
    var allWidgets = [];//存放 模板中所有的widgets
    var autoStartWidgets = [];//存放加载即启动的widget
    //默认可折叠方式加载的 widget 集合
    var defaultCollapsibles = [];//['Mark', 'Print', 'Measure', 'IOWidget','Spy','ClearCache'];
    var menuWidgets = [];
    var widgets = [];

    /**
     * 加载菜单项
     */
    function initMenu() {
        Handlebars.registerHelper("parseIcon", function () {
            var icon = this.icon;
            if (icon == undefined)
                return new Handlebars.SafeString("");
            if (icon.startWith("&"))
                return new Handlebars.SafeString('iconfont ' + icon);
            else if (icon.startWith("fa")) {
                return new Handlebars.SafeString('fa ' + icon);
            } else
                return new Handlebars.SafeString('fa fa-th-large');
        });
        var widgets = filterCollapsibleWidgets(appConfig.widgetContainer.widgets);
        var widgetGroup = filterCollapsibleWidgets(appConfig.widgetContainer.widgetsGroup);

        loadCollapsibleWidgets(menuWidgets, $('#menu-content'), singleMenuTpl);

        $.each(widgets, function (index, value) {
            widgetsManager.loadFuncWidget(value);
        });

        $.each(widgetGroup, function (index, value) {
            widgetsManager.loadWidgetsGroup(value);
        });

        //主地图以及功能加载完成后 此时打开默认开启的widget
        EventBus.listener(EventBus.APP_INITIALIZED, function () {
            arrayUtil.forEach(autoStartWidgets, function (item) {
                widgetsManager.loadFuncWidget(item);
            });
        });
    }

    /**
     *  加载菜单项 不做初始化 只是添加显示
     * @param widgets
     * @param selector
     */
    function loadCollapsibleWidgets(widgets, selector, template) {
        arrayUtil.forEach(widgets, function (widget, idx) {
            var widgetId = RandomUuid();
            widget.id = widgetId;
            allWidgets.push(widget);
            var templData = {id: widgetId, label: widget.label, icon: widget.icon};
            if (idx === widgets.length - 1) {
                lang.mixin(templData, {hideGap: true});
            }
            selector.append(renderTpl(template, templData));
            $("#menu_" + widgetId).on('click', function () {
                var wid = $(this).data("wid");
                var w = findWidgetById(wid);
                openCollapsibleWidget(w);
            });
            //
            if (widget.open == true) {
                autoStartWidgets.push(widget);
            }
        });
    }

    /**
     * 过滤 collapsible widget
     * @param array
     */
    function filterCollapsibleWidgets(array) {
        var ret = [];
        arrayUtil.forEach(array, function (item) {
            if (item.hasOwnProperty('widgets')) {
                //process widget group
                item.widgets = filterCollapsibleWidgets(item.widgets);
                ret.push(item);
            } else {
                //process single widget
                if (arrayUtil.indexOf(defaultCollapsibles, item.url) > -1 || item.collapsible === true) {
                    menuWidgets.push(item);
                } else {
                    ret.push(item);
                }
            }
        });
        return ret;
    }

    /**
     * 加载 collapsible widget
     * @param widget
     */
    function openCollapsibleWidget(widget) {
        console.log('widget.url---',widget.url);
        arrayUtil.forEach(widgets, function (value){
            value.onPause();
        });
        var url = widget.url;
        var id=widget.id;
        if ($(".panel-widget").length > 0) {
            $(".panel-widget").addClass('hidden');
            arrayUtil.forEach(widgets, function (value){
                if(value.id === id){
                    value.onOpen();
                }
            })
        }
        if ($("#panel".concat(id)).length === 0) {
            log('加载模块 [' + widget.label + '] ');
            require(["map/widgets/" + url + "/Index", "text!map/widgets/" + url + "/Index.html",
                "css!map/widgets/" + url + "/Style"], function (Main, html) {
                if (!(Main instanceof Function)) {
                    log("widget [" + widget.url + "] must return BaseWidget");
                    return;
                }
                // _switchWidget(url);

                var main = new Main({
                    id: id,
                    label: widget.label,
                    url: widget.url.indexOf("/") > -1 ? widget.url : "map/widgets/" + widget.url
                });
                lang.mixin(widget, {content: html});
                $("#collapseWidgetContainer").append(renderTpl(collapseWidgetTpl, widget));
                // close listener
                $("#panel".concat(id)).find('.panel-close').on('click', function () {
                    $("#panel".concat(id)).addClass('hidden');
                    arrayUtil.forEach(widgets, function (value){
                        if(value.id === id){
                            value.onPause();
                        }
                    })
                });
                main.setMap(map);
                main.setConfig(widget.config || {});

                if (!main.created) {
                    main.onCreate();
                    main.created = true;
                }
                widgets.push(main);

                try {
                    main.onOpen();
                } catch (e) {
                    error('widget [' + main.label + '] on open error [' + e.info + ']');
                }
                log('模块 [' + widget.label + '] 加载成功');

            });
        }
        $("#panel".concat(id)).removeClass('hidden');
    }

    /**
     * 切换widget
     *
     * @param widgetId
     */
    // function _switchWidget(widgetId) {
    //     arrayUtil.forEach(widgets, function (value) {
    //         if (value.id != widgetId) {
    //             try {
    //                 if (value.created) value.onPause();
    //             } catch (e) {
    //                 error('widget [' + value.label + '] on pause error [' + e.message + ']');
    //             }
    //         }
    //     })
    // }

    /**
     * find widget by id
     * @param widgetId
     */
    function findWidgetById(widgetId) {
        for (var i in allWidgets) {
            var wid = allWidgets[i].id;
            if (wid === widgetId) {
                return allWidgets[i];
            }
        }
    }

    /**
     *
     */
    function addListener() {
        $('#menu-content > li').bind('mouseover', openMenu);
        $('#menu-content > li').bind('mouseout', setTimer);
        document.onclick = closeMenu;
    }

    /**
     *打开二级菜单
     */
    function openMenu() {
        var $menuUl = $(this).find('ul').eq(0);
        // $menuUl.css("width",$menuUl.prev("a").innerWidth());
        $menuUl.css("width", 'auto');
        cancelTimer();
        closeMenu();
        $ddmenuitem = $(this).find('ul').eq(0).css('visibility', 'visible');
        $groupIcon = $(this).find('i[data-group="1"]').removeClass('fa-angle-down').addClass('fa-angle-up');
    }

    /**
     * 关闭二级菜单
     */
    function closeMenu() {
        if ($ddmenuitem) $ddmenuitem.css('visibility', 'hidden');
        if ($groupIcon) $groupIcon.removeClass('fa-angle-up').addClass('fa-angle-down');
    }

    function setTimer() {
        closetimer = window.setTimeout(closeMenu, timeout);
    }

    function cancelTimer() {
        if (closetimer) {
            window.clearTimeout(closetimer);
            closetimer = null;
        }
    }

    /***
     * render tpl
     * @param tpl
     * @param data
     */
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }

    return Menu;
});
