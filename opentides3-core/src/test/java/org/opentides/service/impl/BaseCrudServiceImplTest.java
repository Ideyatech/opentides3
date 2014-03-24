package org.opentides.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.opentides.bean.SystemCodes;
import org.opentides.bean.user.SessionUser;
import org.opentides.dao.SystemCodesDao;
import org.opentides.exception.InvalidImplementationException;
import org.opentides.service.SystemCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Unit test for {@link BaseCrudServiceImpl} using mocking. Its main objective is to verify
 * if the expected DAO methods were invoked and also check if the checking of access 
 * is working.
 * 
 * @author gino
 *
 */
@ContextConfiguration(locations = {"classpath:applicationContext-service-test.xml"})
public class BaseCrudServiceImplTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private SystemCodesService systemCodesService;
	
	@Autowired
	private SystemCodesDao systemCodesDao;
	
	@Before
	public void init() {
		SecurityContextHolder.clearContext();
		Mockito.reset(systemCodesDao);
	}

	@Test
	public void testFindAllBypassSecurity() {
		when(systemCodesDao.findAll()).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findAll(true);
		verify(systemCodesDao).findAll();
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindAllWithSecurityNoAccess() {
		systemCodesService.findAll(false);
	}
	
	@Test
	public void testFindAllWithSecurityWithAccess() {
		when(systemCodesDao.findAll()).thenReturn(getExpectedListOfSystemCodes());
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		List<SystemCodes> actual = systemCodesService.findAll(false);
		verify(systemCodesDao).findAll();
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testFindAllIntIntBypassSecurity() {
		when(systemCodesDao.findAll(2, 5)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findAll(2, 5, true);
		verify(systemCodesDao).findAll(2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindAllIntIntWithSecurity() {
		systemCodesService.findAll(2, 5, false);
	}
	
	@Test
	public void testFindAllIntIntWithSecurityWithAccess() {
		when(systemCodesDao.findAll(2, 5)).thenReturn(getExpectedListOfSystemCodes());
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		List<SystemCodes> actual = systemCodesService.findAll(2, 5, true);
		verify(systemCodesDao).findAll(2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testFindByExampleTWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample);
		verify(systemCodesDao).findByExample(sample);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindByExampleTWithoutAccess() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample);
		verify(systemCodesDao).findByExample(sample);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testFindByExampleTIntIntWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, 2, 5)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, 2, 5);
		verify(systemCodesDao).findByExample(sample, 2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindByExampleTIntIntWithoutAccess() {
		SystemCodes sample = new SystemCodes();
		systemCodesService.findByExample(sample, 2, 5);
	}

	@Test
	public void testFindByExampleTIntIntBooleanWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, 2, 5)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, 2, 5, false);
		verify(systemCodesDao).findByExample(sample, 2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testFindByExampleTBooleanBooleanBypassSecurity() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, true)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, true, true);
		verify(systemCodesDao).findByExample(sample, true);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindByExampleTBooleanBooleanDoNotBypassSecurity() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		systemCodesService.findByExample(sample, true, false);
	}
	
	@Test
	public void testFindByExampleTBooleanBooleanDoNotBypassSecurityWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, true)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, true, false);
		verify(systemCodesDao).findByExample(sample, true);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testFindByExampleTBooleanIntIntBooleanBypassSecurity() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, true, 2, 5)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, true, 2, 5, true);
		verify(systemCodesDao).findByExample(sample, true, 2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testFindByExampleTBooleanIntIntBooleanWithSecurity() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		systemCodesService.findByExample(sample, true, 2, 5, false);
	}
	
	@Test
	public void testFindByExampleTBooleanIntIntBooleanWithSecurityWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.findByExample(sample, true, 2, 5)).thenReturn(getExpectedListOfSystemCodes());
		List<SystemCodes> actual = systemCodesService.findByExample(sample, true, 2, 5, false);
		verify(systemCodesDao).findByExample(sample, true, 2, 5);
		assertEquals(getExpectedListOfSystemCodes(), actual);
	}

	@Test
	public void testCountAll() {
		when(systemCodesDao.countAll()).thenReturn(10l);
		Long actual = systemCodesService.countAll();
		verify(systemCodesDao).countAll();
		assertEquals(new Long(10l), actual);
	}

	@Test
	public void testCountByExampleT() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.countByExample(sample)).thenReturn(10l);
		Long actual = systemCodesService.countByExample(sample);
		verify(systemCodesDao).countByExample(sample);
		assertEquals(new Long(10l), actual);
	}

	@Test
	public void testCountByExampleTBoolean() {
		SystemCodes sample = new SystemCodes();
		sample.setCategory("CATEGORY_1");
		when(systemCodesDao.countByExample(sample, true)).thenReturn(10l);
		Long actual = systemCodesService.countByExample(sample, true);
		verify(systemCodesDao).countByExample(sample, true);
		assertEquals(new Long(10l), actual);
	}

	@Test
	public void testLoadStringWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load("1");
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testLoadStringWithoutAccess() {
		systemCodesService.load("1");
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testLoadStringEmptyParams() {
		systemCodesService.load("");
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testLoadStringNotANumber() {
		systemCodesService.load("Abb");
	}

	@Test
	public void testLoadLong() {
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load(1l);
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}

	@Test
	public void testLoadStringBoolean() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load("1", true);
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testLoadStringBooleanNoAccess() {
		systemCodesService.load("1", true);
	}

	@Test
	public void testLoadLongBoolean() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load(1l, true);
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testLoadLongBooleanNoAccess() {
		systemCodesService.load(1l, true);
	}

	@Test
	public void testLoadLongBooleanBooleanBypassSecurity() {
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load(1l, true, true);
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testLoadLongBooleanBooleanWithSecurityWithoutAccess() {
		systemCodesService.load(1l, true, false);
	}
	
	@Test
	public void testLoadLongBooleanBooleanWithSecurityWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		when(systemCodesDao.loadEntityModel(1l, true, false)).thenReturn(sc);
		SystemCodes actual = systemCodesService.load(1l, true, false);
		verify(systemCodesDao).loadEntityModel(1l, true, false);
		assertEquals(sc, actual);
	}

	@Test
	public void testSaveT() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		systemCodesService.save(sc);
		verify(systemCodesDao).saveEntityModel(sc);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testSaveTNoAccess() {
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		systemCodesService.save(sc);
	}

	@Test
	public void testSaveTBooleanBypassSecurity() {
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		systemCodesService.save(sc, true);
		verify(systemCodesDao).saveEntityModel(sc);
	}
	
	@Test
	public void testSaveTBooleanWithSecurityWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		systemCodesService.save(sc, false);
		verify(systemCodesDao).saveEntityModel(sc);
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testSaveTBooleanWithSecurityWithoutAccess() {
		SystemCodes sc = new SystemCodes("CATEGORY_1", "KEY_1", "Value 1");
		systemCodesService.save(sc, false);
	}

	@Test
	public void testDeleteStringWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		systemCodesService.delete("1");
		verify(systemCodesDao).deleteEntityModel(new Long(1l));
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testDeleteStringEmptyParam() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		systemCodesService.delete("");
	}
	
	@Test(expected = InvalidImplementationException.class)
	public void testDeleteStringParamNotANumber() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		systemCodesService.delete("aaa");
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testDeleteStringWithoutAccess() {
		systemCodesService.delete("1");
	}

	@Test
	public void testDeleteStringBooleanBypassSecurity() {
		systemCodesService.delete("1", true);
		verify(systemCodesDao).deleteEntityModel(new Long(1l));
	}
	
	@Test
	public void testDeleteStringBooleanWithSecurityWithAccess() {
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
		systemCodesService.delete("1", false);
		verify(systemCodesDao).deleteEntityModel(new Long(1l));
	}
	
	@Ignore("Fix problem with @CrudSecure first")
	@Test(expected = AccessDeniedException.class)
	public void testDeleteStringBooleanWithSecurityWithoutAccess() {
		systemCodesService.delete("1", false);
	}

	@Test
	public void testDeleteLong() {
		
	}

	@Test
	public void testDeleteLongBoolean() {
		
	}

	@Test
	public void testGetDao() {
		SystemCodesDao dao = (SystemCodesDao)((SystemCodesServiceImpl)systemCodesService).getDao();
		assertNotNull(dao);
	}
	
	private List<SystemCodes> getExpectedListOfSystemCodes() {
		List<SystemCodes> scs = new ArrayList<>();
		scs.add(new SystemCodes("CATEGORY_1", "KEY_1", "Value 1"));
		scs.add(new SystemCodes("CATEGORY_2", "KEY_2", "Value 2"));
		scs.add(new SystemCodes("CATEGORY_3", "KEY_3", "Value 3"));
		scs.add(new SystemCodes("CATEGORY_4", "KEY_4", "Value 4"));
		return scs;
	}
	
	private Authentication getAuthentication() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("VIEW_SYSTEM_CODES"));
		auths.add(new SimpleGrantedAuthority("SEARCH_SYSTEM_CODES"));
		auths.add(new SimpleGrantedAuthority("ADD_SYSTEM_CODES"));
		auths.add(new SimpleGrantedAuthority("EDIT_SYSTEM_CODES"));
		auths.add(new SimpleGrantedAuthority("DELETE_SYSTEM_CODES"));
		
		UserDetails ud = new User("user", "password", auths);
		SessionUser su = new SessionUser(ud);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(su, "password");
		
		return auth;
	}

}
