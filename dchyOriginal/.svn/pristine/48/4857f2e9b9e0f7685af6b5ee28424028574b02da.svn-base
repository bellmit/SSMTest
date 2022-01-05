
<#assign cssContent>
<style type="text/css">
.table-content{
    padding: 5px;
}
</style>
</#assign>

<@master.html title="最近未使用监控点明细" css=cssContent>
<div class="table-content">
    <button class="btn btn-primary" id="exportExcel" style="float: right;margin-bottom: 5px;">导出excel</button>
<table class="table table-border table-bordered table-bg table-hover table-sort">
    <tr>
        <th>区镇名称</th>
        <th>设备名称</th>
        <th>设变编号</th>
        <th>最近一次使用时间</th>
    </tr>
    <#assign cameras = unused/>
    <#list cameras as camera>
        <tr>
            <td>${camera.regionName!}</td>
            <td>${camera.deviceName!}</td>
            <td>${camera.cameraId!}</td>
            <td>${camera.time!}</td>
        </tr>
    </#list>


</table>
</div>

<@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>

<script type="text/javascript">
    $("#exportExcel").on("click", function () {
        var form = $("<form method='post' style='display:none;' target='_blank'></form>"),
                input;
        form.attr({"action": '<@com.rootPath/>/video/unusedRecently/export'});
        input = $("<input type='hidden'>");
        input.attr({"name": 'data'});
        input.val('${unusedStr!}');
        form.append(input);
        form.appendTo(document.body);
        form.submit();
        document.body.removeChild(form[0]);
   });
</script>
</@master.html>