package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/21
 * @description TODO
 */
public class DchyXmglTjfxDto {

    @ApiModelProperty(value = "key值")
    private String key;

    @ApiModelProperty(value = "开始时间")
    private String kssj;

    @ApiModelProperty(value = "结束时间")
    private String jssj;

    @ApiModelProperty(value = "按阶段统计开始时间")
    private String wtkssj;

    @ApiModelProperty(value = "按阶段统计结束时间")
    private String wtjssj;

    @ApiModelProperty(value = "委托年份")
    private String year;

    @ApiModelProperty(value = "委托月份")
    private String month;

    @ApiModelProperty(value = "委托季度")
    private String quarter;

    @ApiModelProperty(value = "建设单位名称")
    private String jsdwmc;

    @ApiModelProperty(value = "测绘单位名称")
    private String chdwmc;

    @ApiModelProperty(value = "项目状态")
    private String xmzt;

    @ApiModelProperty(value = "委托状态")
    private String wtzt;

    @ApiModelProperty(value = "根据区县统计项目数量")
    private List<Map<String, Object>> xmslbyqxList;

    @ApiModelProperty(value = "溧阳市统计项目数量")
    private List<Map<String, Object>> xmslbylysList;

    @ApiModelProperty(value = "根据年份统计项目数量")
    private List<Map<String, Object>> xmslbyyearList;

    @ApiModelProperty(value = "根据月份统计项目数量")
    private List<Map<String, Object>> xmslbymouthList;

    @ApiModelProperty(value = "根据委托状态统计项目数量")
    private List<Map<String, Object>> xmslbywtztList;

    @ApiModelProperty(value = "委托记录分页数据")
    private DchyXmglTjgxFyDto dchyXmglTjgxFyDto;

    @ApiModelProperty(value = "根据区县和测绘阶段统计项目数量")
    private List<Map<String, Object>> xmslbyqxmcandchjdList;

    @ApiModelProperty(value = "根据测绘阶段统计项目数量")
    private List<Map<String, Object>> xmslbychjdList;

    @ApiModelProperty(value = "建设单位列表")
    private List<Map<String, Object>> jsdwList;

    @ApiModelProperty(value = "测绘单位列表")
    private List<Map<String, Object>> chdwList;

    @ApiModelProperty(value = "委托记录导出记录")
    private List<Map<String, Object>> exportwtjlList;

    @ApiModelProperty(value = "导出标识")
    private String exportflag;

    @ApiModelProperty(value = "分页标识")
    private String pageflag;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getJsdwmc() {
        return jsdwmc;
    }

    public void setJsdwmc(String jsdwmc) {
        this.jsdwmc = jsdwmc;
    }

    public String getChdwmc() {
        return chdwmc;
    }

    public void setChdwmc(String chdwmc) {
        this.chdwmc = chdwmc;
    }

    public String getWtkssj() {
        return wtkssj;
    }

    public void setWtkssj(String wtkssj) {
        this.wtkssj = wtkssj;
    }

    public String getWtjssj() {
        return wtjssj;
    }

    public void setWtjssj(String wtjssj) {
        this.wtjssj = wtjssj;
    }

    public List<Map<String, Object>> getXmslbyqxList() {
        return xmslbyqxList;
    }

    public void setXmslbyqxList(List<Map<String, Object>> xmslbyqxList) {
        this.xmslbyqxList = xmslbyqxList;
    }

    public List<Map<String, Object>> getXmslbyyearList() {
        return xmslbyyearList;
    }

    public void setXmslbyyearList(List<Map<String, Object>> xmslbyyearList) {
        this.xmslbyyearList = xmslbyyearList;
    }

    public List<Map<String, Object>> getXmslbymouthList() {
        return xmslbymouthList;
    }

    public void setXmslbymouthList(List<Map<String, Object>> xmslbymouthList) {
        this.xmslbymouthList = xmslbymouthList;
    }

    public List<Map<String, Object>> getXmslbywtztList() {
        return xmslbywtztList;
    }

    public void setXmslbywtztList(List<Map<String, Object>> xmslbywtztList) {
        this.xmslbywtztList = xmslbywtztList;
    }

    public DchyXmglTjgxFyDto getDchyXmglTjgxFyDto() {
        return dchyXmglTjgxFyDto;
    }

    public void setDchyXmglTjgxFyDto(DchyXmglTjgxFyDto dchyXmglTjgxFyDto) {
        this.dchyXmglTjgxFyDto = dchyXmglTjgxFyDto;
    }

    public String getXmzt() {
        return xmzt;
    }

    public void setXmzt(String xmzt) {
        this.xmzt = xmzt;
    }

    public List<Map<String, Object>> getXmslbyqxmcandchjdList() {
        return xmslbyqxmcandchjdList;
    }

    public void setXmslbyqxmcandchjdList(List<Map<String, Object>> xmslbyqxmcandchjdList) {
        this.xmslbyqxmcandchjdList = xmslbyqxmcandchjdList;
    }

    public List<Map<String, Object>> getXmslbychjdList() {
        return xmslbychjdList;
    }

    public void setXmslbychjdList(List<Map<String, Object>> xmslbychjdList) {
        this.xmslbychjdList = xmslbychjdList;
    }

    public List<Map<String, Object>> getJsdwList() {
        return jsdwList;
    }

    public void setJsdwList(List<Map<String, Object>> jsdwList) {
        this.jsdwList = jsdwList;
    }

    public List<Map<String, Object>> getChdwList() {
        return chdwList;
    }

    public void setChdwList(List<Map<String, Object>> chdwList) {
        this.chdwList = chdwList;
    }

    public List<Map<String, Object>> getExportwtjlList() {
        return exportwtjlList;
    }

    public void setExportwtjlList(List<Map<String, Object>> exportwtjlList) {
        this.exportwtjlList = exportwtjlList;
    }

    public String getExportflag() {
        return exportflag;
    }

    public void setExportflag(String exportflag) {
        this.exportflag = exportflag;
    }

    public String getWtzt() {
        return wtzt;
    }

    public void setWtzt(String wtzt) {
        this.wtzt = wtzt;
    }

    public String getPageflag() {
        return pageflag;
    }

    public void setPageflag(String pageflag) {
        this.pageflag = pageflag;
    }

    public List<Map<String, Object>> getXmslbylysList() {
        return xmslbylysList;
    }

    public void setXmslbylysList(List<Map<String, Object>> xmslbylysList) {
        this.xmslbylysList = xmslbylysList;
    }
}

