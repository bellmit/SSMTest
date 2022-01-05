<html>
	<head>
		<title>索引服务与配置</title>
		<meta name="tab" content="index"/>
		<meta name="js" content="${base}/static/js/page_index_index_edit.js" />
	</head>
	<body>

		<div class="container-fluid">
			<#include "../../common/ret.ftl" />
			<div class="row-fluid">

				<div class="span12">

					<div class="widget-box">
						<div class="widget-title">
							<h5>索引列表</h5>
							<div class="buttons">
								<a href="${base}/console/index/edit" data-toggle="modal" class="btn btn-primary btn-mini"><i class="icon-white icon-plus"></i>&nbsp;创建实例</a>
								<#--<a href="#" class="btn btn-primary btn-mini"><i class="icon-white icon-chevron-down"></i>&nbsp;导入索引</a>-->
							</div>
						</div>
						<div class="widget-content nopadding">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th>名称</th>
										<th>空间参考</th>
										<th>记录数</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<#list idxes as i>
									<tr>
										<td>${i.name!}</td>
										<td>${i.wkid!}</td>
										<td>${counts[i.id]}</td>
										<td class="fn-tc">
											<a href="${base}/console/index/edit?id=${i.id}" class="btn btn-mini btn-primary">编辑</a>
											<!-- <a href="" class="btn btn-mini">导出</a> -->
											<!-- <a href="" data-toggle="modal" class="btn btn-mini">服务关联</a>
											<a href="" class="btn btn-mini btn-danger">停用</a> -->
											<a href="${base}/console/index/rebuildAll?id=${i.id}" class="btn btn-mini btn-info">重建</a>
											<a href="${base}/console/index/remove?id=${i.id}" class="btn btn-mini btn-inverse j_btn_del">删除</a>
										</td>
									</tr>
									</#list>
								</tbody>
								<tfoot>
									
								</tfoot>
							</table>
						</div><!-- END widget-content -->

					</div>
					<!-- /widget-box -->

				</div>
				<!-- /.span12 -->

			</div>
			<!-- /.row-fluid -->

		</div>
		<!-- /container-fluid -->

	</body>
</html>