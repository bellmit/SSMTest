package cn.gtmap.onemap.platform.service;

import java.util.List;
import java.util.Map;

import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Recognition;

/**
 * 视频智能识别服务接口
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/11/14 (c) Copyright gtmap Corp.
 */
public interface VideoRecognizeService {


    /**
     * 执行识别任务
     */
    void runTask();

    /**
     * 执行一次挖掘机识别分析
     * @param origin
     * @return
     */
    Recognition execute(FileStore origin);

    /**
     * 返回所有的识别记录
     * @return
     */
    List<Map> getAllRecords();


    /**
     * 根据原始照片查询 结果照片
     * @param fileIds
     * @return
     */
    List<Recognition> getRecogsByOrigin(List<String> fileIds);

    /**
     * 
     * @param originFileId
     * @return
     */
    boolean add2WhiteList(String originFileId);

    /**
     * 修改状态
     * @param fileID
     * @param status 0预警1建设用地2违法用地
     * @return
     */
    Recognition updateRecogStatusByFileID(String fileID,int status);

    /**
     * 根据fileid解除预警
     * @param id
     * @return
     */
    boolean deleteRecog(String id);

    Recognition findByOriginal(String originalID);
    
    /**
     * 描述：保存分析图片(海康江阴)
     * @author 卜祥东
     * 2019年5月5日 上午10:51:11
     * @param oldFileStore 原始图片
     * @param diffXyJsonStr 差异坐标串 (JsonStr)
     * @return
     */
    public Recognition saveRecogHikJiangyin(FileStore oldFileStore,String diffXyJsonStr);
    /**
     * 描述：保存分析图片(瞭望神州)
     * @author 卜祥东
     * 2019年7月17日 上午10:51:11
     * @param oldFileStore 原始图片
     * @param diffXyJsonStr 差异坐标串 (JsonStr)
     * @return
     */
    public Recognition saveRecogByLwsz(FileStore oldFileStore,String diffXyJsonStr);

}
