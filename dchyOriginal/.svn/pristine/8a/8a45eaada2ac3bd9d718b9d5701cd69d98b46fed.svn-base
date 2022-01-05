package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Preset;
import cn.gtmap.onemap.platform.entity.video.Project;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/16 20:24
 */
public interface ProjectService {

    /***
     *
     * @param project
     */
    Project saveOrUpdate(Project project, boolean setPreset);

    /**
     * @param proid
     * @return
     */
    Project getByProid(String proid);

    Project getById(String id);

    /**
     * @param proid
     * @return
     */
    boolean deleteByProid(String proid);

    /***
     * 获取camera关联的所有项目
     * @param indexCode
     * @return
     */
    Set<Project> getByCamera(String indexCode);

    /**
     * @param page
     * @param size
     * @return
     */
    Page<Project> getPage(int page, int size, String ownerId, String order, String orderField, String xzqdm);
    /***
     * get location info of all projects
     * @return
     */
    List getLocationInfo(String ownerId);

    /**
     * @param ownerId
     * @return
     */
    List<Project> getAllProjects(String ownerId, String regionCode);

    /**
     * @param prpjectList
     * @return
     */
    Workbook getExportExcel(List<Project> prpjectList);

    /**
     * 多个条件同时查询
     *
     * @param page
     * @param size
     * @param condition
     * @return
     */
    Page<Project> search(int page, int size, Map condition, boolean withXzq,String order,String orderField);

    /**
     * 获取符合条件的所有项目
     *
     * @param condition
     * @param withXzq
     * @return
     */
    List<Project> search(Map condition, boolean withXzq);

    /**
     * 获取该项目关联的照片记录 并按年度分组
     *
     * @param proid
     * @return '[{year:2015,data:['xx','ddd']}]'
     */
    List<Map> getImgRecords(String proid, final Map condition);

    /***
     * 获取某个项目一定时间内的相关记录 按时间降序
     * （包括巡查记录和照片记录）
     * @param proid
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map> getRecords(String proid, Date startTime, Date endTime);

    /**
     * 获取一定时间内的所有项目的拍照记录
     * @param start
     * @param end
     * @return
     */
    List<FileStore> getImgRecordsByTimeSpan(Date start, Date end);

    /**
     *  获取一定时间内的某个项目的拍照记录
     * @param proId
     * @param start
     * @param end
     * @return
     */
    List<FileStore> getImgRecordsByTimeSpanAndPro(String proId, Date start, Date end);

    /**
     * 获取
     *
     * @param proid
     * @return
     */
    List getRecordsDistinctYear(String proid);

    /**
     * 移除项目照片记录（含文件删除）
     *
     * @param proid
     * @param fileStoreIds
     */
    void removeProjectRecord(String proid, String[] fileStoreIds);

    /**
     * 获取预设位已设置的项目
     *
     * @param condition
     * @param page
     * @param size
     * @return
     */
    Map getNormalProject(Map condition, int page, int size);

    /**
     * @param proId
     * @return
     */
    Set<String> getRefCameras(String proId);

    /**
     * 维护存在的项目摄像头对应的预设位关系
     *
     * @param proId
     * @param indexCode
     * @param presetId
     * @return
     */
    boolean insertRefAboutPreset(String proId, String indexCode, String presetId);

    /**
     * 移除存在的项目摄像头对应的预设位关系
     *
     * @param proId
     * @param indexCode
     * @param presetId
     * @return
     */
    boolean delRefAboutPreset(String proId, String indexCode, String presetId);

    /**
     * 添加项目点缓冲范围内的新摄像头信息
     *
     * @param proId
     * @param cameras
     * @return
     */
    List<Preset> addProCameras(String id, String proId, String[] cameras);

    /**
     * 删除项目的摄像头信息
     *
     * @param id
     * @param proId
     * @param indexCode
     * @return
     */
    boolean deleteProCamera(String id, String proId, String indexCode);

    /**
     * 获取所有存在的行政区单位信息
     *
     * @return
     */
    List getOrganList(String xzqdm);

    Page<Project> getWarningPage(int page, int size, String order,Map condition,int status);

    List<Camera> getCamerasBySpecialProtype(String type);

    int getWarningCount(int status);

    Camera getCameraByProName(String proName);
}
