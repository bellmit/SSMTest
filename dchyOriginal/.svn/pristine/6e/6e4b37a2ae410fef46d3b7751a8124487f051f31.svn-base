<div id="main" style="margin: auto 20px; min-width:660px; ">
    <h3>shp包导入接口</h3>

    <p>http://{ip}:{port}/omp/geometryService/zip/upload</p>
    <br/>
    <h4>1.功能</h4>
    <p>导入shp文件的zip包，zip包中不能再包含文件夹，zip中至少要包含.shp和.dbf文件。调用方式可以在后台传文件流参数，也可以在前端用ajax上传文件方式调用。  </p>
    <h4>2.参数</h4>
    <table width="80%" border="1">
        <tr>
            <th width="18%" scope="col">参数</th>
            <th width="18%" scope="col">类型</th>
            <th width="64%" scope="col">描述</th>
        </tr>
        <tr>
            <td>layerName</td>
            <td>String</td>
            <td>空间图层名称</td>

        </tr>
        <tr>
            <td>inputStream</td>
            <td>InputStream</td>
            <td>zip文件流,(可选)</td>
        </tr>
        <tr>
            <td>file</td>
            <td>MultipartFile</td>
            <td>zip文件(可选)</td>
        </tr>
        <tr>
            <td>check</td>
            <td>Boolean</td>
            <td>值为true时会对要插入的图形要素进行拓扑检查，在图层中已存在的图形要素无法插入,(可选，默认是true)</td>
        </tr>
        <tr>
            <td>dataSource</td>
            <td>String</td>
            <td>数据源名称，可选参数，多数据源下可指定数据源</td>
        </tr>
    </table>
    <h4>3.返回值</h4>

    <p>i. 正常返回值：{&quot;result&quot;:{要素插入成功的图形的geojson格式}；<br/>
        示例：<br/>
        {&quot;result&quot;: {&quot;
        {"type":"FeatureCollection","features":[{"type":"Feature","bbox":[40432764.2308,3499597.9984,40440319.1393,3507576.9068],"geometry":{"coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440319.1393,3503452.5435],[40434267.5034,3507576.9068]]],"type":"Polygon"},"properties":{"OBJECTID":"1611","PRONAME":"insert1"},"id":"Featuref1403cbe08284028b229403cbe080001"},{"type":"Feature","bbox":[40432764.2308,3499597.9984,40440126.412,3507576.9068],"geometry":{"coordinates":[[[40434267.5034,3507576.9068],[40432764.2308,3499597.9984],[40440126.412,3500060.5438],[40434267.5034,3507576.9068]]],"type":"Polygon"},"properties":{"OBJECTID":"1612","PRONAME":"insert2"},"id":"Featuref1403cbe08284028b229403cbe080002"}]}&quot;}}<br/>
        ii. 异常返回值：{&quot;success&quot;:false,msg:&quot;error info&quot;}</p>
</div>