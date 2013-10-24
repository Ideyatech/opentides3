package org.opentides.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.dao.WidgetDao;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class WidgetServiceImplTest {
	
	@InjectMocks
	private WidgetServiceImpl widgetService = new WidgetServiceImpl();
	
	@Mock
	private WidgetDao widgetDao;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
	}

	@Test
	public void testFindByName() {
		Widget expected = new Widget();
		expected.setName("Widget1");
		expected.setUrl("/url/widget1");
		expected.setDescription("This is widget 1");
		
		Widget sample = new Widget();
		sample.setName("Widget1");
		
		when(widgetDao.findByExample(sample, true)).thenReturn(Arrays.asList(expected));
		Widget actual = widgetService.findByName("Widget1");
		
		verify(widgetDao).findByExample(sample, true);
		assertEquals(expected, actual);
		
		Mockito.reset(widgetDao);
		when(widgetDao.findByExample(sample, true)).thenReturn(null);
		actual = widgetService.findByName("Widget1");
		
		verify(widgetDao).findByExample(sample, true);
		assertNull(actual);
		
	}

	@Test
	public void testFindByUrl() {
		Widget expected = new Widget();
		expected.setName("Widget1");
		expected.setUrl("/url/widget1");
		expected.setDescription("This is widget 1");
		
		Widget sample = new Widget();
		sample.setUrl("/url/widget1");
		
		when(widgetDao.findByExample(sample, true)).thenReturn(Arrays.asList(expected));
		Widget actual = widgetService.findByUrl("/url/widget1");
		
		verify(widgetDao).findByExample(sample, true);
		assertEquals(expected, actual);
		
		Mockito.reset(widgetDao);
		when(widgetDao.findByExample(sample, true)).thenReturn(null);
		actual = widgetService.findByUrl("/url/widget1");
		
		verify(widgetDao).findByExample(sample, true);
		assertNull(actual);
	}

	@Test
	public void testGetCurrentUserWidgets() {
		Widget widget1 = new Widget();
		widget1.setName("Widget 1");
		widget1.setDescription("Description 1");
		widget1.setUrl("/url/widget1");
		widget1.setAccessCode("ACCESS_WIDGET_1");
		widget1.setIsUserDefined(true);
		
		Widget widget2 = new Widget();
		widget2.setName("Widget 2");
		widget2.setDescription("Description 2");
		widget2.setUrl("/url/widget2");
		widget2.setAccessCode("ACCESS_WIDGET_2");
		widget2.setIsUserDefined(true);
		
		Widget widget3 = new Widget();
		widget3.setName("Widget 3");
		widget3.setDescription("Description 3");
		widget3.setUrl("/url/widget3");
		widget3.setAccessCode("ACCESS_WIDGET_3");
		widget3.setIsUserDefined(true);
		
		Widget widget4 = new Widget();
		widget4.setName("Widget 4");
		widget4.setDescription("Description 4");
		widget4.setUrl("/url/widget4");
		widget4.setAccessCode("");
		widget4.setIsUserDefined(true);
		
		List<Widget> widgets = Arrays.asList(widget1, widget2, widget3, widget4);
		
		Widget sample = new Widget();
		sample.setIsUserDefined(true);
		
		List<Widget> expected = Arrays.asList(widget1, widget2, widget4);
		when(widgetDao.findByExample(sample, true)).thenReturn(widgets);
		List<Widget> actual = widgetService.getCurrentUserWidgets();
		assertEquals(expected, actual);
	}

	@Test
	public void testRequestWidgetNullWidget() {
		Widget sample = new Widget();
		sample.setName("Widget1");
		when(widgetDao.findByExample(sample, true)).thenReturn(null);
		assertNull(widgetService.requestWidget("/url/widget1", "Widget1", new MockHttpServletRequest()));
	}
	
	@Test
	public void testRequestWidgetUseCacheImage() {
		Widget widget = new Widget();
		widget.setName("Widget1");
		widget.setUrl("/url/widget1");
		widget.setCacheType(Widget.TYPE_IMAGE);
		widget.setLastCacheUpdate(new Date());
		
		Widget sample = new Widget();
		sample.setName("Widget1");
		when(widgetDao.findByExample(sample, true)).thenReturn(Arrays.asList(widget));
		
		Widget actual = widgetService.requestWidget("/url/widget1", "Widget1", new MockHttpServletRequest());
		
		assertEquals(widget, actual);
	}
	
	@Test
	public void testRequestWidgetUseCacheNotExpired() {
		Widget widget = new Widget();
		widget.setName("Widget1");
		widget.setUrl("/url/widget1");
		widget.setCacheType(Widget.TYPE_HTML);
		widget.setLastCacheUpdate(new Date());
		widget.setCacheDuration(3600);
		
		Widget sample = new Widget();
		sample.setName("Widget1");
		when(widgetDao.findByExample(sample, true)).thenReturn(Arrays.asList(widget));
		
		Widget actual = widgetService.requestWidget("/url/widget1", "Widget1", new MockHttpServletRequest());
		
		assertEquals(widget, actual);
	}
	
	@Test
	public void testGetColumnConfig() {
		widgetService.setWidgetColumn("3");
		assertEquals(3, widgetService.getColumnConfig());
		widgetService.setWidgetColumn("");
		assertEquals(2, widgetService.getColumnConfig());
	}

	@Test
	public void testFindDefaultWidget() {
		BaseUser user = new BaseUser();
		user.setId(1l);
		user.setFirstName("First1");
		user.setLastName("Last1");
		
		Widget expected = new Widget();
		expected.setName("Widget1");
		expected.setUrl("/url/widget1");
		expected.setDescription("This is widget 1");
		
		when(widgetDao.findDefaultWidget(user)).thenReturn(Arrays.asList(expected));
		List<Widget> actual = widgetService.findDefaultWidget(user);
		verify(widgetDao).findDefaultWidget(user);
		assertEquals(Arrays.asList(expected), actual);
	}

	@Test
	public void testFindWidgetWithAccessCode() {
		List<String> accessCodes = Arrays.asList("ACCESS_CODE_1", "ACCESS_CODE_2", "ACCESS_CODE_3");
		when(widgetDao.findWidgetWithAccessCode(accessCodes)).thenReturn(getSampleWidgets());
		List<Widget> actual = widgetService.findWidgetWithAccessCode(accessCodes);
		verify(widgetDao).findWidgetWithAccessCode(accessCodes);
		assertEquals(getSampleWidgets(), actual);
		
	}
	
	private Authentication getAuthentication() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("VIEW_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("SEARCH_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("ADD_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("EDIT_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("DELETE_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("ACCESS_WIDGET_1"));
		auths.add(new SimpleGrantedAuthority("ACCESS_WIDGET_2"));
		
		UserDetails ud = new User("user", "password", auths);
		SessionUser su = new SessionUser(ud);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(su, "password");
		
		return auth;
	}
	
	private List<Widget> getSampleWidgets() {
		Widget widget1 = new Widget();
		widget1.setName("Widget 1");
		widget1.setDescription("Description 1");
		widget1.setUrl("/url/widget1");
		widget1.setAccessCode("ACCESS_WIDGET_1");
		widget1.setIsUserDefined(true);
		
		Widget widget2 = new Widget();
		widget2.setName("Widget 2");
		widget2.setDescription("Description 2");
		widget2.setUrl("/url/widget2");
		widget2.setAccessCode("ACCESS_WIDGET_2");
		widget2.setIsUserDefined(true);
		
		Widget widget3 = new Widget();
		widget3.setName("Widget 3");
		widget3.setDescription("Description 3");
		widget3.setUrl("/url/widget3");
		widget3.setAccessCode("ACCESS_WIDGET_3");
		widget3.setIsUserDefined(true);
		
		Widget widget4 = new Widget();
		widget4.setName("Widget 4");
		widget4.setDescription("Description 4");
		widget4.setUrl("/url/widget4");
		widget4.setAccessCode("");
		widget4.setIsUserDefined(true);
		
		List<Widget> widgets = Arrays.asList(widget1, widget2, widget3, widget4);
		
		return widgets;
	}

}
