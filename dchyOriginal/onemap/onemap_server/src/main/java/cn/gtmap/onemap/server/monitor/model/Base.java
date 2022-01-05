/*
 * Project:  onemap
 * Module:   onemap-common
 * File:     Base.java
 * Modifier: xyang
 * Modified: 2013-06-13 02:05:32
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.model;

import cn.gtmap.onemap.core.key.Keyable;
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-13
 */
@MappedSuperclass
public class Base implements Keyable<String>, Comparable<Base>, Cloneable, Serializable {
    private static final long serialVersionUID = -781009966058145715L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 256, nullable = false)
    private String name;
    @Column(length = 1024)
    private String description;
    private boolean enabled = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @JSONField(serialize = false)
    public String getKey() {
        return name;
    }

    @Override
    public int compareTo(Base o) {
        return getKey().compareTo(o.getKey());
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ignored) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Base && getKey().equals(((Base) o).getKey());
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + id + "," + getKey() + "]";
    }
}
