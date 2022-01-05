<#assign jsContent>
<script type="text/javascript">
    setTimeout(function(){
        $('#${type!}').click();
    }, 100);
    $('body').delegate('.a4d','click', function(){
    	$($(this).parents('ul')[0]).children('li').removeClass('active');
    	$($(this).parents('li')[0]).addClass('active');
    });
</script>
</#assign>
<#assign cssContent>
<link rel="stylesheet" href="<@com.rootPath/>/static/css/metadata.css" />
<style>
.rescenter .accordion li.active a{
	color: white;
}
</style>
</#assign>
<@base.main nav="api" js=jsContent css=cssContent>
<div class="main rescenter">
    <div class="container">
        <ul class="breadcrumb">
            <li><a href="<@com.rootPath/>/">首页</a> <span class="divider">/</span></li>
            <li><a href="<@com.rootPath/>/portal2/api/index">地图API</a> <span class="divider">/</span></li>
            <li>基础服务接口</li>
        </ul>
        <div class="row">
            <div class="span3">
                <div class="accordion" id="accordion2">
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a id="jcfwjk" class="accordion-toggle toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">基础服务接口</a>
                        </div>
                        <div id="collapseOne" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <ul class="nav nav-list">
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/cx" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 查询</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/crys" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 插入要素</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/gxys" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 更新要素</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/scys" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 删除要素</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/sjfx" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 相交分析</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/location" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 地图定位</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/layerdefinition" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 地图过滤</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/httpinvoker" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> httpInvoker接口</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/shp" class="a4d"
                                           data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i>shp导入接口</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a id="ztfwjk" class="accordion-toggle toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">专题服务接口</a>
                        </div>
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <ul class="nav nav-list">
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/tdlyxz" class="a4d" data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 土地利用现状分析</a>
                                    </li>
                                    <li>
                                        <a href="#" data-url="<@com.rootPath/>/portal2/api/tdlyghsc" class="a4d" data-result-ctn="#mapResultCtn"><i class="icon icon-folder-close"></i> 土地利用总体规划审查</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!-- span -->

            <div class="span9">
                <div id="mapResultCtn" style="padding: 30px 0;">
					
                </div>
            </div>

        </div><!--row-->
    </div><!-- container -->
    <br /><br />
    <br /><br />
</div><!-- main -->
</@base.main>