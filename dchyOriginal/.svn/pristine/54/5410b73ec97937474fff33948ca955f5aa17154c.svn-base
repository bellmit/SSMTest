/**
 * for archive of changzhou
 * @Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * @Date:  2016/7/28 8:40
 * @Version: v1.0 (c) Copyright gtmap Corp.2016
 */

/***
 * 征地档案 经开区
 * @param bzdah
 * @param id
 * @param qzh
 */
function openZdArchive(bzdah, id, qzh, dkid) {
    bzdah = decodeURI(bzdah);
    id = decodeURI(id);
    qzh = decodeURI(qzh);
    dkid = decodeURI(dkid);

    if (bzdah != "" && bzdah != "空" && bzdah != "Null") {
        window.open("http://10.4.1.32/docpdf/%E5%BE%81%E5%9C%B0/" + bzdah + "/" + bzdah + "_1.pdf");
        return;
    }
    else if (id != "" && id != "空" && id != "Null") {
        var url = "http://10.4.1.35:8888/CZSJWebSite/engine/Form.html?projectId=";
        var obj = id.substring(4, id.length);
        window.open(url + obj);
        return;
    } else if (qzh != "" && qzh != "空" && qzh != "Null") {
        var outArr = new Array();
        var arr = new Array();
        arr = qzh.split(",");
        for (var i = 0; i < arr.length; i++) {
            var url = "http://10.4.30.48:8080/docpdf/" + arr[i] + ".pdf"
            if (isEffective(url)) {
                window.open(url);
                return;
            } else {
                outArr.push(arr[i]);
            }
        }
        for (var i = 0; i < outArr.length; i++) {
            alert(outArr[i] + ".pdf 文件不存在，查询不到相关档案信息！");
        }
    } else if (dkid != "" && dkid != "空" && dkid != "Null") {
        var url = "http://10.4.1.15:8080/portal/taskHandle?taskid=";
        window.open(url + dkid);
    } else {
        alert("查询不到档案信息！");
    }
}

function openUrl(url) {
    window.open(url);
}

/**
 * 征地档案 市局
 * @param bzdah
 * @param id
 * @param dkid
 */
function openZdArchive(bzdah, id, dkid) {
    bzdah = decodeURI(bzdah);
    id = decodeURI(id);
    dkid = decodeURI(dkid);
    if (bzdah != "" && bzdah != "空" && bzdah != "Null") {
       getUrl("http://10.4.1.32/docpdf/%E5%BE%81%E5%9C%B0/" + bzdah + "/" + bzdah + "_1.pdf");
        return;
    }
    else if (id != "" && id != "空" && id != "Null") {
        var url = "http://10.4.1.35:8888/CZSJWebSite/engine/Form.html?projectId=";
        var obj = id.substring(4, id.length);
       getUrl(url + obj);
        return;
    } else if (dkid != "" && dkid != "空" && dkid != "Null") {
        var url = "http://10.4.1.15:8080/cas/custom?user_card=123&url=http://10.4.1.15:8080/portal/projectHandle?wiid=";
       getUrl(url + dkid);
    } else {
        alert("查询不到档案信息！");
    }
}


/**
 * 征地档案 新北
 * @param bzdah
 * @param id
 * @param dkid
 */
function openXbZdArchive(bzdah) {
    bzdah = decodeURI(bzdah);
    if (bzdah != "" && bzdah != "空" && bzdah != "Null") {
        var path =encodeURI("http://10.4.40.245/docpdf/征地/");
        window.open(path + bzdah + "/" + bzdah + "_1.pdf");
    } else {
        alert("查询不到档案信息！");
    }
}
/**
 * 供地档案  经开区
 * @param bzdah
 * @param id
 * @param dah
 */
