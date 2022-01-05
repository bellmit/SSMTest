L.Control.LayerControl = L.Control.extend({

    options: {
        position: 'topright',
    },

    initialize: function (layers) {
        this._layers = [];
        this._handlingClick = false;
        for (let i = 0; i < layers.length; i++) {
            this._addLayer(layers[i]);
        }
    },

    onAdd: function (map) {
        this.__map = map;
        this._initLayout();
        this._update();

        map.on('zoomend', this._checkDisabledLayers, this);

        return this._container;
    },

    onRemove: function () {
        // 取消绑定的事件
        this.__map.off('zoomend', this._checkDisabledLayers, this);
        for (var i = 0; i < this._layers.length; i++) {
            this._layers[i].layer.off('add remove', this._onLayerChange, this);
        }
    },

    expand: function () {
        L.DomUtil.addClass(this._container, 'leaflet-control-layer-control-expanded');
        this._form.style.height = null;
        var acceptableHeight = this.__map.getSize().y - (this._container.offsetTop + 50);
        if (acceptableHeight < this._form.clientHeight) {
            L.DomUtil.addClass(this._form, 'leaflet-control-layer-control-scrollbar');
            this._form.style.height = acceptableHeight + 'px';
        } else {
            L.DomUtil.removeClass(this._form, 'leaflet-control-layer-control-scrollbar');
        }
        this._checkDisabledLayers();
        return this;
    },

    collapse: function () {
        L.DomUtil.removeClass(this._container, 'leaflet-control-layer-control-expanded');
        return this;
    },

    _initLayout: function () {
        // create container
        var className = 'leaflet-control-layer-control',
            container = this._container = L.DomUtil.create('div', className);

        container.setAttribute('aria-haspopup', true);

        L.DomEvent.disableClickPropagation(container);
        if (!L.Browser.touch) {
            L.DomEvent.disableScrollPropagation(container);
        }

        // create form
        var form = this._form = L.DomUtil.create('form', className + '-list');

        this.__map.on('click', this.collapse, this);

        if (!L.Browser.android) {
            L.DomEvent.on(container, {
                mouseenter: this.expand,
                mouseleave: this.collapse
            }, this);
        }

        var link = this._layersLink = L.DomUtil.create('a', className + '-toggle', container);
        link.href = '#';
        link.title = '图层控制';

        if (L.Browser.touch) {
            L.DomEvent
                .on(link, 'click', L.DomEvent.stop)
                .on(link, 'click', this.expand, this);
        } else {
            L.DomEvent.on(link, 'focus', this.expand, this);
        }

        L.DomEvent.on(form, 'click', function () {
            setTimeout(L.bind(this._onInputClick, this), 0);
        }, this);

        this._layerList = L.DomUtil.create('div', className + '-layers', form);

        container.appendChild(form);
    },

    _getLayer: function (layerUrl, layerId) {
        let layers = this._layers;
        for (let i = 0; i < layers.length; i++) {
            if (layers[i] && layers[i].layerId == layerId && layers[i].layerUrl == layerUrl) {
                return layers[i];
            }
        }
    },

    _getLayerIds: function (url) {
        let ids = [];
        let layers = this._layers;
        for (let i = 0; i < layers.length; i++) {
            if (layers[i] && layers[i].layerUrl == url) {
                ids.push(layers[i].layerId);
            }
        }
        return ids;
    },

    _addLayer: function (layerInfo) {
        let layer = layerInfo.layer;
        let subLayers = layerInfo.subLayers;
        let layers = this._layers;
        layer.on('add remove', this._onLayerChange, this);
        if (subLayers) {
            subLayers.forEach(function (item) {
                layers.push({
                    layer: layer,
                    layerUrl: layer.options.url,
                    layerId: item.layerId,
                    name: item.layerName,
                });
            })
        }
    },

    _update: function () {
        if (!this._container) {
            return this;
        }

        L.DomUtil.empty(this._layerList);   // 清空图层列表

        let obj;
        let layers = this._layers;
        for (let i = 0, len = layers.length; i < len; i++) {
            obj = layers[i];
            this._addItem(obj); // 添加项
        }

        return this;
    },

    _onLayerChange: function (e) {
        if (!this._handlingClick) {
            this._update();
        }
    },

    _addItem: function (obj) {
        var label = document.createElement('label'),
            layerId = obj.layerId,
            input;

        var checked = true;
        var layerids = obj.layer.getLayers();
        if (layerids && layerids.indexOf(layerId) < 0) {
            checked = false;        // 不可见
        }
        input = document.createElement('input');
        input.type = 'checkbox';
        input.className = 'leaflet-control-layer-control-selector';
        input.defaultChecked = checked;
        input.layerUrl = obj.layer.options.url;
        input.layerId = layerId;

        L.DomEvent.on(input, 'click', this._onInputClick, this);

        var name = document.createElement('span');
        name.innerHTML = ' ' + obj.name;

        var holder = document.createElement('div');

        label.appendChild(holder);
        holder.appendChild(input);
        holder.appendChild(name);

        this._layerList.appendChild(label);

        this._checkDisabledLayers();
        return label;
    },

    _onInputClick: function () {
        let inputs = this._form.getElementsByTagName('input'),
            input, layer, layerUrl, layerId, layerInfo;
        // 勾选或取消勾选
        this._handlingClick = true;
        let checkedLayer = {};
        for (let i = inputs.length - 1; i >= 0; i--) {
            input = inputs[i];
            layerUrl = input.layerUrl;
            layerId = input.layerId;
            layerInfo = this._getLayer(layerUrl, layerId);
            layer = layerInfo.layer;

            if (!checkedLayer.hasOwnProperty(layerUrl)) {
                checkedLayer[layerUrl] = {};
                checkedLayer[layerUrl].layer = layer;
                checkedLayer[layerUrl].checkedIds = [];
                checkedLayer[layerUrl].uncheckedIds = [];
            }
            if (input.checked) {
                checkedLayer[layerUrl].checkedIds.push(layerId);
            } else {
                checkedLayer[layerUrl].uncheckedIds.push(layerId);
            }
        }
        for (let j in checkedLayer) {
            checkedLayer[j].layer.setLayers(checkedLayer[j].checkedIds);
        }
        this._handlingClick = false;

        this._refocusOnMap();
    },

    _checkDisabledLayers: function () {
        var inputs = this._form.getElementsByTagName('input'),
            input, layerInfo, layer, layerUrl, layerId,
            zoom = this.__map.getZoom();

        // 设置每一项是否可用
        for (var i = inputs.length - 1; i >= 0; i--) {
            input = inputs[i];
            layerUrl = input.layerUrl;
            layerId = input.layerId;
            layerInfo = this._getLayer(layerUrl, layerId);
            layer = layerInfo.layer;
            input.disabled = (layer.options.minZoom !== undefined && zoom < layer.options.minZoom) ||
                (layer.options.maxZoom !== undefined && zoom > layer.options.maxZoom);
        }
    },

    _expand: function () {
        return this.expand();
    },

    _collapse: function () {
        return this.collapse();
    }

});

// 添加 map 初始化的钩子方法
L.Map.addInitHook(function () {
    if (this.options.layerControlControl) {
        this.layerControlControl = L.control.addError();
        this.addControl(this.layerControlControl);
    }
});

L.control.layerControl = function (layers, options) {
    return new L.Control.LayerControl(layers, options);
};