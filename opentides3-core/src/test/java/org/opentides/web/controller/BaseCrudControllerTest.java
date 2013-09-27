package org.opentides.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opentides.bean.MessageResponse;
import org.opentides.bean.SearchResults;
import org.opentides.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.ideyatech.bean.Ninja;
import com.ideyatech.service.NinjaService;

/**
 * This will use an instance of NinjaCrudControllerTest to test 
 * methods of {@link BaseCrudController}.
 * 
 * @author gino
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class BaseCrudControllerTest {

	//Class to test
	@Autowired
	private BaseCrudController<Ninja> ninjaCrudController;
	
	@Autowired
	private NinjaService ninjaService;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	//Mocks
	MockHttpServletRequest mockRequest;
	MockHttpServletResponse mockResponse;
	
	@Before
	public void init() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
	}
	
	/**
	 * Exception is an instance of BindException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleBindException1() throws Exception {
		BindingResult bindingResult = new BeanPropertyBindingResult(new Ninja(), "ninja");
		bindingResult.addError(new FieldError("ninja", "firstName", null, false, 
				new String[]{"error.required"}, new String[]{"First name"}, 
				"First name is required"));
		BindException ex = new BindException(bindingResult);
		
		Map<String, Object> bindExceptions = ninjaCrudController.handleBindException(ex, mockRequest);
		
		assertNotNull(bindExceptions);
		assertNotNull(bindExceptions.get("messages"));
		List<MessageResponse> messages = (List<MessageResponse>)bindExceptions.get("messages");
		assertTrue(messages.size() == 1);
		MessageResponse messageResponse = messages.get(0);
		
		assertEquals("First name is required", messageResponse.getMessage());
		assertEquals("error.required", messageResponse.getCode());
		assertEquals("firstName", messageResponse.getFieldName());
		assertEquals("ninja", messageResponse.getObjectName());
		
	}
	
	/**
	 * Exception is NOT an instance of BindException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testHandleBindException2() throws Exception {
		Exception ex = new RuntimeException("Sample Exception");
		mockRequest.addHeader("Accept", "application/json");
		
		Map<String, Object> bindExceptions = ninjaCrudController.handleBindException(ex, mockRequest);
		
		assertNotNull(bindExceptions);
		assertNotNull(bindExceptions.get("messages"));
		List<MessageResponse> messages = (List<MessageResponse>)bindExceptions.get("messages");
		assertTrue(messages.size() == 1);
		MessageResponse messageResponse = messages.get(0);
		
		assertEquals("Oops... We encountered a server error. <br/> Sample Exception", messageResponse.getMessage());
		
	}
	@Test
	public void testSearchEverythingWithPaging() {
		List<Ninja> expected = new ArrayList<Ninja>();
		expected.add(new Ninja());
		//command is null
		Mockito.when(ninjaService.findAll(0, 20)).thenReturn(expected);
		Mockito.when(ninjaService.countAll()).thenReturn(1l);
		SearchResults<Ninja> results = ninjaCrudController.search(null, mockRequest);
		
		//Verify that ninjaService methods expected to run were invoked once...
		Mockito.verify(ninjaService).findAll(0, 20);
		Mockito.verify(ninjaService).countAll();
		
		assertNotNull(results);
		assertEquals(1, results.getTotalResults());
		assertNotNull(results.getSearchTime());
	}
	
	@Test
	public void testSearchWithExampleWithPaging() {
		List<Ninja> expected = new ArrayList<Ninja>();
		Ninja expectedNinja1 = new Ninja();
		expectedNinja1.setFirstName("Naruto");
		expectedNinja1.setLastName("Uzumaki");
		expected.add(expectedNinja1);
		
		Ninja sample = new Ninja();
		sample.setFirstName("Naruto");
		sample.setLastName("Uzumaki");
		
		//command is null
		Mockito.when(ninjaService.findByExample(sample,0, 20)).thenReturn(expected);
		Mockito.when(ninjaService.countByExample(sample)).thenReturn(1l);
		SearchResults<Ninja> results = ninjaCrudController.search(sample, mockRequest);
		
		//Verify that ninjaService methods expected to run were invoked once...
		Mockito.verify(ninjaService).findByExample(sample,0, 20);
		Mockito.verify(ninjaService).countByExample(sample);
		
		assertNotNull(results);
		assertEquals(1, results.getTotalResults());
		Ninja resultNinja = results.getResults().get(0);
		assertNotNull(resultNinja);
		assertNotNull(results.getSearchTime());
		assertEquals(expectedNinja1, resultNinja);
		
	}
	
	@Test
	public void testSearchHtml() {
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		command.setFirstName("Sasuke");
		command.setLastName("Uchiha");
		
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		String result = ninjaCrudController.searchHtml(command, bindingResult, modelMap, mockRequest, mockResponse);
		assertEquals("/base/ninja-codes-crud", result);
		
		Object formCommand = modelMap.get("formCommand");
		assertNotNull(formCommand);
		assertEquals(Ninja.class, formCommand.getClass());
		assertEquals(command, modelMap.get("searchCommand"));
		assertEquals("search", modelMap.get("mode"));
		assertEquals("ot3-search", modelMap.get("search"));
		assertEquals("ot3-form hide", modelMap.get("form"));
		assertEquals("ot3-add", modelMap.get("add"));
		assertEquals("ot3-update", modelMap.get("update"));
		assertEquals("post", modelMap.get("method"));
		
	}
	
	@Test
	public void testGetHtmlExistingObject() {
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Long id = 1l;
		Ninja expected = new Ninja();
		expected.setFirstName("Richard");
		expected.setLastName("Buendia");
		
		Mockito.when(ninjaService.load(id)).thenReturn(expected);
		
		String result = ninjaCrudController.getHtml(id, modelMap, mockRequest, mockResponse);
		
		assertEquals("/base/ninja-codes-crud", result);
		assertEquals(expected, modelMap.get("formCommand"));
		assertEquals("ot3-add hide", modelMap.get("add"));
		assertEquals("ot3-update", modelMap.get("update"));
		assertEquals("put", modelMap.get("method"));
		assertEquals("form", modelMap.get("mode"));
		assertEquals("ot3-search hide", modelMap.get("search"));
		assertEquals("ot3-form", modelMap.get("form"));
		
	}
	
	@Test
	public void testGetHtmlObjectForAdding() {
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Long id = 0l;
		Ninja expected = new Ninja();
		expected.setFirstName("Richard");
		expected.setLastName("Buendia");
		
		String result = ninjaCrudController.getHtml(id, modelMap, mockRequest, mockResponse);
		
		assertEquals("/base/ninja-codes-crud", result);
		assertNotNull(modelMap.get("formCommand"));
		assertEquals("ot3-update hide", modelMap.get("update"));
		assertEquals("ot3-add", modelMap.get("add"));
		assertEquals("post", modelMap.get("method"));
		assertEquals("form", modelMap.get("mode"));
		assertEquals("ot3-search hide", modelMap.get("search"));
		assertEquals("ot3-form", modelMap.get("form"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDelete() throws Exception {
		Long id = 1l;
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		command.setId(id);
		command.setFirstName("Sasuke");
		command.setLastName("Uchiha");
		
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		Map<String, Object> model = ninjaCrudController.delete(id, command,
				bindingResult, modelMap, mockRequest, mockResponse);
		
		List<MessageResponse> messages = (List<MessageResponse>)model.get("messages");
		assertEquals(1, messages.size());
		
		MessageResponse message = messages.get(0);
		assertEquals("Record has been successfully deleted.", message.getMessage());
		
		//Verify that method delete(Long id) is called
		Mockito.verify(ninjaService).delete(id);
		
	}
	
	@Test(expected = DataAccessException.class)
	public void testDeleteUnsuccessful() throws Exception {
		Long id = 1l;
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		command.setId(id);
		command.setFirstName("Sasuke");
		command.setLastName("Uchiha");
		
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		//Throw an exception when delete fails
		Mockito.doThrow(new DataAccessException("Mock exception")).when(ninjaService).delete(id);
		ninjaCrudController.delete(id, command,
				bindingResult, modelMap, mockRequest, mockResponse);
		
	}
	
	@Test(expected = DataAccessException.class)
	public void testDeleteInvalidId() throws Exception {
		Long id = -1l;
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		ninjaCrudController.delete(id, command, bindingResult, modelMap, mockRequest, mockResponse);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() {
		Long id = 1l;
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		command.setId(id);
		command.setFirstName("Sasuke");
		command.setLastName("Uchiha");
		
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		Map<String, Object> model = ninjaCrudController.update(command, id,
				bindingResult, modelMap, mockRequest, mockResponse);
		
		assertEquals(command, model.get("command"));
		List<MessageResponse> messages = (List<MessageResponse>)model.get("messages");
		assertEquals(1, messages.size());
		
		MessageResponse message = messages.get(0);
		assertEquals("Record has been successfully updated.", message.getMessage());
		
		//Verify that ninjaService(T t) method was invoked 
		Mockito.verify(ninjaService).save(command);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreate() {
		Long id = 0l;
		ExtendedModelMap modelMap = new ExtendedModelMap();
		Ninja command = new Ninja();
		command.setId(id);
		command.setFirstName("Sasuke");
		command.setLastName("Uchiha");
		
		BindingResult bindingResult = new BeanPropertyBindingResult(command, "ninja");
		
		Map<String, Object> model = ninjaCrudController.create(command,
				bindingResult, modelMap, mockRequest, mockResponse);
		
		assertEquals(command, model.get("command"));
		List<MessageResponse> messages = (List<MessageResponse>)model.get("messages");
		assertEquals(1, messages.size());
		
		MessageResponse message = messages.get(0);
		assertEquals("Record has been successfully added.", message.getMessage());
		
		//Verify that ninjaService(T t) method was invoked 
		Mockito.verify(ninjaService).save(command);
	}

}
