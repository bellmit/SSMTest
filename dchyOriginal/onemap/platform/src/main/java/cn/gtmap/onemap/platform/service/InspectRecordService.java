package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.InspectRecord;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * 巡查记录 业务接口定义
 *
 * @author <a href="zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 16-3-18 下午17:00
 */
public interface InspectRecordService {
    /**
     * 获取巡查记录
     * @param condition  存储条件
     * @param pageIndex
     * @param size
     * @return
     */
    Page<InspectRecord> search(Map condition, int pageIndex, int size);

    /**
     * 保存巡查记录
     * @param data inspectRecord json string
     * @param proId
     * @param type
     * @return
     */
    InspectRecord saveOrUpdate(String data, String proId, String type,String limitTime);

    /**
     * 保存并推送巡查记录
     * @param data
     * @param proId
     * @return
     */
    Map saveAndSend(String data, String proId,String url);

    /**
     * 通过id查询巡查记录
     * @param id
     * @return
     */
    InspectRecord findInspectRecordById(String id);

    /**
     * 删除巡查记录
     * @param id
     * @return
     */
    boolean deleteInspectRecordById(String id);

    /**
     *根据proid批量删除巡查记录
     * @param proid
     * @return
     */
    boolean deleteByProid(String proid);

    /**
     * 获取巡查记录的定制模板
     * @return
     */
    String getInpectRecordTpl();

    /**
     *
     * @param id inspectRecord 主键
     * @return
     */
    Map sendInspectRecord(String id);
}
