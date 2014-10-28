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

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import org.opentides.processor.param.ParamContext;
import org.opentides.processor.param.ParamReader;
import org.opentides.processor.param.ParamReaderFactory;

/**
 * Code generation processor for creating source codes in field level.
 * 
 * @author allantan
 */

@SupportedAnnotationTypes(value = "org.opentides.annotation.field.*")
public class AttributeProcessor extends AbstractProcessor {

	private static final Logger _log = Logger.getLogger(AttributeProcessor.class
			.getName());

	/* (non-Javadoc)
	 * @see javax.annotation.processing.AbstractProcessor#init(javax.annotation.processing.ProcessingEnvironment)
	 */
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		System.out.println("init:"+processingEnv.toString());
		super.init(processingEnv);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment env) {
		for (TypeElement te : annotations) {
			for (Element e : env.getElementsAnnotatedWith(te)) {
				if (e.getKind() == ElementKind.FIELD) {
					String annotation = te.getSimpleName().toString();					
					String attributeName = e.toString();
					displayMessage("Processing: " + attributeName
							+ " for annotation " + annotation);
					ParamReader reader = ParamReaderFactory.getReader(annotation);					
					if (reader!=null) {
						ParamContext.addDefinition(reader.getDefinition(te, e));
					}
				}
			}
		}
		return true;
	}

	/**
	 * Private helper to display message to console.
	 * 
	 * @param message
	 */
	private void displayMessage(String message) {
		Messager messager = processingEnv.getMessager();
		_log.log(Level.INFO, message);
		messager.printMessage(Diagnostic.Kind.NOTE, message);
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

}
