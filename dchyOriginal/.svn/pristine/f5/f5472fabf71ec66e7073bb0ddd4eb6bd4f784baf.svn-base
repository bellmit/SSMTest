/*
 * Project:  onemap
 * Module:   server
 * File:     ExplodedTileFile.java
 * Modifier: xyang
 * Modified: 2013-05-10 01:21:09
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.arcgis.tile;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-9
 */
public class ExplodedTileFile implements TileFile {
    protected File file;

    public ExplodedTileFile(File file) {
        this.file = file;
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public void transferTo(WritableByteChannel target) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.getChannel().transferTo(0, file.length(), target);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
}
