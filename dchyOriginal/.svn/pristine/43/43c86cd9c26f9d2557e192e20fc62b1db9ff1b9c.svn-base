<#---->

<#macro html title="" import="" css="">
    <!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
        <title>${title}</title>
        <script type="text/javascript" language="javascript">
            var folder = "<@com.rootPath/>";
        </script>
        <#if import??>
            <#list import?split(",") as lib>
                <#switch lib>
                    <#case "jquery">
                        <@script name="static/thirdparty/jquery/jquery-1.11.1.min.js"></@script>
                        <@script name="static/thirdparty/jquery/jquery-ui.min.js"></@script>
                        <@script name="static/thirdparty/jquery/jquery.validationEngine-zh_CN.js"></@script>
                        <@script name="static/thirdparty/jquery/jquery.validationEngine.js"></@script>
                        <@script name="static/thirdparty/jquery/jquery.gridtree.js"></@script>
                        <@script name="static/thirdparty/jquery/jquery.ajaxfileupload.js"></@script>
                        <#break />
                    <#case "swf">
                        <@script name="resources/js/swfobject.js"></@script>
                        <#break />
                    <#case "portal">
                        <@script name="static/js/portal/lib/bootstrap.js"></@script>
                        <@script name="static/js/portal/main.js"></@script>
                        <@style name="static/css/portal/bootstrap.min.css"></@style>
                        <@style name="static/css/portal/main.css"></@style>
                        <#break/>
                    <#case "fullscreen">
                        <@style name="static/css/portal/fullscreen.css"></@style>
                        <@script name="static/js/portal/fullscreen.js"></@script>
                        <#break/>
                    <#case "video">
                        <@script name="static/js/video/webVideoCtrl.js"></@script>
                        <@script name="static/js/video/jsPlugin-1.0.0.min.js"></@script>
                        <#break/>
                    <#case "dhVideo">
                        <@script name="static/js/video/json2.js"></@script>
                        <#break/>
                    <#case "layui">
                        <@style name="static/thirdparty/layui/css/layui.css"></@style>
                        <@script name="static/thirdparty/layui/layui.js"></@script>
                        <#break/>
                    <#case "finereport">
                        <@style name="static/lib/layui/css/layui.css"></@style>
                        <@style name="static/lib/layer/2.1/skin/layer.css"></@style>
                        <@style name="static/lib/finereport/finereport.css"></@style>
                        <@script name="static/lib/finereport/finereport.js"></@script>
                        <@script name="static/lib/layui/layui.js"></@script>
                        <@script name="static/lib/jquery/jquery.form.js"></@script>
                        <@script name="static/lib/jquery.validation/1.14.0/jquery.validate.min.js"></@script>
                        <@script name="static/lib/jquery.validation/1.14.0/messages_zh.min.js"></@script>
                        <@script name="static/lib/jquery/jquery-ui.js"></@script>
                        <@style name="static/lib/layui/css/layui.css"></@style>
                        <@script name="static/lib/layui/layui.js"></@script>
                        <@style name="static/lib/layer/2.1/skin/layer.css"></@style>
                        <@script name="static/lib/layer/2.1/layer.js"></@script>
                        <@script name="static/lib/My97DatePicker/WdatePicker.js"></@script>
                        <#break />
                    <#default>

                </#switch>
            </#list>
        </#if>
        <#if css?? && (css!="")>
            <#list css?split(",") as name>
                <@style name="static/css/portal/${name}.css"></@style>
            </#list>
        </#if>
    </head>
    <body>
    <!-- /#header -->
    <#nested />

    </body>
    </html>
</#macro>

