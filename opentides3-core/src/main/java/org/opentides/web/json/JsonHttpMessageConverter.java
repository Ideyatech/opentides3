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
package org.opentides.web.json;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.Assert;

/**
 * @author allantan
 *
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

		public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

		private boolean prefixJson = false;

		/**
		 * Construct a new {@code JsonHttpMessageConverter}.
		 */
		public JsonHttpMessageConverter() {
			super(new MediaType("application", "json", DEFAULT_CHARSET));
		}

		/**
		 * Indicate whether the JSON output by this view should be prefixed with "{} &&". Default is false.
		 * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
		 * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
		 * This prefix does not affect the evaluation of JSON, but if JSON validation is performed on the
		 * string, the prefix would need to be ignored.
		 */
		public void setPrefixJson(boolean prefixJson) {
			this.prefixJson = prefixJson;
		}

		@Override
		protected boolean supports(Class<?> clazz) {
			// for now, let's assume we can support all classes
			return true;
		}

		@Override
		protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
				throws IOException, HttpMessageNotReadableException {
			// for now, we are only supporting writing to JSON.
			// all input should be submitted as form
			throw new HttpMessageNotReadableException("Reading from JSON is not supported");
		}

		@Override
		protected void writeInternal(Object object, HttpOutputMessage outputMessage)
				throws IOException, HttpMessageNotWritableException {
		
		}
/*
			JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
			JsonGenerator jsonGenerator =
					this.objectMapper.getJsonFactory().createJsonGenerator(outputMessage.getBody(), encoding);
			try {
				if (this.prefixJson) {
					jsonGenerator.writeRaw("{} && ");
				}
				this.objectMapper.writeValue(jsonGenerator, object);
			}
			catch (IOException ex) {
				throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
			}
		}
*/

}
