package cn.gtmap.msurveyplat.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享业务信息参数对象
 */
@ApiModel(value = "GxywxxDTO", description = "共享业务信息")
public class GxywxxDTO {
    @ApiModelProperty(value = "共享业务ID")
    private String gxywid;
    @ApiModelProperty(value = "共享业务名称")
    private String gxywmc;
    @ApiModelProperty(value = "共享业务部门ID")
    private String gxbmid;
    @ApiModelProperty(value = "共享业务部门名称")
    private String gxbmmc;
    @ApiModelProperty(value = "共享角色ID")
    private String gxjsid;
    @ApiModelProperty(value = "共享角色名称")
    private String gxjsmc;
    @ApiModelProperty(value = "共享开始时间")
    @JsonFormat( pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date gxkssj;
    @ApiModelProperty(value = "共享结束时间")
    @JsonFormat( pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date gxjssj;
    @ApiModelProperty(value = "共享内容")
    private List<String> gxnrList;
    @ApiModelProperty(value = "是否长期有效")
    private String sfcqyx;

    public String getGxywid() {
        return gxywid;
    }

    public void setGxywid(String gxywid) {
        this.gxywid = gxywid;
    }

    public String getGxywmc() {
        return gxywmc;
    }

    public void setGxywmc(String gxywmc) {
        this.gxywmc = gxywmc;
    }

    public String getGxbmid() {
        return gxbmid;
    }

    public void setGxbmid(String gxbmid) {
        this.gxbmid = gxbmid;
    }

    public String getGxbmmc() {
        return gxbmmc;
    }

    public void setGxbmmc(String gxbmmc) {
        this.gxbmmc = gxbmmc;
    }

    public String getGxjsid() {
        return gxjsid;
    }

    public void setGxjsid(String gxjsid) {
        this.gxjsid = gxjsid;
    }

    public String getGxjsmc() {
        return gxjsmc;
    }

    public void setGxjsmc(String gxjsmc) {
        this.gxjsmc = gxjsmc;
    }

    public List<String> getGxnrList() {
        return gxnrList;
    }

    public Date getGxkssj() {
        return gxkssj;
    }

    public void setGxkssj(Date gxkssj) {
        this.gxkssj = gxkssj;
    }

    public Date getGxjssj() {
        return gxjssj;
    }

    public void setGxjssj(Date gxjssj) {
        this.gxjssj = gxjssj;
    }

    public void setGxnrList(List<String> gxnrList) {
        this.gxnrList = gxnrList;
    }

    public String getSfcqyx() {
        return sfcqyx;
    }

    public void setSfcqyx(String sfcqyx) {
        this.sfcqyx = sfcqyx;
    }
}
