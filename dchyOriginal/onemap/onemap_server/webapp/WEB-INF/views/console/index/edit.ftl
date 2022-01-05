<html>
	<head>
		<title>服务管理 - 索引编辑</title>
		<meta name="tab" content="index"/>
		<meta name="js" content="${base}/static/js/page_index_index_edit.js" />
	</head>
	<body>
		<div id="breadcrumbWrapper">
			<div id="breadcrumb">
				<a href="${base}/console/index/index"><i class="icon-tasks"></i>索引列表</a>
				<a class="current"><#if idx.id??>编辑索引<#else>新建实例</#if></a>
			</div>
		</div><!-- /#breadcrumbWrapper -->

		<div class="container-fluid">
			
			<#include "../../common/ret.ftl" />
			
			<div class="row-fluid">

				<div class="widget-box span12">

					<div class="widget-content">

							<div id="J_BASE_INFO" class="tab-pane fade active in">

								<form id="J_FORM_EDIT" action="${base}/console/index/save" method="post" class="form-horizontal">
									<div class="row-fluid">
										<div class="span12">
											<div class="title-wrapper">
												<h6>基本信息</h6>
											</div>
											<div class="clearfix">
												<div class="span6">
													<div class="control-group">
														<label for="idx-name" class="control-label">索引名称</label>
														<div class="controls">
															<input id="idx-name" name="name" class="validate[required]" type="text" value="${idx.name!}"/>
														</div>
													</div>
												</div>
											</div>
											
										</div><!-- /.span12 -->

									</div><!-- /.row-fluid -->

									<div class="row-fluid">
										<div class="span12">
											<div class="title-wrapper">
												<h6>空间数据信息</h6>
											</div>
											<div class="control-group row-fluid">
												<label for="wkid" class="control-label">空间参考</label>
												<div class="controls">
													<input id="wkid" name="wkid" class="validate[required]" type="text" value="${idx.wkid!}"/>
												</div>
											</div><!-- /.row-fluid -->
											<div class="control-group row-fluid">
												<div class="span6">
													<label for="xmin" class="control-label">最小x</label>
													<div class="controls">
														<input id="xmin" class="validate[required,custom[number]]" name="extent.xmin" type="text" value="<#if idx.extent??>${idx.extent.xmin!}</#if>"/>
													</div>
												</div>
												<div class="span6">
													<label for="xmax" class="control-label">最大x</label>
													<div class="controls">
														<input id="xmax" class="validate[required,custom[number]]" name="extent.xmax" type="text" value="<#if idx.extent??>${idx.extent.xmax!}</#if>"/>
													</div>
												</div>
											</div><!-- /.row-fluid -->
											<div class="control-group row-fluid">
												<div class="span6">
													<label for="ymin" class="control-label">最小y</label>
													<div class="controls">
														<input id="ymin" class="validate[required,custom[number]]" name="extent.ymin" type="text" value="<#if idx.extent??>${idx.extent.ymin!}</#if>"/>
													</div>
												</div>
												<div class="span6">
													<label for="ymax" class="control-label">最大y</label>
													<div class="controls">
														<input id="ymax" class="validate[required,custom[number]]" name="extent.ymax" type="text" value="<#if idx.extent??>${idx.extent.ymax!}</#if>"/>
													</div>
												</div>
											</div><!-- /.row-fluid -->

										</div><!-- /.span6 -->

									</div><!-- /.row-fluid -->

									<div class="row-fluid">

										<div class="span12 form-actions">
											<#if idx.id??>
											<input name="id" type="hidden" value="${idx.id}"/>
											</#if>
											<a href="${base}/console/index/index" class="btn">返回</a>
											<button type="submit" class="btn btn-primary">
												保存
											</button>
										</div><!-- /.form-actions -->

									</div><!-- /.row-fluid -->

								</form>

							</div><!-- J_BASE_INFO -->

					</div><!-- /.widget-content -->

				</div><!-- /.widget-box -->

			</div><!-- /.row -->

		</div><!-- /.container-fluid -->

	</body>
</html>