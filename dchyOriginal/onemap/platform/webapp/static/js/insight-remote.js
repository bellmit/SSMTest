//远程调用omp查看视频控件

/**
* 查看（设置）摄像头控件
* insightUrl 
* type (project或camera或preset)（项目中查看摄像头传project,摄像头列表中传camera，预设位中传preset）
* projectId 项目id
* deviceId 摄像头id
* presetNo 预设位编号
* previewType preview或preset （查看或设置预设位）
**/
function cameraPreviewWin(insightUrl,type,projectId,deviceId,presetNo,previewType) {
	 $.ajax({
        url: insightUrl + '/video/getPreviewToken',
        type: 'POST',
        data: {
        	type: type,
        	projectId: projectId,
            deviceId: deviceId,
            presetNo: presetNo,
            previewType: previewType
        },
        dataType: 'json',
        success: function (responseObj) {
        	if(!responseObj.result){
            	alert(responseObj.msg);
            	return;
        	}
	        var url = 'CMRV://|'+insightUrl+'/video/preview?token='+responseObj.msg+'|';
	        window.location = url;
        }
    });
}

//查看摄像头统一入口
function openPreviewWin(insightUrl,type,projectId,deviceId,presetNo,previewType){
	$.ajax({
	     url: insightUrl + '/video/getPreviewInfo',
	     type: 'POST',
	     data: {
	    	 type: type,
	         projectId: projectId,
	         deviceId: deviceId,
	         presetNo: presetNo
	     },
	     dataType: 'json',
	     success: function (responseObj) {
	         if(!responseObj.success){
	             alert(responseObj.msg);
	             return;
	         }else{
	        	 getPreviewVersion(insightUrl,type,projectId,deviceId,presetNo,previewType,responseObj.result);
	         }
	     }
	});
}

//选择使用摄像头预览版本
function getPreviewVersion(insightUrl,type,projectId,deviceId,presetNo,previewType,result){
	var cameraType = result.cameraType;
	 var version = result.versionId;
	if('OLD' == version){
		cameraPreviewWin(insightUrl,type,projectId,deviceId,presetNo,previewType);
	}else{
		if('hk' == cameraType){
			previewHkCamera(insightUrl,result);
		}
	}
}

//预览新版海康摄像头
function previewHkCamera(insightUrl,result) {
	var recursiveEncoded = $.param(result);
    var url = insightUrl + "/camera/previewHkCamera?" + recursiveEncoded;
    openUrl(url,1000,600);
}

//打开新窗口
function openUrl(url, width, height, resizable) {
	if (url) {
		var w_width = screen.availWidth-14;
		var w_height = screen.availHeight-66;
		var left = 1;
		var top = 0;
		if (width) {
			w_width = width;
			left = (screen.availWidth - w_width) / 2;
		}
		if (height) {
			w_height = height;
			top = (screen.availHeight - w_height) / 2;
		}
		if (!resizable) {
			resizable = "yes";
		}
		window.open(url, "_blank", "left=" + left + ",top=" + top + ",height=" + w_height + ",width=" + w_width + ",resizable=" + resizable + ",scrollbars=yes");
	}
}
