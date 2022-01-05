<!DOCTYPE html>
<html lang="en">
	<head>
		<title>一张图</title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="${base}/static/wro/console.css"/>
		<link rel="stylesheet" type="text/css" href="${base}/static/css/login.css"/>
	    <script type="text/javascript">
	        var _ctx = '${base}';
	    </script>
	    <style>
	    .checkbox{padding-left: 0}
	    </style>
	</head>

	<body>
		<div id="fullscreen">
		
			<div class="floor top">
				<div class="container">
					<h1>国土资源"一张图"信息门户</h1>
				</div>
			</div>
			
			<div class="floor second">
				<div class="container">
					<div class="banner"></div>
					<div class="form-wrap">
						<form action="/oms/login" method="POST" class="j_validation_form">
							<fieldset>
								<legend>
									<span class="fsbh f22">用户登录</span>
								</legend>
								<div class="control-group">
									<#include "common/ret.ftl" />
								</div>
								<div class="control-group" >
									<div>
										<span class="f16 fsbh">用户名:&nbsp;</span><input type="text" name="username" class="validate[required]"  value="${username!}"/>
									</div>
								</div>
								<div class="control-group">
									<div id="J_WRAP_PWD">
                                        <span class="f16 fsbh">密　码:&nbsp;</span><input type="password" name="password" class="validate[required]" value=""/>
									</div>
								</div>
								<div class="control-group">
									<label for="cbx" class="checkbox">
										<input type="checkbox" name="remember" <#if username??>checked</#if> id="cbx" value=""/> <span class="fsbh">记住用户名</span>
									</label>
								</div>
								<div class="control-group">
									<input type="hidden" value="${url!}" name="url"/>
									<button class="btn btn-primary btn-block" style="font-size:20px;height:38px;">登&nbsp;&nbsp;&nbsp;录</button>
								</div>
							</fieldset>
						</form>
					</div>
				</div>
			</div><!-- second -->
		</div>
	
		<div class="container">
			<div class="footer">
				&copy;江苏省国土资源信息中心 Copyright Reserved 2004-2013 All Rights Reserved 版权所有
			</div>
		</div>
		<script src="${base}/static/wro/console.js"></script>
		<script>
		//$('#J_WRAP_PWD').html('<input type="password" name="password" class="validate[required]" placeholder="密码" value=""/>');
		//$('input[type="password"]').focus(function(){
			
		//});
		//var div = document.getElementById('J_WRAP_PWD');
		//div.innerHTML = '<input type="password" name="password" class="validate[required]" placeholder="密码" value=""/>';
		</script>
	</body>
</html>