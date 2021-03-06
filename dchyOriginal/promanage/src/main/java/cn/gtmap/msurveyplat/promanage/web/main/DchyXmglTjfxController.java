package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.DchyXmglTjfxDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.service.DchyXmglTjfxService;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/14
 * @description
 */
@RestController
@RequestMapping(value = "/tjfx")
public class DchyXmglTjfxController {

    @Autowired
    private DchyXmglTjfxService dchyXmglTjfxService;

    private static final Log logger = LogFactory.getLog(DchyXmglTjfxController.class);


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取测绘单位列表信息
     */
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    @PostMapping(value = "/getchdwlist")
    public ResponseMessage getChdwList() {
        ResponseMessage responseMessage;
        try {
            List<DchyXmglMlk> chdwList = dchyXmglTjfxService.getChdwList();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", chdwList);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 各阶段项目数量统计
     */
    @PostMapping(value = "/getxmsl")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public ResponseMessage getXmsl(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> xmslList = dchyXmglTjfxService.getXmsl(param);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", xmslList);
        } catch (Exception e) {
            logger.info("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取项目备案记录分页信息
     */
    @PostMapping(value = "/getXmbajllist")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public ResponseMessage getXmbajlByPage(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Page<Map<String, Object>> xmbajlPage = dchyXmglTjfxService.getXmbajlByPage(param);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", xmbajlPage);
        } catch (Exception e) {
            logger.info("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目数量统计
     */
    @PostMapping(value = "/getxmslbyqx")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public ResponseMessage getXmslByQx(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> xmslbyqxList = dchyXmglTjfxService.getXmslByqx(param);
            List<Map<String, Object>> xmslbyqxlysList = dchyXmglTjfxService.getXmslByqxlys(param);
            Map<String, Object> getXmslFromBdst = dchyXmglTjfxService.getXmslFromBdst(param);
            DchyXmglTjfxDto dchyXmglTjfxDto = (DchyXmglTjfxDto) getXmslFromBdst.get("data");
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("XmslFromXmgl", xmslbyqxList);
            responseMessage.getData().put("XmslFromlys", xmslbyqxlysList);
            responseMessage.getData().put("XmslFromBdst", dchyXmglTjfxDto.getXmslbyqxList());
            responseMessage.getData().put("xmslFromBsdtlys", dchyXmglTjfxDto.getXmslbylysList());
        } catch (Exception e) {
            logger.info("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 获取各区县项目委托方式统计
     */
    @PostMapping(value = "/getxmwtfsbyqx")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public ResponseMessage getXmWtfsByQx(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> xmslbyqxList = dchyXmglTjfxService.getXmWtfsByQx(param);
            List<Map<String, Object>> xmslbyLysLis = dchyXmglTjfxService.getXmWtfsByQxlys(param);
            Map<String, Object> getXmslFromBdst = dchyXmglTjfxService.getXmslFromBdst(param);
            DchyXmglTjfxDto dchyXmglTjfxDto = (DchyXmglTjfxDto) getXmslFromBdst.get("data");
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("XmslFromXmgl", xmslbyqxList);
            responseMessage.getData().put("XmslFromlys", xmslbyLysLis);
            responseMessage.getData().put("XmslFromBdst", dchyXmglTjfxDto.getXmslbyqxList());
            responseMessage.getData().put("xmslFromBsdtlys", dchyXmglTjfxDto.getXmslbylysList());
            return responseMessage;
        } catch (Exception e) {
            logger.info("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 业务统计逻辑
     */
    @PostMapping(value = "/getxmslbytotal")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public ResponseMessage getXmslByTotal(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> getXmslFromBdst = dchyXmglTjfxService.getXmslFromBdst(param);
            DchyXmglTjfxDto dchyXmglTjfxDto = (DchyXmglTjfxDto) getXmslFromBdst.get("data");
            if (dchyXmglTjfxDto != null) {
                responseMessage = ResponseUtil.wrapSuccessResponse();
                responseMessage.getData().put("XmslByMouth", dchyXmglTjfxDto.getXmslbymouthList());
                responseMessage.getData().put("XmslByYear", dchyXmglTjfxDto.getXmslbyyearList());
                responseMessage.getData().put("XmslBywtzt", dchyXmglTjfxDto.getXmslbywtztList());
                responseMessage.getData().put("WtjlByPage", dchyXmglTjfxDto.getDchyXmglTjgxFyDto());
                responseMessage.getData().put("XmslByChjd", dchyXmglTjfxDto.getXmslbychjdList());
                List<Map<String, Object>> xmslList = dchyXmglTjfxDto.getXmslbyqxmcandchjdList();
                List<Map<String, Object>> returnData = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(xmslList)) {
                    //查询qxmc存入list
                    List<String> qxmcList = getQxmcList(xmslList);
                    qxmcList.forEach(qxmc -> {
                        Map<String, Object> rowData = Maps.newHashMap();
                        rowData.put("qxmc", qxmc);
                        List<Map<String, Object>> rowList = Lists.newArrayList();
                        xmslList.forEach(xmsl -> {
                            if (qxmc.equals(xmsl.get("QXMC"))) {
                                rowList.add(xmsl);
                            }
                        });
                        rowData.put("data", rowList);
                        returnData.add(rowData);
                    });
                }
                responseMessage.getData().put("XmslByQxAndChjd", returnData);
                responseMessage.getData().put("jsdwList", dchyXmglTjfxDto.getJsdwList());
                responseMessage.getData().put("chdwList", dchyXmglTjfxDto.getChdwList());
            } else {
                responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_FAIL.getMsg(), ResponseMessage.CODE.QUERY_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.info("错误原因{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * @return
     * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
     * @description 委托记录导出
     */
    @GetMapping(value = "/exportWtjl")
    @SystemLog(czmkMc = ProLog.CZMC_TJFX_MC, czmkCode = ProLog.CZMC_TJFX_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_TJFX_CODE)
    public void exportWtjl(HttpServletResponse response, HttpServletRequest request, String year, String jsdwmc, String chdwmc, String exportflag) {
        List<String> keysList = new ArrayList<>();
        keysList.add("XH");
        keysList.add("XQFBBH");
        keysList.add("CHGCBH");
        keysList.add("GCMC");
        keysList.add("JSDWMC");
        keysList.add("CHDWMC");
        keysList.add("CHJD");
        keysList.add("WTSJ");
        keysList.add("WTZT");
        Map<String, Object> paramMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(year)) {
            paramMap.put("year", year);
        }
        if (StringUtils.isNotBlank(jsdwmc)) {
            paramMap.put("jsdwmc", jsdwmc);
        }
        if (StringUtils.isNotBlank(chdwmc)) {
            paramMap.put("chdwmc", chdwmc);
        }
        if (StringUtils.isNotBlank(exportflag)) {
            paramMap.put("exportflag", exportflag);
        }
        Map<String, Object> getXmslFromBdst = dchyXmglTjfxService.getXmslFromBdst(paramMap);
        DchyXmglTjfxDto dchyXmglTjfxDto = (DchyXmglTjfxDto) getXmslFromBdst.get("data");
        List<Map<String, Object>> wjjlList = dchyXmglTjfxDto.getExportwtjlList();
        OutputStream fOut = null;
        try {
            String fileName = "多测合一业务统计委托记录表";
            String codedFileName = URLEncoder.encode(fileName, "UTF-8");
            String egovConf = AppConfig.getProperty("egov.conf");
            String filePath = egovConf + "/promanage/" + "wtjltj" + ".xlsx";
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                filePath = filePath.substring(filePath.indexOf("/") + 1);
            } else {//linux
                filePath = filePath.substring(filePath.indexOf(":") + 1);
            }
            FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            //产生工作表对象
            XSSFSheet sheet = workbook.getSheetAt(0);
            int i = 1;
            if (CollectionUtils.isNotEmpty(wjjlList)) {
                CellStyle styleBorder = workbook.createCellStyle();
                styleBorder.setBorderBottom(XSSFCellStyle.BORDER_THIN);
                styleBorder.setBorderTop(XSSFCellStyle.BORDER_THIN);
                styleBorder.setBorderLeft(XSSFCellStyle.BORDER_THIN);
                styleBorder.setBorderRight(XSSFCellStyle.BORDER_THIN);
                for (int j = 0; j < wjjlList.size(); j++) {
                    Map<String, Object> temp = wjjlList.get(j);
                    XSSFRow rowIndex = sheet.createRow(i);
                    for (int k = 0; k < keysList.size(); k++) {
                        String key = keysList.get(k);
                        XSSFCell hssfCell = rowIndex.createCell(k);
                        hssfCell.setCellStyle(styleBorder);
                        if (StringUtils.equals(key, "XH")) {
                            hssfCell.setCellValue(j + 1);
                        } else {
                            hssfCell.setCellValue(CommonUtil.formatEmptyValue(temp.get(key)));
                        }
                    }
                    i++;
                }
            }
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(Charsets.UTF_8.toString());
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xlsx");
            response.setHeader("Location", codedFileName + ".xlsx");
            fOut = response.getOutputStream();
            workbook.write(fOut);
            fOut.close();
        } catch (Exception ex) {
            logger.error("DchyXmglTjfxController.exportWtjl IOException in !{}", ex);
        } finally {
            try {
                if (null != fOut) {
                    fOut.flush();
                    fOut.close();
                }
            } catch (IOException e) {
                logger.error("DchyXmglTjfxController.exportWtjl IOException out !{}", e);
            }
        }
    }


    private List<String> getQxmcList(List<Map<String, Object>> xmslList) {
        List<String> qxmcList = Lists.newArrayList();
        //
        xmslList.forEach(xmsl -> {
            String qxmc = (String) xmsl.get("QXMC");
            if (!qxmcList.contains(qxmc)) {
                qxmcList.add(qxmc);
            }
        });
        return qxmcList;
    }
}
