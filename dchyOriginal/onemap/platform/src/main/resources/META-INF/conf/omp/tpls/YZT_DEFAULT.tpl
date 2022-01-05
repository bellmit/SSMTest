{
  "coordinateVisible": true,
  "createAt": "2013-12-19 14:03:55",
  "defaultRegionCode": "320000",
  "defaultYear": "current",
  "description": "一张图默认模板",
  "geometryService": {
    "url": "http://tasks.arcgisonline.com/ArcGIS/rest/services/Geometry/GeometryServer"
  },
  "logo": "/omp/img/mlogo.png",
  "logoVisible": true,
  "map": {
    "baseLayers": [],
    "defaultScale": 0,
    "lods": [
      {
        "resolution": 0.007138383017490841,
        "scale": 3000000
      },
      {
        "resolution": 0.002749665686873995,
        "scale": 1155583.42
      },
      {
        "resolution": 0.0006874164098211937,
        "scale": 288895.85
      },
      {
        "resolution": 0.00017185409650664595,
        "scale": 72223.96
      },
      {
        "resolution": 0.00004296352412666149,
        "scale": 18055.99
      },
      {
        "resolution": 0.00002148177396063577,
        "scale": 9028
      },
      {
        "resolution": 0.000010740886980317885,
        "scale": 4514
      }
    ],
    "operationalLayers": []
  },
  "name": "一张图",
  "printService": {
    "author": "作者",
    "copyright": "版权",
    "title": "打印标题",
    "url": "http://192.168.1.27:6080/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task"
  },
  "title": "江苏省国土资源 一张图",
  "widgetContainer": {
    "widgets": [
      {
        "config": {
          "hideInvisibleLayer": true
        },
        "desc": "图层控制，控制图层通透度及展示图层图例",
        "icon": "/omp/img/menu/ruler.png",
        "id": "LayerList",
        "label": "图层控制",
        "open": false,
        "url": "LayerList",
        "weight": 0
      }
    ],
    "widgetsGroup": [
      {
        "icon": "/omp/img/menu/magnifier.png",
        "id": "Query",
        "label": "信息查询",
        "type": 1,
        "url": "Query",
        "weight": 0,
        "widgets": []
      },
      {
        "icon": "/omp/img/menu/analysis.png",
        "id": "Analysis",
        "label": "统计分析",
        "type": 5,
        "url": "Analysis",
        "weight": 0,
        "widgets": []
      },
      {
        "icon": "/omp/img/menu/cart32.png",
        "id": "Tool",
        "label": "常用工具",
        "type": 6,
        "url": "Tool",
        "weight": 0,
        "widgets": [
          {
            "icon": "/omp/img/menu/assessment.png",
            "id": "Spy",
            "label": "通透镜",
            "open": false,
            "url": "Spy",
            "weight": 0
          },
          {
            "group": "标记",
            "icon": "/omp/img/menu/mark.png",
            "id": "Mark",
            "label": "标记",
            "open": false,
            "url": "Mark",
            "weight": 0
          },
          {
            "config": {},
            "icon": "/omp/img/menu/legend.png",
            "id": "Legend",
            "label": "图例",
            "open": false,
            "url": "Legend",
            "weight": 0
          }
        ]
      }
    ]
  },
  "widgets": [
    {
      "config": {
        "style": "normal"
      },
      "id": "MenuBar",
      "left": "0",
      "open": true,
      "top": "0",
      "url": "MenuBar",
      "weight": 0
    },
    {
      "bottom": "10",
      "id": "Command",
      "left": "10",
      "open": false,
      "url": "Command",
      "weight": 0
    },
    {
      "id": "Navigation",
      "open": false,
      "right": "0",
      "top": "60",
      "url": "Navigation",
      "weight": 0
    },
    {
      "id": "Region",
      "left": "0",
      "open": false,
      "top": "0",
      "url": "Region",
      "weight": 0
    },
    {
      "config": {
        "spit": "service",
        "uncheck": [
          1,
          2
        ]
      },
      "id": "RegionControl",
      "label": "行政区服务",
      "open": false,
      "url": "RegionControl",
      "weight": 0
    },
    {
      "config": {
        "layers": []
      },
      "icon": "/omp/img/menu/location.png",
      "id": "Location",
      "label": "定位",
      "open": false,
      "url": "Location",
      "weight": 0
    },
    {
      "icon": "/omp/img/menu/location.png",
      "id": "layerDefinition",
      "label": "图层过滤",
      "open": false,
      "url": "LayerDefinition",
      "weight": 0
    },
    {
      "bottom": "0",
      "config": {
        "lods": [
          {
            "resolution": 0.04283029810494505,
            "scale": 3000000
          },
          {
            "resolution": 0.01099866274749598,
            "scale": 1155583.42
          },
          {
            "resolution": 0.004124498458927162,
            "scale": 288895.85
          }
        ],
        "maxExtent": {
          "xmax": 124.20372337470876,
          "xmin": 114.23854068229154,
          "ymax": 35.58510134945418,
          "ymin": 31.33062507102963
        }
      },
      "icon": "/omp/img/menu/location.png",
      "id": "Overview",
      "label": "鹰眼",
      "open": true,
      "right": "0",
      "url": "Overview",
      "weight": 0
    }
  ]
}