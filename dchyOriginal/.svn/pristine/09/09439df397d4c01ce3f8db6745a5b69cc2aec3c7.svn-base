<#assign cssContent>
<style>
ul.nav-tabs{
	margin-bottom: 0;
}
.tab-content{
	padding: 20px;
	border: 1px solid #ddd;
	border-top: none;
	border-radius: 0 0 5px 5px;
}
.tab-pane label{
	display: inline-block;
	color: #3E63D6;
	font-weight: bold;
	margin-right: 5px;
}
.tab-pane em{
	display: inline-block;
	margin-right: 5px;
}
.tab-pane > span{
	display: block;
	border-bottom: 1px solid #E9E9E9;
}
.tab-pane > span span{
	display: block;
	margin-left: 20px;
}
.tab-pane .icon{
	margin-right: 8px;
	font-size: 12px;
}
.tab-pane > span .icon{
	color: #5F6163;
}
.tab-pane > span span .icon{
	color: #BEBEBE;
}
</style>
</#assign>
<#assign jsContent>
<script>
$(document).ready(function(){
	var htmlString = '${xml!}'.replace(/<item/g, '<span')
						 .replace(/item>/g, 'span>')
						 .replace('<?xml version="1.0" encoding="utf-8"?>','')
						 .replace(/Applications/g,'div')
						 .replace(/<Category/g,'<p')
						 .replace(/Category>/g,'p>');
	
	var target = $(htmlString);
	
	target.find('span').each(function(){
		$(this).prepend('<i class="icon icon-circle-blank"></i><label>'+ $(this).attr('name') +'</label><em>'+ $(this).attr('value') +'</em>');
	});
	
	target.children('p').each(function(){
		var self = $(this);
		if( self.attr('CategoryName') === '元数据信息' ){
			$('#metaInfo').html( self.html() );
		} else if( self.attr('CategoryName') === '标识信息' ){
			$('#markInfo').html( self.html() );
		} else if( self.attr('CategoryName') === '空间特征' ){
			$('#spatialInfo').html( self.html() );
		} else if( self.attr('CategoryName') === '数据内容说明' ){
			$('#descriptionInfo').html( self.html() );
		} else if( self.attr('CategoryName') === '分发信息' ){
			$('#distributionInfo').html( self.html() );
		}
	});
});
</script>
</#assign>
<@base.main nav="res" js=jsContent css=cssContent>
<div class="main rescenter">
    <div class="container">
        <ul class="breadcrumb">g
            <li><a href="<@com.rootPath/>/">首页</a> <span class="divider">/</span></li>
            <li><a href="<@com.rootPath/>/portal2/rescenter/index">资源中心</a> <span class="divider">/</span></li>
            <li><a href="<@com.rootPath/>/portal2/rescenter/db">数据资源</a> <span class="divider">/</span></li>
            <li>元数据信息</li>
        </ul>
        <h3 style="font-weight: normal; color:#188074"><span class="icon icon-paper-clip"></span>${metaDataTitle!}元数据</h3>
        <br />
        <ul class="nav nav-tabs">
			<li class="active"><a href="#metaInfo" data-toggle="tab">元数据信息</a></li>
            <li><a href="#markInfo" data-toggle="tab">标识信息</a></li>
            <li><a href="#spatialInfo" data-toggle="tab">空间特征</a></li>
            <li><a href="#descriptionInfo" data-toggle="tab">数据内容说明</a></li>
            <li><a href="#distributionInfo" data-toggle="tab">分发信息</a></li>
        </ul>
        <div class="tab-content">
        	<div id="metaInfo" class="tab-pane fade in active">元数据信息</div>
            <div id="markInfo" class="tab-pane fade">标识信息</div>
            <div id="spatialInfo" class="tab-pane fade">空间特征</div>
            <div id="descriptionInfo" class="tab-pane fade">内容说明</div>
            <div id="distributionInfo" class="tab-pane fade">分发信息</div>
        </div>
    </div>
</div>
</@base.main>