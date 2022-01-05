<#if pMsg??><p>${pMsg!}</p><#else>
<#if providers??>
    <#list providers as p>
    <tr id="J_TR_P_${p.id}" class="<#if p.enabled>j_enabled<#else>j_disabled</#if>">
        <td style="width:10%">${p.name!}</td>
        <td style="width:10%">${p.type!}</td>
        <td style="width:40%">
            <#switch p.type>
                <#case "arcgisProxy">
                <#case "wmtsProxy">
                    <a href="${p.attr['url']}" target="_blank">
                    	<#if (p.attr['url']?? && (p.attr['url']?length>40))>
                    		${p.attr['url']?substring(0,40)}...
	                    <#else>
	                    	${p.attr['url']}
	                    </#if>
                    </a>
                    <#break>
                <#case "index">
                    <a href="${base}/console/index/edit?id=${p.attr['id']!}" target="_blank">查看索引实例配置</a>
                    <#break>
                <#case "localTile">
                    <#if (p.attr['path']?? && (p.attr['path']?length>40))>
                    	${p.attr['path']?substring(0,40)}...
                    <#else>
                    	${p.attr['path']}
                    </#if>
                    <#break>
            </#switch>
        ${p.desc!}
        </td>
        <td class="fn-tc">
        	<#if p.enabled>
			<a href="${base}/console/provider/ajax/toggle?id=${p.id}" class="btn btn-mini btn-danger j_enable_disable">禁用</a>
			<#else>
			<a href="${base}/console/provider/ajax/toggle?id=${p.id}" class="btn btn-mini btn-success j_enable_disable">启用</a>
			</#if>
            <a href="${base}/console/provider/ajax/changeOrder?id=${p.id}&isUp=true&mapId=${mapId}" class="btn btn-info btn-mini j_btn_change_order">上移</a>
            <a href="${base}/console/provider/ajax/changeOrder?id=${p.id}&isUp=false&mapId=${mapId}" class="btn btn-info btn-mini j_btn_change_order">下移</a>
            <a href="#J_NEW_PROVIDER_MODAL_2" data-toggle="modal" gtdata-result="#J_NEW_PROVIDER_MODAL_2" url="${base}/console/provider/ajax/edit?id=${p.id}&serviceType=${p.type!}&mapId=${mapId}" class="btn btn-primary btn-mini j_ajax_for_data">编辑</a>
            <a href="${base}/console/provider/remove?id=${p.id}&mapId=${mapId}" class="btn btn-mini btn-inverse j_btn_del">删除</a>
            <#if p.attr['id']??><a href="${base}/console/index/rebuild?id=${mapId}" class="btn btn-info btn-mini">重建索引</a></#if>
        </td>
    </tr>
    </#list>
</#if>
</#if>