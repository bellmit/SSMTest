/*
 * Project:  onemap
 * Module:   server
 * File:     RegionServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-03 02:27:59
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.core.support.jpa.Filter;
import cn.gtmap.onemap.model.Region;
import cn.gtmap.onemap.server.dao.RegionDAO;
import cn.gtmap.onemap.service.RegionService;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.gtmap.onemap.model.QRegion.region;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-16
 */
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionDAO regionDAO;

    private GeometryFactory geometryFactory;

    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    @Override
    public Region getRegion(String id) {
        return regionDAO.findOne(id);
    }

    @Override
    public List<Region> getAllRegions() {
        return regionDAO.findAll(new Sort("id"));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Region> getChildrenRegions(String id, boolean recursively) {
        List<Region> regions = (List) regionDAO.findAll(StringUtils.isEmpty(id) ? region.parent.isNull() : region.parent.id.eq(id), region.id.asc());
        if (recursively) {
            initChildren(regions);
        }
        return regions;
    }

    private void initChildren(List<Region> regions) {
        for (Region region : regions) {
            initChildren(region.getChildren());
        }
    }

    @Override
    public Map<String, String> getRegionPolygons(Collection<String> ids, Double distanceTolerance, Integer wkid) {
        Map<String, String> map = Maps.newHashMapWithExpectedSize(ids.size());
        for (String id : ids) {
            String geoString = getGeometry(getRegion(id).getGeometry(), distanceTolerance, wkid);
            if (geoString != null) {
                map.put(id, geoString);
            }
        }
        return map;
    }

    private String getGeometry(String geometryString, Double distanceTolerance, Integer wkid) {
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            Geometry geometry = reader.read(geometryString);
            if (distanceTolerance != null) {
                geometry = Densifier.densify(geometry, distanceTolerance);
            }
            if (wkid != null) {
                geometry = GeoUtils.project(geometry, geometryFactory.getSRID(), wkid);
            }
            return geometry.toText();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Deprecated
    public Page<Region> findRegion(List<Filter> filters, Pageable request) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Specification<Region> spec = Filter.byFilter(filters, Region.class);
        if (request == null) {
            return new PageImpl<Region>(regionDAO.findAll(spec, sort));
        } else {
            if (request.getSort() == null) {
                request = new PageRequest(request.getPageNumber(), request.getPageSize(), sort);
            }
            return regionDAO.findAll(spec, request);
        }
    }

    @Override
    @Transactional
    public Region saveRegion(Region region) {
        return regionDAO.save(region);
    }

    @Override
    @Transactional
    public void removeRegion(String id) {
        regionDAO.delete(id);
    }
}
