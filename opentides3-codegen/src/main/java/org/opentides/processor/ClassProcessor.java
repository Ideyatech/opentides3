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

package org.opentides.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.opentides.bean.BeanDefinition;
import org.opentides.bean.FieldDefinition;
import org.opentides.processor.param.ParamContext;
import org.opentides.processor.param.ParamReader;
import org.opentides.processor.param.ParamReaderFactory;
import org.opentides.util.CloningUtil;
import org.opentides.util.PackageUtil;

/**
 * Code generation processor for creating source codes in class level.
 * 
 * @author allantan
 */
@SupportedAnnotationTypes(value = "org.opentides.annotation.*")
public class ClassProcessor extends AbstractProcessor {

	private static Logger _log = Logger.getLogger(ClassProcessor.class
			.getName());

	public static final String ENCODING = "utf-8";

	private static Map<String, String[]> templateMap = new HashMap<String, String[]>();

	private static Properties props = null;
	
	/**
	 * Static initializer to read the template definition files. Template
	 * definition files are named "template-definition.properties" and should be
	 * in the root folder of the template jar file.
	 */
	static {
		try {
			props = PackageUtil.getDefinitionProperties();
			Enumeration<?> e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				if (key.startsWith("TEMPLATE-SOURCE.")) {
					String annotation = key.substring(16);
					String[] templates = props.getProperty(key).split(",");
					templateMap.put(annotation, templates);
				}
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, "Failed to read template definition file.",e);
		}
	}

	/**
	 * Recursively clones all vm templates in the given path 
	 * using the specified parameters.
     *     params.get("template-source-base"): templates/opentides
     *   
	 * @param templateFolder - templates.opentides.dao
	 * @param params
	 * @throws IOException 
	 */
	public void cloneTemplates(String templateFolder, Map<String, Object> params) {
		Set<String> templateJars = PackageUtil.getTemplateJars();
		List<String> templates = new ArrayList<String>();
		if (templateJars.isEmpty()) {
			// process folders
			templates = CloningUtil.getFolderTemplates();
		} else {
			// process jars
			templates = CloningUtil.getJarTemplates();
		}
		
		for (String templateName : templates) {
			BufferedWriter bw = null;
			if (templateName.startsWith(templateFolder) && 
					templateName.endsWith(".vm") &&
					!templateName.endsWith("_.vm") ) {				
				try {
					String outputFile = PackageUtil.toPackageName(CloningUtil.getOutputName(templateName, params));
					File file = new File(outputFile);
					if (file.exists()) {
						displayMessage("    Skipping " + outputFile + "... File already exist.");
						continue;								
					}
					displayMessage("    Generating " + outputFile + "...");
					JavaFileObject gen = processingEnv.getFiler().createSourceFile(outputFile);				
					bw = new BufferedWriter(gen.openWriter());			
					CloningUtil.mergeVmTemplate(templateName, params, bw);
					displayMessage("    Success.");
				} catch (IOException ex) {
					_log.log(Level.SEVERE,"Failed to generate source for ["+templateName+"].",ex);
				} finally {
					try {
						bw.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	/**
	 * Executes the code generation at the class level by reading the annotations
	 * and building the code from velocity templates.
	 * List of velocity parameters are:
	 *   bean - BeanDefinition
	 *   templateSourceBase - templates/opentides
	 *   dateToday - Feb 22, 1977 08:45:39
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment env) {
		
		for (TypeElement te : annotations) {
			for (Element e : env.getElementsAnnotatedWith(te)) {
				if (e.getKind() == ElementKind.CLASS) {
					String annotation = te.getSimpleName().toString();
					displayMessage("Processing: "
							+ e.toString() + " for annotation " + annotation);
					Map<String, Object> params = new HashMap<String, Object>();
			        SimpleDateFormat dtFormatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
					params.put("templateSourceBase", props.get("TEMPLATE-SOURCE-BASE"));
					params.put("dateToday", dtFormatter.format(new Date()));		
					ParamReader reader = ParamReaderFactory.getReader(annotation);
					if (reader!=null) {
						BeanDefinition beanDefn = (BeanDefinition) reader.getDefinition(te, e);
						Set<FieldDefinition> fields = ParamContext.getFieldDefinitions(beanDefn.getQualifiedName());
						beanDefn.setFields(fields);
						params.put("bean",beanDefn);
					}
					String[] templates = templateMap.get(annotation);
					for (String template:templates) {
//						for (String name: params.keySet())
//							displayMessage(name);
						cloneTemplates(PackageUtil.toFolderName(template),params);
					}
				}				
			}
		}
		return true;
	}

	/**
	 * Private helper to display message to console.
	 * @param message
	 */
	private void displayMessage(String message) {
		Messager messager = processingEnv.getMessager();
		_log.log(Level.INFO,  message);
		messager.printMessage(Diagnostic.Kind.NOTE, message);		
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

}
