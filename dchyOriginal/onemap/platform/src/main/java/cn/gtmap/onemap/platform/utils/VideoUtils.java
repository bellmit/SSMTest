package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Ptz;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * video utils
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/5/31 16:09
 */
public final class VideoUtils {

    private static final Logger logger = Logger.getLogger("cn.gtmap.onemap.platform.utils.VideoUtils");

    public static final String PTZ_OFFSET_P = "ptz.p.offset";

    public static final String PTZ_OFFSET_T = "ptz.t.offset";

    static {
        logger.setLevel(Level.INFO);
    }

    /***
     * 实时获取配置的ptz的偏移量
     * @param offsetKey
     * @return
     */
    public static final int getPtzOffset(String offsetKey) {
        Assert.notNull(offsetKey);
        int offset = 0;
//        return AppConfigs.getIntProperty(offsetKey,0);
        Properties result = new Properties();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources(AppConfig.getConfHome() + "omp/application.properties");
            if (resources.length > 0) {
                logger.info("Loading properties file from " + resources[0].getFile().getPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        try {
            if (resources != null && resources.length > 0) {
                PropertiesLoaderUtils.fillProperties(
                        result, new EncodedResource(resources[0], Constant.UTF_8));
                offset = Integer.parseInt(result.getProperty(offsetKey));
            }
            return offset;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }

    /***
     * 计算ptz的值
     * @param sPnt
     * @param tPnt
     * @param device
     * @return
     */
    public static final Ptz calculatePtz(Point sPnt, Point tPnt, Camera device) {
        Ptz ptz = new Ptz();
        double p = 0;
        double t = 0;
        int z = 1;

        double x0 = sPnt.getX();
        double y0 = sPnt.getY();
        double x = tPnt.getX();
        double y = tPnt.getY();

        //计算x y坐标的差
        double deltaX = x - x0;
        double deltaY = y - y0;

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        //确定目标点在第几象限（以设备点为坐标轴）右上是第一象限 顺时针方向递推
        int quadrant = Integer.MIN_VALUE;
        if (deltaX > 0 && deltaY > 0)
            quadrant = 1;
        else if (deltaX > 0 && deltaY < 0)
            quadrant = 2;
        else if (deltaX < 0 && deltaY < 0)
            quadrant = 3;
        else if (deltaX < 0 && deltaY > 0)
            quadrant = 4;
        else {
            //落在坐标轴上的
            if (deltaX == 0) {
                if (deltaY > 0)
                    ptz.setP(0);
                else
                    ptz.setP(180);
            } else {
                if (deltaX > 0)
                    p = 90;
                else
                    p = 270;
            }
        }
        switch (quadrant) {
            case 1:
                p = Math.atan(Math.abs(deltaX) / Math.abs(deltaY)) * 180 / Math.PI;
                break;
            case 2:
                p = 90 + (Math.atan(Math.abs(deltaY) / Math.abs(deltaX)) * 180 / Math.PI);
                break;
            case 3:
                p = 180 + (Math.atan(Math.abs(deltaX) / Math.abs(deltaY)) * 180 / Math.PI);
                break;
            case 4:
                p = 270 + (Math.atan(Math.abs(deltaY) / Math.abs(deltaX)) * 180 / Math.PI);
                break;
            default:
                break;
        }
        //修改p异常值
        if (p > 360 || p < 0) p = 0;
        //计算t值
        double h = device.getHeight();
        t = Math.atan(h / distance) * 180 / Math.PI;
        //修改t异常值
        if (t < 0) t = 0;
        //确定z
        Map zoomMap = (Map) ApplicationContextHelper.getBean("zoomMap");
        if (distance >= 1000) z = MapUtils.getIntValue(zoomMap, "z4");
        else if (distance >= 500) z = MapUtils.getIntValue(zoomMap, "z3");
        else if (distance >= 300) z = MapUtils.getIntValue(zoomMap, "z2");
        else if (distance >= 100) z = MapUtils.getIntValue(zoomMap, "z1");
        //测试代码开始
        if(p>180&&p<360){
            p = p -180;
        }else if(p>0&&p<180){
            p=p+180;
        }
        //测试代码结束
        ptz.setP((p + getPtzOffset(PTZ_OFFSET_P)) % 360);
        ptz.setT(t + getPtzOffset(PTZ_OFFSET_T));
        ptz.setZ(z);
        ptz.setDistance(distance);
        logger.info("the distance of camera is:"+distance+" and the height of the camera is "+ device.getHeight());
        logger.info("result of ptz calculate:" + ptz.toString());
        return ptz;
    }
}
