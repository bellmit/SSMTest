//根据url地址参数获取页面数据
var taskId = "";
var gzlslId = "";
var xmId = "";
var formKey = "";
var zt = '';
var jdmc = "";
var flag = true;
var zxzj = false;
var hasSjcl = true;
var wdid = '';
//审核信息请求参数
var shxxParams;
//刷新首页数据
var reloadHome = function () {
	window.opener.location.reload();
}
//受理单页面获取项目备案数据
var hqxmbasjData;
layui.use(['element', 'jquery', 'laydate', 'layer', 'tree', 'table', 'form', 'upload', 'laytpl', 'workflow'], function () {
	var $ = layui.jquery,
		element = layui.element,
		laydate = layui.laydate,
		table = layui.table,
		form = layui.form,
		laytpl = layui.laytpl,
		layer = layui.layer,
		workflow = layui.workflow,
		upload = layui.upload;
	$(function () {
		//iframe高度自适应
		var parentNodeHeight = document.documentElement.clientHeight - 46;
		$('.layui-tab-item').css('height', parentNodeHeight + 'px');
		//受理编号
		var slbh = '';
		//根据url地址参数获取页面数据
		taskId = getIpHz().taskid;
		gzlslId = getIpHz().gzlslid;
		xmId = getIpHz().xmid;
		formKey = getIpHz().formKey;
		zt = getIpHz().type;
		wdid = getIpHz().wdid;
		//获取页面tab初始化数据
		var getformMunuData = [];
		//按钮权限
		function buttnAuth(btn, boolean) {
			if (boolean) $('.header-right-btn .bdc-' + btn + '-btn').css('display', 'block');
		}
		//控制顶部操作按钮权限
		getReturnData("/index/getButtnAuth", { 'taskid': taskId, 'zt': zt }, "GET", function (data) {
			//console.log('操作按钮数据：', data);
			jdmc = data.jdmc;
			hasSjcl = data.hasSjcl;
			if (zt != 'yb') {//已办列表，所有按钮都不显示
				//打印
				buttnAuth('print', data.hasPrint);
				//保存
				buttnAuth('save', data.hasSave);
				//删除
				buttnAuth('delete', data.hasDel);
				//成果完整性检查
				buttnAuth('cgwzxjc', data.hasClzj);
				//退回
				buttnAuth('back', data.hasBack);
				//入库
				buttnAuth('rk', data.hasRk);
				//获取项目备案数据
				buttnAuth('hqxmbasj', data.hasHqxmbasj);
				//办结      有办结没有转发，有转发没有办结
				if (data.hasFinish) {
					$('.header-right-btn .bdc-finish-btn').css('display', 'block');
					$('.header-right-btn .bdc-serviceSend-btn').css('display', 'none');
				} else {
					$('.header-right-btn .bdc-finish-btn').css('display', 'none');
					$('.header-right-btn .bdc-serviceSend-btn').css('display' , 'none');
				}
			}
		}, true);
		$.ajax({
			type: "get",
			url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/query/sld/" + xmId,
			async: false,
			success: function (data) {
				slbh = data.dchyCgglXmDO.slbh;
			}
		});
		//渲染页面tab选项
		getReturnData("/index/getformMunu", { 'taskid': taskId, 'zt': zt, 'wiid': gzlslId }, "GET", function (data) {
			//console.log('受理数据：', data)
			getformMunuData = data;
			var getServiceHeaderTabTpl = top.serviceHeaderTabTpl.innerHTML,
				serviceTabBOx = top.document.getElementById('service-header-tab-box');
			laytpl(getServiceHeaderTabTpl).render(data, function (html) {
				serviceTabBOx.innerHTML = html;
			});
			//初始加载第一个页面
			var first_url = data[0].thirdPath + '?taskid=' + taskId + '&gzlslid=' + gzlslId + '&xmid=' + xmId;
			$('#sl-iframe').attr('src', first_url);
		})
		//监听tab切换
		element.on('tab(service-header-tab)', function (d) {
			var iframe_url;
			if (getformMunuData[d.index].formStateName == "附件查看" || getformMunuData[d.index].formStateName == "附件管理") {
				iframe_url = getformMunuData[d.index].thirdPath + '&proid=' + slbh;
			} else {
				iframe_url = getformMunuData[d.index].thirdPath + '?taskid=' + taskId + '&gzlslid=' + gzlslId + '&xmid=' + xmId;
			}
			$('#sl-iframe').attr('src', iframe_url);
			var element = $('#sl-iframe').contents().find("#bdc-save-btn-son")[0];
			if (element != undefined) {
				element.click();
			}
			if (d.index > 0) {
				flag = false;
			} else if (d.index = 0) {
				flag = true;
			}
		});
		//保存按钮
		$(".bdc-save-btn").click(function () {
			$('#sl-iframe').contents().find("#bdc-save-btn-son")[0].click();
			window.opener.location.reload();
		});
		//删除按钮
		$(".bdc-delete-btn").click(function () {
			baseLayer('确认删除', function () {
				addModel("删除中");
				getReturnData("/index/delTask", { 'taskid': taskId, 'xmid': xmId }, "GET", function (data) {
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
			$('#sl-iframe').contents().find(".bdc-hqxmbasj-btn-son")[0].click();
		});
		//转发按钮
		$(".bdc-serviceSend-btn").click(function () {
			var element = $('#sl-iframe').contents().find("#bdc-save-btn-son")[0];
			if (element != undefined) {
				element.click();
			}
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
		//办结按钮
		$(".bdc-finish-btn").click(function () {
			baseLayer('办结', function () {
				addModel("办结中");
				getReturnData("/index/turnTaskByWorkFlowInfo", { 'taskid': taskId }, "GET", function (data) {
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
			"shxxid": ''
		}
		$(".bdc-back-btn").click(function () {
			baseLayer('退回', function () {
				var shxxid = '';
				var jdName = jdmc == 'cs' ? '初审' : jdmc == 'fs' ? '复审' : jdmc == 'bj' ? '办结' : '';
				if (workflow.allowBack(taskId)) {
					$.ajax({
						type: "post",
						url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/shxx/list",
						data: JSON.stringify(shxxParams),
						dataType: "json",
						contentType: 'application/json;charset=utf-8',
						async: false,
						success: function (res) {
							res.forEach(function (item, index) {
								if (item.jdmc == jdName) shxxid = item.shxxid;
							})
							deleteSign(shxxid);
						}
					})

				}
			})
		});
		//删除相应节点签名
		function deleteSign(shxxId) {
			$.ajax({
				type: "delete",
				url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/shxx/sign/" + shxxId,
				dataType: "json",
				contentType: 'application/json;charset=utf-8',
				async: false,
				success: function (res) {
					layer.msg('退回成功！');
					window.opener.location.reload();
					window.close();
				},
				error: function (err) {
				}
			})
		}
		//成果完整性检查
		$(".bdc-cgwzxjc-btn").click(function () {
			baseLayer('开始检查', function () {
				layer.msg('成果完整性检查完成', {
					time: 1500
				});
			})
		});
	})
});