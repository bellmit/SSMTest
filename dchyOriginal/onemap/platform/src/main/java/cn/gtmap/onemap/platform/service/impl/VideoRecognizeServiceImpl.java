package cn.gtmap.onemap.platform.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraRecogWhitelistDao;
import cn.gtmap.onemap.platform.dao.CameraRecognitionDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.RecogWhitelist;
import cn.gtmap.onemap.platform.entity.video.Recognition;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.service.ProjectService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.service.VideoRecognizeService;
import cn.gtmap.onemap.platform.utils.FilesUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.RecognitionResultSubModel;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;

/**
 * 挖掘机识别 service
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/11/14 (c) Copyright gtmap Corp.
 */
@SuppressWarnings("unused")
public class VideoRecognizeServiceImpl extends BaseLogger implements VideoRecognizeService {

    private final ProjectService projectService;

    private final FileStoreService fileStoreService;

    private final CameraRecognitionDao cameraRecognitionDao;

    private final CameraRecogWhitelistDao cameraRecogWhitelistDao;

    private final VideoMetadataService videoMetadataService;

    private final CacheManager cacheManager;

    /**
     * 接口提供商 海康
     */
    private final static String PROVIDER_HK = "hik";

    /**
     * 接口提供商 小网
     */
    private final static String PROVIDER_XW = "xw";
    /**
     * 接口提供商 国图
     */
    private final static String PROVIDER_GT="gt";

    /**
     * 存放从 海康接口返回的 uuid
     */
    private final static String CACHE_UUID = "uuidCache";

    /**
     * 挖掘机识别服务地址
     */
    private String detectUrl;

    /**
     * 接口提供商 (决定了不同的结果处理方式)
     */
    private String provider = PROVIDER_XW;


    @Autowired
    public VideoRecognizeServiceImpl(ProjectService projectService, FileStoreService fileStoreService,
                                     CameraRecognitionDao cameraRecognitionDao, VideoMetadataService videoMetadataService,
                                     CameraRecogWhitelistDao cameraRecogWhitelistDao, CacheManager cacheManager) {
        this.projectService = projectService;
        this.fileStoreService = fileStoreService;
        this.cameraRecognitionDao = cameraRecognitionDao;
        this.videoMetadataService = videoMetadataService;
        this.cameraRecogWhitelistDao = cameraRecogWhitelistDao;
        this.cacheManager = cacheManager;
    }

    /**
     * set  detect url
     *
     * @param detectUrl 识别地址
     */
    public void setDetectUrl(String detectUrl) {
        this.detectUrl = detectUrl;
    }

