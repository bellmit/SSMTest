package cn.gtmap.msurveyplat.exchange.core.entity;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description
 */
public class BdcGzyzVO {
    /**
     * 验证类型 alert confirm
     */
    private Integer yzlx;
    /**
     * 提示信息
     */
    private String tsxx;


    public String getTsxx() {
        return tsxx;
    }

    public void setTsxx(String tsxx) {
        this.tsxx = tsxx;
    }

    public Integer getYzlx() {
        return yzlx;
    }

    public void setYzlx(Integer yzlx) {
        this.yzlx = yzlx;
    }

    @Override
    public String toString() {
        return "BdcGzyzVO{" +
                "yzlx=" + yzlx +
                ", tsxx='" + tsxx + '\'' +
                '}';
    }
}
