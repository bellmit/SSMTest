/*
 * Project:  onemap
 * Module:   server
 * File:     IndexServerManagerImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 08:46:16
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

import cn.gtmap.onemap.server.event.IndexConfigEvent;
import cn.gtmap.onemap.server.index.IndexConfig;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import cn.gtmap.onemap.server.index.IndexServerManager;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-28
 */
public class IndexServerManagerImpl implements IndexServerManager, ServletContextAware, ApplicationListener<IndexConfigEvent> {
    protected static final Logger LOG = LoggerFactory.getLogger(IndexServerManagerImpl.class);
    private ConcurrentMap<String, IndexServerImpl> indexServers = Maps.newConcurrentMap();
    private String tplPath;

    private String basePath;
    private EmbeddedSolrServer solrServer;
    private IndexConfigManager indexConfigManager;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setSolrServer(EmbeddedSolrServer solrServer) {
        this.solrServer = solrServer;
    }

    public void setIndexConfigManager(IndexConfigManager indexConfigManager) {
        this.indexConfigManager = indexConfigManager;
    }

    @Override
    public void start(String id) {
        try {
            if (isRunning(id)) {
                return;
            }
            CoreAdminRequest.createCore(id, basePath + "/" + id, solrServer);
        } catch (Exception e) {
            LOG.error("Start core error", e);
        }
    }

    @Override
    public void stop(String id) {
        try {
            CoreAdminRequest.unloadCore(id, solrServer);
        } catch (Exception e) {
            LOG.error("Stop core error", e);
        }
    }

    @Override
    public void reload(String id) {
        try {
            CoreAdminRequest.reloadCore(id, solrServer);
        } catch (Exception e) {
            LOG.error("Reload core error", e);
        }
    }

    @Override
    public boolean isRunning(String id) {
        try {
            return CoreAdminRequest.getStatus(id, solrServer).getCoreStatus(id).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void remove(String id) {
        try {
            stop(id);
            FileUtils.deleteDirectory(new File(basePath, id));
            indexServers.remove(id);
        } catch (IOException e) {
            LOG.error("Remove core error", e);
        }
    }

    public IndexServerImpl getServer(String id) {
        IndexServerImpl server = indexServers.get(id);
        if (server == null) {
            IndexConfig config = indexConfigManager.getIndexConfig(id);
            Assert.notNull(config, "IndexConfig not found");
            server = new IndexServerImpl(indexConfigManager, new EmbeddedSolrServer(solrServer.getCoreContainer(), id));
            server.setConfig(config);
            IndexServerImpl oldServer = indexServers.putIfAbsent(id, server);
            if (oldServer != null) {
                server = oldServer;
            }
        }
        return server;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        tplPath = servletContext.getRealPath("/WEB-INF/solr/tpl");
    }

    @Override
    public void onApplicationEvent(IndexConfigEvent event) {
        String id = event.getSource().getId();
        switch (event.getType()) {
            case INSERT:
                updateCoreConfig(event.getSource());
                start(id);
                break;
            case UPDATE:
                updateCoreConfig(event.getSource());
                reload(id);
                break;
            case DELETE:
                remove(id);
                break;
        }
    }


    private void updateCoreConfig(IndexConfig indexConfig) {
        try {
            FileUtils.copyDirectory(new File(tplPath), new File(basePath, indexConfig.getId()));
            File schemaFile = new File(basePath, indexConfig.getId() + "/conf/schema.xml");
            String content = IOUtils.toString(new FileInputStream(schemaFile), "utf-8");
            IOUtils.write(StringUtils.replaceEach(content,
                    new String[]{"${worldBounds}", "${maxLevels}"},
                    new String[]{indexConfig.getExtent().toString(), String.valueOf(indexConfig.getLevel())}
            ), new FileOutputStream(schemaFile), "utf-8");
        } catch (IOException e) {
            LOG.error("Update core error", e);
        }
        String id = indexConfig.getId();
        if (indexServers.containsKey(id)) {
            indexServers.get(id).setConfig(indexConfig);
        }
    }
}
