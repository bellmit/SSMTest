package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;
import cn.gtmap.onemap.platform.entity.video.CameraPatrolLog;
import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.*;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/16 9:02
 */
public interface VideoMetadataService {

    /***
     * get config
     * @return
     */
    @Deprecated
    Map getConfig();

    /**
     * save camera
     * @param camera
     * @return
     */
    Camera saveCamera(Camera camera);

    /**
     * 从 video.cfg 中获取数据集合 并入库
     * @return
     */
    List<Camera> parseCfgToDb(String sr,boolean replaceAll);

    /**
     * 通过名称获取
     * @param name
     * @return
     */
    List<Camera> getByCameraName(String name);

    /**
     * find By IndexCode
     * @param indexCode
     * @return
     */
    Camera getByIndexCode(String indexCode);


    /**
     * find By IndexCodes in
     * @param indexCodes
     * @return
     */
    List<Camera> getByIndexCodeIn(Set<String> indexCodes);

    /**
     * 分页
     * @param pageRequest
     * @return
     */
    Page<Camera> getPage(PageRequest pageRequest);

    /**
     * 获取所有设备
     * @param
     * @return
     */
    List<Camera> getAll();


    /**
     * 获取所有设备
     * @param
     * @return
     */
    List<Camera> getAll(String loginName) throws Exception;



    /**
     * find RegionNames
     *
     * @return
     */
    List<String> findRegionNames();


    /**
     * 查询行政区根目录
     * @return
     */
    List<CameraRegion> queryRootRegion();

//    HashMap  getCameraRegionCount();
    /**
     * query All Region
     * @return
     */
    List<CameraRegion> queryAllRegion();

    /**
     * 查询子行政区目录
     * @param parentId
     * @return
     */
    List<CameraRegion> queryRegionByParent(String parentId);

    /**
     * 根据行政区查询摄像头
     * @param region
     * @return
     */
    List<Camera> queryCameraByRegion(String region);


    /**
     * 根据行政区查询摄像头
     * @param region
     * @return
     */
    Page<Camera> queryCameraByRegion(String region,Pageable pageable);

    Page<Camera> queryCameraByRegions(Set<String> regions,Pageable pageable);

    /**
     * 根据名称或 indexcode 查询
     * @param nameOrIndexCode
     * @param page
     * @param size
     * @return
     */
    Page<Camera> findByNameLikeOrIndexCode(String nameOrIndexCode,int page,int size);

    /**
     * 获取缓冲区内的摄像头
     * @param geo
     * @param bufferSize
     * @return
     */
    List<Camera> getByGeo(String geo, double bufferSize);

    /**
     *  get regions by name
     * @param name
     * @return
     */
    List<Map> getRegions(String name);
    
    /***
     * get region by id
     * @param regionId
     * @return
     */
    Map getRegion(String regionId);

    /***
     *get devices by region
     * @param regionId
     * @return
     */
    List<Map> getDevicesByRegion(String regionId);



    /***
     *
     * @param name
     * @return
     */
    String updateUnitName(String name);

    /***
     * update region
     * @param regionId
     * @param content
     * @return
     */
//    Map updateRegion(String regionId,String content);

    /***
     * update device
     * @param deviceId
     * @param content
     * @return
     */
    Map updateDevice(String deviceId,String regionId,String content);


    /***
     * delete region by id
     * @param regionId
     */
    void deleteRegion(String regionId);

    /***
     * delete device by id
     * @param id
     */
    void deleteDevice(String id);

    /***
     * 从地图服务中导入config
     * @param queryUrl
     */
    void importConfig(String queryUrl,String pt);

    /**
     * 从excel文件中获取配置
     */
    void importConfigByFile(MultipartHttpServletRequest request);

    /**
     * 根据行政区id查询探头总数
     * @param parentId
     * @return
     */
    Integer countCameraByRegionId(String parentId);

    /**
     * 根据行政区名称获取CameraRegion
     * @param names
     * @return
     */
    List<CameraRegion> findAllByNameIn(List<String> names);

    /**
     *
     * @param layerName
     * @param where
     * @param useCache
     * @return
     * @throws IOException
     */
    List<String> getOverlayCamera(String layerName,String where,boolean useCache) throws IOException;

    Map getLayerCache();

    Page<CameraAutoShot>  findCameraAutoShot(Date start, Date end, List<String> cameraIds, Pageable pageable);

    Page<CameraAutoShot> findCameraAutoShot(Date start, Date end, Pageable pageable);

    List<Map> searchCameraLog(Date start, Date end,List<String> result,List<String> userIds) throws Exception;

    List<Map> searchCameraLogST(Date start, Date end,List<String> result) throws Exception;

    List<String> getCameraIdByRegionsId(String id);

    CameraRegion getRegionById(String id);

    List<String> getProCameraIdByRegionsId(String RegionsId);

    Map getCameraCountData() throws IOException;

    /**
     * 统计连云港所摄像头
     * @param start
     * @param end
     * @param organName
     * @param regionCode
     * @return
     * @throws Exception
     */
    Map countCameraLog(Date start ,Date end,String organName,String regionCode) throws Exception;


    Map countCameraLogST(Date start ,Date end, String regionCode) throws Exception;

    List<Map> searchCameraLogByIndexCode(Date start,Date end,String cameraID,List<String> userIds);
}
