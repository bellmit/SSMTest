/**
 * Created by Administrator on 2019/8/12.
 * 当前js只存放3.0公共内容，例如统一每页条数的选择项
 */
//统一每页条数的选择项
var commonLimits = [10,20,50,100,200,500];

/**
 * 复选框背景设置颜色----暂时不用，备用
 * @param colorList [{name:'bdc-change-red',color: '#ff0000'},{name:'bdc-change-green',color: 'green'}]
 * name: class名称
 * color：颜色值
 */
function changeCheckboxBackground1(colorList) {
    colorList.forEach(function (v) {
        var $changeCheckbox = $('.'+ v.name);
        $changeCheckbox.each(function (i) {
            var getIndex = $($changeCheckbox[i]).parents('tr').index() + 1;
            $('.layui-table-fixed .layui-table-body tr:nth-child('+ getIndex +') .laytable-cell-checkbox').parent().css('background-color',v.color);
        });
    });
}
/**
 * tab下面的复选框背景设置颜色
 * @param colorList [{name:'bdc-change-red',color: '#ff0000'},{name:'bdc-change-green',color: 'green'}]
 * name: class名称
 * color：颜色值
 */
function changeTabCheckboxBackground(colorList) {
    colorList.forEach(function (v) {
        var $changeCheckbox = $('.layui-show .'+ v.name);
        $changeCheckbox.each(function (i) {
            var getIndex = $($changeCheckbox[i]).parents('tr').index() + 1;
            $('.layui-show .layui-table-fixed .layui-table-body tr:nth-child('+ getIndex +') .laytable-cell-checkbox').parent().css('background-color',v.color);
        });
    });
}
/**
 * 整行tr背景设置颜色
 * @param colorList [{name:'bdc-change-red',color: '#ff0000'},{name:'bdc-change-green',color: 'green'}]
 * @param tabTable 正常表格可不传值，tab下面的表格传值 true
 * name: class名称
 * color：颜色值
 */
function changeCheckboxBackground(colorList,tabTable) {
    colorList.forEach(function (v) {
        var $changeCheckbox;
        if(tabTable){
            $changeCheckbox = $('.layui-show .'+ v.name);
        }else {
            $changeCheckbox = $('.'+ v.name);
        }
        $changeCheckbox.each(function (i) {
            var getIndex = $($changeCheckbox[i]).parents('tr').index() + 1;
            $('.layui-table-main tr:nth-child('+ getIndex +') td>.layui-table-cell').css({'color': v.color?v.color:"#fff",'background-color':v.background}).find('span').css('color', v.color?v.color:"#fff");
            $('.layui-table-fixed .layui-table-body tr:nth-child('+ getIndex +')').css('background-color',v.background).find('td').css('color', v.color?v.color:"#fff");
        });
    });
}

/**
 * 整行tr背景设置颜色,最右侧操作列保持不变
 * @param colorList [{name:'bdc-change-red',color: '#000',background:'red'},{name:'bdc-change-green',color: 'green',background:'#fff'}]
 * @param tabTable 正常表格可不传值，tab下面的表格传值 true
 * name: class名称
 * color：字体颜色值，不传值默认白色
 * background：背景颜色值
 */
function changeTrBackgroundExceptRight(colorList,tabTable) {
    colorList.forEach(function (v) {
        var $changeCheckbox;
        if(tabTable){
            $changeCheckbox = $('.layui-show .'+ v.name);
        }else {
            $changeCheckbox = $('.'+ v.name);
        }
        $changeCheckbox.each(function (i) {
            var getIndex = $($changeCheckbox[i]).parents('tr').index() + 1;
            var getColor = v.color?v.color:"#fff";
            if(tabTable){
                $('.layui-show .layui-table-main tr:nth-child('+ getIndex +') td:not(.layui-table-col-special:last-child)>.layui-table-cell').addClass('bdc-special-bdcdyh-color').css({'color': getColor,'background-color':v.background}).find('span').css('color', getColor);
                $('.layui-show .layui-table-main tr:nth-child('+ getIndex +') .layui-table-col-special:last-child').css('background-color','transparent');
                $('.layui-show .layui-table-fixed-l .layui-table-body tr:nth-child('+ getIndex +')').css('background-color',v.background).find('td').css('color', getColor);
                $('.layui-show .layui-table-fixed-r .layui-table-body tr:nth-child('+ getIndex +') td:not(.layui-table-col-special:last-child)>.layui-table-cell').addClass('bdc-special-bdcdyh-color').css({'color': getColor,'background-color':v.background}).find('span').css('color', getColor);
            }else {
                $('.layui-table-main tr:nth-child('+ getIndex +') td:not(.layui-table-col-special:last-child)>.layui-table-cell').addClass('bdc-special-bdcdyh-color').css({'color': getColor,'background-color':v.background}).find('span').css('color', getColor);
                $('.layui-table-main tr:nth-child('+ getIndex +') .layui-table-col-special:last-child').css('background-color','transparent');
                $('.layui-table-fixed-l .layui-table-body tr:nth-child('+ getIndex +')').css('background-color',v.background).find('td').css('color', getColor);
                $('.layui-table-fixed-r .layui-table-body tr:nth-child('+ getIndex +') td:not(.layui-table-col-special:last-child)>.layui-table-cell').addClass('bdc-special-bdcdyh-color').css({'color': getColor,'background-color':v.background}).find('span').css('color', getColor);
            }
        });
    });
}
/**
 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
 * @description 替换中文 括号 为英文括号
 */