<#macro main title="${env.getEnv('local.title')}国土资源“一张图”信息门户" import="" css="">
    <@com.html title="${title!}" import="${import!}" css="${css!}">
        <div id="hearder" class="navbar navbar-inverse">
            <div class="navbar-inner">
                <div class="container">
                    <div class="brand">${env.getEnv('local.title')}国土资源一张图 运维管理</div>

                    <div class="pull-right">
                        <div class="small-nav">
                            <ul class="nav nav-pills">
                                <li><a href="#">登录</a></li>
                                <li><a href="#">江苏省国土资源厅</a></li>
                                <li><a href="#">帮助</a></li>
                            </ul>
                        </div>
                        <div class="main-nav">
                            <ul class="nav nav-pills">
                                <li><a href="#">一张图</a></li>
                                <li><a href="<@com.rootPath/>/portal/thememap">专题图</a></li>
                                <li><a href="#">统计报表</a></li>
                                <li><a href="<@com.rootPath/>/portal/rescenter/index">资源中心</a></li>
                                <li><a href="<@com.rootPath/>/portal/api">地图API</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="mainBody">
            <#nested />
        </div>
        <div class="footer">
            <div class="container">
                <div class="pull-right">
                    <a href="#">意见反馈</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">法律声明</a>
                </div>
                &copy;2013 ${env.getEnv('local.title')}国土资源信息中心
            </div>
        </div>
    </@com.html>
</#macro>

<#macro htmlWithTitle title="" import="" css="">
    <@com.html title="${title!}" import="${import!}" css="${css!}">
        <div id="header">
            <div class="container">
                <div class="brand"><h1>${env.getEnv('local.title')}国土资源一张图 运维管理</h1></div>

                <div class="head-nav-wrapper">
                    <ul id="J_NAV_HEAD" class="nav nav-pills">
                        <li class="auth-wrap">
                            <a><i class="icon icon-white icon-user"></i> <@sec.name/></a>
                        </li>
                        <li class="auth-wrap">
                            <a href="${path_oms}/logout?url=${path_omp}" data-ask="确定退出系统吗?" class="j_ask"><i
                                        class="icon icon-white icon-share"></i> 注销</a>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- /.container -->
        </div>
        <#nested />
    </@com.html>
</#macro>

<#macro swf title="" swfname="" tpl="">
    <@com.html title="${title!}" import="swf">
        <style type="text/css" media="screen">
            html, body {
                height: 100%;
            }

            body {
                margin: 0;
                padding: 0;
                overflow: auto;
                text-align: center;
                background-color: #25639c;
            }

            object:focus {
                outline: none;
            }

            #flashContent {
                top: 50%;
                width: 360px;
                margin: 0px auto;
            }

            #flashContent p {
                color: #ffffff;
            }
        </style>

        <#nested />

        <script type="text/javascript">
            var swfVersionStr = "11.5.0";
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#fcfffb";
            params.allowscriptaccess = "sameDomain";
            //params.allowfullscreen = "true";
            params.allowFullScreenInteractive = "true";
            params.wmode = "direct";
            var attributes = {};
            attributes.id = "${swfname}";
            attributes.name = "${swfname}";
            attributes.align = "middle";
            attributes.tpl = "${tpl!}";
            swfobject.embedSWF(
                "<@rootPath/>/flash/${swfname}.swf", "flashContent",
                "100%", "100%",
                swfVersionStr, xiSwfUrlStr,
                flashvars, params, attributes);
            swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        </script>

        <div id="flashContent">
            <p>
                亲，难道你不知道看Flash要装Adobe Flash Player的么，记得安装11.5.0以上的版本哦！<br/>
                点击以下链接即可下载安装哦~<br/>安装后记得重启浏览器。
            </p>
            <script type="text/javascript">
                var axUrl = "<@rootPath/>/bin/install_flash_player_ax.exe";
                var otherUrl = "<@rootPath/>/bin/install_flash_player.exe";
                var onlineUrl = "http://get.adobe.com/cn/flashplayer/";
                var playerUrl = window.ActiveXObject ? axUrl : otherUrl;
                document.write("<a href='" + onlineUrl + "'><images src='<@rootPath/>/images/get_flash_player.gif'/> </a>");
            </script>
        </div>
    </@com.html>
</#macro>

