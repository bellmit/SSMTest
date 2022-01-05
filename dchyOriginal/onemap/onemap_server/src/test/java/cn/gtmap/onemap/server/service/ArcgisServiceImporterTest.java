/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisServiceImporterTest.java
 * Modifier: xyang
 * Modified: 2013-03-28 09:40:34
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

import cn.gtmap.onemap.server.arcgis.ArcgisServiceImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class ArcgisServiceImporterTest {
    @Autowired
    private ArcgisServiceImporter arcgisServiceImporter;

    @Test
    public void testImportMap() throws Exception {
        arcgisServiceImporter.importMap( "http://192.168.1.125:8399/arcgis/services/JinTan/BPDK_H_320482_2010/MapServer", null, null);
    }

    @Test
    public void testImportMaps() throws Exception {

    }
}
