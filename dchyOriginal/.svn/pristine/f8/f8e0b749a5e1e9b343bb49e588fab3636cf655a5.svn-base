
layui.config({
    base: '/omp/static/thirdparty/layui_exts/' //配置 layui 第三方扩展组件存放的基础目录
}).extend({
    protree: 'proTree/protree'
}).use(['protree'], function () {
    layui.use(['jquery', 'element', 'tree'], function () {
        var $ = layui.jquery
            , element = layui.element
            , tree = layui.tree;
        $(function () {
            //动态获取url
            var _url = '';
            $.ajax({
                url: getContextPath() + "/oms/rest/v1.0/dchy/geturl",
                type: 'post',
                async: false,
                success: function (data) {
                    _url = data;
                }
            });
            //导航材料
            var treeData = [];
            function getTreeItem(obj, pid) {
                var newObj = {};
                newObj.id = obj.id;
                newObj.pid = pid;
                newObj.url = obj.url;
                newObj.name = obj.title;
                newObj.uid = obj.id;
                newObj.type = obj.type;
                treeData.push(newObj);
                if (obj.children.length > 0) {
                    obj.children.forEach(function (item) {
                        getTreeItem(item, obj.id);
                    });
                }
            }
            var protree = layui.protree;
            //打开材料导航
            $('.project-info').on('click', '.finished-box .checked-span', function () {
                $(this).addClass('active').parent().siblings().children('span').removeClass('active');
                var slbh = $(this).attr('data');
                $('#materialTreeMenu').empty();
                $.ajax({
                    type: "post",
                    url: _url + "/msurveyplat-exchange/rest/v1.0/onemap/fjxx/" + slbh,
                    //url: "http://192.168.50.60:8083/msurveyplat-exchange/rest/v1.0/onemap/fjxx/" + slbh,
                    async: false,
                    success: function (res) {
                        treeData = [];
                        getTreeItem(res, 0);
                        protree.init('#materialTreeMenu', {
                            arr: treeData,
                            close: true,
                            first_close: false,//是否默认关闭第一组
                            // accordion: true,是否开启手风琴模式
                            simIcon: "fa fa-file-o",
                            mouIconOpen: "fa fa-folder-open",
                            mouIconClose: "fa fa-folder",
                            callback: function (id, name, url) {
                                if (url != 'null') {
                                    window.open(url);
                                }
                            }
                        });

                    }
                })

                $('.material-nav').show();
            })
            //关闭材料导航
            $('.material-close-btn').click(function () {
                $('.material-nav').hide();
                $('.finished-item').children('span').removeClass('active');
            })
            //打开项目图层 
            $('.space-tree-btn').click(function () {
                //项目图层
                $.ajax({
                    type: "post",
                    url: getContextPath() + "/oms/rest/v1.0/dchy/tcxx",
                    data: {
                        chsx: ''
                    },
                    async: false,
                    success: function (res) {
                        var ywtcId = res.id;
                        res.spread = true;
                        res.checked = true;
                        res.children.forEach(function (v) {
                            v.spread = true;
                        })
                        var idArr = [];
                        var data = [];
                        data.push(res);
                        tree.render({
                            elem: '#projectLayerTree'
                            , data: data
                            , showCheckbox: true
                            , oncheck: function (obj) {
                                var idList = getCheckId(obj.data);
                                if (obj.checked) {
                                    idArr = idArr.concat(idList);
                                } else {
                                    for (var i = 0; i < idList.length; i++) {
                                        var idx = idArr.indexOf(idList[i]);
                                        idArr.splice(idx, 1);
                                    }
                                }
                                // console.log('点击后的所有id--------------:', idArr);
                                map.map().getLayer(ywtcId).setVisibleLayers(idArr);
                            }
                        });
                    }
                })
                $('.right-layers').show();

            })
            //关闭项目图层
            $('.layers-close-btn').click(function () {
                $('.right-layers').hide();
            })

            function getLayerItemId(obj) {
                var idArr = [];
                if (obj.hasOwnProperty('children')) {
                    var arr = obj.children;
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].hasOwnProperty('children')) {
                            var c = arr[i].children;
                            for (var j = 0; j < c.length; j++) {
                                idArr.push(c[j].id);
                            }
                        }
                    }
                }
                return idArr;
            }
            function getCheckId(obj) {
                var idArr = [];
                if (obj.hasOwnProperty('children')) {
                    var arr = obj.children;
                    for (var i = 0; i < arr.length; i++) {
                        if (arr[i].hasOwnProperty('children')) {
                            var c = arr[i].children;
                            for (var j = 0; j < c.length; j++) {
                                idArr.push(c[j].id);
                            }
                        } else {
                            idArr.push(arr[i].id);
                        }
                    }
                } else {
                    idArr.push(obj.id);
                }
                return idArr;
            }
        });
    });
})
