<@base.main nav="zt">
<div class="main">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="<@com.rootPath/>/portal2">首页</a> <span class="divider">/</span></li>
            <li class="active">专题图</li>
        </ul>
        <br/>
        <table class="table table-bordered tpls">
        <tr>
            <#list types as t>
                <td>
                    <#if env.hasZtAuth(t.name)>
                        <a href="<@com.rootPath/>/portal2/tpl/tree?typeId=${t.id}"
                           style="background: url(<@com.rootPath/>/file/download/${t.thumbnail!}) center center no-repeat">
                            &nbsp;</a>
                        <h4><a href="<@com.rootPath/>/portal2/tpl/tree?typeId=${t.id}">${t.title!}</a></h4>
                    <#else>
                        <a style="background: url(<@com.rootPath/>/file/download/${t.thumbnail!}) center center no-repeat">
                            &nbsp;</a>
                        <h4><a style="color: #999">${t.title!}</a></h4>
                    </#if>
                    <p style="text-indent: 1em;">
                    ${t.desc!}
                    </p>
                </td>
                <#if (t_index+1)%3==0>
                </tr>
                    <#if t_has_next>
                    <tr>
                    </#if>
                </#if>
            </#list>
        </table>
        <br/>
        <br/>
    </div>
    <!-- container -->
</div><!-- main -->
</@base.main>