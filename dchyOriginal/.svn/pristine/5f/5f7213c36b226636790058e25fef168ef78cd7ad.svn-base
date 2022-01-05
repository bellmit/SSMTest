/*!
 * 专题图层控制widget
 * Author: yingxiufeng
 * Date:   2017/2/10 
 * Version:v1.0 (c) Copyright gtmap Corp.2017
 * ---------------------------------------
 * config:
 *--    {
 *--    "themes": [
 *--      {
 *--        "id": "001",
 *--        "label": "建设用地",
 *--        "services": [
 *--          {
 *--            "id": "xxxx",
 *--            "label": "工业用地",
 *--            "loadOnStart": false
 *--          }
 *--        ]
 *--      },
 *--      {
 *--        "id": "002",
 *--        "label": "热力图分布",
 *--        "services": [
 *--          {
 *--            "id": "eeee",
 *--            "label": "热力图1",
 *--            "loadOnStart": false
 *--          },
 *--          {
 *--            "id": "ggggg",
 *--            "label": "热力图2",
 *--            "loadOnStart": false
 *--          }
 *--        ]
 *--      }
 *--     ]
 *--    }
 * -
 * -
 * ---------------------------------------
 */

define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "map/core/BaseWidget",
    "map/core/support/Environment",
    "map/manager/ConfigManager",
    "esri/lang",
    "handlebars"], function (declare, lang, arrayUtil, BaseWidget, Environment, ConfigManager,esriLang, Handlebars) {

    var _mainmap, _widgetData;
    var _configManager=ConfigManager.getInstance();
    var me = declare([BaseWidget], {
        /**
         *
         */
        constructor: function () {

        },
        /**
         *
         */
        onCreate: function () {
            _mainmap = this.getMap();
            _widgetData = this.getConfig();
            this.getAppConfig();
            _init();
        },
        onOpen: function () {

        },
        /***
         * fires before destroy widget
         */
        onDestroy: function () {

        }

    });

    var thematicMaps = [];
    var $thematicList;

    /***
     *
     * @private
     */
    function _init() {
        thematicMaps = _widgetData.themes;
        $thematicList = $("#thematic-accordion");
        //遍历已配置的专题图层 并加载loadOnStart为true的专题服务
        arrayUtil.forEach(thematicMaps, function (item) {
            var services = item.services;
            if (esriLang.isDefined(services) && services.length > 0) {
                var themeLoaded = false;
                arrayUtil.forEach(services, function (service) {
                    if (service.loadOnStart === true) {
                        //加载服务
                        addLayer(service);
                        //设置父节点为选中状态
                        themeLoaded = true;
                    }
                });
                item.loadOnStart = themeLoaded;
            }
        });
        $thematicList.append(renderTpl($("#tGroupItemsTpl").html(), _widgetData));

        $('.panel-collapse').on('show.bs.collapse', function (evt) {
            $(evt.target).prev().find('i').removeClass('fa-chevron-down').addClass('fa-chevron-up');
        });
        $('.panel-collapse').on('hide.bs.collapse', function (evt) {
            $(evt.target).prev().find('i').removeClass('fa-chevron-up').addClass('fa-chevron-down');
        });

        //监听checkbox事件
        $(".thematic-cbx").on('change', function () {
            var $this = $(this);
            var checked = $this[0].checked;
            //分类的id
            var pid = $this.data("pid");
            //服务id
            var sid = $this.data("sid");
            if (checked) {
                if (esriLang.isDefined(sid))
                    addLayer(sid);
            } else {
                //移除已经加载服务
                if (esriLang.isDefined(sid))
                    _mainmap.removeLayerById(sid);
            }
        });
    }

    /***
     * 获取专题分类下的子服务组
     * @param pid
     */
    function getItemServices(pid){
        var arr=arrayUtil.filter(thematicMaps,function(theme){
            return theme.id=pid;
        });
        if(lang.isArray(arr)){
            return arr[0].services;
        }
        return [];
    }

    /***
     * 加载专题服务图层
     * @param service
     */
    function addLayer(service) {
        var layer = undefined;
        if (typeof service === 'string')
            layer = getOperaLyr(service);
        else
            layer = getOperaLyr(service.id);
        if (esriLang.isDefined(layer)) {
            layer.visible = true;
            _mainmap.addLayer(layer, layer.index);//按照配置的顺序加载
        }
    }

    /***
     * 获取模板里的服务图层
     * @param sId
     * @returns {*}
     */
    function getOperaLyr(id) {
        var operaLyrs = _configManager.getOperaLayers();
        if (lang.isArray(operaLyrs)) {
            var temp = arrayUtil.filter(operaLyrs, function (item) {
                return item.id == id;
            });
            return temp[0];
        }
        return undefined;
    }

    /***
     * render tpl
     * @param tpl
     * @param data
     */
    function renderTpl(tpl, data) {
        var templ = Handlebars.compile(tpl);
        return templ(data);
    }

    return me;


});
