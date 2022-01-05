/*
 * Project:  onemap
 * Module:   server
 * File:     ThumbnailController.java
 * Modifier: xyang
 * Modified: 2013-05-16 11:35:24
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.web.service;

import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.Thumbnail;
import cn.gtmap.onemap.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-4
 */
@Controller
public class ThumbnailController {
    @Autowired
    private MetadataService metadataService;
    private int cacheSeconds = 300;

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    @RequestMapping(value = "arcgisrest/{map}/MapServer/info/thumbnail")
    @ResponseBody
    public void thumbnail0(@PathVariable("map") String mapName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        thumbnail(null, mapName, request, response);
    }

    @RequestMapping(value = "arcgisrest/{folder}/{map}/MapServer/info/thumbnail")
    @ResponseBody
    public void thumbnail(@PathVariable("folder") String folder,
                          @PathVariable("map") String mapName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = metadataService.getMapByName(mapName);
        thumbnail(map != null ? map.getId() : null, request, response);
    }

    @RequestMapping(value = "thumbnail/{mapId}")
    @ResponseBody
    public void thumbnail(@PathVariable("mapId") String mapId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mapId != null) {
            Thumbnail thumbnail = metadataService.getThumbnail(mapId);
            if (thumbnail != null) {
                long modified = thumbnail.getUpdateAt().getTime();
                long ifModifiedSince = request.getDateHeader("If-Modified-Since");
                if (ifModifiedSince > 0L) {
                    long modDate = (modified / 1000L) * 1000L;
                    if (modDate <= ifModifiedSince) {
                        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        return;
                    }
                }
                response.setContentType(thumbnail.getMimetype());
                response.setDateHeader("Last-Modified", modified);
                //response.setDateHeader("Expires", System.currentTimeMillis() + cacheSeconds * 1000L);
                //String headerValue = "max-age=" + cacheSeconds;
                //response.setHeader("Cache-Control", headerValue);
                FileCopyUtils.copy(thumbnail.getBytes(), response.getOutputStream());
            } else {
                request.getRequestDispatcher("/static/img/thumb_1.png").forward(request, response);
            }
        }
    }
}
