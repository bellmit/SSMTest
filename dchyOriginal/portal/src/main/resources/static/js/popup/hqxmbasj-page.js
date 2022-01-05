layui.use(['element', 'jquery', 'table', 'form'], function () {
    var element = layui.element,
        $ = layui.jquery,
        laydate = layui.laydate,
        table = layui.table,
        form = layui.form;

    var xmData = [];
    var lxrphpne ={};
    var  tableresult;
    var   allchdwmc ={};

    var  xmdz;

    var  xmdzmc

    table.render({
          elem: '#searchTable'
          , page: true
            ,id: 'searchReload'
           ,url:getContextPath(1) + "/msurveyplat-server/rest/v1.0/ObtainXmBanSj/sld/"
          , cols: [[
              {
                  field: 'XMHTBH',
                  title: '项目（合同）编号',
                  align: 'center',
                  event: 'importData',
                  style: 'color:#1d87d1;cursor: pointer;'
              }
              , {field: 'XMMC', title: '项目名称', align: 'center', width: 150}
              , {field: 'JSDW', title: '建设单位', align: 'center', width: 150}
              , {field: 'CHDW', title: '测绘单位', align: 'center', width: 200}
              , {field: 'XMDZ', title: '项目地址', align: 'center'}
          ]],
      });


    // 导入数据
    // 得到当前iframe层的索引
    var index = parent.layer.getFrameIndex(window.name);
    table.on('tool(searchTable)', function (obj) {
        var data = obj.data; //获得当前行数据
        if (obj.event === 'importData') {
            baseLayer('导入当前数据', function () {
                setSlResult(data)
                getLxrPhone(obj.data.MLKID,"1")
                setPhonelxr(lxrphpne)
                layer.msg('导入成功！');
                parent.layer.close(index);
            });
        }
    });

    $('#search').click(function () {
        var xmhtbh = $("#xmhtbh").val();
        var xmmc = $("#xmmc").val();
        var jsdw = $("#jsdw").val();
        var chdw = $("#chdw").val();
        table.reload('searchReload', {
            url:getContextPath(1) + "/msurveyplat-server/rest/v1.0/ObtainXmBanSj/sld/",
            page: {
                curr: 1 //重新从第 1 页开始
            }
            ,where: {xmhtbh:xmhtbh ,xmmc:xmmc,jsdw:jsdw,chdw:chdw}
        }, );
    });


    function  setSlResult(data) {
        console.log(data);
        parent.$('#iframeBox').contents().find("input[name='jsdwlxr']").eq(0).val(data.JSDWLXR);
        parent.$('#iframeBox').contents().find("input[name='xmmc']").eq(0).val(data.XMMC);
        parent.$('#iframeBox').contents().find("input[name='chxmbh']").eq(0).val(data.XMHTBH);
        /*项目地址拼接*/
        var xmdz ={};
        xmdz= getxmdz(data.CHGCBH);
        var paramdzmc =[];
        paramdzmc.push(xmdz[0].GCDZSS)
        paramdzmc.push(xmdz[0].GCDZQX)
        var result =[]
        result =getxmdzmc(paramdzmc);
        parent.$('#iframeBox').contents().find("input[name='xmdz']").eq(0).val(result[0]+result[1]+xmdz[0].GCDZXX);

        parent.$('#iframeBox').contents().find("input[name='jsdw']").eq(0).val(data.JSDW)
        parent.$('#iframeBox').contents().find("input[name='sqrlxdh']").eq(0).val(data.JSDWLXDH)


        // var chdw =data.CHDW
        //先要找到所有的 测绘单位名称和代码


        var  chdwmc = queryChdwMc();
         //查询data的dm 下标是第几个
        var a ;
        for(var i=0;i<chdwmc.length;i++){
            if(data.CHDW ==chdwmc[i].MC){
                 a =i
                 return;
             }
        }
        setChdwmc(0,chdwmc);
          //前台必须要先加载完毕才可以 要按照顺序加载不软
        // for (var i=0;i<chdwmc.length;i++){
        //     if($(chdwDom[i+1]).attr('lay-value') ==chdwmc[i].DM ){//这里1是chdw的dm
        //         console.log(i);
        //         console.log(chdwmc[i].MC);
        //         parent.$('#iframeBox').contents().find("#chdw-select-box").eq(0).find('input').eq(0).val(chdwmc[i].MC)//这里1是chdw的mc
        //         $(chdwDom[i]).siblings().removeClass('layui-this');
        //         $(chdwDom[i]).attr('layui-this');
        //     }
        // }
    }

    function  setPhonelxr(lpdata) {
        parent.$('#iframeBox').contents().find("input[name='chdwdlr']").eq(0).val(lpdata.LXR);
        parent.$('#iframeBox').contents().find("input[name='lxdh']").eq(0).val(lpdata.LXDH);
    }

     function setChdwmc(index,chdwmc){
         var chdwDom = parent.$('#iframeBox').contents().find("#chdw-select-box").eq(0).find('dd');
         parent.$('#iframeBox').contents().find("#chdw-select-box").eq(0).find('input').eq(0).val(chdwmc[0].MC)//这里1是chdw的mc
         $(chdwDom[index]).siblings().removeClass('layui-this');
         $(chdwDom[index]).attr('layui-this');
     }



    /**
     * 查找所有的测绘名称和代码
     */
    function  queryChdwMc() {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/queryChdwMc",
            type: "POST",
            dataType: 'JSON',
            async:false,
            success: function (res) {
                allchdwmc = res;
            }
        });
        return allchdwmc

    }

    function getData(paramPage) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/ObtainXmBanSj/sld/",
            data: paramPage,
            type: "POST",
            dataType: 'JSON',
            async:false,
            success: function (res) {
                xmData = res.content;
            }
        });

    }


    function getLxrPhone(mlkid,chdwlx) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/ObtainXmBanSj/sld/lxrphone",
            data: {"mlkid":mlkid,"chdwlx":chdwlx},
            type: "POST",
            dataType: 'JSON',
            async:false,
            success: function (res) {
                lxrphpne = res;
            }
        });


    }


    function  getxmdz(gcbh) {
        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/queryXmdz",
            data: {gcbh:gcbh},
            type: "POST",
            dataType: 'JSON',
            async:false,
            success: function (res) {
                xmdz = res;
            }
        });
        return xmdz;
    }

    function  getxmdzmc(xmzdmc) {

        $.ajax({
            url: getContextPath(1) + "/msurveyplat-server/rest/v1.0/queryXmdzmc",
            data: JSON.stringify(xmzdmc),
            contentType: "application/json;charset=utf-8",
            type: "POST",
            dataType: 'JSON',
            async:false,
            success: function (res) {
                xmdzmc = res;
            }
        });
        return xmdzmc;

    }
})


















