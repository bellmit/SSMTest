package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraDao;
import cn.gtmap.onemap.platform.dao.CameraRegionDao;
import cn.gtmap.onemap.platform.dao.FileCacheDao;
import cn.gtmap.onemap.platform.dao.ProjectCameraRefDao;
import cn.gtmap.onemap.platform.entity.video.*;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.nio.DoubleBuffer;
import java.util.*;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/6/3 18:45
 */
public class VideoServiceImpl extends BaseLogger implements VideoService {

    @Autowired
    public GeometryService geometryService;



    @Autowired
    public AuthorizationService authorizationService;

    @Autowired
    private ProjectCameraRefDao projectCameraRefDao;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private CameraDao cameraDao;

    @Autowired
    private CameraRegionDao cameraRegionDao;




    /**
     * 监控点配置信息
     */
    private Map videoCfg;

    /**
     *
     */
    @Override
    public void init() {

    }

    /***
     * 根据geometry获取其附近的监控点
     * @param geojson        geojson 必须携带crs信息
     * @param bufferSize
     * @return
     */
    @Override
    public List<Camera> getPoiCameras(String geojson, String bufferSize,String xzqdm) {
        Object feature = geometryService.readUnTypeGeoJSON(geojson);
        Geometry poiGeo=null;
        List<Camera> cameras;
        List<Camera> result = new ArrayList<Camera>();
        if (feature instanceof SimpleFeature) {
            SimpleFeature simpleFeature = (SimpleFeature) feature;
            poiGeo = (Geometry) simpleFeature.getDefaultGeometry();
            CoordinateReferenceSystem crs = simpleFeature.getFeatureType().getCoordinateReferenceSystem();
            if (isNotNull(crs)) {
                poiGeo = geometryService.project(poiGeo, crs, geometryService.parseUndefineSR("EPSG:4528"));
            }
        }else if(feature instanceof FeatureCollection){
            FeatureCollection featureCollection = (FeatureCollection)feature;
            while (featureCollection.features().hasNext()){
                SimpleFeature feature1 = (SimpleFeature)featureCollection.features().next();
                poiGeo =(Geometry) feature1.getDefaultGeometry();
                poiGeo= poiGeo.getCentroid();
//                break;
            }
        }
        else {
            throw new RuntimeException(getMessage("geometry.type.unsupported"));
        }
        if (isNotNull(poiGeo)) {
            if(xzqdm!=null&&!xzqdm.equals("")){
                xzqdm = xzqdm.substring(0,4)+"00";
                cameras = cameraDao.findCamerasByPlatform(xzqdm);
            }else {
                cameras  = cameraDao.findAll();
            }
            if (StringUtils.isNotBlank(bufferSize))
                poiGeo = preojectToTY(poiGeo);
                poiGeo = geometryService.buffer(poiGeo, Double.valueOf(bufferSize));
            for (Camera camera : cameras) {
                Point point= getPointFromXY(camera.getX(),camera.getY());
                point=(Point) preojectToTY(point);
                Geometry intersectGeo = poiGeo.intersection(point);
                if (isNotNull(intersectGeo) && !intersectGeo.isEmpty())
                    result.add(camera);
            }
        }
        return result;
    }

    @Override
    public List<Camera> getPoiCameras(Geometry poiGeo, String bufferSize,String xzqdm) {
        List<Camera> result = new ArrayList<Camera>();
        List<Camera> cameras;
        if (isNotNull(poiGeo)) {
//            poiGeo=geometryService.project(poiGeo,geometryService.getCRSBySRID("4490"),geometryService.getCRSBySRID("4528"));
            poiGeo= preojectToTY(poiGeo);
            if(xzqdm!=null&&!xzqdm.equals("")){
                xzqdm = xzqdm.substring(0,4)+"00";
                cameras = cameraDao.findCamerasByPlatform(xzqdm);
            }else {
                cameras  = cameraDao.findAll();
            }

            if (StringUtils.isNotBlank(bufferSize))
                poiGeo = geometryService.buffer(poiGeo, Double.valueOf(bufferSize));
            for (Camera camera : cameras) {

//                Geometry point2=geometryService.project(point,geometryService.getCRSBySRID("4490"),geometryService.getCRSBySRID("4528"));
                Point point= getPointFromXY(camera.getX(),camera.getY());
                Geometry intersectGeo = poiGeo.intersection(point);
                if (isNotNull(intersectGeo) && !intersectGeo.isEmpty())
                    result.add(camera);
            }
        }
        return result;
    }

