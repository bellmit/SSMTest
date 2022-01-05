package cn.gtmap.msurveyplat.server.service.ywxx.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import cn.gtmap.msurveyplat.common.domain.DchyCgglZjqtcwDO;
import cn.gtmap.msurveyplat.common.domain.DchyZdChdwDo;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.server.config.DjxlGxConfig;
import cn.gtmap.msurveyplat.server.core.mapper.QualityCheckMapper;
import cn.gtmap.msurveyplat.server.core.service.DchyCgglXmService;
import cn.gtmap.msurveyplat.server.service.ywxx.AlterClsxztService;
import cn.gtmap.msurveyplat.server.service.ywxx.QualityCheckService;
import cn.gtmap.msurveyplat.server.util.Constants;
import cn.gtmap.msurveyplat.server.util.ExchangeFeignUtil;
import cn.gtmap.msurveyplat.server.util.WordUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;


/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/10
 * @description 质检信息接口
 */
@Service
public class QualityCheckServiceImpl implements QualityCheckService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

    @Autowired
    private QualityCheckMapper qualityCheckMapper;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private ExchangeFeignUtil exchangeFeignUtil;
    @Autowired
    private DchyCgglXmService dchyCgglXmService;
    @Autowired
    private DjxlGxConfig djxlGxConfig;
    @Autowired
    private AlterClsxztService alterClsxztService;


    @Override
    public void xzqtcw(List<DchyCgglZjqtcwDO> cwList) {
        qualityCheckMapper.xzqtcw(cwList);
    }


    @Override
    public Map<String, Object> getqtcwxx(Map<String, String> cwid) {
        return qualityCheckMapper.getqtcwxx(cwid);
    }


    @Override
    public void delQtcw(List<String> cwidList) {
        for (String cwid : cwidList) {
            DchyCgglZjqtcwDO dchyCgglZjqtcwDO = entityMapper.selectByPrimaryKey(DchyCgglZjqtcwDO.class, cwid);
            if (StringUtils.isNotBlank(dchyCgglZjqtcwDO.getWjzxjdid())) {
                exchangeFeignUtil.deleteNodeById(Integer.valueOf(dchyCgglZjqtcwDO.getWjzxjdid()));
            }
        }
        qualityCheckMapper.delQtcw(cwidList);
    }

    @Override
    public List<Map<String, Object>> getJclx(String xmbh) {
        List<Map<String, String>> jclxList = qualityCheckMapper.getJclx();
        List<Map<String, Object>> resuluList = packagDate("1", jclxList, xmbh);
        return resuluList;
    }

    /**
     * @param dm       代码
     * @param jclxList 检查类型集合
     * @return 树状结构
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 递归组成树状结构
     */
    private List<Map<String, Object>> packagDate(String dm, List<Map<String, String>> jclxList, String xmbh) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap;
        int num = 1;
        if (CollectionUtils.isEmpty(jclxList)) {
            return resultList;
        }
        for (Map<String, String> map : jclxList) {
            String newDm = dm + num;
            if (StringUtils.equals(newDm, map.get("DM"))) {
                resultMap = Maps.newHashMap();
                resultMap.put("id", map.get("DM"));
                resultMap.put("title", map.get("MC"));
                List<Map<String, Object>> childList = packagDate(newDm, jclxList, xmbh);
                resultMap.put("children", childList);
/*                if (CollectionUtils.isNotEmpty(childList)) {
                    Map<String, Object> count = qualityCheckMapper.countJgsl(xmbh, map.get("DM"));
                    BigDecimal sl = (BigDecimal)count.get("JGSL");
                    if (sl.intValue() != 0) {
                        resultMap.put("mcsl", map.get("MC") + "("+ sl +")");
                    }
                }*/
                resultList.add(resultMap);
                num++;
            }
        }
        return resultList;
    }

    @Override
    public void updateQtcw(DchyCgglZjqtcwDO dchyCgglZjqtcwDO) {
        if (dchyCgglZjqtcwDO != null && StringUtils.isNotBlank(dchyCgglZjqtcwDO.getCwid())) {
            entityMapper.saveOrUpdate(dchyCgglZjqtcwDO, dchyCgglZjqtcwDO.getCwid());
        }
    }

    @Override
    public List<Map<String, String>> getCwjb() {
        return qualityCheckMapper.getCwjb();
    }

    @Override
    public Map<String, String> getJcnr(String xmbh) {
        return qualityCheckMapper.getJcnr(xmbh);
    }

    @Override
    public List<Map<String, Object>> getJclxTotal(String xmbh) {
        List<Map<String, String>> jclxList = qualityCheckMapper.getJclx();
        List<Map<String, Object>> resuluList = packagDateTotal("", jclxList, xmbh);
        return resuluList;
    }

    /**
     * @param dm       代码
     * @param jclxList 检查类型集合
     * @return 树状结构
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 递归组成树状结构
     */
    private List<Map<String, Object>> packagDateTotal(String dm, List<Map<String, String>> jclxList, String xmbh) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap;
        int num = 1;
        if (CollectionUtils.isEmpty(jclxList)) {
            return resultList;
        }
        for (Map<String, String> map : jclxList) {
            String newDm = dm + num;
            if (StringUtils.equals(newDm, map.get("DM"))) {
                resultMap = Maps.newHashMap();
                resultMap.put("id", map.get("DM"));
                List<Map<String, Object>> childList = packagDateTotal(newDm, jclxList, xmbh);
                resultMap.put("children", childList);
                Map<String, Object> count = qualityCheckMapper.countJgsl(xmbh, map.get("DM"));
                BigDecimal sl = (BigDecimal) count.get("JGSL");
                if (sl.intValue() != 0) {
                    resultMap.put("title", map.get("MC") + "(" + sl + ")");
                } else {
                    num++;
                    continue;
                }
                resultList.add(resultMap);
                num++;
            }
        }
        return resultList;
    }

    /**
     * @param dm       代码
     * @param jclxList 检查类型集合
     * @return 树状结构
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 递归组成树状结构
     */
    private List<Map<String, Object>> packagDateAllTotal(String dm, List<Map<String, String>> jclxList, String xmbh) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> resultMap;
        int num = 1;
        if (CollectionUtils.isEmpty(jclxList)) {
            return resultList;
        }
        for (Map<String, String> map : jclxList) {
            String newDm = dm + num;
            if (StringUtils.equals(newDm, map.get("DM"))) {
                resultMap = Maps.newHashMap();
                resultMap.put("id", map.get("DM"));
                List<Map<String, Object>> childList = packagDateAllTotal(newDm, jclxList, xmbh);
                resultMap.put("children", childList);
                Map<String, Object> count = qualityCheckMapper.countJgsl(xmbh, map.get("DM"));
                BigDecimal sl = (BigDecimal) count.get("JGSL");
                resultMap.put("cwzs", sl);
                resultMap.put("mc", map.get("MC"));
                resultList.add(resultMap);
                num++;
            }
        }
        return resultList;
    }

    @Override
    public Map<String, Object> countJcjgTotal(String xmbh) {
        return qualityCheckMapper.countJcjgTotal(xmbh);
    }

    @Override
    public void deletDchyCgglZjqtcwDOByXmid(String xmid) {
        if (StringUtils.isNotBlank(xmid)) {
            Example example = new Example(DchyCgglZjqtcwDO.class);
            example.createCriteria().andEqualTo("xmid", xmid);
            entityMapper.deleteByExample(example);
        }
    }

    @Override
    public Object startQualityCheck(String xmid, String slbh) {
        String cgsjlx = Constants.ZJ_CGSJLX_GHCH;
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        List<Map> djxlCgsjlxMapList = djxlGxConfig.getDjxlCgsjlxMapList();
        if (dchyCgglXmDO != null && CollectionUtils.isNotEmpty(djxlCgsjlxMapList)) {
            for (Map djxlCgsjlxMap : djxlCgsjlxMapList) {
                if (StringUtils.equals(dchyCgglXmDO.getDjxl(), String.valueOf(djxlCgsjlxMap.get("djxl")))) {
                    cgsjlx = String.valueOf(djxlCgsjlxMap.get("cgsjlx"));
                    break;
                }
            }
        }
        return exchangeFeignUtil.startQualityCheck(slbh, cgsjlx);
    }

    private List<Map<String, Object>> getJclxAllTotal(String xmbh) {
        List<Map<String, String>> jclxList = qualityCheckMapper.getJclx();
        List<Map<String, Object>> resuluList = packagDateAllTotal("", jclxList, xmbh);
        return resuluList;
    }

    @Override
    public void export(HttpServletRequest request, HttpServletResponse response, String xmid, String slbh, String type) throws IOException {
        if (StringUtils.equals("excel", type)) {
            // 导出检查结果记录 excel
            exportExcel(request, response, xmid, slbh);
        } else {
            // 导出成果质检报告 word
            exportWord(request, response, xmid);
        }
    }

    /**
     * @param xmid 项目id
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 导出质检报告word
     */
    private void exportWord(HttpServletRequest request, HttpServletResponse response, String xmid) {
        String filename = AppConfig.getProperty("server.zjbg.word.filename");
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        String chdw = "";
        List<DchyZdChdwDo> dchyZdChdwDoList = dchyCgglXmService.getChdw();
        if (CollectionUtils.isNotEmpty(dchyZdChdwDoList) && dchyCgglXmDO != null) {
            for (DchyZdChdwDo dchyZdChdwDo : dchyZdChdwDoList) {
                if (StringUtils.equals(dchyCgglXmDO.getChdw(), dchyZdChdwDo.getDm())) {
                    chdw = dchyZdChdwDo.getMc();
                    break;
                }
            }
        }
        Map<String, Object> params = Maps.newHashMap();
        if (dchyCgglXmDO != null) {
            params.put("JSDW", dchyCgglXmDO.getJsdw() == null ? "" : dchyCgglXmDO.getJsdw());
            params.put("CHDW", chdw);
            params.put("XMMC", dchyCgglXmDO.getXmmc() == null ? "" : dchyCgglXmDO.getXmmc());
        } else {
            params.put("JSDW", "");
            params.put("CHDW", "");
            params.put("XMMC", "");
        }
        // 暂时写死
        params.put("ZLPJJB", "良好");
/*        List<DchyCgglShxxDO> dchyCgglShxxDOList = dchyCgglShxxService.getDchyCgglShxxListByXmid(xmid);
        if (CollectionUtils.isNotEmpty(dchyCgglShxxDOList)) {
            params.put("ZLPJJB",dchyCgglShxxDOList.get(0).getZlpjjb() ==null ? "" : dchyCgglShxxDOList.get(0).getZlpjjb());
        } else {
            params.put("ZLPJJB","");
        }*/
        WordUtil.createDoc(request, response, params, filename);
    }


    /**
     * @param xmid 项目id
     * @param slbh 受理编号
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 导出质检报告excel
     */
    private void exportExcel(HttpServletRequest request, HttpServletResponse response, String xmid, String slbh) throws IOException {
        String conf = AppConfig.getProperty("egov.conf");
        String filename = AppConfig.getProperty("server.zjbg.filename");
        conf = conf.replace("file:/", "");
        String path = conf + "/server/" + filename + ".xlsx";
        FileInputStream fis = new FileInputStream(path);
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        List<Map<String, Object>> queryData = getJclxAllTotal(slbh);
        // 检查记录概况
        List<Map<String, Object>> data = new ArrayList<>();
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        String sjlx = AppConfig.getProperty("server.sjlx");
        String sjlxStr = "";
        if (StringUtils.isNotBlank(sjlx) && dchyCgglXmDO != null) {
            JSONArray zdArray = JSONArray.parseArray(sjlx);
            for (int i = 0; i < zdArray.size(); i++) {
                JSONObject jo = zdArray.getJSONObject(i);
                if (StringUtils.equals(jo.getString("dm"), dchyCgglXmDO.getDjxl())) {
                    sjlxStr = jo.getString("mc");
                    break;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(queryData)) {
            List<Map<String, Object>> resultData = (List) queryData.get(0).get("children");
            for (Map<String, Object> map : resultData) {
                map.put("sjlx", sjlxStr);
                data.add(map);
            }
        }
        List<Map<String, String>> title = new ArrayList<>();
        Map<String, String> titleMap = Maps.newHashMap();
        titleMap.put("key", "sjlx");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "id");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "mc");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "cwzs");
        title.add(titleMap);
        exportExcel(wb, "检查错误记录概况", title, data, 2);

        title.clear();
        titleMap = Maps.newHashMap();
        titleMap.put("key", "TCMC");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "ZDMC");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "CWDJ");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "JCLX");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "CWYSID");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "CWMS");
        title.add(titleMap);
        titleMap = Maps.newHashMap();
        titleMap.put("key", "JCSJ");
        title.add(titleMap);

        Map<String, String> param = Maps.newHashMap();
        int num = 0;
        for (int i = 0; i < data.size(); i++) {
            param.put("xmbh", slbh);
            param.put("dm", data.get(i).get("id").toString());
            List<Map<String, Object>> cwData = qualityCheckMapper.getJcjgByPage(param);
            if (CollectionUtils.isNotEmpty(cwData)) {
                exportExcel(wb, data.get(i).get("mc").toString(), title, cwData, num + 3);
                num++;
            }
        }
        if (wb.getNumberOfSheets() > 2) {
            wb.removeSheetAt(0);
            wb.removeSheetAt(0);
        }
        // 第六步，将文件存到指定位置
        // 兼容ie
        String timeStr;
        String agent = request.getHeader("User-Agent");
        boolean isMsie = (agent != null && agent.indexOf("MSIE") != -1 || -1 != agent.indexOf("Trident"));
        if (isMsie) {
            timeStr = URLEncoder.encode(filename, "UTF-8");
        } else {
            timeStr = new String(filename.getBytes(), "iso-8859-1");
        }

        response.setContentType("application/vnd.ms-excel");
        //timeStr为文件名
        response.setHeader("Location", timeStr + ".xlsx");
        // 设置下载时的文件名称
        response.setHeader(
                "Content-Disposition",
                "attachment; filename="
                        + timeStr + ".xlsx");

        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.close();
        wb.close();
    }

    /**
     * 导出
     *
     * @param excelName 文件名
     * @param title     表头
     * @param rowsList  数据内容
     */
    private XSSFWorkbook exportExcel(XSSFWorkbook wb, String excelName, List<Map<String, String>> title, List<Map<String, Object>> rowsList, int sheetNum) {
        try {
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet;
            if (sheetNum == 2) {
                sheet = wb.cloneSheet(0);
            } else {
                sheet = wb.cloneSheet(1);
            }
            wb.setSheetName(sheetNum, excelName);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = wb.createCellStyle();
            style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            // 指定单元格垂直居中对齐
            style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
            style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            style.setBorderTop(XSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            style.setBorderRight(XSSFCellStyle.BORDER_THIN);
            //插入数据
            if (CollectionUtils.isNotEmpty(rowsList)) {
                for (int j = 0; j < rowsList.size(); j++) {
                    //获取新的一行,第J+1行
                    Row datarow = sheet.createRow(j + 1);
                    //对这一行的各个字段，按照表头的key插入数据
                    for (int m = 0; m < title.size(); m++) {
                        String textValue = "";
                        if (rowsList.get(j).get(title.get(m).get("key")) != null) {
                            Object value = rowsList.get(j).get(title.get(m).get("key"));
                            if (value instanceof Date) {
                                Date date = (Date) value;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                textValue = sdf.format(date);
                            } else if (value instanceof String) {
                                textValue = (String) value;
                            } else if (value instanceof BigDecimal) {
                                textValue = String.valueOf(value);
                            } else {
                                textValue = "";
                            }
                        }
                        //插入第m列字段,获取第m列的title的key值，从rowList中的（Map类型）第j个记录中，通过第m列的title的key值获取数据
                        datarow.createCell(m).setCellValue(textValue);
                        datarow.getCell(m).setCellStyle(style);
                    }
                }
            }
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    @Override
    public Map<String, Object> check(String slbh) {
        return exchangeFeignUtil.check(slbh);
    }

    @Override
    public Integer countcwxx(Map<String, String> xmidMap) {
        return qualityCheckMapper.countcwxx(xmidMap);
    }

    @Override
    public Object importDatabase(String xmid, String slbh,String chgcbh,String gzldyid) {
        String cgsjlx = Constants.ZJ_CGSJLX_GHCH;
        DchyCgglXmDO dchyCgglXmDO = dchyCgglXmService.getDchyCgglXmDOByXmid(xmid);
        List<Map> djxlCgsjlxMapList = djxlGxConfig.getDjxlCgsjlxMapList();
        if (dchyCgglXmDO != null && CollectionUtils.isNotEmpty(djxlCgsjlxMapList)) {
            for (Map djxlCgsjlxMap : djxlCgsjlxMapList) {
                if (StringUtils.equals(dchyCgglXmDO.getDjxl(), String.valueOf(djxlCgsjlxMap.get("djxl")))) {
                    cgsjlx = String.valueOf(djxlCgsjlxMap.get("cgsjlx"));
                    break;
                }
            }
        }
        Object result = null;
        try {
            result = exchangeFeignUtil.importDatabase(slbh, cgsjlx);
        } catch (Exception e) {
            LOGGER.error("QualityCheckServiceImpl.importDatabase", e);
        }
        //todo 根据调用接口是否成功判断入库状态
        if (dchyCgglXmDO != null) {
            dchyCgglXmDO.setRkzt(Constants.RKZT_YRK_DM);
            dchyCgglXmDO.setRksj(CalendarUtil.getCurHMSDate());
            dchyCgglXmService.saveDchyCgglXm(dchyCgglXmDO);
            //接口异步调用
            asyncInvoke(chgcbh,gzldyid);
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getFileInfo(String xmid, String slbh) {
        List<Map<String, String>> fileInfo = new ArrayList<>();
        Map<String, String> word = Maps.newHashMap();
        word.put("fileName", AppConfig.getProperty("server.zjbg.word.filename") + ".doc");
        word.put("path", AppConfig.getProperty("server.url") + "/v1.0/qualitycheck/export/zjbg/" + xmid + "/" + slbh + "/word");
        fileInfo.add(word);
        Map<String, String> excel = Maps.newHashMap();
        excel.put("fileName", AppConfig.getProperty("server.zjbg.filename") + ".xlsx");
        excel.put("path", AppConfig.getProperty("server.url") + "/v1.0/qualitycheck/export/zjbg/" + xmid + "/" + slbh + "/excel");
        fileInfo.add(excel);
        return fileInfo;
    }

    /**
     * @author: <a href="mailto:huming@gtmap.cn">huming</a>
     * @description 异步调用
     */
    private Future<String> asyncInvoke(String chgcbh  ,String gzldyid) {
        Map<String, Object> head = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        //TODO 接口地址 参数封装
        head.put("code", "");
        head.put("msg", "");
        data.put("chgcbh", chgcbh);
        data.put("clsx", gzldyid);
        data.put("cgtjzt", "2");
        String url = AppConfig.getProperty("alertclxszt.url");
        return alterClsxztService.alterClsxzt(head, data, url);
    }
}
