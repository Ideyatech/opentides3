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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.opentides.bean.SortedProperties;
import org.opentides.exception.InvalidImplementationException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author allanctan
 * 
 */
public class FileUtil {

	private static Logger _log = Logger.getLogger(FileUtil.class);

	/**
	 * Helper class to read certain file and return contents as string. 
	 * 
	 * Note: readFile() returns a newline at end of every line 
	 * including the last line even when newline is not found on the 
	 * original file.
	 * 
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		try {
			InputStream is = FileUtil.getFileStream(file.getAbsolutePath());
			return readFile(is, file.getAbsolutePath());
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + file.getAbsolutePath() + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		}
	}
    
	/**
	 * Helper class to read certain file and return contents as string. 
	 * 
	 * Note: readFile() returns a newline at end of every line 
	 * including the last line even when newline is not found on the 
	 * original file.
	 * 
	 * @param filename
	 * @return
	 */
	public static String readFile(String filename) {		
		try {
			InputStream is = FileUtil.getFileStream(filename);
			return readFile(is, filename);
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		}
	}
		
	/**
	 * Helper class to read certain file from inputStream.
	 * 
	 * Note: readFile() returns a newline at end of every line 
	 * including the last line even when newline is not found on the 
	 * original file.
	 * 
	 * @param filename
	 * @return
	 */
	private static String readFile(InputStream is, String filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer ret = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				ret.append(line + System.getProperty("line.separator"));
			}
			return ret.toString();
		} catch (NullPointerException npe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, npe);
			throw new InvalidImplementationException(msg, npe);
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		} catch (IOException ioe) {
			String msg = "Cannot access file [" + filename + "].";
			_log.error(ioe, ioe);
			throw new InvalidImplementationException(msg, ioe);
		} finally {
			if (reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					// do nothing
				}
		}
	}
	
	/**
	 * Helper class to read certain file and return contents as bytes. 
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] readFileAsBytes(File file) {
		try {
			InputStream is = FileUtil.getFileStream(file.getAbsolutePath());
			return readFileAsBytes(is, file.getAbsolutePath());
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + file.getAbsolutePath() + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		}
	}

	/**
	 * Helper class to read certain file and return contents as bytes. The file
	 * must be located relative to classpath.
	 * 
	 * @param filename
	 * @return
	 */
	public static byte[] readFileAsBytes(String filename) {		
		try {
			InputStream is = FileUtil.getFileStream(filename);
			return readFileAsBytes(is, filename);
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		}
	}

	
	/**
	 * Private helper to read file as bytes
	 * @param inStream
	 * @param filename
	 * @return
	 */
	private static byte[] readFileAsBytes(InputStream inStream, String filename) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}			
			return outStream.toByteArray();
		} catch (NullPointerException npe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, npe);
			throw new InvalidImplementationException(msg, npe);
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file [" + filename + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		} catch (IOException ioe) {
			String msg = "Cannot access file [" + filename + "].";
			_log.error(ioe, ioe);
			throw new InvalidImplementationException(msg, ioe);
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}
	
	/**
	 * Helper class to read certain file and return contents as string. The file
	 * must be located relative to classpath.
	 * 
	 * @param filename
	 * @return
	 */
	public static String backupFile(File source) {
		File backup = new File(source.getParent() + "/~" + source.getName());
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(source)));
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(backup)));
			return FileUtil.backupFile(reader, writer, source.getAbsolutePath());
		} catch (FileNotFoundException fe) {
			String msg = "Failed to find file for backup [" + source.getAbsolutePath() + "].";
			_log.error(msg, fe);
			throw new InvalidImplementationException(msg, fe);
		}
	}
	
	/**
	 * Helper class to read certain file and return contents as string. The file
	 * must be located relative to classpath.
	 * 
	 * @param filename
	 * @return
	 */
	private static String backupFile(BufferedReader reader, 
									 BufferedWriter writer,
									 String filename) {
		try {
			String line = null;
			StringBuffer ret = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				writer.append(line+"\n");
			}
			return ret.toString();
		} catch (NullPointerException npe) {
			String msg = "Failed to copy file for backup [" + filename + "].";
			_log.error(msg, npe);
			throw new InvalidImplementationException(msg, npe);
		} catch (IOException ioe) {
			String msg = "Cannot access file for backup [" + filename + "].";
			_log.error(ioe, ioe);
			throw new InvalidImplementationException(msg, ioe);
		} finally {
			try {
				reader.close();
				writer.close();			
			} catch (IOException e) { 
				// ignore 
			}
		}
	}
	
	/**
	 * Helper class that tries to load an existing file from various location, 
	 * including direct file location, relative to classpath, relative to project source.
	 * The order of search is: absolute, relative to source, relative to classpath.
	 * @param name
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static InputStream getFileStream(String name) throws FileNotFoundException {
		File file = new File(name);
		if (file.isAbsolute()) {
			return new FileInputStream(file);
		} else {
			// check relative to source
			if (file.exists()) {
				return new FileInputStream(file);
			} else {
				return FileUtil.class.getClassLoader().getResourceAsStream(name);
			}
		}
	}

	/**
	 * Helper class to write specified content to certain file.
	 * 
	 * @param filename
	 * @param content
	 * @return true if writing is successful
	 */
	public static void writeFile(String filename, String content) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(content);
			writer.close();
		} catch (IOException ioe) {
			String msg = "Cannot access file [" + filename + "].";
			_log.error(ioe, ioe);
			throw new InvalidImplementationException(msg, ioe);
		}
	}

	/**
	 * Helper class to append specified content to certain file.
	 * 
	 * @param filename
	 * @param content
	 * @return true if writing is successful
	 */
	public static void appendFile(String filename, String content) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filename, true));
			writer.write(content);
			writer.close();
		} catch (IOException ioe) {
			String msg = "Cannot access file [" + filename + "].";
			_log.error(ioe, ioe);
			throw new InvalidImplementationException(msg, ioe);
		}
	}

	/**
	 * Creates a directory if not yet existing
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File createDirectory(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * Saves multipart file into disk
	 * 
	 * @param multipartFile
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean copyMultipartFile(final MultipartFile multipartFile,
			File dest) throws IOException {
		InputStream inStream = multipartFile.getInputStream();
		OutputStream outStream = new FileOutputStream(dest);

		try {
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, len);
			}
			outStream.flush();
			return true;
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if (outStream != null) {
				outStream.close();
			}

			if (inStream != null) {
				inStream.close();
			}
		}
	}
	
	/**
	 * Retrieves the properties for the specified filename
	 * @param filename
	 * @return
	 */
	public static Properties readProperty(File f) {
		Properties properties = new Properties();
        FileInputStream fis = null;
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			properties.load(fis);
		} catch (IOException e) {
			_log.error("Failed to load properties ["+f.getAbsolutePath()+"]", e);
		} finally {
		    if (fis!=null)
                try {
                    fis.close();
                } catch (IOException e) {
                }
		}
		return properties;			
	}
	
	/**
	 * Saves the properties to a specified filename in a sorted manner.
	 * 
	 * @param filename
	 * @param properties
	 * @param header
	 */
	public static void saveProperty(String filename, Properties properties, String header) {
		OutputStream out = null;
		try {
			Properties sortedProperties = new SortedProperties(properties);
			File f = new File(filename);
			_log.info("Storing file to: " + f.getAbsolutePath());
			out = new FileOutputStream(f);
			sortedProperties.store(out, header);
		} catch (Exception e) {
			_log.error(e,e);
		} finally {
			try {
				if(out != null)
					out.close();
			} catch (IOException e) { }
		}
	}
	
	/**
	 * Returns the filename for a complete filepath.
	 * @param filepath
	 * @return
	 */
	public static String getFilename(String filepath) {
		if (StringUtil.isEmpty(filepath))
			return "";
		File file = new File(filepath);
		return file.getName();
	}
	
}
