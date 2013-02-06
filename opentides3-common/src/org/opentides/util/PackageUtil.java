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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Helper class of opentides in generating package and folder paths.
 * 
 * @author allantan
 * 
 */
public class PackageUtil {

	private static Set<String> templateJars = new HashSet<String>();
	
	/**
	 * Converts a file path into the package name.
	 * Conversion is sensitive to path separator of the OS.
	 * For example: templates/org/ideyatech/model -> template.org.ideyatech.model
	 * @param name
	 * @return
	 */
	public static String toPackageName(String name) {
		return name.replaceAll(File.separator, ".");
	}
	
	/**
	 * Converts a package name into folder structure.
	 * Conversion is sensitive to path separator of the OS.
	 * For example: org.opentides.bean -> org/opentides/bean
	 * @param name
	 * @return
	 */
	public static String toFolderName(String name) {
		if (name.endsWith(".java")) {
			return name.substring(0, name.length()-5).replaceAll("\\.",File.separator) + ".java";
		} else {
			return name.replaceAll("\\.",File.separator);
		}
	}
	
	/**
	 * Reads property file from classpath
	 * 
	 * @param propFileName
	 * @return
	 * @throws IOException
	 */
	public static Properties getDefinitionProperties() throws IOException {
		// loading xmlProfileGen.properties from the classpath
		InputStream inputStream = null;
		Properties combined = new Properties();
		Enumeration<URL> urls = PackageUtil.class.getClassLoader()
				.getResources("template-definition.properties");
		while (urls.hasMoreElements()) {
			Properties props = new Properties();
			URL url = urls.nextElement();
			String propFileName = url.getFile();
			try {
				// add the jar filename containing the property file
				String name = url.getFile();
				if (name.startsWith("file:")) {
					int excIndex = name.indexOf("!");
					if (excIndex > 0)
						templateJars.add(name.substring(5,excIndex));
					else
						templateJars.add(name.substring(5));
				}
				// load the property file
				inputStream = url.openStream();
				if (inputStream == null) {
					throw new FileNotFoundException("Property file '"
							+ propFileName + "' not found in the classpath.");
				}
				props.load(inputStream);
			} finally {
				if (inputStream != null)
					inputStream.close();
			}
			combined.putAll(props);
		}
		return combined;
	}

	/**
	 * @return the templateJars
	 */
	public static final Set<String> getTemplateJars() {
		return templateJars;
	}

}
