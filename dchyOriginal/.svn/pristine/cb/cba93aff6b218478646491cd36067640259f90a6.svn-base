layui.use(['jquery', 'element', 'form', 'table'], function () {
    var $ = layui.jquery
        , element = layui.element
        , form = layui.form
        , table = layui.table;
    $(function () {
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