package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.AnalysisLog;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


public interface AnalysisLogService {
    /**
     * 保存分析log
     *
     * @param purpose
     * @return
     */
    boolean saveLog(String purpose, String type);


    List<Object> findLogYear();

    /**
     * 获取日志信息（分页）
     *
     * @param condition
     * @param page
     * @param size
     * @return
     */

    Page<AnalysisLog> queryAnalysisLogByParam(final Map condition, int page, int size);

    /**
     * 获取日志信息
     *
     * @param condition
     * @return
     */
    List<AnalysisLog> queryAnalysisList(final Map condition);

    /**
     * 组织excel导出日志
     *
     * @param loggers
     * @return
     */
    Workbook getExportExcel(List<AnalysisLog> loggers);
}
