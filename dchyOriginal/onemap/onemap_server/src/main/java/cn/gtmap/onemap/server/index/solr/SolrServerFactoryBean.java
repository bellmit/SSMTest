/*
 * Project:  onemap
 * Module:   server
 * File:     SolrServerFactoryBean.java
 * Modifier: xyang
 * Modified: 2013-05-17 05:45:43
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

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-1-13
 */
public class SolrServerFactoryBean implements FactoryBean<EmbeddedSolrServer>, ServletContextAware {
    private ServletContext servletContext;

    public EmbeddedSolrServer getObject() throws Exception {
        if (servletContext != null)
            return (EmbeddedSolrServer) servletContext.getAttribute(SolrDispatchFilter.class.getName());
        return null;
    }

    public Class<?> getObjectType() {
        return EmbeddedSolrServer.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
