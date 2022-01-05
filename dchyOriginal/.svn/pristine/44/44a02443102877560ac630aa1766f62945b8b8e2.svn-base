/**
 * 一键查看
 * @author hanguanghui
 * @Description 一键查看功能
 * @version V1.0, 2017/4/25
 * @project onemap-parent
 */
/*!
* 配置
*{
 "dataSource": "masyzt",
 "display": [
 {
 "children": [
 {
 "name": "项目状态",
 "sId": "015a273e3d524028daac5a0695df008a,015a273da6f04028daac5a0695df0071,015a273ed3424028daac5a0695df00a3",
 "type": "group"
 },
 {
 "rId": 0,
 "showName": "全市项目用地开竣工情况汇总表",
 "sourceLayer": "MASYZT.YDDK_3405",
 "type": "excel",
 "where": "1=1"
 },
 {
 "rId": 1,
 "showName": "全市项目用地开竣工情况周报表",
 "sourceLayer": "MASYZT.YDDK_3405",
 "type": "excel",
 "where": "1=1"
 }
 ],
 "showName": "全市项目用地开竣工情况"
 },
 {
 "children": [
 {
 "name": "涉嫌闲置",
 "sId": "015a27383eed4028daac5a0695df0008",
 "type": "group"
 },
 {
 "rId": 3,
 "showName": "市本级涉嫌土地闲置开发建设项目一览表",
 "sourceLayer": "MASYZT.YDDK_3405",
 "type": "excel",
 "where": "SXXZ_FJ='是'"
 }
 ],
 "showName": "市本级涉嫌土地闲置开发建设项目一览"
 },
 {
 "children": [
 {
 "name": "重点监管",
 "sId": "015a273a47634028daac5a0695df0036",
 "type": "group"
 },
 {
 "rId": 4,
 "showName": "重点监管建设项目一览表",
 "sourceLayer": "MASYZT.YDDK_3405",
 "type": "excel",
 "where": "ZDJSXM_FJ='是'"
 }
 ],
 "showName": "重点监管建设项目一览"
 },
 {
 "children": [
 {
 "name": "用而未尽",
 "sId": "014edd73a2c44028e4824edd192203b8",
 "type": "group"
 },
 {
 "rId": 5,
 "showName": "市本级用而未尽开发建设项目一览表",
 "sourceLayer": "MASYZT.YDDK_3405",
 "type": "excel",
 "where": "YEWJ_FJ='是'"
 }
 ],
 "showName": "市本级用而未尽开发建设项目一览"
 },
 {
 "children": [
 {
 "name": "慈湖已批在建未供",
 "sId": "014edd756d614028e4824edd192203fb",
 "type": "group"
 },
 {
 "rId": 2,
 "showName": "慈湖高新区已批在建(未供)项目",
 "sourceLayer": "MASYZT.YDDK_CH_YPZJWG",
 "type": "excel",
 "where": "YPZJWG_FJ='是'"
 }
 ],
 "showName": "慈湖高新区已批在建(未供)项目"
 },
 {
 "children": [
 {
 "name": "标准化厂房建设情况",
 "sId": "014fba4950078bb021ea4fba094e001d,014fba4a18838bb021ea4fba094e0036,014fba4ace4a8bb021ea4fba094e004f,014fba4ba44e8bb021ea4fba094e0068",
 "type": "group"
 },
 {
 "rId": 7,
 "showName": "标准化厂房建设情况汇总表",
 "sourceLayer": "MASYZT.BZHCFJS",
 "type": "excel",
 "where": "1=1"
 }
 ],
 "showName": "标准化厂房建设情况"
 },
 {
 "rId": 6,
 "showName": "打包下载",
 "sourceLayer": "MASYZT.YDDK_3405",
 "where": "1=1,1=1,YPZJWG_FJ='是',SXXZ_FJ='是',ZDJSXM_FJ='是',YEWJ_FJ='是'"
 }
 ]
 }
* */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "esri/lang",
    "map/utils/MapUtils",
    "map/core/BaseWidget",
    "map/core/JsonConverters",
    "handlebars",
    "slimScroll",
    "ztree"], function (declare, lang,arrayUtil, esriLang,MapUtils,  BaseWidget, JsonConverters, Handlebars,slimScroll) {

    var _map,_reportConfig;

    var EXPORT_FILE_URL = "/omp/transitService/report/output";
    var EXPORT_VIEW_URL ="/omp/transitService/report/view";

    var me = declare([BaseWidget], {

        constructor: function () {

        },

        onCreate: function () {
            _map = this.getMap().map();
            _reportConfig=this.getConfig();
            _init();
        },

        onPause: function () {
        },

        onOpen: function () {
            render();
        },

        onDestroy:function(){

        }
    });

    var display=[];
    var dataSource;

    var thisTpl = tpl;
    var expandLevel =2;

    var configs=[];
    /***
     * init
     * @private
     */
    function _init() {
        if(_reportConfig.hasOwnProperty("display"))display = _reportConfig.display;
        if(_reportConfig.hasOwnProperty("dataSource"))dataSource=_reportConfig.dataSource;
        arrayUtil.forEach(display,function(item){
            item.id = Math.uuid();
            item.excels=[];
            if(item.hasOwnProperty("children")){
                arrayUtil.forEach(item.children,function(child){
                    if(child.type=="excel"||child.type=="Excel"){
                        item.excels.push(child);
                    }
                });
            }else{
                item.excels.push(item);
            }
        });
        var tpl = $("#displayLstTpl").html();
        var templ = Handlebars.compile(tpl);
        $("#display-list").append(templ({displays:display}));

        var treeHeight = $(window).height() - 130;
        $(".panel-container").slimScroll({
            height: treeHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        render();
        addListeners();
    }

    function addListeners(){
        $(".export-excel").on("click",function(){
            var rId = $(this).data("rid");
            // var dataSource = $(this).data("datasource");
            var layerName = $(this).data("layername");
            var where = $(this).data("where");
            var showName = $(this).data("showname");
            var data={
                rId:rId,
                layerName:layerName,
                dataSource:dataSource,
                queryCondition:where,
                fileName:showName
            };
            if(rId=="6"){
                //打包下载
                execute(EXPORT_FILE_URL,data);
            }else{
                //查看
                execute(EXPORT_VIEW_URL,data);
            }

        });
    }
    function execute (a, b) {
        var form = $("<form method='post' style='display:none;' target='_blank'></form>"),
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
    /**
     * 渲染tree
     */
    function render(){
        var d = display;
        arrayUtil.forEach(d,function(item){
            if(item.hasOwnProperty("children")){
                arrayUtil.forEach(item.children,function(child){
                    if(child.type=="group"){
                        var serviceIds = child.sId.split(",");
                        var _url = esriLang.substitute({val: true}, "/omp/map/" + thisTpl + "/services?simple=${val}");
                        $.ajax({
                            url: _url,
                            dataType: "json",
                            async:false,
                            success: function (result) {
                                if (esriLang.isDefined(result.success)) {
                                    layer.msg(result.msg, {icon: 0, time: 8000});
                                    return;
                                }
                                var data = _groupService(result,serviceIds);
                                if(data.length>0){
                                    renderTree(item.id,data);
                                }
                            }
                        });
                    }
                });
            }
        })
    }
    /**
     * 渲染tree
     */
    function renderTree(id,data){
        var setting = {
            check: {
                enable: true,
                autoCheckTrigger: true,
                chkPosition: 'after'
            },
            data: {
                key: {
                    checked: "visible",
                    name: "alias",
                    url: "nourl"
                }
            },
            callback: {
                onCheck: onNodeCheck,
            }
        };
        $("#dirTree"+id).empty();
        $.fn.zTree.init($("#dirTree"+id), setting, data);

        var treeObj = $.fn.zTree.getZTreeObj("dirTree"+id);
        _dispose(treeObj);
        var count = calTreeHeight(treeObj.getNodes());
        $(".tree-"+id).css("height",count*28);
        $(".tree-"+id).slimScroll({
            height: count*28,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

    }


    /**
    * @param obj
    * @private
    */
    function _dispose(obj){
        var nodes = obj.getNodes();
        if (nodes.length > 0) {
            if (expandLevel == 1) {
                for (var i in nodes) {
                    obj.expandNode(nodes[i], true, false, false);
                }
            } else {
                obj.expandAll(true);
            }
        }
    }

    function calTreeHeight(nodes){
        var count = 1;
        arrayUtil.forEach(nodes,function(node){
            if(node.hasOwnProperty("children")){
                count+=calTreeHeight(node.children);
            }else{
                count++;
            }
        });
        return count;
    }
    /**
     * treeNode点击事件
     */
    function onNodeCheck(event, treeId, treeNode){
        if (!treeNode.isParent) {
            if (treeNode.getCheckStatus().checked) {
                if (treeNode.url != "") {
                    addLayer(treeNode);
                }
            } else {
                if (_map.getLayer(treeNode.id) != undefined && map.map().getLayer(treeNode.id).loaded) {
                    map.removeLayerById(treeNode.id);
                }
            }
        }
    }

    /**
     * 添加图层
     * @param layer
     */
    function addLayer(node) {
        var layer = node;
        if (esriLang.isDefined(layer)) {
            layer.visible = true;
            map.addLayer(layer, layer.index); //按照配置的顺序加载
        }
    }

    /**
     * 根据配置的id区分信息
     * @param services
     * @param ids
     * @private
     */
    function _groupService(services,ids){
        var data=[];
        arrayUtil.forEach(services,function(item){
            var group = {
                "id": item.id,
                "alias": item.alias,
                "name": item.name,
                "visible": item.visible
            };
            if (item.hasOwnProperty("children")) {
                var children = [];
                arrayUtil.forEach(item.children, function (child) {
                    if (child.hasOwnProperty("children")) {
                        var childGroup = {
                            "id": child.id,
                            "alias": child.alias,
                            "name": child.name,
                            "visible": child.visible
                        };
                        childGroup.children = _groupService(child.children, ids);
                        if (childGroup.children.length > 0)children.push(childGroup);
                    }else{
                        arrayUtil.forEach(ids,function(id){
                            if(child.id == id){
                                child.visible=checkLyr(id);
                                children.push(child);
                                configs.push(child);
                            }
                        });
                    }
                });
                group.children = children;
                if (group.children.length > 0)data.push(group);
            }else{
                arrayUtil.forEach(ids,function(id){
                    if(group.id == id){
                        item.visible=checkLyr(id);
                        data.push(item);
                        configs.push(item);
                    }
                });
            }
        });
        return data;
    }

    function checkLyr(id){
        var lyrs = _map.layerIds;
        if(lyrs.indexOf(id)>0){
            return true;
        }else
            return false;
    }
    return me;
});
