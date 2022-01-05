//项目ID
var xmid = "";
var reloadTable;
var form ;
// 受理页面数据
var sl_data = {};
layui.use(['element', 'jquery', 'table', 'form', 'upload', 'laytpl', 'laydate', 'layer'], function () {
	var $ = layui.jquery,
		element = layui.element,
		table = layui.table,
		form = layui.form,
		laytpl = layui.laytpl,
		laydate = layui.laydate,
		layer = layui.layer,
		upload = layui.upload;

	$(function () {
		//日期选择
		laydate.render({
			elem: '#sgxkzqdrq'
			, done: function (value, date, endDate) {
				if(value) $('#sgxkzqdrq').parent().removeClass('emptyInput')
			}
		});
		laydate.render({
			elem: '#ghxkzqdrq'
			, done: function (value, date, endDate) {
				if(value) $('#ghxkzqdrq').parent().removeClass('emptyInput')
			}
		});
		//获取父页面的参数
		var getParams = parent.sendParams;
		//受理编号
		var slbh = "";
		//收件材料表格数据
		var sjclTableData = [];
		//获取必填字段
		$.ajax({
			type: 'post',
			url: getContextPath() + "/msurveyplat-server/rest/v1.0/check/bdbtzd/" + getParams.gzldyid + '/' + getParams.gzljdid,
			dataType: "json",
			success: function (data) {
				var html = '<i class="required-icon">*</i>';
				if (data.sl_page) {
					data.sl_page.forEach(function (v) {
						var inputList = $('.bdc-table').find('.td-item');
						for (var i = 0; i < inputList.length; i++) {
							var inputItem = inputList[i];
							if ($(inputItem).attr('name') == v.sjkbzd.toLowerCase()) {
								$(inputItem).attr('lay-verify', 'required').parent().prev().prepend(html);
							}
						}
					})
				}
			}
		})
		//数据初始化渲染
		$.ajax({
			type: "get",
			url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/query/sld/" + parent.xmId,
			async: false,
			success: function (data) {
				sl_data = JSON.parse(JSON.stringify(data));
				xmid = sl_data.dchyCgglXmDO.xmid;
				slbh = sl_data.dchyCgglXmDO.slbh;
				var xmData = data.dchyCgglXmDO;
				var jsdwData = data.dchyCgglSqrDOList;
				var chdwData = data.dchyZdChdwDo;
				var optionList = data.dchyZdSjlxDOList;
				//项目字段
				if (xmData != null) {
					xmData.slsj = xmData.slsj == null ? "" : (new Date(xmData.slsj).Format("yyyy-MM-dd"));
					xmData.ghxkzqdrq = xmData.ghxkzqdrq == null ? "" : (new Date(xmData.ghxkzqdrq).Format("yyyy-MM-dd"));
					xmData.sgxkzqdrq = xmData.sgxkzqdrq == null ? "" : (new Date(xmData.sgxkzqdrq).Format("yyyy-MM-dd")),
						form.val("bdc-sl-form", xmData);
				}
				//申请人字段
				if (jsdwData != null && jsdwData.length > 0) {
					var jsdw = jsdwData[0];
					form.val('bdc-sl-form', jsdw)
				}
				//测绘单位字段
				// chdwData.push({
				// 	dm:'1',mc:'111111'
				// })
				if (chdwData.length > 0) {
					chdwData.forEach(function (op, index) {
						$('#chdw-select-box .chdw-select').append('<option value="' + op.dm + '">' + op.mc + '</option>');
					})
					form.render('select');
				}
				var chdwselect = chdwData.filter(function (item) {
					return item.dm == xmData.chdw
				})[0];
				if (chdwselect) {
					form.val("bdc-sl-form", {
						'chdw': chdwselect.dm,
						'lxdh': chdwselect.lxdh,
					})
				}
			}
		})
        var obj = {'chxmbh':'江阴市交通运输局'}
		//获取项目备案数据
		$('.bdc-hqxmbasj-btn-son').click(function () {

			$(".td-item").trigger("input");
			form.val("bdc-sl-form", obj
			);
			return false;
		})
		//监听测绘单位下拉选项，测绘单位、测绘单位联系电话 联动
		form.on('select(chdwSelect)', function (data) {
			var lxdh = '';
			if (data.value) {
				lxdh = sl_data.dchyZdChdwDo.filter(function (op, index) {
					return data.value == op.dm
				})[0].lxdh;
			}
			form.val("bdc-sl-form", {
				'lxdh': lxdh
			})
		});

		//收件材料数据表格
		table.render({
			elem: '#sjclTable',
			method: 'get',
			url: getContextPath(1) + '/msurveyplat-server/rest/v1.0/query/sld/' + xmid,
			cellMinWidth: 80,
			toolbar: '#sjclBtnTpl',
			defaultToolbar: [],
			loading: true,
			cols: [[
				{ type: 'checkbox', width: 50, align: 'center' },
				{ type: 'numbers', title: '序号', width: 50, align: 'center' },
				{ field: 'clmc', title: '材料名称', align: 'center', edit: 'text' },
				{ field: 'fs', title: '份数', width: 70, align: 'center', edit: 'text' },
				{ field: 'ys', title: '页数', width: 70, align: 'center', edit: 'text' },
				{ field: '', title: '类型', width: 300, templet: '#selectTpl', align: 'center' },
				{ fixed: 'right', title: '操作', width: 110, templet: '#uploadTpl', align: 'center', }
			]],
			parseData: function (res) {
				//收件类型下拉选项
				res.dchyCgglSjclDOList.forEach(function (item) {
					item.fjlxConfig = res.dchyZdSjlxDOList;
					$('#sjclType').val(item.sjlx);
					form.render('select', 'sjclType');
				});
				return {
					"code": 0,
					"msg": '1',
					"count": res.dchyCgglSjclDOList.length,
					"data": res.dchyCgglSjclDOList
				};
			},
			done: function () {
				// 根据节点控制上传附件权限
				if (!getParams.hasSjcl) {
					$("#addBtn").attr("lay-event", "forbidden");
					$("#deleteBtn").attr("lay-event", "forbidden");
					$("#batchUploadBtn").attr("lay-event", "forbidden");
					$(".bdc-upload-btn").attr("disabled", "disabled");
					$(".bdc-number-table").find('td').data('edit', false);
					$('.sjcl-type').attr('disabled', 'disabled');
					form.render('select');
					$(".bdc-number-table").find('div').off("click");
				}
			}
		});
		//焦点事件
		$(".td-item").blur(function () {
			var v = $(this).val();
			if (v == '' && $(this).hasClass('emptyItem')) {
				$(this).parent().addClass('emptyInput');
			}else{
				$(this).parent().removeClass('emptyInput');
			}
		})
		//提交保存
		form.on('submit(bdc-save-btn-son)', function (data) {
			addModel("保存中");
			sl_data.dchyCgglSjclDOList = sjclTableData;// 收件材料表格数据
			var sjxx = sl_data.dchyCgglSjxxDO;
			var shxx = sl_data.dchyCgglShxxDO;
			var xm = sl_data.dchyCgglXmDO;
			var chdw = sl_data.dchyZdChdwDo;
			var sqr = sl_data.dchyCgglSqrDOList;
			var newData = data.field;
			if (sjxx == null) {
				sjxx = {};
				sjxx.xmid = xmid;
			}
			if (shxx == null) {
				shxx = {};
				shxx.xmid = xmid;
			}
			if (sqr == null || sqr.length === 0) {
				sqr = [];
				sqr.push({
					sqrid: "",
					xmid: xmid,
					sqr: newData.sqr,
					sqrlxdh: newData.sqrlxdh,
					dlr: "",
					dlrsfzh: "",
					dlrlxdh: "",
					chdwdm: '',
					chdwdlr: newData.chdwdlr,
					bz: '',
				})
			} else {
				sqr[0].sqr = newData.sqr;
				sqr[0].sqrlxdh = newData.sqrlxdh;
				sqr[0].chdwdlr = newData.chdwdlr;
			}
			xm.xmmc = newData.xmmc;
			xm.jsdw = newData.jsdw;
			xm.xmdz = newData.xmdz;
			xm.chdw = newData.chdw;
			xm.dchybh = newData.dchybh;
			xm.chxmbh = newData.chxmbh;
			xm.ghxkzh = newData.ghxkzh;
			xm.ghxkmj = newData.ghxkmj;
			xm.ghxkfw = newData.ghxkfw;
			xm.ghxkzqdrq = newData.ghxkzqdrq;
			xm.sgxkzh = newData.sgxkzh;
			xm.sgxkmj = newData.sgxkmj;
			xm.sgxkfw = newData.sgxkfw;
			xm.sgxkzqdrq = newData.sgxkzqdrq;
			xm.ybbdcdydm = newData.ybbdcdydm
			sl_data.dchyCgglSqrDOList = sqr;
			sl_data.gzldyid  = getParams.gzldyid;


			$.ajax({
				type: "post",
				url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/save/sld/",
				//url: "http://192.168.50.69/msurveyplat-server/rest/v1.0/save/sld/",
				data: JSON.stringify(sl_data),
				dataType: "json",
				contentType: 'application/json;charset=utf-8',
				async: false,
				success: function (res) {
					removeModal();
					if (parent.flag) {
						layer.msg('保存成功')
					}
				},
				error: function (err) {
					if (parent.flag) {
						layer.msg('保存失败')
					}
				}
			})
			return false;
		});

		//单元格编辑事件
		table.on('edit(sjclTable)', function (obj) {
			//编辑后当前表格数据
			var tableData = layui.table.cache.sjclTable;
			sjclTableData = tableData;
		});
		var rowData = {};
		//监听收件材料表格收件类型下拉框
		form.on('select(sjclType)', function (data) {
			var tableData = layui.table.cache.sjclTable;
			tableData.forEach(function (item) {
				if (item.sjclid == rowData.sjclid) {
					item.sjlx = data.value;
				}
			})
			sjclTableData = tableData;
			form.render('select', 'sjclType');
		});
		//监听行单击事件
		table.on('row(sjclTable)', function (obj) {
			rowData = JSON.parse(JSON.stringify(obj.data));
		});
		//监听收件材料表格上传事件
		table.on('tool(sjclTable)', function (obj) {
			var data = obj.data; //获得当前行数据
			if (obj.event === 'upload') {
				var sjclId = data.sjclid;
				var sfpl = false;
				sjclUpload(slbh, xmid, sjclId, sfpl);
			}
		})
		//监听表格工具栏事件
		table.on('toolbar(sjclTable)', function (obj) {
			var checkStatus = table.checkStatus(obj.config.id);
			var selectItemIds = [];
			checkStatus.data.forEach(function (item) {
				selectItemIds.push(item.sjclid);
			})
			var tableData = layui.table.cache.sjclTable;
			switch (obj.event) {
				//增加
				case 'add':
					var newRowData = {
						'clmc': '',
						'count': '',
						'page-num': '',
						'type': ''
					}
					tableData.push(newRowData);
					layer.open({
						type: 2,
						skin: 'layui-layer-rim',
						title: '新增收件材料',
						area: ['430px', '360px'],
						content: '../view/module/addSjcl.html',
					});
					break;
				//删除
				case 'del':
					if (checkStatus.data.length === 0) {
						layer.msg('请选择一条数据', {
							time: 1500
						});
						return;
					}
					baseLayer('确认删除', function () {
						addModel("删除中");
						$.ajax({
							type: "post",
							url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/delete/sjcl",
							data: JSON.stringify(selectItemIds),
							dataType: "json",
							contentType: 'application/json;charset=utf-8',
							async: false,
							success: function (res) {
								removeModal();
								layer.msg('删除成功');
								reloadTable()
							},
							error: function (err) {
								layer.msg('删除失败')
							}
						})
					});
					break;
				//批量上传
				case 'plsc':
					baseLayer('上传', function () {
						var sjclId = 'sjdid';
						var sfpl = true;
						sjclUpload(slbh, xmid, sjclId, sfpl);
					});
					break;
			};
		});
		reloadTable = function () {
			table.reload("sjclTable");
		}
		//材料上传
		function sjclUpload(slbh, xmid, sjclId, sfpl) {
			$.ajax({
				type: 'post',
				url: getContextPath() + "/msurveyplat-server/rest/v1.0/getUploadParam/sld/" + slbh + '/' + xmid + '/' + sjclId + '/' + sfpl,
				dataType: "json",
				async: false,
				success: function (data) {
					$('#_url').val(data.fileCenterUrl + '|' + data.nodeId + '|' + data.sfjcwjwzx + '|' + data.chlx);
					window.open('../view/sccl-page.html');
				},
				error: function (err) {
					layer.msg('上传失败');
				}
			})
		}


	});
});