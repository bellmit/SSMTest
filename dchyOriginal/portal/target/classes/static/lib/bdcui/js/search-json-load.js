/**
 * author: 前端组
 * date: 2018-12-14
 * version 3.0.0
 * describe: 查询条件所需的日期初始化、复选框初始化及高级查询
 */
layui.config({
    base: '../../' //此处路径请自行处理, 可以使用绝对路径
}).extend({
    formSelects: 'form-select/formSelects-v4'
});
layui.use(['form','jquery','laydate','element','formSelects'],function () {
    var $ = layui.jquery,
        form = layui.form,
        formSelects = layui.formSelects,
        laydate = layui.laydate;
    $(function () {
        //页面加载完成，根据json控制查询条件显示隐藏
        var data = [
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
        hideSearchByData(data,false);
        /**
         * 根据json数据，隐藏指定查询条件
         * @param data  接口获取json
         * @param isTab 是否是tab下面的查询条件
         */
        function hideSearchByData(data,isTab){
            var $searchBox;
            if(isTab){
                $searchBox = $('.layui-show .bdc-search-box');
            }else{
                $searchBox = $('.bdc-search-box');
            }
            data.forEach(function(v){
                if(!v.isShow){
                    var $label = $searchBox.find('.layui-form-label');
                    for(var i = 0,len = $label.length; i < len; i++){
                        if($label[i].innerHTML == v.name){
                            var $senior = $searchBox.find('.pf-senior-show');
                            if($senior.length > 0){
                                var $seniorFirstChild = $senior.find('.layui-inline:first-child');
                                $senior.before('<div class="layui-inline">'+ $seniorFirstChild.html() +'</div>');
                                $seniorFirstChild.remove();
                                if($senior.find('.layui-inline').length == 0){
                                    $senior.remove();
                                    $searchBox.find('#seniorSearch').remove();
                                }
                            }

                            $($label[i]).parents('.layui-inline').remove();
                        }
                    }
                    //隐藏后如果查询条件个数为4，按钮居中
                    if($searchBox.find('.layui-form-label').length == 4){
                        $searchBox.find('.bdc-button-box').addClass('bdc-button-box-four');
                    }
                }
            })
        }
        //1. 日期
        lay('.test-item').each(function(){
            //精确到时分秒日期控件
            laydate.render({
                elem: '#exact1',
                type: 'datetime'
                ,trigger: 'click'
            });
            //初始化日期控件
            var startDate = laydate.render({
                elem: '#startTime'
                ,type: 'datetime'
                ,trigger: 'click',
                done: function(value,date){
                    //选择的结束时间大
                    if($('#endTime').val() != '' && !completeDate($('#endTime').val(),value)){
                        $('#endTime').val('');
                        $('.laydate-disabled.layui-this').removeClass('layui-this');
                    }
                    endDate.config.min ={
                        year:date.year,
                        month:date.month-1,
                        date: date.date
                        ,hours:date.hours,
                        minutes:date.minutes,
                        seconds:date.seconds
                    }
                }
            });
            var endDate = laydate.render({
                elem: '#endTime'
                ,trigger: 'click'
                ,type: 'datetime'
            });

        });
        $('#endTime').on('change',function () {
            if($('#endTime').val() != '' && !completeDate($('#endTime').val(),$('#startTime').val())){
                $('#endTime').val('');
                $('.laydate-disabled.layui-this').removeClass('layui-this');
            }
        });
        function completeDate(date1,date2){
            var oDate1 = new Date(date1);
            var oDate2 = new Date(date2);
            if(oDate1.getTime() > oDate2.getTime()){
                return true;
            } else {
                return false;
            }
        }

        //2. 下拉选择
        //2.2 js渲染复选框
        formSelects.data('selectJs', 'local', {
            arr: [
                {"name": "北京北京北京北京北京北京北京北京", "value": 1},
                {"name": "上海", "value": 2,"selected":"selected"},
                {"name": "广州", "value": 3},
                {"name": "深圳", "value": 0},
                {"name": "天津", "value": 5}
            ]
        });

        //3. 输入框删除图标
        if(!(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i)=="9."))
        {
            //监听input点击
            $('.layui-form-item .layui-input-inline').on('click','.layui-icon-close',function () {
                $(this).siblings('.layui-input').val('');
                $(this).siblings().find('.layui-input').val('');
            });

            $('.layui-form-item .layui-input-inline .layui-input').on('focus',function () {
                $(this).siblings('.layui-icon-close').removeClass('bdc-hide');
                $(this).parents('.layui-input-inline').find('.layui-icon-close').removeClass('bdc-hide');
                $(this).siblings('.layui-edge').addClass('bdc-hide');
            }).on('blur',function () {
                var $this = $(this);
                setTimeout(function () {
                    $this.siblings('.layui-icon-close').addClass('bdc-hide');
                    $this.parents('.layui-input-inline').find('.layui-icon-close').addClass('bdc-hide');
                    $this.siblings('.layui-edge').removeClass('bdc-hide');
                },150)
            });
        }

        //4.高级查询
        // 点击高级查询--4的倍数
        $('#seniorSearch').on('click',function () {
            $('.pf-senior-show').toggleClass('bdc-hide');
            $(this).parent().toggleClass('bdc-button-box-four');

            if($(this).html() == '高级查询'){
                $(this).html('收起');
            }else {
                $(this).html('高级查询');
            }
        });
        //点击高级查询--一般情况
        $('#seniorSearchNormal').on('click',function () {
            $('.pf-senior-show-normal').toggleClass('bdc-hide');

            if($(this).html() == '高级查询'){
                $(this).html('收起');
            }else {
                $(this).html('高级查询');
            }
        });
    });
});