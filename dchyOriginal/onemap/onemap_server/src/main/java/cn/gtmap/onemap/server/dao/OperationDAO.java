/*
 * Project:  onemap
 * Module:   server
 * File:     OperationDAO.java
 * Modifier: xyang
 * Modified: 2013-06-04 02:19:03
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.dao;

import cn.gtmap.onemap.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-3
 */
public interface OperationDAO extends JpaRepository<Operation, String> {
}