<#---->
<#macro script name>
    <script src="<@rootPath/>/${name}" type="text/javascript"></script>
</#macro>

<#macro style name>
    <link href="<@rootPath/>/${name}" type="text/css" media="screen" rel="stylesheet"/>
</#macro>

<#macro rootPath>${springMacroRequestContext.getContextPath()}</#macro>

<#macro tdghsc key="">
    <#if result[key]??>
        <#assign info=result["${key}"].info>
        <h5 style="font-weight: normal; color:#188074"><span class="icon icon-double-angle-right"></span>
            总面积 ${(info[0].AREA)!?string("####")}&nbsp;m<sup>2</sup></a></h5>
        <div>
        <ul class="nav nav-tabs">
            <#list info[1..] as type>
                <li <#if type_index==0>class="active"</#if> ><a href="#info${info?size}${type_index}"
                                                                data-toggle="tab">${type["LXMC"]!}</a></li>
            </#list>
        </ul>
        <div class="tab-content">
            <#list info[1..] as parent>
                <#assign detail=parent.detail>
                <div id="info${info?size}${parent_index}" class="tab-pane fade <#if parent_index==0> in active</#if>">
                    <h5><span class="icon icon-double-angle-right"></span> 所占面积 ${parent["AREA"]!?string("####")}&nbsp;m<sup>2</sup></a>
                    </h5>
                    <h5><span class="icon icon-double-angle-right"></span> 所占比例 ${parent["PER"]!?string("##")}%</a></h5>
                    <h5><span class="icon icon-double-angle-right toggle" style="cursor:pointer;"></span> 详细信息</h5>
                    <div class="detailPanel">
                        <#if (detail?size>0)>
                            <table>
                            <tr>
                                <th>序号</th>
                                <#list detail[0]?keys as key>
                                    <th>${key}</th>
                                </#list>
                            </tr>
                            <#list detail as child>
                                <tr>
                                    <td>${child_index+1}</td>
                                    <#list child?keys as key>
                                        <td><#if (key?contains("标识码")||key?contains("面积"))&&child[key]?exists>${child[key]!?string('####')}<#else>${child[key]!?string}</#if></td>
                                    </#list>
                                </tr>
                            </#list>
                            </table><#else ><h5>无</h5></#if>
                    </div>
                </div>
            </#list>
        </div>
        </div><#else ><h5>该分类下无分析结果</h5>
    </#if>
</#macro>

