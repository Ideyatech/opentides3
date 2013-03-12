/*
 * This source code is property of Ideyatech,Inc.
 * All rights reserved. 
 * 
 * ImageUtil.java
 * Created on Apr 12, 2011 9:13:36 PM
 */
package org.opentides.util;


import imageUtil.Image;
import imageUtil.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.util.FileCopyUtils;

public class ImageUtil {

	/**
	 * Loads the image from cache if available.
	 * 
	 * @param fullPath
	 * @return
	 * @throws IOException 
	 */
	public static byte[] loadImage(String fullPath) throws IOException {
		File file = new File(fullPath);
		
		return FileCopyUtils.copyToByteArray(file);
	}
	
	/**
	 * Checks if image is valid
	 * 
	 * Image must be at least 200 x 200 pixels
	 * 
	 * @author ajalbaniel
	 */
	public static boolean isValidSize(InputStream inputStream) {
		try {
			BufferedImage image = ImageIO.read(inputStream);
			Integer width = image.getWidth();
			Integer height = image.getHeight();
			if(width >= 200 && height >= 200)
				return true;
			else
				return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * @author ajalbaniel
	 * 
	 * Generate photo thumbnails of sizes L, M, S and XS.
	 * If this does not work, please do not blame me.
	 * 
	 */
	public static void createPhotoThumbnails(String filePath) throws IOException {
		
		Image img = ImageLoader.fromFile(filePath);
		
		Image l = img.getResizedToSquare(200, 0.0);
		Image m = img.getResizedToSquare(100, 0.0);
		Image s = img.getResizedToSquare(50, 0.0);
		Image xs = img.getResizedToSquare(25, 0.0);
		
		l.writeToFile(new File(buildFileName(filePath, "_l") + ".png"));
		m.writeToFile(new File(buildFileName(filePath, "_m") + ".png"));
		s.writeToFile(new File(buildFileName(filePath, "_s") + ".png"));
		xs.writeToFile(new File(buildFileName(filePath, "_xs") + ".png"));
		
	}
	
	/**
	 * Public helper used to concatenate postFix of image
	 */
	public static String buildFileName(String fullPath, String postFix) {
		int fullPathExt = fullPath.lastIndexOf(".");
		return fullPath.substring(0, fullPathExt) + postFix;
	}

}
