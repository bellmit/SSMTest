package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.video.CameraLog;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-03-23 19:47:00
 */
public interface LoggerService<T> {
    /**
     * 存储logger信息
     *
     * @param logger
     * @return
     */
    T save(T logger);

    /**
     * 查询日志信息
     * @param condition
     * @param index
     * @param page
     * @return
     */
    Page<T> search(Map condition, int index, int page);

    /**
     * 不分页获取日志
     * @param condition
     * @return
     */
    List<T> export(Map condition);

    /**
     *导出日志信息excel
     * @param loggers
     * @return
     */
    Workbook getExportExcel(List<T> loggers);

    /**
     * 导出监控统计信息
     * @param map
     * @return
     */
    Workbook getExportStatisticExcel(Map map,String title);

    /**
     * 导出全部
     * @param results
     * @return
     */
   Workbook getAlLExportAllStatisticExcel(List<Map> results);

    /**
     * 删除日志
     * @param id
     * @return
     */
    boolean remove(String id);


    /**
     * find one
     * @param id
     * @return
     */
     T getById(String id);

    /**
     * 描述：多线程遍历list并根据用户id查询用户所属部门
     *
     * @return
     * @author 杨文军
     * @create 16:24 2019/8/8
     **/
    List getCameraLogUserDeptByUserId(List<CameraLog> list,final int nThreads);

}
