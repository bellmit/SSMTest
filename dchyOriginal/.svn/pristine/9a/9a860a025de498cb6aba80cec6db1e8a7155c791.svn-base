package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.CameraPanorama;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 全景 service
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/4/13 19:16
 */
public interface PanoramaService {


    /***
     *
     * @param indexCode
     * @param createTime
     * @return
     */
    CameraPanorama get(String indexCode,Date createTime);
    /**
     * save panorama file
     * @param file
     * @param createAt
     * @return
     */
    CameraPanorama save(File file, String indexCode,Date createAt);
    /**
     * get panorama image by id
     *
     * @param id
     * @return
     */
    File getFile(String id) throws IOException;
}
