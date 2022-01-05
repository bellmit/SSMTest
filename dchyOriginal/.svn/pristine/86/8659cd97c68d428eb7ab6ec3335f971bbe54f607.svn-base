<#assign cssContent>
<!--[if IE]>
    <@com.script name="static/thirdparty/PIE/PIE.js"></@com.script>
<![endif]-->
    <@com.style name="static/thirdparty/bootstrap/css/bootstrap-v3.css"></@com.style>
    <@com.style name="static/thirdparty/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"></@com.style>
    <@com.style name="static/thirdparty/font-awesome/css/font-awesome.css"></@com.style>
<style type="text/css">
@charset "utf-8";
* {
    margin: 0;
    padding: 0;
    list-style-type: none;
}

a, img {
    border: 0;
}

body {
    _background-image: url(about:blank); /*用浏览器空白页面作为背景*/
    _background-attachment: fixed; /* prevent screen flash in IE6 确保滚动条滚动时，元素不闪动*/
    position: relative;
}

body {
    color: #535353;
    font-size: 12px;
    font-family: "arial", "微软雅黑";
    background: #f0f1f1;
}

.clear {
    clear: both;
    display: block;
    height: 0;
    overflow: hidden;
}

/* history */
#history {
    height: 450px;
    position: relative;
    margin: 50px auto 0 auto;
}

.title {
    height: 95px;
    line-height: 95px;
    /*text-indent: 280px;*/
    text-indent: 50%;
    position: relative;
}

.title1 {
    position: absolute;
    bottom: 0px;
    width: 100%;
    padding: 0px;
}

.title1 h1{
    color: #7c7c7c;
    font-weight: 500;
    bottom: -7px;
    height:45px;
    background-color:black;
    filter:alpha(opacity=40);
    -moz-opacity:0.4;
    opacity:0.6;
    color:white;
    font-weight:bold;
    line-height: 45px;
    z-index: 10;
    display: none;
}

.title h2, .title1 h1{
    color: #7c7c7c;
    /*font-size: 18px;*/
    font-weight: 500;
    position: relative;
}

#circle {
    width: 95px;
    height: 95px;
    position: absolute;
    top: 0;
    /*left: 223px;*/
    left: 60%;
    border: 6px solid #CCCDCD;
    border-radius: 95px;
    -moz-border-radius: 95px;
    -webkit-border-radius: 95px;
    border-radius: 95px;
    text-indent: 0;
    text-align: center;
    -webkit-transition: all 0.3s linear;
    -moz-transition: all 0.3s linear;
    -o-transition: all 0.3s linear;
    transition: all 0.3s linear;
    z-index:100;
    behavior: url('/omp/static/thirdparty/h-ui/css/ie-css3.htc');
    background: #f0f1f1;
}

#circle .cmsk {
    height: 83px;
    position: absolute;
    width: 83px;
    top: 0;
    left: 0;
}

#circle:hover {
    transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -webkit-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    border-color: rgba(0, 0, 0, 0);
}

#circle:hover {
    border-color: rgba(255, 255, 255, 0.6);
    background-color: #6bc30d;
}

#circle:hover .clock {
    display: block;
}

#circle:hover .circlecontent {
    display: none;
}

#circle span {
    font-size: 18px;
    color: #b0b0b0;
}

#circle .clock {
    background: url('/omp/static/img/timeline/clock.png') no-repeat 0 0;
    width: 83px;
    height: 83px;
    position: absolute;
    top: 0px;
    left: 0;
    display: none;
    -webkit-transition: all 0.5s linear;
    -moz-transition: all 0.5s linear;
    -o-transition: all 0.5s linear;
    transition: all 0.5s linear;
    border-radius: 50%;
    -moz-border-radius: 50%;
    -webkit-border-radius: 50%;
    behavior: url(/omp/static/thirdparty/PIE/PIE.htc);
}

#circle:hover .clock {
    background-color: #6bc30d;
}

.timeyear {
    color: #b0b0b0;
    font-size: 18px;
    line-height: 20px;
}

.timeblock {
    height: 28px;
    margin-top: 22px;
    margin-left: 5px;
}

.timeblock span {
    display: block;
    height: 24px;
    width: 18px;
    background: url('/omp/static/img/timeline/date.png') no-repeat 0 0;
    float: left;
}

.timeblock .numf {
    background-position: 0px -48px;
}

.timeblock .nums {
    background-position: 0px 0px;
}

