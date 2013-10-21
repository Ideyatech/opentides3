package org.opentides.util;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class XMLPersistenceUtilTest {

	@Test
	public void testGetProperties() {
		Properties props = XMLPersistenceUtil.getProperties("META-INF/persistence-test.xml", "testPU");
		assertNotNull(props);
		//assertEquals("org.hibernate.dialect.MySQL5InnoDBDialect", props.get("hibernate.dialect"));
		assertEquals("create-drop", props.get("hibernate.hbm2ddl.auto"));
		
		assertEquals("false", props.get("hibernate.show_sql"));
		assertEquals("class", props.get("hibernate.ejb.autodetection"));
		assertEquals("false", props.get("hibernate.cache.use_second_level_cache"));
		assertEquals("false", props.get("hibernate.cache.use_query_cache"));
		
	}

}
