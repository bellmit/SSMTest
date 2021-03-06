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
 * . ??????????????????????????????????????????
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
     * oms??????????????????service??????
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
     * ????????????????????????????????????
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
                        || Constant.VideoOptType.CAPTURE.getType() == operation) {  //??????????????????????????????
                    cameraLog= createNewLog(cameraId,uesrId,operation,token);

                    if (Constant.VideoOptType.OPEN.getType() == operation) {
                        cameraLog.setOptContent("??????");
                        cameraLog.setStartTime(now);
                    } else if (Constant.VideoOptType.CAPTURE.getType() == operation) {
                        cameraLog.setOptContent("??????");
                    }
                    cameraLog.setYear(String.valueOf(new DateTime(now).getYear()));

                } else if (Constant.VideoOptType.CLOSE.getType() == operation    //??????????????????????????????????????????
                        || Constant.VideoOptType.END.getType() == operation) {
                    cameraLog.setEndTime(new Date());
                }else if(Constant.VideoOptType.HEARTBEAT.getType()==operation){
                    //????????????
                    if(cameraLog==null){
                        cameraLog = createNewLog(cameraId,uesrId,operation,token);
                    }
                    //????????????????????????
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
            cameraLog.setUserName("????????????");
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
                cameraLog.setUserName("??????????????????");
//                cameraLog.setOptContent("?????????" + camaraName +"??????"+ DateFormatUtils.format(now, "yyyy???MM???dd??? HH???mm???ss???") +"???????????????" + proName + "?????????");
                cameraLog.setOptContent("???????????????" + camaraName + "???URL?????????" + optContent + "...");
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
     * ???????????????????????????????????????
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
     * ?????????????????????
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
     * ????????????
     *
     * @param cameraLogs
     * @return
     */
    @Override
    public Workbook getExportExcel(List<CameraLog> cameraLogs) {
        String[] excelHeader = {"??????", "?????????", "???????????????", "??????id", "????????????", "????????????"};
        int[] excelHeaderWidth = {200,100, 200, 200, 100, 370};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("?????????????????????");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        // ??????????????????
        style.setAlignment(CellStyle.ALIGN_LEFT); // ????????????
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ????????????
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("??????");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// ???????????????
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // ???????????????????????????
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // ???????????????
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        DateFormat format = new SimpleDateFormat("yyyy???MM???dd??? HH???mm???ss??? E");
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
        Sheet sheet = wb.createSheet("?????????????????????");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        // ??????????????????
        style.setAlignment(CellStyle.ALIGN_LEFT); // ????????????
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ????????????
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("??????");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// ???????????????
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
            row.createCell(1).setCellValue(sum.get(i).toString().concat("???"));
        }
        return wb;
    }


    @Override
    public Workbook getAlLExportAllStatisticExcel(List<Map> results) {
        String[] excelHeader = {"??????", "?????????", "???????????????", "????????????", "??????"};
        int[] excelHeaderWidth = {200, 200, 200, 200, 200};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("?????????????????????");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        // ??????????????????
        style.setAlignment(CellStyle.ALIGN_LEFT); // ????????????
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ????????????
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("??????");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// ???????????????
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // ???????????????????????????
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // ???????????????
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
        logger.info("??????????????????excel");
        logger.info(JSON.toJSONString(data));
        Workbook wb = new HSSFWorkbook();
        try {
            //??????excel
            String[] excelHeader = {"??????", "?????????", "???????????????", "??????????????????", "??????????????????","???????????????","??????"};
            int[] excelHeaderWidth = {100, 200, 200, 200,200,200,200};
            Sheet sheet = wb.createSheet("???????????????????????????");
            Row row = sheet.createRow((int) 1);
            Row row1 = sheet.createRow((int)0);
            CellStyle style = wb.createCellStyle();
            // ??????????????????
            style.setAlignment(CellStyle.ALIGN_CENTER); // ????????????
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ????????????
            row.setHeight((short) 400);
            row1.setHeight((short) 500);
            Font font = wb.createFont();
            font.setFontName("??????");
            font.setBoldweight((short)20);
            font.setFontHeightInPoints((short) 14);
            style.setFont(font);
            //??????????????????
            CellStyle titleStyle = wb.createCellStyle();
            Font fontTitle = wb.createFont();
            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // ????????????
            fontTitle.setFontName("??????");
            fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            fontTitle.setFontHeightInPoints((short) 20);
            titleStyle.setFont(fontTitle);

            // ???????????????????????????
            for (int i = 0; i < excelHeaderWidth.length; i++) {
                sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
            }
            // ???????????????
            for (int i = 0; i < excelHeader.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(excelHeader[i]);
                cell.setCellStyle(style);
                sheet.setDefaultColumnStyle(i,style);
            }
            double useCountAll=0;
            double cameraCountAll=0;
            double noUseCountAll =0;
            //????????????
            sheet.addMergedRegion((new CellRangeAddress(0, 0, 0, 7)));
            Cell titleCell = row1.createCell(0);
            titleCell.setCellValue("\"????????????\"???????????????????????????");
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
            cell1.setCellValue("??????");
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
            logger.error("??????cameraLog??????!",e);
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
            logger.error("??????????????????????????????????????????!",e);
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
     * ??????????????????????????????
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
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //???????????????
        }

        if (condition.containsKey("endDate") && !"".equals(condition.get("endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error(getMessage("", e.getMessage()));
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // ??????????????????
        }

        if (condition.containsKey("user")) {
            results.addAll(cameraLogDao.queryGroupByUserId(startDate, endDate, condition.get("user").toString()));
            dataItem.put("name", "?????????????????????????????????");
        } else if (condition.containsKey("camera")) {
            results.addAll(cameraLogDao.queryGroupByCameraId(startDate, endDate, condition.get("camera").toString()));
            dataItem.put("name", "?????????????????????????????????");
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

        //?????????echarts ??? option????????????????????????
        dataItem.put("value", dataValue);
        data.add(dataItem);
        result.put("indicator", indicator);
        result.put("data", data);

        return result;
    }


    /**
     * ????????????????????????????????????
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
                logger.error("?????????"+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //???????????????
        }

        if (StringUtils.isNotBlank(MapUtils.getString(condition, "endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error("?????????"+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // ??????????????????
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
                organName = "???";
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
                    cameraLog.setTotalTime(Seconds.secondsBetween(start, end).getSeconds() + "???");
                } else if (Minutes.minutesBetween(start, end).getMinutes() < 60) {
                    cameraLog.setTotalTime(Minutes.minutesBetween(start, end).getMinutes() + "???");
                } else if (Hours.hoursBetween(start, end).getHours() < 24) {
                    cameraLog.setTotalTime(Hours.hoursBetween(start, end).getHours() + "???");
                } else {
                    cameraLog.setTotalTime(Days.daysBetween(start, end).getDays() + "???");
                }
            } else {
                cameraLog.setTotalTime("0???");
            }
        }
        return cameraLogs;
    }


    /**
     * ????????????????????????????????????
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

    /**??????????????????**/
    @Override
    public Map getCameraLogDetail(Map condition){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null, endDate = null;
        List<Map> results = new ArrayList<Map>();
        if (StringUtils.isNotBlank(MapUtils.getString(condition, "startDate"))) {
            try {
                startDate = dateFormat.parse(condition.get("startDate").toString());
            } catch (ParseException e) {
                logger.error("?????????"+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //???????????????
        }

        if (StringUtils.isNotBlank(MapUtils.getString(condition, "endDate"))) {
            try {
                endDate = dateFormat.parse(condition.get("endDate").toString());
            } catch (ParseException e) {
                logger.error("?????????"+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // ??????????????????
        }
        //????????????
        long diffTime= endDate.getTime()-startDate.getTime();
        //??????????????????
        long secondsOfDay = 1000L * 24 * 60 * 60;
        double dayDiff =((double)diffTime / secondsOfDay);// ??????????????????
        logger.info("???????????????"+condition.get("startDate").toString()+"????????????"+condition.get("endDate").toString());
        List<Object[]> objects = cameraLogDao.queryGroup(startDate, endDate);
        logger.info("objects?????????"+objects.size());
        Map result = new HashMap();
        //???????????????????????????
        //??????  ???????????????????????????  ???????????????????????????
        // userName, cameraName,userId, count
        for (Object[] o : objects) {
            String organName = "";
            Map organInfo = null;
            try {
                organName = sysUserService.getOrganListByUser(o[2].toString()).get(0).getOrganName();
                logger.info("??????"+organName);
            } catch (Exception e) {
                logger.error("5-------------------" + e.getLocalizedMessage());
                organName = "???";
            }
            if(result.get(organName)!=null){
                organInfo =(Map)result.get(organName);
                logger.info("organ?????????,organName???"+organName);
            }else {
                //?????????info
                logger.info("organ?????????,organName???"+organName);
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
                logger.error("??????????????????");
            }
            logger.info("??????????????????");
            if(cameraMap.get(o[1])==null){
                cameraMap.put(o[1],true);
            }
            organInfo.put("cameraMap",cameraMap);
            organInfo.put("useCount",useCount);

        }
        //?????????????????????????????????
        for(Object key:result.keySet()){
            String organName =(String) key;
            try{
                logger.info("?????????????????????");
                logger.info("???????????????organ???"+organName);
                regionDic = getRegionDic();
                if(regionDic==null){
                    throw new Exception("?????????regionDic??????");
                }
                String regionId =  regionDic.get(organName).toString();
                if(regionId==null){
                    logger.error("?????????regionDic??????");
                }else {
                    logger.info("regionId???"+regionId);
                }
                Map organInfo = (Map)result.get(key);
                if(organInfo!=null){
                    logger.info("useCount???"+organInfo.get("useCount"));
                }else{
                    organInfo=new HashMap();
                }
                int cameraCount = videoMetadataService.countCameraByRegionId(regionId);
                logger.info(organName+"cameraCount???"+cameraCount);
                int useCount = (int)organInfo.get("useCount");
                Object cameraDic = organInfo.get("cameraMap");
                int useCameraCount =0;
                if(cameraDic!=null){
                    useCameraCount = ((Map)cameraDic).size();
                }
                organInfo.put("cameraCount",cameraCount);
                logger.info("????????????useFre");
                double useFre= (double)Math.round((double)useCount/cameraCount * 100)/100;
                organInfo.put("useFre",useFre);
                logger.info("??????useFre??????");
                int cut =cameraCount-useCameraCount;
                if(cut <0){
                    cut=0;
                }
                organInfo.put("noUseCount",cut);
            }catch (Exception er){
                logger.error(organName+"????????????????????????");
                logger.error(er.getMessage());
                logger.info(JSON.toJSONString(result));
            }
        }
        return result;
    }

    /**
     * ?????????"????????????"??????????????????????????????????????????
     *
     * @return
     * @author ?????????
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
                logger.error("?????????"+e);
            }
        } else {
            startDate = DateTime.now().monthOfYear().withMinimumValue().toDate();  //???????????????
        }

        if (StringUtils.isNotBlank(endDateStr)) {
            try {
                endDate = dateFormat.parse(endDateStr);
            } catch (ParseException e) {
                logger.error("?????????"+e);
            }
        } else {
            endDate = DateTime.now().monthOfYear().withMaximumValue().plusDays(1).toDate();   // ??????????????????
        }
        //????????????
        long diffTime= endDate.getTime()-startDate.getTime();
        //??????????????????
        long secondsOfDay = 1000L * 24 * 60 * 60;
        double dayDiff =((double)diffTime / secondsOfDay);// ??????????????????
        logger.info("???????????????"+startDateStr+"????????????"+endDateStr);
        List<Object[]> objects = cameraLogDao.queryGroup(startDate, endDate);
        logger.info("objects?????????"+objects.size());
        Map result = new HashMap();
        //???????????????????????????
        //??????  ???????????????????????????  ???????????????????????????
        // userName, cameraName,userId, count
        for (Object[] o : objects) {
            String organName = "";
            Map organInfo = null;
            try {
                organName = sysUserService.getOrganListByUser(o[2].toString()).get(0).getOrganName();
                logger.info("??????"+organName);
            } catch (Exception e) {
                logger.error("5-------------------" + e.getLocalizedMessage());
                organName = "???";
            }
            if(result.get(organName)!=null){
                organInfo =(Map)result.get(organName);
                logger.info("organ?????????,organName???"+organName);
            }else {
                //?????????info
                logger.info("organ?????????,organName???"+organName);
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
                logger.error("??????????????????");
            }
            logger.info("??????????????????");
            if(cameraMap.get(o[1])==null){
                cameraMap.put(o[1],true);
            }
            organInfo.put("cameraMap",cameraMap);
            organInfo.put("useCount",useCount);

        }
        //?????????????????????????????????
        for(Object key:result.keySet()){
            String organName =(String) key;
            try{
                logger.info("?????????????????????");
                logger.info("???????????????organ???"+organName);
                regionDic = getRegionDic();
                if(regionDic==null){
                    throw new Exception("?????????regionDic??????");
                }
                String regionId =  regionDic.get(organName).toString();
                if(regionId==null){
                    logger.error("?????????regionDic??????");
                }else {
                    logger.info("regionId???"+regionId);
                }
                Map organInfo = (Map)result.get(key);
                if(organInfo!=null){
                    logger.info("useCount???"+organInfo.get("useCount"));
                }else{
                    organInfo=new HashMap();
                }
                int cameraCount = videoMetadataService.countCameraByRegionId(regionId);
                logger.info(organName+"cameraCount???"+cameraCount);
                int useCount = (int)organInfo.get("useCount");
                Object cameraDic = organInfo.get("cameraMap");
                int useCameraCount =0;
                if(cameraDic!=null){
                    useCameraCount = ((Map)cameraDic).size();
                }
                organInfo.put("cameraCount",cameraCount);
                logger.info("????????????useFre");
                double useFre= (double)Math.round((double)useCount/cameraCount * 100)/100;
                organInfo.put("useFre",useFre);
                logger.info("??????useFre??????");
                int cut =cameraCount-useCameraCount;
                if(cut <0){
                    cut=0;
                }
                organInfo.put("noUseCount",cut);
            }catch (Exception er){
                logger.error(organName+"????????????????????????");
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
    /**??????????????????**/

    /**
     * ????????????????????????list???????????????id????????????????????????
     *
     * @return
     * @author ?????????
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
