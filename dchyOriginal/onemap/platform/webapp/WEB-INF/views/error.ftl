<html>
<head></head>
<body>
<div>
    <div class="errTitle">
        <p style="color:red;">系统惊现异常</p>
    </div>
    <#if exception??>
        <div style="clear: both;padding: 0 80px 10px;font-size: 14px;">信息:${exception.message!}</div></#if>
        <div style="padding: 10px 40px 20px;font-size: 16px;">&nbsp;<a class="top-btn" href="<@com.rootPath/>">返回首页</a>&nbsp;&nbsp;<a
                class="top-btn" href="#" onclick="document.getElementById('detail_error_msg').style.display = '';">错误详情</a>
        </div>
        <pre>${ctx.getEx(exception)}</pre>
</div>
</body>
</html>