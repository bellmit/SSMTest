<#if logDetail??>
	<#list logDetail.lines as line>
		<div><strong style="display:inline-block; text-align: right; color: #888;">${line_index+logDetail.current-99}.&nbsp;&nbsp;</strong>${line}</div>
	</#list>
<script>
changeUrl( ${logDetail.current}, ${logDetail.hasMore?string} );
</script>
<#elseif !ret>
	${msg!}
</#if>