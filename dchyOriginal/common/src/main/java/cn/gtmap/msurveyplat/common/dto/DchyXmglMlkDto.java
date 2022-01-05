package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;

import java.util.List;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/4/9 17:23
 * @description
 */
public class DchyXmglMlkDto {

    private String glsxid;//关联事项id

    private String czlx;   //推送的操作类型(新增或者删除)

    @DescribeAno("名录库")
    List<DchyXmglMlk> dchyXmglMlkList;

    @DescribeAno("从业人员")
    List<DchyXmglCyry> dchyXmglCyryList;

    @DescribeAno("多测合一收件材料")
    private List<DchyXmglSjcl> dchyXmglSjclList;

    @DescribeAno("多测合一收件信息")
    private List<DchyXmglSjxx> dchyXmglSjxxList;

    @DescribeAno("测量事项")
    List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxList;

    @DescribeAno("多测合一考评表")
    private List<DchyXmglKp> dchyXmglKpList;

    private String mlkTp;

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public List<DchyXmglMlkClsxGx> getDchyXmglMlkClsxGxList() {
        return dchyXmglMlkClsxGxList;
    }

    public void setDchyXmglMlkClsxGxList(List<DchyXmglMlkClsxGx> dchyXmglMlkClsxGxList) {
        this.dchyXmglMlkClsxGxList = dchyXmglMlkClsxGxList;
    }

    public List<DchyXmglCyry> getDchyXmglCyryList() {
        return dchyXmglCyryList;
    }

    public void setDchyXmglCyryList(List<DchyXmglCyry> dchyXmglCyryList) {
        this.dchyXmglCyryList = dchyXmglCyryList;
    }

    public List<DchyXmglMlk> getDchyXmglMlkList() {
        return dchyXmglMlkList;
    }

    public void setDchyXmglMlkList(List<DchyXmglMlk> dchyXmglMlkList) {
        this.dchyXmglMlkList = dchyXmglMlkList;
    }

    public List<DchyXmglSjcl> getDchyXmglSjclList() {
        return dchyXmglSjclList;
    }

    public void setDchyXmglSjclList(List<DchyXmglSjcl> dchyXmglSjclList) {
        this.dchyXmglSjclList = dchyXmglSjclList;
    }

    public List<DchyXmglSjxx> getDchyXmglSjxxList() {
        return dchyXmglSjxxList;
    }

    public void setDchyXmglSjxxList(List<DchyXmglSjxx> dchyXmglSjxxList) {
        this.dchyXmglSjxxList = dchyXmglSjxxList;
    }

    public String getCzlx() {
        return czlx;
    }

    public void setCzlx(String czlx) {
        this.czlx = czlx;
    }

    public List<DchyXmglKp> getDchyXmglKpList() {
        return dchyXmglKpList;
    }

    public void setDchyXmglKpList(List<DchyXmglKp> dchyXmglKpList) {
        this.dchyXmglKpList = dchyXmglKpList;
    }

    public String getMlkTp() {
        return mlkTp;
    }

    public void setMlkTp(String mlkTp) {
        this.mlkTp = mlkTp;
    }
}
