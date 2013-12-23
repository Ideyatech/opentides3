/*
   Licensed to the Apache Software Foundation (ASF) under one
   or more contributor license agreements.  See the NOTICE file
   distributed with this work for additional information
   regarding copyright ownership.  The ASF licenses this file
   to you under the Apache License, Version 2.0 (the
   "License"); you may not use this file except in compliance
   with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing,
   software distributed under the License is distributed on an
   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
   KIND, either express or implied.  See the License for the
   specific language governing permissions and limitations
   under the License.    
 */
package org.opentides.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.opentides.util.imageutil.Image;
import org.opentides.util.imageutil.ImageLoader;

/**
 * @author allantan
 *
 */
public class ImageUtilTest {

	@Test
	public void testTransformImage() throws IOException {
		Image img = ImageLoader.fromFile("src/test/resources/images/bmw.jpg");
		compareImage(img,"32");
		compareImage(img,"x64");
		compareImage(img,"500x500");
		compareImage(img,"320-c");
		compareImage(img,"320x500-c");
		compareImage(img,"100x100-c@500x400");
		compareImage(img,"700x100");
		compareImage(img,"320x320");		
	}
	
	private void compareImage(Image img, String command) throws IOException {
		Image rez = ImageUtil.transformImage(img, command);
		rez.writeToFile(new File("src/test/resources/images/actual/bmw_"+command+".png"));
		byte[] expected = FileUtil.readFileAsBytes(new File("src/test/resources/images/expected/bmw_"+command+".png"));
		byte[] actual   = FileUtil.readFileAsBytes(new File("src/test/resources/images/actual/bmw_"+command+".png"));
		Assert.assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testIsValidCommand() {
		Assert.assertTrue(ImageUtil.isValidCommand("32"));
		Assert.assertTrue(ImageUtil.isValidCommand("x50"));
		Assert.assertTrue(ImageUtil.isValidCommand("32x50"));
		Assert.assertTrue(ImageUtil.isValidCommand("32-c"));
		Assert.assertTrue(ImageUtil.isValidCommand("32x50-c"));
		Assert.assertTrue(ImageUtil.isValidCommand("32x50-c@100x100"));
		Assert.assertFalse(ImageUtil.isValidCommand("x"));
		Assert.assertFalse(ImageUtil.isValidCommand("x50-c"));
		Assert.assertFalse(ImageUtil.isValidCommand("32x50a"));
		Assert.assertFalse(ImageUtil.isValidCommand("32bx20"));
		Assert.assertFalse(ImageUtil.isValidCommand("100x100x100"));
		Assert.assertFalse(ImageUtil.isValidCommand("32x50@100x100"));
		Assert.assertFalse(ImageUtil.isValidCommand("32x"));
	}

}
