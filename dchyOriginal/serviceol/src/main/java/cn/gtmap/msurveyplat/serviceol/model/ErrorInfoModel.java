package cn.gtmap.msurveyplat.serviceol.model;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/9
 * @description 成果上传返回错误信息列表
 */
public class ErrorInfoModel {

    private String clsx;//测量事项
    private String wjmc;//文件名称
    private String msxx;//描述信息
    private String mlmc;//目录名称
    private String wjzt;//文件状态

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getWjmc() {
        return wjmc;
    }

    public void setWjmc(String wjmc) {
        this.wjmc = wjmc;
    }

    public String getMsxx() {
        return msxx;
    }

    public void setMsxx(String msxx) {
        this.msxx = msxx;
    }

    public String getMlmc() {
        return mlmc;
    }

    public void setMlmc(String mlmc) {
        this.mlmc = mlmc;
    }

    public String getWjzt() {
        return wjzt;
    }

    public void setWjzt(String wjzt) {
        this.wjzt = wjzt;
    }
}
