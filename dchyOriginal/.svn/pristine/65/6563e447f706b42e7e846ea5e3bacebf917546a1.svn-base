//根据url地址参数获取页面数据
var taskId = "";
var gzlslId = "";
var xmId = "";
var formKey = "";
var zt = '';
var jdmc = "";
var flag = true;
var zxzj = false;
var wdid = '';
var ywlx = '';
var chgcbh = '';
var gzldyid = '';

var sendFilters = "";

//子页面需要的getButtnAuth的参数
var sendParams = {
    hasSjcl: true
};
//受理编号
var slbh = '';
//审核信息请求参数
var shxxParams;
var addProgressModel;
var element;
//刷新首页数据
var reloadHome = function () {
    window.opener.location.reload();
}
//受理单页面获取目备案数据
var hqxmbasjData;
layui.use(['element', 'jquery', 'laydate', 'layer', 'tree', 'table', 'form', 'upload', 'laytpl', 'workflow', 'response'], function () {
    element = layui.element;
    var $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table,
        form = layui.form,
        laytpl = layui.laytpl,
        layer = layui.layer,
        workflow = layui.workflow,
        response = layui.response,
        upload = layui.upload;
    $(function () {
        //iframe高度自适应
        var parentNodeHeight = document.documentElement.clientHeight - 46;
        $('.layui-tab-item').css('height', parentNodeHeight + 'px');

        //根据url地址参数获取页面数据
        taskId = getIpHz().taskid;
        gzlslId = getIpHz().gzlslid;
        xmId = getIpHz().xmid;
        formKey = getIpHz().formKey;
        zt = getIpHz().type;
        wdid = getIpHz().wdid;
        ywlx = getIpHz().ywlx;


        // gzldyid	=

        var ymname; //页面名称
        //获取页面tab初始化数据
        var getformMunuData = [];
        //第一个页面url
        var firstUrl = '';
        //当前tab的url
        var sldUrl = $('#iframeBox').attr('src');
        //工程建设项目审批按钮权限
        var hasGcjsxmsp = false;

        //控制顶部操作按钮权限
        function buttnAuth(btn, boolean) {
            if (boolean) {
                $('.header-right-btn .bdc-' + btn + '-btn').css('display', 'block');
            } else {
                $('.header-right-btn .bdc-' + btn + '-btn').css('display', 'none');
            }
        };
        getReturnData("/index/getButtnAuth", {'taskid': taskId, 'zt': zt}, "GET", function (data) {
            jdmc = data.jdmc;
            sendParams = data;
            if (zt == 'db') {//已办状态 项目，所有按钮都不显示
                buttnAuth('print', data.hasPrint);
                buttnAuth('save', data.hasSave);
                buttnAuth('delete', data.hasDel);
                buttnAuth('cgwzxjc', data.hasClzj);
                buttnAuth('back', data.hasBack);
                buttnAuth('rk', data.hasRk);
                buttnAuth('hqxmbasj', data.hasHqxmbasj);
                buttnAuth('zjjgdc', data.hasFinish);
                //工程建设项目审批
                hasGcjsxmsp = data.hasGcjsxmsp;
                //办结      有办结没有转发，有转发没有办结
                if (data.hasFinish) {
                    $('.header-right-btn .bdc-finish-btn').css('display', 'block');
                    $('.header-right-btn .bdc-serviceSend-btn').css('display', 'none');
                } else {
                    $('.header-right-btn .bdc-finish-btn').css('display', 'none');
                    if(data.jdmc == "cgtj"){
                        $('.header-right-btn .bdc-serviceSend-btn').css('display', 'none');
                    } else {
                        $('.header-right-btn .bdc-serviceSend-btn').css('display', 'block');
                    }
                }
            }
        }, true);
        //获取受理编号
        getReturnData("/msurveyplat-server/rest/v1.0/query/sld/" + xmId, {}, "GET", function (data) {
            slbh = data.dchyCgglXmDO.slbh;
        }, false, 1);

        //渲染页面tab选项
        getReturnData("/index/getformMunu", {'taskid': taskId, 'zt': zt, 'wiid': gzlslId}, "GET", function (data) {
            getformMunuData = data;
            var getServiceHeaderTabTpl = top.serviceHeaderTabTpl.innerHTML,
                serviceTabBOx = top.document.getElementById('serviceHeaderTab');
            laytpl(getServiceHeaderTabTpl).render(data, function (html) {
                serviceTabBOx.innerHTML = html;
            });
            //初始加载第一个页面
            firstUrl = data.length > 0 ? (formatThirdPath(data[0].thirdPath) + '?taskid=' + taskId + '&gzlslid=' + gzlslId + '&xmid=' + xmId) : "";
            ymname = data.length > 0 ? (data[0].formStateName) : "";
            $('#iframeBox').attr('src', firstUrl);
        })
        //tab切换
        $('#serviceHeaderTab').on('click', 'li', function () {
            var _this = this;
            ymname = _this.innerHTML;
            var idx = $(this).index();
            var iframe_url = getformMunu(getformMunuData, idx);
            if (sldUrl == firstUrl) {
                var inputList = $('#iframeBox').contents().find('.td-item');
                var nullCount = 0;
                for (var i = 0; i < inputList.length; i++) {
                    $(inputList[i]).attr('lay-verify') == 'required' && $(inputList[i]).val() == '' ? nullCount++ : nullCount;
                }
                if (nullCount > 0) {
                    var index = layer.open({
                        type: 1,
                        title: '提示',
                        skin: 'bdc-small-tips',
                        area: ['320px'],
                        content: '必填项为空,若继续操作,当前数据丢失!',
                        btn: ['确定', '取消'],
                        btnAlign: 'c',
                        yes: function () {
                            layer.close(index);
                            changeTab(_this, iframe_url)
                        }
                    });
                    return false;
                }
                var element = $('#iframeBox').contents().find("#bdcSaveBtnSon")[0];
                element.click();
            }
            changeTab(_this, iframe_url)
        })

        function changeTab(_this, url) {
            $('#iframeBox').attr('src', url);
            $(_this).addClass('layui-this').siblings().removeClass('layui-this');
        }

        function getformMunu(data, index) {
            if (data[index].formStateName == "附件查看" || data[index].formStateName == "附件管理") {
                return formatThirdPath(data[index].thirdPath) + '&proid=' + slbh;
            } else {
                if (data[index].formStateName == "审核信息") {
                    buttnAuth('gcjsxmsp', hasGcjsxmsp);
                } else {
                    buttnAuth('gcjsxmsp', false);
                }
                return formatThirdPath(data[index].thirdPath) + '?taskid=' + taskId + '&gzlslid=' + gzlslId + '&xmid=' + xmId;
            }
        }

        //保存按钮
        $(".bdc-save-btn").click(function () {
            saveData();
            window.opener.location.reload();
        });
        //删除按钮
        $(".bdc-delete-btn").click(function () {
            baseLayer('确认删除', function () {
                addModel("删除中");
                getReturnData("/index/delTask", {'taskid': taskId, 'xmid': xmId}, "GET", function (data) {
                    removeModal();
                    if (data.status == 'ok') {
                        layer.msg('删除成功！');
                        window.opener.location.reload();
                        window.close();
                    }
                })
            });
        });
        //获取项目备案数据
        $(".bdc-hqxmbasj-btn").click(function () {
            var zfUrl = "../view/popup/hqxmbasj-page.html";
            removeModal();
            layer.open({
                type: 2,
                skin: 'layui-layer-lan',
                title: '选择项目（合同）编号',
                area: ['960px', '490px'],
                content: zfUrl
            });
        });
        //转发按钮
        $(".bdc-serviceSend-btn").click(function () {
            if (!saveData()) {
                return false
            }
            ;
            var zfUrl = "../view/send.html";
            removeModal();
            layer.open({
                type: 2,
                skin: 'layui-layer-lan',
                title: '任务转发',
                area: ['960px', '490px'],
                content: zfUrl + '?name=' + taskId + '&isIndex=' + true
            });
        });

        // 保存受理单数据
        function saveData() {
            var flag = true;
            var sldUrl = $('#iframeBox').attr('src');
            if (sldUrl == firstUrl) {
                var inputs = $('#iframeBox').contents().find("input[lay-verify='required']")
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].value == "") {
                        var nvalue = inputs[i].name;
                        flag = false;
                        $('#iframeBox').contents().find("input[name=" + nvalue + "]").addClass('emptyItem').parent().addClass('emptyInput');
                    }
                }
                var element = $('#iframeBox').contents().find("#bdcSaveBtnSon")[0];
                if (element != undefined) {
                    element.click();
                }
            } else {
                var element = $('#iframeBox').contents().find("#bdcSaveBtnSon")[0];
                if (element != undefined) {
                    element.click();
                }
            }
            return flag;
        }

        //办结按钮
        $(".bdc-finish-btn").click(function () {
            baseLayer('办结', function () {
                addModel("办结中");
                getReturnData("/index/turnTaskByWorkFlowInfo", {'taskid': taskId}, "GET", function (data) {
                    removeModal();
                    if (data.status == 'ok') {
                        layer.msg('办结成功！');
                        window.opener.location.reload();
                        window.close();
                    }
                })
            })
        });

        /**
         * 退回按钮
         * 1、退回
         * 2、退回时删除相应节点签名
         */
        var userId = localStorage.getItem('userId');
        shxxParams = {
            "dqjdmc": jdmc,
            "gzlslid": gzlslId,
            "sfzfhdqjdxx": false,
            "taskid": taskId,
            "xmid": xmId,
            "userid": userId,
            "shxxid": '',
            "signKey": jdmc
        }
        $(".bdc-back-btn").click(function () {
            baseLayer('退回', function () {
                addModel("退回中");
                var shxxid = '';
                var jdName = jdmc == 'cs' ? '初审' : jdmc == 'fs' ? '复审' : jdmc == 'bj' ? '办结' : '';
                if (workflow.allowBack(taskId)) {
                    getReturnData("/msurveyplat-server/rest/v1.0/shxx/list", JSON.stringify(shxxParams), "POST", function (data) {

                        data.forEach(function (item, index) {
                            if (item.jdmc == jdName) shxxid = item.shxxid;
                        })
                        deleteSign(shxxid);
                    }, false, 1);
                }
            })
        });

        //删除相应节点签名
        function deleteSign(shxxId) {
            getReturnData("/msurveyplat-server/rest/v1.0/shxx/sign/" + shxxId, {}, "delete", function (data) {
                removeModal();
                layer.msg('退回成功！');
                window.opener.location.reload();
                window.close();
            }, false, 1);
        }

        //入库
        $(".bdc-rk-btn").click(function () {
            chgcbh = parent.$('#iframeBox').contents().find("input[name='chxmbh']").eq(0).val();
            var getParams = parent.sendParams;
            gzldyid = getParams.gzldyid;

            baseLayer('入库', function () {
                addModel("入库中");
                getReturnData("/msurveyplat-server/v1.0/qualitycheck/rest/v1.0/qc/importdatabase/" + xmId + '/' + slbh + '/' + chgcbh + '/' + gzldyid, {}, "post", function (data) {
                    removeModal();
                    if (data != null) {
                        if (!data.success) {
                            layer.msg(data.message, {
                                time: 1500
                            });
                        } else if (data.result != null && data.result[0] != null) {
                            if (!data.result[0].success) {
                                layer.msg(data.result[0].message, {
                                    time: 1500
                                });
                            } else {
                                layer.msg("入库成功！", {
                                    time: 1500
                                });
                            }
                        }
                    }
                }, false, 1);
            })
        });
        //质检结果导出
        $(".bdc-zjjgdc-btn").click(function () {
            layer.open({
                type: 2,
                skin: 'layui-layer-rim',
                title: '导出质检结果',
                area: ['430px', '180px'],
                content: '../view/popup/zjjg-export.html',
            });
        });
        //工程建设项目审批
        $('.bdc-gcjsxmsp-btn').click(function () {
            var btn = $('#iframeBox').contents().find("#bdc-son-gcjsxmsp-btn")[0];
            btn.click();
        })
    })
});