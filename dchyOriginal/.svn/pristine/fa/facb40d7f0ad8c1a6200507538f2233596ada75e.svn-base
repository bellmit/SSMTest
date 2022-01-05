{
  "coordinateVisible": true,
  "createAt": "2020-03-03 16:15:12",
  "defaultRegionCode": "320000",
  "description": "",
  "dockWidgets": [
    {
      "collapsible": false,
      "config": {
        "expandLevel": 1,
        "groupByYear": false
      },
      "display": true,
      "icon": "glyphicon glyphicon-tasks",
      "id": "DataList",
      "label": "数据",
      "open": false,
      "url": "DataList",
      "weight": 0
    },
    {
      "collapsible": false,
      "config": {
        "autoCreateProject": false,
        "exportXls": false,
        "identifyLayers": [],
        "modifyProVideo": false,
        "newProjectLayer": [],
        "panoramaSwitch": "off",
        "popupStyle": "layer",
        "proType": [
          {
            "color": "#ffb61e",
            "name": "建设用地"
          },
          {
            "color": "#00bc12",
            "name": "农用地"
          },
          {
            "color": "#ff2d51",
            "name": "未利用地"
          },
          {
            "color": "#000000",
            "name": "其他"
          }
        ],
        "searchVideoByXy": false,
        "showArcCenter": false,
        "showStatus": false,
        "showVideoByCluster": false,
        "style": "list",
        "supervise": false,
        "txVersionEnable": false,
        "txtSymbol": {
          "color": "#32bfef",
          "fontSize": "12"
        }
      },
      "display": true,
      "icon": "glyphicon glyphicon-facetime-video",
      "id": "VideoManager",
      "label": "视频",
      "open": false,
      "url": "VideoManager",
      "weight": 0
    }
  ],
  "geometryService": {
    "url": "http://tasks.arcgisonline.com/ArcGIS/rest/services/Geometry/GeometryServer"
  },
  "logo": "/omp/static/img/new/logo.jpg",
  "map": {
    "baseLayers": [],
    "defaultScale": 0,
    "operationalLayers": [
      {
        "alias": "3到5年拟用房地产开发地块",
        "alpha": 1,
        "category": "业务图层",
        "group": "0165f5b9785ee4e4e48965f5a70b0027",
        "id": "0170a457e1a14028da9f70a4570c0003",
        "index": 1,
        "name": "NYFDCKFDK3_5",
        "type": "dynamic",
        "url": "http://127.0.0.1:6070/oms/arcgisrest/业务图层/NYFDCKFDK3_5/MapServer",
        "visible": true,
        "xMaxExtent": 40495315.6986,
        "xMinExtent": 40494228.2589,
        "xzdm": "320000",
        "yMaxExtent": 3586065.8688,
        "yMinExtent": 3585192.7421,
        "year": 2020
      }
    ]
  },
  "name": "泰兴",
  "title": "江苏省国土资源 一张图",
  "widgetContainer": {
    "widgets": [
      {
        "collapsible": false,
        "config": {
          "hideInvisibleLayer": true
        },
        "display": false,
        "icon": "fa-bars",
        "id": "LayerList",
        "label": "图层控制",
        "open": false,
        "url": "LayerList",
        "weight": 0
      },
      {
        "collapsible": false,
        "config": {
          "dataSource": "sde",
          "dltb": "",
          "scopeLayers": [],
          "xzdw": "",
          "year": []
        },
        "display": false,
        "icon": "fa-bar-chart",
        "id": "1F93B56A-CDB5-4467-84F3-AAB547F707C0",
        "label": "土地利用现状分析",
        "open": false,
        "url": "XzAnalysis",
        "weight": 0
      },
      {
        "collapsible": false,
        "config": {
          "layers": [
            {
              "html": "",
              "layerIndex": "0",
              "layerName": "3到5年拟用房地产开发地块",
              "layerUrl": "http://127.0.0.1:6070/oms/arcgisrest/业务图层/NYFDCKFDK3_5/MapServer/0",
              "link": {
                "tip": "",
                "url": ""
              },
              "queryFields": {
                "fields": [
                  {
                    "alias": "OBJECTID",
                    "name": "OBJECTID",
                    "type": "OID"
                  },
                  {
                    "alias": "序号",
                    "name": "XH",
                    "type": "STRING"
                  },
                  {
                    "alias": "责任主体",
                    "name": "ZRZT",
                    "type": "STRING"
                  },
                  {
                    "alias": "地块名称",
                    "name": "DKMC",
                    "type": "STRING"
                  }
                ],
                "prefix": "%"
              },
              "queryPaging": true,
              "returnFields": [
                {
                  "alias": "OBJECTID",
                  "name": "OBJECTID",
                  "type": "OID"
                },
                {
                  "alias": "序号",
                  "name": "XH",
                  "type": "STRING"
                },
                {
                  "alias": "责任主体",
                  "name": "ZRZT",
                  "type": "STRING"
                },
                {
                  "alias": "地块名称",
                  "name": "DKMC",
                  "type": "STRING"
                },
                {
                  "alias": "四至范围",
                  "name": "SZFW",
                  "type": "STRING"
                }
              ],
              "serviceId": "0170a457e1a14028da9f70a4570c0003",
              "titleField": {
                "alias": "OBJECTID",
                "name": "OBJECTID"
              },
              "type": "field"
            }
          ],
          "merge": false
        },
        "display": true,
        "icon": "fa-search",
        "id": "82CFB17C-7687-F900-0555-FC470B2BF307",
        "label": "信息查询",
        "open": false,
        "url": "Query",
        "weight": 4
      },
      {
        "collapsible": false,
        "config": {
          "layers": []
        },
        "display": false,
        "icon": "fa-info",
        "id": "17101A7E-844D-6288-444F-23468D9D51E1",
        "label": "属性识别",
        "open": false,
        "url": "Identify",
        "weight": 6
      },
      {
        "collapsible": false,
        "config": {
          "layers": [
            {
              "dataSource": "sde",
              "drawMethod": "polygon",
              "enableVideo": false,
              "html": "",
              "layerAlias": "3到5年拟用房地产开发地块",
              "layerIndex": "1",
              "layerName": "NYFDCKFDK3_5",
              "layerUrl": "http://127.0.0.1:6070/oms/arcgisrest/业务图层/NYFDCKFDK3_5/MapServer/0",
              "id":"0170a457e1a14028da9f70a4570c0003",
              "titleField": {
                "alias": "序号",
                "name": "XH",
                "type": "STRING"
              },
              "returnFields": [
                {
                  "alias": "序号",
                  "name": "XH",
                  "type": "STRING"
                },
                {
                  "alias": "责任主体",
                  "name": "ZRZT",
                  "type": "STRING"
                },
                {
                  "alias": "地块名称",
                  "name": "DKMC",
                  "type": "STRING"
                },
                {
                  "alias": "四至范围",
                  "name": "SZFW",
                  "type": "STRING"
                },
                {
                  "alias": "面积",
                  "name": "MJ",
                  "type": "STRING"
                },
                {
                  "alias": "目前土规",
                  "name": "MQTG",
                  "type": "STRING"
                },
                {
                  "alias": "目前控规",
                  "name": "MQKG",
                  "type": "STRING"
                },
                {
                  "alias": "拟调整规划",
                  "name": "NTZGH",
                  "type": "STRING"
                },
                {
                  "alias": "备注",
                  "name": "BZ",
                  "type": "STRING"
                }
                ,
                                {
                                  "alias": "测试",
                                  "name": "TEST",
                                  "type": "STRING"
                                }
              ],
              "type": "field"
            }
          ]
        },
        "display": false,
        "icon": "fa-info",
        "id": "8B706279-2357-4141-B41E-11ED52816DC9",
        "label": "数据录入",
        "open": false,
        "url": "TP/tz/InsertData",
        "weight": 7
      }
    ],
    "widgetsGroup": [
      {
        "icon": "fa-bar-chart",
        "id": "Analysis",
        "label": "统计分析",
        "type": 5,
        "url": "Analysis",
        "weight": 0,
        "widgets": []
      },
      {
        "icon": "fa-wrench",
        "id": "Tool",
        "label": "常用工具",
        "type": 6,
        "url": "Tool",
        "weight": 0,
        "widgets": [
          {
            "collapsible": false,
            "config": {
              "areaunits": [
                {
                  "abbr": "平方米",
                  "conversion": "1",
                  "precision": "3"
                },
                {
                  "abbr": "平方千米",
                  "conversion": "0.000001",
                  "precision": "3"
                },
                {
                  "abbr": "亩",
                  "conversion": "0.0015",
                  "precision": "3"
                },
                {
                  "abbr": "公顷",
                  "conversion": "0.0001",
                  "precision": "3"
                }
              ],
              "defaultWkid": "2364",
              "distanceunits": [
                {
                  "abbr": "米",
                  "conversion": "1",
                  "precision": "3"
                },
                {
                  "abbr": "千米",
                  "conversion": "0.001",
                  "precision": "3"
                }
              ],
              "showAdvance": false,
              "spatialrefs": [
                {
                  "name": "Xian_1980_3_Degree_GK_Zone_40",
                  "wkid": "2364"
                },
                {
                  "name": "Xian_1980_3_Degree_GK_CM_120E",
                  "wkid": "2385"
                }
              ],
              "textsymbol": {
                "color": "0xe76049",
                "font": "微软雅黑",
                "size": "15"
              }
            },
            "display": false,
            "group": "Tool",
            "icon": "",
            "id": "MapMeasure",
            "label": "地图量算",
            "open": false,
            "url": "Measure",
            "weight": 3
          },
          {
            "collapsible": false,
            "config": {
              "author": "作者",
              "copyright": "版权",
              "customTextItems": [],
              "title": "打印标题",
              "url": "http://192.168.1.125:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task"
            },
            "display": false,
            "group": "Tool",
            "icon": "/omp/img/menu/print.png",
            "id": "Print",
            "label": "打印",
            "open": false,
            "url": "Print",
            "weight": 0
          },
          {
            "collapsible": false,
            "display": false,
            "group": "Tool",
            "icon": "/omp/img/menu/mark.png",
            "id": "Mark",
            "label": "标记",
            "open": false,
            "url": "Mark",
            "weight": 0
          },
          {
            "collapsible": false,
            "config": {
              "importStyle": {
                "fill_color": "0x00CC99",
                "outline_color": "0xea3a36"
              },
              "importTitle": "导入地块名称",
              "selectStyle": {
                "fill_color": "0xff0000",
                "outline_color": "0xFF0033"
              }
            },
            "display": false,
            "group": "Tool",
            "icon": "/omp/img/menu/assessment.png",
            "id": "IOWidget",
            "label": "导入导出",
            "open": false,
            "url": "IOWidget",
            "weight": 0
          }
        ]
      }
    ]
  },
  "widgets": [
    {
      "collapsible": false,
      "config": {
        "style": "normal"
      },
      "display": true,
      "id": "MenuBar",
      "open": false,
      "url": "MenuBar",
      "weight": 0
    },
    {
      "collapsible": false,
      "display": true,
      "id": "Navigation",
      "open": false,
      "url": "Navigation",
      "weight": 0
    },
    {
      "collapsible": false,
      "config": {
        "style": "default"
      },
      "display": true,
      "open": true,
      "url": "Scalebar",
      "weight": 0
    },
    {
      "collapsible": false,
      "display": true,
      "id": "Region",
      "open": false,
      "url": "Region",
      "weight": 0
    },
    {
      "collapsible": false,
      "config": {
        "layers": []
      },
      "display": false,
      "icon": "fa-location",
      "id": "Location",
      "label": "定位",
      "open": false,
      "url": "Location",
      "weight": 0
    }
  ]
}