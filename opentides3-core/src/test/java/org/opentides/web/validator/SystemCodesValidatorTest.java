package org.opentides.web.validator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.SystemCodes;
import org.opentides.service.SystemCodesService;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

/**
 * JUnit test for SystemCodesValidator. Uses Mockito @Mock and @InjectMocks
 * annotations to inject spring dependencies.
 * 
 * @author gino
 *
 */
public class SystemCodesValidatorTest {
	
	@InjectMocks
	private SystemCodesValidator systemCodesValidator = new SystemCodesValidator();
	
	@Mock
	private SystemCodesService systemCodesService;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidateEverythingOk() {
		SystemCodes obj = new SystemCodes();
		obj.setKey("KEY");
		obj.setCategory("CATEGORY");
		obj.setValue("Value");
		
		BindException errors = new BindException(obj, "systemCodes");
		
		Mockito.when(systemCodesService.isDuplicateKey(obj)).thenReturn(false);
		
		systemCodesValidator.validate(obj, errors);
		
		Mockito.verify(systemCodesService).isDuplicateKey(obj);
		assertFalse(errors.hasErrors());

	}
	
	@Test
	public void testValidateEmptyRequiredFields() {
		SystemCodes obj = new SystemCodes();
		obj.setKey("");
		obj.setCategory("");
		obj.setValue("");
		
		BindException errors = new BindException(obj, "systemCodes");
		
		Mockito.when(systemCodesService.isDuplicateKey(obj)).thenReturn(false);
		
		systemCodesValidator.validate(obj, errors);
		
		Mockito.verify(systemCodesService).isDuplicateKey(obj);
		assertTrue(errors.hasErrors());
		assertEquals(3, errors.getFieldErrors().size());
		
		FieldError keyError = errors.getFieldError("key");
		assertNotNull(keyError);
		
		FieldError categoryError = errors.getFieldError("category");
		assertNotNull(categoryError);
		
		FieldError valueError = errors.getFieldError("value");
		assertNotNull(valueError);

	}
	
	@Test
	public void testValidateDuplicateKey() {
		SystemCodes obj = new SystemCodes();
		obj.setKey("KEY");
		obj.setCategory("CATEGORY");
		obj.setValue("Value");
		
		BindException errors = new BindException(obj, "systemCodes");
		
		Mockito.when(systemCodesService.isDuplicateKey(obj)).thenReturn(true);
		
		systemCodesValidator.validate(obj, errors);
		
		Mockito.verify(systemCodesService).isDuplicateKey(obj);
		
		assertTrue(errors.hasErrors());
		assertEquals(1, errors.getGlobalErrorCount());

	}

}
