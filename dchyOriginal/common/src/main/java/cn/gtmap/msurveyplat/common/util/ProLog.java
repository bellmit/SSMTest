package cn.gtmap.msurveyplat.common.util;

public class ProLog {
    public enum Czlx {
        SAVE(CZLX_SAVE_CODE, CZLX_SAVE_MC),
        DELETE(CZLX_DELETE_CODE, CZLX_DELETE_MC),
        CREATE(CZLX_CREATE_CODE, CZLX_CREATE_MC),
        RESET(CZLX_RESET_CODE, CZLX_RESET_MC);

        private String code;

        private String mc;

        Czlx(String code, String mc) {
            this.code = code;
            this.mc = mc;
        }

        public String getCode() {
            return this.code;
        }

        public String getMc() {
            return mc;
        }
    }

    public enum Czmk {
        XZSLDJ(CZMK_XZSLDJ_CODE, CZMK_XZSLDJ_MC),
        SIGN(CZMK_SIGN_CODE, CZMK_SIGN_MC),
        SH(CZMK_SH_CODE, CZMK_SH_MC),
        CZZD(CZMK_CZZD_CODE, CZMK_CZZD_MC);

        public String code;

        public String mc;

        Czmk(String code, String mc) {
            this.code = code;
            this.mc = mc;
        }

        public String getCode() {
            return this.code;
        }

        public String getMc() {
            return mc;
        }
    }

    public enum Ssmkid {
        MLKRZ(SSMKID_MLKRZ_CODE, SSMKID_MLKRZ_MC),
        CYRY(SSMKID_CYRY_CODE, SSMKID_CYRY_MC),
        CHDWZC(SSMKID_CHDWZC_CODE, SSMKID_CHDWZC_MC),
        JSDWZC(SSMKID_JSDWZC_CODE, SSMKID_JSDWZC_MC),
        HTMBGL(SSMKID_HTMBGL_CODE, SSMKID_HTMBGL_MC),
        FBXQWJ(SSMKID_FBXQWJ_CODE, SSMKID_FBXQWJ_MC),
        TZGG(SSMKID_TZGG_CODE, SSMKID_TZGG_MC),
        CHXMSLHTXX(SSMKID_CHXMSLHTXX_CODE, SSMKID_CHXMSLHTXX_MC),
        MBGL(SSMKID_MBGL_CODE, SSMKID_MBGL_MC),
        MLKRZSHJG(SSMKID_MLKRZSHJG_CODE, SSMKID_MLKRZSHJG_MC),
        PJ(SSMKID_PJ_CODE, SSMKID_PJ_MC),
        CHGZJD(SSMKID_CHGZJD_CODE, SSMKID_CHGZJD_MC),
        CHTJCGSH(SSMKID_CHTJCGSH_CODE, SSMKID_CHTJCGSH_MC),
        YCMLK(SSMKID_YCMLK_CODE, SSMKID_YCMLK_MC),
        HTBASL(SSMKID_HTBASL_CODE, SSMKID_HTBASL_MC),
        SLDBJ(SSMKID_SLDBJ_CODE, SSMKID_SLDBJ_MC),
        FBWT(SSMKID_FBWT_CODE, SSMKID_FBWT_MC),
        JSDWWT(SSMKID_JSDWWT_CODE, SSMKID_JSDWWT_MC),
        CHDWHY(SSMKID_CHDWHY_CODE, SSMKID_CHDWHY_MC),
        JCSJSQ(SSMKID_JCSJSQ_CODE, SSMKID_JCSJSQ_MC),
        CGCCPJ(SSMKID_CGCCPJ_CODE, SSMKID_CGCCPJ_MC),
        ZXBA(SSMKID_ZXBA_CODE, SSMKID_ZXBA_MC),
        XXBA(SSMKID_XXBA_CODE, SSMKID_XXBA_MC),
        CGTJ(SSMKID_CGTJ_CODE, SSMKID_CGTJ_MC),
        YHZC(SSMKID_YHZC_CODE, SSMKID_YHZC_MC),
        ZXKF(SSMKID_ZXKF_CODE, SSMKID_ZXKF_MC),
        SJCLPZ(SSMKID_SJCLPZ_CODE, SSMKID_SJCLPZ_MC),
        TJFX(SSMKID_TJFX_CODE, SSMKID_TJFX_MC),
        EXAMPLE(SSMKID_EXAMPLE_CODE, SSMKID_EXAMPLE_MC);

        public String code;

        public String mc;

        Ssmkid(String code, String mc) {
            this.code = code;
            this.mc = mc;
        }

