package cn.gtmap.msurveyplat.common.dto;

import java.util.List;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/21
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
public class DchyXmglJcgzCgmlDto {

    private List<DchyXmglClcgpzDto> DchyXmglClcgpzs;

    private List<DchyXmglJcgzDto> dchyXmglJcgzs;

    public List<DchyXmglClcgpzDto> getDchyXmglClcgpzs() {
        return DchyXmglClcgpzs;
    }

    public void setDchyXmglClcgpzs(List<DchyXmglClcgpzDto> DchyXmglClcgpzs) {
        this.DchyXmglClcgpzs = DchyXmglClcgpzs;
    }

    public List<DchyXmglJcgzDto> getDchyXmglJcgzs() {
        return dchyXmglJcgzs;
    }

    public void setDchyXmglJcgzs(List<DchyXmglJcgzDto> dchyXmglJcgzs) {
        this.dchyXmglJcgzs = dchyXmglJcgzs;
    }
}
