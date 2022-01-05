/*!
 * 地图弹出窗
 * 提供三种方式展示地图弹出窗
 * >>>openPopup():类似于ags自带的infoWindow
 * >>>openPopupLite():缩略版 只显示标题信息
 * >>>openPopupFix():弹出窗固定方式 可固定在右侧或左侧
 * 用法:
 * >>>单例模式 var popup=MapPopup.getInstance();
 * 注意:
 * >>>在弹出信息窗之后再定位居中要素图形 否则会发生不可描述的偏移
 * >>> popup.openPopup(loc).then(//定位,居中..);
 *
 * Author: yingxiufeng
 * Date:   2016/10/8 
 * Version:v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/Evented",
    "dojo/_base/Deferred",
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/dnd/Moveable",
    "esri/lang",
    "dijit/Menu",
    "static/js/map/core/support/Environment",
    "handlebars",
    "text!map/template/map/map-popup-tpl.html",
    "slimScroll",
    "css!map/template/map/map-popup.css"], function (Evented, Deferred, declare, lang, arrayUtil, Moveable, esriLang, Menu, Environment,
                                                     Handlebars, ContentTpl) {

    var instance, me = declare([Evented], {
        /***
         * 关联的地图
         */
        map: undefined,

        /***
         *
         */
        geo: undefined,

        /***
         * 展示数据
         */
        showData: undefined,

        /***
         * 要展示的字段集合 通常是配置中的returnFields
         */
        showFields: [],

        /**
         * 不动产状态
         */
        bdczt: undefined,

        /**
         * 是否编辑字段
         */
        isEdit: false,

        /**
         * 插入数据信息
         */
        insertDataInfo: undefined,
        /**
         * 标题字段名称
         */
        tfName: undefined,

        /**
         * 展示的标题 用于自定义展示
         */
        title: undefined,

        /***
         * 展示的内容 用于自定义展示
         */
        content: undefined,

        /***
         * 超链接 eg. {tip: "超链接", url: "http://www.baidu.com?q=${DKNAME}"}
         */
        hyperLink: undefined,

        /**
         * 保存数据字段
         */
        insertKey: undefined,
        /**
         * 是否忽略值为null的字段
         */
        ignoreNullValue: false,
        /**
         * 值为null的字符串字段会显示为空
         */
        writeNullStringAsEmpty: true,

        /**
         * 业务外链是否可用 如. 常州用来是否显示底部档案查看按钮
         */
        bLinkEnabled: false,


        /**
         * 调用来源
         */
        source: undefined,

        /***
         * 多个超链接
         */
        secLink: undefined,
        /**
         *
         */
        constructor: function () {
            this.isShowing = false;//全局属性 可通过.获取
            Handlebars.registerHelper('ifContent', function (context) {
                if (esriLang.isDefined(context))
                    return new Handlebars.SafeString(context);
            });
        },
        /***
         * 修改配置参数
         * @param feature
         * @param value
         */
        config: function (prop, value) {
            switch (prop) {
                case 'ignoreNullValue':
                    this.ignoreNullValue = value;
                    break;
                case 'writeNullStringAsEmpty':
                    this.writeNullStringAsEmpty = value;
                    break;
            }
        },
        /***
         * 绑定关联的地图
         * @param m
         */
        setMap: function (m) {
            this.map = m;
            this.infoWindow = m.infoWindow;
        },

        /**
         * 这是展示数据
         * @param data
         */
        setData: function (data) {
            this.showData = data;
        },

        /***
         * 设置展示字段
         * @param f
         */
        setFields: function (f) {
            if (esriLang.isDefined(f)) this.showFields = f;
        },

        /**
         *
         * @param tf
         */
        setTitleField: function (tf) {
            this.tfName = tf;
        },


        /**
         *
         * @param bdczt
         */
        setBdczt: function (bdczt) {
            this.bdczt = bdczt;
        },

        /**
         *
         * @param isEdit
         */
        setIsEdit: function (isEdit) {
            this.isEdit = isEdit;
        },

        /**
         *
         * @param insertGeometry
         */
        setInsertDataInfo: function (insertDataInfo) {
            this.insertDataInfo = insertDataInfo;
        },


        /**
         *
         * @param l
         */
        setLink: function (l) {
            this.hyperLink = l;
        },

        /**
         * 设置展示标题
         * @param title
         */
        setTitle: function (title) {
            this.title = title;
        },

        /**
         * 保存数据字段名key
         * @param insertKey
         */
        setInsertKey: function (insertKey) {
            this.insertKey = insertKey;
        },


        /**
         *
         * @param content
         */
        setContent: function (content) {
            this.content = content;
        },
        /**
         *
         * @param value
         */
        setBLinkEnabled: function (value) {
            this.bLinkEnabled = value;
        },

        setSource: function (val) {
            this.source = val;
        },

        /**
         *
         * @param l
         */
        setSecLink: function (l) {
            this.secLink = l;
        },
        /**
         * 打开地图弹出窗
         *
         * @param location
         * @param type  保存后是否关闭窗口
         * @returns {*}
         */
        openPopup: function (location, instance) {
            var _this = this
            var thisVar = instance || this;
            var deferred = new Deferred();
            thisVar.location = location;
            var isPrepared = thisVar._prepare();
            if (isPrepared) thisVar._show();
            deferred.callback(true);
            var serializeObj = {};
            var columns = {};
            var insertKey = thisVar.insertKey;
            serializeObj["insertDataInfo"] = thisVar.insertDataInfo;
            $("#drawEditSave").on("click", function () {
                var msgHandle = layer.msg('保存中...', {time: 6000});
                $.each($("#drawLayerForm .row"), function (index, layerFormItem) {
                    var name = layerFormItem.children[0].children[0].title;
                    var value = layerFormItem.children[1].children[0].value;
                    columns[name] = value;

                });
                if (insertKey != undefined) {
                    columns[insertKey] = proid;
                }
                serializeObj["columns"] = columns;
                var serializeStr = JSON.stringify(serializeObj);
                $.ajax({
                    type: 'post'
                    , url: root + '/geometryService/insertDrawData'
                    , data: {insertData: serializeStr}
                    , cache: false
                    , async: true
                    , success: function (ret) {
                        if (ret.success === true) {
                            var linkUrl = thisVar.hyperLink;
                            thisVar.closePopup();
                            var data = {insertKey: proid, layerName: thisVar.insertDataInfo.layerName};
                            $.ajax({
                                type: 'post'
                                , url: linkUrl
                                , data: data
                                , cache: false
                                , async: false
                                , success: function (ret) {
                                    layer.close(msgHandle);
                                    layer.msg("保存成功！");
                                    //保存成功后刷新
                                    var center = _this.map.extent.getCenter()
                                    _this.map.resize(true)
                                    _this.map.reposition()
                                    _this.map.centerAt(center)
                                    if (_this.source == "InsertData_TZ") {
                                        //成功后触发回调事件
                                        EventBus.trigger("InsertData_TZ");
                                    }

                                }
                            });
                        } else {
                            layer.msg("保存失败！");
                        }
                    }
                });
            });

            return deferred;
        },

        /***
         *
         * @param location
         */
        openPopupLite: function (location) {
            this.location = location;
            var isPrepared = this._prepare();
            if (isPrepared) this._show();
        },
        /**
         * 关闭弹出窗 同时清空变量
         */
        closePopup: function (instance) {
            // var that =instance||this;
            var that = this;
            var d = new Deferred();
            that._hide();
            that.title = null;
            that.content = null;
            that.showData = null;
            that.showFields = [];
            that.tfName = null;
            that.hyperLink = null;
            that.bdczt = null;
            //用于回调
            that.emit("close", {});
            d.callback(true);
            return d;
        },

        /**
         *
         * @private
         */
        _hide: function () {
            this.infoWindow.hide();
            this.isShowing = false;
        },

        /**
         *
         * @private
         */
        _show: function () {
            this.infoWindow.show(this.location);
            this.isShowing = true;
            this.infoWindow.resize(320, 320);
            //设置竖滚动条
            var contentHeight = $(".map-popup-content").height();
            var maxHeight = this.bLinkEnabled ? 320 : 400;
            if (this.bdczt) {
                maxHeight = maxHeight - 50;
            }
            var slimHeight = contentHeight > maxHeight ? maxHeight : contentHeight + 10;
            $(".map-popup-content").slimScroll({
                height: slimHeight < 30 ? 30 : slimHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });
            //监听关闭事件
            $(".map-popup-close").unbind('click').on('click', lang.hitch(this, this.closePopup));
            //用于在显示弹出窗之后回调
            this.emit("shown", {});

            //弹框可拖动
            var handle = this.infoWindow.domNode.querySelector(".map-popup-header");
            var dnd = new Moveable(this.infoWindow.domNode, {handle: handle});
            dnd.on('FirstMove', function () {
                var arrowNode = this.infoWindow.domNode.querySelector(".outerPointer");
                arrowNode.classList.add("hidden");
                arrowNode = this.infoWindow.domNode.querySelector(".pointer");
                arrowNode.classList.add("hidden");
            }.bind(this));
            /*        $(".map-popup-content").on('dblclick', function () {
                        $("#popupCopy").removeClass("hidden");
                    });
                    $("#popupCopy").on('click', function () {
                        $("#input").removeClass("hidden");
                        var text = $(".map-popup-content")[0].innerText;
                        var input = document.getElementById("input");
                        input.value = text;
                        input.select();
                        document.execCommand("copy");
                        $("#input").addClass("hidden");


                    });*/

        },
        /**
         *
         * @private
         */
        _prepare: function () {
            if (this.isShowing) this._hide();
            try {
                this.title = this._getTitle();
                this.infoWindow.setContent(this._getContent());
                this._resetDefaultStyle();
            } catch (e) {
                error(e);
                return false;
            }
            return true;
        },
        /**
         *
         * @private
         */
        _getContent: function () {
            var data = this.showData;
            var fields = this.showFields;
            var title = this.title;
            if (esriLang.isDefined(this.content)) {
                return _renderTpl(ContentTpl, {
                    content: this.content,
                    title: title,
                    bLinkEnabled: this.bLinkEnabled,
                    bdczt: this.bdczt
                });
            } else {
                var array = [];
                if (esriLang.isDefined(data)) {
                    var linkUrl = null;
                    var secLinkUrl = [];
                    if (esriLang.isDefined(this.hyperLink) && this.hyperLink.url != "") linkUrl = this._getLinkUrl(data, fields, this.hyperLink);
                    if (esriLang.isDefined(this.secLink) && this.secLink.url != "") secLinkUrl = this._getSecLinkUrl(data, fields, this.secLink);
                    array = arrayUtil.map(fields, function (item) {
                        var obj = {};
                        lang.mixin(obj, item);
                        obj.value = data[item.name] || data[item.alias];
                        return obj;
                    });
                }
                if (this.isEdit) {
                    this.isEdit = false;
                    return _renderTpl(ContentTpl, {
                        fields: array,
                        content: this.content,
                        title: title,
                        bLinkEnabled: this.bLinkEnabled,
                        isEdit: true
                    });
                } else {
                    if (esriLang.isDefined(secLinkUrl) && secLinkUrl.length > 0) {
                        return _renderTpl(ContentTpl, {
                            fields: array,
                            title: title,
                            secLink: secLinkUrl,
                            bLinkEnabled: this.bLinkEnabled,
                            bdczt: this.bdczt
                        });
                    } else if (esriLang.isDefined(linkUrl)) {
                        return _renderTpl(ContentTpl, {
                            fields: array,
                            title: title,
                            link: {tip: this.hyperLink.tip, url: linkUrl},
                            bLinkEnabled: this.bLinkEnabled,
                            bdczt: this.bdczt
                        });
                    } else {
                        return _renderTpl(ContentTpl, {
                            fields: array,
                            title: title,
                            bLinkEnabled: this.bLinkEnabled,
                            bdczt: this.bdczt
                        });
                    }
                }
            }
        },
        /**
         *
         * @returns {*}
         * @private
         */
        _getTitle: function () {
            var tf = this.tfName;
            var fields = this.showFields;
            var data = this.showData;
            if (esriLang.isDefined(this.title)) return this.title;
            else {
                if (esriLang.isDefined(data)) {
                    if (esriLang.isDefined(tf)) {
                        if (data.hasOwnProperty(tf))
                            return data[tf];
                        else {
                            var tmp = arrayUtil.filter(fields, function (item) {
                                return item.name === tf;
                            });
                            if (tmp.length > 0) {
                                return data[tmp[0].alias];
                            }
                        }
                    } else {
                        //若标题字段为空 则使用showFields的第一个字段值作为标题
                        var f = fields[0];
                        return data[f.name] || data[f.alias];
                    }
                }
                return "";
            }
        },
        /**
         *
         * @param d
         * @param l
         * @returns {*}
         * @private
         */
        _getLinkUrl: function (d, f, l) {
            var tmp = {};
            arrayUtil.forEach(f, function (field) {
                var val = d[field.alias] || d[field.name];
                tmp[field.alias] = val;
                tmp[field.name] = val;
            });
            return esriLang.substitute(tmp, l.url ? l.url.matchEL() : l.url);
        },
        /**
         *
         * @param d
         * @param l
         * @returns {*}
         * @private
         */
        _getSecLinkUrl: function (d, f, l) {
            var tmp = {};
            arrayUtil.forEach(f, function (field) {
                var val = d[field.alias] || d[field.name];
                tmp[field.alias] = val;
                tmp[field.name] = val;
            });
            var array = [];
            if (l != null) {
                arrayUtil.forEach(l, function (a) {
                    var tmpMap = {};
                    tmpMap.tip = a.tip;
                    tmpMap.url = esriLang.substitute(tmp, a.url ? a.url.matchEL() : a.url);
                    array.push(tmpMap);
                });
            }
            return array;
        },
        /**
         * 移除esri自身的样式
         * @private
         */
        _resetDefaultStyle: function () {
            $.each($('.esriPopupWrapper .sizer'), function () {
                if (!$(this).hasClass('content')) $(this).remove();
            });
        }
    });

    /***
     * render tpl
     * @param tpl
     * @param data
     * @returns {*}
     * @private
     */
    function _renderTpl(tpl, data) {
        if (tpl != undefined) {
            var template = Handlebars.compile(tpl);
            return template(data);
        }
        return undefined;
    }

    me.getInstance = function () {
        if (instance === undefined) {
            instance = new me();
        }
        return instance;
    };
    return me;
});