/*!
 * 列表数据渲染器 用于渲染前端列表数据
 * Author: yingxiufeng
 * Date:   2016/9/23 
 * Version:v1.0 (c) Copyright gtmap Corp.2016
 * 用法:
 * >>>>实例化: 使用 new ListDataRenderer(option)并传递相应的参数
 *  >>>>renderTo:dom对象或jq对象或id名称
 *  >>>>renderData:要进行渲染的json数据
 * >>>>进行渲染:
 * >>>> var listDataRenderer=new ListDataRenderer({renderTo:$listContainer,renderData:[{title:'标题',subtitle:'副标题'}],type:'io'});
 * >>>> listDataRenderer.render();
 */
define(["dojo/Evented",
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/json",
    "dojox/uuid/generateRandomUuid",
    "handlebars",
    "map/utils/MapUtils",
    "esri/graphic",
    "esri/lang"], function (Evented, declare, lang, arrayUtil, dojoJSON, generateRandomUuid, Handlebars, MapUtils, Graphic, esriLang) {
        var _noData, _type;
        //定位事件
        var LIST_EVENT_LOC = "location";
        //分析事件
        var LIST_EVENT_ANA = "analysis";
        //推送事件
        var LIST_EVENT_POST = "post";

        var LIST_EVENT_EXPORT = "export";

        var LIST_EVENT_DELETE = "delete"

        var noDataTpl = '<span class="text-muted pull-left no-data">{{desc}}</span>';

        //基础模板
        var baseTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //定位模板
        var locTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}" data-dchybh="{{dchybh}}">&#xe634;</span>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //项目信息定位模板
        var projectLocTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-openXmxx-btn-loc" title="定位" data-uid="{{uid}}" data-dchybh="{{dchybh}}">&#xe634;</span>' +
            '<i class="fa fa-outdent info-openXmxx-btn" title="查看项目信息" data-dchybh="{{dchybh}}"></i>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //多选模板
        var checkboxTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt" style="float: left;padding-left: 10px;">' +
            '<span class="iconfont text-primary info-box-btn-loc"><input type="checkbox" value="{{title}}" /></span>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';


        //定位导出模板
        var exportTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}">&#xe634;</span>' +
            '<span class="iconfont text-primary info-box-btn-export" title="导出" data-uid="{{uid}}">&#xe617;</span>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //分析模板
        var analysisTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}">&#xe634;</span>' +
            '<span class="iconfont text-primary info-box-btn-analyze" title="分析" data-uid="{{uid}}">&#xe620;</span>' +
            '</div>' +
            '<div class="info-box-content m-r-48">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //导入导出模板
        var ioTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}">&#xe634;</span>' +
            '<span class="iconfont text-primary info-box-btn-sel active" data-uid="{{uid}}">&#xe61c;</span>' +
            '</div>' +
            '<div class="info-box-content m-r-48">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //定位并post信息模板
        var locAndPostTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}">&#xe634;</span>' +
            '<span class="iconfont text-primary info-box-btn-post" title="确认关联" data-uid="{{uid}}">&#xe614;</span>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';

        //定位删除模板
        var locAndDelTpl = '<div class="info-box" title="{{title}}">' +
            '<div class="info-box-opt">' +
            '<span class="iconfont text-primary info-box-btn-loc" title="定位" data-uid="{{uid}}">&#xe634;</span>' +
            '<span class="iconfont text-primary info-box-btn-del" title="删除" data-uid="{{uid}}">&#xe60b;</span>' +
            '</div>' +
            '<div class="info-box-content">' +
            '<span class="info-box-title text-primary">{{title}}</span>' +
            '<span class="info-box-subtitle">{{subtitle}}{{#blankData subtitle}}{{/blankData}}</span>' +
            '</div></div>';


        var $container, _cacheData = [], _activeData = [];

        var me = declare([Evented], {

            /**
             * 渲染目标节点
             * jq/dom/string
             */
            renderTo: undefined,

            /***
             * 渲染的数据
             * json
             */
            renderData: undefined,
            /***
             * 模板类型
             * basic ---基本模板
             * analysis---分析列表模板
             * loc----属性识别/信息查询模板
             * io-----导入导出
             */
            type: 'basic',
            /***
             *
             */
            noData: '无数据',
            /***
             *
             */
            map: undefined,

            constructor: function (option) {
                lang.mixin(this, option);
                _noData = this.noData;
                _type = this.type;
                MapUtils.setMap(this.map);
                Handlebars.registerHelper('blankData', function (context, options) {
                    if (context === undefined || context === "")
                        return new Handlebars.SafeString("<small class='text-muted'>空</small>");
                });
            },
            /***
             * 渲染数据
             * @param data
             */
            render: function (rd) {
                if (esriLang.isDefined(rd)) this.renderData = rd;
                var renderTo = this.renderTo;
                var data = this.renderData;
                if (esriLang.isDefined(renderTo)) {
                    $container = getContainer(renderTo);
                    if (data === '' || !esriLang.isDefined(data)) {
                        $container.append(renderTpl(noDataTpl, { desc: _noData }));
                    } else {
                        if (lang.isString(data)) {
                            data = dojoJSON.parse(data);
                        }
                        if (lang.isArray(data)) {
                            $.each(data, lang.hitch(this, this._renderItem));
                        }
                    }
                } else
                    console.error("Property renderTo cannot be undefined!");
                //监听按钮事件
                $(".info-box-btn-loc").on('click', lang.hitch(this, this._eventListeners, 'location'));

                $(".info-openXmxx-btn-loc").on('click', lang.hitch(this, this._eventListeners, 'location'));
                $(".info-box-btn-export").on('click', lang.hitch(this, this._eventListeners, 'export'));
                $(".info-box-btn-del").on('click', lang.hitch(this, this._eventListeners, 'delete'));


                $(".info-box-btn-analyze").on('click', lang.hitch(this, this._eventListeners, 'analysis'));
                $(".info-box-btn-post").on('click', lang.hitch(this, this._eventListeners, 'post'));
                selectionListener();
                // 显示项目信息
                openXmxq();
            },
            /***
             * 获取已经选中的列表数据(针对有状态的数据)
             */
            getActiveData: function () {
                return _activeData;
            },
            /***
             * 渲染单个项目
             * @param item
             * @private
             */
            _renderItem: function (idx, item) {
                //渲染数据列表
                if (esriLang.isDefined(item)) {
                    if (!item.hasOwnProperty("id")) {
                        item.uid = generateRandomUuid();
                    }
                    _cacheData.push(item);
                    _activeData.push(item);//默认"选中"状态
                    $container.append(renderTpl(item));
                }
            },
            /***
             * 发布事件监听
             * @private
             */
            _eventListeners: function (evtType, evt) {
                var uid = $(evt.currentTarget).data("uid");
                var tmp = arrayUtil.filter(_cacheData, function (item) {
                    return item.uid === uid;
                });
                if (tmp.length > 0) {
                    switch (evtType) {
                        case 'location':
                            this.emit(LIST_EVENT_LOC, tmp[0]);
                            break;
                        case 'analysis':
                            this.emit(LIST_EVENT_ANA, tmp[0]);
                            break;
                        case 'post':
                            this.emit(LIST_EVENT_POST, tmp[0]);
                            break;
                        case 'export':
                            this.emit(LIST_EVENT_EXPORT, tmp[0]);
                        case 'delete':
                            this.emit(LIST_EVENT_DELETE, tmp[0]);
                            break;
                    }
                }
            }
        });
        // 显示项目信息
        function openXmxq() {
            var slbh = '';
            var xmid = '';
            var xmbh = '';
            //打开空间一棵树
            $('.info-openXmxx-btn').click(function () {
                //动态获取url
                var _url = '';
                $.ajax({
                    url: getContextPath() + "/oms/rest/v1.0/dchy/geturl",
                    type: 'post',
                    async: false,
                    success: function (data) {
                        _url = data;
                    }
                });
                $('.project-info .finished-box form').empty();
                var dchybh = $(this).attr('data-dchybh');
                $('.project-info').find('.xmbh').text('');
                $('.project-info').find('.xmmc').text('');
                $('.project-info').find('.xmdz').text('');
                $('.project-info').find('.jsdw').text('');
                $.ajax({
                    url: _url + '/msurveyplat-server/rest/v1.0/onemap/xmxx/'+dchybh,
                    //url: "http://192.168.50.60:8083/msurveyplat-server/rest/v1.0/onemap/xmxx/" + dchybh,
                    type: 'post',
                    async: false,
                    success: function (res) {
                        var xm = res.dchyCgglXmDO;
                        slbh = xm.slbh;
                        xmid = xm.xmid;
                        xmbh = xm.chxmbh;
                        $('.project-info').find('.xmbh').text(xm.chxmbh);
                        $('.project-info').find('.xmmc').text(xm.xmmc);
                        $('.project-info').find('.xmdz').text(xm.xmdz);
                        $('.project-info').find('.jsdw').text(xm.jsdw);

                        //渲染已完成事项
                        layui.use(['form'], function () {
                            var form = layui.form;
                            res.surveyItemVoList.forEach(function (v) {
                                var isChecked = v.sfwcsx ? 'checked' : 'disabled';
                                var className = v.sfwcsx ? 'checked-span' : 'disabled-span';
                                var html = '<p class="finished-item"><input type="checkbox" name="" disabled lay-skin="primary" ' + isChecked + '/><span class="' + className + '" data="' + v.slbh + '">' + v.chsxmc + '</span></p>';
                                $('.project-info .finished-box form').append(html);
                            })
                            form.render('checkbox');
                            $('.project-info').show();
                        })   
                    }
                })
                
                //打开项目一棵树详情
                $('.project-tree-btn').unbind('click');
                $('.project-tree-btn').bind('click', function () {
                    window.open(root + '/static/js/map/template/dchyxxglpt/dchyxmsmx.html?slbh=' + slbh + '&xmid=' + xmid + '&xmbh=' + xmbh);
                });
            })
            //关闭项目信息弹出框
            $('.project-info-close-btn').click(function () {
                $('.project-info').hide();
            })
        }
        /**
         * 监听选中切换按钮
         */
        function selectionListener() {
            $(".info-box-btn-sel").on('click', function () {
                var $this = $(this);
                var uid = $this.data("uid");
                var isActive = $this.hasClass("active");
                if (isActive) {
                    $this.removeClass("active");
                    $this.empty();
                    $this.html("&#xe60c;");
                    arrayUtil.forEach(_activeData, function (item) {
                        if (esriLang.isDefined(item) && item.hasOwnProperty("uid") && uid === item.uid) {
                            _activeData.splice(i, 1);
                            return;
                        }
                    });
                } else {
                    $this.addClass("active");
                    $this.empty();
                    $this.html("&#xe61c;");
                    arrayUtil.forEach(_cacheData, function (item) {
                        if (esriLang.isDefined(item) && item.hasOwnProperty("uid") && uid === item.uid) {
                            _activeData.push(item);
                            return;
                        }
                    });
                }
            });
        }

        /***
         * render tpl with handlebars
         * @param source   render tpl
         * @param data     render data
         */
        function renderTpl(data) {
            var source = "";
            switch (_type) {
                case 'analysis':
                    source = analysisTpl;
                    break;
                case 'io':
                    source = ioTpl;
                    break;
                case 'loc':
                    source = locTpl;
                    break;
                case 'projectLoc':
                    source = projectLocTpl;
                    break;
                case 'checkbox':
                    source = checkboxTpl;
                    break;
                case 'basic':
                    source = baseTpl;
                    break;
                case 'locAndPost':
                    source = locAndPostTpl;
                    break;
                case 'locAndDelTpl':
                    source = locAndDelTpl;
                    break;
                case 'export':
                    source = exportTpl;
                    break;
            }
            var template = Handlebars.compile(source);
            return template(data);
        }

        /***
         * jq object
         * @param t
         * @returns {*}
         */
        function getContainer(t) {
            if (lang.isString(t)) {
                if (t.startWith('#')) return $(t);
                else return $('#' + t);
            } else if (t && typeof t === 'object' && t.nodeType === 1 && typeof t.nodeName === 'string') {
                return $(t);
            } else if (t instanceof Object) {
                return t;
            } else return undefined;
        }

        return me;
    });