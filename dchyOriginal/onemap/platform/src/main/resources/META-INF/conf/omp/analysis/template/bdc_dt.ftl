<#if tplData.iframeUrl??>
<iframe name="if_${key!}" style="width: 100%;height: 100%;min-height: 572px;" frameborder="no"
        border="0"
        marginwidth="0"
        marginheight="0" scrolling="no" allowtransparency="yes">

</iframe>

<script>

    var data = '${tplData.iframeData!}';
    var tpLink = '${tplData.iframeUrl!}';
    var k = '${key!}';

    var TF = "DJH";

    $(document).ready(function () {
        if(data === '') {
            $("#".concat(k)).html('<span class="text-muted">无数据</span>');
            return;
        }
        var arr = $.parseJSON(data);
        var tArr = [];
        $.each(arr, function (idx, item) {
            tArr.push(item[TF]);
        });
        if (tArr.length > 0) {
            goLink(tpLink, tArr.join(","));
        }
    });

    /***
     *
     * go to link if accessible
     * */
    function goLink(action, postData) {

        var tempForm = document.createElement("form");
        tempForm.method = "post";
        tempForm.action = action;
        tempForm.target = "if_".concat(k);

        var hideInput = document.createElement("input");
        hideInput.type = 'hidden';
        hideInput.name = 'djh';
        hideInput.value = postData;

        tempForm.appendChild(hideInput);
        document.body.appendChild(tempForm);
        tempForm.submit();
        document.body.removeChild(tempForm);
    }

</script>
<#else >
    <#include "./default.ftl" />
</#if>
