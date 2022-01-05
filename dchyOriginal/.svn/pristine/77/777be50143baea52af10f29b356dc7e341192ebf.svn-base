<#assign jsContent>
<script type="text/javascript">
    var lastSelected;
    $('.tree-menu').listtree();
    $('.tree-menu li').on("click", function () {
        if ($(this).attr('data-has-child') == 'false') {
            if (lastSelected != null) {
                $(lastSelected).find('span').css('background-color', '');
                $(lastSelected).find('span').css('border-radius', '0');
                $(lastSelected).find('span').css('color', 'black');
            }
            $(this).find('span').css('background-color', '#485358');
            $(this).find('span').css('border-radius', '1px');
            $(this).find('span').css('color', 'white');
            lastSelected = $(this);
        }
    });
    //
    $('.tree-menu li[data-has-child="false"]')[0].click();
    $('iframe').attr('src', $('.tree-menu li[data-has-child="false"] a')[0].href);
    $(document).ready(function(){
        var resultHeight = parseInt($('body').css('height'))-parseInt($('.navbar').css('height'))-
                parseInt($('.breadcrumb').css('height'))-50;
        $('#iframe').css('height',resultHeight);
        $('#iframe').css('min-height',resultHeight)
    });
</script>
</#assign>
<#assign cssContent>
<style type="text/css">
    .main{
        padding: 0px;
    }
    #resultCtn {
        border: 1px solid #e5e5e5;
        border-radius: 4px;
        position: relative;
        background-color: #faf7f7;
        overflow: hidden;
        word-break: break-all;
    }

    .row {
        margin-left: 0px;
    }

    .row a {
        color: #333333;
    }

    .span9 {
        width: 84%;
        margin-left: 9px;
    }

    .span3 {
        margin-left: 0px;
        width: 15%;
    }

    .icon-folder-open, .icon-folder-close {
        color: #ed8f2b;
    }

    .icon-copy {
        color: #0b69b8;
    }
</style>
</#assign>
<@base.main nav="chart" js=jsContent css=cssContent showFooter="false">
<div class="main">
    <div class="container" style="width: 98%">
        <ul class="breadcrumb">
            <li><a href="<@com.rootPath/>/portal2">首页</a> <span class="divider">/</span></li>
            <li class="active">统计报表</li>
        </ul>
        <div class="row">
            <div class="span3">
                <div style="border: 1px solid #ddd;padding-left: 5px;">
                    <ul class="nav tree-menu">
                        <@base.treeMenu nodes=nodes></@base.treeMenu>
                    </ul>
                </div>
            </div>
            <div class="span9">
                <div id="resultCtn">
                    <iframe id="iframe" name="iframe" src="" frameborder="0" width="100%" height="100%">

                    </iframe>
                </div>
            </div>
        </div>
    </div>
</div>
</@base.main>