function openGdArchive(bzdah, id, dah, dkid) {
    bzdah = decodeURI(bzdah);
    id = decodeURI(id);
    dah = decodeURI(dah);
    dkid = decodeURI(dkid);
    if (bzdah != "" && bzdah != "空" & bzdah != "Null") {
        window.open("http://10.4.1.32/docpdf/%E4%BE%9B%E5%9C%B0/" + bzdah + "/" + bzdah + "_1.pdf");
        return;
    } else if (id != "" && id != "空" && id != "Null") {
        var url = "http://10.4.1.35:8888/CZSJWebSite/engine/Form.html?projectId=";
        var obj = id.substring(2, id.length);
        window.open(url + obj);
        return;
    } else if (dah != "" && dah != "空" & dah != "Null") {
        var arr = new Array();
        var outArr = new Array();
        arr = dah.split(",");
        for (var i = 0; i < arr.length; i++) {
            var url = "http://10.4.30.48:8080/docpdf/" + dah + ".pdf"
            if (isEffective(url)) {
                window.open(url);
                return;
            }
        }
    } else if (dkid != "" && dkid != "空" && dkid != "Null") {
        var url = "http://10.4.1.15:8080/portal/taskHandle?taskid=";
        window.open(url + dkid);
    } else {
        outArr.push(arr[i]);
    }
    for (var i = 0; i < outArr.length; i++) {
        alert(outArr[i] + ".pdf 文件不存在，查询不到相关档案信息！");
    }
}
/**
 * 供地档案 市局
 * @param bzdah
 * @param id
 */
function openGdArchive(bzdah, id, dkid) {
    bzdah = decodeURI(bzdah);
    id = decodeURI(id);
    dkid = decodeURI(dkid);

    if (bzdah != "" && bzdah != "空" && bzdah != "Null") {
        window.open("http://10.4.1.32/docpdf/%E4%BE%9B%E5%9C%B0/" + bzdah + "/" + bzdah + "_1.pdf");
        return;
    } else if (id != "" && id != "空" & id != "Null") {
        var url = "http://10.4.1.35:8888/CZSJWebSite/engine/Form.html?projectId=";
        var obj = id.substring(2, id.length);
        window.open(url + obj);
        return;
    } else if (dkid != "" && dkid != "空" && dkid != "Null") {
        var url = "http://10.4.1.15:8080/cas/custom?user_card=123&url=http://10.4.1.15:8080/portal/projectHandle?wiid=";
        window.open(url + dkid);
    } else {
        outArr.push(arr[i]);
    }

    for (var i = 0; i < outArr.length; i++) {
        alert(outArr[i] + ".pdf 文件不存在，查询不到相关档案信息！");
    }
}

/**
 * 违法用地档案 常州市局
 * @param bzdah
 * @param id
 * @param dkid
 */
function openWFYDArchive(bzdah) {
    bzdah = decodeURI(bzdah);
    if (bzdah != "" && bzdah != "空" && bzdah != "Null") {
        for (var i = 1; i < 10; i++) {
            var url = "http://10.4.1.32/D/%E6%A1%A3%E6%A1%88PDF/%E8%BF%9D%E6%B3%95%E7%94%A8%E5%9C%B0/" + bzdah + "/" + bzdah + "_" + i + ".pdf";
            isEffectiveCORS(url);
        }
        return;
    } else {
        alert("查询不到相关档案信息!");
    }
}

/**
 * 判断服务器资源是否存在
 * @param url
 * @returns {boolean}
 */
function isEffective(url) {
    try {
        var xmlhttp;
        if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        } else { // code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.open("get", url, false);
        xmlhttp.send();
        if (xmlhttp.status == "404") {
            return false;
        } else {
            return true;
        }
    } catch (e) {
        return false;
    }
}

/**
 * 判断服务器资源是否存在,并打开  支持跨域
 * @param urls
 */
function isEffectiveCORS(url) {
    try {
        $.ajax({
            url: url,
            dataType: "jsonp",
            complete: function (xhr, statusText) {
                console.log(url);
                window.open(url);
            }
        });
    } catch (e) {
        console.log(e);
    }
}

/**
 * 市局通过oa免登陆
 * @param targetUrl
 */
function getUrl(targetUrl) {
    var url = 'http://10.4.1.15:8080/cas/custom?user_card=';
    var token = GetQueryString("Token");
    var idFetchUrl ='http://10.4.1.34/Sys/Flex/XmlService.ashx';
    var proxyUrl='http://10.4.1.15:8080/omp/map/proxy?requestUrl='+idFetchUrl;
	$.ajax({
        url: proxyUrl,
		async:'false',
        type:'post',
		data:{"SessionID":token,"events":"GetOperatorInfo","ResultType":"Json"},
		dataType:'json',
        success: function (r) {
            var ob = JSON.parse(r);
            var userId = ob.Status[0].PersonID;
            var result = url.concat(userId).concat("&url=").concat(targetUrl);
			// if(!userId){
				window.open(targetUrl);
			// }else{
			// 	window.open(result);
			// }
             
        }

    });
}


function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


