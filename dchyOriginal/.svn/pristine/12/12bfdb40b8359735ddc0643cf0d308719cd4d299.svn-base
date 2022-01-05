/**
 * Created by user on 2016-06-19.
 */
define(["esri/graphic",
        "dojo/_base/declare",
        "dojo/_base/lang",
        "dojo/_base/array",
        "dojo/topic",
        "dojo/json",
        "dojox/uuid/generateRandomUuid",
        "map/core/BaseWidget",
        "map/core/BaseAnalysis",
        "map/core/JsonConverters",
        "map/core/GeoDataStore",
        "handlebars",
        "esri/lang",
        "map/utils/MapUtils",
        "text!static/js/map/template/analysis/analysis-basic-tpl.html",
        "text!static/js/map/template/analysis/analysis-list-item.html",
        "static/thirdparty/multi-select/multiple-select.min",
        "css!static/thirdparty/multi-select/multiple-select.css"],
    function (Graphic, declare, lang, arrayUtil, topic, dojoJSON, RandomUuid, BaseWidget, BaseAnalysis, JsonConverters, GeoDataStore, Handlebars,
              esriLang, MapUtils, BasicTpl, ListItem) {

        var me = declare([BaseWidget], {
            onCreate: function () {
                _map = this.getMap().map();
                _shareConfig = this.getConfig();
                _init();
            },
            onPause: function () {

            },
            onOpen: function () {

            },
            onDestroy: function () {

            }
        });

        var _map, _shareConfig;
        var shareSelect,$dataPush,$dataPickUp,$dataReceive;
        var role,queryDsName,pushDsName,layerNamesArr=[];
        var interval=null;

        /***
         * init
         * @private
         */
        function _init() {
            role=_shareConfig.role,
            queryDsName=_shareConfig.queryDsName,
            pushDsName=_shareConfig.pushDsName;

            //初始化变量
            shareSelect = $("#shareSelect");
            $dataPush = $("#dataPush");
            $dataPickUp = $("#dataPickUp");
            $dataReceive = $("#dataReceive");

            // 用户权限判断
            if(role=='qx'){
                $dataPush.hide();
                $dataPickUp.hide();
            }else if(role=='shi'){
                $dataReceive.hide();
            }

            if (lang.isArray(_shareConfig.layerNames) && _shareConfig.layerNames.length > 0) {
                shareSelect.append(renderTpl($("#shareSelectTpl").html(), {layerNames: _shareConfig.layerNames}));
                //多选
                $("#layerNameSelector").multipleSelect({
                    selectAll: false,
                    placeholder: '选择来源',
                    allSelected: '所有来源',
                    minimumCountSelected: 5,
                    delimiter: '|',
                    onClick:function (obj) {
                        if(obj.checked){
                            layerNamesArr.push(obj.value);
                        }else{
                            layerNamesArr.splice($.inArray('obj.value',layerNamesArr),1);
                        }
                    }
                });

            }
            // 市局推送（数据推送到共享平台）
            $dataPush.on('click', function () {
                shareData('push',$dataPush);
            })
            // 市局更新提取（从目标库获取数据）
            $dataPickUp.on('click', function () {
                shareData('pickup',$dataPickUp);
            })
            // 区县接收（共享平台获取数据）
            $dataReceive.on('click', function () {
                shareData('receive',$dataReceive);
            })
        }

        /**
         * 数据操作方法
         * @param actionType 操作类型（推送、提取、获取）
         * @param btnObj 按钮对象
         */
        function shareData(actionType,btnObj) {
            var tipsMsg="",resultTipsMsg="";
            var paramData = {
                queryDsName: queryDsName,
                pushDsName: pushDsName,
                layerNames: layerNamesArr
            };

            if(actionType=='push'){
                tipsMsg='推送中';
                resultTipsMsg='<i class="iconfont">&#xe632;</i>推送';
            }else if(actionType=='pickup'){
                tipsMsg='提取中';
                resultTipsMsg='<i class="iconfont">&#xe631;</i>提取';
                paramData.queryDsName=pushDsName;
                paramData.pushDsName=queryDsName;
            }else if(actionType=='receive'){
                tipsMsg='接收中';
                resultTipsMsg='<i class="iconfont">&#xe60f;</i>接收';
            }
            if(layerNamesArr.length==0){
                layer.msg("请选择数据来源！",{icon:0,time:2500});
                return false;
            }
            // 动画操作中....
            loadingInterval(btnObj,tipsMsg);
            $.ajax({
                url: "/omp/graphdata/share",
                data: paramData,
                traditional: true,
                success: function (data) {
                    if(!data.success){
                        layer.msg(data.msg,{icon:2,time:3000});
                    }else{
                        layer.msg("操作成功！",{icon:1,time:2000});
                    }
                    btnObj.find('a').html(resultTipsMsg);
                    // 清除定时
                    clearInterval(interval);
                }
            });


        }
        /**
         * loading动画效果加载
         * @param fun 绑定效果的id
         */
        function loadingInterval(btnObj,tipsMsg) {
            var x = "";
            interval=setInterval(function () {
                x = (x == '.....') ? '.' : (x + '.');
                btnObj.find('a').text(tipsMsg+x);
            }, 300);
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
    }
);