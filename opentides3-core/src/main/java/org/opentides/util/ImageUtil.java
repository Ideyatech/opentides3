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

import imageUtil.Image;
import imageUtil.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/*
 * Static helper functions for image manipulation.
 * 
 * ImageUtil.java
 * @author allanctan, ajalbaniel
 * 
 * Created on Apr 12, 2011 9:13:36 PM
 */
public class ImageUtil {

	private static Logger _log = Logger.getLogger(ImageUtil.class);
	
	// Default image is 1x1 png
	private static final byte[] DEFAULT_IMAGE = StringUtil.convertHexToArray(
			"89504E470D0A1A0A0000000D49484452000000010000000" +
			"1010300000025DB56CA00000003504C5445000000A77A3D" +
			"DA0000000174524E530040E6D8660000000A4944415408D" +
			"76360000000020001E221BC330000000049454E44AE426082");	
		
	// default dimension of large image
	private static int DEFAULT_SIZE  = 64;
 

	/**
	 * Loads the image from cache if available.
	 * 
	 * @param fullPath
	 * @return
	 */
	public static byte[] loadImage(String fullPath, String command) {
		try {
			String processedPath = processPath(fullPath, command);			
			File file = new File(processedPath);
			File oFile = new File(fullPath);
			if (file.exists())
				return FileUtil.readFileAsBytes(file);
			else if (!StringUtil.isEmpty(command) && oFile.exists()) {
				// file doesn't exist yet, needs pre-processing				
				Image img = ImageLoader.fromFile(oFile);
				Image rez = ImageUtil.transformImage(img, command);				
				rez.writeToFile(file);
				return FileUtil.readFileAsBytes(file);
			}
			return DEFAULT_IMAGE;
		} catch (Exception e) { 
			_log.error("Failed to load image.", e);
			return null;
		}
	}
	
	/**
	 * Private helper that creates the appropriate filename 
	 * based on the command.
	 * 
	 * @param fullPath
	 * @param command
	 * @return
	 */
	private static String processPath(String fullPath, String command) {
		if (StringUtil.isEmpty(command))
			return fullPath;
		int idx = fullPath.lastIndexOf(".");
		if (idx > 0)
			return fullPath.substring(0, idx) + "_" + command +".png";
		else 
			return fullPath + "_" + command + ".png";
	}

	/**
	 * Checks if image is larger than the specified size.
	 * @author AJ
	 * 
	 * @param inputStream
	 * @param width
	 * @param height
	 * @return
	 */
	public static boolean isLargerThan(InputStream inputStream, int width, int height) {
		try {
			BufferedImage image = ImageIO.read(inputStream);
			Integer iWidth = image.getWidth();
			Integer iHeight = image.getHeight();
			if(iWidth >= width && iHeight >= height)
				return true;
			else
				return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Transforms the image by resizing or cropping the image.
	 * Command can be any of the following:
	 * 32      - resizes the width to 32px and keeps aspect ratio
	 * x50     - resizes the height to 50px and keep aspect ratio
	 * 32x50   - converts the image to max of 32px width or 5px height depending 
	 *           which produces the larger image
	 * 32-c    - crops the image to 32 x 32 pixels from the center
	 * 32x50-c - crops the image to 32 x 50 pixels from the center
	 * 32x50-c@100x100 - crops the image to 32 x 50 pixels starting at location 100x100
	 * 
	 * @param img
	 * @param command
	 */
	public static final Image transformImage(Image img, String command) {
		if (StringUtil.isEmpty(command) || img == null)
			return img;
		// validate if command is in correct syntax
		if (!isValidCommand(command)) {
			_log.error("Cannot recognize transform command ["+command+"]. " +
					   "Check Javadoc for instructions.");
			return img;
		}
		int oWidth = img.getWidth();
		int oHeight = img.getHeight();
		if (command.contains("-c")) {
			// we are cropping the image
			String[] c1 = command.split("-c");
			int t = -1, l = -1, w = 0, h = 0;
			String[] c2 = c1[0].split("x");
			w = StringUtil.convertToInt(c2[0], DEFAULT_SIZE);
			if (c2.length > 1)
				h = StringUtil.convertToInt(c2[1], DEFAULT_SIZE);
			else
				h = w;
			if ((c1.length > 1) && c1[1].startsWith("@")) {
				String[] c3 = c1[1].split("x");
				l = StringUtil.convertToInt(c3[0].substring(1), DEFAULT_SIZE);
				t = StringUtil.convertToInt(c3[1], DEFAULT_SIZE);
			}
			// no location, use center for cropping
			if (l < 0) l = (oWidth / 2) - (w / 2);
			if (t < 0) t = (oHeight / 2) - (h / 2);
			return img.crop(l, t, l+w, t+h);
		} else {
			// we are resizing the image, format expected is WxH
			String[] c2 = command.split("x");
			int h = 0, w = 0;
			if (c2.length==1) {
				// only 1 dimension specified, we are keeping aspect ratio
				if (command.startsWith("x")) {
					w = h * oWidth / oHeight;
				} else {
					w = StringUtil.convertToInt(c2[0], DEFAULT_SIZE);
				}
			} else {
				// 2 dimension, check aspect ratio for possible cropping
				w = StringUtil.convertToInt(c2[0], DEFAULT_SIZE);
				h = StringUtil.convertToInt(c2[1], DEFAULT_SIZE);
				double ar1 = oWidth / (double) oHeight;
				double ar2 = w / (double) h;
				if (ar1 > ar2) {
					// crop the image to match aspect ratio
					int w2 = (int) (oHeight * ar2);
					int l = (oWidth / 2) - (w2 / 2);
					img = img.crop(l, 0, l+w2, oHeight);
				} else if (ar1 < ar2) {
					// crop the image to match aspect ratio
					int h2 = (int) (oWidth / ar2);
					int t = (oHeight / 2) - (h2 / 2);
					img = img.crop(0, t, oWidth, t+h2);
				}
			}
			return img.getResizedToWidth(w);			
		}
	}
	
	/**
	 * Helper to checks if command has valid syntax
	 * @param command
	 * @return
	 */
	public static final boolean isValidCommand(String command) {
		if (command.matches("(\\d+)|(\\d*x\\d+)") ) // for resizing image
			return true; 
		if (command.matches("((\\d+)|(\\d+x\\d+))-c(\\@\\d+x\\d+)?")) // for cropping image
			return true; 
		return false;		
	}
	
}
