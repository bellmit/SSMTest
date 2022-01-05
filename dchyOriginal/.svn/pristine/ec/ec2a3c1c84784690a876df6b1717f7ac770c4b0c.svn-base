layui.use(['element', 'jquery', 'table'], function() {
	element = layui.element;
	$ = layui.jquery;
	$(function() {
		$.ajax({
			type: "get",
			url: getContextPath() + "/index/getCasUrl",
			async: true,
			success: function(data) {
				$('#lct-iframe').attr('src', data + '/platform/showchart!image.action?wiid=' + parent.gzlslId + '&taskid=' + parent.taskId);
				$('#lcxq-iframe').attr('src', data + '/platform/showflow!taskOverList.action?wiid=' + parent.gzlslId + '&taskid=' + parent.taskId);
			}
		});
	})

})