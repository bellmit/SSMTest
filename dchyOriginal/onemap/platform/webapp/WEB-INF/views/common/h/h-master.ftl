<#--html-->
<#macro html title="" css="">
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <title>${title!}</title>
    <#include "h-css.ftl"/>
      ${css!}
    <#include "h-js.ftl"/>
</head>
<body>
    <#nested />
</body>
</html>
</#macro>