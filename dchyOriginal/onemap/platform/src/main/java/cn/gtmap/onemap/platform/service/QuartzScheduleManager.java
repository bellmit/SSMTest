package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.job.SchedularJob;

import java.util.List;
import java.util.Map;

/**
 * quartz 定时任务管理
 * 可以实现根据配置文件生成定时任务以及管理任务
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/8 (c) Copyright gtmap Corp.
 */
public interface QuartzScheduleManager {


    /**
     * 列举所有的定时任务
     * @return
     */
    List<Map> listJobs();
    /**
     * 启动 scheduler
     */
    void start();
    
    /**
     * 关闭 scheduler
     */
    void shutdown();

    /**
     *  立即执行一次定时任务
     * @param jobName
     * @return
     */
    boolean runOnce(String jobName, String jobClass, String method);
    
    /**
     * 添加定时任务
     * @param job       任务名称
     * @return
     */
    boolean addJob(SchedularJob job);
    /**
     * 移除定时任务
     * @param jobName
     * @return
     */
    boolean removeJob(String jobName);
    /**
     * 启用某个定时任务
     * @param jobName
     * @return
     */
    boolean enableJob(String jobName);
    /**
     * 禁用某个定时任务
     * @param jobName
     * @return
     */
    boolean disableJob(String jobName);
    /**
     * 检测定时任务是否正在运行中
     * @param jobName
     * @return
     */
    boolean isRunning(String jobName);
    /**
     * 修改定时任务的时间
     * @param jobName
     * @param timeExpression
     * @return
     */
    boolean modifyJobTime(String jobName, String timeExpression);
    
}
