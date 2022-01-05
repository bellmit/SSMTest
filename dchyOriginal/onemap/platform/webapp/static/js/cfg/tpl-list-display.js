/**
 *
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/4 18:26
 * Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["wookmark",
    "dojo/_base/array",
    "dojo/_base/lang",
    "mustache",
    "slimScroll",
    "layer",
    "text!./template/tpl-list-item.html",
    "text!./template/tpl-create-modal.html",
    "static/thirdparty/bootstrap/js/bootstrap-v3.min",
    "static/thirdparty/Validform/5.3.2/Validform.min",
    "css!static/thirdparty/layer/skin/layer-omp.css"], function (wookmark, arrayUtils, lang, Mustache, SlimScroll, layer, tplItem, tplCreate) {


    var $tplsList = $(".tpls-list");
    var tplList;

    /***
     * 加载tpls
     * @param search
     */
    function loadTpls() {
        layer.config();
        Mustache.parse(tplCreate);
        $("#addBtn").attr("href", "#add-app-modal");
        getTpls();
        var scrollHeight = $(window).height() - 140;
        $(".tpls-content").slimScroll({
            height: scrollHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .3,
            railDraggable: true,
            wrapperClass: 'slimScrollDiv', //外包div类名
            distance: '10px' //组件与侧边之间的距离
        });

        $('#searchShowBtn').on('click',function(){
            filterTpls($("#searchInput")[0].value);
        });

        //添加搜索框的监听
        $("#searchInput").on('keyup', function(evt){
            filterTpls($(this)[0].value);
        });
    }

    /***
     * 过滤模板
     */
    function filterTpls(value) {
        var regExp = new RegExp(".tpl$");
        value = value.replace(regExp, "");
        var arr = arrayUtils.filter(tplList, function (item) {
            if (item.name.indexOf(value) > -1 || item.tpl.indexOf(value) > -1)
                return item;
        });
        if (arr.length > 0) {
            renderTplsList(arr);
        }else{
            /*layer.msg('没有相关的模板！');*/
            var tpl = "<div class='noSameTpl'>没有相关的模板!</div>";
            $($tplsList).empty();
            $($tplsList).append(Mustache.render(tpl));
        }
    }

    /***
     * 请求后台获取模板集合
     */
    function getTpls() {
        $.ajax({
            url: root + '/config/tpls?timestamp=' + (new Date()).getTime(), //时间戳修复ie重复资源不加载bug
            complete: function (evt) {
                var result = evt.responseJSON;
                if (result.hasOwnProperty("success")) {
                    alert(result.msg);
                    return;
                }
                $(".search-panel-form").show();
                tplList = arrayUtils.map(result, function (item) {
                    var tmp = {};
                    lang.mixin(tmp, item);
                    tmp.rootPath = root;
                    return tmp;
                });

                //渲染新建地图模板的内容
                if (document.getElementById("add-app-modal") != null)
                    $("#add-app-modal").remove();
                $("#mainDiv").append(Mustache.render(tplCreate, {tpls: tplList}));

                ////给form添加验证
                $("#create-app-form").Validform({
                    btnSubmit:"#createTplBtn",
                    tiptype:function(msg,o,cssctl){
                        var objtip=$("#msg");
                        cssctl(objtip,o.type);
                        objtip.text(msg);
                        objtip.show();
                    },
                    callback:function(d){
                        //创建新模板
                        var data = {};
                        $.each($("#create-app-form :input").filter(':visible'), function (index, item) {
                            data[item.name] = item.value;
                        });
                        var src = $("input[name='sourceRadio']:checked")[0].value;
                        if (src == 1) {
                            var parent = $("#parentSelect")[0].value;
                            data.parentTpl = parent;
                            console.log(parent);
                        } else {

                        }
                        createTpl(data);
                    }
                });

                //渲染已有的模板列表
                renderTplsList(tplList);

                $("input[name='sourceRadio']").on('click', function (evt) {
                    var src = evt.currentTarget.value;
                    if (src == 1) {
                        $("#parentSelect").fadeIn();
                    } else {
                        $("#parentSelect").fadeOut();
                    }
                });

                $(".tpl-item").on('mouseenter', function () {
                    if ($(this).hasClass("bounceInDown"))
                        $(this).removeClass('bounceInDown');
                    $(this).addClass("animated pulse");
                });

                $(".tpl-item").on('mouseleave', function () {
                    $(this).removeClass('animated pulse');
                });

                //点击删除时 弹出confirm
                $(".tpl-remove").on('click', function (evt) {
                    var tplName = $(this).data("name");
                    var tpl = $(this).data("tpl");
                    layer.confirm('删除『' + tplName + '』模板?', {title: '提示', shadeClose: true}, function (index) {
                        $.ajax({
                            url: root + '/config/tpl/remove/' + tpl + '?timestamp=' + (new Date()).getTime(),
                            complete: function (evt) {
                                layer.close(index);
                                var r = $.parseJSON(evt.responseText);
                                if (r.success != undefined && r.success == false) {
                                    layer.msg(r.msg, {icon: 2});
                                    return;
                                }
                                layer.msg(r.result, {icon: 1, time: 1000}, function () {
                                    getTpls();
                                });
                            }
                        });
                    });
                });
            }
        });
    }
    /***
     * 渲染模板
     * @param data
     */
    function renderTplsList(data) {
        Mustache.parse(tplItem);
        $tplsList.empty();
        $tplsList.append(Mustache.render(tplItem, {data: data}));
        $tplsList.find('.tpl-item').wookmark({
            autoResize: true,
            offset: 20,
            outerOffset: 10 // Optional, the distance to the containers border
        });
    }

    /**
     * 创建新的模板
     * @param data
     */
    function createTpl(data) {
        $.ajax({
            type: "POST",
            url: root + "/config/tpl/create/" + $.trim(data.tpl),
            data: data,
            success: function (msg) {
                if (msg.success == false) {
                    layer.msg(msg.msg, {icon: 2});
                } else {
                    layer.msg('添加成功！',
                        {icon: 1, time: 1000},
                        function (index) {
                            getTpls(); //添加成功后重新渲染列表
                            layer.close(this);
                        });
                }

                $('#add-app-modal').modal('hide');
                //解决模态框阴影问题
                if ($('.modal-backdrop').length > 0) {
                    $("div").detach(".modal-backdrop");
                }
            }
        });
    }

    return {
        init: function () {
            loadTpls();
        }
    };
});