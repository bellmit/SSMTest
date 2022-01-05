/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     FileLogAccessor.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-13 下午1:54:12
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.log.server.model;

import java.io.File;

import cn.gtmap.onemap.core.util.DateUtils;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-13 下午1:54:12
 */
public class WebServerLogAccessor {
	private String name;
	private String lastModified;
	private String size;
	private File file;
	
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(long millisecond) {
		this.lastModified = DateUtils.DATETIME_FORMAT.format(millisecond);
	}

	public String getSize() {
		return size;
	}

	public void setSize(long size) {
		long s = size / 1024;
		this.size = String.valueOf(s) + "kb";
	}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetClass() {
        return "stdout";
    }

    public String getLogType() {
        return "stdout";
    }

    public String getConversionPattern() {
        return "";
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
