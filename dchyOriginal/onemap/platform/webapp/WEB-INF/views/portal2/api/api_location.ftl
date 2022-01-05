<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>地图定位</h3>

    <p>http://{ip}:{port}/omp/map/{tpl}?action=location<span>&</span>params={"type":"","params":{}}</p>
    <br/>
    <h4>1.功能</h4>

    <p>定位图形要素，Http请求Get方式. 有三种定位方式--图层定位、url地址定位以及坐标串定位，即type可以为layerLocation、urlLocation、coordsLocation三个值，这三种方式在传参上有区别。
        <br/>
        <strong>DemoUrl1:</strong>http://127.0.0.1:8080/omp/map/test?action=location<span>&</span>params={%22type%22:%22layerLocation%22,%22params%22:{%22layerAlias%22:%22%E7%9C%81%E7%BA%A7%E9%93%81%E8%B7%AF2015%22,%22where%22:%22TILUMC='%E5%AE%81%E5%90%AF%E7%BA%BF'%22,%22showInfo%22:true,%22layerDefinition%22:%22TILUMC='%E5%AE%81%E5%90%AF%E7%BA%BF'%22}}
        <br/>
        <strong>DemoUrl2:</strong>
        <br/>
        <strong>DemoUrl3:</strong>
    </p>
    <h4>2.参数</h4>
    <table width="100%" border="1">
        <tr>
            <th width="18%" scope="col">参数</th>
            <th width="82%" scope="col">描述</th>
        </tr>
        <tr>
            <td style="text-align: center;">type</td>
            <td>定位类型(layerLocation、urlLocation、coordsLocation)</td>
        </tr>
        <tr>
            <td rowspan="4" style="text-align: center;">params</td>
            <td>定位参数</td>
        </tr>
        <tr>
            <td>(1)layerLocation(图层定位)<strong>layerAlias:</strong>图层别名;<strong>where:</strong>查询子句;<strong>showInfo:</strong>是否显示信息窗(true/false);<strong>layerDefinition:</strong>图层要素过滤表达式;</td>
        </tr>
        <tr>
            <td>(2)urlLocation(地址定位)<strong>url:</strong> 用于获取定位图层的url请求地址</td>
        </tr>
        <tr>
            <td>(3)coordsLocation(坐标定位)<strong>geometry:</strong>geoJSON格式的图形要素</td>
        </tr>
    </table>
</div>