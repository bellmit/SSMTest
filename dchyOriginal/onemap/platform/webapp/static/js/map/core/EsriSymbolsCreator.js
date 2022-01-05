/**
 * esri symbols creator
 * 用于生成esri的简单样式以及复杂样式
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/6/13 19:48
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/lang",
    "dojo/_base/array",
    "dojo/json",
    "esri/symbols/PictureMarkerSymbol",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleLineSymbol",
    'esri/symbols/SimpleFillSymbol',
    "esri/symbols/TextSymbol",
    "esri/symbols/Font",
    "esri/Color"], function (lang, arrayUtil, Json, PictureMarkerSymbol, SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol, TextSymbol, Font, Color) {

    var STYLE_MARKER_CONSTANT = {
        STYLE_CIRCLE: "circle",
        STYLE_SQUARE: "square",
        STYLE_CROSS: "cross",
        STYLE_X: "x",
        STYLE_DIAMOND: "diamond",
        STYLE_TARGET: "target"
    };
    var STYLE_FILL_CONSTANT = {
        STYLE_SOLID: "solid",
        STYLE_NULL: "none",
        STYLE_HORIZONTAL: "horizontal",
        STYLE_VERTICAL: "vertical",
        STYLE_FORWARD_DIAGONAL: "forwarddiagonal",
        STYLE_BACKWARD_DIAGONAL: "backwarddiagonal",
        STYLE_CROSS: "cross",
        STYLE_DIAGONAL_CROSS: "diagonalcross",
        STYLE_FORWARDDIAGONAL: "forwarddiagonal",
        STYLE_BACKWARDDIAGONAL: "backwarddiagonal",
        STYLE_DIAGONALCROSS: "diagonalcross"
    };
    var STYLE_LINE_CONSTANT = {
        STYLE_SOLID: "solid",
        STYLE_DASH: "dash",
        STYLE_DOT: "dot",
        STYLE_DASHDOT: "dashdot",
        STYLE_DASHDOTDOT: "longdashdotdot",
        STYLE_NULL: "none",
        STYLE_SHORTDASH: "shortdash",
        STYLE_SHORTDOT: "shortdot",
        STYLE_SHORTDASHDOT: "shortdashdot",
        STYLE_SHORTDASHDOTDOT: "shortdashdotdot",
        STYLE_LONGDASH: "longdash",
        STYLE_LONGDASHDOT: "longdashdot"
    };

    /***
     * 线样式生成器
     */
    function lineSymbolCreator() {
        var lsc = {};
        lsc.createSymbol = function (style, color, width) {
            if (lang.isString(color) && color.startWith("#"))
                color = Color.fromHex(color);
            return new SimpleLineSymbol(style || STYLE_LINE_CONSTANT.STYLE_SOLID, color, width || 2);
        };
        return lsc;
    }

    /***
     * 面 样式生成器
     */
    function fillSymbolCreator() {
        var fsc = {};
        fsc.createSymbol = function (style, outline, color) {
            if (lang.isString(color) && color.startWith("#"))
                color = Color.fromHex(color);
            return new SimpleFillSymbol(style || STYLE_FILL_CONSTANT.STYLE_SOLID, outline, color);
        };
        return fsc;
    }

    /***
     * 点 样式生成器
     */
    function markerSymbolCreator() {
        var msc = {};
        msc.createSymbol = function (type, param) {
            var color = param.color;
            if (lang.isString(color) && color.startWith("#"))
                color = Color.fromHex(color);
            switch (type) {
                case 'simple':
                    return new SimpleMarkerSymbol(param.style || STYLE_MARKER_CONSTANT.STYLE_CIRCLE, param.size || 16, param.outline, color);
                    break;
                case 'svg':
                    var svgSymbol = new SimpleMarkerSymbol();
                    svgSymbol.setPath(param.path);
                    svgSymbol.setColor(color);
                    svgSymbol.setOutline(null);
                    svgSymbol.setSize(param.size || 24);
                    return svgSymbol;
                    break;
                case 'pic':
                    return new PictureMarkerSymbol(param.url, param.width || 24, param.height || 24);
                    break;
            }
        };
        return msc;
    }

    /***
     * 文本 样式生成器
     */
    function textSymbolCreator() {
        var tsc = {};

        function getFont(size, weight) {
            var font = new Font();
            font.setSize(size);
            font.setWeight(weight);
            font.setFamily('微软雅黑');
            return font;
        }

        tsc.createSymbol = function (text, color, fontSize, fontBold) {
            if (lang.isString(color) && color.startWith("#"))
                color = Color.fromHex(color);
            fontSize = fontSize === undefined ? 12:fontSize;
            var txtSymbol = new TextSymbol();
            txtSymbol.setText(text);
            txtSymbol.setVerticalAlignment("bottom");
            txtSymbol.setHorizontalAlignment("center");
            txtSymbol.setColor(color);
            txtSymbol.setFont(getFont(fontSize, fontBold == true ? Font.WEIGHT_BOLD : Font.STYLE_NORMAL));
            return txtSymbol;
        };
        return tsc;
    }

    return {
        /***
         * refer to esri simple marker symbol
         * @param style
         * @param size
         * @param outline  simple line symbol
         * @param color
         */
        createSimpleMarkerSymbol: function (style, size, outline, color) {
            return markerSymbolCreator().createSymbol('simple', {
                style: style,
                size: size,
                outline: outline,
                color: color
            });
        },
        /***
         * create svg symbol
         * @param path
         * @param size
         * @param color
         * @returns {*}
         */
        createSvgMarkerSymbol: function (path, size, color) {
            if (!lang.isString(path)) {
                console.error("svg图形路径不能为空!");
                return null;
            }
            return markerSymbolCreator().createSymbol('svg', {path: path, size: size, color: color});
        },
        /***
         * create picture symbol
         * @param url
         * @param width
         * @param height
         * @returns {*}
         */
        createPicMarkerSymbol: function (url, width, height) {
            if (!lang.isString(url)) {
                console.error("图片资源地址不能为空!");
                return null;
            }
            return markerSymbolCreator().createSymbol('pic', {url: url, width: width, height: height});
        },
        /***
         * create line symbol
         * @param style
         * @param color
         * @param width
         */
        createSimpleLineSymbol: function (style, color, width) {
            return lineSymbolCreator().createSymbol(style, color, width);
        },
        /***
         * create simple fill symbol
         * @param style
         * @param outline
         * @param color
         */
        createSimpleFillSymbol: function (style, outline, color) {
            return fillSymbolCreator().createSymbol(style, outline, color);
        },
        /***
         * create text symbol
         * @param text
         * @param color
         * @param fontSize
         * @param fontBold
         * @returns {*}
         */
        createTextSymbol: function (text, color, fontSize, fontBold) {
            return textSymbolCreator().createSymbol(text, color, fontSize, fontBold);
        },
        /***
         * convert color from hex
         * @param hex
         * @returns {*}
         */
        colorFromHex: function (hex) {
            return Color.fromHex(hex);
        },
        defaultMarkerSymbol: new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 24, null, new Color([51, 136, 255, 1])),
        defaultLineSymbol: new SimpleLineSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new Color([51, 136, 255, 1]), 2),
        //defaultFillSymbol: new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new Color([51, 136, 255, 1]), 2), new Color([51, 136, 255, 0.2])),
        defaultFillSymbol: new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new Color([	255,0,0, 1]), 2), new Color([255,0,0, 0.2])),
        defaultLocateFillSymbol: new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_LINE_CONSTANT.STYLE_DASH, new Color([	255,0,0, 1]), 2), new Color([255,255,0, 1])),
        ghLocateFillSymbol: new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_LINE_CONSTANT.STYLE_DASH, new Color([	255,0,0, 1]), 2), new Color([0,255,0, 1])),
        defaultNoColorSymbol:new SimpleFillSymbol(STYLE_FILL_CONSTANT.STYLE_SOLID, new SimpleLineSymbol(STYLE_LINE_CONSTANT.STYLE_DASH, new Color([	255,0,0, 1]), 4), new Color([255,255,0, 1])),
        fillStyle: STYLE_FILL_CONSTANT,
        lineStyle: STYLE_LINE_CONSTANT,
        markerStyle: STYLE_MARKER_CONSTANT

    };
});