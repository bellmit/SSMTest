/*
 * Project:  onemap
 * Module:   server
 * File:     RelationType.java
 * Modifier: xyang
 * Modified: 2013-05-23 05:36:18
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.arcgis;

public enum RelationType {
    INTERSECTS, CONTAINS, CROSSES, ENVELOPE_INTERSECTS, INDEX_INTERSECTS, OVERLAPS, TOUCHES, WITHIN
}
