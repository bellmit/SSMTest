/**
 * 动态巡查系统
 * 地图实时监控模块
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/5/24 22:39
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */
define(["dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/_base/array",
    "dojo/on",
    "map/core/BaseWidget",
    "map/core/GeometryServiceTask",
    "map/utils/MapUtils",
    "map/manager/MapManager",
    "esri/geometry/Point",
    "esri/geometry/Polyline",
    "esri/geometry/Polygon",
    "esri/layers/GraphicsLayer",
    "esri/graphic",
    "esri/symbols/PictureMarkerSymbol",
    "esri/symbols/SimpleMarkerSymbol",
    "esri/symbols/SimpleLineSymbol",
    'esri/symbols/SimpleFillSymbol',
    "esri/symbols/TextSymbol",
    "map/core/EsriSymbolsCreator",
    "esri/Color",
    'esri/toolbars/draw',
    "esri/tasks/IdentifyTask",
    "esri/tasks/IdentifyParameters",
    "esri/lang",
    "handlebars",
    "layer",
    "mock/MockHttps",
    "esri/geometry/Extent",
    "static/js/cfg/core/SerializeForm",
    "static/thirdparty/Validform/5.3.2/Validform.min",
    "text!map/template/leas/monitor-header.html",
    "text!map/template/leas/monitor-main.html",
    "text!map/template/leas/monitor-form.html",
    "text!map/template/leas/monitor-new.html",
    "ztree"
], function (declare, lang, arrayUtil, on, BaseWidget, GeometryServiceTask, MapUtils, MapManager, Point, Polyline, Polygon,
             GraphicsLayer, Graphic, PictureMarkerSymbol, SimpleMarkerSymbol, SimpleLineSymbol,
             SimpleFillSymbol, TextSymbol, EsriSymbolsCreator, Color, Draw, IdentifyTask, IdentifyParameters, EsriLang,
             Handlebars, layer, MockHttps, Extent, SerializeForm, Validform, headerTpl, mainTpl, formTpl, newTpl) {

    var me = declare([BaseWidget], {

        onCreate: function () {
            _map = this.getMap().map();
            _monitorConfig = this.getConfig();
            init();
            getDevices();
        },
        onOpen: function () {
            //getGpsTracks();
            //setTimeout(renderPage, 400);
        },
        onPause: function () {
            clearEventHandles();
            _map.removeLayer(monitorGraphicsLayer);
        }
    });

    var PROXY_URL = root + "/map/proxy";

    //设备地址
    var LS_DEVICES_URL = leasUrl + "/map/inspect/gps/devices";
    //部门列表地址
    var LS_ORGANS_URL = leasUrl + "/map/inspect/gps/organs";

    //存放设备数据
    var deviceData = [];
    //存放部门数据
    var organData = [];

    //获取当前所有在线用户信息
    var GPS_TRACKS_URL = leasUrl + "/map/inspect/gps/online";
    var GPS_LANDRES_LIST_URL = leasUrl + "/map/inspect/gps/landresourcelist";
    var GPS_ROLES_LIST_URL = leasUrl + "/map/inspect/gps/rolelist";
    var APPOINT_POST_URL = leasUrl + "/map/inspect/gps/dynamic/plan";

    var MONITOR_TRACKS_URL=leasUrl+"/map/device/tracks?deviceId=";
    var MESSAGE_URL=leasUrl+"/map/mobile/send_message";

    //用户所在部门字段
    var USER_ORGON_KEY = "userOrg";

    //刷新频率（ajax轮询间隔，单位秒）
    var refreshInterval = 30;
    //指派的目标图层
    var appointLayers = [];

    var gpsUsers = [];

    //按区域分组后的数据
    var groupUsers = [];

    //主容器
    var $container;

    var userPMS = new PictureMarkerSymbol('/omp/static/img/map/street-view.png', 19, 24);
    var hightLightSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 10, new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, "#ff0000", 1), "#ff0000")

    var monitorGraphicsLayer, drawTool;

    var timeOutHandler;

    //进行指派的地块
    var appointGraphic, appointCandidates = [];

    var _mEventHandles = [];

    var operaLayers = MapManager.getInstance().getMapConfig().operationalLayers;

    //用于取消定位功能
    var fullExtent;
    //userId from leas
    var userId = undefined;
    //符号
    var lineSymbol;

    /***
     * 初始化界面
     */
    function init() {
        if (urlParams != undefined && urlParams.hasOwnProperty("userId")) {
            userId = urlParams.userId;
        } else {
            layer.msg("userId未获取到,检查地址参数!", {icon: 0});
        }
        if (_monitorConfig != undefined) {
            refreshInterval = _monitorConfig.refreshInterval;
            appointLayers = _monitorConfig.appointLayers;
        }
        if(_monitorConfig.hasOwnProperty("lineColor")){
            lineSymbol= EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH,_monitorConfig.lineColor, 3);
        }else{
            lineSymbol= EsriSymbolsCreator.createSimpleLineSymbol(EsriSymbolsCreator.lineStyle.STYLE_DASH,"#ff0000", 3);
        }
        layer.config();
        //获取元素
        $container = $("#monitorContainer");

        monitorGraphicsLayer = new GraphicsLayer({id: 'monitorGraphicsLayer'});

        MapUtils.setMap(_map);

    }

    /***
     * 获取gps定位点以及路线等信息
     */
    function getGpsTracks() {
        $.getJSON(PROXY_URL, {requestUrl: GPS_TRACKS_URL, dataType: 'json'}, function (data) {
            if (data.success === true) {
                gpsUsers = data.result;
                if (lang.isArray(gpsUsers) && gpsUsers.length > 0) {
                    groupUsers = convert2Group(gpsUsers, USER_ORGON_KEY);
                    clearContainer();
                    renderData();
                } else {
                    layer.msg('当前没有在线用户!');
                }
            } else {
                layer.msg(data.msg, {icon: 0});
            }
        });
    }

    /***
     * 获取设备列表
     */
    function getDevices() {
        //test data
        //deviceData=[{"devices":[{"deviceId":"8","deviceName":"金坛分局法规科设备08","mobile":"13851778992","online":false},{"deviceId":"18","deviceName":"金坛分局法规科设备018","mobile":"13851778982","online":false}],"orgId":"65B9644FFC3C457B8088CC226112F575","orgName":"金坛分局法规科"},{"devices":[{"deviceId":"9","deviceName":"金坛分局经发所设备09","mobile":"13851778991","online":false},{"deviceId":"19","deviceName":"金坛分局经发所设备019","mobile":"13851778981","online":false}],"orgId":"9CCFF7A236074D598D396A86E4D7C7C9","orgName":"金坛分局经发所"},{"devices":[{"deviceId":"6","deviceName":"戚墅堰分局横山桥所设备06","mobile":"13851778994","online":false},{"deviceId":"16","deviceName":"戚墅堰分局横山桥所设备016","mobile":"13851778984","online":false}],"orgId":"19F7392973964D0BB1E4E12B052EB1E5","orgName":"戚墅堰分局横山桥所"},{"devices":[{"deviceId":"3","deviceName":"新北分局奔牛所设备03","mobile":"13851778997","online":false},{"deviceId":"13","deviceName":"新北分局奔牛所设备013","mobile":"13851778987","online":false}],"orgId":"C770BD51D4334EDF8366B5956B443E7D","orgName":"新北分局奔牛所"},{"devices":[{"deviceId":"4","deviceName":"戚墅堰分局遥观所设备04","mobile":"13851778996","online":false},{"deviceId":"14","deviceName":"戚墅堰分局遥观所设备014","mobile":"13851778986","online":false}],"orgId":"2D407788E56247C6B4580A5A9197AD95","orgName":"戚墅堰分局遥观所"},{"devices":[{"deviceId":"2","deviceName":"钟楼分局邹区所设备02","mobile":"13851778998","online":false},{"deviceId":"12","deviceName":"钟楼分局邹区所设备012","location":{"x":40497217.1427336,"y":3522874.0089201},"mobile":"13851778988","online":true,"tracks":"[[40455458.406229764,3505185.235743594],[40455147.89722332,3505147.0192504944],[40455076.241298765,3504989.376216458],[40455119.2348535,3504812.6249358724],[40455104.90366859,3504564.2177307243],[40454980.700066015,3504416.1288199634],[40454732.292860866,3504406.5746966884],[40454402.67560788,3504549.8865458122],[40454149.4913411,3504459.1223747004],[40454149.4913411,3504210.7151695527]]"}],"orgId":"1AEEC2C712174876959E27E6AAC9582A","orgName":"钟楼分局邹区所"},{"devices":[{"deviceId":"10","deviceName":"信息中心设备010","location":{"x":40453150.2349,"y":3474790.2679},"mobile":"13851778990","online":true,"tracks":"[[40493022.96829016,3518660.086147466],[40493218.83866523,3518578.871601704],[40493354.99246254,3518566.9282861506],[40493538.91952206,3518574.0942754825],[40493725.23524469,3518703.082083458],[40493983.21086064,3518829.6812283234],[40494164.74925705,3518734.134703897]]"},{"deviceId":"20","deviceName":"信息中心设备020","mobile":"13851778980","online":false}],"orgId":"651A948EB746417A95D0885E9F258D02","orgName":"信息中心"},{"devices":[{"deviceId":"7","deviceName":"金坛分局局领导设备07","mobile":"13851778993","online":false},{"deviceId":"17","deviceName":"金坛分局局领导设备017","mobile":"13851778983","online":false}],"orgId":"8E4159E52FEA49A3977DF3318E2609E9","orgName":"金坛分局局领导"},{"devices":[{"deviceId":"1","deviceName":"天宁分局郑陆所设备01","mobile":"13851778999","online":false},{"deviceId":"11","deviceName":"天宁分局郑陆所设备011","mobile":"13851778989","online":false},{"deviceId":"21","deviceName":"天宁分局郑陆所设备021","mobile":"13851778979","online":false}],"orgId":"EAA00920DCE54864889D3CC2EFDA0F56","orgName":"天宁分局郑陆所"},{"devices":[{"deviceId":"5","deviceName":"戚墅堰分局横林所设备05","mobile":"13851778995","online":false},{"deviceId":"15","deviceName":"戚墅堰分局横林所设备015","mobile":"13851778985","online":false}],"orgId":"23D32688EA6B44D7BFB90F4F735D7593","orgName":"戚墅堰分局横林所"}];
        $.getJSON(PROXY_URL, {requestUrl: LS_DEVICES_URL, dataType: 'json'}, function (data) {
            deviceData = data;

            getOrgans();
        });
    }

    /***
     * 获取部门列表
     */
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

            renderPage();
        });
    }

    function getTracks(deviceId,type){
        /*if(type==1){
            dynamicTracks = [[40496827.6251356,3522100.03781868],[40496827.22522,3522099.94585366],[40496827.22522,3522099.94585366],[40496827.22522,3522099.94585366],[40496900.1409634,3522325.40400916],[40496900.1409634,3522325.40400916],[40496900.2453088,3522325.20809771],[40496900.2453088,3522325.20809771],[40496900.2453088,3522325.20809771],[40497217.1427336,3522874.0089201]];
        }else{
            dynamicTracks =[[40496827.6251356,3522100.03781868],[40496827.22522,3522099.94585366],[40496827.22522,3522099.94585366],[40496827.22522,3522099.94585366],[40496900.1409634,3522325.40400916],[40496900.1409634,3522325.40400916],[40496900.2453088,3522325.20809771],[40496900.2453088,3522325.20809771],[40496900.2453088,3522325.20809771],[40497217.1427336,3522874.0089201],[40497217.1427336,3522874.0089201],[40497217.0444059,3522875.32569797],[40497217.0444059,3522875.32569797],[40497217.0444059,3522875.32569797],[40497506.2493651,3523441.05838737]];
        }*/
        $.ajaxSettings.async = false;
        $.getJSON(PROXY_URL, {requestUrl: MONITOR_TRACKS_URL+deviceId, dataType: 'json'}, function (data) {
            dynamicTracks = data.tracks;
        });
    }

    var treeObj,appointTreeObj, departDevice = [], allDepartDevice = [], departName, device;
    //标记是否可实时回放路线
    var routeDisplayCheck=false;
    //实时动态请求路线句柄
    var tracksInterval= null;

    var lastPonitGraphic = null;

    var originTracks=[];

    var dynamicTracks=[];
    //最新的中心点位置
    var centerPoint={};
    /***
     * 渲染页面
     */
    function renderPage() {
        fullExtent =_map.extent;
        var page = Handlebars.compile(newTpl);
        $container.empty();
        $container.append(page({organs: organData}));
        $("#MapMonitor").height(490);
        $("#MapMonitor").css('overflow-y','auto');
        $("#MapMonitor").css('overflow-x','hidden');
        //添加部门下拉切换事件等 todo...
        $(document).on("mousedown", function (evt) {
            if ($(evt.target).attr("id") === "removeLocBtn") {
                //取消定位事件
                _map.setExtent(fullExtent);
                clearInterval(tracksInterval);
                return false;
            }else if ($(".organs-wrapper").hasClass("open") && $(evt.target).attr("id") != undefined && $(evt.target).attr("id").indexOf("departTree") < 0) {
                $(".organs-wrapper").click();

            }else{
                if($(evt.target).attr("id")!="userRoleId"){
                    if( $(evt.target).attr("id")===undefined){
                        $(".appoint-ztree-container").remove();
                        return;
                    }
                    var id = $(evt.target).attr("id").split("_")[0];
                    if(id!="appointTree"){
                        $(".appoint-ztree-container").remove();
                    }
                }
            }

        });

        $(".organs-wrapper").on("click", function (evt) {
            var attId =$(evt.target).attr("id");
            evt.stopPropagation();

            var $this = $(this);
            if ($this.hasClass("open")) {
                $this.removeClass("open");
                $(".depart-ztree-container").remove();
                return;
            }

            if(attId==="removeLocBtn"||attId!="selBtnTxt"&&attId!="organSelBtn"){
                return;
            }
            $this.addClass("open");
            var depart = $("#departTpl").html();
            $(".depart-ztree-container").remove();

            $("#MapMonitor").append(depart);
            var treeHeight = 300;
            $(".depart-ztree-container").height(treeHeight);
            $(".depart-ztree-container").width($("#organSelBtn").width() + 4);
            var setting = {
                data: {
                    key: {
                        checked: "checked",
                        name: "text",
                        url: "nourl"
                    }
                },
                callback: {
                    onClick: zTreeOnClick
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
            if(treeData.length>0){
                $.fn.zTree.init($("#departTree"), setting, treeData);
                treeObj = $.fn.zTree.getZTreeObj("departTree");
            }

            var keywords = $("#selBtnTxt").val() || $("#selBtnTxt").html();
            var nodes = [];
            if (keywords != undefined) {
                nodes = treeObj.getNodesByParamFuzzy("text", keywords, null);
            }
            var treeInitHeight = 0;
            if (nodes.length > 0) {
                treeObj.selectNode(nodes[0]);

                var treeId = nodes[0].id;
                var treePId = nodes[0].parentId;
                var rootId;
                for (var i = 0; i < organData.length; i++) {
                    for (var k = 0; k < organData[i].children.length; k++) {
                        if (organData[i].children[k].id == treePId) {
                            treeInitHeight += k * 23;
                            var child = organData[i].children[k].children;
                            for (var j = 0; j < child.length; j++) {
                                if (child[j].id == treeId) {
                                    treeInitHeight += j * 17;
                                }
                            }
                            rootId = organData[i].children[k].parentId;
                        }
                        if (organData[i].id == rootId) {
                            treeInitHeight += i * 23;
                        }
                    }
                }
            }
            $("#departTree").slimScroll({
                height: treeHeight,
                railVisible: true,
                railColor: '#333',
                railOpacity: .2,
                railDraggable: true
            });

            setTimeout(function () {
                $("#departTree").slimScroll({scrollTo: treeInitHeight});
            }, 100);
        });

        departDevice = [];
        //显示所有在线以及不在线设备
        for (var i = 0; i < deviceData.length; i++) {
            arrayUtil.forEach(deviceData[i].devices, function (item) {
                item.orgName = deviceData[i].orgName;
                departDevice.push(item);
            });
        }
        for (var j = 0; j < departDevice.length; j++) {
            var item = departDevice[j];
            item.offline = !item.online;
        }
        allDepartDevice = departDevice;

        var onlineDevices=arrayUtil.filter(departDevice,function(item){
            return item.online==true;
        })
        var offlineDevices =arrayUtil.filter(departDevice,function(item){
            return item.offline==true;
        })
        departDevice=onlineDevices;
        arrayUtil.forEach(offlineDevices,function(item){
            departDevice.push(item);
        })

        renderDevice(departDevice);
        //添加事件监听等
        addListener();
    }

    /**
     * treenode 点击事件
     * @param event
     * @param treeId
     * @param treeNode
     */
    function zTreeOnClick(event, treeId, treeNode) {
        var sId = treeNode.id;
        $("#selBtnTxt").html(treeNode.text);
        $(".depart-ztree-container").remove();
        device = getDevice(sId);
        clear();
        if (device != null) {
            departDevice = device;
            for (var i = 0; i < departDevice.length; i++) {
                var item = departDevice[i];
                item.offline = !item.online;
                departDevice[i] = item;
            }
            var onlineDevices=arrayUtil.filter(departDevice,function(item){
                return item.online==true;
            })
            var offlineDevices =arrayUtil.filter(departDevice,function(item){
                return item.offline==true;
            })
            departDevice=onlineDevices;
            arrayUtil.forEach(offlineDevices,function(item){
                departDevice.push(item);
            })
            renderDevice(departDevice);
        }
    }

    function renderDevice(devices) {
        var deviceTpl = $("#deviceTpl").html();
        var temp = Handlebars.compile(deviceTpl);
        var htm = temp({list: devices});
        var listHeight = 220;
        $(".device-list").append(htm);
        $(".device-list").height(listHeight);
        $(".device-list").slimScroll({
            height: listHeight,
            railVisible: true,
            railColor: '#333',
            railOpacity: .2,
            railDraggable: true
        });

        //计算pda在线不在线数量
        var onlineCount = 0;
        var offLineCount = 0;

        //加载在线设备位置点
        var onlineDevice = arrayUtil.filter(devices, function (item) {
            return item.online == true;
        });
        if (onlineDevice.length > 0) {
            arrayUtil.forEach(onlineDevice, function (item) {
                item.dockGraphicId=item.deviceId;
                var point = new Point(item.location.x, item.location.y, _map.spatialReference);
                var gra = new Graphic(point, hightLightSymbol, item);
                monitorGraphicsLayer.add(gra);
            });

            if (monitorGraphicsLayer != undefined && monitorGraphicsLayer.graphics.length > 0)
                _map.addLayer(monitorGraphicsLayer);
        }

        onlineCount = onlineDevice.length;
        offLineCount = devices.length - onlineCount;

        $(".pda-online-info").remove();
        $("#deviceInfo").append(" <div class='pda-online-info'>在线设备：" + onlineCount + "台;不在线：" + offLineCount + "台</div>");

        $(".device-list >li").on("click", function () {
            var id = $(this).data("id");
            $.each( $(".device-list >li"),function(){
                if($(this).hasClass("device-selected")){
                    $(this).removeClass("device-selected");
                }
            });
            $(this).addClass("device-selected");
            getDeviceInfo(id);
        });
    }

    function clear() {
        $(".device-list").empty();
        $(".pda-online-info").remove();
        $(".pda-info").remove();
    }

    /**
     * 获取device
     * @param id
     */
    function getDeviceInfo(id) {
        var d, device;

        clearInterval(tracksInterval);

        $(".pda-info").remove();
        device = arrayUtil.filter(departDevice, function (item) {
            return item.deviceId == id;
        });
        if (device.length > 0) {
            //定位
            d = device[0];
            $("#pdaBaseInfo").css("display", "block");
            var infoTpl = $("#padInfoTpl").html();
            var temp = Handlebars.compile(infoTpl);
            d.offline = !d.online;
            var htm = temp({data: d});
            $("#pdaBaseInfo").append(htm);

            if (d.hasOwnProperty("location")) {
                if(!d.hasOwnProperty("checked")){
                    locFunc(d.location.x, d.location.y,null);
                    var point= new Point(d.location.x, d.location.y,_map.spatialReference);
                    _map.centerAt(point);
                }
            }
            //请求并获取路线 并加载地图
            if(routeDisplayCheck){
                var attr={"graphicId":id};
                monitorGraphicsLayer.clear();
                if(!d.hasOwnProperty("location")){
                    return;
                }

                getTracks(d.deviceId,1);
                var tracks = dynamicTracks;

                centerPoint.x = tracks[tracks.length-1][0];
                centerPoint.y = tracks[tracks.length-1][1];

                var point = new Point(centerPoint.x, centerPoint.y, _map.spatialReference);
                var gra = new Graphic(point, hightLightSymbol,attr);
                monitorGraphicsLayer.add(gra);
                lastPonitGraphic = gra;
                locFunc(centerPoint.x, centerPoint.y);
                _map.centerAt(point);

                if(tracks!=undefined&&tracks!=null){
                    addTracks(tracks, d.deviceId);
                } else {
                    layer.msg("获取实时巡查路线失败！",{icon:0});
                }
            }

            $(".path-check").on("change", function () {
                var id = $(this).data("id");
                if ($(this).is(":checked")) {
                    arrayUtil.forEach(departDevice, function (item) {
                        if(item.deviceId == id){
                            device = item;
                        }
                        item.checked = true;
                    });
                    routeDisplayCheck= true;
                    d = device;
                    //判断显示历史巡查路线  实时巡查路线
                    if (d.online) {
                        getTracks(d.deviceId,1);
                        var tracks = dynamicTracks;
                        if(tracks!=undefined&&tracks!=null){
                            monitorGraphicsLayer.clear();
                            centerPoint.x =d.location.x;
                            centerPoint.y =d.location.y;
                            var tracks = dynamicTracks;

                            centerPoint.x = tracks[tracks.length-1][0];
                            centerPoint.y = tracks[tracks.length-1][1];

                            var point = new Point(centerPoint.x, centerPoint.y, _map.spatialReference);
                            var gra = new Graphic(point, hightLightSymbol,attr);
                            monitorGraphicsLayer.add(gra);
                            lastPonitGraphic = gra;

                            addTracks(tracks, d.deviceId);
                        } else {
                            layer.msg("获取实时巡查路线失败！",{icon:0});
                        }
                    } else {
                        layer.msg("当前设备不在线！");
                    }
                } else {
                    clearInterval(tracksInterval);
                    arrayUtil.forEach(departDevice, function (item) {
                        item.checked = false;
                    });
                    monitorGraphicsLayer.clear();
                    routeDisplayCheck = false;
                }
            });
            $("#qSearchBtn").on("click", function () {
                var layerIndex = layer.open({
                    id: "mapDialog",
                    type: 1,
                    title: '短信编辑',
                    content: "<textarea  id='messageContent' style='width:400px; height:60px;'/>",
                    shade: 0,
                    area: ['400px', '150px'],
                    btn: ['发送'],
                    success: function () {
                    },
                    cancel: function () {
                    },
                    yes: function () {
                        var mobile = $("#telephone").val();
                        var message = $("#messageContent").val();
                        $.post(MESSAGE_URL, {mobile: mobile, message: message},function(data){
                          if(data =="success"){
                              layer.close(layerIndex);
                              layer.msg("发送成功",{icon:1});
                          }else{
                              layer.msg("发送失败",{icon:1});
                          }
                        });
                    }
                });
            });
        }else{
            monitorGraphicsLayer.clear();
        }
    }


    /**
     * 设置时间间隔 动态获取当前设备的id 时间为5s
     * @param deviceId
     */
    function getTracksInterval(deviceId){
        var attr={"graphicId":deviceId};

        tracksInterval = setInterval(function () {
            getTracks(deviceId,2);


            var moTracks = dynamicTracks;

            var paths =moTracks;
            centerPoint.x = paths[paths.length-1][0];
            centerPoint.y = paths[paths.length-1][1];
            locFunc(centerPoint.x,centerPoint.y);

            var originPaths = originTracks;
            var codeindex = originPaths.length-1;
            var dynamicGra = null;
            var monitorInterval = setInterval(function (){

                if(paths.length == originPaths.length){
                    return;
                }

                monitorGraphicsLayer.remove(lastPonitGraphic);
                monitorGraphicsLayer.remove(dynamicGra);

                if (codeindex == paths.length - 1) {
                    originPaths = moTracks;
                    centerPoint.x = paths[codeindex][0];
                    centerPoint.y = paths[codeindex][1];

                    locFunc(centerPoint.x,centerPoint.y);

                    var point = new Point(paths[codeindex][0], paths[codeindex][1], _map.spatialReference);
                    var gra = new Graphic(point, hightLightSymbol,attr);
                    lastPonitGraphic = gra;
                    monitorGraphicsLayer.add(gra);
                    return false;
                }else{
                    originPaths = moTracks;
                    centerPoint.x = paths[paths.length-1][0];
                    centerPoint.y = paths[paths.length-1][1];
                    locFunc(centerPoint.x,centerPoint.y);
                }
                var pas = [];
                pas.push(originPaths[codeindex]);
                for(var i =codeindex+1;i<paths.length;i++){
                    pas.push(paths[i]);
                }

                var point = new Point(paths[paths.length-1][0], paths[paths.length-1][1], _map.spatialReference);
                var gra = new Graphic(point, hightLightSymbol,attr);
                monitorGraphicsLayer.add(gra);
                lastPonitGraphic = gra;
                dynamicGra = gra;

                var polyLineJson = {
                    "paths": [pas],
                    "spatialReference": _map.spatialReference
                };
                var polyLine = new Polyline(polyLineJson);
                var g = new Graphic(polyLine, lineSymbol, attr);
                monitorGraphicsLayer.add(g);
                codeindex=paths.length-1;
            },400);
        },5000);
    }

    /**
     * 定位前准备
     * @param tracks
     */
    function locFunc(x,y) {
        var locExtent = new Extent(x-200,y-200,x+200,y+200,_map.spatialReference);
        map.setExtent(locExtent.expand(4));
    }

    function addTracks(tracks,deviceId) {
        originTracks = tracks;
        var paths = [tracks];
        locFunc(centerPoint.x,centerPoint.y);
        var attr={"graphicId":deviceId};

        var path = paths[0];
        var polyLineJson = {
            "paths": [path],
            "spatialReference": _map.spatialReference
        };
        var polyLine = new Polyline(polyLineJson);
        var g = new Graphic(polyLine, lineSymbol, attr);
        monitorGraphicsLayer.add(g);
        getTracksInterval(deviceId);
    }

    /**
     * 获取设备信息
     * @param id
     * @returns {*}
     */
    function getDevice(id) {
        if (id == "all") {
            return allDepartDevice;
        } else {
            getOrganById(organData[0],id);
            var idArray = [];
            getSubOrganIds(findOrgan,idArray);

            if(idArray.length > 0){
                var devices = [];
                $(deviceData).each(function(index,el){
                    if(contains(idArray,el.orgId)){
                        $(el.devices).each(function(index1,el1){
                            devices.push(el1);
                        });
                    }
                });
                return devices;
            } else {
                layer.msg("未查询到设备信息！");
                return null;
            }
            var tmpArr = arrayUtil.filter(deviceData, function (item) {
                return item.orgId == id;
            })
            if (tmpArr.length > 0) {
                var device = tmpArr[0];
                return device.devices;
            } else {

            }
        }
    }

    // 判断集合中是否含有后者的数据
    function contains(array,orgId){
        for(var i = 0; i < array.length; i++){
            if(array[i] == orgId){
                return true;
            }
        }
        return false;
    }

    // 根据部门id获取部门对象数据
    var findOrgan = null;
    function getOrganById(organDataTemp, organId) {
        if(organDataTemp.id == organId){
            findOrgan = organDataTemp;
        } else {
            for(var i = 0; i < organDataTemp.children.length; i++){
                var organVo = organDataTemp.children[i];
                if(organVo.id == organId){
                    findOrgan = organVo;
                    break;
                } else if(organVo.children.length > 0){
                    getOrganById(organVo, organId);
                }
            }
        }
    }

    // 获取当前部门id及下属所有层次部门数据id集合
    function getSubOrganIds(organVo,idArray) {
        idArray.push(organVo.id);
        if(organVo.children.length > 0){
            $(organVo.children).each(function(index,el){
                getSubOrganIds(el,idArray);
            });
        }
    }

    /***
     * 渲染前台列表页面以及在地图上显示位置点
     */
    function renderData() {
        //加载头部信息
        var header = Handlebars.compile(headerTpl);
        $container.append(header({size: gpsUsers.length}));
        //加载列表部分
        var main = Handlebars.compile(mainTpl);
        $container.append(main({listData: gpsUsers, groupData: groupUsers}));
        //添加位置信息至地图
        add2Map();
        //添加事件监听等
        addListener();
    }

    /**
     * 添加事件监听
     */
    function addListener() {
        _mEventHandles.push(on($("#appointBtn")[0], 'click', function () {
            resetMap();
            drawTool = drawTool ? drawTool : new Draw(_map);
            on.once(drawTool, "draw-end", function (evt) {
                drawTool.deactivate();
                // 进行属性识别 找出需要指派的巡查点（地块）
                identify(evt.geometry);
            });
            esri.bundle.toolbars.draw.addPoint = "单击开始进行指派";
            drawTool.activate(Draw.POINT);
            on.once(_map, 'click', function () {
                window.clearTimeout(timeOutHandler);
            });
            timeOutHandler = window.setTimeout(function () {
                drawTool.deactivate();
                resetMap();
            }, 8000);
        }));

        //点击列表定位 并选中
        _mEventHandles.push(on($(".monitor-main-li-list"), 'click', function () {
            var id = $(this).data("id").toString();
            var results = arrayUtil.filter(gpsUsers, function (item) {
                return item.id === id;
            });
            if (results[0] != undefined) {
                var tmp = results[0];
                // 切换选中状态
                var pnt = new Point(tmp.x, tmp.y, _map.spatialReference);
                _map.centerAt(pnt);
            }
        }));

    }

    /***
     * 清除事件句柄 避免重复监听
     */
    function clearEventHandles() {
        arrayUtil.forEach(_mEventHandles, function (handle) {
            if (handle != undefined) {
                if (lang.exists("remove", handle))handle.remove();
            }
        });
        _mEventHandles = [];
    }

    var identifyCount = 0;

    var needOpenEditForm = true;
	/***
     *
     * @param geo
     */
    function identify(geo) {
		needOpenEditForm = true;
        if (appointLayers === undefined || appointLayers.length == 0) {
            layer.msg("未配置指派图层!", {icon: 0});
            return;
        }
        var identifyParams = new IdentifyParameters();
        identifyParams.tolerance = 3;
        identifyParams.returnGeometry = true;
        identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_VISIBLE;
        identifyParams.width = _map.width;
        identifyParams.height = _map.height;
        identifyParams.geometry = geo;
        identifyParams.mapExtent = _map.extent;

        arrayUtil.forEach(appointLayers, function (item) {
            var mLyr = _map.getLayer(item.sId);
            if (mLyr != undefined) {
                var identifyTask = new IdentifyTask(mLyr.url);
                on(identifyTask, 'error', function (err) {
                    console.error(err.message)
                });
                identifyCount++;
                identifyTask.execute(identifyParams, lang.hitch(this, identifyResult, lang.mixin(item, {
                    url: mLyr.url,
                    sName: getServiceNameById(item.sId)
                })));
            }
        });
    }

    /***
     * 处理地图识别结果
     * @param token
     * @param result
     */
    function identifyResult(token, result) {
        identifyCount--;
		if (result != null && result.length > 0) {
            appointGraphic = result[0].feature;
            var attr = appointGraphic.attributes;
            //增加额外属性
            attr.mapUrl = token.url;
			attr.geometry = JSON.stringify(appointGraphic.geometry);
            attr.serviceName = token.sName;
            appointCandidates.push(appointGraphic);
            if (needOpenEditForm) {
				needOpenEditForm = false;
                //高亮图形
                MapUtils.highlightFeatures([appointGraphic], false);
                formFullFill(attr);
//                //缓冲一定范围内的在线用户 将数据进行关联
//                layer.msg('查找附近的在线用户..', {time: 20000});
//                GeometryServiceTask.buffer(appointGraphic.geometry, 800, function (r) {
//                    var geos = r.geometries;
//                    layer.closeAll();
//                    if (geos != undefined && geos.length > 0) {
//                        var bufferGeo = geos[0];
//                        //搜索缓冲范围内的监控点
//                        var users = findUsers(bufferGeo);
//                        if (users.length > 0) {
//                            formFullFill(attr,users[0]);
//                        } else
//                            layer.msg("附近没有在线用户!", {time: 1500});
//                    }
//
//                }, {outSpatialReference: _map.spatialReference});

            }
        }
    }

    /***
     * get service name
     * @param id
     */
    function getServiceNameById(id) {
        if (operaLayers != undefined) {
            var data = arrayUtil.filter(operaLayers, function (item) {
                return item.id === id;
            });
            if (data.length > 0)
                return data[0].name;
        }
    }

    /***
     * 表单填充
     * @param attr      属性数据
     * @param userInfo  用户信息
     * @param sUrl      服务地址
     */
    function formFullFill(attr) {
        //弹出form窗口
        var templ = Handlebars.compile(formTpl);
        var contentTxt ="";
        if(organData.length>0){
            contentTxt = organData[0].text;
        }
        var content = templ({data: attr, userId: userId,text:contentTxt});
        layer.open({
            type: 1,
            title: ['<i class="fa fa-leaf"></i>&nbsp;实时指派', 'background-color:#428bca;color:#ffffff;border-color:#357ebd;border: 1px solid transparent;'],
            area: '600px',
            shade: 0,
            content: content,
            cancel: function () {
                resetMap();
            },
            success: function () {
                $.getJSON(PROXY_URL, {requestUrl: GPS_LANDRES_LIST_URL, dataType: 'json'}, function (data) {
                    if (data.success === true) {
                        var arr = data.result;
                        if (lang.isArray(arr)) {
                            arrayUtil.forEach(arr, function (item) {
                                $("#colZpYwlx").append("<option value=" + item + ">" + item + "</option>");
                            });
                        }
                    } else {
                        layer.msg(data.msg, {icon: 0});
                    }
                });
                //巡查单位下拉树
                if(organData.length>0){
                    var ztreeData = organData;
                    $("#colZpZpbmmc").on("click",function(){
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
                        var msgHandle = layer.msg('提交数据中...', {time: 6000});
                        $.ajax({
                            url: APPOINT_POST_URL,
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
		$("#colZpZpbmmc").val(treeNode.text);
        $(".appoint-ztree-container").remove();
        $("#colZpZpbmmc").parent().children(".Validform_wrong").html("");
    }

    /**
     * 添加到地图上显示
     */
    function add2Map() {
        arrayUtil.forEach(gpsUsers, function (item) {
            var pnt = new Point(item.x, item.y, _map.spatialReference);
            var g = new Graphic(pnt, userPMS, item, null);
            //添加txt gra
            var txtSymbol = new TextSymbol();
            txtSymbol.setText(item.userAlias);
            txtSymbol.setVerticalAlignment("bottom");
            txtSymbol.setColor(new Color([0, 0, 0, 0.5]));
            txtSymbol.setOffset(0, 5);
            monitorGraphicsLayer.add(new Graphic(pnt, txtSymbol));

            monitorGraphicsLayer.add(g);

        });
        if (monitorGraphicsLayer != undefined && monitorGraphicsLayer.graphics.length > 0)
            map.addLayer(monitorGraphicsLayer);
    }

    /**
     * 清空dom
     */
    function clearContainer() {
        $container.empty();
    }

    /**
     *reset map
     */
    function resetMap() {
        layer.closeAll();
        esri.bundle.toolbars.draw.addPoint = "单击以添加点";
        appointGraphic = null;
        appointCandidates = [];
        MapUtils.clearMapGraphics();
    }

    /***
     * 找出附近的在线用户
     * @param geo
     */
    function findUsers(geo) {
        var data = [];
        for (var i = 0; i < gpsUsers.length; i++) {
            var user = gpsUsers[i];
            var pnt = new Point(user.x, user.y, _map.spatialReference);
            if (geo.contains(pnt)) {
                data.push(user);
            }
        }
        return data;
    }

    /***
     * 进行分组
     */
    function convert2Group(array, groupKey) {
        var result = [];
        if (lang.isArray(array)) {
            arrayUtil.forEach(array, function (item) {
                if (item.hasOwnProperty(groupKey)) {
                    var val = item[groupKey];
                    var tmpArr = arrayUtil.filter(result, function (i) {
                        return i.groupName === val;
                    });
                    if (tmpArr.length > 0) {
                        tmpArr[0].data.push(item);
                    } else
                        result.push({groupName: val, data: [item]});
                }
            });
        }
        return result;
    }

    return me;
});