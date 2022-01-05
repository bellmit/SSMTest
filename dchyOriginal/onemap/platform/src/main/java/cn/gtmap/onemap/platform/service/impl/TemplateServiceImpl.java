package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.event.TemplateException;
import cn.gtmap.onemap.platform.service.TemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-27 下午3:16
 */
public class TemplateServiceImpl extends BaseLogger implements TemplateService {

    private final String UTF_8 = "utf-8";

    private String location;

    private Configuration cfg;

    private void init() {
        cfg = new Configuration();
        cfg.setEncoding(Locale.getDefault(), UTF_8);
        cfg.setTemplateUpdateDelay(60);
//        addVars();
        try {
            cfg.setDirectoryForTemplateLoading(getTplResource("").getFile());
        } catch (IOException e) {
            logger.error(" tpls path not exsit : [{}] ", e.getLocalizedMessage());
        }
    }

    /**
     * 获取模版内容 ,默认utf-8编码
     *
     * @param tplName 模版全名，带后缀
     * @return
     */
    public String getTemplate(String tplName) {
        return getTplContent(tplName, null);
    }

    /**
     * 获取模版内容
     *
     * @param tplName
     * @param encoding
     * @return
     */
    public String getTemplate(String tplName, String encoding) {
        return getTplContent(tplName, encoding);
    }

    /**
     * 获取模板内容 并process value
     * @param value
     * @param tplName
     * @return
     * @throws IOException
     * @throws freemarker.template.TemplateException
     */
    @Override
    public String getTemplate(Object value, String tplName) throws IOException, freemarker.template.TemplateException {
        Template tpl = cfg.getTemplate(tplName,UTF_8);
        StringWriter writer = new StringWriter();
        try {
            tpl.process(value, writer);
            return writer.toString();
        }
        finally {
            writer.close();
        }
    }

    /**
     *  获取模板内容 并process value
     * @param value    need to process
     * @param tplName  tpl name
     * @param encoding encoding default:utf-8
     * @return
     */
    @Override
    public String getTemplate(Object value, String tplName, String encoding) throws IOException, freemarker.template.TemplateException {
        Template tpl = cfg.getTemplate(tplName, encoding == null ? UTF_8 : encoding);
        StringWriter writer = new StringWriter();
        try {
            tpl.process(value, writer);
            return writer.toString();
        }
        finally {
            writer.close();
        }
    }

    /**
     * 修改模板内容
     *
     * @param tplName
     * @param content
     * @return
     * @throws TemplateException
     */
    public String modify(String tplName, String content) throws TemplateException {
        try {
            OutputStream outputStream = new FileOutputStream(getTplResource(tplName).getFile());
            IOUtils.write(content, outputStream, UTF_8);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            throw new TemplateException(tplName, TemplateException.Type.TPL_WRITE_ERROR, e.getLocalizedMessage());
        }
        return content;
    }

    /**
     * @param folder
     * @return
     */
    public String[] getFileNames(String folder) {
        String[] names = null;
        try {
            Resource resource = new UrlResource(location.concat(folder));
            File[] files = resource.getFile().listFiles();
            names = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                names[i] = files[i].getName();
            }
        } catch (Exception e) {
            return null;
        }
        return names;
    }

    /**
     * list tpl names under folder
     *
     * @param folder
     * @return
     */
    @Override
    public List<String> listTplNames(String folder) {
        List<String> names = null;
        try {
            names = new ArrayList<String>();
            for (File file : getTplResource(folder).getFile().listFiles()) {
                names.add(file.getName());
            }
        } catch (IOException e) {
            throw new TemplateException(folder, TemplateException.Type.FOLDER_LIST_ERROR, e.getLocalizedMessage());
        }
        return names;
    }

    /**
     * create tpl
     *
     * @param tplName
     * @param content
     * @return
     */
    @Override
    public String createTpl(String tplName, String content) {
        try {
            if (getTplResource(tplName).getFile().exists())
                throw new TemplateException(tplName, TemplateException.Type.TPL_EXIST, null);
        } catch (IOException e) {
            throw new TemplateException(tplName, TemplateException.Type.TPL_CREATE_ERROR, e.getLocalizedMessage());
        }
        return modify(tplName, content);
    }

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    @Override
    public void deleteTpl(String tpl) {
        try {
            FileUtils.forceDelete(getTplResource(tpl).getFile());
        } catch (Exception e) {
            throw new TemplateException(tpl, TemplateException.Type.TPL_DELETE_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * 获取模板
     *
     * @param tplName
     * @param encoding
     * @return
     */
    private String getTplContent(String tplName, String encoding) {
        if (StringUtils.isBlank(tplName)) throw new RuntimeException(getMessage("template.name.not.null"));
        try {
            return IOUtils.toString(getTplResource(tplName).getURI(), encoding == null ? "utf-8" : encoding);
        } catch (IOException e) {
            throw new TemplateException(tplName, TemplateException.Type.TPL_NOT_EXIST, e.getLocalizedMessage());
        } catch (Exception e) {
            throw new TemplateException(tplName, TemplateException.Type.TPL_READ_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * get tpl resource
     *
     * @param tplName
     * @return
     * @throws MalformedURLException
     */
    private Resource getTplResource(String tplName) throws MalformedURLException {
        return new UrlResource(location.concat(tplName));
    }

    /**
     * set location
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
