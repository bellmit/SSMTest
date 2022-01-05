/*
 * Project:  onemap
 * Module:   server
 * File:     CompactTileFile.java
 * Modifier: xyang
 * Modified: 2013-05-10 01:43:05
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
 * @version V1.0, 13-5-10
 */
public class CompactTileFile extends ExplodedTileFile {
    private final long offset;

    public CompactTileFile(File file, long offset) {
        super(file);
        this.offset = offset;
    }

    @Override
    public void transferTo(WritableByteChannel target) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.skip(offset);
            byte[] lengthBytes = new byte[4];
            fis.read(lengthBytes);
            int length =
                    (lengthBytes[0] & 0xff) +
                            (lengthBytes[1] & 0xff) * 256 +
                            (lengthBytes[2] & 0xff) * 65536 +
                            (lengthBytes[3] & 0xff) * 16777216;
            fis.getChannel().transferTo(offset + 4, length, target);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
}
