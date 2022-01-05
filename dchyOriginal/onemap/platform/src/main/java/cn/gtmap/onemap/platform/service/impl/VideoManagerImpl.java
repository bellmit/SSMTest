package cn.gtmap.onemap.platform.service.impl;


import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.*;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.*;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 20:45
 */
public class VideoManagerImpl extends BaseLogger implements VideoManager {

    enum VideoEngine {
        hk, hw, dh, ivs
    }

    private VideoService videoService;

    private VideoPlats.Plat defaultPlat;

    @Autowired
    private CameraAutoShotDao cameraAutoShotDao;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PresetService presetService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private CameraOfflineDao cameraOfflineDao;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private PanoramaService panoramaService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectCameraRefDao projectCameraRefDao;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private CameraPresetTaskService cameraPresetTaskService;

    @Autowired
    private FileStoreDao fileStoreDao;

    @Autowired
    private CameraRecognitionDao cameraRecognitionDao;

    @Autowired
    private CameraDao cameraDao;

    private Cache captureCache;

    private Cache panoramicCache;

    private List<Camera> cameras;

    private ConcurrentMap<Integer, String[]> ptzServerMap = new ConcurrentHashMap<Integer, String[]>(20);

    private Random random=new Random();

    /**
     * 视频平台
     */
    private VideoPlats videoPlats = new VideoPlats();

    /**
     * 多个videoService bean
     */
    private Map<String, VideoService> videoServices = new HashMap<String, VideoService>();

    private static final int basePort = 6000;
    private int portSize = 6;
    private int currentPort = basePort;


    /**
     * 默认视频平台 （适用于单个平台）
     *
     * @param defaultPlat
     */
    public void setDefaultPlat(VideoPlats.Plat defaultPlat) {
        this.defaultPlat = defaultPlat;
        videoService = createVideoServiceBean(defaultPlat);
    }

