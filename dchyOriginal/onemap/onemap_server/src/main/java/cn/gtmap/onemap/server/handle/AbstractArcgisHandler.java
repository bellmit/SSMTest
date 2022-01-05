/*
 * Project:  onemap
 * Module:   server
 * File:     AbstractArcgisHandler.java
 * Modifier: xyang
 * Modified: 2013-05-23 09:12:34
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

import cn.gtmap.onemap.core.template.ModelRenderer;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.service.MetadataService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-9
 */
public abstract class AbstractArcgisHandler {
    private String baseUrl;
    @Autowired
    protected MetadataService metadataService;
    @Autowired
    protected ModelRenderer modelRenderer;
    @Autowired
    protected ArcgisRestRenderer arcgisRestRenderer;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected String getServiceUrl(ServiceProvider sp) {
        return baseUrl + "/" + sp.getMap().getPath() + "/" + Constants.MAP_SERVER;
    }

    public static boolean isFindRequest(String[] paths) {
        return paths.length == 2 && "find".equals(paths[1]);
    }

    public static boolean isIdentifyRequest(String[] paths) {
        return paths.length == 2 && "identify".equals(paths[1]);
    }

    public static boolean isQueryRequest(String[] paths) {
        return paths.length == 3 && "query".equals(paths[2]);
    }

    public static boolean isTileRequest(String[] paths) {
        return paths.length == 5 && "tile".equals(paths[1]);
    }

    public static boolean isMapServerRequest(String[] paths) {
        return paths.length == 1 && Constants.MAP_SERVER.equals(paths[0]);
    }

    public static boolean isLayerRequest(String[] paths) {
        if (paths.length != 2) {
            return false;
        }
        if (!Constants.MAP_SERVER.equals(paths[0])) {
            return false;
        }
        String layer = paths[1];
        return "layers".equals(layer) || NumberUtils.isNumber(layer);
    }
}
