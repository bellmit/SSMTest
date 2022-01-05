/*
 * Project:  onemap
 * Module:   server
 * File:     SolrDispatchFilter.java
 * Modifier: xyang
 * Modified: 2013-05-10 02:40:38
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.solr;

import com.gtis.config.EgovConfigLoader;
import com.gtis.spring.Container;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-28
 */
public class SolrDispatchFilter extends org.apache.solr.servlet.SolrDispatchFilter {
    protected static final Logger LOG = LoggerFactory.getLogger(SolrDispatchFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
        EgovConfigLoader.load();
        System.setProperty("solr.solr.home", config.getServletContext().getRealPath("/WEB-INF/solr"));
        try {
            File data = ResourceUtils.getFile(System.getProperty("egov.data") + "/solr");
            LOG.info("Use solr.data.dir:[{}]", data.getAbsolutePath());
            System.setProperty("solr.data.dir", data.getAbsolutePath());
        } catch (FileNotFoundException ignored) {
            throw new ServletException("solr data dir not found");
        }
        super.init(config);
        EmbeddedSolrServer solrServer = new EmbeddedSolrServer(cores, cores.getDefaultCoreName());
        config.getServletContext().setAttribute(SolrDispatchFilter.class.getName(), solrServer);
        Container.createApplicationContext(config.getServletContext());
    }
}
