package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraLogDao;
import cn.gtmap.onemap.platform.dao.FileCacheDao;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraLog;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.CameraLoggerService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.security.IdentityService;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfOrganVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * . 探头操作日志信息管理业务实现
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-03-23 19:47:00
 */
@Service
public class CameraLoggerServiceImpl extends AbstractLogger<CameraLog> implements CameraLoggerService {

    @Autowired
    private CameraLogDao cameraLogDao;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private VideoManager videoManager;

    @Autowired
    private SysUserService sysUserService;
    /**
     * oms获取用户接口service接口
     */
    @Autowired
    private IdentityService identityService;

    private Map regionDic;

    @Autowired
    private FileCacheDao fileCacheDao;

    @Override
    public CameraLog save(CameraLog cameraLog) {
        if (cameraLogDao.queryRecent(cameraLog.getCameraId(), cameraLog.getUserId(), new Date()).size() > 0) {
            return null;
        }
        cameraLog.setEnabled(true);
        cameraLog.setCreateAt(new Date());
        if (isNull(cameraLog.getYear())) {
            cameraLog.setYear(DateTime.now().year().getAsShortText());
        }
        System.out.println(cameraLog.getUserId());
        if(cameraLog != null && StringUtils.isBlank(cameraLog.getUserDept()) && StringUtils.isNotBlank(cameraLog.getUserId())){
            String uesrId = cameraLog.getUserId();
            if (StringUtils.isNotBlank(uesrId)){
                List<PfOrganVo> pfOrganVoList= sysUserService.getOrganListByUser(uesrId);
                if (pfOrganVoList != null && !pfOrganVoList.isEmpty()){
                    PfOrganVo pfOrganVo = pfOrganVoList.get(0);
                    cameraLog.setUserDept(pfOrganVo.getOrganName());
                }else {
                    cameraLog.setUserDept("");
                }
            }
        }
        return cameraLogDao.save(cameraLog);
    }


