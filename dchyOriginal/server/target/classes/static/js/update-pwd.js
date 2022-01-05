/**
 * Created by Administrator on 2019/3/5.
 */
layui.use(['jquery','element','form','response'], function() {
    var $ = layui.jquery,
        element = layui.element,
        response = layui.response,
        form = layui.form;

    $(function () {

        function getParentIpHz() {
            var $paramArr = [];
            var $params = window.parent.location.search.replace('?', '');
            var $paramSplit = $params.split('&');
            for (var i in $paramSplit) {
                var $subParam = $paramSplit[i].split('=');
                $paramArr[$subParam[0]] = $subParam[1];
            }
            return $paramArr;
        }
        //点击确认修改
        form.on('submit(updateFilter)', function(){
            var oldPwd = $('#oldPwd').val();
            var newPwd = $('#newPwd').val();
            var confirmPwd = $('#confirmPwd').val();
            if (newPwd == confirmPwd && !isNullOrEmpty(oldPwd) && !isNullOrEmpty(newPwd)) {
                $.ajax({
                    type: "PUT",
                    url: getContextPath() + "/index/password?old="+oldPwd+'&password='+newPwd,
                    success: function (data) {
                    	console.log('pwd:',data);
                        layer.msg('修改成功');
                    },error: function(e){
                        response.fail(e,'');
                    }
                });
                return false;
            }else {
                layer.msg('您两次输入的新密码不一致');
                $('#newPwd').val('');
                $('#confirmPwd').val('');
                $('#newPwd').focus();
                return false;
            }
        });
//      form.on('submit(updateFilter)', function(){
//          var oldPwd = $('#oldPwd').val();
//          var newPwd = $('#newPwd').val();
//          var confirmPwd = $('#confirmPwd').val();
//          if (newPwd == confirmPwd && !isNullOrEmpty(oldPwd) && !isNullOrEmpty(newPwd)) {
//              $.ajax({
//                  type: "PUT",
//                  url: getContextPath() + "/index/password?old="+oldPwd+'&password='+newPwd,
//                  success: function (data) {
//                  	console.log('pwd:',data);
//                      var $paramArr = getParentIpHz();
//                      if(data == 'success'){
//                          $.ajax({
//                              type: "GET",
//                              url: getContextPath() + "/rest/v1.0/user/logout",
//                              success: function (data) {
//                                  var path = "http://" + window.parent.location.host + window.parent.location.pathname;
//                                  var search = "?t=" + (new Date()).getTime();
//                                  for (var parami in $paramArr) {
//                                      //加的时间戳不处理
//                                      if (parami == "t") {
//                                          continue;
//                                      }
//                                      search += "&" + parami + "=" + $paramArr[parami]
//                                  }
//                                  window.parent.location.href = data + path + encodeURIComponent(search);
//                              }, error: function (e) {
//                                  response.fail(e,'登出系统失败，请刷新后重试！');
//                              }
//                          });
//                      }else {
//                          layer.msg('修改失败');
//                      }
//                  },error: function(e){
//                      response.fail(e,'');
//                  }
//              });
//              return false;
//          }else {
//              layer.msg('您两次输入的新密码不一致');
//              $('#newPwd').val('');
//              $('#confirmPwd').val('');
//              $('#newPwd').focus();
//              return false;
//          }
//      });

    });
});