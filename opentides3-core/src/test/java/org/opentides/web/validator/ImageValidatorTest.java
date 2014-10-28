package org.opentides.web.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.opentides.bean.AjaxUpload;
import org.opentides.bean.ImageInfo;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

public class ImageValidatorTest {
	
	private ImageValidator imageValidator;
	
	@Before
	public void init() {
		imageValidator = new ImageValidator();
	}

	@Test
	public void testSupports() {
		assertTrue(imageValidator.supports(AjaxUpload.class));
		assertFalse(imageValidator.supports(ImageInfo.class));
	}
	
	@Test
	public void testValidateNoFile() {
		AjaxUpload obj = new AjaxUpload();
		
		BindException errors = new BindException(obj, "ajaxUpload");
		imageValidator.validate(obj, errors);
		
		assertTrue(errors.hasErrors());
		ObjectError error = errors.getFieldError("attachment");
		assertNotNull(error);
	}

	@Test
	public void testValidateInvalidFile() {
		MockMultipartFile file = new MockMultipartFile("image1", "image1", "application/doc", "image".getBytes());
		AjaxUpload obj = new AjaxUpload();
		obj.setAttachment(file);
		
		BindException errors = new BindException(obj, "ajaxUpload");
		imageValidator.validate(obj, errors);
		
		assertTrue(errors.hasErrors());
		ObjectError error = errors.getFieldError("attachment");
		assertNotNull(error);
	}
	
	@Test
	public void testValidateInvalidFileSize() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 1024 * 1024 * 10; i++) {
			sb.append("-");
		}
		sb.append("-----");
		MockMultipartFile file = new MockMultipartFile("image1", "image1", "image/gif", sb.toString().getBytes());
		AjaxUpload obj = new AjaxUpload();
		obj.setAttachment(file);
		
		BindException errors = new BindException(obj, "ajaxUpload");
		imageValidator.validate(obj, errors);
		
		assertTrue(errors.hasErrors());
		ObjectError error = errors.getFieldError("attachment");
		assertNotNull(error);
	}

}
