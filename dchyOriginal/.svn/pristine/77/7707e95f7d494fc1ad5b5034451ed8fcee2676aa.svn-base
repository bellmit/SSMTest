/*
 * Project:  onemap
 * Module:   server
 * File:     RequestInterceptor.java
 * Modifier: xyang
 * Modified: 2013-05-28 02:52:10
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

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.MapStatus;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.handle.RequestHandler;
import cn.gtmap.onemap.service.MetadataService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import static cn.gtmap.onemap.core.util.ArrayUtils.subArray;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-8
 */
public class RequestInterceptor implements HandlerInterceptor {
    public static final Pattern HEX_STRING_PATTERN = Pattern.compile("^[\\da-fA-F]+$");
    private static final Logger LOG = LoggerFactory.getLogger(RequestInterceptor.class);
    private UrlPathHelper urlPathHelper = RequestUtils.URL_PATH_HELPER;
    private java.util.Map<String, RequestHandler> handlers;
    private MetadataService metadataService;

    public void setHandlers(java.util.Map<String, RequestHandler> handlers) {
        this.handlers = handlers;
    }

    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = urlPathHelper.getRequestUri(request).substring(urlPathHelper.getContextPath(request).length() + 1);
        int index = path.indexOf("/");
        String handleName = index == -1 ? path : path.substring(0, index);
        RequestHandler handle = handlers.get(handleName);
        if (handle != null) {
            try {
                service(handle, index == -1 ? "" : path.substring(index + 1), request, response);
            } catch (Exception e) {
                LOG.info("Proccess handle [path:" + handleName + ",name:" + handle + "] error", e);
//                throw e;
            }
            return false;
        }
        return true;
    }

    private void service(RequestHandler handle, String path, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] paths = StringUtils.split(path, '/');
        String[] subPaths = ArrayUtils.EMPTY_STRING_ARRAY;
        Map map = null;
        if (ArrayUtils.isNotEmpty(paths)) {
            String mapName = paths[0];
            if (mapName.length() == 32 && HEX_STRING_PATTERN.matcher(mapName).matches()) {
                map = metadataService.getMap(mapName);
                subPaths = subArray(paths, 1);
            } else if (paths.length == 1) {
                map = metadataService.getMapByName(mapName);
                if (map == null) {
                    subPaths = paths;
                }
            } else {
                String str = paths[1];
                if (Constants.MAP_SERVER.equals(str)) {
                    map = metadataService.getMapByName(mapName);
                    subPaths = subArray(paths, 1);
                } else if (Constants.GEOMETRY_SERVER.equals(str)) {
                    subPaths = subArray(paths, 1);
                } else {
                    map = metadataService.getMapByName(str);
                    if (map == null) {
                        map = metadataService.getMapByName(mapName);
                        subPaths = subArray(paths, 1);
                    } else {
                        subPaths = subArray(paths, 2);
                    }
                }
            }
        }
        if (map != null && map.getStatus() != MapStatus.RUNNING) {
            throw new OneMapException("Service [" + map.getId() + "] not running");
        }
        handle.handle(map, subPaths, request, response);
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
