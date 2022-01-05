package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraView;
import cn.gtmap.onemap.platform.entity.video.Ptz;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang.StringUtils;

import java.nio.DoubleBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 视频service
 * 各类视频平台
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 18:41
 */
public interface VideoService {

    /**
     * 操作类型
     */
    enum OperaType {
        preview, playback
    }

    /**
     * 授权资源名称
     */
    String VIDEO_RESOURCE = "omp-video:";

    /**
     * 初始化bean
     */
    void init();

    /***
     * 获取所有的监控点列表(通过webservice接口 for hk)
     * @param outSR
     * @return
     */
    List<Camera> getAllCamerasByWs(String outSR);

    /**
     * 根据geometry获取其附近的监控点
     *
     * @param geojson
     * @param bufferSize
     * @return
     */
    List<Camera> getPoiCameras(String geojson, String bufferSize, String xzqdm);

    /**
     * 根据geometry获取其附近的监控点
     *
     * @param geometry
     * @param bufferSize
     * @return
     */
    List<Camera> getPoiCameras(Geometry geometry, String bufferSize,String xzqdm);

    /**
     * 根据camera的indexCode获取报文xml(预览和回放)
     *
     * @param operaType
     * @param cameraIndexCode
     * @return
     */
    String getCameraXml(OperaType operaType, String cameraIndexCode);

    /**
     * 获取监控球机的可视域参数，用于建模地图上渲染可视区域
     *
     * @param indexCode 监控点编号（多个监控点可用英文逗号分隔）
     * @return
     */
    List<CameraView> getCameraView(String indexCode);

    /***
     * 查询某个用户对于某个设备已授权的操作
     * @param indexCode 设备编号
     * @param userId    用户id
     * @return
     */
    Set<Operation> getAuthorizedOperations(String indexCode, String userId);

    /**
     * 获取 ptz
     *
     * @param indexCode
     * @return
     */
    Ptz getPTZ(String indexCode);

    /**
     * ivs 预设位拍照
     * @param indexCode
     * @param startTime
     * @param endTime
     * @param proId
     * @param presetId
     */
    void snapShot(String indexCode, String startTime, String endTime, String proId, String presetId);

    /***
     * 设置ptz 参数
     * @param indexCode  关联的设备
     * @param target    转向的目标点
     */
    void setPTZ(String indexCode, Point target);

    /***
     * 设置ptz 参数(wsu帐号登录)
     * @param indexCode
     * @param target
     * @param loginInfo
     */
    void setPTZ(String indexCode, Point target, Map loginInfo);

    /***
     * 设置ptz 参数
     * @param indexCode
     * @param p
     * @param t
     * @param z
     */
    void setPTZ(String indexCode, String p, String t, String z);

    /**
     * 获取设备indexcode集合
     *
     * @return
     */
    List<String> getIndexCodes();

    /**
     * 获取单个行政区indexcode集合
     *
     * @param regionName
     * @return
     */
    List<String> getIndexCodesByRegion(String regionName);

    /**
     * 根据proId获取设备信息
     *
     * @param proId
     * @return
     */
    List<Camera> getCamerasByProId(String proId);


    /**
     * ivs ptz 控制
     * @param cameraCode
     * @param controlCode
     * @param param1
     * @param param2
     * @return
     */
    int ptzMoveControl(String cameraCode, Constant.IVS_PTZ_CODE controlCode, String param1, String param2);

    /**
     *
     * @param cameraId
     * @return
     */
    String getRtspUrl(String cameraId);

    Camera findById(String id);

    Camera Save(Camera camera);

    String SetFileID(String cameraID,String fileID);

    Map getCamerasCountByRegID(String cameraId);

    /**
     * 统计区域在线离线
     * @return
     */
    HashMap getRegionCount();

    Point getPointFromXY(double x,double y);

    public List<String> getIndexCodesByXzq(String xzqh);
}
