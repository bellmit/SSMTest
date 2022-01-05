<#if (tplData?keys?size>0)>
    <#assign analysisResult=tplData.result!/>
    <#if analysisResult??>
        <#assign fhgh=analysisResult.fhgh!/>
        <#assign bfhgh=analysisResult.bfhgh!/>
        <#assign ytj=analysisResult.ytj!/>

        <#assign fhghTotal=fhgh.total!/>
        <#assign bfhghTotal=bfhgh.total!/>
        <#assign ytjTotal=ytj.total!/>

        <#assign fhghDetail=fhgh.detail!/>
        <#assign bfhghDetail=bfhgh.detail!/>
        <#assign ytjDetail=ytj.detail!/>

        <#assign bfhghShp=bfhgh.shpId!/>
        <#assign ytjShp=ytj.shpId!/>

    <#--<#if fhghDetail?size &gt;0>-->
    <#--<#assign firstItem=fhghDetail[0]/>-->
    <#--<#elseif bfhghDetail?size &gt;0>-->
    <#--<#assign firstItem=bfhghDetail[0]/>-->
    <#--<#elseif ytjDetail?size &gt;0>-->
    <#--<#assign firstItem=ytjDetail[0]/>-->
    <#--</#if>-->
    </#if>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tdlyghTab1" data-toggle="tab">汇总</a></li>
        <li><a href="#tdlyghTab2" data-toggle="tab">详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="tdlyghTab1" class="tab-pane fade in active">
        <#if tplData.level??&&tplData.level=="mas">
            <table>
                <tr>
                    <th colspan="5">符合规划</th>
                </tr>
                <tr>
                    <th>建设用地</th>
                    <th>农用地</th>
                    <th>基本农田</th>
                    <th>其他用地</th>
                    <th>总面积</th>
                </tr>
                <tr>
                    <td>${fhghTotal.jsyd!0}</td>
                    <td>${fhghTotal.nyd!0}</td>
                    <td>${fhghTotal.jbnt!0}</td>
                    <td>${fhghTotal.unused!0}</td>
                    <td>${fhghTotal.sum!0}</td>
                </tr>
            </table>
            <table>
                <tr>
                    <th colspan="<#if bfhghShp!="">6<#else >5</#if>">不符合规划</th>
                </tr>
                <tr>
                    <th>建设用地</th>
                    <th>农用地</th>
                    <th>基本农田</th>
                    <th>其他用地</th>
                    <th>总面积</th>
                    <#if bfhghShp!=""><th>shp成果</th></#if>
                </tr>
                <tr>
                    <td>${bfhghTotal.jsyd!0}</td>
                    <td>${bfhghTotal.nyd!0}</td>
                    <td>${bfhghTotal.jbnt!0}</td>
                    <td>${bfhghTotal.unused!0}</td>
                    <td>${bfhghTotal.sum!0}</td>
                    <#if bfhghShp!=""><td><a class="btn btn-info btn-small" onclick=exportFeatures('${bfhghShp!}',0);>导出</a></td></#if>
                </tr>
            </table>
            <table>
                <tr>
                    <th colspan="<#if ytjShp!="">6<#else >5</#if>">有条件建设</th>
                </tr>
                <tr>
                    <th>建设用地</th>
                    <th>农用地</th>
                    <th>基本农田</th>
                    <th>其他用地</th>
                    <th>总面积</th>
                    <#if ytjShp!=""><th>shp成果</th></#if>
                </tr>
                <tr>
                    <td>${ytjTotal.jsyd!0}</td>
                    <td>${ytjTotal.nyd!0}</td>
                    <td>${ytjTotal.jbnt!0}</td>
                    <td>${ytjTotal.unused!0}</td>
                    <td>${ytjTotal.sum!0}</td>
                    <#if ytjShp!=""><td><a class="btn btn-info btn-small" onclick=exportFeatures('${ytjShp!}',0);>导出</a></td></#if>
                </tr>
            </table>
        </#if>
    </div>
    <div id="tdlyghTab2" class="tab-pane fade in">
        <table>
            <tr>
                <th>类型</th>
                <th>地类代码</th>
                <th>地类名称</th>
                <th>面积(平方米)</th>
            </tr>
            <#list fhghDetail as fhghItem>
                <tr>
                    <td>符合规划面</td>
                    <td>${fhghItem.DLBM!}</td>
                    <td>${fhghItem.DLMC!}</td>
                    <td>${fhghItem.SHAPE_AREA!}</td>
                </tr>
            </#list>
            <#list bfhghDetail as bfhghItem>
                <tr>
                    <td>不符合规划面</td>
                    <td>${bfhghItem.DLBM!}</td>
                    <td>${bfhghItem.DLMC!}</td>
                    <td>${bfhghItem.SHAPE_AREA!}</td>
                </tr>
            </#list>
            <#list ytjDetail as ytjItem>
                <tr>
                    <td>有条件建设区</td>
                    <td>${ytjItem.DLBM!}</td>
                    <td>${ytjItem.DLMC!}</td>
                    <td>${ytjItem.SHAPE_AREA!}</td>
                </tr>
            </#list>
        </table>
    </div>
</div>
<#else >无分析结果</#if>