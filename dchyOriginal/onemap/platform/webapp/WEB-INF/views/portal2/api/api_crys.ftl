<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>插入要素</h3>

    <p>http://{ip}:{port}/omp/geometryService/rest/insert</p>
    <br/>
    <h4>1.功能</h4>

    <p>插入图形要素，可包含属性以及空间数据。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。
        <br/>
       <strong>DemoUrl1:</strong>http://192.168.50.181:8080/omp/geometryService/rest/insert?layerName=sde.test5_Polygon&geometry=
        <br/>{%22type%22:%22Feature%22,%22geometry%22:{%22type%22:%22Polygon%22,%22coordinates%22:[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440319.1393,3503452.5435],[40434267.5034,3507576.9068]]]},%22properties%22:{%22PRONAME%22:%22insert1%22}}
        <br/>
        <strong>DemoUrl2:</strong> http://192.168.50.181:8080/omp/geometryService/rest/insert?layerName=sde.test5_Polygon&geometry=
        <br/>
        {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440319.1393,3503452.5435],[40434267.5034,3507576.9068]]]},"properties":{"PRONAME":"insert1"}},{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440126.412,3500060.5438],[40434267.5034,3507576.9068]]]},"properties":{"PRONAME":"insert2"}}]}
    </p>
    <h4>2.参数</h4>
    <table width="80%" border="1">
        <tr>
            <th width="18%" scope="col">参数</th>
            <th width="82%" scope="col">描述</th>
        </tr>
        <tr>
            <td>layerName</td>
            <td>空间图层名称</td>
        </tr>
        <tr>
            <td>geometry</td>
            <td>图形要素，为GeoJSON格式,Feature或者FeatureCollection</td>
        </tr>
        <tr>
            <td>dataSource</td>
            <td>可选参数，多数据源下可指定数据源</td>
        </tr>
        <tr>
            <td>check</td>
            <td>可选参数，值为true时会对要插入的图形要素进行拓扑检查，在图层中已存在的图形要素无法插入</td>
        </tr>
    </table>
    <h4>3.返回值</h4>

    <p>i. 正常返回值：{&quot;result&quot;:{要素插入成功的图形的geojson格式}；<br/>
        示例：<br/>
        {&quot;result&quot;: {&quot;
        {"type":"FeatureCollection","features":[{"type":"Feature","bbox":[40432764.2308,3499597.9984,40440319.1393,3507576.9068],"geometry":{"coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440319.1393,3503452.5435],[40434267.5034,3507576.9068]]],"type":"Polygon"},"properties":{"OBJECTID":"1611","PRONAME":"insert1"},"id":"Featuref1403cbe08284028b229403cbe080001"},{"type":"Feature","bbox":[40432764.2308,3499597.9984,40440126.412,3507576.9068],"geometry":{"coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440126.412,3500060.5438],[40434267.5034,3507576.9068]]],"type":"Polygon"},"properties":{"OBJECTID":"1612","PRONAME":"insert2"},"id":"Featuref1403cbe08284028b229403cbe080002"}]}&quot;}}<br/>
        ii. 异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}</p>
</div>