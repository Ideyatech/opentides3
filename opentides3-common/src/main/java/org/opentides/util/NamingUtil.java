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

package org.opentides.util;

/**
 * Utility functions for manipulating method and attribute names.
 * 
 * TODO: Consider using cache to reduce string manipulation.
 * @author allantan
 */
public class NamingUtil {

	/**
	 * Hide the constructor.
	 */
	private NamingUtil() {		
	}
	
	/**
	 * Ensures that name starts with big letter.
	 * For example:
	 * input - fieldName; output - Field Name
	 * @param name
	 * @return
	 */
	public static String toLabel(String name) {
		if (StringUtil.isEmpty(name))
			return "";

    	String label = name.startsWith("get")?name.replaceFirst("get", ""):name;

		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<label.length();i++) {
			if (i==0) {
				// capitalize first character
				buffer.append(label.substring(0, 1).toUpperCase());
			} else {
				// append spaces between words
				if (label.charAt(i) >= 'A' && label.charAt(i)<= 'Z') 
					buffer.append(" ");
				buffer.append(label.charAt(i));
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Create a getter name. (e.g. getSystemCodes)
	 * @param name
	 * @return
	 */
	public static String toGetterName(String name) {
    	if (StringUtil.isEmpty(name))
    		return "";
		return "get" + name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	/**
	 * Create a getter name. (e.g. setSystemCodes)
	 * @param name
	 * @return
	 */
	public static String toSetterName(String name) {
    	if (StringUtil.isEmpty(name))
    		return "";
		return "set" + name.substring(0,1).toUpperCase() + name.substring(1);
	}

	/**
	 * Ensures that name starts with small letter.
	 * @param name
	 * @return
	 */
	public static String toAttributeName(String name) {

		if (StringUtil.isEmpty(name))
			return "";
		
		if (name.length()>=2) {
			return name.substring(0,1).toLowerCase() + name.substring(1);
		} else
			return name.toLowerCase();
		
		// how about names with several words?
	}
	
	/**
	 * Ensures that name are in html element format (e.g. system-codes)
	 * @param name
	 * @return
	 */
	public static String toElementName(String name) {
		if (StringUtil.isEmpty(name))
			return "";
		StringBuilder buffer = new StringBuilder();
		int startIndex = 0;
		for (int i=0; i<name.length();i++) {
			if (name.charAt(i) >= 'A' && name.charAt(i)<= 'Z') {
				if (startIndex!=0)
					buffer.append("-");
				buffer.append(name.substring(startIndex, i).toLowerCase());
				startIndex = i;				
			}
		}
		if (startIndex<name.length()) {
			if (startIndex!=0)
				buffer.append("-");
			buffer.append(name.substring(startIndex).toLowerCase());
		}
		return buffer.toString();
	}
	
	
	/**
	 * Ensures that name are in html element format (e.g. system-codes)
	 * @param name
	 * @return
	 */
	public static String toSQLName(String name) {
		if (StringUtil.isEmpty(name))
			return "";
		StringBuilder buffer = new StringBuilder();
		int startIndex = 0;
		for (int i=0; i<name.length();i++) {
			if (name.charAt(i) >= 'A' && name.charAt(i)<= 'Z') {
				if (startIndex!=0)
					buffer.append("_");
				buffer.append(name.substring(startIndex, i).toLowerCase());
				startIndex = i;				
			}
		}
		if (startIndex<name.length()) {
			if (startIndex!=0)
				buffer.append("_");
			buffer.append(name.substring(startIndex).toLowerCase());
		}
		return buffer.toString();
	}
	
    /**
     * Retrieves the property name for a method name. 
     * (e.g. getName will return name)
     * @param methodName
     * @return
     */
    public static String getPropertyName(String methodName) {
    	if (StringUtil.isEmpty(methodName) || methodName.length()<=3)
    		return null;
    	if (methodName.startsWith("get") || methodName.startsWith("set")) {
    		String prop = methodName.substring(4);
    		char c = Character.toLowerCase(methodName.charAt(3));
    		return c+prop;
    	} else 
    		return null;
    }

    /**
     * Returns the simple name of the given name.
     * For example: org.opentides.bean.SystemCodes will return SystemCodes
     * @param qualifiedName
     * @return
     */
    public static String getSimpleName(String qualifiedName) {
		int idx = qualifiedName.lastIndexOf(".");
		if (idx > 0)
			return qualifiedName.substring(idx+1);	
		else
			return qualifiedName;
    }

    /**
     * Returns the simple name of the given name.
     * For example: org.opentides.bean.SystemCodes will return SystemCodes
     * @param qualifiedName
     * @return
     */
    public static String getPackageName(String qualifiedName) {
		int idx = qualifiedName.lastIndexOf(".");
		if (idx > 0)
			return qualifiedName.substring(0,idx);	
		else
			return ""; // no package
    }

}
