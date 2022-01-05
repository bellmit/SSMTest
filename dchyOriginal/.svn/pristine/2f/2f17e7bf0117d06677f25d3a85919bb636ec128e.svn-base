package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.*;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.VideoUtils;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Sets;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.MapUtils;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 华为视频平台接口实现类
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/22 10:31
 */
public class HwVideoServiceImpl extends VideoServiceImpl {

    @Autowired
    private GeometryService geometryService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    private WsuServiceImpl wsuService;

    /**
     * 平台属性
     */
    private VideoPlats.Plat plat;

    public void setPlat(VideoPlats.Plat plat) {
        this.plat = plat;
    }

    /**
     * 初始化
     */
    public void init() {
        if (plat == null) return;
        try {
            logger.info(getMessage("cm.init.starting", plat.getName()));
//            if (isNull(videoContextHolder.get())) {
//                super.reloadConfig();
//            } else {
//                videoCfg = (Map) videoContextHolder.get();
//            }
            wsuService = new WsuServiceImpl(isNotNull(plat.getWsuServer()) ? plat.getWsuServer() : plat.getServer(), plat.getUserName(), plat.getPassword());
            logger.info(getMessage("cm.init.success", plat.getName()));
        } catch (Exception e) {
            throw new RuntimeException(getMessage("cm.init.error", plat.getName(), e.getLocalizedMessage()));
        }
    }


    /**
     * 获取可视域
     * @param indexCodes 监控点编号（多个监控点可用英文逗号分隔）
     * @return
     */
    @Override
    public List<CameraView> getCameraView(String indexCodes) {
        logger.info("start hw getCameraView,indexCode: {}",indexCodes);
        wsuService.login();
        logger.info("hw login sucess,indexCode: {}",indexCodes);
        List<String> indexCodeList = Arrays.asList(indexCodes.split(","));
        List<CameraView> result = new ArrayList<CameraView>();
        try {
            for (String indexCode : indexCodeList) {
                CameraView cameraView = new CameraView();
                Map ptz = wsuService.getPtz(indexCode);
                if (MapUtils.getString(ptz, "Pan") == null || MapUtils.getString(ptz, "Zoom") == null)
                    continue;
                cameraView.setIndexCode(indexCode);
                cameraView.setAzimuth(Double.valueOf(MapUtils.getString(ptz, "Pan")));
                double zoom = (Double.valueOf(MapUtils.getString(ptz, "Zoom")));
                double horizontalValue = zoom > 20 ? 5 : getHorizontalValue(zoom);
                cameraView.setHorizontalAngle(horizontalValue);
                Double vRadius = Math.abs(20 / Math.tan(Math.toRadians(horizontalValue)));//根据Z计算最大可视半径
                if (vRadius > 200)
                    vRadius = 200D;
                Double radius = Math.tan(Math.toRadians(90 - Double.valueOf(MapUtils.getString(ptz, "Tilt")))) * 30;//根据T计算可视半径  30是高度
                radius = Math.abs(radius);
                cameraView.setViewRadius(radius > vRadius ? vRadius : radius);
                logger.info("vRdius:"+vRadius+",radius:"+radius+",indexCode:"+cameraView.getIndexCode());
                logger.info(getMessage("camera.scope.view.info", cameraView.getIndexCode(), cameraView.toString()));
                result.add(cameraView);
            }
        } catch (Exception e) {
            logger.error("hw login error,indexCode: {},error:{}",indexCodes,e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * 计算视场角
     * @param zoom
     * @return
     */
    public double getHorizontalValue(double zoom) {
        return 2 * Math.toDegrees(Math.atan(Math.sqrt(3) / (zoom+1) / 2));
    }

    /**
     * get ptz
     * @param indexCode
     * @return
     */
    @Override
    public Ptz getPTZ(String indexCode) {
        Map ptz = wsuService.getPtz(indexCode);
        return new Ptz(MapUtils.getDouble(ptz, "Pan", 0.0), MapUtils.getDoubleValue(ptz, "Tilt", 0.0),
                MapUtils.getIntValue(ptz, "Zoom", 1));
    }

    /***
     * 设置ptz 参数
     *
     * @param indexCode 关联的设备
     * @param target    转向的目标点
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
        //检查是否需要转换到平面的坐标系上
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
            logger.debug("start calculate ....");
            Ptz ptz = VideoUtils.calculatePtz(source, target, camera);
            logger.info("get the PTZ :" + ptz.toString());
            wsuService.setPtz(indexCode, ptz.getP(),ptz.getT(),ptz.getZ());
        } catch (Exception e) {
            logger.error("setPTZ with error:" + e.toString());
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param indexCode
     * @param target
     * @param loginInfo
     */
    @Override
    public void setPTZ(String indexCode, Point target, Map loginInfo) {
        assert indexCode != null;
        assert target != null;
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        List list = new ArrayList();
        list.add(camera.getX());
        list.add(camera.getY());
        Point source = GeometryUtils.createPoint(new JSONArray(list));
        //检查是否需要转换到平面的坐标系上
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
            logger.debug("start calculate ....");
            Ptz ptz = VideoUtils.calculatePtz(source, target, camera);
            logger.info("get the PTZ :" + ptz.toString());
            if(isNotNull(loginInfo) && !loginInfo.isEmpty()) {
                wsuService.login(MapUtils.getString(loginInfo, "username"), MapUtils.getString(loginInfo, "password"), MapUtils.getString(loginInfo, "server"));
            }
            wsuService.setPtz(indexCode, ptz.getP(),ptz.getT(),ptz.getZ());
        } catch (Exception e) {
            logger.error("setPTZ with error:" + e.toString());
            throw new RuntimeException(e);
        }
    }

    /***
     * 设置ptz 参数
     * @param indexCode  操作的设备
     * @param p
     * @param t
     * @param z
     */
    @Override
    public void setPTZ(String indexCode, String p, String t, String z) {
        Ptz ptz = new Ptz(Double.valueOf(p), Double.valueOf(t), Integer.valueOf(z));
        logger.info("get the PTZ :" + ptz.toString());
        //仅仅使用系统配置的默认用户 不考虑多用户
        wsuService.login();
        String retCode = wsuService.setPtz(indexCode, ptz.getP(), ptz.getT(), ptz.getZ());
        if ("0".equals(retCode))
            logger.info("set ptz successful");
        else
            logger.error("set ptz error:{}", retCode);
    }

    /**
     * 获取设备列表
     * @return
     */
    public List<Map> getDeviceList() {
        wsuService.login();
        return wsuService.getDeviceList();
    }

    /**
     *
     * @param cameraId
     * @return
     */
    @Override
    public String getRtspUrl(String cameraId){
        return wsuService.getRtspUrl(cameraId);
    }
}
