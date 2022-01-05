package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglLcgpz;

import java.util.List;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/21
 * @desc DchyXmglLcgpzDto: DchyXmglLcgpzDto
 */
public class DchyXmglLcgpzDto extends DchyXmglLcgpz {

    private List<DchyXmglLcgpzDto> children;

    public List<DchyXmglLcgpzDto> getChildren() {
        return children;
    }

    public void setChildren(List<DchyXmglLcgpzDto> children) {
        this.children = children;
    }

}
