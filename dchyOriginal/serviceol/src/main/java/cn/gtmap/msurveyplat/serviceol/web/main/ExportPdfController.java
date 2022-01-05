package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglJcsjsqService;
import cn.gtmap.msurveyplat.serviceol.service.ExportPdfService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/6/22
 * @description TODO
 */
@RestController
@RequestMapping("/export")
public class ExportPdfController {

    protected final Log LOGGER = LogFactory.getLog(ExportPdfController.class);

    @Autowired
    private ExportPdfService exportPdfService;

    @GetMapping("/exportPdf")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseEntity exportPdf(HttpServletRequest request, HttpServletResponse response) {
        String chxmid = CommonUtil.ternaryOperator(request.getParameter("chxmid"));
        // 构建响应
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // 二进制数据流
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);


        String fileName = Constants.DCHY_XMGL_HZDMC + CalendarUtil.getTimeMs() + ".pdf";
        response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
        response.setContentType("application/octet-stream");

        HttpStatus httpState = null;
        byte[] body = null;
        try {
//            body = exportPdfService.exportPdf(chxmid);
            chxmid = "56L94714ACX2P00G;56L947094AX2P006";
//            chxmid = "56L94714ACX2P00G";
            body = exportPdfService.pdfReport(chxmid);
            fileName = FileDownoadUtil.encodeFileName(request, fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; " + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Last-Modified", new Date().toString());
            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(body.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(body);

        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            LOGGER.error("错误原因{}：", e);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
