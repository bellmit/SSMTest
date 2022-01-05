/*
 * Project:  onemap
 * Module:   server
 * File:     MetadataServiceImplTest.java
 * Modifier: xyang
 * Modified: 2013-04-08 05:13:42
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

import cn.gtmap.onemap.model.MapQuery;
import cn.gtmap.onemap.service.MetadataService;
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
 * @version V1.0, 13-4-8
 */
@WebAppConfiguration("webapp")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class MetadataServiceImplTest {

    static {
        EgovConfigLoader.load();
    }

    @Autowired
    private MetadataService metadataService;

    @Test
    public void testMoveMapGroup() throws Exception {
        metadataService.moveMapGroup("013daee6f7bb297ebab43daee54100d9", false);
    }

    @Test
    public void testFindMaps() throws Exception {
        MapQuery query=MapQuery.query();
        System.out.println(metadataService.findMaps("1", query, null).getContent());
    }

    public static void main(String[] args) {

    }
}
