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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author allantan
 *
 */
public class FileUtilTest {

	/**
	 * Test method for {@link org.opentides.util.FileUtil#readFile(java.io.File)}.
	 */
	@Test
	public void testReadFile() {
		// read file as string
		String content = FileUtil.readFile("resources/testing.txt");
		Assert.assertEquals("ideyatech\nsample file\n", content);
		
		try {
			FileUtil.readFile("test/missing.txt");
			fail("No Exception thrown with missing file.");
		} catch (Exception e) {
			// do nothing
		}

		// read file as byte
		content = FileUtil.readFile(new File("test/resources/testing.txt"));
		Assert.assertEquals("ideyatech\nsample file\n", content);
		
		try {
			FileUtil.readFile(new File("test/missing.txt"));
			fail("No Exception thrown with missing file.");
		} catch (Exception e) {
			// do nothing
		}

	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#readFileAsBytes(java.io.File)}.
	 */
	@Test
	public void testReadFileAsBytes() {
		byte[] bytes = FileUtil.readFileAsBytes("resources/testing.txt");
		byte[] expected = {105, 100, 101, 121, 97, 116, 101, 99, 104, 10, 115, 
							97, 109, 112, 108, 101, 32, 102, 105, 108, 101};
		Assert.assertEquals(21,bytes.length);
		for (int i=0;i<expected.length; i++) 
			Assert.assertEquals(expected[i], bytes[i]);

		try {
			FileUtil.readFileAsBytes("test/missing.txt");
			fail("No Exception thrown with missing file.");
		} catch (Exception e) {
			// do nothing
		}

		bytes = FileUtil.readFileAsBytes(new File("test/resources/testing.txt"));
		Assert.assertEquals(21,bytes.length);
		for (int i=0;i<expected.length; i++) 
			Assert.assertEquals(expected[i], bytes[i]);

		try {
			FileUtil.readFileAsBytes(new File("test/missing.txt"));
			fail("No Exception thrown with missing file.");
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#backupFile(java.lang.String)}.
	 */
	@Test
	public void testBackupFile() {
		String content = FileUtil.readFile("test/resources/testing.txt");
		FileUtil.backupFile("test/resources/testing.txt");
		String content2 = FileUtil.readFile("test/resources/~testing.txt");
		Assert.assertEquals(content, content2);

		try {
			FileUtil.backupFile("test/missing.txt");
			fail("No Exception thrown with missing file.");
		} catch (Exception e) {
			// do nothing
		}

	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#writeFile(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testWriteFile() throws IOException {
		File tmpFile = File.createTempFile("ot-unit-test-write", ".txt");
		String content ="write\noutput file\nTest's out\n";
		FileUtil.writeFile(tmpFile.getAbsolutePath(), content);
		String written = FileUtil.readFile(tmpFile.getAbsolutePath());
		Assert.assertEquals(content, written);
	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#appendFile(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testAppendFile() throws IOException {
		File tmpFile = File.createTempFile("ot-unit-test-append", ".txt");
		String content ="write\noutput file\nTest's out\n";
		FileUtil.writeFile(tmpFile.getAbsolutePath(), content);
		FileUtil.appendFile(tmpFile.getAbsolutePath(), content);
		String written = FileUtil.readFile(tmpFile.getAbsolutePath());
		Assert.assertEquals(content+content, written);
	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#copyMultipartFile(org.springframework.web.multipart.MultipartFile, java.io.File)}.
	 * @throws IOException 
	 */
/*	
	@Test
	
	public void testCopyMultipartFile() throws IOException {
		String content = FileUtil.readFile("test/resources/testing.txt");
		InputStream is = FileUtil.getFileStream("test/resources/testing.txt");
		MockMultipartFile mfile = new MockMultipartFile("testing.txt", is);
		File dest = new File("copy.txt");
		FileUtil.copyMultipartFile(mfile, dest);
		String copied = FileUtil.readFile(dest);
		Assert.assertEquals(content, copied);		
	}
*/
	/**
	 * Test method for {@link org.opentides.util.FileUtil#readProperty(java.lang.String)}.
	 */
	@Test
	public void testReadProperty() {
		Properties props = FileUtil.readProperty("test/resources/test.properties");
		Assert.assertEquals(2, props.size());
		Assert.assertEquals("test", props.get("run.environment"));
		Assert.assertEquals("true", props.get("application.mode.debug"));
	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#saveProperty(java.lang.String, java.util.Properties, java.lang.String)}.
	 */
	@Test
	public void testSaveProperty() {
		Properties props = new Properties();
		props.put("two.two", "two");
		props.put("one.one", "one");
		props.put("_sample", "3");
		FileUtil.saveProperty("test/resources/saved.properties", props, "HEADER");
		
		String actual = FileUtil.readFile("test/resources/saved.properties");
		String expected = "#HEADER\n.*?\n_sample=3\none.one=one\ntwo.two=two\n";
		Assert.assertTrue(actual.matches(expected));
	}

	/**
	 * Test method for {@link org.opentides.util.FileUtil#getFilename(java.lang.String)}.
	 */
	@Test
	public void testGetFilename() {
		Assert.assertEquals("filename.txt", FileUtil.getFilename("/tmp/filename.txt"));
		Assert.assertEquals("filename.txt", FileUtil.getFilename("/filename.txt"));
		Assert.assertEquals("filename.txt", FileUtil.getFilename("/tmp/sample/filename.txt"));
	}
}
