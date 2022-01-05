package cn.gtmap.msurveyplat.common.dto;

import java.util.List;

/**
  * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
  * @description 分页查询参数
  * @time 2021/5/8 11:20
  */
public class UserAuthParamDto {

    private List<String> useridList;

    public List<String> getUseridList() {
        return useridList;
    }

    public void setUseridList(List<String> useridList) {
        this.useridList = useridList;
    }
}
