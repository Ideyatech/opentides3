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

package org.opentides.util.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageLoader {
	public static Image fromUrl(URL url) throws IOException {
		return new Image(url.openStream(), extensionToImageType(url.getPath()));
	}

	public static Image fromUrl(String url) throws IOException {
		return fromUrl(new URL(url));
	}

	public static Image fromFile(File file) throws IOException {
		return new Image(new FileInputStream(file),
				extensionToImageType(file.getPath()));
	}

	public static Image fromFile(String file) throws IOException {
		return fromFile(new File(file));
	}

	public static Image fromBytes(byte[] data) throws IOException {
		return fromBytes(data, ImageType.UNKNOWN);
	}

	public static Image fromBytes(byte[] data, ImageType sourceType)
			throws IOException {
		return new Image(new ByteArrayInputStream(data), sourceType);
	}

	public static Image fromStream(InputStream in) throws IOException {
		return fromStream(in, ImageType.UNKNOWN);
	}

	public static Image fromStream(InputStream in, ImageType sourceType)
			throws IOException {
		return new Image(in, sourceType);
	}

	private static ImageType extensionToImageType(String path) {
		int idx = (path == null) ? -1 : path.lastIndexOf(".");
		if (idx != -1) {
			return ImageType.getType(path.substring(idx + 1));
		}
		return ImageType.UNKNOWN;
	}
}