layui.use(['element', 'jquery', 'table', 'form', 'upload', 'laytpl'], function() {
	var $ = layui.jquery,
		element = layui.element,
		table = layui.table,
		form = layui.form,
		laytpl = layui.laytpl,
		upload = layui.upload;
	//数据初始化渲染
	$(function() {

		var sl_data = {};
		$.ajax({
			type: "get",
			url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/query/sld/" + parent.xmId,
			async: false,
			success: function(data) {
				sl_data = JSON.parse(JSON.stringify(data));
				console.log('受理初始化数据：', data);
				//项目列表字段
				if($.isEmptyObject(data.dchyCgglXmDO)) {
					form.val("bdc-sl-form", {
						"slbh": '',
						"date": '',
						"slr": '',
						"dchybh": '',
						'jsdw': '',
						'chdw': '',
						'xmdz': ''
					});
				} else {
					var basicVal = data.dchyCgglXmDO;
					var getDate = '';
					if(basicVal.slsj != null) {
						getDate = (new Date(basicVal.slsj).Format("yyyy-MM-dd"));
					}
					form.val("bdc-sl-form", {
						"slbh": basicVal.slbh,
						"date": getDate,
						"slr": basicVal.slr,
						"dchybh": basicVal.dchybh,
						'jsdw': basicVal.jsdw,
						'xmdz': basicVal.xmdz,

					});
				}
				//申请人字段
				if(data.dchyCgglSqrDOList == null || data.dchyCgglSqrDOList.length == 0) {
					form.val("bdc-sl-form", {
						'dlrqm': '',
						'dlrzjhm': '',
						'dlrTel': '',
					});
				} else {
					var sqrqkVal = data.dchyCgglSqrDOList[0];
					form.val('bdc-sl-form', {
						'dlrqm': sqrqkVal.dlr,
						'dlrzjhm': sqrqkVal.dlrsfzh,
						'dlrTel': sqrqkVal.dlrlxdh,
						'jsdwlxfs': sqrqkVal.sqrlxdh,
					})
				}
				//测绘单位字段
				if(data.dchyZdChdwDo.length > 0) {
					for(var i = 0; i < data.dchyZdChdwDo.length; i++) {
						var op = data.dchyZdChdwDo[i];
						$('#chdw-select-box .chdw-select').append('<option value="' + op.dm + '">' + op.mc + '</option>');
					}
					form.val("bdc-sl-form", {
						'chdwName': data.dchyCgglXmDO.chdw
					})

					var chdwTel = ''
					for(var i = 0; i < sl_data.dchyZdChdwDo.length; i++) {
						var op = sl_data.dchyZdChdwDo[i];
						if(data.dchyCgglXmDO.chdw == op.dm) {
							chdwTel = op.lxdh
						}
					}
					form.val("bdc-sl-form", {
						'chdwTel': chdwTel
					})

				} else {
					form.val("bdc-sl-form", {
						'chdwName': ''
					})
				}
			}
		})
		//监听测绘单位下拉选项，测绘单位、测绘单位联系电话 联动
		form.on('select(chdwSelect)', function(data) {
			var chdwTel = '';
			if(data.value) {
				for(var i = 0; i < sl_data.dchyZdChdwDo.length; i++) {
					var op = sl_data.dchyZdChdwDo[i];
					if(data.value == op.dm) {
						chdwTel = op.lxdh
					}
				}
			}
			form.val("bdc-sl-form", {
				'chdwTel': chdwTel
			})
		});

		//收件材料数据表格
		var sjclData = table.render({
			elem: '#sjclTable',
			method: 'get',
			url: getContextPath(1) + '/msurveyplat-server/rest/v1.0/query/sld/' + sl_data.dchyCgglXmDO.xmid,
			cellMinWidth: 80,
			toolbar: '#sjclBtnTpl',
			defaultToolbar: [],
			loading: true,
			cols: [
				[
					{ type: 'checkbox', width: 50, align: 'center' },
					{ type: 'numbers', title: '序号', width: 50, align: 'center' },
					{ field: 'clmc', title: '材料名称', align: 'center', edit: 'text' },
					{ field: 'fs', title: '份数', width: 70, align: 'center', edit: 'text' },
					{ field: 'ys', title: '页数', width: 70, align: 'center', edit: 'text' },
					{ field: '', title: '类型', width: 300, templet: '#selectTpl', align: 'center' },
					{ fixed: 'right', title: '操作', width: 110, templet: '#uploadTpl', align: 'center',}
				]
			],
			parseData: function(res) {
				//收件类型下拉选项
				res.dchyCgglSjclDOList.forEach(function(item) {
					item.fjlxConfig = res.dchyZdSjlxDOList;
					$('.sjclType').val(item.sjlx);
				});
				return {
					"code": 0, //解析接口状态
					"msg": '1', //解析提示文本
					"count": res.dchyCgglSjclDOList.length, //解析数据长度
					"data": res.dchyCgglSjclDOList //解析数据列表
				};
			},
			done: function () {
                // 根据节点控制上传附件权限
                if (parent.jdmc == 'cs' || parent.jdmc == 'fs') {
                    $("#sjcl-add-btn").attr("lay-event","forbidden");
                    $("#sjcl-delete-btn").attr("lay-event","forbidden");
                    $("#sjcl-plsc-btn").attr("lay-event","forbidden");
                    $(".bdc-upload-btn").attr("disabled","disabled");
                    $(".bdc-number-table").find('td').data('edit', false);
                    $('.sjclType').attr('disabled', 'disabled');
                    form.render('select');
                    $(".bdc-number-table").find('div').off("click");
                }
            }
		});
		//      监听下拉选项事件
		//		form.on('select(sjclType)', function(obj) {
		//			alert(obj.value);
		//		});

		//提交保存
		form.on('submit(bdc-save-btn-son)', function(data) {
            if(sl_data.dchyCgglSjxxDO == null || sl_data.dchyCgglShxxDO == null) {
                sl_data.dchyCgglSjxxDO = {};
                sl_data.dchyCgglSjxxDO.xmid = sl_data.dchyCgglXmDO.xmid;
            }
            if(sl_data.dchyCgglShxxDO == null) {
                sl_data.dchyCgglShxxDO = {};
                sl_data.dchyCgglShxxDO.xmid = sl_data.dchyCgglXmDO.xmid;
            }
            if(sl_data.dchyCgglSqrDOList == null || sl_data.dchyCgglSqrDOList.length === 0) {
                sl_data.dchyCgglSqrDOList = [];
                sl_data.dchyCgglSqrDOList.push({
                    'xmid': sl_data.dchyCgglXmDO.xmid,
                    'dlr': data.field.dlrqm,
                    'dlrsfzh': data.field.dlrzjhm,
                    'dlrlxdh': data.field.dlrTel,
                    'sqrlxdh': data.field.jsdwlxfs,
                    'jsdw': data.field.jsdw,
                    'xmdz': data.field.xmdz,
                })
            } else {
                sl_data.dchyCgglSqrDOList[0].dlr = data.field.dlrqm;
                sl_data.dchyCgglSqrDOList[0].dlrsfzh = data.field.dlrzjhm;
                sl_data.dchyCgglSqrDOList[0].dlrlxdh = data.field.dlrTel;
                sl_data.dchyCgglSqrDOList[0].sqrlxdh = data.field.jsdwlxfs;
                sl_data.dchyCgglSqrDOList[0].jsdw = data.field.jsdw;
                sl_data.dchyCgglSqrDOList[0].xmdz = data.field.xmdz;
                sl_data.dchyCgglSqrDOList[0].chdwdm = data.field.chdwName;
            }
            sl_data.dchyCgglXmDO.slbh = data.field.slbh;
            sl_data.dchyCgglXmDO.slsj = data.field.date;
            sl_data.dchyCgglXmDO.slr = data.field.slr;
            sl_data.dchyCgglXmDO.jsdw = data.field.jsdw;
            sl_data.dchyCgglXmDO.chdw = data.field.chdw;
            sl_data.dchyCgglXmDO.xmdz = data.field.xmdz;
            sl_data.dchyCgglXmDO.dchybh = data.field.dchybh;
            sl_data.dchyCgglXmDO.chdw = data.field.chdwName;

            console.log('slData:', sl_data);
            parent.pagrSubmit(sl_data);

            $.ajax({
                type: "post",
                url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/save/sld/",
                //url: "http://192.168.50.38:8085/msurveyplat-server/rest/v1.0/save/sld/",
                data: JSON.stringify(sl_data),
                dataType: "json",
                contentType: 'application/json;charset=utf-8',
                async: false,
                success: function(res) {
                    console.log('res:', res);
                    layer.msg('保存成功')
                },
                error: function(err) {
                    layer.msg('保存失败')
                }
            })
			return false;
		});
		//监听收件材料表格上传事件
		var xmid = sl_data.dchyCgglXmDO.xmid;

        table.on('tool(sjclTable)', function(obj) {
            var data = obj.data; //获得当前行数据
            if(obj.event === 'openUploadPage') { //查看
                console.log('当前行数据:', data);
                var clmc = data.clmc;
                $('#_url').val(xmid + '|' + clmc);
                window.open('../view/sccl-page.html');
            }
        })

        //监听表格工具栏事件
        table.on('toolbar(sjclTable)', function(obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            console.log('checkStatus：', checkStatus);
            var data = []; //定义一个空数组,用来存储之前编辑过的数据已经存放新数据
            var tableData = layui.table.cache.sjclTable;
            switch(obj.event) {
            	//增加
                case 'add':
                    for(var i = 0; i < tableData.length; i++) {
                        data.push(tableData[i]); //将之前的数组备份
                    }
                    data.push({
                        'clmc': '',
                        'count': '',
                        'page-num': '',
                        'type': ''
                    });

                    table.reload("sjclTable", {
                        data: data // 将新数据重新载入表格
                    })
                    break;
                  //删除
                case 'del':
                    if(checkStatus.data.length === 0) {
                        layer.msg('请选择一条数据', {
                            time: 1500
                        });
                        return;
                    }
                    layer.open({
                        type: 1,
                        title: '提示',
                        skin: 'bdc-small-tips bdc-zf-tips',
                        area: ['320px', '150px'],
                        content: '是否确认删除？',
                        btn: ['确定', '取消'],
                        btnAlign: 'c',
                        yes: function() {
                            layer.closeAll();
                        }
                    });
                    break;
                    //批量上传
                case 'plsc':
                    layer.open({
                        type: 1,
                        title: '提示',
                        skin: 'bdc-small-tips bdc-zf-tips',
                        area: ['320px', '150px'],
                        content: '确认上传？',
                        btn: ['确定', '取消'],
                        btnAlign: 'c',
                        yes: function() {
                            layer.closeAll();
                            $('#_url').val(xmid+'|undefined');
                            window.open('../view/sccl-page.html');
                        }
                    });
                    break;
            };
        });

	});
});