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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.opentides.bean.BeanDefinition;


/**
 * @author allantan
 *
 */
public class CloningUtil {
	
	private static Logger _log = Logger.getLogger(CloningUtil.class.getName());
	
	public static final String ENCODING = "utf-8";
	
	/**
	 * Hides the constructor. This is a singleton.
	 */
	private CloningUtil() {
		
	}
	
	/**
	 * Outputs the velocity template along with specified parameters
	 * to the given output writer.
	 * @param path
	 * @param params
	 * @param output
	 */
	public static void mergeVmTemplate(String path, Map<String, Object> params, Writer output) {		
		try {
			// initialize velocity to read from Jar
			Properties p = new Properties();
			p.put("resource.loader", "class");
			p.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			VelocityEngine ve = new VelocityEngine();
			ve.init(p);
			
		    VelocityContext context = new VelocityContext();
			// Put all params to context
			for (Entry<String,Object> entry:params.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

		    Template template = ve.getTemplate(path, ENCODING);		    
		    template.merge(context, output);
		} catch (ResourceNotFoundException e) {
			// handle this templating error
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch (ParseErrorException e) {
			// handle this templating error
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch (MethodInvocationException e) {
			// handle this templating error
			_log.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception e) {
			// report this unknown error
			throw new RuntimeException(e);
		} finally {
			// just make sure file is closed
			try { output.close(); } catch (IOException e) { };
		}
	}
	
	/**
	 * Builds the path to the output file.
	 * params.get("templateSourceBase") = templates/opentides
	 * params.get("package") = org.opentides
	 * 
	 * @param template - classpath reference to the template (e.g. templates/opentides/dao/classNameDao.java.vm)
	 * @param params
	 * @return the output file name = com/ideyatech/dao/SystemCodesDao.java
	 */
	public static String getOutputName(String template, Map<String, Object> params) {
		// find the appropriate path of output file
		String base = PackageUtil.toFolderName(""+params.get("templateSourceBase"));
		String output = template;
		int idx = template.indexOf(base);
		if (idx >= 0) {
			idx += base.length();
			output = template.substring(idx);
		}
		BeanDefinition bean = (BeanDefinition) params.get("bean");
		// now build the filename
		String outputName = PackageUtil.toFolderName(""+bean.getPackage_()) +
							output.replace("className", bean.getClassName())
							.replace("modelName", bean.getModelName())
							.replaceAll(".java.vm$", "").replaceAll(".vm$", "");
		return outputName;
	}
}
