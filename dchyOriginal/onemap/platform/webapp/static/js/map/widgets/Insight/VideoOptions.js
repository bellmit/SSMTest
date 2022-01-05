/**
 * @file video options
 * @author ibm
 * @date 2017/7/1
 * @version v1.0
 */
(function (window, document) {
    var VmOptions;
    if (!VmOptions) {
        VmOptions = window.VmOptions = {

            // 多视频记录模式
            multipleVideo: false,
            preWarningUrl: '',
            //泰兴版本控制
            txVersionEnable: true,
            //是否显示在/离线状态
            showStatus: false,
            // 检测摄像头在离线状态的间隔
            detectStatusTick: 600000,
            //监控点显示样式 list/group
            videoStyle: 'group',
            //弹出窗的样式 layer/infoWindow
            popupStyle: 'layer',
            //cluster方式显示地图监控点位置
            showVideoByCluster: false,
            // 是否每次只显示一个监控点的可视域
            showSingleViewScope: true,
            //是否显示可视域中心线
            showArcCenter: false,
            //全景图配置
            panoramaSwitch: false,

            //项目关联图层配置项
            relateToPlotInfos: false,
            //导出项目
            exportPros2Excel: false,
            //是否一次显示所有的项目位置
            showProLocAll: false,
            //是否根据监控点自动添加项目
            addProjectByCamera: false,
            //是否查询监控未使用情况
            checkUse:false,
            //项目类型
            proType: [
                {
                    "color": "#ffb61e",
                    "name": "建设用地"
                },
                {
                    "color": "#00bc12",
                    "name": "农用地"
                },
                {
                    "color": "#ff2d51",
                    "name": "未利用地"
                },
                {
                    "color": "#000000",
                    "name": "其他"
                }
            ],
            showYearInfo: false
        };
    }
    if (typeof define === 'function' && define.amd) {
        define(VmOptions);
    }

}(window, document));