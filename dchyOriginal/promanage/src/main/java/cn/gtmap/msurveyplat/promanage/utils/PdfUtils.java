package cn.gtmap.msurveyplat.promanage.utils;

import com.google.common.base.Charsets;
import com.gtis.config.AppConfig;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/16
 * @description 参考原文地址：https://www.cnblogs.com/wangpeng00700/p/8418594.html
 */
public class PdfUtils {

    private static final Log logger = LogFactory.getLog(PdfUtils.class);


    private static BaseFont simsunbf = null;
    private static String modelPath = null;

    static {
        try {

            // 获取路径地址
            modelPath = AppConfig.getProperty("egov.conf") + "/promanage/";
            String simsunbfPath = ResourceUtils.getURL("classpath:").getPath() + "static/fonts/simsun.ttc,1";

            // 处理前缀
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                modelPath = modelPath.substring(modelPath.indexOf("/") + 1);
                simsunbfPath = simsunbfPath.substring(simsunbfPath.indexOf("/") + 1);
            } else {
                modelPath = modelPath.substring(modelPath.indexOf(":") + 1);
                simsunbfPath = simsunbfPath.substring(simsunbfPath.indexOf(":") + 1);
            }

            // 定义字体信息
            simsunbf = BaseFont.createFont(simsunbfPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            logger.error("pdf 字体设置异常 {}", e);
        } catch (IOException e) {
            logger.error("classpath获取异常 {}", e);
        }
    }


    // 利用模板生成pdf
    public static byte[] pdfout(String pdfName, Map<String, Object> dataMap) {
        // 模板路径
//        String templatePath = "D:/多测合一项目合同备案登记受理回执单.pdf";
        String filePath = modelPath + pdfName + ".pdf";

        // 生成的新文件路径
        PdfReader reader = null;
        PdfStamper stamper = null;
        ByteArrayOutputStream bos = null;
        byte[] body = null;
        try {
            bos = new ByteArrayOutputStream();
            reader = new PdfReader(filePath);// 读取pdf模板
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            //文字类的内容处理
            form.addSubstitutionFont(simsunbf);
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key).toString();
                form.setField(key, value);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            body = bos.toByteArray();
        } catch (Exception e) {
            logger.error("pdf生成异常 {}", e);
        } finally {
            if (null != stamper) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    logger.error("pdf stamper 关闭异常 {}", e);
                }
            }
            if (null != reader) {
                reader.close();
            }
            if (null != bos) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    logger.error("pdf输出流关闭异常 {}", e);
                }
            }
        }

        return body;
    }

    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = Constants.DCHY_XMGL_HZDMC + ".pdf";
//            filename = filename.substring(0,filename.length()-3);
            // 取得文件的后缀名。
//            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1));
//            response.addHeader("Content-Disposition", "attachment;filename=" + "liuqiang");
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

            boolean isOk = file.exists();
            if (isOk) {
                file.delete();
            }
        } catch (IOException ex) {
            logger.error("", ex);
        }
        return response;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap();
        map.put("gcbh", "南京国图测绘工程");
        map.put("jsdw", "南京国图");
        map.put("chdw", "浙江阿里");
        map.put("xh", "001");
        map.put("clmc", "受理材料明细");
        map.put("slr", "刘强");
        map.put("sln", "2021");
        map.put("sly", "06");
        map.put("slr", "11");
        map.put("lqr", "张三");
        map.put("lqn", "2021");
        map.put("lqy", "05");
        map.put("lhsj", "2018年1月1日");

        pdfout(Constants.DCHY_XMGL_HZDMC, map);
    }

}
