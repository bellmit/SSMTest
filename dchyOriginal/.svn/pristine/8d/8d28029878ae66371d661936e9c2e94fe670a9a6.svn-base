package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraPresetTaskDao;
import cn.gtmap.onemap.platform.dao.FileStoreDao;
import cn.gtmap.onemap.platform.dao.PresetDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.job.SchedularJob;
import cn.gtmap.onemap.platform.entity.video.CameraPresetTask;
import cn.gtmap.onemap.platform.entity.video.Preset;
import cn.gtmap.onemap.platform.service.CameraPresetTaskService;
import cn.gtmap.onemap.platform.service.PresetService;
import cn.gtmap.onemap.platform.service.QuartzScheduleManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ruihui.li
 * @version V1.0
 * @Description:
 * @date 2018/8/8
 */
@Service
public class CameraPresetTaskServiceImpl extends BaseLogger implements CameraPresetTaskService {

    @Autowired
    private CameraPresetTaskDao cameraPresetTaskDao;
    @Autowired
    private QuartzScheduleManager quartzScheduleManager;
    @Autowired
    private FileStoreDao fileStoreDao;

    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @PostConstruct
    void init(){
        List<CameraPresetTask> presetTasks = cameraPresetTaskDao.findAll();
        if(!CollectionUtils.isEmpty(presetTasks)){
            for(CameraPresetTask task : presetTasks){
                SchedularJob schedularJob = new SchedularJob();
                schedularJob.setEnable(task.isEnable());
                schedularJob.setName(task.getId());
                schedularJob.setArgs(Arrays.asList(task.getId()));
                schedularJob.setClazz("cn.gtmap.onemap.platform.service.impl.VideoManagerImpl");
                schedularJob.setMethod("cameraCaptureWithPreser");
                schedularJob.setTime(getCron(task.getTime()));
                quartzScheduleManager.addJob(schedularJob);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(CameraPresetTask cameraPresetTask) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        int pNo = 51;
        List<CameraPresetTask> presetTasks = cameraPresetTaskDao.findByIndexCode(cameraPresetTask.getIndexCode());
        if(!CollectionUtils.isEmpty(presetTasks)){
            Collections.sort(presetTasks, new Comparator<CameraPresetTask>() {
                @Override
                public int compare(CameraPresetTask o1, CameraPresetTask o2) {
                    return o2.getPNo() - o1.getPNo();
                }
            });

            pNo = presetTasks.get(0).getPNo() + 1;
        }
        String dateString = "08:30:00";
        Date time = null;
        try {
            time = df.parse(dateString);
        } catch (ParseException e) {
            logger.error(e.toString());
        }
        cameraPresetTask.setPresetName("预置位"+pNo);
        cameraPresetTask.setPNo(pNo);
        cameraPresetTask.setTime(time);
        cameraPresetTask.setEnable(true);
        cameraPresetTaskDao.save(cameraPresetTask);
        SchedularJob schedularJob = new SchedularJob();
        schedularJob.setName(cameraPresetTask.getId());
        schedularJob.setEnable(true);
        schedularJob.setTime(getCron(cameraPresetTask.getTime()));
        schedularJob.setClazz("cn.gtmap.onemap.platform.service.impl.VideoManagerImpl");
        schedularJob.setMethod("cameraCaptureWithPreser");
        String[] args = {cameraPresetTask.getId()};
        schedularJob.setArgs(Arrays.asList(args));
        return quartzScheduleManager.addJob(schedularJob);
    }

    @Override
    public CameraPresetTask findById(String id) {
        return cameraPresetTaskDao.findById(id);
    }

    @Override
    public List<CameraPresetTask> findByIndexCodes(String indexCode) {
        return cameraPresetTaskDao.findByIndexCode(indexCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delById(String id) {
        if(StringUtils.isEmpty(id)){
            return;
        }
        CameraPresetTask task = cameraPresetTaskDao.findById(id);
        if(null != task) {
            cameraPresetTaskDao.delete(task);
        }
        boolean running = quartzScheduleManager.isRunning(id);
        if(running){
            quartzScheduleManager.removeJob(id);
        }

    }

    @Override
    public boolean modifyTask(String id, Date time) {
        CameraPresetTask presetTask = cameraPresetTaskDao.findById(id);
        if(null != presetTask){
            presetTask.setTime(time);
            presetTask.setEnable(true);
        }
        cameraPresetTaskDao.save(presetTask);
        return quartzScheduleManager.modifyJobTime(id,getCron(time));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeTask(String id, boolean enable) {
        try {
            CameraPresetTask task = cameraPresetTaskDao.findById(id);
            task.setEnable(enable);
            task = cameraPresetTaskDao.saveAndFlush(task);
            boolean running = quartzScheduleManager.isRunning(id);
            if (enable) {
                if (running) {
                    return true;
                } else {
                    quartzScheduleManager.enableJob(id);
                }
            } else {
                quartzScheduleManager.disableJob(id);
            }
            return true;
        }catch (Exception e){
            logger.error("修改摄像头预设位拍照任务的状态发生错误{}",e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String,Object>> getPic(final String id,final Date startTime, final Date endTime) {
        List<Map<String,Object>> resultMap = new ArrayList<Map<String, Object>>();
//        List<FileStore> fileStores = fileStoreDao.findAll(new Specification<FileStore>() {
//            @Override
//            public Predicate toPredicate(Root<FileStore> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.and(criteriaBuilder.equal(root.get("parentId").as(String.class), id),//
//                        criteriaBuilder.between(root.get("createTime").as(Date.class), startTime, endTime));
//            }
//        }, new Sort(Sort.Direction.DESC, "createTime"));
//        DateTime startDate = new DateTime(startTime);
//        DateTime endDate = new DateTime(endTime);
//        int period = Days.daysBetween(startDate, endDate).getDays();
//        for (int i = 0; i <= period; i++) {
//            final String date = endDate.minusDays(i).toString(Constant.DEFAULT_DATE_FORMATE);
//            Map map = new HashMap();
//            boolean isToday = DateTime.now().toString(Constant.DEFAULT_DATE_FORMATE).equals(date);
//            if (isToday) {
//                map.put("isToday", isToday);
//            }
//            if (i == 0 || isToday) {
//                map.put("isShow", true);
//            } else {
//                map.put("isShow", false);
//            }
//            map.put("date", date);
//            map.put("images", org.apache.commons.collections.CollectionUtils.select(fileStores, new org.apache.commons.collections.Predicate() {
//                @Override
//                public boolean evaluate(Object object) {
//                    return date.equals(formatter.format(((FileStore) object).getCreateTime()));
//                }
//            }));
//
//            resultMap.add(map);
//        }
        for(int i = 1;i<=10;i++){
            FileStore one = fileStoreDao.findOne("01633c759e9840288150633a76c4152f");
            Map map = new HashMap();
            map.put("isToday",false);
            map.put("isShow",false);
            map.put("date","2018-08-0"+i);
            map.put("images",one);
            resultMap.add(map);
        }
        return resultMap;
    }

    private String getCron(Date date){
        String format = "ss mm HH * * ?";
        SimpleDateFormat sm = new SimpleDateFormat(format);
        String timeFormat = null;
        if(date != null){
            timeFormat = sm.format(date);
        }
        return timeFormat;
    }
}
