<div class="base-info-wrap">
	<div class="base-info-inner">
		<a href="#" class="toggle"><span class="icon icon-angle-right"></span></a>
		<div class="base-info">
			<h4><i class="icon icon-copy"></i>${map.alias!map.name}</h4>
            <p>${map.description!}</p>
                <ul id="geoDataList">
                    <li>
                        <label>行政区代码</label><span>${map.regionCode!}</span>
                    </li>
                    <li>
                        <label>服务年份</label><span>${map.year!}</span>
                    </li>
                    <li>
                        <label>空间参考</label><span class="geoCode">${map.wkid!?c}</span>
                    </li>
                    <li>
                        <label>最小X</label><span>${map.extent.xmin!?string("0.##")}</span>
                    </li>
                    <li>
                        <label>最小Y</label><span>${map.extent.ymin!?string("0.##")}</span>
                    </li>
                    <li>
                        <label>最大X</label><span>${map.extent.xmax!?string("0.##")}</span>
                    </li>
                    <li>
                        <label>最大Y</label><span>${map.extent.ymax!?string("0.##")}</span>
                    </li> 
                <#if (layers?? && layers?size >0)>
                    <li>
                        <label>图层信息</label>
                        <#list layers as layer>
                        	<div>&nbsp;&nbsp;${layer.alias!layer.name}</div>
                        </#list>
                    </li>
               	</#if>
                    
                </ul>
		</div>
        <!-- <a href="<@com.rootPath/>/portal2/rescenter/metadataInfo" class="metadata" title="元数据信息"><span class="icon icon-info"></span></a> -->
	</div>
</div>

<div id="J_MAP" style="height:574px"></div>
           
<script src="<@com.rootPath/>/static/js/black-opt.js"></script>
<script src="<@com.rootPath/>/js/agsapi/init.js"></script>
<script>
	geo.codeTrans('#geoDataList');
	
    dojo.require("esri.map");

    function init() {

//        $('.base-info-inner .metadata').tooltip('test');
        var map = new esri.Map("J_MAP", {
            logo:false,
            zoom:5
//            fadeOnZoom:true

        });
        if ("${map.status.label}" == "运行中") {
            try {
                var layer;
            <#switch serviceType>
                <#case "tiled">
                    layer = new esri.layers.ArcGISTiledMapServiceLayer('${serviceUrl}');
                    map._mapParams.sliderStyle = "large";
                    <#break>
                <#case "dynamic">
                    layer = new esri.layers.ArcGISDynamicMapServiceLayer('${serviceUrl}');
                    map._mapParams.sliderStyle = "small";
                    <#break>
            </#switch>
                if (layer) map.addLayer(layer);
            } catch (Error) {
                alert(Error)
            }
        } else if ("${map.status.label}" == "停止") {
            alert("该服务未启动");
        } else {
            alert("该服务无效");
        }
    }
    dojo.addOnLoad(init);
</script>