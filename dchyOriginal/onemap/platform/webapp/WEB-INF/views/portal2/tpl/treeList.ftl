<#assign jsContent>
<script type="text/javascript">
    $(document).ready(function(){
        $("#mapwrap").height($(this).height()-$("#hearder").height());
    });
</script>
</#assign>
<#assign cssContent>
<style type="text/css">
    #mapwrap{
        overflow:hidden;
        border-radius: 4px;
    }
    .main{
        padding-bottom:0px;
	padding-top:0px;
    }
    .container{
        width:100%;
    }
</style>
</#assign>
<@base.main nav="zt" js=jsContent css=cssContent showFooter="false">
<div class="main">
    <div class="container">
        <div id="mapwrap">
            <iframe id="flashContent" frameborder="0" width="100%" height="100%" src="<@com.rootPath/>/map/${tpl!}?hideTopBar=true"></iframe>
        </div>
    </div>
</div>
</@base.main>
