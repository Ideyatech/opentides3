package org.opentides.util.imageutil;

import java.util.HashMap;
import java.util.Map;

public enum ImageType {
	JPG, GIF, PNG, UNKNOWN;

	private static final Map<String, ImageType> extensionMap;

	public static ImageType getType(String ext) {
		ext = ext.toLowerCase();
		if (extensionMap.containsKey(ext)) {
			return ((ImageType) extensionMap.get(ext));
		}
		return UNKNOWN;
	}

	static {
		extensionMap = new HashMap();

		extensionMap.put("jpg", JPG);
		extensionMap.put("jpeg", JPG);
		extensionMap.put("gif", GIF);
		extensionMap.put("png", PNG);
	}
}