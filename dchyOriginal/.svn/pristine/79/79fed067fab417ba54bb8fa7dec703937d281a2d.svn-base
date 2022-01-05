{
  "name": "标准服务模板",
  "logoVisible": true,
  "coordinateVisible": true,
  "defaultYear": "current",
  "defaultRegionCode": 320000,
  "title": "江苏省国土资源 一张图",
  "logo": "/omp/static/img/new/logo.jpg",
  "createAt": "2016-09-05 17:22:43",
  "geometryService": {
    "url": "http://tasks.arcgisonline.com/ArcGIS/rest/services/Geometry/GeometryServer"
  },
  "map": {
    "baseLayers": [],
    "operationalLayers": []
  },
  "dockWidgets": [
    {
      "label": "数据",
      "config": {
        "expandLevel": 1,
        "groupByYear": false
      },
      "icon": "glyphicon glyphicon-tasks",
      "desc": "分类数据",
      "display": true,
      "url": "DataList",
      "id": "DataList"
    },
    {
      "label": "视频",
      "icon": "glyphicon glyphicon-facetime-video",
      "desc": "视频管理",
      "config": {
        "txVersionEnable": false,
        "showVideoByCluster":false,
        "showArcCenter": false,
        "style": "list",
        "popupStyle": "layer",
        "autoCreateProject": false,
        "supervise": false,
        "newProjectLayer": [],
        "panoramaSwitch": "off",
        "searchVideoByXy": false,
        "exportXls": false,
        "showStatus": false,
        "modifyProVideo": false,
        "txtSymbol": {
          "color": "#32bfef",
          "fontSize": "12"
        },
        "proType": [
          {
            "name": "建设用地",
            "color": "#ffb61e"
          },
          {
            "name": "农用地",
            "color": "#00bc12"
          },
          {
            "name": "未利用地",
            "color": "#ff2d51"
          },
          {
            "name": "其他",
            "color": "#000000"
          }
        ],
        "identifyLayers": []
      },
      "display": true,
      "url": "VideoManager",
      "id": "VideoManager"
    }
  ],
  "widgets": [
    {
      "url": "MenuBar",
      "id": "MenuBar",
      "top": 0,
      "left": 0,
      "config": {
        "style": "normal"
      },
      "display": true
    },
    {
      "url": "Navigation",
      "id": "Navigation",
      "display": true,
      "right": 0,
      "top": 60
    },
    {
      "config": {
        "style": "default"
      },
      "open": true,
      "display": true,
      "url": "Scalebar"
    },
    {
      "url": "Region",
      "id": "Region",
      "left": 0,
      "top": 0,
      "display": true
    },
    {
      "label": "定位",
      "icon": "fa-location",
      "url": "Location",
      "id": "Location",
      "open": false,
      "config": {
        "layers": []
      }
    }
  ],
  "widgetContainer": {
    "widgets": [
      {
        "config": {
          "hideInvisibleLayer": true
        },
        "icon": "fa-bars",
        "url": "LayerList",
        "id": "LayerList",
        "label": "图层控制",
        "desc": "图层控制，控制图层通透度及展示图层图例"
      },
      {
        "config": {
          "layers": [],
          "merge": false
        },
        "desc": "",
        "display": true,
        "icon": "fa-search",
        "id": "82CFB17C-7687-F900-0555-FC470B2BF307",
        "label": "信息查询",
        "open": false,
        "url": "Query",
        "weight": 4
      },
      {
        "config": {
          "layers": []
        },
        "desc": "",
        "display": false,
        "icon": "fa-info",
        "id": "17101A7E-844D-6288-444F-23468D9D51E1",
        "label": "属性识别",
        "open": false,
        "url": "Identify",
        "weight": 6
      }
    ],
    "widgetsGroup": [
      {
        "label": "统计分析",
        "icon": "fa-bar-chart",
        "url": "Analysis",
        "id": "Analysis",
        "type": 5,
        "widgets": []
      },
      {
        "label": "常用工具",
        "icon": "fa-wrench",
        "url": "Tool",
        "id": "Tool",
        "type": 6,
        "widgets": [
          {
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
            "desc": "地图量算，对地图上要素进行量测，以获取其长度或者面积",
            "icon": "",
            "id": "MapMeasure",
            "label": "地图量算",
            "open": false,
            "url": "Measure",
            "group": "Tool",
            "weight": 3
          },
          {
            "url": "Print",
            "id": "Print",
            "label": "打印",
            "icon": "/omp/img/menu/print.png",
            "group": "Tool",
            "config": {
              "url": "http://192.168.1.125:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task",
              "title": "打印标题",
              "author": "作者",
              "copyright": "版权",
              "customTextItems": []
            }
          },
          {
            "label": "标记",
            "icon": "/omp/img/menu/mark.png",
            "url": "Mark",
            "id": "Mark",
            "group": "Tool"
          },
          {
            "bottom": "0",
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
            "desc": "",
            "icon": "/omp/img/menu/assessment.png",
            "id": "IOWidget",
            "label": "导入导出",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "IOWidget",
            "weight": 0,
            "group": "Tool"
          }
        ]
      }
    ]
  }
}