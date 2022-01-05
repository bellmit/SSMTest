<template>
	<div>
		<div class="form-title mlk-top">
			<div class="list-title">项目基本信息</div>
			<div>
				<Button type="primary" class="btn-h-34 bdc-major-btn" v-if="!isEdit&&xmzt=='1'" @click="save(false)">保存</Button>
				<Button type="primary" class="btn-h-34 bdc-major-btn" v-if="isEdit&&xmly=='2'" @click="edit">编辑</Button>
				<Button type="primary" class="btn-h-34 bdc-major-btn margin-left-10" v-if="xmzt=='1'" @click="clickHandler">备案</Button>
				<Button type="primary" class="btn-h-34 bdc-major-btn margin-left-10" v-if="isCg" @click="cgdownload">成果下载</Button>
				<Button type="primary" class="btn-h-34 bdc-major-btn margin-left-10" v-if="xmzt!='1'" @click="viewFj">附件材料</Button>
				<Button class="btn-h-34 btn-cancel margin-left-10" @click="cancel">返回</Button>
				<Button type="primary" class="btn-h-34 bdc-delete-btn margin-left-10" v-if="xmly == '2'&&xmzt=='1'" @click="deleteBa">删除</Button>
				<!-- <Button class="btn-h-34 btn-cancel margin-left-10" v-if="!isRecord" @click="editRecord">修改记录</Button> -->
				<Button class="btn-h-34 btn-cancel margin-left-10" v-if="xmzt!='1'&&!cgca" @click="printHzd()">打印回执单</Button>
			</div>
		</div>
		<projectInfo 
			:readonly="readonly" 
			:ssmkid="ssmkid" 
			:bdid="bdid" 
			:xmzt="xmzt"
			:checkedList="checkedList"
			:projectInfo="projectInfoData" 
			:glxqfbbhList="glxqfbbhList" 
			@select-gxxq="selectGlxqfbbh"
			@xqfbbh="selectedXqfbbh" 
			@change-clsx="clsxSelectChange" 
			@clsx="clsxSelect"
			ref="project-info"
		></projectInfo>
		<div class="line-dashed margin-bottom-20"></div>
		<projectCompony 
			ref="project-company" 
			:readonly="readonly" 
			:checkedClsxList="checkedList"
			:projectInfoData="projectInfoData"
			@selectChdw="selectChdw"
		></projectCompony>
		<div class="line-dashed margin-bottom-20"></div>
		<div class="form-title">
			<div class="list-title">{{ xmzt=='1' ? "测绘体量" : "测绘时限" }}</div>
		</div>
		<div v-if="xmzt=='1'" class="table-select">
			<Table
				ref="chtlTable"
				:showPage="false"
				:unShowTool="true"
				:cols="tableCols"
				:data="chtlData"
				@input="inputNumChange"
				@select="selectChange"
			></Table>
		</div>
		<div v-else>
			<Table
				:showPage="false"
				:unShowTool="true"
				:cols="chtlTableCols"
				:data="chtlData"
			></Table>
		</div>
		<!-- <div class="line-dashed margin-top-20 margin-bottom-20"></div> -->
		<!-- 暂时隐藏取件地址 -->
		<!-- <projectQj ref="project-qj" :readonly="readonly" :projectInfoData="projectInfoData"></projectQj>
			<div class="line-solid margin-bottom-10"></div> -->
		<div class="form-title">
			<div class="list-title">合同（协议书）材料</div>
		</div>
		<uploadHt 
			ref="upload-ht" 
			:readonly="readonly" 
			:ssmkid="htssmkid" 
			:clsxList="checkedList"
			:glsxid="chxmid" 
			:xmzt="xmzt"
			:projectInfoData="projectInfoData"
		></uploadHt>
		<div v-if="xmly!='2'" class="line-solid margin-top-20 margin-bottom-10"></div>
		<div v-if="xmly!='2'" class="form-title">
			<div class="list-title">核验材料</div>
		</div>
		<div v-if="xmly!='2'">
			<uploadFileInfo 
				ref="upload-file-hy-info"
				:ssmkid="'19'" 
				:deleteColumns="deleteHyColumns"
				:glsxid="chxmid"
			></uploadFileInfo>
		</div>
		<div class="line-solid margin-top-20 margin-bottom-10"></div>
		<div class="form-title">
			<div class="list-title">附件材料</div>
		</div>
		<uploadFile 
			ref="upload-file" 
			v-if="!readonly" 
			:ssmkid="ssmkid" 
			:glsxid="chxmid"
			:needclsx="true"
			:sjclUrl="sjclUrl"
			:clsxList="checkedList"
			:showDefaultTool="true"
		></uploadFile>
		<uploadFileInfo 
			ref="upload-file-info"
			v-else 
			:ssmkid="fjssmkid" 
			:needclsx="true"
			:clsxList="checkedList"
			:sjclUrl="sjclUrl"
			:xmzt="xmzt"
			:deleteColumns="deleteColumns"
			:glsxid="chxmid"
		></uploadFileInfo>
		<div id="toolbarDemo" v-show="false">
			<div class="table-form-edit form-edit">
				<select>
					<option value="平方米">平方米</option>
					<option value="米">米</option>
				</select>
			</div>
		</div>
		<Modal 
			class="modal-base form-record" 
			v-model="visible" 
			:title="'备案意见'"
			width="700"
			:mask-closable="false" 
			:footer-hide="true" 
			closable>
			<Form v-if="visible" class="form-edit" ref="record-form" @on-validate="validateChecked"
				:model="recordForm" :rules="fileRule" :label-width="114">
				<FormItem v-model="recordForm.sftg" prop="sftg" label="备案结果 ">
					<RadioGroup v-model="recordForm.sftg">
						<Radio label="1">通过</Radio>
						<Radio label="0">退回</Radio>
					</RadioGroup>
				</FormItem>
				<FormItem v-model="recordForm.hyyj" prop="hyyj" label="备案意见 ">
					<Input v-model="recordForm.hyyj" style="width: 500px" :rows="4" type="textarea" placeholder="" />
				</FormItem>
			</Form>
			<div class="save-btn">
				<Button type="primary" class="btn-h-34 bdc-major-btn" @click="handlerRecord()">确认</Button>
				<Button class="margin-left-10 btn-h-34 btn-cancel" @click="cancelRecord()">取消</Button>
			</div>
		</Modal>
  </div>
