package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
public interface CheckZipFileService {

    List<ErrorInfoModel> checkZipFiles(byte[] zipBytes, String bh, Map<String, Object> mlMap, String babh, String gcbh, String chxmid, String glsxid) throws IOException;

    String getCode();

}
