define(["dojo/_base/declare",
     "dojo/_base/lang",
    "map/core/BaseWidget",
    "esri/layers/ArcGISTiledMapServiceLayer",
    "esri/layers/ArcGISDynamicMapServiceLayer",
    "map/MainMap",
    "map/manager/ConfigManager"
], function (declare,lang,BaseWidget,ArcGISTiledMapServiceLayer,ArcGISDynamicMapServiceLayer,MainMap,ConfigManager) {
    var _map,_baseMapConfig,optLayer,baseMap,baseMapx,nowLayer,newLayer,nowPhoto,newPhoto,photo,photox;
    var _configManager = ConfigManager.getInstance();
    var me = declare([BaseWidget], {
        constructor: function () {
        },
      
        onCreate: function () {
            _map=this.getMap().map();
            _baseMapConfig=this.getConfig();
            baseMap=_baseMapConfig.baseMap;
            baseMapx=_baseMapConfig.baseMapx;
            optLayer=_configManager.getOperaLayers();
            MainMap.addLayer(baseMap,-1);
            nowLayer=baseMap;
            newLayer=baseMapx;
            $("#BasemapTitle").text(newLayer.title);
            photo="../static/js/map/widgets/Basemap/BasemapPhoto/satellite.jpg";
            photox="../static/js/map/widgets/Basemap/BasemapPhoto/osm.jpg";
            nowPhoto=photo;
            newPhoto=photox;
            $("#BasemapView").css("background-image","url("+newPhoto+")");
            _addListeners();
        },
      
        onPause: function () {

        },
      
        onOpen: function () {

        }
        
    });
    function _addListeners() {
        $("#Basemap").click(function () {
            MainMap.removeLayerById(nowLayer.id);
            MainMap.addLayer(newLayer,-1);
            var changeLayer=nowLayer;
            nowLayer=newLayer;
            newLayer=changeLayer;
            var photoUrl=nowPhoto;
            nowPhoto=newPhoto;
            newPhoto=photoUrl;
            $("#BasemapTitle").text(newLayer.title);
            $("#BasemapView").css("background-image","url("+newPhoto+")");
        })
    }
    return me;
});