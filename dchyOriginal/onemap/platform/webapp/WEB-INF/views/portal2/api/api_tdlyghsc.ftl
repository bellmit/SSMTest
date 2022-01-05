<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>土地利用总体规划审查</h3>
    <p>http://{ip}:{port}/omp/geometryService/rest/analysis/tdghsc</p>
    <h4>1.功能</h4>
    <p>土地利用总体规划审查。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。<br/>
        DemoUrl: http://192.168.50.181:8080/omp/geometryService/rest/analysis/tdghsc?layerType=TDYTQ<span>&</span>regionCode=320102&geometry=
        <br/>{%20%22type%22:%20%22feature%22,%20%22geometry%22:%20{%20%22type%22:%20%22Polygon%22,%20%22coordinates%22:%20[%20[%20[%2039674531.467,%203546502.298%20],
        <br/>%20[%2039675759.136,%203544676.670%20],%20[%2039673536.632,%203545533.921%20],%20[%2039674531.467,%203546502.298%20]%20]%20]%20}%20}
    </p>
    <h4>2.参数</h4>
    <table width="84%" border="1">
        <tr>
            <th width="16%" scope="col">参数</th>
            <th width="84%" scope="col">描述</th>
        </tr>
        <tr>
            <td>layerType</td>
            <td>图层类型，可选值为 TDYTQ、JSYDGZQ、GHJBNTBHQ、MZZDJSXM四类，不指定该值即同时分析四类，并返回结果。</td>
        </tr>
        <tr>
            <td>regionCode</td>
            <td>行政区代码</td>
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
    <p>i.	正常返回值：{&ldquo;result&rdquo;:{GeoJSON格式FeatureCollection  }}；<br />
        示例：<br />
        {&ldquo;result&rdquo;:  {<br />
            {<br />
            &quot;type&quot;:  &quot;FeatureCollection&quot;,<br />
            &quot;features&quot;: [<br />
                {<br />
                    &quot;type&quot;:  &quot;Feature&quot;,<br />
                    &quot;geometry&quot;: {<br />
                        &quot;type&quot;: &quot;Polygon&quot;,<br />
                        &quot;coordinates&quot;: [<br />
        [<br />
                            [100, 0], [101, 0], [101,  1], [100, 1], [100, 0]<br />
                            ]<br />
                        ]<br />
                    },<br />
                    &quot;properties&quot;: {<br />
                        &quot;prop0&quot;:  &quot;value0&quot;,<br />
                        &quot;prop1&quot;:  &quot;value1&quot;<br />
                    }<br />
                }<br />
            ]<br />
        }</p>
    <p>} }</p>
    <p>ii 不指定layerType返回结果：{&ldquo;result&rdquo;:{四类分析结果FeatureCollection }};\
        <br/>
        示例：
        <br/>
        {&ldquo;result&rdquo;:  {<br />
            {&ldquo;土地用途分区&rdquo;: {<br />
            &quot;type&quot;:  &quot;FeatureCollection&quot;,<br />
            &quot;features&quot;: [<br />
                {<br />
                    &quot;type&quot;:  &quot;Feature&quot;,<br />
                    &quot;geometry&quot;: {<br />
                        &quot;type&quot;:  &quot;Polygon&quot;,<br />
                        &quot;coordinates&quot;: [<br />
        [<br />
                            [100, 0], [101, 0], [101,  1], [100, 1], [100, 0]<br />
                            ]<br />
                        ]<br />
                    },<br />
                    &quot;properties&quot;: {<br />
                        &quot;prop0&quot;: &quot;value0&quot;,<br />
                        &quot;prop1&quot;:  &quot;value1&quot;<br />
                    }<br />
                }<br />
            ]<br />
        }<br />
        },<br />
        …<br />
        }}
    <p>
        iii.	异常返回值：{"success":false,msg:"error info"}
    </p>
</div>