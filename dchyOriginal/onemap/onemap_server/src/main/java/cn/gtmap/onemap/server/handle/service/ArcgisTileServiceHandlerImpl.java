/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisTileServiceHandlerImpl.java
 * Modifier: xyang
 * Modified: 2013-05-23 09:34:13
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.handle.service;

import cn.gtmap.onemap.core.gis.Bound;
import cn.gtmap.onemap.model.Service;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.model.ServiceType;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.arcgis.tile.ExplodedTileFile;
import cn.gtmap.onemap.server.arcgis.tile.TileFile;
import cn.gtmap.onemap.server.handle.AbstractArcgisHandler;
import cn.gtmap.onemap.server.handle.ServiceHandler;
import cn.gtmap.onemap.server.model.TileConfig;
import cn.gtmap.onemap.server.service.ArcgisTileManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.channels.Channels;
import java.util.Collections;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-8
 */
public class ArcgisTileServiceHandlerImpl extends AbstractArcgisHandler implements ServiceHandler, ServletContextAware {
    @Autowired
    private ArcgisTileManager arcgisTileManager;
    private int cacheSeconds = 60 * 60 * 24;
    private File blankPng;

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    public void setBlankPng(File blankPng) {
        this.blankPng = blankPng;
    }

    @Override
    public boolean accept(String[] paths, ServiceProvider provider, HttpServletRequest request) {
        return isTileRequest(paths) || isMapServerRequest(paths);
    }

    @Override
    public void handle(String[] paths, ServiceProvider provider, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TileConfig tileConfig = arcgisTileManager.getTileConfig(provider.getId());
        if (isMapServerRequest(paths)) {
            Model model = new ExtendedModelMap();
            if (tileConfig != null) {
                model.addAttribute(Constants.TILE_INFO, tileConfig.getCacheInfo());
                Bound extent = tileConfig.getExtent();
                if (extent != null) {
                    model.addAttribute("initialExtent", extent);
                    model.addAttribute("fullExtent", extent);
                    model.addAttribute("spatialReference", tileConfig.getCacheInfo().getSpatialReference());
                }
            }
            arcgisRestRenderer.renderMap(model, provider.getMap(), request, response);
        } else {
            int level = Integer.parseInt(paths[2]);
            int row = Integer.parseInt(paths[3]);
            int col = Integer.parseInt(StringUtils.substringBeforeLast(paths[4], "."));
            TileFile tileFile = arcgisTileManager.getTileFile(provider.getId(), level, row, col);
            if (tileFile == null) {
                response.setContentType("image/png");
                tileFile = new ExplodedTileFile(blankPng);
            } else {
                response.setContentType("image/" + tileConfig.getCacheInfo().getFormat());
            }
            long modified = tileFile.lastModified();
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");
            if (ifModifiedSince > 0L) {
                long modDate = (modified / 1000L) * 1000L;
                if (modDate <= ifModifiedSince) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                }
            } else {
                response.setDateHeader("Last-Modified", modified);
                response.setDateHeader("Expires", System.currentTimeMillis() + cacheSeconds * 1000L);
                response.setHeader("Cache-Control", "max-age=" + cacheSeconds);
                tileFile.transferTo(Channels.newChannel(response.getOutputStream()));
            }
        }
    }

    @Override
    public List<Service> getServices(ServiceProvider sp) {
        Service tileService = new Service();
        tileService.setServiceType(ServiceType.ARCGIS_TILE);
        tileService.setUrl(getServiceUrl(sp));
        return Collections.singletonList(tileService);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        blankPng = new File(servletContext.getRealPath("/static/img/blank.png"));
    }
}
