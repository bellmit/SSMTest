package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxx;

import java.util.List;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/12
 * @description TODO
 */
public class DchyXmglXxtxDto {

    private List<DchyXmglYhxx> dchyXmglYhxxList;

    public List<DchyXmglYhxx> getDchyXmglYhxxList() {
        return dchyXmglYhxxList;
    }

    public void setDchyXmglYhxxList(List<DchyXmglYhxx> dchyXmglYhxxList) {
        this.dchyXmglYhxxList = dchyXmglYhxxList;
    }
}
