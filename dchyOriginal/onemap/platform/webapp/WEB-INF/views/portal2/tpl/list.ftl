<@base.main nav="zt">
<div class="main">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="<@com.rootPath/>/portal2">首页</a> <span class="divider">/</span></li>
            <li><a href="<@com.rootPath/>/portal2/tpl/index">专题图</a> <span class="divider">/</span></li>
            <li class="active">${type.name}</li>
        </ul>
        <div class="article">
        	<br />
        	<h1>${type.name}</h1>
        	<br />
        </div>
        <ul class="nav horizontal-items">
        	<#list tpls as tpl>
        	<li>
        		<#if tpl.configuration.map.operationalLayers?? && (tpl.configuration.map.operationalLayers?size>0)>
        			<a href="<@com.rootPath/>/map/${tpl.tpl}" target="_blank"><img src="${path_oms}/thumbnail/${tpl.configuration.map.operationalLayers[0].id}" alt="" /></a>
        		<#else>
        			<a href="<@com.rootPath/>/map/${tpl.tpl}" target="_blank"><img src="${path_oms}/thumbnail/1}" alt="" /></a>
        		</#if>
		       	<div class="desc">
				   	<h4><a href="<@com.rootPath/>/map/${tpl.tpl}" target="_blank">${tpl.configuration.name}</a></h4>
				   	<p>
				   		${tpl.configuration.description!}
				   	</p>
				   	<span class="time">
				   		${tpl.configuration.createAt!}
				   	</span>
		        </div>
		     </li>
        	</#list>
        </ul>
        <br />
        <br />
    </div><!-- container -->
</div><!-- main -->
</@base.main>