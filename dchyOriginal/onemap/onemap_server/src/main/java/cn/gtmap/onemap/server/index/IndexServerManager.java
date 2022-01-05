/*
 * Project:  onemap
 * Module:   server
 * File:     IndexServerManager.java
 * Modifier: xyang
 * Modified: 2013-05-17 05:23:04
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-28
 */
public interface IndexServerManager {

    void start(String id);

    void stop(String id);

    void reload(String id);

    boolean isRunning(String id);

    void remove(String id);

    public IndexServer getServer(String id);
}
