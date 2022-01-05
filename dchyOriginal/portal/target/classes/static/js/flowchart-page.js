layui.use(['element', 'jquery', 'table'], function() {
	element = layui.element;
	$ = layui.jquery;
	$(function() {
		var lctData = '';
        var taskId = parent.taskId== "undefined" ? '': parent.taskId;
        $.ajax({
			type: "get",
			url: getContextPath() + "/index/getCasUrl",
			async: true,
			success: function(data) {
                lctData = data;
				$('#lct-iframe').attr('src', data + '/platform/showchart.action?wiid=' + parent.gzlslId + '&taskid=' + taskId);
			}
		});
        //监听tab切换
        element.on('tab(flowchart-aside-tab)', function (d) {
            if(d.index==0){
                $('#lct-iframe').attr('src', lctData + '/platform/showchart.action?wiid=' + parent.gzlslId + '&taskid=' + taskId);
			}else {
                $('#lct-iframe').attr('src', lctData + '/platform/showflow!taskOverList.action?wiid=' + parent.gzlslId + '&taskid=' + parent.taskId);
            }
        });
	})

})