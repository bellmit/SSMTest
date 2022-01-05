/*
 * Project:  onemap
 * Module:   server
 * File:     TaskManager.java
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

package cn.gtmap.onemap.server.service;

import cn.gtmap.onemap.server.model.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-29
 */
public interface TaskManager {

    ExecutorService getExecutor();

    boolean containsTask(String id);

    void submitTask(Task task, Runnable runnable);

    void completeTask(String id);

    void stopTask(String id);

    List<Task> getTasks();
}
