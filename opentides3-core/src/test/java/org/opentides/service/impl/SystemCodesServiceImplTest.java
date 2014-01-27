package org.opentides.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.SystemCodes;
import org.opentides.dao.SystemCodesDao;
import org.opentides.service.SystemCodesService;

public class SystemCodesServiceImplTest {
	
	@InjectMocks
	private SystemCodesService systemCodesService = new SystemCodesServiceImpl();
	
	@Mock
	private SystemCodesDao systemCodesDao;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetAllCategories() {
		List<String> expected = Arrays.asList(new String[]{"CATEGORY_1", "CATEGORY_2", "CATEGORY_3"});
		when(systemCodesDao.getAllCategories()).thenReturn(expected);
		
		List<String> actual = systemCodesService.getAllCategories();
		verify(systemCodesDao).getAllCategories();
		
		assertEquals(expected, actual);
	}

	@Test
	public void testFindByKeySystemCodes() {
		SystemCodes sample = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		SystemCodes expected = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadBySystemCodesByKey("KEY_1")).thenReturn(expected);
		
		SystemCodes actual = systemCodesService.findByKey(sample);
		verify(systemCodesDao).loadBySystemCodesByKey("KEY_1");
		
		assertEquals(expected, actual);
		
	}

	@Test
	public void testFindByKeyString() {
		SystemCodes expected = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadBySystemCodesByKey("KEY_1")).thenReturn(expected);
		
		SystemCodes actual = systemCodesService.findByKey("KEY_1");
		verify(systemCodesDao).loadBySystemCodesByKey("KEY_1");
		
		assertEquals(expected, actual);
	}

	@Test
	public void testFindSystemCodesByCategory() {
		List<SystemCodes> expected = Arrays.asList(
				new SystemCodes("CATEGORY_1", "KEY_1", "Value 1"),
				new SystemCodes("CATEGORY_1", "KEY_2", "Value 2"));
		when(systemCodesDao.findSystemCodesByCategory("CATEGORY_1")).thenReturn(expected);
		
		List<SystemCodes> actual = systemCodesService.findSystemCodesByCategory("CATEGORY_1");
		verify(systemCodesDao).findSystemCodesByCategory("CATEGORY_1");
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGetAllCategoriesExcept() {
		List<String> expected = Arrays.asList(new String[]{"CATEGORY_1", "CATEGORY_2", "CATEGORY_3"});
		when(systemCodesDao.getAllCategoriesExcept("CATEGORY_4")).thenReturn(expected);
		
		List<String> actual = systemCodesService.getAllCategoriesExcept("CATEGORY_4");
		verify(systemCodesDao).getAllCategoriesExcept("CATEGORY_4");
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGetSystemCodesDao() {
		assertEquals(this.systemCodesDao, ((SystemCodesServiceImpl)systemCodesService).getDao());
	}

	@Test
	public void testIsDuplicateKeyTrue() {
		SystemCodes sample = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.countDuplicate(sample)).thenReturn(1l);
		
		assertTrue(systemCodesService.isDuplicateKey(sample));
		verify(systemCodesDao).countDuplicate(sample);
	}
	
	@Test
	public void testIsDuplicateKeyFalse() {
		SystemCodes sample = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.countDuplicate(sample)).thenReturn(0l);
		
		assertFalse(systemCodesService.isDuplicateKey(sample));
		verify(systemCodesDao).countDuplicate(sample);
	}
	
	@Test
	public void testIsParentValid() throws Exception {
		Map<String, SystemCodes> testCodes = getTestSystemCodes();
		when(systemCodesDao.loadBySystemCodesByKey("PARENT_1")).thenReturn(testCodes.get("PARENT_1"));
		when(systemCodesDao.loadBySystemCodesByKey("PARENT_2")).thenReturn(testCodes.get("PARENT_2"));
		when(systemCodesDao.loadBySystemCodesByKey("PARENT_3")).thenReturn(testCodes.get("PARENT_3"));
		when(systemCodesDao.loadBySystemCodesByKey("PARENT_4")).thenReturn(testCodes.get("PARENT_4"));
		
		SystemCodes systemCodes1 = testCodes.get("PARENT_1");
		SystemCodes systemCodes3 = testCodes.get("PARENT_3");
		systemCodes1.setParent(systemCodes3);
		assertFalse(systemCodes1.isParentValid());
		
		SystemCodes parent5 = new SystemCodes();
		parent5.setKey("PARENT_5");
		systemCodes1.setParent(parent5);
		assertTrue(systemCodes1.isParentValid());
		
		SystemCodes parent6 = new SystemCodes();
		parent6.setKey("CASE_STATUS");
		SystemCodes parent7 = new SystemCodes();
		parent7.setKey("CASE_ACTION");
		parent6.setParent(parent7);
		parent7.setParent(parent6);
		assertFalse(parent6.isParentValid());
		
	}
	
	private Map<String, SystemCodes> getTestSystemCodes() throws Exception {
		Map<String, SystemCodes> maps = new HashMap<>();
		SystemCodes systemCodes1 = new SystemCodes();
		systemCodes1.setKey("PARENT_1");
		systemCodes1.setParent(null);
		
		SystemCodes systemCodes2 = new SystemCodes();
		systemCodes2.setKey("PARENT_2");
		systemCodes2.setParent(systemCodes1);
		
		SystemCodes systemCodes3 = new SystemCodes();
		systemCodes3.setKey("PARENT_3");
		systemCodes3.setParent(systemCodes2);
		
		SystemCodes systemCodes4 = new SystemCodes();
		systemCodes4.setKey("PARENT_4");
		systemCodes4.setParent(systemCodes1);
		
		maps.put("PARENT_1", systemCodes1);
		maps.put("PARENT_2", systemCodes2);
		maps.put("PARENT_3", systemCodes3);
		maps.put("PARENT_4", systemCodes4);
		
		return maps;
	}

}
