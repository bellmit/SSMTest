<div id="J_ALERT" class="alert <#if ret??><#if ret>alert-success<#else>alert-error</#if></#if> hide">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<div class="j_alert_ctn">
		<#if ret??>
	        <#if ret>
	            <strong>操作成功</strong>
	        <#else>
	            <strong>错误</strong> ${msg!}
	        </#if>
		</#if>
	</div>
</div>