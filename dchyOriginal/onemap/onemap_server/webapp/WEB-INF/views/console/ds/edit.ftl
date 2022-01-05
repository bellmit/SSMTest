<html>
<head>
    <title>数据源管理</title>
    <meta name="tab" content="ds"/>
</head>
<body>
<div id="breadcrumbWrapper">
    <div id="breadcrumb">
        <a href="${base}/console/ds/index"><i class="icon-tasks"></i>数据源列表</a>
        <a class="current"><#if ds.id??>编辑<#else>新增</#if>${dbtype}数据源 ${ds.name!}</a>
    </div>
</div>

<div class="container-fluid">
<#include "../../common/ret.ftl" />
    <div class="widget-box">
    <div class="widget-content">
    		<#switch dbtype>
	            <#case "oracle">
	                <div id="oracle">
	                    <form action="" class="form-horizontal" method="post">
	                        <input type="hidden" name="type" value="oracle"/>
	                        <div class="row-fluid">
	                        	<div class="span6">
			                        <div class="control-group">
			                            <label for="" class="control-label">数据源名称</label>
			                            <div class="controls"><input type="text" name="name" value="${ds.name!}"/></div>
			                        </div>
		                        </div>
		                        <div class="span3">
	                                <div class="control-group">
	                                	<label for="" class="control-label">数据库</label>
	                                	<div class="controls"><input type="text" name="attr['database']" value="${ds.attr.database!}"/></div>
	                            	</div>
	                            </div>
	                        </div>
	                        <div class="row-fluid">
	                            <div class="span6">
	                            	<div class="control-group">
	                                	<label for="" class="control-label">主机</label>
	                                	<div class="controls"><input type="text" name="attr['host']" value="${ds.attr.host!}"/></div>
	                            	</div>
	                            </div>
	                            <div class="span3">
	                            	<div class="control-group">
	                                	<label for="" class="control-label">端口</label>
	                               	 	<div class="controls"><input type="text" name="attr['port']" value="${ds.attr.port!}"/></div>
	                            	</div>
	                            </div>
	                            
	                        </div><!-- row -->
	                        <div class="row-fluid">
		                        <div class="span6">
		                            <div class="control-group">
		                                <label for="" class="control-label">用户名</label>
		                                <div class="controls"><input type="text" name="attr['user']" value="${ds.attr.user!}"/></div>
		                            </div>
		                        </div>
	                            <div class="span6">
	                            	<div class="control-group">
	                                	<label for="" class="control-label">密码</label>
	                                	<div class="controls"><input type="password" name="attr['passwd']" value="${ds.attr.password!}"/></div>
	                            	</div>
	                            </div>
	                        </div><!-- row -->
	                        <div class="row-fluid">
		                        <div class="span12">
			                        <div class="control-group">
			                            <label for="" class="control-label">数据源描述</label>
			                            <div class="controls"><textarea name="description">${ds.description!}</textarea></div>
			                        </div>
			                    </div>
	                        </div><!-- row -->
	                        <div class="modal-footer">
	                            <a href="${base}/console/ds/index" class="btn">返回</a>
	                            <input type="button" class="btn btn-primary" onclick="testDs(this.form);" value="测试"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                            <button type="submit" class="btn btn-primary">保存</button>
	                        </div>
	                    </form>
	                </div>
	                <#break>
	            <#case "sharp">
	                <div id="shp">
	                    <form action="" class="form-horizontal" method="post">
	                        <input type="hidden" name="type" value="sharp"/>
	                        <input type="hidden" name="attr['charset']" value="gbk"/>
	                        <div class="control-group">
	                            <label for="" class="control-label">数据源名称</label>
	                            <div class="controls"><input type="text" name="name" value="${ds.name!}"/></div>
	                        </div>
	                        <div class="control-group">
	                            <label for="" class="control-label">shp文件地址</label>
	                            <div class="controls"><input type="text" name="url" value="${ds.url!}"/></div>
	                        </div>
	                        <div class="control-group">
	                            <label for="" class="control-label">数据源描述</label>
	                            <div class="controls"><textarea name="description">${ds.description!}</textarea></div>
	                        </div>
	                        <div class="modal-footer">
	                            <a href="${base}/console/ds/index" class="btn">返回</a>
	                            <input type="button" class="btn btn-primary" onclick="testDs(this.form);" value="测试"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                            <button type="submit" class="btn btn-primary">保存</button>
	                        </div>
	                    </form>
	                </div>
	                <#break>
	            <#case "arcsde">
	                <div id="arcsde">
	                    <form action="" class="form-horizontal" method="post">
	                        <div class="row-fluid">
		                        <div class="span12">
			                        <input type="hidden" name="type" value="arcsde"/>
			                        <div class="control-group">
			                            <label for="" class="control-label">数据源名称</label>
			                            <div class="controls"><input type="text" name="name" value="${ds.name!}"/></div>
			                        </div>
			                    </div>
	                        </div>
	                        
	                        <div class="row-fluid">
	                        	<div class="span3">
		                            <div class="control-group">
		                                <label for="" class="control-label">主机</label>
		                                <div class="controls"><input type="text" name="attr['server']" value="${ds.attr.server!}"/></div>
		                            </div>
	                        	</div>
	                            <div class="span3">
	                            	<div class="control-group">
	                                	<label for="" class="control-label">端口</label>
	                                	<div class="controls"><input type="text" name="attr['port']" value="${ds.attr.port!}"/></div>
	                            	</div>
	                            </div>
	                        </div>
	                        
	                        <div class="control-group row-fluid">
	                            <div class="span3">
	                                <label for="" class="control-label">用户名</label>
	                                <div class="controls"><input type="text" name="attr['user']" value="${ds.attr.user!}"/></div>
	                            </div>
	                            <div class="span3">
	                                <label for="" class="control-label">密码</label>
	                                <div class="controls"><input type="password" name="attr['password']" value="${ds.attr.password!}"/></div>
	                            </div>
	                        </div>
	                        
	                        <div class="control-group">
	                            <label for="" class="control-label">数据源描述</label>
	                            <div class="controls"><textarea name="description">${ds.description!}</textarea></div>
	                        </div>
	                        
	                        <div class="modal-footer">
	                            <a href="${base}/console/ds/index" class="btn">返回</a>
	                            <input type="button" class="btn btn-primary" onclick="testDs(this.form);" value="测试"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                            <button type="submit" class="btn btn-primary">保存</button>
	                        </div>
	                        
	                    </form>
	                </div>
	                <#break>
	        </#switch>
    </div><!-- widget-content -->
    
    </div><!-- widget-box -->
</div>
<script type="text/javascript">
    function testDs(form){
        $.post("${base}/console/ds/test", $(form).serialize(),function(data){
            alerts(data?'配置正确':'配置错误', data?'success':'error');
        });
    }
</script>
</body>
</html>