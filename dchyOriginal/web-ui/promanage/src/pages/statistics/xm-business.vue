<template>
    <div>
        <Tabs :value="tabValue" @on-click="clickTab">
            <TabPane label="按年份" name="ForYear">
                <div class="search-form">
                    <Form :model="recordForm" ref="search-form" inline :label-width="120">
                        <Row>
                            <i-col span="6">
                                <FormItem label="委托年份" class="form-list-search" v-model="recordForm.year">
                                    <Select filterable clearable v-model="recordForm.year">
                                        <Option v-for="(item) in wtnfList" :key="item.WTNF" :value="item.WTNF">{{item.WTNF}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="委托季度" class="form-list-search" v-model="recordForm.quarter">
                                    <Select filterable clearable v-model="recordForm.quarter" @on-change="selectQuarterChange">
                                        <Option v-for="(item) in quarterList" :key="item.DM" :value="item.DM">{{item.MC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="委托月份" class="form-list-search" v-model="recordForm.month">
                                    <Select filterable clearable v-model="recordForm.month">
                                        <Option v-for="(item) in monthList" :key="item.DM" :value="item.DM">{{item.MC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="建设单位" class="form-list-search" v-model="recordForm.jsdwmc">
                                    <Select filterable clearable v-model="recordForm.jsdwmc">
                                        <Option v-for="(item,index) in jsdwList" :key="item.JSDWMC+index" :value="item.JSDWMC">{{item.JSDWMC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="测绘单位" class="form-list-search" v-model="recordForm.chdwmc">
                                    <Select filterable clearable v-model="recordForm.chdwmc">
                                        <Option v-for="(item,index) in chdwList" :key="item.CHDWMC+index" :value="item.CHDWMC">{{item.CHDWMC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem :label-width="50">
                                    <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryAllData(1,recordForm.pageSize)">查询</Button>
                                    <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                                </FormItem>
                            </i-col>
                        </Row>
                    </Form>
                </div>
                <div class="line-dashed"></div>
                <div class="project-statistic" id="anf-tj">
                    <div class="xmsl-statistic">
                        <div class="form-title">
                            <div class="list-title">各年份委托数量统计</div>
                        </div>
                        <div id="xmsl" style="width: 100%;height: 300px"></div>
                    </div>
                    <div class="xmzb-statistic">
                        <div class="form-title">
                            <div class="list-title">各状态委托占比统计</div>
                        </div>
                        <div id="xmzb" style="width: 100%;height: 300px"></div>
                    </div>
                </div>
                <div class="line-dashed"></div>
                <div style="padding: 0 10px">
                    <div class="form-title">
                        <div class="list-title">委托记录</div>
                        <div>
                            <Button type="primary" class="bdc-major-btn" @click="exportWtjl">导出</Button>
                        </div>
                    </div>
                    <div>
                        <Table
                            ref="tableRef"
                            :cols="tableCols"
                            :data="bajlList"
                            :count="totalNum"
                            :page="recordForm.page"
                            :size="recordForm.pageSize"
                            :func="queryAllData"
                            :unShowTool="true"
                            :showHeadCheckBox="false"
                        ></Table>
                    </div>
                </div>
            </TabPane>
            <TabPane label="按阶段" name="ForSteps">
                <div class="search-form">
                    <Form :model="recordForm" ref="search-form" inline :label-width="120">
                        <Row>
                            <i-col span="12">
                                <FormItem label="委托时间" style="width: 100%">
                                    <Row class="form-list-search">
                                        <i-col span="11">
                                            <DatePicker class="form-search-item"  @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="recordForm.wtkssj" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                        <i-col span="1">
                                            <div style="text-align:center">-</div>
                                        </i-col>
                                        <i-col span="12">
                                            <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="recordForm.wtjssj" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                    </Row>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="项目状态" class="form-list-search" v-model="recordForm.xmzt">
                                    <Select filterable clearable v-model="recordForm.xmzt">
                                        <Option v-for="(item) in xmztList" :key="item.DM" :value="item.DM">{{item.MC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem :label-width="50">
                                    <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryAllData(1,recordForm.pageSize)">查询</Button>
                                    <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                                </FormItem>
                            </i-col>
                        </Row>
                    </Form>
                </div>
                <div class="line-dashed"></div>
                <div  id="ajd-tj">
                    <div class="project-statistic">
                        <div class="xmsl-statistic">
                            <div class="form-title">
                                <div class="list-title">各地区各阶段项目数量统计</div>
                            </div>
                            <div>
                                <div id="gdqjdXmsl" style="width: 100%;height: 300px"></div>
                            </div>
                        </div>
                        <div class="xmzb-statistic">
                            <div class="form-title">
                                <div class="list-title">各阶段项目占比统计</div>
                            </div>
                            <div>
                                <div id="gjdXmzb" style="width: 100%;height: 300px"></div>
                            </div>
                        </div>
                    </div>
                    <div class="line-dashed"></div>
                    <div style="padding: 0 10px">
                        <div class="form-title">
                            <div class="list-title">项目数量统计</div>
                        </div>
                        <div>
                            <Table
                                ref="jdXmslTableRef"
                                :id="'jdxmslId'"
                                :cols="jdTableCols"
                                :data="jdxmslList"
                                :showPage='false'
                                :unShowTool="true"
                                :showHeadCheckBox="false"
                                @deleteOpr="renderHeader"
                            ></Table>
                        </div>
                    </div>
                </div>
            </TabPane>
            <!-- <TabPane label="按超期" name="ForCq">
                <div class="search-form">
                    <Form :model="recordForm" ref="search-form" inline :label-width="120">
                        <Row>
                            <i-col span="12">
                                <FormItem label="委托时间" style="width: 100%">
                                    <Row class="form-list-search">
                                        <i-col span="11">
                                            <DatePicker class="form-search-item"  @on-change="kssjChange" format="yyyy-MM-dd" :options="ksOptions" v-model="recordForm.wtkssj" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                        <i-col span="1">
                                            <div style="text-align:center">-</div>
                                        </i-col>
                                        <i-col span="12">
                                            <DatePicker class="form-search-item" @on-change="jssjChange" format="yyyy-MM-dd" :options="jsOptions" v-model="recordForm.wtjssj" type="date" placeholder="请选择"></DatePicker>
                                        </i-col>
                                    </Row>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem label="是否超期" class="form-list-search" v-model="recordForm.xmzt">
                                    <Select filterable clearable v-model="recordForm.xmzt">
                                        <Option v-for="(item) in sfcqList" :key="item.DM" :value="item.DM">{{item.MC}}</Option>
                                    </Select>
                                </FormItem>
                            </i-col>
                            <i-col span="6">
                                <FormItem :label-width="50">
                                    <Button type="primary" class="btn-h-32 bdc-major-btn" @click="queryAllData(1,recordForm.pageSize)">查询</Button>
                                    <Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
                                </FormItem>
                            </i-col>
                        </Row>
                    </Form>
                </div>
                <div class="line-dashed"></div>
                <div id="acq-tj">
                    <div class="project-statistic">
                        <div class="xmsl-statistic">
                            <div class="form-title">
                                <div class="list-title">各地区超期项目数量统计</div>
                            </div>
                            <div>
                                <div id="gdqCqsl" style="width: 100%;height: 300px"></div>
                            </div>
                        </div>
                        <div class="xmzb-statistic">
                            <div class="form-title">
                                <div class="list-title">各地区超期项目占比统计</div>
                            </div>
                            <div>
                                <div id="gdqCqzb" style="width: 100%;height: 300px"></div>
                            </div>
                        </div>
                    </div>
                    <div class="line-dashed"></div>
                    <div class="project-statistic">
                        <div>
                            <div class="form-title">
                                <div class="list-title">各测绘单位超期项目数量统计</div>
                            </div>
                            <div>
                                <div id="gchdwCqsl" style="width: 100%;height: 300px"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="line-dashed"></div>
                <div style="padding: 0 10px">
                    <div class="form-title">
                        <div class="list-title">超期项目数量统计</div>
                    </div>
                    <div>
                        <Table
                            ref="cqXmslTableRef"
                            :id="'cqxmslId'"
                            :cols="cqTableCols"
                            :data="cqxmslList"
                            :showPage='false'
                            :unShowTool="true"
                            :showHeadCheckBox="false"
                        ></Table>
                    </div>
                </div>
            </TabPane> -->
            <Button slot="extra" type="primary" class="bdc-major-btn" @click="exportData">统计报告输出</Button>
        </Tabs>
    </div>
</template>
<script>
import * as echarts from 'echarts';
import moment from "moment";
import { queryYwfxData } from "../../service/statistics"
export default {
    data() {
        return {
            recordForm: {
                year: "",
                wtkssj: "",
                wtjssj: "",
                jsdwmc: "",
                chdwmc: "",
                xmzt: "",
                pageflag: false,
                page: 1,
                pageSize: 10
            },
            cqTableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "BABH",
                    title: "项目编号",
                    align: "center"
                },
                {
                    field: "GCMC",
                    title: "工程名称",
                    align: "center"
                },
                {
                    field: "JSDWMC",
                    title: "建设单位",
                    align: "center"
                },
                {
                    field: "WTSJ",
                    title: "委托时间",
                    align: "center"
                },
                {
                    field: "JCRQ",
                    title: "进场日期",
                    align: "center"
                },
                {
                    field: "CHSX",
                    title: "测绘时限",
                    align: "center"
                },
                {
                    field: "CQTS",
                    title: "超期天数",
                    align: "center"
                }
            ],
            cqxmslList: [],
            quarterList: [
                {
                    DM: "1",
                    MC: "第一季度",
                    KEY: "first"
                },
                {
                    DM: "2",
                    MC: "第二季度",
                    KEY: "second"
                },
                {
                    DM: "3",
                    MC: "第三季度",
                    KEY: "third"
                },
                {
                    DM: "4",
                    MC: "第四季度",
                    KEY: "fourth"
                }
            ],
            monthList: [],
            firstQuarterList: [
                {
                    DM: "01",
                    MC: "一月"
                },
                {
                    DM: "02",
                    MC: "二月"
                },
                {
                    DM: "03",
                    MC: "三月"
                }
            ],
            secondQuarterList: [
                {
                    DM: "04",
                    MC: "四月"
                },
                {
                    DM: "05",
                    MC: "五月"
                },
                {
                    DM: "06",
                    MC: "六月"
                }
            ],
            thirdQuarterList: [
                {
                    DM: "07",
                    MC: "七月"
                },
                {
                    DM: "08",
                    MC: "八月"
                },
                {
                    DM: "09",
                    MC: "九月"
                }
            ],
            fourthQuarterList: [
                {
                    DM: "10",
                    MC: "十月"
                },
                {
                    DM: "11",
                    MC: "十一月"
                },
                {
                    DM: "12",
                    MC: "十二月"
                }
            ],
            tabValue: "ForYear",
            xmslList: [],
            wtnfList: [],
            jsdwList: [],
            chdwList: [],
            sfcqList: [
                {
                    DM: "1",
                    MC: "超期"
                },
                {
                    DM: "2",
                    MC: "未超期"
                }
            ],
            xmztList: [
                {
                    DM: "1",
                    MC: "待接受"
                },
                {
                    DM: "2",
                    MC: "已接受"
                },
                {
                    DM: "3",
                    MC: "已备案"
                },
                {
                    DM: "4",
                    MC: "已办结"
                }
            ],
            jdxmslList: [],
            jdTableCols: [
                {
                    field: "QXMC",
                    title: "",
                    width: 95,
                    align: "center"
                }
            ],
            tableCols: [
                {
                    field: "ROWNUM_",
                    title: "序号",
                    align: "center",
                    width: 70,
                    fixed: "left"
                },
                {
                    field: "XQFBBH",
                    title: "委托编号",
                    align: "center"
                },
                {
                    field: "CHGCBH",
                    title: "项目代码",
                    align: "center"
                },
                {
                    field: "GCMC",
                    title: "工程名称",
                    align: "center"
                },
                {
                    field: "JSDWMC",
                    title: "建设单位",
                    align: "center"
                },
                {
                    field: "CHDWMC",
                    title: "测绘单位",
                    align: "center"
                },
                {
                    field: "CHJD",
                    title: "测绘阶段",
                    align: "center"
                },
                {
                    field: "WTSJ",
                    title: "委托时间",
                    align: "center"
                },
                {
                    field: "WTZT",
                    title: "当前状态",
                    align: "center"
                },
            ],
            bajlList: [],
            totalNum: 0,
            ksOptions: {},
            jsOptions: {},
            xmslOptions:  {
                color: ["#f85858"],
                tooltip: {
                    trigger: 'item'
                },
                xAxis: {
                    type: 'category',
                    data: [],
                    axisLabel: {
                        show: true,
                        width: 100,
                        overflow: "breakAll",
                        interval: 0,
                        color: "#333",
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: [],
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true, //开启显示
                                position: 'inside', //在上方显示
                                textStyle: { //数值样式
                                    color: '#fff',
                                    fontSize: 16
                                }
                            }
                        }
                    }
                }]
            },
            gdqjdXmslOptions: {
                color: ["#8497b0","#ffc000","#b3dba5","#ed7d31","#4472c4"],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                        type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        data: []
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: []
            },
            gdqCqslOptions:  {
                color: ["#f85858"],
                tooltip: {
                    trigger: 'item'
                },
                xAxis: {
                    type: 'category',
                    data: [],
                    axisLabel: {
                        show: true,
                        width: 100,
                        overflow: "breakAll",
                        interval: 0,
                        color: "#333",
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: [],
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true, //开启显示
                                position: 'inside', //在上方显示
                                textStyle: { //数值样式
                                    color: '#fff',
                                    fontSize: 16
                                }
                            }
                        }
                    }
                }]
            },
            gchdwCqslOptions:  {
                color: ["#f85858"],
                tooltip: {
                    trigger: 'item'
                },
                xAxis: {
                    type: 'category',
                    data: [],
                    axisLabel: {
                        show: true,
                        width: 100,
                        overflow: "breakAll",
                        interval: 0,
                        color: "#333",
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: [],
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true, //开启显示
                                position: 'inside', //在上方显示
                                textStyle: { //数值样式
                                    color: '#fff',
                                    fontSize: 16
                                }
                            }
                        }
                    }
                }]
            },
            wtZtList: ["待接受","已接受","已备案","已办结"],
            xmzbOptions:  {
                color: ["#ed7d31","#ffc000","#b3dba5","#4472c4","#8497b0"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}）%'
                },
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '60%',
                        data: [],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true, //开启显示
                                    formatter: '{b} : {c} ({d}%)'
                                }
                            }
                        },
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
            gjdXmzbOptions:  {
                color: ["#ed7d31","#ffc000","#b3dba5","#4472c4","#8497b0"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}）%'
                },
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '60%',
                        data: [],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true, //开启显示
                                    formatter: '{b} : {c} ({d}%)'
                                }
                            }
                        },
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
            gdqCqzbOptions:  {
                color: ["#ed7d31","#ffc000","#b3dba5","#4472c4","#8497b0"],
                tooltip: {
                    trigger: 'item',
                    formatter: '{b}: {c}（{d}）%'
                },
                series: [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius: '60%',
                        data: [],
                        itemStyle: {
                            normal: {
                                label: {
                                    show: true, //开启显示
                                    formatter: '{b} : {c} ({d}%)'
                                }
                            }
                        },
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
            xmslEcharts: "",
            xmzbEcharts: "",
            gdqjdXmslEcharts: "",
            gjdXmzbEcharts: ""
        }
    },
    mounted() {
        this.xmslEcharts = echarts.init(document.getElementById("xmsl"))
        this.xmzbEcharts = echarts.init(document.getElementById("xmzb"))
        this.gdqjdXmslEcharts = echarts.init(document.getElementById("gdqjdXmsl"))
        this.gjdXmzbEcharts = echarts.init(document.getElementById("gjdXmzb"))
        this.queryAllData();
    },
    methods: {
        renderHeader(){
            $("th[data-field=QXMC]").css({
                width: "95px",
                height: "40px",
                position: "relative"
            })
            $("th[data-field=QXMC] > div").css({
                position: "static",
                padding: "0px",

            })
            let innerHTML = '<span class="pf-tale-xzq">地区</span>'+
                '<span class="pf-table-jg">阶段</span>'+
                '<img class="pf-table-img" src="static/images/table.png" alt="">';
            $("th[data-field=QXMC] > div").html(innerHTML);
        },
        // 统计报告输出
        exportData(){
            $(".project-statistic").css("display","block")
            $(".project-statistic").css("width","50%")
            this.$loading.show("导出中...")
            let _self = this;
            if(this.tabValue == "ForYear"){
                this.getPdf("anf-tj","按年份统计",function(){
                    _self.$loading.close()
                    $(".project-statistic").css("display","flex")
                    $(".project-statistic").css("width","100%")
                })
            } else if(this.tabValue == "ForSteps"){
                this.getPdf("ajd-tj","按阶段统计",function(){
                    _self.$loading.close()
                    $(".project-statistic").css("display","flex")
                    $(".project-statistic").css("width","100%")
                })
            } else {
               this.getPdf("acq-tj","按超期统计",function(){
                    _self.$loading.close()
                    $(".project-statistic").css("display","flex")
                    $(".project-statistic").css("width","100%")
                }) 
            }
        },
        clickTab(name){
            this.tabValue = name;
        },
        selectQuarterChange(select){
            if(select){
                let find = this.quarterList.find(quarter => quarter.DM == select)
                this.monthList = this[find.KEY+"QuarterList"];
            } else {
                this.monthList = []
            }
        },
        // 获取所有数据
        queryAllData(page,size,change){
            if(page){
                this.recordForm.page = page;
                this.recordForm.pageSize = size;
            }
            if(change){
                this.recordForm.pageflag = true;
            } else {
                this.recordForm.pageflag = false;
            }
            this.$loading.show("加载中...")
            this.recordForm.wtkssj = this.recordForm.wtkssj ? moment(this.recordForm.wtkssj).format("YYYY-MM-DD") : ""
            this.recordForm.wtjssj = this.recordForm.wtjssj ? moment(this.recordForm.wtjssj).format("YYYY-MM-DD") : ""
            queryYwfxData(this.recordForm).then(res => {
                this.$loading.close();
                // 委托记录统计
                this.bajlList = res.data.WtjlByPage?res.data.WtjlByPage.rows : [];
                this.totalNum = res.data.WtjlByPage?res.data.WtjlByPage.totalElements : 0;
                if(this.recordForm.pageflag){
                    return;
                }
                this.wtnfList = res.data.XmslByYear || [];
                this.chdwList = [];
                this.jsdwList = [];
                // 测绘单位
                res.data.chdwList.forEach(chdw => {
                    if(chdw){
                        this.chdwList.push(chdw)
                    }
                })
                // 建设单位
                res.data.jsdwList.forEach(chdw => {
                    if(chdw){
                        this.jsdwList.push(chdw)
                    }
                })
                if(!this.recordForm.year){
                    // 各年份委托数量统计-按年份统计
                    let xAxisData = [];
                    let xmslData = [];
                    res.data.XmslByYear.forEach(list => {
                        xAxisData.push(list.WTNF);
                        xmslData.push(list.XMSL);
                    })
                    this.xmslOptions.xAxis.data = xAxisData;
                    this.xmslOptions.series[0].data = xmslData;
                    this.xmslEcharts.setOption(this.xmslOptions)
                } else {
                    // 各年份委托数量统计-按各月份统计
                    let xAxisData = [];
                    let xmslData = [];
                    res.data.XmslByMouth.forEach(list => {
                        xAxisData.push(list.WTSJ);
                        xmslData.push(list.XMSL);
                    })
                    this.xmslOptions.xAxis.data = xAxisData;
                    this.xmslOptions.series[0].data = xmslData;
                    this.xmslEcharts.setOption(this.xmslOptions)
                }
                // 各状态委托占比统计
                let xmzbData = [];
                res.data.XmslBywtzt.forEach(list => {
                    xmzbData.push({
                        name: list.WTZT,
                        value: list.XMSL
                    })
                })
                this.xmzbOptions.series[0].data = xmzbData;
                this.xmzbEcharts.setOption(this.xmzbOptions)
                // 各地区各阶段项目数量统计
                let gdqjdxAxis = []
                let seriesData = [];
                let jdTableCols = _.cloneDeep(this.jdTableCols)
                let jsTableData = [];
                res.data.XmslByQxAndChjd.forEach((chjd,i) => {
                    gdqjdxAxis.push(chjd.qxmc);
                    jsTableData.push({
                        QXMC: chjd.qxmc
                    })
                    chjd.data.forEach(item => {
                        jsTableData[i][item.CHJD] = item.XMSL;
                        if(!jdTableCols.find(data => data.title == item.CHJD)){
                            jdTableCols.push({
                                title: item.CHJD,
                                field: item.CHJD,
                                align: "center"
                            })
                        }
                        if(!seriesData.find(data => data.name == item.CHJD)){
                            let data = [];
                            data.push(item.XMSL)
                            seriesData.push({
                                name: item.CHJD,
                                type: 'bar',
                                stack: 'jd',
                                emphasis: {
                                    focus: 'series'
                                },
                                data: data
                            })
                        } else {
                            let findIndex = seriesData.findIndex(data => data.name == item.CHJD)
                            seriesData[findIndex].data.push(item.XMSL)
                        }
                    })
                })
                this.jdTableCols = _.cloneDeep(jdTableCols)
                this.$nextTick(() => {
                    this.$refs["jdXmslTableRef"].rendered();
                    this.jdxmslList = jsTableData
                })
                this.gdqjdXmslOptions.xAxis[0].data = gdqjdxAxis;
                this.gdqjdXmslOptions.series = seriesData;
                this.gdqjdXmslEcharts.setOption(this.gdqjdXmslOptions)
                // 各阶段项目占比统计
                let gjdxmzbData = [];
                res.data.XmslByChjd.forEach(list => {
                    gjdxmzbData.push({
                        name: list.CHJD,
                        value: list.XMSL
                    })
                })
                this.gjdXmzbOptions.series[0].data = gjdxmzbData;
                this.gjdXmzbEcharts.setOption(this.gjdXmzbOptions)
            })
        },
        resetForm(){
            this.recordForm = {
                ...this.recordForm,
                year: "",
                quarter: "",
                month: "",
                wtkssj: "",
                wtjssj: "",
                jsdwmc: "",
                chdwmc: "",
                pageflag: false,
                xmzt: ""
            }
        },
        // 导出委托记录
        exportWtjl(){
            location.href = config.msurveyplatContext + `/tjfx/exportWtjl?year=${this.recordForm.year}&jsdwmc=${this.recordForm.jsdwmc}&chdwmc=${this.recordForm.chdwmc}&exportflag=1`
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
    .project-statistic {
        display: flex;
        justify-content: flex-start;
        padding: 0 10px;
    }
    .project-statistic > div {
        width: 50%;
        padding-top: 10px;
    }
    .form-title {
        display: flex;
        justify-content: space-between;
    }
</style>