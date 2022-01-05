### 数据中心配置文件特殊配置说明 ###

- 模板文件配置项目
 >视频功能配置项

       "txVersionEnable":false, //泰兴定制功能开关
       "txInspectPostUrl":"http://www.baidu.com", //泰兴执法监察推送接口配置
       "showVideoByCluster":false, //是否通过聚合方式展示监控点
       "showArcCenter":false, //姜堰是否展示可视域中心线
       "style": "list"/"group",//监控点列表分组展示
       "popupStyle": "layer",//地图弹出框是layer/infowindow
       "autoCreateProject":false, //泰兴自动创建项目
       "searchVideoByXy":false,//泰兴通过输入x,y坐标检索监控点功能
       "exportXls":false, //泰兴导出项目
       "showStatus":false, //显示离线状态
       "preWarningUrl":"http://183.207.215.110:8080",//移动预警功能接口地址
       "warnDays":50,//默认预警时间
       "txtSymbol": {
                 "color": "#32bfef",
                 "fontSize": "12"
               }, //监控点名称字体颜色
       "proType": [
                 {
                   "name": "建设用地",
                   "color": "#ffb61e"
                 },
                 {
                   "name": "农用地",
                   "color": "#00bc12"``
                 },
                 {
                   "name": "未利用地",
                   "color": "#ff2d51"
                 },
                 {
                   "name": "其他",
                   "color": "#000000"
                 }
               ],   //项目类型及其对应项目点颜色
          "identifyLayers": [
                  {
                    "layerName": "SDE.BPDK_320000",
                    "serviceId": "0153694f72504028daac5368cd440003",
                    "url": "http://192.168.90.44:8088/oms/arcgisrest/%E4%B8%9A%E5%8A%A1%E6%95%B0%E6%8D%AE/BPDK_320000/MapServer",
                    "returnFields": [
                      {
                        "alias": "地块名称",
                        "name": "DKNAME",
                        "type": "STRING"
                      },
                      {
                        "alias": "项目名称",
                        "name": "PRONAME",
                        "type": "STRING"
                      }
                    ]
                  }
                ]        //江阴属性识别

>信息查询配置项

            "filterAfterQuery":false,//查询后地图只显示结果地块
            "locateOpacity":0.5,//定位时图形透明度，取值： 0-1
            "popUpStyle":"infowindow", 地图信息弹窗模式
             对于每个图层可单独增加配置用于不动产状态查询和超链接
                 "bdcQueryField":"DJH",
                 "bdcUrl":"http://www.baidu.com?xxx=${DJH}",

>属性识别配置项
            
            "locateOpacity":1,定位后图形高亮透明度
            "popUpStyle":"infowindow",地图信息弹窗模式
            "leasPostUrl":"", 识别结果推送至执法监察地址
            对于每个图层可单独增加配置用于不动产状态查询和超链接
                 "bdcQueryField":"DJH",
                 "bdcUrl":"http://www.baidu.com?xxx=${DJH}",

>地图定位配置项

            "locateOpacity":0.5,//定位时图形透明度，取值： 0-1
>综合分析配置
            
            "exportDiff":true,//可导出不重叠图形  仅支持国有建设用地分析和报批分析
        
>常州出图
            
             {
                        "config":{
                          "author":"作者",
                          "copyright":"版权",
                          "customTextItems":[{
                            "key":"1",
                            "name":"1",
                            "value":"1"
                          }],
                          "title":"打印标题",
                          "url":"http://192.168.90.7:6080/arcgis/rest/services/ExportWebMap/GPServer/%E5%AF%BC%E5%87%BA%20Web%20%E5%9C%B0%E5%9B%BE"
                        },
                        "desc":"",
                        "display":false,
                        "group":"1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
                        "icon":"/omp/img/menu/assessment.png",
                        "id":"6A0C7B00-AE5D-5315-DB36-EB5E3C860901",
                        "label":"常州出图",
                        "open":false,
                        "url":"TP/cz/ExportMap",
                        "weight":2
                      }

- 系统配置 *application.properties*
>omp业务数据库链接配置

     db.driver=oracle.jdbc.driver.OracleDriver
     db.url=jdbc:oracle:thin:@10.1.1.101:1521:gtmap
     db.username=OMP
     db.password=OMP

>默认年份

     default.year=current

>适应版本 provincial（省级） municipal（市县级）

     main.level=provincial

> 版本主标题

     local.title=江苏省

>版本相应资源路径

    local.path=jiangsu

>filestore存储路径(可选)

    file.store.location = D:\\filestore

>开启全文检索功能

    search.update=false
>office版本 old/new

    office.plugin.version=new

>视频监控连接配置

    video.platform=hw  //hw/hk
    video.userName=admin
    video.userPwd=qwer1234
    video.server=192.168.90.7
    video.port=7302

>服务地址（全景，预设位拍照...）

    ptz.server=http://192.168.90.46:8081
