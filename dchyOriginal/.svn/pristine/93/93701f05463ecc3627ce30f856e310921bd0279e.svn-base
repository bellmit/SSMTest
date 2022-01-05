<template>
    <div>
        <div class="search-form">
            <Form :model="recordForm" ref="search-form" inline :label-width="120">
                <Row>
                    <i-col span="12">
                        <FormItem label="备案时间" style="width: 100%">
                            <Row class="form-list-search">
                                <i-col span="11">
                                    <DatePicker class="form-search-item"  @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="recordForm.kssj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                                <i-col span="1">
                                    <div style="text-align:center">-</div>
                                </i-col>
                                <i-col span="12">
                                    <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="recordForm.jssj" type="date" placeholder="请选择"></DatePicker>
                                </i-col>
                            </Row>
                        </FormItem>
                    </i-col>
                    <i-col span="6">
                        <FormItem :label-width="50">
                            <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryAllData()">查询</Button>
                            <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                        </FormItem>
                    </i-col>
                </Row>
            </Form>
        </div>
        <div class="line-dashed"></div>
        <div class="classify-detail">
            <div class="qx-xmzt-classify">
                <div class="form-title">
                    <div class="list-title">各区县项目状态统计</div>
                </div>
                <div>
                    <div id="qx-xmzt" style="width: 100%;height: 300px"></div>
                </div>
            </div>
             <div class="qx-xmwtfs-classify">
                <div class="form-title">
                    <div class="list-title">各区县项目委托方式统计</div>
                </div>
                <div>
                    <div id="qx-xmwtfs" style="width: 100%;margin: 0 auto;height: 300px"></div>
                </div>
            </div>
        </div>
        <div class="line-dashed"></div>
        <div class="classify-detail">
            <div class="qx-xmzb-classify">
                <div class="form-title">
                    <div class="list-title">各区县项目占比统计</div>
                </div>
                <div>
                    <div id="qx-xmzb" style="width: 100%;height: 300px"></div>
                </div>
            </div>
            <div class="qs-xmzt-classify">
                <div class="form-title">
                    <div class="list-title">全市项目状态统计</div>
                </div>
                <div>
                    <div id="qs-xmzt" style="width: 100%;height: 300px"></div>
                </div>
            </div>
            <div class="qs-xmwtfs-classify">
                <div class="form-title">
                    <div class="list-title">全市项目委托方式统计</div>
                </div>
                <div>
                    <div id="qs-xmwtfs" style="width: 100%;height: 300px"></div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import * as echarts from 'echarts';
