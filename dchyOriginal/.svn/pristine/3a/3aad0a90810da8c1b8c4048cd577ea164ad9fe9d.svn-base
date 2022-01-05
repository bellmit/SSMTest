

define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/manager/ConfigManager",
    "esri/lang",
    "handlebars",
    "layer"], function (declare, lang, arrayUtil, BaseWidget, Environment, ConfigManager,esriLang, Handlebars,layer) {

    var _mainmap, _widgetData;
    var _configManager=ConfigManager.getInstance();
    var me = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            _mainmap = this.getMap();
            _widgetData = this.getConfig();
            this.getAppConfig();
            _init();
        },
        onOpen: function () {

        },
        /***
         * fires before destroy widget
         */
        onDestroy: function () {

        }

    });

    var thematicList,tpl_,queryDataTpl,title,features;
    /***
     *
     * @private
     */
    function _init() {

        tpl_ = $("#ConstructionGroupItemsTpl").html();
        var thematicMapsTpl = $("#thematicMapsTpl").html();
        queryDataTpl = $("#queryDataTpl").html();


        //table 统计
        $("#statistics_data").append(renderTpl(tpl_, _widgetData));
        $("#statistics_data").append(renderTpl(thematicMapsTpl, _widgetData));

        //大于两行滚动
        var td=document.getElementById("table").getElementsByTagName("td")
        var num=0
        for(var i=0;i<2;i++)
        {
            num =parseInt(td[i].offsetHeight)+num;
        }
        document.getElementById("table_div").style.height=num;

        //labelHead
        $("#label_head").html(_widgetData.label);
        //ThematicMaps
        thematicList = _widgetData.thematicMaps;


        for (var j = 0; j < thematicList.length; j++) {//配置好直接去查
            var dataUrl;
            var obj = thematicList[j].titleHead;
            var check = thematicList[j].checked;
            if (check == "checked") { //配置直接配勾选点时候
                dataUrl = thematicList[j].dataUrl;
                thematicQuery(dataUrl, obj);
            }
        }

        //监听checkbox事件
        $(".thematic-cbx").on('change', function () {
            var $this = $(this);
            var checked = $this[0].checked;
            var sid = $this.data("sid");

            for (var i = 0; i < thematicList.length; i++) {
                var dataUrl;
                var obj = thematicList[i].titleHead;
                if(!checked){
                    $("#queryDatahtml").remove();
                }
                if(sid == obj&& checked == true){
                    dataUrl = thematicList[i].dataUrl;
                    if(checked){
                        thematicQuery(dataUrl,obj);
                    }
                }
            }
        });
    }

    /**
     * 只能勾选一个
     */
    function checkOne(){
        thematicList

    }

    /**
     * 专题查询
     * @constructor
     */
    function thematicQuery(dataUrl,obj){
        title = obj;

        var where = "1=1";
        var data = {//数据组装返回查询
            layerUrl: dataUrl.concat("/0"),
            where: where,
            returnFields: obj,
            page: 0,
            size: 10
        };

        $.ajax({
            url: "/omp/map/query",
            data: data,
            success: function (r) {
                $("#queryDatahtml").remove();
               var obj_data = JSON.parse(r);
                features = obj_data.features;
               if(typeof(obj_data.features) == "undefined" ){
                   $("#queryDatahtml").remove();
               }
                $("#quert_data").append(renderTpl(queryDataTpl,{fs: obj_data.features}));
            }
        });

        //点击查询
        $("#query_").on('click', function () {

            var queryData = $("#queryData").val();
            if(queryData == ""){
                layer.msg("查询输入不能为空");
                $("#queryDatahtml").remove();
               return ;
            }
            var arr = [];
            for (var i = 0; i < features.length; i++) {
                var fea = features[i];
                var obj1 = fea.attributes;
                $.each(obj1,function(i,n){
                    if(queryData == n){
                        arr.push(fea);
                    }
                });
            }
            $("#queryDatahtml").remove();
            $("#quert_data").append(renderTpl(queryDataTpl,{fs: arr}));
        });


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