function replaceBracket(str){

    if(str==''||str==undefined){
        return str;
    }
    if(str.indexOf("（")!=-1){
        str=str.replace(new RegExp("（","gm"),"(");
    }
    if(str.indexOf("）")!=-1){
        str=str.replace(new RegExp("）","gm"),")");
    }
    return str;
}
/**
 * @author <a href ="mailto:songhaowen@gtmap.cn">songhaowen</a>
 * @description 替换 class 为parentheses 的中文括号为英文括号
 */
function replaceBracketArray() {
    var obj = {};
    $(".bracket").each(function (i) {
        var value = $(this).val();
        var name = $(this).attr('name');
        obj[name] = replaceBracket(value);
    });
    return obj;
}

/**
 * 格式化 bdcdyh
 * @param bdcdyh 不动产单元号
 * @return {string} 返回格式化的不动产单元号字符串
 */
function formatBdcdyh(bdcdyh) {
    var result;
    if (!isNullOrEmpty(bdcdyh) && bdcdyh.length == 28) {
        result = bdcdyh.substring(0, 6) + ' '
            + bdcdyh.substring(6, 12) + ' '
            + bdcdyh.substring(12, 19) + ' '
            + bdcdyh.substring(19, 28);
    } else {
        result = bdcdyh;
    }
    return result;
}

/**
 * 对传入的字符串进行去空格处理
 * @date 2019.03.14 18:43
 * @author
 * @param str 要处理的字符串
 * @param where 处理方式，all---所有空格；edge---两边；left——左边；right——右边
 * @return
 */
function deleteWhitespace(str, where) {
    if (!isNullOrEmpty(str)) {
        switch (where) {
            case "all":
                return str.replace(/\s*/g, "");
            case "edge":
                return str.replace(/^\s*|\s*$/g, "");
            case "left":
                return str.replace(/^\s*/, "");
            case "right":
                return str.replace(/(\s*$)/g, "");
            default :
                return str.replace(/\s*/g, "");
        }
    } else {
        return "";
    }
}

/**
 * 验证联系电话,包括手机号码，固话,为空时直接验证通过
 */
function validatePhone(lxdh) {
    var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;//电话号码
    var isMob = /^0?1[3|4|5|6|7|8|9][0-9]\d{8}$/;//手机号码
    if (!isNotBlank(lxdh) || isMob.test(lxdh) || isPhone.test(lxdh)) {
        return true;
    } else {
        return false;
    }
}

/**
 * 验证身份证号码格式
 * @param 证件号
 */
function verifyIdNumber(value) {
    var yzxx = {};
    var city = {
        11: "北京",
        12: "天津",
        13: "河北",
        14: "山西",
        15: "内蒙古",
        21: "辽宁",
        22: "吉林",
        23: "黑龙江 ",
        31: "上海",
        32: "江苏",
        33: "浙江",
        34: "安徽",
        35: "福建",
        36: "江西",
        37: "山东",
        41: "河南",
        42: "湖北 ",
        43: "湖南",
        44: "广东",
        45: "广西",
        46: "海南",
        50: "重庆",
        51: "四川",
        52: "贵州",
        53: "云南",
        54: "西藏 ",
        61: "陕西",
        62: "甘肃",
        63: "青海",
        64: "宁夏",
        65: "新疆",
        71: "台湾",
        81: "香港",
        82: "澳门",
        91: "国外 "
    };

    if (!value || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(value)) {
        yzxx.isSubmit = false;
        yzxx.verifyMsg = "身份证号格式错误！";
    } else if (!city[value.substr(0, 2)]) {
        yzxx.isSubmit = false;
        yzxx.verifyMsg = "地址编码错误！";
    } else {
        //18位身份证需要验证最后一位校验位
        if (value !== null && value !== "" && value.length === 18) {
            value = value.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            //校验位
            var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++) {
                ai = value[i];
                wi = factor[i];
                sum += ai * wi;
            }
            if (parity[sum % 11] != value[17].toUpperCase()) {
                yzxx.isSubmit = false;
                yzxx.verifyMsg = "校验位错误！";
            }
        }
    }
    return yzxx;
}