/*
 * Project:  onemap
 * Module:   server
 * File:     ServiceProviderManager.java
 * Modifier: xyang
 * Modified: 2013-05-15 03:01:09
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service;

import cn.gtmap.onemap.model.ServiceProvider;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-26
 */
public interface ServiceProviderManager {

    /**
     * 获取服务提供者列表
     *
     * @return 服务提供者列表
     */
    List<ServiceProvider> getServiceProviders(String mapId);


    /**
     * 根据id获取服务提供者
     *
     * @param serviceProviderId 服务提供者id
     * @return 服务提供者对象
     */
    ServiceProvider getServiceProvider(String serviceProviderId);

    /**
     * 获取指定类型的服务提供者列表
     *
     * @param serviceProviderType 服务提供者类型
     * @return 服务提供者对象列表
     */
    List<ServiceProvider> getServiceProvidersByType(String serviceProviderType);

    /**
     * 根据name获取服务提供者
     *
     * @param mapId               地图id
     * @param serviceProviderType 服务提供者类型
     * @return 服务提供者对象
     */
    ServiceProvider getServiceProviderByType(String mapId, String serviceProviderType);

    /**
     * 保存一个服务提供者
     *
     * @param serviceProvider 服务提供者对象
     * @return 服务对象
     */
    ServiceProvider saveServiceProvider(ServiceProvider serviceProvider);

    /**
     * 移动一个服务提供者
     *
     * @param serviceProviderId 服务提供者id
     * @param isUp              是否向上
     */
    void moveServiceProvider(String serviceProviderId, boolean isUp);

    /**
     * 删除一个服务提供者
     *
     * @param serviceProviderId 服务提供者id
     */
    void removeServiceProvider(String serviceProviderId);
}
