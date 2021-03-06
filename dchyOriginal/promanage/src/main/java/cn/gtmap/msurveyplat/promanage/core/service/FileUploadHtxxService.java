package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
public interface FileUploadHtxxService {

    //现场受理登记上传保存合同信息
    boolean saveHtxx(DchyXmglHtxxDto dchyXmglHtxxDto);

    //更新测绘项目测量事项
    Map<String,Object> saveClsxByChxmid(Map<String, Object> map);

    //更新测绘项目测量事项
    Map<String,Object> saveChdwxxByChxmid(Map<String, Object> map);

    //删除合同相关信息
    Map<String,Object> deleteFileHtxxJl(Map<String,Object> paramMap);


}