.timeblock .numt {
    background-position: 0px -24px;
}

.timeblock .numfo {
    background-position: 0px -72px;
}

#content {
    height: 355px;
    width: 100%;
    overflow: hidden;
    background: url('/omp/static/img/timeline/vertical.png') no-repeat 80% 2px;
    padding-top: 10px;
    z-index: 0;
}

.list {
    overflow: hidden;
    position: relative;
}

.list li {
    height: 40px;
    vertical-align: bottom;
    overflow: hidden;
    position: relative;
}

.liwrap {
    height: 40px;
}

.lileft {
    position: absolute;
    top: 5px;
    left: 0px;
    height: 40px;
    width: 64%;
    line-height: 40px;
    text-align: right;
}

.liright {
    position: absolute;
    top: 0;
    /*right: 550px;*/
    right: 5%;
    height: 55px;
    /*width: 300px;*/
    width: 50%;
}

.histt {
    height: 35px;
    line-height: 35px;
}

.hisct {
    font-size: 14px;
    color: #6e6e6e;
}

.md {
    font-size: 18px;
    color: #AEAEAE;
}

.year {
    font-size: 12px;
    color: #AEAEAE;
    margin-right: 10px;
}

.point {
    width: 55px;
    height: 55px;
    position: absolute;
    top: 0;
    left: 68%;
    background: url('/omp/static/img/timeline/point.png') no-repeat 0px 18px;
    overflow: hidden;
}

.point b{
    position: relative;
    height: 20px;
    width: 20px;
    background: #fff;
    display: block;
    margin: 17px 0 0 19px;
    border: 2px solid #6bc30d;
    -webkit-border-radius: 50%;
    -moz-border-radius: 50%;
    border-radius: 50%;
    z-index: 1900;
    behavior: url(/omp/static/thirdparty/PIE/PIE.htc);
}

.thiscur .point b {
    position: relative;
    border: 4px solid #6bc30d;
    margin: 18px 0px 0px 18px;
    border-radius: 52px;
    z-index: 1900;
}

.thiscur .histt a {
    color: #6bc30d;
}

.histt a {
    font-size: 24px;
    color: #747474;
    -webkit-transition: all 0.3s linear;
    -moz-transition: all 0.3s linear;
    -o-transition: all 0.3s linear;
    transition: all 0.3s linear;
}

#arrow {
    position: fixed;
    top: 50%;
    right: 30px;
}

* html #arrow {
    position: absolute;
    top: expression(eval(document.documentElement.scrollTop));
    margin-top: 350px;
}

#arrow ul li {
    display: block;
    height: 20px;
    width: 20px;
    background: url('/omp/static/img/timeline/icons.png') no-repeat 0 0;
    cursor: pointer;
    -webkit-transition: all 0.2s ease-out;
    -moz-transition: all 0.2s ease-out;
    -o-transition: all 0.2s ease-out;
    transition: all 0.2s ease-out;
}

#arrow ul li:active {
    background-color: #000;
}

#arrow ul .arrow_active {
    background-color: #000;
    -webkit-transition: all 0.1s ease-in;
    -moz-transition: all 0.1s ease-in;
    -o-transition: all 0.1s ease-in;
    transition: all 0.1s ease-in;
}

#arrow ul .arrowup {
    background-position: 0px -26px;
    margin-bottom: 10px;
}

#arrow ul .arrowdown {
    background-position: 0px 0px;
}

.timeline {
    /*width: 450px;*/
    width: 265px;
    height: 100%;
    /*min-width: 400px;*/
    position: relative;
}

.panorama {
    width: 60%;
}

.panorama-pic {
    text-align: center;
    /*border: solid #00FFFF 1px;*/
    height: 700px;
    border-left: #6bc30d dashed 2px;
}

.panorama-pic:before {
    content: '';
    display: inline-block;
    height: 100%;
    vertical-align: middle;
    margin-right: -0.25em; /* Adjusts for spacing */
}

.cycle {
    position: relative;
    display: inline-block;
    vertical-align: middle;
    width: 90%;
}

.ellipsis{
    display:block;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
    width: 100%;
}

.monthChanege-icon{
    display:inline-block;
    background: #818181;
    width: 21px;
    height: 21px;
    color: #FFF;
    padding: 3px 5px 7px;
    cursor: pointer;
}

.event_year {
    width: 60px;
    border-bottom: 2px solid #DDD;
    text-align: center;
    margin-top: 10px;
}

