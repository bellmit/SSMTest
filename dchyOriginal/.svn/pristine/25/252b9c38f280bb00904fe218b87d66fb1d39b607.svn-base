/**
 * Created by Administrator on 2019/3/2.
 */
/**
 * Created by Administrator on 2019/1/21.
 */
layui.use(['jquery', 'table', 'element', 'form', 'laytpl', 'response'], function () {
    var $ = layui.jquery,
        element = layui.element,
        table = layui.table,
        laytpl = layui.laytpl,
        response = layui.response,
        form = layui.form;

    $(function () {
        // 是否存在默认角色
        var defaultUser = "";
        // 获取地址栏参数
        var $paramArr = getIpHz();
        var taskId = "";
        var roleId = '';
        var isIndex;
        if ($paramArr['name']) {
            taskId = $paramArr['name'];
        } else if ($paramArr['taskId']) {
            taskId = $paramArr['taskId'];
        }
        var isIndex = $paramArr['isIndex'];
        var currentId = $paramArr['currentId'];
        //获取转发类型、内容和角色
        var col = [[]],
            isSelectAll = false;
        var forwardTask = [];
        // 优先加载表格的边框
        col = [[
            { type: 'radio', width: 50, fixed: 'left' },
            { field: 'activityName', title: '转发活动' },
            { title: '参与角色', templet: '#roleTpl' },
            { title: '参与人', templet: '#personTpl' }
        ]];
        // addModel();
        // 获取转发数据
        $.ajax({
            type: "GET",
            url: getContextPath() + "/index/forward/taskAndRole",
            data: {
                taskId: taskId
            },
            success: function (data) {
                console.log('sendData:', data);
                roleId = data.forWardTaskVOList[0].roleDtoList[0].id;
                // 判断结点类型是否并行
                if (data.nodeType == 'ParallelGateway') {
                    isSelectAll = true;
                    //全选
                    col = [[
                        { type: 'checkbox', width: 50, fixed: 'left', LAY_CHECKED: true },
                        { field: 'activityName', title: '转发活动' },
                        { title: '参与角色', templet: '#roleTpl' },
                        { title: '参与人', templet: '#personTpl' }
                    ]]
                } else {
                    isSelectAll = false;
                    col = [[
                        { type: 'radio', width: 50, fixed: 'left' },
                        { field: 'activityName', title: '转发活动' },
                        { title: '参与角色', templet: '#roleTpl' },
                        { title: '参与人', templet: '#personTpl' }
                    ]]
                }
                forwardTask = data.forWardTaskVOList;
                if (forwardTask.length != 0) {
                    for (var i = 0, len = forwardTask.length; i < len; i++) {
                        var forwardDto = forwardTask[i].forwardTaskDto;
                        forwardTask[i].activityName = forwardDto.activityName;
                        if (i == 0) {
                            forwardTask[i].LAY_CHECKED = true;
                        }
                        var roleDtoList = forwardTask[i].roleDtoList;
                        if (roleDtoList.length != 0 && !isNullOrEmpty(roleDtoList[0]['id'])) {
                            var queryurl;
                            queryurl = getContextPath() + "/index/forward/users?roleId=" + forwardTask[i].roleDtoList[0]['id'] + '&&taskid=' + taskId;
                            $.ajax({
                                type: "GET",
                                async: false,
                                url: queryurl,
                                success: function (data) {
                                    forwardTask[i].personList = data;
                                    console.log('isNullOrEmpty(defaultUser):', isNullOrEmpty(defaultUser));
                                    if (isNullOrEmpty(defaultUser)) {
                                        renderZfTable(forwardTask, col);
                                    } else {
                                        col = [[
                                            { type: 'radio', width: 50, fixed: 'left' },
                                            { field: 'activityName', title: '转发活动' },
                                            { title: '参与角色', templet: '#roleTpl' },
                                            { title: '参与人', templet: '#personDefaultTpl' }
                                        ]];
                                        renderZfTable(forwardTask, col);
                                    }
                                }, error: function (e) {
                                    response.fail(e, '');
                                }
                            });
                        } else {
                            layer.msg('转发节点未配置角色，即将关闭转发窗口。');
                            setTimeout(function () { window.closeWin(); }, 1000);
                        }
                    }
                } else {
                    layer.msg('未配置转发节点，即将关闭转发窗口。');
                    setTimeout(function () { window.closeWin(); }, 1000);
                }
            }, error: function (e) {
                response.fail(e, '');
            }, complete: function () {
                removeModal();
            }
        });

        // 渲染表格
        function renderZfTable(data, col) {
            table.render({
                elem: '#checkboxTable',
                id: 'checkBoxTableId',
                cols: col,
                data: data,
                limit: 1000,
                done: function () {
                    form.render('select');
                }
            });
        }
        //监听角色选择
        form.on('select(roleFilter)', function (data) {
            roleId = data.value;
            console.log('data:', data);
            if (isNullOrEmpty(defaultUser)) {
                renderPerson(data.value, data.othis.parents('td').next().find('select'));
            }
        });

        //默认渲染第一个角色的，选择角色，重新渲染
        function renderPerson(roleId, $select) {
            queryurl = getContextPath() + "/index/forward/users?roleId=" + roleId + '&&taskid=' + taskId;
            $.ajax({
                type: "GET",
                url: queryurl,
                data: {
                    taskId: taskId
                },
                success: function (data) {

                    $select.html('<option value="">请选择</option>');
                    $select.append('<option value="-1" selected>全部</option>');
                    data.forEach(function (v) {
                        $select.append('<option value="' + v.id + '">' + v.alias + '</option>');
                    });
                    form.render('select');
                }, error: function (e) {
                    response.fail(e, '');
                }
            });

        }
        //监听参与人选择
        var userId = '-1';
        form.on('select(personFilter)', function (data) {
            console.log('userid', data.value);
            userId = data.value;
        });
        //点击转发
        $('.bdc-send-btn').on('click', function () {
            //$("a[name='bdc-sign-name']").click();
            var checkStatus = table.checkStatus('checkBoxTableId'); //idTest 即为基础参数 id 对应的值
            var selectData = checkStatus.data;
            console.log('selectData:', selectData);
            var opinion = $('.bdc-opinion').val();
            if (isSelectAll) {
                if (checkStatus.isAll) {
                    var $selectTr = $('.layui-table-main tr');
                    if ($selectTr.find('td:last-child .layui-anim-upbit .layui-this').length == checkStatus.data.length) {
                        var selectRoleIds, selectUserNames;
                        for (var i = 0; i < checkStatus.data.length; i++) {
                            var selectRoleId = $selectTr.find('td:nth-child(3) select option:selected').val();
                            var selectUserName = $selectTr.find('td:last-child select option:selected').val();
                            if (i = checkStatus.data.length - 1) {
                                selectRoleIds = selectRoleId;
                                selectUserNames = selectUserName;
                            } else {
                                selectRoleIds = selectRoleId + ",";
                                selectUserNames = selectUserName + ",";
                            }
                        }
                        addModel('转发中');
                        $.ajax({
                            type: "POST",
                            traditional: true,
                            //url: getContextPath() + "/rest/v1.0/workflow/process-instances/forward",
                            url: getContextPath() + "/index/forward",
                            data: {
                                "taskId": taskId,
                                "selectRoleIds": selectRoleIds,
                                "selectUserNames": selectUserNames,
                                "opinion": opinion
                            },
                            success: function (data) {
                                layer.msg('转发成功，即将关闭当前页。');
                                setTimeout(function () {

                                    if (currentId != null && isIndex == 'true') {
                                        closeWin();
                                        window.parent.renderTable('', '', currentId);
                                    } else if (currentId != null && isIndex == 'false') {
                                        window.renderTable('', '', currentId);
                                        window.parent.close();
                                    } else {
                                        window.parent.close();
                                    }
                                }, 1000);
                            }, error: function (e) {
                                response.fail(e, '');
                            }, complete: function () {
                                removeModal();
                            }
                        });
                    } else {
                        layer.msg('请选择参与人');
                    }
                } else {
                    layer.msg('请选择全部数据');
                }
            } else {
                //单选
                if (checkStatus.data.length == 1) {
                    var trIndex = $('.layui-form-radioed').parents('tr').data('index') + 1;
                    var $selectTr = $('.layui-table-main tr:nth-child(' + trIndex + ')');
                    var selectUserNames = $selectTr.find('td:last-child select option:selected').val();
                    if (isNullOrEmpty(selectUserNames)) {
                        layer.msg('请选择参与人');
                    } else {
                        var selectParams = selectData[0].forwardTaskDto;
                        var selectRoleIds = $selectTr.find('td:nth-child(3) select option:selected').val();
                        addModel('转发中');
                        $.ajax({
                            type: "POST",
                            traditional: true,
                            url: getContextPath() + "/index/forward",
                            data: {
                                "activityId": selectParams.activityId,
                                "activityName": selectParams.activityName,
                                "procDefId": selectParams.procDefId,
                                "roleIds": roleId,
                                "usernames": userId,
                                "taskId": taskId,
                                "selectRoleIds": roleId,
                                "selectUserNames": selectUserNames,
                                "opinion": opinion
                            },
                            success: function (data) {
                                layer.msg('转发成功，即将关闭当前页。');
                                setTimeout(function () {
                                    //首页的转发只刷新表格
                                    if (currentId != null && isIndex == 'true') {
                                        closeWin();
                                        window.parent.renderTable('', '', currentId);
                                    } else {
                                        //非首页的转发，刷新首页页面
                                        parent.reloadHome();
                                        window.parent.close();
                                    }
                                }, 1000);
                            }, error: function (e) {
                                response.fail(e, '');
                            }, complete: function () {
                                removeModal();
                            }
                        });
                    }
                } else {
                    layer.msg('请选择一条数据');
                }
            }
        });
        //点击取消
        $('.bdc-cancel-btn').on('click', function () {
            closeWin();
        });
    });
});