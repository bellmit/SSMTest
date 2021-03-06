package cn.gtmap.msurveyplat.server.util;


import com.gtis.config.AppConfig;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 常量类
 */
public class Constants {

    //DEFAULT大写
    public static final String DEFAULT_UPPERCASE = "DEFAULT";
    //字符串分割符
    public static final String SPLIT_STR = "$";


    //成果数据类型
    public static final String ZJ_CGSJLX_GHCH = "规划竣工测绘";
    public static final String ZJ_CGSJLX_FCCH = "房产测绘";
    public static final String ZJ_CGSJLX_DJCH = "地籍测绘";

    //入库状态
    public static final String RKZT_YRK_DM = "1"; //已入库
    public static final String RKZT_WRK_DM = "0"; //未入库

    //项目状态
    public static final String XMZT_YBJ_DM = "1"; //已办结
    public static final String XMZT_BLZ_DM = "0"; //办理中

    //竣工验收
    public static final String CHJD_JGYS_MC = "竣工验收";

    //附件上传是否检查文件完整性
    public static Integer FJSC_SFJCWJWZX_YES = 1;
    public static Integer FJSC_SFJCWJWZX_NO = 0;

    //分页查询 默认的page 和 size
    public static final Integer DCHY_XMGL_PAGINATION_PAGE = 1;
    public static final Integer DCHY_XMGL_PAGINATION_SIZE = 10;
    // 是否
    public static String VALID = "1";
    public static String INVALID = "0";

    // 申请审核状态
    public static String GXYWSQ_SHZT_SHTG = "99";//审核通过
    public static String GXYWSQ_SHZT_TH = "98";//审核退回
    public static String GXYWSQ_SHZT_YSQ = "1";//审核中
    public static String GXYWSQ_SHZT_WSQ = "0";//未申请

    // 共享申请流程工作流定义id
    public static final String GXYWSQ_GZLDYID = AppConfig.getProperty("gxywsq.gzldyid");
}
