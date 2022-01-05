/**
 * Created by wangcheng on 2017/3/28.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "layer",
    "dojo/topic",
    "map/core/BaseWidget",
    "hbarsUtils",
    "map/core/EsriSymbolsCreator"
], function (declare,
             lang,
             arrayUtil,
             layer, topic,
             BaseWidget,hbarsUtils,EsriSymbolsCreator) {
    var _map,cameraData,currentIndex;
    var _appConfig;
    var me = declare([BaseWidget], {
        constructor: function () {
        },

        onCreate: function () {
            _map =  this.getMap().map();
            _init();
        },

        onPause: function () {
            
        },

        onOpen: function () {
        }
    });
    //分局正常计划，每个摄像头3次
    var FJZCJH =3;
    //分局正常计划，每个摄像头10次
    var FJZDJH =10;
    //市局正常计划，每个摄像头1次
    var SJZCJH =1;
    var isSJ=false;//是否市局
    var isCommon =true;//是否正常任务
    var pmsSelected = EsriSymbolsCreator.createPicMarkerSymbol('/omp/static/img/map/video_mark_select.png', 18, 18);

    function _init() {
        //判断ui活动tab页面从而确定重点/平常
        loadData();
        initSelector();
    }

    /**
     * 加载数据
     */
    function loadData() {
        var weekIndex = $("[name = weekIndex]").val();
        var yearIndex = $("[name=yearIndex]").val();
        $.ajax({
            url:"/omp/video/searchCameraPatrolLog",
            data:{isCommonTask:isCommon,weekIndex:weekIndex,yearIndex:yearIndex},
            success:function (data) {
                var regionName = data["regionName"];
                //此处为定制服务，硬编码
                if(data["isSJ"]){
                    isSJ =true;
                    //隐藏重点计划
                    $("#keyPlane").hide();
                }
                $("[name=title]").html(regionName+"视频巡查周计划情况");

                cameraData = data.data;
                initData();
                bindEvent();
                setPage(1);
                showOrHide(false);
                editGraphic(function (graphic) {
                    for(var i =0;i<cameraData.length;i++){
                        var id =cameraData[i].DEVICE_ID;
                        if(graphic.attributes["indexCode"]==id){
                            graphic.show();
                            //如果满足条件高亮显示
                            if(cameraData[i]["COUNT"]>cameraData[i]["YQCS"]||cameraData[i]["COUNT"]==cameraData[i]["YQCS"]){
                                graphic.setSymbol(pmsSelected);
                            }
                            break;
                        }
                    }
                });
                layout();
            }
        });
    }

    function bindEvent() {
        $("#nextPage").click(function (evt) {
            setPage(currentIndex+1);
        });
        $("#beforePage").click(function (evt) {
            setPage(currentIndex-1);
        });

        $("#keyPlane").click(function (evt) {
            isCommon = false;
            loadData();

        });

        $("#commonPlane").click(function (evt) {
            isCommon = true;
            loadData();
        });

        $("#commonPlane").click(function (evt) {
            isCommon = true;
        });

        $("[name=weekIndex]").change(function () {
            loadData();
        })

        $("[name=yearIndex]").change(function () {
            loadData();
        })
    }

    function initTool() {
        $("[name=locate]").click(function (evt) {
            var id =$(evt.target).attr("data-id");
            editGraphic(function (graphic) {
                if(graphic.attributes["indexCode"]==id){
                    _map.setScale(40000);
                    _map.centerAt(graphic.geometry);
                    if(window.Global&&window.Global.cameraGraClickHandler){
                        window.Global.cameraGraClickHandler(graphic);
                    }
                }
            });
        });

        $("[name=detail]").click(function (evt) {
            var id =$(evt.target).attr("data-id");
            openDetail(id);
        });

        $("[name=createFlow]").click(function (evt) {
            var id =$(evt.target).attr("data-id");
            createFlow(id);
        });
    }
    
    function createFlow(indexCode) {
        var height = $("#panel-VideoPatrol").height();
        $.ajax({
            url:"/omp/video/createFlow",
            data:{indexCode:indexCode},
            success:function (data) {
                data = encodeURI(data);
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.open({
                        type: 2,
                        title: '',
                        shadeClose: true,
                        shade: false,
                        maxmin: true, //开启最大化最小化按钮
                        area: ['893px', height+"px"],
                        content: [data, 'no'],
                    });
                });
            }
        });
    }

    function getWeekOfYear(){
        var today = new Date();
        var firstDay = new Date(today.getFullYear(),0, 1);
        var dayOfWeek = firstDay.getDay();
        var spendDay= 1;
        if (dayOfWeek !=0) {
            spendDay=7-dayOfWeek+1;
        }
        firstDay = new Date(today.getFullYear(),0, 1+spendDay);
        var d =Math.ceil((today.valueOf()- firstDay.valueOf())/ 86400000);
        var result =Math.ceil(d/7);
        return result+1;
    };
    
    function setPage(index,pageSize) {
        pageSize = pageSize||8;
        var pageCount = cameraData.length/pageSize;
        pageCount =Math.ceil(pageCount);
        if(!index||index==0){
            index=1;
        }
        if(index>=pageCount){
            index = pageCount;
        }
        currentIndex = index;
        var data =[];
        for(var i = (index-1)*pageSize;i<index*pageSize-1;i++){
            if(i>=cameraData.length){
                break;
            }
            data.push(cameraData[i]);
        }
        var html =  hbarsUtils.renderTpl($("#commonTask").html(),{list:data});
        $("#table_container").empty().html(html);
        initTool();
        $("[name=page]").html(index+"/"+pageCount);
    }
    /**
     * 循环管理摄像头
     */
    function editGraphic(callBack) {
        var layer = _map.getLayer("videoGraphicsLyr");
        if(!layer){
            return;
        }
        for(var i in layer.graphics){
            callBack(layer.graphics[i]);
        }
    }

    function showOrHide(isShow) {
        editGraphic(function (gra) {
            if(isShow){
                gra.show();
            }else {
                gra.hide();
            }
        });
    }
    
    function initData() {
        for(var i=0;i<cameraData.length;i++){
            var item = cameraData[i];
            if(!isSJ&&isCommon){//分局正常
                item["YQCS"]=FJZCJH;
            }else if(!isSJ&&!isCommon){//分局重点
                item["YQCS"] = FJZDJH;
            }else if(isSJ&&isCommon){//市局正常
                item["YQCS"] = SJZCJH;
            }
            if(!item["COUNT"]){
                item["COUNT"]=0;
            }
            if(item["COUNT"]>item["YQCS"]||item["COUNT"]==item["YQCS"]){
                item["isFinished"] =true;
            }
        }
        //排序
        var compare = function (x, y) {//比较函数
            if (x["COUNT"] < y["COUNT"]) {
                return -1;
            } else if (x["COUNT"] > y["COUNT"]) {
                return 1;
            } else {
                return 0;
            }
        }
        cameraData.sort(compare);
    }
    
    function initSelector() {
        var weekIndex = getWeekOfYear();
        for(var i=1;i<53;i++){
            var text = "第"+i+"周";
            var $option =$("<option></option>").html(text).attr("value",i);
            if(weekIndex==i){
                $option.attr("selected","selected");
            }
            $("[name=weekIndex]").append($option);
        }
    }
    
    function openDetail(indexCode) {
        var height = $("#panel-VideoPatrol").height();
        $.ajax({
            url:"/omp/video/getCameraDetails",
            data:{indexCode:indexCode},
            success:function (data) {
                layui.use('layer', function(){
                    var layer = layui.layer;
                    var html =  hbarsUtils.renderTpl($("#cameraDetail").html(),{list:data["data"]});
                    layer.open({
                        type: 1
                        , title: '详细信息'
                        , area: ['755px', height+'px']
                        , shade: 0.5
                        , maxmin: true
                        , zIndex: layer.zIndex
                        , content:html
                    });
                });
            }
        });
    }

    function layout() {
        var height = $("#panel-VideoPatrol").height();
        var contentHeight = height - 160;
        $("#container").height(contentHeight);
    }
    return me;
});