        public String getCode() {
            return this.code;
        }

        public String getMc() {
            return mc;
        }
    }

    // 所属模块id
    public static final String SSMKID_EXAMPLE_CODE = "XXXXX";
    public static final String SSMKID_EXAMPLE_MC = "XXXXX";

    public static final String SSMKID_MLKRZ_CODE = "1";
    public static final String SSMKID_MLKRZ_MC = "名录库入驻";

    public static final String SSMKID_CYRY_CODE = "2";
    public static final String SSMKID_CYRY_MC = "从业人员";

    public static final String SSMKID_CHDWZC_CODE = "3";
    public static final String SSMKID_CHDWZC_MC = "测绘单位注册";

    public static final String SSMKID_JSDWZC_CODE = "4";
    public static final String SSMKID_JSDWZC_MC = "建设单位注册";

    public static final String SSMKID_HTMBGL_CODE = "5";
    public static final String SSMKID_HTMBGL_MC = "合同模板管理";

    public static final String SSMKID_FBXQWJ_CODE = "6";
    public static final String SSMKID_FBXQWJ_MC = "发布需求文件";

    public static final String SSMKID_TZGG_CODE = "7";
    public static final String SSMKID_TZGG_MC = "通知公告";

    public static final String SSMKID_CHXMSLHTXX_CODE = "8";
    public static final String SSMKID_CHXMSLHTXX_MC = "测绘项目受理合同信息";

    public static final String SSMKID_MBGL_CODE = "9";
    public static final String SSMKID_MBGL_MC = "模板管理";

    public static final String SSMKID_MLKRZSHJG_CODE = "10";
    public static final String SSMKID_MLKRZSHJG_MC = "名录库入驻审核结果";

    public static final String SSMKID_PJ_CODE = "11";
    public static final String SSMKID_PJ_MC = "评价";

    public static final String SSMKID_CHGZJD_CODE = "12";
    public static final String SSMKID_CHGZJD_MC = "测绘工作进度";

    public static final String SSMKID_CHTJCGSH_CODE = "13";
    public static final String SSMKID_CHTJCGSH_MC = "测绘提交成果审核";

    public static final String SSMKID_YCMLK_CODE = "14";
    public static final String SSMKID_YCMLK_MC = "移出名录库";

    public static final String SSMKID_HTBASL_CODE = "15";
    public static final String SSMKID_HTBASL_MC = "合同备案受理";

    public static final String SSMKID_SLDBJ_CODE = "16";
    public static final String SSMKID_SLDBJ_MC = "受理单办结";

    public static final String SSMKID_FBWT_CODE = "17";
    public static final String SSMKID_FBWT_MC = "发布委托";

    public static final String SSMKID_JSDWWT_CODE = "18";
    public static final String SSMKID_JSDWWT_MC = "建设单位委托";

    public static final String SSMKID_CHDWHY_CODE = "19";
    public static final String SSMKID_CHDWHY_MC = "测绘单位核验";

    public static final String SSMKID_JCSJSQ_CODE = "20";
    public static final String SSMKID_JCSJSQ_MC = "基础数据申请";

    public static final String SSMKID_CGCCPJ_CODE = "21";
    public static final String SSMKID_CGCCPJ_MC = "成果抽查评价";

    public static final String SSMKID_ZXBA_CODE = "22";
    public static final String SSMKID_ZXBA_MC = "在线备案上传合同";

    public static final String SSMKID_XXBA_CODE = "23";
    public static final String SSMKID_XXBA_MC = "线下备案附件材料";

    public static final String SSMKID_CGTJ_CODE = "24";
    public static final String SSMKID_CGTJ_MC = "成果提交";

    public static final String SSMKID_YHZC_CODE = "25";
    public static final String SSMKID_YHZC_MC = "用户注册";

    public static final String SSMKID_ZXKF_CODE = "26";
    public static final String SSMKID_ZXKF_MC = "在线客服";

    public static final String SSMKID_SJCLPZ_CODE = "27";
    public static final String SSMKID_SJCLPZ_MC = "收件材料配置";

    public static final String SSMKID_TJFX_CODE = "28";
    public static final String SSMKID_TJFX_MC = "统计分析";

