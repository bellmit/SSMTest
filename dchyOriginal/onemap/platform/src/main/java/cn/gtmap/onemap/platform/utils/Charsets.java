/*
 * Project:  hydroplat-parent
 * Module:   hydroplat-common
 * File:     Charsets.java
 * Modifier: yangxin
 * Modified: 2014-06-11 10:38
 *
 * Copyright (c) 2014 Mapjs All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent
 * or the registration of a utility model, design or code.
 */

package cn.gtmap.onemap.platform.utils;

import java.nio.charset.Charset;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-10-11
 */
public final class Charsets {

    public static final String UTF8 = "UTF-8".intern();
    public static final String GBK = "GBK";
    public static final String ISO88591 = "ISO-8859-1";
    public static final Charset CHARSET_UTF8 = Charset.forName(UTF8);
    public static final Charset CHARSET_GBK = Charset.forName(GBK);
    public static final Charset CHARSET_ISO88591 = Charset.forName(ISO88591);

    private Charsets() {
    }
}
