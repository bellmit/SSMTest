<article class="error-wrap hero-unit">
    <h1>发现异常</h1>
    <p>${exception.message!}</p>
    <p style="font-size: 14px;">
        <a href="${base}">返回首页</a>&nbsp;&nbsp;
        <a href="#" onclick="document.getElementById('detail_error_msg').style.display = '';">错误详情</a>
    </p>
    <pre id="detail_error_msg" style="display:none;padding: 5px 10px;">${ctx.getEx(exception)}</pre>
</article>