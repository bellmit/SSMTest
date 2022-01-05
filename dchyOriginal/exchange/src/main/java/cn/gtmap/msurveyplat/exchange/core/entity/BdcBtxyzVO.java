package cn.gtmap.msurveyplat.exchange.core.entity;

import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description 必填项验证VO
 */
public class BdcBtxyzVO {
    /**
     * 表单名称
     */
    private String bdmc;
    /**
     * 验证信息
     */
    private List<String> yzxx;

    public String getBdmc() {
        return bdmc;
    }

    public void setBdmc(String bdmc) {
        this.bdmc = bdmc;
    }

    public List<String> getYzxx() {
        return yzxx;
    }

    public void setYzxx(List<String> yzxx) {
        this.yzxx = yzxx;
    }

    @Override
    public String toString() {
        return "BdcBtxyzVO{" +
                "bdmc='" + bdmc + '\'' +
                ", yzxx=" + yzxx +
                '}';
    }
}
