/**
 *  图形编辑
 *  config： {
 *    "dataSource": "sde",
 *    "editLayer": "SDE.CSDK",
 *    "editFields": [
            {
              "label": "项目名称",
              "name": "PRONAME",
              "type": "STRING"
            }
          ]
 *  }
 * Created by alex on 2017/12/10.
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojox/uuid/generateRandomUuid",
    "dojo/json",
    "map/core/BaseWidget",
    "map/utils/MapUtils",
    "map/core/JsonConverters",
    "esri/graphic",
    "esri/geometry/Geometry",
    "esri/geometry/Polygon",
    "esri/layers/GraphicsLayer",
    "esri/SpatialReference",
    "esri/toolbars/draw",
    "map/core/EsriSymbolsCreator",
    "hbarsUtils",
    "static/js/cfg/core/SerializeForm",
    "layer"], function (declare, lang, arrayUtil, RandomUuid, dojoJson, BaseWidget, MapUtils, JsonConverters, Graphic,
                        Geometry, Polygon, GraphicsLayer, SpatialReference, Draw, EsriSymbolsCreator,
                        HbarsUtils, SerializeForm, layer) {

    var _map, _widgetData;
    var drawTool, drawHandler, graphicsLyr, currentGra;
    var me = declare([BaseWidget], {

        onCreate: function () {
            _map = this.getMap().map();
            _widgetData = this.getConfig();
            _init();
            MapUtils.setMap(_map);
        },
        /**
         *
         */
        onPause: function () {
            _pause();
        },
        /**
         *
         */
        onOpen: function () {
            _resume();
        },
        /**
         *
         */
        onDestroy: function () {

        }

    });

    function _init() {
        graphicsLyr = new GraphicsLayer({id: 'editGraphics'});
        _map.addLayer(graphicsLyr);
        $(".btn-wrapper").find('.btn').on('click', function (evt) {
            _draw($(this).data('geotype'));
        });
    }

    function _pause() {

    }

    function _resume() {

    }

    /**
     * 画图形
     * @param geoType
     * @private
     */
    function _draw(geoType) {
        if (drawTool !== undefined) drawTool.deactivate();
        if (drawHandler !== undefined) drawHandler.remove();
        drawTool = drawTool !== undefined ? drawTool : new Draw(_map, {
            tooltipOffset: 20
        });
        drawHandler = drawTool.on("draw-end", lang.hitch(this, function (evt) {
            drawTool.deactivate();
            if (evt.geometry !== null) {
                var gra = new Graphic(evt.geometry, EsriSymbolsCreator.defaultFillSymbol, {});
                graphicsLyr.add(gra);
                currentGra = gra;
                // update ui
                $(".edit-prop-wrapper").empty();
                $(".edit-prop-wrapper").append(HbarsUtils.renderTpl($("#propTpl").html(), {
                    fields: _widgetData.editFields
                }));
                _updatePropFields();
                _submitOrCancel();
            }
        }));
        drawTool.activate(geoType);
    }

    /**
     * 获取属性字段
     * @return {[*,*]}
     * @private
     */
    function _updatePropFields() {
        var fields = [];
        var urlParam = getUrlParams();
        var proid = urlParam.proid;
        if (proid !== undefined) {
            var df = _widgetData.editFields[1];
            $("#input".concat(df.name)).val(proid);
            $.getJSON(root + '/transitService/plat/project', {proid: proid}, function (res) {
                var f = _widgetData.editFields[0];
                $("#input".concat(f.name)).val(res);
            });
        }
        return fields;
    }

    /**
     * 提交
     * @private
     */
    function _submitOrCancel() {
        $(".btn-edit-prop").on('click', function () {
            if ($(this).data('type') === 'submit') {
                var attribute = SerializeForm.serializeObject($('.edit-prop-wrapper .form-horizontal'));
                currentGra.setAttributes(attribute);
                var msgHandler = layer.msg('提交数据中...', {time: 20000, icon: 16});
                $.ajax({
                    url: root + '/geometryService/rest/insert',
                    method: 'post',
                    data: {
                        layerName: _widgetData.editLayer,
                        geometry: dojoJson.stringify(JsonConverters.toGeoJson(currentGra)),
                        check: false,
                        dataSource: _widgetData.dataSource
                    },
                    success: function (res) {
                        layer.close(msgHandler);
                        if (res.hasOwnProperty("success")) {
                            console.error(res);
                            layer.msg(res.msg, {icon: 0, time: 4000});
                            return;
                        }
                        layer.msg('提交成功', {icon: 1});
                    }
                });

            } else {
                graphicsLyr.clear();
                $(".edit-prop-wrapper").empty();
            }
        });
    }

    return me;
});