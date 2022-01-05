<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>土地利用现状分析</h3>
    <p>http://{ip}:{port}/omp/geometryService/rest/analysis/tdlyxz</p>
    <br/>
    <h4>1.功能</h4>
    <p>土地利用现状分析，与省厅分析方式保持一致，仅扣除现状地类。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。<br/>
        DemoUrl: http://192.168.50.181:8080/omp/geometryService/rest/analysis/tdlyxz?dltb=SDE.DLTB_H_321204&xzdw=SDE.XZDW_H_321204&geometry=
        <br/>{%20%22type%22:%20%22feature%22,%20%22geometry%22:%20{%20%22type%22:%20%22Polygon%22,%20%22coordinates%22:%20[%20[%20[%2040486073.639,%203589015.753%20],
        <br/>%20[%2040487287.339,%203584970.083%20],%20[%2040484961.08,%203586520.923%20],%20[%2040486073.639,%203589015.753%20]%20]%20]%20}%20}
    </p>
    <h4>2.参数</h4>
    <table width="80%" border="1">
        <tr>
            <th width="18%" scope="col">参数</th>
            <th width="82%" scope="col">描述</th>
        </tr>
        <tr>
            <td>regionCode</td>
            <td>行政区代码，与下两个参数互斥</td>
        </tr>
        <tr>
            <td>dltb</td>
            <td>地类图斑图层名称，可选，自定义地类图斑图层</td>
        </tr>
        <tr>
            <td>xzdw</td>
            <td>现状地物图层名称，可选，自定义现状地物图层</td>
        </tr>
        <tr>
            <td>geometry</td>
            <td>图形要素，为GeoJSON格式</td>
        </tr>
        <tr>
            <td>dataSource</td>
            <td>可选参数，多数据源下可指定数据源</td>
        </tr>
    </table>
    <h4>3.返回值</h4>
    <p>i.	正常返回值：{&ldquo;result&rdquo;:{分类名与分类值}}；<br />
        示例：<br />
        {&ldquo;result&rdquo;:  {<br />
            &quot;分类1&quot;: &quot;面积值&quot;,<br />
            &quot;分类2&quot;: &quot;面积值&quot;,<br />
               …<br />
        } }</p>
    ii.	异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}
</div>