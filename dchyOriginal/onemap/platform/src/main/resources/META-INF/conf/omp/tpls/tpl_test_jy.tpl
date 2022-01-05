{
  "coordinateVisible": true,
  "createAt": "2014-10-09 14:06:43",
  "defaultRegionCode": "320000",
  "defaultYear": "current",
  "description": "",
  "name": "配置测试_江阴",
  "title": "江阴市国土资源 一张图",
  "logo": "/omp/img/mlogo.png",
  "logoVisible": true,
  "geometryService": {
    "url": "http://192.168.90.7:6080/arcgis/rest/services/Utilities/Geometry/GeometryServer"
  },
  "map": {
    "baseLayers": [],
    "defaultScale": 0,
    "operationalLayers": [
      {
        "alias": "基础行政区",
        "alpha": 1,
        "category": "基础地理",
        "group": "01461c4478544028b229461c3f4900bf",
        "id": "01536952e2ef4028daac5368cd44001f",
        "index": 25,
        "name": "shengting",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/基础地理/shengting/MapServer",
        "visible": true,
        "xzdm": "320000",
        "year": 2015
      },
      {
        "alias": "BPDK_320000",
        "alpha": 1,
        "category": "报批",
        "group": "014648107beb402881fd46458bf00063",
        "id": "0153694f72504028daac5368cd440003",
        "index": 1,
        "name": "BPDK_320000",
        "type": "dynamic",
        "url": "http://192.168.90.44:8088/oms/arcgisrest/业务数据/BPDK_320000/MapServer",
        "visible": true,
        "xzdm": "320000",
        "year": 2015
      }
    ]
  },
   "dockWidgets": [
      {
        "label": "数据",
         "icon":"glyphicon glyphicon-tasks",
        "desc": "分类数据",
        "display": true,
        "url": "DataList"
      },
      {
        "label": "视频",
         "icon":"glyphicon glyphicon-facetime-video",
        "desc": "视频管理",
        "config": {
          "style": "list",
          "popupStyle": "layer",
          "supervise": true
        },
        "display": true,
        "url": "VideoManager"
      }
    ],
  "widgetContainer": {
    "widgets": [
      {
        "bottom": "0",
        "config": {},
        "desc": "图层控制，控制图层通透度及展示图层图例",
        "icon": "fa-list",
        "id": "LayerList",
        "label": "图层控制",
        "left": "0",
        "open": false,
        "display": true,
        "right": "0",
        "top": "0",
        "url": "LayerList",
        "weight": 2
      },
      {
        "bottom": "0",
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
        "icon": "fa-asterisk",
        "id": "85F52884-CB7D-C484-2256-F39FB593F293",
        "label": "地图量算",
        "left": "0",
        "open": false,
        "display": true,
        "right": "0",
        "top": "0",
        "url": "Measure",
        "weight": 3
      },
      {
        "bottom": "0",
        "config": {
          "layers": [
            {
              "html": "",
              "layerIndex": 0,
              "layerName": "SDE.GDDK_340521",
              "layerUrl": "http://192.168.50.169:8088/oms/arcgisrest/当涂分析/GDDK_340521/MapServer/0",
              "link": {
                "url": "http://www.baidu.com?q=${YDDW}"
              },
              "queryFields": {
                "fields": [
                  {
                    "alias": "XMMC",
                    "name": "XMMC",
                    "operator": "like",
                    "type": "string"
                  }
                ],
                "prefix": "%"
              },
              "returnFields": [
                {
                  "alias": "GDZT",
                  "name": "GDZT",
                  "type": "STRING"
                },
                {
                  "alias": "XMMC",
                  "name": "XMMC",
                  "operator": "like",
                  "type": "string"
                },
                {
                  "alias": "YDDW",
                  "name": "YDDW",
                  "type": "STRING"
                }
              ],
              "serviceId": "0146fafab5ab402881fd46d7406500ae",
              "titleField": {
                "alias": "XMMC",
                "name": "XMMC"
              },
              "type": "field"
            },
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
        "icon": "fa-search",
        "id": "82CFB17C-7687-F900-0555-FC470B2BF307",
        "label": "信息查询",
        "left": "0",
        "open": false,
        "display": true,
        "right": "0",
        "top": "0",
        "url": "Query",
        "weight": 4
      },
      {
        "bottom": "0",
        "config": {
          "layers": [
            {
              "enableVideo": false,
              "html": "",
              "layerIndex": 1,
              "layerName": "SDE.BPDK_320000",
              "serviceId": "0153694f72504028daac5368cd440003",
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
              "titleField": {
                "alias": "地块名称",
                "name": "DKNAME"
              },
              "type": "field"
            }
          ]
        },
        "desc": "",
        "icon": "/omp/img/menu/identify.png",
        "id": "17101A7E-844D-6288-444F-23468D9D51E1",
        "label": "属性识别",
        "left": "0",
        "open": false,
        "right": "0",
        "top": "0",
        "url": "Identify",
        "weight": 6
      },
      {
        "config": {
          "data": [
            {
              "children": [
                {
                  "name": "建设用地预审台账",
                  "sId": "0142c23853624848859442c2359d0024",
                  "url": "http://200.200.5.15:8080/buildland/preview/preview-jsydys.action?reqType=map&busiType=ysdk"
                }
              ],
              "name": "建设用地预审"
            },
            {
              "children": [
                {
                  "name": "报批地块台账",
                  "sId": "0142c23803cb4848859442c2359d0005",
                  "url": "http://200.200.5.15:8080/buildland/plot/plot-basic-sqb.action?reqType=map&busiType=bpdk"
                },
                {
                  "name": "建设用地报批台账",
                  "sId": "0142c23803cb4848859442c2359d0005",
                  "url": "http://200.200.5.15:8080/buildland/report/report-proj-build-info.action?reqType=map&busiType=bpdk"
                }
              ],
              "name": "建设用地报批"
            },
            {
              "children": [
                {
                  "name": "国有土地划拨台账",
                  "sId": "014fda825e2a4848858f4fd9c2f70005",
                  "url": "http://200.200.5.15:8080/buildland/listview/gdxm-list!openGyOrJthbList.action?gdxmType=huabo&reqType=map&busiType=gddk"
                },
                {
                  "name": "集体土地划拨台账",
                  "sId": "014fda825e2a4848858f4fd9c2f70005",
                  "url": "http://200.200.5.15:8080/buildland/listview/gdxm-list!openGyOrJthbList.action?gdxmType=jiti&reqType=map&busiType=gddk"
                },
                {
                  "name": "国有建设用地出让(工业)",
                  "sId": "014fda825e2a4848858f4fd9c2f70005",
                  "url": "http://200.200.5.15:8080/buildland/listview/gdxm-list!openGytdcrList.action?gdxmType=guapai&remiseType=gy&reqType=map&busiType=gddk"
                },
                {
                  "name": "国有建设用地出让(经营)",
                  "sId": "014fda825e2a4848858f4fd9c2f70005",
                  "url": "http://200.200.5.15:8080/buildland/listview/gdxm-list!openGytdcrList.action?gdxmType=guapai&remiseType=jy&reqType=map&busiType=gddk"
                }
              ],
              "name": "建设用地供地"
            },
            {
              "children": [
                {
                  "name": "工业用地挂牌地块台账",
                  "sId": "014fda825e2a4848858f4fd9c2f70005",
                  "url": "http://200.200.5.15:8080/buildland/landsource/gdxm-dktz.action?reqType=map&busiType=gddk"
                }
              ],
              "name": "工业挂牌台账"
            }
          ],
          "height": 300,
          "iframeFunc": "locationProj"
        },
        "desc": "",
        "icon": "fa-list",
        "id": "LedgerWidget",
        "label": "项目台账",
        "open": false,
        "url": "TP/jy/LedgerWidget",
        "weight": 0
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
            "bottom": "0",
            "config": {
              "dataSource": "sde",
              "exportAnalysis": true,
              "importType": "bj,cad,shp,txt,xls",
              "exportType": "cad,shp",
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
              "reportInfoUrl": "http://www.baidu.com?q=${所在乡镇名称}",
              "showSelMode": true,
              "areaUnit": "SQUARE",
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
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "2CEEAF61-839F-2CD5-A2F9-64264C491431",
            "label": "土地利用现状分析",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "XzAnalysis",
            "weight": 0
          },
          {
            "bottom": "0",
            "config": {
              "dataSource": "sde",
              "exportAnalysis": true,
              "importType": "bj,cad,shp,txt,xls",
              "exportType": "cad,shp",
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
              "areaUnit": "SQUARE",
              "year": "2014"
            },
            "desc": "",
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "FDE793E3-C133-22B0-B9E8-EDA18E3E72D4",
            "label": "规划审查分析",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "ghAnalysis",
            "weight": 1
          },
          {
            "bottom": "0",
            "config": {
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
              "dataSource": "sde",
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
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "6D60BBA3-23A1-CF88-A8B2-C69E860D3E8A",
            "label": "报批分析",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "Analysis",
            "weight": 3
          },
          {
            "bottom": "0",
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
            "group": "3EE2C807-CEE1-E0FB-AFB5-6425CF15CCB6",
            "icon": "/omp/img/menu/analysis.png",
            "id": "F53B0AF2-B2C9-C8D7-748D-DCD3C9A99958",
            "label": "综合分析",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
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
            "bottom": "0",
            "config": {},
            "desc": "",
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "fa ",
            "id": "38EABC59-D7AB-A946-20CE-D7C3EACDEF00",
            "label": "通透镜",
            "left": "0",
            "open": false,
            "display": false,
            "right": "0",
            "top": "0",
            "url": "Spy",
            "weight": 6
          },
          {
            "bottom": "0",
            "config": {},
            "desc": "",
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/mark.png",
            "id": "9FF76EB2-ED45-69B2-DBC6-13F58037212D",
            "label": "地图标记",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "Mark",
            "weight": 0
          },
          {
            "bottom": "0",
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
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/print.png",
            "id": "D7FCAF07-187E-2F3D-401E-762AA73C8AA8",
            "label": "地图打印",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
            "url": "Print",
            "weight": 1
          },
          {
            "bottom": "0",
            "config": {
              "gpUrl": "http://192.168.1.125:6080/arcgis/rest/services/GP/CAD2PoylgonFeatureSets/GPServer/CAD%20File%20Transform%20to%20Poylgon%20FeatureSets%20Python%20Script",
              "importCad": true,
              "importStyle": {
                "fill_color": "0x000099",
                "outline_color": "0xCC0033"
              },
              "importTitle": "导入地块名称",
              "selectStyle": {
                "fill_color": "0x0000CC",
                "outline_color": "0x00FF00"
              },
              "sort": [
                1,
                4,
                0,
                2
              ]
            },
            "desc": "",
            "group": "1A8EF898-E4B9-F0BC-6C65-13F551E849C8",
            "icon": "/omp/img/menu/assessment.png",
            "id": "6A0C7B00-AE5D-5315-DB36-EB5E3C860901",
            "label": "导入导出",
            "left": "0",
            "open": false,
            "right": "0",
            "top": "0",
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
      "open": false,
      "display": true,
      "url": "MenuBar",
      "weight": 0
    },
    {
      "position": {
        "top": 0,
        "right": 10,
        "bottom": 20,
        "left": 0
      },
      "open": true,
      "display": true,
      "url": "Navigation",
      "weight": 0
    },
    {
      "open": false,
      "display": false,
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
      "position": {
        "top": 0,
        "right": 0,
        "bottom": 5,
        "left": 0
      },
      "open": false,
      "display": false,
      "url": "Overview",
      "weight": 0
    },
    {
      "config": {
        "layers": [
          {
            "layerAlias": "SDE.BPDK_320000",
            "layerIndex": 0,
            "serviceId": "0153694f72504028daac5368cd440003",
            "fillStyle": {
              "alpha": 1,
              "color": "0xf16924",
              "lineAlpha": 1,
              "lineColor": "0xf16924",
              "lineWidth": 3
            },
            "fontStyle": {
              "bgColor": "0xffffff",
              "color": "0xf16924",
              "fontSize": 15
            },
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
            "scale": "5000"
          }
        ]
      },
      "display": true,
      "url": "Location",
      "weight": 0
    }
  ]
}