    /**
     * 存储摄像头操做的具体日志
     *
     * @param cameraId
     * @param uesrId
     * @param operation
     * @return
     */
    @Transactional
    @Override
    public CameraLog saveOptLog(String cameraId, String uesrId, int operation,String token) {
        try {
           CameraLog cameraLog= cameraLogDao.findFirstByToken(token);
            if(cameraLog != null && StringUtils.isBlank(cameraLog.getUserDept())){
                if (StringUtils.isNotBlank(uesrId)){
                    List<PfOrganVo> pfOrganVoList= sysUserService.getOrganListByUser(uesrId);
                    if (pfOrganVoList != null && !pfOrganVoList.isEmpty()){
                        PfOrganVo pfOrganVo = pfOrganVoList.get(0);
                        cameraLog.setUserDept(pfOrganVo.getOrganName());
                    }else {
                        cameraLog.setUserDept("");
                    }
                }
            }
            synchronized (this) {
                Date now = new Date();
                if (Constant.VideoOptType.OPEN.getType() == operation
                        || Constant.VideoOptType.CAPTURE.getType() == operation) {  //打开操作或者抓图操作
                    cameraLog= createNewLog(cameraId,uesrId,operation,token);

                    if (Constant.VideoOptType.OPEN.getType() == operation) {
                        cameraLog.setOptContent("查看");
                        cameraLog.setStartTime(now);
                    } else if (Constant.VideoOptType.CAPTURE.getType() == operation) {
                        cameraLog.setOptContent("抓图");
                    }
                    cameraLog.setYear(String.valueOf(new DateTime(now).getYear()));

                } else if (Constant.VideoOptType.CLOSE.getType() == operation    //关闭或者超时未操作强制关闭的
                        || Constant.VideoOptType.END.getType() == operation) {
                    cameraLog.setEndTime(new Date());
                }else if(Constant.VideoOptType.HEARTBEAT.getType()==operation){
                    //心跳监测
                    if(cameraLog==null){
                        cameraLog = createNewLog(cameraId,uesrId,operation,token);
                    }
                    //执行记录心跳检测
                    if(cameraLog.getStartTime()==null){
                        cameraLog.setStartTime(new Date());
                    }
                    cameraLog.setEndTime(new Date());
                }
                if (isNotNull(cameraLog)) {
                    return cameraLogDao.save(cameraLog);
                } else {
                    return null;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CameraLog createNewLog(String cameraId,String uesrId,int operation,String token){
        CameraLog cameraLog = new CameraLog();
        Date now = new Date();
        cameraLog.setCreateAt(now);
        cameraLog.setCameraId(cameraId);
        cameraLog.setCameraName(videoMetadataService.getByIndexCode(cameraId).getName());
        cameraLog.setUserId(uesrId);
        cameraLog.setOptType(operation);
        cameraLog.setToken(token);
        if (isNotNull(identityService.getUser(uesrId))) {
            cameraLog.setUserName(identityService.getUser(uesrId).getViewName());
        } else {
            cameraLog.setUserName("匿名用户");
        }
        cameraLog.setYear(String.valueOf(new DateTime(now).getYear()));
        return cameraLog;
    }

    @Transactional
    @Override
    public void saveCaptureLog(String cameraId, String optContent, String deviceInfo) {
        try {
            synchronized (this) {
                CameraLog cameraLog = new CameraLog();
                cameraLog = new CameraLog();
                Date now = new Date();

                String camaraName = videoMetadataService.getByIndexCode(cameraId).getName();
                cameraLog.setCreateAt(now);
                cameraLog.setCameraId(cameraId);
                cameraLog.setCameraName(camaraName);
                cameraLog.setUserId("");
                cameraLog.setUserName("系统定时任务");
//                cameraLog.setOptContent("探头【" + camaraName +"】于"+ DateFormatUtils.format(now, "yyyy年MM月dd天 HH时mm分ss秒") +"对项目点【" + proName + "】拍照");
                cameraLog.setOptContent("请求探头【" + camaraName + "】URL地址：" + optContent + "...");
                cameraLog.setYear(String.valueOf(new DateTime(now).getYear()));
                cameraLog.setStartTime(now);
                cameraLog.setRemark(deviceInfo);
                cameraLogDao.save(cameraLog);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return;
    }

    /**
     * 查询符合条件的所有日志信息
     *
     * @param condition
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<CameraLog> search(final Map condition, int page, int size) {
        Page<CameraLog> cameraLogs = null;
        DateTime start = null, end = null;
        cameraLogs = cameraLogDao.findAll(getSpecification(condition),
                new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createAt")));
        buildFieldsInCameraLog(cameraLogs.getContent());
        return cameraLogs;
    }

    /**
     * 不分页获取日志
     *
     * @param condition
     * @return
     */
    @Override
    public List<CameraLog> export(final Map condition) {
        List<CameraLog> cameraLogs = null;
        cameraLogs = cameraLogDao.findAll(getSpecification(condition),
                new Sort(Sort.Direction.DESC, "createAt"));
        return buildFieldsInCameraLog(cameraLogs);
    }

    /**
     * 导出日志
     *
     * @param cameraLogs
     * @return
     */
    @Override
    public Workbook getExportExcel(List<CameraLog> cameraLogs) {
        String[] excelHeader = {"部门", "用户名", "摄像头名称", "设备id", "操作内容", "操作时间"};
        int[] excelHeaderWidth = {200,100, 200, 200, 100, 370};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("监控点日志信息");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置列宽度（像素）
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E");
        for (int i = 0; i < cameraLogs.size(); i++) {
            row = sheet.createRow(i + 1);
            CameraLog cameraLog = cameraLogs.get(i);
            row.createCell(0).setCellValue(cameraLog.getUserDept() != null ? cameraLog.getUserDept() : "");
            row.createCell(1).setCellValue(cameraLog.getUserName() != null ? cameraLog.getUserName() : "");
            row.createCell(2).setCellValue(cameraLog.getCameraName() != null ? cameraLog.getCameraName() : "");
            row.createCell(3).setCellValue(cameraLog.getCameraId() != null ? cameraLog.getCameraId() : "");
            row.createCell(4).setCellValue(cameraLog.getOptContent() != null ? cameraLog.getOptContent() : "");
            row.createCell(5).setCellValue(cameraLog.getCreateAt() != null ? format.format(cameraLog.getCreateAt()) : "");
        }
        return wb;
    }


    @Override
    public Workbook getExportStatisticExcel(Map map, String title) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("监控点统计信息");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        sheet.setColumnWidth(0, 32 * 250);
        sheet.setColumnWidth(1, 32 * 150);


        List<Map> indicator = (List<Map>) map.get("indicator");
        Map data = (Map) ((List) map.get("data")).get(0);
        String name = MapUtils.getString(data, "name");
        List sum = (List) data.get("value");
        Cell cell = row.createCell(0);
        cell.setCellValue(name);
        cell.setCellStyle(style);

        for (int i = 0; i < sum.size(); i++) {
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(indicator.get(i).get("text").toString());
            row.createCell(1).setCellValue(sum.get(i).toString().concat("次"));
        }
        return wb;
    }


    @Override
    public Workbook getAlLExportAllStatisticExcel(List<Map> results) {
        String[] excelHeader = {"区镇", "使用人", "摄像头名称", "使用次数", "合计"};
        int[] excelHeaderWidth = {200, 200, 200, 200, 200};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("监控点统计信息");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // 设置列宽度（像素）
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }

        String userName = "";
        Row lastRow = null;
        Map lastMap = new HashMap();
        for (int i = 0; i < results.size(); i++) {
            row = sheet.createRow(i + 1);
            Map map = results.get(i);
            if (!MapUtils.getString(map, "userName", "").equals(userName)) {
                userName = MapUtils.getString(map, "userName", "");
                if (!lastMap.isEmpty()) {
                    lastRow.createCell(4).setCellValue(MapUtils.getString(lastMap, "total"));
                }
            }
            lastMap = map;
            lastRow = row;
            row.createCell(0).setCellValue(MapUtils.getString(map, "organName"));
            row.createCell(1).setCellValue(MapUtils.getString(map, "userName"));
            row.createCell(2).setCellValue(MapUtils.getString(map, "cameraName"));
            row.createCell(3).setCellValue(MapUtils.getString(map, "sum"));

        }
        lastRow.createCell(4).setCellValue(MapUtils.getString(lastMap, "total"));
        return wb;
    }

    @Override
    public Workbook getAllExportGroupByOrgan(Map data){
        logger.info("开始执行导出excel");
        logger.info(JSON.toJSONString(data));
        Workbook wb = new HSSFWorkbook();
        try {
            //导出excel
            String[] excelHeader = {"序号", "行政区", "使用总次数", "监控点总个数", "平均使用次数","未使用次数","备注"};
            int[] excelHeaderWidth = {100, 200, 200, 200,200,200,200};
            Sheet sheet = wb.createSheet("监控探头使用频率表");
            Row row = sheet.createRow((int) 1);
            Row row1 = sheet.createRow((int)0);
            CellStyle style = wb.createCellStyle();
            // 设置居中样式
            style.setAlignment(CellStyle.ALIGN_CENTER); // 水平居左
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
            row.setHeight((short) 400);
            row1.setHeight((short) 500);
            Font font = wb.createFont();
            font.setFontName("宋体");
            font.setBoldweight((short)20);
            font.setFontHeightInPoints((short) 14);
            style.setFont(font);
            //设置标题样式
            CellStyle titleStyle = wb.createCellStyle();
            Font fontTitle = wb.createFont();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
            fontTitle.setFontName("宋体");
            fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            fontTitle.setFontHeightInPoints((short) 20);
            titleStyle.setFont(fontTitle);

            // 设置列宽度（像素）
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
            }
            // 添加表格头
            for (int i = 0; i < excelHeader.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(excelHeader[i]);
                cell.setCellStyle(style);
                sheet.setDefaultColumnStyle(i,style);
            }
            double useCountAll=0;
            double cameraCountAll=0;
            double noUseCountAll =0;
            //设置表头
            sheet.addMergedRegion((new CellRangeAddress(0, 0, 0, 7)));
            Cell titleCell = row1.createCell(0);
            titleCell.setCellValue("\"慧眼守土\"监控探头使用频率表");
            titleCell.setCellStyle(titleStyle);
            int index=2;
            for(Object key:data.keySet()){
                row = sheet.createRow(index);
                String itemKey = (String) key;
                Map itemValue =(HashMap)data.get(key);
                row.createCell(0).setCellValue(index-1);
                row.createCell(1).setCellValue(itemKey);
                row.createCell(2).setCellValue(getFieledStr(itemValue,"useCount"));
                row.createCell(3).setCellValue(getFieledStr(itemValue,"cameraCount"));
                row.createCell(4).setCellValue(getFieledStr(itemValue,"useFre"));
                row.createCell(5).setCellValue(getFieledStr(itemValue,"noUseCount"));
                row.createCell(6).setCellValue(getFieledStr(itemValue,"bz"));
                useCountAll = useCountAll+getFieledDouble(itemValue,"useCount");
                cameraCountAll = cameraCountAll+getFieledDouble(itemValue,"cameraCount");
                noUseCountAll = noUseCountAll+getFieledDouble(itemValue,"noUseCount");
                index++;
            }
            sheet.addMergedRegion((new CellRangeAddress(index, index, 0, 1)));
            row = sheet.createRow(index);
            Cell cell1= row.createCell(0);
            cell1.setCellValue("合并");
            Cell cell2= row.createCell(2);
            cell2.setCellValue(useCountAll);
            Cell cell3= row.createCell(3);
            cell3.setCellValue(cameraCountAll);
            Cell cell5= row.createCell(5);
            cell5.setCellValue(noUseCountAll);
        }catch (Exception er){
            throw new JSONMessageException(er.getLocalizedMessage());
        }
        return wb;
    }

    @Override
    public void saveCameraLog(CameraLog cameraLog) {
        try {
            if (cameraLog != null){
                cameraLogDao.save(cameraLog);
            }
        }catch (Exception e){
            logger.error("保存cameraLog失败!",e);
        }
    }

    @Override
    public List<Map> countOptCameraLogByOrgan(String organ, Map conditions) {
        List<Map> list = new ArrayList<>();
        String organArray[] = organ.split(",");
        try {
            for (int index = 1; index < organArray.length; index++){
                Map<String,Integer> map = new HashMap<>();
                conditions.put("userDpet",organArray[index]);
                List<CameraLog> CameraLogList = export(conditions);
                String key = "organ_" + index;
                map.put(key,CameraLogList.size());
                list.add(map);
            }
        }catch (Exception e){
            logger.error("按部门统计摄像头使用次数失败!",e);
        }
        return list;
    }

    @Override
    public String getConstantValue(String type) throws Exception {
        String value = "";
        if (type.equals("TAIZHOU_CAMERALOG_XZQ")){
            value = Constant.TAIZHOU_CAMERALOG_XZQ;
        }
        return value;
    }


    private String getFieledStr(Map data,String key){
        try {
            Object tmp= data.get(key);
            return tmp.toString();
        }catch (Exception er){
            logger.error(er.getMessage());
            return "";
        }
    }

    private double getFieledDouble(Map data,String key){
        try {
            Object tmp= data.get(key);
            return Double.parseDouble(tmp.toString());
        }catch (Exception er){
            logger.error(er.getMessage());
            return 0;
        }
    }

    @Override
    public boolean remove(String id) {
        try {
            cameraLogDao.delete(id);
        } catch (Exception e) {
            logger.error(getMessage("", e.getMessage()));
            return false;
        }
        return true;
    }

    /**
     * 查看摄像探头使用信息
     *
     * @param condition
     * @return
     */
    @Override
    public Map showUseCameraInfo(Map condition) {
        Map result = new HashedMap();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null, endDate = null;
        List<Object[]> results = new ArrayList<Object[]>();
        List<Map> indicator = new ArrayList();
        List data = new ArrayList();
        Map dataItem = new HashedMap();
        List dataValue = new ArrayList();

        if (condition.containsKey("startDate") && !"".equals(condition.get("startDate"))) {
            try {
                startDate = dateFormat.parse(condition.get("startDate").toString());
            } catch (ParseException e) {
                logger.error(getMessage("", e.getMessage()));
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //本月第一天
        }

        if (condition.containsKey("endDate") && !"".equals(condition.get("endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error(getMessage("", e.getMessage()));
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // 本月最后一天
        }

        if (condition.containsKey("user")) {
            results.addAll(cameraLogDao.queryGroupByUserId(startDate, endDate, condition.get("user").toString()));
            dataItem.put("name", "单个人使用监控点及次数");
        } else if (condition.containsKey("camera")) {
            results.addAll(cameraLogDao.queryGroupByCameraId(startDate, endDate, condition.get("camera").toString()));
            dataItem.put("name", "单个监控点使用人及次数");
        }

        for (Object[] obj : results) {
            Map map = new HashedMap();
            map.put("text", obj[1]);
            map.put("max", 20);
            indicator.add(map);
            dataValue.add(obj[2]);
        }

        int max = 0;
        if (dataValue.size() != 0) {
            max = Integer.parseInt(Collections.max(dataValue).toString());
            max = (max < 10) ? 10 : max + 5;
        } else {
            max = 10;
        }

        for (Map map : indicator) {
            map.put("max", max);
        }

        //组织成echarts 的 option里面的数据列格式
        dataItem.put("value", dataValue);
        data.add(dataItem);
        result.put("indicator", indicator);
        result.put("data", data);

        return result;
    }


    /**
     * 查看所有摄像探头使用信息
     *
     * @param condition
     * @return
     */
    @Override
    public List<Map> showAllUseCameraInfo(Map condition) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null, endDate = null;
        List<Map> results = new ArrayList<Map>();

        if (StringUtils.isNotBlank(MapUtils.getString(condition, "startDate"))) {
            try {
                startDate = dateFormat.parse(condition.get("startDate").toString());
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //本月第一天
        }

        if (StringUtils.isNotBlank(MapUtils.getString(condition, "endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // 本月最后一天
        }
        List<Object[]> objects = cameraLogDao.queryGroup(startDate, endDate);
        Map<String, ?> countTimes = countTimesGroupByUserName(startDate, endDate);
        for (Object[] o : objects) {
            Map result = new HashedMap();
            result.put("userName", o[0]);
            result.put("cameraName", o[1]);
            String organName = "";
            try {
                organName = sysUserService.getOrganListByUser(o[2].toString()).get(0).getOrganName();
            } catch (Exception e) {
                logger.error("5-------------------" + e.getLocalizedMessage());
                organName = "无";
            }
            result.put("organName", organName);
            result.put("sum", o[3]);
            result.put("total", countTimes.get(o[0].toString()));
            results.add(result);
        }

        Collections.sort(results, new Comparator<Map>() {
            @Override
            public int compare(Map m, Map m1) {
                return MapUtils.getString(m, "organName").compareTo(MapUtils.getString(m1, "organName"));
            }
        });

        return results;
    }


    public Map countTimesGroupByUserName(Date startDate, Date endDate) {
        Map map = new HashMap();
        List<Object[]> list = cameraLogDao.countTimesGroupByUserName(startDate, endDate);
        for (Object[] o : list) {
            map.put(o[0].toString(), o[1]);
        }
        return map;
    }


    @Override
    public List<Map> getDistinctUserOrCamera(String type, String start, String end) {
        List<Map> result = new ArrayList<Map>();
        if ("user".equalsIgnoreCase(type)) {
            List<Object[]> objects = null;
            if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
                objects = cameraLogDao.queryDistinctUser();
            } else {
                objects = cameraLogDao.queryDistinctUser();
            }
            for (Object[] o : objects) {
                Map map = new HashedMap();
                map.put("userId", o[0]);
                map.put("userName", o[1]);
                result.add(map);
            }
        } else if ("camera".equalsIgnoreCase(type)) {
            List<Object[]> objects = null;
            if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
                objects = cameraLogDao.queryDistinctCamera();
            } else {
                objects = cameraLogDao.queryDistinctCamera();
            }
            for (Object[] o : objects) {
                Map map = new HashedMap();
                map.put("cameraId", o[0]);
                map.put("cameraName", o[1]);
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public CameraLog getById(String id) {
        return cameraLogDao.findOne(id);
    }

    private List<CameraLog> buildFieldsInCameraLog(List<CameraLog> cameraLogs) {
        DateTime start = null, end = null;
        for (CameraLog cameraLog : cameraLogs) {
            if (cameraLog.getStartTime() != null && cameraLog.getEndTime() != null) {
                start = new DateTime(cameraLog.getStartTime());
                end = new DateTime(cameraLog.getEndTime());
                if (Seconds.secondsBetween(start, end).getSeconds() < 60) {
                    cameraLog.setTotalTime(Seconds.secondsBetween(start, end).getSeconds() + "秒");
                } else if (Minutes.minutesBetween(start, end).getMinutes() < 60) {
                    cameraLog.setTotalTime(Minutes.minutesBetween(start, end).getMinutes() + "分");
                } else if (Hours.hoursBetween(start, end).getHours() < 24) {
                    cameraLog.setTotalTime(Hours.hoursBetween(start, end).getHours() + "时");
                } else {
                    cameraLog.setTotalTime(Days.daysBetween(start, end).getDays() + "天");
                }
            } else {
                cameraLog.setTotalTime("0秒");
            }
        }
        return cameraLogs;
    }


    /**
     * 查询三天以上未使用监控点
     *
     * @return
     */
    @Override
    public List unusedCameraIn3Days() {
        String daysStr = AppConfig.getProperty("unused.camera.recently.days");
        int days = 3;
        if(daysStr != null && !daysStr.equals("")) {
            days = Integer.parseInt(daysStr);
        }
        EntityManager em = entityManagerFactory.createEntityManager();
        String sql = "select cameraId,maxc from(select max(c.createat) as maxc, c.cameraId as cameraid from omp_camera_log c group by c.cameraid ) where  (sysdate-Cast(maxc As Date))<" + days;
        Query query = em.createNativeQuery(sql);
        List<Object[]> list = query.getResultList();

        List<Camera> cameraList = videoMetadataService.getAll();
        List<Camera> used = new ArrayList<Camera>();

        for (Camera camera : cameraList) {
            for (Object[] o : list) {
                if (camera.getIndexCode().equalsIgnoreCase(o[0].toString())) {
                    used.add(camera);
                }
            }
        }
        List cameras = new ArrayList();
        for (Camera camera : cameraList) {
            if (used.contains(camera))
                continue;
            Map map = new HashMap();
            map.put("regionName", camera.getRegionName());
            map.put("deviceName", camera.getName());
            map.put("cameraId", camera.getIndexCode());
            List<CameraLog> cameraLogs = cameraLogDao.findMaxCreateByCameraId(camera.getIndexCode());
            if (cameraLogs.size() > 0) {
                map.put("time", cameraLogs.get(0).getCreateAt());
            }

            cameras.add(map);
        }
        return cameras;
    }

    /**如东项目开始**/
    @Override
    public Map getCameraLogDetail(Map condition){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null, endDate = null;
        List<Map> results = new ArrayList<Map>();
        if (StringUtils.isNotBlank(MapUtils.getString(condition, "startDate"))) {
            try {
                startDate = dateFormat.parse(condition.get("startDate").toString());
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //本月第一天
        }

        if (StringUtils.isNotBlank(MapUtils.getString(condition, "endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // 本月最后一天
        }
        //相隔天数
        long diffTime= endDate.getTime()-startDate.getTime();
        //一天的毫秒数
        long secondsOfDay = 1000L * 24 * 60 * 60;
        double dayDiff =((double)diffTime / secondsOfDay);// 计算差多少天
        logger.info("开始时间为"+condition.get("startDate").toString()+"结束时间"+condition.get("endDate").toString());
        List<Object[]> objects = cameraLogDao.queryGroup(startDate, endDate);
        logger.info("objects个数为"+objects.size());
        Map result = new HashMap();
        //对查询结果进行统计
        //部门  部门摄像头使用个数  部门摄像头使用次数
        // userName, cameraName,userId, count
        for (Object[] o : objects) {
            String organName = "";
            Map organInfo = null;
            try {
                organName = sysUserService.getOrganListByUser(o[2].toString()).get(0).getOrganName();
                logger.info("属于"+organName);
            } catch (Exception e) {
                logger.error("5-------------------" + e.getLocalizedMessage());
                organName = "无";
            }
            if(result.get(organName)!=null){
                organInfo =(Map)result.get(organName);
                logger.info("organ已存在,organName为"+organName);
            }else {
                //初始化info
                logger.info("organ不存在,organName为"+organName);
                organInfo = new HashMap();
                organInfo.put("useCount",0);
                organInfo.put("cameraMap",new HashedMap());
                result.put(organName,organInfo);
            }
            Map cameraMap = (Map)organInfo.get("cameraMap");
            int useCount=0;
            try {
                useCount = (int)organInfo.get("useCount");
                useCount += ((Long)o[3]).intValue();
            }catch (Exception er){
                logger.error("数据转换失败");
            }
            logger.info("格式转换结束");
            if(cameraMap.get(o[1])==null){
                cameraMap.put(o[1],true);
            }
            organInfo.put("cameraMap",cameraMap);
            organInfo.put("useCount",useCount);

        }
        //对统计结果进行二次处理
        for(Object key:result.keySet()){
            String organName =(String) key;
            try{
                logger.info("开始第二次统计");
                logger.info("第二次统计organ为"+organName);
                regionDic = getRegionDic();
                if(regionDic==null){
                    throw new Exception("请检查regionDic配置");
                }
                String regionId =  regionDic.get(organName).toString();
                if(regionId==null){
                    logger.error("请检查regionDic配置");
                }else {
                    logger.info("regionId为"+regionId);
                }
                Map organInfo = (Map)result.get(key);
                if(organInfo!=null){
                    logger.info("useCount为"+organInfo.get("useCount"));
                }else{
                    organInfo=new HashMap();
                }
                int cameraCount = videoMetadataService.countCameraByRegionId(regionId);
                logger.info(organName+"cameraCount为"+cameraCount);
                int useCount = (int)organInfo.get("useCount");
                Object cameraDic = organInfo.get("cameraMap");
                int useCameraCount =0;
                if(cameraDic!=null){
                    useCameraCount = ((Map)cameraDic).size();
                }
                organInfo.put("cameraCount",cameraCount);
                logger.info("开始计算useFre");
                double useFre= (double)Math.round((double)useCount/cameraCount * 100)/100;
                organInfo.put("useFre",useFre);
                logger.info("计算useFre结束");
                int cut =cameraCount-useCameraCount;
                if(cut <0){
                    cut=0;
                }
                organInfo.put("noUseCount",cut);
            }catch (Exception er){
                logger.error(organName+"执行二次处理出错");
                logger.error(er.getMessage());
                logger.info(JSON.toJSONString(result));
            }
        }
        return result;
    }

    /**
     * 描述："慧眼守土"监控探头使用频率表导出新方法
     *
     * @return
     * @author 杨文军
     * @create 16:11 2019/7/29
     **/
    @Override
    public Map getCameraLogDetailNew(String startDateStr, String endDateStr){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null, endDate = null;
        List<Map> results = new ArrayList<Map>();
        if (StringUtils.isNotBlank(startDateStr)) {
            try {
                startDate = dateFormat.parse(startDateStr);
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //本月第一天
        }

        if (StringUtils.isNotBlank(endDateStr)) {
            try {
                endDate = dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                logger.error("错误："+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // 本月最后一天
        }
        //相隔天数
        long diffTime= endDate.getTime()-startDate.getTime();
        //一天的毫秒数
        long secondsOfDay = 1000L * 24 * 60 * 60;
        double dayDiff =((double)diffTime / secondsOfDay);// 计算差多少天
        logger.info("开始时间为"+startDateStr+"结束时间"+endDateStr);
        List<Object[]> objects = cameraLogDao.queryGroup(startDate, endDate);
        logger.info("objects个数为"+objects.size());
        Map result = new HashMap();
        //对查询结果进行统计
        //部门  部门摄像头使用个数  部门摄像头使用次数
        // userName, cameraName,userId, count
        for (Object[] o : objects) {
            String organName = "";
            Map organInfo = null;
            try {
                organName = sysUserService.getOrganListByUser(o[2].toString()).get(0).getOrganName();
                logger.info("属于"+organName);
            } catch (Exception e) {
                logger.error("5-------------------" + e.getLocalizedMessage());
                organName = "无";
            }
            if(result.get(organName)!=null){
                organInfo =(Map)result.get(organName);
                logger.info("organ已存在,organName为"+organName);
            }else {
                //初始化info
                logger.info("organ不存在,organName为"+organName);
                organInfo = new HashMap();
                organInfo.put("useCount",0);
                organInfo.put("cameraMap",new HashedMap());
                result.put(organName,organInfo);
            }
            Map cameraMap = (Map)organInfo.get("cameraMap");
            int useCount=0;
            try {
                useCount = (int)organInfo.get("useCount");
                useCount += ((Long)o[3]).intValue();
            }catch (Exception er){
                logger.error("数据转换失败");
            }
            logger.info("格式转换结束");
            if(cameraMap.get(o[1])==null){
                cameraMap.put(o[1],true);
            }
            organInfo.put("cameraMap",cameraMap);
            organInfo.put("useCount",useCount);

        }
        //对统计结果进行二次处理
        for(Object key:result.keySet()){
            String organName =(String) key;
            try{
                logger.info("开始第二次统计");
                logger.info("第二次统计organ为"+organName);
                regionDic = getRegionDic();
                if(regionDic==null){
                    throw new Exception("请检查regionDic配置");
                }
                String regionId =  regionDic.get(organName).toString();
                if(regionId==null){
                    logger.error("请检查regionDic配置");
                }else {
                    logger.info("regionId为"+regionId);
                }
                Map organInfo = (Map)result.get(key);
                if(organInfo!=null){
                    logger.info("useCount为"+organInfo.get("useCount"));
                }else{
                    organInfo=new HashMap();
                }
                int cameraCount = videoMetadataService.countCameraByRegionId(regionId);
                logger.info(organName+"cameraCount为"+cameraCount);
                int useCount = (int)organInfo.get("useCount");
                Object cameraDic = organInfo.get("cameraMap");
                int useCameraCount =0;
                if(cameraDic!=null){
                    useCameraCount = ((Map)cameraDic).size();
                }
                organInfo.put("cameraCount",cameraCount);
                logger.info("开始计算useFre");
                double useFre= (double)Math.round((double)useCount/cameraCount * 100)/100;
                organInfo.put("useFre",useFre);
                logger.info("计算useFre结束");
                int cut =cameraCount-useCameraCount;
                if(cut <0){
                    cut=0;
                }
                organInfo.put("noUseCount",cut);
            }catch (Exception er){
                logger.error(organName+"执行二次处理出错");
                logger.error(er.getMessage());
                logger.info(JSON.toJSONString(result));
            }
        }
        return result;
    }


    public Map getRegionDic() {
        try {
            String rootUrl = fileCacheDao.getLocation();
            String url = rootUrl+"regionDic.yml";
            Resource resource = new UrlResource(url);
            Yaml yaml = new Yaml();
            this.regionDic = yaml.loadAs(new FileInputStream(new File(resource.getURI())), Map.class);
            return this.regionDic;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }
    /**如东项目结束**/

    /**
     * 描述：多线程遍历list并根据用户id查询用户所属部门
     *
     * @return
     * @author 杨文军
     * @create 16:33 2019/8/8
     **/
    @Override
    public List getCameraLogUserDeptByUserId(List<CameraLog> list, int nThreads){
        try {
            if (list == null || list.isEmpty()) {
                return null;
            }

            List<CameraLog> result = new ArrayList<CameraLog>();
            final List<List<CameraLog>> resultCameraLogList = new ArrayList<List<CameraLog>>();
            int size = list.size();
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            List<Future<List<CameraLog>>> futures = new ArrayList<Future<List<CameraLog>>>(nThreads);
            for (int threadIndex = 0; threadIndex < nThreads; threadIndex++) {
                final List<CameraLog> subList = list.subList(size / nThreads * threadIndex, size / nThreads * (threadIndex + 1));
                Callable<List<CameraLog>> task = new Callable<List<CameraLog>>() {
                    @Override
                    public List<CameraLog> call() throws Exception {
                        for (CameraLog cameraLog : subList) {
                            if (StringUtils.isBlank(cameraLog.getUserDept())){
                                String userId = cameraLog.getUserId();
                                List<PfOrganVo> pfOrganVoList= sysUserService.getOrganListByUser(userId);
                                if (pfOrganVoList != null && !pfOrganVoList.isEmpty()){
                                    PfOrganVo pfOrganVo = pfOrganVoList.get(0);
                                    cameraLog.setUserDept(pfOrganVo.getOrganName());
                                }else {
                                    cameraLog.setUserDept("");
                                }
                            }
                        }
                        return subList;
                    }
                };
                futures.add(executorService.submit(task));
            }

            for (Future<List<CameraLog>> future : futures) {
                resultCameraLogList.add(future.get());
            }
            executorService.shutdown();
            for (List<CameraLog> cameraLogList: resultCameraLogList){
                result.addAll(cameraLogList);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
