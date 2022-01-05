package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.FFmpegTaskDao;
import cn.gtmap.onemap.platform.entity.ffmpeg.FFmpegTask;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
@Repository
public class FFmpegTaskDaoImpl implements FFmpegTaskDao {

    /**
     *  存放任务信息
     */
    private ConcurrentMap<String, FFmpegTask> taskMap;

    @PostConstruct
    public void init() {
        //初始化 存储
        // todo 待优化
        taskMap = Maps.newConcurrentMap();
    }

    /**
     * find one by id
     *
     * @param id
     * @return
     */
    @Override
    public FFmpegTask findOne(String id) {
        return taskMap.get(id);
    }

    /**
     * find all
     *
     * @return
     */
    @Override
    public Collection<FFmpegTask> findAll() {
        return taskMap.values();
    }

    /**
     * delete one by id
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {
        return taskMap.remove(findOne(id)) != null;
    }

    /**
     * save/update one
     *
     * @param task
     * @return id of task
     */
    @Override
    public String save(FFmpegTask task) {
        String id = task.getId();
        if (id != null) {
            taskMap.put(task.getId(), task);
        }
        if (taskMap.get(id) != null) {
            return id;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 任务是否已存在
     *
     * @param id
     * @return
     */
    @Override
    public boolean exists(String id) {
        return taskMap.containsKey(id);
    }
}
