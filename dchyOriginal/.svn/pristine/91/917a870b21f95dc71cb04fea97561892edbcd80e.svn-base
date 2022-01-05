<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
        >
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Author>gtmap</Author>
  <LastAuthor>user</LastAuthor>
  <Created>2019-09-10T03:18:55Z</Created>
  <LastSaved>2019-09-11T07:53:47Z</LastSaved>
  <Version>16.00</Version>
 </DocumentProperties>
 <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
  <AllowPNG/>
 </OfficeDocumentSettings>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>7500</WindowHeight>
  <WindowWidth>20490</WindowWidth>
  <WindowTopX>32767</WindowTopX>
  <WindowTopY>32767</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Bottom"/>
   <Borders/>
   <Font ss:FontName="Arial" x:Family="Swiss"/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="m288031220">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="宋体" x:CharSet="134"/>
  </Style>
  <Style ss:ID="m288031280">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="宋体" x:CharSet="134"/>
  </Style>
  <Style ss:ID="s62">
   <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
   <Font ss:FontName="黑体" x:CharSet="134" x:Family="Modern" ss:Size="11"/>
   <Interior ss:Color="#C0C0C0" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s63">
   <Alignment ss:Horizontal="Left" ss:Vertical="Center"/>
  </Style>
  <Style ss:ID="s64">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="黑体" x:CharSet="134" x:Family="Modern" ss:Size="11"/>
   <Interior ss:Color="#C0C0C0" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s65">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="宋体" x:CharSet="134"/>
  </Style>
  <Style ss:ID="s69">
   <Alignment ss:Horizontal="Center" ss:Vertical="Center"/>
   <Borders>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Font ss:FontName="宋体" x:CharSet="134"/>
  </Style>
 </Styles>
 <Worksheet ss:Name="监控点日志信息">
  <Table ss:ExpandedColumnCount="6" ss:ExpandedRowCount="${list?size+12}" x:FullColumns="1"
   x:FullRows="1">
   <Column ss:AutoFitWidth="0" ss:Width="131.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="65.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="131.25" ss:Span="1"/>
   <Column ss:Index="5" ss:AutoFitWidth="0" ss:Width="65.25"/>
   <Column ss:AutoFitWidth="0" ss:Width="243"/>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:StyleID="s62"><Data ss:Type="String">部门</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">用户名</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">摄像头名称</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">设备id</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">操作内容</Data></Cell>
    <Cell ss:StyleID="s62"><Data ss:Type="String">操作时间</Data></Cell>
   </Row>
  <#if list?? && (list?size > 0)>
   <#list list as object>
       <Row ss:StyleID="s63">
           <Cell><Data ss:Type="String">${object["userDept"]!}</Data></Cell>
           <Cell><Data ss:Type="String">${object["userName"]!}</Data></Cell>
           <Cell><Data ss:Type="String">${object["cameraName"]!}</Data></Cell>
           <Cell><Data ss:Type="String">${object["cameraId"]!}</Data></Cell>
           <Cell><Data ss:Type="String">${object["optContent"]!}</Data></Cell>
           <Cell><Data ss:Type="String">${(object["createAt"]?string("yyyy-MM-dd hh:mm:ss"))!}</Data></Cell>
       </Row>
   </#list>
  </#if>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <Print>
    <ValidPrinterInfo/>
    <HorizontalResolution>300</HorizontalResolution>
    <VerticalResolution>300</VerticalResolution>
   </Print>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>1</ActiveRow>
     <ActiveCol>5</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
 <Worksheet ss:Name="监控日志汇总信息">
  <Table ss:ExpandedColumnCount="3" ss:ExpandedRowCount="${list?size+200}" x:FullColumns="1"
   x:FullRows="1">
   <Column ss:AutoFitWidth="0" ss:Width="87.75"/>
   <Column ss:AutoFitWidth="0" ss:Width="186"/>
   <Column ss:AutoFitWidth="0" ss:Width="119.25"/>
   <Row ss:AutoFitHeight="0" ss:Height="33">
    <Cell ss:StyleID="s64"><Data ss:Type="String">市局&#10;（分局）&#10;</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">处室（科室）</Data></Cell>
    <Cell ss:StyleID="s64"><Data ss:Type="String">登陆总次数</Data></Cell>
   </Row>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 0>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:MergeDown="43" ss:StyleID="s69"><Data ss:Type="String">市局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="String">法规处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_1"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 1>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">国土空间规划处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_2"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 2>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">人事处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_3"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 3>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">财务审计处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_4"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 4>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">自然资源执法监督处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_5"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 5>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">监察室</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_6"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 6>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">其他班子成员</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_7"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 7>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">地质和矿产资源处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_8"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 8>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">储备中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_9"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 9>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">车队</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_10"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 10>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">不动产登记部门</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_11"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 11>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">万源估价所</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_12"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 12>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">测绘信息管理处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_13"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 13>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">系统管理</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_14"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 14>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">自然资源开发利用处（行政许可处）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_15"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 15>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">派驻纪检组</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_16"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 16>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">测试</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_17"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 17>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">不动产登记中心（权籍调查科）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_18"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 18>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">林业站</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_19"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 19>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">规划编制研究中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_20"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 20>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">创新中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_21"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 21>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">党建和效能督查处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_22"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 22>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">林业管理处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_23"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 23>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">自然资源调查监测和科技处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_24"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 24>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">自然资源确权登记处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_25"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 25>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">建设项目规划技术审查处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_26"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 26>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">泰州市规划院</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_27"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 27>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">自然资源所有者权益处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_28"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 28>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">督导组</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_29"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 29>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">智慧规划</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_30"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 30>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">不动产登记中心（登记审核科）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_31"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 31>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">其他处级干部</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_32"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 32>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">国土空间批后监管处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_33"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 33>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">规划展示馆</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_34"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 34>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">泰州市测绘院</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_35"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 35>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">办公室</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_36"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 36>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">国土空间用途管制处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_37"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 37>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">信息中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_38"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 38>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">办公室（信访办公室）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_39"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 39>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">耕地保护和生态修复处</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_40"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 40>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">局长室</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_41"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 41>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">测绘调查科</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_42"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 42>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">登记审核科</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_43"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_0?? && (list_organ_0?size > 0)>
 <#list list_organ_0 as object>
  <#if object_index == 43>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">综合科</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_44"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 0>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:MergeDown="11" ss:StyleID="m288031220"><Data ss:Type="String">海陵分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="String">海陵分中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_1"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 1>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">海陵区分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_2"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 2>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">局长室（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_3"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 3>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">分管局长(海陵)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_4"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 4>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">综合科（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_5"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 5>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">办公室（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_6"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 6>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">用地科（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_7"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 7>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">执法监察科（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_8"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 8>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">地籍地矿科（海陵）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_9"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 9>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第一国土资源中心所(海陵)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_10"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 10>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第二国土资源中心所(海陵)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_11"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_1?? && (list_organ_1?size > 0)>
 <#list list_organ_1 as object>
  <#if object_index == 11>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第三国土资源中心所(海陵)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_12"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 0>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:MergeDown="14" ss:StyleID="s69"><Data ss:Type="String">高港分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="String">高港分中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_1"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 1>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">高港区分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_2"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 2>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">局长室（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_3"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 3>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">分管局长（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_4"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 4>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">综合科（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_5"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 5>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">规划科（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_6"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 6>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">用地科（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_7"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 7>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">地籍地矿科(高港)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_8"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 8>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">执法监察科（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_9"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 9>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">信访室（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_10"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 10>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">行政服务中心（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_11"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 11>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第四国土资源中心所（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_12"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 12>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第五国土资源中心所（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_13"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 13>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第六国土资源中心所（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_14"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_2?? && (list_organ_2?size > 0)>
 <#list list_organ_2 as object>
  <#if object_index == 14>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">第七国土资源中心所（高港）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_15"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 0>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:MergeDown="13" ss:StyleID="s69"><Data ss:Type="String">医药高新区</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="String">医药高新区分中心</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_1"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 1>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">医药高新区分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_2"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 2>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">农业开发区分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_3"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 3>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">医药园区</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_4"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 4>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">局长室（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_5"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 5>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">办公室（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_6"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 6>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">用地科(医药高新区)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_7"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 7>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">地籍地矿科(医药高新区)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_8"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 8>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">执法监察科(农业开发区)</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_9"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 9>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">利用监察科（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_10"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 10>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">规划耕保科（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_11"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 11>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">野徐国土所（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_12"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 12>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">塘湾国土所（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_13"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_3?? && (list_organ_3?size > 0)>
 <#list list_organ_3 as object>
  <#if object_index == 13>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">寺巷国土所（医药高新区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_14"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 0>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:MergeDown="15" ss:StyleID="m288031280"><Data ss:Type="String">姜堰区</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="String">姜堰区分局</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_1"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 1>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">分管局长（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_2"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 2>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">办公室（姜堰）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_3"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 3>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">规划科（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_4"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 4>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">地籍科（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_5"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 5>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">耕保科（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_6"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 6>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">矿产管理办公室（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_7"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 7>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">复垦中心（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_8"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 8>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">勘测院（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_9"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 9>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">白米中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_10"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 10>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">开发区中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_11"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 11>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">淤溪中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_12"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 12>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">溱潼中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_13"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 13>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">姜堰中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_14"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 14>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">娄庄中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_15"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
<#if list_organ_4?? && (list_organ_4?size > 0)>
 <#list list_organ_4 as object>
  <#if object_index == 15>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625">
    <Cell ss:Index="2" ss:StyleID="s65"><Data ss:Type="String">张甸中心所（姜堰区）</Data></Cell>
    <Cell ss:StyleID="s65"><Data ss:Type="Number">${object["organ_16"]!}</Data></Cell>
   </Row>
  </#if>
 </#list>
</#if>
   <Row ss:AutoFitHeight="0" ss:Height="20.0625" ss:Span="12"/>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <PageSetup>
    <Header x:Margin="0.3"/>
    <Footer x:Margin="0.3"/>
    <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
   </PageSetup>
   <Print>
    <ValidPrinterInfo/>
    <PaperSizeIndex>9</PaperSizeIndex>
    <HorizontalResolution>180</HorizontalResolution>
    <VerticalResolution>180</VerticalResolution>
   </Print>
   <TopRowVisible>85</TopRowVisible>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>101</ActiveRow>
     <ActiveCol>2</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>
