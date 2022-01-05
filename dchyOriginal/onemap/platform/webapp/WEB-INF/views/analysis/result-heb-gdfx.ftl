<#assign cssContent>
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <style>
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

        h3, h5 {
            font-weight: normal;
            color: #188074;
            margin-left: 10px;
        }

        .wrapper {
            margin-top: 30px;
            width: 80%;
        }

    </style>
</#assign>
<@aBase.tpl showHeader="false" css=cssContent>
    <div class="container wrapper">
        <h3><span class="icon icon-columns"></span>&nbsp;
            供地分析结果展示</h3>
        </h3>
        <div class="pull-right" style="margin-top:-43px;"><h5>单位:平方米</h5></div>
    </div>

    <div id="basicContainer" class="container" style="width: 80%;">
        <div class="row" style="margin-bottom: 15px;">
            <div>
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#tab1" data-toggle="tab">供地汇总</a></li>
                    <li><a href="#tab2" data-toggle="tab">供地详情</a></li>
                </ul>
            </div>
            <div class="tab-content" style="margin-top: 5px;overflow-x: auto;">
                <div id="tab1" class="tab-pane fade in active">
                    <table>

                        <tr height=54 style='height:40.5pt'>
                            <th height=54 class=xl66 width=101 style='height:40.5pt;width:76pt'>类型</th>
                            <th class=xl67 width=153 style='width:115pt'>居住用地(R)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=117 style='width:88pt'>公共管理与公共服务<br> 设施用地(A)
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=108 style='width:81pt'>商业服务业设施用地(B)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=73 style='width:55pt'>工业用地（M)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=73 style='width:55pt'>物流仓储用地 (W)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=98 style='width:74pt'>道路与交通设施用地(S)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=86 style='width:65pt'>公用设施用地(U)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>绿地与广场用地(G)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=73 style='width:55pt'>建设用地(H)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=73 style='width:55pt'>非建设用地<br> (E)
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl65 width=106 style='width:80pt'>总计</th>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl65 style='height:13.5pt'>已供</td>
                            <td class=xl70>996369.464 </td>
                            <td class=xl70>487983.678 </td>
                            <td class=xl70>1723.175 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>109551.600 </td>
                            <td class=xl70>0.000 </td>
                            <td class=xl70>1303.140 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>1599220.997 </td>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl65 style='height:13.5pt'>未供</td>
                            <td class=xl70>538768.563 </td>
                            <td class=xl70>866745.085 </td>
                            <td class=xl70>160715.264 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>701449.925 </td>
                            <td class=xl70>10064.199 </td>
                            <td class=xl70>184912.784 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>2462655.819 </td>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl65 style='height:13.5pt'>合计</td>
                            <td class=xl70>1535138.027 </td>
                            <td class=xl70>863250.811 </td>
                            <td class=xl70>162438.439 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>811001.525 </td>
                            <td class=xl70>10064.199 </td>
                            <td class=xl70>186215.924 </td>
                            <td class=xl65>0</td>
                            <td class=xl65>0</td>
                            <td class=xl70>4061876.816 </td>
                        </tr>
                    </table>
                </div>
                <div id="tab2" class="tab-pane fade">
                    <table>

                        <tr height=18 style='height:13.5pt'>
                            <th rowspan=2 height=72 class=xl66 width=72 style='height:54.0pt;width:54pt'>类型<ruby><font
                                            class="font5"><rt class=font5></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=101 style='width:76pt'>居住用地（R）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=169 style='width:127pt'>一类居住用地（R1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=205 style='width:154pt'>二类居住用地（R2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=194 style='width:146pt'>三类居住用地（R3）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=120 style='width:90pt'>公共管理与公共服务设施用地（A）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=87 style='width:65pt'>行政办公<br> 用地
                                <br>
                                <span style='mso-spacerun:yes'>&nbsp;</span>(A1)<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=144 style='width:108pt'>文化设施用地 (A2)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=5 class=xl67 width=420 style='width:316pt'>教育科研用地 (A3)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=174 style='width:131pt'>体育用地 (A4)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=4 class=xl67 width=309 style='width:232pt'>医疗卫生用地 (A5)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>社会福利用地（A6）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>文物古迹用地（A7）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>外事用地（A8）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>宗教用地 （A9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=86 style='width:65pt'>商业服务业设施用地（B）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=4 class=xl67 width=309 style='width:232pt'>商业用地(B1)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=216 style='width:162pt'>商务用地(B2)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=174 style='width:131pt'>娱乐康体用地(B3)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=188 style='width:141pt'>公用设施营业网点用地(B4)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>其他服务设施用地（B9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>工业用地（M）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=216 style='width:162pt'>其中</th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>物流仓储用地 （W）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=216 style='width:162pt'>其中</th>
                            <th rowspan=2 class=xl67 width=80 style='width:60pt'>道路与交通设施用地（S）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=246 style='width:185pt'>其中</th>
                            <th colspan=2 class=xl67 width=156 style='width:117pt'>交通场站用地(S4)<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>其他交通设施用地（S9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>公用设施用地（U）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=6 class=xl67 width=444 style='width:333pt'>供应设施用地（U1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=144 style='width:108pt'>供应设施用地（U2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=156 style='width:117pt'>安全设施用地（U3）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>其他公用设施用地（U9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>绿地与广场用地（G）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=246 style='width:185pt'>其中<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>建设用地（H）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=4 class=xl67 width=288 style='width:216pt'>城乡居民点建设用地 （H1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=5 class=xl67 width=360 style='width:270pt'>区域交通设施用地 （H2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>区域公共设施用地（H3）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=2 class=xl67 width=144 style='width:108pt'>特殊用地（H4）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>采矿用地（H5）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>其他建设用地（H9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=81 style='width:61pt'>非建设用地（E）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th colspan=3 class=xl67 width=216 style='width:162pt'>水域（E1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>农林用地（E2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th rowspan=2 class=xl67 width=72 style='width:54pt'>其他非建设用地（E9）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                        </tr>
                        <tr height=54 style='height:40.5pt'>
                            <th height=54 class=xl67 width=72 style='height:40.5pt;width:54pt'>住宅用地（R11）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=97 style='width:73pt'>服务设施用地（R12）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=108 style='width:81pt'>住宅用地（R21）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=97 style='width:73pt'>服务设施用地（R22）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=97 style='width:73pt'>住宅用地（R31）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=97 style='width:73pt'>服务设施用地（R32）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>图书展览用地 （A21）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>文化活动用地（A22）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>高等院校用地（A31）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>中等专业学校用地（A32）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>中小学<br> 用地（A33）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>特殊教育用地（A34）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>科研用地 （A35）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>体育场馆用地 （A41）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>体育训练用地（A42）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=93 style='width:70pt'>医院用地<span style='mso-spacerun:yes'>&nbsp; </span>（A51）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>卫生防疫用地（A52）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>特殊医疗用地（A53）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>其他医疗卫生用地（A59）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=93 style='width:70pt'>零售商业用地（B11）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>批发市场用地（B12）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>餐饮用地（B13）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>旅馆用地（B14）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>金融保险用地<br> （B21）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>艺术传媒用地（B22）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>其他商务用地（B29）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>娱乐用地（B31）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>康体用地（B32）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=87 style='width:65pt'>加油加气站<br> 用地（B41）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=101 style='width:76pt'>其他公用设施营业网点用地（B49）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>一类工业用地（M1）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>二类工业用地（M2）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>三类工业用地（M3）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>一类物流仓储用地（W1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>二类物流仓储用地（W2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>三类物流仓储用地（W3）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>城市道路用地（S1）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>城市轨道交通用地（S2）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>交通枢纽用地（S3）<ruby><font class="font8"><rt
                                                class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>公共交通场站用地（S41）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=84 style='width:63pt'>社会停车场用地（S42）<ruby><font
                                            class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>供水用地<br> （U11）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=84 style='width:63pt'>供电用地<br> （U12）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>供燃气<br> 用地
                                <br> （U13）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>供热用地<br> （U14）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>通信用地<br> （U15）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>广播电视用地<br> （U16）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>排水用地<br> （U21）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>环卫用地<br> （U22）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=84 style='width:63pt'>消防用地<br> （U31）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>防洪用地<br> （U32）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=102 style='width:77pt'>公园绿地<br> （G1）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>防护绿地<br> （G2）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>广场用地<br> （G3）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>城市建设用地<br> （H11）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>镇建设用地<br> （H12）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>乡建设用地<br> （H13）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>村庄建设用地<br> （H14）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>铁路用地<br> （H21）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>公路用地 <br> （H22）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>港口用地<br> （H23）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>机场用地<br> （H24）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>管道运输用地<br> （H25）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>军事用地<br> （H41）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>安保用地<br> （H42）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>自然水域<br> （E11）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>水库<br> （E12）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                            <th class=xl67 width=72 style='width:54pt'>坑塘沟渠<br> （E13）
                                <ruby><font class="font8"><rt class=font8></rt></font></ruby></th>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl69 style='height:13.5pt'>已供</td>
                            <td class=xl71>996369.464 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl72 width=108 style='width:81pt'>996369.464 </td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=120 style='width:90pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>59260.081 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>2289.926 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>94871.833 </td>
                            <td class=xl72 width=102 style='width:77pt'>309352.229 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>24499.536 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=86 style='width:65pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>1547.513 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=102 style='width:77pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>175.662 </td>
                            <td class=xl67 width=101 style='width:76pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=80 style='width:60pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>104051.763 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>5499.852 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=84 style='width:63pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=84 style='width:63pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>1303.140 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=81 style='width:61pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl65 style='height:13.5pt'>未供<ruby><font class="font5"><rt
                                                class=font5></rt></font></ruby></td>
                            <td class=xl71>538768.563 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=97 style='width:73pt'>0</td>
                            <td class=xl72 width=108 style='width:81pt'>538768.563 </td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl72 width=120 style='width:90pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>3494.274 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>133012.279 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>428982.528 </td>
                            <td class=xl72 width=102 style='width:77pt'>240255.801 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>61000.202 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=86 style='width:65pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>37350.149 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>111224.085 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>12141.030 </td>
                            <td class=xl67 width=101 style='width:76pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=80 style='width:60pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>700949.892 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>500.032 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>3549.925 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>6514.274 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>184912.784 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=81 style='width:61pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                        </tr>
                        <tr height=18 style='height:13.5pt'>
                            <td height=18 class=xl65 style='height:13.5pt'>合计</td>
                            <td class=xl71>1535138.027 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=97 style='width:73pt'>0</td>
                            <td class=xl71>1535138.027 </td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl67 width=97 style='width:73pt'>0</td>
                            <td class=xl72 width=120 style='width:90pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>62754.355 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>135302.204 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>523854.361 </td>
                            <td class=xl72 width=102 style='width:77pt'>549608.030 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>85499.738 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=86 style='width:65pt'>0</td>
                            <td class=xl72 width=93 style='width:70pt'>38897.662 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>111224.085 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=87 style='width:65pt'>12316.692 </td>
                            <td class=xl67 width=101 style='width:76pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=80 style='width:60pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>805001.655 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>5999.884 </td>
                            <td class=xl72 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>3549.925 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=84 style='width:63pt'>6514.274 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl72 width=102 style='width:77pt'>186215.924 </td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=81 style='width:61pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                            <td class=xl67 width=72 style='width:54pt'>0</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div style="text-align: center;padding-top: 20px;">
            <a class="btn btn-primary" style="margin-right: 12px;" onclick="exportExcel();">导出excel</a>
        </div>
    </div>


    <script type="text/javascript">
        $(document).ready(function () {
            $('.disabled').attr('disabled', true);

            $("[data-toggle='tooltip']").tooltip({
                html: true,
                trigger: 'click'
            });
        });

        function exportExcel(type) {
            window.open("<@com.rootPath/>/static/template/叠加供地分析结果.xlsx");
        }
    </script>
</@aBase.tpl>