    /***
     * 加载多视频平台(或多账号)配置
     * @param path
     */
    public void setVideoPlats(Resource path) {
        try {
            Yaml yaml = new Yaml();
            this.videoPlats = yaml.loadAs(IOUtils.toString(path.getURI(), Constant.UTF_8), VideoPlats.class);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 初始化视频接口
     */
    public void initEngine() {
        for (VideoPlats.Plat plat : videoPlats.getPlats()) {
            VideoService bean = createVideoServiceBean(plat);
            if (videoService == null) {
                videoService = bean;
            }
            videoServices.put(plat.getName(), bean);
        }
        captureCache = cacheManager.getCache("captureCache");
    }

    /**
     * 默认的 videoService
     *
     * @return
     */
    @Override
    public VideoService getVideoService() { return videoService;
    }

    /**
     * 多平台情形下查询 videoService
     *
     * @param platform
     * @return
     */
    @Override
    public VideoService getVideoService(String platform) {
        if (StringUtils.isBlank(platform)) {
            return getVideoService();
        }
        return videoServices.get(platform);
    }

    /**
     * 获取平台连接配置信息
     *
     * @param name
     * @return
     */
    public VideoPlats.Plat getPlat(String name) {
        // 默认返回 application.properties 中的相应配置
        if (StringUtils.isBlank(name)) {
            return defaultPlat;
        }
        return videoPlats.getPlat(name);
    }

    /**
     * 获取所有的视频平台配置
     *
     * @return
     */
    @Override
    public List<VideoPlats.Plat> getPlats() {
        return videoPlats.getPlats();
    }

    /**
     * 向视频平台客户端发送请求拍照
     * 定时发送请求
     */
    @Override
    public void runCaptureTask() {
        captureCache.clear();
        List<ProjectCameraRef> refList = projectCameraRefDao.findAll();
        if (isNotNull(refList) && refList.size() > 0) {
            for (ProjectCameraRef ref : refList) {
                try {
                    String hikConf = StringUtils.EMPTY;
                    final String indexCode = ref.getCameraId();
                    String proId = ref.getProId();
                    Project pro = projectService.getByProid(proId);
                    List<Preset> presets = presetService.find(pro.getId(), indexCode);
                    List<Camera> cameras = videoService.getCamerasByProId(proId);
                    Collection<Camera> filterCameras = Collections2.filter(cameras, new Predicate<Camera>() {
                        @Override
                        public boolean apply(Camera input) {
                            return indexCode.equalsIgnoreCase(input.getIndexCode());
                        }
                    });
                    if (presets.size() > 0 && filterCameras.size() > 0) {
                        Camera camera = cameras.get(0);
                        VideoPlats.Plat plat = getPlat(camera.getPlatform());
                        if (plat.getPlatType().equalsIgnoreCase(VideoEngine.hk.name())) {
                            try {
                                hikConf = videoServices.get(camera.getPlatform()).getCameraXml(VideoService.OperaType.preview, camera.getIndexCode());
                            } catch (Exception e) {
                                continue;
                            }
                            if (StringUtils.isBlank(hikConf)) {
                                logger.error(getMessage("hk.xml.error", camera.getName()));
                                continue;
                            }
                        }
                        try {
                            captureExecute(presets.get(0), camera, proId, plat, hikConf);
                        } catch (Exception e) {
                            logger.error("请求定时拍照失败，设备编号【{}】，预设位【{}】，平台为【{}】", indexCode, presets.get(0).getPresetNo(), plat.getName());
                            throw new RuntimeException(e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                    continue;
                }
            }
        } else {
            logger.warn("未找到任何关联的项目监控点!");
        }
    }

    /**
     * 八相位定时拍照任务
     * 如果配置文件中指定开启部分摄像头则开启相应的
     * 否则开启全部
     */
    public void autoShot(List<String> indexCodes) {
        List<Camera> cameras = new ArrayList<Camera>();
        Set<String> set = new HashSet<String>();
        //如果参数不为空则执行指定的摄像头，否则执行不在omp_camera_dfw_task中记录的
        if (isNotNull(indexCodes) && indexCodes.size() > 0) {
            set.addAll(indexCodes);
        } else {
            List<String> indexCodeList = cameraDao.findCameraIdsNotInCameraDfwTask();
            set.addAll(indexCodeList);
        }
        if(CollectionUtils.isEmpty(set)){
            return;
        }
        if(set.size() > 1000) {
            int k = set.size() / 1000;
            for (int i = 0; i <= k; i++) {
                Set<String> newSet = new HashSet<String>(1000);
                Iterator<String> iterator = set.iterator();
                int j = 0;
                while (iterator.hasNext()) {
                    j++;
                    newSet.add(iterator.next());
                   iterator.remove();
                    if (j >= 1000) {
                        break;
                    }

                }
                //oracle max 1000
                cameras.addAll(cameraDao.findByIndexCodeIn(newSet));
            }
        }else {
            cameras = cameraDao.findByIndexCodeIn(set);
        }
        logger.info("######执行多方位拍照的摄像头个数"+cameras.size());
        for (Camera camera : cameras) {
            if (camera.getStatus() == 0) {
                continue;
            }
//            VideoPlats.Plat targetPlat = this.getPlat(camera.getPlatform());
//            String targetPlatName = targetPlat.getName();
            //只有以下平台进行多方位拍照
//            if ("hw1".equalsIgnoreCase(targetPlatName)|| "ivs".equalsIgnoreCase(targetPlatName)) {
                String ptzServers = AppConfig.getProperty("camera.client");
                VideoService videoService = getVideoService(camera.getPlatform());
                threadPoolTaskExecutor.execute(new AutoShotTask(videoService, cameraAutoShotDao, captureCache, camera, ptzServers, this));
//            }
        }
    }

    /**
     * 刷新摄像头缓存
     */
    public void refreshCameraCache(){
        try {
            Map data = videoMetadataService.getLayerCache();
            ArrayList<Map> layers = (ArrayList<Map>)data.get("layers");
            for(int i=0;i<layers.size();i++){
                Map temp = layers.get(i);
                String layerName = (String) temp.get("name");
                videoMetadataService.getOverlayCamera(layerName,null,false);
            }
        }catch (Exception er){
            logger.error(er.getLocalizedMessage());
        }
    }

    /**
     * 全景任务
     *
     * @description 按照设定的时间点启动后 对每个监控点进行360度的全景拍照
     */
    @Override
    public void runPanoramicTask() {
        panoramicCache = cacheManager.getCache("panoramicCache");
        panoramicCache.clear();
        for (Camera camera : cameras) {
            try {
                String deviceConfigInfo = null;
                VideoPlats.Plat targetPlat = getPlat(camera.getPlatform());
                String ptzServer = targetPlat.getPtzServer();
                if (targetPlat.getPlatType().equalsIgnoreCase(VideoEngine.hk.name())) {
                    deviceConfigInfo = videoService.getCameraXml(VideoService.OperaType.preview, camera.getIndexCode());
                    if (isNull(deviceConfigInfo)) {
                        logger.error(getMessage("hk.xml.error", camera.getName()));
                        continue;
                    }
                    //进行全景拍照
                    String url = "";
                    String vcuId = camera.getVcuId();
                    String indexCode = camera.getIndexCode();
                    String defaultPreset = "setnum=30";//默认预设位30
                    String receiveUrl = "&returnurl=" + URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/panorama/receive"), Constant.UTF_8);
                    switch (VideoEngine.valueOf(targetPlat.getPlatType())) {
                        case hw:
                            url = getRandomPtzServer(ptzServer).concat("/getpic").concat("?vcuid=".concat(vcuId)).concat("&deviceid=" + indexCode).concat("&".concat(defaultPreset)).concat("&tasktype=full").concat(receiveUrl);
                            break;
                        case hk:
                            Pattern p = Pattern.compile("\t|\r|\n");
                            Matcher m = p.matcher(deviceConfigInfo);
                            String deviceInfo = m.replaceAll("");
                            url = getRandomPtzServer(ptzServer).concat("/getpic").concat(VideoEngine.hk.name()).concat("?".concat(defaultPreset)).concat("&tasktype=full").concat("&deviceinfo=").concat(URLEncoder.encode(deviceInfo, "UTF-8")).concat(receiveUrl);
                            break;
                    }
                    logger.info("调用全景拍照地址:" + url);
                    String response = HttpRequest.sendRequest2(url, null);
                    if (StringUtils.isNotBlank(response)) {
                        Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                        String taskId = MapUtils.getString(result, "taskid", "");
                        if (StringUtils.isNotBlank(taskId)) {
                            panoramicCache.put(taskId, indexCode);
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 定时拼接任务
     */
    @Override
    public void runStitchPanoramaTask() {
        List<String> indexCodes = videoService.getIndexCodes();
        Date date = new Date();
        for (String indexCode : indexCodes) {
            panoramaService.get(indexCode, date);
            try {
                Thread.sleep(180000);//3 minutes
            } catch (InterruptedException e) {
                logger.error("全景拼接线程出现异常{}", e.toString());
            }
        }
    }

    /**
     * 请求控件拍照
     *
     * @param preset     预设位
     * @param camera     目标 camera
     * @param proId      监控项目 proid
     * @param targetPlat 摄像头所在的平台参数
     * @param hikConf    海康摄像头配置
     * @throws Exception
     */
    private void captureExecute(Preset preset, Camera camera, String proId, VideoPlats.Plat targetPlat, String hikConf) throws Exception {
        int pNo = preset.getPresetNo();
        try {
            String captureUrl = "";
            String ptzServer = targetPlat.getPtzServer();
            String returnUrl = "&returnurl=" + URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/capture/receive"), Constant.UTF_8);
            switch (VideoEngine.valueOf(targetPlat.getPlatType())) {
                case dh:
                case hw:
                    captureUrl = ptzServer.concat("/getpic").concat("?vcuid=".concat(camera.getVcuId())).concat("&deviceid=" + camera.getIndexCode()).concat("&setnum=" + pNo).concat(returnUrl);
                    break;
                case hk:
                    Pattern p = Pattern.compile("\t|\r|\n");
                    Matcher m = p.matcher(hikConf);
                    String deviceInfo = m.replaceAll("");
                    captureUrl = ptzServer.concat("/getpic").concat("hk").concat("?setnum=" + pNo).concat("&deviceinfo=").concat(URLEncoder.encode(deviceInfo, "UTF-8")).concat(returnUrl);
                    logger.info(getMessage("capture.img.debug", ptzServer.concat("hk").concat("?setnum=" + pNo).concat("&deviceinfo="), hikConf));
                    break;
                case ivs:
                    captureUrl = ptzServer.concat("/getpichwivs").concat("?vcuid=").concat("&deviceinfo=" + camera.getIndexCode() + "%23" + camera.getVcuId()).concat("&setnum=" + pNo).concat(returnUrl);
                    break;
            }
            logger.info("拍照地址： {}" + captureUrl);
            String response = HttpRequest.sendRequest2(captureUrl, null);
            if (StringUtils.isNotBlank(response)) {
                Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                String taskId = MapUtils.getString(result, "taskid", "");
                if (StringUtils.isNotBlank(taskId)) {
                    captureCache.put(taskId, proId);
                }
            } else
                logger.error(getMessage("capture.img.error", captureUrl));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 预设位即时拍照
     *
     * @param pNo
     * @param indexCode
     * @param proId
     * @return
     * @throws Exception
     */
    public String captureWithPreset(int pNo, String indexCode, String proId) {
        try {
            Camera camera = cameraDao.findByIndexCode(indexCode);
            String captureUrl;
            VideoPlats.Plat plat = getPlat(camera.getPlatform());
            String ptzServer = plat.getPtzServer();
            String returnUrl = "&returnurl=" + URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/capture/receive"), Constant.UTF_8);
            captureUrl = ptzServer.concat("/getpic").concat("?vcuid=".concat(camera.getVcuId())).concat("&deviceid=" + camera.getIndexCode()).concat("&setnum=" + pNo).concat(returnUrl);
            logger.info("拍照地址： {}" + captureUrl);
            String response = HttpRequest.sendRequest2(captureUrl, null);
            if (StringUtils.isNotBlank(response)) {
                Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                String taskId = MapUtils.getString(result, "taskid", "");
                if (StringUtils.isNotBlank(taskId)) {
                    captureCache.put(taskId, proId);
                    return taskId;
                }
            } else {
                logger.error(getMessage("capture.img.error", captureUrl));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public void setPtz(List<Camera> cameras,double x,double y ){
        for(int i=0;i<cameras.size();i++){
            Camera camera = cameras.get(i);
            threadPoolTaskExecutor.execute(new ThreadRunable(camera,x,y) {
                @Override
                public void run() {
                    Camera camera = (Camera)this.obj1;
                    double x = (double)this.obj2;
                    double y = (double)this.obj3;
                    Point point = videoService.getPointFromXY(x,y);
                    getVideoService(camera.getPlatform()).setPTZ(camera.getIndexCode(),point);
                }
            });
        }
    }


    /**
     * 实时请求拍照
     *
     * @param vcuId
     * @param indexCode
     * @param receiveUrl
     * @return
     * @throws Exception
     */
    public String capture(String vcuId, String indexCode, String receiveUrl) {
        String ptzServer;
        Camera targetCamera = cameraDao.findById(indexCode);
        VideoPlats.Plat targetPlat = getPlat(targetCamera.getPlatform());
        ptzServer = targetPlat.getPtzServer();
        String deviceConfigInfo = null;
        if (targetPlat.getPlatType().equalsIgnoreCase(VideoEngine.hk.name())) {
            deviceConfigInfo = videoService.getCameraXml(VideoService.OperaType.preview, indexCode);
        }
        try {
            String url = StringUtils.EMPTY;
            String returnUrl = StringUtils.isBlank(receiveUrl) ? "&returnurl=".concat(URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/capture/receive?indexCode=".concat(indexCode)), Constant.UTF_8)) :
                    "&returnurl=".concat(receiveUrl);
            switch (VideoEngine.valueOf(targetPlat.getPlatType())) {
                case dh:
                case hw:
                    url = (ptzServer.concat("/getpic")).concat("?vcuid=".concat(vcuId)).concat("&deviceid=" + indexCode).concat(returnUrl);
                    break;
                case hk:
                    Pattern p = Pattern.compile("\t|\r|\n");
                    Matcher m = p.matcher(deviceConfigInfo);
                    String deviceInfo = m.replaceAll("");
                    url = (ptzServer.concat("/getpic")).concat("hk").concat("?deviceinfo=").concat(URLEncoder.encode(deviceInfo, "UTF-8")).concat(returnUrl);
                    break;
                case ivs:
                    return null;



            }
            String response = HttpRequest.sendRequest2(url, null);
            if (StringUtils.isNotBlank(response)) {
                Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                String taskId = MapUtils.getString(result, "taskid", "");
                if (StringUtils.isNotBlank(taskId)) {
                    return taskId;
                }
            } else
                logger.error(getMessage("capture.img.error", url));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 动态创建 VideoService Bean
     *
     * @param plat
     * @return
     */
    private VideoService createVideoServiceBean(VideoPlats.Plat plat) {
        VideoEngine engine;
        try {
            engine = VideoEngine.valueOf(plat.getPlatType());
        } catch (IllegalArgumentException e) {
            engine = VideoEngine.hw;
        }
        switch (engine) {
            case dh:
                DhVideoServiceImpl.plat = plat;
                return (VideoService) ApplicationContextHelper.createBean(DhVideoServiceImpl.class);
            case hk:
                HikVideoServiceImpl hikVideoService = (HikVideoServiceImpl) ApplicationContextHelper.createBean(HikVideoServiceImpl.class);
                hikVideoService.setPlat(plat);
                hikVideoService.init();
                return hikVideoService;
            case hw:
                HwVideoServiceImpl hwVideoService = (HwVideoServiceImpl) ApplicationContextHelper.createBean(HwVideoServiceImpl.class);
                hwVideoService.setPlat(plat);
                hwVideoService.init();
                return hwVideoService;
            case ivs:
                return (IvsVideoServiceImpl) ApplicationContextHelper.createBean(IvsVideoServiceImpl.class);
            default:
                return null;
        }
    }

    /***
     * 多个相同服务端时随机选择某个服务地址
     * 采用随机法获取本次拍照的控件服务器地址
     * 不采用轮询法 是因为轮询法会使用悲观锁 效率较低
     * @return ptz服务地址
     */
    public String getRandomPtzServer(String ptzServer) {
        if (StringUtils.contains(ptzServer, "|")) {
            List<String> serverList = Arrays.asList(ptzServer.split("\\|"));
            return serverList.get(random.nextInt(serverList.size()));
        } else {
            return ptzServer;
        }
    }

    /***
     * 依次获取 拍照服务端地址
     * @param camera
     * @param capacity
     * @return
     */
    private String getRandomPtzServer(Camera camera, int capacity) {
        String ip = "http://127.0.0.1:";
        String[] indexcodes = ptzServerMap.get(currentPort);
        if (currentPort - basePort == portSize && indexcodes.length >= capacity) {
            //  到达最后一个服务重新开始
            currentPort = basePort + 1;
            getRandomPtzServer(camera, capacity + 1);
        }
        if (indexcodes == null) {
            ++currentPort;
            ptzServerMap.put(currentPort, new String[]{camera.getIndexCode()});
            return ip.concat(String.valueOf(currentPort));
        }
        int sizeOfCurrPort = indexcodes.length;
        if (sizeOfCurrPort >= capacity) {
            ++currentPort;
            ptzServerMap.put(currentPort, new String[]{camera.getIndexCode()});
            return ip.concat(String.valueOf(currentPort));
        } else {
            ptzServerMap.put(currentPort, ArrayUtils.add2Arrays(indexcodes, camera.getIndexCode()));
            return ip.concat(String.valueOf(currentPort));
        }
    }


    /**
     * 更新摄像头状态-定时任务
     */
    public void updateStatus() {
        /*if (MapUtils.isEmpty(videoServices)) {
            if (videoService instanceof HwVideoServiceImpl) {
                updateWsuStatus2DB((HwVideoServiceImpl) videoService);
            }
        } else {
            for (VideoService v : videoServices.values()) {
                if (v instanceof HwVideoServiceImpl) {
                    updateWsuStatus2DB((HwVideoServiceImpl) v);
                }
            }
        }*/
        if (MapUtils.isEmpty(videoServices)) {
            if (videoService instanceof HwVideoServiceImpl) {
                updateWsuStatus2DB((HwVideoServiceImpl) videoService);
            } else if (videoService instanceof IvsVideoServiceImpl) {
                updateWsuStatus2DB((IvsVideoServiceImpl) videoService);
            }
        } else {
            for (VideoService v : videoServices.values()) {
                if (v instanceof HwVideoServiceImpl) {
                    updateWsuStatus2DB((HwVideoServiceImpl) v);
                } else if (v instanceof IvsVideoServiceImpl) {
                    updateWsuStatus2DB((IvsVideoServiceImpl) v);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateWsuStatus2DB(HwVideoServiceImpl hwVideoService) {
        List<Map> devices = hwVideoService.getDeviceList();
        List<Camera> cameras = new ArrayList<Camera>();
        for (Map map : devices) {
            int status = MapUtils.getInteger(map, "status", 1);
            String vcuId = MapUtils.getString(((Map) ((List) map.get("cameras")).get(0)), "id", "");
            Camera camera = null;
            if (vcuId.endsWith("0101")) {
                camera = cameraDao.findByIndexCode(vcuId);
            } else {
                List<Camera> list = cameraDao.findByVcuId(vcuId);
                if (!CollectionUtils.isEmpty(list)) {
                    camera = cameraDao.findByVcuId(vcuId).get(0);
                }
            }
            if (isNotNull(camera)) {
                camera.setStatus(status);
                cameras.add(camera);
                // 埋点 增加 离线统计
                if (status == 0) {
                    saveOffline(camera);
                }
            }
        }
        cameraDao.save(cameras);
    }

    /**
     * update status to db
     *
     * @param ivsVideoService
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWsuStatus2DB(IvsVideoServiceImpl ivsVideoService) {
        logger.info("update ivs camera status start");
        List<Map> devices = ivsVideoService.getDeviceList();
        List<Camera> cameras = new ArrayList<Camera>();
        logger.info(JSON.toJSONString(cameras));
        for (Map device : devices) {
            String code = MapUtils.getString(device, "code");
            Camera camera = cameraDao.findByIndexCode(code.split("#")[0]);
            int status = MapUtils.getInteger(device, "status");
            if (isNotNull(camera)) {
                // 埋点 增加 离线统计
                logger.info("【{}】change status 【{}】", code, status);
                if (status == 0) {
                    saveOffline(camera);
                }
                camera.setStatus(status);
                cameras.add(camera);
            }
        }
        cameraDao.save(cameras);
    }

    public String cameraCaptureWithPreser(String cameraTaskId){
        String hikConf = StringUtils.EMPTY;
        CameraPresetTask task = cameraPresetTaskService.findById(cameraTaskId);
        String indexCode = task.getIndexCode();
        if(StringUtils.isEmpty(indexCode)){
            return null;
        }
        Camera camera = cameraDao.findByIndexCode(indexCode);
        if(null == camera){
            return null;
        }
        VideoPlats.Plat plat = getPlat(camera.getPlatform());
        if (plat.getPlatType().equalsIgnoreCase(VideoEngine.hk.name())) {
            try {
                hikConf = videoServices.get(camera.getPlatform()).getCameraXml(VideoService.OperaType.preview, camera.getIndexCode());
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
            if (StringUtils.isBlank(hikConf)) {
                logger.error(getMessage("hk.xml.error", camera.getName()));
            }
        }
        int pNo = task.getPNo();
        try {
            String captureUrl = "";
            String ptzServer = plat.getPtzServer();
            String returnUrl = "&returnurl=" + URLEncoder.encode(AppConfig.getProperty("omp.url").concat("/video/presetCapture/receive"), Constant.UTF_8);
            switch (VideoEngine.valueOf(plat.getPlatType())) {
                case hk:
                    Pattern p = Pattern.compile("\t|\r|\n");
                    Matcher m = p.matcher(hikConf);
                    String deviceInfo = m.replaceAll("");
                    captureUrl = ptzServer.concat("/getpic").concat("hk").concat("?setnum=" + pNo).concat("&deviceinfo=").concat(URLEncoder.encode(deviceInfo, "UTF-8")).concat(returnUrl);
                    logger.info(getMessage("capture.img.debug", ptzServer.concat("hk").concat("?setnum=" + pNo).concat("&deviceinfo="), hikConf));
                    break;
                case ivs:
                    captureUrl = ptzServer.concat("/getpichwivs").concat("?vcuid=").concat("&deviceinfo=" + camera.getIndexCode() + "%23" + camera.getVcuId()).concat("&setnum=" + pNo).concat(returnUrl);
                    break;
                case dh:
                case hw:
                    captureUrl = ptzServer.concat("/getpic").concat("?vcuid=".concat(camera.getVcuId())).concat("&deviceid=" + camera.getIndexCode()).concat("&setnum=" + pNo).concat(returnUrl);
                    break;
            }
            logger.info("拍照地址： {}" + captureUrl);
            String response = HttpRequest.sendRequest2(captureUrl, null);
            if (StringUtils.isNotBlank(response)) {
                Map result = JSON.parseObject(response.substring(1, response.length() - 1), Map.class);
                String taskId = MapUtils.getString(result, "taskid", "");
                if (StringUtils.isNotBlank(taskId)) {
                    captureCache.put(taskId, cameraTaskId);
                }
            } else {
                logger.error(getMessage("capture.img.error", captureUrl));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

        return null;
    }

    /**
     * @param imgUrl
     * @param parentId
     * @return
     */
    @Override
    public String imgRecognize(final String imgUrl,final String parentId) {
        String recognizeUrl = AppConfig.getProperty("recognize.url");
        HttpClient http = new HttpClient();
        PostMethod postMethod;
        File file = null;
        try {
            file = fileStoreService.createNewFile(UUIDGenerator.generate() + ".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(OutputStream out =new FileOutputStream(file)) {
            postMethod = new PostMethod(HttpRequest.encode(recognizeUrl, Constant.UTF_8));
            postMethod.setRequestHeader("Content-Type", "application/json");
            postMethod.setRequestHeader("Accept", "image/*");
            postMethod.getParams().setSoTimeout(3000000);
            Map map = new HashMap();
            Map image = new HashMap();
            image.put("url", imgUrl);
            map.put("image", image);
            RequestEntity entity = new StringRequestEntity(JSON.toJSONString(map), "application/json", "UTF-8");
            postMethod.setRequestEntity(entity);
            logger.info("发送只能识别请求");
            http.executeMethod(postMethod);
            byte[] bytes = postMethod.getResponseBody();
            logger.info("智能分析识别的返回图片字节数{}",bytes.length);
            out.write(bytes);
            String count = postMethod.getResponseHeader("Results-Count").getValue();
            FileStore fileStore = fileStoreService.saveWithThumb(file, parentId);
            logger.info("识别后fileStore的id：[{}]", fileStore.getId());
            return fileStore.getId();
        } catch (UnsupportedEncodingException e) {
            logger.error("智能识别出错{}",e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error("智能识别出错{}",e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 保存离线统计
     *
     * @param camera
     */
    @Transactional(rollbackFor = Exception.class)
    public  void saveOffline(Camera camera) {
        CameraOfflineDuration offlineDuration = new CameraOfflineDuration();
        offlineDuration.setCreateAt(new Date());
        offlineDuration.setIndexCode(camera.getIndexCode());
        offlineDuration.setName(camera.getName());
                    /* 设置 持续时间
                    （根据刷新任务的一次间隔时间来确定）eg. 5min
                     */
        Long time = (Long)AppConfig.getProperties().get("lxsj");
        offlineDuration.setDuration(time * 60 *60);
        try {
            cameraOfflineDao.saveAndFlush(offlineDuration);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    /**
     * 保持预警图片数据与数据库同步
     */
    public void deleteWarnFileFromDb(){
        //获取所有预警数据
        logger.info("####开始执行deleteWarnFileFromDb任务");
        List<Recognition> recogs= cameraRecognitionDao.findByEnabledOrderByCreateAtDesc(true);
        for(int i=0;i<recogs.size();i++){
            Recognition item = recogs.get(i);
            try {
                String oriId= item.getOriginFile();
                String resultId= item.getResultFile();
                FileStore orFileStore = fileStoreDao.findOne(oriId);
                FileStore resultFileStore = fileStoreDao.findOne(resultId);
                if(orFileStore==null||resultFileStore==null){
                    item.setEnabled(false);
                    item.setDescription("未能找到实际图片");
                    cameraRecognitionDao.save(item);
                    logger.info("删除成功!");
                    return;
                }
                File oriFile = new File(orFileStore.getPath());
                File resultFile = new File(resultFileStore.getPath());
                if((!oriFile.exists())||(!resultFile.exists())){
                    item.setEnabled(false);
                    item.setDescription("未能找到实际图片");
                    cameraRecognitionDao.save(item);
                    logger.info("删除成功!");
                }else {
                    logger.info("跳过");
                }
            }catch (Exception er){
                er.printStackTrace();
                logger.error("执行检查删除文件出错,{},{}",item.getId(),er.getMessage());
            }
        }
    }



}