<#macro tdghsc1 key="" unit=""  decimal="">
    <#if result[key]??>
        <#assign info=result["${key}"].info>
        <#assign rItem=result["${key}"]>
        <table style="margin-top: 20px;">
            <tr>
                <th>类别</th>
                <th>所占面积(<@com.unitConversion unit=unit></@com.unitConversion>)</th>
                <th>详细</th>
            </tr>
            <#list info[1..] as item>
                <tr>
                    <td style="text-align: center;">${item["LXMC"]!}</td>
                    <td>${item["AREA"]!?string("${decimal!'####.####'}")}</td>
                    <td style="text-align: center;" rel="#detailPanel${key}${item_index}"><a
                                onclick="detailInfo(${item_index});" style="cursor:pointer;"><span>详细信息</span></a></td>
                </tr>
            </#list>
        </table>
        <#if rItem.shape??>
            <#assign shape = rItem.shape/>
            <div style="text-align: center;padding-top: 20px;">
                <#-- <a class="btn btn-primary" data-toggle="tooltip" data-placement="bottom"
                    style="margin-right: 12px;"
                    title="<div style='float:left;'><button class='btn btn-default' data-geo='${shape.shpId!}' onclick='exportFeature(this,0)'> shp</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' data-geo='${shape.shpId!}' onclick='exportFeature(this,1)'> dwg</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' data-geo='${shape.coords!}' onclick='exportFeature(this,3)'>Txt</button></div>">导出符合规划图形</a>-->
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                        导出符合规划图形 <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)" onclick="exportFeature(this,0)"
                               data-geo="${shape.shpId!}">SHP</a></li>
                        <li><a href="javascript:void(0)" data-geo="${shape.shpId!}"
                               onclick="exportFeature(this,1)">CAD</a></li>
                        <li><a href="javascript:void(0)" data-geo="${shape.coords!}"
                               onclick="exportFeature(this,3)">TXT</a></li>
                    </ul>
                </div>
            </div>
        </#if>
        <#list info[1..] as parent>
            <#assign detail=parent.detail>
            <div class="detailPanel" id="detailPanel${key}${parent_index}">
                <div style="width: 100%;margin-top: 30px; max-height: 580px;overflow-y: auto;padding-bottom: 20px;">
                    <#if (detail?size>0)>
                        <table>
                        <tr>
                            <th>序号</th>
                            <#list detail[0]?keys as key>
                                <#if key?contains('OG_PRO_')>
                                <#else >
                                    <th>${key}</th>
                                </#if>
                            </#list>
                        </tr>
                        <#list detail as child>
                            <tr>
                                <td>${child_index+1}</td>
                                <#list child?keys as key>
                                    <#if key?contains('OG_PRO_')>
                                    <#else >
                                        <td><#if key?contains("标识码")&&child[key]?exists><#if child[key]?is_string>${child[key]!}<#else>${child[key]!?string('####')}</#if>
                                            <#elseif key?contains("面积")&&child[key]??>${child[key]!?string('####.####')}
                                            <#else>${child[key]!?string}</#if></td>
                                    </#if>
                                </#list>
                            </tr>
                        </#list>
                        </table><#else ><h5>无</h5></#if>
                </div>
            </div>
        </#list>
    <#else ><h5>该分类下无分析结果</h5>
    </#if>
</#macro>

