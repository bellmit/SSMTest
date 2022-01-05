/*
 * Project:  onemap
 * Module:   server
 * File:     IndexConfigManagerImpl.java
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

import cn.gtmap.onemap.core.event.EventType;
import cn.gtmap.onemap.server.dao.IndexConfigDAO;
import cn.gtmap.onemap.server.event.IndexConfigEvent;
import cn.gtmap.onemap.server.index.IndexConfig;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-26
 */
public class IndexConfigManagerImpl implements IndexConfigManager, ApplicationContextAware, InitializingBean {
    private IndexConfigDAO indexConfigDAO;
    private ApplicationContext appCtx;

    @Autowired
    public void setIndexConfigDAO(IndexConfigDAO indexConfigDAO) {
        this.indexConfigDAO = indexConfigDAO;
    }

    @Override
    public List<IndexConfig> getIndexConfigs() {
        return indexConfigDAO.findAll();
    }

    @Override
    public IndexConfig getIndexConfig(String id) {
        return indexConfigDAO.findOne(id);
    }

    @Override
    @Transactional
    public IndexConfig saveIndexConfig(IndexConfig indexConfig) {
        boolean isCreate = false;
        if (StringUtils.isEmpty(indexConfig.getId())) {
            indexConfig.setCreateAt(new Date());
            isCreate = true;
        }
        indexConfigDAO.save(indexConfig);
        appCtx.publishEvent(new IndexConfigEvent(isCreate ? EventType.INSERT : EventType.UPDATE, indexConfig));
        return indexConfig;
    }

    @Override
    @Transactional
    public void updateIndexConfig(IndexConfig indexConfig) {
        indexConfigDAO.save(indexConfig);
    }

    @Override
    @Transactional
    public void removeIndexConfig(String id) {
        IndexConfig ic = indexConfigDAO.findOne(id);
        indexConfigDAO.delete(ic);
        appCtx.publishEvent(new IndexConfigEvent(EventType.DELETE, ic));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (IndexConfig config : getIndexConfigs()) {
            if (config.isEnabled()) {
                appCtx.publishEvent(new IndexConfigEvent(EventType.INSERT, config));
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }
}
