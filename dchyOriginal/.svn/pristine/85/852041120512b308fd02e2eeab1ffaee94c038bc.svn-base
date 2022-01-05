<html>
<head>
    <title>日志管理</title>
    <meta name="tab" content="logging"/>
    <meta name="js" content="${base}/static/js/page_map_edit.js" />
</head>
<body>

<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	<div class="row-fluid">
	
		<div class="widget-box">
			
			<div class="widget-title">
			    <ul class="nav nav-tabs">
					<li>
						<a href="${base}/console/log/arcgis/query">ArcGIS服务器</a>
					</li>
					<li class="active">
						<a href="${base}/console/log/webserver/query">应用服务器</a>
					</li>
					<li>
						<a href="${base}/console/log/audit/query">用户行为</a>
					</li>
				</ul>
			</div><!-- widget-title -->
		
			<div class="widget-content">
				<div class="buttons-opt-wrap title-wrapper">
					<!-- <h6>应用服务器日志列表</h6> -->
					<div style="margin-bottom: 10px;">
						<form id="J_FORM_ARC_Q" action="${base}/console/log/webserver/query" class="form-inline" method="get">
							<label for="startTime">起始时间 <input id="startTime" type="text" name="startTime" readonly class="datepicker" style="width:130px" value="${startTime!}"/></label>
							<label for="endTime">结束时间 <input id="endTime" type="text" name="endTime" readonly class="datepicker" style="width:130px" value="${endTime!}"/></label>
							<label for="fileName">关键字 <input type="text" id="fileName" name="fileName" style="width:130px" value="${cfg.fileName!}"/></label>
							<label for="directory">选择目录
								<select name="server" id="directory" style="width:100px">
									<option value="default" selected>默认</option>
									<#list servers as server>
										<option value="${server}" <#if cfg.directory?? && cfg.directory==server>selected</#if>  >${server}</option>
									</#list>
								</select>
							</label>
							<button class="btn btn-primary">查询</button>
						</form>
					</div>
				</div>
				<table class="table table-bordered table-condensed table-hover">
						<#if logFiles?? && (logFiles?size>0)>
							<thead>
								<tr>
									<th>文件名称</th>
									<th>文件大小</th>
									<th>最后修改</th>
								</tr>
							</thead>
							<tbody>
							<#list logFiles?sort_by("lastModified") as log>
							<tr>
								<td><a href="${base}/console/log/webserver/detail?fileLocation=${log.file.absolutePath}">${log.file.name}</#></td>
								<td><div class="fn-tc">${log.size}</div></td>
								<td><div class="fn-tc">${log.lastModified}</div></td>
							</tr>
							</#list>
						<#else>
						<tr>
							<td colspan="3"><p class="fn-tc"><br />查无对应结果<br /><br /></p></td>
						</tr>
						</#if>
					</tbody>
				</table>
			</div><!-- /.widget-content -->
		
		</div><!-- /.widget-box -->
	
	</div><!-- /.row -->

</div><!-- /.container-fluid -->

</body>
</html>