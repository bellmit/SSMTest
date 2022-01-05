/*
 * Project:  onemap
 * Module:   server
 * File:     ServiceHandleFactoryImpl.java
 * Modifier: xyang
 * Modified: 2013-05-09 09:20:07
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

import cn.gtmap.onemap.model.ServiceProvider;

import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-9
 */
public class ServiceHandleFactoryImpl implements ServiceHandleFactory {
    private Map<String, ServiceHandler> handlers;

    public void setHandlers(Map<String, ServiceHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public ServiceHandler getServiceHandler(ServiceProvider provider) {
        return handlers.get(provider.getType());
    }
}
