package org.opentides.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.opentides.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-dao-test.xml"})
@TransactionConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BaseDaoTest extends AbstractJUnit4SpringContextTests {
	
	private static final Logger _log = Logger.getLogger(BaseDaoTest.class);
	
	@Autowired
	private DataSource dataSource;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	protected TransactionTemplate transactionTemplate;
	
	protected String datasetBasePath = "./src/test/resources/dataset/";
	protected String datasetPath;
	
	@Before
	public void init() throws Exception {
		IDatabaseConnection dbUnitCon = null;
		
		Connection conn = DataSourceUtils.getConnection(dataSource);
		dbUnitCon = new DatabaseConnection(conn);
		DatabaseConfig config = dbUnitCon.getConfig();
		
		if(StringUtil.isEmpty(datasetPath))
			datasetPath = datasetBasePath + getClass().getSimpleName() + ".xml";
		
		File datasetFile = new File(datasetPath);
		if(datasetFile.exists()) {
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
			_log.debug("Dataset Path: " + datasetPath);
			IDataSet dataSet = new FlatXmlDataSet(new FileInputStream(datasetFile));
			DatabaseOperation.REFRESH.execute(dbUnitCon, dataSet);
		} else {
			_log.info("No initial dataset loaded");
		}
		
		transactionTemplate = new TransactionTemplate(transactionManager);
	}
	
}
