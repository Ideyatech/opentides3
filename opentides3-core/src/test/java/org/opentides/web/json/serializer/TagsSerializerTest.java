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
package org.opentides.web.json.serializer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opentides.bean.Tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Unit test for {@link TagsSerializer}
 * @author gino
 *
 */
public class TagsSerializerTest {
	
	@Test
	public void testSerialize() throws Exception {
		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag("tag1"));
		tags.add(new Tag("tag2"));
		tags.add(new Tag("tag3"));
		
		SampleClass sample = new SampleClass();
		sample.setTags(tags);
		
		ObjectMapper mapper = new ObjectMapper();
		assertEquals("{\"tags\":\"tag1,tag2,tag3\"}", mapper.writeValueAsString(sample));
	}
	
	/**
	 * Sample class for testing.
	 * @author gino
	 *
	 */
	private static class SampleClass {
		@JsonSerialize(using = TagsSerializer.class)
		private List<Tag> tags;
		
		public void setTags(List<Tag> tags) {
			this.tags = tags;
		}
	}

}
