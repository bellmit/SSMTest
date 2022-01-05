layui.use(['tree', 'util', 'jquery', 'form', 'laydate'], function () {
    var tree = layui.tree,
        layer = layui.layer,
        $ = layui.jquery,
        form = layui.form,
        laydate = layui.laydate,
        util = layui.util;


    // 渲染开始时间
    laydate.render({
        elem: '#kssj',
    });

    // 渲染结束时间
    laydate.render({
        elem: '#jssj',
    });

    var nameItem = [];
    var gxywid = '';
    var gxnridList = [];
    var gxnrnameList = [];
    var loadPage = '';
    var loadSize = '';
    var sfcqyx = 0;

    form.on('select(gxbmmc)', function (data) {
        selectRoles(data.value);
    });


    form.on('select(gxjsmc)', function (data) {
        layui_gxjsid = data.value;
    });


    //新增时获取gxywid
    function getGxywid() {
        $.ajax({
            type: 'GET',
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/common/uuid",
            dataType: 'text',
            data: {},
            success: function (res) {
                gxywid = res;
            },
            error: function () {
                alert("error");
            }
        })
    }

    //渲染部门和角色下拉
    function selectOrgan(organ, role) {
        $.ajax({
            type: 'POST',
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/department/list",
            data: {},
            dataType: 'json',
            success: function (res) {
                var html = '<option value="">请选择共享部门</option>';
                res.forEach(function (item) {
                    if (item.organName == organ) {
                        html += '<option selected value=' + item.organId + '>' + item.organName + '</option>'
                    } else {
                        html += '<option value=' + item.organId + '>' + item.organName + '</option>'
                    }
                });
                $('.gxbmmc select').html(html);
                form.render('select');
            }
        })
    }

    function selectRoles(organid, role) {
        if (organid != null && organid != '') {
            $.ajax({
                type: "GET",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/role/list/" + organid,
                data: organid,
                dataType: "json",
                success: function (res) {
                    var RoleHtml = '<option value="">请选择共享角色</option>';
                    res.forEach(function (item) {
                        if (item.roleName == role) {
                            RoleHtml += '<option selected value=' + item.roleId + '>' + item.roleName + '</option>'
                        } else {
                            RoleHtml += '<option value=' + item.roleId + '>' + item.roleName + '</option>'
                        }
                    });
                    $('.gxjsmc select').html(RoleHtml);
                    form.render('select');
                }
            })
        } else {
            var RoleHtml = '<option value="">请选择共享角色</option>';
            $('.gxjsmc select').html(RoleHtml);
            form.render('select');
        }
    }


    tree.render({
        elem: '#tree0',
        data: getGxnr(0),
        id: "tree0",
        showCheckbox: true,
        oncheck: function (obj) {
            ClickHandler(obj);
        }
    });

    tree.render({
        elem: '#tree1',
        data: getGxnr(1),
        id: "tree1",
        showCheckbox: true,
        oncheck: function (obj) {
            ClickHandler(obj);
        }
    });

    tree.render({
        elem: '#tree2',
        data: getGxnr(2),
        id: "tree2",
        showCheckbox: true,
        oncheck: function (obj) {
            ClickHandler(obj);
        }
    });


    tree.render({
        elem: '#tree3',
        data: getGxnr(3),
        id: "tree3",
        showCheckbox: true,
        oncheck: function (obj) {
            ClickHandler(obj);
        }
    });


    //节点点击事件
    function ClickHandler() {
        for (i = 0; i < gxnridList.length; i++) {
            var flag = document.getElementById(gxnridList[i]);
            if (flag !== null) {
                delLi(gxnridList[i]);
            }
        }
        gxnridList = [];
        gxnrnameList = [];
        var checkData0 = tree.getChecked('tree0');
        var checkData1 = tree.getChecked('tree1');
        var checkData2 = tree.getChecked('tree2');
        var checkData3 = tree.getChecked('tree3');
        getCheckedId(checkData0);
        getCheckedId(checkData1);
        getCheckedId(checkData2);
        getCheckedId(checkData3);

        // 获取选中节点的id
        function getCheckedId(jsonObj) {
            $.each(jsonObj, function (index, item) {
                if (item.children.length != 0) {
                    getCheckedId(item.children);
                } else {
                    if (item.type == '2') {
                        gxnridList.push(item.id);
                        gxnrnameList.push(item.title);
                    }
                }
            });
        }

        for (i = 0; i < gxnridList.length; i++) {
            addLi(gxnridList[i], gxnrnameList[i]);
        }
    }

    //动态添加li
    function addLi(id, title) {
        if (id.startsWith("2")) {
            $(".addtree2").css('display', '');
            $("#node_add2").append("<li id=" + id + ">" + title + "</li>");
        }
        if (id.startsWith("3")) {
            $(".addtree0").css('display', '');
            $("#node_add3").append("<li id=" + id + ">" + title + "</li>");
        }
        if (id.startsWith("4")) {
            $(".addtree3").css('display', '');
            $("#node_add0").append("<li id=" + id + ">" + title + "</li>");
        }
        if (id.startsWith("5")) {
            $(".addtree1").css('display', '');
            $("#node_add1").append("<li id=" + id + ">" + title + "</li>");
        }


    }

    //动态删除li
    function delLi(id) {
        document.getElementById(id).remove();
        var delFlag0 = document.querySelector('#node_add0').innerHTML;
        var delFlag1 = document.querySelector('#node_add1').innerHTML;
        var delFlag2 = document.querySelector('#node_add2').innerHTML;
        var delFlag3 = document.querySelector('#node_add3').innerHTML;
        if (delFlag0 == '') {
            $(".addtree3").css('display', 'none');
        }
        if (delFlag1 == '') {
            $(".addtree1").css('display', 'none');
        }
        if (delFlag2 == '') {
            $(".addtree2").css('display', 'none');
        }
        if (delFlag3 == '') {
            $(".addtree0").css('display', 'none');
        }
    }

    util.event('lay-active', {
        getTree0: function () {
            $(".tree1").css('display', '');
            $(".tree0").css('display', 'none');
            $(".tree2").css('display', 'none');
            $(".tree3").css('display', 'none');
            $("#gcjsxk").css('color', '#1E9FFF');
            $("#jgys").css('color', '');
            $("#lxydghxk").css('color', '');
            $("#sgxk").css('color', '');
        },
        getTree1: function () {
            $(".tree0").css('display', 'none');
            $(".tree3").css('display', '');
            $(".tree2").css('display', 'none');
            $(".tree1").css('display', 'none');
            $("#gcjsxk").css('color', '');
            $("#sgxk").css('color', '');
            $("#lxydghxk").css('color', '');
            $("#jgys").css('color', '#1E9FFF');

        },
        getTree2: function () {
            $(".tree2").css('display', 'none');
            $(".tree1").css('display', 'none');
            $(".tree0").css('display', '');
            $(".tree3").css('display', 'none');
            $("#gcjsxk").css('color', '');
            $("#jgys").css('color', '');
            $("#sgxk").css('color', '');
            $("#lxydghxk").css('color', '#1E9FFF');
        },
        getTree3: function () {
            $(".tree0").css('display', 'none');
            $(".tree1").css('display', 'none');
            $(".tree3").css('display', 'none');
            $(".tree2").css('display', '');
            $("#gcjsxk").css('color', '');
            $("#jgys").css('color', '');
            $("#lxydghxk").css('color', '');
            $("#sgxk").css('color', '#1E9FFF');
        }
    });


    //获取共享内容
    function getGxnr(i) {
        var Treedata = [];
        $.ajax({
            type: "POST",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywnrpz/list",
            async: false,
            data: {},
            dataType: "json",
            success: function (res) {
                Treedata.push(res[i + 1]);
            }
        });
        return Treedata;
    }

    $(function () {
        var urlData = GetRequest();
        var gxywxxidd = urlData.gxywxxid;
        var event = urlData.event;
        loadPage = urlData.loadPage;
        loadSize = urlData.loadSize;
        if (event === "detail") {
            $("#jssj").attr("disabled", "disabled");
            $("#gxywmc").attr("disabled", "disabled");
            $("#gxbmmc").attr("disabled", "disabled");
            $("#gxjsmc").attr("disabled", "disabled");
            $("#sfcqyx").attr("disabled", "disabled");
            $('.div-batch-container').hide();
        } else if (event === "add") {
            getGxywid();
            selectOrgan();
        }
        if (gxywxxidd !== undefined && gxywxxidd !== false) {
            gxywid = gxywxxidd;
            obtainGxywxx(gxywxxidd);
        } else {
            var date = new Date();
            $("#kssj").val(getMyDate(date));
        }
    });


    //页面初始化根据台账获取共享业务信息ID并初始化表单赋值
    function obtainGxywxx(gxywxxid) {
        $.ajax({
            type: "GET",
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx/" + gxywxxid,
            data: {gxywxxid: gxywxxid},
            dataType: "json",
            contentType: 'application/json;charset=utf-8',
            async: false,
            success: function (res) {
                var form = layui.form;
                $("#kssj").val(res.gxkssj);
                $("#jssj").val(res.gxjssj);
                $("#gxywmc").val(res.gxywmc);
                if (res.sfcqyx === "1") {
                    $("#sfcqyx").attr("checked", "checked");
                    sfcqyx = 1
                } else {
                    sfcqyx = 0
                }
                form.render()
                var gxbmmc = res.gxbmmc;
                var gxjsmc = res.gxjsmc;
                var gxbmid = res.gxbmid;
                var gxnrList = res.gxnrList;
                selectOrgan(gxbmmc, gxjsmc);
                selectRoles(gxbmid, gxjsmc);
                gxnrList = gxnrList.map(function (item, index, array) {
                    return item - 0;
                });
                tree.setChecked('tree0', gxnrList);
                tree.setChecked('tree1', gxnrList);
                tree.setChecked('tree2', gxnrList);
                tree.setChecked('tree3', gxnrList);
            }
        });

    }


    function GetRequest() {
        var url = location.search; //获取url中"?"符后的字串
        var theRequest = new Object();
        if (url.indexOf("?") != -1) {
            var str = url.substr(1);
            strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
            }
        }
        return theRequest;
    }


    //格式化时间yyyy-mm-dd
    function getMyDate(str) {
        let oDate = new Date(str),
            oYear = oDate.getFullYear(),
            oMonth = oDate.getMonth() + 1,
            oDay = oDate.getDate(),
            oHour = oDate.getHours(),
            oMin = oDate.getMinutes(),
            oSen = oDate.getSeconds(),
            oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay);//最后拼接时间
        //oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + ' ' + getzf(oHour) + ':' + getzf(oMin) + ':' + getzf(oSen);//最后拼接时间
        if (str == null) {
            oTime = "";
        }
        return oTime;
    }

    //补0操作
    function getzf(num) {
        if (parseInt(num) < 10) {
            num = '0' + num;
        }
        return num;
    }


    // 获取选中节点的id
    function getId(jsonObj) {
        if (jsonObj.length != 0) {
            for (var i = 0; i < jsonObj.length; i++) {
                if (jsonObj[i].children.length != 0) {
                    getId(jsonObj[i].children);
                } else if (jsonObj[i].type == "2") {
                    gxnridList.push(jsonObj[i].id);
                    gxnrnameList.push(jsonObj[i].title);
                }

            }
        }
    }

    //点击保存
    $('#save').on('click', function () {
        var gxywmc = $("#gxywmc").val();
        var gxbmid = $(".gxbmmc select option:selected").val();
        var gxbmmc = $(".gxbmmc select option:selected").text();
        var gxjsid = $(".gxjsmc select option:selected").val();
        var gxjsmc = $(".gxjsmc select option:selected").text();
        var gxkssj = $('#kssj').val();
        var gxjssj = $('#jssj').val();
        if (gxjssj !== '') {
            if (gxkssj > gxjssj) {
                layer.msg("开始时间不能大于结束时间！")
                return false;
            }
        }
        if (gxywmc == "") {
            layer.msg("请输入共享业务名称")
            return false
        }
        if (sfcqyx != 1 && gxjssj == "") {
            layer.msg("请选择共享结束时间或者配置成长期有效！")
            return false
        }
        if (gxbmmc == "") {
            layer.msg("请选择共享部门！")
            return false
        }
        if (gxjsmc == "") {
            layer.msg("请选择共享角色！")
            return false
        }
        //var gxjssj = getMyDate(date);
        var checkData0 = tree.getChecked('tree0');
        var checkData1 = tree.getChecked('tree1');
        var checkData2 = tree.getChecked('tree2');
        var checkData3 = tree.getChecked('tree3');
        getCheckedId(checkData0);
        getCheckedId(checkData1);
        getCheckedId(checkData2);
        getCheckedId(checkData3);

        // 获取选中节点的id
        function getCheckedId(jsonObj) {
            $.each(jsonObj, function (index, item) {
                if (item.children.length != 0) {
                    getCheckedId(item.children);
                } else {
                    if (item.type == '2') {
                        nameItem.push(item.id);
                    }
                }
            });
        }

        var params = {
            gxywid: gxywid,
            gxywmc: gxywmc,
            gxbmid: gxbmid,
            gxbmmc: gxbmmc,
            gxjsid: gxjsid,
            gxjsmc: gxjsmc,
            gxkssj: gxkssj,
            gxjssj: gxjssj,
            sfcqyx: sfcqyx,
            gxnrList: nameItem
        };

        if (nameItem.length == 0) {
            layer.msg("请选择共享内容!");
        } else {
            $.ajax({
                type: 'POST',
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/gx/gxywxx",
                data: JSON.stringify(params),
                dataType: 'json',
                contentType: 'application/json;charset=utf-8',
                success: function (res) {
                    layer.msg("保存成功", {
                        time: 500,
                        end: function () {
                            window.location = "sjgxjk.html";
                        }
                    });
                }
            });
        }

    });

    //点击返回
    $('#back').on('click', function () {
        var url = "sjgxjk.html?loadPage=" + loadPage + "&loadSize=" + loadSize;
        window.location = url;
    });


    //监听复选框
    form.on('checkbox(sfcqyx)', function (data) {
        if (data.elem.checked === true) {
            sfcqyx = 1;
            $("#jssj").attr("disabled", "disabled");
        } else {
            sfcqyx = 0;
            $("#jssj").removeAttr("disabled")
        }
    });


});