<template>
	<div>
		<div class="search-form">
			<Form ref="formInline" :model="formInline" :label-width="120" :rules="ruleInline" inline>
				<Row>
					<i-col span="6">
						<FormItem label="模板名称" class="form-list-search" prop="mbmc">
							<Input type="text" class="form-search-item" v-model="formInline.mbmc" placeholder="" @keydown.enter.native.prevent="getTemplateList(1,formInline.pageSize)"/>
						</FormItem>
					</i-col>
					<i-col span="6">
						<FormItem label="模板类型" class="form-list-search" prop="mblx">
							<Select v-model="formInline.mblx" class="form-search-item">
								<Option v-for="item in typeList" :value="item.value" :key="item.value" >{{ item.label }}</Option>
							</Select>
						</FormItem>
					</i-col>
					<i-col span="6">
						<FormItem :label-width="20">
							<Button type="primary" class="btn-h-32 bdc-major-btn" @click="getTemplateList(1,formInline.pageSize)">查询</Button>
							<Button type="primary" class="btn-h-32 btn-cancel margin-left-10" @click="resetForm()">重置</Button>
						</FormItem>
					</i-col>
				</Row>
			</Form>
		</div>
		<div class="announce-table">
			<Table
				:id="'templateTableId'"
				:cols="tableCols"
				:data="templateList"
				:size="formInline.pageSize"
				:page="formInline.page"
				:count="totalNum"
				:operation="operationList"
				:func="getTemplateList"
				:tool="tool"
				@disable="disableTemplate"
				@enable="enableTemplate"
				@deleteOpr="deleteOpr"
				@download="downloadFile"
				@delete="deleteTemplate"
				@btn1="addTemplate"
			></Table>
		</div>
	</div>
</template>
<script>
import util from "../../../service/util";
import { getTemplate, cancelTemplate } from "../../../service/template";
import { getUserInfo } from "../../../service/template";
import { enableAndDisabled } from "../../../service/template";
import { getMbid } from "../../../service/template";
export default {
	data() {
		return {
			ruleInline: {},
			formInline: {
				mbmc: "",
				mblx: "",
				page: 1,
				pageSize: 10,
			},
			tool: '<div>' +
				'<span class="layui-btn main-btn-a" lay-event="btn1">上传新模板</span>' +
			'</div>',
			typeList: [
				{
					value: "",
					label: "全部",
				},
				{
					value: "0",
					label: "合同模板",
				},
				{
					value: "1",
					label: "申请表模板",
				},
				{
					value: "2",
					label: "其他",
				},
				
			],
			operationList: ["disable", "enable","download","delete"],
			totalNum: 0,
			templateList: [],
			tableCols: [
				{
					title: "序号",
					align: "center",
					width: 70,
					field: "ROWNUM_",
					fixed: "left"
				},
				{
					field: "MBMC",
					title: "模板名称",
					align: "center",
				},
				{
					field: "MBLX",
					title: "模板类型",
					align: "center",
				},
				{
					field: "SCR",
					title: "上传人",
					align: "center",
				},
				{
					field: "SCSJ",
					title: "上传时间",
					align: "center"
				},
				{
					field: "MBQYZT",
					title: "状态",
					align: "center",
				},
				{
					title: "操作",
					align: "center",
					width: 240,
					toolbar: "#operation"
				},
			],
			userInfo: {},
			templateStatus: {
				mbid: "",
				mbqyzt: "",
				mblx: ""
			},
		};
	},
	mounted() {
		this.getTemplateList();
		// this.getUser();
	},
	methods: {
		getUser() {
			getUserInfo().then((res) => {
				this.userInfo = res.data;
				sessionStorage.setItem("userInfo", JSON.stringify(res.data));
			});
		},
		// 重置查询表单
		resetForm(){
			this.formInline = {
				page: this.formInline.page,
				pageSize: this.formInline.pageSize
			}
		},
		// 查询
		getTemplateList(page, pageSize) {
			if (page) {
				this.formInline.page = page;
				this.formInline.pageSize = pageSize;
			}
			this.$loading.show("加载中...");
			getTemplate(this.formInline).then((res) => {
				this.$loading.close();
				this.templateList = res.data.dataList || [];
				this.totalNum = res.data.totalNum || 0;
			});
		},
		deleteOpr(obj) {
			if(obj){
				this.templateList = _.orderBy(this.templateList,[obj.field],[obj.type]);
			}
			this.templateList.forEach((list, index) => {
				if (list.MBQYZT == "使用中") {
				$(".layui-table:eq(1) tr:eq(" + index + ")")
					.find("td")
					.last()
					.find("span[lay-event='enable']")
					.remove();
				} else if (list.MBQYZT == "未启用") {
				$(".layui-table:eq(1) tr:eq(" + index + ")")
					.find("td")
					.last()
					.find("span[lay-event='disable']")
					.remove();
				}
			});
		},
		addTemplate() {
			getMbid().then(res => {
				this.$router.push({path:"/admin/template/add", query: {mbid: res.data.mbid}});
			})
		},
		//禁用
		disableTemplate(data) {
			this.templateStatus.mbqyzt = data.MBQYZT == "使用中" ? "0" : "1";
			this.templateStatus.mbid = data.MBID;
			this.templateStatu = util.lxzh(this.templateStatus, data)
			layer.confirm("确认禁用模板?", (index) => {
				enableAndDisabled(this.templateStatus).then((res) => {
				layer.msg("模板已禁用");
				layer.close(index);
				this.getTemplateList();
				});
			});
		},
		//启用
		enableTemplate(data) {
			this.templateStatus.mbqyzt = data.MBQYZT == "使用中" ? "0" : "1";
			this.templateStatus.mbid = data.MBID;
			this.templateStatu = util.lxzh(this.templateStatus, data)
			layer.confirm("确认启用模板?", (index) => {
				enableAndDisabled(this.templateStatus).then((res) => {
				layer.msg("模板已启用");
				layer.close(index);
				this.getTemplateList();
				});
			});
		},
		downloadFile(data){
			if(!data.WJZXID){
				layer.msg("暂无材料")
			} else if(data.WJZXID){
				if (!location.origin) {
						location.origin = location.protocol + "//" + location.hostname + (location.port ? ':' + location.port: '');
				}
				location.href=location.origin + '/msurveyplat-serviceol/fileoperation/download?wjzxid=' +data.WJZXID
			}
		},
		// 删除模板
		deleteTemplate(data){
			if(data.MBQYZT == "使用中"){
				layer.msg("该模板当前使用中，无法删除")
			} else {
				layer.confirm("您确认删除该模板吗？", (index) => {
					layer.close(index)
					let params = {
						mbid: data.MBID
					}
					cancelTemplate(params).then(res => {
						this.getTemplateList();
					})
				})
			}
		}
	},
};
</script>
<style lang="less" scoped>
</style>