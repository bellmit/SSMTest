<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>更新要素</h3>
    <p>http://{ip}:{port}/omp/geometryService/rest/update</p>
    <h4>1.功能</h4>
    <p>更新图形要素，可包含属性以及空间数据。Http请求Get与Post方式均可，考虑到参数有可能数据量过大，建议采用Post方式。
        <br/>
        DemoUrl: http://192.168.50.181:8080/omp/geometryService/rest/update?layerName=sde.test5_Polygon&primaryKey=643&geometry={%20%22type%22:%20%22Polygon%22,%20%22coordinates%22:%20[%20[%20[40453557.304,3514493.430],%20[40455412.308,3517016.236],%20[40453779.904,3517387.236],[40453557.304,3514493.430]%20]%20]%20}</p>
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
            <td>primaryKey</td>
            <td>更新要素主键值</td>
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
    <p>i.	正常返回值：{{&ldquo;result&rdquo;:{true}}；<br />
        示例：<br />
        {&ldquo;result&rdquo;:  {true}}</p>
    ii.	异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}
</div>