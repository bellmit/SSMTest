/*
 * Project:  onemap
 * Module:   server
 * File:     TaskManagerImpl.java
 * Modifier: xyang
 * Modified: 2013-05-23 08:48:17
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator;
import cn.gtmap.onemap.server.model.Task;
import cn.gtmap.onemap.server.service.TaskManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-29
 */
public class TaskManagerImpl implements TaskManager {
    private Map<String, Task> taskMap = Maps.newConcurrentMap();
    private ExecutorService executor;

    @Override
    public ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        }
        return executor;
    }

    @Override
    public boolean containsTask(String id) {
        return taskMap.containsKey(id);
    }

    @Override
    public void submitTask(Task task, Runnable runnable) {
        if (task.getId() == null) {
            task.setId(UUIDHexGenerator.generate());
        }
        task.setFuture(getExecutor().submit(runnable));
        taskMap.put(task.getId(), task);
    }

    @Override
    public void completeTask(String id) {
        taskMap.remove(id);
    }

    @Override
    public void stopTask(String id) {
        Task task = taskMap.get(id);
        if (task != null) {
            task.getFuture().cancel(true);
            taskMap.remove(id);
        }
    }

    @Override
    public synchronized List<Task> getTasks() {
        List<Task> list = Lists.newArrayList();
        Iterator<Map.Entry<String, Task>> iterator = taskMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next().getValue();
            if (task.isCompleted()) {
                iterator.remove();
            } else {
                list.add(task);
            }
        }
        return list;
    }
}
