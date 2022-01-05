<html>
<head>
    <title>日志管理</title>
    <meta name="tab" content="logging"/>
    <meta name="js" content="${base}/static/js/module_logging.js" />
</head>
<body>

<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	<div class="row-fluid">
	
		<div class="widget-box">
			
			<div class="widget-title">
			    <ul class="nav nav-tabs">
					<li class="active">
						<a href="${base}/console/log/arcgis/query">ArcGIS服务器</a>
					</li>
					<li>
						<a href="${base}/console/log/webserver/query">应用服务器</a>
					</li>
					<li>
						<a href="${base}/console/log/audit/query">用户行为</a>
					</li>
				</ul>
			</div><!-- widget-title -->
		
			<div class="widget-content">
				<div class="title-wrapper buttons-opt-wrap">
					<!--<h6>arcgis服务器日志列表</h6>-->
					<div style="margin-bottom: 10px;">
						<form id="J_FORM_ARC_Q" action="${base}/console/log/arcgis/query" class="form-inline" method="get">
							<label for="startTime">起始日期 <input id="startTime" type="text" name="startTime" readonly class="datepicker" style="width:130px" value="${startTime!}"/></label>
							<!-- 
							<#if cfg.startTime?? && cfg.startTime!="" >value="${cfg.startTime?number?number_to_datetime}"</#if>
							<#if cfg.endTime?? && cfg.endTime!="" >value="${cfg.endTime?number?number_to_datetime}"</#if> -->
							<label for="endTime">结束日期 <input id="endTime" type="text" name="endTime" readonly class="datepicker" style="width:130px" value="${endTime!}"/></label>
							
							<label for="pageSize">每页条数 <input id="pageSize" type="text" name="pageSize" maxlength="3" style="width:30px" value="${cfg.pageSize!}"/></label>
							
							<label for="">类型
								<select name="level" id="" style="width:100px">
									<#if logLevels??>
										<#list logLevels as level>
										<option value="${level}" <#if cfg.level?? && cfg.level==level>selected</#if> >${level}</option>
										</#list>
									</#if>
								</select>
							</label>
							
							<label for="">服务器
								<select name="server" id="" style="width:100px">
									<#if servers??>
									<#list servers as s>
									<option value="${s}" <#if server==s>selected</#if>>${s}</option>
									</#list>
									</#if>
								</select>
							</label>
							<button class="btn btn-primary">查询</button>
							<a href="${base}/console/log/arcgis/clean" data-ask="在服务器上清空所有日志？" class="btn btn-mini btn-danger j_btn_del">清空所有日志</a>
						</form>
					</div>
				</div>
				<table class="table table-bordered table-condensed table-hover">
					<#if result?? && result.logMessages?? && (result.logMessages?size>0)>
					<thead>
						<tr>
							<th>类型</th>
							<th width="50%">描述信息</th>
							<th width="15%">时间</th>
							<th>目标</th>
							<th>方法名</th>
						</tr>
					</thead>
					<tbody>
						<#list result.logMessages as log>
						<tr>
							<td>${log.type!}</td>
							<!--<td><#if (log.message?? && log.message?length>20)>${log.message?substring(0,20)}<#else>${log.message!}</#if></td>-->
							<td>${log.message!}</td>
							<td>${log.time?datetime}</td>
							<td>${log.source}</td>
							<td>${log.methodName}</td>
						</tr>
						</#list>
					</tbody>
					<#else>
					<tr>
						<td colspan="5"><p class="fn-tc"><br />查无对应结果<br /><br /></p></td>
					</tr>
					</#if>
				</table>
			</div><!-- /.widget-content -->
		
		</div><!-- /.widget-box -->
	
	</div><!-- /.row -->

</div><!-- /.container-fluid -->

</body>
</html>