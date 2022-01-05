package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.PresetDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraView;
import cn.gtmap.onemap.platform.entity.video.Preset;
import cn.gtmap.onemap.platform.entity.video.Ptz;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.VideoUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Ivs Video Service
 *
 * @author monarchCheng
 * @create 2018-01-19 15:55
 **/
@Service
public class IvsVideoServiceImpl extends VideoServiceImpl {

    private final static String PTZ_URL = "/api/ptz";
    private final static String DEVICE_LIST_URL = "/api/devices";
    private final static String SNAP_SHOT_URL = "/api/snapshot/take";
    private final static String SHOT_LIST_URL = "/api/snapshot/query";
    private final static String DELETE_SHOT_URL = "/api/snapshot/del";
    private final static String IVS_SERVER = AppConfig.getProperty("ivsServer.url");
    private final static String PTZ_ABSOLUTE_MOVE = "PTZ_ABSOLUTE_MOVE";

    private final VideoMetadataService videoMetadataService;

    private final FileStoreService fileStoreService;

    private final PresetDao presetDao;

    @Autowired
    public IvsVideoServiceImpl(VideoMetadataService videoMetadataService, FileStoreService fileStoreService, PresetDao presetDao) {
        this.videoMetadataService = videoMetadataService;
        this.fileStoreService = fileStoreService;
        this.presetDao = presetDao;
    }

    /**
     * 获取设备列表
     *
     * @return
     */
    public List<Map> getDeviceList() {
        String devices = (String) HttpRequest.get(IVS_SERVER.concat(DEVICE_LIST_URL), "", null);
        List<Map> deviceList = new ArrayList<Map>();
        if (StringUtils.isNotBlank(devices)) {
            deviceList = JSON.parseObject(devices, List.class);
        }
        return deviceList;
    }

