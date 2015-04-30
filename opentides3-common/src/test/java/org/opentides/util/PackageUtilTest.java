/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * PackageUtilTest.java
 * Created on March, 2 2013, 1:27:19 PM
 */
package org.opentides.util;


import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author allanctan
 *
 */
public class PackageUtilTest {
	

	@Test 
	public void testToPackageName() {
		Assert.assertEquals(null, PackageUtil.toPackageName(null));
		Assert.assertEquals("", PackageUtil.toPackageName(""));
		Assert.assertEquals(
				"template.org.ideyatech.model",
				PackageUtil.toPackageName("template" + File.separator + "org"
						+ File.separator + "ideyatech" + File.separator
						+ "model"));
	}
	
	@Test
	public void testToFolderName() {
		Assert.assertEquals(null, PackageUtil.toFolderName(null));
		Assert.assertEquals("", PackageUtil.toFolderName(""));
		Assert.assertEquals("org" + File.separator + "opentides"
				+ File.separator + "bean",
				PackageUtil.toFolderName("org.opentides.bean"));
		Assert.assertEquals(
				"org" + File.separator + "opentides" + File.separator + "bean"
						+ File.separator + "SystemCodes.java",
				PackageUtil.toFolderName("org.opentides.bean.SystemCodes.java"));
	}
	
}

