package org.opentides.dao.impl;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;
import org.opentides.bean.SystemCodes;
import org.opentides.dao.SystemCodesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

@Ignore
public class AuditLogDAOIntegrationTest extends BaseDaoTest {
	
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
	
	@Override
	public void init() throws Exception {
		jdbcTemplate.update("DELETE FROM SYSTEM_CODES WHERE KEY_ = 'HR'");
		datasetPath = datasetBasePath + "SystemCodesDAOIntegrationTest.xml";
		super.init();
	}
			
	@Test
	public void testLogEvent(){
		int prevCount = jdbcTemplate.queryForInt("SELECT count(*) FROM HISTORY_LOG");
		
		jdbcTemplate.execute("INSERT INTO SYSTEM_CODES(CATEGORY_,KEY_,VALUE_) VALUES('OFFICE','HR','Human Resources')");
		SystemCodes sc = (SystemCodes) jdbcTemplate.query("SELECT * FROM SYSTEM_CODES WHERE VALUE_='Human Resources'", new SystemCodesExtractor());
		
		AuditLogDaoImpl.logEvent("Sample log for testing.", sc);
		
		int currCount = jdbcTemplate.queryForInt("SELECT count(*) FROM HISTORY_LOG");
		assertEquals(prevCount+1, currCount);
	}
	
}
