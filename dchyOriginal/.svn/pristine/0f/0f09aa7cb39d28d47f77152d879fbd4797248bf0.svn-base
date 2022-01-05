/*
 * Project:  onemap
 * Module:   server
 * File:     GeometryServerServiceHandle.java
 * Modifier: xyang
 * Modified: 2013-05-29 04:13:14
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

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.core.template.ModelRenderer;
import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.arcgis.EsriJsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-28
 */
public class GeometryServerServiceHandle {

    @Autowired
    protected ModelRenderer modelRenderer;

    public void handle(String method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Model model = new ExtendedModelMap();
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        if (method == null) {
            modelRenderer.render("arcgis/geometry", model, request, response);
        } else {
            if (RequestUtils.isPost(request)) {
                if ("project".equals(method)) {
                    project(
                            request.getParameter("geometries"),
                            RequestUtils.getInt(request, "inSR"),
                            RequestUtils.getInt(request, "outSR"),
                            model);
                } else if ("simplify".equals(method)) {
                    simplify(
                            request.getParameter("geometries"),
                            RequestUtils.getInt(request, "sr"),
                            RequestUtils.getDouble(request, "tolerance", 1e-9),
                            model);
                } else if ("buffer".equals(method)) {
                    buffer(
                            request.getParameter("geometries"),
                            RequestUtils.getInt(request, "inSR"),
                            RequestUtils.getInt(request, "outSR"),
                            RequestUtils.getInt(request, "bufferSR"),
                            request.getParameter("distances"),
                            request.getParameter("unit"),
                            RequestUtils.getBool(request, "bufferSR", false),
                            model);
                } else if ("areasAndLengths".equals(method)) {
                    areasAndLengths(
                            request.getParameter("polygons"),
                            RequestUtils.getInt(request, "sr"),
                            RequestUtils.getInt(request, "lengthUnit"),
                            model);
                } else if ("lengths".equals(method)) {
                    lengths(
                            request.getParameter("polylines"),
                            RequestUtils.getInt(request, "sr"),
                            RequestUtils.getInt(request, "lengthUnit"),
                            model);
                } else if ("densify".equals(method)) {
                    densify(
                            request.getParameter("geometries"),
                            RequestUtils.getInt(request, "maxSegmentLength"),
                            model);
                } else if ("distance".equals(method)) {
                    distance(
                            request.getParameter("geometry1"),
                            request.getParameter("geometry2"),
                            RequestUtils.getInt(request, "sr"),
                            model);
                }
            }
            modelRenderer.render("arcgis/geometry/" + method, model, request, response);
        }
    }

    private void project(String geometries, int inSR, int outSR, Model model) {
        List<JSONObject> list = Lists.newArrayList();
        for (Geometry geo : EsriJsonUtil.json2Geometries(geometries)) {
            list.add(EsriJsonUtil.geometry2JSON(GeoUtils.project(geo, inSR, outSR)));
        }
        model.addAttribute("geometries", list);
    }

    private void simplify(String geometries, int sr, double tolerance, Model model) {
        List<JSONObject> list = Lists.newArrayList();
        for (Geometry geo : EsriJsonUtil.json2Geometries(geometries)) {
            list.add(EsriJsonUtil.geometry2JSON(GeoUtils.simplify(geo, tolerance)));
        }
        model.addAttribute("geometries", list);
    }

    private void buffer(String geometries, int inSR, Integer outSR, Integer bufferSR, String distancesStr, String unit, boolean unionResults, Model model) {
        if (outSR == null) {
            outSR = inSR;
        }
        if (bufferSR == null) {
            bufferSR = inSR;
        }
        List<JSONObject> list = Lists.newArrayList();
        List<Geometry> json2Geometries = EsriJsonUtil.json2Geometries(geometries);
        String[] arr = StringUtils.split(distancesStr, ',');
        Geometry[] geos = new Geometry[json2Geometries.size()];
        for (int i = 0; i < json2Geometries.size(); i++) {
            Geometry geo = json2Geometries.get(i);
            geo = GeoUtils.buffer(geo, inSR, outSR, bufferSR, Double.parseDouble(arr[i]));
            if (unionResults) {
                geos[i] = geo;
            }
            list.add(EsriJsonUtil.geometry2JSON(geo));
        }
        if (unionResults) {
            list.add(EsriJsonUtil.geometry2JSON(new GeometryFactory().createGeometryCollection(geos).union()));
        }
        model.addAttribute("geometries", list);
    }

    private void areasAndLengths(String polygons, Integer sr, Integer lengthUnit, Model model) {
        List<Double> areas = Lists.newArrayList();
        List<Double> lengths = Lists.newArrayList();
        for (Geometry geo : EsriJsonUtil.json2Geometries(polygons)) {
            lengths.add(geo.getLength());
            areas.add(geo.getArea());
        }
        model.addAttribute("areas", areas);
        model.addAttribute("lengths", lengths);
    }

    private void lengths(String polylines, Integer sr, Integer lengthUnit, Model model) {
        List<Double> lengths = Lists.newArrayList();
        for (Geometry geo : EsriJsonUtil.json2Geometries(polylines)) {
            lengths.add(geo.getLength());
        }
        model.addAttribute("lengths", lengths);
    }

    private void densify(String geometries, int maxSegmentLength, Model model) {
        List<JSONObject> list = Lists.newArrayList();
        for (Geometry geo : EsriJsonUtil.json2Geometries(geometries)) {
            list.add(EsriJsonUtil.geometry2JSON(GeoUtils.densify(geo, maxSegmentLength)));
        }
        model.addAttribute("geometries", list);
    }

    private void distance(String geometry1, String geometry2, int sr, Model model) {
        model.addAttribute("distance", EsriJsonUtil.json2Geometry(geometry1).distance(EsriJsonUtil.json2Geometry(geometry2)));
    }
}