.event_year li {
    height: 40px;
    line-height: 40px;
    background: #FFF;
    margin-bottom: 1px;
    font-size: 18px;
    color: #828282;
    cursor: pointer;

}

.event_year li label{
    font-weight: normal!important;
}

.event_year .arrow-right {
    display: inline-block;
    position: absolute;
    top: 10px;
    left: 60px;
    width:0px;
    height:0px;
    border:10px solid;
    border-color:transparent  transparent transparent #6BC30D;
    border-style: dashed dashed dashed solid;
}

.event_year .current{
    background: #6BC30D;
    color: #fff;
    position: relative;
}

.hidden{
    display: none!important;
}
.datetimepicker table tr td span.active, .datetimepicker table tr td span.active:hover, .datetimepicker table tr td span.active.disabled, .datetimepicker table tr td span.active.disabled:hover {
    background: none;
    background-image: -webkit-linear-gradient(top,#7EE67F,#6BC30D);
}

.datetimepicker table tr td span.active:active, .datetimepicker table tr td span.active:hover:active, .datetimepicker table tr td span.active.disabled:active, .datetimepicker table tr td span.active.disabled:hover:active, .datetimepicker table tr td span.active.active, .datetimepicker table tr td span.active:hover.active, .datetimepicker table tr td span.active.disabled.active, .datetimepicker table tr td span.active.disabled:hover.active {
    background-color: #6BC30D!important;
}
</style>
</#assign>
<@master.html title="${cameraName!}全景照片展示" css=cssContent>
<div style="position: absolute;top: 50px;left: 10px;z-index: 2000">
    <ul class="event_year">
        <li id="12"><label for="12" data-date="12">12月</label><span class="arrow-right hidden"></span></li>
        <li id="11" class="current"><label for="11" data-date="11">11月</label><span class="arrow-right"></span></li>
        <li id="10"><label for="10" data-date="10">10月</label><span class="arrow-right hidden"></span></li>
        <li id="9"><label for="09" data-date="09">09月</label><span class="arrow-right hidden"></span></li>
        <li id="8"><label for="08" data-date="08">08月</label><span class="arrow-right hidden"></span></li>
        <li id="7"><label for="07" data-date="07">07月</label><span class="arrow-right hidden"></span></li>
        <li id="6"><label for="06" data-date="06">06月</label><span class="arrow-right hidden"></span></li>
        <li id="5"><label for="05" data-date="05">05月</label><span class="arrow-right hidden"></span></li>
        <li id="4"><label for="04" data-date="04">04月</label><span class="arrow-right hidden"></span></li>
        <li id="3"><label for="03" data-date="03">03月</label><span class="arrow-right hidden"></span></li>
        <li id="2"><label for="02" data-date="02">02月</label><span class="arrow-right hidden"></span></li>
        <li id="1"><label for="01" data-date="01">01月</label><span class="arrow-right hidden"></span></li>
    </ul>
</div>

<div class="pull-left timeline">
    <div>
    <#--<div id="arrow">-->
    <#--<ul>-->
    <#--<li class="arrowup"></li>-->
    <#--<li class="arrowdown"></li>-->
    <#--</ul>-->
    <#--</div>-->

        <div id="history">
            <div class="title">
                <div id="circle">
                    <div class="cmsk"></div>
                    <div class="circlecontent">
                        <div thisyear="2016" class="timeblock">
                            <span class="numf"></span>
                            <span class="nums"></span>
                            <span class="numt"></span>
                            <span class="numfo"></span>

                            <div class="clear"></div>
                        </div>
                        <div class="timeyear">年</div>
                    </div>
                    <a href="#" class="clock"></a>
                </div>
            </div>

            <div id="content">
                <ul class="list">

                </ul>
            </div>
        </div>
    </div>
</div>

<div class="pull-left panorama">
    <div class="panorama-pic">
        <div class="cycle">
            <div class="title1">
                <h1>${cameraName!}全景图</h1>
            </div>

            <div id="panoramaImageCont" style="height: 500px;border: #6bc30d dashed 2px;">
            </div>
        </div>
    </div>
</div>

<script id="timelineDate" type="x-mustache-tpl">
{{#result}}
    <li>
        <div class="liwrap">
            <div class="lileft" data-date="{{date}}">
                <div class="date">
                    <span class="md">{{alias}}</span>
                </div>
            </div>

            <div class="point" data-date="{{date}}"><b></b></div>
        </div>
    </li>
    {{/result}}
</script>
<script id="panoramaTpl" type="x-mustache-tpl">
    <div id="panoramaImage" style="background-image:url('');height:100%;"></div>
</script>
<#--<@com.script name="static/thirdparty/jquery/jquery-1.11.1.js"></@com.script>-->
<@com.script name="static/thirdparty/jquery/jquery.cyclotron.min.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.mousewheel.js"></@com.script>
<@com.script name="static/thirdparty/jquery/jquery.easing.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap/js/bootstrap-v3.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></@com.script>
<@com.script name="static/thirdparty/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></@com.script>
<script type="text/javascript">
debugger
var indexCode = "${indexCode!}";
var today = "${today!}", row=0;
var now = getDate(today.split('-')[0], (today.split('-')[1] -1), today.split('-')[2]);
var timelineResult = [];
$(document).ready(function() {
    (function () {
        var a = $(window).height();
        890 <= a && (row = 17);
        800 <= a && 890 > a && (row = 15);
        726 <= a && 800 > a && (row = 14);
        726 > a && (row = 10)
    })();

    $(".list").height(40 * row);
    $("#content").height(40 * row + 25);

    $.fn.liOut = function () {
        $(this).find(".lileft").animate({left: "-400px"}, 500, "easeOutQuart");
        $(this).find(".liright").animate({right: "-700px"}, 500, "easeOutQuart");
    };

    $.fn.liIn = function () {
        $(this).find(".lileft").animate({left: "0px"}, 500, "easeOutQuart");
        $(this).find(".liright").animate({right: "5%"}, 500, "easeOutQuart");
    };

    changeYear(today.split('-')[0]);

    $(".panorama,.panorama-pic").css("height", $(window).height());
    $(".panorama").css("width", $(window).width() - 285);
    $("#panoramaImageCont").css("height", $(window).height()*0.95);
    changeTimeline(now);
    initTimeLine();
    listeners();
    openToday();
});

/***
 * 打开当天的全景图
 * */
function openToday(){
    $.each($(".lileft"), function () {
        var dateStr = $(this).closest('div').data("date");
        if(dateStr!=undefined){
            var str = dateStr.split(" ")[0].replace(/-/g, "/");
            var now = new Date();
            if (str === now.toLocaleDateString())
                $(this).click();
        }
    });
}
/**
 * 初始化时间轴
 */
function initTimeLine(){
    var flag = !0;

    $(".list .liwrap").on('mouseover',function () {
        $(".list li").removeClass("thiscur");
        $(this).parent().addClass("thiscur")
    });

    $(".list li:gt(" + (row - 1) + ")").find(".lileft").css({left: "-400px"});
    $(".list li:gt(" + (row - 1) + ")").find(".liright").css({right: "-700px"});

    /**
     * 向上点击滚动逻辑
     */
    $(".arrowup").on('click',function () {
        gotoUp();
    });

    /**
     * 向下点击滚动逻辑
     */
    $(".arrowdown").click(function (event) {
        event.stopPropagation();
        gotoDown();
    });

    /**
     * 向上滚动逻辑
     */
    function gotoUp(){
        var f = $(".list li").length;
        var hiddenRow = 0;
        var hiddenHeight = parseInt($(".list li:first").css("marginTop"));
        hiddenRow = parseInt(Math.abs(hiddenHeight) / 40) ;
        if(flag && f > row && hiddenRow > 0 ){  //此时还有行被隐藏
            hiddenRow
            flag = !1;
            $(".list li").eq(hiddenRow-1).liIn();
            $(".list li").eq(row+hiddenRow).liOut();
            $(".list li:first").animate({marginTop: hiddenHeight+40 }, 300, "easeInOutQuad", function () {
                flag = !0;
            });
        }else{
            flag=!0;
        }
    }

    /**
     * 向下滚动逻辑
     */
    function gotoDown(){
        var f = $(".list li").length;
        var hiddenRow = 0;
        var hiddenHeight = parseInt($(".list li:first").css("marginTop"));
        hiddenRow = parseInt(Math.abs(hiddenHeight) / 40) ;

        if(flag && f>row && (f-row) > hiddenRow){ //大于展示行且没有到最后一个才开始进行效果
            flag = !1;
            $(".list li").eq(row + hiddenRow).liIn();
            $(".list li").eq(hiddenRow).liOut();
            $(".list li:first").animate({marginTop: hiddenHeight -40 }, 300, "easeInOutQuad", function () {
                flag = !0;
            });
        }else {
            flag = !0;
        }
    }

    /**
     * 滚轮监听
     */
    $(".list").mousewheel(function (event, c) {
        if (flag && 0>c) {  //向下
            gotoDown();
        }else if(flag && 0<c){ //向上
            gotoUp();
        }
    });

    if (window.PIE) {
        $('.clock').each(function() {
            PIE.attach(this);
        });
        $( '.point b').each(function() {
            PIE.attach(this);
        });
    }
}

function listeners(){
    /**
     * 切换年份
     */
    $('.clock').datetimepicker({
        language:  'zh-CN',
        autoclose: 1,
        startView: 4,
        minView: 3,
        maxView: 4,
        forceParse: 0,
        endDate: now,
        fontAwesome:'fontAwesome'
    }).on('changeDate', function(ev){
        changeYear(ev.date.getFullYear());
        changeTimeline(ev.date);
    }).on('hide',function(){
        $("#circle .circlecontent").css("display","block");
        $("#circle .clock").css("display","none");
    });

    /**
     * 切换月
     */
    $(".event_year li").on('click', function(event){
        var selectYear = $(".timeblock").attr("thisyear");
        var selectMonth = $(this).find("label").data('date');
        changeTimeline( getDate(selectYear, selectMonth - 1, 1));
    });

    /**
     *  展示照片
     */
    $(".point, .lileft").on('click', function () {
        $("#panoramaImageCont").empty();
        $("#panoramaImageCont").append(Mustache.render($("#panoramaTpl").html(),{}));
        var url = '<@core.rootPath/>/video/panorama/fetch?indexCode=' + indexCode + "&date=" + $(this).data('date') +'?'+Math.random();
        $("#panoramaImage").css("background-image","url('"+url+"')");
        $('#panoramaImage').cyclotron({dampingFactor:1,autorotation:1});
    });

    $("#circle").on('mouseover', function(){
        $(this).find(".circlecontent").css("display","none");
        $(this).find(".clock").css("display","block");
    });

    $("#panoramaImageCont").on('mouseenter',function(){
        $(".title1 h1").slideToggle("slow");
    }).on('mouseout', function(){
        $(".title1 h1").slideToggle("slow");
    });
}

/**
 * 切换年共用逻辑
 */
function changeYear(year) {
    $(".timeblock").attr("thisyear", year);
    var a = year.toString().split(""), b = ["numf", "nums", "numt", "numfo"];
    for (var i = 0; 4 > i; i++){
        $("." + b[i] + "").css("background-position","0px " +(-24 * a[i]) +'px');
    }
}

/**
 * 修改时间轴
 */
function changeTimeline(selectDate){
    if(now.getTime() - selectDate.getTime()>=0){
        $(".event_year li").removeClass("current");
        $(".event_year li span").addClass("hidden");
        $("#"+(selectDate.getMonth()+1)).addClass("current").find("span").removeClass("hidden");

        $(".list li").detach();
        packageData(selectDate.getFullYear(), selectDate.getMonth(), timelineResult);
        $(".list").append(Mustache.render($("#timelineDate").html(), {result:timelineResult}));
        timelineResult.length = 0;
    }else{
        layer.msg("对不起，不能选择未来的月份！", {icon:5})
    }
}

/**
 * 获取日期
 * @param year
 * @param month
 * @param day
 */
function getDate(year, month, day){
    var date = null;
    date = new Date(year, month, day);
    return date;
}

/**
 * 组装数据
 * @param date
 * @param month
 */
function packageData(year, month, result){
    //
    var dates;
    if(result){
        dates = result;
    }else{
        dates = [];
    }
    for(var i=1; i<= getMonthDays(year, month); i++){
        if(now.getMonth()==month&&i==now.getDate()+1){
            break;
        }
        dates.push({date:year+'-'+ (month+1) + '-' + i + " 00:00:00", alias:(month+1) + '月' + i + '日'});
    }
    return dates.reverse();
}

/**
 * 判断年份是否为润年
 *
 * @param {Number} year
 */
function isLeapYear(year) {
    return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
}

/**
 * 获取某一年份的某一月份的天数
 *
 * @param {Number} year
 * @param {Number} month
 */
function getMonthDays(year, month) {
    return [31, null, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month] || (isLeapYear(year) ? 29 : 28);
}
</script>
</@master.html>