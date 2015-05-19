/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * MultitenancyUtil.java
 * May 14, 2015
 *
 */
package org.opentides.util;

/**
 * @author Jeric
 *
 */
public class MultitenancyUtil {

	private static ThreadLocal<String> tenantName;

	static {
		tenantName = new ThreadLocal<String>();
	}

	/**
	 * @return the tenantName
	 */
	public static String getTenantName() {
		return tenantName.get();
	}

	public static void setTenantName(final String tenant) {
		tenantName.set(tenant);
	}
}
