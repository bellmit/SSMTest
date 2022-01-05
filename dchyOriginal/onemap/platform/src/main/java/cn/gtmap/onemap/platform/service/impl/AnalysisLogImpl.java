package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.AnalysisLogDao;
import cn.gtmap.onemap.platform.entity.AnalysisLog;
import cn.gtmap.onemap.platform.entity.video.OperationLog;
import cn.gtmap.onemap.platform.service.AnalysisLogService;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfOrganVo;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnalysisLogImpl extends AbstractLogger<AnalysisLog> implements AnalysisLogService {

    @Autowired
    private AnalysisLogDao analysisLogDao;


    @Autowired
    private SysUserService sysUserService;


    /**
     * 保存分析log
     *
     * @param purpose
     * @return
     */
    @Override
    public boolean saveLog(String purpose, String type) {
        AnalysisLog analysisLog = new AnalysisLog();
        analysisLog.setId(UUIDGenerator.generate());
        analysisLog.setPURPOSE(purpose);
        analysisLog.setTYPE(type);
        Date date = new Date();
        analysisLog.setTIME(date);
        Calendar cale = Calendar.getInstance();
        analysisLog.setYEAR(String.valueOf(cale.get(Calendar.YEAR)));
        User user = SecHelper.getUser();
        if (user != null) {
            analysisLog.setUSERNAME(user.getViewName());
            List<PfOrganVo> pfOrganVoList = sysUserService.getOrganListByUser(user.getId());
            String pfName = "";
            if (pfOrganVoList.size() > 0) {
                pfName = pfOrganVoList.get(0).getOrganName();
            }
            analysisLog.setDEPARTMENT(pfName);
        }
        analysisLogDao.save(analysisLog);
        return true;
    }

    /**
     * 获取日志年度
     *
     * @return
     */
    @Override
    public List<Object> findLogYear() {
        return analysisLogDao.findLogYear();
    }

    /**
     * 获取日志信息（分页）
     *
     * @return
     */
    @Override
    public Page<AnalysisLog> queryAnalysisLogByParam(final Map condition, int page, int size) {

        Page<AnalysisLog> analysisLogs = analysisLogDao.findAll(getSpecification(condition), new PageRequest(page - 1, size, new Sort(Sort.Direction.DESC, "TIME")));

        return analysisLogs;
    }

    /**
     * 获取日志信息
     *
     * @param condition
     * @return
     */
    @Override
    public List<AnalysisLog> queryAnalysisList(final Map condition){
        List<AnalysisLog> analysisLogList=analysisLogDao.findAll(getSpecification(condition), new Sort(Sort.Direction.DESC, "TIME"));
        return analysisLogList;
    }

    /**
     * 组织excel导出日志
     *
     * @param loggers
     * @return
     */
    @Override
    public Workbook getExportExcel(List<AnalysisLog> loggers) {
        Workbook wb = new HSSFWorkbook();
        String[] excelHeader = {"用户名", "部门", "分析类型", "分析时间", "分析目的"};
        int[] excelHeaderWidth ={150, 300, 200, 300};
        Sheet sheet =wb.createSheet("分析操作日志");
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
            AnalysisLog analysisLog = loggers.get(i);
            row.createCell(0).setCellValue(analysisLog.getUSERNAME() != null ? analysisLog.getUSERNAME() : "");
            row.createCell(1).setCellValue(analysisLog.getDEPARTMENT() != null ? analysisLog.getDEPARTMENT() : "");
            row.createCell(2).setCellValue(analysisLog.getTYPE() != null ? analysisLog.getTYPE() : "");
            row.createCell(3).setCellValue(analysisLog.getTIME() != null ? format.format(analysisLog.getTIME()) : "");
            row.createCell(2).setCellValue(analysisLog.getPURPOSE() != null ? analysisLog.getPURPOSE() : "");
        }

        return wb;
    }


    /**
     * 组织查询语句
     *
     * @param condition
     * @return
     */
    Specification getSpecification(final Map condition) {
        return new Specification<AnalysisLog>() {
            @Override
            public Predicate toPredicate(Root<AnalysisLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                Predicate predicate = criteriaBuilder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if (condition.containsKey("TYPE") && isNotNull(condition.get("TYPE"))) {
                    expressions.add(criteriaBuilder.like(root.<String>get("TYPE"), "%" + condition.get("TYPE").toString() + "%"));
                }
                if (condition.containsKey("YEAR") && isNotNull(condition.get("YEAR"))) {
                    expressions.add(criteriaBuilder.like(root.<String>get("YEAR"), "%" + condition.get("YEAR").toString() + "%"));
                }
                if (condition.containsKey("USERNAME") && isNotNull(condition.get("USERNAME"))) {
                    expressions.add(criteriaBuilder.like(root.<String>get("USERNAME"), "%" + condition.get("USERNAME").toString() + "%"));
                }
                if (condition.get("startDate") != null && !StringUtils.isEmpty(condition.get("startDate").toString())) {
                    try {
                        expressions.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("TIME"), dateFormat.parse(condition.get("startDate").toString())));
                    } catch (ParseException e) {
                        logger.error("错误：" + e);
                    }
                }

                if (condition.get("endDate") != null && !StringUtils.isEmpty(condition.get("endDate").toString())) {
                    try {
                        expressions.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("TIME"), dateFormat.parse(condition.get("endDate").toString())));
                    } catch (ParseException e) {
                        logger.error("错误：" + e);
                    }
                }
                return predicate;
            }
        };
    }
}
