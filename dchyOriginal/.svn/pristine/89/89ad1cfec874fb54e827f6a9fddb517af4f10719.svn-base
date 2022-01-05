package cn.gtmap.msurveyplat.server.util;


import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/3
 * @description word
 */
public class WordUtil {
    private static Configuration configuration = null;

    static  {
        configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("utf-8");
    }

    public static void createDoc(HttpServletRequest request, HttpServletResponse response, Map dataMap, String filename) {
        // 这里我们的模板是放在com.ftl包下面
        configuration.setClassForTemplateLoading(WordUtil.class,"/static/");
        Template t = null;
        // 输出文档路径及名称
        Writer out;
        try {
            // test.ftl为要装载的模板
            t = configuration.getTemplate("ftl/多测合一成果质检报告.ftl");
            //t.setEncoding("utf-8");
            response.setContentType("application/msword");
            String timeStr;
            String agent = request.getHeader("User-Agent");

            boolean isMsie = (agent != null && agent.indexOf("MSIE") != -1 || -1 != agent.indexOf("Trident"));
            if (isMsie) {
                timeStr = URLEncoder.encode(filename,"UTF-8");
            } else {
                timeStr = new String(filename.getBytes(), "iso-8859-1");
            }
            //timeStr为文件名
            response.setHeader("Location", timeStr + ".doc");
            // 设置下载时的文件名称
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename="
                            + timeStr + ".doc");

            OutputStream outputStream = response.getOutputStream();
            out = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
