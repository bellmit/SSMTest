<#assign jsContent>
<script type="text/javascript">
    $('.tree-menu').listtree();
    setTimeout(function(){
        $('#linkMap').click();
        $('#collapseTwo').find('li:first-child').find('._treetoggle').click();
    }, 100);
    $('body').delegate('.a4d','click', function(){
    	$($(this).parents('ul')[0]).children('li').removeClass('active');
    	$($(this).parents('li')[0]).addClass('active');
    });
</script>
<script src="<@com.rootPath/>/js/highcharts/highcharts.js"></script>
</#assign>
<#assign cssContent>
<link rel="stylesheet" href="/omp/js/agsapi/js/esri/css/esri.css">
<link rel="stylesheet" href="/omp/js/agsapi/js/dojo/dijit/themes/claro/claro.css">
<link rel="stylesheet" href="<@com.rootPath/>/static/css/metadata.css" />
</#assign>
<@base.main nav="res" js=jsContent css=cssContent>
<div class="main rescenter">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="<@com.rootPath/>/portal2">首页</a> <span class="divider">/</span></li>
            <li>资源中心</li>
        </ul>
        <div class="row">
            <div class="span3" style="height: 500px;">
                <div class="accordion" id="accordion2">
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a id="linkDB" class="accordion-toggle toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">数据库资源</a>
                        </div>
                        <div id="collapseOne" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <ul class="nav tree-menu">
                                    <li id="yztsjgl" data-has-child="true">
                                        <a href="#" data-url="<@com.rootPath/>/portal2/ajax/fetchOverview/yzt" class="a4d" data-result-ctn="#mapResultCtn">
                                            <i class="icon icon-folder-open"></i><strong>一张图数据管理</strong></a>
                                    </li>
                                    <#list dbList as db>
                                        <li id="${db.id}" dat-has-child="true" data-parent="yztsjgl">
                                            <a href="#" data-url="<@com.rootPath/>/portal2/ajax/fetchOverview/${db.id}" class="a4d" data-result-ctn="#mapResultCtn"><i class="icon icon-hdd">
                                            </i><strong>${db.name}</strong> </a>
                                        </li>
                                        <#list db.children as child>
                                            <#if db.type?contains("0")>
                                                <li id="${child.ywlx}" data-parent="${child.parentId}" data-has-child="true">
                                                    <a href="#"><i class="icon icon-folder-open"></i><strong>${child.ywlxmc}</strong></a>
                                                </li>
                                                <#list child.metadatas as metadata>
                                                    <li id="${metadata.id}" data-parent="${metadata.ywlx}" data-has-child="false">
                                                        <a href="#" data-url="<@com.rootPath/>/portal2/ajax/fetchDetail/${metadata.id}" class="a4d" data-result-ctn="#mapResultCtn">
                                                            <i class="icon icon-copy"></i>${metadata.userName}_${metadata.nd}</a>
                                                    </li>
                                                </#list>
                                            <#else >
                                                <li id="${child.id}" data-parent="${child.parentId}" data-has-child="true">
                                                    <a href="#"><i class="icon icon-folder-open"></i><strong>${child.type}</strong></a>
                                                </li>
                                                <#list child.children as grandChild>
                                                    <li id="${grandChild.name}" data-parent="${child.id}" data-has-child="false">
                                                        <a href="#" data-url="<@com.rootPath/>/portal2/ajax/fetchOverview/${grandChild.name}_${child.type}" class="a4d" data-result-ctn="#mapResultCtn"><i class="icon icon-copy"></i>${grandChild.name}</a>
                                                    </li>
                                                </#list>
                                            </#if>
                                        </#list>
                                    </#list>
                                    <#--<#list servers as item>-->
                                        <#--<li id="${item.id}" data-has-child="true" data-url="<@com.rootPath/>/portal2/ajax/fetchDbInfo/${item.id}">-->
                                            <#--<a href="#"><i class="icon icon-desktop"></i>${item.ipdz}</a>-->
                                        <#--</li>-->
                                    <#--</#list>-->
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a id="linkMap" class="accordion-toggle toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">地图服务资源</a>
                        </div>
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <ul class="nav tree-menu">
                                    <#list groups as g>
                                        <li id="${g.id}" data-has-child="true" data-url="<@com.rootPath />/portal2/ajax/fetchMapGroup?parentId=${g.id}">
                                            <a href="#"><i class="icon icon-folder-close"></i>${g.name}</a>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- span -->
            <div class="span9">
                <div id="mapResultCtn" style="">
					
                </div>
            </div>
        </div>
    </div>
    <br />
</div><!-- main -->
</@base.main>