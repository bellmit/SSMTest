<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>相交分析</h3>
    <p>http://{ip}:{port}/omp/geometryService/rest/intersect</p>
    <h4>1.功能</h4>
    <p>通用相交分析。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。
        <br/>
        DemoUrl: http://192.168.50.181:8080/omp/geometryService/%20rest/intersect?layerName=sde.test2&geometry=
        <br/>{%20%22type%22:%20%22Polygon%22,%20%22coordinates%22:%20[%20[%20[%2040463840.000,%203508512.883%20],%20[%2040477894.906,%203508869.245%20],
        <br/>%20[%2040478065.960,%203499432.786%20],%20[%2040463840.000,%203508512.883%20]%20]%20]%20}</p>
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
            <td>图形要素，为GeoJSON格式</td>
        </tr>
        <tr>
            <td>outFields</td>
            <td>返回字段，多个以逗号隔开，全部返回以"*"表示</td>
        </tr>
        <tr>
            <td>dataSource</td>
            <td>可选参数，多数据源下可指定数据源</td>
        </tr>
    </table>
    <h4>3.返回值</h4>
    <p>i.	正常返回值：{&quot;result&quot;:{GeoJSON格式FeatureCollection  }}；<br />
        示例：<br />
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
                    &quot;type&quot;:  &quot;Feature&quot;,<br />
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
                        &quot;prop1&quot;: &ldquo;value2&rdquo;</p>
    <p>            }<br />
        }<br />
        ]<br />
        } }</p>
    ii.	异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}
</div>