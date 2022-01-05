<html>
<div id="content">
<table style="float: left;width: 400px;" cellspacing="0" border="1">
    <!--[if !IE]><!--> <tr>
        <td colspan="3" style="height: 75px;"></td>
    </tr>
    <!--<![endif]-->

    <!--[if IE]>  <td colspan="3" style="height: 68px;"></td> <![endif]-->
    
        <tr>
        <td rowspan="50" width="15px">拟占用土地总面积</td>
        <td class="ie-td" colspan="2">总面积</td>
    </tr>
    <tr>
        <td rowspan="11" style="width: 70px;text-align: center;">农用地</td>
        <td class="ie-td">小计</td>
    </tr>
    <tr>
        <td class="ie-td">耕地</td>
    </tr>
    <tr>
        <td class="ie-td">园地</td>
    </tr>
    <tr>
        <td class="ie-td">林地</td>
    </tr>
    <tr>
        <td class="ie-td">天然牧草地</td>
    </tr>
    <tr>
        <td class="ie-td">人工牧草地</td>
    </tr>
    <tr>
        <td class="ie-td">农村道路</td>
    </tr>
    <tr>
        <td class="ie-td">坑塘水面</td>
    </tr>
    <tr>
        <td class="ie-td">沟渠</td>
    </tr>
    <tr>
        <td class="ie-td">设施农用地</td>
    </tr>
    <tr>
        <td class="ie-td">田坎</td>
    </tr>
    <tr>
        <td rowspan="7" style="width: 70px;text-align: center;">建设用地</td>
        <td class="ie-td">小计</td>
    </tr>
    <tr>
        <td class="ie-td">城镇及工矿用地</td>
    </tr>
    <tr>
        <td class="ie-td">铁路用地</td>
    </tr>
    <tr>
        <td class="ie-td">公路用地</td>
    </tr>
    <tr>
        <td class="ie-td">机场用地</td>
    </tr>
    <tr>
        <td class="ie-td">港口码头用地</td>
    </tr>
    <tr>
        <td class="ie-td">管道运输用地</td>
    </tr>
    <tr>
        <td rowspan="11" style="width: 70px;text-align: center;">未利用地</td>
        <td class="ie-td">小计</td>
    </tr>
    <tr>
        <td class="ie-td">沙地</td>
    </tr>
    <tr>
        <td class="ie-td">裸地</td>
    </tr>
</table>

<#assign fixed>0.##
</#assign>

<#list data as dataRow>
    <#assign dataRowSize = dataRow.analysisData?size>
    <#assign analysisTotal=dataRow.analysisData[dataRowSize-1]>
    <#assign tot=analysisTotal.categoryB>
<table style="float: left;width: 400px;margin-left: -2px" cellspacing="0" border="1">
    <tr>
        <td colspan="3">${dataRow.year!}年度分析</td>
    </tr>
    <tr>
        <td rowspan="2">合计</td>
        <td colspan="2">其中</td>
    </tr>
    <tr>
        <td>国有面积</td>
        <td>集体面积</td>
    </tr>
    <tr>
        <td>${analysisTotal.sumArea!?string(fixed)}</td>
        <td>${analysisTotal.sumAreaGy!?string(fixed)}</td>
        <td>${analysisTotal.sumAreaJt!?string(fixed)}</td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("农用地")>${tot["农用地"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("农用地_gy")>${tot["农用地_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("农用地_jt")>${tot["农用地_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("01")>${tot["01"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("01_gy")>${tot["01_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("01_jt")>${tot["01_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("02")>${tot["02"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("02_gy")>${tot["02_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("02_jt")>${tot["02_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("03")>${tot["03"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("03_gy")>${tot["03_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("03_jt")>${tot["03_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("041")>${tot["041"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("041_gy")>${tot["041_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("041_jt")>${tot["041_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("042")>${tot["042"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("042_gy")>${tot["042_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("042_jt")>${tot["042_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("104")>${tot["104"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("104_gy")>${tot["104_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("104_jt")>${tot["104_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("114")>${tot["114"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("114_gy")>${tot["114_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("114_jt")>${tot["114_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("117")>${tot["117"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("117_gy")>${tot["117_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("117_jt")>${tot["117_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("122")>${tot["122"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("122_gy")>${tot["122_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("122_jt")>${tot["122_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("123")>${tot["123"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("123_gy")>${tot["123_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("123_jt")>${tot["123_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>

    <tr>
        <td><#if tot?keys?seq_contains("建设用地")>${tot["建设用地"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("建设用地_gy")>${tot["建设用地_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("建设用地_jt")>${tot["建设用地_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("20")>${tot["20"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("20_gy")>${tot["20_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("20_jt")>${tot["20_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("101")>${tot["101"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("101_gy")>${tot["101_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("101_jt")>${tot["101_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("102")>${tot["102"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("102_gy")>${tot["102_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("102_jt")>${tot["102_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("105")>${tot["105"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("105_gy")>${tot["105_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("105_jt")>${tot["105_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("106")>${tot["106"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("106_gy")>${tot["106_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("106_jt")>${tot["106_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("107")>${tot["107"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("107_gy")>${tot["107_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("107_jt")>${tot["107_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>

    <tr>
        <td><#if tot?keys?seq_contains("未利用地")>${tot["未利用地"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("未利用地_gy")>${tot["未利用地_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("未利用地_jt")>${tot["未利用地_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("126")>${tot["126"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("126_gy")>${tot["126_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("126_jt")>${tot["126_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>
    <tr>
        <td><#if tot?keys?seq_contains("127")>${tot["127"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("127_gy")>${tot["127_gy"]!?string(fixed)}<#else >0</#if></td>
        <td><#if tot?keys?seq_contains("127_jt")>${tot["127_jt"]!?string(fixed)}<#else >0</#if></td>
    </tr>


</table>
</#list>
</div>


<style>
    td{
        text-align: center;
        height: 20px;
    }

   
               .ie-td{
                   text-align: center;
                   height: 23px!important;
               }
</style>

<script src="<@com.rootPath/>/static/thirdparty/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        var width  = 400*${data?size}+410;
        $("#content").width(width+"px");
    })

</script>
</html>