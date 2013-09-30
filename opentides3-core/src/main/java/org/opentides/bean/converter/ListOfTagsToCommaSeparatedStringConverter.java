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
package org.opentides.bean.converter;

import java.util.List;

import org.opentides.bean.Tag;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a list of {@link Tag} objects to comma separated String
 * @author gino
 *
 */
public class ListOfTagsToCommaSeparatedStringConverter implements
		Converter<List<Tag>, String> {

	@Override
	public String convert(List<Tag> source) {
		if(source != null && !source.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < source.size(); i++) {
				Tag tag = source.get(i);
				sb.append(tag.getTagText());
				if(i != source.size() - 1)
					sb.append(",");
			}
			return sb.toString();
		}
		return null;
	}

}
