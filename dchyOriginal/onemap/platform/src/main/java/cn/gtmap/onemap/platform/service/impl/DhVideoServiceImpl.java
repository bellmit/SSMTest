package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Ptz;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.VideoUtils;
import com.alibaba.fastjson.JSONArray;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 大华视频接口实现
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/30 14:27
 */
public class DhVideoServiceImpl extends HwVideoServiceImpl {

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private GeometryService geometryService;

    /**
     * 平台属性
     */
    public static VideoPlats.Plat plat;

    /***
     * 设置ptz转向某个点
     * @param indexCode  关联的设备
     * @param target    转向的目标点
     */
    @Override
    public void setPTZ(String indexCode, Point target) {
        assert indexCode != null;
        assert target != null;
        String url = null;
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
            //计算ptz
            Ptz ptz = VideoUtils.calculatePtz(source, target, camera);
            logger.debug("ptz计算结果:" + ptz.toString());
            setPTZ(indexCode,String.valueOf(ptz.getP()),String.valueOf(ptz.getT()),String.valueOf(ptz.getZ()));
        } catch (Exception e) {
            logger.error("setPTZ with error:" + e.toString());
            throw new RuntimeException(e);
        }
    }

    /***
     * 设置探头的ptz
     * 大华的p和t的值需要乘以10
     * @param indexCode
     * @param p
     * @param t
     * @param z
     */
    @Override
    public void setPTZ(String indexCode, String p, String t, String z) {
        String cServer = AppConfig.getProperty("ptz.server");
        String url = cServer.concat("/runptz");
        try {
            url = url.concat("?p=" + customizedValue(Double.valueOf(p).doubleValue()))
                    .concat("&t=" + customizedValue(Double.valueOf(t).doubleValue()))
                    .concat("&z=" + z).concat("&deviceinfo=" + indexCode);
            logger.debug("请求服务端控件地址:" + url);
            String response = HttpRequest.sendRequest2(url, null);
            logger.debug("请求服务端控件相应结果:" + response);
        } catch (Exception e) {
            logger.error("请求视频客户端异常" + e.toString());
        }
    }

    /**
     * customized by times 10
     *
     * @param val
     * @return
     */
    private int customizedValue(double val) {
        return Integer.parseInt(String.valueOf(Math.round(val*10)));
    }

}
