<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>查询</h3>
    <p>http://{ip}:{port}/omp/geometryService/rest/query</p>
    <h4>1.功能</h4>
    <p>&nbsp;&nbsp;通用查询，包含属性以及空间查询。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。 <br />
        DemoUrl:
        <br/>
        http://192.168.50.181:8080/omp/geometryService/rest/query?layerName=sde.test5_Polygon&amp;geometry={%20%22type%22:%20%22Polygon%22,%20%22coordinates%22:%20[%20
        <br/>[%20[40453557.304,3514493.430],%20[40455412.308,3517016.236],%20[40453779.904,3517387.236],[40453557.304,3514493.430]%20]%20]%20}</p>
    <h4>2.参数</h4>
    <br/>
    <table width="87%" border="1">
        <tr>
            <th width="23%" scope="col">参数</th>
            <th width="77%" scope="col">描述</th>
        </tr>
        <tr>
            <td>layerName</td>
            <td>查询空间图层名称</td>
        </tr>
        <tr>
            <td>where</td>
            <td>属性查询条件</td>
        </tr>
        <tr>
            <td>geometry</td>
            <td>空间查询图形，为GeoJSON格式</td>
        </tr>
        <tr>
            <td>outFields</td>
            <td>返回字段，多个以逗号隔开，全部返回以"*"表示</td>
        </tr>
        <tr>
            <td>returnGeometry</td>
            <td>是否返回图形，默认值为true</td>
        </tr>
        <tr>
            <td>dataSource</td>
            <td>可选参数，多数据源下指定数据源查询</td>
        </tr>
    </table>
    <br/>
    <h4>3.返回值</h4>
    <p>
        i.	正常返回值：{&quot;result&quot;:{GeoJSON格式FeatureCollection }}；<br />
        示例： <br />
        {&ldquo;result&rdquo;:  {<br />
        &quot;type&quot;:  &quot;FeatureCollection&quot;,<br />
        &quot;features&quot;: [<br />
        {<br />
        &quot;type&quot;:  &quot;Feature&quot;,<br />
        &quot;geometry&quot;: {<br />
        &quot;type&quot;:  &quot;LineString&quot;,<br />
        &quot;coordinates&quot;: [<br />
        [ 102, 0], [103, 1], [104,  0], [105, 1]<br />
        ]<br />
        },<br />
        &quot;properties&quot;: {<br />
        &quot;prop0&quot;:  &quot;value0&quot;,<br />
        &quot;prop1&quot;: 0<br />
        }<br />
        },<br />
        {<br />
        &quot;type&quot;: &quot;Feature&quot;,<br />
        &quot;geometry&quot;: {<br />
        &quot;type&quot;:  &quot;Polygon&quot;,<br />
        &quot;coordinates&quot;: [<br />
        [ <br />
        [100,  0], [101, 0], [101, 1], [100, 1], [100, 0]<br />
        ]<br />
        ]<br />
        },<br />
        &quot;properties&quot;: {<br />
        &quot;prop0&quot;:  &quot;value0&quot;,<br />
        &quot;prop1&quot;: {<br />
        &quot;this&quot;:  &quot;that&quot;<br />
        }<br />
        }<br />
        }<br />
        ]<br />
        } }<br/>
        ii.	异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}</p>
</div>