<!DOCTYPE html>
<html lang="en">
	<head>
		<title>一张图</title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="${base}/static/wro/console.css"/>
	    <script type="text/javascript">
	        var _ctx = '${base}';
	    </script>
	    <style>
	    /* login form */
		.form-page{
			background-color:#fff;
			width: 500px; 
			margin: 10% auto;
			box-shadow: 0 2px 15px rgba(0,0,0,1);
			-webkit-box-shadow: 0 10px 35px rgba(0,0,0,.5);
			-moz-box-shadow: 0 10px 35px rgba(0,0,0,.5);
			-ms-box-shadow: 0 10px 35px rgba(0,0,0,.5);
			-o-box-shadow: 0 10px 35px rgba(0,0,0,.5);
		}
		.login-form .control-group,
		.login-form .form-actions{
			border:none;
		}
		.login-form{
			
			
			
		}
		.login-form input[type="text"],
		.login-form input[type="password"],
		.login-form .btn{
		 	box-sizing: border-box; /* css3 rec */
		   	-moz-box-sizing: border-box; /* ff2 */
		   	-ms-box-sizing: border-box; /* ie8 */
		   	-webkit-box-sizing: border-box; /* safari3 */
		   	-khtml-box-sizing: border-box;
		   	height: 35px;
		   	width: 100%;
		}
	    </style>
	</head>

	<body>
		<div class="form-page">
			<div id="header" style="padding-top: 15px;height:50px;background:#272727; position: static">
				<h1 style="margin:0 auto;position:static">江苏省国土资源“一张图”运维管理子系统</h1>
			</div>
			<div style="background-color: #eee;border: 1px solid #DEDEDE; padding:10px 80px 50px;">
				<div class="alert alert-danger fade active in">
					<button class="close" data-dismiss="alert">×</button>
					<strong>账号或密码错误</strong>
				</div>
				<br />
				<form action="" class="login-form">
					<fileldset>
						<div>
							<label for="">账&nbsp;&nbsp;&nbsp;号：</label>
							<input type="text"/>
						</div>
						<div>
							<label for="">密&nbsp;&nbsp;&nbsp;码：</label>
							<input type="password"/>
						</div>
						<div>
							<label for="" class="checkbox" style="padding:0;">
								<input type="checkbox" />
								记住登录</label>
						</div>
						<div>
							<button type="submit" class="btn btn-block btn-primary">
								登录
							</button>
						</div>
					</fileldset>
				</form>
			</div>
		</div>
		<script src="${base}/static/wro/console.js"></script>
	</body>
</html>