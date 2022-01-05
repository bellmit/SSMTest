package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-27 下午1:45
 */
public class BaseController extends BaseLogger {

    protected final String RESULT = "result";


    /**
     * 独立处理异常json返回
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(JSONMessageException.class)
    @ResponseBody
    public Map<String, ?> handlerJSONException(final Exception e, final HttpServletRequest request) {
        logger.error(e.getLocalizedMessage());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        result.put("msg", e.getLocalizedMessage());
        return result;
    }

    /**
     * send file
     *
     * @param file
     * @param response
     */
    protected void sendFile(File file, HttpServletResponse response) throws IOException {
        if (file == null || response == null) return;
        if (file.exists()) {
            response.setDateHeader("Last-Modified", file.lastModified());
            response.setContentLength((int) file.length());
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "request file not found");
        }

    }

    /**
     * @param inputStream
     * @param response
     * @throws IOException
     */
    protected void sendFile(InputStream inputStream, HttpServletResponse response, String fileName) throws IOException {
        if (inputStream == null || response == null) return;
        if (inputStream.available() > 0) {
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setDateHeader("Last-Modified", new Date().getTime());
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "request file not found");
        }
    }

    /**
     * send document
     * @param response
     * @param document
     * @throws IOException
     */
    protected void sendDocument(HttpServletResponse response, Document document) throws IOException {
        sendStream(new ByteArrayInputStream(document.getContent()), response, document.getFileName());
    }

    /**
     * send stream
     *
     * @param inputStream
     * @param response
     * @param fileName
     * @throws IOException
     */
    protected void sendStream(InputStream inputStream, HttpServletResponse response, String fileName) throws IOException {
        if (inputStream == null || response == null) return;
        if (inputStream.available() > 0) {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, Constant.UTF_8));
            response.setDateHeader("Last-Modified", new Date().getTime());
            response.setContentLength(inputStream.available());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "request file not found");
        }
    }

    /**
     * result map
     *
     * @param value
     * @return
     */
    protected Map<String, Object> result(Object value) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(RESULT, value);
        return result;
    }

    /**
     * write error to client
     *
     * @param info
     * @param response
     */
    protected void error(String info, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", false);
        result.put("msg", info);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * write result to client
     *
     * @param value
     * @param response
     * @return
     */
    protected void result(Object value, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(RESULT, value);
        try {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 自动将时间格式的字符串转成 Date类型
     * @param request
     * @param binder
     * @throws Exception
     */
    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }


}
