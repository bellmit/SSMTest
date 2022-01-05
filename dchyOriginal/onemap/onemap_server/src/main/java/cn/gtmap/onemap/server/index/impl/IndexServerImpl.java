/*
 * Project:  onemap
 * Module:   server
 * File:     IndexServerImpl.java
 * Modifier: xyang
 * Modified: 2013-05-23 08:48:17
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.impl;

import cn.gtmap.onemap.core.gis.Bound;
import cn.gtmap.onemap.model.FieldType;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.index.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-17
 */
class IndexServerImpl implements IndexServer {
    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("([\\w\\u4e00-\\u9fa5]+)\\s*(?=:)");
    private static final Logger LOG = LoggerFactory.getLogger(IndexServerImpl.class);
    private final SolrServer solr;
    private final IndexConfigManager indexConfigManager;

    private IndexConfig config;
    private boolean configChange;

    IndexServerImpl(IndexConfigManager indexConfigManager, SolrServer solr) {
        this.indexConfigManager = indexConfigManager;
        this.solr = solr;
    }

    public void setConfig(IndexConfig config) {
        if (configChange) {
            config.setFields(this.config.getFields());
            config.setLayerIds(this.config.getLayerIds());
        }
        this.config = config;
    }

    @Override
    public String getIndexId() {
        return config.getId();
    }

    @Override
    public int getWkid() {
        return config.getWkid();
    }

    @Override
    public Bound getExtent() {
        return config.getExtent();
    }

    @Override
    public Map<String, FieldType> getFields() {
        return config.getFields();
    }

    @Override
    public synchronized void reset() {
        try {
            solr.deleteByQuery("*:*");
            solr.commit();
        } catch (Exception e) {
            LOG.warn("Reset error: " + e.getMessage());
        }
    }

    @Override
    public synchronized void optimize() {
        try {
            solr.optimize();
        } catch (Exception e) {
            LOG.warn("Optimize error: " + e.getMessage());
        }
    }

    @Override
    public void add(Index index) throws Exception {
        solr.add(toDoc(index));
    }

    @Override
    public synchronized void commit() {
        if (configChange) {
            indexConfigManager.updateIndexConfig(config);
            configChange = false;
        }
        try {
            solr.commit();
        } catch (Exception e) {
            LOG.warn("Commit error: " + e.getMessage());
        }
    }

    @Override
    public int count() {
        SolrQuery query = new SolrQuery();
        query.setRows(0);
        query.setQuery("*:*");
        try {
            QueryResponse rsp = solr.query(query);
            return (int) rsp.getResults().getNumFound();
        } catch (SolrServerException ignored) {
        }
        return 0;
    }

    @Override
    public Map<String, Integer> countByMap() {
        SolrQuery query = new SolrQuery("*:*");
        query.setRows(0);
        query.addFacetField(Index.MAP);
        Map<String, Integer> map = Maps.newHashMap();
        try {
            QueryResponse rsp = solr.query(query);
            FacetField ff = rsp.getFacetField(Index.MAP);
            if (ff != null) {
                for (FacetField.Count count : ff.getValues()) {
                    map.put(count.getName(), (int) count.getCount());
                }
            }
        } catch (SolrServerException ignored) {
        }
        return map;
    }

    @Override
    public Map<String, Integer> countByLayer(String mapId) {
        SolrQuery query = new SolrQuery(StringUtils.isNotEmpty(mapId) ? Index.MAP + ":" + mapId : "*:*");
        query.setRows(0);
        query.addFacetField(Index.LAYER);
        Map<String, Integer> map = Maps.newHashMap();
        try {
            QueryResponse rsp = solr.query(query);
            FacetField ff = rsp.getFacetField(Index.LAYER);
            if (ff != null) {
                for (FacetField.Count count : ff.getValues()) {
                    map.put(count.getName(), (int) count.getCount());
                }
            }
        } catch (SolrServerException ignored) {
        }
        return map;
    }

