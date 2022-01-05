layui.use(['element', 'jquery', 'laytpl'], function () {
    var element = layui.element,
        $ = layui.jquery,
        laytpl = layui.laytpl;
    $(function () {
        var IP = getContextPath(1) + "/msurveyplat-server/v1.0/qualitycheck/";
        $.ajax({
            type: "post",
            url: IP + "wjxx/zjbg/" + parent.xmId + "/" + parent.slbh,
            contentType: 'application/json;charset=utf-8',
            async: false,
            success: function (data) {
                // console.log('zjjg:', data);
                var getFileTpl = fileTpl.innerHTML,
                    fileBox = document.getElementById('fileBox');
                laytpl(getFileTpl).render(data, function (html) {
                    fileBox.innerHTML = html;
                });
            }
        })
        $('#fileBox').on('click','.bdc-downLoad-btn',function(){
            window.open($(this).attr('data-id'));
        })
       
    })
})