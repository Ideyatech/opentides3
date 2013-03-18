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
package org.opentides.web.validator;

import java.io.IOException;

import org.opentides.bean.Photoable;
import org.opentides.util.ImageUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 * Ensures validity of photos uploaded
 * 
 * @author ajalbaniel
 * 
 */
@Component
public class PhotoValidator implements Validator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return Photoable.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {

		Photoable photoable = (Photoable) obj;
		MultipartFile photo = photoable.getPhoto();

		if (photo != null && !photo.isEmpty()) {

			String contentType = photo.getContentType().substring(0, 6);

			if (!"image/".equals(contentType)) {

				errors.rejectValue("photo",
						"photo.invalid-file-type",
						"Invalid file. Profile Image must be in PNG, JPEG, GIF or BMP format.");

			} else {

				if (photo.getSize() < 1024 * 1024 * 10) {

					try {
						if (!ImageUtil.isValidSize(photo.getInputStream())) {
							errors.rejectValue("photo", "photo.invalid-image-size",
									"Image size must be at least 200 x 200 pixels");
						}
					} catch (IOException e) {

					}

				} else {

					errors.rejectValue("photo", "photo.invalid-file-size",
							"Invalid file. Maximum file size is 10 Megabytes");

				}

			}
		}

	}

}
