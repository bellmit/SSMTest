/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     HostManagerTest.java
 * Modifier: xyang
 * Modified: 2013-07-25 04:18:24
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.service;

import cn.gtmap.onemap.server.monitor.collector.impl.ArcgisRestServiceMonitor;
import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.Interface;
import cn.gtmap.onemap.server.monitor.model.Item;
import com.gtis.config.EgovConfigLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-25
 */
@WebAppConfiguration("webapp")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class MonitorTest {

    static {
        EgovConfigLoader.load();
    }

    @Autowired
    private HostManager hostManager;
    @Autowired
    private InterfaceManager interfaceManager;
    @Autowired
    private ItemManager itemManager;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ArcgisRestServiceMonitor arcgisRestServiceMonitor;

    @Test
    public void importAll() throws Exception {
        arcgisRestServiceMonitor.importAll();
    }

    @Test
    public void testSaveHost() throws Exception {
        Host host = new Host();
        host.setName("192.168.50.160");
        host.setDescription("zabbix test");
        hostManager.saveHost(host);
    }

    @Test
    public void testSaveInf() throws Exception {
        Interface inf = new Interface();
        inf.setName("zabbix");
        inf.setDescription("zabbixInf");
        inf.getAttrs().put("host", "192.168.50.160");
        inf.getAttrs().put("port", "10050");
        inf.setHost(hostManager.getHostByName("192.168.50.160"));
        interfaceManager.saveInterface(inf);
    }

    @Test
    public void testSaveItem() throws Exception {
        Host host = hostManager.getHostByName("192.168.50.160");
        Interface inf = interfaceManager.getInterfaceByName(host.getId(), "zabbix");
        Item item = new Item();
        item.setName("free mem");
        item.setKey("vm.memory.size[free]");
        item.setDescription("vm.memory.size[free]");
        item.setHost(host);
        item.setInf(inf);
        itemManager.saveItem(item);
    }

    @Test
    public void testStat() throws Exception {
        System.out.println(dataManager.stat(hostManager.getHostByName("192.168.50.160").getId(), null, null, 100));
    }
}
