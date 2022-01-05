/**
 * 东台批而未供分析
 * 并可以打印输出添加标记后的地图 以及添加动态标绘 并结合分析 进行扩展
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "mustache",
    "map/core/BaseWidget",
    "handlebars",
    "layer"], function (declare, lang, Mustache, BaseWidget, Handlebars, layer) {

    var __map, _config;


    var me = declare([BaseWidget], {
        /***
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            _config = this.getConfig();
            _init();
        }
    });


    /***
     * init
     * @private
     */
    function _init() {
        layer.config();
        if (_config.year.length > 0) {
            //默认选中最新的年份数据
            var temp = _config.year.reverse();
            temp[0].selected = 'selected';
            $("#pewgAnalysisPanel").append(renderTpl($("#pewgYearSelectTpl").html(), {years: _config.year}));
        }

        /**
         * 分析按钮
         */
        $("#pewgBtn").click(function () {
            var nd=$("#pewgYearSelector  option:selected").val();
            var dataSource = _config.dataSource;
            var bpdkLayerName = _config.bpdkLayerName;
            var gddkLayerName = _config.gddkLayerName;
            var xzqLayerName = _config.xzqLayerName;
            if (dataSource !== undefined && bpdkLayerName !== undefined && gddkLayerName !== undefined && xzqLayerName !== undefined) {
                var url = root + '/geometryService/analysis/dtpewg';
                execute(url, {
                    dataSource: _config.dataSource,
                    bpdkLayerName: _config.bpdkLayerName,
                    gddkLayerName: _config.gddkLayerName,
                    xzqLayerName: _config.xzqLayerName,
                    year:nd
                });
            }
            else {
                layer.msg("请配置完整参数！");
            }
        });


        function execute(a, b) {
            var form = $("<form method='post' style='display:none;' target='" + this.targetName + "'></form>"),
                input;
            form.attr({"action": a});
            $.each(b, function (key, value) {
                input = $("<input type='hidden'>");
                input.attr({"name": key});
                input.val(value);
                form.append(input);
            });
            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
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

    return me;
});