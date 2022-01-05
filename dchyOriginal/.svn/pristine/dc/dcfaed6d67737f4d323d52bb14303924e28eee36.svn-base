<html>
<head>
    <title>数据源管理</title>
    <meta name="tab" content="ds"/>
</head>
<body>
<div class="container-fluid">
    <#include "../../common/ret.ftl" />
    <div class="row-fluid">
        <div class="span12">
            <div class="widget-box">
                <div class="widget-title">
                    <h5>数据源列表</h5>
                    <div class="buttons">
                        <a href="#J_NEW_DS_MODAL" data-toggle="modal" class="btn btn-mini btn-primary">添加数据源</a>
                    </div>
                </div>
                <div class="widget-content nopadding">
                    <table class="table table-bordered table-striped">
                        <thead><tr>
                            <th>名称</th>
                            <th style="width: 100px;">数据源类型</th>
                            <th>描述</th>
                            <th style="width: 150px;">操作</th>
                        </tr></thead>
                        <tbody>
                        <#list dss as ds>
                        <tr>
                            <td>${ds.name!}</td>
                            <td>${ds.type!}</td>
                            <td>${ds.description!}</td>
                            <td class="fn-tc">
                                <form action="" class="form-inline">
                                    <a href="edit?id=${ds.id}" class="btn btn-primary btn-mini">编辑</a>
                                    <a href="delete?id=${ds.id}" class="btn btn-inverse btn-mini j_btn_del">删除</a>
                                </form>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div><!-- END widget-content -->
            </div><!-- END widget-box -->
        </div><!-- END span -->
    </div><!-- END .row-fluid -->
</div><!-- END container-fluid -->


<div id="J_NEW_DS_MODAL" class="modal hide">

    <div class="modal-header">
   		<button type="button" class="close" data-dismiss="modal">
   			&times;
   		</button>
   		<h3>添加数据源</h3>
   	</div>

   	<form id="J_FORM_NEW_DS_MODAL" action="${base}/console/ds/edit" method="get">

   		<div class="modal-body">

   			<div class="radio-list-wrapper">

   				<ul class="nav">
   					<li>
   						<div class="radio-item-wrapper">
   							<label for="own1"> <input id="own1" type="radio" name="dbtype" value="oracle" checked/> oracle数据源</label>
   							<span class="radio-desc">通过读取oracle spatial来访问gis数据</span>
   						</div>
   					</li>
   					<li>
   						<div class="radio-item-wrapper">
   							<label for="own2"> <input id="own2" type="radio" name="dbtype" value="sharp"/>sharpfile数据源</label>
   							<span class="radio-desc">通过读取本地sharpfile来访问gis数据</span>
   						</div>
   					</li>
   					<li>
   						<div class="radio-item-wrapper">
   							<label for="own3"> <input id="own3" type="radio" name="dbtype" value="arcsde"/>sde数据源</label>
   							<span class="radio-desc">通过读取arcsde来访问gis数据</span>
   						</div>
   					</li>
   				</ul>
   			</div>

   		</div>
   		<div class="modal-footer">
            <input type="submit" class="btn btn-primary" value="下一步"/>
   		</div>
   	</form>
</div>

</body>
</html>
