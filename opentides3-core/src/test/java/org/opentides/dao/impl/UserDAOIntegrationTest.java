package org.opentides.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opentides.bean.user.BaseUser;
import org.opentides.bean.user.UserCredential;
import org.opentides.bean.user.UserGroup;
import org.opentides.dao.UserDao;
import org.opentides.dao.UserGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;


/**
 * This unit test checks BaseUserDAO functions.
 * @author allanctan
 *
 */
@TransactionConfiguration(defaultRollback = false)
public class UserDAOIntegrationTest extends BaseDaoTest {

	@Autowired
	private UserDao coreUserDAO;
	
	@Autowired
	private UserGroupDao userGroupDAO;
	
    private UserGroup group1;
	private UserGroup group2;
	private BaseUser user;

    @Before
    public void init() throws Exception {
    	super.init();
    	group1 = userGroupDAO.loadEntityModel(981l);
    	group2 = userGroupDAO.loadEntityModel(982l);
    }
	
    @Ignore
	@Test
	@Transactional
	public void testAddUser() {
		final BaseUser user = new BaseUser();
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				user.addGroup(userGroupDAO.loadEntityModel(981l));
				user.setFirstName("John");
				user.setLastName("Doe");
				user.setEmailAddress("johndoe@unittest.com");
				UserCredential userAccount = new UserCredential();
				userAccount.setPassword("unittest");
				userAccount.setUsername("jdoe");
				userAccount.setUser(user);
				user.setCredential(userAccount);
				
				coreUserDAO.saveEntityModel(user);
				}
			});
				// ensure user is saved and has id
				assertNotNull(user.getId());
				assertEquals( 1, jdbcTemplate.queryForInt("select count(*) from USER_PROFILE where EMAIL='johndoe@unittest.com'"));
				long userId = user.getId();
				
				Map result = jdbcTemplate.queryForMap("select * from USER_PROFILE where ID=?", new Object[]{userId});
				assertEquals("John",result.get("FIRSTNAME"));
				assertEquals("Doe",result.get("LASTNAME"));
				assertEquals("johndoe@unittest.com",result.get("EMAIL"));
		
				Map cred = jdbcTemplate.queryForMap("select * from USERS where USERID=?", new Object[]{userId});
				assertEquals("jdoe",cred.get("USERNAME"));
				assertEquals("unittest",cred.get("PASSWORD"));	
				
				// add user group
				final BaseUser testUser = coreUserDAO.loadEntityModel(user.getId());
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						entityManager.flush();
						Set<UserGroup> groups = testUser.getGroups();
						assertNotNull(groups);
						assertTrue(groups.size() == 1);
						assertTrue(groups.iterator().next().getId().equals(group1.getId()));
						}
				});
				
				// remove user group
				testUser.removeGroup(group1);
				
				coreUserDAO.saveEntityModel(user);
				BaseUser testUser2 = coreUserDAO.loadEntityModel(testUser.getId());
				assertTrue(testUser2.getGroups().size() == 0);
				
				// remove user
				int id = jdbcTemplate.queryForInt("select id from USER_PROFILE where EMAIL='johndoe@unittest.com'");
				coreUserDAO.deleteEntityModel(user.getId());
				assertEquals(0, jdbcTemplate.queryForInt("select count(*) from USER_PROFILE where ID="+user.getId()));
				assertEquals(0, jdbcTemplate.queryForInt("select count(*) from USERS where USERID="+user.getId()));
	}
	
	@Test
	public void testIsRegisteredByEmail() {
	    assertEquals(true, coreUserDAO.isRegisteredByEmail("admin@opentides.com"));
        assertEquals(false, coreUserDAO.isRegisteredByEmail("test@missing.com"));
	}
	
	@Test
    public void testLoadByUsername() {
	    BaseUser user = coreUserDAO.loadByUsername("admin");
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());        
        assertEquals(null, coreUserDAO.loadByUsername("missing"));
		assertNull(coreUserDAO.loadByUsername(""));
		assertNull(coreUserDAO.loadByUsername(null));
        
    }
	
	@Test
    public void testLoadByEmailAddress() {
	    BaseUser user = coreUserDAO.loadByEmailAddress("admin@opentides.com");
	    assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());	    
    }
	
	@Ignore
    @Test
    @Transactional(readOnly = false)
	public void testFindByUsergroupName() {
    
        // a user can be member of multiple user group
        user = new BaseUser();
        user.addGroup(group1);
        user.addGroup(group2);
        user.setEmailAddress("multiple@unittest.com");
        UserCredential userAccount = new UserCredential();
        userAccount.setPassword("test");
        userAccount.setUsername("multiple");
        userAccount.setUser(user);
        userAccount.setEnabled(true);
        user.setCredential(userAccount);
        
        coreUserDAO.saveEntityModel(user);
        
        List<BaseUser> users1 = coreUserDAO.findByUsergroupName("group1");
        assertEquals(1, users1.size());
        assertEquals(user, users1.get(0));
        
        List<BaseUser> users2 = coreUserDAO.findByUsergroupName("group2");
        assertEquals(1, users2.size());
        assertEquals(user, users2.get(0));
    }
	
	@Test
	public void testUniqueUsernameConstraints() {
		try {
			final BaseUser user = new BaseUser();
			user.addGroup(group1);
			user.setFirstName("SuperUser");
			user.setLastName("Duplicate");
			user.setEmailAddress("duplicate@unittest.com");
			UserCredential userAccount = new UserCredential();
			userAccount.setPassword("unittest");
			userAccount.setUsername("admin");
			userAccount.setUser(user);
			user.setCredential(userAccount);
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					coreUserDAO.saveEntityModel(user);
				}
			});
			fail("Failed to throw exception on duplicate username");
		} catch (Exception e) {
			// success
		}
	}
	
	@Test
	public void testUniqueEmailConstraints() {
		try {
			final BaseUser user = new BaseUser();
			user.addGroup(group1);
			user.setFirstName("SuperUser");
			user.setLastName("Duplicate");
			user.setEmailAddress("admin@ideyatech.com");
			UserCredential userAccount = new UserCredential();
			userAccount.setPassword("unittest");
			userAccount.setUsername("admin");
			userAccount.setUser(user);
			user.setCredential(userAccount);
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					coreUserDAO.saveEntityModel(user);
				}
			});
			fail("Failed to throw exception on duplicate email");
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void testCountAll() {
		long cnt = coreUserDAO.countAll();
		int cnt2 = jdbcTemplate.queryForInt("select count(*) from USER_PROFILE");
		assertEquals("Mismatch on count all.", cnt2, (int) cnt);			
	}
	
	@Test
	public void testIsRegistered() {
		assertTrue(coreUserDAO.isRegisteredByEmail("admin@opentides.com"));
		assertFalse(coreUserDAO.isRegisteredByEmail("none@nothing.com"));
		assertFalse(coreUserDAO.isRegisteredByEmail(""));
		assertFalse(coreUserDAO.isRegisteredByEmail(null));
	}

	/**
	 * Setter method for coreUserDAO.
	 *
	 * @param coreUserDAO the coreUserDAO to set
	 */
	public final void setCoreUserDAO(UserDao coreUserDAO) {
		this.coreUserDAO = coreUserDAO;
	}

	/**
	 * Setter method for userGroupDAO.
	 *
	 * @param userGroupDAO the userGroupDAO to set
	 */
	public final void setUserGroupDAO(UserGroupDao userGroupDAO) {
		this.userGroupDAO = userGroupDAO;
	}
}
