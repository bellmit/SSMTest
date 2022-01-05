package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.ffmpeg.FFmpegTask;

import java.util.Collection;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
public interface FFmpegManagerService {

    /**
     * 开始一个 ffmpeg 命令
     * @param command
     * @return
     */
    String start(String id, String command);

    /**
     * 开启视频流
     * @param cameraCode
     * @return
     */
    String startStream(String cameraCode, String domainCode);

    /**
     * 终止一个 ffmpeg 命令
     * @param id
     * @return
     */
    boolean terminate(String id);

    /**
     * 终止所有的 ffmpeg 命令
     * @return
     */
    int terminateAll();

    /**
     * find by id
     * @param id
     * @return
     */
    FFmpegTask find(String id);

    /**
     * find all 
     * @return
     */
    Collection<FFmpegTask> findAll();
}
