package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.Preset;

import java.util.List;

/**
 * 预设位service
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/2 16:42
 */
public interface PresetService {

    /***
     *
     * @param id    project 主键
     * @param proId
     * @param indexCode
     * @return
     */
    Preset insert(String id, String proId, String indexCode);

    /**
     * 插入全景预设位
     * @param proId
     * @param indexCode
     * @return
     */
    Preset insertPanoramaPreset(String proId, String indexCode);

    /**
     * insert preset
     * @param preset
     * @return
     */
    Preset insert(Preset preset);

    /***
     *
     * @param proId
     * @param indexCode
     * @return
     */
    List<Preset> find(String proId, String indexCode);

    /***
     *
     * @param indexCode
     * @return
     */
    List<Preset> findByDevice(String indexCode);

    /**
     *
     * @param proid
     * @return
     */
    List<Preset> findByPro(String proid);

    /***
     *
     * @param proid
     */
    boolean deleteByProId(String proid);

    /***
     *
     * @param indexCode
     */
    boolean deleteByDevice(String indexCode);

    /***
     *
     * @param id
     */
    boolean deleteById(String id, String proId);


    /**
     * 设置预设位有效性
     * @param enabled
     * @param id
     */
    void setEnabled(boolean enabled, String id);

    /**
     * delete preset only
     * @param preset
     */
    void delete(Preset preset);
}