>ptz参数的p的偏移量，为数字

    ptz.p.offset=20
>ptz参数的t的偏移量，为数字

    ptz.t.offset=0

>发送巡查记录开关

    send.inspect.record=false
>新增项目时关联设备的默认预设位

    project.default.preset=true

>分析配置
>analysis hub

    analysis.useHub=false
>使用cs的分析工具进行土地利用现状分析

    analysis.useWcf=false
    wcfUrl=http://192.168.1.125:81/GtMapService/GPAreaAnalyseService/AreaAnalyseWeb/?wsdl
    wcfMethod=DoAnalyseJson

>GP
>导出cad的GP服务地址

    dwg.exp.url=http://192.168.90.7:6080/arcgis/rest/services/ShpExportToCAD/GPServer/Shp%20Convert%20To%20CAD
>解析dwg的GP服务地址

    dwg.imp.url=http://192.168.90.7:6080/arcgis/rest/services/GP/CAD2PoylgonFeatureSets1/GPServer/CAD%20File%20Transform%20to%20Poylgon%20FeatureSets%20Python%20Script

>空间分析实现方式
>ARC_SDE
>ORACLE_SPATIAL

    spatial.engine=ARC_SDE

>sde数据连接

    sde.db.dsname=sde
    sde.db.server=192.168.90.7
    sde.db.instance=5151
    sde.db.username=sde
    sde.db.password=sde

- 行政区列表配置 *organ.json*

> 一个包含行政区信息的json数组

          [
            {
              "organName": "相城区局",
              "regionCode": "320507"
            },
            {
              "organName": "姑苏区局",
              "regionCode": "320508"
            },
            {
              "organName": "渭塘支局",
              "regionCode": "320507105"
            }
          ]


## 数据中心配置文件注解 ##

- 分析配置 *analysis.json*
>配置分析中的字段中英文映射以及其他保留的分析图层相关配置

>修改完需要重启tomcat

>描述：

>analysis-fields-mapping 配置分析结果展示页面的字段映射 左边为字段名 多个用|隔开 右边为 映射的中文名称


----------
- 全文检索配置 *search.json*
>配置哪些图层可以用于全文检索，具体图层配置均置于layers数组中，

>修改完注意将search.update改为true，重启建立索引

>描述：
    
    layer：空间图层名称；
    fields：图层字段；
    id：ID，用于后续link主键值；
    title：图形界面展示每个图斑主标题；
    subtitle：图形界面展示下方子标题；
    content：图形界面展示其余信息；
    group：图形界面展示分组信息；
    link：图形界面展示超链接地址；
    dataSource：图层位于数据源，默认数据源中可为null；

----------
- ---- 地理投影对照表 -- *regions.json*
>空间库中源数据图层为地理坐标系时使用该对照表进行平面投影纠正

>描述：
    
    simplifyTolerance：图形简化精度 经纬度单位
    regionLayers：图层与分区字段或者空间参考对应关系键值对。图层名称可以用正则表达式描述，也可用完整的图层名描述。
                  若图层存在行政区分组字段，则该图层对应值配置为分组字段名称；如不存在分组字段则配置对应的空间参考，
                  空间参考可为EPSG描述也可为wkt的描述信息。
    regionMap：不同行政区对应原始图层的空间参考，其值格式可为srid，EPSG+srid，或者完整wkt的空间参考描述。


----------
- --- 界址点导入 -- *bmark.json*
>界址点导入业务类型与具体图层关联关系描述，json对象，key为业务类型，值则为对应图层描述

>描述

    layerName：空间图层名称
    idField：图层主键
    dataSource：图层位于的数据源，于默认数据源中可为null

----------


- -- 统计图表 -- *statistic.json*
>配置统计图表页面的左侧树形菜单项

>描述
    
    id:节点id
    name:节点名称
    url:节点的链接地址
	parentId：父节点的id，根节点为空
	children：子节点集合，叶子节点为空

----------


- ---移动端监控信息--- *monitor.json*
>配置并记录移动端的外业人员的在线情况，便于实时监控
>描述：

    id：用户的id
    name：姓名
    location:当前位置，geojson格式
    status:当前状态，*0--离线*; *1--在线*
    desc：外业任务信息以及其他需要展示的信息
    updateTime:更新时间
    phoneNumber：手机号

- ---视频监控点配置信息---- *video.cfg*
>配置用于视频监控的监控点信息(包含属性以及位置等信息)
>修改完刷新页面生效 无需重启
>描述:
   
    unitName:控制中心名称
    regions:区域
    > id:监控区域id
    > name：监控区域名称
    > enabled:是否启用(0/1,default=1)
    > devices:区域下的监控设备
      >> id:设备的id
      >> name:设备的名称
      >> indexCode:设备编码(海康需要依据此字段)
      >> x:x坐标
      >> y:y坐标
      >> enabled:是否启用(0/1,default=1)





