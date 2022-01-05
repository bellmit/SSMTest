/**
 *  属性识别配置
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 15:51
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "mustache",
    "static/js/cfg/core/BaseWidgetCfg",
    "static/js/cfg/core/SerializeForm",
    "static/js/cfg/core/layer-config",
    "text!../template/font-icons.html",
    "text!static/js/cfg/template/year-info.html",
    "dojo/domReady!"
], function (declare, lang, Mustache, BaseWidgetCfg, SerializeForm,layerConfig, FontIconsTpl,yearInfoTpl) {

    //配置数据
    var data = null;
    var tpl = null;

    var XzAnalysis = declare([BaseWidgetCfg], {
        constructor: function () {
            tpl = XzAnalysis.arguments[1];
        },
        onCreate:function(){
            data = this;
            data.sdeConfs = sdeConfs;
            data.isCheck = function () {
                if (this.config.showSelMode) {
                    return 'checked';
                }
                return '';
            };

            data.getLayerInfo = function () {
                return JSON.stringify(this);
            };

            data.getYearInfo = function () {
                var years = [];
                $.each(this.config.year, function (i, n) {
                    years.push(n.year);
                });
                return years.toString();
            };

            XzAnalysis.superclass.init(tpl,data);

            layerConfig.configLayer($('#layerBtn'));

            pageListeners();

            delete data.sdeConfs;
        }
    });

    /***
     * year popover listener
     */
    function yearPopListener() {
        //初始化popover
        $("#year .label:not(.delete)").popover({
            title: '编辑信息',
            trigger: 'click',
            html: true,
            container: 'body',
            animation: true,
            placement: 'button',
            content: function () {
                if ($(".popover ").length != 0) {
                    $("#year .label").popover('hide');
                    $(".popover").detach();
                }
                var value = $(this).data('value');
                var content = Mustache.render(yearInfoTpl, $("#year .menu div[data-value='" + value + "']").data('info'));
                return content;
            }
        });
        //追加popover监听
        $("#year .label").on('shown.bs.popover', function () {
            //阻止冒泡，防止父元素监听
            $('#yearForm, #yearForm input').on('click', function (evt) {
                evt.stopPropagation();
            });
            /**
             *  修改年度里面的信息
             */
            $("#yearOkBtn").on('click', function (evt) {
                evt.stopPropagation();
                var data = SerializeForm.serializeObject($("#yearForm"));
                $("#year .menu div[data-value='" + data.year + "']").removeData('info');
                $("#year .menu div[data-value='" + data.year + "']").data('info', data);
                $("#year .label").popover('hide');
                $(".popover").detach();
            });

            $("#yearCancelBtn").on('click', function (evt) {
                evt.stopPropagation();
                $("#year .label").popover('hide');
                $(".popover ").detach();
            });
        });
    }


    /**
     * 现状分析添加事件监听
     */
    function pageListeners() {
        var years = [];
        var yearItem = '{{#years}}<div class="item" data-info="{{getInfo}}" data-value="{{year}}">{{year}}</div>{{/years}}';
        var scrollHeight = $(window).height()-280;
        $("#scrollContent").slimScroll({
            height: scrollHeight>500?500:scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });


        //界面控制
        $("#icons").append(FontIconsTpl);
        $('#icons').dropdownSelect();
        $('#dataSource').dropdownSelect();
        $('#import').dropdownSelect();

        //构建年份的选择菜单
        $.each(['2009', '2010', '2011', '2012', '2013', '2014', '2015', '2016', '2017', '2018', '2019', '2020'], function (i, n) {
            var year = {
                "dltb": "DLTB_H_" + n,
                "xzdw": "XZDW_H_" + n,
                "year": n
            };
            var flag = true;
            $.each(data.config.year, function (j, m) {
                if (m.year == n) {
                    years.push(m);
                    flag = false;
                    return false;
                }
            });
            if (flag) {
                years.push(year);
            }
        });
        $("#year .menu").empty();
        $("#year .menu").append(Mustache.render(yearItem, {
            years: years, getInfo: function () {
                return JSON.stringify(this)
            }
        }));

        $('#year').dropdownSelect({
            action: 'select',
            keys: false,
            metadata: {
                toggle: 'popover',
                trigger: 'focus'
            },
            onChange: function (value, text, $selectedItem) {
                // custom action
                $("#year .label").popover('hide');
                $(".popover ").detach();
            },
            onAdd: function (value, text, $selected) {
                setTimeout(function () {
                    yearPopListener();
                }, 1500);
            }
        });

        XzAnalysis.superclass.subscribersListener();
        yearPopListener();
        XzAnalysis.superclass.toggleScoupeLayer();

        $("#fields,#layers").slimScroll({
            height: '150px',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });


        //保存
        $("#xzAnalysisSaveBtn").on('click', function () {
            var result = {};
            var form = SerializeForm.serializeObject($("#xzAnalyssis"));
            var year = null;

            result.label = form.label;
            result.icon = form.icon;
            result.config = {};
            result.config.scopeLayers = [];
            result.config.year = [];

            delete form.label;
            delete form.icon;

            if (form.showSelMode == 'on') {
                form.showSelMode = true;
            } else {
                form.showSelMode = false;
            }
            year = lang.clone(form.year);
            if (year != null || year != undefined) {
                $.each(year.split(","), function (i, n) {
                    result.config.year.push($("#year .menu div[data-value='" + n + "']").data('info'));
                });
            }

            delete form.year;
            form.exportAnalysis=true;
            lang.mixin(result.config, form);

            $.each($("#layersContainer button"), function (i, n) {
                result.config.scopeLayers.push($(n).data('info'));
            });

            lang.mixin(data, result);

            XzAnalysis.superclass.commomUpdateWidget(data);

            $(".dd3-click").parent().removeData('info');
            $(".dd3-click").parent().data('info', data);
        });
    }

    return XzAnalysis;
});