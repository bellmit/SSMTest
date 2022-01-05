package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcgpz;

import java.util.List;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/21
 * @desc DchyXmglLcgpzDto: DchyXmglLcgpzDto
 */
public class DchyXmglClcgpzDto extends DchyXmglClcgpz {

    private List<DchyXmglClcgpzDto> children;

    public List<DchyXmglClcgpzDto> getChildren() {
        return children;
    }

    public void setChildren(List<DchyXmglClcgpzDto> children) {
        this.children = children;
    }

}
