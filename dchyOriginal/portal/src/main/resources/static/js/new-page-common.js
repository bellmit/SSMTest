/**
 * Created by Ypp on 2020/1/16.
 * 任务办理各版本通用js
 */
var $, response,
	processInstanceType = '',
	print = false;
layui.use(['jquery', 'element', 'form', 'laytpl', 'layer', 'table', 'workflow', 'moduleAuthority', 'response'], function() {
	$ = layui.jquery;
	response = layui.response;
	var layer = layui.layer,
		element = layui.element,
		laytpl = layui.laytpl,
		form = layui.form,
		table = layui.table,
		lcArray = '',
		taskId = '',
		formKey = '',
		processDefName = '',
		processInstanceId = '',
		claimStatus = '',
		claimStatusName = '',
		taskName = '',
		workflow = layui.workflow,
		moduleAuthority = layui.moduleAuthority,
		checkData;

	$(function() {
		//获取地址栏参数
		var $paramArr = getIpHz();

		//获取首页传递过来的数据
		if($paramArr['taskId']) {
			taskId = $paramArr['taskId'];
			processInstanceType = 'todo';
		} else if($paramArr['processinsid']) {
			processInstanceId = $paramArr['processinsid'];
			processInstanceType = 'list';
		} else {
			var getName = $paramArr['name'];
			if(sessionStorage.getItem('lcArray' + getName)) {
				lcArray = JSON.parse(sessionStorage.getItem('lcArray' + getName));
				console.log('lcArray:', lcArray);
				taskId = lcArray.taskId;
				formKey = lcArray.formKey;
				processInstanceId = lcArray.processInstanceId;
				processInstanceType = lcArray.processInstanceType;
				processDefName = lcArray.processDefName;
				claimStatus = lcArray.claimStatus;
				claimStatusName = lcArray.claimStatusName;
				taskName = lcArray.taskName;

				$('.bdc_processDefName').text(processDefName);
			} else if(sessionStorage.getItem('listArray' + getName)) {
				listArray = JSON.parse(sessionStorage.getItem('listArray' + getName));
				processInstanceId = listArray.processInstanceId;
				processInstanceType = listArray.processInstanceType;
				claimStatus = listArray.claimStatus;
				claimStatusName = listArray.claimStatusName;
				taskName = listArray.taskName;
			} else {
				// session 没有获取到对应信息
				processInstanceType = $paramArr['type'];
				// 获取必须参数
				$.ajax({
					type: "GET",
					url: getContextPath() + "/rest/v1.0/task/info/" + taskId,
					data: {},
					async: false,
					success: function(data) {
						checkData = data;
						taskId = data.taskId;

						if(data.taskName == null) {
							data.taskName = '';
						}
						taskName = data.taskName;

						if(data.taskDueDate == null) {
							data.newTaskDueDate = '';
						} else {
							data.newTaskDueDate = data.taskDueDate;
						}
						if(data.startTime == null) {
							data.newStartTime = '';
						} else {
							data.newStartTime = data.startTime;
						}
						if(data.taskAssigin == null) {
							data.taskAssigin = '';
						}

						formKey = data.formKey;
						processInstanceId = data.processInstanceId;
						processDefName = data.processDefName;
						claimStatus = data.claimStatus;
						claimStatusName = data.claimStatusName;
						data.opinion = workflow.queryOpinion(processInstanceId, taskId, 'back_opinion');

						$('.bdc_processDefName').text(processDefName);

						var getMenuTpl = menuTipsTpl.innerHTML,
							menuView = document.getElementById('bdcMenuTipsView');
						laytpl(getMenuTpl).render(data, function(html) {
							menuView.innerHTML = html;
						});
					},
					error: function(e) {
						response.fail(e, '');
					}
				});
			}
		}

		//关闭处理
		window.onunload = function() {
			top.opener.renderCurrentTable();
		};

		if(lcPageEdition == 'hf') {
			//待办增加websocket
			if(processInstanceType == 'todo') {
				getReturnData("/rest/v1.0/user/info", {}, "GET", function(data) {
					//消息中心推送
					webSocket(data.username);
				});
			}
		}
		if(processInstanceType == 'list') { //项目列表
			$.ajax({
				type: "GET",
				url: getContextPath() + "/rest/v1.0/task/process/last",
				data: {
					processInstanceId: processInstanceId
				},
				async: false,
				success: function(data) {
					formKey = data.processInstanceData.formKey;
					taskId = data.taskId;
					if(lcPageEdition = "hf") {
						var user = queryCurrentUser();
						if(user != undefined && user.username == data.startUserId) {
							print = true;
						}
					}
					// 用户配置了补打角色则获得打印权限
					queryPrintRoles();
				},
				error: function(e) {
					response.fail(e, '');
				}
			});
		}

		//按钮权限
		authorityNewPage('', moduleAuthority);

		function authorityNewPage(moduleCode, moduleAuthority) {
			$.ajax({
				type: "GET",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/module/authority",
				data: { moduleCode: moduleCode },
				success: function(data) {
					console.log(data);
					// 根据 实例类型 控制按钮

					if(processInstanceType == 'done') {
						showDoneButton(data);
					} else if(processInstanceType == 'todo' || processInstanceType == 'rl') {
						showTodoButton(data);
						showRlButton();
					} else if(iSabandon == 'show') {
						resizeOprateBtn(0, 80, 40, 0);
					}
				},
				error: function(e) {
					response.fail(e, '');
				}
			});
		}

		function showDoneButton(authorityData) {
			if(authorityData.indexOf('fetch') > 0) {
				resizeOprateBtn(60, 60 + 80, 60 + 40, 60);
				$("#bdc-take-btn").css('display', 'inline-block');
				$("#bdc-back-btn").css('display', 'none');
				$("#bdc-delete-btn").css('display', 'none');
			} else if(iSabandon == "show") {
				resizeOprateBtn(0, 80, 40, 0);
			}
		}

		function showTodoButton(authorityData) {
			if(authorityData.indexOf('delete') > 0) {
				$("#bdc-delete-btn").css('display', 'inline-block');
			}
			if(authorityData.indexOf('back') > 0) {
				$("#bdc-back-btn").css('display', 'inline-block');
			}
			// 判断下一个结点是否是办结
			var isEnd;
			$.ajax({
				type: "GET",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/forward/isEnd",
				data: { taskId: taskId },
				async: false,
				success: function(data) {
					isEnd = data;
					resizeOprateBtn(3 * 60, 3 * 60 + 80, 3 * 60 + 40, 3 * 60);

					if(authorityData.indexOf('forward') > 0) {
						if(isEnd) {
							$("#bdc-end-btn").css('display', 'inline-block');
							$("#bdc-forward-btn").css('display', 'none');
						} else {
							$("#bdc-forward-btn").css('display', 'inline-block');
							$("#bdc-end-btn").css('display', 'none');
						}
					}

				},
				error: function(e) {
					resizeOprateBtn(2 * 60, 2 * 60 + 80, 2 * 60 + 40, 2 * 60);
					response.fail(e, '');
				}
			});
		}

		function showRlButton() {
			if(claimStatus != 0) {
				resizeOprateBtn(4 * 60, 4 * 60 + 80, 4 * 60 + 40, 4 * 60);
				$("#bdc-cancel-claim-btn").css('display', 'inline-block');
			}
		}

		//不是直接传递的taskId可以先加载
		if(!$paramArr['taskId']) {
			queryForm(processInstanceType);
			listFiles();
		}

		//异步处理 获取操作参数
		$.ajax({
			type: "GET",
			url: getContextPath() + "/rest/v1.0/task/info/" + taskId,
			data: {},
			async: false,
			success: function(data) {
				checkData = data;
				if(data.taskName == null) {
					data.taskName = '';
				}
				if(lcPageEdition == 'hf') {
					isAbandon(data.processInstanceId);
					// 判断节点是否显示
					if(showAandonTaskNames.indexOf(data.taskName) > -1 && iSabandon == "show") {
						iSabandon = "show";
						$("#bdc-revoke-btn").css('display', 'inline-block');
					} else {
						iSabandon = "hide";
						$("#bdc-revoke-btn").css('display', 'none');
					}
				}
				if(data.taskDueDate == null) {
					data.newTaskDueDate = '';
				} else {
					data.newTaskDueDate = data.taskDueDate;
				}
				if(data.startTime == null) {
					data.newStartTime = '';
				} else {
					data.newStartTime = data.startTime;
				}
				if(data.taskAssigin == null) {
					data.taskAssigin = '';
				}
				if(!isNullOrEmpty(data.formKey)) {
					formKey = data.formKey;
				}
				if(!isNullOrEmpty(data.processInstanceId)) {
					processInstanceId = data.processInstanceId;
				}

				data.opinion = workflow.queryOpinion(processInstanceId, taskId, 'back_opinion');
				processDefName = data.processDefName;
				$('.bdc_processDefName').text(processDefName);

				var getMenuTpl = menuTipsTpl.innerHTML,
					menuView = document.getElementById('bdcMenuTipsView');
				laytpl(getMenuTpl).render(data, function(html) {
					menuView.innerHTML = html;
				});
			},
			error: function(e) {
				response.fail(e, '');
			}
		});

		//直接传递的taskId
		if($paramArr['taskId']) {
			queryForm(processInstanceType);
			listFiles();
		}

		// 0.1 渲染附件侧边栏
		//0. 侧边栏动态渲染
		var imgList,
			imgListLength,
			imgIndex;
		//0.1 tab点击，改变iframe的高度
		$('.layui-side-menu .layui-nav-item a').on('click', function() {
			setTimeout(function() {
				$('.layadmin-iframe').height($('.layui-layout-admin .layui-body .layadmin-tabsbody-item').height());
			}, 100);
		});

		//鼠标覆盖在顶部 info 图标，展示相关信息
		$('.bdc-aside-title img').on('mouseenter', function() {
			$('.bdc-aside-title .bdc-title-tips').removeClass('bdc-hide');
		});
		$('.bdc-aside-title .bdc-title-tips').on('mouseleave', function() {
			$('.bdc-aside-title .bdc-title-tips').addClass('bdc-hide');
		});
		//点击附件一级、二级按钮
		$('#fjLeftTree').on('click', '.bdc-fj-file', function() {
			imgIndex = 0;
			imgList = $(this).siblings('.layui-nav-child').children();
			if(imgList.length != 0) {
				imgListLength = imgList.length;
			}
			$(this).parent().siblings().removeClass('layui-nav-itemed');
			$(this).parent().siblings().find('dd').removeClass('layui-nav-itemed');
			$('.fl-list-box').find('dd').removeClass('layui-this');
			if($('#seeImgView .viewer-container').length > 0) {
				viewer.destroy();
			} else {
				$('#seeImgView').html('');
			}
			var src = '';
			renderImg(src, '');
		});

		//------------------附件相关动效----------------------
		//默认展示图片
		var image = new Image();
		var viewer;
		renderImg('', '');

		//点击展示小箭头
		var isShow = false,
			clickTimes = 0;
		$('.bdc-fj-show img').on('click', function() {
			isShow = !isShow;
			clickTimes++;
			if(isShow) {
				$('.bdc-fj-default-content').addClass('bdc-hide');
				$('.bdc-fj-list').removeClass('bdc-hide');
				$('.bdc-fj-show img').css('transform', 'rotate(180deg)');
				listFiles();

				$('.bdc-fj-content').removeClass('bdc-hide');
				$('.layui-layout-admin .layui-body .layadmin-tabsbody-item').css('padding-bottom', '284px');
				$('.layui-side-menu .layui-side-scroll').css('padding-bottom', '300px');

			} else {
				$('.bdc-fj-show img').css('transform', 'rotate(0)');
				$('.bdc-fj-content').addClass('bdc-hide');
				$('.layui-layout-admin .layui-body .layadmin-tabsbody-item').css('padding-bottom', '0');
				$('.layui-side-menu .layui-side-scroll').css('padding-bottom', '35px');
				$('.bdc-fj-list').addClass('bdc-hide');
				$('.bdc-fj-default-content').removeClass('bdc-hide');
			}

		});

		//点击左上角展示方式图标，如果是弹出层方式展示
		var popupLayer;
		$('.bdc-fj-title .bdc-show-popup').on('click', function() {
			$('.bdc-fj-default-content').addClass('bdc-hide');
			$('.bdc-fj-list').addClass('bdc-hide');

			$('.layui-side-menu .layui-side-scroll').css('padding-bottom', 0);
			$('.layui-layout-admin .layui-body .layadmin-tabsbody-item').css('padding-bottom', 0);
			$('.bdc-fj-content').height('203px');
			// $('.bdc-fj-list').addClass('bdc-hide');

			isShow = false;
			clickTimes = 0;
			$('.bdc-fj-show img').css('transform', 'rotate(0)');
			popupLayer = layer.open({
				type: 2,
				skin: 'bdc-add-fj-layer',
				title: '<div class="bdc-fj-title-content">' +
					'<p class="bdc-fj-title">' +
					'<span class="bdc-fj-word">附件' +
					'</span>' +
					'</p>' +
					'<div class="bdc-operate-icon">' +
					'<i class="fa fa-expand" aria-hidden="true"></i>' +
					'<i class="layui-icon layui-icon-close"></i>' +
					'</div>' +
					'</div>',
				area: ['960px', '350px'],
				shade: 0,
				maxmin: true,
				content: './popup-img.html?processinsid=' + processInstanceId,
				zIndex: layer.zIndex //重点1
					// ,move: '.pf-bottom-btn'
					,
				success: function(layero) {
					layer.setTop(layero); //重点2
				},
				cancel: function() {
					$('.bdc-fj-content').addClass('bdc-hide');
					$('.layui-side-menu .layui-side-scroll').css('padding-bottom', '35px');

					$('.bdc-fj-default-content').removeClass('bdc-hide');
				}
			});
		});

		$(document).on('mousedown', '.bdc-fj-title .bdc-show-bottom', function() {
			layer.close(popupLayer);
			$('.bdc-fj-list').removeClass('bdc-hide');
			$('.bdc-fj-content').addClass('bdc-hide');
			$('.layui-side-menu .layui-side-scroll').css('padding-bottom', '35px');
		});

		//1. 复选框相关动效
		preventBubbling();

		//1.1 监听 文件夹 复选框选择
		form.on('checkbox(folderFilter)', function(data) {
			if(!data.elem.checked) {
				$(this).parent().siblings().find('.layui-form-checkbox').removeClass('layui-form-checked')
			}
		});

		//1.2 监听 文件 复选框选择
		form.on('checkbox(fileFilter)', function(data) {
			if(data.elem.checked) {
				//选中，父元素选中
				$(this).parents('.layui-nav-child').siblings().find('.layui-form-checkbox').addClass('layui-form-checked');
			} else {
				// 没选中，看其他兄弟元素是否选中
				if($(this).parent().parent().siblings().find('.layui-form-checkbox').hasClass('layui-form-checked')) {
					$(this).parents('.layui-nav-child').siblings().find('.layui-form-checkbox').addClass('layui-form-checked');
				} else {
					$(this).parents('.layui-nav-child').siblings().find('.layui-form-checkbox').removeClass('layui-form-checked');
				}
			}
		});

		//阻止点击复选框向上冒泡
		var $checkBox = $('.layui-form-checkbox');

		function preventBubbling() {
			$checkBox = $('.layui-form-checkbox');
			//阻止复选框向上冒泡
			$checkBox.on('click', function(event) {
				event.stopPropagation();
			});
		}

		//2. 监听图片侧边栏单击
		$('.fl-list-box').on('click', '.layui-nav-child dd>a', function() {
			$(this).parent().addClass('layui-this');
			imgIndex = $(this).index() + 1;
			imgList = $(this).parent().parent().children();
			imgListLength = imgList.length;
			if($('#seeImgView .viewer-container').length > 0) {
				viewer.destroy();
			} else {
				$('#seeImgView').html('');
			}
			var src = $(this).data('src');
			var type = $(this).data('type');
			renderImg(src, type);
		});

		//2.1 监听上一个 下一个
		$('.seeLeftBtn').on('click', function() {
			imgIndex--;
			if(imgIndex >= 0) {
				if($(imgList[imgIndex]).children().length == 1) {
					$('.fl-list-box').find('dd').removeClass('layui-this');
					$(imgList[imgIndex]).addClass('layui-this');
					if($('#seeImgView .viewer-container').length > 0) {
						viewer.destroy();
					} else {
						$('#seeImgView').html('');
					}
					var src = $(imgList[imgIndex]).children('a').data('src');
					var type = $(imgList[imgIndex]).children('a').data('type');
					renderImg(src, type);
				} else {
					$(imgList[imgIndex]).siblings().removeClass('layui-nav-itemed');
					$(imgList[imgIndex]).siblings().find('dd').removeClass('layui-nav-itemed');
					$(imgList[imgIndex]).addClass('layui-nav-itemed');

					imgList = $(imgList[imgIndex]).children('dl').children();
					if(imgList.length != 0) {
						imgListLength = imgList.length;
					}
					if($('#seeImgView .viewer-container').length > 0) {
						viewer.destroy();
					} else {
						$('#seeImgView').html('');
					}
					var src = '';
					var type = '';
					renderImg(src, type);
					imgIndex = 0;
				}
			} else {
				imgIndex = 1;
				layer.msg('已经是第一张了');
			}
		});

		$('.seeRightBtn').on('click', function() {
			if(imgIndex <= imgListLength - 1) {
				if($(imgList[imgIndex]).children().length == 1) {
					$('.fl-list-box').find('dd').removeClass('layui-this');
					$(imgList[imgIndex]).addClass('layui-this');
					if($('#seeImgView .viewer-container').length > 0) {
						viewer.destroy();
					} else {
						$('#seeImgView').html('');
					}
					var src = $(imgList[imgIndex]).children('a').data('src');
					var type = $(imgList[imgIndex]).children('a').data('type');
					renderImg(src, type);
					imgIndex++;
				} else {
					$(imgList[imgIndex]).siblings().removeClass('layui-nav-itemed');
					$(imgList[imgIndex]).siblings().find('dd').removeClass('layui-nav-itemed');
					$(imgList[imgIndex]).addClass('layui-nav-itemed');
					imgList = $(imgList[imgIndex]).children('dl').children();
					if(imgList.length != 0) {
						imgListLength = imgList.length;
					}
					if($('#seeImgView .viewer-container').length > 0) {
						viewer.destroy();
					} else {
						$('#seeImgView').html('');
					}
					var src = '';
					var type = '';
					renderImg(src, type);
					imgIndex = 0;
				}
			} else {
				imgIndex = imgListLength - 1;
				layer.msg('已经是最后一张了');
			}
		});

		//渲染指定图片
		function renderImg(src, type) {
			//if(type == 'pdf'){
			//    $('#seeImgView').html('<iframe src="' + getContextPath() + "/static/lib/pdf-viewer/web/viewer.html?file=" + src +'"></iframe>');
			//}
			if(type == 'pdf' || type == 'lsx' || type == 'xls' || type == 'ofd' || type == 'doc' || type == 'docx') {
				$('#seeImgView').html('<iframe src="' + src + '"></iframe>');
			} else {
				image.src = src;
				viewer = new Viewer(image, {
					hidden: function() {
						viewer.destroy();
					}
				});
				viewer.show();
			}
		}

		//3. 点击左侧操作按钮
		//3.1 全选
		$('.bdc-select-all').on('click', function() {
			$checkBox.addClass('layui-form-checked');
		});
		//3.2 新增
		$('.bdc-add-btn').on('click', function(event) {
			event.stopPropagation();
			$('.bdc-add-details').toggleClass('bdc-hide');
		});
		$('body').on('click', function() {
			$('.bdc-add-details').addClass('bdc-hide');
			$('.layui-nav .bdc-prepend-li a .layui-icon-close').click();
			$('.bdc-aside-title .bdc-title-tips').addClass('bdc-hide');
		});
		// 3.2.1 新增文件夹
		$('.bdc-add-wjj').on('click', function(event) {
			event.stopPropagation();
			$('.fl-list-box>.layui-nav').prepend('<li class="layui-nav-item bdc-prepend-li">' +
				'<a href="javascript:;">' +
				'<input type="checkbox" name="bdcFolder" lay-filter="folderFilter" lay-skin="primary">' +
				'<img src="../image/file.png" alt="">' +
				'<input type="text" autocomplete="off" class="layui-input bdc-new-folder-name" value="新建文件夹">' +
				'<i class="layui-icon bdc-icon-add layui-icon-ok"></i>' +
				'<i class="layui-icon bdc-icon-add layui-icon-close"></i>' +
				'</a>' +
				'</li>');
			form.render('checkbox');
			$('.bdc-new-folder-name').focusEnd();
			preventBubbling();
		});

		// 3.2.1.1 点击√
		$('.pf-fl-list').on('click', '.layui-nav .bdc-prepend-li a .layui-icon-ok', function(event) {
			event.stopPropagation();
			var folderName = $('.bdc-new-folder-name').val();
			$(this).parent().append('<span>' + folderName + '</span>');
			$('.bdc-new-folder-name').remove();
			$('.bdc-icon-add').remove();
		});
		// 3.2.1.2 点击×
		$('.pf-fl-list').on('click', '.layui-nav .bdc-prepend-li a .layui-icon-close', function(event) {
			event.stopPropagation();
			$(this).parents('.bdc-prepend-li').remove();
			// $('.bdc-prepend-li').remove();
		});
		//3.2.1.3 点击输入框
		$('.pf-fl-list').on('click', '.bdc-prepend-li .bdc-new-folder-name', function(event) {
			event.stopPropagation();
		});

		// 3.3 删除
		$('.bdc-fj-delete').on('click', function() {
			layer.msg('您确认要删除吗？', {
				skin: 'bdc-del-bg',
				time: 0 //不自动关闭
					,
				shade: [0.4, '#000'],
				btn: ['确定', '取消'],
				yes: function(index) {
					layer.close(index);
					layer.msg('删除成功');
				}
			});
		});

		//拖拽调整 附件 高度
		var maxHeight = $('body').height() - 40;
		var src_posi_Y = 0,
			dest_posi_Y = 0,
			move_Y = 0,
			is_mouse_down = false,
			destHeight = 299;
		$(".bdc-drag-line").mousedown(function(e) {
			src_posi_Y = e.pageY;
			is_mouse_down = true;

			return false;
		});

		$(document).bind("mouseup click", function(e) {
			// if(is_mouse_down){
			//     is_mouse_down = false;
			// }
			is_mouse_down = false;
		}).mousemove(function(e) {
			dest_posi_Y = e.pageY;
			move_Y = src_posi_Y - dest_posi_Y;
			src_posi_Y = dest_posi_Y;
			destHeight = $(".bdc-fj-list").height() + move_Y;
			if(is_mouse_down) {
				if(destHeight < maxHeight) {
					$(".bdc-fj-content").css("height", destHeight > 299 ? destHeight - 35 : 264);
				} else {
					$(".bdc-fj-content").css("height", maxHeight - 35);
				}
			}
		});

		//点击办结/转发
		$('.bdc-send').on('click', function() {
			addModel();
			var checkStatus = {};
			var datas = [];
			datas.push(checkData);
			checkStatus.data = datas;
			setTimeout(function() {
				workflow.forwardauto(checkStatus, '', '', 'dbTable', false, processInstanceId);
			}, 50);
		});

		// 点击退回
		$('#bdc-back-btn').on('click', function() {
			workflow.backProcess(checkData, "", "", "dbTable", false);
		});

		// 点击取回
		$('#bdc-take-btn').on('click', function() {
			workflow.fetchProcess(checkData, "", "", "doneTableId", false);
		});
		// 取消认领
		$('#bdc-cancel-claim-btn').on('click', function() {
			workflow.cancelClaimProcess(checkData, "", "", "dbTable", false);
		});

		// 点击删除
		$('#bdc-delete-btn').on('click', function() {
			var data = [];
			data.push(checkData);
			var allData = { 'data': data };
			workflow.deleteProcess(allData, '', '', 'dbTable', false);
		});

		// 点击撤销
		$('#bdc-revoke-btn').on('click', function() {
			workflow.abandonProcess(checkData, "", "", "dbTable", false);
		});
		// 点击侧边的收缩按钮
		// 隐藏
		$('.bdc-aside-close').on('click', function() {
			$('.layui-layout-admin .layui-side').animate({ left: '-220px' }, 100);
			$('.layui-layout-admin .layui-layout-left').animate({ left: 0 }, 100);
			$('.layui-layout-admin .layui-footer').animate({ left: 0 }, 100);
			$('.layui-layout-admin .layui-body').animate({ left: 0 }, 100);
			$('.layadmin-pagetabs').animate({ left: 0 }, 100);
			$('.bdc-aside-zoom').toggleClass('bdc-hide').animate({ left: 0 }, 300);
			// $('.bdc-aside-open').animate({left: 0},300);
		});
		// 显示
		$('.bdc-aside-open').on('click', function() {
			$('.layui-layout-admin .layui-side').animate({ left: '0' }, 100);

			$('.layui-layout-admin .layui-layout-left').animate({ left: '220px' }, 100);
			$('.layui-layout-admin .layui-footer').animate({ left: '220px' }, 100);
			$('.layui-layout-admin .layui-body').animate({ left: '220px' }, 100);
			$('.layadmin-pagetabs').animate({ left: '220px' }, 100);

			$('.bdc-aside-zoom').toggleClass('bdc-hide').animate({ left: '206px' }, 300);
			// $('.bdc-aside-open').animate({left: '208px'},300);
		});

		//默认显示的第一个tab 删除
		//      $('.bdc-aside-tab li .bdc-tab-close').on('click', function () {
		//          var _this = $(this).parent();
		//          if (_this.hasClass('layui-this')) {
		//              if($('.bdc-aside-tab .layui-tab-title li').length > 2){
		//                  _this.parent().find('li:nth-child(3)').addClass('layui-this');
		//                  $('.layui-body .layadmin-tabsbody-item:nth-child(3)').addClass('layui-show');
		//                  var layId = $('.bdc-aside-tab .layui-tab-title li:nth-child(3)').attr('lay-id');
		//                  var $sideLi = $('.layui-side-scroll .layui-nav-tree li');
		//                  for(var i = 1; i< $sideLi.length; i++){
		//                      if($($sideLi[i]).find('a').attr('lay-href') == layId){
		//                          $($sideLi[i]).addClass('layui-this');
		//                      }
		//                  }
		//
		//                  _this.remove();
		//                  $('.layui-body .layadmin-tabsbody-item:nth-child(2)').remove();
		//                  $('.layui-side-scroll .layui-nav-tree li:first-child').removeClass('layui-this');
		//              }else {
		//                  _this.remove();
		//                  $('.layui-body .layadmin-tabsbody-item:nth-child(2)').remove();
		//                  $('.layui-side-scroll .layui-nav-tree li:first-child').removeClass('layui-this');
		//              }
		//
		//
		//          } else {
		//              _this.remove();
		//              $('.layui-body .layadmin-tabsbody-item:nth-child(2)').remove();
		//          }
		//
		//      });

		//焦点在input文字最后
		$.fn.setCursorPosition = function(position) {
			if(this.lengh == 0) return this;
			return $(this).setSelection(position, position);
		};

		$.fn.setSelection = function(selectionStart, selectionEnd) {
			if(this.lengh == 0) return this;
			input = this[0];

			if(input.createTextRange) {
				var range = input.createTextRange();
				range.collapse(true);
				range.moveEnd('character', selectionEnd);
				range.moveStart('character', selectionStart);
				range.select();
			} else if(input.setSelectionRange) {
				input.focus();
				input.setSelectionRange(selectionStart, selectionEnd);
			}

			return this;
		};

		$.fn.focusEnd = function() {
			if(this.val() != undefined) {
				this.setCursorPosition(this.val().length);
			}
		};

	});

	function resizeOprateBtn(controlWidth, pageTabs, nextIcon, setIcon) {
		if(iSabandon == "show") {
			controlWidth += 60;
			pageTabs += 60;
			nextIcon += 60;
			setIcon += 60;
		}
		$('.layadmin-pagetabs .bdc-control-icon').width(controlWidth + 'px');
		$('.layadmin-pagetabs').css('padding-right', pageTabs + 'px');
		$('.layadmin-pagetabs .layui-icon-next').css('right', nextIcon + 'px');
		$('.layadmin-pagetabs .layui-icon-set-sm').css('right', setIcon + 'px');
	}

	function queryForm(processInstanceType) {
		var readOnly = true;
		/**
		 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
		 * @description 添加  processInstanceType == 'rl'，认领列表打开页面的同时已认领，只读设置为false
		 */
		// lixin 项目列表的表单权限从大云中的流程表单单独控制，所以放开 readOnly 的限制
		if(processInstanceType == 'todo' || processInstanceType == 'rl' || processInstanceType == 'list') {
			readOnly = false;
		}
		//0. 侧边栏动态渲染
//		$.ajax({
//			type: "GET",
//			url: "http://192.168.0.59:8030/realestate-portal-ui/rest/v1.0/task/form-center/" + formKey,
//			data: {},
//			success: function(data) {
//				data.forEach(function(v) {
//					if(v.thirdPath.indexOf("?") != -1) {
//						// 有参数
//						var newPath = v.thirdPath + '&taskId=' + taskId + '&processInsId=' + processInstanceId + '&formStateId=' + v.formStateId + '&readOnly=' + readOnly + '&print=' + print;
//						v.thirdPath = newPath;
//					} else {
//						//没有参数
//						var newPath = v.thirdPath + '?taskId=' + taskId + '&processInsId=' + processInstanceId + '&formStateId=' + v.formStateId + '&readOnly=' + readOnly + '&print=' + print;
//						v.thirdPath = newPath;
//					}
//				});
//				// 默认选中第一个表单资源
//				if(data && data.length > 0) {
//					$('.layui-body .layadmin-tabsbody-item.layui-show iframe').attr('src', data[0].thirdPath);
//					$('.bdc-aside-tab .layui-tab-title li.layui-this').attr('lay-id', data[0].thirdPath).attr('lay-attr', data[0].thirdPath);
//					$('.bdc-aside-tab .layui-tab-title li.layui-this>span').html(data[0].formStateName);
//				}
//				var getAsideTpl = asideTpl.innerHTML,
//					getAsideView = document.getElementById('LAY_app_tabsheader');
//				laytpl(getAsideTpl).render(data, function(html) {
//					getAsideView.append(html);
//				});
//				element.render('nav', 'layadmin-system-side-menu');
//			},
//			error: function(e) {
//				response.fail(e, '');
//			}
//		});

	}

	function listFiles() {
		$.ajax({
			type: "GET",
			url: getContextPath() + "/rest/v1.0/files/all",
			data: {
				clientId: "clientId",
				spaceId: processInstanceId
			},
			success: function(data) {
				data.forEach(function(value) {
					if(value.child) {
						value.child.forEach(function(v) {
							v.picType = v.name.substring(v.name.length - 3);
							if(v.child) {
								v.child.forEach(function(childV) {
									childV.picType = childV.name.substring(childV.name.length - 3);
								});
							}
						});
					}
				});
				var getFjAsideTpl = fjAsideTpl.innerHTML,
					getFjAsideView = document.getElementById('fjLeftTree');
				laytpl(getFjAsideTpl).render(data, function(html) {
					getFjAsideView.innerHTML = html;
				});
				element.render('nav', 'fjTreeFilter');
			},
			error: function(e) {
				response.fail(e, '');
			}
		});
	}
});

/**
 * 查询补打角色
 */
function queryPrintRoles() {
	$.ajax({
		type: "GET",
		url: getContextPath() + "/rest/v1.0/user/isbd",
		async: false,
		success: function(data) {
			if(data) {
				print = true;
			}
		},
		error: function(e) {
			response.fail(e, '');
		}
	});
}