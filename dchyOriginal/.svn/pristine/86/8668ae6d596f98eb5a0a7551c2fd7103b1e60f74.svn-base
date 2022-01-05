package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.OperationLogDao;
import cn.gtmap.onemap.platform.entity.video.CameraLog;
import cn.gtmap.onemap.platform.entity.video.OperationLog;
import cn.gtmap.onemap.platform.service.OperateLoggerService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 系统操作日志接口实现
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-18 10.19:00
 */
@Service
public class OperateLoggerServiceImpl extends AbstractLogger<OperationLog> implements OperateLoggerService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public OperationLog save(OperationLog logger) {
        return operationLogDao.save(logger);
    }

    /**
     * 分页查询符合条件的日志信息
     * @param condition
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<OperationLog> search(final Map condition, int page, int size) {
        Page<OperationLog> operationLogs = null;
        operationLogs = operationLogDao.findAll(getSpecification(condition),
                new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createAt")));

        return operationLogs;
    }

    /**
     * 按条件获取不分页的日志信息
     * @param condition
     * @return
     */
    @Override
    public List<OperationLog> export(final Map condition) {
        List<OperationLog> operationLogs = null;
        operationLogs = operationLogDao.findAll(getSpecification(condition),
                new Sort(Sort.Direction.DESC, "createAt"));
        return operationLogs;
    }

    @Override
    public Workbook getExportExcel(List<OperationLog> loggers) {
        Workbook wb = new HSSFWorkbook();
        String[] excelHeader = {"用户名", "项目名称", "操作内容", "操作时间"};
        int[] excelHeaderWidth ={150, 300, 200, 300};
        Sheet sheet =wb.createSheet("项目操作系统日志");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        //设置居中样式
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
        row.setHeight((short)400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short)500);
        font.setFontHeightInPoints((short)11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //设置列宽
        for (int i = 0; i <excelHeaderWidth.length; i++){
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        //添加表格头
        for (int i = 0;i <excelHeader.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }

        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E");
        for (int i = 0; i < loggers.size(); i++){
            row = sheet.createRow(i + 1);
            OperationLog operationLog = loggers.get(i);
            row.createCell(0).setCellValue(operationLog.getUserName() != null ? operationLog.getUserName() : "");
            row.createCell(1).setCellValue(operationLog.getProName() != null ? operationLog.getProName() : "");
            row.createCell(2).setCellValue(operationLog.getOptContent() != null ? operationLog.getOptContent() : "");
            row.createCell(3).setCellValue(operationLog.getCreateAt() != null ? format.format(operationLog.getCreateAt()) : "");
        }

        return wb;
    }

    @Override
    public Workbook getExportStatisticExcel(Map map,String title) {
        return null;
    }

    @Override
    public boolean remove(String id) {
        try {
            operationLogDao.delete(id);
        } catch (Exception e) {
            logger.error(getMessage("delete.logger.error", e.toString()));
            return false;
        }
        return true;
    }

    @Override
    public OperationLog getById(String id) {
        return operationLogDao.findOne(id);
    }

    @Override
    public Workbook getAlLExportAllStatisticExcel(List<Map> results){
        return null;
    }


    @Override
    public List getCameraLogUserDeptByUserId(List<CameraLog> list, int nThreads) {
        return null;
    }

}
