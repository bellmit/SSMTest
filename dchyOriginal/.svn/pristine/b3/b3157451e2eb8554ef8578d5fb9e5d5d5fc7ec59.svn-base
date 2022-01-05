package cn.gtmap.onemap.platform.service;

import java.io.IOException;
import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-27 下午3:15
 */
public interface TemplateService {

    /**
     * 获取模版内容 ,默认utf-8编码
     *
     * @param tplName 模版全名，带后缀
     * @return
     */
    public String getTemplate(String tplName);

    /**
     * 获取模版内容
     *
     * @param tplName
     * @param encoding
     * @return
     */
    public String getTemplate(String tplName, String encoding);

    /**
     *  获取模板内容 并process value
     * @param value
     * @param tplName
     * @return
     * @throws IOException
     * @throws freemarker.template.TemplateException
     */
    public String getTemplate(Object value, String tplName) throws IOException,freemarker.template.TemplateException;
    /**
     * 获取模板内容 并process value
     * @param value    need to process
     * @param tplName  tpl name
     * @param encoding encoding default:utf-8
     * @return
     */
    public String getTemplate(Object value, String tplName,String encoding) throws IOException,freemarker.template.TemplateException;

    /**
     * 修改模板内容
     *
     * @param tplName
     * @param content
     * @return
     */
    public String modify(String tplName, String content);

    /**
     * 根据目录获取所有模板名称
     *
     * @param folder
     * @return
     * @deprecated replace by {@link #listTplNames(String)}
     */
    @Deprecated
    String[] getFileNames(String folder);

    /**
     * list tpl names under folder
     *
     * @param folder
     * @return
     */
    List<String> listTplNames(String folder) throws IOException;

    /**
     * create tpl
     *
     * @param tplName
     * @param content
     * @return
     */
    String createTpl(String tplName, String content);

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    void deleteTpl(String tpl);
}
