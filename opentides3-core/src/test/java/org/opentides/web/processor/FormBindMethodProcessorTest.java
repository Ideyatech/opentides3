package org.opentides.web.processor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.web.controller.NinjaCrudController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ideyatech.bean.Ninja;
import com.ideyatech.service.NinjaService;

public class FormBindMethodProcessorTest {
	
	@Mock
	private BeanFactory beanFactory;
	
	@Mock
	private NinjaService ninjaService;
	
	@Mock
	private Validator validator;
	
	@InjectMocks
	private FormBindMethodProcessor resolver = new FormBindMethodProcessor();
	
	@InjectMocks
	private NinjaCrudController controller = new NinjaCrudController();
	
	private MethodParameter param1;
	private MethodParameter param2;
	
	private ModelAndViewContainer mvc;
	private NativeWebRequest nwr;
	private MockHttpServletRequest request;
	private WebDataBinderFactory dataBinderFactory;
	private ConfigurableWebBindingInitializer initializer;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	
		Method method = NinjaCrudController.class.getMethod("sampleMethod", Ninja.class);
		param1 = new MethodParameter(method, 0);
		
		Method method2 = NinjaCrudController.class.getMethod("sampleMethodWithoutFormBindAnnot", Ninja.class);
		param2 = new MethodParameter(method2, 0);
		
		mvc = new ModelAndViewContainer();

		request = new MockHttpServletRequest();
		nwr = new ServletWebRequest(request);
		
		initializer = new ConfigurableWebBindingInitializer();
		dataBinderFactory = new DefaultDataBinderFactory(initializer);
		
	}

	@Test
	public void testSupportsParameter() {
		assertTrue(resolver.supportsParameter(param1));
		assertFalse(resolver.supportsParameter(param2));
	}

	@Test
	public void testResolveArgumentSuccessfulMergeOfRequestAndDBData() throws Exception {
		request.setMethod("put");
		request.setRequestURI("/opentides/ninja/1");
		request.addParameter("firstName", "Master New");
		request.addParameter("lastName", "Splinter New");
		
		when(beanFactory.getBean(NinjaCrudController.class)).thenReturn(controller);
		
		Ninja ninja = new Ninja();
		ninja.setId(1l);
		ninja.setFirstName("Master");
		ninja.setLastName("Splinter");
		
		//Mock loading of data from database
		when(ninjaService.load("1")).thenReturn(ninja);
		
		Ninja actual = (Ninja)resolver.resolveArgument(param1, mvc, nwr, dataBinderFactory);
		assertEquals("Master New", actual.getFirstName());
		assertEquals("Splinter New", actual.getLastName());
		
		//Verify that method load of ninjaService was invoked.
		verify(ninjaService).load("1");
	}
	
	@Test(expected = BindException.class)
	public void testResolveArgumentWithValidationError() throws Exception {
		initializer.setValidator(new AlwaysFailValidator());
		
		request.setMethod("put");
		request.setRequestURI("/opentides/ninja/1");
		request.addParameter("firstName", "Master New");
		request.addParameter("lastName", "Splinter New");
		
		when(beanFactory.getBean(NinjaCrudController.class)).thenReturn(controller);
		
		Ninja ninja = new Ninja();
		ninja.setId(1l);
		ninja.setFirstName("Master");
		ninja.setLastName("Splinter");
		
		//Mock loading of data from database
		when(ninjaService.load("1")).thenReturn(ninja);
		
		resolver.resolveArgument(param1, mvc, nwr, dataBinderFactory);
		
	}
	
	@Test
	public void testResolveArgumentPostMethod() throws Exception {
		request.setMethod("post");
		request.setRequestURI("/opentides/ninja/0");
		request.addParameter("firstName", "Master New");
		request.addParameter("lastName", "Splinter New");
		
		when(beanFactory.getBean(NinjaCrudController.class)).thenReturn(controller);
		
		//For post just get all the values from the request
		Ninja actual = (Ninja)resolver.resolveArgument(param1, mvc, nwr, dataBinderFactory);
		assertEquals("Master New", actual.getFirstName());
		assertEquals("Splinter New", actual.getLastName());
		
		//Verify that method load of ninjaService was invoked.
		verify(ninjaService, never()).load("0");
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testResolveArgumentRequestMethodNotPostAndNotPut() throws Exception {
		request.setMethod("delete");
		request.setRequestURI("/opentides/ninja/0");
		request.addParameter("firstName", "Master New");
		request.addParameter("lastName", "Splinter New");
		
		when(beanFactory.getBean(NinjaCrudController.class)).thenReturn(controller);
		
		resolver.resolveArgument(param1, mvc, nwr, dataBinderFactory);
	}

	private static class AlwaysFailValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			errors.reject("some.error", "Some Error");
		}
		
	}
	
}