    /**
     * set provider
     *
     * @param provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * 执行识别任务
     */
    @Override
    public void runTask() {
        DateTime now = new DateTime();
        DateTime start = now.withMillisOfDay(0);
        DateTime end = now.withHourOfDay(23);
        List<FileStore> fileStoreList = projectService.getImgRecordsByTimeSpan(start.toDate(), end.toDate());
        if (fileStoreList.size() == 0) {
            logger.warn("项目拍照记录为空，无法进行识别！24h 后会进行下一次扫描识别");
            return;
        }
        // 遍历每一张照片 进行智能识别
        for (FileStore fileStore : fileStoreList) {
            // 避免重复操作 已有的不会再识别
            List<Recognition> exists = cameraRecognitionDao.findByOriginFile(fileStore.getId());
            if (isNotNull(exists) && exists.size() > 0) {
                continue;
            }
            // 查看该项目是否已加入白名单 
            String proid = fileStore.getParentId();
            List<RecogWhitelist> whitelists = cameraRecogWhitelistDao.findAllByProject(proid);
            if (isNotNull(whitelists) && whitelists.size() > 0) {
                continue;
            }
            try {
                execute(fileStore);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 执行一次智能分析
     *
     * @param origin 原始照片
     * @return 识别结果
     */
    @Override
    public Recognition execute(FileStore origin) {
        Assert.notNull(origin);
        switch (provider){
            case PROVIDER_XW:
                return executeXw(origin);
            case PROVIDER_HK:
                executeHik(origin);
                break;
            case PROVIDER_GT:
                executeGT(origin);
        }
        return null;
    }

    /**
     * 获取所有可用的识别记录
     *
     * @return 所有的识别结果（按camera分组）
     */
    @Override
    public List<Map> getAllRecords() {
//        List<Map> ret = Lists.newArrayList();
//        List<Recognition> recognitions = cameraRecognitionDao.findByEnabled(true);
//        Map groupedMap = recogs2Map(recognitions);
//        for (Object k : groupedMap.keySet()) {
//            String devId = String.valueOf(k);
//            Camera camera = videoMetadataService.getByIndexCode(devId);
//            Map temp = Maps.newHashMap();
//            temp.put("camera", camera);
//            temp.put("recogs", groupedMap.get(k));
//            ret.add(temp);
//        }

        List<Map> ret = Lists.newArrayList();
        List<Recognition> recognitions = cameraRecognitionDao.findByEnabledOrderByCreateAtDesc(true);
        for(int i=0;i<recognitions.size();i++){
            Recognition item = recognitions.get(i);
            Map tmpMap = new HashMap();
            List list2 = Lists.newArrayList();
            list2.add(item);
            tmpMap.put("recogs",list2);
            Camera camera = videoMetadataService.getByIndexCode(item.getCameraId());
            tmpMap.put("camera",camera);
            ret.add(tmpMap);
        }
        return ret;
    }





    /**
     * 根据原始照片查询 结果照片
     *
     * @param fileIds 原始照片 ids
     * @return 识别结果集合
     */
    @Override
    public List<Recognition> getRecogsByOrigin(List<String> fileIds) {
        Iterator<String> iterator = fileIds.iterator();
        List<Recognition> ret = Lists.newArrayList();
        while (iterator.hasNext()) {
            String id = iterator.next();
            List<Recognition> list = cameraRecognitionDao.findByOriginFileAndEnabled(id, true);
            if (isNotNull(list) && list.size() > 0) {
                ret.addAll(list);
            }
        }
        return ret;
    }



    /**
     * 添加进智能识别白名单
     *
     * @param originFileId 原始文件id （filestore id）
     * @return true/false
     */
    @Override
    public boolean add2WhiteList(String originFileId) {
        try {
            FileStore fileStore = fileStoreService.get(originFileId);
            Recognition recognition = cameraRecognitionDao.findOneByOriginFile(originFileId);
            recognition.setEnabled(false);
            cameraRecognitionDao.saveAndFlush(recognition);
            // 关联 proid
            String proid = fileStore.getParentId();
            cameraRecogWhitelistDao.saveAndFlush(new RecogWhitelist(recognition.getCameraId(), proid));
        } catch (Exception e) {
            logger.error("添加识别白名单异常 {}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return true;
    }

    /**
     * 小网接口调用
     *
     * @param origin
     * @return
     */
    private Recognition executeXw(FileStore origin) {
        Assert.notNull(origin);
        String imgUrl = AppConfig.getProperty("omp.url") + "/file/img/" + origin.getId() + ".jpg";
        Map params = Maps.newHashMap();
        params.put("fileurl", imgUrl);
        params.put("thresh", 0.7);
        params.put("model", "smoke");
        params.put("debug", 0);
        params.put("net", "vgg");
        logger.info("对 {} 进行智能识别分析...", origin.getName());
        Map resultMap = (Map) HttpRequest.post(detectUrl, params, HttpRequest.RES_DATA_TYPE.json.name());
        int retCode = MapUtils.getIntValue(resultMap, "result");
        // 1 正常有结果 0 正常无结果 -1 表示有异常
        if (retCode == 1) {
            Set<String> cameras = projectService.getRefCameras(origin.getParentId());
            String indexCode = cameras.iterator().next();
            FileStore retFile = save2FileStore(origin.getId(), indexCode, MapUtils.getString(resultMap, "output_url"));
            if (isNotNull(retFile)) {
                // 记录入库
                assert retFile != null;
                return cameraRecognitionDao.saveAndFlush(new Recognition(MapUtils.getString(resultMap, "file"), indexCode,
                        retFile.getId(), origin.getId(), JSON.toJSONString(resultMap)));
            }
        } else {
            logger.warn("调用小网接口未识别到目标物: {}", JSON.toJSONString(resultMap));
        }
        return null;
    }

    /**
     * 海康接口 调用
     *
     * @param file
     */
    private void executeHik(FileStore file) {
        String imgUrl = AppConfig.getProperty("omp.url") + "/file/img/" + file.getId() + ".jpg";
        Set<String> cameras = projectService.getRefCameras(file.getParentId());
        String indexCode = cameras.iterator().next();
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        Map params = Maps.newHashMap();
        params.put("picUrl", imgUrl);
        params.put("deviceCode", indexCode);
        params.put("deviceName", camera.getName());
        logger.info("对 {} 进行智能识别分析...", file.getName());
        Map resultMap = (Map) HttpRequest.requestHttps(detectUrl, params);
        int retCode = MapUtils.getIntValue(resultMap, "ret");
        String msg = MapUtils.getString(resultMap, "msg");
        // 0 表示正常 其他表示异常
        if (retCode == 0) {
            Map uidMap = MapUtils.getMap(resultMap, "content");
            String uuid = MapUtils.getString(uidMap, "uuid");
            if (StringUtils.isBlank(uuid)) {
                throw new RuntimeException("error");
            }
            cacheManager.getCache(CACHE_UUID).put(uuid, file);
        } else {
            logger.warn("调用海康接口返回异常.返回码:{} 消息： {}", retCode, msg);
        }
    }

    /**
     * 国图 接口
     * @param file
     */
    private Recognition executeGT(FileStore file) {
        String recognizeUrl = AppConfig.getProperty("recognize.url");
        logger.info("***开始执行GT智能监测,recognize.url:"+recognizeUrl);
        HttpClient http = new HttpClient();
        String imgUrl = AppConfig.getProperty("omp.url") + "/file/img/" + file.getId() + ".jpg";
        logger.info("imgUrl为:"+imgUrl);

        try{
            PostMethod postMethod = new PostMethod(HttpRequest.encode(recognizeUrl, Constant.UTF_8));
            postMethod.setRequestHeader("Content-Type", "application/json");
            postMethod.setRequestHeader("Accept", "image/*");
            postMethod.getParams().setSoTimeout(3000000);
            File recognizeFile = null;
            try {
                recognizeFile = fileStoreService.createNewFile(file.getId().concat("_recog_.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try(OutputStream out = new FileOutputStream(recognizeFile)) {
                Map map = new HashMap();
                Map image = new HashMap();
                image.put("url", imgUrl);
                map.put("image", image);
                RequestEntity entity = new StringRequestEntity(JSON.toJSONString(map), "application/json", "UTF-8");
                postMethod.setRequestEntity(entity);
                logger.info("发送只能识别请求");
                http.executeMethod(postMethod);
                byte[] bytes = postMethod.getResponseBody();
                out.write(bytes);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage());
            }
            //保存
            Set<String> cameras = projectService.getRefCameras(file.getParentId());
            String indexCode = cameras.iterator().next();
            FileStore retFile=null;
            logger.info("***开始保存");
            logger.info("***indexCode:"+indexCode);
            if (recognizeFile.exists()) {
                retFile= fileStoreService.saveWithThumb(recognizeFile, indexCode);
                logger.info("***保存recognize成功");
            }
            if (retFile!=null) {
                // 记录入库
                assert retFile != null;
                logger.info("***保存成功");
                return cameraRecognitionDao.saveAndFlush(new Recognition(recognizeFile.getName(), indexCode,
                        retFile.getId(), file.getId()));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("智能识别出错{}",e.getLocalizedMessage());
        } catch (IOException e) {
            logger.error("智能识别出错{}",e.getLocalizedMessage());
        }
        return null;
    }
    
    /**
     * 描述：保存分析图片(海康江阴)
     * @author 卜祥东
     * 2019年5月5日 上午10:51:11
     * @param oldFileStore 原始图片
     * @param diffXyJsonStr 差异坐标串 (JsonStr)
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public Recognition saveRecogHikJiangyin(FileStore oldFileStore,String diffXyJsonStr) {
    	try{
    		File oldFile = new File(oldFileStore.getPath());
    		File recognizeFile = fileStoreService.createNewFile(oldFileStore.getId().concat("_recog_.jpg"));
    		recognizeFile = FilesUtils.generateMarkImg(oldFile, diffXyJsonStr, recognizeFile);
    		//保存
    		Set<String> cameras = projectService.getRefCameras(oldFileStore.getParentId());
    		String indexCode = cameras.iterator().next();
    		FileStore retFile=null;
    		logger.info("***开始保存");
    		logger.info("***indexCode:"+indexCode);
    		if (recognizeFile.exists()) {
    			retFile= fileStoreService.saveWithThumb(recognizeFile, oldFileStore.getParentId());
    			logger.info("***保存recognize成功");
    		}
    		if (retFile!=null) {
    			// 记录入库
    			assert retFile != null;
    			logger.info("***保存成功");
    			return cameraRecognitionDao.saveAndFlush(new Recognition(recognizeFile.getName(), indexCode,
    					retFile.getId(), oldFileStore.getId(),diffXyJsonStr));
    		}
    	} catch (Exception e) {
    		logger.error("保存智能识别图片异常{}",e);
    	}
    	return null;
    }

    /**
     * 按 cameraid 分组
     *
     * @param list 识别结果
     * @return 分组map
     */
    private Map recogs2Map(List<Recognition> list) {
        Map<String, List<Map>> map = Maps.newHashMap();
        for (Recognition recognition : list) {
            String cameraId = recognition.getCameraId();
            if (map.containsKey(cameraId)) {
                List list1 = map.get(cameraId);
                list1.add(recognition);
                map.put(cameraId, list1);
            } else {
                List list2 = Lists.newArrayList();
                list2.add(recognition);
                map.put(cameraId, list2);
            }
        }
        return map;
    }

    /**
     * 保存结果记录
     *
     * @param originId 原始文件id
     * @param imgUrl   照片url
     * @return 文件存储实体
     */
    private FileStore save2FileStore(String originId, String cameraIndexCode, String imgUrl) {
        FileOutputStream outStream = null;
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //超时响应时间为30秒
            conn.setConnectTimeout(30000);
            InputStream inStream = conn.getInputStream();
            byte[] data = IOUtils.toByteArray(inStream);
            File imgFile = fileStoreService.createNewFile(originId.concat("_recog_.jpg"));
            outStream = new FileOutputStream(imgFile);
            outStream.write(data);
            //关闭输出流
            outStream.close();
            // 图片入库
            if (imgFile.exists()) {
                return fileStoreService.saveWithThumb(imgFile, cameraIndexCode);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(outStream);
        }
        return null;
    }

    /**
     * 根据fileID修改camera_recog内容
     * @param fileID
     * @param status 0预警1建设用地2违法用地
     * @return
     */
    @Override
    public Recognition updateRecogStatusByFileID(String fileID,int status){
        //找到实体
        Recognition recognition = cameraRecognitionDao.findRecognitionByOriginFile(fileID);
        recognition.setStatus(status);
        return cameraRecognitionDao.save(recognition);
    }

    public boolean deleteRecog(String id){
        cameraRecognitionDao.delete(id);
        return true;
    }

    public Recognition findByOriginal(String originalID){
        Recognition recognition = cameraRecognitionDao.findRecognitionByOriginFile(originalID);
        return recognition;
    }

    /**
     * 描述：保存分析图片(瞭望神州)
     * @author 卜祥东
     * 2019年7月17日 上午10:51:11
     * @param oldFileStore 原始图片
     * @param diffXyJsonStr 差异坐标串 (JsonStr)
     * @return
     */
	@Override
	public Recognition saveRecogByLwsz(FileStore oldFileStore, String diffXyJsonStr) {
		try{
    		File oldFile = new File(oldFileStore.getPath());
    		File recognizeFile = fileStoreService.createNewFile(oldFileStore.getId().concat("_recog_.jpg"));
    		List<RecognitionResultSubModel> recognitionResultList = JSON.parseArray(diffXyJsonStr, RecognitionResultSubModel.class);
    		recognizeFile = FilesUtils.generateMarkImgByLwsz(oldFile, recognitionResultList, recognizeFile);
    		//保存
    		Set<String> cameras = projectService.getRefCameras(oldFileStore.getParentId());
    		String indexCode = cameras.iterator().next();
    		FileStore retFile=null;
    		logger.info("***开始保存");
    		logger.info("***indexCode:"+indexCode);
    		if (recognizeFile.exists()) {
    			retFile= fileStoreService.saveWithThumb(recognizeFile, oldFileStore.getParentId());
    			logger.info("***保存recognize成功");
    		}
    		if (retFile!=null) {
    			// 记录入库
    			assert retFile != null;
    			logger.info("***保存成功");
    			return cameraRecognitionDao.saveAndFlush(new Recognition(recognizeFile.getName(), indexCode,
    					retFile.getId(), oldFileStore.getId(),diffXyJsonStr));
    		}
    	} catch (Exception e) {
    		logger.error("保存智能识别图片异常{}",e);
    	}
		return null;
	}

}
