import util from './util'
import { getYzList } from "./manage"
let yz_mixins_common = {
    data () {
        return {
            btxyzList: [],
            ywljyzList: [],
            ruleInline: {}
        }
    },
    props: {
        ssmkid: {
            type: String,
            default: ""
        },
        bdid: {
            type: String,
            default: ""
        }
    },
    created() {
        if(this.ssmkid&&this.bdid){
            getYzList({ssmkid: this.ssmkid,bdid: this.bdid}).then(res => {
                if(res.data){
                    this.btxyzList = res.data.btxyzList
                    this.ywljyzList = res.data.ywljyzList
                    this.ruleInline = util.getBtxYzRuleInline(this.ruleInline,this.btxyzList)
                }
            })
        }
    },
    methods: {
        // 弹出表单校验的失败信息
        validateChecked(prop, status, error){
            if(error){
                this.$error.show(error)
            }
        }
    },
}

export default yz_mixins_common