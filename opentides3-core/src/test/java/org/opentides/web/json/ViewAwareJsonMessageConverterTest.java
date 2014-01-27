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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.opentides.web.json.Views.DisplayView;
import org.opentides.web.json.Views.FormView;
import org.opentides.web.json.Views.FullView;
import org.opentides.web.json.Views.SearchView;
import org.springframework.mock.http.MockHttpOutputMessage;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Unit test for {@link ViewAwareJsonMessageConverter}
 * @author gino
 *
 */
public class ViewAwareJsonMessageConverterTest {

	private ViewAwareJsonMessageConverter converter;

	@Before
	public void init() {
		converter = new ViewAwareJsonMessageConverter();
	}

	@Test
	public void testWriteInternal() throws Exception {
		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		PojoView pojoView = new PojoView(new SampleClass("John", "Doe",
				"25", "john.doe@opentides.com"), SearchView.class);

		converter.writeInternal(pojoView, outputMessage);
		assertEquals("{\"name\":\"John\"}", outputMessage.getBodyAsString());
		
		pojoView = new PojoView(new SampleClass("John", "Doe",
				"25", "john.doe@opentides.com"), FormView.class);
		outputMessage = new MockHttpOutputMessage();
		converter.writeInternal(pojoView, outputMessage);
		assertEquals("{\"name\":\"John\",\"lastName\":\"Doe\"}", outputMessage.getBodyAsString());
		
		pojoView = new PojoView(new SampleClass("John", "Doe",
				"25", "john.doe@opentides.com"), DisplayView.class);
		outputMessage = new MockHttpOutputMessage();
		converter.writeInternal(pojoView, outputMessage);
		assertEquals("{\"name\":\"John\",\"lastName\":\"Doe\",\"age\":\"25\"}", outputMessage.getBodyAsString());
		
		pojoView = new PojoView(new SampleClass("John", "Doe",
				"25", "john.doe@opentides.com"), FullView.class);
		outputMessage = new MockHttpOutputMessage();
		converter.writeInternal(pojoView, outputMessage);
		assertEquals("{\"name\":\"John\",\"lastName\":\"Doe\",\"age\":\"25\",\"email\":\"john.doe@opentides.com\"}", outputMessage.getBodyAsString());

	}

	/**
	 * Helper class for testing ViewAwareJsonMessageConverter
	 * @author gino
	 *
	 */
	private static class SampleClass {

		public SampleClass(String name, String lastName, String age,
				String email) {
			super();
			this.name = name;
			this.lastName = lastName;
			this.age = age;
			this.email = email;
		}

		@JsonView(Views.SearchView.class)
		private String name;

		@JsonView(Views.FormView.class)
		private String lastName;

		@JsonView(Views.DisplayView.class)
		private String age;

		@JsonView(Views.FullView.class)
		private String email;

	}

}