    @Override
    public Page<Index> find(SolrQuery query) {
        try {
            query.setQuery(rewriteQuery(query.getQuery()));
            String[] fqs = query.getFilterQueries();
            if (fqs != null) {
                for (int i = 0, len = fqs.length; i < len; i++) {
                    fqs[i] = rewriteQuery(fqs[i]);
                }
                query.setFilterQueries(fqs);
            }
            List<SolrQuery.SortClause> sorts = Lists.newArrayList();
            for (SolrQuery.SortClause sortClause : query.getSorts()) {
                String name = sortClause.getItem();
                Field f = config.getField(name);
                sorts.add(f != null ? new SolrQuery.SortClause(name, sortClause.getOrder()) : sortClause);
            }
            if (!sorts.isEmpty()) {
                query.setSorts(sorts);
            }
            QueryResponse rsp = solr.query(query);
            SolrDocumentList docs = rsp.getResults();
            List<Index> indexs = Lists.newArrayListWithExpectedSize(docs.size());
            for (SolrDocument doc : docs) {
                indexs.add(toIndex(doc));
            }
            return new PageImpl<Index>(indexs, toPageable(query), docs.getNumFound());
        } catch (SolrServerException e) {
            LOG.warn("Error to find with query [" + query + "]," + e.getMessage());
        }
        return new PageImpl<Index>(Collections.<Index>emptyList());
    }


    private String rewriteQuery(String query) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = FIELD_NAME_PATTERN.matcher(query);
        while (matcher.find()) {
            Field f = config.getField(matcher.group(1));
            if (f != null) {
                matcher.appendReplacement(sb, f.getSolrFieldName());
            } else {
                matcher.appendReplacement(sb, matcher.group(1));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Index toIndex(SolrDocument doc) {
        Index index = new Index();
        index.setId(getStr(doc, Index.ID));
        index.setMapId(getStr(doc, Index.MAP));
        index.setLayerId(getStr(doc, Index.LAYER));
        index.setTitle(getStr(doc, "title"));
        index.setGeometry(getStr(doc, Index.GEOMETRY));
        for (Map.Entry<String, Object> entry : doc.entrySet()) {
            String name = entry.getKey();
            int idx = name.lastIndexOf(Constants.INDEX_SEPARATOR);
            if (idx > -1) {
                Field field = config.getField(name.substring(0, idx));
                if (field != null) {
                    index.addField(field, entry.getValue());
                }
            }
        }
        return index;
    }

    public static String getStr(SolrDocument doc, String name) {
        return (String) doc.getFieldValue(name);
    }

    private Pageable toPageable(SolrQuery query) {
        Integer start = query.getStart();
        if (start == null) {
            start = 0;
        }
        Integer size = query.getRows();
        if (size == null) {
            size = Constants.PAGE_SIZE;
        }
        List<Sort.Order> orders = Lists.newArrayList();
        for (SolrQuery.SortClause sc : query.getSorts()) {
            orders.add(new Sort.Order(sc.getOrder() == SolrQuery.ORDER.desc ? Sort.Direction.DESC : Sort.Direction.ASC, sc.getItem()));
        }
        return new PageRequest(start / size, size, orders.isEmpty() ? null : new Sort(orders));
    }

    private SolrInputDocument toDoc(Index index) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(Index.ID, index.getId());
        doc.addField(Index.MAP, index.getMapId());
        doc.addField(Index.LAYER, index.getLayerId());
        if (!config.hasLayerId(index.getLayerId())) {
            config.addLayerId(index.getLayerId());
            configChange = true;
        }
        if (index.getTitle() != null) {
            doc.addField("title", index.getTitle());
        }
        doc.addField(Index.GEOMETRY, index.getGeometry());
        for (Map.Entry<Field, Object> entry : index.getFields().entrySet()) {
            Field field = entry.getKey();
            if (!config.hasField(field.getName())) {
                config.addField(field);
                configChange = true;
            }
            doc.addField(entry.getKey().getSolrFieldName(), entry.getValue());
        }
        return doc;
    }
}
