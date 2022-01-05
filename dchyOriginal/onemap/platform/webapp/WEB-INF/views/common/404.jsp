<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
         isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>404 ~ Page Not Found</title>
</head>

<style>
    ::-moz-selection {
        background: #b3d4fc;
        text-shadow: none;
    }

    ::selection {
        background: #b3d4fc;
        text-shadow: none;
    }

    html {
        padding: 30px 10px;
        font-size: 20px;
        line-height: 1.4;
        color: #737373;
        background: #f0f0f0;
        -webkit-text-size-adjust: 100%;
        -ms-text-size-adjust: 100%;
    }

    html,
    input {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    }

    body {
        max-width: 500px;
        _width: 500px;
        padding: 30px 20px 50px;
        border: 1px solid #b3b3b3;
        border-radius: 4px;
        margin: 0 auto;
        box-shadow: 0 1px 10px #a7a7a7, inset 0 1px 0 #fff;
        background: #fcfcfc;
    }

    h1 {
        margin: 0 10px;
        font-size: 50px;
        text-align: center;
    }

    h1 span {
        color: #bbb;
    }

    h3 {
        margin: 1.5em 0 0.5em;
    }

    p {
        margin: 1em 0;
    }

    ul {
        padding: 0 0 0 40px;
        margin: 1em 0;
    }

    .container {
        max-width: 380px;
        _width: 380px;
        margin: 0 auto;
    }

        /* google search */

    #goog-fixurl ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    #goog-fixurl form {
        margin: 0;
    }

    input::-moz-focus-inner {
        padding: 0;
        border: 0;
    }

    a {
        color: #737373;
    }

</style>

<body>
<div class="container">
    <h1>404&nbsp;&nbsp;<span> :(</span></h1>

    <p>当前请求的页面不存在或已经移至他处</p>

    <p>可以尝试以下链接:</p>
    <ul>
        <li><a href="<%=request.getContextPath()%>">回主页</a></li>
    </ul>
    <p>

    </p>

</div>
</body>
</html>