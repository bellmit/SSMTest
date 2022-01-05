<#assign cssContent>
    <style>
        ::-webkit-scrollbar {
            width: 10px;
            height: 10px
        }

        ::-webkit-scrollbar-button:vertical {
            display: none
        }

        ::-webkit-scrollbar-corner, ::-webkit-scrollbar-track {
            background-color: #e2e2e2
        }

        ::-webkit-scrollbar-thumb {
            border-radius: 0;
            background-color: rgba(0, 0, 0, .3)
        }

        ::-webkit-scrollbar-thumb:vertical:hover {
            background-color: rgba(0, 0, 0, .35)
        }

        ::-webkit-scrollbar-thumb:vertical:active {
            background-color: rgba(0, 0, 0, .38)
        }

        table tr td {
            border: 1px solid #ddd;
            padding: 5px 10px;
            height: 50px;
            text-align: center;
        }

        table tr th {
            border: 1px solid #ddd;
            padding: 10px;
            max-height: 60px;
            min-width: 90px;
            text-align: center;
        }

        table {
            background-color: transparent;
            border: 1px solid #ddd;
            table-layout: auto;
            border-collapse: collapse;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 15px;
            width: 100%;
        }

        .infoTable {
            background-color: transparent;
            border: 0px solid #DDDDDD;
            table-layout: auto;
            border-collapse: collapse;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 15px;
            width: 100%;
        }

        .infoTable tr th {
            border: 0px solid #DDDDDD;
            max-height: 60px;
            width: 100px;
            text-align: right;
        }

        .infoTable tr td {
            border: 0px solid #DDDDDD;
            padding: 5px 10px;
            height: 50px;
            text-align: left;
        }

        .nav-tabs {
            height: 40px !important;
        }

        h5 {
            font-weight: normal;
            color: #188074;
            margin-left: 10px;
        }

        .wrapper,
        .header {
            width: 84%;
            margin-bottom: 15px;
        }

        .header > h3 {
            font-weight: normal;
            color: #188074
        }

        .header > div {
            margin-top: -43px;
            margin-right: auto;
        }

        .btn-options {
            margin-top: -10px;
        }

        .btn-options button {
            color: #188074;
        }

        .basic-container, .func-container {
            width: 100%;
        }

        .basic-container .row {
            margin-bottom: 15px;
        }

        .basic-container .tab-content {
            margin-top: 5px;
            overflow-x: auto;
        }

        .basic-container .export-container {
            padding-top: 20px;
        }

        .basic-container .export-container a {
            margin-right: 12px;
        }

        .func-header-panel {
            margin-top: 20px;
        }

        .func-main-wrapper {
            width: 320px;
            float: left;
            padding-right: 20px;
        }

        .dropdown-menu li {
            text-align: left;
        }

    </style>
</#assign>
<#assign fixed>
    0.00
</#assign>
<#assign resultMap=tplData.result!/>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab11" data-toggle="tab">耕地质量等别分析结果</a></li>
    </ul>
</div>

<div class="tab-content">
    <#if resultMap?size gt 0>
        <div>
            <div class="pull-right" style="margin-bottom: 10px;">
                <h5 style="margin-right: 30px"></h5>
                单位：平方米
            </div>
        </div>
        <table>
            <tr>
                <th>地类</th>
                <th>国家利用等</th>
                <th>面积</th>
            </tr>
            <#list resultMap?keys as key>
                <#assign resultOne=resultMap[key]!>
                <#if key!="pjdj_zmj">
                    <tr>
                        <td>${resultOne["dlmc"]!}</td>
                        <td>${resultOne["gjlyd"]!}</td>
                        <td>${resultOne["area"]!?string(fixed)}</td>
                    </tr>
                </#if>
            </#list>
            <#list resultMap?keys as key>
                <#assign resultOne=resultMap[key]!>
                <#if key=="pjdj_zmj">
                    <tr>
                        <td>平均等级
                        </td>
                        <td>${resultOne["gjlyd"]!}</td>
                        <td>----</td>
                    </tr>
                    <tr>
                        <td>总面积
                        </td>
                        <td>----</td>
                        <td>${resultOne["area"]!?string(fixed)}</td>
                    </tr>
                </#if>
            </#list>
        </table>
    <#--</div>-->
    <#else>
        <h5 class="text-muted text-center">无结果</h5>
    </#if>
</div>

