<!-- 不动产分析结果页面 -->
<link rel="stylesheet" href="/omp/static/thirdparty/remodal/remodal-default-theme.css">
<link rel="stylesheet" href="/omp/static/thirdparty/remodal/remodal.css">
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#bdcTab1" data-toggle="tab">汇总</a></li>
        <li><a href="#bdcTab2" data-toggle="tab">详情</a></li>
    </ul>
</div>
<div class="tab-content">
    <div id="bdcTab1" class="tab-pane fade in active">
    <#if tplData.result??&&tplData.total??>
        <#if (tplData.result?size>0)>
            <table>
                <tr>
                    <th>房地产权</th>
                    <th>抵押</th>
                    <th>查封</th>
                    <th>异议</th>
                    <th>预告</th>
                    <th>地役权</th>
                </tr>
                <#assign item = tplData.total />
                <tr>
                    <td><#if item.fdcq??><a href="#fdcqmodal" style="cursor: pointer;">${item.fdcq.count}</a><#else>0</#if></td>
                    <td><#if item.dy??><a href="#dymodal" style="cursor: pointer;">${item.dy.count}</a><#else>0</#if></td>
                    <td><#if item.cf??><a href="#cfmodal" style="cursor: pointer;">${item.cf.count}</a><#else>0</#if></td>
                    <td><#if item.yy??><a href="#yymodal" style="cursor: pointer;">${item.yy.count}</a><#else>0</#if></td>
                    <td><#if item.yg??><a href="#ygmodal" style="cursor: pointer;">${item.yg.count}</a><#else>0</#if></td>
                    <td><#if item.dyq??><a href="#dyqmodal" style="cursor: pointer;">${item.dyq.count}</a><#else>0</#if></td>
                </tr>
            </table>
        </#if>
    <#else >
        <div>无分析结果！</div>
    </#if>
    </div>
<#if tplData.result??&&tplData.total??>
    <div id="bdcTab2" class="tab-pane fade">
        <#if (tplData.result?size>0)>
        <#--<a href="#modal">Call the modal with data-remodal-id="modal"</a>-->
            <table>
                <tr>
                    <th>地籍号</th>
                    <th>土地坐落</th>
                <#--<th>是否抵押</th>-->
                <#--<th>是否查封</th>-->
                <#--<th>是否异议</th>-->
                <#--<th>是否预告</th>-->
                </tr>
                <#list tplData.result as item>
                    <tr>
                        <td>${item.DJH!}</td>
                        <td>${item.TDZL!}</td>
                    <#--<td></td>-->
                    <#--<td></td>-->
                    <#--<td></td>-->
                    <#--<td></td>-->
                    </tr>
                </#list>
            </table>
        <#else >
            <div class="tab-content" style="border-top: 1px solid #ddd;">
                无分析结果
            </div>
        </#if>
    </div>
</#if>
</div>

<#--<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="modal" aria-labelledby="modalTitle" aria-describedby="modalDesc" tabindex="-1">-->
<#--<button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>-->
<#--<div>-->
<#--<h2 id="modalTitle">Remodal v1</h2>-->
<#--<p id="modalDesc">-->
<#--The modal done right.<br>-->
<#--Tell a man, tell another man, tell the next man.-->
<#--</p>-->
<#--</div>-->
<#--</div>-->
<#if tplData.result??&&tplData.total??>
<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="dymodal" aria-labelledby="modalTitle"
     aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>抵押详情</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>权利人</th>
                <th>证书号</th>
                <th>抵押开始日期</th>
                <th>抵押结束日期</th>
                <th>详情</th>
            </tr>
            <#if  tplData.total.dy??>
                <#list tplData.total.dy.detail as item>
                    <tr>
                        <td>${item.qlrmc!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.kssj??>${item.kssj?date}</#if></td>
                        <td><#if item.jssj??>${item.jssj?date}</#if></td>
                        <td>${item.detail!}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>

<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="cfmodal" aria-labelledby="modalTitle"
     aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>查封详情</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>权利人</th>
                <th>证书号</th>
                <th>查封开始日期</th>
                <th>查封结束日期</th>
                <th>详情</th>
            </tr>
            <#if  tplData.total.cf??>
                <#list tplData.total.cf.detail as item>
                    <tr>
                        <td>${item.qlrmc!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.kssj??>${item.kssj?date}</#if></td>
                        <td><#if item.jssj??>${item.jssj?date}</#if></td>
                        <td>${item.detail!}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>

<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="yymodal" aria-labelledby="modalTitle" aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>异议详情</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>权利人</th>
                <th>证书号</th>
                <th>开始日期</th>
                <th>结束日期</th>
                <th>详情</th>
            </tr>
            <#if  tplData.total.yy??>
                <#list tplData.total.yy.detail as item>
                    <tr>
                        <td>${item.qlrmc!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.kssj??>${item.kssj?date}</#if></td>
                        <td><#if item.jssj??>${item.jssj?date}</#if></td>
                        <td>${item.detail!}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>

<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="ygmodal" aria-labelledby="modalTitle" aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>预告详情</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>权利人</th>
                <th>证书号</th>
                <th>开始日期</th>
                <th>结束日期</th>
                <th>详情</th>
            </tr>
            <#if  tplData.total.yg??>
                <#list tplData.total.yg.detail as item>
                    <tr>
                        <td>${item.qlrmc!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.kssj??>${item.kssj?date}</#if></td>
                        <td><#if item.jssj??>${item.jssj?date}</#if></td>
                        <td>${item.detail!}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>

<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="dyqmodal" aria-labelledby="modalTitle" aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>地役权</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>权利人</th>
                <th>证书号</th>
                <th>开始日期</th>
                <th>结束日期</th>
                <th>详情</th>
            </tr>
            <#if  tplData.total.dyq??>
                <#list tplData.total.dyq.detail as item>
                    <tr>
                        <td>${item.qlrmc!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.kssj??>${item.kssj?date}</#if></td>
                        <td><#if item.jssj??>${item.jssj?date}</#if></td>
                        <td>${item.detail!}</td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>


<div class="remodal remodal-is-initialized remodal-is-opened" data-remodal-id="fdcqmodal" aria-labelledby="modalTitle" aria-describedby="modalDesc" tabindex="-1">
    <button data-remodal-action="close" class="remodal-close" aria-label="Close"></button>
    <h3>房地产权</h3>
    <div class="bdcDetail">
        <table>
            <tr>
                <th>地籍号</th>
                <th>证书号</th>
                <th>权利人</th>
            <#--<th>开始日期</th>-->
            <#--<th>结束日期</th>-->
                <th>详情</th>
            </tr>
            <#if  tplData.total.fdcq??>
                <#list tplData.total.fdcq.detail as item>
                    <tr>
                        <td>${item.djh!}</td>
                        <td>${item.tdzh!}</td>
                        <td><#if item.qlrmc??>${item.qlrmc!}<#else>${item.qlr!}</#if></td>
                    <#--<td><#if item.kssj??>${item.kssj?date}</#if></td>-->
                    <#--<td><#if item.jssj??>${item.jssj?date}</#if></td>-->
                        <td><#if item.zl??>${item.zl}<#else>${item.detail!}</#if></td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>
</#if>

<script src="/omp/static/thirdparty/remodal/remodal.min.js"></script>


