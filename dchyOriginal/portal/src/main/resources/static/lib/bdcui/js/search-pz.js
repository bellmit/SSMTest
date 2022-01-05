/**
 * Created by Ypp on 2020/2/12.
 * 查询条件配置显示隐藏及顺序
 */
layui.use(['form','laytpl','layer'],function () {
    var laytpl = layui.laytpl,
        $ = layui.jquery,
        form = layui.form;
    $(function () {
        var searchData = [
            {name: "抽取批次号",isShow: true},
            {name: "测试",isShow: false},
            {name: "测试1",isShow: true},
            {name: "行政区",isShow: true},
            {name: "质检人",isShow: false},
            {name: "精确时分秒",isShow: true},
            {name: "权利人",isShow: true},
            {name: "质检起始时间",isShow: true},
            {name: "质检结束时间",isShow: true}
        ];
        var getTpl = contentTpl.innerHTML;
        laytpl(getTpl).render(searchData, function(html){
            $('.layui-form-item').html(html);
            form.render();
        });

        form.on('switch(isShow)', function(data){
            searchData.forEach(function(v){
                if(v.name == $(data.elem).attr('name')){
                    v.isShow = data.elem.checked;
                }
            });
        });

        //点击保存
        $('#save').on('click',function(){
            console.log(searchData);
        });
    });
});