layui.use(['element', 'form', 'jquery', 'laytpl'], function () {
	var element = layui.element;
	var $ = layui.jquery;
	var form = layui.form;
	var laytpl = layui.laytpl;
	//	form.render(); // 加入这一句
	$(function () {
		if (parent.jdmc == "fs") {
			$('.jgpd').show();
			$('.gcjsxmsp-table').show();
		}
		if (parent.jdmc == "fs" || parent.jdmc == "bj") {
			$('.gcjsxmsp-table').show();
		}
		var params = JSON.parse(JSON.stringify(parent.shxxParams));
		//签名初始化数据
		init(params);

		//签名
		$('.shxx-table').on('click', '.bdc-sign-btn', function () {
			//addModel();
			var shyj = $(this).parents('tr').find('.opinion').val();
			params.shyj = shyj;
			var shxxid = $(this).attr('data-id');
			params.shxxid = shxxid;
			var _this = this;
			$.ajax({
				type: "put",
				url: getContextPath() + "/msurveyplat-server/rest/v1.0/shxx/sign",
				data: JSON.stringify(params),
				dataType: "json",
				contentType: 'application/json;charset=utf-8',
				async: true,
				success: function (res) {
					//setTimeout(function () {
					//removeModal();
					$(_this).parent().prev().find('img').attr("src", res.qmtpdz);
					$(_this).parent().parent().nextAll('.date-item').html(res.qmsj);
					//}, 2000)
				},
				error: function (err) {
					layer.msg('签名失败！')
				}
			})
		})
		//删除签名
		$('.shxx-table').on('click', '.bdc-delete-btn', function () {
			var shxxid = $(this).prev().attr('data-id');
			var _this = this;
			$.ajax({
				type: "delete",
				url: getContextPath() + "/msurveyplat-server/rest/v1.0/shxx/sign/" + shxxid,
				dataType: "json",
				contentType: 'application/json;charset=utf-8',
				async: false,
				success: function (res) {
					$(_this).parent().prev().find('img').attr("src", res.qmtpdz);
					$(_this).parent().parent().nextAll('.date-item').html(res.qmsj);
				},
				error: function (err) {
					layer.msg('失败')
				}
			})
		})

		//复审时转发
		$('#bdc-shztSend-btn-son').on('click', function () {
			var data = form.val('shjg');
			console.log(data);
			if (parent.jdmc == "fs") {
				$.ajax({
					url: getContextPath() + "/rest/v1.0/updateRemark?wiid=" + parent.gzlslId + '&&shzt=' + data.ispass,
					dataType: "json",
					async: false,
					success: function (data) {
						layer.msg('转发成功');
					},
					error: function (err) {
						layer.msg('转发失败')
					}
				})
			}
		})
		//工程建设项目审批
		$('#bdc-son-gcjsxmsp-btn').click(function () {
			var _date = (new Date()).Format("yyyy-MM-dd");
			setTimeout(function () {
				$('.audit-result').val('审核通过');
				$('.audit-department').text('南京市建委');
				$('.audit-date').text(_date);

				var data = {
					shyj: $('.audit-result').val(),
					shr: $('.audit-department').text(),
					shsj: $('.audit-date').text(),
					xmid: params.xmid
				}
				$.ajax({
					type: "post",
					url: getContextPath() + "/msurveyplat-server/rest/v1.0/shxx/gcjssp",
					data: JSON.stringify(data),
					dataType: "json",
					contentType: 'application/json;charset=utf-8',
					async: true,
					success: function (res) {

					},
					error: function (err) {
						layer.msg('审核失败！')
					}
				})
			}, 1500);
		})
		//初始化
		function init(params) {
			$.ajax({
				type: "post",
				url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/shxx/list",
				data: JSON.stringify(params),
				dataType: "json",
				contentType: 'application/json;charset=utf-8',
				async: false,
				success: function (res) {
					//初始化渲染审核签名模板
					var getReviewTpl = reviewTpl.innerHTML,
						shxxTable = document.getElementById('shxxTable');
					laytpl(getReviewTpl).render(res, function (html) {
						shxxTable.innerHTML = html;
					})
					/**
					 * 签名可控权限
					 * 1.复审节点不可修改初审节点签名及审核信息
					 * 2.办结节点不可修改任何节点签名及审核信息
					 */
					if (res != null && res.length > 1) {
						if (parent.jdmc == 'bj') {
							$('#shxxTable .layui-table').find('.opinion').attr('readonly', true);
							$('#shxxTable .layui-table').find('.bdc-sign-btn').css({ "cursor": "not-allowed", 'pointer-events': 'none' });
							$('#shxxTable .layui-table').find('.bdc-delete-btn').css({ "cursor": "not-allowed", 'pointer-events': 'none' });
						}
						$('#shxxTable .layui-table:first-child').find('.opinion').attr('readonly', true);
						$('#shxxTable .layui-table:first-child').find('.bdc-sign-btn').css({ "cursor": "not-allowed", 'pointer-events': 'none' });
						$('#shxxTable .layui-table:first-child').find('.bdc-delete-btn').css({ "cursor": "not-allowed", 'pointer-events': 'none' });
					}
				}
			})
			//获取工程建设项目审批数据
			$.ajax({
				type: "post",
				url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/shxx/gcjssp/" + params.xmid,
				async: false,
				success: function (res) {
					//渲染模板
					var date = res.spsj == null ? '' : (new Date(res.spsj)).Format("yyyy-MM-dd");
					$('.gcjsxmsp-table').find('.audit-result').val(res.spyj || '');
					$('.gcjsxmsp-table').find('.audit-department').text(res.spbm || '');
					$('.gcjsxmsp-table').find('.audit-date').text(date);
					if (res != null && parent.jdmc == 'bj') {
						$('.gcjsxmsp-table').find('.audit-result').attr('readonly', true);
					}
				}
			})
		}
	})
});