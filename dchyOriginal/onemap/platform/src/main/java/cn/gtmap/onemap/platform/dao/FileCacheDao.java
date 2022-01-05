package cn.gtmap.onemap.platform.dao;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * ycx
 * fileCacheDao
 */
public interface FileCacheDao {
    /**
     * get tpl names in tpl folder
     *
     * @return文件列表
     */
    String[] getCacheNames();

    void setCache(String content, String name) throws IOException;

    String getCacheContent(String name) throws IOException;

    boolean isExist(String name);

    String getLocation();
}
