/*!
 * 扩展查询 可与业务结合的查询功能
 * -----------------------------------------------------------
 *---------配置示例:
 *---------{
 *--------- "subjects": [
 *---------   {
 *---------     "name": "dk",
 *---------     "alias": "工业地块",
 *---------     "serviceUrl": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/GYYD/MapServer/0",
 *---------     "linkUrl": "http://www.baidu.com/s?wd=${YDDW}",
 *---------     "columns": [
 *---------       {
 *---------         "field": "YDDW",
 *---------         "title": "用地单位",
 *---------         "width": 80,
 *---------         "type": "String"
 *---------       },
 *---------       {
 *---------         "field": "TDSYQR",
 *---------         "title": "土地所有",
 *---------         "width": 100,
 *---------         "type": "String"
 *---------       },
 *---------       {
 *---------         "field": "JZMJ",
 *---------         "title": "面积",
 *---------         "type": "Number",
 *---------         "width": 80
 *---------       }
 *---------     ]
 *---------   }
 *--------- ],
 *--------- "keywords": [
 *---------   {
 *---------     "title": "用地类型",
 *---------     "field": "YDLX",
 *---------     "values": [
 *---------       "合法审批",
 *---------       "未批先用"
 *---------     ]
 *---------   }
 *---------  ]
 *--------- }
 * ----------------------------------------------------------------
 * Author: yingxiufeng
 * Date:   2017/2/17 
 * Version:v1.0 (c) Copyright gtmap Corp.2017
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/_base/Color",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/core/EsriSymbolsCreator",
    "map/manager/ConfigManager",
    "map/utils/MapUtils",
    "esri/geometry/Polygon",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/lang",
    "handlebars",
    "layer",
    "static/thirdparty/easyui/easyloader"], function (declare, lang, arrayUtil,Color, BaseWidget, Environment, EsriSymbolsCreator,ConfigManager,
                                                      MapUtils, Polygon,GraphicsLayer, Graphic,esriLang, Handlebars,layer) {

    var _mainmap, _widgetData;
    var me = declare([BaseWidget], {

        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            _mainmap = this.getMap();
            _widgetData = this.getConfig();
            MapUtils.setMap(this.getMap().map());
            _init();
        },
        /**
         *
         */
        onOpen: function () {


        },
        /***
         * fires before destroy widget
         */
        onDestroy: function () {

        }

    });

    var querySubs=[],targetSub;

    var extendQueryGraphicLayer;

    //存放用户输入条件 和关键词 过滤条件
    var inputStatements=[],kwStatements=[];
    var $queryDg,$lyrSelect;

    var linkHander;
    /***
     * 初始化方法
     * @private
     */
    function _init() {
        layer.config();
        querySubs=_widgetData.subjects;
        targetSub=querySubs[0];
        var pageSize;
        //初始化关键词条件区域
        var $ekeysWrapper=$(".e-search-keys-wrapper");
        if(esriLang.isDefined(_widgetData.keywords)){
            $ekeysWrapper.append(renderTpl($("#keywordsTpl").html(),{keywords:_widgetData.keywords}));
            pageSize =Math.floor(($(document).height()-193-57-_widgetData.keywords.length*30)/25);

        }else{
            pageSize =Math.floor(($(document).height()-270)/25);
        }

        //初始化datagrid
        $queryDg=$('#extendQueryDG');
        //初始化datagrid
        using('datagrid', function () {
            $queryDg= $('#extendQueryDG').datagrid({
                emptyMsg: '无数据',
                singleSelect: true,
                rownumbers: true,
                showHeader: true,
                striped: true,
                scrollbarSize: 10,
                rownumberWidth: 35,
                fitColumns: true,
                pagination: true,
                pageSize: pageSize,
                columns: [targetSub.columns],
                url: getDataUrl(targetSub),
                onBeforeLoad: customsizeDg,
                onSelect:onRowSelect
            });
        });
        extendQueryGraphicLayer =new GraphicsLayer({id:"extendQueryGraphicLayer"});

        //初始化
        var tpl = $("#searchLayerTpl").html();
        var templ = Handlebars.compile(tpl);
        var rHtml =  templ({layers:querySubs});
        $("#layerSelectPanel").append(rHtml);
        $lyrSelect = $("#layersSelect");
        addListener();
    }

    /***
     * 事件监听
     */
    function addListener(){
        //监听查询按钮
        $("#eSearchTxt").on('change', function () {
            var $this = $(this);
            if ($this.val() === "")inputStatements = [];
        });
        $("#eSearchBtn").on("click", function () {
            //清空现有的语句
            inputStatements = [];
            var value = $("#eSearchTxt").val();
            if (esriLang.isDefined(value) && value != "") {
                arrayUtil.forEach(targetSub.columns, function (item) {
                    var statement = undefined;
                    if (item.type === "Number" || item.type === "number") {
                        if (!isNaN(value)) {
                            statement = item.field + "=" + value;
                        }
                    } else
                        statement = item.field + " LIKE  '%" + value + "%'";
                    if (esriLang.isDefined(statement)) {
                        inputStatements.push(statement);
                    }
                });
            }else{
                inputStatements.push("1=1");
            }
            executeQuery();
        });
        //监听关键词操作事件
        $(".kw-av-collapse>li>a").on('click',function(){
            var $this=$(this);
            var $li=$this.parent();
            var isSel=$li.hasClass('kw-sel-attr');
            var field = $li.parent().parent().parent().data("field");
            if(!isSel){
                //选中当前关键词并加入条件到statements
                $li.addClass("kw-sel-attr");
                $this.append("<span class=\"iconfont\">&#xe63b;</span>");
                kwStatements.push(field + "='" + $this.data("val") + "'");
                $this.attr('title','移除筛选条件');
            }else{
                //取消当前选中的关键词 并移出条件
                $li.removeClass('kw-sel-attr');
                var statement=field + "='" + $this.data("val") + "'";
                kwStatements=arrayUtil.filter(kwStatements,function(item){
                    return item!=statement;
                });
                $this.children().remove();
                $this.attr('title','添加筛选条件');
            }
            executeQuery();
        });
        //监听选择图层
        $lyrSelect.on('change', function(){

            var name =  $(this).val();
            targetSub = arrayUtil.filter(querySubs,function(item){
                return item.name==name;
            });
            if(targetSub.length>0){
                targetSub =targetSub[0];
                $queryDg.datagrid({
                    columns: [targetSub.columns],
                    url: getDataUrl(targetSub)
                });
            }

        });
    }

    /***
     * 执行查询
     */
    function executeQuery(){
        extendQueryGraphicLayer.clear();

        var whereClause = undefined;
        if (inputStatements.length > 0) {
            whereClause = inputStatements.join(" OR ");
        }
        if (kwStatements.length > 0) {
            if (esriLang.isDefined(whereClause)) {
                whereClause += " AND (";
                whereClause += kwStatements.join(" OR ")+")";
            } else {
                whereClause = kwStatements.join(" OR ");
            }
        }
        console.info("查询语句："+whereClause);
        if(esriLang.isDefined(whereClause)){
            $queryDg.datagrid({queryParams: {where: inputStatements.join(" OR ")}, pageNumber: 1,emptyMsg:'无数据'});
            $queryDg.datagrid('reload');
        }
    }

    /***
     * 选中行数据后，定位图形并打开链接
     * @param index
     * @param rowData
     */
    function onRowSelect(rowIndex,rowData){
        extendQueryGraphicLayer.clear();
        var geojson = JSON.parse(rowData.geometry);
        var polyGon = new Polygon(geojson.geometry.coordinates);
        var lineSymbol=EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASHDOT, new Color([255, 0, 0]), 2);
        var fillSymbol =EsriSymbolsCreator.createSimpleFillSymbol(EsriSymbolsCreator.fillStyle.STYLE_SOLID,lineSymbol, new Color([255, 255, 0, 0.25]));

        var gra = new Graphic(polyGon,fillSymbol,geojson.properties);
        extendQueryGraphicLayer.add(gra);

        MapUtils.locateFeatures([gra], 2);

        if (extendQueryGraphicLayer != undefined && extendQueryGraphicLayer.graphics.length > 0)
            _mainmap.addLayer(extendQueryGraphicLayer);

        //打开业务系统链接 layer弹窗
        if(esriLang.isDefined(targetSub.linkUrl)){
            var linkUrl=esriLang.substitute(rowData,targetSub.linkUrl);
            layer.close(linkHander);
            linkHander = layer.open({
                type: 2,
                title:"地块详情",
                area:['800px',"600px"],
                shade:0,
                content: [linkUrl,'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
            });
        }
    }

    /**
     * 拼接获取图层数据的url
     * @param subject
     */
    function getDataUrl(subject){
        var lyrUrl=subject.serviceUrl;
        var url="/omp/map/pageQuery";
        var outFields=[];
        arrayUtil.forEach(subject.columns,function(item){
            outFields.push(item.field);
        });
        return url.concat("?layerUrl="+lyrUrl).concat("&returnFields="+outFields.join(","));
    }

    /**
     * 订制ddatgrid的外观样式
     * 添加序号列名称 等
     */
    function customsizeDg(){
        var $numberHeader=$(".datagrid-header-rownumber");
        if(esriLang.isDefined($numberHeader)){
            $numberHeader.html("<span>序号</span>");
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