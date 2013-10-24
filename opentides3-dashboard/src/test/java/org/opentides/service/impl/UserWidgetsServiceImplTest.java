package org.opentides.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opentides.bean.UserWidgets;
import org.opentides.bean.Widget;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.dao.UserWidgetsDao;
import org.opentides.service.UserService;
import org.opentides.service.WidgetService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserWidgetsServiceImplTest {
	
	@InjectMocks
	private UserWidgetsServiceImpl userWidgetService = new UserWidgetsServiceImpl();
	
	@Mock
	private UserWidgetsDao userWidgetsDao;
	
	@Mock
	private WidgetService widgetService;
	
	@Mock
	private UserService userService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		SecurityContextHolder.clearContext();
		SecurityContextHolder.getContext().setAuthentication(getAuthentication());
	}

	@Test
	public void testAddUserWidgetsLongString() {
		long userId = 1l;
		String selectedWidgets = "1,2,3";
		when(userService.load(1l)).thenReturn(getTestBaseUser());
		Widget widget1 = getSampleWidget(1l, "Widget1", "Widget 1");
		Widget widget2 = getSampleWidget(2l, "Widget2", "Widget 2");
		Widget widget3 = getSampleWidget(3l, "Widget3", "Widget 3");
		when(widgetService.load("1")).thenReturn(widget1);
		when(widgetService.load("2")).thenReturn(widget2);
		when(widgetService.load("3")).thenReturn(widget3);
		when(userWidgetService.countUserWidgetsColumn(1, 1l)).thenReturn(3l);
		when(userWidgetService.countUserWidgetsColumn(2, 1l)).thenReturn(2l);
		userWidgetService.addUserWidgets(userId, selectedWidgets);
		verify(userWidgetsDao, times(3)).saveEntityModel(isA(UserWidgets.class));
	}
	
	@Test
	public void testAddUserWidgetsLongStringWithNullWidget() {
		long userId = 1l;
		String selectedWidgets = "1,2,3,4";
		when(userService.load(1l)).thenReturn(getTestBaseUser());
		when(widgetService.load("1")).thenReturn(getSampleWidget(1l, "Widget1", "Widget 1"));
		when(widgetService.load("2")).thenReturn(getSampleWidget(1l, "Widget2", "Widget 2"));
		when(widgetService.load("3")).thenReturn(getSampleWidget(1l, "Widget3", "Widget 3"));
		when(widgetService.load("4")).thenReturn(null);
		userWidgetService.addUserWidgets(userId, selectedWidgets);
		verify(userWidgetsDao, times(3)).saveEntityModel(isA(UserWidgets.class));
	}
	
	@Test
	public void testAddUserWidgetsLongWidget() {
		Widget widget1 = getSampleWidget(1l, "Widget1", "Widget 1");
		userWidgetService.addUserWidgets(1l, widget1);
		verify(userWidgetsDao, times(1)).saveEntityModel(isA(UserWidgets.class));
	}

	/*
	@Test
	public void testFindUserWidgets() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindSpecificUserWidgets() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUserWidgetsOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateUserWidgetsStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testCountUserWidgetsColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveUserGroupWidgetsWithAccessCodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetupUserGroupWidgets() {
		fail("Not yet implemented");
	}*/
	
	private Widget getSampleWidget(Long id, String name, String description) {
		Widget widget = new Widget();
		widget.setId(id);
		widget.setName(name);
		widget.setDescription(description);
		widget.setUrl("/url/" + name);
		return widget;
	}
	
	private BaseUser getTestBaseUser() {
		BaseUser baseUser = new BaseUser();
		UserCredential uc = new UserCredential();
		uc.setUsername("username");
		uc.setPassword("password");
		uc.setEnabled(true);
		baseUser.setId(1l);
		baseUser.setCredential(uc);
		
		return baseUser;
	}
	
	private Authentication getAuthentication() {
		List<GrantedAuthority> auths = new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("VIEW_USER_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("SEARCH_USER_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("ADD_USER_WIDGETS"));
		auths.add(new SimpleGrantedAuthority("EDIT_USER_WIDGETS"));
		
		SessionUser su = new SessionUser(getTestBaseUser(), auths);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(su, "password");
		
		return auth;
	}

}
