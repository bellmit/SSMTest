package cn.gtmap.msurveyplat.promanage.model;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-10
 * description
 */
public class DchyXmglSjclDto {
    String glsxid;

    @DescribeAno("测绘项目收件材料")
    DchyXmglSjcl dchyXmglSjcl;

    public DchyXmglSjcl getDchyXmglSjcl() {
        return dchyXmglSjcl;
    }

    public void setDchyXmglSjcl(DchyXmglSjcl dchyXmglSjcl) {
        this.dchyXmglSjcl = dchyXmglSjcl;
    }

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }
}
