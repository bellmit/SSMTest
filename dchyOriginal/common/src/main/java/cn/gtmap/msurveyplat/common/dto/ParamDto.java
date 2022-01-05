package cn.gtmap.msurveyplat.common.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/16
 * @description TODO
 */
public class ParamDto {

    @JSONField(ordinal=1)
    private String page;

    @JSONField(ordinal=2)
    private String size;

    @JSONField(ordinal=3)
    private String title;

    @JSONField(ordinal=4)
    private String sfzx;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSfzx() {
        return sfzx;
    }

    public void setSfzx(String sfzx) {
        this.sfzx = sfzx;
    }
}
