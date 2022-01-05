/*!
 * 地图采集功能（常州订制）
 * - 采集点/线/面
 * - 进行属性识别（图层配置）
 * - post 到配置的 url
 * widget config:
 * {
 *   "drawTypes": "point,polygon",
 *   "postUrl": "http://xxx",
 *   "layers": [{"layerUrl": "", "fields": "XZQDM,DKMC,PRONAME", "enable": true}]
 * }
 * Author: yingxiufeng
 * Date:   2018/3/7
 * Version:v1.0 (c) Copyright gtmap Corp.2018
 */
define(["dojo/_base/declare",
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/on",
    "esri/toolbars/draw",
    "esri/graphic",
    "esri/lang",
    "map/core/QueryTask",
    "esri/tasks/FeatureSet",
    "map/utils/MapUtils",
    "map/component/MapPopup",
    "map/core/JsonConverters",
    "map/core/EsriSymbolsCreator",
    "layer",
    "handlebars",
    "hbarsUtils",
    "map/component/ListDataRenderer",
    "esri/symbols/SimpleMarkerSymbol",
    "static/js/cfg/core/SerializeForm",
    "esri/Color",
    "map/core/BaseWidget",
    "text!map/template/leas/draw-geo-form.html",
    "ztree"
], function (declare, arrayUtil, lang, on, Draw, Graphic, esriLang, QueryTask, FeatureSet, MapUtils, MapPopup,
              JsonConverters, EsriSymbolsCreator, layer,Handlebars, HbarsUtils, ListDataRenderer, SimpleMarkerSymbol,SerializeForm, Color,
              BaseWidget,formTpl) {

    var _map, _config;
    var mapPopup = MapPopup.getInstance();

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
            _map = this.getMap().map();
            _config = this.getConfig();
            _init();

            getOrgans();
        },
        onOpen: function () {

        },
        onPause: function () {
            if (mapPopup.isShowing) mapPopup.closePopup();
            _map.graphics.clear();
        },
        onDestroy: function () {
            if (mapPopup.isShowing) mapPopup.closePopup();
        }

    });
    var drawTool, drawHandler;
    var returnGeometry;
    var returnFieldsData = [];
    var $collectResultPanel, $collectDrawPanel, $collectOptPanel;

    var STYLE_MARKER_CONSTANT = {
        STYLE_CIRCLE: "circle",
        STYLE_SQUARE: "square",
        STYLE_CROSS: "cross",
        STYLE_X: "x",
        STYLE_DIAMOND: "diamond",
        STYLE_TARGET: "target"
    };

    // 可提供的绘制类型
    var drawTypes = [{
        name: 'point',
        alias: '绘点'
    }, {
        name: 'polyline',
        alias: '绘线'
    }, {
        name: 'polygon',
        alias: '绘面'
    }];

    var drawTypeTpl = '<div class="col-sm-{{colLen}} col-xs-{{colLen}} text-center">' +
        '<div class="{{name}}-choose spatial-geo-icon" data-type="{{name}}" title="{{alias}}" style="height: 47px;width: 47px"></div></div>';


    var userId = '';
    //存放部门数据
    var organData = [];

    var PROXY_URL = root + "/map/proxy";

    var DIKUAI_POST_URL = leasUrl + "/map/inspect/gps/dynamic/plan";
    var GPS_LANDRES_LIST_URL = leasUrl + "/map/inspect/gps/landresourcelist";
    //部门列表地址
    var LS_ORGANS_URL = leasUrl + "/map/inspect/gps/organs";

    /**
     * 初始化参数等
     * @private
     */
    function _init() {
        if (urlParams != undefined && urlParams.hasOwnProperty("userId")) {
            userId = urlParams.userId;
        }

        layer.config();

        Date.prototype.toLocaleString = function () {
            return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日 ";
        };
        //初始化jq变量
        $collectDrawPanel = $("#collectDrawPanel");
        $collectResultPanel = $("#collectResultPanel");
        $collectOptPanel = $("#collectOptPanel");
        //根据配置初始化界面绘制类型
        var _drawTypes = _config.drawTypes;
        arrayUtil.forEach(drawTypes, function (item) {
            if (_drawTypes != undefined && _drawTypes.indexOf(item.name) > -1) {
                // render
                $collectDrawPanel.append(HbarsUtils.renderTpl(drawTypeTpl, lang.mixin(item, {colLen: 12 / (_drawTypes.split(',').length)})));
            }
        });
        // 增加按钮监听事件
        $collectDrawPanel.find('.spatial-geo-icon').on('click', function () {
            spatialClickHandler($(this).data("type"));
        });
        //取消按钮点击事件
        $collectOptPanel.find('.btn-default').on('click', function () {
            clear();
            $collectResultPanel.empty();
            returnFieldsData = [];
        });
        //提交按钮点击事件
        $collectOptPanel.find('.btn-primary').on('click', function () {
            submitData();
        });
    }

    function getOrgans() {
        organData = [];
        /*//var data = [{"children":[{"children":[{"children":[],"id":"A20AE9965B1E41AFB1F618805B3F2725","isLeaf":true,"parentId":"9D70BFC156BA4BD4BCD100A21EC3ADC9","text":"征地中心一科"},{"children":[],"id":"3AA8B99838FA4293960A0E0CA3F7F8F1","isLeaf":true,"parentId":"9D70BFC156BA4BD4BCD100A21EC3ADC9","text":"征地中心二科"}],"id":"9D70BFC156BA4BD4BCD100A21EC3ADC9","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"征地中心"},{"children":[{"children":[],"id":"FF4D18D506B940328A79D78EA44BA4D3","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局湟里所"},{"children":[],"id":"5BB916705AA2481E9C0A4695FA4E5148","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局礼嘉所"},{"children":[],"id":"3B42EAF4121E44599A1BCD20D069D5C5","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局洛阳所"},{"children":[],"id":"728F14D82D28449D86ECA3BDCE0E6DBA","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局监察大队"},{"children":[],"id":"7D8E07093F934C329CA5571A90E19CF0","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局法规科"},{"children":[],"id":"76D757C74C7D4A6D9FCAC38C96183642","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局经发区所"},{"children":[],"id":"C1FD1A7D60BB49259BB03F40E7A160F5","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局湖塘所"},{"children":[],"id":"F95AC63457A24C29B96881D32DCDE577","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局雪堰所"},{"children":[],"id":"7BEE27E27ACE47C39915CF6BD8A0702D","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局嘉泽所"},{"children":[],"id":"1DCE75A1351B43B7A3EA79F168B38BD8","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局局领导"},{"children":[],"id":"2B57C817ECC74671B880F338ABBEE754","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局牛塘所"},{"children":[],"id":"3043C95F151A491B83F2B7648C418205","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局高新区所"},{"children":[],"id":"58B60797B7734634BEC4726FD9AB9BC5","isLeaf":true,"parentId":"83A3984ACFF347C0967FD3BBDE2A1DCB","text":"武进分局前黄所"}],"id":"83A3984ACFF347C0967FD3BBDE2A1DCB","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"武进分局"},{"children":[{"children":[],"id":"9CCFF7A236074D598D396A86E4D7C7C9","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局经发所"},{"children":[],"id":"21966268E0B748C790CB807A4D27B134","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局朱林所"},{"children":[],"id":"381A321C81CB4414BB420E145EA3D547","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局薛埠所"},{"children":[],"id":"D80ECEA80AA84EE8806B9E976AF628E3","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局监察大队"},{"children":[],"id":"269D231411E14373921B821F60E73160","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局指前所"},{"children":[],"id":"0E1414321A0B45D68881B18FB9787970","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局直溪所"},{"children":[],"id":"C12C9CE1E1004E9BBB391E72CDF3BC95","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局金城所"},{"children":[],"id":"8E4159E52FEA49A3977DF3318E2609E9","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局局领导"},{"children":[],"id":"7CCE1FB2275D4D10A861D04726E3AEC0","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局儒林所"},{"children":[],"id":"65B9644FFC3C457B8088CC226112F575","isLeaf":true,"parentId":"8D13B0E43CC344DF927E56D1A43C2467","text":"金坛分局法规科"}],"id":"8D13B0E43CC344DF927E56D1A43C2467","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"金坛分局"},{"children":[{"children":[],"id":"55C82212C248409D9F0E4FE165706BD2","isLeaf":true,"parentId":"651A948EB746417A95D0885E9F258D02","text":"信息中心测试"}],"id":"651A948EB746417A95D0885E9F258D02","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"信息中心"},{"children":[],"id":"24E7183DA128481DB7CA27751DCB20D9","isLeaf":true,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"法规监察处"},{"children":[],"id":"58DF2365E1E84B8D9915D09362E4C6EC","isLeaf":true,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"市局领导"},{"children":[{"children":[],"id":"2211EA22966B44D188C2F78B1F85FEB2","isLeaf":true,"parentId":"7ED5BE790B5046A89FFBD0D099F71C12","text":"国土资源监察支队监察三科"},{"children":[],"id":"9676F626F1134BC5AEB7A7357C3861C3","isLeaf":true,"parentId":"7ED5BE790B5046A89FFBD0D099F71C12","text":"国土资源监察支队监察一科"},{"children":[],"id":"DE9E0FC4F6D1474890791A9099892986","isLeaf":true,"parentId":"7ED5BE790B5046A89FFBD0D099F71C12","text":"国土资源监察支队监察二科"},{"children":[],"id":"B523CD8DA112451EB022BBD3C26AC968","isLeaf":true,"parentId":"7ED5BE790B5046A89FFBD0D099F71C12","text":"国土资源监察支队综合科"}],"id":"7ED5BE790B5046A89FFBD0D099F71C12","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"国土资源监察支队"},{"children":[{"children":[],"id":"5CF54A558CDE40FD88A7BE3628D32B4F","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局利用科"},{"children":[],"id":"33386401276E45BBA81E2BB25A87B759","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局经发区中心国土资源所"},{"children":[],"id":"C1B633F435B544B98B5B658375B3AEC6","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局法规科"},{"children":[],"id":"6628099D059C47A7B2D0D0361DAF846D","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局红梅中心国土资源所"},{"children":[],"id":"DC796D7399634E46A83C5BAC71CAC519","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局局领导"},{"children":[],"id":"EAA00920DCE54864889D3CC2EFDA0F56","isLeaf":true,"parentId":"FD260F02EBEA4964A9BB6BC35498D4DD","text":"天宁分局郑陆所"}],"id":"FD260F02EBEA4964A9BB6BC35498D4DD","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"天宁分局"},{"children":[{"children":[],"id":"162425F58C654DB4A6DDABF1EA273A2A","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局戴埠国土所"},{"children":[],"id":"C74FAF001BD542A79C7B0BF268D82F70","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局监察大队"},{"children":[],"id":"4C57318D708843D99D79F29A37BBBEBA","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局上兴国土所"},{"children":[],"id":"048119F6F80F49A79AAFF5682D530446","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局竹箦国土所"},{"children":[],"id":"8A8D5688039642EB9C5EF738DDEFAC3F","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局局领导"},{"children":[],"id":"CE4F849B3B1947E999EBB715EF04D83E","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局社渚国土所"},{"children":[],"id":"E0E4090625764FDD8733C559B5209CFD","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局天目湖国土所"},{"children":[],"id":"6CDD3ECF06CA47A0A27FA80669F1EE03","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局上黄国土所"},{"children":[],"id":"7EF3A0CF8BF8486C91DFF3C8FCF65AA3","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局溧城镇国土所"},{"children":[],"id":"664F5A34134D4E98A3B4F88F35839CFB","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局埭头国土所"},{"children":[],"id":"944C5682A9E74AAAA9E815BC85763671","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局别桥国土所"},{"children":[],"id":"7E1708B0B85B4338A0CB826DBB57F3A5","isLeaf":true,"parentId":"DF3DF8D935C843918575BFF4E1F1B50D","text":"溧阳市局南渡国土所"}],"id":"DF3DF8D935C843918575BFF4E1F1B50D","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"溧阳市局"},{"children":[{"children":[],"id":"461A2D5775344114BA47B2AF4344D054","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局监察大队"},{"children":[],"id":"756544BF3284435D9BF557D9532AD66D","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局监察大队二中队"},{"children":[],"id":"C770BD51D4334EDF8366B5956B443E7D","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局奔牛所"},{"children":[],"id":"59E3E993F9C54703BF1507D381E9A512","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局龙虎塘所"},{"children":[],"id":"7AE1C63B2D9642C7A1A2C135938DA6DA","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局罗溪所"},{"children":[],"id":"74DC4C623F6B4C328753097D82DD1DF6","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局孟河所"},{"children":[],"id":"8EB6F3A4E222484C8E76A973E34CC42A","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局监察大队一中队"},{"children":[],"id":"3374A52B0AF14AD28FEDA9586DFA0F5F","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局薛家所"},{"children":[],"id":"069AEC9D51384C12B0DD2429A988B3C0","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局春江所"},{"children":[],"id":"31885034516249ECB927CE836E57824F","isLeaf":true,"parentId":"2E71C2A219DB481F80F27B086A61863F","text":"新北分局局领导"}],"id":"2E71C2A219DB481F80F27B086A61863F","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"新北分局"},{"children":[],"id":"F5D33CA2CDDD4EEBB66D2D0B37B31F72","isLeaf":true,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"纪委监察室"},{"children":[],"id":"E967D1B05ED24397A4FBA1FE9A2B624A","isLeaf":true,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"利用处"},{"children":[{"children":[],"id":"CC17D072E9DB477C85C004148207672C","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局监察大队"},{"children":[],"id":"01318EF8272046A1A65173F0A9ABD84F","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局法规科"},{"children":[],"id":"2D407788E56247C6B4580A5A9197AD95","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局遥观所"},{"children":[],"id":"BAD4DFAC2DE147F1814760A50DAB9096","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局局领导"},{"children":[],"id":"7BD3DA845A6C433B88F454FB44B9CD78","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰区经发中心国土所"},{"children":[],"id":"23D32688EA6B44D7BFB90F4F735D7593","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局横林所"},{"children":[],"id":"19F7392973964D0BB1E4E12B052EB1E5","isLeaf":true,"parentId":"D61D0405A0D74BCEBA31CB599C83D6DE","text":"戚墅堰分局横山桥所"}],"id":"D61D0405A0D74BCEBA31CB599C83D6DE","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"戚墅堰分局"},{"children":[{"children":[],"id":"EFBF639AE037413BA9010E2F3F1F8F86","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局开发区中心所"},{"children":[],"id":"1118E87C622340E98F1294A81E760C89","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局西林中心所"},{"children":[],"id":"1AEEC2C712174876959E27E6AAC9582A","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局邹区所"},{"children":[],"id":"5A80580DD72C4981B755F4F1F206091F","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局局领导"},{"children":[],"id":"5B46D915544046F09696FC1123186020","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局新闸中心所"},{"children":[],"id":"EB39BE842CE2488F99501B3E23A1013B","isLeaf":true,"parentId":"B524B044C0F64B1589EF9C1337B07BEB","text":"钟楼分局法规科"}],"id":"B524B044C0F64B1589EF9C1337B07BEB","isLeaf":false,"parentId":"1209E3AFE3134DE5BA6171BA57E06DDC","text":"钟楼分局"}],"id":"1209E3AFE3134DE5BA6171BA57E06DDC","isLeaf":false,"text":"常州市国土资源局"}];

         arrayUtil.forEach(data, function (item) {
         organData.push(item);
         });*/

        $.getJSON(PROXY_URL, {requestUrl: LS_ORGANS_URL, dataType: 'json'}, function (data) {
            arrayUtil.forEach(data, function (item) {
                organData.push(item);
            });
        });
    }

    /**
     * 提交数据
     */
    function submitData() {
        // var returnData = [];
        // returnData.push(returnFieldsData);
        // returnData.push(JsonConverters.toGeoJson(returnGeometry));

        var checkValues = [];

        $('#collectResultPanel input:checked').each(function (index, el) {
            var value = $(el).val();
            value = value.replace('图幅号:', '');
            checkValues.push({
                MAP: value
            });
        });

        if (checkValues.length == 0) {
            checkValues = returnFieldsData;
        }

        var postData = {
            geo: {type: 'Feature', geometry: JsonConverters.toGeoJson(returnGeometry)},
            attrs: checkValues,
            proid: getUrlParams().proid
        };
        $.ajax({
            url: _config.postUrl
            , data: {postData: JSON.stringify(postData)}
            , success: function (r) {
                if (r) {
                    layer.msg("提交成功！");
                } else {
                    layer.msg("提交失败！");
                }
            }
        });
    }

    /**
     * 空间查询Handler
     * @param type
     */
    function spatialClickHandler(type) {
        if (drawTool != undefined) drawTool.deactivate();
        if (drawHandler != undefined) drawHandler.remove();
        drawTool = drawTool ? drawTool : new Draw(_map);
        drawHandler = on(drawTool, "draw-end", lang.hitch(this, onDrawEnd));
        highLight(type);
        switch (type) {
            case "point":
                drawTool.activate(Draw.POINT);
                break;
            case "polyline":
                drawTool.activate(Draw.POLYLINE);
                break;
            case "polygon":
                drawTool.activate(Draw.POLYGON);
                break;
            default:
                console.error(type + "is unsupported yet!");
                break;
        }
    }

    /**
     * 高亮要素
     * @param type
     */
    function highLight(type) {
        $collectDrawPanel.find('.spatial-geo-icon').removeClass("highLight");
        $collectDrawPanel.find("." + type + "-choose").addClass("highLight");
    }

    /**
     * 执行空间查询
     * @param evt
     */
    function onDrawEnd(evt) {
        drawHandler.remove();
        drawTool.deactivate();
        var geo = evt.geometry;
        returnGeometry = evt.geometry;
        // 显示绘制图形
        _map.graphics.add(new Graphic(evt.geometry, getSymbol(geo.type)));
        // 获取配置图层
        var layers = _config.layers;
        var agsGeoStr = JSON.stringify(geo);

        var templ = Handlebars.compile(formTpl);
        var contentTxt ="";
        if(organData.length>0){
            contentTxt = organData[0].text;
        }

        var content = templ({ userId: userId,geoString: agsGeoStr});
        layer.open({
            type: 1,
            title: ['<i class="fa fa-leaf"></i>&nbsp;图形指派', 'background-color:#428bca;color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
            area: '600px',
            shade: 0,
            content: content,
            cancel: function () {
                layer.closeAll();
            },
            success: function () {
                $.getJSON(PROXY_URL, {requestUrl: GPS_LANDRES_LIST_URL, dataType: 'json'}, function (data) {
                    if (data.success === true) {
                        var arr = data.result;
                        if (lang.isArray(arr)) {
                            arrayUtil.forEach(arr, function (item) {
                                $("#landResource").append("<option value=" + item + ">" + item + "</option>");
                            });
                        }
                    } else {
                        layer.msg(data.msg, {icon: 0});
                    }
                });
                //巡查单位下拉树
                if (organData.length > 0) {
                    var ztreeData = organData;
                    $("#userRoleId").on("click", function () {
                        $(".appoint-ztree-container").remove();
                        addAppointTree();
                    });
                }

                $("#appointForm").Validform({
                    tiptype: 3,
                    label: ".label",
                    showAllError: true,
                    ajaxPost: true,
                    callback: function () {
                        var data = SerializeForm.serializeObject($("#appointForm"));
                        console.log(data);
                        var msgHandle = layer.msg('提交数据中...', {time: 6000});
                        $.ajax({
                            url: DIKUAI_POST_URL,
                            method: "post",
                            data: data,
                            success: function (rp) {
                                layer.close(msgHandle);
                                if (rp.success == true) {
                                    layer.msg('操作成功!', {icon: 1}, function () {
                                        layer.closeAll();
                                        MapUtils.clearMapGraphics();
                                    });
                                } else
                                    layer.msg("提交失败! " + rp.result, {icon: 0});
                            }
                        });
                        return false;
                    }
                });
            }
        });
    }

    function addAppointTree(){
        var appointTpl = $("#appointDepartTpl").html();
        $("#appointForm").append(appointTpl);
        var treeHeight = 145;
        $(".appoint-ztree-container").height(treeHeight);
        var setting = {
            data: {
                key: {
                    checked: "checked",
                    name: "text",
                    url: "nourl"
                }
            },
            callback: {
                onClick: appointOnClick
            }
        }
        var treeData = [];
        if (organData.length == 1) {
            var ch = {};
            ch.children = [];
            ch.id = "all";
            ch.isLeaf = "false";
            ch.parentId = "0"
            ch.text = "所有";
            treeData.push(ch);
            arrayUtil.forEach(organData[0].children, function (item) {
                treeData.push(item);
            });
        } else {
            treeData = organData;
        }
        $.fn.zTree.init($("#appointTree"), setting, treeData);
        appointTreeObj = $.fn.zTree.getZTreeObj("appointTree");
        if($(".Validform_wrong").val()!=undefined||$(".Validform_wrong").html()!=undefined){
            $(".appoint-ztree-container ").css("top","108px");
        }

        $("#appointTree").slimScroll({
            height: treeHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });
    }

    function appointOnClick(event, treeId, treeNode) {
        var sId = treeNode.id;
		$("#colZpZpbmid").val(sId);
        $("#userRoleId").val(treeNode.text);
        $(".appoint-ztree-container").remove();
        $("#userRoleId").parent().children(".Validform_wrong").html("");
    }

    /**
     * 解析空间查询结果
     * @param r
     * @param data
     */
    function parseQueryResult(r) {
        {
            var attrs = [];//存放要进行展示的属性集合
            var layerObj = null; //选定的图层对象
            arrayUtil.forEach(_config.layers, function (layerItem, n) {
                layerObj = layerItem;
                //一级标题
                var tf = layerItem.titleField;
                //二级标题
                var sf = getSubTitleField(layerItem);
                var rf = layerItem.returnFields;
                var graphics = [];
                arrayUtil.forEach(r.features, function (feature, i) {
                    //返回属性数据
                    returnFieldsData.push(feature.attributes);
                    arrayUtil.forEach(rf, function (r) {
                        if (r.type == "DATA" && feature.attributes[r.name]) {
                            feature.attributes[r.name] = new Date(feature.attributes[r.name]).toLocaleString();
                        }
                    });
                    var titleText = esriLang.substitute({
                        alias: tf.alias
                        , value: feature.attributes[tf.name] || '空'
                    }, '${alias}:${value}');
                    var subTitleText = esriLang.isDefined(sf) ? esriLang.substitute({
                        alias: sf.alias
                        , value: feature.attributes[sf.name] || '空'
                    }, '${alias}:${value}') : '';
                    var g = r.features[i];
                    var geo = g.geometry;
                    if(typeof(geo) != 'undefined'){
                        if (!geo.spatialReference) {
                            geo.spatialReference = _map.spatialReference;
                        }
                    }
                    var graphic = new Graphic(g);
                    graphics.push(graphic);
                    attrs.push({
                        title: titleText
                        , subtitle: subTitleText
                        , graphic: graphic
                    });
                });
            });
            //渲染模板显示结果
            // console.log(attrs);
            var listDataRenderer = new ListDataRenderer({
                renderTo: $('#collectResultPanel'),
                type: "checkbox",
                map: _map,
                renderData: attrs
            });
            listDataRenderer.on('location', function (data) {
                doLocation(data.graphic);
            });

            listDataRenderer.render();

        }
    }

    /***
     * 定位图形
     * @param g
     */
    function doLocation(g) {
        var geo = g.geometry;
        var geoType = geo.type;
        var geoCenter;
        switch (geoType) {
            case 'point':
                geoCenter = geo;
                break;
            case 'polyline':
            case 'polygon':
                geoCenter = geo.getExtent().getCenter();
                break;
        }
        geoCenter = lang.mixin(geoCenter, {spatialReference: _map.spatialReference});
        var graphic = new Graphic(lang.mixin(geo, {spatialReference: _map.spatialReference}));
        graphic.setAttributes(g.attributes);
        MapUtils.setMap(_map);
        MapUtils.highlightFeatures([graphic], false, 1.0);

    }

    /**
     * get symbol by geo type
     * @param type
     * @returns {*}
     */
    function getSymbol(type) {
        switch (type) {
            case 'point':
                return new SimpleMarkerSymbol(STYLE_MARKER_CONSTANT.STYLE_CIRCLE, 12, null, new Color([255, 0, 0, 1]));
                break;
            case 'polyline':
                return EsriSymbolsCreator.defaultLineSymbol;
                break;
            default:
                return EsriSymbolsCreator.defaultFillSymbol;
        }
    }


    /**
     * 设置二级标题字段
     * 非一级标题字段
     * @param lyr
     * @returns {undefined}
     */
    function getSubTitleField(lyr) {
        var tf = lyr.titleField;
        var rf = lyr.returnFields;
        arrayUtil.forEach(rf, function (item) {
            if (esriLang.isDefined(item) && item.name != tf.name) {
                return item;
            }
        });
        return undefined;
    }

    /**
     * 清除
     */
    function clear() {
        _map.graphics.clear();
    }

    return me;
});