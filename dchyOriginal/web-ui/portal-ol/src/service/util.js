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
    getSearchParams(key){
        var search = location.search?location.search.split("?"):location.hash.split("?");
        var searchStr = search.length > 1 ? search[1] : "";
        var value = ""
        if(searchStr){
            var searchs = searchStr.split("&")
            searchs.forEach(function(str){
                var strs = str.split("=")
                if(strs[0] === key) {
                    value = strs[1]
                }
            })
        }
        return value
    }
}
export default util;
