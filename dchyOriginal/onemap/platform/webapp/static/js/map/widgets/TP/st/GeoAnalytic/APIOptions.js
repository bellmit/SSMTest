/**
 * Created by jhj on 2017/9/26.
 */
(function (window) {
    var AgsAPI;
    var HTTPS = "https://";
    /** GA part **/
    var gaHost = HTTPS + "gis161.jsgt.local";
    /** portal part **/
    var portalHost = HTTPS + "gis154.jsgt.local";
    /** SpatialAnalysisTools host **/
    var spHost = HTTPS + "gis155.jsgt.local";
    var xzAnaHost = HTTPS + "172.18.31.190:9000";
    if (!AgsAPI) {
        AgsAPI = window.AgsAPI = {

            // xzdw: 'https://gis161.jsgt.local/arcgis/rest/services/DataStoreCatalogs/bigDataFileShares_jsgt80/BigDataCatalogServer/xzdw',
            // dltb: 'https://gis161.jsgt.local/arcgis/rest/services/DataStoreCatalogs/bigDataFileShares_jsgt80/BigDataCatalogServer/dltb',

            xzdw: 'https://gis161.jsgt.local/server/rest/services/DataStoreCatalogs/bigDataFileShares_jsgt80/BigDataCatalogServer/xzdw',
            dltb: 'https://gis161.jsgt.local/server/rest/services/DataStoreCatalogs/bigDataFileShares_jsgt80/BigDataCatalogServer/dltb',
            
            sr: "4610",

            portalUser: {
                username: 'arcgis',
                password: 'esri1234'
            },

            /**
             * esri-ga server
             * @return {string}
             */
            xzAnaServer: function () {
              return xzAnaHost + '/sdeGaAnalysis';
            },

            gaAdmin: function () {
                return gaHost + '/server/';
            },
            /**
             * geoanalytic gp server
             * @return {string}
             */
            gaGpServer: function () {
                return this.gaAdmin() + 'rest/services/System/GeoAnalyticsTools/GPServer';
            },
            /**
             * SpatialAnalysisTools gp server
             */
            spGpServer: function () {
                return spHost + '/arcgis/rest/services/System/SpatialAnalysisTools/GPServer';
            },
            /**
             * intersect gp server
             * @return {string}
             */
            isGpServer: function () {
                return spHost + '/arcgis/rest/services/intersecttest/intesectAll2/GPServer';
            },
            portalAdmin: function () {
                return portalHost + "/arcgis/";
            },
            portalRestRoot: function () {
                return this.portalAdmin() + "sharing/rest/";
            },
            portalContentRoot: function () {
                return this.portalRestRoot() + "content/";
            },
            soeUrl: function () {
                return "https://gis155.jsgt.local:6443/arcgis/rest/services/SampleWorldCities/MapServer/exts/CalcualteAreaLength/Intersect";
            },
            soeTargetPath: function () {
                return "/usr/local/apache-tomcat-8.0.32/webapps/ROOT";
            },
            soeTomcatUrl: function () {
                return "http://gis155.jsgt.local/";
            }
        };
    }
    if (typeof define === 'function' && define.amd) {
        define(AgsAPI);
    }

}(window));