{
  "coordinateVisible": true,
  "createAt": "2014-10-09 14:06:43",
  "defaultRegionCode": "320000",
  "defaultYear": "current",
  "description": "",
  "dockWidgets": [
    {
      "config": {
        "expandLevel": 1
      },
      "desc": "分类数据",
      "display": true,
      "icon": "glyphicon glyphicon-tasks",
      "id": "DataList",
      "label": "数据",
      "open": false,
      "url": "DataList",
      "weight": 0
    },
    {
      "config": {
        "autoCreateProject": false,
        "exportXls": false,
        "identifyLayers": [
          {
            "layerName": "SDE.BPDK_320000",
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
            ],
            "serviceId": "0153694f72504028daac5368cd440003",
            "url": "http://192.168.90.44:8088/oms/arcgisrest/%E4%B8%9A%E5%8A%A1%E6%95%B0%E6%8D%AE/BPDK_320000/MapServer"
          }
        ],
        "modifyProVideo": false,
        "newProjectLayer": [
          {
            "layerName": "SDE.BPDK_320000",
            "returnName": "项目名称",
            "serviceId": "0153694f72504028daac5368cd440003"
          }
        ],
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
        "showProLocAll": true,
        "showStatus": false,
        "style": "list",
        "supervise": true,
        "txInspectPostUrl": "http://192.168.90.46:8080/omp/project/sendToLeasTest",
        "txVersionEnable": true,
        "txtSymbol": {
          "color": "#32bfef",
          "fontSize": "12"
        }
      },
      "desc": "视频管理",
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
    "url": "http://192.168.90.7:6080/arcgis/rest/services/Utilities/Geometry/GeometryServer"
  },
  "logo": "/omp/static/img/new/logo.jpg",
  "logoVisible": true,
  "map": {
    "baseLayers": [],
    "defaultScale": 0,
    "initExtent": {
      "spatialReference": {
        "latestWkid": 2364,
        "wkid": 2364
      },
      "xmax": 40645962.1577075,
      "xmin": 40375708.54684843,
      "ymax": 3698397.7652846524,
      "ymin": 3524040.596988481
    },
    "operationalLayers": [
      {
        "alias": "泰州行政区",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a27383eed4028daac5a0695df0008",
        "index": 1,
        "name": "XZQ",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/XZQ/MapServer",
        "visible": true,
        "xMaxExtent": 40552352.383,
        "xMinExtent": 40465831.17819,
        "xzdm": "320000",
        "yMaxExtent": 3677234.381413022,
        "yMinExtent": 3534366.4031000026,
        "year": 2016
      },
      {
        "alias": "KFQSJGXFW",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a273e3d524028daac5a0695df008a",
        "index": 2,
        "name": "KFQSJGXFW",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/KFQSJGXFW/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653136.4021175224,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      },
      {
        "alias": "KFQHZFW_TZ",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a273da6f04028daac5a0695df0071",
        "index": 3,
        "name": "KFQHZFW",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/KFQHZFW/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653136.4021175224,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      },
      {
        "alias": "泰州工业用地",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a273a47634028daac5a0695df0036",
        "index": 4,
        "name": "GYYD",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/GYYD/MapServer",
        "visible": false,
        "xMaxExtent": 40552097.65125001,
        "xMinExtent": 40472699.5499618,
        "xzdm": "320000",
        "yMaxExtent": 3672655.416300021,
        "yMinExtent": 3535650.001100002,
        "year": 2016
      },
      {
        "alias": "QYWZ",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a273ed3424028daac5a0695df00a3",
        "index": 5,
        "name": "QYWZ",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/QYWZ/MapServer",
        "visible": false,
        "xMaxExtent": 40551888.03125,
        "xMinExtent": 40472868.646800004,
        "xzdm": "320000",
        "yMaxExtent": 3672603.576870123,
        "yMinExtent": 3535767.337600002,
        "year": 2016
      },
      {
        "alias": "tax",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a2c2d47434028daac5a2b8d360003",
        "index": 6,
        "name": "tax",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/tax/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653143.3696776517,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      },
      {
        "alias": "企业分布密度热力图",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a27496cc24028daac5a0695df00be",
        "index": 7,
        "name": "heatmap_allent",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/heatmap_allent/MapServer",
        "visible": false,
        "xMaxExtent": 40552148.15103093,
        "xMinExtent": 40472598.15103093,
        "xzdm": "320000",
        "yMaxExtent": 3672896.8418309293,
        "yMinExtent": 3535496.8418309293,
        "year": 2016
      },
      {
        "alias": "employee",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a2c57b59c4028daac5a2b8d36003c",
        "index": 8,
        "name": "从业人员",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/employee/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653143.3696776517,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      },
      {
        "alias": "income",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a2c575ebd4028daac5a2b8d360020",
        "index": 9,
        "name": "营业收入",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/income/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653143.3696776517,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      },
      {
        "alias": "area",
        "alpha": 1,
        "category": "泰州工业用地",
        "group": "015a2731cbc44028daac5a0695df0006",
        "id": "015a2c67dc174028daac5a2b8d360058",
        "index": 10,
        "name": "土地面积",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/area/MapServer",
        "visible": false,
        "xMaxExtent": 40544969.3186,
        "xMinExtent": 40479731.4505027,
        "xzdm": "320000",
        "yMaxExtent": 3653143.3696776517,
        "yMinExtent": 3535648.1708000023,
        "year": 2016
      }
    ]
  },
  "name": "配置测试",
  "title": "常州市国土资源 一张图",
  "widgetContainer": {
    "widgets": [
      {
        "config": {
          "themes": [
            {
              "id": "001",
              "label": "建设用地",
              "services": [
                {
                  "id": "015a273a47634028daac5a0695df0036",
                  "label": "工业用地",
                  "loadOnStart": true
                }
              ]
            },
            {
              "id": "002",
              "label": "热力图分布",
              "services": [
                {
                  "id": "015a27496cc24028daac5a0695df00be",
                  "label": "企业分布热力图",
                  "loadOnStart": false
                }
              ]
            },
            {
              "id": "003",
              "label": "专题图",
              "services": [
                {
                  "id": "015a2c2d47434028daac5a2b8d360003",
                  "label": "税收",
                  "loadOnStart": false
                },
                {
                  "id": "015a2c575ebd4028daac5a2b8d360020",
                  "label": "营业收入",
                  "loadOnStart": false
                },
                {
                  "id": "015a2c57b59c4028daac5a2b8d36003c",
                  "label": "从业人员",
                  "loadOnStart": false
                },
                {
                  "id": "015a2c67dc174028daac5a2b8d360058",
                  "label": "土地面积",
                  "loadOnStart": false
                }
              ]
            }
          ]
        },
        "desc": "专题图层",
        "display": true,
        "icon": "fa-list",
        "id": "ThematicMap",
        "label": "专题图层",
        "open": false,
        "url": "ThematicMap",
        "weight": 2
      },
      {
        "config": {
          "subjects": [
            {
              "name": "dk",
              "alias": "工业地块",
              "serviceUrl": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/GYYD/MapServer/0",
              "linkUrl": "http://www.baidu.com/s?wd=${YDDW}",
              "columns": [
                {
                  "field": "YDDW",
                  "title": "用地单位",
                  "width": 80,
                  "type": "String"
                },
                {
                  "field": "TDSYQR",
                  "title": "土地所有",
                  "width": 100,
                  "type": "String"
                },
                {
                  "field": "JZMJ",
                  "title": "面积",
                  "type": "Number",
                  "width": 80
                }
              ]
            }
          ],
          "keywords": [
            {
              "title": "用地类型",
              "field": "YDLX",
              "values": [
                "合法审批",
                "未批先用"
              ]
            }
          ]
        },
        "desc": "扩展查询 可与业务结合的查询",
        "display": true,
        "icon": "fa-plane",
        "id": "ExtendQuery",
        "label": "业务查询",
        "open": false,
        "url": "ExtendQuery",
        "weight": 2
      },
      {
        "config": {},
        "desc": "图层控制，控制图层通透度及展示图层图例",
        "display": true,
        "icon": "fa-list",
        "id": "LayerList",
        "label": "图层控制",
        "open": false,
        "url": "LayerList",
        "weight": 2
      },
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
        "display": true,
        "icon": "fa-asterisk",
        "id": "85F52884-CB7D-C484-2256-F39FB593F293",
        "label": "地图量算",
        "open": false,
        "url": "Measure",
        "weight": 3
      },
      {
        "config": {
          "layers": [
            {
              "html": "",
              "layerIndex": 0,
              "layerName": "SDE.JCTB_2012",
              "layerUrl": "http://192.168.50.169:8088/oms/arcgisrest/当涂分析/JCTB_2012/MapServer/0",
              "link": {
                "url": "javascript:test();"
              },
              "queryFields": {
                "fields": [
                  {
                    "alias": "所在乡镇名称",
                    "defaultValue": "",
                    "name": "XZMC",
                    "operator": "like",
                    "type": "string"
                  },
                  {
                    "alias": "乡镇代码",
                    "name": "XZDM",
                    "operator": "like",
                    "type": "string"
                  },
                  {
                    "alias": "图斑编号",
                    "name": "JCBH",
                    "operator": "like",
                    "type": "string"
                  }
                ],
                "prefix": "%"
              },
              "returnFields": [
                {
                  "alias": "图斑编号",
                  "name": "JCBH",
                  "operator": "like",
                  "type": "string"
                },
                {
                  "alias": "标识码",
                  "name": "BSM",
                  "type": "STRING"
                },
                {
                  "alias": "所在乡镇名称",
                  "name": "XZMC",
                  "operator": "like",
                  "type": "string"
                },
                {
                  "alias": "乡镇代码",
                  "name": "XZDM",
                  "operator": "like",
                  "type": "string"
                },
                {
                  "alias": "项目名称",
                  "name": "XMMC",
                  "type": "STRING"
                },
                {
                  "alias": "建设单位",
                  "name": "JSDW",
                  "type": "STRING"
                },
                {
                  "alias": "监测面积",
                  "fixed": 3,
                  "name": "JCMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "图斑类型",
                  "name": "TBLX",
                  "type": "STRING"
                },
                {
                  "alias": "耕地面积",
                  "fixed": 3,
                  "name": "GDMJ01",
                  "type": "DOUBLE"
                },
                {
                  "alias": "已报批面积",
                  "fixed": 3,
                  "name": "BPMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "已报批耕地面积",
                  "fixed": 3,
                  "name": "BPGDMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "未报批面积",
                  "fixed": 3,
                  "name": "WBPMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "未报批耕地面积",
                  "fixed": 3,
                  "name": "WBPGDMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "已供地面积",
                  "fixed": 3,
                  "name": "GDMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "未供地面积",
                  "fixed": 3,
                  "name": "WGDMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "允许建设区面积",
                  "fixed": 3,
                  "name": "JSQMJ",
                  "type": "DOUBLE"
                },
                {
                  "alias": "基本农田面积",
                  "fixed": 3,
                  "name": "JBNTMJ",
                  "type": "DOUBLE"
                }
              ],
              "serviceId": "0146fbe8572c402881fd46d74065014c",
              "titleField": {
                "alias": "所在乡镇名称",
                "name": "XZMC"
              },
              "type": "field"
            }
          ],
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
          "layers": [
            {
              "enableVideo": false,
              "html": "",
              "layerIndex": 1,
              "layerName": "SDE.BPDK_320000",
              "link": {
                "tip": "超链接",
                "url": "http://www.baidu.com?q=${DKNAME}"
              },
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
                },
                {
                  "alias": "地块用途",
                  "name": "USAGE_",
                  "type": "STRING"
                },
                {
                  "alias": "申请单位名称",
                  "name": "UNIT_NAME",
                  "type": "STRING"
                },
                {
                  "alias": "行政代码",
                  "name": "REGIONCODE",
                  "type": "STRING"
                },
                {
                  "alias": "报件名称",
                  "name": "PCMC",
                  "type": "STRING"
                },
                {
                  "alias": "项目类型",
                  "name": "APP_TYPE",
                  "type": "STRING"
                }
              ],
              "serviceId": "0153694f72504028daac5368cd440003",
              "titleField": {
                "alias": "地块名称",
                "name": "DKNAME"
              },
              "type": "field"
            }
          ]
        },
        "desc": "",
        "display": false,
        "icon": "/omp/img/menu/identify.png",
        "id": "17101A7E-844D-6288-444F-23468D9D51E1",
        "label": "属性识别",
        "open": false,
        "url": "Identify",
        "weight": 6
      },
      {
        "config": {
          "layers": [
            {
              "html": "",
              "layerIndex": 1,
              "layerName": "KFQHZFW",
              "layerUrl": "http://192.168.90.114:8090/oms/arcgisrest/%E6%B3%B0%E5%B7%9E%E5%B7%A5%E4%B8%9A%E7%94%A8%E5%9C%B0/GYYD/MapServer/0",
              "link": {
                "tip": "超链接",
                "url": "http://192.168.90.114:8080/omp/project/test",
                "paramName": "OBJECTID",
                "openLinkImmd": true
              },
              "returnFields": [
                {
                  "alias": "OBJECTID",
                  "name": "OBJECTID",
                  "type": "STRING"
                }
              ],
              "serviceId": "015a273da6f04028daac5a0695df0071",
              "titleField": {
                "alias": "KFQMC",
                "name": "KFQMC"
              },
              "type": "field"
            },
            {
              "html": "",
              "layerIndex": 1,
              "layerName": "企业位置",
              "layerUrl": "http://192.168.90.44:8088/oms/arcgisrest/%E6%B3%B0%E5%B7%9E%E5%B7%A5%E4%B8%9A%E7%94%A8%E5%9C%B0/QYWZ/MapServer/0",
              "link": {
                "tip": "超链接",
                "url": "http://www.baidu.com",
                "paramName": "OBJECTID",
                "openLinkImmd": true
              },
              "returnFields": [
                {
                  "alias": "OBJECTID",
                  "name": "OBJECTID",
                  "type": "STRING"
                }
              ],
              "serviceId": "015a273ed3424028daac5a0695df00a3",
              "titleField": {
                "alias": "KFQMC",
                "name": "KFQMC"
              },
              "type": "field"
            },
            {
              "html": "",
              "layerIndex": 1,
              "layerName": "工业用地",
              "layerUrl": "http://192.168.90.44:8088/oms/arcgisrest/泰州工业用地/GYYD/MapServer/MapServer/0",
              "link": {
                "tip": "超链接",
                "url": "http://www.baidu.com",
                "paramName": "OBJECTID",
                "openLinkImmd": true
              },
              "returnFields": [
                {
                  "alias": "OBJECTID",
                  "name": "OBJECTID",
                  "type": "STRING"
                }
              ],
              "serviceId": "015a273a47634028daac5a0695df0036",
              "titleField": {
                "alias": "KFQMC",
                "name": "KFQMC"
              },
              "type": "field"
            }
          ]
        },
        "desc": "",
        "display": false,
        "icon": "/omp/img/menu/identify.png",
        "id": "17101A7E-844D-6288-444F-23468D9D51E2",
        "label": "空间分析",
        "open": false,
        "url": "SpatialAnalysis",
        "weight": 6
      }
    ],
    "widgetsGroup": [
      {
        "icon": "",
        "id": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
        "label": "分析组",
        "type": 0,
        "url": "",
        "weight": 7,
        "widgets": [
          {
            "config": {
              "analysis": {
                "dataSource": "sde",
                "layers": [
                  {
                    "fid": "bpdk",
                    "fields": "GD,PZWH,PCJC",
                    "layerName": "SDE.BPDK_340521",
                    "title": "报批"
                  },
                  {
                    "fid": "gddk",
                    "fields": "XMBH",
                    "layerName": "SDE.GDDK_340521",
                    "title": "供地"
                  },
                  {
                    "fid": "dltb",
                    "fields": "DLBM,DLMC",
                    "layerName": "SDE.DLTB_G_340521",
                    "title": "地类图斑"
                  },
                  {
                    "fid": "xzdw",
                    "fields": "DLBM,DLMC,XZDWMJ",
                    "layerName": "SDE.XZDW_G_340521",
                    "title": "现状地物"
                  },
                  {
                    "fid": "jsydgzq",
                    "fields": "GZQLXDM",
                    "layerName": "SDE.JSYDGZQ_E_2020",
                    "title": "建设用地管制区"
                  },
                  {
                    "fid": "tdytq",
                    "fields": "TDYTQLXDM",
                    "layerName": "SDE.TDYTQ_E_2020",
                    "title": "土地用途区"
                  },
                  {
                    "fid": "lsyd",
                    "fields": "BH",
                    "layerName": "SDE.LSYD_340521",
                    "title": "临时用地"
                  },
                  {
                    "fid": "ssnyd",
                    "fields": "BH",
                    "layerName": "SDE.SSNYD_340521",
                    "title": "设施农用地"
                  }
                ],
                "tdlyxz": [
                  {
                    "dltb": "SDE.DLTB_G_340521",
                    "xzdw": "SDE.XZDW_G_340521",
                    "year": "2011"
                  },
                  {
                    "dltb": "SDE.DLTB_G_2012",
                    "xzdw": "SDE.XZDW_G_2012",
                    "year": "2012"
                  },
                  {
                    "dltb": "SDE.DLTB_G_340521",
                    "xzdw": "SDE.XZDW_G_340521",
                    "year": "2013"
                  }
                ],
                "unit": {
                  "alias": "公顷",
                  "format": "0.0000"
                }
              },
              "query": {
                "layers": [
                  {
                    "url": "http://192.168.1.125:6080/arcgis/rest/services/DangTu/JCTB_2011/MapServer/0",
                    "year": "2011"
                  },
                  {
                    "url": "http://192.168.1.125:6080/arcgis/rest/services/DangTu/JCTB_2012/MapServer/0",
                    "year": "2012"
                  },
                  {
                    "url": "http://192.168.1.125:6080/arcgis/rest/services/DangTu/JCTB_2013/MapServer/0",
                    "year": "2013"
                  }
                ],
                "outFields": [
                  {
                    "alias": "所在镇名称",
                    "name": "XZMC"
                  },
                  {
                    "alias": "监测编号",
                    "name": "JCBH"
                  },
                  {
                    "alias": "图斑类型",
                    "name": "TBLX"
                  },
                  {
                    "alias": "标识码",
                    "name": "BSM"
                  },
                  {
                    "alias": "项目名称",
                    "name": "XMMC"
                  },
                  {
                    "alias": "建设单位",
                    "name": "JSDW"
                  },
                  {
                    "alias": "监测面积",
                    "name": "JCMJ"
                  }
                ],
                "queryFields": [
                  {
                    "alias": "所在乡镇",
                    "defaultValue": [
                      {
                        "name": "--所有--",
                        "value": ""
                      },
                      {
                        "name": "里桥村",
                        "value": "里桥村"
                      },
                      {
                        "name": "年陡村",
                        "value": "年陡村"
                      },
                      {
                        "name": "查联村",
                        "value": "查联村"
                      },
                      {
                        "name": "官碾村",
                        "value": "官碾村"
                      }
                    ],
                    "name": "XZMC",
                    "operator": "like",
                    "type": "string"
                  },
                  {
                    "alias": "监测编号",
                    "name": "JCBH",
                    "operator": "=",
                    "type": "string"
                  }
                ],
                "titleField": "XMMC"
              }
            },
            "desc": "",
            "display": false,
            "icon": "fa ",
            "id": "82CFB17C-7687-F900-0555-FC470B2Bxx",
            "label": "监测图斑分析",
            "open": false,
            "url": "JctbAnalysis",
            "weight": 2
          },
          {
            "config": {
              "areaUnit": "SQUARE",
              "dataSource": "sde",
              "exportAnalysis": true,
              "exportType": "cad,shp",
              "importType": "bj,cad,shp,txt,xls",
              "reportInfoUrl": "http://www.baidu.com?q=${所在乡镇名称}",
              "scopeLayers": [
                {
                  "layerName": "SDE.JCTB_2012",
                  "layerUrl": "http://192.168.50.169:8088/oms/arcgisrest/当涂分析/JCTB_2012/MapServer/0",
                  "returnFields": [
                    {
                      "alias": "项目名称",
                      "name": "XMMC",
                      "type": "string"
                    },
                    {
                      "alias": "所在乡镇名称",
                      "name": "XZMC",
                      "type": "string"
                    },
                    {
                      "alias": "乡镇代码",
                      "name": "XZDM",
                      "type": "string"
                    }
                  ]
                }
              ],
              "showSelMode": true,
              "year": [
                {
                  "dltb": "SDE.DLTB_G_340521",
                  "xzdw": "SDE.XZDW_G_340521",
                  "year": "2011"
                },
                {
                  "dltb": "JSXZ.DLTB_H_2012",
                  "xzdw": "JSXZ.XZDW_H_2012",
                  "year": "2012"
                }
              ]
            },
            "desc": "",
            "display": false,
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "2CEEAF61-839F-2CD5-A2F9-64264C491431",
            "label": "土地利用现状分析",
            "open": false,
            "url": "XzAnalysis",
            "weight": 0
          },
          {
            "config": {
              "areaUnit": "SQUARE",
              "dataSource": "sde",
              "exportAnalysis": true,
              "exportType": "cad,shp",
              "importType": "bj,cad,shp,txt,xls",
              "scopeLayers": [
                {
                  "layerName": "SDE.JCTB_2012",
                  "layerUrl": "http://192.168.50.169:8088/oms/arcgisrest/当涂分析/JCTB_2012/MapServer/0",
                  "returnFields": [
                    {
                      "alias": "项目名称",
                      "name": "XMMC",
                      "type": "string"
                    },
                    {
                      "alias": "所在乡镇名称",
                      "name": "XZMC",
                      "type": "string"
                    },
                    {
                      "alias": "乡镇代码",
                      "name": "XZDM",
                      "type": "string"
                    }
                  ]
                }
              ],
              "showSelMode": true,
              "year": "2014"
            },
            "desc": "",
            "display": false,
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "FDE793E3-C133-22B0-B9E8-EDA18E3E72D4",
            "label": "规划审查分析",
            "open": false,
            "url": "GhAnalysis",
            "weight": 1
          },
          {
            "config": {
              "dataSource": "sde",
              "groupDict": "",
              "groupFields": [
                {
                  "alias": "XMMC",
                  "name": "XMMC",
                  "type": "STRING"
                }
              ],
              "importCad": true,
              "importShp": true,
              "layerName": "SDE.BPDK_320000",
              "link": {
                "url": "http://192.168.50.169:8088/omp/geometryService/analysis/result/common"
              },
              "returnFields": [
                {
                  "alias": "PRONAME",
                  "name": "PRONAME",
                  "type": "STRING"
                },
                {
                  "alias": "NYD",
                  "name": "NYD",
                  "type": "STRING"
                },
                {
                  "alias": "ZMJ",
                  "name": "ZMJ",
                  "type": "STRING"
                },
                {
                  "alias": "BPZT",
                  "name": "BPZT",
                  "type": "STRING"
                },
                {
                  "alias": "YT",
                  "name": "YT",
                  "type": "STRING"
                }
              ],
              "showPie": true,
              "titleField": "PRONAME"
            },
            "desc": "",
            "display": false,
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "6D60BBA3-23A1-CF88-A8B2-C69E860D3E8A",
            "label": "报批分析",
            "open": false,
            "url": "Analysis",
            "weight": 3
          },
          {
            "config": {
              "areaLimit": false,
              "dataSource": "sde",
              "importBj": false,
              "importCad": false,
              "importTxt": true,
              "importXls": true,
              "jsonParams": [
                {
                  "areaorlenType": "area",
                  "dataSource": "sde",
                  "funid": "zd",
                  "layerName": "SDE.DZDJ",
                  "relationType": "intersect",
                  "returnFields": "ZDH,QLRMC",
                  "visible": false,
                  "whereClouse": ""
                },
                {
                  "areaorlenType": "area",
                  "funid": "dz",
                  "layerName": "SDE.ZA0C0500XPD,SDE.ZB0C0500HPD,SDE.ZC0C0500BTD,SDE.ZD0C0500TXD",
                  "relationType": "intersect",
                  "returnFields": "TYBH,MC",
                  "whereClouse": ""
                },
                {
                  "areaorlenType": "area",
                  "funid": "kc",
                  "layerName": "SDE.CKQ,SDE.TKQ",
                  "relationType": "intersect",
                  "returnFields": "XMMC",
                  "whereClouse": ""
                },
                {
                  "areaorlenType": "area",
                  "dataSource": "sde",
                  "funid": "bp",
                  "layerName": "SDE.BPDK_340521",
                  "relationType": "intersect",
                  "returnFields": "XMMC,BPZT,YT",
                  "visible": true,
                  "whereClouse": ""
                },
                {
                  "areaorlenType": "area",
                  "dataSource": "sde",
                  "dataUrl": "http://192.168.50.169:8088/omp/geometryService/dj/gateway/${XMBH}",
                  "funid": "gd",
                  "hyperlink": "XMMC|http://www.baidu.com?q={XMBH},XMBH|http://www.baidu.com?q={XMMC}",
                  "layerName": "SDE.GDDK_340521",
                  "relationType": "intersect",
                  "returnFields": "XMMC,XMBH",
                  "visible": true,
                  "whereClouse": ""
                },
                {
                  "dataSource": "sde",
                  "funid": "gh",
                  "layerType": "",
                  "requestUrl": "/omp/geometryService/analysis/tdghsc",
                  "visible": true,
                  "year": "340521"
                },
                {
                  "areaorlenType": "area",
                  "funid": "cl",
                  "layerName": "SDE.CLBZD",
                  "relationType": "intersect",
                  "returnFields": "BZDMC,BZDLX,DJ",
                  "whereClouse": ""
                },
                {
                  "areaorlenType": "area",
                  "funid": "sp",
                  "layerName": "SDE.SPJKD",
                  "relationType": "intersect",
                  "returnFields": "DEVICE_NAM",
                  "whereClouse": ""
                },
                {
                  "dltb": "DLTB",
                  "funid": "xz",
                  "requestUrl": "/omp/geometryService/analysis/tdlyxz",
                  "tpl": "multiAnalysis",
                  "xzdw": "XZDW",
                  "year": "2011"
                },
                {
                  "areaorlenType": "area",
                  "funid": "nt",
                  "layerName": "SDE.JBNT_340502_2012,SDE.JBNT_340503_2012,SDE.JBNT_340504_2012",
                  "relationType": "intersect",
                  "returnFields": "TDYTQLXDM,TDYTQBH",
                  "whereClouse": ""
                }
              ],
              "query": {
                "layers": []
              },
              "showSelMode": false,
              "tdlyxzYear": [
                2011,
                2012,
                2013,
                2014
              ]
            },
            "desc": "综合分析",
            "display": false,
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "F53B0AF2-B2C9-C8D7-748D-DCD3C9A99958",
            "label": "综合分析",
            "open": false,
            "url": "MultipleAnalysis",
            "weight": 6
          }
        ]
      },
      {
        "icon": "",
        "id": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
        "label": "工具",
        "type": 0,
        "url": "",
        "weight": 8,
        "widgets": [
          {
            "config": {},
            "desc": "",
            "display": false,
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "fa ",
            "id": "38EABC59-D7AB-A946-20CE-D7C3EACDEF00",
            "label": "通透镜",
            "open": false,
            "url": "Spy",
            "weight": 6
          },
          {
            "config": {},
            "desc": "",
            "display": false,
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/mark.png",
            "id": "9FF76EB2-ED45-69B2-DBC6-13F58037212D",
            "label": "地图标记",
            "open": false,
            "url": "Mark",
            "weight": 0
          },
          {
            "config": {
              "author": "作者",
              "copyright": "版权",
              "customTextItems": [
                {
                  "key": "1",
                  "name": "1",
                  "value": "1"
                }
              ],
              "title": "打印标题",
              "url": "http://192.168.90.7:6080/arcgis/rest/services/ExportWebMap/GPServer/%E5%AF%BC%E5%87%BA%20Web%20%E5%9C%B0%E5%9B%BE"
            },
            "desc": "",
            "display": false,
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/print.png",
            "id": "D7FCAF07-187E-2F3D-401E-762AA73C8AA8",
            "label": "地图打印",
            "open": false,
            "url": "Print",
            "weight": 1
          },
          {
            "config": {
              "exportType": "txt,xls,cad,shp,bj",
              "importStyle": {
                "fill_color": "#000099",
                "outline_color": "#cc0033"
              },
              "importTitle": "导入地块名称",
              "importType": "bj,shp,txt,xls,cad",
              "layers": [
                {
                  "layerIndex": 1,
                  "layerName": "SDE.BPDK_320000",
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
                    },
                    {
                      "alias": "地块用途",
                      "name": "USAGE_",
                      "type": "STRING"
                    },
                    {
                      "alias": "申请单位名称",
                      "name": "UNIT_NAME",
                      "type": "STRING"
                    },
                    {
                      "alias": "行政代码",
                      "name": "REGIONCODE",
                      "type": "STRING"
                    },
                    {
                      "alias": "报件名称",
                      "name": "PCMC",
                      "type": "STRING"
                    },
                    {
                      "alias": "项目类型",
                      "name": "APP_TYPE",
                      "type": "STRING"
                    }
                  ],
                  "serviceId": "0153694f72504028daac5368cd440003",
                  "titleField": {
                    "alias": "地块名称",
                    "name": "DKNAME"
                  },
                  "type": "field"
                }
              ],
              "selectStyle": {
                "fill_color": "#0000cc",
                "outline_color": "#00ff00"
              }
            },
            "desc": "",
            "display": false,
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/assessment.png",
            "id": "6A0C7B00-AE5D-5315-DB36-EB5E3C860901",
            "label": "导入导出",
            "open": false,
            "url": "IOWidget",
            "weight": 2
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
      "display": true,
      "open": false,
      "url": "MenuBar",
      "weight": 0
    },
    {
      "display": true,
      "open": true,
      "position": {
        "bottom": 60,
        "left": 0,
        "right": 0,
        "top": 0
      },
      "url": "Navigation",
      "weight": 0
    },
    {
      "config": {
        "style": "default"
      },
      "display": true,
      "open": true,
      "url": "Scalebar",
      "weight": 0
    },
    {
      "display": true,
      "open": false,
      "url": "Region",
      "weight": 0
    },
    {
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
      "display": false,
      "open": false,
      "position": {
        "bottom": 5,
        "left": 0,
        "right": 0,
        "top": 0
      },
      "url": "Overview",
      "weight": 0
    },
    {
      "config": {
        "layers": [
          {
            "layerAlias": "SDE.BPDK_320000",
            "layerIndex": 0,
            "returnFields": [
              {
                "alias": "地块ID",
                "name": "PL_PM_ID",
                "type": "STRING"
              },
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
            ],
            "scale": "5000",
            "serviceId": "0153694f72504028daac5368cd440003"
          }
        ]
      },
      "display": true,
      "id": "Location",
      "label": "地图定位",
      "open": true,
      "url": "Location",
      "weight": 0
    }
  ]
}