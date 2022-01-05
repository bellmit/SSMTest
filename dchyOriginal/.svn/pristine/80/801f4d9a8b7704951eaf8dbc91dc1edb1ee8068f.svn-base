/**
 * 地图标注 功能不局限于添加地图标记
 * 并可以打印输出添加标记后的地图 以及添加动态标绘 并结合分析 进行扩展
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/15 20:02
 * Version: v1.0 (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "mustache",
    "map/core/BaseWidget",
    "layer",
    "slimScroll",
    'esri/toolbars/draw',
    "esri/layers/GraphicsLayer",
    "esri/Color",
    "map/manager/LayoutManager",
    "esri/graphic",
    "esri/tasks/GeometryService",
    "esri/tasks/BufferParameters",
    "esri/SpatialReference",
    "map/core/JsonConverters",
    "esri/geometry/Point",
    "esri/symbols/TextSymbol",
    "map/component/MapInfoWindow",
    "map/core/EsriSymbolsCreator",
    "static/js/cfg/core/SerializeForm",
    "static/js/UUIDUtils",
    "static/thirdparty/icheck/icheck",
    "ko",
    "text!map/template/mark/mark-info-form.html",
    "text!map/template/mark/mark-add.html"], function (declare, lang, Mustache, BaseWidget, layer,
                                                       slimScroll, Draw, GraphicsLayer, Color, LayoutManager,
                                                       Graphic, GeometryService, BufferParameters, SpatialReference, JsonConverters, Point, TextSymbol, MapInfoWindow,
                                                       EsriSymbolsCreator, SerializeForm, uuid, icheck, ko, MarkInformation, MarkAddTpl) {
    var __map,_markConfig;
    var graphicsLayer = new GraphicsLayer({id: 'markPoints'});

    var viewModel; //ko view-model

    var me = declare([BaseWidget], {
        /***
         *
         */
        onCreate: function () {
            __map = this.getMap().map();
            _markConfig=this.getConfig();
            _init();
        },
        /***
         *
         */
        onOpen: function () {
            if (graphicsLayer != undefined)
                __map.addLayer(graphicsLayer);
        },
        /***
         *
         */
        onPause: function () {
            __map.removeLayer(graphicsLayer);
        },
        /**
         *
         */
        onDestroy: function () {
            graphicsLayer.clear();
        }

    });

    var drawTool;
    var addProHandler, timeOutHandler, drawHandler;
    var sms = EsriSymbolsCreator.createSimpleMarkerSymbol(EsriSymbolsCreator.markerStyle.STYLE_SQUARE, 8,
        EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_SOLID, new Color([255, 0, 0]), 1), Color([255, 0, 0]));

    var mapInfoWindow = MapInfoWindow.getInstance();

    var ANONYMOUS_USER="匿名用户";
    var  $marksContainer,$descSpan;
    //分开存放公有和私有标记
    var _publicMarks=[],_privateMarks=[];

    /***
     * init
     * @private
     */
    function _init() {
        if(_markConfig.detail===true){
            $("#MarkPanelBody").css("width","200px");
            $("#marksContent").css("width","200px");
        }
        layer.config();
        $marksContainer = $("#marksContainer");
        $descSpan=$("#mDescSpan");
        //init vm
        viewModel = new ViewModel();
        ko.applyBindings(viewModel,document.getElementById('marksContent'));

        fetchMarks();
        //添加标记事件
        $('#addMarkBtn').on('click', function () {
            drawTool = drawTool ? drawTool : new Draw(__map);
            drawHandler = drawTool.on("draw-end", lang.hitch(map, createNewMark));
            esri.bundle.toolbars.draw.addPoint = "单击添加标记位置";
            drawTool.activate(Draw.POINT);
            var mapClickHandler = __map.on('click', function () {
                window.clearTimeout(timeOutHandler);
                mapClickHandler.remove();
            });
            timeOutHandler = window.setTimeout(function () {
                drawTool.deactivate();
            }, 5000);
        });
        //添加查询事件
        $('#searchLocationMark').on('click', function () {
            var markMc = $("#queryLocationMark").val();
            $.getJSON(root.concat("/map/mark/" + loginUser.id + "/queryMarks"),{publicity: '0',markMc: markMc},lang.hitch(this,parseMarks));
        });
    }

    /***
     * fetch marks
     */
    function fetchMarks(){
        $.getJSON(root.concat("/map/mark/" + loginUser.id + "/marks"),{publicity: '0'},lang.hitch(this,parseMarks));
    }

    /***
     * 渲染已有的地图标记
     */
    function parseMarks(r) {
        _publicMarks = [];
        _privateMarks = [];
        var _marks = [];
        if (r.hasOwnProperty("success")) {
            layer.msg(r.msg, {icon: 0});
            return;
        }
        if (lang.isArray(r)) {
            _marks = r;
            viewModel.marks(_marks);
            add2Map(_marks);
        }
    }

    /***
     * vm 对象
     * @param marks
     * @constructor
     */
    function ViewModel(marks) {
        var self = this;
        self.marks = ko.observableArray(marks);
        self.removeMark = lang.hitch(this,removeMark);
    }

    /***
     * 移除mark
     * @param vm
     * @param mark
     */
    function removeMark(mark){
        if(mark != undefined){
            var id = mark.id;
            layer.confirm('确定要删除该标记吗？', {
                btn: ['是', '否']
            }, function () {
                $.ajax({
                    url: root + "/map/mark/" + id + "/delete",
                    method: 'post',
                    async: false,
                    success: function () {
                        $('.close').trigger('click');
                        layer.msg("标记删除成功！", {icon: 1, time: 700});
                        fetchMarks();
                    }
                });
            })
        }
    }

    /***
     * 渲染标记位置至地图上
     * @param data
     */
    function add2Map(data){
        if(lang.isArray(data)){
            //添加图形
            graphicsLayer.clear();
            $.each(data, function (i, item) {
                var txtSymbol = EsriSymbolsCreator.createTextSymbol(item.title, new Color([0, 0, 0, 0.5]));
                txtSymbol.setAlign(TextSymbol.ALIGN_START);
                txtSymbol.setVerticalAlignment("top");
                txtSymbol.setHorizontalAlignment("center");
                txtSymbol.setOffset(24, 0);
                var x = item.x;
                var y = item.y;
                var pnt = new Point(x, y, __map.spatialReference);
                var txtGra = new Graphic(pnt, txtSymbol);
                var icon = sms;
                if (parseInt(item.symbol, 10) > 0) {
                    var src = '/omp/static/img/marks/mark' + item.symbol + '.png';
                    icon = EsriSymbolsCreator.createPicMarkerSymbol(src, 24, 24);
                }
                var g = new Graphic(pnt, icon, item, null);
                graphicsLayer.add(txtGra);
                graphicsLayer.add(g);
            });
            addListeners();
        }

    }

    /***
     * 添加监听
     */
    function addListeners(){
        var scrollHeight = $(window).height() - 240;
        $("#marks").slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
        //图形定位
        $('.private-information').on('click', function () {
            var id = $(this).data('id');
            editMark(id);
        });
        graphicsLayer.on('click', lang.hitch(this, onGraClickHandler));
    }

    /***
     *
     * @param evt
     */
    function onGraClickHandler(evt) {
        var gra = evt.graphic;
        var id = gra.attributes.id;
        editMark(id);
    }

    /**
     * 编辑标记信息
     * @param id
     */
    function editMark(id) {
        var mark = {};
        $.ajax({
            url: root + "/map/mark/" + id,
            method: 'post',
            async: false,
            success: function (rp) {
                mark = rp;
                var pos = {};
                var temp = {};
                temp.wkid = __map.spatialReference.wkid;
                pos.spatialReference = temp;
                pos.x = rp.x;
                pos.y = rp.y;
                if(_markConfig.detail!==true){
                mapInfoWindow.showDetail(MarkInformation, pos, rp.title).then(function (pos) {
                    var pnt = new Point(pos.x, pos.y, __map.spatialReference);
                    __map.setScale(20000);
                    __map.centerAt(pnt);
                    $('#editMark #markTitle').val(rp.title);
                    $('#editMark #symbol').val(rp.symbol);
                    $('#editMark #description').val(rp.description);
                    $('#editMark #isPublic').val(rp.publicity);
                    if (rp.publicity == "0") {
                        $('#editMark #pubCheck').attr("checked", "true");
                    }
                    if (loginUser.name===ANONYMOUS_USER) {
                        $('#editMark #pubHide').hide();
                    }
                    var html = '<img class="mark-radio" src="../static/img/marks/mark' + rp.symbol + '.png">';
                    $('#editMark #modifyMarkImg').html(html);
                });}else {
                    __map.setScale(15000);
                    __map.centerAt(pos);
                }
            }
        });
        $(".icheck input").on('ifChecked', function () {
            $('#editMark #isPublic').val("0");
        });
        $(".icheck input").on('ifUnchecked', function () {
            $('#editMark #isPublic').val("1");
        });
        $('#editMark #modifyMark').on('click', function () {
            var val = $('#editMark #symbol').val();
            $("#editMark input[value=" + val + "]").attr('checked', 'checked');
            $('#editMark #selectMark').hide();
            $('#editMark #markList').show();
        });
        $('#editMark #hideMarkList').on('click', function () {
            var temp = $('#editMark #markList input[name="markIcons"]:checked').val();
            $('#editMark #markList').hide();
            if (temp != undefined) {
                var html = '<img class="mark-radio" src="../static/img/marks/mark' + temp + '.png">';
                $('#editMark #modifyMarkImg').html(html);
                $('#editMark #symbol').val(temp);
            }
            $('#editMark #selectMark').show();
        });
        $('#editMark #modifyMarkImg').on('click', function () {
            $('#editMark #modifyMark').trigger('click');
        });
        $('#editMark #saveMark').on('click', function () {
            if ($('#editMark #markTitle').val() == undefined || $('#editMark #markTitle').val() == "") {
                $('#editMark #markTitle').val("我的标记");
            }
            var markRadio = $('#editMark #markList input[name="markIcons"]:checked').val();
            if (markRadio != undefined) {
                $('#editMark #symbol').val(markRadio);
            }
            mark.title = $('#editMark #markTitle').val();
            mark.symbol = $('#editMark #symbol').val();
            mark.description = $('#editMark #description').val();
            mark.publicity = $('#editMark #isPublic').val();
            var data = {data: JSON.stringify(mark)};
            saveMarkDetail(data);
        });
    }

    /**
     * 创建新标记
     * @param evt
     */
    function createNewMark(evt) {
        layer.closeAll();
        drawTool.deactivate();
        drawHandler.remove();
        addProHandler = layer.open({
            type: 1,
            title: ['<i class="fa fa-plus"></i>&nbsp;新增标记', 'background-color:#428bca;color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
            area: '620px',
            shade: 0,
            content: MarkAddTpl,
            btn: []
        });
        if (loginUser.name===ANONYMOUS_USER) {
            $('#newMark #pubHide').hide();
        }
        var isPublic = $("#newMark #isPublic");
        isPublic.val("0");
        $('#newMark #symbol').val("16");
        $(".icheck input").on('ifChecked', function () {
            isPublic.val("0");
        });
        $(".icheck input").on('ifUnchecked', function () {
            isPublic.val("1");
        });
        $('#newMark #modifyMark').on('click', function () {
            var val = $('#newMark #symbol').val();
            $("#newMark input[value='" + val + "']").attr('checked', 'checked');
            $('#newMark #selectMark').hide();
            $('#newMark #markList').show();
        });
        $('#newMark #hideMarkList').on('click', function () {
            var temp = $('#newMark #markList input[name="markIcons"]:checked').val();
            if (temp != undefined) {
                var html = '<img class="mark-radio" src="../static/img/marks/mark' + temp + '.png">';
                $('#newMark #modifyMarkImg').html(html);
                $('#newMark #symbol').val(temp);
            }
            $('#newMark #markList').hide();
            $('#newMark #selectMark').show();
        });
        $('#newMark #modifyMarkImg').on('click', function () {
            $('#newMark #modifyMark').trigger('click');
        });
        $('#newMark #saveMark').on('click', function () {
            if ($('#newMark #markTitle').val() == undefined || $('#newMark #markTitle').val() == "") {
                $('#newMark #markTitle').val("我的标记");
            }
            var markRadio = $('#newMark #markList input[name="markIcons"]:checked').val();
            if (markRadio != undefined) {
                $('#newMark #symbol').val(markRadio);
            }
            else {
                $('#newMark #symbol').val('16');
            }
            var mark = SerializeForm.serializeObject($('#newMark #MarkInfoForm'));
            var geos = evt.geometry;
            mark.enable = 1;
            mark.name = Math.uuid(32);
            mark.owner = loginUser.id;
            mark.x = geos.x;
            mark.y = geos.y;
            var data = {data: JSON.stringify(mark)};
            saveMarkDetail(data)
        });

    }

    /**
     * 保存标记信息
     * @param data
     */
    function saveMarkDetail(data) {
        $.ajax({
            url: root + "/map/mark/" + loginUser.id + "/save",
            method: 'post',
            async: false,
            data: data,
            success: function (rp) {
                $('.close').trigger('click');
                layer.closeAll();
                layer.msg("保存成功！", {icon: 1, time: 700});
                fetchMarks();
            }
        });
    }
    return me;
});
