package com.ideyatech.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.ideyatech.bean.Ninja;
import com.ideyatech.dao.NinjaDAO;

/**
 * Integration test for BaseEntityDAOJpaImpl class using the sample Ninja class.
 * This test uses an actual MySQL database. To be able to run this successfully 
 * you will need the following configurations:
 * 
 *  <li>DB Server - localhost
 *  <li>Port	  - 3306
 *  <li>DB Name	  - opentidestest
 *  <li>Username  - opentides3
 *  <li>Password  - password
 *  
 *  Please check applicationContext-dao-test.xml for more information. 
 * 
 * 
 * @author gino
 *
 */
@ContextConfiguration(locations = {"classpath:applicationContext-dao-test.xml"})
public class BaseEntityDAOJpaImplIntegrationTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private NinjaDAO ninjaDAO;
	
	@Autowired
	private DataSource dataSource;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	/**
	 * We will use transactionTemplate to handle transaction. For some unknown reason annotating with @TransactionConfiguration
	 * is not working.
	 */
	private TransactionTemplate transactionTemplate;
	
	@Before
	public void init() throws Exception {
		IDatabaseConnection dbUnitCon = null;
		
		Connection conn = DataSourceUtils.getConnection(dataSource);
		dbUnitCon = new DatabaseConnection(conn);
		DatabaseConfig config = dbUnitCon.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
		
		IDataSet dataSet = new FlatXmlDataSet(
				new FileInputStream("./src/test/resources/dataset/ninja-dataset.xml"));
		DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, dataSet);
		
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
	@Test
	public void testInsertSelectAllDelete() {
		//Insert new Ninja
		final Ninja ninja = new Ninja();
		ninja.setFirstName("Richard");
		ninja.setLastName("Buendia");
		ninja.setGender("Male");
		ninja.setEmail("richard@buendia.com");
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				
				ninjaDAO.saveEntityModel(ninja);
				
				//Find the ninja. Initially there are 3. So Id should be 4..
				Ninja searchedNinja = ninjaDAO.loadEntityModel(4l);
				assertNotNull(searchedNinja);
				assertEquals(ninja, searchedNinja);
				
				//Updating
				searchedNinja.setEmail("email@email.com");
				ninjaDAO.saveEntityModel(ninja);
				
				//ID should not be changed
				assertEquals(new Long(4l), searchedNinja.getId());
				assertEquals("email@email.com", searchedNinja.getEmail());
				
				// Select all Ninja
				List<Ninja> ninjas = ninjaDAO.findAll();
				assertEquals(4, ninjas.size());
				
				//Delete ninja
				ninjaDAO.deleteEntityModel(4l);
				
				// Select all Ninja again...
				List<Ninja> newNinjas = ninjaDAO.findAll();
				assertEquals(3, newNinjas.size());
			}
		});
	}
	
	@Test
	public void testFindByNamedQuery() {
		final Ninja ninja = new Ninja();
		ninja.setFirstName("Richard");
		ninja.setLastName("Buendia");
		ninja.setGender("Male");
		ninja.setEmail("richard@buendia.com");
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				//We will use this for searching...
				ninjaDAO.saveEntityModel(ninja);
				String jpql = "jpql.ninja.findByName";
				Map<String, Object> params = new HashMap<>();
				params.put("firstName", "Richard");
				List<Ninja> ninjas = ninjaDAO.findByNamedQuery(jpql, params);
				//I expect only 1 Richard..
				assertEquals(1, ninjas.size());
				Ninja result = ninjas.get(0);
				assertEquals("Richard", result.getFirstName());
				assertEquals("Buendia", result.getLastName());
				assertEquals("Male", result.getGender());
				
				//Find all using jpql but with limit
				jpql = "jpql.ninja.findAll";
				ninjas = ninjaDAO.findByNamedQuery(jpql, null, 1, 2);
				
				assertEquals(2, ninjas.size());
				Ninja ninja1 = ninjas.get(0);
				Ninja ninja2 = ninjas.get(1);
				
				//jpql is ordered by ID so ID of ninja1 should be 2 and for ninja2 it should be 3
				//Check ninja-dataset.xml
				
				assertEquals(new Long(2l), ninja1.getId());
				assertEquals("email2@email.com", ninja1.getEmail());
				assertEquals("Master", ninja1.getFirstName());
				assertEquals("Ninja", ninja1.getLastName());
				
				assertEquals(new Long(3l), ninja2.getId());
				assertEquals("email3@email.com", ninja2.getEmail());
				assertEquals("Sensei", ninja2.getFirstName());
				assertEquals("Boss", ninja2.getLastName());
				
			}
		});
	}
	
	@Test
	public void testCountAndFindByExample() {
		final Ninja ninja1 = new Ninja();
		ninja1.setFirstName("Richard");
		ninja1.setLastName("Buendia");
		ninja1.setGender("Male");
		ninja1.setEmail("richard@buendia.com");
		
		final Ninja ninja2 = new Ninja();
		ninja2.setFirstName("Richard");
		ninja2.setLastName("Santos");
		ninja2.setGender("Male");
		ninja2.setEmail("richard@santos.com");
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ninjaDAO.saveEntityModel(ninja1);
				ninjaDAO.saveEntityModel(ninja2);
				
				Ninja sample = new Ninja();
				sample.setFirstName("Richard");
				
				Long count = ninjaDAO.countByExample(sample);
				assertEquals(new Long(2l), count);
				
				List<Ninja> result = ninjaDAO.findByExample(sample);
				assertEquals(2, result.size());
				
				List<Ninja> expected = new ArrayList<>();
				expected.add(ninja1);
				expected.add(ninja2);
				
				assertEquals(expected, result);
			}
		});
	}
	
	@Test
	public void testSaveAllEntityModel() {
		final Ninja ninja1 = new Ninja();
		ninja1.setFirstName("Richard");
		ninja1.setLastName("Buendia");
		ninja1.setGender("Male");
		ninja1.setEmail("richard@buendia.com");
		
		final Ninja ninja2 = new Ninja();
		ninja2.setFirstName("Richard");
		ninja2.setLastName("Santos");
		ninja2.setGender("Male");
		ninja2.setEmail("richard@santos.com");
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				List<Ninja> ninjas = new ArrayList<>();
				ninjas.add(ninja1);
				ninjas.add(ninja2);
				
				ninjaDAO.saveAllEntityModel(ninjas);
				
				Long result = ninjaDAO.countAll();
				assertEquals(new Long(5l), result);
				
			}
		});
	}

}
