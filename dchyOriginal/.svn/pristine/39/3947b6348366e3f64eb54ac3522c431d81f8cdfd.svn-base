package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.ffmpeg.FFmpegTask;

import java.util.Collection;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/1/23 (c) Copyright gtmap Corp.
 */
public interface FFmpegTaskDao {

   /**
    * find one by id
    * @param id
    * @return
    */
   FFmpegTask findOne(String id);

   /**
    * find all
    * @return
    */
   Collection<FFmpegTask> findAll();

   /**
    * delete one by id
    * @param id
    * @return
    */
   boolean delete(String id);

   /**
    * save one
    * @param task
    * @return  id of task
    */
   String save(FFmpegTask task);

   /**
    * 任务是否已存在
    * @param id
    * @return
    */
   boolean exists(String id);
}
