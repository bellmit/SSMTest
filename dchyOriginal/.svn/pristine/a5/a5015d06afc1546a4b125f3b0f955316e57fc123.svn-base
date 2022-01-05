package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.FileCacheDao;
import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.event.TemplateException;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class FileCacheDaoImpl extends BaseLogger implements FileCacheDao {

    private final String FilePath ="fileCaches";
    @Override
    public String[] getCacheNames() {
        String[] names = null;
        try {
            Resource resource = new UrlResource(location.concat("/"+FilePath));
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

    @Override
    public String getLocation(){
        return location;
    }

    @Override
    public boolean isExist(String name) {
        try {
            name+=".cache";
            Resource dirResouce = new UrlResource(location+"/"+FilePath);
            File[] files = dirResouce.getFile().listFiles();
            if(files==null) {
                logger.warn("***未配置layerCache文件夹");
                return false;
            }
            for (int i = 0; i < files.length; i++) {
                if(files[i].getName().equals(name)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setCache(String content, String name) throws IOException {
        //设置缓存内容
        try {
            //没有默认创建
            File file = getCacheResouce(name).getFile();
            logger.info("***开始设置缓存内容");
            logger.info("***name为"+name);
            logger.info("***content"+content);
            if(!file.exists()){
                logger.info("***name为"+name+"***没有缓存记录，正在新建");
                file.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.write(content, outputStream, "utf-8");
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new TemplateException(name, TemplateException.Type.TPL_WRITE_ERROR, e.getLocalizedMessage());
        }
    }

    private String location;

    @Override
    public String getCacheContent(String name) throws IOException {
        if(!getCacheResouce(name).exists()){
            throw new RuntimeException("该文件不存在!");
        }
        String content = IOUtils.toString(getCacheResouce(name).getURI(),"utf-8");
        return content;
    }
    /**
     * 获取文件后缀名
     * @param fileName
     * @return
     */
    private String getSuffix(String fileName){
        String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
        return prefix;
    }

    /**
     * 缓存
     * @param cacheName
     * @return
     * @throws MalformedURLException
     */
    private UrlResource getCacheResouce(String cacheName) throws IOException {
        //设置缓存内容
        if(cacheName.isEmpty()){
            throw new  RuntimeException("文件名不能为空!");
        }
        String realPath = location.concat(FilePath+"/"+cacheName+".cache");
        UrlResource source = new UrlResource(realPath);
        return source;
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
