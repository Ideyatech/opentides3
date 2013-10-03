package org.opentides.web.json;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.annotation.JsonView;

public class ViewInjectingReturnValueHandlerTest {

	private ViewInjectingReturnValueHandler handler;

	ModelAndViewContainer mavContainer;
	NativeWebRequest nativeWebRequest;

	@Mock
	HandlerMethodReturnValueHandler mockHandler;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		handler = new ViewInjectingReturnValueHandler(mockHandler);
		mavContainer = new ModelAndViewContainer();
		nativeWebRequest = new ServletWebRequest(new MockHttpServletRequest());

	}

	@Test
	public void testHandleReturnValueMethodWithResponseViewAnnotation() throws Exception {
		MethodParameter responseViewType = new MethodParameter(this.getClass().getDeclaredMethod("getSampleClassWithAnnotation"), -1);
		Object sampleClass = new SampleClass("John", "Doe", "25", "john.doe@opentides.com");
		handler.handleReturnValue(sampleClass, responseViewType, mavContainer, nativeWebRequest);

		//Check if a PojoView object is delegated to mockHandler instead of a SampleClass object
		verify(mockHandler).handleReturnValue(isA(PojoView.class),
				any(MethodParameter.class), any(ModelAndViewContainer.class),
				any(NativeWebRequest.class));
		
	}
	
	@Test
	public void testHandleReturnValueMethodWithoutResponseViewAnnotation() throws Exception {
		MethodParameter responseViewType = new MethodParameter(this.getClass().getDeclaredMethod("getSampleClassWithoutAnnotation"), -1);
		Object sampleClass = new SampleClass("John", "Doe", "25", "john.doe@opentides.com");
		handler.handleReturnValue(sampleClass, responseViewType, mavContainer, nativeWebRequest);

		//Check if a SampleClass object is delegated to mockHandler instead of a PojoView object
		verify(mockHandler).handleReturnValue(isA(SampleClass.class),
				any(MethodParameter.class), any(ModelAndViewContainer.class),
				any(NativeWebRequest.class));
		
	}

	@ResponseView(Views.FormView.class)
	public SampleClass getSampleClassWithAnnotation() {
		return new SampleClass("John", "Doe", "25", "john.doe@opentides.com");
	}
	
	public SampleClass getSampleClassWithoutAnnotation() {
		return new SampleClass("John", "Doe", "25", "john.doe@opentides.com");
	}

	private static class SampleClass {
		public SampleClass(String name, String lastName, String age,
				String email) {
			super();
			this.name = name;
			this.lastName = lastName;
			this.age = age;
			this.email = email;
		}

		@JsonView(Views.SearchView.class)
		private String name;

		@JsonView(Views.FormView.class)
		private String lastName;

		@JsonView(Views.DisplayView.class)
		private String age;

		@JsonView(Views.FullView.class)
		private String email;
	}

}
