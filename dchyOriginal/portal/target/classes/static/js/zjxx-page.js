layui.use(['element', 'jquery', 'tree', 'table','layer','form'], function() {
	let element = layui.element,
		$ = layui.jquery,
		tree = layui.tree,
		table = layui.table,
	    layer = layui.layer,
		form = layui.form;

	let parentNodeHeight = document.documentElement.clientHeight;
	$(".service-header-tab>.layui-tab-content").height(parentNodeHeight - 46 + "px");

	var maxheight = parentNodeHeight - 550;

	let parentNodeWidth = document.documentElement.clientWidth;
	$(".bdc-tab-aside .layui-tab-content").width(parentNodeWidth - 180 + "px");
	//首页四棵树的数据
	let checkTreeOne = [
		{
			title: '成果完整性检查',
			id: 1,
			spread: true,
			children: [{
				title: '目录及文件完整性检查',
				id: 1000
			}]
		}
	];

	let checkTreeTwo = [
		{
			title: '数据格式正确性检查',
			id: 1,
			spread: true,
		}
	];

	let checkTreeThree = [
		{
			title: '数据有效性检查',
			id: 1,
			spread: true,
		}
	];

	let checkTreeFour = [
		{
			title: '数据完整性检查',
			id: 1,
			checked: true,
			spread: true,
			children: [
				{
					title: '数字基础检查',
					id: 10001
				},
				{
					title: '要素完整性检查',
					id: 10002
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				},
				{
					title: '数据存在性检查',
					id: 10003
				}]
		}
	];
	//质检详情树形菜单
	let zjxqTreeMenudata = [
		{
			title: '要素正确性检查',
			id: 1,
			checked: true,
			spread: true,
			children: [{
				title: '要素正确性检查1',
				id: 1000,
				children: [{
					title: '要素正确性检查1-1',
					id: 10001
				}, {
					title: '要素正确性检查1-2',
					id: 10002
				}]
			}]
		},
		{
			title: '空间数据基本检查',
			id: 2,
			children: [{
				title: '数学基础检查',
				id: 2000
			}, {
				title: '要素完整性检查',
				id: 2001,
				children: [
					{ title: '图层完整性', id: 20010, }
				]
			}]
		},
		{
			title: '要素正确性检查',
			id: 3,
			children: [{
				title: '要素正确性检查1',
				id: 3000
			}, {
				title: '要素正确性检查2',
				id: 3001,
				children: [
					{ title: '图层完整性', id: 30010, }
				]
			}]
		}
	];
	//总览的四棵树
	tree.render({
		elem: '#checkTreeOne',
		data: checkTreeOne,
		accordion: true,
		showLine: false, //是否开启连接线
		//		click: function(obj) {
		//			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
		//		}
	});

	tree.render({
		elem: '#checkTreeTwo',
		data: checkTreeTwo,
		accordion: true,
		showLine: false, //是否开启连接线
		//		click: function(obj) {
		//			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
		//		}
	});

	tree.render({
		elem: '#checkTreeThree',
		data: checkTreeThree,
		accordion: true,
		showLine: false, //是否开启连接线
		//		click: function(obj) {
		//			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
		//		}
	});

	tree.render({
		elem: '#checkTreeFour',
		data: checkTreeFour,
		accordion: true,
		showLine: false, //是否开启连接线
		//		click: function(obj) {
		//			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
		//		}
	});


	tree.render({
		elem: '#check-tree',
		data: zjxqTreeMenudata,
		accordion: true,
		showLine: false, //是否开启连接线
		//		click: function(obj) {
		//			console.log('当前点击的节点数据:', obj.data); //得到当前点击的节点数据
		//		}
	});


	//核查结果
	let zjxqHcjgData = {
		tcData: [
			{ 'cwdj': '一般错误', 'zdmc': 'AAAAA', 'cwtx': '50', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '是', 'lwyy': '' },
			{ 'cwdj': '一般错误', 'zdmc': 'AAAAA', 'cwtx': '50', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '是', 'lwyy': '' },
			{ 'cwdj': '一般错误', 'zdmc': 'AAAAA', 'cwtx': '50', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '是', 'lwyy': '' },
			{ 'cwdj': '一般错误', 'zdmc': 'AAAAA', 'cwtx': '50', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '是', 'lwyy': '' }
		],
		jlData: [
			{ 'cwdj': '严重错误', 'zdmc': 'BBBBB', 'cwtx': '96', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '否', 'lwyy': '' },
			{ 'cwdj': '严重错误', 'zdmc': 'BBBBB', 'cwtx': '96', 'jcsj': '20200209', 'cwms': '城镇开发边界图层', 'sflw': '否', 'lwyy': '' }
		]
	}
	$('#hcjg-item-tab').on('click', 'a', function() {
		$(this).css('color', '#1d87d1').siblings().css('color', '#333');
	});


	//质检详情
	function zjxq(){
		table.render({
			elem: '#hcjg-table',
			width:parentNodeWidth - 20 -180,
			cols: [[
				{ field: 'cwdj', title: '错误等级', align: 'center' },
				{ field: 'zdmc', title: '字段名称', align: 'center' },
				{ field: 'cwtx', title: '错误图形', align: 'center' },
				{ field: 'jcsj', title: '检查时间', align: 'center' },
				{ field: 'cwms', title: '错误描述', align: 'center' },
				{ field: 'sflw', title: '是否例外', align: 'center' },
				{ field: 'lwyy', title: '例外原因', align: 'center' }
			]],
			data: zjxqHcjgData.tcData
		});
	}
	zjxq();

	$(".content-bottom .layui-table-view").css("max-height",maxheight+"px");
	$(".content-bottom .layui-table-view").css("overflow","auto");

	//其他错误表格
	function qtcw(){
		table.render({
			elem: '#otherError-table',
			toolbar:'#otherErrorToolbarBtn',
			width:parentNodeWidth - 20 -180,
			// defaultToolbar: [],
			// page:true,
			// url:"http://192.168.50.38:8085/msurveyplat-server/v1.0/qualitycheck/getQtcw?",
			// method:"post",
			// request: {
			// 	pageName: 'page' //页码的参数名称，默认：page
			// 	,limitName: 'size' //每页数据量的参数名，默认：limit
			// },
			// where:{xmid: '1'},
			// parseData:function(res){
			// 	return {
			// 		"code": "0000", //解析接口状态
			// 		"msg": "", //解析提示文本
			// 		"count": res.total, //解析数据长度
			// 		"data": res.totalElements //解析数据列表
			// 	};
			// },
			cols: [[ //表头
				{ type:'checkbox'},
				{ type: 'numbers', title: '序号', align: 'center'},
				{ field: 'cwms', title: '错误描述', align: 'center'},
				// { field: 'cwdj', title: '错误等级', align: 'center',templet:'#cwdjTpl'},
				{ field: 'cwdj', title: '错误等级', align: 'center'},
				{ field: 'cz', title: '操作', align: 'center' ,templet:'#uploadTpl'}
			]],
			data: [
				{'cwms':'描述','cwdj':''},
				{'cwms':'描述','cwdj':''},
				{'cwms':'描述','cwdj':''},
				{'cwms':'描述','cwdj':''}
			]
		});
	}
	qtcw();

	//其他错误新增 删除 修改 操作
	//头工具栏事件
	table.on('toolbar(otherError-table)', function(obj){
		var checkStatus = table.checkStatus(obj.config.id);
		var length = checkStatus.data.length;
		switch(obj.event){
			case 'addBtn':
				addqtcw();
				break;
			case 'deleteBtn':
				if(length<1){
					layer.alert("请选择至少一条数据");
				}else{
					deleteqtcw(checkStatus.data);
				}
				break;
			case 'saveBtn':
				if(length>1){
					layer.alert("只可选择一条数据进行修改");
				}else if(length<1){
					layer.alert("请选择一条数据");
				}else{
					saveqtcw(checkStatus.data);
				}
				break;
		}
	});

	//其他错误新增
	function addqtcw() {
		var content = '<div class="addqtcwmodal"><form class="layui-form layui-form-pane" action="">' +
			'<div class="layui-form-item">' +
			'\t\t<label class="layui-form-label">错误等级</label>' +
			'\t\t<div class="layui-input-block">' +
			'\t\t\t<select name="interest" lay-filter="aihao">' +
			'\t\t\t\t<option value=""></option>' +
			'\t\t\t\t<option value="0">一级</option>' +
			'\t\t\t\t<option value="1">二级</option>' +
			'\t\t\t\t<option value="2">三级</option>' +
			'\t\t\t\t<option value="3">四级</option>' +
			'\t\t\t</select>' +
			'\t\t</div>' +
			'</div>' +
			'<div class="layui-form-item layui-form-text">' +
			'\t\t<label class="layui-form-label">错误描述</label>' +
			'\t\t<div class="layui-input-block">' +
			'\t\t\t<textarea placeholder="请输入内容" class="layui-textarea"></textarea>' +
			'\t\t</div>' +
			'</div> ' +
			' <div class="layui-form-item" style="text-align: center">\n' +
			'    <input id="addqtcwConfirm" class="addqtcw-confirm" value="确定" style="width: 75px;font-size: 14px;padding: 5px;text-align: center;background: #1d87d1;color: #ffffff;border: none;">' +
			'  </div>'+
			'</form></div>';
		layer.open({
			type: 1,
			title:"新增其他错误",
			skin: 'layui-layer-rim', //加上边框
			area: ['420px', '330px'], //宽高
			content: content
		});
		form.render();
	}

	//其他错误删除
	function deleteqtcw(data){
		layer.confirm('确定删除？', function(index){
			debugger
		});
	}

	//其他错误修改
	function saveqtcw(data){
		var content = '<div class="addqtcwmodal"><form class="layui-form layui-form-pane" action="">' +
			'<div class="layui-form-item">' +
			'\t\t<label class="layui-form-label">错误等级</label>' +
			'\t\t<div class="layui-input-block">' +
			'\t\t\t<select name="interest" lay-filter="aihao">' +
			'\t\t\t\t<option value=""></option>' +
			'\t\t\t\t<option value="0">一级</option>' +
			'\t\t\t\t<option value="1">二级</option>' +
			'\t\t\t\t<option value="2">三级</option>' +
			'\t\t\t\t<option value="3">四级</option>' +
			'\t\t\t</select>' +
			'\t\t</div>' +
			'</div>' +
			'<div class="layui-form-item layui-form-text">' +
			'\t\t<label class="layui-form-label">错误描述</label>' +
			'\t\t<div class="layui-input-block">' +
			'\t\t\t<textarea placeholder="请输入内容" class="layui-textarea">'+data[0].cwms+'</textarea>' +
			'\t\t</div>' +
			'</div> ' +
			' <div class="layui-form-item" style="text-align: center">\n' +
			'    <input id="saveqtcwConfirm" class="addqtcw-confirm" value="确定" style="width: 75px;font-size: 14px;padding: 5px;text-align: center;background: #1d87d1;color: #ffffff;border: none;">' +
			'  </div>'+
			'</form></div>';
		layer.open({
			type: 1,
			title:"修改其他错误",
			skin: 'layui-layer-rim', //加上边框
			area: ['420px', '330px'], //宽高
			content: content
		});
		form.render();
	}
})





