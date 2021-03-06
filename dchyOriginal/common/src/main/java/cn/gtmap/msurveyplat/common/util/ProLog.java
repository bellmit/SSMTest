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

    // ????????????id
    public static final String SSMKID_EXAMPLE_CODE = "XXXXX";
    public static final String SSMKID_EXAMPLE_MC = "XXXXX";

    public static final String SSMKID_MLKRZ_CODE = "1";
    public static final String SSMKID_MLKRZ_MC = "???????????????";

    public static final String SSMKID_CYRY_CODE = "2";
    public static final String SSMKID_CYRY_MC = "????????????";

    public static final String SSMKID_CHDWZC_CODE = "3";
    public static final String SSMKID_CHDWZC_MC = "??????????????????";

    public static final String SSMKID_JSDWZC_CODE = "4";
    public static final String SSMKID_JSDWZC_MC = "??????????????????";

    public static final String SSMKID_HTMBGL_CODE = "5";
    public static final String SSMKID_HTMBGL_MC = "??????????????????";

    public static final String SSMKID_FBXQWJ_CODE = "6";
    public static final String SSMKID_FBXQWJ_MC = "??????????????????";

    public static final String SSMKID_TZGG_CODE = "7";
    public static final String SSMKID_TZGG_MC = "????????????";

    public static final String SSMKID_CHXMSLHTXX_CODE = "8";
    public static final String SSMKID_CHXMSLHTXX_MC = "??????????????????????????????";

    public static final String SSMKID_MBGL_CODE = "9";
    public static final String SSMKID_MBGL_MC = "????????????";

    public static final String SSMKID_MLKRZSHJG_CODE = "10";
    public static final String SSMKID_MLKRZSHJG_MC = "???????????????????????????";

    public static final String SSMKID_PJ_CODE = "11";
    public static final String SSMKID_PJ_MC = "??????";

    public static final String SSMKID_CHGZJD_CODE = "12";
    public static final String SSMKID_CHGZJD_MC = "??????????????????";

    public static final String SSMKID_CHTJCGSH_CODE = "13";
    public static final String SSMKID_CHTJCGSH_MC = "????????????????????????";

    public static final String SSMKID_YCMLK_CODE = "14";
    public static final String SSMKID_YCMLK_MC = "???????????????";

    public static final String SSMKID_HTBASL_CODE = "15";
    public static final String SSMKID_HTBASL_MC = "??????????????????";

    public static final String SSMKID_SLDBJ_CODE = "16";
    public static final String SSMKID_SLDBJ_MC = "???????????????";

    public static final String SSMKID_FBWT_CODE = "17";
    public static final String SSMKID_FBWT_MC = "????????????";

    public static final String SSMKID_JSDWWT_CODE = "18";
    public static final String SSMKID_JSDWWT_MC = "??????????????????";

    public static final String SSMKID_CHDWHY_CODE = "19";
    public static final String SSMKID_CHDWHY_MC = "??????????????????";

    public static final String SSMKID_JCSJSQ_CODE = "20";
    public static final String SSMKID_JCSJSQ_MC = "??????????????????";

    public static final String SSMKID_CGCCPJ_CODE = "21";
    public static final String SSMKID_CGCCPJ_MC = "??????????????????";

    public static final String SSMKID_ZXBA_CODE = "22";
    public static final String SSMKID_ZXBA_MC = "????????????????????????";

    public static final String SSMKID_XXBA_CODE = "23";
    public static final String SSMKID_XXBA_MC = "????????????????????????";

    public static final String SSMKID_CGTJ_CODE = "24";
    public static final String SSMKID_CGTJ_MC = "????????????";

    public static final String SSMKID_YHZC_CODE = "25";
    public static final String SSMKID_YHZC_MC = "????????????";

    public static final String SSMKID_ZXKF_CODE = "26";
    public static final String SSMKID_ZXKF_MC = "????????????";

    public static final String SSMKID_SJCLPZ_CODE = "27";
    public static final String SSMKID_SJCLPZ_MC = "??????????????????";

    public static final String SSMKID_TJFX_CODE = "28";
    public static final String SSMKID_TJFX_MC = "????????????";

    // ??????
    public static final String CZLX_SAVE_CODE = "1";
    public static final String CZLX_SAVE_MC = "???????????????";
    // ??????
    public static final String CZLX_DELETE_CODE = "2";
    public static final String CZLX_DELETE_MC = "??????";
    // ??????
    public static final String CZLX_CREATE_CODE = "3";
    public static final String CZLX_CREATE_MC = "??????";
    // ??????
    public static final String CZLX_RESET_CODE = "9";
    public static final String CZLX_RESET_MC = "??????";
    //??????
    public static final String CZLX_SELECT_CODE = "4";
    public static final String CZLX_SELECT_MC = "??????";
    //??????
    public static final String CZLX_UPLOAD_CODE = "5";
    public static final String CZLX_UPLOAD_MC = "??????";
    //??????
    public static final String CZLX_DOWNLOAD_CODE = "6";
    public static final String CZLX_DOWNLOAD_MC = "??????";


    public static final String CZMK_XZSLDJ_CODE = "1001";
    public static final String CZMK_XZSLDJ_MC = "??????????????????";

    public static final String CZMK_SIGN_CODE = "1002";
    public static final String CZMK_SIGN_MC = "??????";

    public static final String CZMK_SH_CODE = "1003";
    public static final String CZMK_SH_MC = "??????";

    public static final String CZMK_CZZD_CODE = "9001";
    public static final String CZMK_CZZD_MC = "??????????????????";

    public static final String CZMK_CZAUTH_CODE = "9002";
    public static final String CZMK_CZAUTH_MC = "??????????????????";

    public static final String CZMK_BALB_CODE = "2001";
    public static final String CZMK_BALB_MC = "????????????";

    public static final String CZMK_XSWT_CODE = "2002";
    public static final String CZMK_XSWT_MC = "????????????";

    public static final String CZMK_DBAXX_CODE = "2003";
    public static final String CZMK_DBAXX_MC = "????????????";

    public static final String CZMK_FBXQ_CODE = "2004";
    public static final String CZMK_FBXQ_MC = "??????????????????";

    public static final String CZMK_BASFGQ_CODE = "2001";
    public static final String CZMK_BASFGQ_MC = "????????????????????????";

    public static final String CZMC_MLKDJ_CODE = "3001";
    public static final String CZMC_MLKDJ_MC = "???????????????";

    public static final String CZMC_MLK_LOGOUT_CODE = "3002";
    public static final String CZMC_MLK_LOGOUT_MC = "???????????????";

    public static final String CZMC_MLK_ALTER_CODE = "3003";
    public static final String CZMC_MLK_ALTER_MC = "?????????????????????";

    public static final String CZMC_CYRY_ALTER_CODE = "3004";
    public static final String CZMC_CYRY_ALTER_MC = "????????????????????????";

    public static final String CZMC_CGTJ_CODE = "4001";
    public static final String CZMC_CGTJ_MC = "????????????";

    public static final String CZMC_CGCC_CODE = "4002";
    public static final String CZMC_CGCC_MC = "????????????";

    public static final String CZMC_CGPJ_CODE = "4003";
    public static final String CZMC_CGPJ_MC = "????????????";

    public static final String CZMC_JCSJSQ_CODE = "4004";
    public static final String CZMC_JCSJSQ_MC = "??????????????????";

    public static final String CZMC_TZGG_CODE = "4005";
    public static final String CZMC_TZGG_MC = "????????????";

    public static final String CZMC_YHZC_CODE = "4006";
    public static final String CZMC_YHZC_MC = "????????????";

    public static final String CZMC_ZXKF_CODE = "4007";
    public static final String CZMC_ZXKF_MC = "????????????";

    public static final String CZMC_CGTYJFQRD_CODE = "4008";
    public static final String CZMC_CGTYJFQRD_MC = "?????????????????????????????????";

    public static final String CZMC_CGTYJFQR_CODE = "4009";
    public static final String CZMC_CGTYJFQR_MC = "????????????????????????";

    public static final String CZMC_CGTYJFQRJC_CODE = "4010";
    public static final String CZMC_CGTYJFQRJC_MC = "????????????????????????????????????";

    public static final String CZMC_SJCLPZ_CODE = "4011";
    public static final String CZMC_SJCLPZ_MC = "??????????????????";

    public static final String CZMC_HTDJBA_CODE = "4012";
    public static final String CZMC_HTDJBA_MC = "??????????????????";

    public static final String CZMC_TJFX_CODE = "4013";
    public static final String CZMC_TJFX_MC = "????????????";

    public static final String CZMC_WJSC_CODE = "4014";
    public static final String CZMC_WJSC_MC = "????????????";

    public static final String CZMC_HTWJSC_CODE = "4015";
    public static final String CZMC_HTWJSC_MC = "??????????????????";

}
