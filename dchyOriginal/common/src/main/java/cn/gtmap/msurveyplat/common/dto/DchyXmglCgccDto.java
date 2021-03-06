package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCgcc;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/3/15 19:20
 * @description 成果抽查
 */
public class DchyXmglCgccDto {

    String glsxid;

    @DescribeAno("成查抽查")
    DchyXmglCgcc dchyXmglCgcc;

    List<Map<String, Object>> cgccPjclList;

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public DchyXmglCgcc getDchyXmglCgcc() {
        return dchyXmglCgcc;
    }

    public void setDchyXmglCgcc(DchyXmglCgcc dchyXmglCgcc) {
        this.dchyXmglCgcc = dchyXmglCgcc;
    }

    public List<Map<String, Object>> getCgccPjclList() {
        return cgccPjclList;
    }

    public void setCgccPjclList(List<Map<String, Object>> cgccPjclList) {
        this.cgccPjclList = cgccPjclList;
    }
}
