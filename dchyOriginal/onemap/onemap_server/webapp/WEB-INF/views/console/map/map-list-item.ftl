<div>
    <div class="service-thumb">
    	<a href="<#if mapRestUrls[map.id]??>${mapRestUrls[map.id]}?f=jsapi</#if>" target="_blank">
    		<img src="${base}/thumbnail/${map.id}" alt=""/>
    	</a>
    </div>
    <div class="pull-right">
        
        <a href="${base}/console/auth/map/grant?mapId=${map.id}" data-toggle="modal" class="btn btn-info btn-mini">授权</a>
        <a href="edit?id=${map.id}" gtdata-toggle="breadpane" class="btn btn-primary btn-mini">编辑</a>
	    <#if map.status == 'RUNNING'>
	        <a href="${base}/console/map/ajax/trigger/${map.id}" data-ctnId="J_MAP_ITEM_${map.id}" class="btn btn-danger btn-mini j_btn_trigger">关闭</a>
	    <#elseif map.status == 'STOP'>
	        <a href="${base}/console/map/ajax/trigger/${map.id}" data-ctnId="J_MAP_ITEM_${map.id}" class="btn btn-success btn-mini j_btn_trigger">开启</a>
	    </#if>
	    
	    <#if map.status == 'IMPORTING'>
	    <#else>
	    	<a href="${base}/console/map/remove?id=${map.id}" class="btn btn-inverse btn-mini j_btn_del">删除</a>
	    </#if>
    </div>
   
    <p>
        <a href="edit?id=${map.id}">${map.name!} <span style="color: gray;">[${map.alias!}]</span></a>
        <i class="myicon-status <#if map.status == 'RUNNING'>myicon-running<#elseif map.status == 'STOP'>myicon-stop</#if>"></i>
    </p>
    
    <p class="service-info">
	    <#if map.group??>服务组: <a href="${base}/console/map/index?groupName=${map.group.name!}">${map.group.name!}</a>&nbsp;&nbsp;&nbsp;</#if>支持的服务:
	    <#list sm[map.id]?values as service>
	        <a href="${service.url!}" target="_blank" title="${service.serviceType.label}">${service.serviceType}</a>&nbsp;&nbsp;
	    </#list>
    </p>
</div>