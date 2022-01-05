/**
 *  属性识别配置
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/17 15:51
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "static/js/cfg/core/BaseWidgetCfg",
    "mustache",
    "static/js/cfg/core/SerializeForm",
    "static/js/cfg/core/layer-config",
    "text!../template/font-icons.html"
], function (declare, lang, BaseWidgetCfg, Mustache, SerializeForm, layerConfig, FontIconsTpl) {

    var data = null;
    var tpl = null;
    var ghAnalysis = declare([BaseWidgetCfg], {

        constructor:function(){
            tpl = ghAnalysis.arguments[1];
        },

        onCreate:function(){
            data = this;
            //data = ghAnalysis.arguments[0];
            data.isCheck = function () {
                if (this.config.showSelMode) {
                    return 'checked';
                }
                return '';
            };

            data.isExport = function () {
                if (this.config.exportAnalysis) {
                    return 'checked';
                }
                return '';
            };

            data.getLayerInfo = function () {
                return JSON.stringify(this);
            };
            data.sdeConfs=sdeConfs;

            ghAnalysis.superclass.init(tpl,data);
            layerConfig.configLayer($('#layerBtn'));
            pageListeners();

            delete data.sdeConfs;
        }
    });



    /**
     * 规划审查分析事件监听
     */
    function pageListeners(){
        var scrollHeight = $(window).height()-280;
        $("#scrollContent").slimScroll({
            height: scrollHeight>500?500:scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        $("#icons").append(FontIconsTpl);
        $('#icons').dropdownSelect();
        $('#areaUnit').dropdownSelect();
        $('#dataSource').dropdownSelect();
        $('#import').dropdownSelect();
        // $('#export').dropdownSelect();
        $('#analysisType').dropdownSelect({
            onChange:function(value, text, $choice){
                $("#analysisTypeLabel").text($($choice).text());
                if(value=='year'){
                    $("#layerType").addClass("hidden");
                    $("#layerType input").val("");
                    $("#year").removeClass("hidden");
                }else if(value=='layerType'){
                    $("#year").addClass("hidden");
                    $("#year").val("");
                    $("#layerType").removeClass("hidden");
                }
            }
        });
        $("#layerType").dropdownSelect();
        $("#analysisType").dropdownSelect('set selected',data.config.year?"year":"layerType");
        $("#analysisType").dropdownSelect('refresh');

        ghAnalysis.superclass.subscribersListener();

        ghAnalysis.superclass.toggleScoupeLayer();

        $("#fields,#layers").slimScroll({
            height: '150px',
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        /**
         * 规划审查分析保存
         */
        $("#ghAnalysisSaveBtn").on('click', function(){
            var result = {};
            var form = SerializeForm.serializeObject($("#ghAnalyssis"));

            result.label = form.label;
            result.icon = form.icon;
            result.config = {};
            result.config.scopeLayers = [];

            delete form.label;
            delete form.icon;

            if (form.showSelMode == 'on') {
                form.showSelMode = true;
            } else {
                form.showSelMode = false;
            }

            if (form.exportAnalysis == 'on') {
                form.exportAnalysis = true;
            } else {
                form.exportAnalysis = false;
            }

            lang.mixin(result.config, form);

            $.each($("#layersContainer button"), function (i, n) {
                result.config.scopeLayers.push($(n).data('info'));
            });

            lang.mixin(data, result);

            ghAnalysis.superclass.commomUpdateWidget(data);

            $(".dd3-click").parent().removeData('info');
            $(".dd3-click").parent().data('info', data);
        });
    }
    return ghAnalysis;
});