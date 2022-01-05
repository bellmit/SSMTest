var treeReload;
layui.use(['element', 'jquery', 'tree', 'table', 'layer', 'form'], function () {
    var element = layui.element,
        $ = layui.jquery,
        tree = layui.tree,
        table = layui.table,
        layer = layui.layer,
        form = layui.form;

    var IP = getContextPath(1) + "/msurveyplat-server/v1.0/qualitycheck/";
    var dIP = getContextPath(1);
    //var IP = "http://192.168.50.38:8083/msurveyplat-server/v1.0/qualitycheck/";
    // let dIP = "http://192.168.50.38:8085";
    var parentNodeHeight = document.documentElement.clientHeight;
    $(".service-header-tab>.layui-tab-content").height(parentNodeHeight - 46 + "px");

    var maxheight = parentNodeHeight - 550;

    var parentNodeWidth = document.documentElement.clientWidth;
    $(".bdc-tab-aside .layui-tab-content").width(parentNodeWidth - 180 + "px");

    //侧边栏隐藏、显示
    var zjxqTableWidth = '';
    $('#asideMoreBtn').click(function () {
        if ($(this).hasClass('layui-icon-shrink-right')) {
            $(this).removeClass('layui-icon-shrink-right').addClass('layui-icon-spread-left');
            $('.bdc-aside').hide().siblings().css({ 'padding': '0px' });
            zjxqTableWidth = parentNodeWidth - 20;
            table.reload('hcjg-table', { width: zjxqTableWidth });
            table.reload('otherError-table', { width: zjxqTableWidth });
        } else {
            $(this).removeClass('layui-icon-spread-left').addClass('layui-icon-shrink-right');
            $('.bdc-aside').show().siblings().css({ 'padding': '0 0 0 180px' });
            zjxqTableWidth = parentNodeWidth - 200;
            table.reload('hcjg-table', { width: zjxqTableWidth });
            table.reload('otherError-table', { width: zjxqTableWidth });
        }
    })

    /**
     * 监听tab切换
     */
    element.on('tab(zjxx-aside-tab)', function (d) {
        switch (d.index) {
            case 1:
                $('.jcxxjg-more-btn').addClass('icon-up');
                $(' .bdc-zjxq-content .right').css('height', '40px');
                $(' .bdc-zjxq-content .left').css('height', 'calc(100% - 40px)');
                $(' .bdc-zjxq-content .right-bottom').css('height', 'calc(100% - 40px)');
                getZjxq();
                break;
            case 2:
                qtcw();
                break;
            case 3:
                zjzlInit();
                break;
            default:
                break;
        }
    })

    var cwdjzdm;
    //获取字典项
    $.ajax({
        url: IP + "getCwjb",
        type: 'post',
        data: "",
        success: function (data) {
            cwdjzdm = data.data;
        }
    });


    var first;

    //获取xmid
    var urlParma = con_getPageUrlParam();

    //获取xmbh
    var xmid = urlParma.xmid;


    var Mainid;
    //获取质检总览多棵树
    $.ajax({
        url: IP + "getJclx",
        type: 'post',
        data: JSON.stringify({
            "xmbh": parent.slbh
        }),
        success: function (data) {
            //质检总览多颗树
            for (var i = 0; i < data.length; i++) {
                var html = '<div id="checkTree' + i + '" class="demo-tree-more"></div>';
                $(".bottom").append(html);
                data[i].spread = true;
                tree.render({
                    elem: '#checkTree' + i,
                    data: [data[i]],
                    accordion: true,
                    showLine: false, //是否开启连接线
                    //		click: function(obj) {
                    //			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
                    //		}
                });

                delete data[i].spread;
            }
        }
    });
    function getZjxq() {
        //设置地图iframe的url
        $('#mapIframe').attr('src', '../map-interaction/index.html?slbh=' + parent.slbh + '&ywlx=' + parent.ywlx)

        //获取质检详情树数据
        getCheckTree();

        //获取错误总数
        $.ajax({
            url: IP + "countJcjgTotal",
            type: 'post',
            data: { "xmbh": parent.slbh },
            success: function (data) {
                $("#cwzs").html(data.JGSL);
            }
        });
    }
    function getCheckTree() {
        $.ajax({
            url: IP + "getJclxTotal",
            type: 'post',
            async: false,
            data: {
                "xmbh": parent.slbh
            },
            success: function (data) {
                if (data.length > 0) {
                    //质检详情树
                    data[0].checked = true;
                    data[0].spread = true;
                    tree.render({
                        elem: '#check-tree',
                        data: data,
                        id: 'checkTree',
                        accordion: true,
                        showLine: false, //是否开启连接线
                        click: function (obj) {
                            zjxq(obj.data.id);
                            $(' .bdc-zjxq-content .right').css('height', 'calc(40% - 10px)');
                            $(' .bdc-zjxq-content .left').css('height', '60%');
                            $(' .bdc-zjxq-content .right-bottom').css('height', '60%');
                            $('.jcxxjg-more-btn').removeClass('icon-up');
                        }
                    });
                    $('#check-tree').children().children().children('.layui-tree-entry').find('.layui-tree-txt').addClass('checkedTreeItem');
                    zjxq(data[0].id);
                }
            }
        })
    }
    //质检详情重载
    treeReload = function (checkTreeData) {
        $.ajax({
            url: IP + "getJclxTotal",
            type: 'post',
            async: false,
            data: {
                "xmbh": parent.slbh
            },
            success: function (data) {
                if (data.length > 0) {
                    //质检详情树
                    data[0].checked = true;
                    data[0].spread = true;
                    tree.render({
                        elem: '#check-tree',
                        data: data,
                        id: 'checkTree',
                        accordion: true,
                        showLine: false, //是否开启连接线
                        click: function (obj) {
                            zjxq(obj.data.id);
                            $(' .bdc-zjxq-content .right').css('height', 'calc(40% - 10px)');
                            $(' .bdc-zjxq-content .left').css('height', '60%');
                            $(' .bdc-zjxq-content .right-bottom').css('height', '60%');
                            $('.jcxxjg-more-btn').removeClass('icon-up');
                        }
                    });
                    $('.layui-tree-txt').removeClass('checkedTreeItem');
                    $('#check-tree').children().children().children('.layui-tree-pack').children("div:last-child").children('.layui-tree-entry').find('.layui-tree-txt').addClass('checkedTreeItem');
                    var otherDataId = '';
                    data[0].children.forEach(function (v) {
                        var title = v.title;
                        if (title.indexOf('其他检查') >= 0) {
                            otherDataId = v.id;
                        }
                    })
                    zjxq(otherDataId);
                    $(' .bdc-zjxq-content .right').css('height', 'calc(40% - 10px)');
                    $(' .bdc-zjxq-content .left').css('height', '60%');
                    $(' .bdc-zjxq-content .right-bottom').css('height', '60%');
                    $('.jcxxjg-more-btn').removeClass('icon-up');
                }
            }
        })
    }
    //点击树节点高亮显示
    $('.left').on('click', '.layui-tree-txt', function () {
        $('.layui-tree-txt').removeClass('checkedTreeItem');
        $(this).addClass('checkedTreeItem');
    });

    //初审时，可以执行质检
    if (parent.jdmc == "cs") {
        $("#zxzj").show();
    }
    if (parent.jdmc == "fs" || parent.jdmc == "bj") {
        $("#zxzj").hide();
    }

    /****************************质检总览**************************/


    /*****************************质检详情部分*********************/
    $('#hcjg-item-tab').on('click', 'a', function () {
        $(this).css('color', '#1d87d1').siblings().css('color', '#333');
    });


    //显示、隐藏检查详细结果
    $('.jcxxjg-more-btn').click(function () {
        var topH = '';
        var bottomH = '';
        if ($(this).hasClass('icon-up')) {
            $(this).removeClass('icon-up');
            topH = '60%';
            bottomH = 'calc(40% - 10px)';
        } else {
            $(this).addClass('icon-up');
            topH = 'calc(100% - 40px)';
            bottomH = '40px';
        }
        $(' .bdc-zjxq-content .right').css('height', bottomH);
        $(' .bdc-zjxq-content .left').css('height', topH);
        $(' .bdc-zjxq-content .right-bottom').css('height', topH);
        table.reload('hcjg-table', { width: zjxqTableWidth });
    })

    //质检详情----检查详细结果
    function zjxq(id) {
        var dm = id;
        table.render({
            elem: '#hcjg-table',
            width: zjxqTableWidth,
            page: true,
            loading: true,
            cols: [[
                { field: 'CWDJ', title: '错误等级', align: 'center', width: 150 },
                { field: 'TCMC', title: '图层名称', align: 'center', width: 200 },
                /*    { field: 'cwtx', title: '错误图形', align: 'center' },*/
                { field: 'JCSJ', title: '检查时间', align: 'center', width: 150 },
                { field: 'CWMS', title: '错误描述', align: 'center' },
                // { field: 'SFLW', title: '是否例外', align: 'center' },
                // { field: 'LWYY', title: '例外原因', align: 'center' }
            ]],
            url: IP + "getJcjg",
            method: "post",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'size' //每页数据量的参数名，默认：limit
                , loadTotal: true
                , loadTotalBtn: false
            },
            limit: 5,
            limits: [5, 10, 15, 20],
            where: { xmbh: parent.slbh, dm: dm },
            parseData: function (res) {
                return {
                    "code": "0000", //解析接口状态
                    "msg": "", //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },

        });
    }
    //监听行单击事件
    table.on('row(hcjg-table)', function (obj) {
        console.log('当前行数据:', obj.data) //得到当前行数据
        window.frames[0].postMessage({
            tcmc: obj.data.TCMC,
            where: obj.data.CWYSID
        }, window.location.protocol + '//' + window.location.host);
    });

    /*****************************其他错误部分*********************/
    var index;
    //其他错误表格
    function qtcw() {
        table.render({
            elem: '#otherError-table',
            toolbar: '#otherErrorToolbarBtn',
            width: zjxqTableWidth,
            height: parentNodeHeight - 30,
            defaultToolbar: [],
            page: true,
            loading: true,
            // data:[{"cwms":"页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称页码的参数名称","cwdj":"一级缺陷"}],
            url: IP + "getQtcw",
            method: "post",
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'size' //每页数据量的参数名，默认：limit
                , loadTotal: true
                , loadTotalBtn: false
            },
            where: { xmid: xmid },
            parseData: function (res) {
                return {
                    "code": "0000", //解析接口状态
                    "msg": "", //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            cols: [[ //表头
                { type: 'checkbox' },
                { type: 'numbers', title: '序号', align: 'center' },
                { field: 'cwms', title: '错误描述', align: 'center' },
                // { field: 'cwdj', title: '错误等级', align: 'center',templet:'#cwdjTpl'},
                { field: 'cwdj', title: '错误等级', align: 'center', width: 120 },
                { field: 'cz', title: '操作', align: 'center', templet: '#uploadTpl', width: 120 }
            ]]
        });
    }
    qtcw();

    //其他错误新增 删除 修改 操作
    //头工具栏事件
    table.on('toolbar(otherError-table)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        var length = checkStatus.data.length;
        switch (obj.event) {
            case 'addBtn':
                clear();    //清空cwid
                addqtcw();
                break;
            case 'deleteBtn':
                if (length < 1) {
                    layer.msg("请选择至少一条数据。");
                } else {
                    deleteqtcw(checkStatus.data);
                }
                break;
            case 'saveBtn':
                if (length > 1) {
                    layer.msg("只可选择一条数据进行修改。");
                } else if (length < 1) {
                    layer.msg("请选择一条数据。");
                } else {
                    saveqtcw(checkStatus.data);
                }
                break;
        }
    });

    //其他错误新增
    function addqtcw() {
        var optionhtml = "";
        for (var i = 0; i < cwdjzdm.length; i++) {
            optionhtml += '<option value="' + cwdjzdm[i].DM + '">' + cwdjzdm[i].MC + '</option>';
        }
        var content = '<div class="addqtcwmodal"><form class="layui-form layui-form-pane" action="" lay-filter="example">' +
            '<div class="layui-form-item">' +
            '   <label class="layui-form-label">错误等级</label>' +
            '   <div class="layui-input-block" id="cwdj">' +
            '       <select name="interest" lay-filter="aihao">' +
            '           <option value=""></option>' +
            optionhtml +
            '       </select>' +
            '   </div>' +
            '</div>' +
            '<div class="layui-form-item layui-form-text">' +
            '   <label class="layui-form-label" style="padding: 8px 25px;">' +
            '       错误描述' +
            '       <a id="xzcwFj" class="uploadhref">上传附件</a>' +
            '   </label>' +
            '   <div class="layui-input-block">' +
            '       <textarea placeholder="请输入内容" class="layui-textarea" id="cwms"></textarea>' +
            '   </div>' +
            '</div> ' +
            '<div class="layui-form-item bdc-add-sure-btn" style="text-align: center">' +
            '    <input id="addqtcwConfirm" type="button" class="addqtcw-confirm" value="确定" style="width: 75px;font-size: 14px;padding: 5px;text-align: center;background: #1d87d1;color: #ffffff;border: none;cursor: pointer;">' +
            '</div>' +
            '</form></div>';
        index = layer.open({
            type: 1,
            title: "新增其他错误",
            skin: 'layui-layer-rim',
            area: ['430px'], //宽高
            content: content
        });
        form.render();
    }
    function clear() {
        cwid = ''
        wjzxjdid = ''
    }



    var cwid = '';
    var wjzxjdid = '';
    var userId = localStorage.getItem("userId");

    //确认新增
    $("body").on("click", "#addqtcwConfirm", function () {
        //layer.close(index);
        var cwms = $("#cwms").val();
        var cwdj = $("#cwdj .layui-form-select dl dd.layui-this").attr("lay-value");
        if (cwms == "" || cwdj == "" || cwdj == undefined) {
            layer.msg("请补充相关信息!");
            return false;
        }
        var upaddurl;
        $.ajax({
            url: IP + "getqtcwxx",
            type: 'post',
            data: cwid,
            contentType: 'application/json;charset=utf-8',
            async: false,
            success: function (data) {
                console.log(data);
                if (data != "") {
                    first = true;
                } else {
                    first = false;
                }
            }
        })

        if (first) { //cwid
            upaddurl = "updateQtcw";
            // 根据cwid取出数据 并且重新赋值 cwms cwdj
            $.ajax({
                url: IP + "getqtcwxx",
                type: 'post',
                data: cwid,
                contentType: 'application/json;charset=utf-8',
                //async: false,
                success: function (data) {
                    console.log(data.CWID);
                    var result = {};
                    result.cwid = data.CWID;
                    result.cwjb = cwdj;
                    result.cwms = cwms;
                    result.shr = data.SHR;
                    result.shrid = data.SHRID;
                    result.tjsj = data.TJSJ;
                    result.wjzxjdid = data.WJZXJDID;
                    result.xmid = data.XMID;
                    $.ajax({
                        url: IP + upaddurl,
                        type: 'post',
                        contentType: 'application/json;charset=utf-8',
                        data: JSON.stringify(result),
                        //async: false,
                        success: function (data) {
                            layer.msg("新增成功");
                            layer.close(index);
                            qtcw();
                        }
                    })
                }
            })
        } else {
            $.ajax({
                url: IP + "xzqtcw",
                type: 'post',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify([{
                    "xmid": xmid,
                    "cwms": cwms,
                    "cwjb": cwdj,
                    "cwid": cwid,
                    "wjzxjdid": wjzxjdid
                }]),
                success: function (data) {
                    layer.msg("新增成功");
                    layer.close(index);
                    qtcw();
                }
            })
        }
    });


    //生成新的cwid
    function scnewcwid() {
        $.ajax({
            url: IP + "scqtcwId",
            type: 'post',
            async: false,
            success: function (data) {
                cwid = data;
                $.ajax({
                    type: 'post',
                    url: getContextPath() + "/msurveyplat-server/rest/v1.0/getqtcwsccs/sld/" + parent.slbh + '/' + cwid + '/' + userId,
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        wjzxjdid = data.nodeId

                    },
                    error: function (err) {
                    }
                })
            }
        });

    }




    //新增错误上传附件
    $("body").on("click", "#xzcwFj", function () {


        //入库
        var cwms = $("#cwms").val();
        var cwdj = $("#cwdj .layui-form-select dl dd.layui-this").attr("lay-value");
        if (cwms == "" || cwdj == "" || cwdj == undefined) {
            layer.msg("请补充相关信息!");
        } else {
            scnewcwid();//每次新增生成新的cwid
            //生成新的cwid
            console.log("新的错误id" + cwid)
            console.log("新的文件id" + wjzxjdid)
            $.ajax({
                url: IP + "xzqtcw",
                type: 'post',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify([{
                    "xmid": xmid,
                    "cwms": cwms,
                    "cwjb": cwdj,
                    "cwid": cwid,
                    "wjzxjdid": wjzxjdid
                }]),
                success: function (data) {
                    qtcw();
                }
            })
            window.open(dIP + "/msurveyplat-server/view/sccl-page.html?slbh=" + parent.slbh + "&qtcwid=" + cwid + "&qtcw=true", "_blank");
        }


    });


    table.on('tool(otherError-table)', function (obj) {
        console.log(obj);
        var cwid = obj.data.cwid;
        if (obj.data.wjzxjdid == null) {
            $.ajax({
                type: 'post',
                url: getContextPath() + "/msurveyplat-server/rest/v1.0/getqtcwsccs/sld/" + parent.slbh + '/' + cwid + '/' + userId,
                dataType: "json",
                async: false,
                success: function (data) {
                    wjzxjdid = data.nodeId;
                    obj.data.wjzxjdid = wjzxjdid;
                    $.ajax({
                        url: IP + "updateQtcw",
                        type: 'post',
                        contentType: 'application/json;charset=utf-8',
                        data: JSON.stringify(obj.data),
                        success: function (data) {
                            layer.msg("修改成功");
                            qtcw();
                        }
                    })
                },
                error: function (err) {
                }
            })
        }
        window.open(dIP + "/msurveyplat-server/view/sccl-page.html?slbh=" + parent.slbh + "&qtcwid=" + cwid + "&qtcw=true", "_blank");
    });




    //其他错误删除
    function deleteqtcw(data) {
        layer.open({
            type: 1,
            title: '提示',
            skin: 'bdc-small-tips bdc-zf-tips',
            area: ['320px', '150px'],
            content: '是否确认删除？',
            btn: ['确定', '取消'],
            btnAlign: 'c',
            yes: function () {

                var cwidlist = [];
                for (var i = 0; i < data.length; i++) {
                    cwidlist.push(data[i].cwid);
                }
                $.ajax({
                    url: IP + "delQtcw",
                    type: 'post',
                    contentType: 'application/json;charset=utf-8',
                    data: JSON.stringify(cwidlist),
                    success: function (data) {
                        layer.msg("删除成功");
                        qtcw();
                        //第一次删除后 先去查询其他错误文件夹id是否存在
                        $.ajax({
                            url: getContextPath() + "/msurveyplat-server/rest/v1.0/getMainId/sld/" + parent.slbh,
                            contentType: 'application/json;charset=utf-8',
                            type: 'post',
                            success: function (data) {
                                Mainid = data
                                if (Mainid != "" && Mainid != undefined && Mainid != null) {
                                    $.ajax({
                                        url: IP + "deleteMainCw?xmid=" + xmid + "&Mainid=" + Mainid,
                                        type: 'post',
                                        datatype: 'text',
                                        success: function (obj) {
                                        }
                                    })
                                }
                            },
                            error: function (err) {
                            }
                        })
                    }
                })
                layer.closeAll();
            }
        });

    }

    //确认删除

    var toUpdata;
    //其他错误修改
    function saveqtcw(data) {
        toUpdata = data;
        var cwdj = data[0].cwjb;
        var optionhtml = "";
        for (var i = 0; i < cwdjzdm.length; i++) {
            if (cwdj == cwdjzdm[i].DM) {
                optionhtml += '<option value="' + cwdjzdm[i].DM + '" selected>' + cwdjzdm[i].MC + '</option>';
            } else {
                optionhtml += '<option value="' + cwdjzdm[i].DM + '">' + cwdjzdm[i].MC + '</option>';
            }

        }
        var content = '<div class="addqtcwmodal"><form class="layui-form layui-form-pane" action="">' +
            '<div class="layui-form-item" id="cwdj">' +
            '\t\t<label class="layui-form-label">错误等级</label>' +
            '\t\t<div class="layui-input-block">' +
            '\t\t\t<select name="interest" lay-filter="aihao">' +
            '\t\t\t\t<option value=""></option>' +
            optionhtml +
            '\t\t\t</select>' +
            '\t\t</div>' +
            '</div>' +
            '<div class="layui-form-item layui-form-text">' +
            '   <label class="layui-form-label" style="padding: 8px 25px;">' +
            '       错误描述' +
            '       <a id="xgcwFj" class="uploadhref" href="' + dIP + '/msurveyplat-server/view/sccl-page.html" target="_blank">上传附件</a>' +
            '   </label>' +
            '\t\t<div class="layui-input-block">' +
            '\t\t\t<textarea placeholder="请输入内容" class="layui-textarea" id="cwms">' + data[0].cwms + '</textarea>' +
            '\t\t</div>' +
            '</div> ' +
            ' <div class="layui-form-item bdc-add-sure-btn" style="text-align: center">\n' +
            '    <input id="saveqtcwConfirm" type="button" class="addqtcw-confirm" value="确定" style="width: 75px;font-size: 14px;padding: 5px;text-align: center;background: #1d87d1;color: #ffffff;border: none;cursor: pointer;">' +
            '  </div>' +
            '</form></div>';
        index = layer.open({
            type: 1,
            title: "修改其他错误",
            skin: 'layui-layer-rim',
            area: ['430px'], //宽高
            content: content
        });
        form.render();
    }

    //确认修改
    $("body").on("click", "#saveqtcwConfirm", function () {
        var newData = toUpdata[0];
        var cwms = $("#cwms").val();
        var cwjb = $("#cwdj .layui-form-select dl dd.layui-this").attr("lay-value");
        var cwdj = $("#cwdj .layui-form-select dl dd.layui-this").text();
        if (cwms == "" || cwdj == "") {
            layer.msg("请补充相关信息!");
            return false;
        } else {
            newData.cwms = cwms;
            newData.cwjb = cwjb;
            delete newData.cwdj;
            $.ajax({
                url: IP + "updateQtcw",
                type: 'post',
                contentType: 'application/json;charset=utf-8',
                data: JSON.stringify(newData),
                success: function (data) {
                    layer.msg("修改成功");
                    layer.close(index);
                    qtcw();
                }
            })
        }
    });

    //修改错误附件
    $("body").on("click", "#xgcwFj", function () {
        //获取cwid
        var newData = toUpdata[0];
        cwid = newData.cwid;
        var cwdj = $("#cwdj .layui-form-select dl dd.layui-this").text();
        var cwms = $("#cwms").val();
        if (cwms == "" || cwdj == "") {
            layer.msg("请补充相关信息!");
            return false;
        } else {
            window.open(dIP + "/msurveyplat-server/view/sccl-page.html?slbh=" + parent.slbh + "&qtcwid=" + cwid + "&qtcw=true", "_blank");
            return false;
        }


    });
    /**
        * 质检初始化
        * 已质检，初始化直接显示质检结果
        * 未质检，手动点击质检查询结果
        */
    getzjResult();
    function getzjResult() {
        $.ajax({
            url: IP + "getJcnr",
            type: 'post',
            data: {
                "xmbh": parent.slbh
            },
            success: function (ResData) {
                if (ResData != null && ResData.data != null) {
                    var reg = new RegExp("\r\n", "g");
                    var jcms = ResData.data.JCMS;
                    jcms = jcms.replace(reg, "<br>");
                    $(".cgzj-detail").html(jcms);
                    $('.no-mapData-box').hide().siblings().show();
                } else {
                    $('.no-mapData-box').show().siblings().hide();
                }
            }
        });
    }
    //质检执行按钮点击
    $("#zxzj").click(function () {
        $.ajax({
            url: IP + "getJcnr",
            type: 'post',
            data: {
                "xmbh": parent.slbh
            },
            success: function (data) {
                if (data.data != null) {
                    layer.msg("已完成质检!");
                    return false;
                } else {
                    // 成果包是否存在
                    $.ajax({
                        url: IP + "check/" + parent.slbh,
                        type: 'post',
                        success: function (res) {
                            //成果包存在允许质检
                            if (res.code) {
                                baseLayer('开始质检', function () {
                                    parent.document.getElementById('progressModal').style.display = 'block';
                                    $.ajax({
                                        url: IP + "rest/v1.0/qc/qualitycheck/" + xmid + '/' + parent.slbh,
                                        type: 'post',
                                        success: function (zxzjData) {
                                            console.log('执行质检：', zxzjData.data);
                                            parent.zxzj = true;
                                            var timeNum = 10;
                                            var timer = setInterval(function () {
                                                if (timeNum < 100) {
                                                    timeNum += 10;
                                                    parent.document.getElementById('bdc-process').setAttribute('lay-percent', timeNum + '%');
                                                    parent.element.init();
                                                } else if (timeNum == 100) {
                                                    clearInterval(timer);
                                                    parent.document.getElementById('progressModal').style.display = 'none';
                                                    parent.document.getElementById('bdc-process').setAttribute('lay-percent', '0%');
                                                    parent.element.init();
                                                    if (!zxzjData.success) {
                                                        layer.msg(zxzjData.message);
                                                    } else if (zxzjData.result != null && zxzjData.result[0] != null) {
                                                        if (!zxzjData.result[0].success) {
                                                            layer.msg(zxzjData.result[0].message);
                                                        } else {
                                                            layer.msg("质检成功！");
                                                        }
                                                    }
                                                    //搜索结果
                                                    getzjResult();
                                                }
                                            }, 500);
                                        }
                                    });
                                })
                                //成果包不存在
                            } else {
                                layer.msg('未上传成果包！');
                            }
                        }
                    });
                }
            }
        });
    });
    //获取URL参数
    function con_getPageUrlParam() {
        var _search = location.search;
        var paramObj = {};
        if (typeof _search == "string") {
            var param = _search.split("?")[1];
            param && param.split("&").forEach(function (item) {
                if (item) {
                    var arr = item.split("=");
                    paramObj[arr[0]] = arr[1];
                }
            });
            return paramObj;
        } else {
            return paramObj;
        }
    }
    //----------------------------质检总览---------------------------
    function zjzlInit() {
        cgzjgktjt();
        cgzjcwtjt();
    }
    // 成果质检概况统计图
    function cgzjgktjt() {
        $('#cgzjgktjtEcharts').css({ 'height': '100%', 'width': '100%' });
        var cgzjgktjtEcharts = echarts.init(document.getElementById('cgzjgktjtEcharts'));
        var cgzjgktjtOption = {
            title: {
                text: '成果质检概况统计图',
                x: 'center',
                textStyle: {
                    fontSize: 14
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                bottom: 'bottom',
                data: [
                   '质检正确项', '质检错误项']
            },
            series: [
                {
                    name: '成果质检概况统计',
                    type: 'pie',
                    radius: '75%',
                    center: ['50%', '50%'],
                    data: [
                        {
                            value: 81, name: '质检正确项',
                            itemStyle: {
                                color: '#68cdde'
                            }
                        }
                        ,
                        {
                            value: 15, name: '质检错误项',
                            itemStyle: {
                                color: '#fcb476'
                            }
                        }
                    ],
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                formatter: '{d}%'
                            },
                            labelLine: { show: true },
                        }
                    },
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
        };
        cgzjgktjtEcharts.setOption(cgzjgktjtOption);
    }
    // 成果质检错误统计图
    function cgzjcwtjt() {
        $('#cgzjcwtjtEcharts').css({ 'height': '100%', 'width': '100%' });
        var cgzjcwtjtEcharts = echarts.init(document.getElementById('cgzjcwtjtEcharts'));
        var cgzjcwtjtOption = {
            title: {
                text: '成果质检错误统计图',
                top: 10,
                left: 'center',
                textStyle: {
                    fontSize: 14
                }
            },
            tooltip: {

            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '5%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                axisTick: {
                    show: false,
                    color: '#707070'
                },
                axisLabel: {
                    textStyle: {
                        fontSize: 12,
                        color: '#4D4D4D'
                    }
                },
                axisLine: {
                    lineStyle: {
                        color: '#707070'
                    }
                },
                data: ['属性表完整性', '图层要素存在性', '属性表非空检查', '属性表枚举检查'],
            }],
            yAxis: {
                "axisTick": { //y轴刻度线
                    "show": false
                },
                "axisLine": { //y轴
                    "show": false

                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed'
                    }
                }

            },
            series: [{
                name: '成果质检错误统计',
                type: 'bar',
                barWidth: '50%',
                data: [{
                    name: '属性表完整性',
                    value: '2',
                    itemStyle: {
                        color: '#1d87d1'
                    }
                },
                {
                    name: '图层要素存在性',
                    value: '2',
                    itemStyle: {
                        color: '#e057f4'
                    }
                },
                {
                    name: '属性表非空检查',
                    value: '10',
                    itemStyle: {
                        color: '#68cdde'
                    }
                },
                {
                    name: '属性表枚举检查',
                    value: '1',
                    itemStyle: {
                        color: '#fcb476'
                    }
                }],
                itemStyle: {
                    normal: {
                        label: {
                            show: true, //开启显示
                            position: 'top', //在上方显示
                            textStyle: { //数值样式
                                color: '#333',
                                fontSize: 14
                            }
                        }
                    }
                }
            }]
        };
        cgzjcwtjtEcharts.setOption(cgzjcwtjtOption);
    }

})