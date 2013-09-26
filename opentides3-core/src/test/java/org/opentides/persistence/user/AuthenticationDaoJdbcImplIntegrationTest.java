package org.opentides.persistence.user;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.sql.Connection;

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
import org.opentides.bean.user.SessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Integration test for AuthenticationDaoJdbcImpl using a MySQL database.
 * 
 * 
 * @author gino
 *
 */
@ContextConfiguration(locations = {"classpath:applicationContext-dao-test.xml"})
@TransactionConfiguration
public class AuthenticationDaoJdbcImplIntegrationTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private AuthenticationDaoJdbcImpl authenticationService;
	
	@Autowired
	private DataSource dataSource;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
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
		
	}
	
	@Test
	public void testLoadUserByUsername() {
		UserDetails userDetails = authenticationService.loadUserByUsername("admin");
		assertNotNull(userDetails);
		//Should be an instance of SessionUser
		SessionUser.class.isAssignableFrom(userDetails.getClass());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNotFound() {
		authenticationService.loadUserByUsername("adminadmin");
	}
	
}
