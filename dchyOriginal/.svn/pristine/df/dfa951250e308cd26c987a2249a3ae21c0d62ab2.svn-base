layui.use(['jquery', 'element', 'form', 'table'], function () {
    var $ = layui.jquery
        , element = layui.element
        , form = layui.form
        , table = layui.table;
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

        //获取阶段/图层
        $.ajax({
            url: _url + "/rest/v1.0/onemap/getchjdmc/1111111",
            type: 'post',
            success: function (data) {
                data.forEach(function (item) {
                    $('.stage-select').append('<option value="' + item.chjdmc + '">' + item.chjdmc + '</option>');
                }, this);
                $('.stage-select').val(data[0].chjdmc);
                getOptions(data[0].chjdmc);
                form.render();
            }
        });

        //监听阶段选择
        form.on('select(stageSelect)', function (data) {
            getOptions(data.value);
        });
        //渲染option选项
        function getOptions(val) {
            $.ajax({
                url: getContextPath() + "/oms/rest/v1.0/dchy/tcxx",
                type: 'post',
                data: {
                    lx: val
                },
                success: function (data) {
                    data.forEach(function (item) {
                        $('.layer-select').append('<option value="' + item.dm + '">' + item.mc + '</option>');
                    }, this);
                    form.render();
                }
            });
        }
        // 表格
        table.render({
            elem: '#contentTable'
            , cols: [[
                { field: 'name', title: '内容', align: 'center', width: 150 },
                { field: 'value', title: '值', align: 'center' }
            ]]
            , data: [
                { name: '多测合一编号', value: '2019031200103' },
                { name: '标识码', value: '1618' },
                { name: '测绘阶段', value: '竣工验收' },
                { name: '要素编码', value: '7001010000' },
                { name: '栋号', value: '1栋' },
                { name: '自然幢号', value: '1' },
                { name: '是否进行过预测', value: '是' },
                { name: '建成年代', value: '2019' }
            ]
        });

    })
})