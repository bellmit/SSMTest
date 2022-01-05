package cn.gtmap.onemap.platform;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午4:39
 */
public final class Constant {

    public static final String UTF_8 = "utf-8";

    /**
     *
     */
    public static final String DEFAULT_DATE_FORMATE = "yyyy-MM-dd";

    /**
     *
     */
    public static final String DEFAULT_DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     */
    public static final String TPL = "/tpls/";

    /**
     *
     */
    public static final String DEFAULT_TPL = "base.tpl";

    /**
     * 默认年份
     */
    public static final String DEFAULT_YEAR = "default.year";

    /**
     *
     */
    public static final String YEAR_CURRENT = "current";

    /**
     *
     */
    public static final String XZDM = "xzdm";

    /**
     *
     */
    public static final String CONFIG_SUFFIX = ".json";

    /**
     *
     */
    public static final String FUNCTION_KEY = "functions";

    public static final String DICTS_WORD = "dicts";

    public static final String MAP_WORD = "map";

    public static final String LAYER_KEY = "operationalLayers";

    /**
     * 空间引擎方式
     */
    public static final String SPATIAL_ENGINE = "spatial.engine";

    public static final String LOCATION_LABEL = "定位";

    public static final String SE_SHAPE_FIELD = "SHAPE";

    public static final String SE_SHAPE_AREA = "SHAPE_AREA";

    public static final String SE_OBJECTID = "OBJECTID";

    public static final String ORIGINAL_SHAPE_AREA = "OG_SHAPE_AREA";

    public static final String INPUT_SHAPE_AREA = "IN_SHAPE_AREA";

    public static final String GEO_KEY_FEATURES="features";

    public static final String GEO_KEY_PROPERTIES="properties";

    /**
     * 空间关系类型
     */
    public static final String INTERSECT_RELATION = "INTERSECT";
    /***
     *
     */
    public static final String WITHIN_RELATION = "WITHIN";

    /**
     *
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGN = "Access-Control-Allow-Origin";

    /**
     * 单独选址项目
     */
    public static final String REP_PROJ_SINGLE = "单独选址项目";
    /**
     * 分批次项目
     */
    public static final String REP_PROJ_BATCH = "批次城镇建设用地";

    /***
     * 4610 xian 1980
     */
    public static final String EPSG_4610 = "EPSG:4610";
    /***
     * 2364 xian 1980
     */
    public static final String EPSG_2364 = "EPSG:2364";

    /**
     * 普通探头
     */
    public static final String CAMERA_TYPE_NORMAL = "normal";

    public static final String FIELD_SHAPE="SHAPE";

    public static final String JSYDGZQ="建设用地管制区";

    public static final String REPORT="report";



    /**
     * 空间引擎类型
     */
    public enum SpatialType {
        ARC_SDE, ORACLE_SPATIAL,FEATURE_SERVICE
    }

    /**
     * 视频探头操作分类
     * OPEN(1) 打开操作
     * CLOSE(2) 关闭操作
     * END(3) 超时不操作关闭操作
     * CAPTURE(4) 抓图操作
     * HEARTBEAT(5) 心跳
     */
    public enum VideoOptType {
        OPEN(1), CLOSE(6), END(3), CAPTURE(4), HEARTBEAT(5);
        private int type;

        VideoOptType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 操作日志类型
     * PROJECTLOG: 项目操作日志
     * INSPECTLOG: 巡查操作日志
     */
    public enum OptLogType {
        PROJECTLOG(1), INSPECTLOG(2);
        private int type;
        OptLogType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    public enum StreamHandleType {
        play, stop
    }


    /**
     * ivs 云台控制码
     */
    public enum IVS_PTZ_CODE{
        PTZ_STOP,
        PTZ_UP,
        PTZ_DOWN,
        PTZ_LEFT,
        PTZ_UP_LEFT,
        PTZ_DOWN_LEFT,
        PTZ_RIGHT,
        PTZ_UP_RIGHT,
        PTZ_DOWN_RIGHT,
        PTZ_LENS_ZOOM_IN,
        PTZ_LENS_ZOOM_OUT,
        PTZ_LENS_FOCAL_NEAT,
        PTZ_LENS_FOCAL_FAR
    }
    
    public static final String STATIC_IMG_TEMP = "imgTemp";//静态图片临时目录

    //omp日志记录导出模板行政区,分局之间以"|"分割，部门之间以","分割
    public static final String TAIZHOU_CAMERALOG_XZQ = "市局,法规处,国土空间规划处,人事处,财务审计处,自然资源执法监督处,监察室,其他班子成员," +
            "地质和矿产资源处,储备中心,车队,不动产登记部门,万源估价所,测绘信息管理处,系统管理,自然资源开发利用处（行政许可处）,派驻纪检组,测试,不动产登记中心（权籍调查科）," +
            "林业站,规划编制研究中心,创新中心,党建和效能督查处,林业管理处,自然资源调查监测和科技处,自然资源确权登记处,建设项目规划技术审查处," +
            "泰州市规划院,自然资源所有者权益处,督导组,智慧规划,不动产登记中心（登记审核科）,其他处级干部,国土空间批后监管处,规划展示馆,泰州市测绘院,办公室," +
            "国土空间用途管制处,信息中心,办公室（信访办公室）,耕地保护和生态修复处,局长室,测绘调查科,登记审核科,综合科" +
            "|海陵分局,海陵分中心,海陵区分局,局长室（海陵）,分管局长(海陵),综合科（海陵）,办公室（海陵）,用地科（海陵）,执法监察科（海陵）,地籍地矿科（海陵）," +
            "第一国土资源中心所(海陵),第二国土资源中心所(海陵),第三国土资源中心所(海陵)" +
            "|高港分局,高港分中心,高港区分局,局长室（高港）,分管局长（高港）,综合科（高港）,规划科（高港）,用地科（高港）,地籍地矿科(高港),执法监察科（高港）," +
            "信访室（高港）,行政服务中心（高港）,第四国土资源中心所（高港）,第五国土资源中心所（高港）,第六国土资源中心所（高港）,第七国土资源中心所（高港）" +
            "|医药高新区,医药高新区分中心,医药高新区分局,农业开发区分局,医药园区,局长室（医药高新区）,办公室（医药高新区）,用地科(医药高新区),地籍地矿科(医药高新区)," +
            "执法监察科(农业开发区),利用监察科（医药高新区）,规划耕保科（医药高新区）,野徐国土所（医药高新区）,塘湾国土所（医药高新区）,寺巷国土所（医药高新区）" +
            "|姜堰区,姜堰区分局,分管局长（姜堰区）,办公室（姜堰）,规划科（姜堰区）,地籍科（姜堰区）,耕保科（姜堰区）,矿产管理办公室（姜堰区）,复垦中心（姜堰区）," +
            "勘测院（姜堰区）,白米中心所（姜堰区）,开发区中心所（姜堰区）,淤溪中心所（姜堰区）,溱潼中心所（姜堰区）,姜堰中心所（姜堰区）,娄庄中心所（姜堰区）,张甸中心所（姜堰区）";
}
