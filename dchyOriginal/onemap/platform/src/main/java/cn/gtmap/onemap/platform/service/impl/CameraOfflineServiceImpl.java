package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.CameraOfflineDao;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.video.CameraOfflineTrend;
import cn.gtmap.onemap.platform.service.CameraOfflineService;
import cn.gtmap.onemap.platform.service.DocumentService;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * . camera offline service implement
 *
 * @author <a href="mailto:zhaozhuyi@gtmap.cn">zhaozhuyi</a>
 * @version v1.0, 2018/1/29 (c) Copyright gtmap Corp.
 */
@Service
public class CameraOfflineServiceImpl extends BaseLogger implements CameraOfflineService {

    private final CameraOfflineDao cameraOfflineDao;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private FileStoreService fileStoreService;

    private final String WEEKREPORT = "weekReport";
    private final String MONTHREPORT = "monthReport";

    @Autowired
    public CameraOfflineServiceImpl(CameraOfflineDao cameraOfflineDao) {
        this.cameraOfflineDao = cameraOfflineDao;
    }


    /**
     * get trend all time in db
     *
     * @return
     */
    @Override
    public List<CameraOfflineTrend> getTrendAllTime() {
        return cameraOfflineDao.aggregateTrend();
    }

    /**
     * get trends by page
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<CameraOfflineTrend> getTrendPage(Pageable pageable) {
        // todo
        return null;
    }

    /**
     * ??????????????????????????????
     */
    public void cretaeReports() {
        //???filestore?????????excel
        //1.????????????????????????????????????????????????7????????????
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 1) {
            logger.info("??????????????????");
            String start = getPastDate(7);
            String end = getPastDate(1);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            int i = calendar.get(Calendar.WEEK_OF_YEAR);
            int year = calendar.get(Calendar.YEAR);
            String fileName = year + "?????????" + i + "?????????";
            createAndSaveReports(start, end, WEEKREPORT, fileName);
        }
        //2.?????????????????????????????????????????????????????????
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (monthDay == 1) {
            logger.info("??????????????????");
            Map<String, String> mothBeginAndEnd = getLastMothBeginAndEnd();
            //?????????????????????
            String start = mothBeginAndEnd.get("begin");
            //????????????????????????
            String end = mothBeginAndEnd.get("end");
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            String fileName = year + "???" + month + "?????????";
            createAndSaveReports(start, end, MONTHREPORT, fileName);
        }
    }

    /**
     * ?????????????????????
     *
     * @param startDate
     * @param endDate
     * @param type      ????????????
     */
    private void createAndSaveReports(String startDate, String endDate, String type, String reportName) {
        List<Object> offLineReports = cameraOfflineDao.findOffLineReports(startDate, endDate);
        Map excelMap = new HashMap(3);
        excelMap.put("name", type);
        String[] headers = {"????????????", "???????????????", "????????????", "????????????", "???????????????"};
        excelMap.put("header", Arrays.asList(headers));
        List dataList = new ArrayList();
        BigDecimal oneWeek = new BigDecimal(604800), oneDay = new BigDecimal(86400), oneHour = new BigDecimal(3600);
        if (null != offLineReports && offLineReports.size() > 0) {
            for (Object report : offLineReports) {
                Object[] reportDatas = (Object[]) report;
                BigDecimal offLineTime = (BigDecimal) reportDatas[0];
                if (offLineTime.compareTo(oneWeek) > 0 || offLineTime.compareTo(oneDay) > 0) {
                    reportDatas[0] = offLineTime.divide(new BigDecimal(86400), 1).intValue() + "???";
                } else if (offLineTime.compareTo(oneHour) > 0) {
                    reportDatas[0] = offLineTime.divide(new BigDecimal(3600), 1).intValue() + "??????";
                } else {
                    reportDatas[0] = offLineTime.divide(new BigDecimal(60), 1).intValue() + "??????";
                }
                dataList.add(Arrays.asList(reportDatas));
            }
            excelMap.put("data", dataList);

            List<Map> excelList = new ArrayList<Map>(1);
            excelList.add(excelMap);
            try {
                Document document = documentService.writeExcelWithWidth(excelList);
                InputStream ins = new ByteArrayInputStream(document.getContent());
                File file = new File(AppConfig.getProperty("file.store.location") + File.separator + URLEncoder.encode(reportName,"UTF-8") + "." + document.getType());
                FileUtils.copyInputStreamToFile(ins, file);
                if (!file.exists()) {
                    file.mkdir();
                }
                fileStoreService.save3(file, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            logger.info("??????????????????????????????????????????");
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param past
     * @return
     */
    private String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    private Map<String, String> getLastMothBeginAndEnd() {
        Map<String, String> resultMap = new HashMap<String, String>(2);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //???????????????????????????
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = sdf.format(calendar1.getTime());
        resultMap.put("begin", firstDay);
        //??????????????????????????????
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = sdf.format(calendar2.getTime());
        resultMap.put("end", lastDay);
        return resultMap;
    }
}