    // 保存
    public static final String CZLX_SAVE_CODE = "1";
    public static final String CZLX_SAVE_MC = "保存或修改";
    // 删除
    public static final String CZLX_DELETE_CODE = "2";
    public static final String CZLX_DELETE_MC = "删除";
    // 新增
    public static final String CZLX_CREATE_CODE = "3";
    public static final String CZLX_CREATE_MC = "新增";
    // 重置
    public static final String CZLX_RESET_CODE = "9";
    public static final String CZLX_RESET_MC = "重置";
    //查询
    public static final String CZLX_SELECT_CODE = "4";
    public static final String CZLX_SELECT_MC = "查询";
    //上传
    public static final String CZLX_UPLOAD_CODE = "5";
    public static final String CZLX_UPLOAD_MC = "上传";
    //下载
    public static final String CZLX_DOWNLOAD_CODE = "6";
    public static final String CZLX_DOWNLOAD_MC = "下载";


    public static final String CZMK_XZSLDJ_CODE = "1001";
    public static final String CZMK_XZSLDJ_MC = "新增受理登记";

    public static final String CZMK_SIGN_CODE = "1002";
    public static final String CZMK_SIGN_MC = "签名";

    public static final String CZMK_SH_CODE = "1003";
    public static final String CZMK_SH_MC = "审核";

    public static final String CZMK_CZZD_CODE = "9001";
    public static final String CZMK_CZZD_MC = "重置字典信息";

    public static final String CZMK_CZAUTH_CODE = "9002";
    public static final String CZMK_CZAUTH_MC = "重置授权信息";

    public static final String CZMK_BALB_CODE = "2001";
    public static final String CZMK_BALB_MC = "备案列表";

    public static final String CZMK_XSWT_CODE = "2002";
    public static final String CZMK_XSWT_MC = "线上委托";

    public static final String CZMK_DBAXX_CODE = "2003";
    public static final String CZMK_DBAXX_MC = "待办备案";

    public static final String CZMK_FBXQ_CODE = "2004";
    public static final String CZMK_FBXQ_MC = "线上需求发布";

    public static final String CZMK_BASFGQ_CODE = "2001";
    public static final String CZMK_BASFGQ_MC = "备案项目是否挂起";

    public static final String CZMC_MLKDJ_CODE = "3001";
    public static final String CZMC_MLKDJ_MC = "名录库冻结";

    public static final String CZMC_MLK_LOGOUT_CODE = "3002";
    public static final String CZMC_MLK_LOGOUT_MC = "名录库注销";

    public static final String CZMC_MLK_ALTER_CODE = "3003";
    public static final String CZMC_MLK_ALTER_MC = "名录库信息修改";

    public static final String CZMC_CYRY_ALTER_CODE = "3004";
    public static final String CZMC_CYRY_ALTER_MC = "从业人员信息修改";

    public static final String CZMC_CGTJ_CODE = "4001";
    public static final String CZMC_CGTJ_MC = "成果提交";

    public static final String CZMC_CGCC_CODE = "4002";
    public static final String CZMC_CGCC_MC = "成果抽查";

    public static final String CZMC_CGPJ_CODE = "4003";
    public static final String CZMC_CGPJ_MC = "成果评价";

    public static final String CZMC_JCSJSQ_CODE = "4004";
    public static final String CZMC_JCSJSQ_MC = "基础数据申请";

    public static final String CZMC_TZGG_CODE = "4005";
    public static final String CZMC_TZGG_MC = "通知公告";

    public static final String CZMC_YHZC_CODE = "4006";
    public static final String CZMC_YHZC_MC = "用户注册";

    public static final String CZMC_ZXKF_CODE = "4007";
    public static final String CZMC_ZXKF_MC = "在线客服";

    public static final String CZMC_CGTYJFQRD_CODE = "4008";
    public static final String CZMC_CGTYJFQRD_MC = "成果统一交付确认单预览";

    public static final String CZMC_CGTYJFQR_CODE = "4009";
    public static final String CZMC_CGTYJFQR_MC = "成果统一交付确认";

    public static final String CZMC_CGTYJFQRJC_CODE = "4010";
    public static final String CZMC_CGTYJFQRJC_MC = "成果统一交付确认状态检查";

    public static final String CZMC_SJCLPZ_CODE = "4011";
    public static final String CZMC_SJCLPZ_MC = "收件材料配置";

    public static final String CZMC_HTDJBA_CODE = "4012";
    public static final String CZMC_HTDJBA_MC = "合同登记备案";

    public static final String CZMC_TJFX_CODE = "4013";
    public static final String CZMC_TJFX_MC = "统计分析";

    public static final String CZMC_WJSC_CODE = "4014";
    public static final String CZMC_WJSC_MC = "文件上传";

    public static final String CZMC_HTWJSC_CODE = "4015";
    public static final String CZMC_HTWJSC_MC = "合同文件上传";

}
