/**
 * full search 全文检索以及命令模式启动功能模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "esri/request",
    "esri/graphic",
    "esri/SpatialReference",
    "layer",
    "laypage",
    "map/manager/WidgetsManager",
    "map/manager/MapManager",
    "map/manager/ConfigManager",
    "map/utils/MapUtils",
    "map/core/support/Environment",
    "text!static/js/map/template/search/search-result-tpl.html",
    "map/component/ListDataRenderer",
    "map/core/JsonConverters",
    "autocomplete",
    "css!static/thirdparty/jquery/jquery.autocomplete.css",
    "dojo/domReady!"], function (declare, lang, arrayUtil, esriRequest, Graphic, SpatialReference, layer, laypage, WidgetsManager, MapManager, ConfigManager, MapUtils, Environment,
                                 SearchTpl, ListDataRenderer, JsonConverters) {

    var instance, me = declare(null, {

        /***
         * 构造函数
         */
        constructor: function () {

        },
        /***
         * 初始化dom以及添加相关监听
         */
        init: function () {
            _init();
        }

    });
    var $searchInput, $searchBtn;
    var widgetsManager = WidgetsManager.getInstance();
    var mapManager = MapManager.getInstance();
    var widgets = [];
    var winHeight = 100;
    var _map;
    var tplConfig = ConfigManager.getInstance().getAppConfig();

    //系统命令集合
    var commands = [{value: "系统管理", data: "Admin", url: "Admin", label: "系统管理"}, {value: "坐标点定位", data: "Point"}, {
        value: "比例尺",
        data: "Scale"
    }, {value: "xy坐标", data: "Coord"}];

    /***
     *
     * @private
     */
    function _init() {
        _map = mapManager.getCurrentMap().map();
        layer.config();
        $searchInput = $("#sysSearchInput");
        $searchBtn = $("#sysSearchBtn");
        fixIE8();
        $searchInput.on('focus', function () {
            fixIE8('focus');
        });
        $searchInput.on('blur', function () {
            fixIE8();
        });

        var wc = tplConfig.widgetContainer;
        var widgets = wc.widgets;
        var wg = wc.widgetsGroup;
        if (lang.isArray(widgets))
            parseWidgetCommands(widgets);
        if (lang.isArray(wg)) {
            $.each(wg, function (i, item) {
                var ws = item.widgets;
                if (lang.isArray(ws)) {
                    parseWidgetCommands(ws);
                }
            });
        }

        $searchInput.autocomplete({
            width: '265px',
            containerClass: 'autocomplete-suggestions g-anim g-anim-upbit',
            lookupLimit: 10,
            lookup: commands,
            lookupFilter: function (suggestion, query, queryLowerCase) {
                if (!queryLowerCase.startWith('#'))return;
                queryLowerCase = queryLowerCase.substr(1, queryLowerCase.length - 1);
                if (queryLowerCase === "")return;
                var val = lang.trim(suggestion.value).toLowerCase();
                var data = lang.trim(suggestion.data).toLowerCase();
                var contains = val.indexOf(queryLowerCase) > -1 || data.indexOf(queryLowerCase) > -1;
                if (contains)
                    return suggestion;
            },
            onSelect: function (suggestion) {
                executeCommand(suggestion);
                $searchInput.val("");
            }
        });

        $searchInput.on('keydown', function (evt) {
            if (evt.keyCode === 13 && evt.key === "Enter") {
                var val = $(this).val();
                if (val.startWith('#'))
                    executeCommand(val);
                else
                    executeIndexSearch(val);
            }
        });

        $searchBtn.on('click', function () {
            var val = $searchInput.val();
            if (val.startWith('#'))
                executeCommand(val);
            else
                executeIndexSearch(val);
        });
    }

    /***
     * 修改ie8的placeholder
     * @param state
     */
    function fixIE8(state) {
        if (Environment.isLessThanIE9()) {
            switch (state) {
                case 'focus':
                    $searchInput.val('');
                    $searchInput.removeClass('input-placeholder-ie');
                    break;
                default:
                    $searchInput.val('搜索/#功能');
                    $searchInput.addClass('input-placeholder-ie');
                    break;
            }
        }
    }

    /***
     * 解析配置中已经配置的菜单 用于命令打开
     * @param arr
     */
    function parseWidgetCommands(arr) {
        $.each(arr, function (i, item) {
            widgets.push(item);
            var obj = {value: item.label, data: item.url, widget: item};
            if ($.inArray(obj, commands) > -1) {
            } else
                commands.push(obj);
        });
    }

    /***
     * 执行系统命令
     * @param obj
     */
    function executeCommand(obj) {
        if (lang.isString(obj)) {
            var arr = obj.split(" ");
            if (arr.length > 1)
                executeSysCommand(arr[0].substr(1, arr[0].length - 1), arr[1]);
            else
                executeSysCommand(arr[0].substr(1, arr[0].length - 1));
        } else {
            if (obj.hasOwnProperty("widget")) {
                widgetsManager.loadFuncWidget(obj.widget);
            } else {
                var command = obj.value.substr(1, obj.value.length - 1);
                executeSysCommand(command);
            }
        }
    }

    /***
     * 执行系统默认命令 如控制控件的显示等
     * @param command
     * @param content
     * @returns {*}
     */
    function executeSysCommand(command, content) {
        if (command != undefined) {
            switch (command.toLowerCase()) {
                case 'admin':
                    widgetsManager.loadFuncWidget(commands[0]);
                    break;
                case 'scale':
                    $("#scaleInfo").toggle();
                    break;
                case 'coord':
                    $("#coordsInfo").toggle();
                    break;
                case 'point':
                    //todo...
                    break;
                default :
                    for (var i in commands) {
                        if (commands[i].value.equalsIgnoreCase(command) || commands[i].data.equalsIgnoreCase(command)) {
                            widgetsManager.loadFuncWidget(commands[i].widget);
                            break;
                        }
                    }
                    break;
            }
        }

    }

    /***
     * 执行索引搜索
     * @param val
     */
    function executeIndexSearch(val) {
        ajaxToQuery(val);
        layer.closeAll();
        if (!val)return;
        layer.open({
            type: 1,
            title: '<i class="fa fa-map-marker"></i>&nbsp;&nbsp;检索结果',
            area: ['320px', winHeight + 'px'],
            content: SearchTpl,
            offset: getWinOffset(),
            shade: 0,
            shadeClose: false
        });
    }

    /**
     * 请求分页数据
     * @param q
     * @param p
     * @param sz
     * @param s
     */
    function ajaxToQuery(q, p) {
        //结果窗的高度
        winHeight = _map.height - $("#MenuBar").height() - $("#Navigation").height() - 20;
        if (q === "" || q === undefined) {
            return;
        }
        $.ajax({
            type: "POST",
            url: "/omp/map/search/page",
            data: {
                q: q,
                p: p || 1,
                sz: parseInt((winHeight - 50) / 70, 10),
                s: false
            },
            success: function (rp) {
                if (!rp.hasOwnProperty('content')) {
                    $(".search-result-wrapper").empty();
                    $(".search-result-wrapper").append('<p class="text-muted text-center m-t-10">未找到 "' + q + '" 相关的内容!</p>');
                } else {
                    showResultWin(rp, parseInt((winHeight - 50) / 70, 10));
                }
            }
        });
    }

    /**
     * 展示分页数据
     * @param rp
     * @param sz
     */
    function showResultWin(rp, sz) {
        var locGras = rp.content;
        if (locGras != undefined && locGras.length > 0) {
            //处理数据
            var renderData = [];
            arrayUtil.forEach(locGras, function (attr) {
                var tmp = {};
                lang.mixin(tmp, {title: attr["title"], subtitle: attr["subtitle"]});
                tmp.graphic = new Graphic({geometry: JsonConverters.toEsri(JSON.parse(attr.shape), _map.spatialReference)});
                tmp.uid = attr.id;
                renderData.push(tmp);
            });
            //加载定位结果列表
            $('.search-result-list').empty();
            var listDataRenderer = new ListDataRenderer({
                renderTo: $('.search-result-list'),
                type: "loc",
                map: _map,
                renderData: renderData
            });
            listDataRenderer.on('location', function (data) {
                var g = data.graphic;
                MapUtils.highlightFeatures([g], false);
                MapUtils.locateFeatures([g], 4);
            });
            listDataRenderer.render();

            //分页模块
            laypage({
                cont: $('#layer_page'),
                pages: rp.totalElements > 100 ? parseInt(100 / sz, 10) - 1 : rp.totalPages, //总页数
                groups: 3,
                skin: 'molv', //皮肤
                curr: rp.number || 1, //当前页
                first: 1,
                last: rp.totalElements > 100 ? parseInt(100 / sz, 10) - 1 : rp.totalPages,
                prev: false,
                next: false,
                jump: function (obj, first) {
                    if (!first) {
                        ajaxToQuery($("#sysSearchInput").val(), obj.curr);
                    }
                }
            });
        } else {
            $('.search-result-list').empty();
            $('.search-result-list').append('<div class="text-center">未查询到相关数据！</div>');
        }
    }


    /***
     * 计算结果窗口的坐标位置
     * @returns {*[]}
     */
    function getWinOffset() {
        var clientHeight = document.body.clientHeight;
        var clientWidth = document.body.clientWidth;
        var left = clientWidth - 335;
        var top = (clientHeight - winHeight) / 2;
        return [top + 'px', left + 'px'];
    }

    //自执行
    instance = new me();
    instance.init();
    /**
     * get instance
     *
     * @returns {*}
     */
    me.getInstance = function () {
        if (instance === undefined) instance = new me();
        return instance;
    };
    return me;
});