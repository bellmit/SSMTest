package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraAutoShotDao;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import cn.gtmap.onemap.platform.service.VideoService;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * auto shot Task
 *
 * @author monarchCheng
 * @create 2018-01-08 10:31
 **/
public class AutoShotTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static int count=0;

    private VideoService videoService;

    private CameraAutoShotDao cameraAutoShotDao;

    private Cache cache;

    private Camera camera;

    private VideoManagerImpl videoManager;

    private String pztServer;

    private final int MAX_PICS = 8;


    public AutoShotTask(VideoService videoService, CameraAutoShotDao cameraAutoShotDao, Cache cache, Camera camera, String ptzServer, VideoManagerImpl videoManager) {
        this.videoService = videoService;
        this.cameraAutoShotDao = cameraAutoShotDao;
        this.cache = cache;
        this.camera = camera;
        this.videoManager = videoManager;
        this.pztServer = ptzServer;
    }

    @Override
    public void run() {
        doShot();
    }

    /**
     * 省厅一个角度拍摄
     */
    public void doShot() {
        logger.info("执行拍照【{}】", camera.getIndexCode());

        //***测试代码请勿上传
        boolean result = filter(camera);
        if(!result){
            count++;
            return;
        }
        //***测试代码结束

        //String ptzServer = videoManager.getRandomPtzServer(this.pztServer);
        String ptzServer = videoManager.getPlat(camera.getPlatform()).getPtzServer();
        logger.info("请求地址" + ptzServer + "indexcode " + camera.getIndexCode());
        String taskId = "";
        CameraAutoShot cameraAutoShot = new CameraAutoShot();
        cameraAutoShot.setIndexCode(camera.getIndexCode());
        cameraAutoShot.setShotAt(new Date());

        // 保存shot
        cameraAutoShot = cameraAutoShotDao.save(cameraAutoShot);
        if (videoManager.getPlat(camera.getPlatform()).getPlatType().equalsIgnoreCase("hw")) {
            try {
                taskId = capture(camera.getVcuId(), camera.getIndexCode(), URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/auto/shot/receive"), Constant.UTF_8), "", ptzServer);
                Thread.sleep(7000);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        } else {
            try {
                //String ptz = "&p=" + String.valueOf(i * 45 + 1) + "&t=10&z=1";
                taskId = capture(camera.getVcuId(), camera.getIndexCode(), URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/auto/shot/receive"), Constant.UTF_8), "", ptzServer);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        cache.put(taskId, cameraAutoShot.getId());
    }

    private boolean filter(Camera camera){
        if(camera.getVcuId().contains("‘")){
            System.out.println("***已过滤‘字节");
            return false;
        }
        if(camera.getVcuId().contains(" ")){
            System.out.println("***已过滤空格字节");
            return false;
        }
        if(videoManager.getPlat(camera.getPlatform())==null){
            System.out.println("***不包含字段");
            return false;
        }
        return true;
    }

//    /**
//     * 南京市局八个角度拍摄
//     */
//    public void doShot() {
//        logger.info("执行多方位拍照【{}】", camera.getIndexCode());
//        String ptzServer = videoManager.getRandomPtzServer(this.pztServer);
//        logger.info("请求地址" + ptzServer + "indexcode " + camera.getIndexCode());
//        int shotNums = cameraAutoShotDao.countByShotAtAndIndexCode(new Date(), camera.getIndexCode());
//        //省厅只拍一张
//        int shotNum = shotNums / MAX_PICS + 1;
//        for (int i = 0; i < MAX_PICS; i++) {
//            String taskId = "";
//            CameraAutoShot cameraAutoShot = new CameraAutoShot();
//            cameraAutoShot.setIndexCode(camera.getIndexCode());
//            cameraAutoShot.setPresetNum(i + 1);
//            cameraAutoShot.setShotAt(new Date());
//            //第n次拍照
//            //cameraAutoShot.setShotNum(shotNum);
//            String name = videoManager.getPlat(camera.getPlatform()).getPlatType();
//            // 1、保存shot
//            cameraAutoShot = cameraAutoShotDao.save(cameraAutoShot);
//            // 2、请求转动ptz p=i*45+1，t=40，z=8
//            if (videoManager.getPlat(camera.getPlatform()).getPlatType().equalsIgnoreCase("hw")) {
//                videoService.setPTZ(camera.getIndexCode(), String.valueOf(i * 45 + 1), "10", "1");
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    logger.error(e.getLocalizedMessage());
//                }
//                try {
//                    taskId = capture(camera.getVcuId(), camera.getIndexCode(), URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/auto/shot/receive"), Constant.UTF_8), "", ptzServer);
//                    Thread.sleep(7000);
//                } catch (Exception e) {
//                    logger.error(e.getLocalizedMessage());
//                }
//            } else {
//                try {
//                    String ptz = "&p=" + String.valueOf(i * 45 + 1) + "&t=10&z=1";
//                    taskId = capture(camera.getVcuId(), camera.getIndexCode(), URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/auto/shot/receive"), Constant.UTF_8), ptz, ptzServer);
//                } catch (UnsupportedEncodingException e) {
//                    logger.error(e.getLocalizedMessage());
//                }
//            }
//            cache.put(taskId, cameraAutoShot.getId());
//            //目前只执行一次
//            break;
//        }
//    }


    private String capture(String vcuId, String indexCode, String receiveUrl,String ptz,String ptzServer) {
        VideoPlats.Plat targetPlat = videoManager.getPlat(camera.getPlatform());
        try {
            String returnUrl = StringUtils.isBlank(receiveUrl) ? "&returnurl=".concat(URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/capture/receive?indexCode=".concat(indexCode)), Constant.UTF_8)) :
                    "&returnurl=".concat(receiveUrl);
            String url = (ptzServer.concat("/getpic")).concat("?vcuid=".concat(vcuId)).concat("&deviceid=" + indexCode).concat("&setnum=-1").concat(returnUrl);
            logger.info("####请求地址为{}",ptzServer);
            switch (VideoManagerImpl.VideoEngine.valueOf(targetPlat.getPlatType())) {
                case hw:
                    break;
                case ivs:
                    url = ptzServer.concat("/getpichwivs").concat("?vcuid=").concat(ptz).concat("&deviceinfo=" + camera.getIndexCode()+"%23"+camera.getVcuId()).concat(returnUrl);
                    break;
                default:
                    break;
            }
            String response = HttpRequest.sendRequest2(url, null);
            if (StringUtils.isNotBlank(response)) {
                Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                String taskId = MapUtils.getString(result, "taskid", "");
                if (StringUtils.isNotBlank(taskId)) {
                    return taskId;
                }
            } else {
                logger.error("多方位拍照异常，设备编码:{}",camera.getIndexCode());
            }
        } catch (Exception e) {
             logger.error(e.getLocalizedMessage());
        }
        return StringUtils.EMPTY;
    }
}
