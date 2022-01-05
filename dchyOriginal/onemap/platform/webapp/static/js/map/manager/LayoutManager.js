/**
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/11/24 19:36
 * File:    LayoutManager
 * (c) Copyright gtmap Corp.2015
 */
define(["dojo/_base/declare",
    "dojo/topic",
    "dojo/_base/lang",
    "./MapManager",
    "../core/support/ScreenHelper",
    "jqueryUI",
    "dojo/domReady!"], function (declare, topic, lang, MapManager, ScreenHelper) {

    var mapManager = MapManager.getInstance();
    var screen = ScreenHelper.getInstance();

    var V_MENU_WIDTH = 46;


    var leftPanelVisible = false, rightPanelVisible = false, offsetLeft = 446, offsetRight = 400;


    var topBarVisible = $(".nav-topbar").length > 0;

    var instance, me = declare(null, {
        constructor: function () {
            _calcOffsets();
            _addListeners();
        },
        /**
         * resize content
         *
         */
        resizeContent: function () {
            _resizeContent();
        },
        /**
         * open widget
         *
         * @param widgetId
         */
        openWidget: function (widgetId) {
            _openWidget(widgetId);
        },
        /**
         * open right panel
         */
        openRightPanel: function () {
            _openRightPanel();
        },
        /**
         * hide right panel
         */
        hideRightPanel: function () {
            _hideRightPanel();
        },
        /**
         * hide left panel
         */
        hideLeftPanel: function () {
            _hideLeftPanel();
        },
        /**
         * set offset of left or right
         */
        setOffset: function (offset) {
            if (typeof offset === "number") {
                offsetLeft = offset;
            } else if (typeof offset === "object") {
                if (offset.hasOwnProperty("left"))
                    offsetLeft = offset.left;
                if (offset.hasOwnProperty("right"))
                    offsetRight = offset.right;
            }
        }
    });

    /**
     * 打开右侧面板
     * @private
     */
    function _openRightPanel() {
        rightPanelVisible = true;
        $(".right_panel").css("display", "block");
        $(".right_panel").css("width", offsetRight + "px");
        //监听右侧面板关闭事件
        $(".history-close-btn").on('click', lang.hitch(this, _hideRightPanel));
        _resizeContent();
    }

    /**
     * 隐藏右侧面板
     * @private
     */
    function _hideRightPanel() {
        rightPanelVisible = false;
        $(".right_panel").css("display", "none");
        topic.publish('right-panel/hidden');
        _resizeContent();
    }

    /***
     * 隐藏左侧面板
     * @private
     */
    function _hideLeftPanel() {
        $(".main-operation").hide();
        $(".main-operation-handle").show();
        leftPanelVisible = false;
        _resizeContent();
    }

    /**
     * add event listener
     *
     * @private
     */
    function _addListeners() {
        addResultContainerListener();
        _widgetsLoadedListener();
        $(window).resize(function () {
            _resizeContent();
        });
    }

    /***
     * 根据屏幕尺寸计算左右面板的宽度
     * @private
     */
    function _calcOffsets() {
        var wl = screen.widthLevel();
        if (wl <= ScreenHelper.LOW) {
            offsetLeft = 346;
        } else if (wl <= ScreenHelper.MEDIUM) {
            offsetLeft = 386;
        } else{
            offsetLeft = 446;
        }
        //兼容马鞍山一张图
        if(typeof hideLeftMenu !="undefined"){
            V_MENU_WIDTH=0;
            offsetLeft=300;
            if (wl <= ScreenHelper.LOW) {
                offsetLeft = 306;
            } else if (wl <= ScreenHelper.MEDIUM) {
                offsetLeft = 326;
            } else{
                offsetLeft = 350;
            }
        }

    }

    /**
     * resize content
     *
     * @private
     */
    function _resizeContent() {
        /**
         * resize operation area
         */
        $("#verticalMenu").height($(window).height() - (topBarVisible ? 75 : 0));

        $(".popup-window").height($(window).height() - (topBarVisible ? 80 : 0));

        $(".popup-window").css('width', offsetLeft - V_MENU_WIDTH + 'px');

        // $(".material-nav").css('width', offsetLeft - V_MENU_WIDTH + 'px');

        $(".right_panel").height($(window).height() - (topBarVisible ? 80 : 0));

        $rc.css('left', (leftPanelVisible ? offsetLeft : 0) + 'px');
        $rc.width($(window).width() - (leftPanelVisible ? offsetLeft : 0) - (rightPanelVisible ? offsetRight : 0));
        try {
            _resizeMapContent(leftPanelVisible ? offsetLeft : 0, rightPanelVisible ? offsetRight : 0);
        }catch (e) {

        }
    }

    /**
     *  resize map content
     *
     * @param leftOffset
     * @private
     */
    function _resizeMapContent(leftOffset, rightOffset) {
        $mainMCDiv = $('#' + mainMapContentDiv);
        if (!topBarVisible) $mainMCDiv.css('top', '0px');

        $mainMCDiv.height($(window).height() - $mainMCDiv.offset().top);
        $mainMCDiv.width($(window).width() - (leftOffset || 0) - (rightOffset || 0));

        if (rightOffset > 0)
            $mainMCDiv.css("right", rightOffset + "px");
        else {
            $mainMCDiv.css("right", "0px");
        }
        mapManager.resizeMapContent();
        topic.publish(MapTopic.MAP_CONTRAST_RESIZE);
    }

    /**
     * all widgets listener
     */
    function _widgetsLoadedListener() {
        //
        $(".main-operation-handle").on("click", function () {
            $(".main-operation-handle").hide('slide', function () {
                leftPanelVisible = true;
                $(".main-operation").show(); 
                _resizeContent();
            });

        });

        $("#verticalMenu").on("click", ".optbtn", function () {
            var widgetId = $(this).attr("toggle-ac");
            _openWidget(widgetId);

        });

        $("#popupwin_area").on("click", ".popup-close-btn", function () {
            $(".main-operation").hide();
            leftPanelVisible = false;
            $(".main-operation-handle").show('slide', 500);
            _resizeContent();
        })

    }

    /**
     * open widget
     *
     * @param widgetId
     * @private
     */
    function _openWidget(widgetId) {
        leftPanelVisible = true;
        if ($("#panel-" + widgetId).is(":visible")) {
            return;
        }
        //过滤不需要加载页面的widget
        var widgetIds =["VideoMetadata","VideoWarning"];
        for(var i=0;i<widgetIds.length;i++){
            if(widgetId==widgetIds[i]){
                return;
            }
        }

        $(".optbtn").removeClass("optBtnActive");
        $(".group-widget-btn").removeClass("group-widget-btn-active");
        if ($("#btn_" + widgetId).hasClass("group-widget-btn")) {
            $("#btn_" + widgetId).addClass("group-widget-btn-active");
        } else {
            $("#btn_" + widgetId).addClass("optBtnActive");
        }
        $(".popup-window").hide();
        $("#panel-" + widgetId).show();
        _resizeContent();
    }

    /***
     *
     */
    function addResultContainerListener() {
        $rc = $('#result-container');
        $content = $rc.find('.content');
        $shrink = $rc.find('.shrink');
        $expand = $rc.find('.expand');

        $rc.on('click', 'a', function () {
            var type = $(this).closest('div').attr('class');
            switch (type) {
                case 'shrink':
                    shrinkResultContainer();
                    break;
                case 'expand':
                    expandResultContainer();
                    break;
                default:
                    return false;
            }
        });

        topic.subscribe("result/show", function (evt) {
            $rc.hide();
            $rc.show();
            $content.empty();
            if (evt.height !== undefined) {
                var targetH = Number(evt.height);
                $content.height(targetH);
                $content.css('overflow-y', 'auto');
                $content.css('max-height', targetH + 'px');
            }
            $content.append(evt.content);
            expandResultContainer();
        });

        // show with shrink state
        topic.subscribe("result/mini-show", function (evt) {
            $rc.hide();
            $rc.show();
            $content.empty();
            if (evt.height !== undefined) {
                var targetH = Number(evt.height);
                $content.css('display', 'none');
                $content.css('overflow-y', 'auto');
                $content.height(targetH);
                $content.css('max-height', targetH + 'px');
            }
            $content.append(evt.content);
            shrinkResultContainer();
        });

        topic.subscribe("result/shrink", function (evt) {
            shrinkResultContainer();
        });

        topic.subscribe("result/expand", function (evt) {
            expandResultContainer();
        });

        topic.subscribe("result/hide", function (evt) {
            $content.empty();
            $rc.hide();
        });
    }

    /**
     * 隐藏地图下方的结果面板
     */
    function shrinkResultContainer() {
        $shrink.fadeOut('fast');
        $content.slideUp(300, function callback() {
            $rc.addClass('shrinked');
            $expand.fadeIn('fast');
        });
    }

    /**
     * 显示地图下方的结果面板
     */
    function expandResultContainer() {
        $expand.hide();
        $rc.removeClass('shrinked');
        $content.slideDown(300, function callback() {
            $shrink.show();
        });
    }

    /**
     * get instance
     *
     * @returns {*}
     */
    me.getInstance = function () {
        if (instance === undefined) instance = new me();
        return instance;
    };

    return me;
});
