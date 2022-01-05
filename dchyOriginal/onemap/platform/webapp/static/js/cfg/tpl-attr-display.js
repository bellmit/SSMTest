/**
 * Created by user on 2016-01-18.
 */
define(["dojo/_base/array",
    "dojo/_base/lang",
    "dojo/json",
    "esri/map",
    "esri/lang",
    "esri/geometry/Extent",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/layer",
    "handlebars",
    "slimScroll",
    "layer",
    "jqueryUI",
    "static/js/cfg/core/SerializeForm",
    "text!./template/attrs-item.html",
    "css!static/thirdparty/agsapi/3.14/dijit/themes/claro/claro.css",
    "css!static/thirdparty/agsapi/3.14/esri/css/esri.css",
    "css!static/thirdparty/layer/skin/layer-omp.css",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "static/thirdparty/bootstrap-switch/bootstrap-switch.min",
    "static/thirdparty/jquery/jquery.ajaxfileupload",
    "css!static/thirdparty/layer/skin/layer-omp.css",
    "css!static/thirdparty/bootstrap-switch/bootstrap-switch.min.css"], function (arrayUtils, lang, JSON, Map, esriLang, Extent,
                                                                                                 ArcGISDynamicMapServiceLayer, ArcGISTiledMapServiceLayer,
                                                                                                 Layer, Handlebars, SlimScroll, layer, jqueryUI, SerializeForm, attrsItem) {

    var tplServices;
    var globalAttrs={};
    var _map;
    /***
     * 初始化界面等
     * @private
     */
    function _init() {
        //渲染页面
        renderPage();
    }

    /**
     * 渲染配置
     */
    function renderPage() {
        //先获取services
        $.getJSON( root + "/config/tpl/" + tpl + "/services",null,function(data){
            if (data.hasOwnProperty("success")) {
                layer.msg('获取模板服务异常:' + data.msg);
                return;
            }
            tplServices=data.services;
        });
        $.ajax({
            url: root + "/config/tpl/" + tpl + "/global",
            method: 'post',
            async: false,
            success: function (rp) {
                if(rp.hasOwnProperty("success")&&rp.success===false){
                    layer.msg('获取模板属性异常:' + rp.msg);
                    return;
                }
                globalAttrs = {};
                for (var key in rp) {
                    if (esriLang.isDefined(key)) {
                        var val = rp[key];
                        if (typeof val != 'object' || key === 'geometryService') {
                            globalAttrs[key] = val;
                        }
                    }
                }
                $("#sectionAttrs>.attrs-info").append(renderTpl(attrsItem,globalAttrs));
                //初始化switch button
                $('input[type=checkBox].switch-box').bootstrapSwitch({
                    size: 'small',
                    onText: '<i class="fa fa-eye"></i>',
                    offText:'<i class="fa fa-eye-slash"></i>',
                    onSwitchChange: function (event, state) {
                        var field=$(event.currentTarget).data("field");
                        if(globalAttrs.hasOwnProperty(field)){
                            globalAttrs[field]=state;
                            saveGlobalConfig();
                        }
                    }
                });
                addListeners();
            }
        });
    }


    /***
     *
     */
    function addListeners(){
        /**
         * 全局配置保存监听
         */
        $(".save-global").on("change", function () {
            saveGlobalConfig();
        });
        /**
         * 上传logo图片
         * 需要把上传后的调用地址回传回来
         */
        $("#file").on('change', function () {
            var config = {
                fileElementId: "file",
                url: root + '/file/logo/upload',
                dataType: 'text',
                success: function (data, textStatus) {
                    var result = $.parseJSON($(data).text());

                    $("#file").on("change", function () {
                        $.ajaxFileUpload(config);
                        $("#file").replaceWith($("#file").clone(true));
                    });

                    $("#bttp").val("/omp/file/logo/"+result.logo);
                    saveGlobalConfig();
                }
            };
            $.ajaxFileUpload(config);
        });
        //清除缓存
        $(".clear-cache-container").on('click','a',function(){
            var $this=$(this);
            var t=$this.data("type");
            var url=undefined;
            switch(t){
                case 'service':
                    url=root+"/map/"+tpl+'/clearServiceCache';
                    break;
                case 'region':
                    url=root+"/map/"+tpl+'/clearRegionCache';
                    break;
            }
            if (url != undefined) {
                layer.msg("执行中...", {time: 3000, shade: 0.35, shadeClose: !1});
                $.ajax({
                    url: url,
                    complete: function (evt) {
                        var r = $.parseJSON(evt.responseText);
                        if (r === true) {
                            layer.msg("缓存已清除!", {time: 1000});
                        }
                    }
                });
            }
        });
        //设置地图加载初始化范围等功能
        $("#extentSetBtn").on('click',function(){
            $(this).attr('disabled','disabled');
            var htmlFragment = '<div id="mapOne" style="width:100%; height:400px; "></div>';
            layer.open({
                id:"mapDialog",
                type:1,
                title:'地图预览',
                content:htmlFragment,
                shade: 0,
                area:'620px',
                btn:['设定'],
                success:function(){
                    $("#mapDialog").find("a[class='layui-layer-btn0']").attr('disabled','disabled');
                    createMap("mapOne");
                },
                cancel:function(){
                    $("#extentSetBtn").removeAttr("disabled");
                },
                yes: function (index) {
                    var extent = _map.extent;
                    var msgHandle=layer.msg('操作执行中...',{time:6000});
                    $.ajax({
                        url: root + "/config/tpl/" + tpl + "/map/extent",
                        data: {extent: JSON.stringify(_map.extent.toJson())}
                    }).done(function (rp) {
                        layer.close(msgHandle);
                        if (rp.success == false) {
                            layer.msg(rp.msg,{icon:2});
                        } else {
                            layer.msg("范围设置成功!", {icon: 1});
                            $("#extentSetBtn").removeAttr("disabled");
                            layer.close(index);
                        }
                    });
                }
            });
        });
    }
    /***
     * create map
     * @param srcNodeRef
     */
    function createMap(srcNodeRef){
        _map = new Map(srcNodeRef, {
            logo: false,
            slider: false,
            autoResize: true
        });
        _map.on('load',function(){
            $("#mapDialog").find("a[class='layui-layer-btn0']").removeAttr("disabled");
            $.ajax({
                url: root + "/config/tpl/" + tpl + "/map/extent",
                data:{extent:''}
            }).done(function (rp) {
                if (rp.success) {
                    console.error(rp.msg);
                } else {
                    _map.setExtent(new Extent(rp));
                    _map.resize(true);
                }
            });
        });
        if(tplServices!=undefined&&tplServices.length>0){
            arrayUtils.forEach(tplServices,function(value){
                try {
                    _addLayer(value,value.index);
                } catch (e) {
                    log("addLayer error:" + e.message);
                }
            });
        }else{
            layer.msg('模板里尚未添加地图服务!',{icon:0},function(){layer.closeAll();});
        }
    }

    /***
     * add layer
     * @param layer
     * @param index
     * @private
     */
    function _addLayer(layer, index) {
        if (layer == null) return;
        else if (layer instanceof Layer) {
            _map.addLayer(layer,index);
            return;
        }
        var nLayer=undefined;
        switch (layer.type) {
            case "export":
            case "dynamic":
                var opt = layer;
                if(opt.visible===true)
                {
                    nLayer = new ArcGISDynamicMapServiceLayer(layer.url, opt);
                    _map.addLayer(nLayer, index);
                }
                break;
            case "tiled":
                var opt = layer;
                if(opt.visible===true)
                {
                    nLayer = new ArcGISTiledMapServiceLayer(layer.url, opt);
                    _map.addLayer(nLayer, index);
                }
                break;
            default :
                console.log(" current layer type" + layer.type + " not supported ! ");
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
    /**
     * 保存全局配置
     */
    function saveGlobalConfig() {
        var form = SerializeForm.serializeObject($('#global-form'));
        var geometryService = {};
        geometryService.url = $('#dlfwdz').val();
        form.geometryService = geometryService;
        form.logoVisible = globalAttrs.logoVisible;
        form.coordinateVisible = globalAttrs.coordinateVisible;
        var config = JSON.stringify(form);
        $.ajax({
            url: root + "/config/tpl/" + tpl + "/global",
            method: 'post',
            async: false,
            data: {global: config},
            success: function () {
                layer.msg('修改成功！',{icon: 1,time:700});
            }
        });
    }
    return {
        init: function () {
            _init();
        }
    };
});


