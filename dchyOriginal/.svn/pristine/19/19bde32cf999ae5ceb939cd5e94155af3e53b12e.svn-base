/**
 * Created by user on 2017/4/13.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "map/core/BaseWidget",
    "layer",
    "static/thirdparty/laydate/laydate"
], function (declare, lang, BaseWidget, layer) {
    var __config; //配置信息
    var ghzxStart, ghzxEnd; //日期控件
    var me = declare([BaseWidget], {

        onCreate: function () {
            __config = this.getConfig();
            _init();
            _addListener();
        },

        onPause: function () {
        },

        onOpen: function () {
        }
    });

    function _init() {
        laydate.skin("danlan");
        //开始日期控件
        ghzxStart = {
            elem: '#ghzsStart',
            path: "/laydate",
            format: 'YYYY-MM-DD',
            max: laydate.now(0),
            istime: false,
            istoday: true,
            choose: function (datas) {
                ghzxEnd.min = datas; //开始日选好后，重置结束日的最小日期
            }
        };

        //结束日期控件
        ghzxEnd = {
            elem: '#ghzxEnd',
            path: "/laydate",
            format: 'YYYY-MM-DD',
            max: laydate.now(0),
            istime: false,
            istoday: true,
            choose: function (datas) {
                ghzxStart.max = datas; //结束日选好后，重置开始日的最小日期
            }
        };
    }

    function _addListener() {
        //开始日期点击事件
        $('#ghzsStart').on('click', function () {
            laydate(ghzxStart);
        });

        //结束日期点击事件
        $('#ghzxEnd').on('click', function () {
            laydate(ghzxEnd);
        });

        $("#doGhzxAnalysis").on('click', function () {
            var startDate = $('#ghzsStart').val();
            var endDate = $('#ghzxEnd').val();
            if (!startDate || !endDate) {
                layer.msg("请选择日期！", {icon: 2, time: 2000});
                return;
            }
            //模拟表单提交
            var form = $("<form method='post' style='display:none;' target='_blank'></form>"),
                input;
            form.attr({"action": root + "/geometryService/analysis/gh"});
            $.each(lang.mixin(__config, {startDate: startDate, endDate: endDate}), function (key, value) {
                input = $("<input type='hidden'>");
                input.attr({"name": key});
                input.val(value);
                form.append(input);
            });
            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
        });
    }

    return me;
});