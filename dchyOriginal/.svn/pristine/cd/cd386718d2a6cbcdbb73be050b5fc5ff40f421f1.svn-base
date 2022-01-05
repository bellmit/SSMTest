layui.define(['jquery', 'element', 'form', 'response', 'layer', 'laytpl', 'table'], function (exports) {
	var $ = layui.jquery,
		element = layui.element,
		response = layui.response,
		layer = layui.layer,
		laytpl = layui.laytpl,
		table = layui.table,
		form = layui.form;
	// 1:转发 2:删除 3:挂起 4: 激活 5:退回 6：认领 7：取回
	var forbiddenActive = [4];
	var forbiddenHangUp = [1, 2, 3, 5, 6, 7];
	var forbiddenClock = [4, 7];

	var workflow = {
		startUpProcess: function (data) {
			return startUpProcess(data);
		},
		startUpFdjywProcess: function (data) {
			return startUpFdjywProcess(data);
		},
		forwardProcess: function (checkData, url, tableId, currentId, isIndex, processInstanceId) {
			return forwardProcess(checkData, url, tableId, currentId, isIndex, processInstanceId);
		},
		backProcess: function (checkData, url, tableId, currentId, isIndex) {
			return backProcess(checkData, url, tableId, currentId, isIndex);
		},
		backPlProcess: function (checkStatus, url, tableId, currentId, isIndex) {
			return backPlProcess(checkStatus, url, tableId, currentId, isIndex);
		},
		claimProcess: function (checkStatus, url, tableId, currentId, openpage) {
			return claimProcess(checkStatus, url, tableId, currentId, openpage)
		},
		allowBack: function (taskId) {
			return allowBack(taskId);
		},
		allowFetchBack: function (taskId) {
			return allowFetchBack(taskId);
		},
		fetchProcess: function (checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
			return fetchProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex);
		},
		hangUpProcess: function (checkStatus, url, tableId, currentId) {
			return hangUpProcess(checkStatus, url, tableId, currentId);
		},
		deleteProcess: function (checkStatus, url, tableId, currentId, isIndex) {
			return deleteProcess(checkStatus, url, tableId, currentId, isIndex);
		},
		forwardauto: function (checkData, url, tableId, currentId, isIndex, processInstanceId) {
			return forwardauto(checkData, url, tableId, currentId, isIndex, processInstanceId);
		},
		forwardPlProcess: function (checkStatus, url, tableId, currentId) {
			return forwardPlProcess(checkStatus, url, tableId, currentId);
		},
		activeProcess: function (checkStatus, url, tableId, currentId) {
			return activeProcess(checkStatus, url, tableId, currentId);
		},
		isEnd: function (taskId) {
			return isEnd(taskId);
		},
		endProcess: function (checkData, url, tableId, currentId, isIndex) {
			return endProcess(checkData, url, tableId, currentId, isIndex);
		},
		checkStaus: function (checkData, staus, type) {
			return checkStaus(checkData, staus, type);
		},
		turnCheck: function (checkData) {
			return turnCheck(checkData, "zf");
		},
		checkBtx: function (formViewKey, gzlslid, taskId) {
			return checkBtx(formViewKey, gzlslid, taskId);
		},
		showHangReson: function (checkData) {
			return showHangReson(checkData);
		},
		queryOpinion: function (processInstanceId, taskId, type) {
			return queryOpinion(processInstanceId, taskId, type);
		},
		queryUserType: function (type) {
			return queryUserType(type);
		},
		lockProcess: function (checkData) {
			lockProcess(checkData);
		},
		cancelClaimProcess: function (checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
			//取消认领
			cancelClaimProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex)
		},
		cancelClaimPlProcess: function (checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
			//取消批量认领
			cancelClaimPlProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex)
		},
		fetchPlProcess: function (checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
			return fetchPlProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex);
		},
		abandonProcess: function (checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
			return abandonProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex);
		}

	};

	// 创建任务
	function startUpProcess(data) {
		if (data) {
			$.ajax({
				type: "GET",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/process/param",
				cache: false, // 设置为 false 将不缓存此页面
				async: false,
				success: function (param) {
					if (!isNullOrEmpty(param.slbh) && !isNullOrEmpty(param.jbxxid)) {
						var slbh = param.slbh;
						var jbxxid = param.jbxxid;
						window.open('/realestate-accept-ui/view/query/selectBdcdyh.html?processDefKey=' + data.processDefKey + '&jbxxid=' + jbxxid + '&slbh=' + slbh);
					} else {
						layer.msg("获取 jbxxid 和 slbh 失败！");
					}
				},
				error: function (e) {
					response.fail(e, '');
				}
			});

		} else {
			layer.msg('创建失败！');
		}
	}

	// 创建非登记业务流程
	function startUpFdjywProcess(data) {
		if (data) {
			$.ajax({
				type: "PUT",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/fdjyw",
				contentType: "application/json;charset=utf-8",
				dataType: "json",
				data: JSON.stringify(data),
				success: function (param) {
					window.open('/realestate-portal-ui/view/new-page.html?taskId=' + param.taskId);
				},
				error: function (e) {
					response.fail(e, '');
				}
			});
		} else {
			layer.msg('创建失败！');
		}
	}

	//转发操作
	function forwardProcess(checkData, url, tableId, currentId, isIndex, processInstanceId) {
		removeModal();
		forward(checkData, url, tableId, currentId, isIndex, processInstanceId);
	}
	function forward(checkData, url, tableId, currentId, isIndex, processInstanceId) {

		var zfUrl = "../view/send.html";
		removeModal();
		layer.open({
			type: 2,
			skin: 'layui-layer-lan',
			title: '任务转发',
			area: ['960px', '490px'],
			content: zfUrl + '?currentId=' + currentId + '&isIndex=' + isIndex + '&name=' + checkData.taskId + "&processInstanceId=" + processInstanceId
		});
	}

	//转发
	function forwardauto(checkStatus, url, tableId, currentId, isIndex, processInstanceId) {
		var checkData = checkStatus.data[0];
		var taskId = checkData.taskId;
		forwardProcess(checkData, url, tableId, currentId, isIndex, processInstanceId);
	}

	//退回操作
	function backProcess(checkData, url, tableId, currentId, isIndex) {
		// var checkResult = turnCheck(checkData, "th");
		back(checkData, url, tableId, currentId, isIndex);
		// if(checkResult == false) {
		// 	removeModal();
		// 	return false;
		// } else if(checkResult == true) {
		// 	removeModal();
		// 	back(checkData, url, tableId, currentId, isIndex);
		// } else {
		// 	removeModal();
		// 	var tsxxHtml = loadTsxx(checkResult);
		// 	rightShowWarn(tsxxHtml, checkData, url, tableId, currentId, isIndex);
		// }

	}
	/**
	 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
	 * @description 退回
	 */
	//	退回---无退回原因
	function back(checkData, url, tableId, currentId, isIndex) {
		addModel("退回中");
		if (!checkStaus(checkData, 5, '退回')) {
			removeModal();
			return false;
		}
		if (allowBack(checkData.taskId)) {
			layer.msg('退回成功！');
			removeModal();
			renderTable(url, tableId, currentId);
		}
		

	}
	//	退回---有退回原因
	//	function back(checkData, url, tableId, currentId, isIndex) {
	//		addModel();
	//		if(!checkStaus(checkData, 5, '退回')) {
	//			removeModal();
	//			return false;
	//		}
	//		if(allowBack(checkData.taskId)) {
	//			layer.open({
	//				title: '退回',
	//				type: 2,
	//				skin: 'layui-layer-lan',
	//				area: ['430px', '430px'],
	//				content: './back-info.html?currentId=' + currentId + '&isIndex=' + isIndex + '&taskId=' + checkData.taskId
	//			});
	//		}
	//		removeModal();
	//	}

	function claimProcess(checkStatus, url, tableId, currentId, openpage) {
		var checkResult = true;
		var taskIds = [];
		var gzlslidIds = [];
		checkStatus.data.forEach(function (v) {
			checkResult = checkStaus(v, 6, '认领');
			if (!checkResult) {
				return false;
			}
			taskIds.push(v.taskId);
			if (gzlslidIds.indexOf(v.procInsId) == -1) {
				gzlslidIds.push(v.procInsId);
			}
		});
		addModel('认领中');
		$.ajax({
			type: "post",
			url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/claim",
			traditional: true,
			data: { taskIds: taskIds, gzlslids: gzlslidIds },
			success: function (data) {
				layer.msg('认领成功');
				// renderTable(url, tableId, currentId);
				if (lcPageEdition == 'nt') {
					$('#rlTab').click();
					$.ajax({
						type: "post",
						url: getContextPath() + "/rest/v1.0/task/todo",
						traditional: true,
						success: function (data) {
							if (data.totalElements > 99) {
								$('.bdc-list-num-tips').html('99+');
							} else {
								$('.bdc-list-num-tips').html(data.totalElements);
							}
						}
					});
				} else {
					$('#todoTab').click();
					var rlUrl = getContextPath() + "/rest/v1.0/task/claim/list";
					//获取互联网+的处理
					if ($('.bdc-rl-num-word').length > 0 && $('.bdc-rl-num-word').html().indexOf("互联网") != -1) {
						$.ajax({
							type: "POST",
							url: rlUrl,
							data: { sply: "3" },
							success: function (data) {
								if (data && data.hasOwnProperty("totalElements")) {
									$('.bdc-rl-num-js').html(data.totalElements)
								}
							}
						});
					} else {
						$.ajax({
							type: "POST",
							url: rlUrl,
							success: function (data) {
								if (data && data.hasOwnProperty("totalElements")) {
									if (data.totalElements > 99) {
										$('.bdc-rl-num-js').html('99+');
									} else {
										$('.bdc-rl-num-js').html(data.totalElements);
									}
								}
							}
						});
					}
				}
				if (openpage) {
					openPage(checkStatus.data[0]);
				}
			},
			error: function (e) {
				response.fail(e, '');
			},
			complete: function () {
				removeModal();
			}
		});
	}
	/**
	 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
	 * @description 取消认领
	 */
	function cancelClaimProcess(checkData, url, tableId, currentId, isIndex) {
		var logoutIndex = layer.confirm('您确定要取消认领吗？', {
			title: '提示',
			btn: ['确定', '取消'] //按钮
		}, function (index) {
			addModel('取消中');
			layer.close(index);
			var gzlslid;
			if (checkData.taskName == '受理') {
				gzlslid = checkData.processInstanceId;
			}
			$.ajax({
				type: "post",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/claim/cancel",
				traditional: true,
				data: { taskId: checkData.taskId, gzlslid: gzlslid },
				success: function (data) {
					layer.msg('取消成功');
					renderTable(url, tableId, currentId);
					$.ajax({
						type: "post",
						url: getContextPath() + "/rest/v1.0/task/todo",
						traditional: true,
						success: function (data) {
							if (data.totalElements > 99) {
								$('.bdc-list-num-tips').html('99+');
							} else {
								$('.bdc-list-num-tips').html(data.totalElements);
							}
						}
					});
				},
				error: function (e) {
					response.fail(e, '');
				},
				complete: function () {
					removeModal();
					window.renderTable('', '', currentId);
					window.parent.close();
				}
			});
		});
	}

	/**
	 * @author <a href ="mailto:lixin1@gtmap.cn">lixin</a>
	 * @description 取消认领
	 */
	function cancelClaimPlProcess(checkData, url, tableId, currentId, isIndex) {
		var logoutIndex = layer.confirm('您确定要批量取消认领吗？', {
			title: '提示',
			btn: ['确定', '取消'] //按钮
		}, function (index) {
			addModel('取消中');
			layer.close(index);
			var workflowList = [];
			$.each(checkData, function (key, item) {
				var workflow = {};
				workflow.taskId = item.taskId;
				workflow.text1 = item.text1;
				workflow.processInstanceId = item.processInstanceId;
				// 只有受理节点需要传入流程 ID 用于清空受理人等信息
				if (item.taskName != '受理') {
					workflow.processInstanceId = "";
				}
				workflowList.push(workflow);
			});

			$.ajax({
				type: "post",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/claim/cancel/pl",
				contentType: "application/json;charset=utf-8",
				dataType: "json",
				data: JSON.stringify(workflowList),
				success: function (data) {
					var failMsg = "";
					var successMsg = "取消成功" + data.success + "条";
					if (!isNullOrEmpty(data.fail) && data.fail > 0) {
						failMsg = "取消失败" + data.fail + "条";
						if (data.success == 0) {
							successMsg = "";
						}
					}
					var otherMsg = "";
					$.each(data.messages, function (key, item) {
						otherMsg += item + "</br>";
					});
					layforwardMsg(successMsg, failMsg, otherMsg);
					renderTable(url, tableId, currentId);
					$.ajax({
						type: "post",
						url: getContextPath() + "/rest/v1.0/task/todo",
						traditional: true,
						success: function (data) {
							if (data.totalElements > 99) {
								$('.bdc-list-num-tips').html('99+');
							} else {
								$('.bdc-list-num-tips').html(data.totalElements);
							}
						}
					});
				},
				error: function (e) {
					response.fail(e, '');
				},
				complete: function () {
					removeModal();
					window.renderTable('', '', currentId);
					window.parent.close();
				}
			});
		});
	}
	//打开新页面
	function openPage(obj) {
		//得到当前行数据
		var lcArray = {
			taskId: obj.taskId,
			formKey: obj.formKey,
			//认领状态
			claimStatus: obj.claimStatus,
			processInstanceId: obj.processInstanceId,
			processDefName: obj.processDefName,
			processInstanceType: 'rl'
		};
		if (!showHangReson(obj)) {
			return false;
		}
		sessionStorage.setItem('lcArray' + obj.taskId, JSON.stringify(lcArray));
		//锁定任务
		lockProcess(obj);
		window.open("../view/service-page.html?name=" + obj.taskId, obj.text1);
	}

	//判断是否可以退回
	function allowBack(taskId) {
		var isAllow = false;
		$.ajax({
			type: "get",
			async: false,
			url: getContextPath() + "/index/turnBackTask",
			data: {
				taskid: taskId
			},
			success: function (data) {
				console.log('退回', data);
				data.status == 'ok' ? isAllow = true : layer.msg(data.result);
			},
			error: function (e) {
				response.fail(e, '');
			}
		});
		return isAllow;
	}

	//判断是否可以取回
	function allowFetchBack(taskId) {
		var isAllowFetchBack = false;
		$.ajax({
			type: "get",
			async: false,
			url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/isAllowFetchBack",
			data: {
				taskId: taskId
			},
			success: function (data) {
				data.code == 0 ? isAllowFetchBack = true : layer.msg(data.msg);
			},
			error: function (e) {
				response.fail(e, '')
			}
		});
		return isAllowFetchBack;
	}

	function fetchProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
		if (!allowFetchBack(checkData.taskId)) {
			return false;
		}
		var logoutIndex = layer.confirm('您确定要取回吗？', {
			title: '提示',
			btn: ['确定', '取消'] //按钮
		}, function (index) {
			layer.close(index);
			addModel('取回中');
			$.ajax({
				type: "get",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/fetchBack",
				traditional: true,
				data: { taskId: checkData.taskId },
				success: function (data) {
					if (isIndex) {
						layer.msg('取回成功');
						renderTable(doneUrl, doneTableId, doneCurrentId);
					} else {
						layer.msg('取回成功，即将关闭当前页。');
						setTimeout(function () { window.close(); }, 1000);
					}
				},
				error: function (e) {
					response.fail(e, '')
				},
				complete: function () {
					removeModal();
				}
			});
		});
	}
	/**
	 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
	 * @description 批量取回
	 */
	function fetchPlProcess(checkData, doneUrl, doneTableId, doneCurrentId, isIndex) {
		var taskIdList = [];
		$.each(checkData.data, function (key, val) {
			taskIdList.push(val.taskId);
		});
		if (taskIdList.length == 0) {
			return false;
		}
		var logoutIndex = layer.confirm('您确定要取回吗？', {
			title: '提示',
			btn: ['确定', '取消'] //按钮
		}, function (index) {
			layer.close(index);
			addModel('取回中');
			$.ajax({
				type: "get",
				url: getContextPath() + "/rest/v1.0/workflow/process-instances/task/plFetchBack",
				traditional: true,
				data: { taskIdList: taskIdList },
				success: function (data) {
					if (isIndex) {
						layer.msg(data, { time: 5000 });
						renderTable(doneUrl, doneTableId, doneCurrentId);
					} else {
						layer.msg(data + '即将关闭当前页。');
						setTimeout(function () { window.close(); }, 1000);
					}
				},
				error: function (e) {
					response.fail(e, '')
				},
				complete: function () {
					removeModal();
				}
			});
		});
	}

	function abandonProcess(checkData, url, tableId, currentId, isIndex) {
		var workflowList = [];
		var checkResult = true;
		if (!checkStaus(checkData, 2, '撤销')) {
			checkResult = false;
			return checkResult;
		}

		var workflow = {};
		//项目列表用的procInsId
		if (isNullOrEmpty(checkData.processInstanceId)) {
			workflow.processInstanceId = checkData.procInsId;
		} else {
			workflow.processInstanceId = checkData.processInstanceId;
		}
		if (isNullOrEmpty(checkData.processKey)) {
			workflow.processDefKey = checkData.procDefKey;
		} else {
			workflow.processDefKey = checkData.processKey;
		}
		workflowList.push(workflow);

		addModel();
		var checkResult = tsGzYz('cxlcgzyz', workflowList[0].processInstanceId);
		if (checkResult == false) {
			removeModal();
			return false;
		} else if (checkResult == true) {
			removeModal();
			// 清空撤消原因的内容
			$("#abandonreason").val('');
			var logoutIndex = layer.open({
				title: '撤销',
				type: 1,
				area: ['430px'],
				btn: ['确定', '取消'],
				content: $('#abandon-reason'),
				yes: function (index, layero) {
					var reason = $("#abandonreason").val();
					if (isNullOrEmpty(reason) && isNullOrEmpty(reason.replace(/\s+/g, ""))) {
						layer.msg('请输入撤销原因!');
						return false;
					}
					//提交 的回调
					addModel('撤销中');
					layer.close(index);
					for (var index in workflowList) {
						workflowList[index].reason = reason;
					}
					$.ajax({
						type: "POST",
						url: getContextPath() + "/rest/v1.0/workflow/process-instances/abandon",
						contentType: "application/json;charset=utf-8",
						dataType: "json",
						data: JSON.stringify(workflowList[0]),
						success: function (data) {
							if (isIndex) {
								layer.msg('撤销成功');
								renderTable(url, tableId, currentId);
							} else {
								layer.msg('撤销成功，即将关闭当前页。');
								setTimeout(function () { window.close(); }, 1000);
							}
						},
						error: function (e) {
							response.fail(e, '')
						},
						complete: function () {
							removeModal();
						}
					});
				},
				btn2: function (index, layero) {
					//取消 的回调
					layer.close(index);
				}
			});
		} else {
			removeModal();
			var tsxxHtml = loadTsxx(checkResult);
			rightShowWarn(tsxxHtml, checkData, url, tableId, currentId, isIndex);
		}
	}

	//挂起任务----无规则验证
	function hangUpProcess(checkStatus, url, tableId, currentId) {
		addModel('挂起中');
		checkStatus.data.forEach(function (v) {
			$.ajax({
				type: "post",
				url: getContextPath() + "/index/postTask?taskid=" + v.taskId,
				traditional: true,
				success: function (data) {
					$("#gqjgyy").val("");
					layer.msg("挂起成功!");
					renderTable(url, tableId, currentId);
				},
				error: function (e) {
					response.fail(e, '')
				},
				complete: function () {
					removeModal();
				}
			});
		})
	}
	//挂起任务-----有规则验证
	//	function hangUpProcess(checkStatus, url, tableId, currentId) {
	//		var processInstanceIds = [];
	//		var checkResult = true;
	//		$.each(checkStatus.data, function(key, item) {
	//			if(!checkStaus(item, 3, '挂起')) {
	//				checkResult = false;
	//				return checkResult;
	//			}
	//			processInstanceIds.push(item.processInstanceId);
	//		});
	//		if(!checkResult) {
	//			return checkResult;
	//		}
	//		//小弹出层
	//		layer.open({
	//			title: '任务挂起',
	//			type: 1,
	//			area: ['430px'],
	//			btn: ['挂起', '取消'],
	//			content: $('#bdc-popup-textarea'),
	//			yes: function(index, layero) {
	//				var reason = $("#gqjgyy").val();
	//				if(isNullOrEmpty(reason)) {
	//					layer.msg('请输入挂起原因');
	//				} else {
	//					addModel('挂起中');
	//					// 挂起当前任务
	//					$.ajax({
	//						type: "post",
	//						url: getContextPath() + "/index/postTask",
	//						traditional: true,
	//						data: {
	//							processInstanceIds: processInstanceIds,
	//							reason: reason
	//						},
	//						success: function(data) {
	//							layer.close(index);
	//							$("#gqjgyy").val("");
	//							layer.msg(data);
	//							renderTable(url, tableId, currentId);
	//						},
	//						error: function(e) {
	//							response.fail(e, '')
	//						},
	//						complete: function() {
	//							removeModal();
	//						}
	//					});
	//				}
	//			},
	//			btn2: function(index, layero) {
	//				$("#gqjgyy").val("");
	//			},
	//			cancel: function() {
	//				$("#gqjgyy").val("");
	//			},
	//			success: function() {
	//				$("#gqjgyyLab").html('<span class="required-span"><sub>*</sub></span>挂起原因');
	//			}
	//		});
	//	}

	//批量退回操作
	//可批量转发列表
	var workflowBackList = [];
	var plBackIgnoreList = [];

	function backPlProcess(checkStatus, url, tableId, currentId, isIndex) {
		// 清空退回原因
		$("#backreason").val("");
		var statusResults = batchCheckStatus(workflowBackList, checkStatus, "转发");
		if (workflowBackList != null && workflowBackList.length > 0) {
			layer.open({
				title: '退回原因',
				type: 1,
				area: ['430px'],
				btn: ['确认', '取消'],
				content: $('#back-reason'),
				yes: function (index, layero) {
					// 判断是否需要填写退回原因
					var thyjCheck = "";
					$.ajax({
						url: getContextPath() + "/rest/v1.0/workflow/process-instances/getThyjCheck",
						dataType: "json",
						async: false,
						success: function (data) {
							thyjCheck = data;
						},
						error: function (e) {
							response.fail(e, '')
						}
					});
					var opinion = $('#backreason').val();
					if (thyjCheck == '1') {
						if (isNullOrEmpty(opinion)) {
							layer.msg('请输入退回意见');
							return;
						}
					}
					layer.close(index);
					addModel('退回中');
					var bdcPlBackDTO = {};
					bdcPlBackDTO.workFlowDTOList = workflowBackList;
					bdcPlBackDTO.opinion = opinion;
					$.ajax({
						type: "POST",
						contentType: "application/json;charset=utf-8",
						url: getContextPath() + "/rest/v1.0/workflow/process-instances/back/pl",
						data: JSON.stringify(bdcPlBackDTO),
						success: function (data) {
							if (data.gzyzMsg == false) {
								showPlThWarnMsg(data, url, tableId, currentId);
							} else {
								workflowBackList = [];
								plBackIgnoreList = [];
							}
							if (isNullOrEmpty(data.message)) {
								data.message = "";
							}
							layforwardMsg(data.successMsg, data.failMsg, data.message + statusResults);
							renderTable(url, tableId, currentId);
						},
						error: function (e) {
							response.fail(e, '');
						},
						complete: function () {
							removeModal();
						}
					});

				},
				btn2: function (index, layero) {
					layer.close(index);
				}
			});
		} else {
			removeModal();
			layer.msg(statusResults);
		}
	}

	//批量转发任务
	//可批量转发列表
	var workflowPlList = [];
	//可忽略全部
	var plIgnoreList = [];

	function forwardPlProcess(checkStatus, url, tableId, currentId) {
		var logoutIndex = layer.confirm('您确定要批量转发吗？', {
			title: '提示',
			btn: ['确定', '取消'] //按钮
		}, function (index) {
			addModel('批量转发中');
			layer.close(index);
			// 对于转发项目的状态进行判断
			var statusResults = batchCheckStatus(workflowPlList, checkStatus, "转发");
			if (workflowPlList != null && workflowPlList.length > 0) {
				// 调用批量转发方法
				$.ajax({
					type: "POST",
					url: getContextPath() + "/rest/v1.0/workflow/process-instances/task-handel/batch-forward/complete",
					contentType: "application/json;charset=utf-8",
					data: JSON.stringify(workflowPlList),
					async: false,
					success: function (data) {
						if (data.gzyzMsg == false) {
							showPlWarnMsg(data, url, tableId, currentId);
						} else {
							workflowPlList = [];
							plIgnoreList = [];
						}
						var btxMsg = initBtxMsg(data.forwardYzDTOS);
						layforwardMsg(data.successMsg, data.failMsg, btxMsg + statusResults);
					},
					error: function (e) {
						response.fail(e, '')
					},
					complete: function () {
						removeModal();
					}
				});
			} else {
				removeModal();
				layer.msg(statusResults);
			}
		});
	}

	/**
	 * 批量流程判断状态
	 * @param checkStatus 选择数据
	 * @param statusResults 提示信息
	 * @param type 类型 「转发，退回」
	 */
	function batchCheckStatus(list, checkStatus, type) {
		var statusResults = "";
		console.log('checkStatus:', checkStatus);
		$.each(checkStatus.data, function (key, item) {
			if ((item.state == 1 || item.state == 4 || isNullOrEmpty(item.state)) && (forbiddenActive.indexOf(1) != -1)) {
				statusResults += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">流程：' + item.text1 + '已激活,不能进行' + type + '!</p>';
			} else if (item.state == 2 && (forbiddenHangUp.indexOf(1) != -1)) {
				statusResults += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">流程：' + item.text1 + '已挂起,不能进行' + type + '!</p>';
			} else if (item.state == 3 && (forbiddenClock.indexOf(1) != -1)) {
				statusResults += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">流程：' + item.text1 + '已办理,不能进行' + type + '!</p>';
			} else if (isNullOrEmpty(item.state) && item.procStatus == 3) { //项目列表的特殊处理
				statusResults += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">流程：' + item.text1 + '已挂起,不能进行' + type + '!</p>';
			} else {
				// 处理数据
				item.processDefKey = item.processKey;
				list.push(item);
			}
		});
		return statusResults;
	}

	// 初始化必填项消息
	function initBtxMsg(data) {
		var btxMsg = '';
		if (!isNullOrEmpty(data)) {
			data.forEach(function (v) {
				if (!isNullOrEmpty(v.errorMsg)) {
					btxMsg += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/info-small.png" alt="">' + v.errorMsg + '</p>';
				}
			});
		}
		return btxMsg;
	}

	//    批量转发右侧显示异常信息
	function showPlWarnMsg(data, url, tableId, currentId) {
		var warnLayer = layer.open({
			type: 1,
			title: '提示信息',
			anim: -1,
			skin: 'bdc-tips-right bdc-error-layer',
			shade: [0],
			area: ['390px'],
			offset: 'r',
			content: loadPlTsxx(data.forwardYzDTOS),
			success: function () {
				//点击忽略全部
				$('#ignoreAll').click(function () {
					addModel();
					$.ajax({
						type: "POST",
						url: getContextPath() + "/rest/v1.0/workflow/process-instances/task-handel/batch-forward/complete",
						contentType: "application/json;charset=utf-8",
						data: JSON.stringify(plIgnoreList),
						success: function (data) {
							layer.close(warnLayer);
							//右上角关闭 清空「转发和忽略」的信息
							workflowPlList = [];
							plIgnoreList = [];
							var btxMsg = initBtxMsg(data.forwardYzDTOS);
							layforwardMsg(data.successMsg, data.failMsg, btxMsg);
						},
						error: function (e) {
							response.fail(e, '')
						},
						complete: function () {
							removeModal();
						}
					});
					renderTable(url, tableId, currentId);
				});
				setTimeout(function () {
					$('.bdc-error-layer').css('opacity', 1)
				}, 500);
			},
			cancel: function () {
				//右上角关闭 清空「转发和忽略」的信息
				workflowPlList = [];
				plIgnoreList = [];
			}
		});
	}

	//    批量退回右侧显示异常信息
	function showPlThWarnMsg(data, url, tableId, currentId) {
		var opinion = $('#backreason').val();
		var warnLayer = layer.open({
			type: 1,
			title: '提示信息',
			anim: -1,
			skin: 'bdc-tips-right bdc-error-layer',
			shade: [0],
			area: ['390px'],
			offset: 'r',
			content: loadPlTsxx(data.forwardYzDTOS),
			success: function () {
				//点击忽略全部
				$('#ignoreAll').click(function () {
					addModel();
					var bdcPlBackDTO = {};
					bdcPlBackDTO.workFlowDTOList = plBackIgnoreList;
					bdcPlBackDTO.opinion = opinion;
					$.ajax({
						type: "POST",
						url: getContextPath() + "/rest/v1.0/workflow/process-instances/back/pl",
						contentType: "application/json;charset=utf-8",
						data: JSON.stringify(bdcPlBackDTO),
						success: function (data) {
							layer.close(warnLayer);
							//右上角关闭 清空「退回和忽略」的信息
							workflowBackList = [];
							plBackIgnoreList = [];
							layforwardMsg(data.successMsg, data.failMsg, "");
						},
						error: function (e) {
							response.fail(e, '')
						},
						complete: function () {
							removeModal();
						}
					});
					renderTable(url, tableId, currentId);
				});
				setTimeout(function () {
					$('.bdc-error-layer').css('opacity', 1)
				}, 500);
			},
			cancel: function () {
				//右上角关闭 清空「转发和忽略」的信息
				workflowBackList = [];
				plBackIgnoreList = [];
			}
		});
	}

	/**
	 * 批量转发信息提示
	 * @param successMsg 成功信息
	 * @param failMsg 失败信息
	 * @param other 其他信息
	 */
	function layforwardMsg(successMsg, failMsg, other) {
		var forwardmsg = '';
		if (!isNullOrEmpty(failMsg)) {
			forwardmsg += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/error-small.png" alt="">' + failMsg + '</p>'
		}
		if (!isNullOrEmpty(successMsg)) {
			forwardmsg += '<p class="bdc-zf-tips-p"><img src="../static/lib/bdcui/images/success-small.png" alt="">' + successMsg + '</p>'
		}
		if (isNullOrEmpty(other)) {
			other = "";
		}
		layer.msg(forwardmsg + other);
	}

	//删除任务-----无验证信息
	function deleteProcess(checkStatus, url, tableId, currentId, isIndex) {

		var   results  = checkStatus.data;//选中多条数据
		var taskId = new Array();
		var  xmid  = new Array();
		for(var i=0;i<results.length;i++){
			taskId[i] = results[i]["taskId"];
			xmid[i]  = results[i]["text9"]
		}
		layer.open({
			type: 1,
			title: '提示',
			skin: 'bdc-small-tips bdc-zf-tips',
			area: ['320px', '150px'],
			content: '是否确认删除？',
			btn: ['确定', '取消'],
			btnAlign: 'c',
			yes: function () {
				layer.closeAll();
                addModel("删除中");
				$.ajax({
					type: "GET",
					url: getContextPath() + "/index/delTask?taskid=" + taskId + "&xmid=" + xmid,
					contentType: "application/json;charset=utf-8",
					dataType: "json",
					success: function (data) {
						layer.msg('删除完成', {
							time: 1500
						});
						removeModal();
						renderTable(url, tableId, currentId)
					},
					error: function (e) {
						response.fail(e, '')
					}, complete: function () {
						removeModal();
					}

				});
			}
		});

	}
	//删除任务------有验证信息
	//	function deleteProcess(checkStatus, url, tableId, currentId, isIndex) {
	//		var workflowList = [];
	//		var checkResult = true;
	//		$.each(checkStatus.data, function(key, item) {
	//			if(!checkStaus(item, 2, '删除')) {
	//				checkResult = false;
	//				return checkResult;
	//			}
	//			var workflow = {};
	//			//项目列表用的procInsId
	//			if(isNullOrEmpty(item.processInstanceId)) {
	//				workflow.processInstanceId = item.procInsId;
	//			} else {
	//				workflow.processInstanceId = item.processInstanceId;
	//			}
	//			if(isNullOrEmpty(item.processKey)) {
	//				workflow.processDefKey = item.procDefKey;
	//			} else {
	//				workflow.processDefKey = item.processKey;
	//			}
	//			workflow.text1 = item.text1;
	//			workflowList.push(workflow);
	//		});
	//		if(!checkResult) {
	//			return checkResult;
	//		}
	//		// 清空删除原因的内容
	//		$("#deletereason").val('');
	//		/**
	//		 * 判断是否需要填写删除原因
	//		 */
	//		      $.ajax({
	//		          type: "GET",
	//		          url: getContextPath() + "/rest/v1.0/workflow/process-instances/getDeleteReason",
	//		          success: function (data) {
	//		              if (data == '1') {
	//		                  var logoutIndex = layer.open({
	//		                      title: '删除',
	//		                      type: 1,
	//		                      area: ['430px'],
	//		                      btn: ['确定', '取消'],
	//		                      content: $('#delete-reason')
	//		                      ,yes: function(index, layero){
	//		                          var reason = $("#deletereason").val();
	//		                          if (isNullOrEmpty(reason)) {
	//		                              layer.msg('请输入删除原因!');
	//		                              return false;
	//		                          }
	//		                          //提交 的回调
	//		                          addModel('删除中');
	//		                          layer.close(index);
	//		                          for (var index in workflowList) {
	//		                              workflowList[index].reason = reason;
	//		                          }
	//		                          $.ajax({
	//		                              type: "DELETE",
	//		                              url: getContextPath() + "/rest/v1.0/workflow/process-instances",
	//		                              contentType: "application/json;charset=utf-8",
	//		                              dataType: "json",
	//		                              data: JSON.stringify(workflowList),
	//		                              success: function (data) {
	//		                                  if (isIndex) {
	//		                                      layer.msg('删除成功');
	//		                                      renderTable(url, tableId, currentId);
	//		                                  } else {
	//		                                      layer.msg('删除成功，即将关闭当前页。');
	//		                                      setTimeout(function(){ window.close(); }, 1000);
	//		                                  }
	//		                              }, error: function (e) {
	//		                                  response.fail(e, '')
	//		                              }, complete: function () {
	//		                                  removeModal();
	//		                              }
	//		                          });
	//		                          }
	//		                      ,btn2: function(index, layero){
	//		                          //取消 的回调
	//		                          layer.close(index);
	//		                      }
	//		                  });
	//		              } else {
	//		                  var logoutIndex = layer.confirm('您确定要删除吗？', {
	//		                      title: '提示',
	//		                      btn: ['确定', '取消'] //按钮
	//		                  }, function (index) {
	//		                      addModel('删除中');
	//		                      layer.close(index);
	//		                      $.ajax({
	//		                          type: "DELETE",
	//		                          url: getContextPath() + "/rest/v1.0/workflow/process-instances",
	//		                          contentType: "application/json;charset=utf-8",
	//		                          dataType: "json",
	//		                          data: JSON.stringify(workflowList),
	//		                          success: function (data) {
	//		                              if (isIndex) {
	//		                                  layer.msg('删除成功');
	//		                                  renderTable(url, tableId, currentId);
	//		                              } else {
	//		                                  layer.msg('删除成功，即将关闭当前页。');
	//		                                  setTimeout(function(){ window.close(); }, 1000);
	//		                              }
	//		                          }, error: function (e) {
	//		                              response.fail(e, '')
	//		                          }, complete: function () {
	//		                              removeModal();
	//		                          }
	//		                      });
	//		                  });
	//		              }
	//		          }, error: function (e) {
	//		              response.fail(e, '')
	//		          }, complete: function () {
	//		              removeModal();
	//		          }
	//		      });
	//	}

	//激活流程
	function activeProcess(checkStatus, url, tableId, currentId) {
		addModel('激活中');
		$.ajax({
			type: "POST",
			url: getContextPath() + "/index/unPostTask",
			traditional: true,
			data: {
				taskid: checkStatus.data[0].taskId,
			},
			success: function (data) {
				$("#gqjgyy").val("");
				layer.msg(data.result + '已激活');
				renderTable(url, tableId, currentId);
			},
			error: function (e) {
				response.fail(e, '')
			},
			complete: function () {
				removeModal();
			}
		});
	}
	//	function activeProcess(checkStatus, url, tableId, currentId) {
	//		var processInstanceIds = [];
	//		var checkResult = true;
	//		$.each(checkStatus.data, function(key, item) {
	//			if(!checkStaus(item, 4, '激活')) {
	//				checkResult = false;
	//				return checkResult;
	//			}
	//			processInstanceIds.push(item.processInstanceId);
	//		});
	//		if(!checkResult) {
	//			return checkResult;
	//		}
	//		//小弹出层
	//		layer.open({
	//			title: '任务激活',
	//			type: 1,
	//			area: ['430px'],
	//			btn: ['激活', '取消'],
	//			content: $('#bdc-popup-textarea'),
	//			yes: function(index, layero) {
	//				var reason = $("#gqjgyy").val();
	//				if(isNullOrEmpty(reason)) {
	//					layer.msg('请输入激活原因');
	//				} else {
	//					addModel('激活中');
	//					// 激活当前任务
	//					$.ajax({
	//						type: "POST",
	//						url: getContextPath() + "/rest/v1.0/workflow/process-instances/activate",
	//						traditional: true,
	//						data: {
	//							processInstanceIds: processInstanceIds,
	//							reason: reason
	//						},
	//						success: function(data) {
	//							layer.close(index);
	//							$("#gqjgyy").val("");
	//							layer.msg(data);
	//							renderTable(url, tableId, currentId);
	//						},
	//						error: function(e) {
	//							response.fail(e, '')
	//						},
	//						complete: function() {
	//							removeModal();
	//						}
	//					});
	//				}
	//			},
	//			btn2: function(index, layero) {
	//				$("#gqjgyy").val("");
	//			},
	//			cancel: function() {
	//				$("#gqjgyy").val("");
	//			},
	//			success: function() {
	//				$("#gqjgyyLab").html('<span class="required-span"><sub>*</sub></span>激活原因');
	//			}
	//		});
	//	}

	//判断是否可以办结
	function isEnd(taskId) {
		var isEnd = false;
		//获取转发按钮还是办结按钮
		$.ajax({
			type: "GET",
			async: false,
			url: getContextPath() + "/rest/v1.0/workflow/process-instances/forward/isEnd",
			data: {
				taskId: taskId
			},
			success: function (data) {
				isEnd = data;
			},
			error: function (e) {
				response.fail(e, '')
			}
		});
		return isEnd;
	}

	//办结
	function endProcess(checkData, url, tableId, currentId, isIndex) {
		var end = false;
		addModel('办结中');
		$.ajax({
			type: "POST",
			url: getContextPath() + "/rest/v1.0/workflow/process-instances/end",
			data: { taskId: checkData.taskId },
			async: false,
			success: function () {
				if (isIndex == 'true') {
					layer.msg("办结成功！");
					renderTable(url, tableId, currentId);
				} else {
					layer.msg('办结成功，即将关闭当前页。');
					setTimeout(function () { window.close(); }, 1000);
				}
			},
			error: function (e) {
				response.fail(e, '')
			},
			complete: function () {
				removeModal();
			}
		});
		return end;
	}

	function checkStaus(checkData, staus, type) {
		var result = false;
		if ((checkData.state == 1 || checkData.state == 4 || isNullOrEmpty(checkData.state)) && (forbiddenActive.indexOf(staus) != -1)) {
			layer.msg('受理编号为' + checkData.text1 + '的流程已激活,不能进行' + type + '!');
		} else if (checkData.state == 2 && (forbiddenHangUp.indexOf(staus) != -1)) {
			layer.msg('受理编号为' + checkData.text1 + '的流程已挂起,不能进行' + type + '!');
		} else if (checkData.state == 3 && (forbiddenClock.indexOf(staus) != -1)) {
			layer.msg('受理编号为' + checkData.text1 + '的流程已办理,不能进行' + type + '!');
		} else if (isNullOrEmpty(checkData.state) && checkData.procStatus == 3) { //项目列表的特殊处理
			layer.msg('受理编号为' + checkData.text1 + '的流程已挂起,不能进行' + type + '!');
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * 规则验证
	 * @param checkData
	 * @returns {boolean}
	 */
	function turnCheck(checkData, mbtype) {
		var check = false;
		$.ajax({
			url: getContextPath() + '/rest/v1.0/check/gzyz/' + checkData.processKey + '/' + checkData.processInstanceId,
			type: 'POST',
			data: { mbtype: mbtype, taskid: checkData.taskId },
			async: false,
			success: function (data) {
				check = data.length == 0 ? true : data;
			},
			error: function (e) {
				if (e.status == 500) {
					var responseText = JSON.parse(e.responseText);
					if (responseText.code == 101) {
						// 规则验证未找到要验证的规则错误 忽略
						check = true;
					}
				}
				if (!check) {
					response.fail(e, '');
				}
			}
		});
		return check;
	}

	/**
	 * 验证必填项
	 */
	function checkBtx(formViewKey, gzlslid, taskId) {
		var check = false;
		$.ajax({
			url: getContextPath() + '/rest/v1.0/check/btxyz/' + formViewKey + '/' + gzlslid,
			type: 'POST',
			data: { taskId: taskId },
			async: false,
			success: function (data) {
				if (data == '') {
					check = true;
				} else {
					var msg = '';
					var bdmc = [];
					$.each(data, function (key, val) {
						msg += '<p>' + val.yzxx + '</p>';
						bdmc.push(val.bdmc);
					});
					//新增 点击 转发 弹出层
					var deleteIndex = layer.open({
						type: 1,
						skin: 'bdc-small-tips',
						title: '错误提示',
						area: ['320px'],
						content: '<h3 style="line-height: 36px;">以下表单有为空项，请填写完整</h3><p>' + bdmc.join("、") + '</p>',
						btn: ['确定'],
						btnAlign: 'c',
						yes: function () {
							//确定操作
							layer.close(deleteIndex);
						}
					});
					//点击 转发 不为空内容提示
					$('.bdc-null-tips-box').removeClass('bdc-hide');
					//循环新增内容
					$('.bdc-null-tips-box .bdc-close').on('click', function () {
						$('.bdc-null-tips-box').addClass('bdc-hide');
					});
					$('#nullTips').html(msg);
				}
			},
			error: function (e) {
				response.fail(e, '')
			}
		});
		return check;
	}

	function showHangReson(checkData) {
		var result = true;
		if (checkData.state == 2 || checkData.procStatus == 3) {
			$.ajax({
				url: getContextPath() + '/rest/v1.0/workflow/process-instances/opinion',
				type: 'POST',
				data: {
					processInstanceId: checkData.processInstanceId == undefined ? checkData.procInsId : checkData.processInstanceId,
					type: 'suspend_opinion'
				},
				async: false,
				success: function (data) {
					var name = data.userAlisa;
					var opinion = data.opinion;
					var msg = '流程已挂起';
					if (name != '' && name != undefined) {
						msg = msg + '，挂起人：' + name;
					}
					if (opinion != '' && opinion != undefined) {
						msg = msg + '，挂起原因：' + opinion;
					}
					layer.msg(msg, { area: ['auto', 'auto'] });
				},
				error: function (e) {
					response.fail(e, '')
				},
				complete: function () {
					result = false;
				}
			});
		}
		return result;
	}

	function queryOpinion(processInstanceId, taskId, type) {
		var result = '';
		var user = queryUserType(type);
		$.ajax({
			url: getContextPath() + '/rest/v1.0/workflow/process-instances/opinion',
			type: 'POST',
			data: { taskId: taskId, processInstanceId: processInstanceId, type: type },
			async: false,
			success: function (data) {
				var opinion = data.opinion;
				if (opinion != '' && opinion != undefined) {
					result += user.opinionType + '：' + opinion;
				}
			},
			error: function (e) {
				response.fail(e, '')
			}
		});
		return result;
	}

	function lockProcess(checkData) {
		if (checkData.state != 3) {
			// 锁定当前任务
			//$.ajax({
			//	type: "post",
			//	url: getContextPath() + "/rest/v1.0/workflow/process-instances/lock",
			//	data: {
			//		taskId: checkData.taskId
			//	},
			//	success: function (data) { },
			//	error: function (e) { }
			//});
		}
	}

	function queryUserType(type) {
		var user = {};
		if (type == 'suspend_opinion') {
			user.userType = '挂起人';
			user.opinionType = '挂起原因';
		} else if (type == 'back_opinion') {
			user.userType = '退回人';
			user.opinionType = '退回原因';
		} else {
			user.userType = '';
			user.opinionType = '';
		}
		return user;
	}

	function loadTsxx(data) {
		var confirmInfo = '';
		var alertInfo = '';
		var showIgnoreAll = true;
		$.each(data, function (index, item) {
			if (item.yzlx === 1) {
				confirmInfo += '<p><img src="../static/lib/bdcui/images/warn.png" alt="">' + item.tsxx + '<a href="javascript:;" class="confirmRemove">忽略</a></p>';
			} else if (item.yzlx === 3 || item.yzlx === 4) {
				if (showIgnoreAll) {
					showIgnoreAll = false;
				}
				// 添加 a 标签，防止 tsxx 为空时，出现压盖现象
				alertInfo += '<p><img src="../static/lib/bdcui/images/error.png" alt="">' + item.tsxx + '<a href="javascript:;" ></a></p>';
			}
		});
		var tsxxHtml = '';
		if (showIgnoreAll) {
			tsxxHtml += '<a class="layui-btn layui-btn-sm bdc-ignore-btn" id="ignoreAll"">忽略全部</a>';
		}
		tsxxHtml += '<div class="bdc-right-tips-box" id="bottomTips">';
		tsxxHtml += alertInfo;
		tsxxHtml += confirmInfo;
		tsxxHtml += '</div>';
		return tsxxHtml;
	}

	//批量转发提示忽略信息
	function loadPlTsxx(data) {
		var tsxxHtml = '<a class="layui-btn layui-btn-sm bdc-ignore-btn" id="ignoreAll">忽略全部</a>' +
			'<div class="bdc-right-tips-box" >';
		data.forEach(function (v) {
			if (v.bdcGzyzVOS != null) {
				var confirmInfo = '';
				var alertInfo = '';
				var isIgnore = true;
				v.bdcGzyzVOS.forEach(function (item) {
					if (item.yzlx === 1) {
						confirmInfo += '<p><img src="../static/lib/bdcui/images/warn.png" alt="">' + item.tsxx + '</p>';
					} else if (item.yzlx === 3 || item.yzlx === 4) {
						isIgnore = false;
						// 添加 a 标签，防止 tsxx 为空时，出现压盖现象
						alertInfo += '<p><img src="../static/lib/bdcui/images/error.png" alt="">' + item.tsxx + '<a href="javascript:;" ></a></p>';
					}
				});
				if (isIgnore) {
					workflowPlList.forEach(function (value) {
						if (value.text1 == v.slbh) {
							value.ignore = true;
							plIgnoreList.push(value);
						}
					});
					tsxxHtml += '<div class="title-area bdc-ignore-content"><p class="tsxx-title">受理编号</p><span>' + v.slbh + '</span>';
				} else {
					tsxxHtml += '<div class="title-area"><p class="tsxx-title">受理编号</p><span>' + v.slbh + '</span>';
				}
				tsxxHtml += alertInfo;
				tsxxHtml += confirmInfo;
				tsxxHtml += '</div>'
			}
		});

		tsxxHtml += '</div>';
		return tsxxHtml;
	}

	var warnLayer;

	function rightShowWarn(tsxxHtml, checkData, url, tableId, currentId, isIndex, processInstanceId) {
		warnLayer = layer.open({
			type: 1,
			title: '提示信息',
			anim: -1,
			skin: 'bdc-tips-right bdc-error-layer',
			shade: [0],
			area: ['600px'],
			offset: 'r',
			content: tsxxHtml,
			time: 5000000, //2秒后自动关闭
			success: function () {
				var pSize = $("#bottomTips > p").size();
				var confirmSize = $(".confirmRemove").size();
				var alertSize = pSize - confirmSize;
				$('.bdc-tips-right .bdc-right-tips-box .layui-icon-close').on('click', function () {
					layer.close(warnLayer);
				});
				$('.confirmRemove').click(function () {
					$(this).parent().remove();
					//在没有警告提示下创建
					if (alertSize == 0 && $(".confirmRemove").size() == 0) {
						addModel();
						forward(checkData, url, tableId, currentId, isIndex, processInstanceId);
						// 避免转发页面不展示将关闭操作放到后面执行
						layer.close(warnLayer);
					}
				});

				$('#ignoreAll').click(function () {
					if (alertSize > 0) {
						layer.msg("警告信息不能忽略！");
						return false;
					}
					//点击忽略时增加日志记录，同步处理
					var data = this.nextElementSibling.innerText.replace(/忽略/g, "").replace(/查看/g, "");
					addRemoveLog(data);
					addModel();
					forward(checkData, url, tableId, currentId, isIndex, processInstanceId);
					// 避免转发页面不展示将关闭操作放到后面执行
					layer.close(warnLayer);
				});

				setTimeout(function () {
					$('.bdc-error-layer').css('opacity', 1)
				}, 500);
			}
		});
	}

	//忽略增加日志记录
	function addRemoveLog(data) {
		getReturnData("/rest/v1.0/check/addIgnoreLog", { data: data }, "POST", function () { }, function () { })
	}

	exports("workflow", workflow);

	/**
	 * 特殊规则验证
	 * @param checkMb
	 * @param gzlslid
	 * @returns {boolean}
	 */
	function tsGzYz(checkMb, gzlslid) {
		var check = false;
		$.ajax({
			url: getContextPath() + '/rest/v1.0/check/tsgzyz/' + checkMb + '/' + gzlslid,
			type: 'GET',
			async: false,
			success: function (data) {
				check = data.length == 0 ? true : data;
			},
			error: function (e) {
				if (e.status == 500) {
					var responseText = JSON.parse(e.responseText);
					if (responseText.code == 101) {
						// 规则验证未找到要验证的规则错误 忽略
						check = true;
					}
				}
				if (!check) {
					response.fail(e, '');
				}
			}
		});
		return check;
	}
});