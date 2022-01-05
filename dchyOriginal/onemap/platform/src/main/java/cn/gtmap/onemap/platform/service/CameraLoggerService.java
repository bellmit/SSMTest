package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.CameraLog;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * 摄像头操作日志接口
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-18 10.20:00
 */
public interface CameraLoggerService extends LoggerService<CameraLog> {
    /**
     * 存储摄像头的操作日志
     * @param cameraId
     * @param uesrId
     * @param operation
     * @return
     */
    CameraLog saveOptLog(String cameraId, String uesrId, int operation,String token);

    /**
     * 存储摄像头拍照日志
     * @param cameraId
     * @param optContent
     * @return
     */
    void saveCaptureLog(String cameraId, String optContent, String deviceInfo);

    /**
     * 展示摄像头的信息
     * @param condition
     * @return
     */
    Map showUseCameraInfo(Map condition);

    /**
     * 查看所有摄像探头使用信息
     * @param condition
     * @return
     */
    List<Map> showAllUseCameraInfo(Map condition);


    /**
     * 获取不重复的摄像头信息或者用户信息
     * @param type
     * @return
     */
     List<Map> getDistinctUserOrCamera(String type,String start,String end);

    /**
     * 查询三天未使用的摄像头
     */
    List unusedCameraIn3Days();

    Map getCameraLogDetail(Map condition);

    Map getCameraLogDetailNew(String startDateStr, String endDateStr);

    Workbook getAllExportGroupByOrgan(Map map);

    void saveCameraLog(CameraLog cameraLog);

    /**
     * 根据部门计算该部门使用摄像头次数
     */
    List<Map> countOptCameraLogByOrgan(String organ,Map conditions);

    String getConstantValue(String type)throws Exception;
}
