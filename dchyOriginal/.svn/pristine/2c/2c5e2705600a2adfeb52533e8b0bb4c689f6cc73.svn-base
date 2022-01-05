package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.job.SchedularJob;
import cn.gtmap.onemap.platform.entity.job.SchedularJobWrapper;
import cn.gtmap.onemap.platform.service.QuartzScheduleManager;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/12/8 (c) Copyright gtmap Corp.
 */
public class QuartzScheduleManagerImpl extends BaseLogger implements QuartzScheduleManager {

    private final SchedulerFactoryBean schedulerFactory;

    /**
     * 默认任务组名称
     */
    private final static String DEFAULT_JOB_GROUP = "default_job_group";

    /**
     * 默认触发器组名称
     */
    private final static String DEFAULT_TRIGGER_GROUP = "default_trigger_group";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DEFAULT_DATETIME_FORMATE);

    private int startupDelay = 0;

    private List<SchedularJob> jobs = Lists.newArrayList();

    private Random random = new Random();

    @Autowired
    public QuartzScheduleManagerImpl(SchedulerFactoryBean schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    public void setStartupDelay(int startupDelay) {
        this.startupDelay = startupDelay;
    }

    public void setJobs(Resource path) {
        try {
            Constructor constructor = new Constructor(SchedularJobWrapper.class);
            TypeDescription typeDescription = new TypeDescription(SchedularJobWrapper.class);
            typeDescription.putListPropertyType("jobs",SchedularJob.class);
            constructor.addTypeDescription(typeDescription);
            Yaml yaml = new Yaml(constructor);
            SchedularJobWrapper schedularJobWrapper = yaml.loadAs(IOUtils.toString(path.getURI(), Constant.UTF_8), SchedularJobWrapper.class);
            jobs = schedularJobWrapper.getJobs();
        } catch (IOException e) {
            logger.error(getMessage("jobs.parse.error", e.getLocalizedMessage()));
        }
    }


    /**
     * 初始化
     * 添加配置的定时任务
     */
    public void init() {
        if (jobs.size() == 0) {
            logger.error(getMessage("jobs.not.found"));
            return;
        }
        for (SchedularJob job : jobs) {
            boolean enable = job.isEnable();
            if (enable) {
                addJob(job);
            }
        }
    }

    /**
     * 列举所有的定时任务
     *
     * @return
     */
    @Override
    public List<Map> listJobs() {
        List<Map> resultJobs = new ArrayList<Map>();
        for (SchedularJob job : jobs) {
            BeanMap beanMap = BeanMap.create(job);
            beanMap.put("running", isRunning(job.getName()));
            resultJobs.add(beanMap);
        }
        return resultJobs;
    }

    /**
     * 启动 scheduler
     */
    @Override
    public void start() {
        try {
            Scheduler scheduler = getScheduler();
            if (scheduler.isShutdown() || scheduler.isInStandbyMode()) {
                getScheduler().startDelayed(this.startupDelay);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(getMessage("scheduler.start.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 停止 scheduler
     * 等待正在运行的 job 执行结束之后再停止
     */
    @Override
    public void shutdown() {
        try {
            Scheduler scheduler = getScheduler();
            scheduler.pauseAll();
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            throw new RuntimeException(getMessage("scheduler.shutdown.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 立即执行一次定时任务
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean runOnce(String jobName, String jobClass, String method) {
        try {
            Class clazz = Class.forName(jobClass);
            Method method1 = ReflectionUtils.findMethod(clazz, method);
            ReflectionUtils.invokeMethod(method1, ApplicationContextHelper.getBean(clazz));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 添加定时任务
     *
     * @param job        任务
     * @return
     */
    @Override
    public boolean addJob(SchedularJob job) {
        Scheduler scheduler = schedulerFactory.getScheduler();
        Object jobBean;
        String jobName = job.getName();
        List args = job.getArgs();
        String timeExpression = job.getTime();
        try {
            Class<?> cls = Class.forName(job.getClazz());
            jobBean = ApplicationContextHelper.getContext().getBean(cls);
        } catch (ClassNotFoundException e) {
            logger.error("{} error: {}", job.getClazz(), e.getLocalizedMessage());
            return false;
        } catch (BeansException e) {
            logger.error("get bean error: {}", e.getLocalizedMessage());
            return false;
        }
        try {
            MethodInvokingJobDetailFactoryBean factoryBean = BeanUtils.instantiate(MethodInvokingJobDetailFactoryBean.class);
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(factoryBean);
            bw.setPropertyValue("targetObject", jobBean);
            bw.setPropertyValue("targetMethod", job.getMethod());
            if(!StringUtils.isEmpty(args)){
                bw.setPropertyValue("arguments",args.toArray());
            }
            factoryBean.prepare();
            JobDetail jobDetail = JobBuilder.newJob(MethodInvokingJobDetailFactoryBean.MethodInvokingJob.class)
                    .withIdentity(jobName, DEFAULT_JOB_GROUP)
                    .storeDurably(true)
                    .build();
            jobDetail.getJobDataMap().put("methodInvoker", factoryBean);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(newTriggerId(), DEFAULT_TRIGGER_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(timeExpression))
                    .build();
            Date runDate = scheduler.scheduleJob(jobDetail, trigger);
            logger.info("任务 {} 执行于 {}", jobName, dateFormat.format(runDate));
            return true;
        } catch (Exception e) {
            logger.error(getMessage("job.add.error", jobName, e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("job.add.error", jobName, e.getLocalizedMessage()));
        }
    }

    /**
     * 删除某个任务
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean removeJob(String jobName) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = new JobKey(jobName);
            scheduler.pauseJob(jobKey);
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 启用 job
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean enableJob(String jobName) {
        try {
            Trigger trigger = getCronTrigger(jobName);
            getScheduler().resumeTrigger(trigger.getKey());
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * 禁用 job
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean disableJob(String jobName) {
        try {
            Trigger trigger = getCronTrigger(jobName);
            getScheduler().pauseTrigger(trigger.getKey());
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * 检测任务是否正在执行中
     *
     * @param jobName
     * @return
     */
    @Override
    public boolean isRunning(String jobName) {
        Scheduler scheduler = schedulerFactory.getScheduler();
        List<JobExecutionContext> executingJobs;
        try {
            executingJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext executingJob : executingJobs) {
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                if (jobName.equals(jobKey.getName())) {
                    return true;
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * 改变任务时间
     *
     * @param jobName
     * @param timeExpression
     * @return
     */
    @Override
    public boolean modifyJobTime(String jobName, String timeExpression) {
        CronTrigger trigger = getCronTrigger(jobName);
        if (isNotNull(trigger)) {
            try {
                String oldCronExpression = trigger.getCronExpression();
                if (oldCronExpression.equals(timeExpression)) {
                    logger.warn(getMessage("job.time.nochange", jobName, timeExpression));
                    return true;
                }
                CronTriggerImpl newTrigger = (CronTriggerImpl) trigger;
                newTrigger.setCronExpression(timeExpression);
                getScheduler().rescheduleJob(trigger.getKey(), newTrigger);
                return true;
            } catch (SchedulerException e) {
                throw new RuntimeException(getMessage("trigger.modify.error", jobName, e.getLocalizedMessage()));
            } catch (ParseException e) {
                throw new RuntimeException(getMessage("trigger.modify.error", jobName, e.getLocalizedMessage()));
            }
        }
        return false;
    }

    /**
     * new trigger id
     *
     * @return
     */
    private String newTriggerId() {
        long r = random.nextLong();
        if (r < 0) {
            r = -r;
        }
        return "MT_"
                + Long.toString(r, 30 + (int) (System.currentTimeMillis() % 7));
    }

    /**
     * 获取触发器
     *
     * @param jobName
     * @return
     */

    private CronTrigger getCronTrigger(String jobName) {
        try {
            Scheduler scheduler = getScheduler();
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(new JobKey(jobName, DEFAULT_JOB_GROUP));
            for (Trigger trigger : triggers) {
                boolean exists = getScheduler().checkExists(trigger.getKey());
                if (!exists) {
                    logger.warn(getMessage("trigger.not.exist", jobName));
                    return null;
                }
                if (jobName.equalsIgnoreCase(trigger.getJobKey().getName())) {
                    return (CronTrigger) trigger;
                }
            }
            return null;
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * get scheduler
     *
     * @return
     */
    private Scheduler getScheduler() {
        return schedulerFactory.getScheduler();
    }
}