    private Geometry preojectToTY(Geometry geometry){
        Point point= null;
        point = geometry.getCentroid();
//        if(geometry instanceof Point){
//            point = geometry.getCentroid();
//        }else {
//            point= geometry.getCentroid();
//        }
        if(point.getX()<10000){
            geometry=geometryService.project(geometry,geometryService.getCRSBySRID("4490"),geometryService.getCRSBySRID("4528"));
        }
        return geometry;
    }

    /***
     * 查询某个用户对于某个设备已授权的操作
     * @param indexCode 设备编号
     * @param userId    用户id
     * @return
     */
    @Override
    public Set<Operation> getAuthorizedOperations(String indexCode, String userId) {
        Set<Operation> set = Sets.newHashSet();
        if (isNull(userId) || isNull(indexCode)) {return set;}
        try {
            Set<Privilege> privileges = authorizationService.getPermittedPrivileges(userId, VIDEO_RESOURCE);
            for (Privilege privilege : privileges) {
                if (indexCode.equals(privilege.getResource())) {
                    set.addAll(privilege.getOperations());
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return set;
    }

    @Override
    public Ptz getPTZ(String indexCode) {
        return null;
    }

    @Override
    public void snapShot(String indexCode, String startTime, String endTime, String proId, String presetId){

    }

    /***
     * 获取设备indexcode集合
     * @return
     */
    @Override
    public List<String> getIndexCodes() {
        List<String> indexCodes = new ArrayList<String>();
        List<Map> regions = (List<Map>) videoCfg.get("regions");
        for (Map region : regions) {
            int enable = MapUtils.getIntValue(region, "enabled", 1);
            if (enable == 1) {
                List<Map> devices = (List<Map>) region.get("devices");
                for (Map device : devices) {
                    if (MapUtils.getInteger(device, "enabled", 1) == 1) {
                        indexCodes.add(MapUtils.getString(device, "indexCode"));
                    }
                }
            }
        }
        return indexCodes;
    }

    /**
     * 获取单个行政区indexcode集合
     *
     * @param regionNames
     * @return
     */
    public List<String> getIndexCodesByRegion(String regionNames) {
        Set<String> sets = new HashSet<String>();
        String[] strArr = regionNames.split("\\|");
        for(String item:strArr){
            if(item.isEmpty()) continue;
            sets.add(item);
        }
        return cameraDao.findIndexCodeByRegionNameIn(sets);
    }

    /***
     * 获取当前用户针对当前操作已授权的所有资源
     * @param operaName
     * @return
     */
    private Set<String> getAuthorizedRes(String operaName) {
        if (isNull(operaName)) operaName = Operation.VIEW;
        User user = SecHelper.getUser();
        Set<String> set = Sets.newHashSet();
        try {
            if (isNull(user) || SecHelper.isAdmin() || SecHelper.isGuest()) return set;
        } catch (Exception e) {
            return set;
        }
        Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getId(), VIDEO_RESOURCE);
        Iterator iterator = privileges.iterator();
        if (iterator.hasNext()) {
            for (Privilege privilege : privileges) {
                for (Operation operation : privilege.getOperations()) {
                    if (operaName.equals(operation.getName())) {
                        Set<Privilege> privilegesChildren = authorizationService.getPermittedPrivileges(user.getId(), VIDEO_RESOURCE.concat(privilege.getResource()));
                        for (Privilege privilege1 : privilegesChildren) {
                            try {
                                set.add(privilege1.getResource());
                                Set PointSet = getP(user.getId(), privilege.getResource().concat(":" + privilege1.getResource()));
                                set.addAll(PointSet);
                            } catch (Exception e) {
                                set.add(privilege1.getResource());
                            }
                        }
                        set.add(privilege.getResource());
                        break;
                    }
                }
            }
        }
        return set;
    }

    /**
     * 获取子节点摄像头标识
     *
     * @param userId 用户ID
     * @param Sname  资源名
     * @return
     */
    private Set<String> getP(String userId, String Sname) {
        Set<String> ps = Sets.newHashSet();
        Set<Privilege> PointChild = authorizationService.getPermittedPrivileges(userId, VIDEO_RESOURCE.concat(Sname));
        try {
            for (Privilege privilege2 : PointChild) {
                ps.add(privilege2.getResource());
                Set Sets = getP(userId, Sname.concat(":" + privilege2.getResource()));
                ps.addAll(Sets);
            }
        } catch (Exception e) {
            ps.add(String.valueOf(PointChild));
        }
        return ps;
    }

    /**
     * 根据proId获取设备信息
     *
     * @param proId
     * @return
     */
    @Override
    public List<Camera> getCamerasByProId(String proId) {
        List<ProjectCameraRef> projectCameraRefs = projectCameraRefDao.findByProId(proId);
        Set<String> indexCodes = new HashSet<String>();
            for (ProjectCameraRef projectCameraRef : projectCameraRefs) {
                indexCodes.add(projectCameraRef.getCameraId());
            }
        return videoMetadataService.getByIndexCodeIn(indexCodes);
    }

    /**
     * 由子类实现
     * {@link HikVideoServiceImpl}
     *
     * @param outSR
     * @return
     */
    @Override
    public List<Camera> getAllCamerasByWs(String outSR) {
        return null;
    }

    /**
     * 由子类实现
     * {@link HikVideoServiceImpl}
     *
     * @param operaType
     * @param cameraIndexCode
     * @return
     */
    @Override
    public String getCameraXml(OperaType operaType, String cameraIndexCode) {
        return null;
    }

    /**
     * 由子类实现
     *
     * @param indexCode 监控点编号（多个监控点可用英文逗号分隔）
     * @return
     */
    @Override
    public List<CameraView> getCameraView(String indexCode) {
        return null;
    }


    /***
     * 由子类实现
     * @param indexCode  关联的设备
     * @param target    转向的目标点
     */
    @Override
    public void setPTZ(String indexCode, Point target) {
    }

    /**
     * 由子类实现
     *
     * @param indexCode
     * @param target
     * @param loginInfo
     */
    @Override
    public void setPTZ(String indexCode, Point target, Map loginInfo) {
    }

    /**
     * 由子类实现
     *
     * @param indexCode
     * @param p
     * @param t
     * @param z
     */
    @Override
    public void setPTZ(String indexCode, String p, String t, String z) {
    }

    @Override
    public int ptzMoveControl(String cameraCode, Constant.IVS_PTZ_CODE controlCode, String param1, String param2) {
        return 0;
    }

    @Override
    public String getRtspUrl(String cameraId){
        return null;
    }

    @Override
    public Camera Save(Camera camera) {
        return cameraDao.save(camera);
    }

    @Override
    public Camera findById(String id) {
        return cameraDao.findById(id);
    }

    @Override
    public String SetFileID(String cameraID,String fileID){
        Camera camera= cameraDao.findById(cameraID);
        camera.setFileId(fileID);
        cameraDao.save(camera);
        return cameraID;
    }

    public List<CameraRegion> getChildrenRegion(CameraRegion region){
        //获取所有子行政区
        //递归获取叶节点行政区
        List<CameraRegion> leafRegion = new ArrayList<CameraRegion>();
        if(region.getChildren()!=null&&region.getChildren().size()>0){
            //递归
            List<CameraRegion> children = region.getChildren();
            for(int i=0;i<children.size();i++){
                CameraRegion item = children.get(i);
                List result = getChildrenRegion(item);
                leafRegion.addAll(result);
            }
        }else {
            leafRegion.add(region);
        }
        return leafRegion;
    }



    //根据regionid获取所有cameras
    @Override
    public Map getCamerasCountByRegID(String cameraId){
        Map result =new HashMap();
        //根据id获取region

        int regOnLine=0;
        int regOffLine=0;
        if(cameraId!=null&&!cameraId.isEmpty()){
            CameraRegion region = cameraRegionDao.getOne(cameraId);
            List<CameraRegion> childrenRegion = getChildrenRegion(region);
            for (CameraRegion item :childrenRegion){
                int offLineCount= cameraDao.countCamerasByStatusAndRegionName(0,item.getName());
                int onLineCount= cameraDao.countCamerasByStatusAndRegionName(1,item.getName());
                regOnLine =regOnLine+onLineCount;
                regOffLine =regOffLine+offLineCount;
                result.put("regOnLine",regOnLine);
                result.put("regOffLine",regOffLine);
            }
        }
        //总体结果
        int onLine;
        int offLine;
        offLine= cameraDao.countCamerasByStatus(0);
        onLine= cameraDao.countCamerasByStatus(1);

        result.put("onLine",onLine);
        result.put("offLine",offLine);
        return result;
    }



    @Override
    public HashMap getRegionCount() {
        HashMap result = new HashMap();
        List<CameraRegion> cameraRegions = videoMetadataService.queryRootRegion();

        for(CameraRegion regionItem :cameraRegions){
            int onLine = 0;
            int offLine =0;
            HashMap tempResult = getRegionChildCount(regionItem,result);
            Iterator iter = tempResult.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                HashMap val = (HashMap) entry.getValue();
                if(val.get("isLeaf")!=null&&val.get("isLeaf").equals(true)){
                    onLine+=Integer.parseInt(val.get("regOnLine").toString());
                    offLine+=Integer.parseInt(val.get("regOffLine").toString());
                }
            }
            //对tempResult进行统计
            result.putAll(tempResult);
            HashMap currentResult = new HashMap();
            currentResult.put("regOnLine",onLine);
            currentResult.put("regOffLine",offLine);
            result.put(regionItem.getId(),currentResult);
        }
        return result;
    }

    private HashMap getRegionChildCount(CameraRegion region,HashMap result){
        HashMap tempResult = new HashMap();
        int onLine =0;
        int offLine=0;
        if(region.getChildren()!=null&&region.getChildren().size()>0){
            //不是叶子节点
            for(CameraRegion item:region.getChildren()){
                HashMap itemResult= getRegionChildCount(item,result);
                //统计后再加入
                Iterator iter = itemResult.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    HashMap val = (HashMap) entry.getValue();
                    if(val.get("isLeaf")!=null&&val.get("isLeaf").equals(true)){
                        onLine+=Integer.parseInt(val.get("regOnLine").toString());
                        offLine+=Integer.parseInt(val.get("regOffLine").toString());
                    }
                }
                tempResult.putAll(itemResult);
            }
            //增加自身
            HashMap currentResult = new HashMap();
            currentResult.put("regOnLine",onLine);
            currentResult.put("regOffLine",offLine);
            tempResult.put(region.getId(),currentResult);
        }else {
            //叶子节点
            //叶子节点父节点进行统计
            HashMap countStatus=(HashMap) getCamerasCountByRegID(region.getId());
            countStatus.put("isLeaf",true);
//            onLine +=Integer.parseInt(countStatus.get("regOnLine").toString());
//            offLine+=Integer.parseInt(countStatus.get("regOffLine").toString());d
            tempResult.put(region.getId(),countStatus);
        }

        return tempResult;
    }


    @Override
    public Point getPointFromXY(double x,double y){
        List tmp = new ArrayList();
        tmp.add(x);
        tmp.add(y);
        Point point = GeometryUtils.createPoint(new JSONArray(tmp));
        point= (Point) preojectToTY(point);
        return point;
    }

    @Override
    public List<String> getIndexCodesByXzq(String xzqh) {
        List<String> indexCodeList = new ArrayList<String>();
        List<String> childRegionNames = null;
        if(StringUtils.isEmpty(xzqh) || "320100".equals(xzqh)){
            childRegionNames = cameraDao.findRegionNames();
        }else {
            CameraRegion region = cameraRegionDao.findById(xzqh);
            childRegionNames = cameraRegionDao.findNameByParent(region);
        }
        if(!CollectionUtils.isEmpty(childRegionNames)){
            for (String regionName : childRegionNames){
                List<Camera> cameraList = cameraDao.findByRegionName(regionName);
                if(!CollectionUtils.isEmpty(cameraList)){
                    for (Camera c : cameraList){
                        indexCodeList.add(c.getIndexCode());
                    }
                }
            }
        }
        return indexCodeList;
    }
}
