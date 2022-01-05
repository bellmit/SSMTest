import { getYzList } from './publish'
const util = {
    getCookie: (objName) => {//获取指定名称的cookie的值 
        let arrStr = document.cookie.split("; ");
        for (let i = 0; i < arrStr.length; i++) {
            let temp = arrStr[i].split("=");
            if (temp[0] == objName)
                return unescape(temp[1]);
        }
    },
    createUUID() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 32; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        var uuid = s.join("");
        return uuid;
    },
    getBtxYzRuleInline(rule,yzxList){
        for(let key in rule){
            if(yzxList.includes(key)){
                rule[key].required = true;
                rule[key].trigger = "blur";
            }
        }
        return rule
    },
    lxzh(templateStatus, data){
        if(data.MBLX == "合同模板"){
            templateStatus.mblx = "0"
        }else if(data.MBLX == "申请表模板"){
            templateStatus.mblx = "1"
        }else if(data.MBLX == "其他"){
            templateStatus.mblx = "2"
        }
        return templateStatus
    },
    convertZjhZl(_param) {
        if (!_param) return ''
        else if (_param == '身份证') return '1'
        else if (_param == '港澳台身份证') return '2'
        else if (_param == '护照') return '3'
        else if (_param == '户口簿') return '4'
        else if (_param == '军官证（士兵证）') return '5'
        else if (_param == '组织机构代码') return '6'
        else if (_param == '营业执照') return '7'
        else if (_param == '统一社会信用代码') return '8'
        else return ''
    },
    convertZlZjh(_param) {
        if (!_param) return ''
        else if (_param == '1') return '身份证'
        else if (_param == '2') return '港澳台身份证'
        else if (_param == '3') return '护照'
        else if (_param == '4') return '户口簿'
        else if (_param == '5') return '军官证（士兵证）'
        else if (_param == '6') return '组织机构代码'
        else if (_param == '7') return '营业执照'
        else if (_param == '8') return '统一社会信用代码'
        else return ''
    },
}
export default util;
