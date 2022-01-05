/*
 * Project:  onemap
 * Module:   monitor-server
 * File:     ItemManagerImpl.java
 * Modifier: xyang
 * Modified: 2013-07-05 04:44:26
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.service.impl;

import cn.gtmap.onemap.core.support.hibernate.Hibernates;
import cn.gtmap.onemap.core.support.hibernate.Repository;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import com.mysema.query.types.Predicate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static cn.gtmap.onemap.server.monitor.model.QItem.item;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-5
 */
@Transactional(readOnly = true)
public class ItemManagerImpl implements ItemManager {
    private Repository<Item, Integer> itemDAO;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        itemDAO = Hibernates.repo(Item.class, sessionFactory);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDAO.list();
    }

    @Override
    public List<Item> getItems(int hostId) {
        return itemDAO.list(item.host.id.eq(hostId));
    }

    @Override
    public Page<Item> getItems(int hostId, Pageable pageable) {
        return itemDAO.find(item.host.id.eq(hostId).and(item.enabled.eq(true)), pageable);
    }

    @Override
    public Page<Item> findItems(Predicate predicate, Pageable pageable) {
        return itemDAO.find(predicate, pageable);
    }

    @Override
    public Item getItem(int id) {
        return itemDAO.load(id);
    }

    @Override
    public Item getItemByKey(int hostId, String key) {
        return itemDAO.get(item.host.id.eq(hostId).and(item.key.eq(key)));
    }

    @Override
    @Transactional
    public Item saveItem(Item item) {
        return itemDAO.save(item);
    }

    @Override
    @Transactional
    public void setItemsStatus(Collection<Integer> ids, Status status) {
        for (Integer id : ids) {
            Item item = getItem(id);
            switch (status) {
                case ENABLED:
                    if (!item.isEnabled()) {
                        item.setEnabled(true);
                        itemDAO.save(item);
                    }
                    break;
                case DISABLED:
                    if (item.isEnabled()) {
                        item.setEnabled(false);
                        itemDAO.save(item);
                    }
                    break;
                case DELETED:
                    itemDAO.delete(item);
                    break;
            }
        }
    }

	@Override
	@Transactional
	public void removeItem(Item item) {
		itemDAO.delete(item);
	}
}
