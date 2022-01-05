/**
 * 任务台账资源的js
 */
var newWin = [];
/**
 * 判断是否可以批量转发
 */
var isPlzf = '';
/**
 * 非登记业务流程，未配置后台返回字符串 'empty'，区分是否已经获取到配置
 */
var fdjywlc = '';
layui.use(['jquery', 'table', 'element', 'carousel', 'form', 'laytpl', 'laydate', 'layer', 'response', 'workflow', 'moduleAuthority'], function () {
    var $ = layui.jquery,
        table = layui.table,
        element = layui.element,
        laytpl = layui.laytpl,
        layer = layui.layer,
        laydate = layui.laydate,
        carousel = layui.carousel,
        response = layui.response,
        workflow = layui.workflow,
        moduleAuthority = layui.moduleAuthority;
    $(function () {
        //按顺序加载tab
        var tabList;
        //      $.ajax({
        //          type: "GET",
        //          url: getContextPath() + "/rest/v1.0/user/order",
        //          data: {},
        //          async: false,
        //          success: function (data) {
        //              tabList = data;
        //          }, error: function (e) {
        //              response.fail(e, '');
        //          }
        //      });
        tabList = ['dbContent', 'ybContent', 'xmContent'];
        tabList.forEach(function (v) {
            var getTpl;
            switch (v) {
                case "xmContent":
                    $('.bdc-list-tab .layui-tab-title').append('<li>项目列表</li>');
                    getTpl = xmContent.innerHTML;
                    break;
                case "ybContent":
                    $('.bdc-list-tab .layui-tab-title').append('<li>已办任务</li>');
                    getTpl = ybContent.innerHTML;
                    break;
                case "dbContent":
                    $('.bdc-list-tab .layui-tab-title').append('<li id="todoTab">待办任务<span class="bdc-list-num-tips"></span></li>');
                    getTpl = dbContent.innerHTML;
                    break;
                //              case "rlContent":
                //                  $('.bdc-list-tab .layui-tab-title').append(rlContentLi);
                //                  getTpl = rlContent.innerHTML;
                //                  break;
                //              case "grContent":
                //                  $('.bdc-list-tab .layui-tab-title').append('<li>个人项目列表</li>');
                //                  getTpl = grContent.innerHTML;
                //                  break;
            }
            laytpl(getTpl).render([], function (html) {
                $('.bdc-list-tab .layui-tab-content').append(html);
            });
        });

        /*
                $('.bdc-list-tab .layui-tab-title').append('<li>项目列表</li>');
                $('.bdc-list-tab .layui-tab-title').append('<li>已办任务</li>');
                $('.bdc-list-tab .layui-tab-title').append('<li id="todoTab">待办任务<span class="bdc-list-num-tips"></span></li>');
        */

        $('.bdc-list-tab .layui-tab-title li:first-child').addClass('layui-this');
        $('.bdc-list-tab .layui-tab-content .layui-tab-item:first-child').addClass('layui-show');

        if (lcPageEdition == 'hf') {
            //合肥默认加载节点下拉框
            var $jdmcDom = $('.bdc-jdmc');
            queryTaskNameList($jdmcDom, '', true);
            var $djyyDom = $('.bdc-djyy');
            queryDjyyList('', true, $djyyDom);
            $('.bdc-reset').on('click', function () {
                if ($(this).data('zt') != 'rl') {
                    queryTaskNameList($(this).parents('.bdc-search-box').find('.bdc-jdmc'), '', false);
                }
                queryDjyyList('', false, $(this).parents('.bdc-search-box').find('.bdc-djyy'));
            });
        }
        //初始化日期控件
        loadDate();

        function loadDate() {
            lay('.test-item').each(function () {
                var kssjdy = laydate.render({
                    elem: '#kssjdy',
                    type: 'datetime',
                    trigger: 'click',
                    done: function (value, date) {
                        //选择的结束时间大
                        if ($('#kssjxy').val() != '' && !completeDate($('#kssjxy').val(), value)) {
                            $('#kssjxy').val('');
                            $('.laydate-disabled.layui-this').removeClass('layui-this');
                        }
                        kssjxy.config.min = {
                            year: date.year,
                            month: date.month - 1,
                            date: date.date,
                            hours: date.hours,
                            minutes: date.minutes,
                            seconds: date.seconds
                        }
                    }
                });
                var kssjxy = laydate.render({
                    elem: '#kssjxy',
                    type: 'datetime',
                    trigger: 'click'
                });

                var ybkssjdy = laydate.render({
                    elem: '#ybkssjdy',
                    type: 'datetime',
                    trigger: 'click',
                    done: function (value, date) {
                        //选择的结束时间大
                        if ($('#ybkssjxy').val() != '' && !completeDate($('#ybkssjxy').val(), value)) {
                            $('#ybkssjxy').val('');
                            $('.laydate-disabled.layui-this').removeClass('layui-this');
                        }
                        ybkssjxy.config.min = {
                            year: date.year,
                            month: date.month - 1,
                            date: date.date,
                            hours: date.hours,
                            minutes: date.minutes,
                            seconds: date.seconds
                        }
                    }
                });
                var ybkssjxy = laydate.render({
                    elem: '#ybkssjxy',
                    type: 'datetime',
                    trigger: 'click'
                });

                var xmkssjdy = laydate.render({
                    elem: '#xmkssjdy',
                    type: 'datetime',
                    trigger: 'click',
                    done: function (value, date) {
                        //选择的结束时间大
                        if ($('#xmkssjxy').val() != '' && !completeDate($('#xmkssjxy').val(), value)) {
                            $('#xmkssjxy').val('');
                            $('.laydate-disabled.layui-this').removeClass('layui-this');
                        }
                        xmkssjxy.config.min = {
                            year: date.year,
                            month: date.month - 1,
                            date: date.date,
                            hours: date.hours,
                            minutes: date.minutes,
                            seconds: date.seconds
                        }
                    }
                });
                var xmkssjxy = laydate.render({
                    elem: '#xmkssjxy',
                    type: 'datetime',
                    trigger: 'click'
                });

                var grkssjdy = laydate.render({
                    elem: '#grkssjdy',
                    type: 'datetime',
                    trigger: 'click',
                    done: function (value, date) {
                        //选择的结束时间大
                        if ($('#grkssjxy').val() != '' && !completeDate($('#grkssjxy').val(), value)) {
                            $('#grkssjxy').val('');
                            $('.laydate-disabled.layui-this').removeClass('layui-this');
                        }
                        grkssjxy.config.min = {
                            year: date.year,
                            month: date.month - 1,
                            date: date.date,
                            hours: date.hours,
                            minutes: date.minutes,
                            seconds: date.seconds
                        }
                    }
                });
                var grkssjxy = laydate.render({
                    elem: '#grkssjxy',
                    type: 'datetime',
                    trigger: 'click'
                });

                var rlkssjdy = laydate.render({
                    elem: '#rlkssjdy',
                    type: 'datetime',
                    trigger: 'click',
                    done: function (value, date) {
                        //选择的结束时间大
                        if ($('#rlkssjxy').val() != '' && !completeDate($('#rlkssjxy').val(), value)) {
                            $('#rlkssjxy').val('');
                            $('.laydate-disabled.layui-this').removeClass('layui-this');
                        }
                        rlkssjxy.config.min = {
                            year: date.year,
                            month: date.month - 1,
                            date: date.date,
                            hours: date.hours,
                            minutes: date.minutes,
                            seconds: date.seconds
                        }
                    }
                });
                var rlkssjxy = laydate.render({
                    elem: '#rlkssjxy',
                    type: 'datetime',
                    trigger: 'click'
                });
                if (lcPageEdition == 'nt') {
                    var cnlzkssjdy = laydate.render({
                        elem: '#cnlzkssjdy',
                        trigger: 'click',
                        done: function (value, date) {
                            //选择的结束时间大
                            if ($('#cnlzkssjxy').val() != '' && !completeDate($('#cnlzkssjxy').val(), value)) {
                                $('#cnlzkssjxy').val('');
                                $('.laydate-disabled.layui-this').removeClass('layui-this');
                            }
                            cnlzkssjxy.config.min = {
                                year: date.year,
                                month: date.month - 1,
                                date: date.date
                            }
                        }
                    });
                    var cnlzkssjxy = laydate.render({
                        elem: '#cnlzkssjxy',
                        trigger: 'click'
                    });

                    var ybcnlzkssjdy = laydate.render({
                        elem: '#ybcnlzkssjdy',
                        trigger: 'click',
                        done: function (value, date) {
                            //选择的结束时间大
                            if ($('#cnlzkssjxy').val() != '' && !completeDate($('#cnlzkssjxy').val(), value)) {
                                $('#cnlzkssjxy').val('');
                                $('.laydate-disabled.layui-this').removeClass('layui-this');
                            }
                            ybcnlzkssjxy.config.min = {
                                year: date.year,
                                month: date.month - 1,
                                date: date.date
                            }
                        }
                    });
                    var ybcnlzkssjxy = laydate.render({
                        elem: '#ybcnlzkssjxy',
                        trigger: 'click'
                    });

                    var xmcnlzkssjdy = laydate.render({
                        elem: '#xmcnlzkssjdy',
                        trigger: 'click',
                        done: function (value, date) {
                            //选择的结束时间大
                            if ($('#cnlzkssjxy').val() != '' && !completeDate($('#cnlzkssjxy').val(), value)) {
                                $('#cnlzkssjxy').val('');
                                $('.laydate-disabled.layui-this').removeClass('layui-this');
                            }
                            xmcnlzkssjxy.config.min = {
                                year: date.year,
                                month: date.month - 1,
                                date: date.date
                            }
                        }
                    });
                    var xmcnlzkssjxy = laydate.render({
                        elem: '#xmcnlzkssjxy',
                        trigger: 'click'
                    });

                    var rlcnlzkssjdy = laydate.render({
                        elem: '#rlcnlzkssjdy',
                        trigger: 'click',
                        done: function (value, date) {
                            //选择的结束时间大
                            if ($('#cnlzkssjxy').val() != '' && !completeDate($('#cnlzkssjxy').val(), value)) {
                                $('#cnlzkssjxy').val('');
                                $('.laydate-disabled.layui-this').removeClass('layui-this');
                            }
                            rlcnlzkssjxy.config.min = {
                                year: date.year,
                                month: date.month - 1,
                                date: date.date
                            }
                        }
                    });
                    var rlcnlzkssjxy = laydate.render({
                        elem: '#rlcnlzkssjxy',
                        trigger: 'click'
                    });
                }
            });
        }

        function completeDate(date1, date2) {
            var oDate1 = new Date(date1);
            var oDate2 = new Date(date2);
            if (oDate1.getTime() > oDate2.getTime()) {
                return true;
            } else {
                return false;
            }
        }

        var $paramArr = getIpHz();
        var clientId = $paramArr['clientId'];
        // 模块编码
        var moduleCode = $paramArr['moduleCode'];
        // 加载登记首页
        var loadHome = $paramArr['loadHome'];
        var viewType = $paramArr['viewType'];
        var authorityCode = '';
        console.log('moduleCode', moduleCode);
        //按钮权限
        //  $.ajax({
        //      type: "GET",
        //      url: getContextPath() + "/rest/v1.0/workflow/process-instances/module/authority",
        //      data: {moduleCode: moduleCode},
        //      success: function (data) {
        //          authorityCode = data;
        //          moduleAuthority.load({
        //              authorityCode: data
        //          });
        //      }, error: function (e) {
        //          response.fail(e, '');
        //      }
        //  });

        //监听台账查询 input 点击
        $('.layui-form-item .layui-input-inline').on('click', '.layui-icon-close', function () {
            $(this).siblings('.layui-input').val('');
        });

        $('.layui-form-item .layui-input-inline .layui-input').on('focus', function () {
            $(this).siblings('.layui-icon-close').removeClass('bdc-hide');
        }).on('blur', function () {
            var $this = $(this);
            setTimeout(function () {
                $this.siblings('.layui-icon-close').addClass('bdc-hide');
            }, 150)
        });

        //4. 轮播图
        //4.0 渲染常用收藏内容
        var commonConnection = false;
        var connectionList = false;
        //监听收藏tab切
        var taskTabClick = 0;
        var taskTabListClick = 0;
        //监听收藏任务tab切换
        element.on('tab(task)', function (data) {
            if (taskTabClick == 0 && data.index == 0) {
                taskTabClick++;
                renderCommonCollection();
            }
            if (taskTabListClick == 0 && data.index == 1) {
                taskTabListClick++;
                renderLbList();
            }
            if ($('.bdc-task-content').hasClass('bdc-hide')) {
                $('.bdc-operate-show>.layui-icon').toggleClass('bdc-hide');
                $('.bdc-task-content').toggleClass('bdc-hide');
                // $('.bdc-task-search-box').toggleClass('bdc-hide');
            }

            switch (data.index) {
                case 0:
                    if (connectionList) {
                        connectionList = false;
                        renderCommonCollection();
                    }
                    break;
                case 1:
                    if (commonConnection) {
                        commonConnection = false;
                        renderLbList();
                    }
                    break;
            }

            changeTableHeight();
        });
        //renderCommonCollection();

        var hasMove = false, isStarClick = false;

        function renderCommonCollection() {
            //          $.ajax({
            //              type: "GET",
            //              url: getContextPath() + "/rest/v1.0/collect",
            //              dataType: "json",
            //              contentType: "application/json",
            //              data: {},
            //              success: function (data) {
            //                  var getCollectionTpl = collectionTpl.innerHTML
            //                      , collectionView = document.getElementById('collectionView');
            //                  laytpl(getCollectionTpl).render(data, function (html) {
            //                      collectionView.innerHTML = html;
            //                  });
            //                  changeTableHeight();
            //
            //                  var oUl = document.getElementById("collectionContent");
            //                  if (!isNullOrEmpty(oUl)) {
            //                      var aLi = oUl.getElementsByTagName("li");
            //                      var disX = 0;
            //                      var disY = 0;
            //                      var minZindex = 1;
            //                      var aPos = [];
            //                      for (var i = 0; i < aLi.length; i++) {
            //                          var t = aLi[i].offsetTop;
            //                          var l = aLi[i].offsetLeft;
            //                          aLi[i].style.top = t + "px";
            //                          aLi[i].style.left = l + "px";
            //                          aPos[i] = {left: l, top: t};
            //                          aLi[i].index = i;
            //                      }
            //                      for (var i = 0; i < aLi.length; i++) {
            //                          aLi[i].style.position = "absolute";
            //                          aLi[i].style.margin = 0;
            //                          setDrag(aLi[i]);
            //                      }
            //                  }
            //                  var moveL = 99999;
            //                  var moveT = 99999;
            //                  var objL = 0;
            //                  var objT = 0;
            //
            //                  function setDrag(obj) {
            //                      obj.onmousedown = function (event) {
            //                          hasMove = false;
            //                          var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            //                          var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
            //                          obj.style.zIndex = minZindex++;
            //                          //当鼠标按下时计算鼠标与拖拽对象的距离
            //                          disX = event.clientX + scrollLeft - obj.offsetLeft;
            //                          disY = event.clientY + scrollTop - obj.offsetTop;
            //
            //                          objL = obj.style.left;
            //                          objL = objL.substring(0, objL.length - 2);
            //
            //                          objT = obj.style.top;
            //                          objT = objT.substring(0, objT.length - 2);
            //                          document.onmousemove = function (event) {
            //                              hasMove = true;
            //                              //当鼠标拖动时计算div的位置
            //                              var l = event.clientX - disX + scrollLeft;
            //                              var t = event.clientY - disY + scrollTop;
            //                              obj.style.left = l + "px";
            //                              obj.style.top = t + "px";
            //                              // console.log('l:'+l+'t:'+t);
            //
            //                              moveL = event.clientX - disX + scrollLeft;
            //                              // console.log(moveL);
            //                              moveT = event.clientY - disY + scrollTop;
            //                              for (var i = 0; i < aLi.length; i++) {
            //                                  aLi[i].className = "";
            //                              }
            //                              var oNear = findMin(obj);
            //                              if (oNear) {
            //                                  oNear.className = "active";
            //                              }
            //                          };
            //                          document.onmouseup = function () {
            //                              document.onmousemove = null;//当鼠标弹起时移出移动事件
            //                              document.onmouseup = null;//移出up事件，清空内存
            //                              if (hasMove) {
            //                                  //console.log('moveL:'+moveL,"objL:"+objL);
            //                                  //console.log('moveT:'+moveT,"objT:"+objT);
            //                                  if (((moveL != 99999 || moveT != 99999) && (moveL != objL && moveL > objL + 10)) || ((moveL != 99999 || moveT != 99999) && (moveT != objT && moveT > objT - 10)) || ((moveL != 99999 || moveT != 99999) && (moveT != objT && moveT < objT + 10)) || ((moveL != 99999 || moveT != 99999) && (moveL != objL && moveL < objL - 10))) {
            //                                      //console.log('aaa');
            //                                      //检测是否普碰上，在交换位置
            //                                      var oNear = findMin(obj);
            //                                      if (oNear) {
            //                                          //console.log(oNear);//被移动的
            //                                          //console.log(obj);//移动的
            //                                          moveCollection(oNear, obj);
            //                                          oNear.className = "";
            //                                          oNear.style.zIndex = minZindex++;
            //                                          obj.style.zIndex = minZindex++;
            //                                          startMove(oNear, aPos[obj.index]);
            //                                          startMove(obj, aPos[oNear.index]);
            //                                          //交换index
            //                                          oNear.index += obj.index;
            //                                          obj.index = oNear.index - obj.index;
            //                                          oNear.index = oNear.index - obj.index;
            //                                      } else {
            //
            //                                          startMove(obj, aPos[obj.index]);
            //                                      }
            //                                  } else {
            //                                      obj.style.left = objL + "px";
            //                                      obj.style.top = objT + "px";
            //                                      $('#collectionContent').find('li').removeClass('active');
            //                                  }
            //                              } else {
            //                                  //console.log("没有移动鼠标松开事件,模拟click");
            //                                  setTimeout(function () {
            //                                      if (!isStarClick) {
            //                                          var useData = {
            //                                              processDefKey: $(obj).find('a .layui-icon').data('code'),
            //                                              name: $(obj).find('a .layui-icon').data('name')
            //                                          };
            //                                          workflow.startUpProcess(useData);
            //                                      }
            //                                      isStarClick = false;
            //                                  },200);
            //                              }
            //                              hasMove = false;
            //                          };
            //                          clearInterval(obj.timer);
            //                          return false;//低版本出现禁止符号
            //                      }
            //                  }
            //                  //碰撞检测
            //                  function colTest(obj1, obj2) {
            //                      var t1 = obj1.offsetTop;
            //                      var r1 = obj1.offsetWidth + obj1.offsetLeft;
            //                      var b1 = obj1.offsetHeight + obj1.offsetTop;
            //                      var l1 = obj1.offsetLeft;
            //
            //                      var t2 = obj2.offsetTop;
            //                      var r2 = obj2.offsetWidth + obj2.offsetLeft;
            //                      var b2 = obj2.offsetHeight + obj2.offsetTop;
            //                      var l2 = obj2.offsetLeft;
            //
            //                      if (t1 > b2 || r1 < l2 || b1 < t2 || l1 > r2) {
            //                          return false;
            //                      } else {
            //                          return true;
            //                      }
            //                  }
            //                  //勾股定理求距离
            //                  function getDis(obj1, obj2) {
            //                      var a = obj1.offsetLeft - obj2.offsetLeft;
            //                      var b = obj1.offsetTop - obj2.offsetTop;
            //                      return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            //                  }
            //                  //找到距离最近的
            //                  function findMin(obj) {
            //                      var minDis = 999999999;
            //                      var minIndex = -1;
            //                      for (var i = 0; i < aLi.length; i++) {
            //                          if (obj == aLi[i]) continue;
            //                          if (colTest(obj, aLi[i])) {
            //                              var dis = getDis(obj, aLi[i]);
            //                              if (dis < minDis) {
            //                                  minDis = dis;
            //                                  minIndex = i;
            //                              }
            //                          }
            //                      }
            //                      if (minIndex == -1) {
            //                          return null;
            //                      } else {
            //                          return aLi[minIndex];
            //                      }
            //                  }
            //              },
            //              error: function (e) {
            //                  response.fail(e,'');
            //              }
            //          });
        }

        /**
         * 拖拽收藏
         * @param target 目标位置上原有的对象
         * @param moved 移动的对象
         */
        function moveCollection(target, moved) {
            target = target.dataset;
            moved = moved.dataset;
            console.info(target.sequencenumber);
            console.info(moved.sequencenumber);
            $.ajax({
                type: "PUT",
                url: getContextPath() + "/rest/v1.0/collect/update",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify([
                    {
                        "id": target.id,
                        "code": target.code,
                        "name": target.name,
                        "content": target.content,
                        "type": target.type,
                        "typeName": target.typename,
                        "sequenceNumber": target.sequencenumber,
                        "createAt": target.createat
                    },
                    {
                        "id": moved.id,
                        "code": moved.code,
                        "name": moved.name,
                        "content": moved.content,
                        "type": moved.type,
                        "typeName": moved.typename,
                        "sequenceNumber": moved.sequencenumber,
                        "createAt": moved.createat
                    }]
                ),
                success: function (data) {
                    renderCommonCollection();
                },
                error: function (e) {
                    response.fail(e, '');
                }
            });
        }

        //监听常用收藏中的收起，展示
        var isShowMoreCellection = false;
        $('.bdc-task-content').on('click', '.bdc-show-more-collection span', function () {
            isShowMoreCellection = !isShowMoreCellection;
            $('.bdc-show-more-collection span').toggleClass('bdc-hide');
            if (isShowMoreCellection) {
                var newHeight = Math.ceil($('.bdc-collection-content li').length / 4) * 26;
                $('.bdc-collection-content').css("height", newHeight + 'px');
            } else {
                $('.bdc-collection-content').css('height', '52px');
            }
        });

        //4.0.1 点击收藏中的星星图标，取消收藏
        $('.bdc-collection-tab').on('click', '.bdc-collection-content .layui-icon-rate-solid', function (e) {
            e.stopPropagation();
            e.preventDefault();
            isStarClick = true;
            commonConnection = true;
            var code = $(this).data('code');
            //          $.ajax({
            //              type: "post",
            //              url: getContextPath() + "/rest/v1.0/collect",
            //              data: {
            //                  _method: "DELETE",
            //                  "code": code
            //              },
            //              success: function (data) {
            //                  layer.msg('取消收藏成功');
            //                  renderCommonCollection();
            //              }, error: function (e) {
            //                  response.fail(e,'');
            //              }
            //          });
        });

        //4.1 轮播图 内容、一屏展示几个，可配置
        //默认一屏展示5个，大数组里面嵌套数组，展示几个，嵌套数组中存储几条数据
        renderLbList();

        function renderLbList() {
            $.ajax({
                type: "GET",
                url: getContextPath() + "/index/list",
                data: {},
                success: function (data) {
                    //var list = [];
                    //if (data && data.content) {
                    //    for (var i = 0; i < data.content.length; i++) {
                    //        for (j = 0; j < data.content[i].processList.length; j++) {
                    //            list.push(data.content[i].processList[j]);
                    //        }
                    //    }
                    //    var carouselData = [];
                    //    for (var i = 0; i < Math.ceil(list.length / carouselCount); i++) {
                    //        carouselData.push([]);
                    //    }
                    //    list.forEach(function (v, i) {
                    //        var getIndex = i + 1;
                    //        carouselData[Math.ceil(getIndex / carouselCount) - 1].push(v);
                    //    });
                    //    if (carouselData.length == 0) {
                    //        $('#carouselView').hide();
                    //    }
                    //    var getCarouselTpl = carouselTpl.innerHTML
                    //        , getCarouselView = document.getElementById('carouselView');
                    //    laytpl(getCarouselTpl).render(carouselData, function (html) {
                    //        console.log('carouselData:', carouselData);
                    //        getCarouselView.innerHTML = html;
                    //    });

                    if (data && data.content) {
                        var carouselData = [];
                        for (var i = 0; i < Math.ceil(data.content.length / carouselCount); i++) {
                            carouselData.push([]);
                        }
                        data.content.forEach(function (v, i) {
                            var getIndex = i + 1;
                            carouselData[Math.ceil(getIndex / carouselCount) - 1].push(v);
                        });
                        //console.log(carouselData);
                        var getCarouselTpl = carouselTpl.innerHTML
                            , getCarouselView = document.getElementById('carouselView');
                        laytpl(getCarouselTpl).render(carouselData, function (html) {
                            getCarouselView.innerHTML = html;
                        });
                        if (carouselData.length > 0) {
                            carousel.render({
                                elem: '#buildTask',
                                width: '100%',
                                height: carouselData.length * 50 + 'px',
                                arrow: 'always',
                                indicator: 'outside',
                                autoplay: false
                            });
                        } else {
                            $(".bdc-container-tab[lay-filter='task']").css("display","none");
                            $(".bdc-list-tab").css("margin",0);
                            changeTableHeight();
                            // div.on('task',function () {
                            //     this.innerHTML = "12321312";
                            // })
                        }

                    }


                },
                error: function (e) {
                    response.fail(e, '');
                }
            });
        }

        //4.2 轮播图内鼠标覆盖事件
        var $carouselTask = $('#buildTask');

        //4.3 轮播图内单击事件
        var $buildTask = $('.bdc-build-task');
        $buildTask.on('click', '.bdc-carousel-son', function (event) {
            event.stopPropagation();
            $('.bdc-carousel-son').removeClass('bdc-carousel-son-this');
            $(this).addClass('bdc-carousel-son-this');
            $('.bdc-carousel-details').addClass('bdc-hide');
            var lbIndex = Math.ceil(($(this).index() + 1) / carouselCount);
            var lineIndex = Math.ceil(($(this).index()) % carouselline);
            $(this).find('.bdc-carousel-details').css('left', -lbIndex * (lineIndex * $(this).width() + lineIndex * 10) + 'px').removeClass('bdc-hide');


        });

        //4.4 控制tab切内容显示与隐藏
        var taskSearchState = true;//在tab展示的时候点击的查询
        $('.bdc-operate-show>.layui-icon').on('click', function () {
            if ($('.bdc-search-content').hasClass('bdc-hide')) {
                $('.bdc-operate-show>.layui-icon').toggleClass('bdc-hide');
                $('.bdc-task-content').toggleClass('bdc-hide');

                if (taskTabClick == 0) {
                    if ($('.bdc-task-tab .layui-tab-title li:first-child').hasClass('layui-this')) {
                        renderCommonCollection();
                    } else {
                        renderLbList();
                    }
                }

                if ($('.bdc-task-tab .layui-tab-title li:first-child').hasClass('layui-this')) {
                    if (connectionList) {
                        connectionList = false;
                        renderCommonCollection();
                    }
                } else {
                    if (commonConnection) {
                        commonConnection = false;
                        renderLbList();
                    }
                }
            } else {
                $('.bdc-task-tab .bdc-task-tools').css('width', taskToolsWidth);
                $('.bdc-task-tab .layui-tab-title li').css('visibility', 'visible');

                $('.bdc-task-content').addClass('bdc-hide');
                $('.bdc-operate-show>.layui-icon').toggleClass('bdc-hide');
                $('.bdc-search-content').addClass('bdc-hide');
            }
            changeTableHeight();
        });
        //4.5 轮播图详情页
        //4.5.1 单击 × 按钮，关闭详情
        $carouselTask.on('click', '.bdc-carousel-close', function (event) {
            event.stopPropagation();
            $(this).parents('.bdc-carousel-son').removeClass('bdc-carousel-son-this');
            $(this).parent().addClass('bdc-hide');
        });

        //点击页面空白处 隐藏新建任务详情弹出层
        $('body').on('click', function () {
            $('.bdc-carousel-details').addClass('bdc-hide');
            $('.bdc-carousel-son').removeClass('bdc-carousel-son-this');
        });
        //4.5.2 ☆ 图标显示与隐藏
        //      var $buildCollectionTask = $('#collectionView');
        //      var $buildTaskTab = $('.bdc-task-tab');
        //      var $searchContent = $('.bdc-search-content');
        //      $buildTaskTab.on('mouseenter', '.bdc-details-type-content>a', function () {
        //          if (!$(this).find('.layui-icon').hasClass('layui-icon-rate-solid')) {
        //              $(this).find('.layui-icon').toggleClass('bdc-visible');
        //          }
        //      }).on('mouseleave', '.bdc-details-type-content>a', function () {
        //          if (!$(this).find('.layui-icon').hasClass('layui-icon-rate-solid')) {
        //              $(this).find('.layui-icon').toggleClass('bdc-visible');
        //          }
        //      });
        //4.5.3 单击☆图标，收藏或取消收藏
        //      $buildTask.on('click', '.bdc-details-type-content>a>.layui-icon', function (event) {
        //          event.stopPropagation();
        //          connectionList = true;
        //          var _this = $(this);
        //          var type = $(this).data('type');
        //          var code = $(this).data('code');
        //          var name = $(this).data('name');
        //          var id = $(this).data('id');
        //          var date = (new Date()).Format("yyyy-MM-dd hh:mm:ss.S");
        //          if ($(this).hasClass('layui-icon-rate')) {
        //              //未收藏，收藏
        //              $.ajax({
        //                  type: "PUT",
        //                  url: getContextPath() + "/rest/v1.0/collect/user",
        //                  dataType: "json",
        //                  contentType: "application/json",
        //                  data: JSON.stringify({
        //                      "code": code,
        //                      "content": "",
        //                      "createAt": date,
        //                      "id": id,
        //                      "name": name,
        //                      "typeName": name
        //                  }),
        //                  success: function (data) {
        //                      layer.msg('收藏成功');
        //                      _this.removeClass('layui-icon-rate bdc-visible').addClass('layui-icon-rate-solid');
        //                  }, error: function (e) {
        //                      response.fail(e,'');
        //                  }
        //              });
        //          } else {
        //              //已收藏 取消收藏
        //              $.ajax({
        //                  type: "post",
        //                  url: getContextPath() + "/rest/v1.0/collect",
        //                  data: {
        //                      _method: "DELETE",
        //                      "code": code
        //                  },
        //                  success: function (data) {
        //                      // console.log(data);
        //                      layer.msg('取消收藏成功');
        //                      _this.removeClass('layui-icon-rate-solid bdc-visible').addClass('layui-icon-rate');
        //                  }, error: function (e) {
        //                      response.fail(e,'');
        //                  }
        //              });
        //          }
        //      });
        //      $searchContent.on('click', '.bdc-details-type-content>a', function () {
        //          var useData = {
        //              processDefKey: $(this).find('.layui-icon').data('code'),
        //              name: $(this).find('.layui-icon').data('name')
        //          };
        //          workflow.startUpProcess(useData);
        //      });
        //      $searchContent.on('click', '.bdc-details-type-content>a>.layui-icon', function (e) {
        //          e.stopPropagation();
        //          commonConnection = true;
        //          connectionList = true;
        //          var _this = $(this);
        //          var type = $(this).data('type');
        //          var code = $(this).data('code');
        //          var name = $(this).data('name');
        //          var id = $(this).data('id');
        //          var date = (new Date()).Format("yyyy-MM-dd hh:mm:ss.S");
        //          if ($(this).hasClass('layui-icon-rate')) {
        //              //未收藏，收藏
        //              $.ajax({
        //                  type: "PUT",
        //                  url: getContextPath() + "/rest/v1.0/collect/user",
        //                  dataType: "json",
        //                  contentType: "application/json",
        //                  data: JSON.stringify({
        //                      "code": code,
        //                      "content": "",
        //                      "createAt": date,
        //                      "id": id,
        //                      "name": name,
        //                      "typeName": name
        //                  }),
        //                  success: function (data) {
        //                      // console.log(data);
        //                      layer.msg('收藏成功');
        //                      _this.removeClass('layui-icon-rate bdc-visible').addClass('layui-icon-rate-solid');
        //                  }, error: function (e) {
        //                      response.fail(e,'');
        //                  }
        //              });
        //          } else {
        //              //已收藏 取消收藏
        //              $.ajax({
        //                  type: "post",
        //                  url: getContextPath() + "/rest/v1.0/collect",
        //                  data: {
        //                      _method: "DELETE",
        //                      "code": code
        //                  },
        //                  success: function (data) {
        //                      layer.msg('取消收藏成功');
        //                      _this.removeClass('layui-icon-rate-solid bdc-visible').addClass('layui-icon-rate');
        //                  }, error: function (e) {
        //                      response.fail(e,'');
        //                  }
        //              });
        //          }
        //      });

        //4.5.4 单击 收起 或 展开
        $carouselTask.on('click', '.bdc-details-type-tool>.bdc-retract', function (event) {
            event.stopPropagation();
            $(this).addClass('bdc-hide').siblings().removeClass('bdc-hide').parents('.bdc-details-type').find('.bdc-details-type-content').height('30px');
        });
        $buildTask.on('click', '.bdc-details-type-tool>.bdc-open', function (event) {
            event.stopPropagation();
            $(this).addClass('bdc-hide').siblings().removeClass('bdc-hide').parents('.bdc-details-type').find('.bdc-details-type-content').height('auto');
        });
        //4.5.5 单击二级小类
        $buildTask.on('click', '.bdc-details-type-content> a', function (event) {

            var wdid = $(this).next().prevObject.context.children[1].dataset.id
            $.ajax({
                type: "GET",
                url: getContextPath() + "/index/createTask?wdid=" + wdid,
                success: function (data) {
                    var ywlx = data.name == '规划测绘成果入库' ? 'GHJG' : data.name == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                    console.log('轮播图Data：', data);
                    var index = window.open("../view/service-page.html?taskid=" + data.taskId + "&xmid=" + data.executionId + "&gzlslid=" + data.processInstanceId + "&formKey=" + data.formKey + "&type=db" + '&wdid=' + wdid + '&ywlx=' + ywlx);
                    newWin.push(index);
                }
            })
        });

        /**
         * 创建流程
         */
        function startUpProcess(useData) {
            // 如果 fdjywlc 为空，则获取配置信息 （未配置默认返回 'empty' 字符串）
            if (isNullOrEmpty(fdjywlc)) {
                $.ajax({
                    type: "GET",
                    url: getContextPath() + "/rest/v1.0/workflow/process-instances/getFdjywlc",
                    data: {},
                    async: false,
                    success: function (data) {
                        fdjywlc = data;
                    }, error: function (e) {
                        response.fail(e, '');
                    }
                });
            }
            // 有值则不去后台获取
            if (useData.processDefKey == fdjywlc) {
                workflow.startUpFdjywProcess(useData);
            } else {
                workflow.startUpProcess(useData);
            }
        }

        // $buildCollectionTask.on('click', '.bdc-collection-content>li>a', function () {
        //     var useData = {
        //         processDefKey: $(this).find('.layui-icon').data('code'),
        //         name: $(this).find('.layui-icon').data('name')
        //     };
        //     startUpProcess(useData);
        // });

        //4.5.6 点击搜索 input框
        $('.bdc-task-search-box .layui-input').on('focus', function () {
            if ($('.bdc-task-content').hasClass('bdc-hide')) {
                taskSearchState = false;
                $('.bdc-operate-show .layui-icon').toggleClass('bdc-hide');
            }

            $(this).siblings('.layui-icon-close').removeClass('bdc-hide');
            $('.bdc-task-tab .bdc-task-tools').animate({'width': taskToolsClickWidth});
            $('.bdc-task-tab>.layui-tab-title>li').css('visibility', 'hidden');
            $('.bdc-task-content').addClass('bdc-hide');
            $('.bdc-search-content').removeClass('bdc-hide');

            changeTableHeight();
        });

        //收藏监听回车事件
        var isSearch = true;
        $('.bdc-task-search-box .layui-input').bind('keyup', function (event) {
            if (event.keyCode == "13") {
                //回车执行查询
                $('.bdc-search-content').html('');
                var inputText = $(this).val();
                if (!isNullOrEmpty(inputText)) {
                    renderSearchList(inputText);
                }
            }
        });
        $('.bdc-task-search-box .layui-input').on('focus', function () {
            isSearch = false;
        }).on('blur', function () {
            isSearch = true;
        });
        $('.bdc-table-box').on('focus', '.layui-laypage-skip .layui-input', function () {
            isSearch = false;
        }).on('blur', '.layui-laypage-skip .layui-input', function () {
            isSearch = true;
        });

        //表格监听回车事件
        // $('.layui-input-inline .layui-input').bind('keyup', function (event) {
        //     if (event.keyCode == "13") {
        //         $(".layui-show .bdc-search-box .searchBtn").click();
        //     }
        // });
        var rlLayerIndex;
        $(document).keydown(function (event) {
            if (event.keyCode == "13") {
                if (isSearch) {
                    if ($('.bdc-zf-tips').length > 0) {
                        $('.layui-layer-btn0').click();
                        layer.close(rlLayerIndex);
                    } else {
                        $(".layui-show .bdc-search-box .searchBtn").click();
                    }
                }
            }
        });

        //监听上一个下一个
        var upTime;
        $('.bdc-content-box').on('mousewheel DOMMouseScroll', '.bdc-build-task', function (e) {
            clearInterval(upTime);
            var delta = -e.originalEvent.wheelDelta || e.originalEvent.detail;//firefox使用detail:下3上-3,其他浏览器使用wheelDelta:下-120上120//下滚
            if (delta > 0) {
                upTime = setTimeout(function () {
                    //console.log('下滚');
                    $('.layui-carousel-arrow[lay-type=add]').click();
                }, 300);
            }
            //上滚
            if (delta < 0) {
                upTime = setTimeout(function () {
                    //console.log('上滚');
                    $('.layui-carousel-arrow[lay-type=sub]').click();
                }, 300);
            }
        });


        //监听 搜索
        /*$('.bdc-task-search-box .layui-input').bind('input propertychange', function (event) {
         var inputText = $(this).val();
         if (inputText != '' && inputText != null) {
         renderSearchList(inputText);
         }
         });*/

        //4.5.7 点击搜索框的删除
        $('.bdc-task-search-box .layui-icon-close').on('click', function () {
            $('.bdc-task-search-box .layui-input').val('');
            $(this).addClass('bdc-hide');
            $('.bdc-search-content').addClass('bdc-hide').html('');

            if (!($('.bdc-task-tools').css('width') == taskToolsWidth)) {
                $('.bdc-task-tab .bdc-task-tools').animate({'width': taskToolsWidth});
                $('.bdc-task-tab>.layui-tab-title>li').css('visibility', 'visible');
                if (taskSearchState) {
                    $('.bdc-task-content').removeClass('bdc-hide');
                } else {
                    $('.bdc-operate-show .layui-icon').toggleClass('bdc-hide');
                }
            }

            var $collectionTabHtml = $('.bdc-task-tab .layui-this').html();
            if (commonConnection && $collectionTabHtml == '新建任务') {
                commonConnection = false;
                renderLbList();
            }
            if (connectionList && $collectionTabHtml == '常用收藏') {
                connectionList = false;
                renderCommonCollection();
            }

            changeTableHeight();
        });


        //6. 渲染表格
        // 6.1 待办表格
        var waitUrl = getContextPath() + "/index/getTaskList";
        var waitTableId = '#waitTable';
        var waitCurrentId = 'dbTable';
        var waitToolbar = '#toolbarDemo';
        renderWaitTable(waitUrl, waitTableId, waitCurrentId, waitToolbar);

        // 6.2 已办表格
        var doneUrl = getContextPath() + "/index/getTaskOverList";
        //var doneUrl = "http://192.168.50.38/index/getTaskOverList";
        var doneTableId = '#doneTable';
        var doneCurrentId = 'doneTableId';
        var doneToolbar = '#toolbarDoneDemo';
        renderDoneTable(doneTableId, doneCurrentId, doneToolbar);
        // 6.3 项目列表表格
        var listUrl = getContextPath() + "/index/getProjectList";
        //var listUrl = "http://192.168.50.38/index/getProjectList";
        var listTableId = '#listTable';
        var listCurrentId = 'listTableId';
        var allToolbar = '#toolbarAllDemo';
        renderListTable(listTableId, listCurrentId, allToolbar);


        //重置第一个
        switch (tabList[0]) {
            case "xmContent":
                refreshTab(listUrl, listCurrentId, authorityCode);
                /*              var param = {};
                              param.queryString = '{"_query":"_query","TASK_STATE":""}';
                              $.ajax({
                                  type : 'post',
                                  url :'http://localhost:8081/index/getTaskParam',
                                  async: false,
                                  success:function(data){
                                      param.splitInfo = data.taskList;
                                      $.ajax({
                                          type:'post',
                                          url:"http://localhost:8080/platform/tag/SplitData.action",
                                          data:param,
                                          crossDomain:true,
                                          xhrFields: {
                                              withCredentials: true
                                          },
                                          dataType: 'jsonp',
                                          success:function(result){
                                              consle.log(result);
                                          }
                                      });
                                  }
                              });*/


                break;
            case "ybContent":
                refreshTab(doneUrl, doneCurrentId, authorityCode);
                break;

        }

        var doneIndex = 0,
            listIndex = 0,
            rlindex = 0,
            waitIndex = 0;
        grListIndex = 0;

        //监听第一次单击任务栏tab，重构表格尺寸
        element.on('tab(listFilter)', function (data) {
            if (data.index == 0) {
                switch (tabList[0]) {
                    case "dbContent":
                        refreshTab(waitUrl, waitCurrentId, authorityCode);
                        break;
                    case "xmContent":
                        table.resize(listCurrentId);
                        refreshTab(listUrl, listCurrentId, authorityCode);
                        break;
                    case "ybContent":
                        table.resize(doneCurrentId);
                        refreshTab(doneUrl, doneCurrentId, authorityCode);
                        break;

                }
            } else {
                switch (tabList[data.index]) {
                    case "dbContent":
                        if (waitIndex == 0) {
                            waitIndex++;
                            table.resize(waitCurrentId);
                        }
                        refreshTab(waitUrl, waitCurrentId, authorityCode);
                        break;
                    case "xmContent":
                        if (listIndex == 0) {
                            listIndex++;
                            table.resize(listCurrentId);
                        }
                        refreshTab(listUrl, listCurrentId, authorityCode);
                        break;
                    case "ybContent":
                        if (doneIndex == 0) {
                            doneIndex++;
                            table.resize(doneCurrentId);
                        }
                        refreshTab(doneUrl, doneCurrentId, authorityCode);
                        break;

                }
            }
        });

        // 首页选择已办跳转模拟点击操作
        if (viewType === "complete") {
            $(".bdc-list-tab li:nth-child(2)").click();
        }

        //6.4 待办任务头工具栏事件
        table.on('toolbar(waitTableFilter)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            if (obj.event != "LAYTABLE_COLS") {
                var selectedNum = checkStatus.data.length;
                var deleteEvent = (obj.event == "delete-process" || obj.event == "hang-up-process" || obj.event == "active-process"
                    || obj.event == "cancel-process" || obj.event == "back-process");
                if (obj.event == 'forward-process' && selectedNum > 1) {
                    if (isPlzf !== 0 || isPlzf !== 1) {
                        // 判断是否配置了自动转发
                        $.ajax({
                            url: getContextPath() + "/rest/v1.0/workflow/process-instances/isPlZf",
                            dataType: "json",
                            async: false,
                            success: function (data) {
                                console.log('zfdata:', data);
                                isPlzf = data;
                            }
                        });
                    }
                    if (isPlzf === 1) {
                        obj.event = "forward-pl-process";
                    }
                    if (selectedNum != 1 && obj.event != "forward-pl-process") {
                        layer.msg('请选择一条数据进行转发！');
                        return false;
                    }
                } else {
                    if ((selectedNum != 1 && !deleteEvent) || (selectedNum == 0 && deleteEvent)) {
                        layer.msg('请选择一条数据进行操作！');
                        return false;
                    }
                }
            }
            var checkData = checkStatus.data[0];
            console.log('checkData:', checkData);
            switch (obj.event) {
                case 'forward-process':
                    addModel();
                    setTimeout(function () {
                        workflow.forwardauto(checkStatus, waitUrl, waitTableId, waitCurrentId, true, checkData.processInstanceId);
                    }, 50);

                    break;
                case 'cancel-process':
                    if (selectedNum == 1) {
                        workflow.cancelClaimProcess(checkData, waitUrl, waitTableId, waitCurrentId, true);
                    } else {
                        workflow.cancelClaimPlProcess(checkStatus.data, waitUrl, waitTableId, waitCurrentId, true);
                    }
                    break;
                case 'back-process':
                    if (selectedNum == 1) {
                        //待办表格操作按钮权限
                        getReturnData("/index/getButtnAuth", {
                            'taskid': checkData.taskId,
                            'zt': "db"
                        }, "GET", function (data) {
                            if (data.hasBack) {
                                workflow.backProcess(checkData, waitUrl, waitTableId, waitCurrentId, true);
                            } else {
                                layer.msg('当前节点不可退回！');
                            }
                        }, true);

                    } else {
                        layer.msg('请选择一条数据！');
                        //workflow.backPlProcess(checkStatus, waitUrl, waitTableId, waitCurrentId, true);
                    }
                    break;
                case 'active-process':
                    // 激活流程
                    workflow.activeProcess(checkStatus, waitUrl, waitTableId, waitCurrentId);
                    break;
                case 'delete-process':
                    // 删除当前任务
                    //待办表格操作按钮权限
                    getReturnData("/index/getButtnAuth", {
                        'taskid': checkData.taskId,
                        'zt': "db"
                    }, "GET", function (data) {
                        if (data.hasDel) {
                            workflow.deleteProcess(checkStatus, waitUrl, waitTableId, waitCurrentId, true);
                        } else {
                            layer.msg('当前节点不可删除！');
                        }
                    }, true);
                    break;
                case 'hang-up-process':
                    workflow.hangUpProcess(checkStatus, waitUrl, waitTableId, waitCurrentId);
                    break;
                case 'forward-pl-process':
                    //批量转发
                    workflow.forwardPlProcess(checkStatus, waitUrl, waitTableId, waitCurrentId);
                    break;
            }
        });

        //项目列表工具栏
        table.on('toolbar(listTableFilter)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var selectedNum = checkStatus.data.length;
            if (selectedNum == 0) {
                layer.msg('请选择一条数据进行操作！');
                return false;
            }
            switch (obj.event) {
                case 'delete-process':
                    // 删除当前任务
                    workflow.deleteProcess(checkStatus, listUrl, listTableId, listCurrentId, true);
                    break;
            }
        });


        //个人项目列表工具栏
        //      table.on('toolbar(grListTableFilter)', function (obj) {
        //          var checkStatus = table.checkStatus(obj.config.id);
        //          var selectedNum = checkStatus.data.length;
        //          if (selectedNum == 0) {
        //              layer.msg('请选择一条数据进行操作！');
        //              return false;
        //          }
        //          switch (obj.event) {
        //              case 'delete-process':
        //                  // 删除当前任务
        //                  workflow.deleteProcess(checkStatus, listUrl, listTableId, listCurrentId, true);
        //                  break;
        //          }
        //      });
        //已办 工具类
        table.on('toolbar(doneTableFilter)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            if (obj.event != "LAYTABLE_COLS" && obj.event != "yjd") {
                if (checkStatus.data.length == 0) {
                    layer.msg('请选择一条数据进行操作！');
                    return false;
                }
            }
            // var checkData = checkStatus.data[0];
            switch (obj.event) {
                case 'fetch-process':
                    workflow.fetchProcess(checkData, doneUrl, doneTableId, doneCurrentId, true);
                    workflow.fetchPlProcess(checkStatus, doneUrl, doneTableId, doneCurrentId, true);
                    break;
                case 'yjd':
                    layer.open({
                        title: '移交单列表',
                        type: 2,
                        area: ['100%', '100%'],
                        content: 'bdcYjd.html'
                        , cancel: function () {

                        }
                        , success: function () {

                        }
                    });
                    break;
            }
        });

        // 6.5 监听行双击事件
        //监听待办 行双击
        table.on('rowDouble(waitTableFilter)', function (obj) {
            console.log('obj:', obj);
            //得到当前行数据
            var lcArray = {
                taskId: obj.data.taskId,
                formKey: obj.data.formKey,
                processInstanceId: obj.data.processInstanceId,
                processDefName: obj.data.processDefName,
                processInstanceType: 'todo'
            };
            if (!workflow.showHangReson(obj.data)) {
                return false;
            }
            sessionStorage.setItem('lcArray' + obj.data.taskId, JSON.stringify(lcArray));
            //锁定任务
            workflow.lockProcess(obj.data);
            var waitCzTimer = setInterval(function () {
                if (sessionStorage.getItem('lcArray' + obj.data.taskId) != null) {
                    clearInterval(waitCzTimer);
                    var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                    var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=db" + '&ywlx=' + ywlx, "待办任务");
                    newWin.push(index);
                }
            }, 50);

        });

        table.on('tool(waitTableFilter)', function (obj) { //bdcdyhList为table的lay-filter对应的值
            console.log('obj.data', obj.data);
            if (obj.event === 'openpage') {

                //得到当前行数据
                var lcArray = {
                    taskId: obj.data.taskId,
                    formKey: obj.data.formKey,
                    processInstanceId: obj.data.processInstanceId,
                    processDefName: obj.data.processDefName,
                    claimStatus: obj.data.claimStatus,
                    claimStatusName: obj.data.claimStatusName,
                    taskName: obj.data.taskName,
                    processInstanceType: 'todo'
                };

                if (!workflow.showHangReson(obj.data)) {
                    return false;
                }
                sessionStorage.setItem('lcArray' + obj.data.taskId, JSON.stringify(lcArray));
                //锁定任务
                workflow.lockProcess(obj.data);

                var waitCzTimer = setInterval(function () {
                    if (sessionStorage.getItem('lcArray' + obj.data.taskId) != null) {
                        clearInterval(waitCzTimer);
                        var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                        var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=db" + '&ywlx=' + ywlx, obj.data.text7);
                        newWin.push(index);
                    }
                }, 50);
            }
        });

        // 监听已办 行双击
        table.on('rowDouble(doneTableFilter)', function (obj) {
            console.log('已办:', obj.data);
            if (!workflow.showHangReson(obj.data)) {
                return false;
            }
            var lcArray = {
                taskId: obj.data.taskId,
                formKey: obj.data.formKey,
                processInstanceId: obj.data.processInstanceId,
                processDefName: obj.data.processDefName,
                claimStatus: obj.data.claimStatus,
                claimStatusName: obj.data.claimStatusName,
                taskName: obj.data.taskName,
                processInstanceType: 'done'
            };
            sessionStorage.setItem('lcArray' + obj.data.taskId, JSON.stringify(lcArray));
            var ybCzTimer = setInterval(function () {
                if (sessionStorage.getItem('lcArray' + obj.data.taskId) != null) {
                    clearInterval(ybCzTimer);
                    var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                    var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=yb" + '&ywlx=' + ywlx, obj.data.text7);
                    newWin.push(index);
                }
            }, 50);
        });

        table.on('tool(doneTableFilter)', function (obj) { //bdcdyhList为table的lay-filter对应的值
            if (obj.event === 'openpage') {
                console.log('已办:', obj.data);
                if (!workflow.showHangReson(obj.data)) {
                    return false;
                }
                var lcArray = {
                    taskId: obj.data.taskId,
                    formKey: obj.data.formKey,
                    processInstanceId: obj.data.processInstanceId,
                    processDefName: obj.data.processDefName,
                    claimStatus: obj.data.claimStatus,
                    claimStatusName: obj.data.claimStatusName,
                    taskName: obj.data.taskName,
                    processInstanceType: 'done'
                };
                sessionStorage.setItem('lcArray' + obj.data.taskId, JSON.stringify(lcArray));
                var ybCzTimer = setInterval(function () {
                    if (sessionStorage.getItem('lcArray' + obj.data.taskId) != null) {
                        clearInterval(ybCzTimer);
                        var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                        var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=yb" + '&ywlx=' + ywlx, obj.data.text7);
                        newWin.push(index);
                    }
                }, 50);
            }
        });
        // 监听项目列表 行双击
        table.on('rowDouble(listTableFilter)', function (obj) {
            if (!workflow.showHangReson(obj.data)) {
                return false;
            }
            //得到当前行数据
            var listArray = {
                processInstanceId: obj.data.procInsId,
                processDefName: obj.data.processDefName,
                claimStatus: obj.data.claimStatus,
                claimStatusName: obj.data.claimStatusName,
                taskName: obj.data.taskName,
                processInstanceType: 'list'
            };
            sessionStorage.setItem('listArray' + obj.data.procInsId, JSON.stringify(listArray));
            var xmCzTimer = setInterval(function () {
                if (sessionStorage.getItem('listArray' + obj.data.procInsId) != null) {
                    clearInterval(xmCzTimer);
                    var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                    var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=xm" + '&ywlx=' + ywlx, obj.data.text7);
                    newWin.push(index);
                }
            }, 50);
        });

        table.on('tool(listTableFilter)', function (obj) { //bdcdyhList为table的lay-filter对应的值
            if (obj.event === 'openpage') {
                if (!workflow.showHangReson(obj.data)) {
                    return false;
                }
                //得到当前行数据
                var listArray = {
                    processInstanceId: obj.data.procInsId,
                    processDefName: obj.data.processDefName,
                    claimStatus: obj.data.claimStatus,
                    claimStatusName: obj.data.claimStatusName,
                    taskName: obj.data.taskName,
                    processInstanceType: 'list'
                };
                sessionStorage.setItem('listArray' + obj.data.procInsId, JSON.stringify(listArray));
                var xmCzTimer = setInterval(function () {
                    if (sessionStorage.getItem('listArray' + obj.data.procInsId) != null) {
                        clearInterval(xmCzTimer);
                        var ywlx = obj.data.text7 == '规划测绘成果入库' ? 'GHJG' : obj.data.text7 == '地籍测绘成果入库' ? 'DJCH' : 'FCCH';
                        var index = window.open("../view/service-page.html?taskid=" + obj.data.taskId + "&xmid=" + obj.data.text9 + "&gzlslid=" + obj.data.procInsId + "&formKey=" + obj.data.formKey + "&type=xm" + '&ywlx=' + ywlx, obj.data.text7);
                        newWin.push(index);
                    }
                }, 50);
            }
        });

        // 监听个人项目列表 行双击
        //      table.on('rowDouble(grListTableFilter)', function (obj) {
        //          if (!workflow.showHangReson(obj.data)) {
        //              return false;
        //          }
        //          //得到当前行数据
        //          var listArray = {
        //              processInstanceId: obj.data.procInsId,
        //              processDefName: obj.data.processDefName,
        //              claimStatus: obj.data.claimStatus,
        //              claimStatusName: obj.data.claimStatusName,
        //              taskName: obj.data.taskName,
        //              processInstanceType: 'list'
        //          };
        //          sessionStorage.setItem('listArray' + obj.data.procInsId, JSON.stringify(listArray));
        //          var grCzTimer = setInterval(function(){
        //              if(sessionStorage.getItem('listArray' + obj.data.procInsId) != null){
        //                  clearInterval(grCzTimer);
        //                  var index = window.open(getContextPath() + "/view/new-page.html?name=" + obj.data.procInsId, obj.data.text1);
        //                  newWin.push(index);
        //              }
        //          },50);
        //      });
        //
        //      table.on('tool(grListTableFilter)', function (obj) { //bdcdyhList为table的lay-filter对应的值
        //          if (obj.event === 'openpage') {
        //              if (!workflow.showHangReson(obj.data)) {
        //                  return false;
        //              }
        //              //得到当前行数据
        //              var listArray = {
        //                  processInstanceId: obj.data.procInsId,
        //                  processDefName: obj.data.processDefName,
        //                  claimStatus: obj.data.claimStatus,
        //                  claimStatusName: obj.data.claimStatusName,
        //                  taskName: obj.data.taskName,
        //                  processInstanceType: 'list'
        //              };
        //              sessionStorage.setItem('listArray' + obj.data.procInsId, JSON.stringify(listArray));
        //              var grCzTimer = setInterval(function(){
        //                  if(sessionStorage.getItem('listArray' + obj.data.procInsId) != null){
        //                      clearInterval(grCzTimer);
        //                      var index = window.open(getContextPath() + "/view/new-page.html?name=" + obj.data.procInsId, obj.data.text1);
        //                      newWin.push(index);
        //                  }
        //              },50);
        //          }
        //      });

        //查询
        var searchObj = {
            "dbSearch": "dbTable",
            "ybSearch": "doneTableId",
            "xmSearch": "listTableId",
            "grSearch": "grListTableId",
            "rlSearch": "rlTableId"
        };
        $('.searchBtn').on('click', function () {
            var id = this.id;
            // 获取查询内容
            var obj = {};
            $("." + id).each(function (i) {
                var value = $(this).val();
                var name = $(this).attr('name');
                console.log('value:', $(this).val());
                obj[name] = value;
            });
            // 重新请求
            table.reload(searchObj[id], {
                where: obj
                , page: {
                    curr: 1  //重新从第 1 页开始
                }
            });
        });

        //点击高级查询--4的倍数
        $('#seniorSearch').on('click', function () {
            $('.pf-senior-show').toggleClass('bdc-hide');
            if (dbSearch == "center") {
                $(this).parent().toggleClass('bdc-button-box-four');
            }
            if ($(this).html() == '高级查询') {
                $(this).html('收起');
            } else {
                $(this).html('高级查询');
            }
            changeTableHeight();
        });
        //点击高级查询--4的倍数
        $('#seniorybSearch').on('click', function () {
            $('.pf-senior-yb-show').toggleClass('bdc-hide');
            if (ybSearch == "center") {
                $(this).parent().toggleClass('bdc-button-box-four');
            }
            if ($(this).html() == '高级查询') {
                $(this).html('收起');
            } else {
                $(this).html('高级查询');
            }
            changeTableHeight();
        });
        //点击高级查询--4的倍数
        $('#seniorxmSearch').on('click', function () {
            $('.pf-senior-xm-show').toggleClass('bdc-hide');
            if (xmSearch == "center") {
                $(this).parent().toggleClass('bdc-button-box-four');
            }
            if ($(this).html() == '高级查询') {
                $(this).html('收起');
            } else {
                $(this).html('高级查询');
            }
            changeTableHeight();
        });
        //点击高级查询--4的倍数  个人项目
        //      $('#seniorGrSearch').on('click', function () {
        //          $('.pf-senior-gr-show').toggleClass('bdc-hide');
        //
        //          if ($(this).html() == '高级查询') {
        //              $(this).html('收起');
        //          } else {
        //              $(this).html('高级查询');
        //          }
        //          changeTableHeight();
        //      });
        //点击高级查询，认领
        //      $('#seniorrlSearch').on('click', function () {
        //          $('.pf-senior-rl-show').toggleClass('bdc-hide');
        //          if (rlSearch == "center") {
        //              $(this).parent().toggleClass('bdc-button-box-four');
        //          }
        //
        //          if ($(this).html() == '高级查询') {
        //              $(this).html('收起');
        //          } else {
        //              $(this).html('高级查询');
        //          }
        //          changeTableHeight();
        //      });

        loadProcessDefName();
        //      if(lcPageEdition == 'nt'){
        //          loadDeptName();
        //      }
        /**
         * 渲染流程名称下拉框
         */
        function loadProcessDefName() {
            $.ajax({
                type: "GET",
                url: getContextPath() + '/index/getWorkflowDefinitions',
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                async: false,
                success: function (data) {
                    $('#selectedDefName').append(new Option("请选择", ""));
                    $('#selectedDoneDefName').append(new Option("请选择", ""));
                    $('#selectedXmDefName').append(new Option("请选择", ""));
                    $('#selectedRlDefName').append(new Option("请选择", ""));
                    $.each(data, function (index, item) {
                        $('#selectedDefName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
                        $('#selectedDoneDefName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
                        $('#selectedXmDefName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
                        $('#selectedRlDefName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
                    });
                    layui.form.render("select");
                }, error: function (e) {
                    response.fail(e, '');
                }
            });
        }

        /**
         * 渲染部门名称下拉框
         */
        //      function loadDeptName() {
        //          $.ajax({
        //              type: "GET",
        //              url: getContextPath() + "/rest/v1.0/task/dept/all",
        //              contentType: "application/json;charset=utf-8",
        //              dataType: "json",
        //              async: false,
        //              success: function (data) {
        //                  $('#selectedDeptName').append(new Option("请选择", ""));
        //                  $('#selectedDoneDeptName').append(new Option("请选择", ""));
        //                  $('#selectedXmDeptName').append(new Option("请选择", ""));
        //                  $('#selectedRlDeptName').append(new Option("请选择", ""));
        //                  $.each(data, function (index, item) {
        //                      $('#selectedDeptName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
        //                      $('#selectedDoneDeptName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
        //                      $('#selectedXmDeptName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
        //                      $('#selectedRlDeptName').append(new Option(item.name, item.id));// 下拉菜单里添加元素
        //                  });
        //                  layui.form.render("select");
        //              }, error: function (e) {
        //                  response.fail(e,'');
        //              }
        //          });
        //      }
        //      $('.bdc-table-box').on('mouseenter', 'td', function () {
        //          if ($(this).text() && $(this).attr("data-field") == "text4") {
        //              $(this).append('<div class="layui-table-grid-down"><i class="layui-icon layui-icon-down"></i></div>')
        //          }
        //          $('.layui-table-grid-down').on('click', function () {
        //              setTimeout(function () {
        //                  $('.layui-table-tips-main .bdc-table-date').html(reverseString($('.layui-table-tips-main .bdc-table-date').html()));
        //              }, 20);
        //          });
        //      });


    });

    function renderSearchList(processDefName) {
        $.ajax({
            type: "GET",
            url: getContextPath() + "/rest/v1.0/task/list",
            data: {
                processDefName: processDefName
            },
            success: function (data) {
                var getSearchTpl = searchTpl.innerHTML
                    , searchView = document.getElementById('searchView');
                laytpl(getSearchTpl).render(data, function (html) {
                    searchView.innerHTML = html;
                });
                changeTableHeight();
            }, error: function (e) {
                response.fail(e, '');
            }
        });
    }

    function refreshTab(url, id, authorityCode) {
        table.reload(id, {
            url: url,
            done: function (data) {
                //              debugger
                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
        moduleAuthority.load({
            authorityCode: authorityCode
        });
    }

    //表格高度自适应
    function changeTableHeight() {
        if ($('.bdc-list-tab .layui-tab-content .layui-show .layui-table-main>.layui-table').height() == 0) {
            $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body .layui-none').html('<img src="../static/lib/bdcui/images/table-none.png" alt="">无数据');
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body').height('56px');
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-fixed .layui-table-body').height('56px');
        } else {
            //$('.bdc-list-tab .layui-tab-content .layui-show .layui-table-main.layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height());
            if($(".bdc-container-tab[lay-filter='task']").css('display') === 'none'){
                $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body').height($('.bdc-content-box').height() - 84 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height());
                $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-fixed .layui-table-body').height($('.bdc-content-box').height() - 84 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height() - 17);
            } else {
                $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height());
                $('.bdc-list-tab .layui-tab-content .layui-show .layui-table-fixed .layui-table-body').height($('.bdc-content-box').height() - 196 - $('.bdc-task-tab').innerHeight() - $('.bdc-list-tab .layui-show .bdc-search-box').height() - 17);
            }
        }
    }


    function renderWaitTable(url, tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            url: url,
            toolbar: toolbar,
            title: '待办任务表格',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10, 30, 50, 70, 90, 110, 130, 150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: '', title: '流程状态', templet: '#dbstateTpl', width: 90},
                {field: 'text1', width: 150, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                {field: 'text2', width: 150, title: '建设单位'},
                {field: 'text3', width: 210, title: '测绘单位'},
                {field: 'text4', minWidth: 180, title: '项目地址', hide: true},
                {field: 'text6', minWidth: 180, title: '项目编号'},
                {field: 'text10', minWidth: 180, title: '项目名称'},
                {field: 'text5', width: 90, title: '受理人'},
                {field: 'text7', title: '流程名称', minWidth: 140},
                {field: 'text8', title: '节点名称', width: 100},
                // { field: 'text6', width: 150, title: '审核状态' },
                {fixed: 'right', title: '流程图', templet: '#dblcTpl', width: 80}
            ]],
            parseData: function (res) { //res 即为原始返回的数据

                if (res.totalElements > 99) {
                    $('#todoTab .bdc-list-num-tips').html('99+');
                } else {
                    $('#todoTab .bdc-list-num-tips').html(res.totalElements);
                }
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var newStartTime = new Date(v.startTime);
                        v.newStartTime = newStartTime.toLocaleString();
                    }
                });
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');
                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    };

    //渲染个人项目列表
    function renderGrListTable(tableId, currentId, toolbar) {
        table.render({
            elem: tableId,
            id: currentId,
            data: [],
            toolbar: toolbar,
            title: '用户数据表',
            method: 'post',
            even: true,
            request: {
                limitName: 'size', //每页数据量的参数名，默认：limit
                loadTotal: true,
                loadTotalBtn: false
            },
            limits: [10, 30, 50, 70, 90, 110, 130, 150],
            defaultToolbar: ['filter'],
            cols: [[
                {type: 'checkbox', width: 50, fixed: 'left'},
                {field: 'procStatus', title: '流程状态', width: 90, templet: '#flowStateTpl'},
                {field: 'text1', minWidth: 110, title: '受理编号', templet: '#slbhTpl', event: 'openpage'},
                {field: 'text3', minWidth: 100, title: '权利人'},
                {field: 'text5', minWidth: 100, title: '义务人'},
                {field: 'text4', minWidth: 200, title: '坐落'},
                {field: 'procDefName', title: '流程名称', minWidth: 160},
                {field: 'startUserName', title: '受理人', minWidth: 100},
                {field: 'startTime', title: '受理时间', width: 100, sort: true},
                {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
                {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
                {field: 'categoryName', title: '业务类型', width: 90, hide: true},
                {fixed: 'right', title: '流程图', templet: '#lcTpl', width: 75},
                {fixed: 'right', field: 'procTimeoutStatus', width: 90, title: '超期状态', templet: '#overStateTpl'}
            ]],
            parseData: function (res) { //res 即为原始返回的数据
                // console.log(res);
                res.content.forEach(function (v) {
                    if (v.startTime) {
                        var startNewTime = new Date(v.startTime);
                        v.startTime = startNewTime.toLocaleString();
                    }
                    if (v.endTime) {
                        var newEndTime = new Date(v.endTime);
                        v.endTime = newEndTime.toLocaleString();
                    }
                });
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.message, //解析提示文本
                    "count": res.totalElements, //解析数据长度
                    "data": res.content //解析数据列表
                };
            },
            page: true,
            done: function () {
                $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');

                changeTableHeight();
                var reverseList = ['text4'];
                reverseTableCell(reverseList);
            }
        });
    }

    // 渲染认领列表
    // function renderRlTable(tableId, currentId, toolbar) {
    //     table.render({
    //         elem: tableId,
    //         id: currentId,
    //         data: [],
    //         toolbar: toolbar,
    //         title: '认领任务表',
    //         method: 'post',
    //         even: true,
    //         request: {
    //             limitName: 'size', //每页数据量的参数名，默认：limit
    //             loadTotal: true,
    //             loadTotalBtn: false
    //         },
    //         limits: [10,30,50,70,90,110,130,150],
    //         defaultToolbar: ['filter'],
    //         cols: [[
    //             {type: 'checkbox', width: 50, fixed: 'left'},
    //             {field: 'text1', minWidth: 110, title: '受理编号',templet: '#slbhTpl', event: 'openpage'},
    //             {field: 'text3', minWidth: 100, title: '权利人'},
    //             {field: 'text5', minWidth: 100, title: '义务人'},
    //             {field: 'text4', minWidth: 200, title: '坐落'},
    //             {title: '流程名称', field: 'processDefName', minWidth: 160},
    //             {field: 'text7', minWidth: 200, title: '登记原因'},
    //             {field: 'procStartUserName', title: '受理人', minWidth: 100},
    //             {field: 'taskName', title: '节点名称', width: 90},
    //             {field: 'newStartTime', title: '开始时间', width: 100, sort: true},
    //             {field: 'newEndTime', title: '结束时间', width: 100, sort: true},
    //             {field: 'text2', minWidth: 270, templet: '#bdcdyhTpl', title: '不动产单元号'},
    //             {title: '项目名称', templet: '#rwNameTpl', minWidth: 200, hide: true},
    //             {field: 'category', title: '业务类型', width: 90, hide: true},
    //             {field: 'taskAssName', title: '处理人', width: 90, hide: true},
    //             {fixed: 'right', title: '流程图', templet: '#lcTpl', minWidth: 75}
    //         ]],
    //         parseData: function (res) { //res 即为原始返回的数据
    //             res.content.forEach(function (v) {
    //                 if (v.startTime) {
    //                     var newStartTime = new Date(v.startTime);
    //                     v.newStartTime = newStartTime.toLocaleString();
    //                 }
    //                 if (v.endTime) {
    //                     var newEndTime = new Date(v.endTime);
    //                     v.newEndTime = newEndTime.toLocaleString();
    //                 }
    //             });
    //             //获取互联网+的处理
    //             if($('.bdc-rl-num-word').length>0 && $('.bdc-rl-num-word').html().indexOf("互联网")!=-1){
    //                 var rlUrl = getContextPath() + "/rest/v1.0/task/claim/list";
    //                 $.ajax({
    //                     type: "POST",
    //                     url: rlUrl,
    //                     data: {sply: "3"},
    //                     success: function (data) {
    //                         if(data && data.hasOwnProperty("totalElements")){
    //                             $('.bdc-rl-num-js').html(data.totalElements)
    //                         }
    //                     }
    //                 });
    //             }else{
    //                 if (res.totalElements > 99) {
    //                     $('.bdc-rl-num-js').html('99+');
    //                 } else {
    //                     $('.bdc-rl-num-js').html(res.totalElements);
    //                 }
    //             }
    //             return {
    //                 "code": res.code, //解析接口状态
    //                 "msg": res.message, //解析提示文本
    //                 "count": res.totalElements, //解析数据长度
    //                 "data": res.content //解析数据列表
    //             };
    //         },
    //         page: true,
    //         done: function () {
    //             $('.layui-table-tool-self').css('right', $('.bdc-export-tools').width() + 17 + 'px');
    //
    //             changeTableHeight();
    //             var reverseList = ['text4'];
    //             reverseTableCell(reverseList);
    //         }
    //     });
    // }

});


function refreshOpen(msg) {
    newWin.forEach(function (v) {
        v.layer.open({
            title: msg.msgTypeName
            , content: '您的账户已在别处登陆，您被迫下线，请核实！'
            , yes: function (index, layero) {
                v.window.location.reload();
            }, cancel: function () {
                v.window.location.reload();
            }
        });
    });
}