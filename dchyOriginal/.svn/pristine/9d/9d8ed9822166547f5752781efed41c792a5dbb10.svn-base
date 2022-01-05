package cn.gtmap.msurveyplat.promanage.web;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.promanage.service.ExportPdfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pdf")
public class PdfController {


    private static final Log logger = LogFactory.getLog(PdfController.class);

    @Autowired
    private ExportPdfService exportPdfService;

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param: request
     * @param: response
      * @return void
      * @time 2021/6/2 9:46
      * @description 统一交付单 预览与打印
      */
    @GetMapping("/tyjfd/{chxmid}")
    @SystemLog(czmkMc = ProLog.CZMC_CGTYJFQRD_MC, czmkCode = ProLog.CZMC_CGTYJFQRD_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public void tyjfd(@PathVariable(name = "chxmid") String chxmid,HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 构建响应
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // 二进制数据流
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);

        response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
        response.setContentType("application/octet-stream");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        try {
            byte[] body = exportPdfService.tyjfdPdf(chxmid);
            servletOutputStream.write(body);
        } catch (Exception e) {
            response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            logger.error("错误原因{}：", e);
            return;
        } finally {
            servletOutputStream.flush();
            servletOutputStream.close();
        }
    }
}
