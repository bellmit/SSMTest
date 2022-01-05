<html>
<head>
    <title>应用服务器日志浏览</title>
    <meta name="tab" content="logging"/>
</head>
<body>
<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="#" onclick="history.back();"><i class="icon-tasks"></i>应用服务器日志列表</a>
        <a class="current" href="#">日志浏览</a>
    </div>
</div><!-- /#breadcrumbWrapper -->
<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	<div class="row-fluid">
	
		<div class="widget-box">
			
			<div class="widget-title">
			    <h5>${log.file.name}</h5>
			</div><!-- widget-title -->
			
			<div class="widget-content">
				<div style="border-bottom: 1px solid #ddd;padding-bottom: 10px;">
					<strong class="text-primary">文件大小: </strong>${log.size}&nbsp;&nbsp;&nbsp;<strong>最后修改：</strong>${log.lastModified}
				</div>
				<br />
				<div id="J_CTN_LOG" style="max-height:500px;overflow:auto;">
					
				</div>
				<br />
				<div><a id="J_BTN_GET_MORE" href="${base}/console/log/webserver/ajax/detail?fileLocation=${log.file.absolutePath}&current=0"
				data-result-ctn="#J_CTN_LOG(append)"
				class="btn btn-block btn-large j_ajax_for_data">加载更多</a></div>
			</div><!-- /.widget-content -->
		
		</div><!-- /.widget-box -->
	
	</div><!-- /.row -->

</div><!-- /.container-fluid -->
<script>
function changeUrl( current , hasMore ){
	if( !hasMore ) {
		$('#J_BTN_GET_MORE').addClass('hide');
	}
	var href = $('#J_BTN_GET_MORE').attr('href'),
		i = href.lastIndexOf('=');
	$('#J_BTN_GET_MORE').attr('href', href.substring(0, i+1) + current);
}
window.onload = function(){
	$('#J_BTN_GET_MORE').click();
}
</script>
</body>
</html>