package cn.gtmap.msurveyplat.serviceol.utils;

import com.gtis.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    private Constants() {
    }

    public static final String LOG_URL = AppConfig.getProperty("log.url");
    public static final String SECRET_KEY = AppConfig.getProperty("SECRET_KEY");
    /*
     * rabbitMq 用户名和连接密码
     */
    @Value("${spring.rabbitmq.host}")
    public static final String ADDRESS = "";
    @Value("${spring.rabbitmq.username}")
    public static final String USERNAME = "";
    @Value("${spring.rabbitmq.password}")
    public static final String PASSWROD = "";
    @Value("${spring.rabbitmq.port:5672}")
    public static int PORT = 5672;

    // mq 消息队列和交换机配置
    public static final String EXCHANGE_TYPE_DIRECT = "direct";

    public static final String BSDT_XMGL_DIRECT_EXCHANGE = "bsdt_xmgl";
    public static final String XMGL_BSDT_DIRECT_EXCHANGE = "xmgl_bsdt";


    public static final String BSDT_XMGL_XMFB_QUEUE = "bsdt_xmgl_xmfb";
    public static final String BSDT_XMGL_XMFB_QUEUE_MC = "线上办事大厅-->线下项目管理--项目发布";
    public static final String BSTD_XMGL_HTBG_QUEUE = "bsdt_xmgl_htbg";
    public static final String BSDT_XMGL_HTBG_QUEUE_MC = "线上办事大厅-->线下项目管理--合同变更";
    public static final String BSDT_XMGL_FWPJ_QUEUE = "bsdt_xmgl_fwpj";
    public static final String BSDT_XMGL_FWPJ_QUEUE_MC = "线上办事大厅-->线下项目管理--服务评价";
    public static final String BSDT_XMGL_MLKRZ_QUEUE = "bsdt_xmgl_mlkrz";
    public static final String BSDT_XMGL_MLKRZ_QUEUE_MC = "线上办事大厅-->线下项目管理--名录库入驻";
    public static final String BSDT_XMGL_CGJC_QUEUE = "bsdt_xmgl_cgjc";
    public static final String BSDT_XMGL_CGJC_QUEUE_MC = "线上办事大厅-->线下项目管理--成果检查";
    public static final String BSDT_XMGL_CGTJ_QUEUE = "bsdt_xmgl_cgtj";
    public static final String BSDT_XMGL_CGTJ_QUEUE_MC = "线上办事大厅-->线下项目管理--成果提交";
    public static final String BSDT_XMGL_ZXBJ_QUEUE = "bsdt_xmgl_zxbj";
    public static final String BSDT_XMGL_ZXBJ_QUEUE_MC = "线上办事大厅-->线下项目管理--在线办结";
    public static final String BSTD_XMGL_CG_VIEW_QUEUE = "bsdt_xmgl_cg_view";
    public static final String BSTD_XMGL_CG_VIEW_QUEUE_MC = "线上办事大厅-->线下项目管理--成果预览";
    public static final String BSDT_XMGL_TSWJ_QUEUE = "bsdt_xmgl_tswj";
    public static final String BSDT_XMGL_TSWJ_QUEUE_MC = "线上办事大厅-->线下项目管理--推送文件";
    public static final String BSDT_XMGL_JCSJ_QUEUE = "bsdt_xmgl_jcsj";
    public static final String BSDT_XMGL_JCSJ_QUEUE_MC = "线上办事大厅-->线下项目管理--基础数据";
    public static final String BSDT_XMGL_XXTX_QUEUE = "bsdt_xmgl_xxtx";
    public static final String BSDT_XMGL_XXTX_QUEUE_MC = "线上办事大厅-->线下项目管理--消息提醒";
    public static final String BSDT_XMGL_TJFX_QUEUE = "bsdt_xmgl_tjfx";
    public static final String BSDT_XMGL_TJFX_QUEUE_MC = "线上办事大厅-->线下项目管理--统计分析";
    public static final String BSDT_XMGL_JSDWLR_QUEUE = "bsdt_xmgl_jsdwlr";
    public static final String BSDT_XMGL_JSDWLR_QUEUE_MC = "线上办事大厅-->线下项目管理--建设单位录入";
    public static final String XMGL_BSDT_SLXX_QUEUE = "xmgl_bsdt_slxx";
    public static final String XMGL_BSDT_SLXX_QUEUE_MC = "线下项目管理-->线上办事大厅--受理信息";
    public static final String XMGL_BSDT_CGJC_QUEUE = "xmgl_bsdt_cgjc";
    public static final String XMGL_BSDT_CGJC_QUEUE_MC = "线下项目管理-->线上办事大厅--成果检查";
    public static final String XMGL_BSDT_CGTJ_QUEUE = "xmgl_bsdt_cgtj ";
    public static final String XMGL_BSDT_CGTJ_QUEUE_MC = "线下项目管理-->线上办事大厅--成果提交 ";
    public static final String XMGL_BSDT_CGCC_QUEUE = "xmgl_bsdt_cgcc ";
    public static final String XMGL_BSDT_CGCC_QUEUE_MC = "线下项目管理-->线上办事大厅--成果抽查 ";
    public static final String XMGL_BSDT_ZXBJ_QUEUE = "xmgl_bsdt_zxbj ";
    public static final String XMGL_BSDT_ZXBJ_QUEUE_MC = "线下项目管理-->线上办事大厅--在线办结 ";
    public static final String XMGL_BSDT_GC_VIEW_QUEUE = "xmgl_bsdt_gc_view";
    public static final String XMGL_BSDT_GC_VIEW_QUEUE_MC = "线下项目管理-->线上办事大厅--成果预览";
    public static final String XMGL_BSDT_JCSJ_QUEUE = "xmgl_bsdt_jcsj ";
    public static final String XMGL_BSDT_JCSJ_QUEUE_MC = "线下项目管理-->线上办事大厅--基础数据 ";
    public static final String XMGL_BSDT_TSWJ_QUEUE = "xmgl_bsdt_tswj ";
    public static final String XMGL_BSDT_TSWJ_QUEUE_MC = "线下项目管理-->线上办事大厅--推送文件";
    public static final String XMGL_BSDT_XXTX_QUEUE = "xmgl_bsdt_xxtx ";
    public static final String XMGL_BSDT_XXTX_QUEUE_MC = "线下项目管理-->线上办事大厅--消息提醒 ";
    public static final String XMGL_BSDT_TJFX_QUEUE = "xmgl_bsdt_tjfx ";
    public static final String XMGL_BSDT_TJFX_QUEUE_MC = "线下项目管理-->线上办事大厅--统计分析 ";
    public static final String XMGL_BSDT_BAXX_QUEUE = "xmgl_bsdt_baxx";
    public static final String XMGL_BSDT_BAXX_QUEUE_MC = "线下项目管理-->线上办事大厅--备案信息";
    public static final String XMGL_BSDT_JSDWLR_QUEUE = "xmgl_bsdt_jsdwlr";
    public static final String XMGL_BSDT_JSDWLR_QUEUE_MC = "线下项目管理-->线上办事大厅--建设单位录入";

    public static final String XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY = "xmgl_bsdt_slxx";
    public static final String BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY = "bsdt_xmgl_xmfb";
    public static final String BSDT_XMGL_HTBG_QUEUE_ROUTINGKEY = "bsdt_xmgl_htbg";
    public static final String BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY = "bsdt_xmgl_fwpj";
    public static final String BSDT_XMGL_MLKRZ_QUEUE_ROUTINGKEY = "bsdt_xmgl_mlkrz";
    public static final String BSDT_XMGL_CGJC_QUEUE_ROUTINGKEY = "bsdt_xmgl_cgjc";
    public static final String BSDT_XMGL_CGTJ_QUEUE_ROUTINGKEY = "bsdt_xmgl_cgtj";
    public static final String BSDT_XMGL_ZXBJ_QUEUE_ROUTINGKEY = "bsdt_xmgl_ZXBJ";
    public static final String BSDT_XMGL_CG_VIEW_QUEUE_ROUTINGKEY = "bsdt_xmgl_cg_view";
    public static final String BSDT_XMGL_TSWJ_QUEUE_ROUTINGKEY = "bsdt_xmgl_tswj";
    public static final String BSDT_XMGL_JCSJ_QUEUE_ROUTINGKEY = "bsdt_xmgl_jcsj";
    public static final String BSDT_XMGL_XXTX_QUEUE_ROUTINGKEY = "bsdt_xmgl_xxtx";
    public static final String BSDT_XMGL_TJFX_QUEUE_ROUTINGKEY = "bsdt_xmgl_tjfx";
    public static final String BSDT_XMGL_JSDWLR_QUEUE_ROUTINGKEY = "bsdt_xmgl_jsdwlr";
    public static final String XMGL_BSDT_CGJC_QUEUE_ROUTINGKEY = "xmgl_bsdt_cgjc";
    public static final String XMGL_BSDT_CGTJ_QUEUE_ROUTINGKEY = "xmgl_bsdt_cgtj ";
    public static final String XMGL_BSDT_CGCC_QUEUE_ROUTINGKEY = "xmgl_bsdt_cgcc ";
    public static final String XMGL_BSDT_ZXBJ_QUEUE_ROUTINGKEY = "xmgl_bsdt_ZXBJ ";
    public static final String XMGL_BSDT_CG_VIEW_QUEUE_ROUTINGKEY = "xmgl_bsdt_gc_view";
    public static final String XMGL_BSDT_JCSJ_QUEUE_ROUTINGKEY = "xmgl_bsdt_jcsj ";
    public static final String XMGL_BSDT_TSWJ_QUEUE_ROUTINGKEY = "xmgl_bsdt_tswj ";
    public static final String XMGL_BSDT_XXTX_QUEUE_ROUTINGKEY = "xmgl_bsdt_xxtx ";
    public static final String XMGL_BSDT_TJFX_QUEUE_ROUTINGKEY = "xmgl_bsdt_tjfx ";
    public static final String XMGL_BSDT_BAXX_QUEUE_ROUTINGKEY = "xmgl_bsdt_baxx ";
    public static final String XMGL_BSDT_JSDWLR_QUEUE_ROUTINGKEY = "xmgl_bsdt_jsdwlr ";

    //    多测合一测绘项目项目状态
    public static final String DCHY_XMGL_CHXM_XMZT_YQY = "已签约";
    public static final String DCHY_XMGL_CHXM_XMZT_YBJ = "已办结";

    //字典表   字段类型
    public static final String DCHY_XMGL_CHXM_TZGG = "GGLX";
    public static final String DCHY_XMGL_CHXM_YHDW = "YHLX";
    public static final String DCHY_XMGL_CHXM_DWXZ = "DWXZ";
    public static final String DCHY_XMGL_CHXM_ZZDJ = "ZZDJ";
    public static final String DCHY_XMGL_CHXM_CLSX = "CLSX";
    public static final String DCHY_XMGL_CHXM_KPJG = "KPJG";
    public static final String DCHY_XMGL_CHXM_CGZL = "CGZL";
    public static final String DCHY_XMGL_CHXM_MYD = "MYD";
    public static final String DCHY_XMGL_CHXM_XYD = "XYD";
    public static final String DCHY_XMGL_CHXM_NDKP = "NDKP";
    public static final String DCHY_XMGL_CHXM_FWPJ = "FWPJ";
    public static final String DCHY_XMGL_CHXM_PJZT = "PJZT";
    public static final String DCHY_XMGL_CHXM_XXLX = "XXLX";
    public static final String DCHY_XMGL_CHXM_WTZT = "WTZT";
    public static final String DCHY_XMGL_CHXM_GCDZ = "GCDZ";
    public static final String DCHY_XMGL_CHXM_WTCLSX = "WTCLSX";

    public static final String DCHY_XMGL_XXLX_PJ = "2";

    public static final String DCHY_XMGL_ZDLX_JDMC = "JDMC";

    public static final String DCHY_XMGL_CLSX_MRCLZT_ZC = "1";


    public static final String DCHY_XMGL_ONLINE_CGCK = "成果报告";
    public static final String DCHY_XMGL_ONLINE_SLTJ = "矢量图件";
    public static final String DCHY_XMGL_ONLINE_RKCG = "入库成果";

    public static final String EQUAL_QUERY = "equal";


    //分页查询 默认的page 和 size
    public static final String DCHY_XMGL_PAGINATION_PAGE = "1";
    public static final String DCHY_XMGL_PAGINATION_SIZE = "10";

    //申请状态
    public static final String DCHY_XMGL_SQZT_DSH = "1";
    public static final String DCHY_XMGL_SQZT_CS = "2";
    public static final String DCHY_XMGL_SQZT_FS = "3";
    public static final String DCHY_XMGL_SQZT_TH = "97";//退回
    public static final String DCHY_XMGL_SQZT_SHTG = "98";//通过
    public static final String DCHY_XMGL_SQZT_YGD = "99";//已归档

    //评价状态
    public static final String PJZT_WPJ = "0"; //未评价
    public static final String PJZT_YPJ = "1"; //已评价

    public static final String SUCCESS = "0000"; // 成功
    public static final String FAIL = "1000"; // 数据查询失败
    public static final String ERROR = "2000"; //用户名密码验证错误
    public static final String SECURITY_ERROR = "2010"; //安全token错误
    public static final String USER_NOT_LOGGED = "2020";// 用户未登录

    //消息提醒消息类型
    public static final String DCHY_XMGL_ZD_MLK = "1";//名录库入驻审核结果
    public static final String DCHY_XMGL_ZD_JP = "2";//评价
    public static final String DCHY_XMGL_ZD_CHGZJD = "3";//测绘工作进度
    public static final String DCHY_XMGL_ZD_CHTJCGSH = "4";//测绘提交成果审核
    public static final String DCHY_XMGL_ZD_YCMLK = "5";//移出名录库
    public static final String DCHY_XMGL_ZD_HTBASL = "6";//合同备案受理
    public static final String DCHY_XMGL_ZD_FBWT = "12";//发布委托

    //消息提醒
    public static final String DCHY_XMGL_ZD_XXNR_MLKRZ_TG = "1";//名录库入驻审核结果通过
    public static final String DCHY_XMGL_ZD_XXNR_MLKRZ_BTG = "2";//名录库入驻审核结果不通过
    public static final String DCHY_XMGL_ZD_XXNR_JSDWPJ = "3";//建设单位评价
    public static final String DCHY_XMGL_ZD_XXNR_GLDWPJ = "4";//管理单位评价
    public static final String DCHY_XMGL_ZD_XXNR_JFQX = "5";//测绘工作进度，交付期限前15天
    public static final String DCHY_XMGL_ZD_XXNR_CQ = "6";//测绘工作进度超期
    public static final String DCHY_XMGL_ZD_XXNR_CGSH_TG = "7";//测绘提交成果审核通过
    public static final String DCHY_XMGL_ZD_XXNR_CGSH_BTG = "8";//测绘提交成果审核不通过
    public static final String DCHY_XMGL_ZD_XXNR_MLKYC = "9";//名录库移出
    public static final String DCHY_XMGL_ZD_XXNR_HTBAYSL = "10"; //合同备案已受理
    public static final String DCHY_XMGL_ZD_XXNR_ZXWT_TG = "11";//在线委托核验通过
    public static final String DCHY_XMGL_ZD_XXNR_ZXWT_CHDW_TG = "12";//委托核验通过(测绘单位)
    public static final String DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_TG = "13"; //委托核验通过(建设单位)
    public static final String DCHY_XMGL_ZD_XXNR_JSDWWT = "14"; //建设单位发布委托通知
    public static final String DCHY_XMGL_ZD_XXNR_CHDWWT = "15"; //测绘单位发布委托通知
    public static final String DCHY_XMGL_ZD_XXNR_ZXWT_CHDW_BTG = "16";//委托核验不通过(测绘单位)
    public static final String DCHY_XMGL_ZD_XXNR_ZXWT_JSDW_BTG = "17"; //委托核验不通过(建设单位)


    public static final String SUCCESS_MSG = "成功";
    public static final String FAIL_MSG = "数据查询失败";
    public static final String ERROR_MSG = "用户名密码验证错误";
    public static final String SECURITY_ERROR_MSG = "安全token错误";
    public static final String USER_NOT_LOGGED_MSG = "用户未登录";

    public static final String LONG_TERM = "1";//长期有效

    public static final boolean WWSHARE_FILEIMPORT_NODENAMEBYXMID = AppConfig.getBooleanProperty("wwshare.fileImport.nodeNameByXmid");

    //文件中心
    public static final String WORK_FLOW_STUFF = "WORK_FLOW_STUFF";

    public static final String SHI = "是";

    public static final String FOU = "否";

    public static final String BLSX_MLKRZ = "1";

    public static final String BLSX_FBWT = "4";

    public static final String BLSX_ZXKF = "5";//办理事项 在线客服

    public static final String BLSX_MLKZX = "6";//办理事项 名录库注销

    public static final String BLSX_MLKYC = "7";//办理事项 名录库移除

    public static final String VALID = "1";// 有效

    public static final String INVALID = "0";//无效

    public static final String TG_MC = "通过";//通过

    public static final String TH_MC = "退回";//退回

    public static final String XMLY_XSFB = "1"; //线上发布
    public static final String XMLY_XXBA = "2"; //线下备案
    public static final String XMLY_XSWT = "3"; //线上委托

    public static final String ISHY_WHY = "0"; //未核验
    public static final String ISHY_HYZ = "1"; //核验中

    public static final String WTZT_DWT = "1"; //待委托
    public static final String WTZT_DJS = "2"; //待接受
    public static final String WTZT_YJS = "3"; //已接受
    public static final String WTZT_YJJ = "4"; //已拒绝
    public static final String WTZT_YBA = "5";//已备案
    public static final String WTZT_DBA = "6";//待备案
    public static final String WTZT_YTH = "7";//已退回

    public static final String DCHY_XMGL_BLSX_LOGOUT_MLK = "6";//办理事项-注销名录库

    //项目状态
    public static final String XMZT_DBA = "1"; //待备案

    //推送项目消息标识未上传的文件中心id
    public static final String WJZXID_FLAG = "NOTUPLOAD";

    //消息推送
    public static final String MLKRZ_TG = "您好，您的名录库申请【审核通过】，您的单位已成功入驻名录库";
    public static final String MLKRZ_BTG = "您好，您的名录库申请【审核不通过】，您可查看退回意见重新提交哦。";
    public static final String MLKRZ_YC = "您好，您的单位在(移出时间)被移出测绘名录库，再接再厉哦";

    //跳转
    public static final String XXTX_SFTZ_BTZ = "0"; //不跳转
    public static final String XXTX_SFTZ_MLKTZ = "1"; //名录库入驻跳转
    public static final String XXTX_SFTZ_PJTZ = "2"; //建设单位评价记录跳转
    public static final String XXTX_SFTZ_KPTZ = "3"; //管理单位考评记录跳转

    public static final int RETURN_SUCCESS = 1;
    public static final int RETURN_FAIL = 0;

    public static final String DWLX_JSDW = "jsdw";
    public static final String DWLX_CHDW = "chdw";

    //默认评价等级
    public static final String MLK_PJDJ = "0";

    public static final String DQJD_SH = "1";  //当前节点 审核

    public static final String FBWT_BC = "1";  //发布委托 保存
    public static final String FBWT_QRWT = "2";  //发布委托 确认委托

    public static final String DCHY_XMGL_JCSJSQ_CLMC = "数据范围";  //基础数据申请数据范围

    public static final Integer DCHY_XMGL_MRFS = 1;  //基础数据申请默认页数

    public static final Integer DCHY_XMGL_MRYS = 1;  //基础数据申请默认份数

    public static final Integer DCHY_XMGL_MRXH = 1;  //默认序号

    public static final String DCHY_XMGL_SJCL_CLLX_QT = "5";  //收件材料材料类型其他

    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_SHZ = "1";  //基础数据申请当前状态审核中
    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_DZZ = "2";  //基础数据申请当前状态待制作
    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_ZZZ = "3";  //基础数据申请当前状态制作中
    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_DJF = "4";  //基础数据申请当前状态待交付
    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_YJF = "98";  //基础数据申请当前状态已交付
    public static final String DCHY_XMGL_JCSJSQ_DQSQZT_YTH = "99";  //基础数据申请当前状态已退回


    public static final String DCHY_XMGL_JCSJSQ_PJZT_DCC = "1";  //基础数据申请当前状态待抽查
    public static final String DCHY_XMGL_JCSJSQ_PJZT_DPJ = "2";  //基础数据申请当前状态待交付
    public static final String DCHY_XMGL_JCSJSQ_PJZT_YPJ = "3";  //基础数据申请当前状态已交付

    public static final String DCHY_XMGL_ZDLX_XMZT = "XMZT";  //字典表字段类型项目状态
    public static final String DCHY_XMGL_ZDLX_GCDZ = "GCDZ";  //字典表字段类型工程地址


    public static final String DCHY_XMGL_CGCC_MRTS = "10";  //成果抽查默认条数

    //成果包是否提交
    public static final String DCHY_XMGL_CLCG_SFTJ_FALSE = "0";
    public static final String DCHY_XMGL_CLCG_SFTJ_TRUE = "1";

    //成果提交状态
    public static final String DCHY_XMGL_XMCGZT_WTJ = "0";//未提交
    public static final String DCHY_XMGL_XMCGZT_DSH = "1";//已提交待审核
    public static final String DCHY_XMGL_XMCGZT_SHZ = "2";//审核中
    public static final String DCHY_XMGL_XMCGZT_YRK = "3";//已入库
    public static final String DCHY_XMGL_XMCGZT_YTH = "4";//已退回
    public static final String DCHY_XMGL_XMCGZT_WWT = "5";//未委托

    public static final String DCHY_XMGL_CHXM_ZDXM = "1";
    public static final String DCHY_XMGL_CHXM_FZDXM = "0";

    public static final String DCHY_XMGL_SQFS_XS = "1"; //基础数据申请申请方式 线上
    public static final String DCHY_XMGL_SQFS_XX = "2";//基础数据申请申请方式 线下


    public static final String DCHY_XMGL_AUTHORIZE_SQLX_YXFW = "1";

    public static final String DCHY_XMGL_AUTHORIZE_ZYLX_INTERFACE = "1";
    public static final String DCHY_XMGL_AUTHORIZE_ZYLX_VIEW = "2";

    public static final String SHTG = "1";  //审核通过
    public static final String SHBTG = "0";  //审核不通过

    //基础数据申请流程
    public static final String DCHY_XMGL_JCSJSQ_LCMC = "基础数据管理";
    public static final String DCHY_XMGL_JCSJSQ_HJ_SH = "审核";

    //在线客服 是否回复
    public static final String DCHY_XMGL_ZXKF_SFHF_DHF = "0";
    public static final String DCHY_XMGL_ZXKF_SFHF_YHF = "1";

    //在线客服 是否公开
    public static final String DCHY_XMGL_ZXKF_SFGK_GK = "1";
    public static final String DCHY_XMGL_ZXKF_SFGK_BGK = "0";

    //附件材料配置
    public static final Integer DCHY_XMGL_SJCL_FBX = 0; //材料非必须上传
    public static final Integer DCHY_XMGL_SJCL_BX = 1;  //材料必须上传

    //mq推送数据操作类型
    public static final String DCHY_XMGL_SJTS_CZLX_DEL = "0";
    public static final String DCHY_XMGL_SJTS_CZLX_SAVE = "1";

    //在线办结的几种情况
    public static final String DCHY_XMGL_ZXBJ = "onlineComplete";
    public static final String DCHY_XMGL_ZXBJ_JC = "onlineCompleteCheck";
    public static final String DCHY_XMGL_ZXBJ_YL = "onlineGcPreview";
    public static final String DCHY_XMGL_ZXBJ_YLZJD = "onlineGcPreviewById";
    public static final String DCHY_XMGL_ZXBJ_ZXSC = "onlineGetUploadFileNums";
    public static final String DCHY_XMGL_ZXBJ_ZXXZ = "onlineGcDownload";


    //自动备案配置项
    public static final String DCHY_XMGL_ZDBA_PZ = "is.automatic.filing";

    public static final String EMPTYPARAM_VALUE = "emptyParamValue";

    //名录库冻结
    public static final String DCHY_XMGL_MLK_DJ = "0";

    //boolean字符串
    public static final String DCHY_XMGL_BOOLEAN_TRUE = "TRUE";
    public static final String DCHY_XMGL_BOOLEAN_FALSE = "FALSE";

    public static final String DCHY_XMGL_CLSX_SEPARATOR = ";";
    public static final String DCHY_XMGL_CLSX_SEPARATOR2 = ",";

    public static final String DCHY_XMGL_SSMKID_HTBA = "22";

    //多测合一用户单位类型
    public static final String DCHY_XMGL_YHLX_JSDW = "1";
    public static final String DCHY_XMGL_YHLX_CHDW = "2";

    //常州测绘体量配置
    public static final String CLSX_JD_ZS = "1"; //农用地转用土地征收阶段
    public static final String CLSX_JD_LX = "2"; //立项用地规划许可阶段
    public static final String CLSX_JD_GX = "3"; //工程建设规划许可阶段
    public static final String CLSX_JD_SG = "4"; //建设工程施工阶段
    public static final String CLSX_JD_JG = "5"; //竣工验收阶段
    public static final String CLSX_JD_GX_XXGC = "10"; //断面测量
    public static final String CLSX_JD_GX_FCYC = "11"; //房产面积预测
    public static final String CLSX_JD_SG_XXFY = "13"; //线性工程放样
    public static final String CLSX_JD_SG_HXYX = "14"; //灰线验线测量
    public static final String CLSX_JD_JG_SPF = "19"; //商品房竣工验收阶段
    public static final String CLSX_JD_JG_DWF = "20"; //单位房竣工验收阶段
    public static final String CLSX_JD_JG_XXGC = "21"; //市政管线及道路交通等线性工程
    //常州测绘事项
    public static final String CLSX_XXGC = "3001"; //断面测量
    public static final String CLSX_FCYC = "3002"; //房产面积预测
    public static final String CLSX_XXFY = "4002";  //线性工程放样
    public static final String CLSX_HXYX = "4003"; //灰线验线测量
    //房屋类型
    public static final String FWLX_SPF = "0"; //商品房
    public static final String FWLX_DWF = "1"; //单位房
    //单位
    public static final String CLSX_CHTL_MC_M = "米";
    public static final String CLSX_CHTL_MC_PFM = "平方米";
    public static final String CLSX_CHTL_MC_Z = "幢";
    public static final String CLSX_CHTL_DM_M = "1";
    public static final String CLSX_CHTL_DM_PFM = "2";
    public static final String CLSX_CHTL_DM_Z = "3";

    //pdf模板名称
    public static final String DCHY_XMGL_BLANK = "model";
    public static final String DCHY_XMGL_HZDMC = "多测合一项目合同备案登记受理回执单";
    public static final String DCHY_XMGL_TYJFQRD = "多测合一项目成果交付确认单";

    public static final String YES = "1";
    public static final String NO = "0";

}

