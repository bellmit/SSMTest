{
	"coordinateVisible":true,
	"createAt":"2016-10-13 14:43:23",
	"defaultRegionCode":"320000",
	"defaultYear":"current",
	"description":"render",
	"dockWidgets":[
		{
			"desc":"分类数据",
			"display":true,
			"icon":"glyphicon glyphicon-tasks",
			"id":"DataList",
			"label":"数据",
			"open":false,
			"url":"DataList",
			"weight":0
		},
		{
			"config":{
				"autoCreateProject":false,
				"popupStyle":"layer",
				"style":"list",
				"supervise":false
			},
			"desc":"视频管理",
			"display":true,
			"icon":"glyphicon glyphicon-facetime-video",
			"id":"VideoManager",
			"label":"视频",
			"open":false,
			"url":"VideoManager",
			"weight":0
		}
	],
	"geometryService":{
		"url":"http://tasks.arcgisonline.com/ArcGIS/rest/services/Geometry/GeometryServer"
	},
	"logo":"/omp/static/img/new/logo.jpg",
	"logoVisible":true,
	"map":{
		"baseLayers":[],
		"defaultScale":0,
		"operationalLayers":[
			{
				"alias":"报批",
				"alpha":1,
				"category":"test",
				"group":"01560091b7c44028daac55fbb0450012",
				"id":"0157bd11a47d4028dafb57bd082a0002",
				"index":1,
				"name":"BPDK_320412",
				"type":"dynamic",
				"url":"http://192.168.90.123:8089/oms/arcgisrest/test/BPDK_320412/MapServer",
				"visible":true,
				"xMaxExtent":4.05145492527E7,
				"xMinExtent":4.04932352425E7,
				"xzdm":"320000",
				"yMaxExtent":3519579.3617,
				"yMinExtent":3505161.7265,
				"year":2014
			},
			{
				"alias":"宗地",
				"alpha":1,
				"category":"test",
				"group":"01560091b7c44028daac55fbb0450012",
				"id":"0157bd12065d4028dafb57bd082a0033",
				"index":2,
				"name":"ZD_320412",
				"type":"dynamic",
				"url":"http://192.168.90.123:8089/oms/arcgisrest/test/ZD_320412/MapServer",
				"visible":false,
				"xMaxExtent":4.05189393237E7,
				"xMinExtent":4.04998104219E7,
				"xzdm":"320000",
				"yMaxExtent":3520802.7836,
				"yMinExtent":3503210.1969,
				"year":2014
			}
		]
	},
	"name":"render",
	"title":"江苏省国土资源 一张图",
	"widgetContainer":{
		"widgets":[
			{
				"config":{
					"hideInvisibleLayer":true
				},
				"desc":"图层控制，控制图层通透度及展示图层图例",
				"display":false,
				"icon":"/omp/img/menu/ruler.png",
				"id":"LayerList",
				"label":"图层控制",
				"open":false,
				"url":"LayerList",
				"weight":0
			},
			{
				"config":{
                           "layers": [
                             {
                               "serviceId":"0157bd12065d4028dafb57bd082a0033",
                               "returnFields": [
                                 {
                                   "alias": "土地主要用途",
                                   "name": "TDYT",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "标识码",
                                   "name": "BSM",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "地籍号",
                                   "name": "DJH",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "宗地四至",
                                   "name": "ZDSZ",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "权属性质",
                                   "name": "QSXZ",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "权属单位代码",
                                   "name": "QSDWDM",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "座落单位代码",
                                   "name": "ZLDWDM",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "土地使用权类型",
                                   "name": "TDSYQLX",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "分区行政区代码",
                                   "name": "FQXZQDM",
                                   "type": "unique"
                                 },
                                 {
                                   "alias": "实测面积",
                                   "name": "SCMJ",
                                   "type": "class"
                                 },
                                 {
                                   "alias": "发证面积",
                                   "name": "FZMJ",
                                   "type": "class"
                                 }
                               ]
                             }
                           ],
                           "rendererTypes": [
                             {
                               "alias": "唯一值渲染",
                               "type": "unique"
                             },
                             {
                               "alias": "分级渲染",
                               "type": "class"
                             }
                           ]
                         },
				"desc":"",
				"display":false,
				"icon":"/omp/img/menu/ruler.png",
				"id":"17101A7E-844D-6288-444F-23468D9DAAAA",
				"label":"动态渲染",
				"open":false,
				"url":"Renderer",
				"weight":7
			}
		],
		"widgetsGroup":[]
	},
	"widgets":[
		{
			"config":{
				"style":"normal"
			},
			"display":true,
			"id":"MenuBar",
			"open":false,
			"url":"MenuBar",
			"weight":0
		},
		{
			"display":true,
			"id":"Navigation",
			"open":false,
			"url":"Navigation",
			"weight":0
		},
		{
			"config":{
				"style":"default"
			},
			"display":true,
			"open":true,
			"url":"Scalebar",
			"weight":0
		},
		{
			"display":true,
			"id":"Region",
			"open":false,
			"url":"Region",
			"weight":0
		},
		{
			"config":{
				"layers":[]
			},
			"display":false,
			"icon":"fa-location",
			"id":"Location",
			"label":"定位",
			"open":false,
			"url":"Location",
			"weight":0
		},
		{
			"display":false,
			"icon":"/omp/img/menu/location.png",
			"id":"layerDefinition",
			"label":"图层过滤",
			"open":false,
			"url":"LayerDefinition",
			"weight":0
		},
		{
			"config":{
				"lods":[
					{
						"resolution":0.04283029810494505,
						"scale":3000000
					},
					{
						"resolution":0.01099866274749598,
						"scale":1155583.42
					},
					{
						"resolution":0.004124498458927162,
						"scale":288895.85
					}
				],
				"maxExtent":{
					"xmax":124.20372337470876,
					"xmin":114.23854068229154,
					"ymax":35.58510134945418,
					"ymin":31.33062507102963
				}
			},
			"display":false,
			"icon":"/omp/img/menu/location.png",
			"id":"Overview",
			"label":"鹰眼",
			"open":false,
			"url":"Overview",
			"weight":0
		}
	]
}