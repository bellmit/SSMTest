/*
 * Project:  onemap
 * Module:   server
 * File:     IndexConfigManager.java
 * Modifier: xyang
 * Modified: 2013-05-22 08:53:29
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-8
 */
public interface IndexConfigManager {
    /**
     * 获取索引服务配置列表
     *
     * @return 索引服务配置列表
     */
    List<IndexConfig> getIndexConfigs();

    /**
     * 根据配置id获取索引配置
     *
     * @param id 索引配置id
     * @return 索引配置
     */
    IndexConfig getIndexConfig(String id);

    /**
     * 保存索引配置,触发indexConfig改变事件
     *
     * @param indexConfig 索引配置
     * @return 索引配置
     */
    IndexConfig saveIndexConfig(IndexConfig indexConfig);

    /**
     * 保存索引配置,不触发事件
     *
     * @param indexConfig 索引配置
     */
    void updateIndexConfig(IndexConfig indexConfig);

    /**
     * 删除索引配置
     *
     * @param id 索引配置id
     */
    void removeIndexConfig(String id);
}
