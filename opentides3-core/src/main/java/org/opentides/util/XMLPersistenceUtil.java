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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * This class process and retrieves configuration settings 
 * from persistence.xml.
 * 
 * @author allantan
 */
public class XMLPersistenceUtil {
	private static final Logger _log = Logger.getLogger(XMLPersistenceUtil.class);	

	/**
	 * Hide the constructor.
	 */
	private XMLPersistenceUtil() {	
	}
	
	public static List<String> getClasses(String persistenceFile, String persistenceUnit) {
		List<String> classes = new ArrayList<String>();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
	        Document doc;
			is = XMLPersistenceUtil.class.getClassLoader().getResourceAsStream(persistenceFile);
			doc = reader.read(is);
	        Element root = doc.getRootElement();
	        List<Element> elements = root.elements();
	        for (Element el:elements) {
	        	if (persistenceUnit.equals(el.attributeValue("name"))) {
	        		// this is the persistence unit specified, let's get all properties inside
	        		List<Element> xmlProps = el.elements("class");
	        		for (Element prop:xmlProps) {
	        			classes.add(prop.getText());
	        		}
	        	}
	        }
		} catch (DocumentException e) {
			_log.error("Failed to read file contents for "+persistenceFile,e);
			return null;
		} finally {
			if (is!=null)
				try {
					is.close();
				} catch(Exception e) {
					// do nothing
				};
		}
        return classes;		
	}
	
	@SuppressWarnings("unchecked")
	public static Properties getProperties (String persistenceFile, String persistenceUnit) {
		Properties properties = new Properties();
		SAXReader reader = new SAXReader();
		InputStream is = null;
		try {
	        Document doc;
			is = XMLPersistenceUtil.class.getClassLoader().getResourceAsStream(persistenceFile);
			doc = reader.read(is);
	        Element root = doc.getRootElement();
	        List<Element> elements = root.elements();
	        for (Element el:elements) {
	        	if (persistenceUnit.equals(el.attributeValue("name"))) {
	        		// this is the persistence unit specified, let's get all properties inside
	        		Element propsElement = el.element("properties");
	        		List<Element> xmlProps = propsElement.elements("property");
	        		for (Element prop:xmlProps) {
	        			properties.put(prop.attributeValue("name"), prop.attributeValue("value"));
	        		}
	        	}
	        }
		} catch (DocumentException e) {
			_log.error("Failed to read file contents for "+persistenceFile,e);
			return null;
		} finally {
			if (is!=null)
				try {
					is.close();
				} catch(Exception e) {
					// do nothing
				};
		}
        return properties;
	}
}
