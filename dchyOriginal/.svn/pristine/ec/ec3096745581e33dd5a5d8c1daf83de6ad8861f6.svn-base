/*
 * Project:  onemap
 * Module:   server
 * File:     ServiceHandler.java
 * Modifier: xyang
 * Modified: 2013-05-09 01:44:56
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.handle;

import cn.gtmap.onemap.model.Service;
import cn.gtmap.onemap.model.ServiceProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-9
 */
public interface ServiceHandler {

    boolean accept(String[] paths, ServiceProvider provider, HttpServletRequest request);

    void handle(String[] paths, ServiceProvider provider, HttpServletRequest request, HttpServletResponse response) throws Exception;

    List<Service> getServices(ServiceProvider sp);
}
