/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     TplType.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 下午3:21:33
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import cn.gtmap.onemap.core.entity.AbstractEntity;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 下午3:21:33
 */
@Entity
@Table(name="om_tpl_type")
public class TplType extends AbstractEntity{
	private static final long serialVersionUID = 8760847713240602043L;
	
	@Lob
	private byte[] thumnail;

    @Column
    private String title;

	public byte[] getThumnail() {
		return thumnail;
	}

	public void setThumnail(byte[] thumnail) {
		this.thumnail = thumnail;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}