<#macro tdghsc2 key="" unit=""  decimal="">
    <#if result[key]??>
        <#assign info=result["${key}"].info>
        <#assign rItem=result["${key}"]>
        <#assign processDatas=result["${key}"].processDatas>
        <div>
            <ul class="nav nav-tabs">
                <li class="active"><a href="#ghtab1${key}" data-toggle="tab">分析汇总</a></li>
                <li><a href="#ghtab2${key}" data-toggle="tab">分析详情</a></li>
            </ul>
        </div>
        <div class="tab-content">
            <div id="ghtab1${key}" class="tab-pane fade in active">
                <table style="margin-top: 20px;">
                    <tr>
                        <th>类别</th>
                        <th>所占面积(<@com.unitConversion unit=unit></@com.unitConversion>)</th>
                        <th>图斑数量</th>
                        <#--<th>详细</th>-->
                    </tr>
                    <#list info[1..] as item>
                        <tr>
                            <td style="text-align: center;">${item["LXMC"]!}</td>
                            <td>${item["AREA"]!?string("${decimal!'####.####'}")}</td>
                            <td>${item["COUNT"]!}</td>
                        </tr>
                    </#list>
                </table>
                <#if rItem.shape??>
                    <#assign shape = rItem.shape/>
                    <div style="text-align: center;padding-top: 20px;">
                        <#-- <a class="btn btn-primary" data-toggle="tooltip" data-placement="bottom"
                            style="margin-right: 12px;"
                            title="<div style='float:left;'><button class='btn btn-default' data-geo='${shape.shpId!}' onclick='exportFeature(this,0)'> shp</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' data-geo='${shape.shpId!}' onclick='exportFeature(this,1)'> dwg</button>&nbsp;&nbsp;&nbsp;<button class='btn btn-default' data-geo='${shape.coords!}' onclick='exportFeature(this,3)'>Txt</button></div>">导出符合规划图形</a>-->
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"
                                    aria-haspopup="true" aria-expanded="false">
                                导出符合规划图形 <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="javascript:void(0)" onclick="exportFeature(this,0)"
                                       data-geo="${shape.shpId!}">SHP</a></li>
                                <li><a href="javascript:void(0)" data-geo="${shape.shpId!}"
                                       onclick="exportFeature(this,1)">CAD</a></li>
                                <li><a href="javascript:void(0)" data-geo="${shape.coords!}"
                                       onclick="exportFeature(this,3)">TXT</a></li>
                            </ul>
                        </div>
                    </div>
                </#if>
                <#list info[1..] as parent>
                    <#assign detail=parent.detail>
                    <div class="detailPanel" id="detailPanel${key}${parent_index}">
                        <div style="width: 100%;margin-top: 30px; max-height: 580px;overflow-y: auto;padding-bottom: 20px;">
                            <#if (detail?size>0)>
                                <table>
                                <tr>
                                    <th>序号</th>
                                    <#list detail[0]?keys as key>
                                        <#if key?contains('OG_PRO_')>
                                        <#else >
                                            <th>${key}</th>
                                        </#if>
                                    </#list>
                                </tr>
                                <#list detail as child>
                                    <tr>
                                        <td>${child_index+1}</td>
                                        <#list child?keys as key>
                                            <#if key?contains('OG_PRO_')>
                                            <#else >
                                                <td><#if key?contains("标识码")&&child[key]?exists><#if child[key]?is_string>${child[key]!}<#else>${child[key]!?string('####')}</#if>
                                                    <#elseif key?contains("面积")&&child[key]??>${child[key]!?string('####.####')}
                                                    <#else>${child[key]!?string}</#if></td>
                                            </#if>
                                        </#list>
                                    </tr>
                                </#list>
                                </table><#else ><h5>无</h5></#if>
                        </div>
                    </div>
                </#list>
            </div>
            <div id="ghtab2${key}" class="tab-pane fade">
                <table style="margin-top: 20px;">
                    <tr>
                        <th>序号</th>
                        <th>类型名称</th>
                        <th>类型代码</th>
                        <th>地块面积</th>
                        <th>标识码</th>
                        <th>相交图形</th>
                        <th>面积</th>
                    </tr>
                    <#list processDatas as processData>
                        <tr style="text-align: center;">
                            <td>${processData_index+1}</td>
                            <td>${processData["DLMC"]!}</td>
                            <td>${processData["DLBM"]!}</td>
                            <td>${processData["OG_SHAPE_AREA"]!?string('####.####')}</td>
                            <td>${processData["BSM"]!}</td>
                            <td>
                                <a style="padding: 1px 12px;" class="btn btn-success btn-small"
                                   onclick=gotoLocation('${processData["processData"]!}','bp');>查看</a>
                            </td>
                            <td>${processData["SHAPE_AREA"]!?string('####.####')}</td>
                        </tr>
                    </#list>
                </table>
            </div>
        </div>
    <#else ><h5>该分类下无分析结果</h5>

    </#if>

</#macro>
<#macro tdghsc3 key="" unit=""  decimal="">
    <#if result[key]??>
        <#assign info=result["${key}"].info>
        <#assign rItem=result["${key}"]>
        <table style="margin-top: 20px;">
            <tr>
                <th>类别</th>
                <th>所占面积(<@com.unitConversion unit=unit></@com.unitConversion>)</th>
                <th>所占比例%</th>
            </tr>
            <#list info[1..] as item>
                <tr>
                    <td style="text-align: center;">${item["LXMC"]!}</td>
                    <td>${item["AREA"]!?string("${decimal!'####.####'}")}</td>
                    <td>${item["PER"]!?string("${decimal!'####.####'}")}</td>
                </tr>
            </#list>
        </table>
    <#else ><h5>该分类下无分析结果</h5>
    </#if>
</#macro>

<#macro unitConversion unit="">
    <#switch unit>
        <#case 'SQUARE'>m<sup>2</sup><#break>
        <#case 'ACRES'>亩<#break>
        <#case 'HECTARE'>公顷<#break>
        <#default>m<sup>2</sup>
    </#switch>
</#macro>
