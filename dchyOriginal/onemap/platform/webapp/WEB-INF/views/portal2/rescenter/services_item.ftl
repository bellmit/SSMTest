<#list groups as g>
<li id="${g.id}" <#if g.parent??>data-parent="${g.parent.id}"</#if> data-has-child="true" data-url="<@com.rootPath />/portal2/ajax/fetchMapGroup?parentId=${g.id}">
    <a href="#"><i class="icon icon-folder-close"></i>${g.name}</a>
</li>
</#list>
<#list page.content as map>
<li id="${map.id}" data-parent="${map.group.id}" data-has-child="false">
    <a href="#" data-url="<@com.rootPath/>/portal2/service/${map['id']}" class="a4d" data-result-ctn="#mapResultCtn"><i class="icon icon-copy"></i><#if map.alias??>${map.alias}<#else>${map.name}</#if></a>
</li>
</#list>
