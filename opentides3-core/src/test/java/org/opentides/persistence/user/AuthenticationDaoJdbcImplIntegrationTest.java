package org.opentides.persistence.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.SessionUser;
import org.opentides.dao.UserDao;
import org.opentides.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Integration test for AuthenticationDaoJdbcImpl.
 * <p>
 * An actual database is use here. For the configurations please check
 * applicationContext-dao-test.xml
 * </p>
 * @author gino
 *
 */
@ContextConfiguration(locations = {"classpath:applicationContext-dao-test.xml"})
public class AuthenticationDaoJdbcImplIntegrationTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private AuthenticationDaoJdbcImpl authenticationService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataSource dataSource;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private TransactionTemplate transactionTemplate;
	
	@Before
	public void init() throws Exception {
		
		IDatabaseConnection dbUnitCon = null;
		
		Connection conn = DataSourceUtils.getConnection(dataSource);
		dbUnitCon = new DatabaseConnection(conn);
		DatabaseConfig config = dbUnitCon.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
		
		IDataSet dataSet = new FlatXmlDataSet(
				new FileInputStream("./src/test/resources/dataset/authenticationDaoJdbcImpl-dataset.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
		
		transactionTemplate = new TransactionTemplate(transactionManager);
		
	}
	
	@Test
	public void testLoadUserByUsername() {
		UserDetails userDetails = authenticationService.loadUserByUsername("admin");
		assertNotNull(userDetails);
		//Should be an instance of SessionUser
		SessionUser.class.isAssignableFrom(userDetails.getClass());
	}
	
	@Test
	public void testLoadUserByUsernameLockedUser() {
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userService.updateFailedLogin("lockeduser", new Date().getTime());
			}
		});
		UserDetails userDetails = authenticationService.loadUserByUsername("lockeduser");
		assertNotNull(userDetails);
		assertFalse(userDetails.isAccountNonLocked());
		
		BaseUser baseUser = userDao.loadByUsername("lockeduser");
		assertEquals(new Long(5), baseUser.getFailedLoginCount());
	}
	
	@Test
	public void testOnApplicationEventAuthSuccessEvent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("192.168.1.1");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				userService.updateFailedLogin("lockeduser", new Date().getTime());
			}
		});
		
		Authentication authentication = new UsernamePasswordAuthenticationToken("lockeduser", new Object[]{});
		final AuthenticationSuccessEvent event = new AuthenticationSuccessEvent(authentication);
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				authenticationService.onApplicationEvent(event);
			}
		});
		
		BaseUser baseUser = userDao.loadByUsername("lockeduser");
		assertEquals(new Long(0), baseUser.getFailedLoginCount());
	}
	
	@Test
	public void testOnApplicationEventAuthFailedEvent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("192.168.1.1");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken("lockeduser", new Object[]{});
		final AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
				authentication, new BadCredentialsException(
						"Invalid username or password"));
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				authenticationService.onApplicationEvent(event);
			}
		});
		
		BaseUser baseUser = userDao.loadByUsername("lockeduser");
		assertEquals(new Long(5), baseUser.getFailedLoginCount());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNotFound() {
		authenticationService.loadUserByUsername("adminadmin");
	}
	
}
