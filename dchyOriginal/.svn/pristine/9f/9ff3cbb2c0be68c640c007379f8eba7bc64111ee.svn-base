package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.awt.PointShapeFactory;

import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 20:44
 */
public interface VideoManager {

    /***
     *
     * @return
     */
    VideoService getVideoService();

    /**
     * 根据平台类型获取
     * @param platform
     * @return
     */
    VideoService getVideoService(String platform);

    /**
     * 获取配置信息
     * @param name
     * @return
     */
    VideoPlats.Plat getPlat(String name);

    /**
     * 获取所有的视频平台配置
     * @return
     */
    List<VideoPlats.Plat> getPlats();

    /**
     * 实时请求拍照
     * @param vcuId
     * @param indexCode
     * @param receiveUrl
     * @return
     */
    String capture(String vcuId, String indexCode,String receiveUrl);

    /**
     * 预设位即时拍照
     * @param pNo
     * @param indexCode
     * @param proId
     * @return
     */
    String captureWithPreset(int pNo, String indexCode, String proId);

    void setPtz(List<Camera> cameras, double x,double y);

    /***
     * 定时拍照任务
     */
    void runCaptureTask();

    /***
     * 360全景任务
     */
    void runPanoramicTask();

    /**
     * 全景拼接任务
     */
    void runStitchPanoramaTask();

    String imgRecognize(String imgUrl,String parentId);

    void deleteWarnFileFromDb();

}
