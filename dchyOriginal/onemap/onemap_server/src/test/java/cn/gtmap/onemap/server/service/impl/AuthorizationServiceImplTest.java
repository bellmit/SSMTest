/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     AuthorizationServiceImplTest.java
 * Modifier: xyang
 * Modified: 2013-11-12 07:57:04
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

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.server.service.BaseServiceTest;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-12
 */
public class AuthorizationServiceImplTest extends BaseServiceTest {
	String VIDEO_RESOURCE = "omp-video:";

	@Autowired
	AuthorizationService authorizationService;

	@Test
	public void testGetPermittedPrivileges() throws Exception {
		Set<String> set = Sets.newHashSet();
		String[] operaNames = new String[]{Operation.VIEW};
		Set<Privilege> privileges = authorizationService.getPermittedPrivileges("FD8650E57BE445528FABAA8359CB2BC5", VIDEO_RESOURCE);
		for (Privilege privilege : privileges) {
			for (Operation operation : privilege.getOperations()) {
				if (contains(operaNames, operation.getName(), true)) {
					set.add(privilege.getResource());
					break;
				}
			}
		}
		System.out.println(privileges);
	}

	/**
	 * 查看数组中是否包含目标字符串
	 *
	 * @param src
	 * @param des
	 * @param ignoreCase
	 * @return
	 */
	private boolean contains(String[] src, String des, boolean ignoreCase) {
		for (String item : src) {
			if (StringUtils.isNotBlank(item) && StringUtils.isNotBlank(des)) {
				if (ignoreCase) {
					if (item.equalsIgnoreCase(des)) {
						return true;
					}
				} else {
					if (item.equals(des)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Test
	public void testGetChildrenPrivileges() throws Exception {
		Object o = authorizationService.getPermittedPrivileges("1", "omp-functions:tpl");
		System.out.println(o);
	}
}
