<html>
<head>
    <title>地图授权</title>
    <meta name="tab" content="map"/>
    <meta name="js" content="${base}/static/js/module_grant.js" />
</head>
<body>

<div id="breadcrumbWrapper">
	<div id="breadcrumb">
		<a href="${base}"><i class="icon-tasks"></i>服务列表</a>
		<a class="current">服务授权</a>
	</div>
</div><!-- /#breadcrumbWrapper -->

<div class="container-fluid">
	<#include "../../common/ret.ftl" />
	<div class="row-fluid">
		<div style="position: relative;">
			<h3>${map.name} <small>服务授权</small></h3>
			<div style="position:absolute;right:0;top:10px;">
				<a id="J_BTN_SAVE_ALL" href="#" class="btn btn-primary hide" onclick="">保存全部</a>
			</div>
		</div>
		<div class="widget-box">
			<div class="widget-content nopadding">
				<table class="table table-bordered table-striped">
                        <thead><tr>
                            <th width="20%">名称</th>
                            <th width="20%">描述</th>
                            <th>地图访问方式</th>
                            <th width="30%">操作</th>
                        </tr></thead>
                        <tbody>
                        	<#list roles?sort_by('id') as r>
                        	<tr class="<#if r.enabled>j_enabled<#else>j_disabled</#if>">
	                        	<td><a href="#">${r.name!}</a></td>
	                        	<td>${r.description!}</td>
	                            <td class="label-wrap">
	                            	<form action="${base}/console/auth/map/ajax/grant" method="post">
		                            	<#list mapOpts as mapOpt>
		                            		<#assign checked="" />
		                            		<#list mapAcls as acl>
		                            			<#if (acl.roleId==r.id && acl.operation==mapOpt.mask)>
		                            				<#assign checked="checked" />
		                            			</#if>
		                            		</#list>
					                	<label for="" class="checkbox inline">
					                		<input type="checkbox" name="optIds" ${checked} value="${mapOpt.mask}"/> <span href="#" class="label label-info">${mapOpt.label!}</span>
					                	</label>
					                	</#list>
				                		<input type="hidden" name="mapId" value="${map.id}"/>
				                		<input type="hidden" name="roleId" value="${r.id}"/>
				                	</form>
				                </td>
	                            <td class="fn-tc">
				                	<label for="" class="checkbox inline"><input type="checkbox" class="j_check_all"/>全选</label>
				                	<a href="#" class="btn btn-mini btn-primary hide j_save">保存</a>
				                </td>
			                 </tr>
                        	</#list>
                        </tbody>
                    </table>
			</div><!-- /.widget-content -->
		
		</div><!-- /.widget-box -->
	
	</div><!-- /.row -->

</div><!-- /.container-fluid -->

</body>
</html>