    /**
     * 获取可视域
     *
     * @param indexCodes 监控点编号（多个监控点可用英文逗号分隔）
     * @return
     */
    @Override
    public List<CameraView> getCameraView(String indexCodes) {
        List<String> indexCodeList = Arrays.asList(indexCodes.split(","));
        List<CameraView> result = new ArrayList<CameraView>();
        try {
            for (String indexCode : indexCodeList) {
                CameraView cameraView = new CameraView();
                Ptz ptz = getPTZ(indexCode);
                if ((ptz.getP() + ptz.getT() + ptz.getZ()) == 0) {
                    continue;
                }
                cameraView.setIndexCode(indexCode);
                cameraView.setAzimuth(ptz.getP());
                double zoom = ptz.getZ();
                double horizontalValue = 2 * Math.toDegrees(Math.atan(Math.sqrt(3) / (zoom + 1) / 2));
                cameraView.setHorizontalAngle(horizontalValue);
                // 根据Z计算最大可视半径
                Double vRadius = Math.abs(20 / Math.tan(Math.toRadians(horizontalValue)));
                if (vRadius > 200) {
                    vRadius = 200D;
                }
                // 根据T计算可视半径  30是高度
                Double radius = Math.tan(Math.toRadians(90 - ptz.getT())) * 30;
                radius = Math.abs(radius);
                cameraView.setViewRadius(radius > vRadius ? vRadius : radius);
                logger.info(getMessage("camera.scope.view.info", cameraView.getIndexCode(), cameraView.toString()));
                result.add(cameraView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }


    /**
     * set ptz by value
     *
     * @param indexCode
     * @param p
     * @param t
     * @param zi
     */
    @Override
    public void setPTZ(String indexCode, String p, String t, String zi) {
        Double x = Double.valueOf(p);
        Double y = Double.valueOf(t);
        Double z = Double.valueOf(zi);
        setPTZ(indexCode, x, y, z);
    }


    /***
     * set ptz by point
     * @param indexCode
     * @param target
     */
    @Override
    public void setPTZ(String indexCode, Point target) {
        assert indexCode != null;
        assert target != null;
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        List list = new ArrayList();
        list.add(camera.getX());
        list.add(camera.getY());
        Point source = GeometryUtils.createPoint(new JSONArray(list));
        CoordinateReferenceSystem srcCrs = null;
        CoordinateReferenceSystem targetCrs = null;
        try {
            srcCrs = geometryService.getCrsByCoordX(source.getX());
            targetCrs = geometryService.getCrsByCoordX(target.getX());
        }catch (Exception e){
            logger.error("getCrsByCoordX with exception:" + e.toString());
            throw new RuntimeException(e);
        }
        try {
            if (srcCrs instanceof GeographicCRS)
                source = (Point) geometryService.project(source, srcCrs, geometryService.parseUndefineSR(Constant.EPSG_2364));
            if (targetCrs instanceof GeographicCRS)
                target = (Point) geometryService.project(target, srcCrs, geometryService.parseUndefineSR(Constant.EPSG_2364));
            //计算p t z
            //计算p t z
            logger.debug("start calculate ....");
            Ptz ptz = VideoUtils.calculatePtz(source, target, camera);
            logger.info("get the PTZ :" + ptz.toString());
            setPTZ(indexCode, ptz.getP(), ptz.getT(), ptz.getZ());
        } catch (Exception e) {
            logger.error("setPTZ with error:" + e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * 请求拍照
     *
     * @param indexCode
     * @param startTime
     * @param endTime
     * @param proId
     */
    @Override
    public void snapShot(String indexCode, String startTime, String endTime, String proId, String presetId) {
        Preset preset = presetDao.findOne(presetId);
        int code = setPTZ(indexCode, preset.getX(), preset.getY(), preset.getZ());
        if (code == 0) {
            saveShotImg(indexCode, startTime, endTime, proId);
        }
    }

    /**
     * 从ivs 获取 ptz
     *
     * @param indexCode
     * @return
     */
    @Override
    public Ptz getPTZ(String indexCode) {
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        String queryString = "domainCode=".concat(camera.getVcuId()).concat("&cameraCode=").concat(indexCode);
        String ptzStr = (String) HttpRequest.get(IVS_SERVER.concat(PTZ_URL), queryString, null);
        if (ptzStr != null) {
            Map map = JSON.parseObject(ptzStr, Map.class);
            Ptz ptz = new Ptz(MapUtils.getDouble(map, "x") / 10, MapUtils.getDouble(map, "y") / 10, MapUtils.getInteger(map, "z"));
            return ptz;
        }
        return null;
    }

    /**
     * set ptz by value
     *
     * @param indexCode
     * @param p
     * @param t
     * @param zi
     * @return
     */
    private int setPTZ(String indexCode, double p, double t, double zi) {
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        Double x = p * 10;
        Double y = t * 10;
        Double z = zi > 10 ? 10 : zi;

        Map data = Maps.newHashMapWithExpectedSize(6);
        data.put("cameraCode", indexCode);
        data.put("domainCode", camera.getVcuId());
        data.put("ptzControlCode", PTZ_ABSOLUTE_MOVE);
        data.put("param1", x + "," + y + "," + z);
        data.put("param2", "5,5,5");
        data.put("lockStatus", 0);
        int code = Integer.valueOf(HttpRequest.post(IVS_SERVER.concat(PTZ_URL), data, null).toString());
        if (code != 0) {
            logger.error("设置ptz异常，indexwuliao Code【{}】，code【{}】", indexCode, code);
        }
        return code;
    }

    private void snapShot(String indexCode) {
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        String queryString = "domainCode=".concat(camera.getVcuId()).concat("&cameraCode=").concat(indexCode);

        int code = Integer.valueOf(HttpRequest.get(IVS_SERVER.concat(SNAP_SHOT_URL), queryString, null).toString());
        if (code != 0) {
            logger.error("请求拍照异常，indexCode【{}】，code【{}】", indexCode, code);
        }

    }

    private void saveShotImg(String indexCode, String startTime, String endTime, String parentId) {
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        String queryString = "domainCode=".concat(camera.getVcuId()).concat("&cameraCode=").concat(indexCode).concat("startTime=".concat(startTime).concat("endTime=").concat(endTime));
        String imgInfo = (String) HttpRequest.get(IVS_SERVER.concat(SHOT_LIST_URL), queryString, null);
        Map map = JSON.parseObject(imgInfo, Map.class);
        String imgUrl = MapUtils.getString(map, "url");
        int imgId = MapUtils.getInteger(map, "id");
        if (StringUtils.isBlank(imgUrl)) {
            return;
        }
        byte[] bytes = HttpRequest.get(imgUrl, queryString, false);
        String uuid = UUIDGenerator.generate();
        File file = new File(System.getProperty("java.io.tmpdir") + uuid + ".jpg");
        try (OutputStream out = new FileOutputStream(file);){
            out.write(bytes);
            out.flush();
            out.close();
            FileStore fileStore = fileStoreService.saveWithThumb(file, parentId);
            file.delete();
            //保存完毕调用接口删除照片
            deleteImg(indexCode, imgId);
        } catch (Exception e) {
            throw new RuntimeException("文件写入异常");
        }
    }

    @Override
    public int ptzMoveControl(String cameraCode, Constant.IVS_PTZ_CODE controlCode, String param1, String param2) {
        Map data = new HashMap();
        Camera camera = videoMetadataService.getByIndexCode(cameraCode);
        data.put("cameraCode", cameraCode);
        data.put("domainCode", camera.getVcuId());
        data.put("ptzControlCode", controlCode.name());
        data.put("param1", param1);
        data.put("param2", param2);
        data.put("lockStatus", 0);
        int code = Integer.valueOf(HttpRequest.post(IVS_SERVER.concat(PTZ_URL), data, null).toString());
        if (code != 0) {
            logger.error("云台控制异常，indexCode【{}】，code【{}】", cameraCode, code);
        }
        return code;
    }

    /**
     * 删除照片
     *
     * @param cameraCode
     * @param picId
     */
    private void deleteImg(String cameraCode, int picId) {
        Camera camera = videoMetadataService.getByIndexCode(cameraCode);
        Map data = Maps.newHashMapWithExpectedSize(3);
        data.put("cameraCode", cameraCode);
        data.put("domainCode", camera.getVcuId());
        data.put("picId", picId);

        int code = Integer.valueOf(HttpRequest.post(IVS_SERVER.concat(DELETE_SHOT_URL), data, null).toString());
        if (code != 0) {
            logger.error("删除照片异常，indexCode【{}】，code【{}】", cameraCode, code);
        }
    }
}

