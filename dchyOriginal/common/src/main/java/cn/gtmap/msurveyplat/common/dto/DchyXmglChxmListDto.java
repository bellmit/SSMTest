package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-10
 * description
 */
public class DchyXmglChxmListDto {

    @DescribeAno("备案项目信息")
    List<DchyXmglChxmDto> dchyXmglChxmListDto;

    public List<DchyXmglChxmDto> getDchyXmglChxmListDto() {
        return dchyXmglChxmListDto;
    }

    public void setDchyXmglChxmListDto(List<DchyXmglChxmDto> dchyXmglChxmListDto) {
        this.dchyXmglChxmListDto = dchyXmglChxmListDto;
    }
}
