/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisRestRequestHandlerImpl.java
 * Modifier: xyang
 * Modified: 2013-05-28 03:31:16
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.handle.request;

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.util.ArrayUtils;
import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.handle.*;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-8
 */
public class ArcgisRestRequestHandlerImpl extends AbstractArcgisHandler implements RequestHandler {
    @Autowired
    private ServiceProviderManager serviceProviderManager;
    @Autowired
    private ServiceHandleFactory serviceHandleFactory;
    @Autowired
    private GeometryServerServiceHandle geometryServerServiceHandle;

    @Override
    public void handle(Map map, String[] paths, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (map == null) {
            if (paths.length > 0 && Constants.GEOMETRY_SERVER.equals(paths[0])) {
                geometryServerServiceHandle.handle(paths.length == 1 ? null : paths[1], request, response);
            } else {
                arcgisRestRenderer.renderCatalog(ArrayUtils.isEmpty(paths) ? null : paths[0], request, response);
            }
            return;
        }
        if ("jsapi".equals(RequestUtils.getParameter(request, "f"))) {
            arcgisRestRenderer.renderMap(null, map, request, response);
            return;
        }
        for (ServiceProvider provider : serviceProviderManager.getServiceProviders(map.getId())) {
            if (provider.isEnabled()) {
                ServiceHandler serviceHandle = serviceHandleFactory.getServiceHandler(provider);
                if (serviceHandle != null && serviceHandle.accept(paths, provider, request)) {
                    serviceHandle.handle(paths, provider, request, response);
                    return;
                }
            }
        }
        if (isMapServerRequest(paths)) {
            arcgisRestRenderer.renderMap(null, map, request, response);
            return;
        }
        if (isLayerRequest(paths)) {
            arcgisRestRenderer.renderLayer(null, map, paths[1], request, response);
            return;
        }

        throw new OneMapException("no serviceProvider for [" + map.getId() + "] found");
    }
}
