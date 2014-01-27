package org.opentides.dao.impl;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.opentides.bean.SystemCodes;
import org.opentides.dao.SystemCodesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class SystemCodesDAOIntegrationTest extends BaseDaoTest {
	private static final Logger _log = Logger.getLogger(SystemCodesDAOIntegrationTest.class);
	
	@Autowired
	private SystemCodesDao dao;

	private static final class SystemCodesMapper implements RowMapper<SystemCodes> {
	    public SystemCodes mapRow(ResultSet rs, int rowNum) throws SQLException {
	        SystemCodes sc = new SystemCodes();
	        sc.setCategory(rs.getString("category_"));
	        sc.setKey(rs.getString("key_"));
	        sc.setValue(rs.getString("value_"));
	        return sc;
	    }
	}
	
	private static final class SystemCodesExtractor implements ResultSetExtractor<SystemCodes>{
		public SystemCodes extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			rs.next();
			SystemCodes sc = new SystemCodes();
			sc.setId(rs.getLong("id"));
			sc.setCategory(rs.getString("category_"));
			sc.setKey(rs.getString("key_"));
	        sc.setValue(rs.getString("value_"));
	        return sc;
		}
	}

	@Test
	public void testFindSystemCodesByCategory(){
		int expected = jdbcTemplate.queryForInt("SELECT count(*) FROM SYSTEM_CODES WHERE CATEGORY_='COUNTRY'");
		int actual = dao.findSystemCodesByCategory("COUNTRY").size(); 
		assertEquals(expected, actual);
	}
	
	@Test
	public void testLoadBySystemCodesByKey(){
		SystemCodes expected = (SystemCodes) jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE KEY_='AD'", new SystemCodesExtractor());		
		SystemCodes actual = dao.loadBySystemCodesByKey("AD");
		
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getCategory(),actual.getCategory());
		assertEquals(expected.getValue(), actual.getValue());
	}
	
	@Test
	public void testCountDuplicate() {
		SystemCodes sample = new SystemCodes();
		sample.setId(9001l);
		sample.setKey("TT");
		Long count = dao.countDuplicate(sample);
		assertEquals(new Long(1l), count);
	}

	@Test
	public void testGetAllCategories(){
		int actual = dao.getAllCategories().size();
		_log.debug("Number of categories: "+dao.getAllCategories().size());
		assertEquals(2, actual);
	}
	
	public void testGetAllCategoriesExcept() {
		List<String> actual = dao.getAllCategoriesExcept("OFFICE");
		List<String> expected = Arrays.asList("COUNTRY");
		assertEquals(expected, actual);
	}
	
	public void testGetAllCategoriesExceptNoReturn() {
		List<String> actual = dao.getAllCategoriesExcept("OFFICE", "COUNTRY");
		assertTrue(actual.isEmpty());
	}

}
