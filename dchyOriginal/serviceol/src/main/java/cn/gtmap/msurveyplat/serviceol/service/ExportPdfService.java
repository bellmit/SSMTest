package cn.gtmap.msurveyplat.serviceol.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExportPdfService {
    /**
     * @param chxmid
     * @return
     * @description 2021/6/8 通过chxmid受理材料明细
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    List<String> generateClmx(String chxmid);

    byte[] pdfReport(String chxmid);

    Map<String, Object> generateDataByChxmid(String chxmid);
}