</template>
<script>
import projectInfo from "../../components/changzhou/project-info";
import projectCompony from "../../components/changzhou/project-company";
import projectQj from "../../components/manage/project-qj";
import uploadFile from "../../components/manage/upload-file";
import uploadFileInfo from "../../components/manage/upload-file-info";
import uploadHt from "../../components/manage/upload-ht-jd";
import moment from "moment";
import { reviewCommission, getuploadfilenums } from "../../service/review";
import {
	deleteDjdData,
	saveBadj,
	queryBadjXX,
	queryChxmInfo,
	queryInfoByGcbh
} from "../../service/manage";
import _ from "loadsh";
export default {
	components: {
		projectInfo,
		uploadFileInfo,
		projectCompony,
		projectQj,
		uploadFile,
		uploadHt
	},
	data() {
		const self = this
		return {
			projectInfoData: {
				chdw: [],
				chzsx: "",
				gcbh: "",
				fwxz: "0",
				gcdzs: "2",
				gcdzss: "02",
				gcmc: "",
				chjglxr: "",
				zdxm: "0"
			},
			selectTreeList: [],
			glxqfbbhList: [],
			cgca: false,
			chtlTableCols: [
				{
					field: "LAY_TABLE_INDEX",
					title: "序号",
					align: "center",
					width: "15%",
                    templet: function(d){
                        return d.LAY_TABLE_INDEX + 1
                    }
				},
				{
					field: "MC",
					title: "测绘阶段/测量事项",
					align: "center"
				},
				{
					field: "SL",
					title: "测绘体量",
					align: "center",
                    templet: function(d){
                        return d.SL + d.DW
                    }
				},
				{
					field: "JCRQ",
					title: "进场日期",
					align: "center"
				},
				{
					field: "CHQX",
					title: "测绘时限",
					align: "center"
				}
			],
			tableCols: [
				{
					field: "ROWNUM_",
					title: "序号",
					align: "center",
					width: "15%"
				},
				{
					field: "clsxmc",
					title: "测绘阶段/测量事项",
					align: "center",
					templet: function(d){
						return `<span class="required-star">*</span>${d.clsxmc}`
					}
				},
				{
					field: "sl",
					title: "数量",
					align: "center",
					templet: function(d){
						return self.readonly ? d.sl : '<div class="table-form-edit form-edit">'+
							'<input class="ivu-input" name="sl" value="'+d.sl+'" lay-event="input" lay-filter="input"/>'+
						'</div>'
					}
				},
				{
					field: "dw",
					title: "单位",
					align: "center",
					templet: function(d){
						return self.readonly? d.dw : d.clsxmc =="规划放样测量" ? '<div class="table-form-edit form-edit">'+
								'<select name="dw" lay-filter="select">'+
									'<option value="幢" ' +(d.dw==`幢` ? `selected` :'') +'>幢</option>'+
									'<option value="米" ' +(d.dw==`米` ? `selected` :'') +'>米</option>'+
								'</select>'+
							'</div>' : d.clsxmc =="竣工验收阶段" ? '<div class="table-form-edit form-edit">'+
								'<select name="dw" lay-filter="select">'+
									'<option value="平方米" ' +(d.dw==`平方米` ? `selected` :'') +'>平方米</option>'+
									'<option value="米" ' +(d.dw==`米` ? `selected` :'') +'>米</option>'+
								'</select>'+
							'</div>' : d.dw
					}
				}
			],
			chtlData: [],
			chtlClsx: [
				{
					DM: "1",
					UNIT: "平方米"
				},
				{
					DM: "2",
					UNIT: "平方米"
				},
				{
					DM: "3001",
					UNIT: "米"
				},{
					DM: "3002",
					UNIT: "平方米"
				},
				{
					DM: "4002",
					UNIT: "幢"
				},
				{
					DM: "4003",
					UNIT: "幢"
				},
				{
					DM: "5",
					UNIT: "米"
				}],
			chxmid: "",
			sjclUrl: "/fileoperation/querySjclByClsx",
			ssmkid: "23",
			fjssmkid: "23",
			htssmkid: '8',
			deleteHyColumns: [],
			deleteColumns: ["签约测绘单位","测绘事项"],
			isRecord: false, // 是否备案
			isCg: false,
			isEdit: false, //是否需要编辑
			bdid: "2",
			xmzt: '1',
			readonly: false,
			visible: false,
			error: "",
			chdwList: [],
			unLastSubmit: true,
			xqfbbh: "",
			dbrwid: "",
			mlkid: "",
			chdwMultiChoose: config.chdwMultiChoose,
			checkedList: [],
			baClsxList: [],
			selectGcXX: {},
			xmly: "2",
			cgccid:"",
			slbh: "",
			babh: "",
			recordForm: {
				sftg: "1",
				hyyj: "",
			},
			numReg: /^[0-9]{1,}$/,
			fileRule: {
				sftg: {
					required: true,
					message: "必填项不能为空",
				}
			},
		};
	},
	created() {
		this.chxmid = this.$route.query.chxmid || "";
		this.cgccid = this.$route.query.cgccid || "";
		this.slbh = this.$route.query.slbh || "";
		this.projectInfoData.slbh = this.$route.query.slbh || "";
		this.projectInfoData.slsj = this.$route.query.slsj || "";
		if (this.$route.query.type != "add") {
			this.readonly = true;
			this.queryBadjXX();
		}
		if(this.$route.query.wtfs){
			this.fjssmkid = this.$route.query.wtfs == "2" ? "23" : "18"
			this.htssmkid = this.$route.query.wtfs == "2" ? "8" : "22"
		}
		if(this.$route.query.type == "cg"){
			this.isCg = true;
			this.readonly = true;
		}
		this.dbrwid = this.$route.query.dbrwid || "";
		if(this.$route.query.from){
			this.cgca = this.$route.query.from.startsWith("/manage/evaluate/list") ? true : false;
		}
	},
	methods: {
		inputNumChange(data){
			let findIndex = this.chtlData.findIndex(chtl => chtl.ROWNUM_ === data.ROWNUM_);
			this.chtlData[findIndex].sl = data.sl;
		},
		selectChange(data){
			this.chtlData[data.index].dw = data.value;
		},
		// 获取查看或编辑的详情
		queryBadjXX() {
			this.$loading.show("加载中...");
			queryBadjXX({ chxmid: this.chxmid }).then((res) => {
				this.$loading.close();
				let baxxList = { ...res.data.baxx };
				let projectInfo = { ...this.projectInfoData };
				for (let key in baxxList) {
					projectInfo[_.toLower(key)] = baxxList[key];
				}
				projectInfo.slbh = this.projectInfoData.slbh;
				projectInfo.qjdz = projectInfo.qjdd;
				this.babh = projectInfo.babh || "";
				// 根据项目状态显示操作按钮
				this.xmzt = projectInfo.xmzt;
				if(this.xmzt != '1'){
					this.deleteColumns.push("operation")
					this.deleteHyColumns.push("operation")
				}
				this.$nextTick(() => {
					this.xmly = projectInfo.xmly;
				})
				if(projectInfo.xmzt == "1"){
					this.isEdit = true;
					if(this.$route.query.type != 'add' && this.$route.query.type != 'cg'){
						this.isRecord = true;
					}
				} 
				projectInfo.slsj = projectInfo.slsj ? moment(projectInfo.slsj).format("YYYY-MM-DD HH:mm:ss"): "";
				projectInfo.yyqjsj = projectInfo.yyqjsj ? moment(projectInfo.yyqjsj).format("YYYY-MM-DD HH:mm:ss"): "";
				let chdwList = res.data.chdwList.map((chdw) => {
					return chdw.MLKID;
				});
				projectInfo.chdw = [...chdwList];
				this.xqfbbh = projectInfo.xqfbbh;
				this.queryInfoByGcbh(projectInfo.gcbh);
				this.projectInfoData = { ...projectInfo };
				let clsxList = res.data.clsxList ? res.data.clsxList.map((clsx) => clsx.CLSX) : [];
				this.checkedList = clsxList;
				res.data.chtlList&&res.data.chtlList.forEach(chtl => {
					chtl.clsxmc = chtl.MC
					for (let key in chtl) {
						chtl[_.toLower(key)] = chtl[key];
						
					}
				})
				this.chtlData = res.data.chtlList || [];
				this.clsxSelect(false,this.chtlData);
				// 级联选择省市区
				this.$refs["project-info"].selectGcdzs({
					value: this.projectInfoData.gcdzs,
				});
				this.$refs["project-info"].selectGcdzss({
					value: this.projectInfoData.gcdzss,
				});
				if(!this.chdwMultiChoose){
					this.projectInfoData.chdwId = this.projectInfoData.chdw[0]
					this.$refs["project-company"].changeSingleChdw(this.projectInfoData.chdwId)
				}
				// this.$refs["project-qj"].selectGcdzs({value: this.projectInfoData.sjdds})
				// this.$refs["project-qj"].selectGcdzss({value: this.projectInfoData.sjddss})
			});
		},
		// 附件材料
		viewFj(data){
			if(this.projectInfoData.wjsftb != "1"){
				layer.msg("文件正在同步")
				return;
			}
			const {href} = this.$router.resolve({
				path: '/review/cl/fj',
				query: {
					chxmid: this.chxmid,
					type: "xm"
				}
			})
			window.open(href);
		},
		// 查询关联的需求发布编号
		queryInfoByGcbh(value) {
			queryInfoByGcbh({ gcbh: value }).then((res) => {
				this.glxqfbbhList = res.data.chxmxx ? [...res.data.chxmxx] : [];
				this.baClsxList = [...res.data.baClsxList];
				this.clsxSelectChange(this.selectTreeList);
			});
		},
		// 点击审核
		clickHandler() {
			this.projectInfoData.sjsj = this.projectInfoData.sjsj ? moment(this.projectInfoData.sjsj).format("YYYY-MM-DD") : "";
			this.projectInfoData.yyqjsj = this.projectInfoData.yyqjsj ? moment(this.projectInfoData.yyqjsj).format("YYYY-MM-DD") : "";
			let valid1 = this.$refs["project-info"].validate();
			let valid2 = this.$refs["project-company"].validate();
			// let valid3 = this.$refs["project-qj"].validate();
			let clsx = this.$refs["project-info"].getSelectedClsx();
			this.projectInfoData.clsx = clsx;
			if (!clsx || clsx.length == 0) {
				this.$error.show("测绘事项不能为空");
				return;
			}
			if(!this.projectInfoData.gcdzqx || !this.projectInfoData.gcdzxx){
				this.$error.show("请完善工程地点信息");
				return;
			}
			 let message = this.validateChtlData()
			if(message){
				this.$error.show(message);
				return
			}
			this.projectInfoData.chtl = this.chtlData;
			if(!this.readonly){
				let validateHt = this.$refs["upload-ht"].validate();
				if(validateHt){
					return
				}
				let validate = this.$refs["upload-file"].validate();
				if(validate){
					return
				}
			} else {
				let validateHt = this.$refs["upload-ht"].validate();
				if(validateHt){
					return
				}
				let validate = this.$refs["upload-file-info"].validate();
				if(validate){
					return
				}
			}
			if(valid1 && valid2){
				this.visible = true;
			}
		},
		// 保存或修改合同备案登记
		save(needRecord) {
			let clsx = this.$refs["project-info"].getSelectedClsx();
			this.projectInfoData.clsx = clsx;
			let uploadList = [];
			let htList = [];
			if(!this.readonly){
				htList = [...this.$refs["upload-ht"].queryUploadList()];
				uploadList = [...this.$refs["upload-file"].queryUploadList()];
			} else {
				uploadList = [...this.$refs["upload-file-info"].queryUploadList()];
				htList = [...this.$refs["upload-ht"].queryUploadList()];
			}
			htList.forEach((list) => {
				list.CHDWID = list.CHDWID ? list.CHDWID.join(",") : "";
				for (let key in list) {
					list[_.toLower(key)] = list[key];
				}
			});
			uploadList.forEach((list) => {
				for (let key in list) {
					list[_.toLower(key)] = list[key];
					delete list[key];
				}
			});
			this.projectInfoData.chxmid = this.chxmid;
			this.projectInfoData.slbh = this.$route.query.slbh || this.projectInfoData.slbh;
			if (!this.projectInfoData.xqfbbh) {
				this.projectInfoData.xqfbbh = this.$route.query.xqfbbh;
			}
			if (this.xqfbbh) {
				this.projectInfoData.xqfbbh = this.xqfbbh;
			}
			this.projectInfoData.clxx = uploadList;
			this.projectInfoData.htxx = htList;
			this.projectInfoData.chtl = this.chtlData;
			this.$loading.show("保存中...");
			saveBadj(this.projectInfoData).then((res) => {
				this.$loading.close();
				// this.$router.push("/changzhou/htbadj");
				this.readonly = true;
				this.isRecord = true;
				this.isEdit = true;
				delete this.tableCols[2].templet
				delete this.tableCols[3].templet
				layer.msg("保存成功");
				this.$nextTick(() => {
					this.$refs["chtlTable"].renderTable();
					this.$refs["project-info"].renderTreeList();
				})
				if(needRecord){
					this.record();
				}
			});
		},
		// 点击编辑
		edit(){
			this.readonly = false;
			this.isEdit = false;
			this.tableCols[2].templet = function(d){
				return '<div class="table-form-edit form-edit">'+
					'<input class="ivu-input" name="sl" value="'+d.sl+'" lay-event="input" lay-filter="input"/>'+
				'</div>';
			}
			this.tableCols[3].templet = function(d){
				return d.clsxmc =="规划放样测量" ? '<div class="table-form-edit form-edit">'+
					'<select name="dw" lay-filter="select">'+
						'<option value="幢" ' +(d.dw==`幢` ? `selected` :'') +'>幢</option>'+
						'<option value="米" ' +(d.dw==`米` ? `selected` :'') +'>米</option>'+
					'</select>'+
				'</div>' : d.clsxmc =="竣工验收阶段" ? '<div class="table-form-edit form-edit">'+
					'<select name="dw" lay-filter="select">'+
						'<option value="平方米" ' +(d.dw==`平方米` ? `selected` :'') +'>平方米</option>'+
						'<option value="米" ' +(d.dw==`米` ? `selected` :'') +'>米</option>'+
					'</select>'+
				'</div>' : d.dw
			}
			this.$nextTick(() => {
				this.$refs["chtlTable"].renderTable();
				this.$refs["project-info"].renderTreeList();
			})
		},
		// 选中测绘单位
		selectChdw(chdw) {
			this.projectInfoData.chdwxx = chdw;
			this.chdwList = chdw;
		},
		// 打印回执单
		printHzd() {
			if (!location.origin) {
				location.origin = location.protocol + "//" + location.hostname + (location.port ? ":" + location.port : "");
			}
			location.href = location.origin + "/msurveyplat-promanage/contractfile/exportPdf?chxmid=" + this.chxmid;
		},
		// 选择关联需求发布编号
		selectGlxqfbbh(list) {
			this.glxqfbbhList = [...list.chxmList];
			this.baClsxList = [...list.baClsxList];
			this.clsxSelectChange(this.selectTreeList);
			this.selectGcXX = { ...list.chgcxx };
			let projectInfo = { ...this.projectInfoData };
			projectInfo.wtdw = this.selectGcXX.WTDW || projectInfo.wtdw;
			projectInfo.lxr = this.selectGcXX.LXR || projectInfo.lxr;
			projectInfo.lxdh = this.selectGcXX.LXDH || projectInfo.lxdh;
			projectInfo.gcdzs = this.selectGcXX.GCDZS || projectInfo.gcdzs;
			projectInfo.gcdzss = this.selectGcXX.GCDZSS || projectInfo.gcdzss;
			projectInfo.gcdzxx = this.selectGcXX.GCDZXX || projectInfo.gcdzxx;
			projectInfo.gcdzqx = this.selectGcXX.GCDZQX || projectInfo.gcdzqx;
			projectInfo.zdbh = this.selectGcXX.ZDBH || projectInfo.zdbh;
			projectInfo.gcmc = this.selectGcXX.GCMC || projectInfo.gcmc;
			projectInfo.xmxz = this.selectGcXX.XMXZ || projectInfo.xmxz;
			projectInfo.zdxm = this.selectGcXX.ZDXM || projectInfo.zdxm;
			this.projectInfoData = { ...projectInfo };
			this.$refs["project-info"].selectGcdzs({ value: projectInfo.gcdzs });
			this.$refs["project-info"].selectGcdzss({ value: projectInfo.gcdzss });
		},
		// 选中的需求发布编号
		selectedXqfbbh(select) {
			if (!select) {
				return;
			}
			let find = this.glxqfbbhList.find((list) => list.XQFBBH == select);
			queryChxmInfo({ xqfbbh: select, chxmid: find.CHXMID }).then((res) => {
				let data = res.data;
				let projectInfo = { ...this.projectInfoData };
				projectInfo.qjdz = data.QJDD || projectInfo.qjdz;
				projectInfo.qjfs = data.QJFS || projectInfo.qjfs;
				projectInfo.chzsx = data.CHZSX || projectInfo.chzsx;
				projectInfo.sjrlxdh = data.SJRLXDH || projectInfo.sjrlxdh;
				projectInfo.chjglxr = data.CHJGLXR || projectInfo.chjglxr;
				projectInfo.chjglxdh = data.CHJGLXDH || projectInfo.chjglxdh;
				projectInfo.chdwId = data.MLKID || projectInfo.chdwId;
				projectInfo.chdw = data.MLKID || projectInfo.chdw;
				this.xqfbbh = select;
				this.checkedList = typeof data.CLSX == "string" ? data.CLSX.split(";") : data.CLSX ? data.CLSX : [];
				if(this.checkedList){
					this.clsxSelect();
				}
				// projectInfo.slbh = data.SLBH ? data.SLBH : projectInfo.slbh;
				projectInfo.yyqjsj = data.YYQJSJ ? moment(data.YYQJSJ).format("YYYY-MM-DD") : projectInfo.yyqjsj;
				this.projectInfoData = { ...projectInfo };
			});
		},
		clsxSelect(reset=true,chtl){
			if(reset){
				this.projectInfoData = {
					...this.projectInfoData,
					chdwId: "",
					chdw: []
				};
			}
			this.$nextTick(() => {
				this.$refs["project-company"].queryChdwList();
				this.$refs["upload-file"]&&this.$refs["upload-file"].getUploadList();
				this.$refs["upload-file-info"]&&this.$refs["upload-file-info"].getUploadList();
				this.$refs["upload-ht"].getUploadList();
				if(!chtl || !chtl.length){
					this.initChtlData();
				}
			})
		},
		validateChtlData(){
			let message = "";
			this.chtlData.forEach(chtl => {
				if(!chtl.sl){
					message = "测绘时限数量不能为空";
				} else if(!this.numReg.test(chtl.sl)){
                    message = "测绘时限数量只能为数字"
                }
			})
			return message
		},
		// 生成测绘体量
		initChtlData(){
			let clsxList = this.$refs["project-info"].getDictionaryData();
			let chtlData = [];
			this.checkedList.forEach(clsx => {
				let fdm = clsx.split("")[0]
				let findclsx = this.chtlClsx.find(c => c.DM == clsx)
				let findfclsx = this.chtlClsx.find(c => c.DM == fdm)
				if(!chtlData.find(chtl => chtl.clsx == clsx)&&findclsx){
					let find = clsxList.find(c => c.DM == clsx);
					chtlData.push({
						clsx: clsx,
						jd: fdm,
						clsxmc: find.MC,
						ROWNUM_: chtlData.length + 1,
						sl: "",
						dw: findclsx.UNIT
					})
					this.chtlData = _.cloneDeep(chtlData)
				} else if(!chtlData.find(chtl => chtl.clsx == fdm)&&findfclsx){
					let find = clsxList.find(c => c.DM == fdm);
					chtlData.push({
						clsx: fdm,
						jd: fdm,
						clsxmc: find.MC,
						ROWNUM_: chtlData.length + 1,
						sl: "",
						dw: findfclsx.UNIT
					})
					this.chtlData = _.cloneDeep(chtlData)
				}
			})
		},
		// clsx选择修改后，更新关联的clsx数据
		clsxSelectChange(treeList) {
			this.selectTreeList = [];
			let checkedList = [];
			let treeL = _.cloneDeep(treeList);
			treeL.forEach((tree) => {
				delete tree[0].render;
				tree[0].checked = false;
				let finds = this.baClsxList.find((clsx) => {
					return clsx.DM.startsWith(tree[0].id);
				});
				if (this.readonly || finds) {
					tree[0].disabled = true;
				}
				tree[0].children.forEach((t) => {
					checkedList.push(t.id)
					t.disabled = false;
					t.checked = false;
					delete t.render;
					let find = this.baClsxList.find((clsx) => clsx.DM === t.id);
					if (this.readonly || find) {
						t.disabled = true;
					}
				});
			});
			this.selectTreeList = _.cloneDeep(treeL);
			if(checkedList.length){
				this.checkedList = [...checkedList];
			}
		},
		// 编辑记录
		editRecord() {
			this.$router.push({
				path: "/edit/record",
				query: { chxmid: this.chxmid, from: this.$route.fullPath },
			});
		},
		// 删除备案
		deleteBa(){
			let params = {
				chxmid: this.chxmid,
			};
			layer.confirm("确认删除?",(index) => {
				layer.close(index)
				this.$loading.show("加载中...");
				deleteDjdData(params).then((res) => {
					this.$loading.close();
					this.$router.push("/changzhou/htbadj");
				});
			})
		},
		// 返回操作
		cancel() {
			if(this.$route.query.type === "add" && !this.isRecord){
				let params = {
					chxmid: this.chxmid,
				};
				this.$loading.show("加载中...");
				deleteDjdData(params).then((res) => {
					this.$loading.close();
					this.$router.push("/changzhou/htbadj");
				});
				return;
			} 
			if (this.$route.query.from) {
				this.$router.push(this.$route.query.from);
			} else {
				this.$router.push("/changzhou/htbadj");
			}
		},
		// 弹出表单校验的失败信息
		validateChecked(prop, status, error) {
			if (error && this.error != error && !this.unLastSubmit) {
				this.error = error;
				this.$error.show(error);
				setTimeout(() => {
					this.error = "";
					this.$error.close();
				}, 1000);
			}
		},
		handlerRecord(){
			if(this.xmly == "2"){
				this.save(true);
			} else {
				this.record()
			}
		},
		// 审核
		record() {
			this.unLastSubmit = false;
			this.$refs["record-form"].validate((valid) => {
				this.unLastSubmit = true;
				if (valid) {
					let params = {
						sftg: this.recordForm.sftg,
						hyyj: this.recordForm.hyyj,
						chxmid: this.chxmid,
						wtdw: this.projectInfoData.wtdw,
						gcmc: this.projectInfoData.gcmc,
						chdwxx: this.projectInfoData.chdwxx,
						clsx: this.selectTreeList
					};
					this.$loading.show("加载中...");
					reviewCommission(params).then((res) => {
						this.$loading.close();
						layer.msg("备案成功")
						this.$router.push("/changzhou/htbadj");
					});
				}
			});
		},
		// 取消审核
		cancelRecord() {
			this.visible = false;
		},
		//成果下载
		cgdownload() {
			getuploadfilenums({wjzxid: "", babh: this.babh}).then(res => {
				location.href = config.msurveyplatContext + '/jcsjsq/achievementdownloadbybabh?babh=' + this.babh + "&cgccid=" + this.cgccid;
			})
		}
	},
};
</script>
<style scoped>
.mlk-top {
  display: flex;
  justify-content: space-between;
}
.form-record .form-edit {
  padding: 0 10px !important;
}
</style>
