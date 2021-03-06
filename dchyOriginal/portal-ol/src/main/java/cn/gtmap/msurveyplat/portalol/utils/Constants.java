package cn.gtmap.msurveyplat.portalol.utils;

import com.gtis.config.AppConfig;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String LOG_URL = AppConfig.getProperty("log.url");

    public static final String CAS_MODE_JSZWFW = "jszwfw";

    //文件中心
    public static final String WORK_FLOW_STUFF = "WORK_FLOW_STUFF";

    public static final String SHI = "是";

    public static final String SSMKID_MLK = "1";

    public static final String BLSX_MLKRZ = "1";

    public static final String INVALID = "0";
    public static final String VALID = "1";

    //字典表   字段类型
    public static final String DCHY_XMGL_CHXM_YHDW = "YHLX";

    public static final String SUCCESS = "0000"; // 成功
    public static final String FAIL = "1000"; // 数据查询失败

    public static final String SUCCESS_MSG = "成功";
    public static final String FAIL_MSG = "数据查询失败";


    //pfUser用户状态是否有效  默认有效
    public static final String DCHY_XMGL_YHZT_YX = "1";

    //文件上传 所属模块id 建设单位营业执照  测绘单位营业执照

    //申请状态
    public static final String DCHY_XMGL_SQZT_TH = "97";
    public static final String DCHY_XMGL_SQZT_SHTG = "98";

    //上传材料  材料名称
    public static final String DCHY_XMGL_SCCL_JSDWYYZZ = "建设单位营业执照";
    public static final String DCHY_XMGL_SCCL_CHDWYYZZ = "测绘单位营业执照";

    public static final String DCHY_XMGL_SCCL_MRFS = "1";

    public static final String DCHY_XMGL_CHXM_CLSX = "CLSX";

    public static final boolean WWSHARE_FILEIMPORT_NODENAMEBYXMID = AppConfig.getBooleanProperty("wwshare.fileImport.nodeNameByXmid");

    //线下备案手动录入建设单位用户类型
    public static final String DCHY_XMGL_YHLX_JSDW = "1";
    public static final String DCHY_XMGL_YHLX_CHDW = "2";

    //分页查询 默认的page 和 size
    public static final String DCHY_XMGL_PAGINATION_PAGE = "1";
    public static final String DCHY_XMGL_PAGINATION_SIZE = "10";

}
