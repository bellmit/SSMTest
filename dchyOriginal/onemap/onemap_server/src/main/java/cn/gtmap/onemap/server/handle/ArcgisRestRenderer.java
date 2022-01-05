/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisRestRenderer.java
 * Modifier: xyang
 * Modified: 2013-05-17 12:15:32
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

import cn.gtmap.onemap.core.gis.SpatialReference;
import cn.gtmap.onemap.core.support.jpa.Filter;
import cn.gtmap.onemap.core.template.ModelRenderer;
import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.service.MetadataService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-9
 */
public class ArcgisRestRenderer {
    protected MetadataService metadataService;
    protected ServiceProviderManager serviceProviderManager;
    protected ModelRenderer modelRenderer;

    @Autowired
    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Autowired
    public void setServiceProviderManager(ServiceProviderManager serviceProviderManager) {
        this.serviceProviderManager = serviceProviderManager;
    }

    @Autowired
    public void setModelRenderer(ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
    }

    public void renderCatalog(String folder, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Model model = new ExtendedModelMap();
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        List<String> folders = Lists.newArrayList();
        if (folder == null) {
            for (MapGroup group : metadataService.getAllMapGroups()) {
                folders.add(group.getName());
            }
        }
        model.addAttribute("folders", folders);
        List<java.util.Map<String, String>> services = new ArrayList<java.util.Map<String, String>>();
        for (Map map : metadataService.getMaps(Collections.singletonList(folder == null ? new Filter("group", Filter.Operator.NULL, null) : new Filter("group.name", Filter.Operator.EQ, folder)), null)) {
            if (!map.isEnabled()) {
                continue;
            }
            java.util.Map<String, String> map1 = Maps.newHashMap();
            map1.put("name", (folder == null ? "" : folder + "/") + map.getName());
            map1.put("type", "MapServer");
            services.add(map1);
        }
        model.addAttribute("services", services);
        model.addAttribute("folder", folder);
        modelRenderer.render("arcgis/catalog", model, request, response);
    }

    public void renderMap(Model model, Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model == null) {
            model = new ExtendedModelMap();
        }
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        model.addAttribute("mapName", map.getName());
        if (map.getGroup() != null) {
            model.addAttribute("folder", map.getGroup().getName());
        }
        model.addAttribute("mapName", map.getName());
        SpatialReference sr = (SpatialReference) model.asMap().get("spatialReference");
        if (sr == null) {
            sr = new SpatialReference(map.getWkid());
        }
        model.addAttribute("spatialReference", sr);

        model.addAttribute("minScale", map.getMinScale());
        model.addAttribute("maxScale", map.getMaxScale());
        if (!model.containsAttribute("initialExtent")) {
            JSONObject extent = (JSONObject) JSON.toJSON(map.getExtent());
            extent.put("spatialReference", sr);
            model.addAttribute("initialExtent", extent);
            model.addAttribute("fullExtent", extent);
        }
        model.addAttribute("units", map.getUnits());
        model.addAttribute("singleFusedMapCache", true);
        model.addAttribute("description", map.getDescription());
        model.addAttribute("folder", map.getGroup() == null ? null : map.getGroup().getName());
        model.addAttribute("map", map.getName());
        if (!model.containsAttribute(Constants.TILE_INFO)) {
            for (ServiceProvider sp : serviceProviderManager.getServiceProviders(map.getId())) {
                if (Constants.PROVIDER_LOCAL_TILE.equals(sp.getType())) {
                    model.addAttribute(Constants.TILE_INFO, JSON.parseObject(sp.getAttribute(Constants.TILE_INFO)));
                    break;
                }
            }
        }

        List<java.util.Map<String, Object>> layers = Lists.newArrayList();
        for (Layer layer : metadataService.getLayers(map.getId())) {
            java.util.Map<String, Object> map1 = Maps.newHashMap();
            map1.put("id", layer.getIndex());
            map1.put("name", layer.getAlias());
            map1.put("parentLayerId", -1);
            map1.put("defaultVisibility", layer.isVisibility());
            layers.add(map1);
        }
        model.addAttribute("layers", layers);
        String viewName = "arcgis/map";
        if ("jsapi".equals(request.getParameter("f"))) {
            viewName += "-jsapi";
        }
        modelRenderer.render(viewName, model, request, response);
    }

    public void renderLayer(Model model, Map map, String layerId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model == null) {
            model = new ExtendedModelMap();
        }
        model.addAttribute("map", map.getName());
        if (map.getGroup() != null) {
            model.addAttribute("folder", map.getGroup().getName());
        }
        String viewName;
        if ("layers".equals(layerId)) {
            List<Model> layers = Lists.newArrayList();
            for (Layer layer1 : metadataService.getLayers(map.getId())) {
                Model model1 = new ExtendedModelMap();
                convertLayer(model1, layer1, map);
                layers.add(model1);
            }
            model.addAttribute("layers", layers);
            viewName = "arcgis/layer-all";
        } else {
            int index = Integer.parseInt(layerId);
            convertLayer(model, metadataService.getLayerByIndex(map.getId(), index), map);
            viewName = "arcgis/layer";
        }
        modelRenderer.render(viewName, model, request, response);
    }

    private void convertLayer(Model model, Layer layer, Map map) {
        model.addAttribute("currentVersion", 10.1);
        model.addAttribute("id", layer.getIndex());
        model.addAttribute("name", layer.getAlias());
        model.addAttribute("type", layer.getCatalog());
        model.addAttribute("minScale", layer.getMinScale());
        model.addAttribute("maxScale", layer.getMaxScale());
        model.addAttribute("defaultVisibility", layer.isVisibility());
        JSONObject extent = (JSONObject) JSON.toJSON(layer.getExtent() != null ? layer.getExtent() : map.getExtent());
        extent.put("spatialReference", new SpatialReference(map.getWkid()));
        model.addAttribute("extent", extent);
        model.addAttribute("displayField", layer.getDisplayField());
        model.addAttribute("description", layer.getDescription());
        List<java.util.Map<String, Object>> fields = Lists.newArrayList();
        for (Field field : metadataService.getFields(layer.getId())) {
            java.util.Map<String, Object> map1 = Maps.newHashMap();
            map1.put("name", field.getName());
            map1.put("alias", field.getAlias());
            map1.put("type", field.getType().getEsriType());
            map1.put("length", field.getLength());
            fields.add(map1);
        }
        model.addAttribute("fields", fields);
    }
}