import moment from "moment";
import { queryQxxmztData, queryQxxmwtfsData } from "../../service/statistics"
export default {
    data() {
        return {
            recordForm: {
                kssj: "",
                jssj: "",
            },
            ksOptions: {},
            jsOptions: {},
            qxxmztOptions: {
                color: ["#f85858","#4cc0e8"],
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['进行中', '已完成'],
                    bottom: 0
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    top: 10,
                    bottom: '10%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        show: true,
                        width: 100,
                        overflow: "breakAll",
                        interval: 0,
                        color: "#333",
                    },
                    data: []
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: '进行中',
                        type: 'bar',
                        data: []
                    },
                    {
                        name: '已完成',
                        type: 'bar',
                        data: []
                    }
                ]
            },
            qxxmwtfsOptions: {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    }
                },
                legend: {
                    data: ['线上委托', '线下登记'],
                    bottom: 0
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    top: 10,
                    bottom: '10%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    boundaryGap: [0, 0.01]
                },
                yAxis: {
                    type: 'category',
                    data: []
                },
                series: [
                    {
                        name: '线上委托',
                        type: 'bar',
                        stack: 'total',
                        data: []
                    },
                    {
                        name: '线下登记',
                        type: 'bar',
                        stack: 'total',
                        data: []
                    }
                ]
            },
            qxxmzbOptions: {
                color: ["#5470c6","#f85858","#658090","#fcdc88","#96c847","#4cc0e8","#e68787"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}%）'
                },
                series: [
                    {
                        name: '各区县项目占比统计',
                        type: 'pie',
                        radius: ['40%', '60%'],
                        avoidLabelOverlap: false,
                        label: {
                            show: true
                        },
                        labelLine: {
                            show: true
                        },
                        data: [
                            {value: 1048, name: '溧阳市'},
                            {value: 735, name: '天宁区'},
                            {value: 580, name: '钟楼区'},
                            {value: 484, name: '新北区'},
                            {value: 300, name: '武进区'},
                            {value: 300, name: '金坛区'},
                        ]
                    }
                ]
            },
            qsxmztOptions:  {
                color: ["#f85858","#28bdf1"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}%）'
                },
                series: [
                    {
                        name: '全市项目状态统计',
                        type: 'pie',
                        radius: '60%',
                        data: [],
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            },
            qsxmwtfsOptions:  {
                color: ["#5470c6","#91cc75"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}%）'
                },
                series: [
                    {
                        name: '全市项目委托方式统计',
                        type: 'pie',
                        radius: '60%',
                        data: [
                            {value: 735, name: '线上委托'},
                            {value: 1048, name: '线下登记'},
                        ],
                        emphasis: {
                            itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            },
            qxxmztEcharts: "",
            qxxmwtfsEcharts: "",
            qxxmzbEcharts: "",
            qsxmztEcharts: "",
            qsxmwtfsEcharts: ""
        }
    },
    mounted() {
        this.qxxmztEcharts = echarts.init(document.getElementById("qx-xmzt"))
        this.qxxmwtfsEcharts = echarts.init(document.getElementById("qx-xmwtfs"))
        this.qxxmzbEcharts = echarts.init(document.getElementById("qx-xmzb"))
        this.qsxmztEcharts = echarts.init(document.getElementById("qs-xmzt"))
        this.qsxmwtfsEcharts = echarts.init(document.getElementById("qs-xmwtfs"))
        this.queryAllData()
    },
    methods: {
        // 各区县项目状态统计
        queryQxxmztData(){
            this.recordForm.kssj = this.recordForm.kssj ? moment(this.recordForm.kssj).format("YYYY-MM-DD") : ""
            this.recordForm.jssj = this.recordForm.jssj ? moment(this.recordForm.jssj).format("YYYY-MM-DD") : ""
            this.$loading.show("加载中...")
            queryQxxmztData(this.recordForm).then(res => {
                this.$loading.close();
                let xAxisData = []
                let processingData = [];
                let xmztData = [];
                let finishData = [];
                let qxxmzbData = [];
                if(res.data.XmslFromlys&&res.data.XmslFromlys.length){
                    res.data.XmslFromlys.forEach(list =>  {
                        res.data.XmslFromXmgl.push(list)
                    })
                }
                if(res.data.xmslFromBsdtlys&&res.data.xmslFromBsdtlys.length){
                    res.data.xmslFromBsdtlys.forEach(list => {
                        res.data.XmslFromBdst.push(list)
                    })
                }
                res.data.XmslFromXmgl.forEach(list => {
                    res.data.XmslFromBdst.forEach(item => {
                            if(list.QXMC == item.QXMC && list.XMZT == "进行中"){
                                list.XMSL += item.XMSL
                            }
                    })
                    if(!xAxisData.includes(list.QXMC)){
                        xAxisData.push(list.QXMC);
                        qxxmzbData.push({
                            value: list.XMSL,
                            name: list.QXMC
                        })
                    }
                    let findIndex = xmztData.findIndex(xmzt => xmzt.name == list.XMZT)
                    let find = xmztData.find(xmzt => xmzt.name == list.XMZT)
                    if(findIndex == '-1'){
                        xmztData.push({
                            name: list.XMZT,
                            value: list.XMSL
                        })
                    } else {
                        xmztData[findIndex].value = find.value + list.XMSL
                    }
                    if(list.XMZT === "进行中"){
                        processingData.push(list.XMSL);
                    } else {
                        finishData.push(list.XMSL)
                    }
                })
                this.qxxmztOptions.xAxis.data = xAxisData
                this.qxxmztOptions.series[0].data = processingData
                this.qxxmztOptions.series[1].data = finishData
                this.qxxmzbOptions.series[0].data = qxxmzbData
                this.qsxmztOptions.series[0].data = xmztData
                this.qxxmztEcharts.setOption(this.qxxmztOptions)
                this.qxxmzbEcharts.setOption(this.qxxmzbOptions)
                this.qsxmztEcharts.setOption(this.qsxmztOptions)
            })
        },
        // 各区县项目委托方式统计
        queryQxxmwtfsData(){
            this.recordForm.kssj = this.recordForm.kssj ? moment(this.recordForm.kssj).format("YYYY-MM-DD") : ""
            this.recordForm.jssj = this.recordForm.jssj ? moment(this.recordForm.jssj).format("YYYY-MM-DD") : ""
            this.$loading.show("加载中...")
            queryQxxmwtfsData(this.recordForm).then(res => {
                this.$loading.close();
                let xAxisData = [];
                let xAxisNameData = []
                let xsData = [];
                let xsCount = 0;
                let xxData = [];
                let xxCount = 0;
                let maxCount = 0;
                if(res.data.XmslFromlys&&res.data.XmslFromlys.length){
                    res.data.XmslFromlys.forEach(list =>  {
                        res.data.XmslFromXmgl.push(list)
                    })
                }
                if(res.data.xmslFromBsdtlys&&res.data.xmslFromBsdtlys.length){
                    res.data.xmslFromBsdtlys.forEach(list => {
                        res.data.XmslFromBdst.push(list)
                    })
                }
                res.data.XmslFromXmgl.forEach(list => {
                    res.data.XmslFromBdst.forEach(item => {
                            if(list.QXMC == item.QXMC && list.XMLY == "线上委托"){
                                list.XMSL += item.XMSL
                            }
                    })
                    if(!xAxisNameData.includes(list.QXMC)){
                        xAxisNameData.push(list.QXMC)
                    }
                    if(maxCount < list.XMSL){
                        maxCount = list.XMSL
                    }
                    if(list.XMLY == "线上委托"){
                        xsCount += list.XMSL
                        xsData.push(list.XMSL)
                    } else {
                        xxCount += list.XMSL
                        xxData.push(list.XMSL)
                    }
                })
                this.qxxmwtfsOptions.yAxis.data = xAxisNameData;
                this.qxxmwtfsOptions.series[0].data = xsData
                this.qxxmwtfsOptions.series[1].data = xxData
                this.qsxmwtfsOptions.series[0].data[0].value = xsCount
                this.qsxmwtfsOptions.series[0].data[1].value = xxCount
                this.qxxmwtfsEcharts.setOption(this.qxxmwtfsOptions)
                this.qsxmwtfsEcharts.setOption(this.qsxmwtfsOptions)
            })
        },
        // 获取所有
        queryAllData(){
            this.queryQxxmztData();
            this.queryQxxmwtfsData();
        },
        resetForm(){
            this.recordForm = {
                kssj: "",
                jssj: "",
            }
        },
        // 控制结束时间可选状态
        kssjChange(select){
            this.jsOptions.disabledDate = (date) => {
                return date && (moment(date).unix() < moment(select).unix());
            }
        },
        // 控制开始时间可选状态
        jssjChange(select){
            this.ksOptions.disabledDate = (date) => {
                return date && (moment(date).unix() > moment(select).unix());
            }
        },
    },
}
</script>
<style scoped>
    .classify-detail {
        display: flex;
        justify-content: flex-start;
    }
    .qx-xmzt-classify {
        width: 60%;
        padding: 10px 0;
    }
    .qx-xmwtfs-classify {
        width: 40%
    }
    .qx-xmzb-classify {
        width: 30%
    }
    .qs-xmzt-classify {
        width: 30%
    }
    .qs-xmwtfs-classify {
        width: 40%
    }
</style>