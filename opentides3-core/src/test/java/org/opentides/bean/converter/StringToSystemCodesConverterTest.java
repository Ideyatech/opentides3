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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.SystemCodes;
import org.opentides.service.SystemCodesService;

/**
 * Unit test for StringToSystemCodesConverter
 * @author gino
 *
 */
public class StringToSystemCodesConverterTest {
	
	@Mock
	private SystemCodesService systemCodesService;
	
	@InjectMocks
	private StringToSystemCodesConverter converter = new StringToSystemCodesConverter();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testConvert() {
		String source = "SC_KEY";
		SystemCodes sc = new SystemCodes("SC_CATEGORY", "SC_KEY", "SC Value");
		Mockito.when(systemCodesService.findByKey(source)).thenReturn(sc);
		
		SystemCodes actual = converter.convert(source);
		
		assertEquals(sc, actual);
	}

}
