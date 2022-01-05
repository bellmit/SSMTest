<template>
    <div class="company-info">
        <van-form validate-first ref="company-form" :show-error-message="false" label-align="right" label-width="8em">
            <van-field
                readonly
                name="wtbh"
                v-model="projectInfoData.wtbh"
                label="委托编号"
                placeholder="请输入委托编号"
                :rules="[{ required: true, message: '委托编号不能为空' }]"
            />
            <van-field
                readonly
                clickable
                v-model="projectInfoData.chdwmc"
                name="chdw"
                class="requireStar"
                label="测绘单位"
                placeholder="请选择测绘单位"
                 @click="showChdwPicker = true"
                :rules="[{ required: true, message: '测绘单位不能为空' }]"
            />
            <van-popup v-model="showChdwPicker" position="bottom">
                <van-picker
                    value-key="CHDWMC"
                    show-toolbar
                    :columns="chdwList"
                    @confirm="onConfirm"
                    @cancel="showChdwPicker = false"
                />
            </van-popup>
            <van-field
                readonly
                v-model="projectInfoData.wtdw"
                name="wtdw"
                label="建设单位"
                placeholder="请输入建设单位"
                :rules="[{ required: true, message: '建设单位不能为空' }]"
            />
            <van-field
                v-model="projectInfoData.lxr"
                name="lxr"
                class="requireStar"
                label="联系人"
            />
            <van-field
                v-model="projectInfoData.lxdh"
                name="lxdh"
                class="requireStar"
                label="联系电话"
            />
        </van-form>
    </div>
</template>
<script>
import { queryChdwList } from "../../service/commission"
import _ from "loadsh"
export default {
    props: {
        projectInfoData: {
            type: Object,
            default: () => {
                return {}
            }
        },
        clsxList: {
            type: Array,
            default: () => {
                return []
            }
        }
    },
    data() {
        return {
            gcdzOptions: [],
            fieldValue: "",
            chdwList: [],
            showChdwPicker: false
        }
    },
    created() {
        this.queryChdwList()  
    },
    methods: {
        validate(){
            return this.$refs["company-form"].validate()
        },
        queryChdwList(){
            let params = {
                clsxList: this.clsxList
            }
            queryChdwList(params).then(res => {
                this.chdwList = res.data.dataList || [];
            })
        },
        onConfirm(select) {
            this.projectInfoData.chdw = select.CHDWID
            this.projectInfoData.chdwmc = select.CHDWMC
            this.projectInfoData.chdwxx = [
                {
                    chdwmc: select.CHDWMC,
                    chdwid: select.CHDWID
                }
            ]
            this.showChdwPicker = false;
        },
    },
}
</script>
<style scoped>
    .dw {
        position: absolute;
        top: 0;
        right: 300px;
        color: #333;
    }
    .company-info >>> .van-cell__value {
        padding-left: 20px;
    }
</style>