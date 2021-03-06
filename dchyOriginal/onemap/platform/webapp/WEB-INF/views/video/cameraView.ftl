<!doctype html>
<html lang="en">
<head>
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <@com.style name="static/thirdparty/layui/css/layui.css"></@com.style>
    <@com.style name="static/thirdparty/ztree_normal/css/metroStyle/metroStyle.css"></@com.style>
    <@com.style name="static/thirdparty/dateSlider-master/css/dateSlider.css"></@com.style>

    <@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>
    <@com.script name="static/js/preview.js"></@com.script>
    <@com.script name="static/thirdparty/layui/layui.all.js"></@com.script>
    <@com.script name="static/thirdparty/ztree_normal/js/jquery.ztree.all.js"></@com.script>
    <@com.script name="static/thirdparty/dateSlider-master/js/jquery.ui.js"></@com.script>
    <@com.script name="static/thirdparty/dateSlider-master/js/DateSlider.js"></@com.script>
    <style type="text/css">
        /**{*/
            /*border: 0.3px solid black;*/
        /*}*/

        #camera_view_all{
            width: 98%;
            background-color: rgb(217,231,253);
            margin:10px auto;
        }

        #camera_view_left{
            min-width: 100px;
            width: 12%;
            margin: auto;
            float: left;
            overflow: auto;
        }

        #camera_view_main{
            width: 85%;
            height: 100%;
            float: left;
            margin-left: 10px;
        }

        #camera_view_content{
            /*height: 760px;*/
        }

        #camera_view_page{
            height: 40px;
        }

        #tree_container{
            width: 80%;
            margin:5% auto;
            height: 90%;
            overflow: hidden;
            background-color: rgb(203,213,225);
        }



        #year_select option{
            height: 50px;
        }

        .imgItem{
            width: 100%;
            cursor: pointer;
        }

        .imgItemContainer{
            padding: 5px;
            position: relative;
        }

        .imgItemLabel{
            position: absolute;
            bottom: 5px;
            right: 5px;
            background-color:rgb(66,132,243);
            padding: 5px;
            line-height: 20px;
            letter-spacing: 1px;
            color: white;
            cursor: pointer;
        }

        .imgShowContainer{
            /*width: 800px;*/
            /*height: 700px;*/
            width: 100%;
            height: 100%;
        }

        .imgShowContainer img{
            width: 100%;
            height: 100%;
        }
    </style>

    <script type="text/x-handlebars-template" id="imgTemplate">
        {{#  layui.each(d.content, function(index, item){ }}
            <div class="layui-col-md4 imgItemContainer">
                <img class="imgItem" src="/omp/file/original/{{item.imgId}}"/>
                <div class="imgItemLabel">{{item.camera.name}}</div>
            </div>
        {{#  }); }}
    </script>

    <script type="text/x-handlebars-template" id="imgShow">
        <img src="{{src}}" style="width: 100%;height: 100%"/>
    </script>

    <script type="text/javascript">
        var start,end;
        var size=9;
        var regions="";

        var window_width = $(window).width();
        var widow_height = $(window).height();

        var camera_view_content_height = widow_height-150;
        var imgHeight = camera_view_content_height/3-10;
        /*
        ???????????????
         */
        function initTree() {
            $.ajax({
                url:  "<@core.rootPath/>/video/fetch/root/groups",
                success: function (r) {
                    renderVideoTreeData(r);
                }
            });
            //?????????
            function renderVideoTreeData(data) {
                //???????????????????????????????????????????????????
                function getCamerasByRegionName(treeNode) {
                    if(treeNode["children"]&&treeNode["children"].length>0)return;
                    if(treeNode.hasOwnProperty("indexCode"))return;//?????????
                    $.ajax({
                        url: "<@core.rootPath/>/video/fetch/region/cameras",
                        data: {regionName: treeNode.name},
                        success: function (r) {
                            if (r instanceof Array && r.length > 0) {
                                videoTreeObj.addNodes(treeNode, r);
                            } else {
                                console.warn('??????????????????????????????');
                            }
                        }
                    });
                }
                for(var i =0;i<data.length;i++){
                    data[i].open="close";
                }
                $("#regionTree").empty();
                var treeSetting = {
                    view: {
                        dblClickExpand: false,
                        showLine: true,
                        selectedMulti: false,
                        showIcon:true
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: "id",
                            pIdKey: "pId",
                            rootPId: ""
                        }
                    },
                    callback: {
                        beforeClick: function (treeId, treeNode){
                            //????????????????????????
                            debugger;
                            regions="";
                            if(treeNode.hasOwnProperty("indexCode")){
                                var cameraId=treeNode.indexCode;
                                refreshView(cameraId,1);
                                return;
                            }
                            getCamerasByRegionName(treeNode);
                            var allNodes =getAllRegionNodes(treeNode);

                            for(var i in allNodes){
                                regions = regions+"|"+allNodes[i].name;
                            }
                            refreshView(null,1);
                        }
                    }
                };
                videoTreeObj = $.fn.zTree.init($("#regionTree"), treeSetting, data);
            }

            function getAllRegionNodes(node) {
                var result =[];
                if(node.hasOwnProperty("indexCode")) return null;//???????????????
                if(node["children"]&&node["children"].length>0){
                    for(var i=0;i<node["children"].length;i++){
                        var item = node["children"][i];
                        //???????????????????????????
                        if(item.hasOwnProperty("indexCode")){
                            //???????????????????????????????????????????????????
                            result.push(node);
                            return result;
                        }
                        var nodes= getAllRegionNodes(item);
                        //null??????????????????????????????
                        if(nodes==null){
                            result = result.concat(item);
                        }else {
                            result= result.concat(nodes);
                        }

                    }
                }else {
                    result.push(node);
                }
                return result;
            }
        }

        function refreshView(cameraId,page) {
            $.ajax({
                type:"POST",
                url:"<@core.rootPath/>/video/findAutoshotImgs",
                data:{
                    start:start,
                    end:end,
                    regionNames:regions,
                    cameraId:cameraId,
                    size:size,
                    page:page-1,
                },
                success:function (result) {
                    if(result["data"]&&result["data"]["content"]){
                        renderView(result["data"]);
                    }
                }
            });
        }

        function renderView(data) {
            //?????????
            $("#camera_view_content").height(camera_view_content_height);

            var content = data["content"];
            var template =$("#imgTemplate").html();
            layui.use(["laypage","laytpl"],function (laypage,laytpl) {
                laytpl(template).render({"content":content},function (html) {
                    $("#camera_view_content").empty().html(html);
                    $("#camera_view_content .imgItem").height(imgHeight);
                });
                laypage.render({
                    elem: "camera_view_page"
                    ,count: data["totalElements"]
                    ,limit:size
                    ,curr:data["number"]+1,
                    prev:"<<",//???????????????
                    next:">>",//???????????????
                    theme:"#0099ff",//????????????
                    layout: ['count', 'prev','page', 'next',  'skip'],//????????????????????????
                    jump:function(obj, first){
                        //?????????????????????????????????????????????????????????
                        if(first)return;
                        refreshView(null,obj.curr);
                    }
                });
            });

        }
        /*
        ???????????????
         */
        function createTimeSlider(year){
            if(window.dateSilderObj){
                window.dateSilderObj=null;
                $("#dateSlider").empty();
            }
            var Months = ["1???", "2???", "3???", "4???", "5???", "6???", "7???", "8???", "9???", "10???", "11???","12???"];

            var	date = $("<div id='date' />").appendTo($("#dateSlider"));//??????????????????
            var dateSilderObj=date.dateRangeSlider({
                arrows:true,//????????????????????????
                bounds: {min: new Date(year, 7, 1), max: new Date(year+1, 6, 31, 12, 59, 59)},//?????? ????????????
                defaultValues: {min: new Date(year, 1, 1), max: new Date(year, 12, 23)},//??????????????????,
                scales:[{
                    first: function(value){return value; },
                    end: function(value) {return value; },
                    next: function(val){
                        var next = new Date(val);
                        return new Date(next.setMonth(next.getMonth() + 1));
                    },
                    label: function(val){
                        return Months[val.getMonth()];
                    },
                    format: function(tickContainer, tickStart, tickEnd){
                        tickContainer.addClass("myCustomClass");
                    }
                }]


            });//????????????

            //?????????????????????????????????
            dateSilderObj.dateRangeSlider("bounds", new Date(year-1, 12, 1), new Date(year, 11, 31));

            //??????????????????????????????
            dateSilderObj.dateRangeSlider("values", new Date(year, 0,1), new Date(year, 12, 31));

            //????????????????????????????????????
            start =year+"-1-1";
            end = year+"-12-31";
            //????????????????????????
            dateSilderObj.bind("valuesChanged", function(e, data){
                var val=data.values;
                var stime=val.min.getFullYear()+"-"+(val.min.getMonth()+1)+"-"+val.min.getDate();
                var etime=val.max.getFullYear()+"-"+(val.max.getMonth()+1)+"-"+val.max.getDate();
                console.log("???????????????"+stime+" ??? "+etime);
                start = stime;
                end = etime;
                debugger;
                refreshView("",1);
            });
            window.dateSilderObj = dateSilderObj;
        }

        function initSelect() {
            //?????????select?????????
            var year = new Date().getFullYear();
            for(var i =2017;i<=year;i++){
                var option= "<option value='"+i+"'><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+i+"</span></option>";
                $("#year_select").append($(option));
            }
            $("#year_select").val(year);
            $("#year_select").on("change",function () {
                var year = $("#year_select").val();
                year =parseInt(year);
                createTimeSlider(year);
                refreshView(null,1);
            });
        }
        
        window.onload = function () {
            initTree();
            createTimeSlider(new Date().getFullYear());
            initSelect();
            //????????????
            debugger;
            refreshView(null,1);
            //???????????? ????????????
            $(document).on('click','.imgItem',function(evt){
                var src = $(evt.target).attr("src");
                var $imgshow =$("<img/>").attr("src",src);
                var height = widow_height-50;
                //????????????
                layui.use('layer', function(){
                    var layer = layui.layer;
                    layer.open({
                        type: 1,
                        title: false,
                        closeBtn: 1,
                        area: ['900px',height+'px'],
                        skin: 'layui-layer-nobg', //???????????????
                        shadeClose: true,
                        content: "<div class='imgShowContainer'><img src='"+src+"'/></div>"
                    });
                });
            });
            $("#camera_view_left").css("height",widow_height-20);
        }

    </script>
</head>
<body>
    <div id="camera_view_all">
        <div id="camera_view_left">
            <div id="tree_container">
                <div id="regionTree" class="ztree">
                </div>
            </div>
        </div>
        <div id="camera_view_main">
            <div id="camera_view_content">
                <div class="layui-container" style="width: 100%">
                    <div class="layui-row">

                    </div>
                </div>
            </div>
            <div style="height: 50px;margin-right: 10px">
                <div id="camera_view_page" style="float: right">

                </div>
            </div>
            <div id="camera_view_time">
                <div class="dateSlider" id="dateSlider" style="width:80%; height:40px;float: left;margin-top: 30px">&nbsp;</div>
                <div style="float: right;margin-right: 20px;width: 15%;margin-top: 30px">
                    <select id="year_select" style="width: 50%;height: 30px">
                    </select>
                </div>
            </div>
        </div>
        <div style="clear: both"></div>
    </div>
</body>
</html>
