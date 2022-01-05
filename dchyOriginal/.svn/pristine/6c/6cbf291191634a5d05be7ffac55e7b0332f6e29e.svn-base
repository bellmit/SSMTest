package cn.gtmap.onemap.platform.entity;

import java.io.Serializable;

/**
 * Base map layer
 * ----------------------------------
 * The layer must be a tiled service
 * The property 'name' should be  specific for each layer
 * ---------------------------------
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2017/2/23
 * Version: v1.0 (c) Copyright gtmap Corp.2017
 */
public class BaseMapLayer implements Cloneable, Serializable {

    private static final long serialVersionUID = -7023898607789350243L;

    /**
     * 服务ID
     */
    private String id;

    /***
     * basemap name
     */
    private String name;

    /***
     * basemap title
     */
    private String title;

    /**
     * 服务地址
     */
    private String url;

    /***
     * basemap thumbnail
     */
    private String thumbnailUrl;

    /***
     * 是否反序(将该basemap置于顶层)
     */
    private boolean isTop;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }
}
