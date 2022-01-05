package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.QuartzScheduleManager;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 定时任务管理控制器
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/3/20 (c) Copyright gtmap Corp.
 */
@Controller
@RequestMapping("/sdm")
public class ScheduleManagerController extends BaseController {


    final QuartzScheduleManager scheduleManager;


    @Autowired
    public ScheduleManagerController(QuartzScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    /**
     * 列出所有的定时任务列表(含当前任务状态)
     *
     * @return
     */
    @RequestMapping(value = "")
    public String index() {
        return "sdm";
    }

    /**
     * rest
     *
     * @return
     */
    @RequestMapping(value = "/jobs")
    @ResponseBody
    public Map list() {
        Map map = Maps.newHashMap();
        map.put("code", 0);
        map.put("data", scheduleManager.listJobs());
        return map;
    }


    /**
     * 启用/禁用 任务
     *
     * @param job
     * @param status
     * @return
     */
    @RequestMapping(value = "/switch/{job}/{status}")
    @ResponseBody
    public Object changeStatus(@PathVariable String job, @PathVariable int status) {
        return status == 1 ? scheduleManager.enableJob(job) : scheduleManager.disableJob(job);
    }

    /**
     * 立即执行一次指定的任务
     *
     * @param job
     * @return
     */
    @RequestMapping(value = "/run")
    @ResponseBody
    public Object runOnce(@RequestParam String job, @RequestParam String jobClass, @RequestParam String jobMethod) {
        try {
            return scheduleManager.runOnce(job, jobClass, jobMethod);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }
}
