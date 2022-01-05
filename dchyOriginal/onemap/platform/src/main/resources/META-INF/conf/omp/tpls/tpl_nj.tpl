{
  "coordinateVisible": true,
  "createAt": "2014-10-09 14:06:43",
  "defaultRegionCode": "320000",
  "description": "",
  "dockWidgets": [
    {
      "collapsible": false,
      "config": {
        "expandLevel": 1
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
        "accounts": [
          {
            "password": "Sz2016",
            "server": "112.4.23.199",
            "username": "szgtjqly@gt.sz.jsqly",
            "wsuServer": "112.4.23.196",
            "xzqdm": "320500"
          }
        ],
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
        "showArcCenter": false,
        "showStatus": false,
        "showVideoByCluster": false,
        "supervise": true,
        "txInspectPostUrl": "http://192.168.90.46:8080/omp/project/sendToLeasTest",
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
    "url": "http://192.168.90.7:6080/arcgis/rest/services/Utilities/Geometry/GeometryServer"
  },
  "logo": "/omp/static/img/new/logo.jpg",
  "map": {
    "baseLayers": [],
    "defaultScale": 0,
    "initExtent": {
      "xmax": 123.71583187489176,
      "xmin": 114.72643218210828,
      "ymax": 35.36092789180001,
      "ymin": 32.174607498601794
    },
    "operationalLayers": [
      {
        "alias": "南京卫片",
        "alpha": 1,
        "category": "基础地理",
        "group": "015fed2ec16e402881505fed25ae0002",
        "id": "015fed34af35402881505fed25ae0008",
        "index": 1,
        "name": "NJZWyzt",
        "type": "swmts",
        "url": "http://192.168.1.208:8080/oms/arcgisrest/基础地理/wp_nj/MapServer",
        "visible": false,
        "xMaxExtent": 539045.4086825321,
        "xMinExtent": 453146.36122642277,
        "yMaxExtent": 3611508.021274528,
        "yMinExtent": 3455252.4435387,
        "year": 2017
      },
      {
        "alias": "南京行政区底图",
        "alpha": 1,
        "category": "基础地理",
        "group": "015fed2ec16e402881505fed25ae0002",
        "id": "015fed2ff4d6402881505fed25ae0004",
        "index": 2,
        "name": "xzq_nj",
        "type": "swmts",
        "url": "http://192.168.1.208:8080/oms/arcgisrest/基础地理/xzq_nj/MapServer",
        "visible": true,
        "xMaxExtent": 539661.293085777,
        "xMinExtent": 452516.9228609864,
        "yMaxExtent": 3610474.0237544077,
        "yMinExtent": 3455482.4879306587,
        "year": 2017
      }
    ]
  },
  "name": "配置测试",
  "title": "常州市国土资源 一张图",
  "widgetContainer": {
    "widgets": [
      {
        "collapsible": false,
        "config": {},
        "display": true,
        "icon": "fa-list",
        "id": "LayerList",
        "label": "图层控制",
        "open": false,
        "url": "LayerList",
        "weight": 2
      },
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
          "openMapPopup": false,
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
        "display": true,
        "icon": "fa-asterisk",
        "id": "85F52884-CB7D-C484-2256-F39FB593F293",
        "label": "地图量算",
        "open": false,
        "url": "Measure",
        "weight": 3
      },
      {
        "collapsible": false,
        "config": {
          "filterAfterQuery": true,
          "layers": [
            {
              "html": "",
              "layerIndex": "0",
              "layerName": "SDE.BPDK_320000",
              "layerUrl": "http://192.168.90.44:8088/oms/arcgisrest/业务数据/BPDK_320000/MapServer/0",
              "link": {
                "tip": "",
                "url": ""
              },
              "queryFields": {
                "fields": [
                  {
                    "alias": "地块ID",
                    "name": "PL_PL_ID",
                    "operator": "like",
                    "type": "STRING"
                  },
                  {
                    "alias": "地块名称",
                    "name": "DKNAME",
                    "operator": "like",
                    "type": "STRING"
                  },
                  {
                    "alias": "项目名称",
                    "name": "PRONAME",
                    "operator": "like",
                    "type": "STRING"
                  }
                ],
                "prefix": "%"
              },
              "returnFields": [
                {
                  "alias": "地块ID",
                  "name": "PL_PL_ID",
                  "operator": "like",
                  "type": "STRING"
                },
                {
                  "alias": "项目ID",
                  "name": "SB_SB_ID",
                  "type": "STRING"
                },
                {
                  "alias": "报件ID",
                  "name": "PB_PB_ID",
                  "type": "STRING"
                },
                {
                  "alias": "地块名称",
                  "name": "DKNAME",
                  "operator": "like",
                  "type": "STRING"
                },
                {
                  "alias": "项目名称",
                  "name": "PRONAME",
                  "operator": "like",
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
          "merge": false,
          "popUpStyle": "infowindow"
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
          ],
          "leasPostUrl": "",
          "popUpStyle": "infowindow"
        },
        "display": false,
        "icon": "/omp/img/menu/identify.png",
        "id": "17101A7E-844D-6288-444F-23468D9D51E1",
        "label": "属性识别",
        "open": false,
        "url": "Identify",
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
            "collapsible": false,
            "config": {
              "areaUnit": "SQUARE",
              "dataSource": "sde",
              "exportAnalysis": true,
              "exportType": "cad,shp",
              "importType": "bj,cad,shp,txt,xls",
              "reportInfoUrl": "http://www.baidu.com?q=${所在乡镇名称}",
              "scopeLayers": [],
              "showSelMode": false,
              "year": [
                {
                  "dltb": "SDE.DLTB_2013",
                  "xzdw": "SDE.XZDW_2013",
                  "year": "2013"
                },
                {
                  "dltb": "sde.DLTB_320400",
                  "xzdw": "sde.XZDW_320400",
                  "year": "2014"
                }
              ]
            },
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
            "collapsible": false,
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
            "collapsible": false,
            "config": {
              "dataSource": "sde",
              "groupDict": "",
              "groupFields": [
                {
                  "alias": "DKNAME",
                  "name": "DKNAME",
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
                  "alias": "DKNAME",
                  "name": "DKNAME",
                  "type": "STRING"
                },
                {
                  "alias": "PRONAME",
                  "name": "PRONAME",
                  "type": "STRING"
                }
              ],
              "titleField": "PRONAME"
            },
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
            "collapsible": false,
            "config": {
              "bpdkLayer": "SDE.BPDK_3206",
              "dataSource": "",
              "jsydgzqLayer": "SDE.JSYDGZQ_E_2020_1",
              "xzqLayer": "SDE.XJSJ_2013_3206"
            },
            "display": false,
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "6D60BBA3-23A1-CF88-A8B2-C69E860D3E8A",
            "label": "规划执行情况分析",
            "open": false,
            "url": "GhzxAnalysis",
            "weight": 3
          },
          {
            "collapsible": false,
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
                  "alias": "报批分析",
                  "areaorlenType": "area",
                  "dataSource": "sde",
                  "funid": "bp",
                  "layerName": "SDE.BPDK_320000",
                  "relationType": "intersect",
                  "returnFields": "DKNAME,PRONAME,APP_TYPE",
                  "visible": true,
                  "whereClouse": ""
                },
                {
                  "alias": "供地分析",
                  "areaorlenType": "area",
                  "dataSource": "sde",
                  "dataUrl": "http://192.168.50.169:8088/omp/geometryService/dj/gateway/${XMBH}",
                  "funid": "gd",
                  "hyperlink": "XMMC|http://www.baidu.com?q={XMBH},XMBH|http://www.baidu.com?q={XMMC}",
                  "layerName": "SDE.GDDK_340521",
                  "relationType": "intersect",
                  "returnFields": "XMMC,XMBH",
                  "visible": false,
                  "whereClouse": ""
                },
                {
                  "alias": "规划分析",
                  "dataSource": "sde",
                  "funid": "gh",
                  "layerType": "",
                  "requestUrl": "/omp/geometryService/analysis/tdghsc",
                  "visible": false,
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
                  "alias": "国有建设用地分析",
                  "dataSource": "sde",
                  "funid": "gyjsydfx",
                  "groupField": "QSXZ",
                  "layerName": "gyjsyd_2015_320381",
                  "returnFields": "*",
                  "visible": true
                },
                {
                  "alias": "现状分析",
                  "dltb": "DLTB_320400",
                  "exportDiff": true,
                  "exportable": true,
                  "funid": "xz",
                  "requestUrl": "/omp/geometryService/analysis/tdlyxz",
                  "tpl": "multiAnalysis",
                  "visible": true,
                  "xzdw": "XZDW_320400",
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
              "level": "lvshun",
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
            "collapsible": false,
            "config": {},
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
            "collapsible": false,
            "config": {},
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
            "collapsible": false,
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
            "collapsible": false,
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
      "collapsible": false,
      "config": {
        "style": "normal"
      },
      "display": true,
      "open": false,
      "url": "MenuBar",
      "weight": 0
    },
    {
      "collapsible": false,
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
      "open": false,
      "url": "Region",
      "weight": 0
    },
    {
      "collapsible": false,
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
      "collapsible": false,
      "config": {
        "layers": [
          {
            "layerAlias": "SDE.BPDK_320000",
            "layerIndex": 0,
            "returnFields": [
              {
                "alias": "地块ID",
                "name": "PL_PL_ID",
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
        ],
        "locateOpacity": 0.5
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