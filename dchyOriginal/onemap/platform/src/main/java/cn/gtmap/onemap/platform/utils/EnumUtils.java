package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.service.impl.GISServiceImpl;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 枚举辅助类
 *
 * @author: <a href="mailto:yxfacw@live.com">yingxiufeng</a>
 * @date: 2013-08-09 下午2:15
 * @version: 1.0
 */
public final class EnumUtils {

    /**
     * 面积单位
     */
    public enum UNIT {
        SQUARE(1, "平方米"),
        ACRES(0.0015, "亩"),
        HECTARE(0.0001, "公顷");

        private double ratio;
        private String alias;

        UNIT(double value, String cvalue) {
            this.ratio = value;
            this.alias = cvalue;
        }

        public double getRatio() {
            return ratio;
        }

        public String getAlias() {
            return alias;
        }
    }

    public enum TDYTQ {
        基本农田保护区("010"),
        一般农地区("020"),
        城镇建设用地区("030"),
        村镇建设用地区("040"),
        独立工矿用地区("050"),
        风景旅游用地区("060"),
        生态环境安全控制区("070"),
        自然与文化遗产保护区("080"),
        林业用地区("090"),
        牧业用地区("100"),
        其他用地区("990");

        private String lxdm;

        TDYTQ(String value) {
            this.lxdm = value;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    public enum JSYDGZQ {

        允许建设用地区("010"),
        有条件建设用地区("020"),
        限制建设用地区("030"),
        禁止建设用地区("040");

        private String lxdm;

        JSYDGZQ(String value) {
            this.lxdm = value;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    public enum JSYDGZQYJ {

        允许建设用地区("01"),
        有条件建设用地区("02"),
        限制建设用地区("03"),
        禁止建设用地区("04");

        private String lxdm;

        JSYDGZQYJ(String value) {
            this.lxdm = value;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    /**
     * 常州建设用地管制区
     */
    public enum JSYDGZQCZ {
        现状建设用地("011"),
        新增建设用地("012"),
        已批未变更建设用地("013"),
        现状交通水利及其他("014");

        private String lxdm;
        JSYDGZQCZ(String value) {
            this.lxdm = value;
        }
        public String getLxdm() {
            return lxdm;
        }
    }

    public enum GHJBNTTZ {

        保留基本农田("00"),
        调入基本农田("01"),
        调出基本农田("02");

        private String lxdm;

        GHJBNTTZ(String value) {
            this.lxdm = value;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    public enum MZZDJSXM {

        能源("01"),
        交通("02"),
        水利("03"),
        电力("04"),
        环保("05"),
        其他("99");

        private String lxdm;

        MZZDJSXM(String value) {
            this.lxdm = value;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    /***
     * 土地利用现状所有分类
     */
    public enum TDLYXZ {
        水田("011"),
        水浇地("012"),
        旱地("013"),
        果园("021"),
        茶园("022"),
        其他园地("023"),
        有林地("031"),
        灌木林地("032"),
        其他林地("033"),
        天然牧草地("041"),
        人工牧草地("042"),
        其他草地("043"),
        城市("201"),
        建制镇("202"),
        村庄("203"),
        采矿用地("204"),
        风景名胜及特殊用地("205"),
        铁路用地("101"),
        公路用地("102"),
        农村道路("104"),
        机场用地("105"),
        港口码头用地("106"),
        管道运输用地("107"),
        河流水面("111"),
        湖泊水面("112"),
        水库水面("113"),
        坑塘水面("114"),
        沿海滩涂("115"),
        内陆滩涂("116"),
        水渠("117"),
        水工建筑用地("118"),
        冰川及永久积雪("119"),
        设施农用地("122"),
        田坎("123"),
        盐碱地("124"),
        沼泽地("125"),
        沙地("126"),
        裸地("127");

        private String dlbm;

        TDLYXZ(String value) {
            this.dlbm = value;
        }

        public String getDlbm() {
            return this.dlbm;
        }
    }

    /***
     * 综合分析的分析类型
     */
    public enum MULTI_ANALYZE_TYPE {
        xz, gh, bp, gd, dj, kc, cl, sp, gyjsydfx, nt, bdc, czpcdjfx, gdzldbfxmas, xzCompare, measure
    }


    /***
     * 监测图斑分析版本，标准，马鞍山，南通
     */
    public enum JCTB_ANALYZE_METHODTYPE {
        standard, jctbmas, ntct
    }

    /***
     * 综合分析 级别 标准/吴中/马鞍山/.etc
     */
    public enum MULTI_ANALYZE_LEVEL {
        standard, jiangyin, xinyi
    }

    /***
     * 土地利用现状三大类
     */
    public enum TDLYXZ_THREE_TYPE {
        TILTH("011,012,013"),
        FARM("011,012,013,021,022,023,031,032,033,041,042,104,114,117,122,123"),
        BUILD("101,102,105,106,107,113,118,201,202,203,204,205"),
        UNUSED("111,112,115,116,119,043,124,125,126,127");

        private String[] dlbms;

        TDLYXZ_THREE_TYPE(String value) {
            this.dlbms = value.split(",");
        }

        public String[] getDlbms() {
            return dlbms;
        }

        public boolean isContained(String value) {
            List<String> dlbms = Arrays.asList(getDlbms());
            if (dlbms.contains(value)) return true;
            return false;
        }

        public String getDlbmStr() {
            return ArrayUtils.listToString(Arrays.asList(dlbms), ",");
        }
    }

    /**
     * 获取tdlyxz by dlbm
     *
     * @param value
     * @return
     */
    public static final TDLYXZ findByDlbm(String value) {

        if (value != null) {
            for (int i = 0; i < TDLYXZ.values().length; i++) {
                if (TDLYXZ.values()[i].getDlbm().equals(value))
                    return TDLYXZ.values()[i];
            }
        }
        return null;
    }

    /**
     * 综合分析-土地用途区
     */
    public enum TDYTQZHFX {
        jbntbhq("基本农田保护区", "010"),
        ybndq("一般农地区", "020"),
        csjsydq("城镇建设用地区", "030"),
        czjsydq("村镇建设用地区", "040"),
        dlgkydq("独立工矿用地区", "050"),
        fjlyydq("风景旅游用地区", "060"),
        sthjaqkzq("生态环境安全控制区", "070"),
        zrywhycbhq("自然与文化遗产保护区", "080"),
        lyydq("林业用地区", "090"),
        myydq("牧业用地区", "100"),
        qtydq("其他用地区", "990");

        private String lxmc;
        private String lxdm;

        TDYTQZHFX(String lxmc, String lxdm) {
            this.lxmc = lxmc;
            this.lxdm = lxdm;
        }

        public String getLxmc() {
            return lxmc;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    /**
     * 综合分析-建设用地管制区
     */
    public enum JSYDGZQZHFX {
        yxjsydq("允许建设用地区", "010"),
        ytjjsydq("有条件建设用地区", "020"),
        xzjsydq("限制建设用地区", "030"),
        jzjsydq("禁止建设用地区", "040");

        private String lxmc;
        private String lxdm;

        JSYDGZQZHFX(String lxmc, String lxdm) {
            this.lxmc = lxmc;
            this.lxdm = lxdm;
        }

        public String getLxmc() {
            return lxmc;
        }

        public String getLxdm() {
            return lxdm;
        }
    }

    /**
     * 土地利用现状的导出类别
     */
    public enum TDLYXZ_EXPORT_TYPE {
        all("all", "全部"),
        gy("gy", "国有"),
        jt("jt", "集体");

        private String type;
        private String name;

        TDLYXZ_EXPORT_TYPE(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 耕地地类
     */
    public enum GDDL {
        SJD("水浇地"),
        ST("水田"),
        HD("旱地");

        private String label;

        GDDL(String value) {
            label = value;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum GDDJ {
        一级("一等", "1"), 二级("二等", "2"), 三级("三等", "3"), 四级("四等", "4"), 五级("五等", "5"), 六级("六等", "6"), 七级("七等", "7"), 八级("八等", "8"), 九级("九等", "9"), 十级("十等", "10"), 其他("其他", "0");
        private String name;
        private String index;

        GDDJ(String name, String index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public String getIndex() {
            return index;
        }

        public static String getName(String index) {
            for (GDDJ c : GDDJ.values()) {
                if (StringUtils.equals(index, c.getIndex())) {
                    return c.name;
                }
            }
            return null;
        }
    }
}
