<html>
	<head>
		<title>服务管理 - 服务编辑</title>
		<meta name="tab" content="map"/>
		<meta name="submenu" content="map_list"/>
		<meta name="js" content="${base}/static/js/page_map_edit.js" />
	</head>
	<body>
		<div id="breadcrumbWrapper">
			<div id="breadcrumb">
				<a href="${base}"><i class="icon-tasks"></i>服务列表</a>
				<a class="current"><#if map.id??>编辑服务<#else>新增服务</#if></a>
			</div>
		</div><!-- /#breadcrumbWrapper -->

		<div class="container-fluid">
			<#include "../../common/ret.ftl" />
			<div class="row-fluid">

				<div class="widget-box span12">
					<#if map.id??>
					<div class="widget-title">
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#J_BASE_INFO" data-toggle="tab">基本信息</a>
							</li>
							<li>
								<a href="${base}/console/provider/showProviders/${map.id}">服务提供者</a>
							</li>
							<li>
								<a href="${base}/console/layer/showLayers/${map.id}">图层信息</a>
							</li>
                            <li><a href="${base}/console/map/monitor?id=${map.id}">监控</a></li>
                        </ul>
                    </div><!-- widget-title -->
					</#if>

					<div class="widget-content">

						<div class="tab-content">

							<div class="tab-pane fade active in">

								<form id="J_FORM_EDIT" action="${base}/console/map/edit" method="post" class="form-horizontal">
									<div class="title-wrapper">
										<h6>基本信息</h6>
									</div>
									<#if map.id??>
									<div class="row-fluid">
										<div class="span12">
											<div class="control-group">
												<label for="" class="control-label"> 修改缩略图 </label>
												<div class="controls clearfix">
													<div id="J_NAIL_WRAP" class="nail-wrap" style="border:1px solid #ccc; background-color:#eee; padding:3px;width: 120px;height:80px; float:left;margin-right:10px;">
														<img src="${base}/thumbnail/${map.id}"/>
													</div>
													<div style="float:left;">
														<input id="J_IMG" name="img" type="file" />
														<span class="help-inline">仅支持.png和.jpg格式</span>
														<br /><br />
														<a id="J_UPLOAD_NAIL" href="${base}/console/map/ajax/uplNail?mapId=${map.id}" class="btn btn-primary btn-mini" style="margin-left:2px;">上传</a>
														&nbsp;<span class="text text-success fn-fb hide">上传成功!</span>
													</div>
												</div>
											</div>
										</div>
									</div>
									</#if>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="map-name" class="control-label">服务名称</label>
												<div class="controls">
													<input id="map-name" name="name" class="validate[required]" type="text" value="${map.name!}"/>
												</div>
											</div>
											<div class="control-group">
												<label for="map-name" class="control-label">服务别名</label>
												<div class="controls">
													<input id="map-name" name="alias" class="validate[required]" type="text" value="${map.alias!}"/>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="map-weight" class="control-label">服务顺序</label>
												<div class="controls">
													<input id="map-weight" name="weight" class="validate[required,custom[number]]" type="text" value="${map.weight!}"/>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="map-desc" class="control-label">服务描述</label>
												<div class="controls">
													<textarea id="map-desc" name="description" rows="3">${map.description!}</textarea>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="group-select" class="control-label">所属服务组</label>
												<div id="J_SELECT_GROUP_CTN" class="controls">
													<select id="group-select" name="groupId">
														<option value="">请选择</option>
														<#list group as g>
														<option value="${g.id}" <#if map.group?? && map.group.id == g.id>selected="selected"</#if>>${g.name}</option>
														</#list>
													</select>
													&nbsp;&nbsp;<a href="#J_NEW_GROUP_MODAL" data-toggle="modal" class="btn btn-mini ">新增服务组</a>
												</div>
											</div>
										</div>
									</div>
									
									<div class="title-wrapper">
										<h6>业务信息</h6>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="map-name" class="control-label">服务年份</label>
												<div class="controls">
													<input id="map-name" name="year" class="validate[required]" type="text" value="${map.year!}"/>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="map-weight" class="control-label">行政区代码</label>
												<div class="controls">
													<!-- <input id="map-weight" name="regionCode" class="validate[required]" type="text" value="${map.regionCode!}"/> -->
													<select name="regionCode" class="validate[required]" id="">
														<#if regions??>
														<#list regions as region>
															<option value="${region.id}" <#if map.regionCode??&&map.regionCode==region.id>selected</#if>>${region.name}${region.id}</option>
														</#list>
														</#if>
													</select>
												</div>
											</div>
										</div>
									</div>
									
									<div class="title-wrapper">
										<h6>空间信息</h6>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="xmin" class="control-label">空间参考</label>
												<div class="controls">
													<input id="wkid" name="wkid" class="validate[required]" type="text" value="${map.wkid!}"/>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="xmax" class="control-label">数据源</label>
												<div class="controls">
													<select name="dataSourceId">
														<option value="">请选择</option>
														<#list dss as ds>
														<option value="${ds.id}"${(map.dataSourceId?? && ds.id==map.dataSourceId)?string(' selected="selected"','')}>${ds.name}</option>
														</#list>
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="xmin" class="control-label">最小x</label>
												<div class="controls">
													<input id="xmin" class="validate[required,custom[number]]" name="extent.xmin" type="text" value="<#if map.extent??>${map.extent.xmin!}</#if>"/>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="xmax" class="control-label">最大x</label>
												<div class="controls">
													<input id="xmax" class="validate[required,custom[number]]" name="extent.xmax" type="text" value="<#if map.extent??>${map.extent.xmax!}</#if>"/>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="ymin" class="control-label">最小y</label>
												<div class="controls">
													<input id="ymin" class="validate[required,custom[number]]" name="extent.ymin" type="text" value="<#if map.extent??>${map.extent.ymin!}</#if>"/>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="ymax" class="control-label">最大y</label>
												<div class="controls">
													<input id="ymax" class="validate[required,custom[number]]" name="extent.ymax" type="text" value="<#if map.extent??>${map.extent.ymax!}</#if>"/>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<label for="minScale" class="control-label">最小比例尺</label>
												<div class="controls">
													<div class="pull-left" style="padding-top:3px;width: 15px">
														<strong>1:</strong>
													</div>
													<div style="margin-left: 15px;">
														<input id="minScale" style="max-width: 150px;" class="validate[custom[number]]" name="minScale" type="text" value="${map.minScale!}"/>
													</div>
												</div>
											</div>
										</div>
										<div class="span6">
											<div class="control-group">
												<label for="maxScale" class="control-label">最大比例尺</label>
												<div class="controls">
													<div class="pull-left" style="padding-top:3px;width: 15px">
														<strong>1:</strong>
													</div>
													<div style="margin-left: 15px;">
														<input id="maxScale" style="max-width: 150px;" class="validate[custom[number]]" name="maxScale" type="text" value="${map.maxScale!}"/>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12">
											<div class="control-group">
												<label for="support-svic" class="control-label">支持的服务</label>
												<div class="controls">
													<div class="label-wrap">
														<#if services??>
														<#list services?values as service>
														<a class="label label-info" href="${service.url!}" target="_blank" title="${service.serviceType.label}">${service.serviceType}</a>&nbsp;
														</#list>
														</#if>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="span12 form-actions">
											<#if map.id??>
											<input name="id" type="hidden" value="${map.id}"/>
											</#if>
											<a href="${base}" class="btn">返回</a>
											<button type="submit" class="btn btn-primary">
												保存
											</button>
										</div><!-- /.form-actions -->

									</div><!-- /.row-fluid -->

								</form>

							</div><!-- /.bp-addBaseInfo -->

						</div><!-- /.breadpane-ctn -->

					</div><!-- /.widget-content -->

				</div><!-- /.widget-box -->

			</div><!-- /.row -->

		</div><!-- /.container-fluid -->
		
		<div id="J_NEW_GROUP_MODAL" class="modal fade hide">
			<form id="J_FORM_ADD_GROUP" action="${base}/console/map/ajax/saveGroup" method="post" class="form-horizontal form-normal">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h3>新增服务组</h3>
				</div>
				<div class="modal-body">
					<div class="control-group">
						<label class="control-label">输入服务组名称</label>
						<div class="controls">
							<input type="text" class="validate[required]" name="name" value=""/>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<a id="J_BTN_ADD_GROUP" class="btn btn-primary">提交</a>
				</div>
			</form>
		</div>
		
	</body>
</html>